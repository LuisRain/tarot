package com.hy.service.order;

import com.alibaba.fastjson.JSONObject;
import com.hy.dao.DaoSupport;
import com.hy.entity.BuyGood;
import com.hy.entity.Page;
import com.hy.entity.product.Product;
import com.hy.mapper.AuditOrderMapper;
import com.hy.mapper.AutoAssignMapper;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.product.ProductService;
import com.hy.threadpool.threadimpl.AutoAssignThread;
import com.hy.threadpool.ResultDto;
import com.hy.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service("exwarehouseorderService")
public class ExWarehouseOrderService {
	@Resource(name = "productService")
	private ProductService productService;
	@Resource
	private EXOrderItemService eXOrderItemService;
	@Resource(name = "productinventoryService")
	private ProductinventoryService productinventoryService;
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private AuditOrderMapper auditOrderMapper;
	@Resource
	private AutoAssignMapper autoAssignMapper;
	protected Logger logger = Logger.getLogger(getClass());
	/*public static void main(String[] args) throws ParseException {
		Calendar cal = Calendar.getInstance();  
        cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-07-1"));  
        int d = 0;  
        if(cal.get(Calendar.DAY_OF_WEEK)==1){  
            d = -6;  
        }else{  
            d = 2-cal.get(Calendar.DAY_OF_WEEK);  
        }  
        cal.add(Calendar.DAY_OF_WEEK, d);  
        //所在周开始日期  
        ////System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        int month=cal.get(Calendar.MONTH)+1;  //当前月
        int weekOfMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);   //当前月的第几个星期天
        //System.out.println(month+"月"+weekOfMonth);
	}*/
	
	public void qrxg(PageData pd) throws Exception{
		dao.update("jssl.qrxg",pd);
	}
	
	public List<PageData> Exconfirmed(PageData pd) throws Exception{
		List<PageData> grou=(List<PageData>) dao.findForList("jssl.findexgroupnum", null);
		pd.put("group_num",grou.get(0).getString("group_num"));  //获取最新的批次号
		return (List<PageData>) dao.findForList("jssl.Exconfirmed",pd);
	}
	
