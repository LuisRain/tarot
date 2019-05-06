package com.hy.service.WarehousingInterface;

import com.google.gson.Gson;
import com.hy.dao.DaoSupport;
import com.hy.util.PageData;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫码入库接口(service)
 * @author gyy
 *
 */
@Service("warehousingservice")
public class WarehousingService {
	
	 @Resource(name="daoSupport")
	 private DaoSupport dao;
	 
	 /**
	  * 通过供应商编码查询供应商信息
	  * @param supperliernum
	  * @return
	  */
	 public String findsupplierinfo(String supperliernum){
		 String json = "";
		 PageData pd = null;
		 if(supperliernum != null && !supperliernum.equals("")){
			 try {
				pd = (PageData) dao.findForObject("warehousing.findsupplierinfo", supperliernum);
			} catch (Exception e) {
				e.printStackTrace();
				json = resultmsg("0");
				return json;
			}
		 }
		 if(pd != null ){
			 pd.put("msg", "1");
			 JSONObject jsonObject = JSONObject.fromObject(pd); 
			 json = jsonObject.toString();
		 }else{
			 json = resultmsg("0");
		 }
		 return json;
	 }
	 /**
	  * 无数据或者错误时返回的json字符串
	  * @return
	  */
	 public String resultmsg(String msg){
		 String json = null;
		 Map<String,String> map = new HashMap<String,String>();
		 map.put("msg", msg);
		 JSONObject  tojson = JSONObject.fromObject(map);
		 json = tojson.toString();
		 return json;
	 }
	 /**
	  * 扫码入库
	  * @param supperliernum
	  * @return 
	  */
	 @Transactional
	 public String warehousing(String supperliernum,List liststr) throws Exception{
		 String json = "false";
		 //根据供货商编码查询订单
		 PageData pd = null;
		 /*if(supperliernum != null && !supperliernum.equals("")){*/
			 try {
				 pd = (PageData) dao.findForObject("warehousing.findsupplierinfo", supperliernum.trim());
				 String order_num = "";
				 String group_num = "";
				 if(pd != null){
					  order_num = pd.getString("order_num");  //订单编码
					  group_num = pd.getString("group_num");	//批次码
				 }else{
					 json =  resultmsg("0");
					 return json;
				 }
				 Gson gson = new Gson();	// String转map
				 if(liststr.size() > 0){
						for (int i = 0; i < liststr.size(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
						//	map = gson.fromJson((String) liststr.get(i), map.getClass());
							map = (Map<String, Object>) liststr.get(i);
							String barcode = (String) map.get("barcode");	//商品条形码
							String number = (String) map.get("number");		//数量
							String prodate = (String) map.get("date");	//商品日期
							String zone = (String) map.get("zone");	//货位区号
							String storey = (String) map.get("storey");	//货位排号
							String storey_num = (String) map.get("storey_num");	//货位号
							
							//根据货位号查询，如果有则返回id 并更新商品表中的货位id
							if(zone != null && !zone.equals("") && storey != null && !storey.equals("") && storey_num != null && !storey_num.equals("")){
								PageData  pa = new PageData();
								PageData  pa2 = new PageData();
								pa.put("zone", zone);
								pa.put("storey", storey);
								pa.put("storey_num", storey_num);
								PageData stor = (PageData) dao.findForObject("warehousing.findtp_cargo_space", pa);
								String id = "";
								if(stor != null){
									id = stor.getString("id");  //获取货位id
								}else{
									//如果没有则新增  并返回新增的id 更新商品表中的货位id
									 dao.save("warehousing.inserttp_cargo_space", pa);
									 PageData strid = (PageData) dao.findForObject("warehousing.findtp_cargo_space", pa);
									 id = strid.getString("id");
								}
								pa2.put("id", id);
								pa2.put("barcode", barcode);
								dao.update("warehousing.updatet_product", pa2);
							}
							PageData pgd = new PageData();
							PageData pda = new PageData();
							pda.put("barcode", barcode);
							pda.put("prodate", prodate);
							//根据条形码查询保质期
						//	PageData  expire_days= (PageData) dao.findForObject("warehousing.findexpire_days", barcode);
							//根据条形码、商品日期查询订单详情中是否有该商品  有则更新数量 否则 添加新的条目
							pgd = (PageData) dao.findForObject("warehousing.findpro", pda);
							if(pgd != null){
								pda.put("number", number);
								pda.put("pid", pgd.get("id"));
								dao.update("warehousing.updateitem", pda);
								json = resultmsg("1");
							}else{
								//根据条形码查询商品id、价格、等信息
								PageData pgdata = (PageData) dao.findForObject("warehousing.findproinfo", barcode);
								String price = pgdata.getString("product_price");
								String product_id = pgdata.getString("pid");
								pda.put("purchase_price", price);
								pda.put("order_num", order_num);
								pda.put("group_num", group_num);
								pda.put("product_id", product_id);
								pda.put("quantity", number);
								pda.put("final_quantity", number);
								pda.put("product_time", prodate);
								dao.save("warehousing.insertwarehousing", pda);
								json = resultmsg("1");
							}
						}
				 }
				 json = resultmsg("1");
			} catch (Exception e) {
				e.printStackTrace();
				json =  resultmsg("0");
			}
		/* }*/
		 return json;
	 }
	 
	 /**
	  * 根据条形码查询商品信息
	  * @param barcode
	  * @return
	  */
	 public String findproduct(String barcode){
		 String json = "";
		 PageData pd = null;
		 if(barcode != null && !barcode.equals("")){
			 try {
				pd = (PageData)  dao.findForObject("warehousing.findexpire_days", barcode);
			} catch (Exception e) {
				e.printStackTrace();
				json = resultmsg("0");
				return json;
			}
		 }
		 if(pd != null ){
			 pd.put("msg", "1");
			 JSONObject jsonObject = JSONObject.fromObject(pd); 
			 json = jsonObject.toString();
		 }else{
			 json = resultmsg("0");
		 }
		 return json;
	 }
	
}
