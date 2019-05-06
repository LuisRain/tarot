package com.hy.service.order;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.DateUtil;
import com.hy.util.DoubleUtil;
import com.hy.util.LoginUtil;
import com.hy.util.MathUtil;
import com.hy.util.PageData;
import com.hy.util.StringUtil;

@Service("thproductService")
public class ThProductService {
	@Resource(name="daoSupport")
	  private DaoSupport dao;
	
	/**
	 * 退货订单导出
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> excelexport(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("thproductMapper.excelexport", pd);
	}
	 /**查看销售退货列表**/
	  public List<PageData> commodityProduct(Page page) throws Exception{
		    return (List)this.dao.findForList("thproductMapper.productreturnlistPage", page);
	  }
	  /**查看销售退货明细**/
	  public List<PageData> commodityProductItem(PageData pd) throws Exception{
		    return (List)this.dao.findForList("thproductMapper.commodityProductItem", pd);
	  }
	  public PageData sumFinalquantity(PageData pd) throws Exception{
		  return (PageData) dao.findForObject("thproductMapper.sumFinalquantity",pd);
	  }
	  public PageData findOrderList(PageData pd) throws Exception{
		  return (PageData) dao.findForObject("thproductMapper.findOrderList",pd);
	  }
	  
	  /**查看销售退货列表**/
	  public List<PageData> cgOrderlistPage(Page page) throws Exception{
		    return (List)this.dao.findForList("thproductMapper.cgOrderlistPage", page);
	  }
	  /**查看销售退货列表**/
	  public List<PageData> xsOrderlistPage(Page page) throws Exception{
		    return (List)this.dao.findForList("thproductMapper.xsOrderlistPage", page);
	  }
	  public List<PageData> xsenOrderitem(PageData pd) throws Exception{
		  return (List<PageData>) dao.findForList("thproductMapper.xsenOrderitem",pd);
	  }
	  
	  public PageData xsenOrder(PageData pd) throws Exception{
		  return (PageData) dao.findForObject("thproductMapper.xsenOrder",pd);
	  }
	  
	  public List<PageData> cgexOrderitem(PageData pd) throws Exception{
		  return (List<PageData>) dao.findForList("thproductMapper.cgexOrderitem",pd);
	  }
	  
