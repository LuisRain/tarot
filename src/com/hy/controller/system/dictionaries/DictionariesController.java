package com.hy.controller.system.dictionaries;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.system.dictionaries.DictionariesService;
import com.hy.service.system.menu.MenuService;
import com.hy.util.AppUtil;
import com.hy.util.Logger;
import com.hy.util.PageData;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/dictionaries"})
public class DictionariesController
  extends BaseController
{
  @Resource(name="menuService")
  private MenuService menuService;
  @Resource(name="dictionariesService")
  private DictionariesService dictionariesService;
  List<PageData> szdList;
  
  @RequestMapping({"/save"})
  public ModelAndView save(PrintWriter out)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    PageData pdp = new PageData();
    pdp = getPageData();
    
    String PARENT_ID = pd.getString("PARENT_ID");
    pdp.put("ZD_ID", PARENT_ID);
    
    if ((pd.getString("ZD_ID") == null) || ("".equals(pd.getString("ZD_ID")))) {
      if ((PARENT_ID != null) && ("0".equals(PARENT_ID))) {
        pd.put("JB", Integer.valueOf(1));
        pd.put("P_BM", pd.getString("BIANMA"));
      } else {
        pdp = this.dictionariesService.findById(pdp);
        pd.put("JB", Integer.valueOf(Integer.parseInt(pdp.get("JB").toString()) + 1));
        pd.put("P_BM", pdp.getString("BIANMA") + "_" + pd.getString("BIANMA"));
      }
      this.dictionariesService.save(pd);
    } else {
      pdp = this.dictionariesService.findById(pdp);
      if ((PARENT_ID != null) && ("0".equals(PARENT_ID))) {
        pd.put("P_BM", pd.getString("BIANMA"));
      } else {
        pd.put("P_BM", pdp.getString("BIANMA") + "_" + pd.getString("BIANMA"));
      }
      
      this.dictionariesService.edit(pd);
    }
    
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping
  public ModelAndView list(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String PARENT_ID = pd.getString("PARENT_ID");
    
    if ((PARENT_ID != null) && (!"".equals(PARENT_ID)) && (!"0".equals(PARENT_ID)))
    {
      PageData pdp = new PageData();
      pdp = getPageData();
      
      pdp.put("ZD_ID", PARENT_ID);
      pdp = this.dictionariesService.findById(pdp);
      mv.addObject("pdp", pdp);
      
      this.szdList = new ArrayList();
      getZDname(PARENT_ID);
      Collections.reverse(this.szdList);
    }
    
    String NAME = pd.getString("NAME");
    if ((NAME != null) && (!"".equals(NAME))) {
      NAME = NAME.trim();
      pd.put("NAME", NAME);
    }
    page.setShowCount(5);
    page.setPd(pd);
    List<PageData> varList = this.dictionariesService.dictlistPage(page);
    
    mv.setViewName("system/dictionaries/zd_list");
    mv.addObject("varList", varList);
    mv.addObject("varsList", this.szdList);
    mv.addObject("pd", pd);
    
    return mv;
  }
  
  public void getZDname(String PARENT_ID)
  {
    logBefore(this.logger, "递归");
    try {
      PageData pdps = new PageData();
      pdps.put("ZD_ID", PARENT_ID);
      pdps = this.dictionariesService.findById(pdps);
      if (pdps != null) {
        this.szdList.add(pdps);
        String PARENT_IDs = pdps.getString("PARENT_ID");
        getZDname(PARENT_IDs);
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/toAdd"})
  public ModelAndView toAdd(Page page)
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      mv.setViewName("system/dictionaries/zd_edit");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    
    return mv;
  }
  
  @RequestMapping({"/toEdit"})
  public ModelAndView toEdit(String ROLE_ID)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    pd = this.dictionariesService.findById(pd);
    if (Integer.parseInt(this.dictionariesService.findCount(pd).get("ZS").toString()) != 0) {
      mv.addObject("msg", "no");
    } else {
      mv.addObject("msg", "ok");
    }
    mv.setViewName("system/dictionaries/zd_edit");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/has"})
  public void has(PrintWriter out)
  {
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (this.dictionariesService.findBmCount(pd) != null) {
        out.write("error");
      } else {
        out.write("success");
      }
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/del"})
  @ResponseBody
  public Object del()
  {
    Map<String, String> map = new HashMap();
    PageData pd = new PageData();
    String errInfo = "";
    try {
      pd = getPageData();
      
      if (Integer.parseInt(this.dictionariesService.findCount(pd).get("ZS").toString()) != 0) {
        errInfo = "false";
      } else {
        this.dictionariesService.delete(pd);
        errInfo = "success";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
}


