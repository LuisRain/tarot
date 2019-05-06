package com.hy.controller.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.order.EXOrderItemService;
import com.hy.service.order.ThProductService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
@Controller
@RequestMapping({"/thproduct"})
public class ThProductController  extends BaseController{
	
	 @Resource
	 private ThProductService thProductService;
	 @Resource(name="sysLogService")
	  private SysLogService sysLogService;
	 @Resource(name = "eXOrderItemService")
		private EXOrderItemService eXOrderItemService;
	
	 /**查看销售退货列表**/
	  @RequestMapping({"/commodityProduct"})
	  public ModelAndView commodityProduct(Page page)
	  {
	    logBefore(this.logger, "退货列表查看");
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
	      Map pp= this.getHC();
	      if(pp.get("cha").equals("1")){
	    	  List<PageData> varList = this.thProductService.commodityProduct(page);
	    	  mv.addObject("varList", varList);
	      }
	      mv.setViewName("commodityreturn/sellingorder/sellingorder_list");
	      mv.addObject("pd", pd);
	    } catch (Exception e) {
	      this.logger.error(e.toString(), e);
	    }
	    return mv;
	  }
	  /**查看销售退货明细**/
	  @RequestMapping({"/commodityProductItem"})
	  public ModelAndView commodityProductItem()
	  {
	    logBefore(this.logger, "退货列表查看");
	    ModelAndView mv = getModelAndView();
	    PageData pd = new PageData();
	    try {
	      pd = getPageData();
	      PageData sell=thProductService.findOrderList(pd);
	      List<PageData> varList = this.thProductService.commodityProductItem(pd);
	      PageData sumNu=thProductService.sumFinalquantity(pd);
	      mv.setViewName("commodityreturn/sellingorder/sellingorder_edit");
	      mv.addObject("orderItemList", varList);
	      mv.addObject("sumNu", sumNu);
	      mv.addObject("sell", sell);
	    } catch (Exception e) {
	      this.logger.error(e.toString(), e);
	    }
	    return mv;
	  }
	 
	/****退货单审核*****/
	  @RequestMapping(value = { "/reviewedAll" }, produces = { "application/text;charset=UTF-8" })
	  @ResponseBody
		public String reviewedAll() {
			String result = "";
			logBefore(this.logger, "批量审核退货单操作人："+LoginUtil.getLoginUser().getUSERNAME());
			PageData pd = new PageData();
			try {
				pd = getPageData();
				String DATA_IDS = pd.getString("DATA_IDS");
				if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
					//String[] ArrayDATA_IDS = DATA_IDS.split(",");
					result =thProductService.reviewedAll(DATA_IDS);
					this.sysLogService.saveLog("批量审核退货单", result);
				}
			} catch (Exception e) {
				this.logger.error(e.toString(), e);
				result = e.toString();
			} finally {
				logAfter(this.logger);
			}
			return result;
		}
	  
	  /**查看采购退货列表**/
	  @RequestMapping({"/CgOrderList"})
	  public ModelAndView CgOrderList(Page page)
	  {
	    logBefore(this.logger, "退货列表查看");
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
	      List<PageData> varList = this.thProductService.cgOrderlistPage(page);
	      mv.setViewName("commodityreturn/exOrder/exwarehouseorder_list");
	      mv.addObject("varList", varList);
	      mv.addObject("pd", pd);
	    } catch (Exception e) {
	      this.logger.error(e.toString(), e);
	    }
	    return mv;
	  }
	  /**查看销售退货列表**/
	  @RequestMapping({"/XsOrderList"})
	  public ModelAndView XsOrderList(Page page)
	  {
	    logBefore(this.logger, "退货列表查看");
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
	      List<PageData> varList = this.thProductService.xsOrderlistPage(page);
	      mv.setViewName("commodityreturn/enOrder/enwarehouseorder_list");
	      mv.addObject("varList", varList);
	      mv.addObject("pd", pd);
	    } catch (Exception e) {
	      this.logger.error(e.toString(), e);
	    }
	    return mv;
	  }
	  /**查看采购退货明细***/
	  @RequestMapping({ "goExwareorderProductEdit" })
		public ModelAndView goExwareorderProductEdit(String orderId) {
			ModelAndView mv = getModelAndView();
			try {
				PageData pd = new PageData();
				pd = getPageData();
				pd.put("order_num", orderId);
				pd = this.thProductService.cgexOrder(pd);
				List<PageData> pageDate = this.thProductService.cgexOrderitem(pd);
				PageData sumOrder = this.eXOrderItemService.selectSumExOrder(orderId);
				mv.addObject("orderItemList", pageDate);
				mv.addObject("enwarhouse", pd);
				mv.addObject("sumOrder", sumOrder);
				mv.setViewName("commodityreturn/exOrder/exwarehouseorder_edit");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mv;
		}
	  /**查看销售退货明细***/
	  @RequestMapping({ "goEnwareorderProductEdit" })
		public ModelAndView goEnwareorderProductEdit( String orderId) {
			ModelAndView mv = getModelAndView();
			try {
				PageData pd = new PageData();
				pd = getPageData();
				pd.put("order_num", orderId);
				pd = this.thProductService.xsenOrder(pd);
				List<PageData> pageDate = this.thProductService.xsenOrderitem(pd);
				mv.setViewName("commodityreturn/enOrder/enwarehouseorder_product_edit");
				mv.addObject("orderItemList", pageDate);
				mv.addObject("enwarhouse", pd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mv;
		}
	  /****采购退货单审核*****/
	  @RequestMapping(value = { "/cgExamine" }, produces = { "application/text;charset=UTF-8" })
	  @ResponseBody
		public String cgExamine() {
			String result = "";
			logBefore(this.logger, "批量审核采购退货单，操作人："+LoginUtil.getLoginUser().getUSERNAME());
			PageData pd = new PageData();
			try {
				pd = getPageData();
				String DATA_IDS = pd.getString("DATA_IDS");
				if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
					result =thProductService.cgExamine(DATA_IDS);
					this.sysLogService.saveLog("批量审核退货单", result);
				}
			} catch (Exception e) {
				this.logger.error(e.toString(), e);
				result = e.toString();
			} finally {
				logAfter(this.logger);
			}
			return result;
		}
	  /****销售退货单审核*****/
	  @RequestMapping(value = { "/xsExamine" }, produces = { "application/text;charset=UTF-8" })
	  @ResponseBody
		public String xsExamine() {
			String result = "";
			logBefore(this.logger, "批量审核采购退货单，操作人："+LoginUtil.getLoginUser().getUSERNAME());
			PageData pd = new PageData();
			try {
				pd = getPageData();
				String DATA_IDS = pd.getString("DATA_IDS");
				if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
					result =thProductService.xsExamine(DATA_IDS);
					this.sysLogService.saveLog("批量审核退货单", result);
				}
			} catch (Exception e) {
				this.logger.error(e.toString(), e);
				result = e.toString();
			} finally {
				logAfter(this.logger);
			}
			return result;
		}
	  
	  @RequestMapping({ "thprint" })
		public ModelAndView print() {
			logBefore(this.logger, "修改出库单商品数量");
			ModelAndView mv = getModelAndView();
			try {
				PageData pd = new PageData();
				pd = getPageData();
				List<PageData> sellitem = this.thProductService.commodityProductItem(pd);
				PageData sell=thProductService.findOrderList(pd);
				mv.addObject("sellitem", sellitem);
				mv.addObject("sell", sell);
				mv.setViewName("commodityreturn/sellingorder/thprinta");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mv;
		}
	  
	  @RequestMapping({ "cgprint" })
		public ModelAndView cgprint() {
			ModelAndView mv = getModelAndView();
			try {
				PageData pd = new PageData();
				pd = getPageData();
				List<PageData> sellitem = this.thProductService.cgexOrderitem(pd);
				PageData sell=thProductService.cgexOrder(pd);
				mv.addObject("sellitem", sellitem);
				mv.addObject("sell", sell);
				mv.setViewName("commodityreturn/exOrder/cgprinta");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mv;
		}
	  
	  @RequestMapping({ "xsprint" })
		public ModelAndView xsprint() {
			ModelAndView mv = getModelAndView();
			try {
				PageData pd = new PageData();
				pd = getPageData();
				List<PageData> sellitem = this.thProductService.xsenOrderitem(pd);
				PageData sell=thProductService.xsenOrder(pd);
				mv.addObject("sellitem", sellitem);
				mv.addObject("sell", sell);
				mv.setViewName("commodityreturn/enOrder/xsprinta");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mv;
		}
	/**
	 * 退货导出到Excel
	 * @return
	 */
	@RequestMapping(value="/exorderlistexcel")
	public ModelAndView exorderlistexcel(){
	 	ModelAndView mv=new ModelAndView();
	 	PageData pd=new PageData();
	 	String operationMsg="退货订单导出";
	 	logBefore(logger, operationMsg);
	 	try {
	 		pd=this.getPageData();
	 		pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
	 		String lastLoginStart=pd.getString("lastLoginStart");
	 		String lastLoginEnd=pd.getString("lastLoginEnd");
			if (lastLoginStart != null && !"".equals(lastLoginStart)) {
				lastLoginStart = lastLoginStart + " 00:00:00";
				pd.put("lastLoginStart", lastLoginStart);
			}
			if (lastLoginEnd != null && !"".equals(lastLoginEnd)) {
				lastLoginEnd = lastLoginEnd + " 00:00:00";
				pd.put("lastLoginEnd", lastLoginEnd);
			}
	 		Map<String,Object> map=new HashMap<String, Object>();
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
	 			titles.add("退货日期"); 
	 			titles.add("状态"); 
	 			map.put("titles", titles);
	 		List<PageData> listpd=thProductService.excelexport(pd);
	 		List<PageData> varList=new ArrayList<PageData>();
	 		for (int i = 0; i < listpd.size(); i++) {
	 			PageData pdd=new PageData();
	 			pdd.put("var1", listpd.get(i).getString("group_num"));
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
	 			pdd.put("var13", listpd.get(i).getString("short_name"));
	 			pdd.put("var14", listpd.get(i).getString("order_date"));
	 		if(listpd.get(i).getString("checked_state").equals("1")){
	 			pdd.put("var15", "新订单");
	 		}else if(listpd.get(i).getString("checked_state").equals("2")){
	 			pdd.put("var15", "已审核，但未入库");
	 		}else if(listpd.get(i).getString("checked_state").equals("3")){
	 			pdd.put("var15", "已审核，且已入库");
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
	  
}
