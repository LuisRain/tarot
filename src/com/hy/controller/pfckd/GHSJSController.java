package com.hy.controller.pfckd;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.pfckd.GHSJSService;
import com.hy.service.product.ProductService;
import com.hy.util.DoubleUtil;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

/**
 * 供货商结算
 * @author gyy
 *
 */
@Controller
@RequestMapping({ "/ghsjscontroller" })
public class GHSJSController extends BaseController{
	
	
	@Resource
	private GHSJSService ghsjsservice;
	
	@Resource(name="productService")
	private ProductService productService;
	
	@RequestMapping({ "findGHSJSlistpage" })
	public ModelAndView findGHSJSlistpage(Page page) {
		logBefore(this.logger, "分页查询供货商结算");
		ModelAndView mv = getModelAndView();
		try {
			String loginname = LoginUtil.getLoginUser().getUSERNAME();
			
			PageData pd = new PageData();
			pd = getPageData();
			pd.put("loginname",loginname);
			page.setPd(pd);
			List<PageData> varList = this.ghsjsservice.findGHSJSlistpage(page);
			mv.setViewName("pfckd/GHSJSlist");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	} 
	/**
	 * 打印所有供应商信息
	 * @param page
	 * @return
	 */
	@RequestMapping({ "printlist" })
	public ModelAndView printlist(Page page) {
		logBefore(this.logger, "分页查询供货商结算");
		ModelAndView mv = getModelAndView();
		try {
			Date date = new Date();
			PageData pd = new PageData();
			pd = getPageData();
			String loginname = LoginUtil.getLoginUser().getUSERNAME();
			pd.put("loginname",loginname);
			List<PageData> varList = this.ghsjsservice.printGHSJSlist(pd);
			//合计总价
			if(varList.size() > 0){                  
				double number = 0.00;
				for (int i = 0; i < varList.size(); i++) {
					double totalPrice  = Double.parseDouble(varList.get(i).getString("TotalPrice")); 
					number = DoubleUtil.add(number, totalPrice);
				}
				PageData map = new PageData();
				map.put("supplier_name", "合计");
				map.put("TotalPrice", number);
				varList.add(map);
			}
			mv.setViewName("pfckd/print");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}  
	/**
	 * 打印单个供应商信息
	 * @param page
	 * @return
	 */
	@RequestMapping({ "supprint" })
	public ModelAndView supprint(Page page) {
		logBefore(this.logger, "打印供货商结算");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			PageData varList = this.ghsjsservice.printGHSJSlist(pd).get(0);
			mv.setViewName("pfckd/supprint");
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = format.format(date);
			String[] str = date1.split("-");
			pd.put("year", str[0]);
			pd.put("month", str[1]);
			pd.put("day", str[2]);
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	} 
	
	/**
	 * 打印供应商对账函
	 * @return
	 */
	@RequestMapping({ "dzhprint" })
	public ModelAndView dzhprint(){
		logBefore(this.logger, "打印供货商对账函");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			List<PageData> varList = this.ghsjsservice.dzhprint(pd);
			PageData  supinfo = ghsjsservice.findsupid(pd);
			String month = pd.getString("month");
			String year = month.substring(0, 4);
			String month2 = month.substring(4, 6);
			int month3 = Integer.parseInt(month2.substring(0,1));
			if(month3 > 0){
				mv.addObject("month", month2);
			}else{
				mv.addObject("month", month2.substring(1,2));
			}
			mv.addObject("year", year);
			mv.setViewName("pfckd/dzhprint");
			mv.addObject("supinfo", supinfo);
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 导出全部供应商excel
	 * @param page
	 * @return
	 */
	@RequestMapping({ "expsupexcel" })
	@ResponseBody
	public void  expsupexcel(HttpServletRequest request,HttpServletResponse response) {
		logBefore(this.logger, "导出供货商结算");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			List<PageData> varList = this.ghsjsservice.printGHSJSlist(pd);
			mv.setViewName("pfckd/supprint");
			//合计总价
			if(varList.size() > 0){                  
				double number = 0.00;
				for (int i = 0; i < varList.size(); i++) {
					double totalPrice  = Double.parseDouble(varList.get(i).getString("TotalPrice")); 
					number = DoubleUtil.add(number, totalPrice);
				}
				PageData map = new PageData();
				map.put("supplier_name", "合计");
				map.put("TotalPrice", number);
				varList.add(map);
			}
			//excel文件名
			String fileName = System.currentTimeMillis()+".xls";
			String sheetName = pd.getString("month")+"供应商结算";
			HSSFWorkbook wb = ExpExcel.getHSSFWorkbook(sheetName, varList, null);
			//响应到客户端
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	/**
	 * 导出单个供应商excel
	 * @param page
	 * @return
	 */
	@RequestMapping({ "expexcel" })
	@ResponseBody
	public void  expexcel(HttpServletRequest request,HttpServletResponse response) {
		logBefore(this.logger, "导出单个供货商结算");
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			pd = getPageData();
			PageData varList =  this.ghsjsservice.printGHSJSlist(pd).get(0);
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = format.format(date);
			String[] str = date1.split("-");
			varList.put("year", str[0]);
			varList.put("month", str[1]);
			varList.put("day", str[2]);
			//excel文件名
			String fileName = "供应商结算"+System.currentTimeMillis()+".xls";
			String sheetName = "供应商结算";
			HSSFWorkbook wb = ExpExcel.getHSSFWorkbook2(sheetName, varList, null);
			//响应到客户端
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	 //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
