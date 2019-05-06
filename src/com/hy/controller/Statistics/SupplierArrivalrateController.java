package com.hy.controller.Statistics;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.Statistics.SupplierArrivalrateService;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;

/**
 * 供应商到货率controller
 * @author gyy
 *
 */
@Controller
@RequestMapping({ "/supplierarrivalrate" })
public class SupplierArrivalrateController extends BaseController {
	
	@Resource(name = "supplierarrivalrateservice")
	private SupplierArrivalrateService supplierarrivalrateservice;
	
	/**
	 * 查询供货商到货率
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "findlistPage" })
	public ModelAndView findlistPage(Page page) throws Exception {
		logBefore(this.logger, "供货商到货率列表");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String startdate = pd.getString("startdate");
		String enddate = pd.getString("enddate");
		if ((enddate != null) && (!"".equals(enddate)) && startdate != null && !startdate.equals("")) {
			pd.put("enddate", enddate);
			pd.put("startdate", startdate);
		}
		page.setPd(pd);
		List<PageData> arrivaList = this.supplierarrivalrateservice.findlistPage(page);
		mv.setViewName("Statistics/SupplierArrivalratelist");
		mv.addObject("arrivaList", arrivaList);
		mv.addObject("pd", pd);
		return mv;
	}
	 @RequestMapping({"/excel"})
	  public ModelAndView exportExcel(Page page)
	    throws UnsupportedEncodingException
	  {
	    ModelAndView mv = getModelAndView();
	    PageData pd = new PageData();
	    pd = getPageData();
	    String startdate = pd.getString("startdate");
		String enddate = pd.getString("enddate");
		if ((enddate != null) && (!"".equals(enddate)) && startdate != null && !startdate.equals("")) {
			pd.put("enddate", enddate);
			pd.put("startdate", startdate);
		}
	    try {
	      Map<String, Object> dataMap = new HashMap();
	      List<String> titles = new ArrayList();
	      titles.add("供应商编码");
	      titles.add("供应商名称");
	      titles.add("订购数");
	      titles.add("订购价");
	      titles.add("实际到货数");
	      titles.add("实际价格");
	      titles.add("数量到货率");
	      titles.add("金额到货率");
	      dataMap.put("titles", titles);
	      List<PageData> productlist =this.supplierarrivalrateservice.findarrivalate(pd);
	      List<PageData> varList = new ArrayList();
	      for (int i = 0; i < productlist.size(); i++) {
	        PageData vpd = new PageData();
	        vpd.put("var1", ((PageData)productlist.get(i)).getString("supplier_num"));
	        vpd.put("var2", ((PageData)productlist.get(i)).getString("supplier_name"));
	        vpd.put("var3", ((PageData)productlist.get(i)).getString("sumquantity"));
	        vpd.put("var4", ((PageData)productlist.get(i)).getString("quantity_price"));
	        vpd.put("var5", ((PageData)productlist.get(i)).getString("sumfinal_quantity"));
	        vpd.put("var6", ((PageData)productlist.get(i)).getString("final_quantity_price"));
	        vpd.put("var7", ((PageData)productlist.get(i)).getString("quantity_dhl"));
	        vpd.put("var8", ((PageData)productlist.get(i)).getString("price_dhl"));
	        varList.add(vpd);
	      }
	      dataMap.put("varList", varList);
	      ObjectExcelView erv = new ObjectExcelView();
	      mv = new ModelAndView(erv, dataMap);
	    }
	    catch (Exception e) {
	      this.logger.error(e.toString(), e);
	    }
	    return mv;
	  }
	
	
	
}
