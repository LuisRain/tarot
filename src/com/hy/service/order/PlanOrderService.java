package com.hy.service.order;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.Const;
import com.hy.util.DoubleUtil;
import com.hy.util.HttpUtil;
import com.hy.util.Logger;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

@Service("planOrderService")
public class PlanOrderService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	protected  Logger logger = Logger.getLogger(this.getClass());
	
	public String stockExOrder(JSONObject pushJson,String url){
		String info ="true";
		JSONObject  obj =null;
		try {
			String data = pushJson.toString();
			logger.error("data="+data);
			data = URLEncoder.encode(data, "UTF-8");
			String jsonData = "jsonData="+data;
			logger.error("--------推送地址："+Const.httpplan);
			String result = HttpUtil.httpPostRequest(Const.httpplan+url,jsonData);
			logger.error("--------推送结束--------");
			obj= JSONObject.parseObject(result);
			if(obj.getString("state").equals("4")){
				info="失败！";
			}else if(obj.getString("state").equals("1")){
				info="true";
			}else{
				PageData pd=new PageData();
				pd.put("plan_order",pushJson.getJSONObject("jsonData").get("plan_order"));
				/*if(pushJson.getJSONObject("jsonData").containsKey("driver_name")){
					pd.put("type",2);
				}else{
					pd.put("type",1);
				}*/
				pd.put("type",1);
				dao.update("PlanOrderMapper.updateplanordertype", pd);
				dao.update("PlanOrderMapper.updateplanitmetype", pd);
			}
		} catch (Exception e) {
			info="计划添加失败！";
			logger.error("--------推送异常--------",e);
		}
		return info;
	}
	
	@Transactional
	public String saveplan(PageData pd){
		String[] orderNum = pd.getString("orderNum").split(",");
		try {
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			dao.save("PlanOrderMapper.saveplanOrder", pd);
			for(String str:orderNum){
				pd.put("order_num",str);
				PageData pdd=(PageData) dao.findForObject("ExWarehouseOrderMapper.findId", pd);
				pd.put("order_num",pdd.getString("order_num"));
				dao.save("PlanOrderMapper.saveplanitem", pd);
			}
			return "true";
		} catch (Exception e) {
			throw new RuntimeException("订单发布失败");
		}
	}
	public List<PageData> returnPlanOrder(PageData page) throws Exception {
		return (List<PageData>) dao.findForList("PlanOrderMapper.returnPlanOrder", page);
	}
	
	public List<PageData> returnexorderitem(PageData page) throws Exception {
		return (List<PageData>) dao.findForList("PlanOrderMapper.returnexorderitem", page);
	}
	
	public List<PageData> findAllplanOrderlistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PlanOrderMapper.findAllplanOrderlistPage", page);
	}
	
	public List<PageData> findAllplanOrderexcel(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PlanOrderMapper.findAllplanOrderexcel", pd);
	}
	
	public List<PageData> findAllplanitem(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PlanOrderMapper.findAllplanitem", pd);
	}
	public List<PageData> findAllplanitemproduct(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PlanOrderMapper.findAllplanitemproduct", pd);
	}
	public List<PageData> findAllplanOrder(PageData pd) throws Exception {
		return  (List<PageData>) dao.findForList("PlanOrderMapper.findAllplanOrder", pd);
	}
	/**查询所有地点**/
	public List<PageData> findkilometre(PageData pd) throws Exception {
		return  (List<PageData>) dao.findForList("PlanOrderMapper.findkilometre", pd);
	}
	/**查询司机信息**/
	public JSONObject findDriver(JSONObject pd) throws Exception {
		JSONObject  obj =new JSONObject();
		String result="";
		try {
			String data = pd.toString();//pushJson.toString();
			logger.error("data="+data);
			data = URLEncoder.encode(data, "UTF-8");
			String jsonData = "jsonData="+data;
			result = HttpUtil.httpPostRequest(Const.httpplan+"interfaces/querydriver",jsonData);
			result = URLDecoder.decode(result, "UTF-8");
			 obj=JSONObject.parseObject(result);
			logger.error("--------推送结束--------");
		} catch (Exception e) {
			logger.error("--------推送异常--------",e);
		}
		return  obj;
	}
	/**查询所有车型**/
	public List<PageData> findModels(PageData pd) throws Exception {
		return  (List<PageData>) dao.findForList("PlanOrderMapper.findModels", pd);
	}
	/**计算金额**/
	public void updataplanAmount(PageData pd){
		try {
			List<PageData> models=findModels(null);
			if(!pd.getString("models_id").equals("")){
				int gg=-1;
				for(PageData car:models){
					if(car.getString("models").equals(pd.getString("models_id"))){
						gg=Integer.valueOf(car.getString("id"));
					}
				}
				if(gg!=-1){
					pd.put("models", gg);
				}else{//添加车辆类型
					dao.save("PlanOrderMapper.saveCarTypeName",pd);
					pd.put("models", pd.getString("id"));
				}
			}
			dao.update("PlanOrderMapper.updateplanordertype", pd);
			pd.put("final_amount",returnDouble(pd));
			dao.update("PlanOrderMapper.updateplanordertype", pd);
			dao.update("PlanOrderMapper.updateplanitmetype", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***计算金额***/
	public double returnDouble(PageData pd) throws Exception{
		PageData planorder=(PageData) dao.findForObject("PlanOrderMapper.findAllplanOrderunit", pd);//查询计划订单关联信息
		List<PageData> planOrderItem=(List<PageData>) dao.findForList("PlanOrderMapper.findAllplanitem", pd);
		double commission=0; //提成
		if(null==pd.get("amount")||	pd.getString("amount").equals("")){
			double kilometre=Double.valueOf(planorder.getString("kilometre"));//公里数
			
			Integer have=2;  //默认挂靠车辆
			if(!planorder.getString("have").equals("")){
				have=Integer.valueOf(planorder.getString("have"));//是否自有车辆 1 是 2 否
				/*pd.put("driver_name", planorder.getString("drivername"));
				pd.put("driver_phone", planorder.getString("telphone"));
				pd.put("driver_platenumber", planorder.getString("platenumber"));*/
			}
			if(have==1){  //自有车辆
				if(planorder.getString("models").equals("中巴")||planorder.getString("models").equals("箱货")||planorder.getString("models").equals("高栏")){
					if(kilometre<=100){
						commission=DoubleUtil.multiply(4.5,kilometre);
					}else if(kilometre<=200){
						commission=DoubleUtil.multiply(4,kilometre);
					}else if(kilometre<=300){
						commission=DoubleUtil.multiply(3.5,kilometre);
					}else if(kilometre>300){
						commission=DoubleUtil.multiply(3,kilometre);
					}
					if(planOrderItem.size()>6){
						commission+=(planOrderItem.size()-6)*20;//卸车费：150元/车。
					}
					commission+=150;
				}else{
					commission=90;
				}	
			}else{//挂靠
				if(planorder.getString("models").equals("中巴")||planorder.getString("models").equals("箱货")||planorder.getString("models").equals("高栏")){
					commission=150+DoubleUtil.multiply(5,kilometre);
					if(planOrderItem.size()>6){
						commission+=(planOrderItem.size()-6)*20;
					}
				}else if(planorder.getString("models").equals("金杯")){
					commission=150+(planOrderItem.size()-1)*10;
				}else{
					commission=100+(planOrderItem.size()-1)*10;
				}
			}
		}else{
			commission=Double.valueOf(pd.getString("amount"));
		}
		return commission;
	}
	public void updataplan(PageData pd){
		try {
			dao.update("PlanOrderMapper.updateplanordertype", pd);
			dao.update("PlanOrderMapper.updateplanitmetype", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***删除订单信息***/
	@Transactional
	public String deleteplanitemOrderNum(PageData pd){
		try {
			if(!pd.getString("type").equals("0")){
				JSONObject json=new JSONObject();
				json.put("jsonData", pd);
				String str=stockExOrder(json,"interfaces/deleteorderinfo");
				if(!str.equals("true")){
					throw new RuntimeException("删除订单失败");
				}
			}
			dao.delete("PlanOrderMapper.deleteplanitemOrderNum", pd);
			List<PageData> planOrderItem=findAllplanitem(pd);
			if(planOrderItem.size()>0){
				dao.update("PlanOrderMapper.updateWVN", pd);
			}else{
				dao.update("PlanOrderMapper.updateItemWVN", pd);
			}
		} catch (Exception e) {
			throw new RuntimeException("删除订单失败");
		}
		return "true";
	}
	/***去掉司机信息:订单状态为0 可以删除司机信息***/
	@Transactional
	public void updateDriverId(PageData pd){
		try {
			dao.delete("PlanOrderMapper.updateDriverId", pd);
			pd.put("final_amount",returnDouble(pd));
			dao.update("PlanOrderMapper.updateplanordertype", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Transactional
	public String saveDriver(JSONObject obj){
		try {
			PageData plan=new PageData();
			plan.put("driver_name", obj.getString("REALNAME"));
			plan.put("license_number", obj.getString("CAR_NUMBER"));
			plan.put("phone", obj.getString("phone"));
			plan.put("plan_order", obj.getString("ordernumber"));
			dao.update("PlanOrderMapper.updateplan", plan);
			return "true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("添加司机信息失败");
		}
		
	}
	@Transactional
	public String deletePlan(PageData pds) throws Exception {
		String result="";
		try {
			PageData pla=(PageData) dao.findForObject("PlanOrderMapper.findAllplanOrderModels", pds);
			if(pla.getString("type").equals("0")){
				dao.update("PlanOrderMapper.deleteplan", pla);
				dao.update("PlanOrderMapper.deleteplanitem", pla);
				result="计划删除成功！";
			}else{
				JSONObject obj=new JSONObject();
				obj.put("plan_order",pla.getString("plan_order"));
				String data = obj.toString();//pushJson.toString();
				logger.error("data="+data);
				data = URLEncoder.encode(data, "UTF-8");
				String jsonData = "jsonData="+data;
				result = HttpUtil.httpPostRequest(Const.httpplan+"interfaces/queryorderstate",jsonData);
				result = URLDecoder.decode(result, "UTF-8");
				obj=JSONObject.parseObject(result);
				if(obj.getString("state").equals("1")){
					PageData pp=new PageData();
					pp.put("type", 0);
					pp.put("plan_order", pla.getString("plan_order"));
					dao.update("PlanOrderMapper.updateplanordertype", pp);
					dao.delete("PlanOrderMapper.updateDriverId", pp);
					result="删除货运平台订单信息成功！";
				}else{
					result="司机已接单！";
				}
			}
			logger.error("--------推送结束--------");
		} catch (Exception e) {
			logger.error("--------推送异常--------",e);
			throw new RuntimeException("计划删除失败");
		}
		return  result;
	}
	/*public String deletePlan(PageData pd){
		try {
			dao.update("PlanOrderMapper.deleteplan", pd);
			dao.update("PlanOrderMapper.deleteplanitem", pd);
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("删除计划任务失败");
		}
		
	}*/
	
}
