package com.hy.controller.order;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.order.PlanOrderService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.AppUtil;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
@Controller
@RequestMapping(value = "/planOrder")
public class PlanOrderController extends BaseController{
	
	@Resource(name = "sysLogService")
	private SysLogService sysLogService;
	
	@Resource
	private PlanOrderService planOrderService;
	
	
	/****
	 * 返回门店出库单订单信息
	 * @return
	 */
	@RequestMapping("returnexorderitem")
	@ResponseBody
	public String returnexOrder() {
		String result = "true";
		String operationMsg = "返回出库单明细";
		JSONObject obj=new JSONObject();
		try {
			PageData pd =  this.getPageData();
			obj=JSONObject.parseObject(pd.getString("jsonData"));
			pd.put("order_num", obj.getString("order_num"));
			List<PageData> plan=planOrderService.returnexorderitem(pd);
			obj.put("plan",plan);
			String data = URLEncoder.encode(obj.toString(), "UTF-8");
			String jsonData = "jsonData="+data;
			return jsonData;
		} catch (Exception e) {
			result=e.toString();
			logger.error("------------------------回传数据出错=="+result);
		} 
		return result;
	}
	
	
	
	
	/**
	 * 打印
	 * @param page
	 * @param orderId
	 * @param type
	 * @return
	 */
	@RequestMapping("planPrint")
	public ModelAndView planPrint(Page page, String orderId, String type) {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "展示计划单打印页面";
		logBefore(logger, operationMsg);
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData plan=(PageData) planOrderService.findAllplanOrder(pd).get(0);
			List<PageData> plist=planOrderService.findAllplanitemproduct(pd);
			List<PageData> planOrderItem=planOrderService.findAllplanitem(pd);
			mv.setViewName("inventorymanagement/plan/plan_edit");
			mv.addObject("planlist", plist);
			mv.addObject("planOrderItem", planOrderItem);
			mv.addObject("planorder", plan);
			mv.setViewName("inventorymanagement/plan/plan_print");
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}

	
	/**修改计划并计算计划单号预计金额**/
	@RequestMapping(value = "updateplan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateplan() {
		String result = "true";
		try {
			PageData pd = this.getPageData();
			planOrderService.updataplanAmount(pd);
			result="true";
			sysLogService.saveLog("计划修改成功", "成功");
		} catch (Exception e) {
			result="false";
		} 
		return result;
	}
	/**
	 *发布计划
	 */
	@RequestMapping(value = "releasePlan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String releasePlan() {
		String result = "true";
		String operationMsg = "发布计划";
		logBefore(logger, operationMsg);
		PageData pd = this.getPageData();
		try {
			PageData planOrder=(PageData) planOrderService.findAllplanOrder(pd).get(0);
			List<PageData> planOrderItem=planOrderService.findAllplanitem(pd);
			String address="";
			String merchant="";
			String order_num="";
			String mobile="";
			String contact_person="";
			String merchant_id="";
			String groupNum="";
			String merchant_num="";
			String type = "";
			for(PageData item:planOrderItem){
				if(order_num.equals("")){
					address=item.getString("address");
					merchant=item.getString("merchant");
					order_num=item.getString("order_num");
					groupNum = item.getString("group_num");
					type = item.getString("order_type");
					mobile=item.getString("mobile");
					contact_person=item.getString("contact_person");
					merchant_id=item.getString("merchant_id");
					merchant_num=item.getString("merchant_num");
				}else{
					address=address+","+item.getString("address");
					merchant=merchant+","+item.getString("merchant");
					order_num=order_num+","+item.getString("order_num");
					groupNum = groupNum + "," + item.getString("group_num");
					type = type + "," + item.getString("order_type");
					mobile=mobile+","+item.getString("mobile");
					contact_person=contact_person+","+item.getString("contact_person");
					merchant_id=merchant_id+","+item.getString("merchant_id");
					merchant_num=merchant_num+","+item.getString("merchant_num");
				}
			}
			planOrder.put("address",address);
			planOrder.put("order_num",order_num);
			planOrder.put("groupNum",groupNum);

			planOrder.put("type",type);
			planOrder.put("mobile",mobile);
			planOrder.put("contact_person",contact_person);
			planOrder.put("merchant_id",merchant_id);
			planOrder.put("merchant",merchant);
			planOrder.put("merchant_num",merchant_num);
			planOrder.put("is_type", "2");//标识 供应链1  石化库2
			//JSONObject json=JSONObject.parseObject(planOrder.toString());
			JSONObject json=new JSONObject();
			json.put("jsonData", planOrder);
			logger.error("--------开始发布计划，计划单号："+planOrder.getString("plan_order")+"--------");
			result=planOrderService.stockExOrder(json,"interfaces/addorderinfo");
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} 
		return result;
	}
	
