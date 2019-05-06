package com.hy.controller.data;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.order.ENOrderItemService;
import com.hy.service.order.EXOrderItemService;
import com.hy.service.order.EnWarehouseOrderService;
import com.hy.service.order.ExWarehouseOrderService;
import com.hy.service.order.OrderGroupService;
import com.hy.service.product.ProductService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/dataCenter"})
public class DataCenterController
  extends BaseController
{
  @Resource
  private ENOrderItemService enOrderItemService;
  @Resource
  private EXOrderItemService exOrderItemService;
  @Resource
  private EnWarehouseOrderService enWarehouseOrderService;
  @Resource
  private ExWarehouseOrderService exWarehouseOrderService;
  @Resource
  private ProductService productService;
  @Resource
  private OrderGroupService orderGroupService;
  @Resource
  private ProductinventoryService productinventoryService;
  @Resource(name = "sysLogService")
  private SysLogService sysLogService;
  
  @RequestMapping({"dataCenterProductQuantitylistPage"})
  public ModelAndView dataCenterProductQuantitylistPage(Page page)
  {
    logBefore(this.logger, "列表pi");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      page.setPd(pd);
      List<PageData> enDataList = this.exOrderItemService
        .dataCenterProductQuantitylistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/dataCenterProductQuantitylistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
  @RequestMapping({"dataCenterSupplierlistPage"})
  public ModelAndView dataCenterSupplierlistPage(Page page)
  {
    logBefore(this.logger, "列表pi");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      if (pd.get("sort") == null)
      {
        pd.put("sort", "1");
      }
      page.setPd(pd);
      List<PageData> enDataList = this.enOrderItemService
        .dataCenterSupplierlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/dataCenterSupplierlistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
  @RequestMapping({"dataCenterPIlistPage"})
  public ModelAndView dataCenterPIlistPage(Page page)
  {
    logBefore(this.logger, "列表pi");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      String orderGrouPNum = this.orderGroupService.getOrderGroupNum();
      if(!orderGrouPNum.equals("false")){
    	  pd.put("group_num", orderGrouPNum);
      }
      page.setPd(pd);
      List<PageData> enDataList = this.productinventoryService
        .dataCenterPIlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/dataCenterPIlistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
  @RequestMapping({"dataCenterProductlistPage"})
  public ModelAndView dataCenterProductlistPage(Page page)
  {
    logBefore(this.logger, "列表en");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
    	pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
      page.setPd(pd);
      List<PageData> enDataList = this.productService
        .dataCenterProductlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/dataCenterProductlistpage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
  @RequestMapping({"dataCeterEnOrderlistPage"})
  public ModelAndView dataCeterEnOrderlistPage(Page page)
  {
    logBefore(this.logger, "列表en");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if ((pd.getString("ivt_state") != null) && 
        (!"".equals(pd.getString("ivt_state")))) {
        pd.put("ivt_state", pd.getString("ivt_state"));
      } else {
        pd.put("ivt_state", Integer.valueOf(0));
      }
      page.setPd(pd);
      List<PageData> enDataList = this.enWarehouseOrderService
        .dataCeterEnOrderlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/enOrderDataCenterlistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
  @RequestMapping({"dataCeterExOrderlistPage"})
  public ModelAndView dataCeterExOrderlistPage(Page page) {
    logBefore(this.logger, "列表en");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if ((pd.getString("ivt_state") != null) && 
        (!"".equals(pd.getString("ivt_state"))))
      {
        pd.put("ivt_state", pd.getString("ivt_state"));
      } else {
        pd.put("ivt_state", Integer.valueOf(0));
      }
      page.setPd(pd);
      List<PageData> enDataList = this.exWarehouseOrderService
        .dataCeterExOrderlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/exOrderDataCenterlistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
  @RequestMapping({"/enDataCenterlistPage"})
  public ModelAndView enDataCenterlistPage(Page page)
  {
    logBefore(this.logger, "列表en");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if ((pd.getString("ivt_state") != null) && 
        (!"".equals(pd.getString("ivt_state"))))
      {
        pd.put("ivt_state", pd.getString("ivt_state"));
      }
      else {
        pd.put("ivt_state", Integer.valueOf(0));
      }
      String keyword=pd.getString("keyword").trim();
      String searchcriteria=pd.getString("searchcriteria");
      if(keyword!=null && keyword!=""){
    	  pd.put("keyword", keyword);
    	  pd.put("searchcriteria", searchcriteria);
      }
      page.setPd(pd);
      
      List<PageData> enDataList = this.enOrderItemService
        .enDataCenterlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/enDataCenterlistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return mv;
  }
  
  @RequestMapping({"/excel"})
  public ModelAndView excel()
  {
	String operationMsg="导出到excel表";
    logBefore(this.logger, operationMsg);
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if ((pd.getString("ivt_state") != null) && 
        (!"".equals(pd.getString("ivt_state"))))
      {
        pd.put("ivt_state", pd.getString("ivt_state"));
      }
      else {
        pd.put("ivt_state", Integer.valueOf(0));
      }
      String keyword=pd.getString("keyword").trim();
      String searchcriteria=pd.getString("searchcriteria");
      if(keyword!=null && keyword!=""){
    	  pd.put("keyword", keyword);
    	  pd.put("searchcriteria", searchcriteria);
      }
      
      List<PageData> enDataList = this.enOrderItemService
        .excel(pd);
      //
      Map<String, Object> map=new HashMap<String, Object>();
      List<PageData> varList=new ArrayList<>();
      List<String> titles=new ArrayList<>();
      titles.add("订单日期");
      titles.add("入库日期");
      titles.add("批次号");
      titles.add("入库单编号");
      titles.add("供应商");
      titles.add("条形码");
      titles.add("商品名称");
      titles.add("类别");
      titles.add("成本价");
      titles.add("出售价");
      titles.add("业务员");
      titles.add("订购数");
      titles.add("实际数");
      titles.add("未到数量");
      titles.add("单位");
      titles.add("金额(元)");
      map.put("titles", titles);
      for (PageData pdd : enDataList) {
		PageData pa=new PageData();
		pa.put("var1", pdd.getString("create_time"));
		pa.put("var2", pdd.getString("order_date"));
		pa.put("var3", pdd.getString("group_num"));
		pa.put("var4", pdd.getString("order_num"));
		pa.put("var5", pdd.getString("supplier_name"));
		pa.put("var6", pdd.getString("bar_code"));
		pa.put("var7", pdd.getString("product_name"));
		pa.put("var8", pdd.getString("classify_name"));
		pa.put("var9", pdd.getString("purchase_price"));
		pa.put("var10", pdd.getString("sale_price"));
		pa.put("var11", pdd.getString("creator"));
		pa.put("var12", pdd.getString("quantity"));
		pa.put("var13", pdd.getString("final_quantity"));
		pa.put("var14", pdd.getString("shuliang"));
		pa.put("var15", pdd.getString("unit_name"));
		pa.put("var16", pdd.getString("sumSalePrice"));
		varList.add(pa);
	}
    map.put("varList", varList);
    ObjectExcelView erv = new ObjectExcelView();
	mv=new ModelAndView(erv,map);
	sysLogService.saveLog(operationMsg, "成功");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return mv;
  }
  
  @RequestMapping({"exDataCenterlistPage"})
  public ModelAndView exDataCenterlistPage(Page page)
  {
    logBefore(this.logger, "列表en");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if ((pd.getString("ivt_state") != null) && 
        (!"".equals(pd.getString("ivt_state"))))
      {
        pd.put("ivt_state", pd.getString("ivt_state"));
      }
      else {
        pd.put("ivt_state", Integer.valueOf(0));
      }
      page.setPd(pd);
      
      List<PageData> enDataList = this.exOrderItemService
        .exDataCenterlistPage(page);
      mv.addObject("enDataList", enDataList);
      mv.setViewName("dataCenter/exDataCenterlistPage");
      mv.addObject("pd", pd);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return mv;
  }
  
	@RequestMapping({ "SupplierProductlistPage" })
	public ModelAndView SupplierProductlistPage(Page page) {
		logBefore(this.logger, "列表en");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			page.setPd(pd);
			List<PageData> enDataList = this.productService
					.dataCenterProductlistPage(page);
			mv.addObject("enDataList", enDataList);
			mv.setViewName("suppliermanagement/dataCenterProductlistpage");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
}


