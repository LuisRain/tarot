package com.hy.controller.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.EXOrder;
import com.hy.entity.order.EXOrderItem;
import com.hy.entity.order.OrderGroup;
import com.hy.entity.order.WaveSortingGroup;
import com.hy.entity.product.CargoSpace;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.Product;
import com.hy.entity.system.User;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.inventory.WarehouseService;
import com.hy.service.order.*;
import com.hy.service.product.*;
import com.hy.service.system.syslog.SysLogService;
import com.hy.threadpool.threadimpl.CheckExwareOrderofPer;
import com.hy.threadpool.ResultDto;
import com.hy.util.*;
import com.hy.util.sqlserver.DBConnectionManager;
import com.hy.util.sqlserver.Midorders;
import com.hy.util.word.WordMain;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Controller
@RequestMapping({ "/exwarehouseorder" })
public class ExWarehouseOrderController extends BaseController {
	@Resource(name = "exwarehouseorderService")
	private ExWarehouseOrderService exwarehouseorderService;
	@Resource
	private ProductService productService;
	@Resource(name = "productTypeService")
	private ProductTypeService productTypeService;
	@Resource(name = "orderGroupService")
	private OrderGroupService orderGroupService;
	@Resource(name = "eXOrderItemService")
	private EXOrderItemService eXOrderItemService;
	@Resource(name = "productinventoryService")
	private ProductinventoryService productinventoryService;
	@Resource(name = "eXOrderService")
	private EXOrderService eXOrderService;
	@Resource(name = "sysLogService")
	private SysLogService sysLogService;
	@Resource
	private WarehouseService warehouseService;
	@Resource
	private MerchantService merchantService;
	@Resource
	private ProductpriceService productpriceService;
	@Resource(name = "waveSortingGroupService")
	private WaveSortingGroupService waveSortingGroupService;
	@Resource(name="supplierService")
	private SupplierService supplierService;