	/****
	 * 返回门店订单信息
	 * @return
	 */
	@RequestMapping("returnPlanOrder")
	@ResponseBody
	public String returnPlanOrder() {
		String result = "true";
		String operationMsg = "返回门店订单信息";
		JSONObject obj=new JSONObject();
		try {
			PageData pd =  this.getPageData();
			obj=JSONObject.parseObject(pd.getString("jsonData"));
			pd.put("merchant_id", obj.getString("merchant_id"));
			List<PageData> plan=planOrderService.returnPlanOrder(pd);
			obj.put("plan",plan);
			String data = URLEncoder.encode(obj.toString(), "UTF-8");
			String jsonData = "jsonData="+data;
			return jsonData;
		} catch (Exception e) {
			result=e.toString();
			logger.error("------------------------回传数据出错=="+result);
		} 
		return result;
	}
	/**
	 *添加计划
	 */
	@RequestMapping(value = "saveplan", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveplan(Page page) {
		String result = "true";
		String operationMsg = "添加计划";
		logBefore(logger, operationMsg);
		try {
			PageData pd = this.getPageData();
			String formatter = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+StringUtil.getFixLenthString(6);
			pd.put("plan_group","GP_"+formatter);
			pd.put("plan_order","PL_"+formatter);
			String str=planOrderService.saveplan(pd);
			if(str.equals("true")){
				sysLogService.saveLog(operationMsg+":"+pd.getString("plan_order"), "成功");
			}
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} 
		return result;
	}
	
	/**
	 *导出
	 */
	@RequestMapping("findAllplanOrderexcel")
	public ModelAndView findAllplanOrderexcel() {
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="ProductExcel导出库存操作";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			pd.put("ck_id",LoginUtil.getLoginUser().getCkId());
			Map<String,Object> map=new HashMap<String, Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("计划单号"); // 2
			titles.add("始发地"); // 2
			titles.add("目的地"); // 5
			titles.add("司机名称"); // 2
			titles.add("司机电话"); // 3
			titles.add("车牌号"); // 3
			titles.add("总件数"); // 3
			titles.add("出库单号"); // 3
			titles.add("出库单总金额"); // 6
			titles.add("出库单重量"); // 7
			titles.add("出库单体积"); // 9
			titles.add("门店名称"); // 8
			titles.add("门店简称"); // 8\
			titles.add("车型"); // 8
			titles.add("备注"); // 8
			map.put("titles", titles);
			List<PageData> listpd=planOrderService.findAllplanOrderexcel(pd);
			List<PageData> varList=new ArrayList<PageData>();
			for (int i = 0; i < listpd.size(); i++) {
				PageData pdd=new PageData();
				pdd.put("var1", listpd.get(i).getString("plan_order"));
				pdd.put("var2", listpd.get(i).getString("originating"));
				pdd.put("var3", listpd.get(i).getString("sitename"));
				pdd.put("var4", listpd.get(i).getString("driver_name"));
				pdd.put("var5", listpd.get(i).getString("driver_phone"));
				pdd.put("var6", listpd.get(i).getString("driver_platenumber"));
				pdd.put("var7", listpd.get(i).getString("total_number"));
				pdd.put("var8", listpd.get(i).getString("order_num"));
				pdd.put("var9", listpd.get(i).getString("final_amount"));
				pdd.put("var10", listpd.get(i).getString("total_weight"));
				pdd.put("var11", listpd.get(i).getString("total_svolume"));
				pdd.put("var12", listpd.get(i).getString("merchant_name"));
				pdd.put("var13", listpd.get(i).getString("short_name"));
				pdd.put("var14", listpd.get(i).getString("models"));
				pdd.put("var15", listpd.get(i).getString("comment"));
				varList.add(pdd);
			}
			map.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv=new ModelAndView(erv,map);
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 查询计划列表
	 */
	@RequestMapping("findplanOrderlistPage")
	public ModelAndView findplanOrderlistPage(Page page, String type) {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "分页查询临时入库单";
		logBefore(logger, operationMsg);
		try {
			PageData pd = new PageData();
			pd = this.getPageData();
			String searchcriteria = pd.getString("searchcriteria"); // 搜索条件
			String keyword = pd.getString("keyword");// 搜索关键字
			String lastLoginStart = pd.getString("lastLoginStart");// 搜索关键字
			if (null != keyword && !"".equals(keyword)) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			/*if (null != lastLoginStart && !"".equals(lastLoginStart)) {
				pd.put("lastLoginStart", lastLoginStart.split("~")[0]);
				pd.put("lastLoginEnd", lastLoginStart.split("~")[1]);
			}*/
				//pd.put("type", pd.getString("types"));
			pd.put("ck_id",LoginUtil.getLoginUser().getCkId());
			page.setPd(pd);
			List<PageData> varList = planOrderService.findAllplanOrderlistPage(page); // 列出EnWarehouseOrder列表
		/*	pd.put("lastLoginStart", lastLoginStart);*/
			mv.setViewName("inventorymanagement/plan/plan_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			//sysLogService.saveLog(operationMsg, "成功", 2);
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 计划详情页
	 */
	@RequestMapping(value = "/planEdit")
	public ModelAndView planEdit() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String operationMsg = "去计划详情页";
		logBefore(logger, operationMsg);
		try {
			PageData plan=(PageData) planOrderService.findAllplanOrder(pd).get(0);
			List<PageData> plist=planOrderService.findAllplanitemproduct(pd);
			List<PageData> planOrderItem=planOrderService.findAllplanitem(pd);
			List<PageData> kilometre=planOrderService.findkilometre(pd);
			List<PageData> models=planOrderService.findModels(pd);
			mv.setViewName("inventorymanagement/plan/plan_edit");
			mv.addObject("planlist", plist);
			mv.addObject("planOrderItem", planOrderItem);
			mv.addObject("planorder", plan);
			mv.addObject("models", models);
			mv.addObject("kilometre", kilometre);
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}

	/**
	 *添加司机信息
	 */
	@RequestMapping("saveDriver")
	@ResponseBody
	public String saveDriver() {
		String result = "true";
		String operationMsg = "添加计划";
		logBefore(logger, operationMsg);
		JSONObject returnObj = new JSONObject();
		try {
			PageData pd =  this.getPageData();
			returnObj=JSONObject.parseObject(pd.getString("jsonData"));
			planOrderService.saveDriver(returnObj);
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} 
		return result;
	}
	/**
	 *修改计划订单状态
	 */
	@RequestMapping("updateplantype")
	@ResponseBody
	public String updateplantype() {
		String result = "true";
		String operationMsg = "添加计划";
		logBefore(logger, operationMsg);
		JSONObject obj=new JSONObject();
		try {
			PageData pd =  this.getPageData();
			obj=JSONObject.parseObject(pd.getString("jsonData"));
			pd.put("type",obj.getString("type"));
			pd.put("plan_order",obj.getString("plan_order"));
			planOrderService.updataplan(pd);
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} 
		return result;
	}
	/****
	 * 当服务器重新开机时接受数据
	 * @return
	 */
	@RequestMapping("openDnPlan")
	@ResponseBody
	public String openDnPlan() {
		String result = "true";
		String operationMsg = "当服务器重新开机时接受数据";
		JSONObject obj=new JSONObject();
		try {
			logger.error("------------------------"+operationMsg);
			PageData pd =  this.getPageData();
			obj=JSONObject.parseObject(pd.getString("jsonData"));
			logger.error("------------------------接受数据=="+obj);
			pd.put("plan_order", obj.getString("ordernumber"));
			pd.put("type", obj.getString("type"));
			if(obj.containsKey("realname")){
				pd.put("driver_name", obj.getString("realname"));
				pd.put("driver_phone", obj.getString("phone"));
				pd.put("driver_platenumber", obj.getString("carNumber"));
			}
			planOrderService.updataplan(pd);
		} catch (Exception e) {
			result=e.toString();
			logger.error("------------------------回传数据出错=="+result);
		} 
		return result;
	}
	
	/**
	 *删除订单信息
	 */
	@RequestMapping("updateplanOrderNum")
	@ResponseBody
	public String updateplanOrderNum() {
		String result = "true";
		String operationMsg = "";
		logBefore(logger, operationMsg);
		try {
			PageData pd =  this.getPageData();
			if(pd.containsKey("order_num")){
				operationMsg = "删除订单信息："+pd.getString("order_num");
				result=planOrderService.deleteplanitemOrderNum(pd);
			}else{
				operationMsg = "删除计划单司机信息";
				planOrderService.updateDriverId(pd);
			}
			sysLogService.saveLog(operationMsg+":"+pd.getString("plan_order"), "成功");
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} 
		return result;
	}
	
	/**
	 *撤销计划
	 */
	@RequestMapping(value = "deleteplan", produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String deleteplan() {
		String result = "true";
		String operationMsg = "撤销计划";
		logBefore(logger, operationMsg);
		try {
			PageData pd = this.getPageData();
			String str=planOrderService.deletePlan(pd);
			result=str;
			sysLogService.saveLog(operationMsg+":"+pd.getString("plan_order"), "成功");
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} 
		return result;
	}
	/**查询司机信息****/
	@RequestMapping(value = "/selectDriver" ,produces = "application/josn;charset=UTF-8")
	@ResponseBody
	public String selectDriver() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		String operationMsg = "商品条件查询搜索";
		logBefore(logger, operationMsg);
		JSONObject obj=new JSONObject();
		JSONArray ja=new JSONArray();
		try {
			pd = this.getPageData();
			if(pd.getString("driver_name")!=null&&!pd.getString("driver_name").equals("")){
				obj.put("driver_name", pd.getString("driver_name"));
			}
			JSONObject obc= planOrderService.findDriver(obj);
			ja=obc.getJSONArray("sj");
			/*obj.put("list", ja);
			//System.out.println(obj);*/
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return ja.toString();
	}
	
}