	  public PageData cgexOrder(PageData pd) throws Exception{
		  return (PageData) dao.findForObject("thproductMapper.cgexOrder",pd);
	  }
	  /***采购退货审核***/
	  @Transactional
	  public String cgExamine(String ids){
		  try {
			  for(String id:ids.split(",")){
				  PageData exo=new PageData();
				  exo.put("order_num",id);
				  List<PageData> exItem= (List<PageData>) dao.findForList("EXOrderMapper.findexorderitem", exo);//获取销售退货单明细
				  for(PageData item:exItem){
					  double quan=-Double.valueOf(item.getString("final_quantity"));
					  double final_quantity=Double.valueOf(item.getString("final_quantity"));  //数量
					  List<PageData> invent=(List<PageData>) dao.findForList("ProductinventoryMapper.cgfindproductin", item);//获取入库记录
					  if(null!=invent){
						  for(PageData inv:invent){
							  quan+=Double.valueOf(inv.getString("product_quantity"));
							  double quantity =Double.valueOf(inv.getString("product_quantity"));
							  if(quantity>final_quantity){
								  inv.put("quantity", DoubleUtil.sub(quantity, final_quantity));
								  dao.update("ProductinventoryMapper.updateQuantity", inv);
								  final_quantity=0;
							  }else if(quantity==final_quantity){
								  inv.put("quantity", 0);
								  dao.update("ProductinventoryMapper.updateQuantity", inv);
								  final_quantity=0;
							  }else{
								  inv.put("quantity", 0);
								  dao.update("ProductinventoryMapper.updateQuantity", inv);
								  final_quantity=DoubleUtil.sub(final_quantity, quantity);
							  }
						  }
					  }else{
						  throw new RuntimeException("条码："+item.getString("bar_code")+"库存不足");
					  }
					  //添加库存历史
					  dao.save("ProductinventoryMapper.savehistory", saveInventHistory(item,quan,4,"采购退货后，"));
				  }
				  exo.put("checked_state", 2);
				  dao.update("EXOrderMapper.updateextype", exo);
			  }
		} catch (Exception e) {
			e.printStackTrace();
			 throw new RuntimeException("审核失败");
		}
		  return "审核成功";
	  }
	  /**销售退货审核**/
	  @Transactional
	  public String xsExamine(String ids){
		  try {
			  for(String id:ids.split(",")){
				  PageData eno=new PageData();
				  eno.put("order_num",id);
				  List<PageData> enItem= (List<PageData>) dao.findForList("ENOrderMapper.findenorderitem", eno);//获取销售退货单明细
				  for(PageData item:enItem){
					  if(!item.containsKey("product_time")){
						  throw new RuntimeException("订单："+id+"，无商品生产日期");
					  }
					  if(null==item.getString("product_time")&&item.getString("product_time").equals("")){
						  throw new RuntimeException("订单："+id+"，无商品生产日期");
					  }
					  double final_quantity=Double.valueOf(item.getString("final_quantity"));  //数量
					  List<PageData> invent=(List<PageData>) dao.findForList("ProductinventoryMapper.findproductin", item);//获取入库记录
					  double quantity=0;
					  if(null!=invent){
						  for(PageData inv:invent){
							  quantity+=Double.valueOf(inv.getString("product_quantity"));
							  String product_date=inv.getString("product_date");
							  if(DateUtil.getDaySub(item.getString("product_time"), product_date)==0){  //日期相等  累计
								  inv.put("quantity", DoubleUtil.add(final_quantity, Double.valueOf(inv.getString("product_quantity"))));
								  dao.update("ProductinventoryMapper.updateQuantity", inv);
								  quantity+=final_quantity;
								  final_quantity=0;
							  } 
						  }
					  }
					  if(final_quantity!=0){
						  dao.save("ProductinventoryMapper.save", saveInvent(item));  //添加
					  }
					  //添加库存历史
					  dao.save("ProductinventoryMapper.savehistory",   saveInventHistory(item,quantity,3,"销售退货后，"));
				  }
				  eno.put("checked_state", 2);
				  dao.update("ENOrderMapper.updateentype", eno);
				  dao.update("thproductMapper.updateSelltypetwo", enItem.get(0));
			  }
			  return "审核成功";
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
	 }
	  
	  public PageData saveInventHistory(PageData item,double quantity,int rck_type,String com){
		 
		  PageData inventHistory=new PageData();
		  inventHistory.put("quantity", item.getString("final_quantity"));
		  inventHistory.put("rck_type",rck_type );
		  inventHistory.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
		  inventHistory.put("ck_id",  1);
		  if(quantity==0){
			  inventHistory.put("comment",com+"数量为："+item.getString("final_quantity"));
		  }else{
			  inventHistory.put("comment",com+"数量为："+quantity);
		  }
		  inventHistory.put("product_id", item.getString("product_id"));
		  inventHistory.put("order_num",item.getString("order_num"));
		  inventHistory.put("warehouseId",2);
		  return inventHistory;
	  }
	  public PageData saveInvent(PageData item){
		  PageData invent=new PageData();
		  invent.put("product_id", item.getString("product_id"));
		  invent.put("warehouse_id",2 );
		  invent.put("product_quantity", item.getString("final_quantity"));
		  invent.put("cargo_space_id",  item.getString("cargo_space_id"));
		  invent.put("state",1 );
		  invent.put("ck_id", 1);
		  invent.put("product_date",item.getString("product_time"));
		  return invent;
	  }
	  @Transactional
	  public String reviewedAll(String ids){
		  List<PageData> enorder=new ArrayList<PageData>();
		  List<PageData> enorderitem=new ArrayList<PageData>();
		  List<PageData> exorder=new ArrayList<PageData>();
		  List<PageData> exorderitem=new ArrayList<PageData>();
		  try {
			  for(String id:ids.split(",")){
				  PageData sell=new PageData();
				  sell.put("id", id);
				  PageData sellorder=(PageData) dao.findForObject("thproductMapper.findOrderList", sell);
				  sellorder.put("id","");
				  List<PageData> sellitem=(List<PageData>) dao.findForList("thproductMapper.findOrderListItem", sellorder);
				  String en="0";
				  for(PageData eno:enorder){
					  if(eno.getString("supplier_id").equals(sellorder.getString("merchant_id"))&&eno.getString("order_num").equals(sellorder.getString("order_num"))){
						  en=eno.getString("order_num");
					  }
				  }
				  //添加入库单
				  if(en.equals("0")){
					  String orderNum= "XSTH_"+StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber();
					  enorder.add(saveEnOrder(sellorder,orderNum));
					  en=orderNum;
				  }
				  for(PageData pdd:sellitem){
					  enorderitem.add(saveEnOrderItem(pdd,en));//添加入库条目
					  String orderNum= "CGTH_"+StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber();
					 //循环判断是否存在供应商信息
					  String ex="0";
					  for(PageData exo:exorder){
						  if(exo.getString("merchant_id").equals(pdd.getString("relation_id"))){ //判断是否存在门店信息
							 ex=exo.getString("order_num"); 
						  }
					  }
					  if(ex.equals("0")){
						  exorder.add(saveExOrder(pdd,orderNum));
					  }
					  //循环判断是否已添加该商品信息
					  for(PageData item:exorderitem){
						  if(!ex.equals("0")){
							  if(item.getString("order_num").equals(ex)&&pdd.getString("product_id").equals(item.getString("product_id"))){
								  ex="1";
								  double final_quantity=Double.valueOf(item.getString("final_quantity"));
								  final_quantity=DoubleUtil.add(final_quantity, Double.valueOf(pdd.getString("final_quantity")));
								  item.put("svolume", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_volume")), final_quantity));
								  item.put("weight", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_weight")), final_quantity));
								  item.put("final_quantity",final_quantity);
								  item.put("quantity",final_quantity);
							  }
						  }
					  }
					  if(!ex.equals("1")){
						  if(ex.equals("0")){
							  exorderitem.add(saveExOrderItem(pdd,orderNum));
						  }else if(!ex.equals("0")){
							  exorderitem.add(saveExOrderItem(pdd,ex));
						  }
					  }
				  }
			  }
			  dao.save("ENOrderMapper.saveEnOrderlist", enorder);
			  dao.save("ENOrderMapper.saveEnOrderItemlsit", enorderitem);
			  dao.save("EXOrderMapper.saveExOrderList", exorder);
			  dao.save("EXOrderMapper.saveExOrderItemList", exorderitem);
			  //更新数据
			  dao.update("ENOrderMapper.updateEnOrderAmount", enorder);
			  dao.update("EXOrderMapper.updateExOrderAmount", exorder);
			  PageData se=new PageData();
			  se.put("ids", ids);
			  se.put("checked_state", 3);
			  dao.update("thproductMapper.updateSelltype", se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("审核出错!");
		}
		return "审核成功";
	  }
	
	 
	  public PageData saveExOrder(PageData pdd,String orderNum){
		  PageData exOrder=new PageData();
		  exOrder.put("group_num", pdd.getString("group_num"));
		  exOrder.put("order_num", orderNum);
		  exOrder.put("checked_state", 1);
		  exOrder.put("merchant_id", pdd.getString("relation_id"));
		  exOrder.put("manager_name", pdd.getString("contact_person"));
		  exOrder.put("manager_tel", pdd.getString("contact_person_mobile"));
		  exOrder.put("deliver_address","太原市清徐县赵家堡工业园（联坤仓储物流园）");
		  exOrder.put("comment", pdd.getString("comment"));
		  exOrder.put("final_amount",0);
		  exOrder.put("total_svolume", 0);
		  exOrder.put("total_weight", 0);
		  exOrder.put("paid_amount", 0);
		  exOrder.put("is_ivt_order_print", 0);
		  exOrder.put("is_temporary", 2);
		  exOrder.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
		  exOrder.put("is_order_print", 1);
		  exOrder.put("ivt_state", 1);
		  exOrder.put("amount", 0);
		  exOrder.put("order_type", 2);
		  exOrder.put("ck_id", 1);
		  exOrder.put("type",0);
		  return exOrder;
	  }
	  /****封装入库单数据*/
	  public PageData saveEnOrder(PageData pdd,String orderNum){
		  PageData enOrder=new PageData();
		  enOrder.put("group_num", pdd.getString("group_num"));
		  enOrder.put("order_num",orderNum);
		  enOrder.put("checked_state", 1);
		  enOrder.put("supplier_id", pdd.getString("merchant_id"));
		  enOrder.put("manager_name", pdd.getString("manager_name"));
		  enOrder.put("manager_tel", pdd.getString("manager_tel"));
		  enOrder.put("comment", pdd.getString("comment"));
		  enOrder.put("final_amount", 0);
		  enOrder.put("total_svolume", 0);
		  enOrder.put("total_weight", 0);
		  enOrder.put("is_ivt_order_print", 0);
		  enOrder.put("is_temporary", 2);
		  enOrder.put("is_order_print", 1);
		  enOrder.put("ivt_state",1);
		  enOrder.put("state", 1);
		  enOrder.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
		  enOrder.put("amount", 0);
		  enOrder.put("order_type", 2);
		  enOrder.put("ck_id", 1);
		  return enOrder;
	  }
	  /***封装入库单详情数据***/
	  public PageData saveEnOrderItem(PageData pdd,String orderNum){
		  PageData enorderItem=new PageData();
		  enorderItem.put("group_num", pdd.getString("group_num"));
		  enorderItem.put("order_num", orderNum);
		  enorderItem.put("product_id", pdd.getString("product_id"));
		  enorderItem.put("purchase_price", pdd.getString("product_price"));
		  enorderItem.put("quantity", pdd.getString("quantity"));
		  enorderItem.put("final_quantity", pdd.getString("final_quantity"));
		  enorderItem.put("gift_quantity", pdd.getString("gift_quantity"));
		  enorderItem.put("svolume", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_volume")), Double.valueOf(pdd.getString("final_quantity"))));
		  enorderItem.put("weight", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_weight")), Double.valueOf(pdd.getString("final_quantity"))));
		  enorderItem.put("is_split_ivt", 2);
		  enorderItem.put("is_ivt_BK", 1);
		  enorderItem.put("creator",LoginUtil.getLoginUser().getUSERNAME());
		  enorderItem.put("state", 1);
		  enorderItem.put("product_time", pdd.getString("product_time"));
		  return enorderItem;
	  }
	  public PageData saveExOrderItem(PageData pdd,String orderNum){
		  PageData exOrderItem=new PageData();
		  exOrderItem.put("group_num", pdd.getString("group_num"));
		  exOrderItem.put("order_num", orderNum);
		  exOrderItem.put("product_id", pdd.getString("product_id"));
		  exOrderItem.put("sale_price", pdd.getString("sale_price1"));
		  exOrderItem.put("quantity", pdd.getString("quantity"));
		  exOrderItem.put("final_quantity", pdd.getString("final_quantity"));
		  exOrderItem.put("gift_quantity", pdd.getString("gift_quantity"));
		  exOrderItem.put("svolume", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_volume")), Double.valueOf(pdd.getString("final_quantity"))));
		  exOrderItem.put("weight", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_weight")), Double.valueOf(pdd.getString("final_quantity"))));
		  exOrderItem.put("creator", LoginUtil.getLoginUser().getUSERNAME());
		  exOrderItem.put("comment", pdd.getString("comment"));
		  exOrderItem.put("state",1);
		  exOrderItem.put("is_ivt_BK", 2);
		  exOrderItem.put("product_time", pdd.getString("product_time"));
		  return exOrderItem;
	  }
}
