package com.hy.controller.pfckd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.product.Product;
import com.hy.entity.system.User;
import com.hy.service.pfckd.PfackdService;
import com.hy.service.product.ProductService;
import com.hy.util.Const;
import com.hy.util.DateUtil;
import com.hy.util.DoubleUtil;
import com.hy.util.FileUpload;
import com.hy.util.LoginUtil;
import com.hy.util.MathUtil;
import com.hy.util.ObjectExcel;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import com.hy.util.StringUtil;

/**
 * 配发出库单导入
 * @author gyy
 *
 */
@Controller
@RequestMapping({ "/pfackdcontroller" })
public class PfackdController extends BaseController{
	
	
	@Resource
	private PfackdService pfackdservice;
	
	@Resource(name="productService")
	private ProductService productService;
	
	  
	  
	 /**
	  * 配送导入 
	  * @param file
	  * @return
	  */
	@RequestMapping(value = "/importExcelOfEXOrder")
	public ModelAndView importExcelOfEXOrder(
			@RequestParam(value = "excel2", required = false) MultipartFile file) {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "导入配发出库单到数据库";
		logBefore(logger, operationMsg);
		try {
			User user = (User) LoginUtil.getLoginUser();
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;
				String fileName = FileUpload.fileUp(file, filePath,"PurchaseOrderExcel");
				// 执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
				List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
				/**
				 * var0 :门店简称  var1 :门店编码  var2 :商品编码  var3 :条形码  var4 :入库数量
				 */
				List<PageData> errors = new ArrayList<PageData>();  //错误信息
				List<PageData> pdM=new ArrayList<PageData>();  //销售单
				List<PageData> pdS=new ArrayList<PageData>();	//销售单详情
				List<PageData> uporder = new ArrayList<PageData>();  //更新销售单
				List<PageData> uporderitem = new ArrayList<PageData>();  //更新销售单详情
				List<PageData> uporderitem2 = new ArrayList<PageData>();  //插入销售单详情
				if (listPd != null && listPd.size() > 0) {
					for (int i = 0; i < listPd.size(); i++) {
						Product product=null;
						PageData pdE=new PageData();  //收集错误信息
						PageData pdP=new PageData();  //保存信息
						PageData order = new PageData();  //保存出库订单信息
						PageData orderitem = new PageData();  //保存出库订单详情信息
						PageData updateorder = new PageData(); //更新出库单
						PageData updateorderitem = new PageData(); //更新出库单详情
						PageData updateorderitem2 = new PageData();
						PageData page=listPd.get(i);
						String ordernum = "CK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber() ;
						String mendname = "";
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var0").toString())) {
							pdP.put("short_name",page.get("var0").toString()); 
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "A");
							pdE.put("reason0", "门店简称为空");
						}
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var1").toString())) {
							pdP.put("merchant_num",page.get("var1").toString()); 
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "B");
							pdE.put("reason0", "门店编码为空");
						}
						boolean  isandnot = false;  // 判断该门店是否有新的出库单 有则为 true
						boolean  isandnotitem = false;  //判断该出库单详情中是否有该商品 有则为true
						String product_id = "";  //商品id
						String order_num = "";  //出库单号
						String group_num = "";  //出库批次号
						String id = ""; //出库单id
						double final_amount = 0.00; //出库单总金额
						double total_svolume = 0.00;  //出库单总体积
						double total_weight = 0.00; //出库单总重量
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var0").toString())&&
								!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var1").toString())) {
							PageData pro=new PageData();
							pro.put("merchant_num", page.getString("var1"));
							pro.put("short_name", page.getString("var0"));
							PageData pd = pfackdservice.findMerchant(pro);
							if(pd == null){
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row0", "A、B");
								pdE.put("reason0", "查无该门店信息");
							}else{
								mendname = pd.getString("merchant_name");
								//如果该门店有最新的出库单则更新出库单信息
								String mdid = pd.getString("id");
								PageData pdm = new PageData();
								pdm.put("merchant_id", mdid);
								PageData findmd = pfackdservice.findMdExorder(pdm);
								if(findmd != null && !findmd.equals("")){
									//更新出库单
									isandnot = true;
									order_num = findmd.getString("order_num");
									group_num = findmd.getString("group_num");
									id = findmd.getString("id");
									final_amount = Double.parseDouble(findmd.getString("final_amount"));
									total_weight = Double.parseDouble(findmd.getString("total_weight"));
									total_svolume = Double.parseDouble(findmd.getString("total_svolume"));
									updateorder.put("id", id);  //封装要更新的出库单
								}else{
									orderitem.put("merchant_id", pd.getString("id")); //此处用于后面处理数据时用到
									//封装出库单信息（新增出库单）
									order.put("order_num", ordernum);
									order.put("group_num","GP_"+DateUtil.group());
									order.put("merchant_id", pd.getString("id"));
									order.put("checked_state", "1");
									order.put("manager_name", pd.getString("merchant_name"));
									order.put("manager_tel", pd.getString("mobile"));
									order.put("deliver_date", "");
									order.put("deliver_style", "");
									order.put("deliver_address", "");
									order.put("comment", "配发订单");
									order.put("paid_amount",0);
									order.put("is_ivt_order_print", 0);
									order.put("is_temporary", 2);
									order.put("user_id", user.getUSER_ID());
									order.put("is_order_print", 2);
									order.put("ivt_state", 1);
									order.put("amount", 0);
									order.put("order_type", 1);
									order.put("wave_sorting_num", 0);
									order.put("wave_order", 0);
									order.put("ck_id", "1");
									order.put("type", "2");
									order.put("confirmed", "1");
								}
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "C、D");
							pdE.put("reason0", "门店简称或门店编码为空");
						}
						
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var2").toString())) {
							pdP.put("product_num",page.get("var2").toString());  
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "C");
							pdE.put("reason0", "商品编码为空");
						}
						
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var3").toString())) {
							pdP.put("bar_code",page.get("var3").toString()); 
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "D");
							pdE.put("reason0", "条形码为空");
						}
						double tj = 0.0000;
						double zl = 0.0000;
						double je = 0.0000;
						
						String comment = ""; //备注
						Double quantity = 0.00; //订单数量
						Double final_quantity = 0.00; //实际数量
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var2").toString())&&
								!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var3").toString())) {
							PageData pro=new PageData();
							pro.put("barCode", page.getString("var3"));
							pro.put("product_num", page.getString("var2"));
							product = productService.findById(pro);
							if(product==null){
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row0", "C、D");
								pdE.put("reason0", "查无该商品信息");
							}else{
								pdP.put("product_id", product.getId());
								tj = Double.parseDouble(product.getFclVolume());
								zl = Double.parseDouble(product.getFclWeight());
								PageData pd = new PageData();
								pd.put("id", product.getId());
								PageData pricedata =  pfackdservice.findprice(pd);
								if(pricedata != null){
									je = Double.parseDouble(pricedata.getString("product_price"));
								}
								if(isandnot){  //在该门店的新出库单进行 更新/新增 出库单详情操作
									
									updateorderitem2.put("order_num", order_num);
									updateorderitem2.put("product_id", product.getId());
									//根据门店新的出库单号和商品id查询订单详情
									PageData itempd = pfackdservice.findexorderitem(updateorderitem2);
									if(itempd != null && !itempd.equals("")){
										isandnotitem = true;
										comment = itempd.getString("comment");
										quantity = Double.parseDouble(itempd.getString("quantity"));
										final_quantity = Double.parseDouble(itempd.getString("final_quantity"));
										updateorderitem.put("id", itempd.getString("id"));
									}else{
										//在门店新出库单详情中添加
										updateorderitem2.put("group_num", group_num);
										updateorderitem2.put("sale_price", je);
									}
								}else{
									//封装出库单详情
									orderitem.put("order_num", ordernum );
									orderitem.put("group_num", "GP_"+DateUtil.group());
									orderitem.put("product_id", product.getId());
									orderitem.put("sale_price", je);
								}
								
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "C、D");
							pdE.put("reason0", "商品编码或商品条形码为空");
						}
						
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var4").toString())) {
							//封装出库单信息
							if(isandnot){
								//在该门店新出库单中进行更新操作
								updateorder.put("final_amount", DoubleUtil.add(final_amount, DoubleUtil.multiply(je, Double.parseDouble(page.get("var4").toString()))));
								updateorder.put("total_svolume", DoubleUtil.add(total_svolume, DoubleUtil.multiply(tj, Double.parseDouble(page.get("var4").toString()))));
								updateorder.put("total_weight", DoubleUtil.add(total_weight, DoubleUtil.multiply(zl, Double.parseDouble(page.get("var4").toString()))));
								uporder.add(updateorder);  //需要更新的出库单list
								if(isandnotitem){
									updateorderitem.put("comment", comment+"，配发订单"+page.get("var4").toString());
									updateorderitem.put("quantity", DoubleUtil.add(quantity, Double.parseDouble(page.get("var4").toString())));
									updateorderitem.put("final_quantity", DoubleUtil.add(final_quantity, Double.parseDouble(page.get("var4").toString())));
									updateorderitem.put("sale_price", je);
									uporderitem.add(updateorderitem);
								}else{
									//封装出库单详情
									updateorderitem2.put("quantity", page.get("var4").toString());
									updateorderitem2.put("final_quantity", page.get("var4").toString());
									updateorderitem2.put("svolume", tj);
									updateorderitem2.put("weight", zl);
									updateorderitem2.put("creator", user.getNAME());
									updateorderitem2.put("comment", "配发订单" + page.get("var4").toString());
									updateorderitem2.put("state", 1);
									updateorderitem2.put("reason", "配发订单");
									updateorderitem2.put("is_ivt_BK", 1);
									updateorderitem2.put("zy_order_num", "");
									updateorderitem2.put("per_count", 0);
									updateorderitem2.put("total_count", 0);
									updateorderitem2.put("fc_order_num", "");
									updateorderitem2.put("product_time", "");
									updateorderitem2.put("gift_quantity", 0);
									uporderitem2.add(updateorderitem2);
								}
							}else{
								order.put("final_amount", DoubleUtil.multiply(je, Double.parseDouble(page.get("var4").toString())));
								order.put("total_svolume", DoubleUtil.multiply(tj, Double.parseDouble(page.get("var4").toString())));
								order.put("total_weight", DoubleUtil.multiply(zl, Double.parseDouble(page.get("var4").toString())));
								//保存出库单
								//pfackdservice.saveExOrder(order);
								pdM.add(order);  //出库单list
								//封装出库单详情
								orderitem.put("quantity", page.get("var4").toString());
								orderitem.put("final_quantity", page.get("var4").toString());
								orderitem.put("svolume", tj);
								orderitem.put("weight", zl);
								orderitem.put("creator", user.getNAME());
								orderitem.put("comment", "配发订单" + page.get("var4").toString());
								orderitem.put("state", 1);
								orderitem.put("reason", "配发订单");
								orderitem.put("is_ivt_BK", 1);
								orderitem.put("zy_order_num", "");
								orderitem.put("per_count", 0);
								orderitem.put("total_count", 0);
								orderitem.put("fc_order_num", "");
								orderitem.put("product_time", "");
								orderitem.put("gift_quantity", 0);
								//pfackdservice.saveExOrderItem(orderitem);
								pdS.add(orderitem);  //出库详情表list
							}
							
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "E");
							pdE.put("reason0", "数量为空");
						}
						if ((pdE != null) && (pdE.getString("line") != null) && (pdE.getString("line") != "")) {
							errors.add(pdE);
						}
					}
					if(errors.size()>0){
						pdS.clear();  //清空数组
						pdM.clear();
						mv.addObject("varList2",errors);
					}else{
						mv.addObject("Msg", pfackdservice.importExcelOfEXOrder(pdM, pdS,uporder,uporderitem,uporderitem2));
					}
					
				} else {
					// 表格中数据为空
					mv.addObject("Msg", "数据表中没有数据");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//logMidway(logger, operationMsg + "，出现错误：" + e.toString(), 1);
		} finally {
			mv.setViewName("procurement/product/importCommodity");
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 封装销售单
	 * @param pdd
	 * @return
	 */
	public PageData saveExOrder(PageData pdd){
		  PageData exOrder=new PageData();
		  exOrder.put("group_num", pdd.getString("group_num"));
		  exOrder.put("order_num", pdd.getString("orderNum"));
		  exOrder.put("checked_state", 1);
		  exOrder.put("merchant_id", pdd.getString("merchant_id"));
		  exOrder.put("manager_name", pdd.getString("manager_name"));
		  exOrder.put("manager_tel", pdd.getString("manager_tel"));
		  exOrder.put("deliver_address", pdd.getString("deliver_address"));
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
		  exOrder.put("order_type",1);
		  exOrder.put("ck_id", 1);
		  exOrder.put("type", pdd.getString("type"));
		  return exOrder;
	  }
	
	/**
	 * 封装销售单详情
	 * @param pdd
	 * @return
	 */
	  public PageData saveExOrderItem(PageData pdd){
		  PageData exOrderItem=new PageData();
		  exOrderItem.put("group_num", pdd.getString("group_num"));
		  exOrderItem.put("order_num", pdd.getString("orderNum"));
		  exOrderItem.put("product_id", pdd.getString("product_id"));
		  exOrderItem.put("sale_price", pdd.getString("sale_price"));
		  exOrderItem.put("quantity", pdd.getString("quantity"));
		  exOrderItem.put("final_quantity", pdd.getString("final_quantity"));
		  String sku_volume = pdd.getString("sku_volume");
		  if(sku_volume == null || sku_volume.equals("")){
			  sku_volume = "0.00";
		  }
		  String sku_weight = pdd.getString("sku_weight");
		  if(sku_weight == null || sku_weight.equals("")){
			  sku_weight = "0.00";
		  }
		  exOrderItem.put("svolume", DoubleUtil.multiply(Double.valueOf(sku_volume), Double.valueOf(pdd.getString("final_quantity"))));
		  exOrderItem.put("weight", DoubleUtil.multiply(Double.valueOf(sku_weight), Double.valueOf(pdd.getString("final_quantity"))));
		  exOrderItem.put("creator", LoginUtil.getLoginUser().getUSERNAME());
		  exOrderItem.put("comment", pdd.getString("comment"));
		  exOrderItem.put("state",1);
		  exOrderItem.put("is_ivt_BK", 1);
		  exOrderItem.put("gift_quantity",pdd.getString("gift_quantity"));
		  return exOrderItem;
	  }
	  
	  /**
	   * 获取仓配批次号
	 * @throws ParseException 
	   */
	 /* public String findGroupNum() throws ParseException{
		  Calendar cal = Calendar.getInstance();  
		  Date date = new Date();
	      cal.setTime(date);  
	     
	      //获取年份
	      String year = new SimpleDateFormat("yyyyMM").format(cal.getTime());
	      //所在周开始日期  
	      ////System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
	      
	      int weekOfMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);   //当前月的第几个星期天
	      String groupnum= "GP_" + year + "0"+String.valueOf(weekOfMonth);
	      return groupnum;
	  }*/
}
