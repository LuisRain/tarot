package com.hy.controller.order;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.*;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.Product;
import com.hy.entity.product.Supplier;
import com.hy.entity.system.User;
import com.hy.service.order.*;
import com.hy.service.product.MerchantService;
import com.hy.service.product.ProductService;
import com.hy.service.product.ProductTypeService;
import com.hy.service.product.SupplierService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping({"/sellingorder"})
public class SellingOrderController
  extends BaseController
{
  @Resource(name="sellingOrderService")
  private SellingOrderService sellingOrderService;
  @Resource(name="sellingOrderItemService")
  private SellingOrderItemService sellingOrderItemService;
  @Resource(name="eNOrderService")
  private ENOrderService eNOrderService;
  @Resource(name="eNOrderItemService")
  private ENOrderItemService eNOrderItemService;
  @Resource(name="eXOrderService")
  private EXOrderService eXOrderService;
  @Resource(name="eXOrderItemService")
  private EXOrderItemService eXOrderItemService;
  @Resource(name="purchaseOrderSerivce")
  private PurchaseOrderSerivce purchaseOrderSerivce;
  @Resource(name="purchaseOrderItemService")
  private PurchaseOrderItemService purchaseOrderItemService;
  @Resource(name="supplierService")
  private SupplierService supplierService;
  @Resource(name="sysLogService")
  private SysLogService sysLogService;
  @Resource(name="productService")
  private ProductService productService;
  @Resource(name="merchantService")
  private MerchantService merchantService;
  @Resource(name="orderGroupService")
  private OrderGroupService orderGroupService;
  @Resource(name = "productTypeService")
  private ProductTypeService productTypeService;
  @Resource(name = "enwarehouseorderService")
  private EnWarehouseOrderService enwarehouseorderService;
	@Autowired
	private AuditOrderService auditOrderService;
  @RequestMapping(value="/dcexorderlistexcel")
 	public ModelAndView dcexorderlistexcel(){
 		ModelAndView mv=new ModelAndView();
 		PageData pd=new PageData();
 		String operationMsg="ProductExcel导出库存操作";
 		logBefore(logger, operationMsg);
 		try {
 			pd=this.getPageData();
 			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
 			String orderdate=pd.getString("order_date");
			if ((orderdate != null) && (!"".equals(orderdate))) {
				String lastLoginEnd = orderdate;
				orderdate = orderdate + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", orderdate);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
 			Map<String,Object> map=new HashMap<String, Object>();
 			List<String> titles=new ArrayList<String>();
 			titles.add("批次号"); // 1
 			titles.add("订单号"); // 2
 			titles.add("商品编码"); // 5
 			titles.add("商品条形码"); // 2
 			titles.add("商品名称"); // 3
 			titles.add("采购价格"); // 3
 			titles.add("订购数量"); // 3
 			titles.add("实际数量"); // 3
 			titles.add("赠品数量"); // 6
 			titles.add("备注"); // 7
 			titles.add("供应商编码"); // 9
 			titles.add("供应商名称"); // 8
 			titles.add("入库日期"); // 8\
 			titles.add("状态"); // 8
 			titles.add("生产日期"); // 8
 			map.put("titles", titles);
 			List<PageData> listpd=enwarehouseorderService.sellingOrderlistexcel(pd);
 			List<PageData> varList=new ArrayList<PageData>();
 			for (int i = 0; i < listpd.size(); i++) {
 				PageData pdd=new PageData();
 				pdd.put("var1", listpd.get(i).getString("group_num"));
 				pdd.put("var2", listpd.get(i).getString("order_num"));
 				pdd.put("var3", listpd.get(i).getString("product_num"));
 				pdd.put("var4", listpd.get(i).getString("bar_code"));
 				pdd.put("var5", listpd.get(i).getString("product_name"));
 				pdd.put("var6", listpd.get(i).getString("purchase_price"));
 				pdd.put("var7", listpd.get(i).getString("quantity"));
 				pdd.put("var8", listpd.get(i).getString("final_quantity"));
 				pdd.put("var9", listpd.get(i).getString("gift_quantity"));
 				pdd.put("var10", listpd.get(i).getString("comment"));
 				pdd.put("var11", listpd.get(i).getString("supplier_num"));
 				pdd.put("var12", listpd.get(i).getString("supplier_name"));
 				pdd.put("var13", listpd.get(i).getString("order_date"));
 				if(listpd.get(i).getString("checked_state").equals("1")){
 					pdd.put("var14", "未审核");
 				}else{
 					pdd.put("checked_state", "已审核");
 				}
 				pdd.put("var15", listpd.get(i).getString("product_time"));
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
  
  
  @RequestMapping(value = { "/sellingorderitemdq" })
	public ModelAndView sellingorderitemdq() {
		String operationMsg="查询销售管理入库单";
		logBefore(logger, operationMsg);
		ModelAndView mv = new ModelAndView();
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			PageData sell=(PageData) sellingOrderService.findorder(pd);
			List<PageData> varList=sellingOrderService.findorderitem(pd);
			mv.addObject("list",varList);
			mv.addObject("obj",sell);
			mv.addObject("pd",pd);
			mv.addObject("role_id",LoginUtil.getLoginUser().getROLE_ID());
			mv.setViewName("procurement/sellingorder/dqfindorderitem");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		
		return mv;
	}
	  /**
	   * 地区负责人修改订单信息
	   * @param page
	   * @return
	   */
    @RequestMapping(value = { "updatesellOrderProduct" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String updateEnOrderProduct(Page page) {
		String result = "true";
		try {
			PageData pd = getPageData();
			String ids = pd.getString("ids");
			String numbers = pd.getString("numbers");
			if (!"".equals(ids)) {
				String[] idsStr = ids.split(",");
				String[] numbersStr = numbers.split(",");
				for (int i = 0; i < idsStr.length; i++) {
					PageData itemid = new PageData();
					itemid.put("id", idsStr[i]);
					itemid=sellingOrderItemService.updatesellitemproduct(itemid);
					if(!numbersStr[i].equals(itemid.getString("quantity"))){
						itemid.put("quantity",numbersStr[i]);
						if(itemid.containsKey("product_activity")){  //查找该商品是否为活动商品
							PageData item=new PageData();
							item.put("order_num",itemid.getString("order_num"));
							item.put("product_id",itemid.getString("product_activity"));
							double proq=Double.valueOf(itemid.getString("quantity"));
							double qq=Double.valueOf(itemid.getString("tpaquantity"));
							int pp=(int)proq/(int)qq;
							double final_quantity=Double.valueOf(itemid.getString("final_quantity"));
							item.put("gift_quantity",(int)pp*(int)final_quantity);
							sellingOrderItemService.updateitemproduct(item);
							sellingOrderItemService.editorderitem(itemid);
						}else{//不是
							sellingOrderItemService.editorderitem(itemid);
						}
					}
				}
			} else {
				result = "false";
				this.sysLogService.saveLog("修改订单数量以及订单商品数量", "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "false";
		}
		return result;
	}
  
  
  @RequestMapping(value="/dqexcel")
	public ModelAndView ProductExcel(Page page){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="ProductExcel导出库存操作";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			pd.put("username", LoginUtil.getLoginUser().getUSERNAME());
			Map<String,Object> map=new HashMap<String, Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("销售单明细id"); // 2
			titles.add("订单号"); // 2
			titles.add("商品编码"); // 5
			titles.add("商品条形码"); // 2
			titles.add("商品名称"); // 3
			titles.add("销售价格"); // 3
			titles.add("订购数量"); // 3
			titles.add("实际数量"); // 3
			titles.add("赠品数量"); // 6
			titles.add("备注"); // 7
			titles.add("门店编码"); // 9
			titles.add("门店名称"); // 8
			titles.add("采购日期"); // 8\
			titles.add("配送"); // 8
			titles.add("状态"); // 8
			map.put("titles", titles);
			List<PageData> listpd=sellingOrderService.dqexcel(pd);
			List<PageData> varList=new ArrayList<PageData>();
			for (int i = 0; i < listpd.size(); i++) {
				PageData pdd=new PageData();
				pdd.put("var1", listpd.get(i).getString("id"));
				pdd.put("var2", listpd.get(i).getString("order_num"));
				pdd.put("var3", listpd.get(i).getString("product_num"));
				pdd.put("var4", listpd.get(i).getString("bar_code"));
				pdd.put("var5", listpd.get(i).getString("product_name"));
				pdd.put("var6", listpd.get(i).getString("product_price"));
				pdd.put("var7", listpd.get(i).getString("quantity"));
				pdd.put("var8", listpd.get(i).getString("final_quantity"));
				pdd.put("var9", listpd.get(i).getString("gift_quantity"));
				pdd.put("var10", listpd.get(i).getString("comment"));
				pdd.put("var11", listpd.get(i).getString("merchant_num"));
				pdd.put("var12", listpd.get(i).getString("merchant_name"));
				pdd.put("var13", listpd.get(i).getString("order_date"));
				if(listpd.get(i).getString("type").equals("1")){
					pdd.put("var14", "加急商品");
				}else{
					pdd.put("var14", "仓库配送");
				}
				if(listpd.get(i).getString("checked_state").equals("1")){
					pdd.put("var15", "未审核");
				}else{
					pdd.put("var15", "已审核");
				}
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
  
  
  @RequestMapping(value = { "/sellingorderdq" })
	public ModelAndView sellingorderdq(Page page) {
		String operationMsg="查询销售管理入库单";
		logBefore(logger, operationMsg);
		ModelAndView mv = new ModelAndView();
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			pd.put("username", LoginUtil.getLoginUser().getUSERNAME());
			String orderdate=pd.getString("order_date");
			if ((orderdate != null) && (!"".equals(orderdate))) {
				String lastLoginEnd = orderdate;
				orderdate = orderdate + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", orderdate);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("checked_state"))) {
				pd.put("checked_state", pd.getString("checked_state"));
			}
			page.setPd(pd);
			List<PageData> varList=sellingOrderService.sellingorderdq(page);
			mv.addObject("varList",varList);
			mv.addObject("pd",pd);
			mv.setViewName("procurement/sellingorder/sellingorderdq_list");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		
		return mv;
	}
  
	// 20161020-修改为只生成销售订单----**begin***************************************
	@RequestMapping(value = "/importExcelOfSellingOrder")
	public ModelAndView importExcelOfSellingOrder(
			@RequestParam(value = "excel", required = false) MultipartFile file) {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "导入采购订单到数据库";
		logBefore(logger, operationMsg);
		try {
			User user = (User) LoginUtil.getLoginUser();
			if (null != file && !file.isEmpty()) {
				String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;
				String fileName = FileUpload.fileUp(file, filePath,"PurchaseOrderExcel");
				// 执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
				List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
				/**
				 * var0 :销售日期  var1 :收银机号  var2 :小票号码  var3 :交易时间  var4 :商品编码  var5:商品名称 
				 * var6 :零售单位 var7 :销售数量 var8 :商品售价 var9 :应收金额 var10:实收金额
				 */
				// 生成采购订单
				List<PageData> pdM=new ArrayList<PageData>();
				List<PageData> pdS=new ArrayList<PageData>();
				
				
				if (listPd != null && listPd.size() > 0) {
					for (int i = 0; i < listPd.size(); i++) {
						Product product=null;
						PageData pdE=new PageData();  //收集错误信息
						PageData pdP=new PageData();  //保存信息
						PageData page=listPd.get(i);
						
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var0").toString())) {
							pdP.put("sell_time",page.get("var0").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "A");
							pdE.put("reason0", "销售日期为空");
						}
						
							pdP.put("cash_register", page.get("var1").toString());
						
						
							pdP.put("small_ticket", page.get("var2").toString());
						
						if(!page.getString("var3").equals("")){
							pdP.put("s_time", page.get("var3").toString());
						}else{
							pdP.put("s_time", pdP.getString("sell_time"));
						}
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var4").toString())&&
								!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var5").toString())) {
							PageData pro=new PageData();
							pro.put("product_name", page.getString("var5"));
							pro.put("product_num", page.getString("var4"));
							product = productService.findById(pro);
							if(product==null){
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row0", "E");
								pdE.put("reason0", "查无该商品信息");
							}else{
								pdP.put("product_id", product.getId());
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "E");
							pdE.put("reason0", "商品编码或商品名称数据为空");
						}
						if(!page.getString("var7").equals("")){
							pdP.put("quantity", page.get("var7").toString());
						}
						if(!page.getString("var8").equals("")){
							pdP.put("sale_price", page.get("var8").toString());
						}
						if(!page.getString("var9").equals("")){
							pdP.put("ammount", page.get("var9").toString());
						}
						if(!page.getString("var10").equals("")){
							pdP.put("final_ammount", page.get("var10").toString());
						}
						pdS.add(pdP);
						if ((pdE != null) && (pdE.getString("line") != null) && (pdE.getString("line") != "")) {
							pdM.add(pdE);
						}
					}
					if(pdM.size()>0){
						pdS.clear();  //清空数组
						mv.addObject("varList",pdM);
					}
					if(pdS.size()>0){
						mv.addObject("errorMsg2", sellingOrderService.saverankings(pdS));
					}
				} else {
					// 表格中数据为空
					mv.addObject("errorMsg2", "数据表中没有数据");
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
	 * 查询销售管理入库单
	 * @param page
	 * @return
	 */
	@RequestMapping(value = { "/sellingRk" })
	public ModelAndView sellingRk(Page page) {
		String operationMsg="查询销售管理入库单";
		logBefore(logger, operationMsg);
		ModelAndView mv = new ModelAndView();
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			String orderdate=pd.getString("order_date");
			if ((orderdate != null) && (!"".equals(orderdate))) {
				String lastLoginEnd = orderdate;
				orderdate = orderdate + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", orderdate);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("checked_state"))) {
				pd.put("checked_state", pd.getString("checked_state"));
			}
			page.setPd(pd);
			List<PageData> varList=enwarehouseorderService.sellingOrderlistPage(page);
			mv.addObject("varList",varList);
			mv.addObject("pd",pd);
			mv.setViewName("procurement/sellingorder/enwarehouseorder_list");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		
		return mv;
	}
	
	/**
	 * 入库单详情
	 * @param page
	 * @param orderId
	 * @param type
	 * @return
	 */
	@RequestMapping({ "goEnwareorderProductEdit" })
	public ModelAndView goEnwareorderProductEdit(Page page, String orderId, String type) {
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));
			List<PageData> productType = this.productTypeService.findByParentId(pd);
			pd = this.enwarehouseorderService.getEnwarouseById(pd);
			List<PageData> pageDate = this.eNOrderItemService.getOrderItemlistPageProductById(page);
			mv.addObject("productType", productType);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("type", type);
			mv.setViewName("procurement/sellingorder/enwarehouseorder_product_edit");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	@RequestMapping(value={"/sellingRkSh"},produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String sellingRkSh(){
		String operationMsg="开始审核入库";
		String result="";
		PageData pd=new PageData();
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String DATA_IDS=pd.getString("DATA_IDS");
			if(DATA_IDS!=null&& !DATA_IDS.equals("")){
				String Ids[]=DATA_IDS.split(",");
				result=sellingOrderService.xsExamine(Ids);
				if(result.equals("true")){
					logMidway(logger, "审核成功");
				}else{
					logMidway(logger,operationMsg+ "失败");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.logger.error(e.toString(),e);
			result = e.toString();
		}
		return result;
	}
	/**
	 * 新增入库单
	 * @param page
	 * @param page
	 * @return
	 */
	@RequestMapping("goEnwareorderProduct")
	public ModelAndView goEnwareorderProduct(Page page){
		ModelAndView mv=new ModelAndView();
		String operationMsg = "新增入库单页面";
		logBefore(logger, operationMsg);
		try {
			PageData pd=new PageData();
			pd=this.getPageData();
			String order_num="RK_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+StringUtil.getFixLenthString(6);
			pd.put("order_num", order_num);
			mv.addObject("pd",pd);
			mv.setViewName("procurement/sellingorder/enwarehouseorder_product_add");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}
		return mv;
	}
	
	@RequestMapping(value = "/saveenorderProduct")
	@ResponseBody
	public Object saveenorderProduct() {
		PageData pd = new PageData();
		String operationMsg = "手动添加入库单商品信息";
		logBefore(logger, operationMsg);
		try {
			pd = this.getPageData();
			pd.put("creator", LoginUtil.getLoginUser().getUSERNAME());
			pd.put("create_time",com.hy.util.DateUtil.getAfterDayDate("0"));
			String group_num =sellingOrderService.findGroupNum();
			pd.put("group_num",group_num);
			sellingOrderService.saveenorder(pd);
			//sellingOrderService.saveenorderitem(pd);
			sysLogService.saveLog(operationMsg, "成功");
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return "2";
	}
	
	
  
  @RequestMapping({"/save"})
  public ModelAndView save()
    throws Exception
  {
    logBefore(this.logger, "新增PurchaseOrder");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    pd.put("group_num", StringUtil.getStringOfMillisecond(""));
    pd.put("order_num", "SO_" + StringUtil.getStringOfMillisecond(""));
    pd.put("checked_state", "1");
    pd.put("is_printed", "2");
    pd.put("create_time", Tools.date2Str(new Date()));
    pd.put("order_date", Tools.date2Str(new Date()));
    this.sellingOrderService.save(pd);
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    this.sysLogService.saveLog("新增PurchaseOrder", "成功");
    return mv;
  }
  
  @RequestMapping({"/delete"})
  public void delete(PrintWriter out)
  {
    logBefore(this.logger, "删除PurchaseOrder");
    PageData pd = new PageData();
    try {
      pd = getPageData();
      this.sellingOrderService.delete(pd);
      out.write("success");
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/edit"})
  public ModelAndView edit()
    throws Exception
  {
    logBefore(this.logger, "修改PurchaseOrder");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    this.sellingOrderService.edit(pd);
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    this.sysLogService.saveLog("修改PurchaseOrder", "成功");
    return mv;
  }
  
  @RequestMapping({"/sellingorderlist"})
  public ModelAndView list(Page page)
  {
    logBefore(this.logger, "列表PurchaseOrder");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
      
      String lastLoginStart = pd.getString("lastLoginStart");
      String lastLoginEnd = pd.getString("lastLoginEnd");
      
      if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
        lastLoginStart = lastLoginStart + " 00:00:00";
        pd.put("lastLoginStart", lastLoginStart);
      }
      if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
        lastLoginEnd = lastLoginEnd + " 00:00:00";
        pd.put("lastLoginEnd", lastLoginEnd);
      }
      page.setPd(pd);
      List<PageData> varList = this.sellingOrderService.listPdPagePurchaseOrder(page);
      mv.setViewName("procurement/sellingorder/sellingorder_list");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
 /**
  * 获取销售订单列表
  * @param page
  * @param request 
  * @return
  */
  @RequestMapping({"/findsellingorderlist"})
  public ModelAndView findsellingorderlist(Page page,HttpServletRequest request)
  {
    logBefore(this.logger, "列表PurchaseOrder");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      List<PageData> areaList = this.merchantService.areaList(pd);
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))){
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
     /* String checked_state = pd.getString("state");*/
      String lastLoginStart = pd.getString("lastLoginStart");
      String lastLoginEnd = pd.getString("lastLoginEnd");
      /*if(checked_state != null && !checked_state.equals("")){
    	  pd.put("checked_state", checked_state);
      }*/
      if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
        lastLoginStart = lastLoginStart + " 00:00:00";
        pd.put("lastLoginStart", lastLoginStart);
      }
      if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
        lastLoginEnd = lastLoginEnd + " 00:00:00";
        pd.put("lastLoginEnd", lastLoginEnd);
      }
      String ordertype = request.getParameter("ordertype");
      if(ordertype != null && !ordertype.equals("")){
    	  pd.put("ordertype", ordertype);
      }
      if (!StringUtil.isEmpty(pd.getString("cityid"))) {
			String city = pd.getString("cityid");
			pd.put("cityid", city);
		}
      page.setPd(pd);
      List<PageData> varList = this.sellingOrderService.listPdPagePurchaseOrder(page);
    /*  String state = pd.getString("checked_state");*/
      mv.addObject("area", areaList);
    /*  pd.put("state", state);*/
      mv.setViewName("procurement/sellingorder/tosellingorderlist");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goAdd"})
  public ModelAndView goAdd(Page page)
  {
    logBefore(this.logger, "去新增PurchaseOrder页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
    try
    {
      mv.setViewName("procurement/sellingorder/sellingorder_edit");
      mv.addObject("msg", "save");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goEdit"})
  public ModelAndView goEdit(Page page)
  {
    logBefore(this.logger, "去修改PurchaseOrder页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      PurchaseOrder purchaseOrder = this.sellingOrderService.findById(pd);
      mv.setViewName("procurement/sellingorder/sellingorder_edit");
      mv.addObject("msg", "edit");
      mv.addObject("pd", purchaseOrder);
    }
    catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/deleteAll"})
  @ResponseBody
  public Object deleteAll()
  {
    logBefore(this.logger, "批量删除PurchaseOrder");
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      pd = getPageData();
      List<PageData> pdList = new ArrayList();
      String DATA_IDS = pd.getString("DATA_IDS");
      if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
        String[] ArrayDATA_IDS = DATA_IDS.split(",");
        this.sellingOrderService.deleteAll(ArrayDATA_IDS);
        pd.put("msg", "ok");
        this.sysLogService.saveLog("批量删除PurchaseOrder", "成功");
      } else {
        pd.put("msg", "no");
        this.sysLogService.saveLog("批量删除PurchaseOrder", "失败");
      }
      pdList.add(pd);
      map.put("list", pdList);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    } finally {
      logAfter(this.logger);
    }
    return AppUtil.returnObject(pd, map);
  }
  
  @RequestMapping({"/goUploadExcel"})
  public ModelAndView goUploadExcel()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("procurement/sellingorder/uploadExcel");
    return mv;
  }
  
  @RequestMapping({"/goImportExcelPage"})
  public ModelAndView goImportExcelPage()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("procurement/ordergroup/importExcelPage");
    return mv;
  }
  
  @RequestMapping({"/goImportExcelPageForGift"})
  public ModelAndView goImportExcelPageForGift()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("procurement/ordergroup/importExcelPageForGift");
    return mv;
  }
  
  @RequestMapping({"/downExcel"})
  public void downExcel(HttpServletResponse response)
    throws Exception
  {
    FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "SellingOrder.xlsx", "Tarot系统-导入门店订单模版.xls");
  }
  @RequestMapping({"/downXlsxExcel"})
  public void downXlsxExcel(HttpServletResponse response)
    throws Exception
  {
    FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "SellingOrder.xlsx", "Tarot系统-导入门店订单模版.xlsx");
  }
  
  @RequestMapping({"/readExcel"})
  public ModelAndView readExcel(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    User user = LoginUtil.getLoginUser();
    if ((file != null) && (!file.isEmpty()))
    {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";

      String fileName = FileUpload.fileUp(file, filePath, "sellingOrderExcel");
      List<PageData> listPd = (List)ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
      
      try {
    	  if ((listPd != null) && (listPd.size() > 0)) {
    		  mv.addObject("errorMsg",sellingOrderService.readExcel(listPd));
          }else{
            mv.addObject("errorMsg", "数据表中没有数据");
          }
      } catch (Exception e) {
    	  mv.addObject("errorMsg",e.toString());
      }
    }
    mv.setViewName("procurement/ordergroup/importExcelPage");
    return mv;
  }
  @RequestMapping({"/readExcel2"})
  public ModelAndView readExcel2(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    User user = LoginUtil.getLoginUser();
    
    if ((file != null) && (!file.isEmpty()))
    {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";

      String fileName = FileUpload.fileUp(file, filePath, "sellingOrderExcel");
      List<PageData> listPd = (List)ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
      
      if ((listPd != null) && (listPd.size() > 0)) {
    	  
        List<ENOrder> listENOrder = new ArrayList();
        List<ENOrderItem> listENOrderItem = new ArrayList();
        StringBuffer commentOfPoi = new StringBuffer();
        
        ENOrder eno = null;
        
        ENOrderItem enoi = null;
        
        Supplier supplier = null;
        
        Merchant merchant = null;
        
        EXOrder exo = null;
        List<EXOrder> listEXOrder = new ArrayList();
        
        EXOrderItem exoi = null;
        List<EXOrderItem> listEXOrderItem = new ArrayList();
        
        PageData pdE = null;
        List<PageData> pdEList = new ArrayList();
        
        Product product = null;
        List<Product> listProduct = new ArrayList();
        boolean pflag = false;
        
        List<PageData> pdPriceList = new ArrayList();
        PageData pdPrice = null;
        
        PageData pdM = null;
        
        PageData pds = new PageData();
        
        String groupNum = "GP_" + StringUtil.getStringOfMillisecond("");
        PageData pdOfOrderGroup = new PageData();
        OrderGroup og = new OrderGroup();
        og.setOrderGroupNum(groupNum);
        og.setUser(user);
        og.setCkId(user.getCkId());
        og.setGroupType(1);
        int k = -1;
        int u = -1;
        double quantityOfProduct = 0.0D;
        double priceOfProduct1 = 0.0D;
        double priceOfProduct2 = 0.0D;
        double skuVolumeEx = 0.0D;
        double skuWeightEx = 0.0D;
        String comment = "";
        String zyOrderNum = "";
        String contactPersonM = "";
        String contactPersonMobileM = "";
        String contactPersonAddressM = "";
        StringBuffer rowMes = null;
        StringBuffer reasonMes = null;
        
        for (int i = 0; i < listPd.size(); i++) {
        	
          merchant = new Merchant();
          product = new Product();
          pdE = new PageData();
          pdM = new PageData();
          exo = new EXOrder();
          exoi = new EXOrderItem();
          k = -1;
          u = -1;
          rowMes = new StringBuffer();
          reasonMes = new StringBuffer();
          if(listPd.get(i).getString("var0").trim().equals("")){
	      	  pdE.put("line", Integer.valueOf(i + 2));
	      	  pdE.put("reason", "数据表中有空行");
	      	  pdEList.add(pdE);
	      	  mv.addObject("varList", pdEList);
    		  mv.setViewName("procurement/ordergroup/importExcelPage");
    		  return mv;
    	  }
          if (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var2").toString())) {
            pdM.put("id", ((PageData)listPd.get(i)).get("var2").toString());

            pds = this.merchantService.findById(pdM);
            if (pds == null || pds.size() == 0) {
				//System.out.println("aa");
			}
            pdM.put("id", "");
            if ((pds != null) && (pds.size() > 0)) {
              if (!StringUtil.isEmpty(pds.getString("contact_person"))) {
                contactPersonM = pds.getString("contact_person");
              }
              if (!StringUtil.isEmpty(pds.getString("mobile"))) {
                contactPersonMobileM = pds.getString("mobile");
              }
              if (!StringUtil.isEmpty(pds.getString("address"))) {
                contactPersonAddressM = pds.getString("address");
              }
              
              if (!StringUtil.isEmpty(pds.get("id").toString()))
              {
                merchant.setId(Long.parseLong(pds.get("id").toString()));
                
                if ((listEXOrder != null) && (listEXOrder.size() > 0)) {
                  for (int m = 0; m < listEXOrder.size(); m++) {
                    if (((EXOrder)listEXOrder.get(m)).getMerchant().getId() == Long.parseLong(pds.get("id").toString()))
                    {
                      k = m;
                      
                      exo = (EXOrder)listEXOrder.get(k);
                      break;
                    }
                  }
                }
              }
              else {
                pdE.put("line", Integer.valueOf(i + 2));
                pdE.put("rowS", "C");
                pdE.put("reasonS", "商户便利店信息不存在");
              }
            }
            else {
              pdE.put("line", Integer.valueOf(i + 2));
              pdE.put("rowS", "C");
              pdE.put("reasonS", "商户便利店信息不存在");
            }
          }
          else {
            pdE.put("line", Integer.valueOf(i + 2));
            pdE.put("rowS", "C");
            pdE.put("reasonS", "商户便利店信息数据为空");
          }
          
          if ((!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var10").toString())) && 
            (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var8").toString()))) {
            pdM.put("barCode", ((PageData)listPd.get(i)).get("var10").toString());
            //pdM.put("hostCode", ((PageData)listPd.get(i)).get("var8").toString());
            product = this.productService.findById(pdM);

            if (product != null)
            {
              if (!StringUtil.isEmpty(product.getSkuVolume())) {
                skuVolumeEx = Double.parseDouble(product.getSkuVolume());
              }
              if (!StringUtil.isEmpty(product.getSkuWeight())) {
                skuWeightEx = Double.parseDouble(product.getSkuWeight());
              }
              pdPrice = new PageData();
              pdPrice.put("product_id", Long.valueOf(product.getId()));
              pdPrice.put("price_type", Integer.valueOf(1));
              
              pdPriceList = this.productService.findPriceOfProductList(pdPrice);
              pdPrice = (PageData)pdPriceList.get(0);
              
              if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
                priceOfProduct1 = Double.parseDouble(pdPrice.getString("product_price").toString());
                pflag = false;
                if ((listProduct != null) && (listProduct.size() > 0)) {
                  for (int pi = 0; pi < listProduct.size(); pi++) {
                    if (product.getId() == ((Product)listProduct.get(pi)).getId()) {
                      pflag = true;
                      break;
                    }
                  }
                }
                pdPrice.put("product_id", Long.valueOf(product.getId()));
                pdPrice.put("price_type", Integer.valueOf(2));
                pdPriceList = this.productService.findPrice2OfProductList(pdPrice);
                if (CollectionUtils.isEmpty(pdPriceList)) {
                	pdPrice = null;
				}else {
					pdPrice = (PageData)pdPriceList.get(0);
				}

                if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
                  priceOfProduct2 = Double.parseDouble(pdPrice.getString("product_price").toString());
                }
                if (!pflag) {
                  listProduct.add(product);
                }
                if (k > -1)
                {
                  for (int ex = 0; ex < listEXOrderItem.size(); ex++) {
                    if ((exo.getOrderNum() == ((EXOrderItem)listEXOrderItem.get(ex)).getOrderNum()) && 
                      (((EXOrderItem)listEXOrderItem.get(ex)).getProduct().getId() == product.getId()))
                    {
                      u = ex;
                      break;
                    }
                  }
                }
              }
              else {
                pdE.put("line", Integer.valueOf(i + 2));
                pdE.put("rowP", "I和K");
                pdE.put("reasonP", "商品不存在");
              }
            }
            else
            {
              pdE.put("line", Integer.valueOf(i + 2));
              pdE.put("rowP", "I和K");
              pdE.put("reasonP", "商品不存在");
            }
          } else {
            pdE.put("line", Integer.valueOf(i + 2));
            pdE.put("rowP", "I和K");
            pdE.put("reasonP", "商品数据为空");
          }
          
          if ((((PageData)listPd.get(i)).get("var17") != null) && (((PageData)listPd.get(i)).get("var17") != "") && (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var17").toString()))) {
            quantityOfProduct = Double.parseDouble(((PageData)listPd.get(i)).get("var17").toString());
            if (u > -1) {
              quantityOfProduct += ((EXOrderItem)listEXOrderItem.get(u)).getQuantity();
            }
          } else {
            pdE.put("line", Integer.valueOf(i + 2));
            pdE.put("rowC", "R");
            pdE.put("reasonC", "商品订购数量为空");
          }
          
          if (((PageData)listPd.get(i)).get("var20") != null) {
            comment = "";
            
            if (k > -1) {
              comment = ((EXOrder)listEXOrder.get(k)).getComment() + ((PageData)listPd.get(i)).get("var20").toString();
            } else {
              comment = ((PageData)listPd.get(i)).get("var20").toString();
            }
          }
          
          if (((PageData)listPd.get(i)).get("var21") != null) {
            zyOrderNum = ((PageData)listPd.get(i)).get("var21").toString();
          }
          
          if (!StringUtil.isEmpty(pdE.getString("rowP"))) {
            rowMes.append(pdE.getString("rowP") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("rowC"))) {
            rowMes.append(pdE.getString("rowC") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("rowS"))) {
            rowMes.append(pdE.getString("rowS") + "；");
          }
          if (!StringUtil.isEmpty(pdE.getString("reasonP"))) {
            reasonMes.append(pdE.getString("reasonP") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("reasonC"))) {
            reasonMes.append(pdE.getString("reasonC") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("reasonS"))) {
            reasonMes.append(pdE.getString("reasonS") + "；");
          }
          
          if ((pdE != null) && (pdE.getString("line") != null) && (pdE.getString("line") != "")) {
            pdE.put("row", rowMes);
            pdE.put("reason", reasonMes);
            
            pdEList.add(pdE);
          } else {
            if (k == -1) {
              exo.setGroupNum(groupNum);
              exo.setOrderNum("CK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
              exo.setCheckedState(1);
              exo.setOrderDate(Tools.date2Str(new Date()));
              exo.setManagerName(contactPersonM);
              exo.setManagerTel(contactPersonMobileM);
              exo.setDeliverAddress(contactPersonAddressM);
              exo.setIs_ivt_order_print(0);
              exo.setIs_temporary(2);
              exo.setUser(user);
              exo.setMerchant(merchant);
              exo.setOrderType(1);
              exo.setComment(comment);
              exo.setCkId(user.getCkId());
            }
            if (u == -1) {
              exoi.setGroupNum(exo.getGroupNum());
              exoi.setOrderNum(exo.getOrderNum());
              
              exoi.setPurchasePrice(priceOfProduct1);
              exoi.setSalePrice(priceOfProduct2);
              exoi.seteXTime(DateUtil.getAfterDayDate("3"));
              exoi.setProduct(product);
              exoi.setCreator(user.getNAME());
              exoi.setZyOrderNum(zyOrderNum);
              if (((PageData)listPd.get(i)).get("var20") != null) {
                exoi.setComment(((PageData)listPd.get(i)).get("var20").toString());
              }
              exoi.setFc_order_num(((PageData)listPd.get(i)).getString("var1"));
              exoi.setProduct(product);
              exoi.setQuantity(quantityOfProduct);
              exoi.setFinalQuantity(quantityOfProduct);
              listEXOrderItem.add(exoi);
            }
            if (u > -1) {
              ((EXOrderItem)listEXOrderItem.get(u)).setQuantity(quantityOfProduct);
              ((EXOrderItem)listEXOrderItem.get(u)).setFinalQuantity(quantityOfProduct);
              if (((PageData)listPd.get(i)).get("var20") != null) {
                ((EXOrderItem)listEXOrderItem.get(u)).setComment(((EXOrderItem)listEXOrderItem.get(u)).getComment() + ";" + ((PageData)listPd.get(i)).get("var20").toString());
              }
              if (((PageData)listPd.get(i)).get("var21") != null) {
                ((EXOrderItem)listEXOrderItem.get(u)).setZyOrderNum(((EXOrderItem)listEXOrderItem.get(u)).getZyOrderNum() + ";" + ((PageData)listPd.get(i)).get("var21").toString());
              }
            }
            if (k > -1)
            {
              ((EXOrder)listEXOrder.get(k)).setAmount(MathUtil.mulAndAdd(Double.parseDouble(((PageData)listPd.get(i)).get("var17").toString()), 
                priceOfProduct2, ((EXOrder)listEXOrder.get(k)).getFinalAmount()));
              ((EXOrder)listEXOrder.get(k)).setFinalAmount(((EXOrder)listEXOrder.get(k)).getAmount());
              ((EXOrder)listEXOrder.get(k)).setTotalSvolume(MathUtil.mulAndAdd(quantityOfProduct, skuVolumeEx, ((EXOrder)listEXOrder.get(k)).getTotalSvolume()));
              ((EXOrder)listEXOrder.get(k)).setTotalWeight(MathUtil.mulAndAdd(quantityOfProduct, skuWeightEx, ((EXOrder)listEXOrder.get(k)).getTotalWeight()));
              ((EXOrder)listEXOrder.get(k)).setComment(comment);
            }
            if (k == -1)
            {
              exo.setFinalAmount(MathUtil.mul(exoi.getQuantity(), exoi.getSalePrice()));
              exo.setAmount(MathUtil.mul(exoi.getQuantity(), exoi.getSalePrice()));
              
              exo.setTotalSvolume(MathUtil.mul(exoi.getQuantity(), skuVolumeEx));
              exo.setTotalWeight(MathUtil.mul(exoi.getQuantity(), skuWeightEx));
              listEXOrder.add(exo);
            }
          }
        }
        if ((pdEList != null) && (pdEList.size() > 0) && (pdEList.get(0) != null) && (!StringUtil.isEmpty(((PageData)pdEList.get(0)).getString("line")))) {
          mv.addObject("varList", pdEList);
        }
        if ((pdEList == null) || (pdEList.size() <= 0) || (StringUtil.isEmpty(((PageData)pdEList.get(0)).getString("line")))) {
          pdEList = null;
        }
        
        if (pdEList == null) {
          double totalQuantity = 0.0D;
          double purPrice = 0.0D;
          PageData pdP = null;
          int enk = -1;
          double productQuantity = 0.0D;
          double productPrice = 0.0D;
          String contactPerson = "";
          String contactPersonMobile = "";
          double skuWeightEn = 0.0D;
          double skVolumeEn = 0.0D;
          if ((listProduct != null) && (listProduct.size() > 0) && 
            (listEXOrderItem != null) && (listEXOrderItem.size() > 0)) {
            for (int pi = 0; pi < listProduct.size(); pi++) {
              supplier = new Supplier();
              eno = new ENOrder();
              enoi = new ENOrderItem();
              pdP = new PageData();
              totalQuantity = 0.0D;
              commentOfPoi = new StringBuffer();
              for (int exoii = 0; exoii < listEXOrderItem.size(); exoii++) {
                if (((Product)listProduct.get(pi)).getId() == ((EXOrderItem)listEXOrderItem.get(exoii)).getProduct().getId()) {
                  totalQuantity += ((EXOrderItem)listEXOrderItem.get(exoii)).getQuantity();
                  purPrice = ((EXOrderItem)listEXOrderItem.get(exoii)).getPurchasePrice();
                }
              }
              
              pdP = new PageData();
              pdP = this.productService.findProductInfoById(((Product)listProduct.get(pi)).getId());
              
              enk = -1;
              if ((listENOrder != null) && (listENOrder.size() > 0)) {
                for (int en = 0; en < listENOrder.size(); en++) {
                  if ((((ENOrder)listENOrder.get(en)).getSupplier().getId() == 42873290L) && (pdP == null)) {
                    //System.out.println("");
                  }
                  if (Long.parseLong(pdP.getString("tsId")) == ((ENOrder)listENOrder.get(en)).getSupplier().getId())
                  {
                    eno = (ENOrder)listENOrder.get(en);
                    enk = en;
                    break;
                  }
                }
              }
              if (!StringUtil.isEmpty(pdP.getString("product_quantity"))) {
                productQuantity = Double.parseDouble(pdP.getString("product_quantity"));
              }
              if (!StringUtil.isEmpty(pdP.getString("product_price"))) {
                productPrice = Double.parseDouble(pdP.getString("product_price"));
              }
              if (!StringUtil.isEmpty(pdP.getString("contact_person"))) {
                contactPerson = pdP.getString("contact_person");
              }
              if (!StringUtil.isEmpty(pdP.getString("contact_person_mobile"))) {
                contactPersonMobile = pdP.getString("contact_person_mobile");
              }
              if (!StringUtil.isEmpty(pdP.getString("sku_weight"))) {
                skuWeightEn = Double.parseDouble(pdP.getString("sku_weight"));
              }
              if (!StringUtil.isEmpty(pdP.getString("sku_volume"))) {
                skVolumeEn = Double.parseDouble(pdP.getString("sku_volume"));
              }
              
              if (enk == -1) {
                supplier.setId(Long.parseLong(pdP.getString("tsId")));
                eno.setGroupNum(groupNum);
                eno.setOrderNum("RK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
                eno.setOrderDate(Tools.date2Str(new Date()));
                eno.setCheckedState(1);
                eno.setManagerName(contactPerson);
                eno.setManagerTel(contactPersonMobile);
                eno.setPaidAmount(0.0D);
                eno.setIsIvtOrderPrint(0);
                eno.setIsTemporary(2);
                eno.setSupplier(supplier);
                eno.setUser(user);
                eno.setTotalSvolume(MathUtil.mul(skVolumeEn, totalQuantity));
                eno.setTotalWeight(MathUtil.mul(skuWeightEn, totalQuantity));
                eno.setOrderType(1);
                eno.setCkId(user.getCkId());
              }
              
              enoi.setGroupNum(groupNum);
              enoi.setOrderNum(eno.getOrderNum());
              enoi.setPurchasePrice(purPrice);
              enoi.setQuantity(totalQuantity);
              enoi.setFinalQuantity(totalQuantity);
              enoi.setIsSplitIvt(2);
              enoi.setSvolume(MathUtil.mul(skVolumeEn, totalQuantity)+"");
              enoi.setWeight(MathUtil.mul(skuWeightEn, totalQuantity)+"");
              enoi.setIsIvtBK(1);
              enoi.seteNTime(DateUtil.getAfterDayDate("3"));
              enoi.setCreator(user.getNAME());
              
              enoi.setProduct((Product)listProduct.get(pi));
              listENOrderItem.add(enoi);
              if (enk > -1)
              {
                ((ENOrder)listENOrder.get(enk)).setFinalAmount(MathUtil.mulAndAdd(totalQuantity, purPrice, ((ENOrder)listENOrder.get(enk)).getFinalAmount()));
                ((ENOrder)listENOrder.get(enk)).setAmount(MathUtil.mulAndAdd(totalQuantity, purPrice, ((ENOrder)listENOrder.get(enk)).getFinalAmount()));
                ((ENOrder)listENOrder.get(enk)).setTotalSvolume(MathUtil.add(((ENOrder)listENOrder.get(enk)).getTotalSvolume(), Double.parseDouble(enoi.getSvolume())));
                ((ENOrder)listENOrder.get(enk)).setTotalWeight(MathUtil.add(((ENOrder)listENOrder.get(enk)).getTotalWeight(), Double.parseDouble(enoi.getWeight())));
              }
              if (enk == -1)
              {
                eno.setAmount(MathUtil.mul(enoi.getPurchasePrice(), totalQuantity));
                eno.setFinalAmount(MathUtil.mul(enoi.getPurchasePrice(), totalQuantity));
                listENOrder.add(eno);
              }
            }
          }
        }
        
        int saveFlag = 1;
        int reFlag = 0;
        try {
          if ((listENOrder != null) && (listENOrder.size() > 0))
          {
            logMidway(this.logger, "入库单数组大小为：" + listENOrder.size());
            reFlag = this.eNOrderService.saveList(listENOrder);
            saveFlag = 2;
            if ((listENOrderItem != null) && (listENOrderItem.size() > 0))
            {
              reFlag = this.eNOrderItemService.saveList(listENOrderItem);
              saveFlag = 3;
              if ((listEXOrder != null) && (listEXOrder.size() > 0))
              {
                reFlag = this.eXOrderService.saveList(listEXOrder);
                saveFlag = 4;
                if ((listEXOrderItem != null) && (listEXOrderItem.size() > 0))
                {
                  reFlag = this.eXOrderItemService.saveList(listEXOrderItem);
                  saveFlag = 5;
                  if (pdOfOrderGroup != null)
                  {
                    reFlag = this.orderGroupService.saveOrderGroup(og);
                    saveFlag = 1;
                  }
                }
              }
            }
          }
        } catch (Exception e) {
          mv.addObject("errorMsg", "操作有误，请稍后再试");
          logMidway(this.logger, e.toString());
        }
        
        if (saveFlag > 1) {
          this.eNOrderService.deleteList(listENOrder);
        }
        if (saveFlag > 2) {
          this.eNOrderItemService.deleteList(listENOrderItem);
        }
        if (saveFlag > 3) {
          this.eXOrderService.deleteList(listEXOrder);
        }
        if (saveFlag > 4) {
          this.eXOrderItemService.deleteList(listEXOrderItem);
        }
        logMidway(this.logger, "-导入入库单出库单，返回状态为-saveFlag=" + saveFlag);
        
        listENOrder = null;
        listENOrderItem = null;
        commentOfPoi = null;
        eno = null;
        enoi = null;
        supplier = null;
        merchant = null;
        exo = null;
        listEXOrder = null;
        exoi = null;
        listEXOrderItem = null;
        pdE = null;
        product = null;
        listProduct = null;
        pdPrice = null;
        pdM = null;
        pds = null;
        if ((saveFlag == 1) && (pdEList == null)) {
          mv.addObject("errorMsg", "数据导入成功。");
          mv.addObject("msg", "success");
        } else {
          mv.addObject("errorMsg", "请检查导入Excel表格式或数据是否为空后重新操作。");
        }
      }
      else
      {
        mv.addObject("errorMsg", "数据表中没有数据");
      }
    }
    mv.setViewName("procurement/ordergroup/importExcelPage");
    return mv;
  }
  
  
  
  
  /** 赠品导入***/
  @RequestMapping({"/readExcelForGift"})
  public ModelAndView readExcelForGift(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    User user = LoginUtil.getLoginUser();
    
    if ((file != null) && (!file.isEmpty()))
    {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
      
      String fileName = FileUpload.fileUp(file, filePath, "sellingOrderExcel");
      
      List<PageData> listPd = (List)ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
      
      if ((listPd != null) && (listPd.size() > 0)) {
        List<ENOrder> listENOrder = new ArrayList();
        List<ENOrderItem> listENOrderItem = new ArrayList();
        StringBuffer commentOfPoi = new StringBuffer();
        
        ENOrder eno = null;
        
        ENOrderItem enoi = null;
        
        Supplier supplier = null;
        
        Merchant merchant = null;
        
        EXOrder exo = null;
        List<EXOrder> listEXOrder = new ArrayList();
        
        EXOrderItem exoi = null;
        List<EXOrderItem> listEXOrderItem = new ArrayList();
        
        PageData pdE = null;
        List<PageData> pdEList = new ArrayList();
        
        Product product = null;
        List<Product> listProduct = new ArrayList();
        boolean pflag = false;
        
        List<PageData> pdPriceList = new ArrayList();
        PageData pdPrice = null;
        
        PageData pdM = null;
        
        List<PageData> pds = new ArrayList();
        
        String groupNum = "GP_" + StringUtil.getStringOfMillisecond("");
        PageData pdOfOrderGroup = new PageData();
        OrderGroup og = new OrderGroup();
        og.setOrderGroupNum(groupNum);
        og.setUser(user);
        og.setGroupType(6);
        int k = -1;
        int u = -1;
        double quantityOfProduct = 0.0D;
        double priceOfProduct1 = 0.0D;
        double priceOfProduct2 = 0.0D;
        String comment = "";
        String zyOrderNum = "";
        String contactPersonM = "";
        String contactPersonMobileM = "";
        String contactPersonAddressM = "";
        StringBuffer rowMes = null;
        StringBuffer reasonMes = null;
        
        for (int i = 0; i < listPd.size(); i++) {
          merchant = new Merchant();
          product = new Product();
          pdE = new PageData();
          pdM = new PageData();
          exo = new EXOrder();
          exoi = new EXOrderItem();
          k = -1;
          u = -1;
          rowMes = new StringBuffer();
          reasonMes = new StringBuffer();
          
          if (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var6").toString())) {
            pdM.put("merchant_name", ((PageData)listPd.get(i)).get("var6").toString());
            
            pds = this.merchantService.findByName(pdM);
            if (pds == null || pds.size() == 0) {
				//System.out.println("aa");
			}
            if ((pds != null) && (pds.size() > 0)) {
              if (!StringUtil.isEmpty(((PageData)pds.get(0)).getString("contact_person"))) {
                contactPersonM = ((PageData)pds.get(0)).getString("contact_person");
              }
              if (!StringUtil.isEmpty(((PageData)pds.get(0)).getString("mobile"))) {
                contactPersonMobileM = ((PageData)pds.get(0)).getString("mobile");
              }
              if (!StringUtil.isEmpty(((PageData)pds.get(0)).getString("address"))) {
                contactPersonAddressM = ((PageData)pds.get(0)).getString("address");
              }
              
              if (!StringUtil.isEmpty(((PageData)pds.get(0)).get("id").toString()))
              {
                merchant.setId(Long.parseLong(((PageData)pds.get(0)).get("id").toString()));
                
                if ((listEXOrder != null) && (listEXOrder.size() > 0)) {
                  for (int m = 0; m < listEXOrder.size(); m++) {
                    if (((EXOrder)listEXOrder.get(m)).getMerchant().getId() == Long.parseLong(((PageData)pds.get(0)).get("id").toString()))
                    {
                      k = m;
                      
                      exo = (EXOrder)listEXOrder.get(k);
                      break;
                    }
                  }
                }
              }
              else {
                pdE.put("line", Integer.valueOf(i + 2));
                pdE.put("rowS", Integer.valueOf(6));
                pdE.put("reasonS", "商户便利店信息不存在");
              }
            }
            else {
              pdE.put("line", Integer.valueOf(i + 2));
              pdE.put("rowS", Integer.valueOf(6));
              pdE.put("reasonS", "商户便利店信息不存在");
            }
          }
          else {
            pdE.put("line", Integer.valueOf(i + 2));
            pdE.put("rowS", Integer.valueOf(6));
            pdE.put("reasonS", "商户便利店信息数据为空");
          }
          
          if (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var0").toString())) {
            pdM.put("barCode", ((PageData)listPd.get(i)).get("var0").toString());
            product = this.productService.findById(pdM);
            if (product != null) {
              pdPrice = new PageData();
              pdPrice.put("product_id", Long.valueOf(product.getId()));
              pdPrice.put("price_type", Integer.valueOf(1));
              pdPriceList = this.productService.findPriceOfProductList(pdPrice);
              pdPrice = (PageData)pdPriceList.get(0);
              
              if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
                priceOfProduct1 = Double.parseDouble(pdPrice.getString("product_price").toString());
                pflag = false;
                if ((listProduct != null) && (listProduct.size() > 0)) {
                  for (int pi = 0; pi < listProduct.size(); pi++) {
                    if (product.getId() == ((Product)listProduct.get(pi)).getId()) {
                      pflag = true;
                      break;
                    }
                  }
                }
                pdPrice.put("product_id", Long.valueOf(product.getId()));
                pdPrice.put("price_type", Integer.valueOf(2));
                pdPriceList = this.productService.findPriceOfProductList(pdPrice);
                pdPrice = (PageData)pdPriceList.get(0);
                if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
                  priceOfProduct2 = Double.parseDouble(pdPrice.getString("product_price").toString());
                }
                if (!pflag) {
                  listProduct.add(product);
                }
                if (k > -1)
                {
                  for (int ex = 0; ex < listEXOrderItem.size(); ex++) {
                    if ((exo.getOrderNum() == ((EXOrderItem)listEXOrderItem.get(ex)).getOrderNum()) && 
                      (((EXOrderItem)listEXOrderItem.get(ex)).getProduct().getId() == product.getId()))
                    {
                      u = ex;
                      break;
                    }
                  }
                }
              }
              else {
                pdE.put("line", Integer.valueOf(i + 2));
                pdE.put("rowP", Integer.valueOf(1));
                pdE.put("reasonP", "商品不存在");
              }
            }
            else {
              pdE.put("line", Integer.valueOf(i + 2));
              pdE.put("rowP", Integer.valueOf(1));
              pdE.put("reasonP", "商品不存在");
            }
          } else {
            pdE.put("line", Integer.valueOf(i + 2));
            pdE.put("rowP", Integer.valueOf(1));
            pdE.put("reasonP", "商品数据为空");
          }
          //商品数量
          if ((((PageData)listPd.get(i)).get("var4") != null) && (((PageData)listPd.get(i)).get("var4") != "") && (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var4").toString()))) {
            quantityOfProduct = Double.parseDouble(((PageData)listPd.get(i)).get("var4").toString());
            if (u > -1) {
              quantityOfProduct += ((EXOrderItem)listEXOrderItem.get(u)).getQuantity();
            }
          } else {
            pdE.put("line", Integer.valueOf(i + 2));
            pdE.put("rowC", Integer.valueOf(4));
            pdE.put("reasonC", "商品订购数量为空");
          }
          
          if (((PageData)listPd.get(i)).get("var7") != null) {
            comment = "";
            
            if (k > -1) {
              comment = ((EXOrder)listEXOrder.get(k)).getComment() + ((PageData)listPd.get(i)).get("var7").toString();
            } else {
              comment = ((PageData)listPd.get(i)).get("var7").toString();
            }
          }
          
          if (((PageData)listPd.get(i)).get("var8") != null) {
            zyOrderNum = ((PageData)listPd.get(i)).get("var8").toString();
          }
          
          if (!StringUtil.isEmpty(pdE.getString("rowP"))) {
            rowMes.append(pdE.getString("rowP") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("rowC"))) {
            rowMes.append(pdE.getString("rowC") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("rowS"))) {
            rowMes.append(pdE.getString("rowS") + "；");
          }
          if (!StringUtil.isEmpty(pdE.getString("reasonP"))) {
            reasonMes.append(pdE.getString("reasonP") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("reasonC"))) {
            reasonMes.append(pdE.getString("reasonC") + "，");
          }
          if (!StringUtil.isEmpty(pdE.getString("reasonS"))) {
            reasonMes.append(pdE.getString("reasonS") + "；");
          }
          
          if ((pdE != null) && (pdE.getString("line") != null) && (pdE.getString("line") != "")) {
            pdE.put("row", rowMes);
            pdE.put("reason", reasonMes);
            
            pdEList.add(pdE);
          } else {
            if (k == -1) {
              exo.setGroupNum(groupNum);
              exo.setOrderNum("CK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
              exo.setCheckedState(1);
              exo.setOrderDate(Tools.date2Str(new Date()));
              exo.setManagerName(contactPersonM);
              exo.setManagerTel(contactPersonMobileM);
              exo.setDeliverAddress(contactPersonAddressM);
              exo.setIs_ivt_order_print(0);
              exo.setIs_temporary(2);
              exo.setUser(user);
              exo.setMerchant(merchant);
              exo.setOrderType(3);
              exo.setComment(comment);
            }
            if (u == -1) {
              exoi.setGroupNum(exo.getGroupNum());
              exoi.setOrderNum(exo.getOrderNum());
              exoi.setQuantity(quantityOfProduct);
              exoi.setFinalQuantity(quantityOfProduct);
              exoi.setPurchasePrice(priceOfProduct1);
              exoi.setSalePrice(priceOfProduct2);
              exoi.seteXTime(DateUtil.getAfterDayDate("3"));
              exoi.setProduct(product);
              exoi.setCreator(user.getNAME());
              exoi.setZyOrderNum(zyOrderNum);
              if (((PageData)listPd.get(i)).get("var7") != null) {
                exoi.setComment(((PageData)listPd.get(i)).get("var7").toString());
              }
              exoi.setProduct(product);
              exoi.setQuantity(quantityOfProduct);
              exoi.setFinalQuantity(quantityOfProduct);
              exoi.setIsivtBK(4);
              listEXOrderItem.add(exoi);
            }
            if (u > -1) {
              ((EXOrderItem)listEXOrderItem.get(u)).setQuantity(quantityOfProduct);
              ((EXOrderItem)listEXOrderItem.get(u)).setFinalQuantity(MathUtil.mulAndAdd(quantityOfProduct, priceOfProduct2, ((EXOrderItem)listEXOrderItem.get(u)).getFinalQuantity()));
              if (((PageData)listPd.get(i)).get("var7") != null) {
                ((EXOrderItem)listEXOrderItem.get(u)).setComment(((EXOrderItem)listEXOrderItem.get(u)).getComment() + ((PageData)listPd.get(i)).get("var7").toString());
              }
              if (((PageData)listPd.get(i)).get("var8") != null) {
                ((EXOrderItem)listEXOrderItem.get(u)).setZyOrderNum(((EXOrderItem)listEXOrderItem.get(u)).getZyOrderNum() + "," + ((PageData)listPd.get(i)).get("var8").toString());
              }
            }
            if (k > -1)
            {
              ((EXOrder)listEXOrder.get(k)).setFinalAmount(MathUtil.mulAndAdd(exoi.getSalePrice(), exoi.getQuantity(), ((EXOrder)listEXOrder.get(k)).getFinalAmount()));
              ((EXOrder)listEXOrder.get(k)).setAmount(MathUtil.mulAndAdd(exoi.getSalePrice(), exoi.getQuantity(), ((EXOrder)listEXOrder.get(k)).getFinalAmount()));
              ((EXOrder)listEXOrder.get(k)).setComment(comment);
            }
            if (k == -1)
            {
              exo.setFinalAmount(MathUtil.mul(exoi.getQuantity(), exoi.getSalePrice()));
              exo.setAmount(MathUtil.mul(exoi.getQuantity(), exoi.getSalePrice()));
              listEXOrder.add(exo);
            }
          }
        }
        if ((pdEList != null) && (pdEList.size() > 0) && (pdEList.get(0) != null) && (!StringUtil.isEmpty(((PageData)pdEList.get(0)).getString("line")))) {
          mv.addObject("varList", pdEList);
        }
        if ((pdEList == null) || (pdEList.size() <= 0) || (StringUtil.isEmpty(((PageData)pdEList.get(0)).getString("line")))) {
          pdEList = null;
        }
        
        if (pdEList == null) {
          double totalQuantity = 0.0D;
          double purPrice = 0.0D;
          PageData pdP = null;
          int enk = -1;
          double productQuantity = 0.0D;
          double productPrice = 0.0D;
          String contactPerson = "";
          String contactPersonMobile = "";
          double skuWeight = 0.0D;
          double skVolume = 0.0D;
          if ((listProduct != null) && (listProduct.size() > 0) && 
            (listEXOrderItem != null) && (listEXOrderItem.size() > 0)) {
            for (int pi = 0; pi < listProduct.size(); pi++) {
              supplier = new Supplier();
              eno = new ENOrder();
              enoi = new ENOrderItem();
              pdP = new PageData();
              totalQuantity = 0.0D;
              commentOfPoi = new StringBuffer();
              for (int exoii = 0; exoii < listEXOrderItem.size(); exoii++) {
                if (((Product)listProduct.get(pi)).getId() == ((EXOrderItem)listEXOrderItem.get(exoii)).getProduct().getId()) {
                  totalQuantity += ((EXOrderItem)listEXOrderItem.get(exoii)).getQuantity();
                  purPrice = ((EXOrderItem)listEXOrderItem.get(exoii)).getPurchasePrice();
                }
              }
              
              pdP = new PageData();
              pdP = this.productService.findProductInfoById(((Product)listProduct.get(pi)).getId());
              
              enk = -1;
              if ((listENOrder != null) && (listENOrder.size() > 0)) {
                for (int en = 0; en < listENOrder.size(); en++) {
                  if (Long.parseLong(pdP.getString("tsId")) == ((ENOrder)listENOrder.get(en)).getSupplier().getId())
                  {
                    eno = (ENOrder)listENOrder.get(en);
                    enk = en;
                    break;
                  }
                }
              }
              if (!StringUtil.isEmpty(pdP.getString("product_quantity"))) {
                productQuantity = Double.parseDouble(pdP.getString("product_quantity"));
              }
              if (!StringUtil.isEmpty(pdP.getString("product_price"))) {
                productPrice = Double.parseDouble(pdP.getString("product_price"));
              }
              if (!StringUtil.isEmpty(pdP.getString("contact_person"))) {
                contactPerson = pdP.getString("contact_person");
              }
              if (!StringUtil.isEmpty(pdP.getString("contact_person_mobile"))) {
                contactPersonMobile = pdP.getString("contact_person_mobile");
              }
              if (!StringUtil.isEmpty(pdP.getString("sku_weight"))) {
                skuWeight = Double.parseDouble(pdP.getString("sku_weight"));
              }
              if (!StringUtil.isEmpty(pdP.getString("sku_volume"))) {
                skVolume = Double.parseDouble(pdP.getString("sku_volume"));
              }
              
              if (enk == -1) {
                supplier.setId(Long.parseLong(pdP.getString("tsId")));
                eno.setGroupNum(groupNum);
                eno.setOrderNum("RK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
                eno.setOrderDate(Tools.date2Str(new Date()));
                eno.setCheckedState(1);
                eno.setManagerName(contactPerson);
                eno.setManagerTel(contactPersonMobile);
                eno.setPaidAmount(0.0D);
                eno.setIsIvtOrderPrint(0);
                eno.setIsTemporary(2);
                eno.setSupplier(supplier);
                eno.setUser(user);
                eno.setTotalSvolume(skVolume * totalQuantity);
                eno.setTotalWeight(skuWeight * totalQuantity);
                eno.setOrderType(3);
              }
              
              enoi.setGroupNum(groupNum);
              enoi.setOrderNum(eno.getOrderNum());
              enoi.setPurchasePrice(purPrice);
              enoi.setQuantity(totalQuantity);
              enoi.setFinalQuantity(totalQuantity);
              enoi.setIsSplitIvt(2);
              enoi.setSvolume(skVolume * totalQuantity+"");
              enoi.setWeight(skuWeight * totalQuantity+"");
              enoi.setIsIvtBK(1);
              enoi.seteNTime(DateUtil.getAfterDayDate("3"));
              enoi.setCreator(user.getNAME());
              enoi.setIsIvtBK(4);
              enoi.setProduct((Product)listProduct.get(pi));
              listENOrderItem.add(enoi);
              if (enk > -1)
              {
                ((ENOrder)listENOrder.get(enk)).setFinalAmount(MathUtil.mulAndAdd(totalQuantity, purPrice, ((ENOrder)listENOrder.get(enk)).getFinalAmount()));
                ((ENOrder)listENOrder.get(enk)).setAmount(MathUtil.mulAndAdd(totalQuantity, purPrice, ((ENOrder)listENOrder.get(enk)).getFinalAmount()));
                ((ENOrder)listENOrder.get(enk)).setTotalSvolume(MathUtil.sub(((ENOrder)listENOrder.get(enk)).getTotalSvolume(), Double.parseDouble(enoi.getSvolume())));
                ((ENOrder)listENOrder.get(enk)).setTotalWeight(MathUtil.sub(((ENOrder)listENOrder.get(enk)).getTotalWeight(), Double.parseDouble(enoi.getWeight())));
              }
              if (enk == -1)
              {
                eno.setAmount(MathUtil.mul(enoi.getPurchasePrice(), totalQuantity));
                
                eno.setFinalAmount(MathUtil.mul(enoi.getPurchasePrice(), totalQuantity));
                listENOrder.add(eno);
              }
            }
          }
        }
        
        int saveFlag = 1;
        int reFlag = 0;
        try {
          if ((listENOrder != null) && (listENOrder.size() > 0))
          {
            logMidway(this.logger, "入库单数组大小为：" + listENOrder.size());
            reFlag = this.eNOrderService.saveList(listENOrder);
            saveFlag = 2;
            if ((listENOrderItem != null) && (listENOrderItem.size() > 0))
            {
              reFlag = this.eNOrderItemService.saveList(listENOrderItem);
              saveFlag = 3;
              if ((listEXOrder != null) && (listEXOrder.size() > 0))
              {
                reFlag = this.eXOrderService.saveList(listEXOrder);
                saveFlag = 4;
                if ((listEXOrderItem != null) && (listEXOrderItem.size() > 0))
                {
                  reFlag = this.eXOrderItemService.savegiftEXOrderItem(listEXOrderItem);
                  saveFlag = 5;
                  if (pdOfOrderGroup != null)
                  {
                    reFlag = this.orderGroupService.saveOrderGroup(og);
                    saveFlag = 1;
                  }
                }
              }
            }
          }
        } catch (Exception e) {
          mv.addObject("errorMsg", "操作有误，请稍后再试");
          logMidway(this.logger, e.toString());
        }
        
        if (saveFlag > 1) {
          this.eNOrderService.deleteList(listENOrder);
        }
        if (saveFlag > 2) {
          this.eNOrderItemService.deleteList(listENOrderItem);
        }
        if (saveFlag > 3) {
          this.eXOrderService.deleteList(listEXOrder);
        }
        if (saveFlag > 4) {
          this.eXOrderItemService.deleteList(listEXOrderItem);
        }
        logMidway(this.logger, "-----------saveFlag=========" + saveFlag);
        
        listENOrder = null;
        listENOrderItem = null;
        commentOfPoi = null;
        eno = null;
        enoi = null;
        supplier = null;
        merchant = null;
        exo = null;
        listEXOrder = null;
        exoi = null;
        listEXOrderItem = null;
        pdE = null;
        product = null;
        listProduct = null;
        pdPrice = null;
        pdM = null;
        pds = null;
        if ((saveFlag == 1) && (pdEList == null)) {
          mv.addObject("errorMsg", "数据导入成功。");
          mv.addObject("msg", "success");
        } else {
          mv.addObject("errorMsg", "数据表中的数据存在问题，请检查修改后重新操作。");
        }
      }
      else
      {
        mv.addObject("errorMsg", "数据表中没有数据");
      }
    }
    mv.setViewName("procurement/ordergroup/importExcelPageForGift");
    return mv;
  }
  
  /**
   * 查看销售订单
   * @return
   * @throws Exception
   */
  @RequestMapping({"/findorderitem"})
  public ModelAndView findorderitem()throws Exception{
    logBefore(this.logger, "查看销售订单详情");
    ModelAndView mv = getModelAndView();
    PageData pd = null;
    pd = getPageData();
 
    pd.put("order_num",pd.getString("order_num"));
    Object obj = sellingOrderService.findorder(pd);
    List list = sellingOrderService.findorderitem(pd);
    if(list.size() > 0){
    	 mv.addObject("obj", obj);
    	  mv.addObject("msg", "success");
    	  mv.addObject("list", list);
    	  mv.addObject("pd", pd);
    }
    mv.setViewName("procurement/sellingorder/findorderitem");
    this.sysLogService.saveLog("查看销售订单详情", "成功");
    return mv;
  }
  /**
   * 查看销售订单
   * @return
   * @throws Exception
   */
  @RequestMapping(value = { "/editorderitem" } , produces = { "application/text;charset=UTF-8" })
  @ResponseBody
  public String  editorderitem()throws Exception{
    logBefore(this.logger, "修改销售订单");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String msg = sellingOrderService.editorderitem(pd);
    mv.setViewName("procurement/sellingorder/findorderitem");
    this.sysLogService.saveLog("修改销售订单详情", "成功");
    return msg;
  }
	/**
	 * 审核销售订单-地市审核
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/examine12" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String examine12()throws Exception{
		logBefore(this.logger, "审核销售订单");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String  ids = pd.getString("DATA_IDS");
		//根据id查询门店下的所有未审核订单
		pd.put("group",DateUtil.group());
		pd.put("ids",ids);
		//List list = sellingOrderService.findOrderById(pd);
		String result ="";
		try {
			result = sellingOrderService.audit12(ids);
			if ("true".equals(result)) {
				this.sysLogService.saveLog("审核销售订单详情", "成功");
			} else {
				this.sysLogService.saveLog("审核销售订单详情", "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result=e.toString();
		}
		return result;
	}
  /**
   * 审核销售订单-升级审核
   * @return
   * @throws Exception
   */
  @RequestMapping(value = { "/examine" }, produces = { "application/text;charset=UTF-8" })
  @ResponseBody
  public String examine()throws Exception{
    logBefore(this.logger, "审核销售订单");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    
    pd = getPageData();
    String  ids = pd.getString("DATA_IDS");
    //根据id查询门店下的所有未审核订单
    pd.put("group",DateUtil.group());
    pd.put("ids",ids);
    List list = sellingOrderService.findOrderById(pd);
//    String[] idss = ids.split(",");
//    List list = Arrays.asList(idss);
    //更新销售订单状态
//    sellingOrderService.test();
    //批量插入采购订单
    String result ="";
    try {
    	 // result = sellingOrderService.audit(list);
    	 if (auditOrderService.provinceAuditOrder(list)) {
			 	result = "审核成功！";
    	    	this.sysLogService.saveLog("审核销售订单详情", "成功");
    		} else {
			 	result = "审核失败！";
    			this.sysLogService.saveLog("审核销售订单详情", "失败");
    		}
	} catch (Exception e) {
		e.printStackTrace();
		result=e.getMessage();
	}
   
   // mv.addObject("result", result);
    
   
    return result;
  }

  
  /**
   * 获取销售订单列表
   * @param page
   * @param request 
   * @return
   */
   @RequestMapping({"/findsellingorderGrouplist"})
   public ModelAndView findsellingorderGrouplist(Page page,HttpServletRequest request)
   {
     logBefore(this.logger, "列表PurchaseOrder");
     ModelAndView mv = getModelAndView();
     PageData pd = new PageData();
     try {
       pd = getPageData();
       List<PageData> areaList = this.merchantService.areaList(pd);
       String searchcriteria = pd.getString("searchcriteria");
       String keyword = pd.getString("keyword");
       if ((keyword != null) && (!"".equals(keyword))){
         keyword = keyword.trim();
         pd.put("keyword", keyword);
         pd.put("searchcriteria", searchcriteria);
       }
      /* String checked_state = pd.getString("state");*/
       String lastLoginStart = pd.getString("lastLoginStart");
       String lastLoginEnd = pd.getString("lastLoginEnd");
       /*if(checked_state != null && !checked_state.equals("")){
     	  pd.put("checked_state", checked_state);
       }*/
       if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
         lastLoginStart = lastLoginStart + " 00:00:00";
         pd.put("lastLoginStart", lastLoginStart);
       }
       if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
         lastLoginEnd = lastLoginEnd + " 00:00:00";
         pd.put("lastLoginEnd", lastLoginEnd);
       }
       String ordertype = request.getParameter("ordertype");
       if(ordertype != null && !ordertype.equals("")){
     	  pd.put("ordertype", ordertype);
       }
       if (!StringUtil.isEmpty(pd.getString("cityid"))) {
 			String city = pd.getString("cityid");
 			pd.put("cityid", city);
 		}
 		if (pd.getString("state")!= null && pd.getString("state").equals("1") ) {
			 pd.put("group", DateUtil.group());
		 }
       page.setPd(pd);
 		page.setShowCount(50);
       List<PageData> varList = this.sellingOrderService.listPdPagePurchaseGroupOrder(page);
     /*  String state = pd.getString("checked_state");*/
       mv.addObject("area", areaList);
     /*  pd.put("state", state);*/
       mv.setViewName("procurement/sellingorder/tosellingorderGrouplist");
       mv.addObject("varList", varList);
       mv.addObject("pd", pd);
     } catch (Exception e) {
       this.logger.error(e.toString(), e);
     }
     return mv;
   }
   
   /**
    * 根据门店id查看销售订单
    * @return
    * @throws Exception
    */
   @RequestMapping({"/findorderitem_md"})
   public ModelAndView findorderitem_md()throws Exception{
     logBefore(this.logger, "查看销售订单详情");
     ModelAndView mv = getModelAndView();
     PageData pd = new PageData();
     pd = getPageData();
  
     pd.put("merchant_id",pd.getString("merchant_id"));
	   PageData obj = (PageData)sellingOrderService.findMDorder(pd);
	   /*String role = LoginUtil.getLoginUser().getROLE_ID();
	   if (role.equals("32")) {
		   role = "地市";
	   } else if (role.equals("35") || role.equals("2")) {
		   role = "省级";
	   } else {
		   role = "";
	   }
	   obj.put("role",role);*/
	   List list = sellingOrderService.findMDorderitem(pd);
	   if(list.size() > 0){
     	 mv.addObject("obj", obj);
     	  mv.addObject("msg", "success");
     	  mv.addObject("list", list);
     	  mv.addObject("pd", pd);
     }
     mv.addObject("pd", pd);
     mv.setViewName("procurement/sellingorder/findMDorderitem");
     this.sysLogService.saveLog("查看销售订单详情", "成功");
     return mv;
   }
   @ResponseBody
   @RequestMapping(value = "/saveSellingOrderItem",produces={"text/html;charset=UTF-8"})
   public String insertSellingOrderItem(){
   	String msg = "添加成功";
	   try {
		   PageData pd = getPageData();
		   PageData pda = sellingOrderItemService.checkProductById(pd);
		   if (pda != null && !pda.isEmpty()) {
			   return "该商品已存在，请勿重复添加！";
		   }
		   pd.put("thstate", "1");
		   pd.put("state", "1");
		   pd.put("create_time", Tools.date2Str(new Date()));
		   pd.put("creator", LoginUtil.getLoginUser().getUSERNAME());
		   sellingOrderItemService.saveOrderItem(pd);
		   return msg;
	   } catch (Exception e) {
		   msg = "添加失败";
	   }
	   return msg;
   }
}