	/**
	 * 自动分配
	 * @author wps
	 * @return
	 */
	@Transactional
	public String jsxg(PageData pd){
		String result="成功！";
		logger.error("-------开始计算修改出库单数量，批次号："+pd.getString("group_num")+",开始时间："+(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:SS")).format(new Date()));
		//判断 销售单、采购、入库是否有未审核订单
		pd.put("checked_state", 1);
		try {
			List<PageData> grou=(List<PageData>) dao.findForList("jssl.findexgroupnum", null);
			if(grou.size()>1){
				 throw new RuntimeException("有多个批次未审核的出库单!");
			}
			if(grou.size()==0){
				 throw new RuntimeException("没有未审核的出库单!");
			}
			String groupNum = grou.get(0).getString("group_num");
			pd.put("group_num",groupNum);  //获取最新的批次号
			
			/*List<PageData> selling=(List<PageData>) dao.findForList("jssl.selectsellingOrder", pd);
			if(selling.size()>0){
				 throw new RuntimeException("该批次号下有未审核的销售订单!");
			}*/
			/*List<PageData> purorder=(List<PageData>) dao.findForList("jssl.selectpurorder", pd);
			if(purorder.size()>0){
				throw new RuntimeException("该批次号下有未审核的采购订单!");
			}*/
			List<PageData> enorder=(List<PageData>) dao.findForList("jssl.selectenorder", pd);
			if(enorder.size()>0){
				throw new RuntimeException("该批次号下有未审核的入库订单!");
			}
		/*	List<PageData> zp=(List<PageData>) dao.findForList("jssl.findexzporder", null);
			if(zp.size()>0){
				 throw new RuntimeException("有未审核的直配出库单!");
			}*/
			/*List<PageData> exorder=(List<PageData>) dao.findForList("jssl.selectexorder", pd);
			if(exorder.size()>0){
				throw new RuntimeException("该批次      订单!");
			}*/
			//根据批次号自动分配订单
			result = autoAssignOrder(groupNum);
			//修改相关信息
			auditOrderMapper.updateExOrderItemInfo(groupNum);
			auditOrderMapper.updateExOrderInfo(groupNum);
			/*//获取供应商到货数量
			//List<PageData> enitem=(List<PageData>) dao.findForList("jssl.findenitemquantity", pd);
			
			List<PageData> active=(List<PageData>) dao.findForList("jssl.findproductactivity", null);  //获取活动列表
			pd.put("groupNum", pd.getString("group_num").substring(3));
			List<PageData> zpitem=(List<PageData>) dao.findForList("jssl.findexzporderitem", pd);  //直配订单商品数量
			
			List<PageData> inventory=(List<PageData>) dao.findForList("jssl.findproductinventory", null);
			
			List li=new ArrayList();//用于保存赠品id
			//库存信息+供应商到货数量
*//*			for(PageData inv:inventory){
				int ii=0;
				if(inv.getString("product_id").equals("96637")){
					//System.out.println("........");
				}
				for(int i=0;i<enitem.size();i++){
					if(Double.valueOf(enitem.get(i).getString("final_quantity"))!=0){
						if(inv.getString("product_id").equals(enitem.get(i).getString("product_id"))){
							ii=1;
							double enqu=Double.valueOf(enitem.get(i).getString("final_quantity"));
							enitem.get(i).put("final_quantity",  Double.valueOf(inv.getString("quantity"))));  //更新入库的总数量
						}
					}
					
				}
				if(ii==0){
					PageData pgd=new PageData();
					pgd.put("quantity",inv.getString("quantity"));
					pgd.put("final_quantity",inv.getString("quantity"));
					pgd.put("product_id",inv.getString("product_id"));
					pgd.put("quantity_dhl",1);
					pgd.put("gift_quantity",0);
					enitem.add(pgd);
				}
			}*//*
			//循环减去直配订单商品数量
			for(PageData zp:zpitem){
				for(int i=0;i<inventory.size();i++){
					*//*if(zp.getString("product_id").equals("96637")){
						//System.out.println("........");
					}*//*
					if(zp.getString("product_id").equals(inventory.get(i).getString("product_id"))){
						double enqu=Double.valueOf(inventory.get(i).getString("quantity"));
						if(enqu> Double.valueOf(zp.getString("final_quantity"))){
							inventory.get(i).put("quantity", DoubleUtil.sub(enqu, Double.valueOf(zp.getString("final_quantity"))));  //更新入库的总数量
						}else{
							inventory.get(i).put("quantity", 0);  //更新入库的总数量
						}
						if(Double.valueOf(inventory.get(i).getString("quantity"))> Double.valueOf(zp.getString("gift_quantity"))){
							inventory.get(i).put("quantity", DoubleUtil.sub(Double.valueOf(inventory.get(i).getString("quantity")), Double.valueOf(zp.getString("gift_quantity"))));  //更新入库的总数量
						}else{
							inventory.get(i).put("quantity", 0);  //更新入库的总数量
						}
					}
				}
			}
			
			if(active!=null){
				for(PageData act:active){
					if(act.getString("product_id").equals("95556")){
						//System.out.println("........");
						//System.out.println(act.getString("product_id"));
					}
					act.put("group_num",pd.getString("group_num"));
					List<PageData> exitem=(List<PageData>) dao.findForList("jssl.findexitem", act);  //获取做活动的商品信息  倒序
					if(null!=exitem){
						String order_num="";
						for(PageData ex:exitem){
							*//********************************************//*
							
							int kk=0;
							double final_quantity=Double.valueOf(ex.getString("final_quantity")); //实际出库数量
							for(int i=0;i<inventory.size();i++){
								if(ex.getString("product_id").equals(inventory.get(i).getString("product_id"))){
									kk=1;
									double enqu=Double.valueOf(inventory.get(i).getString("quantity"));
									if(enqu!=0){
										if(enqu>final_quantity){
											inventory.get(i).put("quantity", DoubleUtil.sub(enqu, Double.valueOf(ex.getString("final_quantity"))));  //更新入库的总数量
										}else if(enqu<=final_quantity){
											ex.put("final_quantity",enqu);
											dao.update("jssl.updateexquantity", ex); //更新订单出库数量
											ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
											dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
											inventory.get(i).put("quantity", 0);  //更新入库的总数量
										}
									}else{
										ex.put("final_quantity",0);
										dao.update("jssl.updateexquantity", ex); //更新订单出库数量
										ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
										dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
									}
								}
							}
							if(kk==0){
								ex.put("final_quantity",0);
								dao.update("jssl.updateexquantity", ex); //更新订单出库数量
								ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
								dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
							}
							*//********************************************//*
							
							act.put("order_num", ex.getString("order_num"));
							if(li.indexOf(act.getString("product_activity")+ex.getString("order_num"))==-1){
								li.add(act.getString("product_activity")+ex.getString("order_num"));
								PageData gift=(PageData) dao.findForObject("jssl.findexitemproduct", act);  //获取赠品数据
								if(null!=gift){
									*//*if(gift.getString("product_id").equals("96127")){
										//System.out.println("........");
										//System.out.println(ex.getString("product_id"));
									}*//*
									int gg=0;
									double gift_quantity=Double.valueOf(gift.getString("gift_quantity")); //赠品数量
									for(int i=0;i<inventory.size();i++){
										if(gift.getString("product_id").equals(inventory.get(i).getString("product_id"))){
											order_num=gift.getString("order_num");
											gg=1;
											double enqu=Double.valueOf(inventory.get(i).getString("quantity"));
											if(enqu!=0){
												if(enqu<=gift_quantity){
													gift.put("gift_quantity",enqu);
													dao.update("jssl.updategift", gift); //更新订单赠品数量
													gift.put("orderNum","SO"+gift.getString("order_num").substring(2));
													dao.update("jssl.updatesellgift", gift); //更新订单出库数量
													inventory.get(i).put("quantity", 0);  //更新入库的总数量
													
												}else if(enqu>gift_quantity){
													inventory.get(i).put("quantity", DoubleUtil.sub(enqu, gift_quantity));  //更新入库的总数量
													//System.out.println(inventory.get(i).getString("quantity"));
												}
											}else{
												gift.put("gift_quantity",0);
												dao.update("jssl.updategift", gift); //更新订单赠品数量
												gift.put("orderNum","SO"+gift.getString("order_num").substring(2));
												dao.update("jssl.updatesellgift", gift); //更新订单出库数量
											}
										}
									}
									if(gg==0){
										gift.put("gift_quantity",0);
										dao.update("jssl.updategift", gift); //更新订单赠品数量
										gift.put("orderNum","SO"+gift.getString("order_num").substring(2));
										dao.update("jssl.updatesellgift", gift); //更新订单出库数量
									}
								}
							}
							*//********************************************//*
						}
					}
				}
			}
			List<PageData> item=(List<PageData>) dao.findForList("jssl.findnotactivity", pd);  //获取不是活动出库单明细
			for(PageData ex:item){
				*//*if(ex.getString("product_id").equals("96127")){
					//System.out.println("........");
					//System.out.println(ex.getString("product_id"));
				}*//*
				double final_quantity=Double.valueOf(ex.getString("final_quantity"));
				int jj=0;
				for(int i=0;i<inventory.size();i++){
					if(ex.getString("product_id").equals(inventory.get(i).getString("product_id"))){
						jj=1;
						double enqu=Double.valueOf(inventory.get(i).getString("quantity"));
						if(enqu!=0){
							//double quantity_dhl=Double.valueOf(inventory.get(i).getString("quantity_dhl")); //到货率
							//double giftquantity=new BigDecimal(DoubleUtil.multiply(final_quantity, quantity_dhl)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
							if(enqu<=final_quantity){
								ex.put("final_quantity", enqu);
								dao.update("jssl.updateexquantity", ex); 
								ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
								dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
								inventory.get(i).put("quantity",0);  //更新入库的总数量
							}else if(enqu>final_quantity){
								ex.put("final_quantity", final_quantity);
								dao.update("jssl.updateexquantity", ex); 
								ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
								dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
								inventory.get(i).put("quantity", DoubleUtil.sub(enqu, final_quantity));  //更新入库的总数量
							}
						}else{
							ex.put("final_quantity", 0);
							////System.out.println(ex.getString("product_id"));
							dao.update("jssl.updateexquantity", ex); 
						}
					}
				
				}
				if(jj==0){
					ex.put("final_quantity", 0);
					////System.out.println(ex.getString("product_id"));
					dao.update("jssl.updateexquantity", ex); 
				}
			}
			for(PageData en:inventory){
				double final_quantity=Double.valueOf(en.getString("quantity"));
				*//*if(en.getString("product_id").equals("96127")){
					//System.out.println("........");
				}*//*
			*//*	double gift_quantity=Double.valueOf(en.getString("gift_quantity"));*//*
				if(final_quantity>0){
					List<PageData> exite=(List<PageData>) dao.findForList("jssl.findexitem", en);
					for(PageData ex:exite){
						if(final_quantity==0){
							break;
						}
						double fin=Double.valueOf(ex.getString("final_quantity"));
						double qu=Double.valueOf(ex.getString("quantity"));
						if(fin==qu){
							continue;
						}else if(fin<qu){
							double cha=DoubleUtil.sub(qu,fin);
							if(final_quantity>cha){
								ex.put("final_quantity", qu);
								dao.update("jssl.updateexquantity", ex);
								ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
								dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
								final_quantity=DoubleUtil.sub(final_quantity, cha);
							}else if(final_quantity<=cha){
								ex.put("final_quantity", DoubleUtil.add(final_quantity, fin));
								dao.update("jssl.updateexquantity", ex);
								ex.put("orderNum","SO"+ex.getString("order_num").substring(2));
								dao.update("jssl.updatesellquantity", ex); //更新订单出库数量
								final_quantity=0;
							}
						}
					}
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			result=e.toString();
		}
		return result;
		
	}
	/**
	 * @author SuPengFei
	 * @DESCRIPTION: //TODO 自动分配订单
	 * @params: [groupNum]
	 * @return: java.lang.String
	 * @Date: 2019/3/12 11:52
	 * @Modified By:
	*/
	private String autoAssignOrder(String groupNum) {
		String msg = "自动分配成功!";
		ExecutorService executor  = ThreadPoolFactory.getThreadPool();
		try {
			//获取改批次所以商品的门店订单总数
			List<BuyGood> allBuyGoods =  autoAssignMapper.getAllBuyGoods(groupNum);
			if (allBuyGoods.size() <= 0) {
				return "该批次号下没有采购商品!";
			}

			List<Future<ResultDto>> futureList = new ArrayList<>();
			allBuyGoods.stream()
					.map(e->new AutoAssignThread(e, groupNum, autoAssignMapper))
					.forEach(a->futureList.add(executor.submit(a)));
			for (Future<ResultDto> resultDtoFuture : futureList) {
				if (!resultDtoFuture.get().getFlag()) {
					throw new RuntimeException("分配失败！");
				}
			}
			executor.shutdown();
			//修改订单状态为新出库单
			autoAssignMapper.updateOrderStart(groupNum);
		} catch (Exception e) {
			msg = "分配失败！";
		} finally {
			if (!executor.isShutdown()) {
				executor.shutdown();
			}
		}
		return msg;
	}
	/**
	 * @author SuPengFei
	 * @DESCRIPTION: //TODO 修改仓库库存数 减库存
	 * @params: [good]
	 * @return: void
	 * @Date: 2019/3/12 11:52
	 * @Modified By:
	*/
	private void updateWarehouse(BuyGood good) {
		double finalBuyCounts = good.getFinalBuyCounts();
		double wareCounts = 0.0;
		//根商品id分批次查库存
		List<BuyGood> warehouse = autoAssignMapper.selectWarehouse(good.getGoodId());
		for (BuyGood wareGood : warehouse) {
			if (finalBuyCounts == 0) {
				return;
			}
			wareCounts = wareGood.getWarehouseGoodCounts();
			if (  finalBuyCounts - wareCounts >= 0) {
				wareGood.setWarehouseGoodCounts(0.0);
				finalBuyCounts = finalBuyCounts - wareCounts;
				autoAssignMapper.updateWarehouse(wareGood);
			} else {
				wareGood.setWarehouseGoodCounts(wareCounts - finalBuyCounts);
				autoAssignMapper.updateWarehouse(wareGood);
				return;
			}
		}
	}


	/**
	 * 审核出库
	 * @param DATA_IDS
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String tomergeShck(String[] DATA_IDS) throws Exception{
		PageData exwarhouseOrder = null;
		PageData sumquantity=null;
		List<PageData> listPditem=null;
		List<PageData> listpd=null;
		PageData productinventory=null;
		PageData product=null;
		PageData exOrderItem=null;
		PageData inven=null;
		for (String id : DATA_IDS) {
			exwarhouseOrder=new PageData();
			exwarhouseOrder.put("id", id);
			PageData exOrder=(PageData) dao.findForObject("ExWarehouseOrderMapper.findById", exwarhouseOrder); //根据ID查询出库
			exwarhouseOrder.put("order_num", exOrder.getString("order_num")); //保存order_num
			listPditem=eXOrderItemService.findOrderItemByOrderNum(exwarhouseOrder); //查询订单详情
			listpd=new ArrayList<PageData>();
			for (PageData pdd : listPditem) { //循环订单详情，
				productinventory=new PageData();
				product=new PageData();
				sumquantity=new PageData();
				inven=new PageData();
				productinventory.put("productId", pdd.getString("product_id"));
				productinventory.put("ck_id", "1");
				productinventory.put("warehouseId", pdd.getString("is_ivt_BK"));
				/*PageData pro=productinventoryService.findProductInventory(productinventory);*/ //查库存数
				sumquantity=productinventoryService.sumquantity(productinventory);
				listpd=productinventoryService.findProductInventoryData(productinventory);//查库存数
				product=productService.findNameNumById(Long.valueOf(pdd.getString("product_id"))); //商品ID
				double ckqutity=Double.parseDouble(pdd.getString("final_quantity"))+Double.valueOf(pdd.getString("gift_quantity")); //当前出库数
				//double zkc=Double.parseDouble(sumquantity.getString("sumpro")); //总库存
				double a=0;
				//System.out.println("当前出库数 = "+ckqutity);
				/*if(pdd.getString("product_id").equals("96746")){
					//System.out.println("000");
				}*/
				////System.out.println(zkc);
				if (ckqutity > 0) {
					if (listpd.size() > 0) {
						if (Double.parseDouble(pdd.getString("final_quantity")) > 0 && !"".equals(pdd.getString("final_quantity"))) {
							for (PageData ck : listpd) {
								double dqkcshu = Double.parseDouble(ck.get("product_quantity").toString());//当前库存
								if (dqkcshu > 0 && ckqutity > dqkcshu) {
									ckqutity = ckqutity - dqkcshu; //出库数减当前库存
									productinventory.put("quantity", 0);
									productinventory.put("product_date", ck.getString("product_date"));
									productinventory.put("id", ck.getString("id"));
									this.dao.update("ProductinventoryMapper.updateProducData", productinventory);//更新一条库存为0
									//添加出库详情
									PageData pdexitem = new PageData();
									pdexitem.put("group_num", pdd.getString("group_num"));
									pdexitem.put("order_num", pdd.getString("order_num"));
									pdexitem.put("product_id", pdd.getString("product_id"));
									/*pdexitem.put("purchase_price", pdd.getString("purchase_price"));*/
									pdexitem.put("sale_price", pdd.getString("sale_price"));
									pdexitem.put("quantity", dqkcshu);
									pdexitem.put("final_quantity", dqkcshu);
									pdexitem.put("svolume", pdd.getString("svolume"));
									pdexitem.put("weight", pdd.getString("weight"));
									pdexitem.put("creator", pdd.getString("creator"));
									pdexitem.put("comment", pdd.getString("comment"));
									pdexitem.put("state", pdd.getString("state"));
									/*pdexitem.put("reason", pdd.getString("reason"));*/
									pdexitem.put("is_ivt_BK", pdd.getString("is_ivt_BK"));
									pdexitem.put("gift_quantity", pdd.getString("gift_quantity"));
									eXOrderItemService.saveitem(pdexitem);
									PageData p = new PageData();
									p.put("id", pdd.getString("id"));
									p.put("final_quantity", ckqutity);
									this.dao.update("EXOrderItemMapper.editgiftQuantity", p);
								} else if (ckqutity <= dqkcshu) { //否则如果出库数量小于当前库存
									a = dqkcshu - ckqutity;  //计算当前库存保存并更新 库存贱出库数
									productinventory.put("quantity", a);
									productinventory.put("product_date", ck.getString("product_date"));
									productinventory.put("id", ck.getString("id"));
									//System.out.println("剩余库存 =" + a);
									ckqutity = 0;
									this.dao.update("ProductinventoryMapper.updateProducData", productinventory);
									//productinventoryService.updateProducData(productinventory); //更新库存
									//更新生产日期和出库日期
									exOrderItem = new PageData();
									exOrderItem.put("id", pdd.getString("id"));
									exOrderItem.put("product_time", ck.getString("product_date"));
									this.dao.update("EXOrderItemMapper.edittime", exOrderItem);
									break;
								}
							}
							if (ckqutity > 0) {
								throw new RuntimeException("订单号:" + pdd.get("order_num") + ",商品:" + product.getString("product_name") + "库存数量小于出库数量");
							}
						}
						listpd = productinventoryService.findProductInventoryData(productinventory);//查库存数
						//赠品
						if (Double.parseDouble(pdd.getString("gift_quantity")) > 0 && !"".equals(pdd.getString("gift_quantity"))) {
							for (PageData ck : listpd) {
								double dqkcshu = Double.parseDouble(ck.get("product_quantity").toString());//当前库存
								if (dqkcshu > 0 && ckqutity > dqkcshu) {
									ckqutity = ckqutity - dqkcshu; //出库数减当前库存
									productinventory.put("quantity", 0);
                                    productinventory.put("id", ck.getString("id"));
									productinventory.put("product_date", ck.getString("product_date"));
									this.dao.update("ProductinventoryMapper.updateProducData", productinventory);
									//productinventoryService.updateProducData(productinventory); //更新一条库存为0
									//添加一条订单明细赠品数量
									PageData pdexitem = new PageData();
									pdexitem.put("group_num", pdd.getString("group_num"));
									pdexitem.put("order_num", pdd.getString("order_num"));
									pdexitem.put("product_id", pdd.getString("product_id"));
									/*pdexitem.put("purchase_price", pdd.getString("purchase_price"));*/
									pdexitem.put("sale_price", pdd.getString("sale_price"));
									pdexitem.put("quantity", pdd.getString("quantity"));
									pdexitem.put("final_quantity", pdd.getString("final_quantity"));
									pdexitem.put("svolume", pdd.getString("svolume"));
									pdexitem.put("weight", pdd.getString("weight"));
									pdexitem.put("creator", pdd.getString("creator"));
									pdexitem.put("comment", pdd.getString("comment"));
									pdexitem.put("state", pdd.getString("state"));
									/*pdexitem.put("reason", pdd.getString("reason"));*/
									pdexitem.put("is_ivt_BK", pdd.getString("is_ivt_BK"));
									pdexitem.put("gift_quantity", dqkcshu);
									eXOrderItemService.saveitem(pdexitem);
									PageData p = new PageData();
									p.put("id", pdd.getString("id"));
									p.put("gift_quantity", ckqutity);
									this.dao.update("EXOrderItemMapper.editgiftQuantity", p);
								} else if (ckqutity <= dqkcshu) { //否则如果出库数量小于当前库存
									a = dqkcshu - ckqutity;  //计算当前库存保存并更新 库存贱出库数
									productinventory.put("quantity", a);
                                    productinventory.put("id", ck.getString("id"));
									productinventory.put("product_date", ck.getString("product_date"));
									////System.out.println(a);
									ckqutity = 0;
									this.dao.update("ProductinventoryMapper.updateProducData", productinventory);
									//productinventoryService.updateProducData(productinventory); //更新库存
									//更新生产日期和出库日期
									exOrderItem = new PageData();
									exOrderItem.put("id", pdd.getString("id"));
									exOrderItem.put("product_time", ck.getString("product_date"));
									this.dao.update("EXOrderItemMapper.edittime", exOrderItem);
								}
							}
							if (ckqutity > 0) {
								throw new RuntimeException("订单号:" + pdd.get("order_num") + ",商品:" + product.getString("product_name") + "库存数量小于出库数量");
							}
						}
						/*else{
							throw new RuntimeException("订单号:" + pdd.get("order_num") + ",商品:" + product.getString("product_name") +"出库数为空");
						}*/
					} else {
						throw new RuntimeException("订单号:" + pdd.get("order_num") + ",商品:" + product.getString("product_name") +"库存为空");
					}
				}
				if(a>0){
					productinventory.put("comment", "出库后数量为" + a);
				}else{
					productinventory.put("comment", "出库后数量为" + a);
				}
				productinventory.put("order_num", pdd.get("order_num"));
				productinventory.put("product_id", pdd.get("product_id"));
				productinventory.put("quantity", Double.valueOf(pdd.getString("final_quantity"))+Double.valueOf(pdd.getString("gift_quantity"))); //出库数
				productinventory.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
				productinventory.put("rck_type", 2);
				dao.save("ProductinventoryMapper.savehistory", productinventory);//库存历史表
				
			}			
			exwarhouseOrder=new PageData();
			exwarhouseOrder.put("ivt_state","5");
			exwarhouseOrder.put("order_num", exOrder.getString("order_num"));
			editIvt(exwarhouseOrder);
			PageData ordergroup=new PageData();
			ordergroup.put("state",Integer.valueOf(2));
			ordergroup.put("group_num", exOrder.getString("group_num"));
			editordergroup(ordergroup);
			exwarhouseOrder.put("wave_sorting_state", Integer.valueOf(2));
			exwarhouseOrder.put("wave_sorting_num", exOrder.getString("wave_sorting_num"));
			updatewavestate(exwarhouseOrder);
		}
			return "true";
	}
	
	public List<PageData> exTemplistPage(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.exTemplistPage", page);
	}

	public void save(PageData pd) throws Exception {
		this.dao.save("ExWarehouseOrderMapper.save", pd);
	}

	public void delete(PageData pd) throws Exception {
		this.dao.delete("ExWarehouseOrderMapper.delete", pd);
	}

	public void edit(PageData pd) throws Exception {
		this.dao.update("ExWarehouseOrderMapper.edit", pd);
	}

	public void updatemerge() throws Exception {
		this.dao.update("ExWarehouseOrderMapper.updatemergeex", null);
		this.dao.update("ExWarehouseOrderMapper.updatemergeen", null);
		this.dao.update("ExWarehouseOrderMapper.updatemergegroup", null);
		this.dao.update("ExWarehouseOrderMapper.updatemergewave", null);
	}

	/**
	 * 更改出库状态
	 * @param pd
	 * @throws Exception
	 */
	public void editIvt(PageData pd) throws Exception {
		this.dao.update("ExWarehouseOrderMapper.editIvt", pd);
	}
	/**
	 * 更改ordergroup状态
	 * @param pd
	 * @throws Exception
	 */
	public void editordergroup(PageData pd) throws Exception{
		dao.update("ExWarehouseOrderMapper.editordergroup", pd);
	}
	/**
	 * 更改wavestate状态
	 * @param pd
	 * @throws Exception
	 */
	public void updatewavestate(PageData pd) throws Exception{
		dao.update("ExWarehouseOrderMapper.updatewavestate", pd);
	}

	public void editEx(PageData pd) throws Exception {
		this.dao.update("ExWarehouseOrderMapper.editEx", pd);
	}

	/**
	 * 仓配
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.datalistPage", page);
	}
	
	/**
	 * 直配
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> directlist(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.directlistPage", page);
	}
	
	public List<PageData> dcexorderlistexcel(PageData pd) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.dcexorderlistexcel", pd);
	}
	
	public List<PageData> printExOrder(List<PageData> list) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.printExOrder", list);
	}


	public List<PageData> listOfMerchants(String waveNum) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.datalistForWave", waveNum);
	}

	public PageData sanhuo(String id) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.sanhuo", id);
	}

	public List<PageData> listOfMerchantsForPer(String waveNum) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.datalistForWaveForPer", waveNum);
	}

	public List<PageData> listAll(PageData pd) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.listAll", pd);
	}

	public PageData findById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.findById", pd);
	}

	public List<PageData> getPerCountOfExwarouseItemByIdsForGift(String[] ArrayDATA_IDS) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getPerCountOfExwarouseItemByIdsForGift",
				ArrayDATA_IDS);
	}

	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		this.dao.delete("ExWarehouseOrderMapper.deleteAll", ArrayDATA_IDS);
	}

	@Transactional
	public String updatereviewedAll(String[] ArrayDATA_IDS) throws Exception {
		String msg = "";
		PageData enwarhouseOrder = null;
		PageData productInvertory = null;
		PageData exOrderItem = null;
		for (String id : ArrayDATA_IDS) {
			enwarhouseOrder = new PageData();
			enwarhouseOrder.put("id", id);
			PageData enOrder = (PageData) dao.findForObject("ExWarehouseOrderMapper.findById", enwarhouseOrder);
			enwarhouseOrder.put("order_num", enOrder.getString("order_num"));
			List<PageData> enOrderItemList = (List) this.dao.findForList("EXOrderItemMapper.findOrderItemByOrderNum",
					enwarhouseOrder);
			List<PageData> pList = new ArrayList();
			for (PageData p : enOrderItemList) {
				productInvertory = new PageData();
				productInvertory.put("productId", p.get("product_id"));
				productInvertory.put("newQuantity", DoubleUtil.add(Double.valueOf(p.getString("final_quantity")),Double.valueOf( p.getString("gift_quantity"))));
				productInvertory.put("warehouseId", p.get("is_ivt_BK"));
				productInvertory.put("ck_id", enOrder.get("ck_id"));
				PageData pd = this.productinventoryService.findProductInventory(productInvertory);
				PageData productData = this.productService
						.findNameNumById(Long.parseLong(p.get("product_id").toString()));
				if (null == pd) {
					throw new RuntimeException(
							"订单号:" + p.get("order_num") + ",商品:" + productData.getString("product_name") + "数量不足");
				}
				if ((!"".equals(productInvertory.get("newQuantity")))
						|| (productInvertory.get("newQuantity") != null)) {
					if ((!"".equals(pd.get("product_quantity"))) || (pd.get("product_quantity") != null)) {
						double newQuantity = Double.parseDouble(productInvertory.get("newQuantity").toString());// 出库数量
						double oldQuantity = Double.parseDouble(pd.get("product_quantity").toString());// 库存数量
						BigDecimal data1 = new BigDecimal(oldQuantity);
						BigDecimal data2 = new BigDecimal(newQuantity);
						int i = data1.compareTo(data2);
						if (i == -1) {// 如果库存数量《出库数量 ，提示库存不足
							// return p.get("order_num") + "," + productData.getString("product_name");
							throw new RuntimeException("订单号:" + p.get("order_num") + ",商品:"
									+ productData.getString("product_name") + "数量不足");
						}
						pList.add(p);
					} else {
						throw new RuntimeException("库存数量为空！");
					}
				} else {
					throw new RuntimeException("出库数量为空！");
				}
			}
			for (PageData p : pList) {
				productInvertory = new PageData();
				productInvertory.put("productId", p.get("product_id"));
				productInvertory.put("newQuantity", DoubleUtil.add(Double.valueOf(p.getString("final_quantity")),Double.valueOf( p.getString("gift_quantity"))));
				productInvertory.put("warehouseId", p.get("is_ivt_BK"));
				productInvertory.put("ck_id", enOrder.get("ck_id"));
				PageData pd = this.productinventoryService.findProductInventory(productInvertory);
				double newQuantity = Double.parseDouble(productInvertory.get("newQuantity").toString());// 出库数量
				double oldQuantity = Double.parseDouble(pd.get("product_quantity").toString());// 库存数量
				double quantity = NumberUtil.sub(oldQuantity, newQuantity);
				productInvertory.put("quantity", Double.valueOf(quantity));
				/*
				 * //System.out.println("新数量---" + newQuantity + "--------------------------");
				 * //System.out.println("库存数量---" + oldQuantity + "--------------------------");
				 * //System.out.println("剩余---" + productInvertory.get("quantity") +
				 * "--------------------------");
				 */
				this.productinventoryService.updateProductinventory(productInvertory); // 修改库存数量
				productInvertory.put("order_num", p.get("order_num"));
				productInvertory.put("product_id", p.get("product_id"));
				productInvertory.put("comment", "出库后数量为" + quantity);
				productInvertory.put("quantity", newQuantity);
				productInvertory.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
				productInvertory.put("rck_type", 2);
				dao.save("ProductinventoryMapper.savehistory", productInvertory);

				exOrderItem = new PageData();
				exOrderItem.put("id", p.get("id"));
				this.eXOrderItemService.edit(exOrderItem);
				enwarhouseOrder.put("ivt_state", Integer.valueOf(5));
				editIvt(enwarhouseOrder);// 修改订单状态
			}
		}
		return "true";
	}

	public boolean saleReviewedAll(String[] ArrayDATA_IDS) {
		try {
			for (String id : ArrayDATA_IDS) {
				PageData enwarhouseOrder = new PageData();
				enwarhouseOrder.put("id", id);

				enwarhouseOrder = (PageData) this.dao.findForObject("ExWarehouseOrderMapper.findById", enwarhouseOrder);

				List<PageData> enOrderItemList = (List) this.dao
						.findForList("EXOrderItemMapper.findOrderItemByOrderNum", enwarhouseOrder);

				for (PageData page : enOrderItemList) {
					PageData productInvertory = new PageData();
					productInvertory.put("productId", page.get("product_id"));
					productInvertory.put("newQuantity", page.get("final_quantity"));
					productInvertory.put("warehouseId", page.get("is_ivt_BK"));
					PageData exOrderItem = new PageData();
					exOrderItem.put("id", page.get("id"));

					if (!this.productinventoryService.updateProductinventoryAdd(productInvertory)) {
						return false;
					}
					this.eXOrderItemService.edit(exOrderItem);
				}

				enwarhouseOrder.remove("ivt_state");
				enwarhouseOrder.put("ivt_state", Integer.valueOf(2));
				edit(enwarhouseOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public PageData getExwarouseById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.getExwarouseById", pd);
	}
	
	/**
	 * 直配打印
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getdirectExwarouseById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.getdirectExwarouseById", pd);
	}
	
	public PageData getExwarousegetId(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.getExwarousegetId", pd);
	}

	public List<PageData> getExwarouseInfosByWaveNum(String waveNum) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getExwarouseByWaveNum", waveNum);
	}

	public int updateFinalAmount(PageData pd) throws Exception {
		Object p = this.dao.update("ExWarehouseOrderMapper.updateFinalAmount", pd);
		return Integer.parseInt(p.toString());
	}

	public List<Product> getExwarouseByIds(String[] ArrayDATA_IDS) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getExwarouseByIds", ArrayDATA_IDS);
	}

	public List<Product> getExwarouseItemForProductByIds(PageData pd) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getExwarouseItemForProductByIds", pd);
	}

	public List<PageData> getPerCountOfExwarouseItemByIds(String[] ArrayDATA_IDS) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getPerCountOfExwarouseItemByIds", ArrayDATA_IDS);
	}

	public List<PageData> getPerCountOfExwarouseItemByIds2(Map<String,Object> map) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getPerCountOfExwarouseItemByIds2",map);
	}

	public List<PageData> StorageExWarehouseList(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.StorageExWarehouselistPage", page);
	}

	public List<PageData> getExwarouseItemByIds(String[] ArrayDATA_IDS) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getExwarouseItemByIds", ArrayDATA_IDS);
	}

	public List<PageData> getExwarouseItemByWaveNum(String waveNum) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getExwarouseItemByWaveNum", waveNum);
	}

	public List<PageData> listOfGift(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.datalistPage", page);
	}

	public List<PageData> getExwarouseItemByIdsForGift(String[] ArrayDATA_IDS) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.getExwarouseItemByIdsForGift", ArrayDATA_IDS);
	}

	public List<PageData> purchaseReturnlistpage(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.purchaseReturnlistPage", page);
	}

	public List<PageData> dataCeterExOrderlistPage(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.dataCeterExOrderlistPage", page);
	}

	public PageData getExwarouseByorderNum(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.getExwarouseByorderNum", pd);
	}

	public void deleteExOrderItemByOrderNum(PageData pd) throws Exception {
		this.dao.delete("ExWarehouseOrderMapper.deleteExOrderItemByOrderNum", pd);
	}
	/**
	 * 根据出库单号码查询出库单详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findexorderitem(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ExWarehouseOrderMapper.findexorderitem", pd);
	}
	/**
	 * 审核出库单
	 * @param ArrayDATA_IDS
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String toExamine(String[] ArrayDATA_IDS) throws Exception {
		String msg = "";
		PageData enwarhouseOrder = null;
		PageData productInvertory = null;
		PageData exOrderItem = null;
		for (String id : ArrayDATA_IDS) {
			enwarhouseOrder = new PageData();
			enwarhouseOrder.put("id", id);
			PageData enOrder = (PageData) dao.findForObject("ExWarehouseOrderMapper.findById", enwarhouseOrder);
			enwarhouseOrder.put("order_num", enOrder.getString("order_num"));
			List<PageData> enOrderItemList = (List) this.dao.findForList("EXOrderItemMapper.findOrderItemByOrderNum",
					enwarhouseOrder);
			List<PageData> pList = new ArrayList();
			for (PageData p : enOrderItemList) {
				productInvertory = new PageData();
				productInvertory.put("productId", p.get("product_id"));
				productInvertory.put("newQuantity", p.get("final_quantity"));
				productInvertory.put("warehouseId", p.get("is_ivt_BK"));
				productInvertory.put("ck_id", enOrder.get("ck_id"));
				PageData pd = this.productinventoryService.findProductInventory(productInvertory);
				PageData productData = this.productService
						.findNameNumById(Long.parseLong(p.get("product_id").toString()));
				if (null == pd) {
					throw new RuntimeException(
							"订单号:" + p.get("order_num") + ",商品:" + productData.getString("product_name") + "数量不足");
				}
				if ((!"".equals(productInvertory.get("newQuantity")))
						|| (productInvertory.get("newQuantity") != null)) {
					if ((!"".equals(pd.get("product_quantity"))) || (pd.get("product_quantity") != null)) {
						double newQuantity = Double.parseDouble(productInvertory.get("newQuantity").toString());// 出库数量
						double oldQuantity = Double.parseDouble(pd.get("product_quantity").toString());// 库存数量
						BigDecimal data1 = new BigDecimal(oldQuantity);
						BigDecimal data2 = new BigDecimal(newQuantity);
						int i = data1.compareTo(data2);
						if (i == -1) {// 如果库存数量出库数量 ，提示库存不足
							throw new RuntimeException("订单号:" + p.get("order_num") + ",商品:"
									+ productData.getString("product_name") + "数量不足");
						}
						pList.add(p);
					} else {
						throw new RuntimeException("库存数量为空！");
					}
				} else {
					throw new RuntimeException("出库数量为空！");
				}
			}
			for (PageData p : pList) {
				productInvertory = new PageData();
				productInvertory.put("productId", p.get("product_id"));
				productInvertory.put("newQuantity", p.get("final_quantity"));
				productInvertory.put("warehouseId", p.get("is_ivt_BK"));
				productInvertory.put("ck_id", enOrder.get("ck_id"));
				PageData pd = this.productinventoryService.findProductInventory(productInvertory);
				double newQuantity = Double.parseDouble(productInvertory.get("newQuantity").toString());// 出库数量
				double oldQuantity = Double.parseDouble(pd.get("product_quantity").toString());// 库存数量
				double quantity = NumberUtil.sub(oldQuantity, newQuantity);
				productInvertory.put("quantity", Double.valueOf(quantity));
				this.productinventoryService.updateProductinventory(productInvertory); // 修改库存数量
				productInvertory.put("order_num", p.get("order_num"));
				productInvertory.put("product_id", p.get("product_id"));
				productInvertory.put("comment", "出库后数量为" + quantity);
				productInvertory.put("quantity", newQuantity);
				productInvertory.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
				productInvertory.put("rck_type", 2);
				dao.save("ProductinventoryMapper.savehistory", productInvertory);

				exOrderItem = new PageData();
				exOrderItem.put("id", p.get("id"));
				this.eXOrderItemService.edit(exOrderItem);			//根据商品ID修改出库详情的价格数量备注创建人等
				enwarhouseOrder.put("ivt_state", Integer.valueOf(5));//更新ivt_state订单状态
				editIvt(enwarhouseOrder);// 修改订单状态
			}
		}
		return "true";
	}
	
	public List<PageData> fenCangdatalistPage(Page page) throws Exception {
		return (List) this.dao.findForList("ExWarehouseOrderMapper.fenCangdatalistPage", page);
	}
	
	/**
	 * 直配出库单导出
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> exorderlistexcel(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ExWarehouseOrderMapper.exorderlistexcel", pd);
	}
	
	public List<PageData> chart(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ExWarehouseOrderMapper.chart",pd);
	}
	
	/**
	 * 图表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> charts(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ExWarehouseOrderMapper.charts",pd);
	}
	
	/**
	 * 导出excel
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listpd(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ExWarehouseOrderMapper.exportexcel", pd);
	}
	  public List<PageData> xslistpd(PageData pd) throws Exception{
			return (List<PageData>) dao.findForList("SellingOrderItemMapper.xsportexcel", pd);
		}
	public List<PageData> findrate(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ExWarehouseOrderMapper.findrate", pd);
	}
	/**
	 * 根据批次号查询总站点数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData sumcity(PageData pd) throws Exception{
		return (PageData) dao.findForObject("ExWarehouseOrderMapper.sumcity", pd);
	}
	
	/**查询配送返单**/
	public JSONObject findDistribution() throws Exception {
		JSONObject  obj =new JSONObject();
		String result="";
		try {
			result = HttpUtil.doGet(Const.httpplan+"interfaces/querywxtime","UTF-8");
			result = URLDecoder.decode(result, "UTF-8");
			obj=JSONObject.parseObject(result);
			
		} catch (Exception e) {
			
		}
		return  obj;
	}
}
