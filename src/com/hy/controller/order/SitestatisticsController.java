package com.hy.controller.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.order.SitestatisticsService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

@Controller
@RequestMapping({ "/Sitestatistics" })
public class SitestatisticsController  extends BaseController {
	
	
	
	@Resource(name = "sysLogService")
	private SysLogService sysLogService;
	@Resource(name = "SitestatService")
	private SitestatisticsService sitestat;
	
	
	@RequestMapping({ "findSiteMerchantlitPage" })
	public ModelAndView findSiteMerchantlitPage(Page page) {
		PageData pd = new PageData();
		pd = this.getPageData();
		ModelAndView mv = getModelAndView();
		try {
			page.setPd(pd);
			List<PageData> lst=sitestat.findSiteMerchantlitPage(page);
			mv.addObject("sitelist",lst);
			mv.addObject("pd",pd);
			mv.setViewName("inventorymanagement/sitestat/sitestat_list");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	  
	  @RequestMapping({"getProductInventroyinfo"})
	  public ModelAndView getProductInventroyinfo(Page page) throws Exception
	  {
	    ModelAndView mv = getModelAndView();
	    logBefore(this.logger, "新增warehouse");
	    com.hy.util.PageData pd = new com.hy.util.PageData();
	    pd = getPageData();
	    pd.put("keyword", pd.getString("keyword").trim());
	    String type = pd.getString("type");
	    if (type.equals("")) {
	      pd.put("type", Integer.valueOf(0));
	    }
	    page.setPd(pd);
	    List<PageData> lst=sitestat.findSiteitemlitPage(page);
	    //java.util.List<com.hy.util.PageData> productInventoryList = this.productinventoryService.getProductInventroyinfo(page);
	    mv.setViewName("inventorymanagement/sitestat/productInventoryList");
	    mv.addObject("list", lst);
	    mv.addObject("pd", pd);
	    return mv;
	  }
	  
	
	
	
	

}