	@RequestMapping(value = { "/qrfh" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String qrfh(){
		String operationMsg="确认订单信息";
		String result="成功";
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			exwarehouseorderService.qrxg(pd);
		} catch (Exception e) {
			result="出错了！";
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
			e.printStackTrace();
		}finally{
			logEnd(logger, operationMsg);
		}
		return result;
	}
	
	
	@RequestMapping({ "/Exconfirmed" })
	public ModelAndView Exconfirmed() {
		logBefore(this.logger, "列表仓配ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			List<PageData> varList = this.exwarehouseorderService.Exconfirmed(pd);
			mv.setViewName("inventorymanagement/exwarehouseorder/confirmed");
			mv.addObject("varList", varList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
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
	 			titles.add("销售价格"); // 3
	 			titles.add("订购数量"); // 3
	 			titles.add("实际数量"); // 3
	 			titles.add("赠品数量"); // 6
	 			titles.add("备注"); // 7
	 			titles.add("门店编码"); // 9
	 			titles.add("门店简称"); // 8
	 			titles.add("出库日期"); // 8\
	 			titles.add("状态"); // 8
	 			titles.add("地区"); // 8
	 			map.put("titles", titles);
	 			List<PageData> listpd=exwarehouseorderService.dcexorderlistexcel(pd);
	 			List<PageData> varList=new ArrayList<PageData>();
	 			for (int i = 0; i < listpd.size(); i++) {
	 				PageData pdd=new PageData();
	 				pdd.put("var1", listpd.get(i).getString("group_num"));
	 				pdd.put("var2", listpd.get(i).getString("order_num"));
	 				pdd.put("var3", listpd.get(i).getString("product_num"));
	 				pdd.put("var4", listpd.get(i).getString("bar_code"));
	 				pdd.put("var5", listpd.get(i).getString("product_name"));
	 				pdd.put("var6", listpd.get(i).getString("sale_price"));
	 				pdd.put("var7", listpd.get(i).getString("quantity"));
	 				pdd.put("var8", listpd.get(i).getString("final_quantity"));
	 				pdd.put("var9", listpd.get(i).getString("gift_quantity"));
	 				pdd.put("var10", listpd.get(i).getString("comment"));
	 				pdd.put("var11", listpd.get(i).getString("merchant_num"));
	 				pdd.put("var12", listpd.get(i).getString("short_name"));
	 				pdd.put("var13", listpd.get(i).getString("order_date"));
	 				
	 				if(listpd.get(i).getString("ivt_state").equals("1")){
	 					pdd.put("var14", "未审核");
	 				}else if(listpd.get(i).getString("ivt_state").equals("2")){
	 					pdd.put("var14", "已标签波次分拣");
	 				}else if(listpd.get(i).getString("ivt_state").equals("3")){
	 					pdd.put("var14", "已LED波次分拣");
	 				}else{
	 					pdd.put("var14", "已出库");
	 				}
	 				pdd.put("var15", listpd.get(i).getString("area_name"));
	 				varList.add(pdd);
	 			}
	 			map.put("varList", varList);
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
	 		} catch (Exception e) {
	 			// TODO: handle exception
	 			e.printStackTrace();
	 			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
	 		}finally{
	 			logEnd(logger, operationMsg);
	 		}
	 		return mv;
	 	}
	
	@RequestMapping(value = { "/jsxg" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String jsxg(){
		String operationMsg="开始计算修改出库单数量";
		logBefore(logger,operationMsg);
		String result="false";
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			result=exwarehouseorderService.jsxg(pd);
		} catch (Exception e) {
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
			e.printStackTrace();
		}finally{
			logEnd(logger, operationMsg);
		}
		return result;
	}
	
	
	
	@RequestMapping({ "addTempOrder" })
	public ModelAndView addTempOrder(Page page) {
		PageData pd = new PageData();
		pd = getPageData();
		ModelAndView mv = getModelAndView();
		try {
			PageData productInvertory = new PageData();
			productInvertory.put("productId", pd.get("productId"));
			productInvertory.put("newQuantity", pd.get("newQuantity"));
			productInvertory.put("warehouseId", pd.get("warehouseId"));

			if (this.productinventoryService.updateProductinventoryReduce(productInvertory)) {
				saveTempOrder(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.setViewName("redirect:exTemplistPage.do?type=" + pd.get("type"));

		return mv;
	}

	/** 分仓订单 ****//*
					 * @RequestMapping({ "saveExOrder" }) public ModelAndView saveExOrder(Page page)
					 * { PageData pd = new PageData(); pd = getPageData(); ModelAndView mv =
					 * getModelAndView(); try { PageData productInvertory = new PageData();
					 * 
					 * productInvertory.put("productId", pd.get("productId"));
					 * productInvertory.put("newQuantity", pd.get("newQuantity"));
					 * productInvertory.put("warehouseId", pd.get("warehouseId"));
					 * 
					 * if
					 * (this.productinventoryService.updateProductinventoryReduce(productInvertory))
					 * { saveTempOrder(page); }
					 * 
					 * } catch (Exception e) { e.printStackTrace(); }
					 * 
					 * mv.setViewName(
					 * "inventorymanagement/exwarehouseorder/exwarehouseorder_listexcel");
					 * 
					 * return mv; }
					 */

	private void saveTempOrder(Page page) throws Exception {
		PageData pd = new PageData();
		pd = getPageData();
		String orderGroupNum = "GP_" + StringUtil.getStringOfMillisecond("");
		OrderGroup og = new OrderGroup();
		PageData orderItem = new PageData();
		String orderNum = OrderNum.exTempOrderNum();
		orderItem.put("order_num", orderNum);
		orderItem.put("product_id", pd.get("productId"));
		orderItem.put("group_num", orderGroupNum);
		orderItem.put("quantity", pd.get("newQuantity"));
		orderItem.put("final_quantity", pd.get("newQuantity"));
		orderItem.put("reason", pd.get("reason"));
		orderItem.put("state", Integer.valueOf(1));
		orderItem.put("comment", pd.get("comment"));
		orderItem.put("is_ivt_BK", pd.get("warehouseId"));

		this.eXOrderItemService.save(orderItem);

		PageData enTempOrderParam = new PageData();
		enTempOrderParam.put("order_num", orderNum);
		enTempOrderParam.put("is_order_print", Integer.valueOf(1));
		enTempOrderParam.put("ivt_state", Integer.valueOf(2));
		if (pd.get("type").equals("2")) {
			enTempOrderParam.put("is_temporary", Integer.valueOf(2));
			enTempOrderParam.put("order_type", Integer.valueOf(4));
		} else {
			enTempOrderParam.put("is_temporary", Integer.valueOf(1));
		}
		enTempOrderParam.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		enTempOrderParam.put("state", Integer.valueOf(1));
		enTempOrderParam.put("group_num", orderGroupNum);
		this.exwarehouseorderService.save(enTempOrderParam);
		og.setOrderGroupNum(orderGroupNum);
		og.setUser(LoginUtil.getLoginUser());
		og.setGroupType(7);
		this.orderGroupService.saveOrderGroup(og);
		this.sysLogService.saveLog("保存临时出库信息", "成功");
	}

	@RequestMapping({ "goSaveTemp" })
	public ModelAndView goSaveTemp(String type) {
		ModelAndView mv = getModelAndView();

		PageData pd = new PageData();
		pd = getPageData();
		pd.put("parent_id", Integer.valueOf(0));
		try {
			PageData warhouse = new PageData();
			List<PageData> productType = this.productTypeService.findByParentId(pd);
			if (type.equals("2")) {
				mv.setViewName("inventorymanagement/exwarehouseorder/extempwarehouseorder_edit");
			} else {
				mv.setViewName("inventorymanagement/extempwarehouseorder/extempwarehouseorder_edit");
			}

			List<PageData> wlist = this.warehouseService.listAll(warhouse);
			mv.addObject("wlist", wlist);
			mv.addObject("productType", productType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping({ "exTemplistPage" })
	public ModelAndView enTemplistPage(Page page, String type) {
		logBefore(this.logger, "分页查询临时出库单");

		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}

			String reason = pd.getString("reason");
			if ((reason != null) && (!"".equals(reason))) {
				pd.put("reason", reason);
			} else {
				pd.put("reason", "");
			}
			page.setPd(pd);

			List<PageData> varList = this.exwarehouseorderService

					.exTemplistPage(page);
			if (type.equals("2")) {
				mv.setViewName("inventorymanagement/exwarehouseorder/extempwarehouseorder_list");
			} else {
				mv.setViewName("inventorymanagement/extempwarehouseorder/extempwarehouseorder_list");
			}

			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping({ "/save" })
	public ModelAndView save() throws Exception {
		logBefore(this.logger, "新增EnWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		pd.put("ENWAREHOUSEORDER_ID", get32UUID());
		pd.put("GROUP_NUM", "");
		pd.put("ORDER_NUM", "");
		pd.put("SUPPLIER_ID", "");
		pd.put("IS_IVT_ORDER_PRINT", "");
		pd.put("USER_ID", "");
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));
		this.exwarehouseorderService.save(pd);
		mv.addObject("msg", "success");
		this.sysLogService.saveLog("新增EnWarehouseOrder", "成功");
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping({ "/delete" })
	public void delete(PrintWriter out) {
		logBefore(this.logger, "删除EnWarehouseOrder");
		PageData pd = new PageData();
		try {
			pd = getPageData();
			this.exwarehouseorderService.delete(pd);
			out.write("success");
			out.close();
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
	}

	@RequestMapping({ "/tomergeEx" })
	public void tomergeEx(PrintWriter out) {
		logBefore(this.logger, "删除EnWarehouseOrder");
		try {
			this.exwarehouseorderService.updatemerge();
			out.write("success");
			out.close();
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
	}
	
	/**
	 * 审核减库存
	 * @return
	 */
	@RequestMapping(value = { "/tomergeSh" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String tomergeSh(){
		String operationMsg="审核开始";
		logBefore(logger,operationMsg);
		String result="false";
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			String DATA_IDS=pd.getString("DATA_IDS");
			if(DATA_IDS!=null && DATA_IDS!=""){
				String ListIDS[]=DATA_IDS.split(",");
				result=exwarehouseorderService.tomergeShck(ListIDS);
				if(result=="true"){
					result="true";
					logMidway(logger,operationMsg+  "成功");
				}else{
					result="false";
					logMidway(logger,operationMsg+ "审核失败");
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			result=e.toString();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
			e.printStackTrace();
		}finally{
			logEnd(logger, operationMsg);
		}
		return result;
	}

	@RequestMapping({ "/edit" })
	public ModelAndView edit() throws Exception {
		logBefore(this.logger, "修改EnWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		this.exwarehouseorderService.edit(pd);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		this.sysLogService.saveLog("修改EnWarehouseOrder", "成功");
		return mv;
	}

	/**
	 * 仓配出库单管理列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping({ "/list" })
	public ModelAndView list(Page page) {
		logBefore(this.logger, "列表仓配ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			List<PageData> areaList = this.merchantService.areaList(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
				pd.put("ivt_state", pd.getString("ivt_state"));
			}
			if (!StringUtil.isEmpty(pd.getString("cityid"))) {
				String city = pd.getString("cityid");
				pd.put("cityid", city);
			}
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			page.setShowCount(351);
			page.setPd(pd);
			List<PageData> varList = this.exwarehouseorderService.list(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/exwarehouseorder_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject("area", areaList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 支配出库单管理列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping({ "/directlist" })
	public ModelAndView directlist(Page page) {
		logBefore(this.logger, "列表直配ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			List<PageData> areaList = this.merchantService.areaList(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
				pd.put("ivt_state", pd.getString("ivt_state"));
			}
			if (!StringUtil.isEmpty(pd.getString("cityid"))) {
				String city = pd.getString("cityid");
				pd.put("cityid", city);
			}
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			page.setShowCount(351);
			page.setPd(pd);
			List<PageData> varList = this.exwarehouseorderService.directlist(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/directexwarehouseorder_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject("area", areaList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({ "goExwareorderProductEditdirect" })
	public ModelAndView goExwareorderProductEditdirect(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			type="1";
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);
			
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
			mv.setViewName("inventorymanagement/exwarehouseorder/directexwarehouseorder_product_edit");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	/**
	 * 出库单管理列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping({ "/fencanglist" })
	public ModelAndView fencanglist(Page page) {
		logBefore(this.logger, "列表ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			List<PageData> areaList = this.merchantService.areaList(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
				pd.put("ivt_state", pd.getString("ivt_state"));
			}
			if (!StringUtil.isEmpty(pd.getString("cityid"))) {
				String city = pd.getString("cityid");
				pd.put("cityid", city);
			}
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			page.setShowCount(351);
			page.setPd(pd);
			List<PageData> varList = this.exwarehouseorderService.fenCangdatalistPage(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/FenCangexwareorder_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject("area", areaList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	@RequestMapping({ "/listOfWaveSortingGroupBq" })
	public ModelAndView listOfWaveSortingGroupBq(Page page) {
		logBefore(this.logger, "查看波次标签分拣详情ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");

			List<PageData> varList = null;
			List<Product> prList = new ArrayList();
			if (DATA_IDS != null) {
				pd = getPageData();
				pd.put("waveSortingGroupNum", DATA_IDS);
				page.setPd(pd);
				EXOrderItem exoi = null;
				List<EXOrderItem> exoiList = new ArrayList();
				List<PageData> pds = null;
				Product product = null;
				CargoSpace cargoSpace = null;
				Merchant merchant = null;
				List<Merchant> merchantList = new ArrayList();
				List<Merchant> merchantListP = null;
				Midorders midorders = null;
				List<Midorders> midordersList = new ArrayList();
				boolean prsFlag = false;

				DBConnectionManager dcm = new DBConnectionManager();
				int pint = -1;
				double totalQuantity = 0.0D;
				int boxNum = 0;
				int splitTotalCount = 0;
				int splitPerCount = 0;
				int pdsi = 0;
				varList = this.eXOrderService.findSplitTotalCountOfEXOrderItemByIdsBq(pd);

				if ((varList != null) && (varList.size() > 0)) {
					for (PageData pdt : varList) {
						prsFlag = false;
						product = new Product();
						product.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("pid")) ? "0" : pdt.getString("pid")));
						merchant = new Merchant();
						merchant.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
						merchant.setMerchantName(
								StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
						merchant.setMerchantNum(
								StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
						merchant.setProductCount(
								StringUtil.isEmpty(pdt.getString("totalCount")) ? "0" : pdt.getString("totalCount"));
						merchant.setTexoiId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
						merchant.setSplitPerCount(
								StringUtil.isEmpty(pdt.getString("perCount")) ? "0" : pdt.getString("perCount"));
						if ((prList != null) && (prList.size() > 0)) {
							for (int prsi = 0; prsi < prList.size(); prsi++) {
								if (product.getId() == ((Product) prList.get(prsi)).getId()) {
									prsFlag = true;
									totalQuantity = MathUtil.add(((Product) prList.get(prsi)).getTotalQuantity(),
											Double.parseDouble(merchant.getSplitPerCount()));
									((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
									((Product) prList.get(prsi)).getMerchants().add(merchant);
									break;
								}
							}
						}
						if ((!prsFlag) || (pdsi == 0)) {
							cargoSpace = new CargoSpace();
							merchantListP = new ArrayList();
							cargoSpace.setId(Long.parseLong(StringUtil.isEmpty(pdt.getString("cargoSpace_id")) ? "0"
									: pdt.getString("cargoSpace_id")));
							cargoSpace.setZone(StringUtil.isEmpty(pdt.getString("zone")) ? "0" : pdt.getString("zone"));
							cargoSpace.setStorey(
									StringUtil.isEmpty(pdt.getString("storey")) ? "0" : pdt.getString("storey"));
							cargoSpace.setStoreyNum(
									StringUtil.isEmpty(pdt.getString("storeyNum")) ? "0" : pdt.getString("storeyNum"));
							product.setBarCode(
									StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
							product.setBoxNumber(
									StringUtil.isEmpty(pdt.getString("boxNumber")) ? "0" : pdt.getString("boxNumber"));
							product.setProductName(
									StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
							product.setSalePrice(
									Double.parseDouble(StringUtil.isEmpty(pdt.getString("product_price")) ? "0"
											: pdt.getString("product_price")));
							product.setCargoSpace(cargoSpace);
							product.setTaxes(StringUtil.isEmpty(pdt.getString("wave_sorting_num")) ? "0"
									: pdt.getString("wave_sorting_num"));
							product.setRemarks(
									StringUtil.isEmpty(pdt.getString("unitName")) ? "0" : pdt.getString("unitName"));

							merchantListP.add(merchant);
							product.setMerchants(merchantListP);
							prList.add(product);
						}
						pdsi++;
					}
					if ((prList != null) && (prList.size() > 0)) {
						for (int prsi = 0; prsi < prList.size(); prsi++) {
							if (((Product) prList.get(prsi)).getMerchants() != null) {
								if (((Product) prList.get(prsi)).getMerchants().size() > 0) {
									totalQuantity = 0.0D;
									for (int pmi = 0; pmi < ((Product) prList.get(prsi)).getMerchants().size(); pmi++) {
										totalQuantity = MathUtil.add(totalQuantity, Double.parseDouble(
												((Merchant) ((Product) prList.get(prsi)).getMerchants().get(pmi))
														.getSplitPerCount()));
									}
									((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
								}
							}
						}
					}
				}
				pd.put("msg", "ok");
				this.sysLogService.saveLog("查询波次出库单商品详情", "成功");
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("查询波次出库单商品详情", "成功");
			}
			mv.addObject("varList", prList);
			mv.setViewName("procurement/wavesorting/wave_ex_list_bq");
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/listOfWaveSortingGroup" })
	public ModelAndView listOfWaveSortingGroup(Page page) {
		logBefore(this.logger, "查看波次整箱分拣详情ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			String waveOrder = pd.getString("waveOrder");
			List<PageData> varList = null;
			List<Product> prList = new ArrayList();
			if (DATA_IDS != null) {
				pd = getPageData();
				pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
				pd.put("waveSortingGroupNum", DATA_IDS);
				pd.put("waveOrder", waveOrder);
				page.setPd(pd);
				EXOrderItem exoi = null;
				List<EXOrderItem> exoiList = new ArrayList();
				List<PageData> pds = null;
				Product product = null;
				CargoSpace cargoSpace = null;
				Merchant merchant = null;
				List<Merchant> merchantList = new ArrayList();
				List<Merchant> merchantListP = null;
				Midorders midorders = null;
				List<Midorders> midordersList = new ArrayList();
				boolean prsFlag = false;
				boolean mFlag = false;
				DBConnectionManager dcm = new DBConnectionManager();
				int pint = -1;
				double totalQuantity = 0.0D;
				int boxNum = 0;
				int splitTotalCount = 0;
				int splitPerCount = 0;
				int pdsi = 0;
				varList = this.eXOrderService.findSplitTotalCountOfEXOrderItemByIds(pd);

				if ((varList != null) && (varList.size() > 0)) {
					for (PageData pdt : varList) {
						mFlag = false;
						prsFlag = false;
						product = new Product();
						product.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("pid")) ? "0" : pdt.getString("pid")));
						merchant = new Merchant();
						merchant.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
						merchant.setMerchantName(
								StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
						merchant.setMerchantNum(
								StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
						merchant.setProductCount(
								StringUtil.isEmpty(pdt.getString("totalCount")) ? "0" : pdt.getString("totalCount"));
						merchant.setTexoiId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
						merchant.setSplitTotalCount(
								StringUtil.isEmpty(pdt.getString("totalCount")) ? "0" : pdt.getString("totalCount"));
						if ((prList != null) && (prList.size() > 0)) {
							for (int prsi = 0; prsi < prList.size(); prsi++) {
								if (product.getId() == ((Product) prList.get(prsi)).getId()) {
									prsFlag = true;
									totalQuantity = MathUtil.add(((Product) prList.get(prsi)).getTotalQuantity(),
											Double.parseDouble(merchant.getSplitTotalCount()));
									((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
									((Product) prList.get(prsi)).getMerchants().add(merchant);
									break;
								}
							}
						}
						if ((!prsFlag) || (pdsi == 0)) {
							cargoSpace = new CargoSpace();
							merchantListP = new ArrayList();
							cargoSpace.setId(Long.parseLong(StringUtil.isEmpty(pdt.getString("cargoSpace_id")) ? "0"
									: pdt.getString("cargoSpace_id")));
							cargoSpace.setZone(StringUtil.isEmpty(pdt.getString("zone")) ? "0" : pdt.getString("zone"));
							cargoSpace.setStorey(
									StringUtil.isEmpty(pdt.getString("storey")) ? "0" : pdt.getString("storey"));
							cargoSpace.setStoreyNum(
									StringUtil.isEmpty(pdt.getString("storeyNum")) ? "0" : pdt.getString("storeyNum"));
							product.setBarCode(
									StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
							product.setBoxNumber(
									StringUtil.isEmpty(pdt.getString("boxNumber")) ? "0" : pdt.getString("boxNumber"));
							product.setProductName(
									StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
							product.setSalePrice(
									Double.parseDouble(StringUtil.isEmpty(pdt.getString("product_price")) ? "0"
											: pdt.getString("product_price")));
							product.setCargoSpace(cargoSpace);
							product.setTaxes(StringUtil.isEmpty(pdt.getString("wave_sorting_num")) ? "0"
									: pdt.getString("wave_sorting_num"));

							merchantListP.add(merchant);
							product.setMerchants(merchantListP);
							product.setRemarks(
									StringUtil.isEmpty(pdt.getString("unitName")) ? "0" : pdt.getString("unitName"));
							prList.add(product);
						}
						pdsi++;
					}
					if ((prList != null) && (prList.size() > 0)) {
						for (int prsi = 0; prsi < prList.size(); prsi++) {
							if (((Product) prList.get(prsi)).getMerchants() != null) {
								if (((Product) prList.get(prsi)).getMerchants().size() > 0) {
									totalQuantity = 0.0D;
									for (int pmi = 0; pmi < ((Product) prList.get(prsi)).getMerchants().size(); pmi++) {
										totalQuantity = MathUtil.add(totalQuantity, Double.parseDouble(
												((Merchant) ((Product) prList.get(prsi)).getMerchants().get(pmi))
														.getSplitTotalCount()));
									}
									((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
								}
							}
						}
					}
				}
				pd.put("msg", "ok");
				this.sysLogService.saveLog("查询波次出库单商品详情", "成功");
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("查询波次出库单商品详情", "成功");
			}
			mv.addObject("varList", prList);
			mv.setViewName("procurement/wavesorting/wave_ex_list");
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/listOfGift" })
	public ModelAndView listOfGift(Page page) {
		logBefore(this.logger, "列表EnWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();

			List<PageData> areaList = this.merchantService.areaList(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
				pd.put("ivt_state", pd.getString("ivt_state"));
			}

			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			page.setShowCount(351);
			page.setPd(pd);
			List<PageData> varList = this.exwarehouseorderService.listOfGift(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/exwarehouseorderOfGift_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject("area", areaList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/goAdd" })
	public ModelAndView goAdd() {
		logBefore(this.logger, "去新增EnWarehouseOrder页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			mv.setViewName("inventorymanagement/exwarehouseorder/exwarehouseorder_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/goEdit" })
	public ModelAndView goEdit() {
		logBefore(this.logger, "去修改EnWarehouseOrder页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			pd = this.exwarehouseorderService.findById(pd);
			mv.setViewName("inventorymanagement/exwarehouseorder/exwarehouseorder_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/deleteAll" })
	@ResponseBody
	public Object deleteAll() {
		logBefore(this.logger, "批量删除EnWarehouseOrder");
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		try {
			pd = getPageData();
			List<PageData> pdList = new ArrayList();
			String DATA_IDS = pd.getString("DATA_IDS");
			if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				this.exwarehouseorderService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
				this.sysLogService.saveLog("批量删除EnWarehouseOrder", "成功");
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("批量删除EnWarehouseOrder", "失败");
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

	@RequestMapping({ "goExwareorderProductEdit" })
	public ModelAndView goExwareorderProductEdit(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			type="1";
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			pd.put("orderNum", orderId);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistProduct(pd);
			
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
			mv.setViewName("inventorymanagement/exwarehouseorder/exwarehouseorder_product_edit");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	@RequestMapping({ "FengoExwareorderProductEdit" })
	public ModelAndView FengoExwareorderProductEdit(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/Fenexwareorder_product_edit");
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	@RequestMapping({ "FenCangprint" })
	public ModelAndView FenCangprint(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			PageData pdd=new PageData();
			List<PageData> listpd=new ArrayList<PageData>();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			page.setShowCount(351);
			pd.put("parent_id", Integer.valueOf(0));
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			pdd.put("name", LoginUtil.getLoginUser().getNAME());
			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			listpd.add(pd);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);
			PageData count=eXOrderItemService.fencangCount(pd);
			mv.setViewName("inventorymanagement/exwarehouseorder/FenCangprint");
			mv.addObject("count",count);
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("listpd",listpd);
			mv.addObject("pdd", pdd);
			mv.addObject("sumOrder", sumOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping({ "goExwareorderProductEditPQ" })
	public ModelAndView goExwareorderProductEditPQ(Page page, String orderId, String type) {
		logBefore(this.logger, "查看出库单标签系统分类详情");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			PageData pdd=new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			type="1";
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			
			pdd.put("orderNum", orderId);
			List<PageData> pageDate = this.eXOrderItemService.getFjItemById(pdd);
			mv.setViewName("inventorymanagement/exwarehouseorder/exwarehouseorder_product_edit_pq");
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping({ "toWordFromExWarehouseInfos" })
	public ModelAndView toWordFromExWarehouseInfos(Page page, String waveNum, String type) {
		logBefore(this.logger, "查看出库单标签系统分类详情");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			Map<String, Object> dataMap0 = null;
			Map<String, Object> dataMap = null;
			Map<String, Object> dataMap10 = null;
			PageData pde = null;
			List<PageData> pdListOfEx = this.exwarehouseorderService.getExwarouseInfosByWaveNum(waveNum);

			List<Merchant> mList = null;
			Merchant mer = null;
			List<Map<String, Object>> list1 = null;
			List<Map<String, Object>> list10 = null;
			List<Map<String, Object>> list0 = null;
			List<Map<String, Object>> list00 = null;
			Map<String, Object> map = null;
			if ((pdListOfEx != null) && (pdListOfEx.size() > 0)) {
				dataMap0 = new HashMap();
				list0 = new ArrayList();
				mList = new ArrayList();
				boolean mIdFlag = false;
				int mio = 0;
				int totalPageNum = 1;
				WordMain wm = new WordMain();
				for (int i = 0; i < pdListOfEx.size(); i++) {
					pde = (PageData) pdListOfEx.get(i);
					mer = new Merchant();
					map = new HashMap();
					dataMap = new HashMap();
					list1 = new ArrayList();
					mIdFlag = false;

					if (!StringUtil.isEmpty(pde.getString("merchant_id"))) {
						mer.setId(Long.parseLong(pde.getString("merchant_id")));
						if ((list0 != null) && (list0.size() > 0)) {
							for (int mi = 0; mi < list0.size(); mi++) {
								if ((!ObjectUtil.isNullOrEmpty(((Map) list0.get(mi)).get("merchantId")))
										&& (mer.getId() == Long
												.parseLong(((Map) list0.get(mi)).get("merchantId").toString()))) {
									mIdFlag = true;
									mio = mi;
									list1 = (List) ((Map) list0.get(mi)).get("table");
									break;
								}
							}
						}

						map.put("barCode", pde.getString("bar_code"));
						map.put("productName", pde.getString("product_name"));
						map.put("specification", pde.getString("specification"));
						map.put("salePrice", pde.getString("sale_price"));
						map.put("quantity", pde.getString("quantity"));
						map.put("finalQuantity", pde.getString("final_quantity"));
						map.put("totalPrice", pde.getString("totalPrice"));

						list1.add(map);
						if (mIdFlag) {
							((Map) list0.get(mio)).put("table", list1);
						} else {
							dataMap.put("table", list1);
						}

						if ((i == 0) || (!mIdFlag)) {
							totalPageNum++;

							dataMap.put("merchantId", pde.getString("merchant_id"));
							dataMap.put("merchantName", pde.getString("merchant_name"));
							dataMap.put("mobile", pde.getString("mobile"));
							dataMap.put("address", pde.getString("address"));
							dataMap.put("comment", pde.getString("comment"));

							dataMap.put("finalTotalQuantity", pde.getString("final_total_quantity"));
							dataMap.put("finalAmount", pde.getString("final_amount"));

							dataMap.put("Y", Integer.valueOf(1));
							list0.add(dataMap);
						}
					}
				}

				int per = 0;
				int total = 0;
				int pageNum = 12;
				int ptotal = 0;
				if (((list0 != null ? 1 : 0) & (list0.size() > 0 ? 1 : 0)) != 0) {
					list00 = new ArrayList();
					for (int di = 0; di < list0.size(); di++) {
						list00.add((Map) list0.get(di));
						dataMap = new HashMap();
						list1 = new ArrayList();
						dataMap = (Map) list0.get(di);
						list1 = (List) dataMap.get("table");
						if (((list1 != null ? 1 : 0) & (list1.size() > 12 ? 1 : 0)) != 0) {
							total = list1.size() / pageNum;
							per = list1.size() % pageNum;
							if (total > 0) {
								if (per > 0) {
									total++;
								}
								ptotal = total;
								for (int it = 1; it < total + 1; it++) {
									dataMap10 = new HashMap(dataMap);
									list10 = new ArrayList();
									for (int ii = 0; ii < pageNum; ii++) {
										if ((it == total) && (per > 0) && (ii >= per)) {
											break;
										}
										list10.add((Map) list1.get(pageNum * (it - 1) + ii));
									}

									dataMap10.put("table", list10);
									dataMap10.put("perOfPage", it + "/" + ptotal);
									if (it == 1) {
										list00.set(di, dataMap10);
									} else {
										list00.add(dataMap10);
									}
								}
							}
						}
					}

					Collections.sort(list00, new Comparator<Map<String, Object>>() {
						public int compare(Map<String, Object> args1, Map<String, Object> args2) {
							return Long.parseLong(args1.get("merchantId").toString()) > Long
									.parseLong(args2.get("merchantId").toString()) ? 1 : -1;
						}
					});
				}
			}

			dataMap0.put("table0", list00);
			WordMain wm = new WordMain();
			wm.createDoc(dataMap0, "D:/出库单/" + waveNum + ".doc");

			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}

			String startDate = pd.getString("startDate");
			String endDate = pd.getString("endDate");
			if ((startDate != null) && (!"".equals(startDate))) {
				startDate = startDate + " 00:00:00";
				pd.put("startDate", startDate);
			}
			if ((endDate != null) && (!"".equals(endDate))) {
				endDate = endDate + " 00:00:00";
				pd.put("endDate", endDate);
			}
			page.setPd(pd);
			List<WaveSortingGroup> varList = this.waveSortingGroupService.waveSortingGroupNew(pd);
			pd.put("flag", Integer.valueOf(1));
			pd.put("waveNum", waveNum);
			mv.addObject("varList", varList);
		} catch (Exception e) {
			pd.put("flag", Integer.valueOf(-1));
		}
		mv.addObject("pd", pd);
		mv.setViewName("procurement/wavesorting/waveSortingGroup_list");
		return mv;
	}

	@RequestMapping({ "goStorageExwareorderProductEdit" })
	public ModelAndView goStorageExwareorderProductEdit(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/addStorageoBeputinstorage");
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = { "updateEnOrderProduct" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String updateEnOrderProduct(Page page) {
		String result = "true";
		try {
			PageData pd = getPageData();
			String ids = pd.getString("ids");
			String numbers = pd.getString("numbers");
			String orderId = pd.get("orderId").toString();
			PageData exwarhouseOrder = new PageData();
			exwarhouseOrder.put("id", orderId);
			exwarhouseOrder = this.exwarehouseorderService.findById(exwarhouseOrder);
			Double finalAmount = Double.valueOf(exwarhouseOrder.get("final_amount").toString());

			double total_svolume = Double.valueOf(exwarhouseOrder.get("total_svolume").toString()).doubleValue();

			double total_weight = Double.valueOf(exwarhouseOrder.get("total_weight").toString()).doubleValue();

			if (!"".equals(ids)) {
				String[] idsStr = ids.split(",");
				String[] numbersStr = numbers.split(",");
				for (int i = 0; i < idsStr.length; i++) {
					PageData orderItem = new PageData();
					orderItem.put("id", idsStr[i]);
					orderItem = this.eXOrderItemService.findItemById(orderItem);
					double oldQuantity = Double.valueOf(orderItem.get("final_quantity").toString()).doubleValue();
					double newQuantity = Double.valueOf(numbersStr[i]).doubleValue();
					Double purchasePrice = Double.valueOf(orderItem.get("sale_price").toString());
					long productId = Long.parseLong(orderItem.get("product_id").toString());
					PageData product = this.productService.findProductTZById(productId);
					double sku_weight = Double.parseDouble(product.get("sku_weight").toString());
					double sku_volume = Double.parseDouble(product.get("sku_volume").toString());
					if (oldQuantity > newQuantity) {
						double subQuantity = NumberUtil.sub(oldQuantity, newQuantity);
						Double newTotalPrice = Double.valueOf(NumberUtil.mul(subQuantity, purchasePrice.doubleValue()));
						finalAmount = Double
								.valueOf(NumberUtil.sub(finalAmount.doubleValue(), newTotalPrice.doubleValue()));

						Double newTotalWeight = Double.valueOf(NumberUtil.mul(sku_weight, subQuantity));
						total_weight = NumberUtil.sub(total_weight, newTotalWeight.doubleValue());

						Double newTotalVolume = Double.valueOf(NumberUtil.mul(sku_volume, subQuantity));
						total_svolume = NumberUtil.sub(total_svolume, newTotalVolume.doubleValue());
					} else {
						double addQuantity = NumberUtil.sub(newQuantity, oldQuantity);

						Double newTotalPrice = Double.valueOf(NumberUtil.mul(addQuantity, purchasePrice.doubleValue()));
						finalAmount = Double
								.valueOf(NumberUtil.add(finalAmount.doubleValue(), newTotalPrice.doubleValue()));

						Double newTotalWeight = Double.valueOf(NumberUtil.mul(sku_weight, addQuantity));
						total_weight = NumberUtil.add(total_weight, newTotalWeight.doubleValue());

						Double newTotalVolume = Double.valueOf(NumberUtil.mul(sku_volume, addQuantity));
						total_svolume = NumberUtil.add(total_svolume, newTotalVolume.doubleValue());
					}

					exwarhouseOrder.put("final_amount", finalAmount);
					exwarhouseOrder.put("total_svolume", Double.valueOf(total_svolume));
					exwarhouseOrder.put("total_weight", Double.valueOf(total_weight));
					this.exwarehouseorderService.edit(exwarhouseOrder);
					orderItem.remove("final_quantity");
					orderItem.put("final_quantity", numbersStr[i]);
					if (this.eXOrderItemService.editQuantity(orderItem) != 1) {
						result = "false";
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

	@RequestMapping(value = { "/reviewedAll" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String reviewedAll() {
		String result = "";
		logBefore(this.logger, "批量审核EnWarehouseOrder");
		PageData pd = new PageData();
		try {
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				result = this.exwarehouseorderService.updatereviewedAll(ArrayDATA_IDS);
				if ("true".equals(result)) {
					this.sysLogService.saveLog("批量审核EnWarehouseOrder", "成功");
				} else {
					this.sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
				}
			}
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
			result = e.toString();
		} finally {
			logAfter(this.logger);
		}
		return result;
	}

	/*** 赠品出库审核 ***/
	@RequestMapping(value = { "/giftReviewedAll" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String giftReviewedAll() {
		String result = "";
		logBefore(this.logger, "批量审核EnWarehouseOrder");
		PageData pd = new PageData();
		try {
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				result = this.exwarehouseorderService.updatereviewedAll(ArrayDATA_IDS);
				if ("true".equals(result)) {
					this.sysLogService.saveLog("批量审核EnWarehouseOrder", "成功");
				} else {
					this.sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
				}
			}
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
			result = e.toString();
		} finally {
			logAfter(this.logger);
		}
		return result;
	}

	@RequestMapping(value = { "/saleReviewedAll" }, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object saleReviewedAll() {
		logBefore(this.logger, "批量审核EnWarehouseOrder");
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		try {
			pd = getPageData();
			List<PageData> pdList = new ArrayList();
			String DATA_IDS = pd.getString("DATA_IDS");
			if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				if (!this.exwarehouseorderService.saleReviewedAll(ArrayDATA_IDS)) {
					pd.put("msg", "no");
				} else {
					pd.put("msg", "ok");
				}
			} else {
				pd.put("msg", "no");
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

	@RequestMapping({ "/toSplitEXToExcel" })
	public ModelAndView toSplitEXToExcel() {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			Map<String, Object> dataMap = new HashMap();
			List<String> titles = new ArrayList();
			titles.add("条形码");
			titles.add("商品编码");
			titles.add("商户名称");
			titles.add("规格");
			titles.add("数量");
			titles.add("客户编号");
			titles.add("客户名称");
			titles.add("备注");
			titles.add("中油采购订单号");
			dataMap.put("titles", titles);
			List<String> sheets = new ArrayList();
			sheets.add("整箱表");
			sheets.add("不足整箱表");

			pd.put("ivt_state", Integer.valueOf(2));
			List<PageData> eXOrderList = this.eXOrderService.listEXOrderNew(pd);
			List<PageData> varList = new ArrayList();
			List<PageData> varListPer = new ArrayList();
			List<PageData> eXOrderItemList = null;
			PageData packingMeasurement = null;
			int packingNum = 1;

			int finalQuantity = 0;
			int boxNum = 0;
			int QuantityPer = 0;
			String tpUnit1 = "";
			String tpUnit2 = "";
			String tpUnit3 = "";
			PageData vpd = null;
			PageData vpdPer = null;
			List<PageData> listUpdates = new ArrayList();
			for (int i = 0; i < eXOrderList.size(); i++) {
				if (!StringUtil.isEmpty(((PageData) eXOrderList.get(i)).getString("order_num"))) {
					if (!StringUtil.isEmpty(((PageData) eXOrderList.get(i)).getString("group_num"))) {
						pd.put("order_num", ((PageData) eXOrderList.get(i)).getString("order_num"));
						pd.put("ivtState", Integer.valueOf(3));
						pd.put("group_num", ((PageData) eXOrderList.get(i)).getString("group_num"));
						listUpdates.add(pd);

						eXOrderItemList = this.eXOrderItemService.findByIdOrOrderNumOrGroupNum(pd);
						if ((eXOrderItemList != null) && (eXOrderItemList.size() > 0)) {
							for (int exi = 0; exi < eXOrderItemList.size(); exi++) {
								if (!StringUtil
										.isEmpty(((PageData) eXOrderItemList.get(exi)).getString("final_quantity"))) {
									if (!StringUtil.isEmpty(
											((PageData) eXOrderItemList.get(exi)).getString("packing_util_num"))) {
										if (!StringUtil.isEmpty(
												((PageData) eXOrderItemList.get(exi)).getString("parent_id"))) {
											tpUnit1 = ((PageData) eXOrderItemList.get(exi)).getString("unit_name");
											packingNum = Integer.parseInt(((PageData) eXOrderItemList.get(exi))
													.getString("packing_util_num"));
											if (Integer.parseInt(
													((PageData) eXOrderItemList.get(exi)).getString("parent_id")) > 0) {
												pd.put("parentId",

														((PageData) eXOrderItemList.get(exi)).getString("parent_id"));
												packingMeasurement = this.eXOrderItemService.findPackingByParentId(pd);
												if (packingMeasurement != null) {
													if (!StringUtil
															.isEmpty(packingMeasurement.getString("parent_id"))) {
														tpUnit2 = packingMeasurement.getString("unit_name");

														if (Integer.parseInt(
																packingMeasurement.getString("parent_id")) <= 0) {
															break;
														}
														packingNum = packingNum * Integer.parseInt(
																packingMeasurement.getString("packing_util_num"));
														packingMeasurement = null;
														packingMeasurement = this.eXOrderItemService
																.findPackingByParentId(pd);
														if (packingMeasurement != null) {
															if (!StringUtil.isEmpty(
																	packingMeasurement.getString("parent_id"))) {
																tpUnit3 =

																		((PageData) eXOrderItemList.get(exi))
																				.getString("unit_name");

																break;
															}
														}
														logMidway(this.logger, "查询第二级的父类拆分单位有误，父类拆分单位不存在，基础信息不完整。");

														break;
													}
												}
												logMidway(this.logger, "查询第一级的父类拆分单位有误，父类拆分单位不存在，基础信息不完整。");
											}
										}
									}

									label726: finalQuantity = new Double(Double.parseDouble(
											((PageData) eXOrderItemList.get(exi)).getString("final_quantity")))
													.intValue();

									if (finalQuantity > 0) {
										boxNum = finalQuantity / packingNum;
										QuantityPer = finalQuantity % packingNum;
										if ((boxNum != 0) || (QuantityPer != 0)) {
											vpd = new PageData();
											vpd.put("var1",
													((PageData) eXOrderItemList.get(exi)).getString("bar_code"));
											vpd.put("var2",
													((PageData) eXOrderItemList.get(exi)).getString("product_num"));
											vpd.put("var3",
													((PageData) eXOrderItemList.get(exi)).getString("product_name"));
											vpd.put("var6", ((PageData) eXOrderList.get(i)).getString("merchant_num"));

											vpd.put("var7", ((PageData) eXOrderList.get(i)).getString("short_name"));
											vpd.put("var8", ((PageData) eXOrderItemList.get(exi)).get("comment"));
											vpd.put("var9",
													((PageData) eXOrderItemList.get(exi)).getString("zy_order_num"));
											if (boxNum != 0) {
												vpd.put("var4", tpUnit2);
												vpd.put("var5", Integer.valueOf(boxNum));

												varList.add(vpd);
											}
										}
										if ((boxNum != 0) || (QuantityPer != 0)) {
											vpdPer = new PageData();
											vpdPer.put("var1",
													((PageData) eXOrderItemList.get(exi)).getString("bar_code"));
											vpdPer.put("var2",
													((PageData) eXOrderItemList.get(exi)).getString("product_num"));
											vpdPer.put("var3",
													((PageData) eXOrderItemList.get(exi)).getString("product_name"));
											vpdPer.put("var6",
													((PageData) eXOrderList.get(i)).getString("merchant_num"));

											vpdPer.put("var7", ((PageData) eXOrderList.get(i)).getString("short_name"));
											vpdPer.put("var8", ((PageData) eXOrderItemList.get(exi)).get("comment"));
											vpdPer.put("var9",
													((PageData) eXOrderItemList.get(exi)).getString("zy_order_num"));
											if (QuantityPer != 0) {
												vpdPer.put("var4", tpUnit1);
												vpdPer.put("var5", Integer.valueOf(QuantityPer));

												varListPer.add(vpdPer);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			dataMap.put("varList0", varList);
			dataMap.put("sheetName0", "商品整箱表");
			dataMap.put("varList1", varListPer);
			dataMap.put("sheetName1", "商品不足整箱表");
			dataMap.put("sheetNum", Integer.valueOf(2));
			dataMap.put("filename", "商品拆分出库单");
			this.eXOrderService.editState(listUpdates);

			ObjectExcelViewFromMap erv = new ObjectExcelViewFromMap();
			mv = new ModelAndView(erv, dataMap);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/toCombineEXTotoExcel" })
	public ModelAndView toCombineEXTotoExcel() {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			Map<String, Object> dataMap = new HashMap();
			List<String> titles = new ArrayList();
			titles.add("条形码");
			titles.add("商品编码");
			titles.add("商户名称");
			titles.add("规格");
			titles.add("数量");
			titles.add("客户编号");
			titles.add("客户名称");
			titles.add("备注");
			titles.add("中油采购订单号");
			dataMap.put("titles", titles);
			List<String> sheets = new ArrayList();
			sheets.add("合并出库单");

			pd.put("ivtState", Integer.valueOf(3));
			List<PageData> eXOrderList = this.eXOrderService.listEXOrderNew(pd);
			List<PageData> varList = new ArrayList();
			List<PageData> eXOrderItemList = null;
			int finalQuantity = 0;
			String tpUnit = "";
			PageData vpd = null;
			List<PageData> listUpdates = new ArrayList();
			for (int i = 0; i < eXOrderList.size(); i++) {
				if (!StringUtil.isEmpty(((PageData) eXOrderList.get(i)).getString("order_num"))) {
					if (!StringUtil.isEmpty(((PageData) eXOrderList.get(i)).getString("group_num"))) {
						pd.put("order_num", ((PageData) eXOrderList.get(i)).getString("order_num"));
						pd.put("ivtState", Integer.valueOf(4));
						pd.put("group_num", ((PageData) eXOrderList.get(i)).getString("group_num"));
						listUpdates.add(pd);
						eXOrderItemList = this.eXOrderItemService.findByIdOrOrderNumOrGroupNum(pd);
						if ((eXOrderItemList != null) && (eXOrderItemList.size() > 0)) {
							for (int exi = 0; exi < eXOrderItemList.size(); exi++) {
								if (!StringUtil
										.isEmpty(((PageData) eXOrderItemList.get(exi)).getString("final_quantity"))) {
									if (!StringUtil.isEmpty(
											((PageData) eXOrderItemList.get(exi)).getString("packing_util_num"))) {
										if (!StringUtil.isEmpty(
												((PageData) eXOrderItemList.get(exi)).getString("parent_id"))) {
											tpUnit = ((PageData) eXOrderItemList.get(exi)).getString("unit_name");

											finalQuantity = new Double(Double.parseDouble(
													((PageData) eXOrderItemList.get(exi)).getString("final_quantity")))
															.intValue();
											vpd = new PageData();
											vpd.put("var1",
													((PageData) eXOrderItemList.get(exi)).getString("bar_code"));
											vpd.put("var2",
													((PageData) eXOrderItemList.get(exi)).getString("product_num"));
											vpd.put("var3",
													((PageData) eXOrderItemList.get(exi)).getString("product_name"));
											vpd.put("var4", tpUnit);
											vpd.put("var5", Integer.valueOf(finalQuantity));
											vpd.put("var6", ((PageData) eXOrderList.get(i)).getString("merchant_num"));
											vpd.put("var7", ((PageData) eXOrderList.get(i)).getString("short_name"));
											vpd.put("var8", ((PageData) eXOrderItemList.get(exi)).getString("comment"));
											vpd.put("var9",
													((PageData) eXOrderItemList.get(exi)).getString("zy_order_num"));
											varList.add(vpd);
										}
									}
								}
							}
						}
					}
				}
			}
			dataMap.put("varList0", varList);
			dataMap.put("sheetName0", "合并出库单表");
			dataMap.put("sheetNum", Integer.valueOf(1));
			dataMap.put("filename", "商品合并出库单");
			this.eXOrderService.editState(listUpdates);
			this.orderGroupService.updateStateOforderGroup(vpd);

			ObjectExcelViewFromMap erv = new ObjectExcelViewFromMap();
			mv = new ModelAndView(erv, dataMap);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping(value = { "saveEnOrderItem" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String saveEnOrderItem(Page page, String bkType) {
		String result = "false";
		PageData pd = getPageData();
		try {
			pd.put("is_ivt_BK", bkType);
			if (this.eXOrderItemService.save(pd) > 0) {
				double quantity = Double.parseDouble(pd.get("quantity").toString());
				double price = Double.parseDouble(pd.get("sale_price").toString());
				double finalAmount = NumberUtil.mul(quantity, price);

				double sku_volume = Double.parseDouble(pd.get("sku_volume").toString());

				double sku_weight = Double.parseDouble(pd.get("sku_weight").toString());

				double sku_volumeLine = NumberUtil.mul(quantity, sku_volume);
				double sku_weightLine = NumberUtil.mul(quantity, sku_weight);

				PageData enwarhouseOrder = new PageData();
				enwarhouseOrder.put("id", pd.get("orderId"));
				enwarhouseOrder = this.exwarehouseorderService.findById(enwarhouseOrder);
				double oldFinalAmount = Double.valueOf(enwarhouseOrder.get("final_amount").toString()).doubleValue();
				double oldVolume = Double.valueOf(enwarhouseOrder.get("total_svolume").toString()).doubleValue();
				double oldWeight = Double.valueOf(enwarhouseOrder.get("total_weight").toString()).doubleValue();
				PageData enwarhouseOrder1 = new PageData();
				enwarhouseOrder1.put("id", pd.get("orderId"));
				enwarhouseOrder1.put("final_amount", Double.valueOf(NumberUtil.add(finalAmount, oldFinalAmount)));
				enwarhouseOrder1.put("total_svolume", Double.valueOf(NumberUtil.add(sku_volumeLine, oldVolume)));
				enwarhouseOrder1.put("total_weight", Double.valueOf(NumberUtil.add(sku_weightLine, oldWeight)));
				if (this.exwarehouseorderService.updateFinalAmount(enwarhouseOrder1) > 0) {
					PageData product = this.productService
							.getproduct(Long.valueOf(Long.parseLong(pd.get("product_id").toString())));
					return product.getString("product_name") + "," + product.getString("bar_code") + ","
							+ product.getString("specification") + "," + product.getString("product_quantity") + ","
							+ product.getString("tppaproduct_price") + "," + pd.get("quantity");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "false";
		}

		return result;
	}

	@RequestMapping({ "StorageExWarehouseList" })
	public ModelAndView StorageExWarehouseList(Page page) {
		logBefore(this.logger, "列表StorageExWarehouseList");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			page.setPd(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			List<PageData> varList = this.exwarehouseorderService.StorageExWarehouseList(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/StorageExWarehouse_List");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "addStorageoBeputinstorage" })
	public ModelAndView addStorageoBeputinstorage(Page page, String orderId, String type) {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd.put("ivt_state", "1");
		mv.setViewName("inventorymanagement/exwarehouseorder/addStorageoBeputinstorage");
		mv.addObject("enwarhouse", pd);
		return mv;
	}

	@RequestMapping({ "doWaveExwareorderOfPer" })
	public ModelAndView doWaveExwareorderOfPer(Page page, String orderId, String type) {
		logBefore(this.logger, "波次操作处理出库单第一步,选择不超过351家店进行标签系统分拣，");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		WaveSortingGroup wsg = null;
		ExecutorService executor  = ThreadPoolFactory.getThreadPool();
		try {
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			pd = getPageData();
			page.setShowCount(351);
			page.setPd(pd);
			List<Product> prList = new ArrayList();
			if (!StringUtil.isEmpty(DATA_IDS)) {
				wsg = new WaveSortingGroup();
				wsg.setWaveSortingGroupNum("wave_" + StringUtil.getStringOfMillisecond(""));
				wsg.setExOrderIdsString(DATA_IDS);
				wsg.setUser(LoginUtil.getLoginUser());
				wsg.setWaveSortingGroupType(1);
				wsg.setWaveSortingGroupState(1);
				List<PageData> pds = null;
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				pds = this.exwarehouseorderService.getExwarouseItemByIds(ArrayDATA_IDS);
				List<Future<ResultDto>> futureList = new ArrayList<>();
				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						Future<ResultDto> dtoFuture = executor.submit(new CheckExwareOrderofPer(pdt, wsg, eXOrderService));
						futureList.add(dtoFuture);
					}
					ResultDto resultDto;
					for (Future<ResultDto> resultDtoFuture : futureList) {
						resultDto = resultDtoFuture.get();
                        if (resultDto.getFlag()) {
							prList .addAll(resultDto.getPd());
						} else {
							throw new RuntimeException("审核出错");
						}
					}
					executor.shutdown();
					for (int gi = 0; gi < ArrayDATA_IDS.length; gi++) {
						pd.put("id", ArrayDATA_IDS[gi]);
						pd.put("ivt_state", Integer.valueOf(2));
						pd.put("wave_order", Integer.valueOf(0));
						this.eXOrderService.editIvtState(pd);
						pd.put("wave_sorting_num", wsg.getWaveSortingGroupNum());
						this.eXOrderService.editWaveSotringNum(pd);
					}
					this.waveSortingGroupService.saveOrderGroup(wsg);
					pd.put("msg", "ok");
					this.sysLogService.saveLog("波次操作处理出库单第一步,选择不超过351家店进行标签系统分拣", "成功");
				} else {
					this.sysLogService.saveLog("波次操作出库单返回商品信息为空", "失败");
					pd.put("msg", "no");
				}
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("出库单数据为空-波次操作处理出库单第一步,选择不超过351家店进行标签系统分拣", "失败");
			}

			mv.setViewName("procurement/wavesorting/wave_ex_list_bq");
			//System.out.println(JSONObject.toJSONString(prList));
			mv.addObject("varList", prList);
			mv.addObject("str", pd.getString("DATA_IDS"));
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
			//System.out.println("线程池是否已经关闭？---" + executor.isShutdown());
			if (!executor.isShutdown()) {
				executor.shutdown();
			}
		}
        //System.out.println("-------------------------------分拣成功------------");
		return mv;
	}
	
	/**
	 * 根据门店id  商品id 查询出库单中每个门店的数量
	 * @param mdid
	 * @param proid
	 * @return
	 */
	public List<List<String>> findmdnum(String mdid,String proid){
		
		List<List<String>> list = new ArrayList<>();
		
		return list;
	}

	@RequestMapping({ "doWaveExwareorderOfLED" })
	public ModelAndView doWaveExwareorderOfLED(Page page) {
		logBefore(this.logger, "从标签系统中读取已经分拣好的商品分最终分拣数量");
		ModelAndView mv = getModelAndView();
		Map<String, Object> map = new HashMap();
		PageData pd = new PageData();
		try {
			DBConnectionManager dcm = new DBConnectionManager();
			List<PageData> pds = null;
			EXOrderItem exoi = null;
			List<EXOrderItem> exoiList = new ArrayList();
			List<EXOrderItem> exoiListR = null;
			pd = getPageData();
			List<Product> prList = new ArrayList();
			Product product = null;
			CargoSpace cargoSpace = null;
			Merchant merchant = null;
			List<Merchant> merchantList = new ArrayList();
			List<Merchant> merchantListP = null;
			Midorders midorders = null;
			List<Midorders> midordersList = new ArrayList();
			boolean prsFlag = false;
			boolean mFlag = false;
			int pint = -1;
			double totalQuantity = 0.0D;
			int boxNum = 0;
			int splitTotalCount = 0;
			int splitPerCount = 0;
			String wave_sorting_num = "";
			int pdsi = 0;
			String DATA_IDS = pd.getString("DATA_IDS");
			String [] ArrayDATA_IDS = DATA_IDS.split(",");
			if (DATA_IDS != null) {
				Map<String, Object> conmap=new  HashMap<String,Object>();
				List<String> statusList=Arrays.asList(ArrayDATA_IDS);
				conmap.put("ck_id", LoginUtil.getLoginUser().getCkId());
				conmap.put("array", statusList);
				pd = getPageData();

				page.setShowCount(351);
				page.setPd(pd);
				pds = this.exwarehouseorderService.getPerCountOfExwarouseItemByIds2(conmap);

				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						prsFlag = false;
						product = new Product();
						product.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("pid")) ? "0" : pdt.getString("pid")));
						merchant = new Merchant();
						merchant.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
						merchant.setMerchantName(
								StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
						merchant.setMerchantNum(
								StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
						merchant.setProductCount(StringUtil.isEmpty(pdt.getString("fq")) ? "0" : pdt.getString("fq"));
						merchant.setTexoiId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
						merchant.setSplitTotalCount(
								StringUtil.isEmpty(pdt.getString("totalCount")) ? "0" : pdt.getString("totalCount"));
						merchant.setSplitPerCount(
								StringUtil.isEmpty(pdt.getString("perCount")) ? "0" : pdt.getString("perCount"));
						splitTotalCount = new Double(Double.parseDouble(
								StringUtil.isEmpty(pdt.getString("totalCount")) ? "0" : pdt.getString("totalCount")))
										.intValue();
						splitPerCount = new Double(Double.parseDouble(
								StringUtil.isEmpty(pdt.getString("perCount")) ? "0" : pdt.getString("perCount")))
										.intValue();
						if (!StringUtil.isEmpty(pdt.getString("boxNumber"))) {
							boxNum = Integer.parseInt(pdt.getString("boxNumber"));
							if (boxNum > 0) {
								splitTotalCount = new Double(Double.parseDouble(merchant.getProductCount())).intValue()
										/ boxNum;
								splitPerCount = new Double(Double.parseDouble(merchant.getProductCount())).intValue()
										% boxNum;
							}
						}
						if ((prList != null) && (prList.size() > 0)) {
							for (int prsi = 0; prsi < prList.size(); prsi++) {
								if (product.getId() == ((Product) prList.get(prsi)).getId()) {
									prsFlag = true;

									totalQuantity = ((Product) prList.get(prsi)).getTotalQuantity() + new Double(
											Double.parseDouble(StringUtil.isEmpty(pdt.getString("totalCount")) ? "0"
													: pdt.getString("totalCount"))).intValue();
									((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
									((Product) prList.get(prsi)).getMerchants().add(merchant);
									break;
								}
							}
						}
						if ((!prsFlag) || (pdsi == 0)) {
							cargoSpace = new CargoSpace();
							merchantListP = new ArrayList();
							cargoSpace.setId(Long.parseLong(StringUtil.isEmpty(pdt.getString("cargoSpace_id")) ? "0"
									: pdt.getString("cargoSpace_id")));
							cargoSpace.setZone(StringUtil.isEmpty(pdt.getString("zone")) ? "0" : pdt.getString("zone"));
							cargoSpace.setStorey(
									StringUtil.isEmpty(pdt.getString("storey")) ? "0" : pdt.getString("storey"));
							cargoSpace.setStoreyNum(
									StringUtil.isEmpty(pdt.getString("storeyNum")) ? "0" : pdt.getString("storeyNum"));
							product.setBarCode(
									StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
							product.setBoxNumber(
									StringUtil.isEmpty(pdt.getString("boxNumber")) ? "0" : pdt.getString("boxNumber"));
							product.setProductName(
									StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
							product.setUnit(
									StringUtil.isEmpty(pdt.getString("unitName")) ? "0" : pdt.getString("unitName"));
							product.setSalePrice(
									Double.parseDouble(StringUtil.isEmpty(pdt.getString("product_price")) ? "0"
											: pdt.getString("product_price")));
							product.setCargoSpace(cargoSpace);

							product.setTotalQuantity(
									new Double(Double.parseDouble(StringUtil.isEmpty(pdt.getString("totalCount")) ? "0"
											: pdt.getString("totalCount"))).intValue());
							product.setTaxes(StringUtil.isEmpty(pdt.getString("wave_sorting_num")) ? "0"
									: pdt.getString("wave_sorting_num"));
							merchantListP.add(merchant);
							product.setMerchants(merchantListP);
							prList.add(product);
						}
						if (pdsi == 0) {
							wave_sorting_num = StringUtil.isEmpty(pdt.getString("wave_sorting_num")) ? "0"
									: pdt.getString("wave_sorting_num");
						}
						if ((merchantList != null) && (merchantList.size() > 0)) {
							for (int msi = 0; msi < merchantList.size(); msi++) {
								if (merchant.getId() == ((Merchant) merchantList.get(msi)).getId()) {
									mFlag = true;
									break;
								}
							}
						}
						pdsi++;
					}
					pd.put("keyword", wave_sorting_num);
					List<WaveSortingGroup> wsgList = this.waveSortingGroupService.waveSortingGroupNew(pd);
					//System.out.println("wsgList.size()=====" + wsgList.size());
					WaveSortingGroup wsg = null;
					if ((wsgList != null) && (wsgList.size() > 0)) {
						wsg = new WaveSortingGroup();
						wsg = (WaveSortingGroup) wsgList.get(0);
					}
					int wave_order = wsg.getWaveOrder();
					if (ArrayDATA_IDS != null) {
						for (int gi = 0; gi < ArrayDATA_IDS.length; gi++) {
							pd.put("id", ArrayDATA_IDS[gi]);
							pd.put("ivt_state", Integer.valueOf(3));
							pd.put("wave_order", Integer.valueOf(wave_order + 1));
							this.eXOrderService.editIvtState(pd);
						}
					}

					pd.put("wave_order", Integer.valueOf(wave_order + 1));
					pd.put("wave_sorting_num", wave_sorting_num);
					this.waveSortingGroupService.updateWaveSortingOrder(pd);
					pd.put("msg", "ok");
					this.sysLogService.saveLog("第二步，波次操作出库单完成", "成功");
				} else {
					pd.put("msg", "no");
					this.sysLogService.saveLog("第二步，查询出库单条目信息为空", "失败");
				}
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("第二步，未选择出库单条目", "失败");
			} 
			mv.addObject("varList", prList);

			mv.setViewName("procurement/wavesorting/wave_ex_list");
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}
		return mv;
	}

	@RequestMapping({ "doSumEXOItem" })
	@ResponseBody
	public Object doSumEXOItem(Page page, String orderId, String type) {
		logBefore(this.logger, "合并出库单条目中的整量和分量");

		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		PageData vpd = new PageData();
		String result = "";
		try {
			DBConnectionManager dcm = new DBConnectionManager();
			List<EXOrderItem> exoiListR = null;
			List<PageData> pds = null;
			List<EXOrder> exoList = new ArrayList();
			EXOrder exo = null;
			EXOrderItem exoi = null;
			List<EXOrderItem> exoiList = new ArrayList();
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			String[] ArrayDATA_IDS = DATA_IDS.split(",");
			String group_num = "";
			String wave_sorting_num = "";
			double final_amount = 0.0D;
			boolean faFlag = false;
			int faInt = 0;
			if (ArrayDATA_IDS != null) {
				pd = getPageData();
				page.setShowCount(351);
				page.setPd(pd);

				pds = this.eXOrderService.findEXOrderByIds(ArrayDATA_IDS);
				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						exoi = new EXOrderItem();
						exoi.setId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
						exoi.setPerCount(
								StringUtil.isEmpty(pdt.getString("perCount")) ? "0" : pdt.getString("perCount"));
						exoiList.add(exoi);
					}
				}
				boolean flag = false;
				exoiListR = dcm.selectRecords(exoiList);
				if ((exoiListR != null) && (exoiListR.size() > 0)
						&& (!StringUtil.isEmpty(((EXOrderItem) exoiListR.get(0)).getComment()))
						&& (exoiList.size() == exoiListR.size())) {
					flag = true;

					for (int eli = 0; eli < exoiListR.size(); eli++) {
						pd.put("id", Long.valueOf(((EXOrderItem) exoiListR.get(eli)).getId()));
						pd.put("per_count", Double.valueOf(((EXOrderItem) exoiListR.get(eli)).getQuantity()));
						this.eXOrderService.editPerCount(pd);
					}
				}

				if (flag) {
					pds = this.eXOrderService.findEXOrderItemByIds(ArrayDATA_IDS);
					if ((pds != null) && (pds.size() > 0)) {
						for (PageData pdt : pds) {
							faFlag = false;
							exoi = new EXOrderItem();
							exo = new EXOrder();
							if (StringUtil.isEmpty(group_num)) {
								group_num = pdt.getString("group_num");
							}
							if (StringUtil.isEmpty(wave_sorting_num)) {
								wave_sorting_num = pdt.getString("wave_sorting_num");
							}
							exo.setId(Long.parseLong(
									StringUtil.isEmpty(pdt.getString("texoId")) ? "0" : pdt.getString("texoId")));
							exoi.setId(Long.parseLong(
									StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
							exoi.setFinalQuantity(MathUtil.mulAndAdd(
									Integer.parseInt(StringUtil.isEmpty(pdt.getString("box_number")) ? "0"
											: pdt.getString("box_number")),
									Double.parseDouble(StringUtil.isEmpty(pdt.getString("total_count")) ? "0"
											: pdt.getString("total_count")),
									Double.parseDouble(StringUtil.isEmpty(pdt.getString("per_count")) ? "0"
											: pdt.getString("per_count"))));
							final_amount = MathUtil
									.mul(Double.parseDouble(StringUtil.isEmpty(pdt.getString("sale_price")) ? "0"
											: pdt.getString("sale_price")), exoi.getFinalQuantity());
							exoiList.add(exoi);
							if ((exoList != null) && (exoList.size() > 0)) {
								for (int exoii = 0; exoii < exoList.size(); exoii++) {
									if (((EXOrder) exoList.get(exoii)).getId() == exo.getId()) {
										final_amount = MathUtil.add(final_amount,
												((EXOrder) exoList.get(exoii)).getFinalAmount());
										((EXOrder) exoList.get(exoii)).setFinalAmount(final_amount);
										faFlag = true;
										break;
									}
								}
							}
							if ((!faFlag) || (faInt == 0)) {
								exo.setFinalAmount(final_amount);
								exo.setIvt_state(4);
								exoList.add(exo);
							}
							faInt++;
						}
						if ((exoiList != null) && (exoiList.size() > 0)) {
							for (int eli = 0; eli < exoiList.size(); eli++) {
								pd.put("id", Long.valueOf(((EXOrderItem) exoiList.get(eli)).getId()));
								pd.put("final_quantity",
										Double.valueOf(((EXOrderItem) exoiList.get(eli)).getFinalQuantity()));
								this.eXOrderService.editFinalQuantity(pd);
							}
						}

						if ((exoList != null) && (exoList.size() > 0)) {
							for (int exoiiii = 0; exoiiii < exoList.size(); exoiiii++) {
								pd.put("id", Long.valueOf(((EXOrder) exoList.get(exoiiii)).getId()));
								pd.put("ivt_state", Integer.valueOf(4));
								pd.put("final_amount",
										Double.valueOf(((EXOrder) exoList.get(exoiiii)).getFinalAmount()));
								this.eXOrderService.editIvtStateAndFinalAmount(pd);
							}
						}

						if (ArrayDATA_IDS != null) {
							for (int gi = 0; gi < ArrayDATA_IDS.length; gi++) {
								pd.put("id", ArrayDATA_IDS[gi]);
								pd.put("ivt_state", Integer.valueOf(4));
								this.eXOrderService.editIvtState(pd);
							}
						}
						vpd.put("group_num", group_num);
						this.orderGroupService.updateStateOforderGroup(vpd);
						vpd.put("wave_sorting_num", wave_sorting_num);
						this.waveSortingGroupService.updateStateOforderGroup(vpd);
						pd.put("msg", "ok");
						map.put("msg", "ok");
						result = "ok";
						this.sysLogService.saveLog("查询并更新出库单条目商品中最终分拣分量成功", "成功");
					}
				} else {
					map.put("msg", "1");
					this.sysLogService.saveLog("查询标签分拣系统分拣结果为空", "失败");
				}
			} else {
				map.put("msg", "2");
				this.sysLogService.saveLog("查询并更新出库单条目中出库单数据为空", "失败");
			}
			pd.put("iv_state", Integer.valueOf(4));
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}

		return AppUtil.returnObject(pd, map);
	}

	@RequestMapping({ "doWaveExwareorderOfPerForGift" })
	public ModelAndView doWaveExwareorderOfPerForGift(Page page) {
		logBefore(this.logger, "从标签系统中读取已经分拣好的商品分最终分拣数 量");
		ModelAndView mv = getModelAndView();
		Map<String, Object> map = new HashMap();
		PageData pd = new PageData();
		try {
			DBConnectionManager dcm = new DBConnectionManager();
			List<PageData> pds = null;
			EXOrderItem exoi = null;
			List<EXOrderItem> exoiList = new ArrayList();
			pd = getPageData();
			List<Product> prList = new ArrayList();
			Product product = null;
			CargoSpace cargoSpace = null;
			Merchant merchant = null;
			List<Merchant> merchantList = new ArrayList();
			List<Merchant> merchantListP = null;
			Midorders midorders = null;
			List<Midorders> midordersList = new ArrayList();
			boolean prsFlag = false;
			boolean mFlag = false;
			int pint = -1;
			double totalQuantity = 0.0D;
			int boxNum = 0;
			int splitTotalCount = 0;
			int splitPerCount = 0;
			int pdsi = 0;
			String DATA_IDS = pd.getString("DATA_IDS");
			String[] ArrayDATA_IDS = DATA_IDS.split(",");
			if (ArrayDATA_IDS != null) {
				pd = getPageData();
				page.setShowCount(351);
				page.setPd(pd);
				pds = this.eXOrderService.findEXOrderByIds(ArrayDATA_IDS);
				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						exoi = new EXOrderItem();
						exoi.setId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
						exoiList.add(exoi);
					}
				}

				exoiList = dcm.selectRecords(exoiList);
				if ((exoiList != null) && (exoiList.size() > 0)) {
					for (int eli = 0; eli < exoiList.size(); eli++) {
						pd.put("id", Long.valueOf(((EXOrderItem) exoiList.get(eli)).getId()));
						pd.put("per_count", Double.valueOf(((EXOrderItem) exoiList.get(eli)).getQuantity()));
						this.eXOrderService.editPerCount(pd);
					}

					if (ArrayDATA_IDS != null) {
						for (int gi = 0; gi < ArrayDATA_IDS.length; gi++) {
							pd.put("id", ArrayDATA_IDS[gi]);
							pd.put("ivt_state", Integer.valueOf(3));
							this.eXOrderService.editIvtState(pd);
						}
					}
					pds = this.exwarehouseorderService.getPerCountOfExwarouseItemByIdsForGift(ArrayDATA_IDS);
					if ((pds != null) && (pds.size() > 0)) {
						for (PageData pdt : pds) {
							product = new Product();
							product.setId(Long
									.parseLong(StringUtil.isEmpty(pdt.getString("pid")) ? "0" : pdt.getString("pid")));
							merchant = new Merchant();
							merchant.setId(Long
									.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
							merchant.setMerchantName(
									StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
							merchant.setMerchantNum(
									StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
							merchant.setProductCount(
									StringUtil.isEmpty(pdt.getString("fq")) ? "0" : pdt.getString("fq"));
							merchant.setTexoiId(Long.parseLong(
									StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
							merchant.setSplitTotalCount(StringUtil.isEmpty(pdt.getString("totalCount")) ? "0"
									: pdt.getString("totalCount"));
							merchant.setSplitPerCount(
									StringUtil.isEmpty(pdt.getString("perCount")) ? "0" : pdt.getString("perCount"));
							if (!StringUtil.isEmpty(pdt.getString("boxNumber"))) {
								boxNum = Integer.parseInt(pdt.getString("boxNumber"));
								if (boxNum > 0) {
									splitTotalCount = new Double(Double.parseDouble(merchant.getProductCount()))
											.intValue() / boxNum;
									splitPerCount = new Double(Double.parseDouble(merchant.getProductCount()))
											.intValue() % boxNum;
								}
							}
							if ((prList != null) && (prList.size() > 0)) {
								for (int prsi = 0; prsi < prList.size(); prsi++) {
									if (product.getId() == ((Product) prList.get(prsi)).getId()) {
										prsFlag = true;
										totalQuantity = ((Product) prList.get(prsi)).getTotalQuantity() + new Double(
												Double.parseDouble(StringUtil.isEmpty(pdt.getString("perCount")) ? "0"
														: pdt.getString("perCount"))).intValue();
										((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
										((Product) prList.get(prsi)).getMerchants().add(merchant);
										break;
									}
								}
							}
							if ((!prsFlag) || (pdsi == 0)) {
								prsFlag = false;
								cargoSpace = new CargoSpace();
								merchantListP = new ArrayList();
								cargoSpace.setId(Long.parseLong(StringUtil.isEmpty(pdt.getString("cargoSpace_id")) ? "0"
										: pdt.getString("cargoSpace_id")));
								cargoSpace.setZone(
										StringUtil.isEmpty(pdt.getString("zone")) ? "0" : pdt.getString("zone"));
								cargoSpace.setStorey(
										StringUtil.isEmpty(pdt.getString("storey")) ? "0" : pdt.getString("storey"));
								cargoSpace.setStoreyNum(StringUtil.isEmpty(pdt.getString("storeyNum")) ? "0"
										: pdt.getString("storeyNum"));
								product.setBarCode(StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0"
										: pdt.getString("pbarcode"));
								product.setBoxNumber(StringUtil.isEmpty(pdt.getString("boxNumber")) ? "0"
										: pdt.getString("boxNumber"));
								product.setProductName(
										StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
								product.setUnit(StringUtil.isEmpty(pdt.getString("unitName")) ? "0"
										: pdt.getString("unitName"));
								product.setCargoSpace(cargoSpace);
								product.setTotalQuantity(new Double(
										Double.parseDouble(StringUtil.isEmpty(pdt.getString("perCount")) ? "0"
												: pdt.getString("perCount"))).intValue());
								product.setTaxes(StringUtil.isEmpty(pdt.getString("wave_sorting_num")) ? "0"
										: pdt.getString("wave_sorting_num"));
								merchantListP.add(merchant);
								product.setMerchants(merchantListP);
								prList.add(product);
							}
							if ((merchantList != null) && (merchantList.size() > 0)) {
								for (int msi = 0; msi < merchantList.size(); msi++) {
									if (merchant.getId() == ((Merchant) merchantList.get(msi)).getId()) {
										mFlag = true;
										break;
									}
								}
							}
							pdsi++;
						}
						pd.put("msg", "ok");
						this.sysLogService.saveLog("波次操作出库单成功", "成功");
					} else {
						this.sysLogService.saveLog("波次操作出库单返回商品信息为空", "失败");
						pd.put("msg", "no");
					}
				}
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("查询并更新出库单条目中出库单数据为空", "失败");
			}
			mv.setViewName("inventorymanagement/exwarehouseorder/wave_ex_product_list_per_gift");
			mv.addObject("varList", prList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}
		return mv;
	}

	@RequestMapping({ "doSumEXOItemForGift" })
	@ResponseBody
	public Object doSumEXOItemForGift(Page page) {
		logBefore(this.logger, "合并赠品出库单条目中的整量和分量");

		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		try {
			DBConnectionManager dcm = new DBConnectionManager();
			List<PageData> pds = null;
			EXOrderItem exoi = null;
			PageData vpd = new PageData();
			String group_num = "";
			List<EXOrderItem> exoiList = new ArrayList();
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			String[] ArrayDATA_IDS = DATA_IDS.split(",");
			if (ArrayDATA_IDS != null) {
				pd = getPageData();
				page.setShowCount(351);
				page.setPd(pd);
				pds = this.eXOrderService.findEXOrderItemByIds(ArrayDATA_IDS);
				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						exoi = new EXOrderItem();
						if (StringUtil.isEmpty(group_num)) {
							group_num = pdt.getString("group_num");
						}
						exoi.setId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
						exoi.setFinalQuantity(Double.parseDouble(
								StringUtil.isEmpty(pdt.getString("per_count")) ? "0" : pdt.getString("per_count"))
								+ Integer
										.parseInt(StringUtil.isEmpty(pdt.getString("box_number")) ? "0"
												: pdt.getString("box_number"))
										* Double.parseDouble(StringUtil.isEmpty(pdt.getString("total_count")) ? "0"
												: pdt.getString("total_count")));
						exoiList.add(exoi);
					}
				}
				if ((exoiList != null) && (exoiList.size() > 0)) {
					for (int eli = 0; eli < exoiList.size(); eli++) {
						pd.put("id", Long.valueOf(((EXOrderItem) exoiList.get(eli)).getId()));
						pd.put("final_quantity", Double.valueOf(((EXOrderItem) exoiList.get(eli)).getFinalQuantity()));
						this.eXOrderService.editFinalQuantity(pd);
					}
				}
				if (ArrayDATA_IDS != null) {
					for (int gi = 0; gi < ArrayDATA_IDS.length; gi++) {
						pd.put("id", ArrayDATA_IDS[gi]);
						pd.put("ivt_state", Integer.valueOf(4));
						this.eXOrderService.editIvtState(pd);
					}
				}
				vpd.put("group_num", group_num);
				this.orderGroupService.updateStateOforderGroup(vpd);
				pd.put("msg", "ok");
				this.sysLogService.saveLog("查询并更新赠品出库单条目商品中最终分拣分量成功", "成功");
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("查询并更新赠品出库单条目中出库单数据为空", "失败");
			}
			pd.put("iv_state", Integer.valueOf(4));
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	@RequestMapping({ "doWaveExwareorderForGift" })
	public ModelAndView doWaveExwareorderForGift(Page page, String orderId, String type) {
		logBefore(this.logger, "波次处理赠品出库单，即分批出库");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();

			String DATA_IDS = pd.getString("DATA_IDS");
			String[] ArrayDATA_IDS = DATA_IDS.split(",");
			pd = getPageData();
			page.setShowCount(351);
			page.setPd(pd);
			List<Product> prList = new ArrayList();
			if (!StringUtil.isEmpty(DATA_IDS)) {
				List<PageData> pds = null;
				Product product = null;
				CargoSpace cargoSpace = null;
				Merchant merchant = null;
				List<Merchant> merchantList = new ArrayList();
				List<Merchant> merchantListP = null;
				Midorders midorders = null;
				List<Midorders> midordersList = new ArrayList();
				boolean prsFlag = false;
				boolean mFlag = false;
				DBConnectionManager dcm = new DBConnectionManager();
				int pint = -1;
				double totalQuantity = 0.0D;
				int boxNum = 0;
				int splitTotalCount = 0;
				int splitPerCount = 0;
				int pdsi = 0;
				pds = this.exwarehouseorderService.getExwarouseItemByIdsForGift(ArrayDATA_IDS);
				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						mFlag = false;
						prsFlag = false;
						product = new Product();
						product.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("pid")) ? "0" : pdt.getString("pid")));
						merchant = new Merchant();
						merchant.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
						merchant.setMerchantName(
								StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
						merchant.setMerchantNum(
								StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
						merchant.setProductCount(StringUtil.isEmpty(pdt.getString("fq")) ? "0" : pdt.getString("fq"));
						if (!StringUtil.isEmpty(pdt.getString("boxNumber"))) {
							boxNum = Integer.parseInt(pdt.getString("boxNumber"));
							if (boxNum > 0) {
								splitTotalCount = new Double(Double.parseDouble(merchant.getProductCount())).intValue()
										/ boxNum;
								splitPerCount = new Double(Double.parseDouble(merchant.getProductCount())).intValue()
										% boxNum;
							}
						}
						merchant.setSplitTotalCount(splitTotalCount + "");
						merchant.setSplitPerCount(splitPerCount + "");
						if ((prList != null) && (prList.size() > 0)) {
							for (int prsi = 0; prsi < prList.size(); prsi++) {
								if (product.getId() == ((Product) prList.get(prsi)).getId()) {
									prsFlag = true;
									totalQuantity = ((Product) prList.get(prsi)).getTotalQuantity() + new Double(
											Double.parseDouble(StringUtil.isEmpty(pdt.getString("fq")) ? "0"
													: pdt.getString("fq"))).intValue();
									((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
									((Product) prList.get(prsi)).getMerchants().add(merchant);
									break;
								}
							}
						}
						if ((!prsFlag) || (pdsi == 0)) {
							cargoSpace = new CargoSpace();
							merchantListP = new ArrayList();
							cargoSpace.setId(Long.parseLong(StringUtil.isEmpty(pdt.getString("cargoSpace_id")) ? "0"
									: pdt.getString("cargoSpace_id")));
							cargoSpace.setZone(StringUtil.isEmpty(pdt.getString("zone")) ? "0" : pdt.getString("zone"));
							cargoSpace.setStorey(
									StringUtil.isEmpty(pdt.getString("storey")) ? "0" : pdt.getString("storey"));
							cargoSpace.setStoreyNum(
									StringUtil.isEmpty(pdt.getString("storeyNum")) ? "0" : pdt.getString("storeyNum"));
							product.setBarCode(
									StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
							product.setBoxNumber(
									StringUtil.isEmpty(pdt.getString("boxNumber")) ? "0" : pdt.getString("boxNumber"));
							product.setProductName(
									StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
							product.setCargoSpace(cargoSpace);
							product.setTotalQuantity(new Double(Double
									.parseDouble(StringUtil.isEmpty(pdt.getString("fq")) ? "0" : pdt.getString("fq")))
											.intValue());
							product.setTaxes(StringUtil.isEmpty(pdt.getString("wave_sorting_num")) ? "0"
									: pdt.getString("wave_sorting_num"));
							merchantListP.add(merchant);
							product.setMerchants(merchantListP);
							prList.add(product);
						}
						if ((prList != null) && (prList.size() > 0)) {
							for (int prsi = 0; prsi < prList.size(); prsi++) {
								if (((Product) prList.get(prsi)).getMerchants() != null) {
									if (((Product) prList.get(prsi)).getMerchants().size() > 0) {
										totalQuantity = 0.0D;
										for (int pmi = 0; pmi < ((Product) prList.get(prsi)).getMerchants()
												.size(); pmi++) {
											totalQuantity = totalQuantity + Double.parseDouble(

													((Merchant) ((Product) prList.get(prsi)).getMerchants().get(pmi))
															.getSplitTotalCount());
										}
										((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
									}
								}
							}
						}
						if ((merchantList != null) && (merchantList.size() > 0)) {
							for (int msi = 0; msi < merchantList.size(); msi++) {
								if (merchant.getId() == ((Merchant) merchantList.get(msi)).getId()) {
									mFlag = true;
									break;
								}
							}
						}
						if ((!mFlag) || (pdsi == 0)) {
							merchantList.add(merchant);
						}
						midorders = new Midorders();
						midorders.setBarcode(
								StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
						midorders.setGoodsname(
								StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
						midorders.setGoodssize(pdt.getString("unitName"));
						midorders.setOrderId(pdt.getString("zy_order_num"));
						midorders.setQuantity(new Double(Double.parseDouble(pdt.getString("fq"))).intValue());
						midorders.setShopid(pdt.getString("mnum"));
						midorders.setShopname(pdt.getString("mname"));
						midorders.setSku(pdt.getString("pnum"));
						midorders.setWmsorderId(pdt.getString("texoiId"));
						midordersList.add(midorders);
						pdsi++;
					}

					dcm.insertMidordersListByBatchPrepareStat(midordersList);
					dcm.insertMerchantListByBatchPrepareStat(merchantList);
					for (int gi = 0; gi < ArrayDATA_IDS.length; gi++) {
						pd.put("id", Integer.valueOf(Integer.parseInt(ArrayDATA_IDS[gi])));
						pd.put("ivt_state", Integer.valueOf(2));
						this.eXOrderService.editIvtState(pd);
					}
					pd.put("msg", "ok");
					this.sysLogService.saveLog("波次操作出库单成功", "成功");
				} else {
					this.sysLogService.saveLog("波次操作出库单返回商品信息为空", "失败");
					pd.put("msg", "no");
				}
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("出库单数据为空", "失败");
			}
			mv.setViewName("inventorymanagement/exwarehouseorder/wave_ex_product_list_gift");
			mv.addObject("varList", prList);
			mv.addObject("str", pd.getString("DATA_IDS"));
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}
		return mv;
	}

	@RequestMapping({ "addPurchaseOrder" })
	public ModelAndView addPurchaseOrder(Page page, String orderId, String type) {
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			List<PageData> warhouseList = this.warehouseService.listAll(pd);
			mv.addObject("wlist", warhouseList);
			mv.addObject("type", type);
			mv.setViewName("inventorymanagement/salesReturn/saleReturn_edit");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(value = { "Generatethestorereceipt" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String Generatethestorereceipt(Integer supplierId, String comment, String ordernum, String orderdate) {
		try {
			if ((ordernum != null) && (!ordernum.equals(""))) {
				PageData pd = new PageData();
				pd.put("ordernum", ordernum);
				pd = this.exwarehouseorderService.getExwarouseByorderNum(pd);
				pd.put("merchant_id", supplierId);
				pd.put("comment", comment);
				this.exwarehouseorderService.edit(pd);
				this.exwarehouseorderService.deleteExOrderItemByOrderNum(pd);
				return pd.get("order_num").toString() + "," + pd.getString("group_num") + "," + pd.getString("id");
			}
			PageData inferiorEnwarhouse = new PageData();
			inferiorEnwarhouse.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
			inferiorEnwarhouse.put("order_num", "CK_" + StringUtil.getStringOfMillisecond(""));
			inferiorEnwarhouse.put("merchant_id", supplierId);

			inferiorEnwarhouse.put("comment", comment);
			inferiorEnwarhouse.put("final_amount", "0.00");
			inferiorEnwarhouse.put("total_svolume", "0.00");
			inferiorEnwarhouse.put("total_weight", "0.00");
			inferiorEnwarhouse.put("is_ivt_order_print", Integer.valueOf(1));
			inferiorEnwarhouse.put("is_temporary", Integer.valueOf(2));
			inferiorEnwarhouse.put("ivt_state", Integer.valueOf(1));
			inferiorEnwarhouse.put("order_date", orderdate);
			inferiorEnwarhouse.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
			inferiorEnwarhouse.put("order_type", Integer.valueOf(2));
			this.exwarehouseorderService.save(inferiorEnwarhouse);
			return inferiorEnwarhouse.get("order_num").toString() + "," + inferiorEnwarhouse.getString("group_num")
					+ "," + inferiorEnwarhouse.getString("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = { "saveSaleReturn" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String saveSaleReturn(Integer supplierId, String comment, String ordernum, String orderdate,
			String managerName, String managerTel) {
		try {
			if ((ordernum != null) && (!ordernum.equals(""))) {
				PageData pd = new PageData();
				pd.put("ordernum", ordernum);
				pd = this.exwarehouseorderService.getExwarouseByorderNum(pd);
				pd.put("merchant_id", supplierId);
				pd.put("comment", comment);
				this.exwarehouseorderService.edit(pd);
				this.exwarehouseorderService.deleteExOrderItemByOrderNum(pd);
				return pd.get("order_num").toString() + "," + pd.getString("group_num") + "," + pd.getString("id");
			}
			PageData inferiorEnwarhouse = new PageData();
			inferiorEnwarhouse.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
			inferiorEnwarhouse.put("order_num", "CK_" + StringUtil.getStringOfMillisecond(""));
			inferiorEnwarhouse.put("merchant_id", supplierId);
			inferiorEnwarhouse.put("order_date", orderdate);
			inferiorEnwarhouse.put("manager_name", managerName);
			inferiorEnwarhouse.put("manager_tel", managerTel);
			inferiorEnwarhouse.put("comment", comment);
			inferiorEnwarhouse.put("final_amount", "0.00");
			inferiorEnwarhouse.put("total_svolume", "0.00");
			inferiorEnwarhouse.put("total_weight", "0.00");
			inferiorEnwarhouse.put("is_ivt_order_print", Integer.valueOf(1));
			inferiorEnwarhouse.put("is_temporary", Integer.valueOf(2));
			inferiorEnwarhouse.put("ivt_state", Integer.valueOf(1));
			inferiorEnwarhouse.put("order_date", orderdate);
			inferiorEnwarhouse.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
			inferiorEnwarhouse.put("order_type", Integer.valueOf(5));
			this.exwarehouseorderService.save(inferiorEnwarhouse);
			return inferiorEnwarhouse.get("order_num").toString() + "," + inferiorEnwarhouse.getString("group_num")
					+ "," + inferiorEnwarhouse.getString("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping({ "/purchaseReturnlistpage" })
	public ModelAndView purchaseReturnlistpage(Page page) {
		logBefore(this.logger, "列表EnWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			page.setPd(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}

			if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
				pd.put("ivt_state", pd.getString("ivt_state"));
			}
			List<PageData> varList = this.exwarehouseorderService.purchaseReturnlistpage(page);
			mv.setViewName("inventorymanagement/salesReturn/saleReturn_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "goEnwareorderPurchaseProductEdit" })
	public ModelAndView goEnwareorderPurchaseProductEdit(Page page, String orderId, String type) {
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);

			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);
			List<PageData> warhouseList = this.warehouseService.listAll(pd);
			mv.addObject("wlist", warhouseList);
			mv.setViewName("inventorymanagement/salesReturn/saleReturn_info");
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);

			//System.out.print(type);
			mv.addObject("type", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(value = { "updateExOrderTotalBoxNumber" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String updateExOrderTotalBoxNumber(Page page) {
		String result = "true";
		try {
			PageData pd = getPageData();
			String texoiIds = pd.getString("texoiIds");
			String quantitys = pd.getString("quantitys");

			if (!"".equals(texoiIds)) {
				PageData texioPd = new PageData();
				String[] idsStr = texoiIds.split(",");
				String[] numbersStr = quantitys.split(",");
				for (int i = 0; i < idsStr.length; i++) {
					texioPd = new PageData();
					texioPd.put("id", idsStr[i]);
					texioPd.put("total_count", numbersStr[i]);
					this.eXOrderService.editTotalCount(texioPd);
				}
				this.sysLogService.saveLog("波次操作出库单，修改出库单中商品整箱数量", "成功");
			} else {
				result = "false";
				this.sysLogService.saveLog("波次操作出库单，修改出库单中商品整箱数量", "失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "false";
		}
		return result;
	}

	@RequestMapping({ "/goUploadExcel" })
	public ModelAndView goUploadExcel() throws Exception {
		ModelAndView mv = getModelAndView();
		mv.setViewName("inventorymanagement/exwarehouseorder/uploadexcel");
		return mv;
	}

	@RequestMapping({ "/readExcel" })
	public ModelAndView readExcel(@RequestParam(value = "excel", required = false) MultipartFile file)
			throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		if ((file != null) && (!file.isEmpty())) {
			String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
			String fileName = FileUpload.fileUp(file, filePath, "userexcel");

			List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath, fileName, 1, 0, 0);

			List<PageData> productlist = new ArrayList();

			for (int i = 0; i < listPd.size(); i++) {
				if (!((PageData) listPd.get(i)).get("var0").equals("")) {
					Product product = new Product();
					product = this.productService
							.findProductByBarCode(((PageData) listPd.get(i)).get("var0").toString());
					String quantity = ((PageData) listPd.get(i)).get("var1").toString();
					boolean result = quantity.matches("[0-9]+");
					if ((result) && (product != null)) {
						PageData productdata = new PageData();
						productdata.put("productid", Long.valueOf(product.getId()));
						productdata.put("quantity", quantity);
						PageData merchprice = this.productpriceService
								.getSellingPriceById(productdata.getString("productid").toString());
						PageData supplieprice = this.productpriceService
								.getSupplierPriceById(productdata.getString("productid").toString());
						productdata.put("merchprice", merchprice.get("product_price"));
						productdata.put("supplierprice", supplieprice.get("product_price"));
						productdata.put("comment", ((PageData) listPd.get(i)).get("var3"));
						productlist.add(productdata);
					}
				}
			}

			PageData merchant = new PageData();
			merchant.put("merchant_name", ((PageData) listPd.get(0)).get("var2").toString());
			List<PageData> meList = this.merchantService.findByName(merchant);
			if ((meList.size() > 0) && (productlist.size() > 0)) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				PageData inferiorEnwarhouse = new PageData();
				inferiorEnwarhouse.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
				inferiorEnwarhouse.put("order_num", "CK_" + StringUtil.getStringOfMillisecond(""));
				inferiorEnwarhouse.put("merchant_id", ((PageData) meList.get(0)).get("id"));
				inferiorEnwarhouse.put("final_amount", "0.00");
				inferiorEnwarhouse.put("total_svolume", "0.00");
				inferiorEnwarhouse.put("total_weight", "0.00");
				inferiorEnwarhouse.put("is_ivt_order_print", Integer.valueOf(1));
				inferiorEnwarhouse.put("is_temporary", Integer.valueOf(2));
				inferiorEnwarhouse.put("ivt_state", Integer.valueOf(1));
				inferiorEnwarhouse.put("order_date", df.format(new Date()));
				inferiorEnwarhouse.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
				inferiorEnwarhouse.put("order_type", Integer.valueOf(2));
				this.exwarehouseorderService.save(inferiorEnwarhouse);
				for (int i = 0; i < productlist.size(); i++) {
					PageData orderitem = new PageData();
					orderitem.put("order_num", inferiorEnwarhouse.get("order_num"));
					orderitem.put("group_num", inferiorEnwarhouse.get("group_num"));
					orderitem.put("product_id", ((PageData) productlist.get(i)).get("productid"));
					orderitem.put("purchase_price", ((PageData) productlist.get(i)).get("supplierprice"));
					orderitem.put("sale_price", ((PageData) productlist.get(i)).get("merchprice"));
					orderitem.put("final_quantity", ((PageData) productlist.get(i)).get("quantity"));
					orderitem.put("creator", LoginUtil.getLoginUser().getUSERNAME());
					orderitem.put("comment", ((PageData) productlist.get(i)).get("comment"));
					orderitem.put("create_time", df.format(new Date()));
					orderitem.put("state", "1");
					orderitem.put("is_ivt_BK", "3");
					this.eXOrderItemService.save(orderitem);
				}
			}
		}

		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping({ "/downExcel" })
	public void downExcel(HttpServletResponse response) throws Exception {
		FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "存储出库单导入.xls",
				"存储出库单导入.xls");
	}

	@RequestMapping({ "print" })
	public ModelAndView print(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			List<PageData> listpd=new ArrayList<PageData>();
			pd = getPageData();
			pd.put("orderNum", orderId);
			/*page.setPd(pd);
			page.setShowCount(351);*/
			pd.put("parent_id", Integer.valueOf(0));
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			listpd.add(pd);
			PageData count=eXOrderItemService.fencangCount(pd);
			PageData pd1 = new PageData();
			pd1.put("orderNum", orderId);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductByIdForPrint(pd1);
			mv.addObject("count", count);
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("listpd",listpd);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
			mv.setViewName("inventorymanagement/exwarehouseorder/printa");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	/**
	 * 直配打印
	 * @param page
	 * @param orderId
	 * @param type
	 * @return
	 */
	@RequestMapping({ "directprint" })
	public ModelAndView directprint(Page page, String orderId, String type) {
		logBefore(this.logger, "直配打印");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			List<PageData> listpd=new ArrayList<PageData>();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			page.setShowCount(351);
			pd.put("parent_id", Integer.valueOf(0));
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getdirectExwarouseById(pd);
			listpd.add(pd);
			PageData count=eXOrderItemService.fencangCount(pd);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);
			mv.addObject("count", count);
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("listpd",listpd);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
			mv.setViewName("inventorymanagement/exwarehouseorder/directprinta");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	@RequestMapping({ "printlist" })
	public ModelAndView printlist(Page page, String city, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		List<List<Object>> listmap = new ArrayList<List<Object>>();
		Map<String,Object> map =new HashMap<String,Object>();
		PageData pad=new PageData();
		List<PageData> list = null;
		/*List<PageData> quantity=null;*/
		try {
			String c[]=city.split(",");
			List<PageData> varList=null;
			if(c.length>0){
				for (int i = 0; i < c.length; i++) {
					//订单
					PageData pd = new PageData();
					pd = getPageData();
					pd.put("cityid", c[i]);
					PageData pdd=new PageData();
					pdd.put("cityid",pd.getString("cityid"));
					pdd.put("ck_id", LoginUtil.getLoginUser().getCkId());
					pdd.put("order_type", 1);
					pdd.put("ivt_state", pd.getString("type"));
					pad.put("name", LoginUtil.getLoginUser().getNAME());
					page.setPd(pdd);
					page.setShowCount(351);
					pd.put("parent_id", Integer.valueOf(0));
					PageData sumOrder = new PageData();
					varList =exwarehouseorderService.list(page);
					list=exwarehouseorderService.printExOrder(varList);
					/*quantity=eXOrderItemService.printquantity(varList);*/
				    /*list =  new ArrayList<PageData>();;
					Map<String,Object> map1 = null;
					for (int j = 0; j < varList.size(); j++) {
						//详情
						map1 = new HashMap<String,Object>();
						pad.put("order_num", varList.get(j).getString("order_num").toString());
						String order_num=varList.get(j).getString("order_num");
						sumOrder = this.eXOrderItemService.selectSumExOrder(order_num);
						List<PageData> productType = this.productTypeService.findByParentId(pd);
						mv.addObject("productType", productType);
						pd = this.exwarehouseorderService.getExwarousegetId(pad);
						PageData count=eXOrderItemService.fencangCount(pad);
						
						map1.put("orderItemList", pageDate);
						map1.put("enwarhouse", pd);
						map1.put("sumOrder", sumOrder);
						map1.put("count", count);
						list.add(map1);
					}
					listmap.add(list);*/
					//List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageById(pad);
				}
				/*mv.addObject("quantity",quantity);*/
				mv.addObject("varList",varList);
				mv.addObject("list",list);
				mv.addObject("numlen",varList.size());
				mv.addObject("pdd",pad);
				mv.setViewName("inventorymanagement/exwarehouseorder/print");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	@RequestMapping({ "printpq" })
	public ModelAndView printpq(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			PageData pdd=new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			pdd.put("orderNum", orderId);
			List<PageData> pageDate = this.eXOrderItemService.getFjItemById(pdd);
			/*List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistPageProductById(page);*/
			mv.setViewName("inventorymanagement/exwarehouseorder/print_pq");
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping({ "/excelOfMerchant" })
	public ModelAndView excelOfMerchant() {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		DBConnectionManager dcm = new DBConnectionManager();

		try {
			Map<String, Object> dataMap = new HashMap();
			List<String> titles = new ArrayList();

			titles.add("门店名称");
			titles.add("门店储位编号");

			dataMap.put("titles", titles);
			List<Merchant> merchantList = dcm.selectStoresInfo();
			PageData vpd = null;
			List<PageData> varList = new ArrayList();
			if ((merchantList != null) && (merchantList.size() > 0)) {
				for (int i = 0; i < merchantList.size(); i++) {
					vpd = new PageData();

					vpd.put("var1", ((Merchant) merchantList.get(i)).getMerchantName());
					vpd.put("var2", ((Merchant) merchantList.get(i)).getImageUrl());

					varList.add(vpd);
				}
			} else {
				vpd = new PageData();
				vpd.put("var1", "门店信息不存在,请确认操作步骤正确.");
				vpd.put("var2", "");

				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv, dataMap);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/excelOfMerchants" })
	public ModelAndView excelOfMerchants() {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			String waveNum = pd.getString("waveNum");
			Map<String, Object> dataMap = new HashMap();
			List<String> titles = new ArrayList();
			StringUtil StringUtil = new StringUtil();
			titles.add("门店编号");
			titles.add("门店简称");
			titles.add("门店线路组");
			if ("1".equals(pd.getString("state"))) {
				titles.add("整箱总数");
			}
			dataMap.put("titles", titles);
			PageData vpd = null;
			List<PageData> pdList = new ArrayList();
			pdList = this.exwarehouseorderService.listOfMerchants(waveNum);
			List<PageData> varList = new ArrayList();
			if ((pdList != null) && (pdList.size() > 0)) {
				for (int i = 0; i < pdList.size(); i++) {
					vpd = new PageData();
					vpd.put("var1", ((PageData) pdList.get(i)).get("merchant_id"));
					vpd.put("var2", ((PageData) pdList.get(i)).get("short_name"));
					vpd.put("var3", ((PageData) pdList.get(i)).get("area_name"));
					if ("1".equals(pd.getString("state"))) {
						vpd.put("var4", ((PageData) pdList.get(i)).get("total_count"));
					}
					varList.add(vpd);
				}
			} else {
				vpd = new PageData();
				vpd.put("var1", "门店信息不存在,请确认操作步骤正确.");
				vpd.put("var2", "");
				vpd.put("var3", "");
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv, dataMap);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/excelOfMerchantsForPer" })
	public ModelAndView excelOfMerchantsForPer() {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			String waveNum = pd.getString("waveNum");
			Map<String, Object> dataMap = new HashMap();
			List<String> titles = new ArrayList();
			titles.add("门店编号");
			titles.add("门店简称");
			titles.add("门店线路组");
			if ("1".equals(pd.getString("state"))) {
				titles.add("散货总数量");
			}
			dataMap.put("titles", titles);
			PageData vpd = null;
			List<PageData> pdList = new ArrayList();
			pdList = this.exwarehouseorderService.listOfMerchantsForPer(waveNum);
			List<PageData> varList = new ArrayList();
			if ((pdList != null) && (pdList.size() > 0)) {
				for (int i = 0; i < pdList.size(); i++) {
					vpd = new PageData();
					vpd.put("var1", ((PageData) pdList.get(i)).get("merchant_id"));
					vpd.put("var2", ((PageData) pdList.get(i)).get("short_name"));
					vpd.put("var3", ((PageData) pdList.get(i)).get("area_name"));
					if ("1".equals(pd.getString("state"))) {
						vpd.put("var4", ((PageData) pdList.get(i)).get("per_count"));
					}
					varList.add(vpd);
				}
			} else {
				vpd = new PageData();
				vpd.put("var1", "门店信息不存在,请确认操作步骤正确.");
				vpd.put("var2", "");
				vpd.put("var3", "");
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv, dataMap);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "getInfoOfPerSystem" })
	@ResponseBody
	public Object getInfoOfPerSystem(Page page, String orderId, String type) {
		logBefore(this.logger, "判断分拣系统是否已经进行“清空数据”操作");

		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		PageData vpd = new PageData();
		String result = "";
		List<Merchant> ms = new ArrayList();
		try {
			DBConnectionManager dcm = new DBConnectionManager();
			ms = dcm.selectStoresInfo();
			if ((ms != null) && (ms.size() > 0)) {
				pd.put("msg", "ok");
				map.put("msg", "1");
			} else {
				pd.put("msg", "no");
				map.put("msg", "2");
			}
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	@RequestMapping({ "importToPerSystem" })
	@ResponseBody
	public Object importToPerSystem() {
		logBefore(this.logger, "导入到标签系统，波次操作页面设置按钮， 由操作人员实时操作");
		PageData pd = new PageData();
		PageData pde = null;
		Map<String, Object> map = new HashMap();
		List<PageData> pdList = new ArrayList();
		String msg = "";

		try {
			pd = getPageData();
			String waveNum = pd.getString("DATA_IDS");
			pd = getPageData();
			List<Product> prList = new ArrayList();
			if (!StringUtil.isEmpty(waveNum)) {
				Merchant merchant = null;
				DBConnectionManager dcm = new DBConnectionManager();
				List<Merchant> merchantList = new ArrayList();
				List<Midorders> midordersList = new ArrayList();
				Midorders midorders = null;
				boolean mFlag = false;

				pd.put("pd", waveNum);
				List<PageData> pds = this.exwarehouseorderService.getExwarouseItemByWaveNum(waveNum);
				int psi = 0;
				if ((pds != null) && (pds.size() > 0)) {
					for (PageData pdt : pds) {
						mFlag = false;
						merchant = new Merchant();
						merchant.setId(
								Long.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
						merchant.setMerchantName(
								StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
						merchant.setMerchantNum(
								StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
						merchant.setProductCount(StringUtil.isEmpty(pdt.getString("fq")) ? "0" : pdt.getString("fq"));
						merchant.setTexoiId(Long.parseLong(
								StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));

						merchant.setSplitPerCount(pdt.getString("per_count"));
						if ((merchantList != null) && (merchantList.size() > 0)) {
							for (int msi = 0; msi < merchantList.size(); msi++) {
								if (merchant.getId() == ((Merchant) merchantList.get(msi)).getId()) {
									mFlag = true;

									if (Integer.parseInt(merchant.getSplitPerCount()) <= 0)
										break;
									((Merchant) merchantList.get(msi)).setImageUrl("1");

									break;
								}
							}
						}

						if (!mFlag) {
							if (Integer.parseInt(merchant.getSplitPerCount()) > 0) {
								psi++;

								merchant.setImageUrl("1");
							}
							merchantList.add(merchant);
						}

						midorders = new Midorders();
						midorders.setBarcode(
								StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
						midorders.setGoodsname(
								StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
						midorders.setGoodssize(pdt.getString("unitName"));
						midorders.setOrderId(pdt.getString("zy_order_num"));
						midorders.setQuantity(Integer.parseInt(
								StringUtil.isEmpty(pdt.getString("per_count")) ? "0" : pdt.getString("per_count")));
						midorders.setShopid(merchant.getMerchantNum());
						midorders.setShopname(merchant.getMerchantName());

						midorders.setSku(pdt.getString("pnum"));
						midorders.setWmsorderId(pdt.getString("texoiId"));

						midordersList.add(midorders);
					}
					//System.out.println("==3===psi:" + psi);

					dcm.insertMidordersListByBatchPrepareStat(midordersList);
					dcm.insertMerchantListByBatchPrepareStat(merchantList);
					msg = "订单导入分拣系统成功";
					pd.put("msg", msg);
				} else {
					msg = "订单信息为空";
					pd.put("msg", "该波次分拣已结束");
				}
			} else {
				msg = "波次编码为空";
				pd.put("msg", "请重新操作");
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

	/**
	 * 下载出库单模板
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "/downExOrderExcel" })
	public void downExOrderExcel(HttpServletResponse response) throws Exception {
		FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "SellingOrder.xlsx",
				"Tarot系统-导入出库单模板.xlsx");
	}
	
	/**
	 * 跳转导入Excel表
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/goExUploadExcel" })
	public ModelAndView goEnUploadExcel(String type) throws Exception {
		ModelAndView mv = getModelAndView();
		mv.addObject("type", type);
		mv.setViewName("inventorymanagement/exwarehouseorder/importExcelPage");
		return mv;
	}

	/**
	 * excel表导入出库单
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/readExOrderExcel" })
	public ModelAndView readExOrderExcel(@RequestParam(value = "excel", required = false) MultipartFile file)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		String operationMsg = "导入出库单到数据库";
		logBefore(logger, operationMsg);
		PageData pdM = null;
		try {

			User user = LoginUtil.getLoginUser();
			if (file != null && !file.isEmpty()) {
				String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
				String fileName = FileUpload.fileUp(file, filePath, "exOrderExcel");
				List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
				if (listPd != null && listPd.size() > 0) {
					EXOrder exorder = null;
					EXOrderItem exorderitem = null;
					List<EXOrder> listEXOrder = new ArrayList();
					List<EXOrderItem> listExOrderItem = new ArrayList();
					Merchant merchant = null;
					Product product = null;
					List<Product> listProduct = new ArrayList();
					PageData pdE = null;
					boolean pflag = false;
					List<PageData> pdEList = new ArrayList();
					List<PageData> pdPriceList = new ArrayList();
					PageData pdPrice = null;

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
					String fc_order_num="";
					String comment = "";
					String contactPersonM = "";
					String contactPersonMobileM = "";
					String contactPersonAddressM = "";
					StringBuffer rowMes = null;
					StringBuffer reasonMes = null;
					for (int i = 0; i < listPd.size(); i++) {
						//System.out.println(">>>>>>>>>>>>>"+ i+"*****/");
						merchant = new Merchant();
						product = new Product();
						pdE = new PageData();
						pdM = new PageData();
						exorder = new EXOrder();
						exorderitem = new EXOrderItem();
						k = -1;
						u = -1;
						rowMes = new StringBuffer();
						reasonMes = new StringBuffer();
						if (listPd.get(i).getString("var0").trim().equals("")) {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("reason", "数据表中有空行");
							pdEList.add(pdE);
							mv.addObject("varList", pdEList);
							mv.setViewName("procurement/ordergroup/importExcelPage");
							return mv;
						}
						//var1单据号 var2门店ID var8 编码 var9 商品名称 var10条码  var2 门店ID var17配送数量
						if(!StringUtil.isEmpty(listPd.get(i).get("var1").toString())){
							fc_order_num=listPd.get(i).get("var1").toString();
						}
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var2").toString())) {
							pdM.put("id", ((PageData) listPd.get(i)).get("var2").toString());

							pds = this.merchantService.findById(pdM);
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

								if (!StringUtil.isEmpty(pds.get("id").toString())) {
									merchant.setId(Long.parseLong(pds.get("id").toString()));

									if ((listEXOrder != null) && (listEXOrder.size() > 0)) {
										for (int m = 0; m < listEXOrder.size(); m++) {
											if (((EXOrder) listEXOrder.get(m)).getMerchant().getId() == Long
													.parseLong(pds.get("id").toString())) {
												k = m;
												exorder = (EXOrder) listEXOrder.get(k);
												break;
											}
										}
									}
								} else {
									pdE.put("line", Integer.valueOf(i + 2));
									pdE.put("rowS", "C");
									pdE.put("reasonS", "商户便利店信息不存在");
								}
							} else {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("rowS", "C");
								pdE.put("reasonS", "商户便利店信息不存在");
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("rowS", "C");
							pdE.put("reasonS", "商户便利店信息数据为空");
						}
						//var8 编码 var9 商品名称 var10条码  var2 门店ID var17配送数量 var 
						if ((!StringUtil.isEmpty((listPd.get(i)).get("var10").toString())) && (!StringUtil.isEmpty((listPd.get(i)).get("var8").toString()))) {
							pdM.put("barCode", (listPd.get(i)).get("var10").toString());
							pdM.put("hostCode", (listPd.get(i)).get("var8").toString());
							product =productService.findById(pdM);
							if (product != null) {
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
								pdPrice = (PageData) pdPriceList.get(0);

								if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
									priceOfProduct1 = Double.parseDouble(pdPrice.getString("product_price").toString());
									pflag = false; //////////////////
									if ((listProduct != null) && (listProduct.size() > 0)) {
										for (int pi = 0; pi < listProduct.size(); pi++) {
											for (int j = 0; j < listExOrderItem.size(); j++) {
												if (product.getId() == (listProduct.get(pi)).getId()) {
													if(listExOrderItem.get(j).getComment() ==listPd.get(i).getString("var5").toString())
														pflag = true;
														break;
											}
											}
										}
									}
									pdPrice.put("product_id", Long.valueOf(product.getId()));
									pdPrice.put("price_type", Integer.valueOf(2));
									pdPriceList = this.productService.findPriceOfProductList(pdPrice);
									pdPrice = (PageData) pdPriceList.get(0);
									if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
										priceOfProduct2 = Double
												.parseDouble(pdPrice.getString("product_price").toString());
									}
									if (!pflag) {
										listProduct.add(product);
									}
									if (k > -1) {
										for (int ex = 0; ex < listExOrderItem.size(); ex++) {
											if ((exorder.getOrderNum() == (listExOrderItem.get(ex)).getOrderNum())
												&& ((listExOrderItem.get(ex)).getProduct().getId() == product.getId())) {
												u = ex;
												break;
											}
										}
									}
								} else {
									pdE.put("line", Integer.valueOf(i + 2));
									pdE.put("rowP", "I和K");
									pdE.put("reasonP", "商品不存在");
								}
							} else {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("rowP", "I和K");
								pdE.put("reasonP", "商品不存在");
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("rowP", "I和K");
							pdE.put("reasonP", "商品数据为空");
						}

						if (((listPd.get(i)).get("var17") != null)
								&& (((PageData) listPd.get(i)).get("var17") != "")
								&& (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var17").toString()))) {
							quantityOfProduct = Double.parseDouble(((PageData) listPd.get(i)).get("var17").toString());
							if (u > -1) {
								quantityOfProduct += ((EXOrderItem) listExOrderItem.get(u)).getQuantity();
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("rowC", "R");
							pdE.put("reasonC", "商品订购数量为空");
						}

						if ((listPd.get(i)).get("var20") != null) {
							comment = "";

							if (k > -1) {
								if(!listEXOrder.get(k).getComment().contains(listPd.get(i).getString("var20").toString())){
									comment = (listEXOrder.get(k)).getComment()
											+ (listPd.get(i)).get("var20").toString();
								}else {
									comment=listEXOrder.get(k).getComment();
								}
								
							} else {
								comment = (listPd.get(i)).get("var20").toString();
							}
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
								exorder.setGroupNum(groupNum);
								exorder.setOrderNum(
										"CK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
								exorder.setCheckedState(1);
								exorder.setOrderDate(Tools.date2Str(new Date()));
								exorder.setManagerName(contactPersonM);
								exorder.setManagerTel(contactPersonMobileM);
								exorder.setDeliverAddress(contactPersonAddressM);
								exorder.setIs_ivt_order_print(0);
								exorder.setIs_temporary(2);
								exorder.setUser(user);
								exorder.setMerchant(merchant);
								exorder.setOrderType(1);
								exorder.setComment(comment);
								exorder.setCkId(user.getCkId());
							}
							if (u == -1) {
								exorderitem.setGroupNum(exorder.getGroupNum());
								exorderitem.setOrderNum(exorder.getOrderNum());

								exorderitem.setPurchasePrice(priceOfProduct1);
								exorderitem.setSalePrice(priceOfProduct2);
								exorderitem.seteXTime(DateUtil.getAfterDayDate("3"));
								exorderitem.setProduct(product);
								exorderitem.setCreator(user.getNAME());
								exorderitem.setFc_order_num(fc_order_num);
								/*if(listPd.get(i).getString("var5").toString()!=null) {
									exorderitem.setComment((listPd.get(i)).getString("var5").toString());
								}*/
								exorderitem.setComment("");
								exorderitem.setProduct(product);
								exorderitem.setQuantity(quantityOfProduct);
								exorderitem.setFinalQuantity(quantityOfProduct);
								listExOrderItem.add(exorderitem);
							}
							if (u > -1) {
								(listExOrderItem.get(u)).setQuantity(quantityOfProduct);
								(listExOrderItem.get(u)).setFinalQuantity(quantityOfProduct);
								/*if (((listPd.get(i)).get("var5") != null)) {
									(listExOrderItem.get(u))
											.setComment((listExOrderItem.get(u)).getComment() + ";"
													+ (listPd.get(i)).get("var5").toString());
								}*/
								/*if (((PageData) listPd.get(i)).get("var21") != null) {
									((EXOrderItem) listExOrderItem.get(u))
											.setZyOrderNum(((EXOrderItem) listExOrderItem.get(u)).getZyOrderNum() + ";"
													+ ((PageData) listPd.get(i)).get("var21").toString());
								}*/
							}
							if (k > -1) {
								(listEXOrder.get(k)).setAmount(MathUtil.mulAndAdd(
										Double.parseDouble((listPd.get(i)).get("var17").toString()),
										priceOfProduct2, (listEXOrder.get(k)).getFinalAmount()));
								(listEXOrder.get(k))
										.setFinalAmount((listEXOrder.get(k)).getAmount());
								(listEXOrder.get(k)).setTotalSvolume(MathUtil.mulAndAdd(quantityOfProduct,
										skuVolumeEx, (listEXOrder.get(k)).getTotalSvolume()));
								(listEXOrder.get(k)).setTotalWeight(MathUtil.mulAndAdd(quantityOfProduct,
										skuWeightEx, (listEXOrder.get(k)).getTotalWeight()));
								(listEXOrder.get(k)).setComment(comment);
							}
							if (k == -1) {
								exorder.setFinalAmount(
										MathUtil.mul(exorderitem.getQuantity(), exorderitem.getSalePrice()));
								exorder.setAmount(MathUtil.mul(exorderitem.getQuantity(), exorderitem.getSalePrice()));

								exorder.setTotalSvolume(MathUtil.mul(exorderitem.getQuantity(), skuVolumeEx));
								exorder.setTotalWeight(MathUtil.mul(exorderitem.getQuantity(), skuWeightEx));
								listEXOrder.add(exorder);
							}
						}
					}
					if ((pdEList != null) && (pdEList.size() > 0) && (pdEList.get(0) != null)
							&& (!StringUtil.isEmpty((pdEList.get(0)).getString("line")))) {
						mv.addObject("varList", pdEList);
					}
					if ((pdEList == null) || (pdEList.size() <= 0)
							|| (StringUtil.isEmpty((pdEList.get(0)).getString("line")))) {
						pdEList = null;
					}
					int saveFlag = 1;
					int reFlag = 0;
					if (pdEList == null) {
						if (listEXOrder != null && listEXOrder.size() > 0) {
							// 出库订单
							logMidway(logger, "本次导入出库订单数组大小为：" + listEXOrder.size());
							reFlag = eXOrderService.saveList(listEXOrder);
							saveFlag = 2;
							if (listExOrderItem != null && listExOrderItem.size() > 0) {
								// 出库订单条目
								logMidway(logger, "出库条目数组大小为：" + listExOrderItem.size());
								reFlag = eXOrderItemService.saveList(listExOrderItem);
								saveFlag = 3;
									if (pdOfOrderGroup != null) {
										pdM.put("group_num", og.getOrderGroupNum());
										reFlag = orderGroupService.saveOrderGroup(og);
										saveFlag = -1;
									}
								 else {
									saveFlag = -1;
								}
							} else {
								this.logMidway(logger, " 出库订单数组条目为空，请检查处理过程");
							}
						} else {
							this.logMidway(logger, " 出库订单数组为空，请检查处理过程");
						}
					}// 数据有错不保存直接在页面告知错误内容
					if (saveFlag > 1) {
						eXOrderService.deleteList(listEXOrder);
					}
					if (saveFlag > 2) {
						eXOrderItemService .deleteList(listExOrderItem);
						orderGroupService.delete(pdM);
					}
					if (saveFlag == -1) {
						this.logMidway(logger, "导入出库订单和出库订单条目成功，返回状态saveFlag="
								+ saveFlag);
					} else {
						this.logMidway(logger, "导入出库订单和出库订单条目失败，返回状态saveFlag="
								+ saveFlag);
					}
					
			        merchant = null;
			        exorder = null;
			        listEXOrder = null;
			        exorderitem = null;
			        listExOrderItem = null;
			        pdE = null;
			        product = null;
			        listProduct = null;
			        pdPrice = null;
			        pdM = null;
			        pds = null;
			        if (saveFlag == -1 && pdEList == null) {// 过程成功且处理完成
						mv.addObject("errorMsg", "数据导入成功。");
						mv.addObject("msg", "success");
					} else {
						mv.addObject("errorMsg", "请检查导入Excel表格或数据后,重新操作。");
					}
					
					
				} else {
					// 表格中数据为空
					mv.addObject("errorMsg", "数据表中没有数据");
				}
			}
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mv.addObject("errorMsg", "请检查商品："+pdM.getString("product_name")+"数据是否准确,重新操作。");
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		mv.setViewName("procurement/ordergroup/importExcelPage");
		return mv;
	}

	/**
	 * 审核出库单
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/examine" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String examine() {
		String result = "";
		logBefore(this.logger, "审核ExWarehouseOrder");
		PageData pd = new PageData();
		try {
			pd = getPageData();
			String DATA_IDS = pd.getString("DATA_IDS");
			if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
				String[] ArrayDATA_IDS = DATA_IDS.split(",");
				result = this.exwarehouseorderService.toExamine(ArrayDATA_IDS);
				if ("true".equals(result)) {
					this.sysLogService.saveLog("审核ExWarehouseOrder", "成功");
				} else {
					this.sysLogService.saveLog("审核ExWarehouseOrder", "失败");
				}
			}
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
			result = e.toString();
		} finally {
			logAfter(this.logger);
		}
		return result;
	}

	@RequestMapping({ "fcPrint" })
	public ModelAndView fcPrint(Page page, String city) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		List<List<Object>> listmap = new ArrayList<List<Object>>();
		Map<String,Object> map =new HashMap<String,Object>();
		PageData pad=new PageData();
		List<PageData> list = null;
		/*List<PageData> quantity=null;*/
		try {
			String c[]=city.split(",");
			List<PageData> varList=null;
			if(c.length>0){
				for (int i = 0; i < c.length; i++) {
					//订单
					PageData pd = new PageData();
					pd = getPageData();
					pd.put("cityid", c[i]);
					PageData pdd=new PageData();
					pdd.put("cityid",pd.getString("cityid"));
					pdd.put("ck_id", LoginUtil.getLoginUser().getCkId());
					pdd.put("order_type", 1);
					pad.put("name", LoginUtil.getLoginUser().getNAME());
					page.setPd(pdd);
					page.setShowCount(351);
					pd.put("parent_id", Integer.valueOf(0));
					PageData sumOrder = new PageData();
					varList =exwarehouseorderService.list(page);
					list=exwarehouseorderService.printExOrder(varList);
				}
				mv.addObject("varList",varList);
				mv.addObject("list",list);
				mv.addObject("numlen",varList.size());
				mv.addObject("pdd",pad);
				mv.setViewName("inventorymanagement/exwarehouseorder/fcPrint");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	/**
	 * 直配订单导出
	 * @return
	 */
	@RequestMapping(value="/exorderlistexcel")
 	public ModelAndView exorderlistexcel(){
 		ModelAndView mv=new ModelAndView();
 		PageData pd=new PageData();
 		String operationMsg="直配订单导出";
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
 			titles.add("销售价格"); // 3
 			titles.add("订购数量"); // 3
 			titles.add("实际数量"); // 3
 			titles.add("赠品数量"); // 6
 			titles.add("备注"); // 7
 			titles.add("门店编码"); // 9
 			titles.add("门店简称"); // 8
 			titles.add("出库日期"); // 8\
 			titles.add("状态"); // 8
 			titles.add("地区"); // 8
 			map.put("titles", titles);
 			List<PageData> listpd=exwarehouseorderService.exorderlistexcel(pd);
 			List<PageData> varList=new ArrayList<PageData>();
 			for (int i = 0; i < listpd.size(); i++) {
 				PageData pdd=new PageData();
 				pdd.put("var1", listpd.get(i).getString("group_num"));
 				pdd.put("var2", listpd.get(i).getString("order_num"));
 				pdd.put("var3", listpd.get(i).getString("product_num"));
 				pdd.put("var4", listpd.get(i).getString("bar_code"));
 				pdd.put("var5", listpd.get(i).getString("product_name"));
 				pdd.put("var6", listpd.get(i).getString("sale_price"));
 				pdd.put("var7", listpd.get(i).getString("quantity"));
 				pdd.put("var8", listpd.get(i).getString("final_quantity"));
 				pdd.put("var9", listpd.get(i).getString("gift_quantity"));
 				pdd.put("var10", listpd.get(i).getString("comment"));
 				pdd.put("var11", listpd.get(i).getString("merchant_num"));
 				pdd.put("var12", listpd.get(i).getString("short_name"));
 				pdd.put("var13", listpd.get(i).getString("order_date"));
 				if(listpd.get(i).getString("ivt_state").equals("1")){
 					pdd.put("var14", "新出库单");
 				}else{
 					pdd.put("var14", "订单已完成");
 				}
 				pdd.put("var15", listpd.get(i).getString("area_name"));
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
	 * 出库图表
	 * @return
	 */
	@RequestMapping(value="/chukulist")
	public ModelAndView chukulist() {
		ModelAndView mv=new ModelAndView();
		String operationMsg="跳转图表页面";
		try {
			mv.setViewName("inventorymanagement/exwarehouseorderchart/chart");
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 出库同比图表
	 * @return
	 */
	@RequestMapping(value="/cktblist")
	public ModelAndView cktblist() {
		ModelAndView mv=new ModelAndView();
		String operationMsg="跳转图表页面";
		try {
			mv.setViewName("inventorymanagement/exwarehouseorderchart/tbchart");
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	@RequestMapping(value="/arrival")
	@ResponseBody
	public String arrival() {
		String operationMsg = "显示数量";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		JSONObject obj=new JSONObject();
		try {
			pd=this.getPageData();
			pd.put("group_num", pd.getString("groupnum"));
			List<PageData> listpd=exwarehouseorderService.chart(pd);
			List<String> listgroup=new ArrayList<>();
			List<String> listquantity=new ArrayList<>();
			List<String> listprice=new ArrayList<>();
			if(listpd.size()>0) {
				for (PageData pdd : listpd) {
					listgroup.add(pdd.getString("group_num"));
					double sumfinalquantity=Double.valueOf(pdd.getString("final_quantity")==null?0:Double.valueOf(pdd.getString("final_quantity")));
					double sumprice=Double.valueOf(pdd.getString("sumprice")==null?0:Double.valueOf(pdd.getString("sumprice")));
					listquantity.add(sumfinalquantity+"");
					listprice.add(sumprice+"");
				}
				obj.put("listgroup", listgroup);
				obj.put("listquantity", listquantity);
				obj.put("listprice", listprice);
			}
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return obj.toString();
	}
	
	/**
	 * 出库同比图表
	 */
	@RequestMapping(value="/tbarrival")
	@ResponseBody
	public String tbarrival() {
		String operationMsg = "显示数量";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		JSONObject obj=new JSONObject();
		try {
			pd=this.getPageData();
			pd.put("group_num", pd.getString("groupnum"));
			List<PageData> listpd=exwarehouseorderService.chart(pd);
			List<String> listgroup=new ArrayList<>();
			List<String> listquantity=new ArrayList<>();
			List<String> listprice=new ArrayList<>();
			PageData pad=null;
			if(listpd.size()>0) {
				for (int i = 0; i < listpd.size(); i++){
					pad=new PageData();
					String dq= listpd.get(i).getString("group_num");
					String sub=dq.substring(3, 11);
					SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
					//字符串转日期
					Date date=sdf.parse(sub);
					//System.out.println(date);
					Calendar c=Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.YEAR, -1);
					Date y=c.getTime();
					String year=sdf.format(y);
					pad.put("group_num", year);
					//查询去年数据
					List<PageData> p=exwarehouseorderService.charts(pad);
					/*listgroup.add(p.get(i).getString("group_num"));*/
					if(p.size()>0) {
						listgroup.add(p.get(0).getString("group_num"));
						double quniansum=Double.valueOf(p.get(0).getString("final_quantity")==null?0:Double.valueOf(p.get(0).getString("final_quantity")));
						double quniansumprice=Double.valueOf(p.get(0).getString("sumprice")==null?0:Double.valueOf(p.get(0).getString("sumprice")));
						listquantity.add(quniansum+"");
						listprice.add(quniansumprice+"");
					}else {
						listgroup.add("GP_"+pad.getString("group_num"));
						double quniansum=0;
						double quniansumprice=0;
						listquantity.add(quniansum+"");
						listprice.add(quniansumprice+"");
					}
					//保存到list
					listgroup.add(listpd.get(i).getString("group_num"));
					double sumfinalquantity=Double.valueOf(listpd.get(i).getString("final_quantity")==null?0:Double.valueOf(listpd.get(i).getString("final_quantity")));
					double sumprice=Double.valueOf(listpd.get(i).getString("sumprice")==null?0:Double.valueOf(listpd.get(i).getString("sumprice")));
					listquantity.add(sumfinalquantity+"");
					listprice.add(sumprice+"");
				}
				obj.put("listgroup", listgroup);
				obj.put("listquantity", listquantity);
				obj.put("listprice", listprice);
			}
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return obj.toString();
	}
	
	/**
	 * 出库环比图表
	 */
	@RequestMapping(value="/hbarrival")
	@ResponseBody
	public String hbarrival() {
		String operationMsg = "显示数量";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		JSONObject obj=new JSONObject();
		try {
			pd=this.getPageData();
			pd.put("group_num", pd.getString("groupnum"));
			List<PageData> listpd=exwarehouseorderService.chart(pd);
			List<String> listgroup=new ArrayList<>();
			List<String> listquantity=new ArrayList<>();
			List<String> listprice=new ArrayList<>();
			PageData pad=null;
			if(listpd.size()>0) {
				for (int i = 0; i < listpd.size(); i++){
					pad=new PageData();
					String dq= listpd.get(i).getString("group_num");
					String sub=dq.substring(3, 11);
					SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
					//字符串转日期
					Date date=sdf.parse(sub);
					//System.out.println(date);
					Calendar c=Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.MONTH, -1);
					Date m=c.getTime();
					String month=sdf.format(m);
					pad.put("group_num", month);
					//查询去年数据
					List<PageData> p=exwarehouseorderService.charts(pad);
					if(p.size()>0) {
						listgroup.add(p.get(0).getString("group_num"));
						double topmonthsum=Double.valueOf(p.get(0).getString("final_quantity")==null?0:Double.valueOf(p.get(0).getString("final_quantity")));
						double topmonthsumprice=Double.valueOf(p.get(0).getString("sumprice")==null?0:Double.valueOf(p.get(0).getString("sumprice")));
						listquantity.add(topmonthsum+"");
						listprice.add(topmonthsumprice+"");
					}else {
						listgroup.add("GP_"+pad.getString("group_num"));
						double topmonthsum=0;
						double topmonthsumprice=0;
						listquantity.add(topmonthsum+"");
						listprice.add(topmonthsumprice+"");
					}
					//保存到list
					listgroup.add(listpd.get(i).getString("group_num"));
					double sumfinalquantity=Double.valueOf(listpd.get(i).getString("final_quantity")==null?0:Double.valueOf(listpd.get(i).getString("final_quantity")));
					double sumprice=Double.valueOf(listpd.get(i).getString("sumprice")==null?0:Double.valueOf(listpd.get(i).getString("sumprice")));
					listprice.add(sumprice+"");
					listquantity.add(sumfinalquantity+"");
				
				}
				obj.put("listgroup", listgroup);
				obj.put("listquantity", listquantity);
				obj.put("listprice", listprice);
			}
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return obj.toString();
	}
	
	@RequestMapping(value="/exportorder")
	public ModelAndView exportorder() {
		ModelAndView mv=new ModelAndView();
		String operationMsg="进入导出页面";
		mv.setViewName("inventorymanagement/exwarehouseorder/orderExport");
		return mv;
	}
	@RequestMapping(value="/xsportexcel")
	public ModelAndView xsportexcel() {
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="导出出库订单信息";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String group_num=pd.getString("groupnum");
			if(group_num!=null && group_num!="") {
				pd.put("group_num", group_num);
			}
			pd.put("ROLE_ID",LoginUtil.getLoginUser().getROLE_ID());
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			Map<String, Object> map=new HashMap<String,Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("批次号");
 			titles.add("订单号");
 			titles.add("商品编码");
 			titles.add("商品条形码");
 			titles.add("商品名称");
 			titles.add("销售价格");
 			titles.add("订购数量");
 			titles.add("实际数量");
 			titles.add("赠品数量");
 			titles.add("备注");
 			titles.add("门店编码");
 			titles.add("门店名称");
 			titles.add("门店简称");
 			titles.add("创建日期");
 			titles.add("状态");
 			titles.add("订单类型");
 			titles.add("是否撤单");
 			titles.add("地区");
 			map.put("titles", titles);
 			List<PageData> listpd=exwarehouseorderService.xslistpd(pd);
 			List<PageData> varList=new ArrayList<PageData>();
 			if(listpd.size()>0) {
 				for (int i = 0; i < listpd.size(); i++) {
 					PageData pdd=new PageData();
	 				pdd.put("var1", listpd.get(i).getString("group_num"));
	 				pdd.put("var2", listpd.get(i).getString("order_num"));
	 				pdd.put("var3", listpd.get(i).getString("product_num"));
	 				pdd.put("var4", listpd.get(i).getString("bar_code"));
	 				pdd.put("var5", listpd.get(i).getString("product_name"));
	 				pdd.put("var6", listpd.get(i).getString("sale_price"));
	 				pdd.put("var7", listpd.get(i).getString("quantity"));
	 				pdd.put("var8", listpd.get(i).getString("final_quantity"));
	 				pdd.put("var9", listpd.get(i).getString("gift_quantity"));
	 				pdd.put("var10", listpd.get(i).getString("comment"));
	 				pdd.put("var11", listpd.get(i).getString("merchant_num"));
	 				pdd.put("var12", listpd.get(i).getString("merchant_name"));
	 				pdd.put("var13", listpd.get(i).getString("short_name"));
	 				pdd.put("var14", listpd.get(i).getString("order_date"));
	 				
	 				if(listpd.get(i).getString("checked_state").equals("1")){
	 					pdd.put("var15", "未审核");
	 				}else{
	 					pdd.put("var15", "已审核");
	 				}
	 				if(listpd.get(i).getString("type").equals("1")) {
	 					pdd.put("var16", "加急订单");
	 				}else if(listpd.get(i).getString("type").equals("2")) {
	 					pdd.put("var16", "仓配订单");
	 				}
	 				if(listpd.get(i).getString("order_type").equals("1")){
	 					pdd.put("var17", "未撤单");
	 				}else{
	 					pdd.put("var17", "已撤单");
	 				}
	 				pdd.put("var18", listpd.get(i).getString("area_name"));
	 				varList.add(pdd);
 					
 				}
	 			map.put("varList", varList);
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
 			}else{
 				map.put("varList", new ArrayList());
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
 			}
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	@RequestMapping(value="/exportexcel")
	public ModelAndView exportexcel() {
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="导出出库订单信息";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String group_num=pd.getString("groupnum");
			if(group_num!=null && group_num!="") {
				pd.put("group_num", group_num);
			}
			pd.put("ROLE_ID",LoginUtil.getLoginUser().getROLE_ID());
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			Map<String, Object> map=new HashMap<String,Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("站点编码");
 			titles.add("站点名称");
 			titles.add("站简称");
 			titles.add("分公司名称");
 			titles.add("商品编码");
 			titles.add("商品条形码");
 			titles.add("商品名称");
 			titles.add("箱装数");
 			titles.add("商品类型");
 			titles.add("单位");
 			titles.add("商品零售价");
 			titles.add("订货数量");
 			titles.add("出货数量");
 			titles.add("赠品");
 			titles.add("出库金额");
 			titles.add("订单总金额");
 			titles.add("供应商编码");
 			titles.add("供应商名称");
 			map.put("titles", titles);
 			List<PageData> listpd=exwarehouseorderService.listpd(pd);
 			List<PageData> varList=new ArrayList<PageData>();
 			if(listpd.size()>0) {
 				for (int i = 0; i < listpd.size(); i++) {
 					/*if(listpd.get(i).getString("confirmed").equals("1") || listpd.get(i).getString("type").equals("1")) {*/
 					PageData pdd=new PageData();
	 				pdd.put("var1", listpd.get(i).getString("站点编码"));
	 				pdd.put("var2", listpd.get(i).getString("站点名称"));
	 				pdd.put("var3", listpd.get(i).getString("站简称"));
	 				pdd.put("var4", listpd.get(i).getString("分公司名称"));
	 				pdd.put("var5", listpd.get(i).getString("商品编码"));
	 				pdd.put("var6", listpd.get(i).getString("商品条形码"));
	 				pdd.put("var7", listpd.get(i).getString("商品名称"));
	 				pdd.put("var8", listpd.get(i).getString("箱装数"));
	 				pdd.put("var9", listpd.get(i).getString("商品类型"));
	 				pdd.put("var10", listpd.get(i).getString("单位"));
	 				pdd.put("var11", listpd.get(i).getString("商品零售价"));
	 				pdd.put("var12", listpd.get(i).getString("订货数量"));
	 				pdd.put("var13", listpd.get(i).getString("出货数量"));
	 				pdd.put("var14", listpd.get(i).getString("赠品"));
	 				pdd.put("var15", listpd.get(i).getString("出库金额"));
	 				pdd.put("var16", listpd.get(i).getString("订单总金额"));
	 				pdd.put("var17", listpd.get(i).getString("供应商编码"));
	 				pdd.put("var18", listpd.get(i).getString("供应商名称"));

	 				/*if(listpd.get(i).getString("ivt_state").equals("1")){
	 					pdd.put("var15", "未审核");
	 				}else if(listpd.get(i).getString("ivt_state").equals("2")){
	 					pdd.put("var15", "已标签波次分拣");
	 				}else if(listpd.get(i).getString("ivt_state").equals("3")){
	 					pdd.put("var15", "已LED波次分拣");
	 				}else{
	 					pdd.put("var15", "已出库");
	 				}
	 				if(listpd.get(i).getString("type").equals("1")) {
	 					pdd.put("var16", "加急订单");
	 				}else if(listpd.get(i).getString("type").equals("2")) {
	 					pdd.put("var16", "仓配订单");
	 				}
	 				pdd.put("var17", listpd.get(i).getString("area_name"));*/
	 				varList.add(pdd);
 					}
 				}
	 			map.put("varList", varList);
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
 			/*}else{
 				map.put("varList", new ArrayList());
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
 			}*/
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 仓配出库单管理列表
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping({ "/cplist" })
	public ModelAndView cplist(Page page) {
		logBefore(this.logger, "列表仓配ExWarehouseOrder");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			List<PageData> areaList = this.merchantService.areaList(pd);
			String lastLoginStart = pd.getString("order_date");
			if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
				String lastLoginEnd = lastLoginStart;
				lastLoginStart = lastLoginStart + " 00:00:00";
				lastLoginEnd = lastLoginEnd + " 23:59:59";
				pd.put("lastLoginStart", lastLoginStart);
				pd.put("lastLoginEnd", lastLoginEnd);
			}
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			if (!StringUtil.isEmpty(pd.getString("order_type"))) {
				pd.put("order_type", pd.getString("order_type"));
			}
			if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
				pd.put("ivt_state", pd.getString("ivt_state"));
			}
			if (!StringUtil.isEmpty(pd.getString("cityid"))) {
				String city = pd.getString("cityid");
				pd.put("cityid", city);
			}
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			page.setPd(pd);
			List<PageData> varList = this.exwarehouseorderService.list(page);
			mv.setViewName("inventorymanagement/exwarehouseorder/ckcpexwarehouseorder_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject("area", areaList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({ "cpgoExwareorderProductEdit" })
	public ModelAndView cpgoExwareorderProductEdit(Page page, String orderId, String type) {
		logBefore(this.logger, "修改出库单商品数量");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			type="1";
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			pd.put("orderNum", orderId);
			List<PageData> pageDate = this.eXOrderItemService.getOrderItemlistProduct(pd);
			
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
			mv.setViewName("inventorymanagement/exwarehouseorder/cpexwarehouseorder_product_edit");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	@RequestMapping({ "cpgoExwareorderProductEditPQ" })
	public ModelAndView cpgoExwareorderProductEditPQ(Page page, String orderId, String type) {
		logBefore(this.logger, "查看出库单标签系统分类详情");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			PageData pdd=new PageData();
			pd = getPageData();
			pd.put("orderNum", orderId);
			type="1";
			page.setPd(pd);
			pd.put("parent_id", Integer.valueOf(0));

			List<PageData> productType = this.productTypeService.findByParentId(pd);
			mv.addObject("productType", productType);
			pd = this.exwarehouseorderService.getExwarouseById(pd);
			PageData sumOrder = new PageData();
			sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
			
			pdd.put("orderNum", orderId);
			List<PageData> pageDate = this.eXOrderItemService.getFjItemById(pdd);
			mv.setViewName("inventorymanagement/exwarehouseorder/cpexwarehouseorder_product_edit_pq");
			mv.addObject("type", type);
			mv.addObject("orderItemList", pageDate);
			mv.addObject("enwarhouse", pd);
			mv.addObject("sumOrder", sumOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}
	
	/**
	 * 跳转配送及时率
	 */
	@RequestMapping(value="/timelyDeliverymv")
	public ModelAndView timelyDeliverymv(){
		ModelAndView mv=new ModelAndView();
		String operationMsg="跳转配送及时率";
		try {
			mv.setViewName("inventorymanagement/exwarehouseorderchart/timelyratechart");
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	//显示配送率图
	@RequestMapping(value="/timelyDeliveryRate")
	@ResponseBody
	public String timelyDeliveryRate(){
		HttpClient httpClient = new HttpClient();
		String operationMsg = "显示数量";
		String result="";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		JSONObject obj=new JSONObject();
		JSONArray ja=new JSONArray();
		
		try {
			pd=this.getPageData();
			if(pd.getString("groupnum").trim()!=null && pd.getString("groupnum").trim()!=""){
				String groupnum=pd.getString("groupnum").trim();
				pd.put("group_num", groupnum);
			}
			result = HttpUtil.doGet(Const.httpplan+"interfaces/querywxtime","UTF-8");
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(60000);
			result = URLDecoder.decode(result, "UTF-8");
			JSONObject jb=JSONObject.parseObject(result);
			//System.out.println(jb);
			//json 转array
			ja=jb.getJSONArray("msg");
			/*logger.info(jb.getJSONArray("msg"));
			//System.out.println(ja);*/
			//查询所有仓配出库单数据
			List<PageData> list=exwarehouseorderService.findrate(pd);
			//查询所有地区
			List<String> tar=new ArrayList<String>();
			/*List<PageData> listarea=merchantService.areaList(pd);
			for (PageData pdd : listarea) {
				tar.add(pdd.getString("area_name"));
			}*/
			List<String> listgroup=new ArrayList<>();
			//保存得分
			List<String> fenshu=new ArrayList<>();
			List<PageData> listname=new ArrayList<>();
			List<PageData> q=new ArrayList<>();
			PageData pa=null;//返单
			PageData ck=null;//出库
			PageData pcity=null;
			//group_number 批次号  site门店编码 create_time 创建日期
			if(jb.size()>0 && jb!=null){
				//if(ja.size()<=list.size()){
				for (int i = 0; i < ja.size(); i++) {
					
						pa=new PageData();
						ck=new PageData();
					for (int j = 0; j < list.size(); j++) {
						
						JSONObject obj1=(JSONObject) ja.get(i);
						if(obj1.get("group_number").equals(list.get(j).get("group_num"))
								&&obj1.get("site").equals(list.get(j).get("merchant_num"))
								&&obj1.get("create_time")!=null){
							/*logger.info(obj1);
							logger.info(list.get(j));
							logger.info("--------------------------");*/
							int jyzsum=0;
							int ts=0;
							//批次号
							pa.put("group_number", obj1.getString("group_number"));
							pd.put("group_num", list.get(j).getString("group_num"));
							//门店编码
							pa.put("site", obj1.getString("site"));
							ck.put("group_num", list.get(j).getString("group_num"));
							ck.put("merchant_num", list.get(j).getString("merchant_num"));
							
							/*if(!listgroup.contains(list.get(j).getString("group_num"))){
								listgroup.add(ck.getString("group_num"));
							}*/
							if(obj1.getString("create_time")!=null && obj1.getString("create_time")!=""){
							if(pa.getString("group_number").equals(ck.getString("group_num")) && pa.getString("site").equals(ck.getString("merchant_num"))){
								int kk=0;
								PageData cun=new PageData();
								for(int k=0;k<listname.size();k++){
									PageData name=listname.get(k);
									if(name.getString("group_num").equals(pa.getString("group_number"))){
										kk=1;
										String cktime=list.get(j).getString("create_time");
										//返单日期
										String fdtime=obj1.getString("create_time");
										pcity=exwarehouseorderService.sumcity(ck);
										/*//查询地区
										PageData findareaname=merchantService.findareaname(ck);*/
										int w=DateUtil.dayForWeek(cktime);
										String week=String.valueOf(w);
										//获取日期为周几如果不是周二的默认为周二的日期
										String a1=DateUtil.getWeek(cktime,week);
										//int days=Integer.valueOf(a1);
										//两个时间相差天数
										String day=DateUtil.getTwoDay(fdtime,a1);
										int da=Integer.valueOf(day);
										//是否为太原 如果太原  5天   其7天
										if(list.get(j).getString("city").equals("4")){
											if(da>5){
												ts+=da-5;//延迟天数
												jyzsum++;//加油站延误数量
											}
										}else{
											if(da>7){
												ts+=da-7;//其他地区延迟天数
												jyzsum++;//加油站延误数量
											}
										}
										listname.get(k).put("ywts", DoubleUtil.add(Double.valueOf(listname.get(k).getString("ywts")), ts));
										listname.get(k).put("ywsl", DoubleUtil.add(Double.valueOf(listname.get(k).getString("ywsl")), jyzsum));
										//listname.get(k).put("zs", DoubleUtil.add(Double.valueOf(listname.get(k).getString("zs")), Double.valueOf(pcity.getString("sumcity"))));
									}
								}
								if(kk==0){
									//出库创建日期
									String cktime=list.get(j).getString("create_time");
									//返单日期
									String fdtime=obj1.getString("create_time");
									pcity=exwarehouseorderService.sumcity(ck);
									/*//查询地区
									PageData findareaname=merchantService.findareaname(ck);*/
									int w=DateUtil.dayForWeek(cktime);
									String week=String.valueOf(w);
									//获取日期为周几如果不是周二的默认为周二的日期
									String a1=DateUtil.getWeek(cktime,week);
									//int days=Integer.valueOf(a1);
									//两个时间相差天数
									String day=DateUtil.getTwoDay(fdtime,a1);
									int da=Integer.valueOf(day);
									//是否为太原 如果太原  5天   其7天
									if(list.get(j).getString("city").equals("4")){
										if(da>5){
											ts+=da-5;//延迟天数
											jyzsum++;//加油站延误数量
										}
									}else{
										if(da>7){
											ts+=da-7;//其他地区延迟天数
											jyzsum++;//加油站延误数量
										}
									}
									cun.put("group_num", ck.getString("group_num"));
									cun.put("merchant_num", ck.getString("merchant_num"));
									cun.put("ywts", ts);
									cun.put("ywsl", jyzsum);
									cun.put("zs",  Double.valueOf(pcity.getString("sumcity")));
									listname.add(cun);
									listgroup.add(cun.getString("group_num"));
								}
								
							}
							
						}
					}
					}	
						
				}
			}
				//循环分数
				for (int i = 0; i < listname.size(); i++) {
					double tianshu=Double.valueOf(listname.get(i).getString("ywts"));
					double zhanshu=Double.valueOf(listname.get(i).getString("ywsl"));
					double zongshu=Double.valueOf(listname.get(i).getString("zs"));
					float f=(float) (tianshu*zhanshu/zongshu);
					double df=0;
					DecimalFormat df1 = new DecimalFormat("0.00");
					if(f>=0.95 ||f ==0){
						df=55;
					}else{
						df=f/0.95*55;
						
					}
					fenshu.add(df1.format(df));
				}
				
				
			//地区
			//System.out.println(listname);
			obj.put("listgroup", listgroup);
			obj.put("fenshu", fenshu);
			/*obj.put("tar", tar);*/
		}catch (Exception e) {
			e.printStackTrace();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally {
			logEnd(logger, operationMsg);
		}
		return obj.toString();
	}
	
	//显示配送率图
		@RequestMapping(value="/timelyDeliveryexcel")
		@ResponseBody
		public ModelAndView timelyDeliveryexcel(){
			HttpClient httpClient = new HttpClient();
			ModelAndView mv=new ModelAndView();
			PageData pd=new PageData();
			String operationMsg="导出信息";
			logBefore(logger, operationMsg);
			String result="";
			JSONObject obj=new JSONObject();
			JSONArray ja=new JSONArray();
			
			try {
				pd=this.getPageData();
				if(pd.getString("groupnum").trim()!=null && pd.getString("groupnum").trim()!=""){
					String groupnum=pd.getString("groupnum").trim();
					pd.put("group_num", groupnum);
				}
				result = HttpUtil.doGet(Const.httpplan+"interfaces/querywxtime","UTF-8");
				result = URLDecoder.decode(result, "UTF-8");
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
				httpClient.getHttpConnectionManager().getParams().setSoTimeout(60000);
				JSONObject jb=JSONObject.parseObject(result);
				//System.out.println(jb);
				//json 转array
				ja=jb.getJSONArray("msg");
				//System.out.println(ja);
				//查询所有仓配出库单数据
				List<PageData> list=exwarehouseorderService.findrate(pd);
				//查询所有地区
				/*List<String> tar=new ArrayList<String>();
				List<PageData> listarea=merchantService.areaList(pd);
				for (PageData pdd : listarea) {
					tar.add(pdd.getString("area_name"));
				}*/
				
				Map<String, Object> map=new HashMap<String,Object>();
				List<String> titles=new ArrayList<String>();
				titles.add("批次");
				titles.add("延误天数");
	 			titles.add("延误站数");
	 			titles.add("总站数");
	 			titles.add("配送率得分");
	 			map.put("titles", titles);
	 			List<PageData> varList=new ArrayList<PageData>();
				
				List<String> listgroup=new ArrayList<>();
				//保存得分
				List<String> fenshu=new ArrayList<>();
				List<PageData> listname=new ArrayList<>();
				List<PageData> q=new ArrayList<>();
				PageData pa=null;//返单
				PageData ck=null;//出库
				PageData pcity=null;
				//group_number 批次号  site门店编码 create_time 创建日期
				if(jb.size()>0 && jb!=null){
					//if(ja.size()<=list.size()){
					for (int i = 0; i < ja.size(); i++) {
						
							pa=new PageData();
							ck=new PageData();
						for (int j = 0; j < list.size(); j++) {
							
							JSONObject obj1=(JSONObject) ja.get(i);
							if(obj1.get("group_number").equals(list.get(j).get("group_num"))
									&&obj1.get("site").equals(list.get(j).get("merchant_num"))
									&&obj1.get("create_time")!=null){
								/*logger.info(obj1);
								logger.info(list.get(j));
								logger.info("--------------------------");*/
								int jyzsum=0;
								int ts=0;
								//批次号
								pa.put("group_number", obj1.getString("group_number"));
								pd.put("group_num", list.get(j).getString("group_num"));
								//门店编码
								pa.put("site", obj1.getString("site"));
								ck.put("group_num", list.get(j).getString("group_num"));
								ck.put("merchant_num", list.get(j).getString("merchant_num"));
								
								/*if(!listgroup.contains(list.get(j).getString("group_num"))){
									listgroup.add(ck.getString("group_num"));
								}*/
								if(obj1.getString("create_time")!=null && obj1.getString("create_time")!=""){
								if(pa.getString("group_number").equals(ck.getString("group_num")) && pa.getString("site").equals(ck.getString("merchant_num"))){
									int kk=0;
									PageData cun=new PageData();
									for(int k=0;k<listname.size();k++){
										PageData name=listname.get(k);
										if(name.getString("group_num").equals(pa.getString("group_number"))){
											kk=1;
											String cktime=list.get(j).getString("create_time");
											//返单日期
											String fdtime=obj1.getString("create_time");
											pcity=exwarehouseorderService.sumcity(ck);
											/*//查询地区
											PageData findareaname=merchantService.findareaname(ck);*/
											int w=DateUtil.dayForWeek(cktime);
											String week=String.valueOf(w);
											//获取日期为周几如果不是周二的默认为周二的日期
											String a1=DateUtil.getWeek(cktime,week);
											//int days=Integer.valueOf(a1);
											//两个时间相差天数
											String day=DateUtil.getTwoDay(fdtime,a1);
											int da=Integer.valueOf(day);
											//是否为太原 如果太原  5天   其7天
											if(list.get(j).getString("city").equals("4")){
												if(da>5){
													ts+=da-5;//延迟天数
													jyzsum++;//加油站延误数量
												}
											}else{
												if(da>7){
													ts+=da-7;//其他地区延迟天数
													jyzsum++;//加油站延误数量
												}
											}
											listname.get(k).put("ywts", DoubleUtil.add(Double.valueOf(listname.get(k).getString("ywts")), ts));
											listname.get(k).put("ywsl", DoubleUtil.add(Double.valueOf(listname.get(k).getString("ywsl")), jyzsum));
											//listname.get(k).put("zs", DoubleUtil.add(Double.valueOf(listname.get(k).getString("zs")), Double.valueOf(pcity.getString("sumcity"))));
										}
									}
									if(kk==0){
										//出库创建日期
										String cktime=list.get(j).getString("create_time");
										//返单日期
										String fdtime=obj1.getString("create_time");
										pcity=exwarehouseorderService.sumcity(ck);
										/*//查询地区
										PageData findareaname=merchantService.findareaname(ck);*/
										int w=DateUtil.dayForWeek(cktime);
										String week=String.valueOf(w);
										//获取日期为周几如果不是周二的默认为周二的日期
										String a1=DateUtil.getWeek(cktime,week);
										//int days=Integer.valueOf(a1);
										//两个时间相差天数
										String day=DateUtil.getTwoDay(fdtime,a1);
										int da=Integer.valueOf(day);
										//是否为太原 如果太原  5天   其7天
										if(list.get(j).getString("city").equals("4")){
											if(da>5){
												ts+=da-5;//延迟天数
												jyzsum++;//加油站延误数量
											}
										}else{
											if(da>7){
												ts+=da-7;//其他地区延迟天数
												jyzsum++;//加油站延误数量
											}
										}
										cun.put("group_num", ck.getString("group_num"));
										cun.put("merchant_num", ck.getString("merchant_num"));
										cun.put("ywts", ts);
										cun.put("ywsl", jyzsum);
										cun.put("zs",  Double.valueOf(pcity.getString("sumcity")));
										listname.add(cun);
										listgroup.add(cun.getString("group_num"));
									}
									
								}
								
							}
						}
						}	
							
					}
				}
					//循环分数
					for (int i = 0; i < listname.size(); i++) {
						PageData pdd=new PageData();
						double tianshu=Double.valueOf(listname.get(i).getString("ywts"));
						double zhanshu=Double.valueOf(listname.get(i).getString("ywsl"));
						double zongshu=Double.valueOf(listname.get(i).getString("zs"));
						float f=(float) (tianshu*zhanshu/zongshu);
						double df=0;
						DecimalFormat df1 = new DecimalFormat("0.00");
						if(f>=0.95 ||f ==0){
							df=55;
						}else{
							df=f/0.95*55;
							
						}
						fenshu.add(df1.format(df));
						
						pdd.put("var1", listname.get(i).getString("group_num"));
						pdd.put("var2", listname.get(i).getString("ywts"));
						pdd.put("var3", listname.get(i).getString("ywsl"));
						pdd.put("var4", listname.get(i).getString("zs"));
						pdd.put("var5", df1.format(df));
						varList.add(pdd);
					}
					
					
				map.put("varList", varList);
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
				//}
				//地区
				//System.out.println(listname);
				//System.out.println(listgroup);
				//System.out.println(fenshu);
			}catch (Exception e) {
				e.printStackTrace();
				logMidway(logger, operationMsg + "，出现错误：" + e.toString());
			}finally {
				logEnd(logger, operationMsg);
			}
			return mv;
		}
}
