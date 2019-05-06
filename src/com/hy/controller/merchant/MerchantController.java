package com.hy.controller.merchant;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.product.MerchantService;
import com.hy.service.system.user.UserService;
import com.hy.util.AppUtil;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/merchant"})
@Controller
public class MerchantController
  extends BaseController
{
  @Resource(name="merchantService")
  private MerchantService merchantService;
  @Resource(name="userService")
	private UserService userService;
  
  @RequestMapping({"/list"})
  public ModelAndView list(Page page)
    throws Exception
  {
    logBefore(this.logger, "列表merchant");
    
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String searchcriteria = pd.getString("searchcriteria");
    String keyword = pd.getString("keyword");
    if ((keyword != null) && (!"".equals(keyword))) {
      keyword = keyword.trim();
      pd.put("keyword", keyword);
      pd.put("searchcriteria", searchcriteria);
    }
    String city = pd.getString("city");
    if ((city != null) && (!"".equals("city")))
    {
      pd.put("city", city);
    }
    else {
      pd.put("city", Integer.valueOf(0));
    }
    String areaNum = LoginUtil.getLoginUser().getUSERNAME();
    if (areaNum.length() == 4 && areaNum.substring(0, 2).equals("65")) {
      pd.put("areaNum", areaNum);
    }
    List<PageData> areaList = this.merchantService.areaList(pd);
    mv.addObject("area", areaList);
    page.setPd(pd);
    List<PageData> merchantList = this.merchantService.listAll(page);
    mv.setViewName("system/merchant/merchant_list");
    mv.addObject("merchantList", merchantList);
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/selectlist"})
  public ModelAndView selectlist(Page page)
    throws Exception
  {
    logBefore(this.logger, "选择门店弹窗");
    
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String searchcriteria = pd.getString("searchcriteria");
    String keyword = pd.getString("keyword");
    if ((keyword != null) && (!"".equals(keyword))) {
      keyword = keyword.trim();
      pd.put("keyword", keyword);
      pd.put("searchcriteria", searchcriteria);
    }
    String city = pd.getString("city");
    if ((city != null) && (!"".equals("city")))
    {
      pd.put("city", city);
    }
    else {
      pd.put("city", Integer.valueOf(0));
    }
    List<PageData> areaList = this.merchantService.areaList(pd);
    mv.addObject("area", areaList);
    page.setPd(pd);
    List<PageData> merchantList = this.merchantService.listAll(page);
    mv.setViewName("system/merchant/select_merchant_list");
    mv.addObject("merchantList", merchantList);
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/deleteMerchant"})
  public void deleteMerchant(PrintWriter out)
  {
    PageData pd = new PageData();
    try {
      pd = getPageData();
      this.merchantService.deleteMerchant(pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/deleteAllMerchant"})
  @ResponseBody
  public Object deleteAllSuppler()
  {
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      pd = getPageData();
      List<PageData> pdList = new ArrayList();
      String SUPPLE_IDS = pd.getString("SUPPLE_IDS");
      if ((SUPPLE_IDS != null) && (!"".equals(SUPPLE_IDS))) {
        String[] ArraySUPPLE_IDS = SUPPLE_IDS.split(",");
        this.merchantService.deleteAllMerchant(ArraySUPPLE_IDS);
        pd.put("msg", "ok");
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
  
  @RequestMapping({"/goAdd"})
  public ModelAndView goAdd()
  {
    logBefore(this.logger, "去新增Merchant页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      List<PageData> areaList = this.merchantService.areaList(pd);
      mv.addObject("area", areaList);
      mv.setViewName("system/merchant/merchant_edit");
      mv.addObject("msg", "save");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/save"})
  public ModelAndView save()
    throws Exception
  {
    logBefore(this.logger, "新增merchant");
    
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    PageData merchant = this.merchantService.getMerchantById(pd);
    if (merchant.size() > 0) {
      this.merchantService.deleteMerchant(merchant);
    }
    //pd.put("merchant_num", "CST_" + StringUtil.getStringOfMillisecond(""));
    pd.put("user", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
   
    PageData pdd=new PageData();
	pdd.put("USERNAME", pd.getString("merchant_num"));
	pdd.put("PASSWORD", new SimpleHash("SHA-1",pd.getString("merchant_num"), "123456").toString());
	pdd.put("NAME", pd.getString("merchant_name"));
	pdd.put("RIGHTS", "");
	pdd.put("ROLE_ID", 26); //供应商ROLE_ID 23
	pdd.put("LAST_LOGIN","");//最后登录时间
	pdd.put("IP", "");
	pdd.put("STATUS",0);
	pdd.put("BZ", "");
	pdd.put("SKIN","default");
	pdd.put("EMAIL","");
	pdd.put("NUMBER", pd.getString("SUPPLIER_ID"));
	pdd.put("PHONE", pd.getString("CONTACT_PERSON_MOBILE"));
	pdd.put("ck_id", 1);
	if(userService.findByUId(pdd)==null){
		userService.saveU(pdd);
	}
	this.merchantService.save(pd);
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/edit"})
  public ModelAndView edit()
    throws Exception
  {
    logBefore(this.logger, "新增merchant");
    
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
    merchantService.editmerchant(pd);
/*    PageData merchant = this.merchantService.getMerchantById(pd);
    if (merchant.size() > 0) {
      this.merchantService.deleteMerchant(merchant);
    }
    if ((pd.getString("oldId") != null) && (pd.getString("oldId") != "")) {
      this.merchantService.deleteMerchantByOldId(pd.getString("oldId"));
    }
    
    //pd.put("merchant_num", "CST_" + StringUtil.getStringOfMillisecond(""));
    pd.put("USERNAME", pd.getString("merchant_num"));
    PageData user=userService.findByUId(pd);
    pd.put("PASSWORD", new SimpleHash("SHA-1",pd.getString("merchant_num"), "123456").toString());
    
    
    
    pd.put("user", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
    this.merchantService.save(pd);*/
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/goEdit"})
  public ModelAndView goEdit()
  {
    logBefore(this.logger, "去修改Merchant页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      List<PageData> areaList = this.merchantService.areaList(pd);
      mv.addObject("area", areaList);
      pd = this.merchantService.findById(pd);
      mv.setViewName("system/merchant/merchant_edit");
      mv.addObject("msg", "edit");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goInfo"})
  public ModelAndView goInfo()
  {
    logBefore(this.logger, "去查看Merchant页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      List<PageData> areaList = this.merchantService.areaList(pd);
      pd = this.merchantService.getMerchantById(pd);
      mv.setViewName("system/merchant/merchant_info");
      mv.addObject("area", areaList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/excel"})
  public ModelAndView exportExcel(Page page)
    throws UnsupportedEncodingException
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String searchcriteria = pd.getString("searchcriteria");
    String keyword = pd.getString("keyword");
    String city = pd.getString("city");
    if ((keyword != null) && (!"".equals(keyword))) {
      keyword = URLDecoder.decode(keyword, "utf-8");
      
      keyword = keyword.trim();
      pd.put("keyword", keyword);
      pd.put("searchcriteria", searchcriteria);
      pd.put("city", city);
    }
    page.setPd(pd);
    try {
      Map<String, Object> dataMap = new HashMap();
      List<String> titles = new ArrayList();
      titles.add("商户ID");
      titles.add("商户编号");
      titles.add("商户名称");
      titles.add("名称");
      titles.add("地区");
      titles.add("地址");
      titles.add("联系人");
      titles.add("联系人手机");
      dataMap.put("titles", titles);
      List<PageData> productlist = this.merchantService.excel(page);
      List<PageData> varList = new ArrayList();
      for (int i = 0; i < productlist.size(); i++) {
        PageData vpd = new PageData();
        vpd.put("var1", ((PageData)productlist.get(i)).getString("商户ID"));
        vpd.put("var2", ((PageData)productlist.get(i)).getString("商户编号"));
        vpd.put("var3", ((PageData)productlist.get(i)).getString("商户名称"));
        vpd.put("var4", ((PageData)productlist.get(i)).getString("名称"));
        vpd.put("var5", ((PageData)productlist.get(i)).getString("地区"));
        vpd.put("var6", ((PageData)productlist.get(i)).getString("地址"));
        vpd.put("var7", ((PageData)productlist.get(i)).getString("联系人"));
        vpd.put("var8", ((PageData)productlist.get(i)).getString("联系人手机"));
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


