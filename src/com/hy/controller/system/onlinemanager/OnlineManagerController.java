package com.hy.controller.system.onlinemanager;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.util.Jurisdiction;
import com.hy.util.Logger;
import com.hy.util.PageData;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/onlinemanager"})
public class OnlineManagerController
  extends BaseController
{
  String menuUrl = "onlinemanager/list.do";
  
  @RequestMapping({"/delete"})
  public void delete(PrintWriter out)
  {
    logBefore(this.logger, "删除OnlineManager");
    if (!Jurisdiction.buttonJurisdiction(this.menuUrl, "del")) return;
    PageData pd = new PageData();
    try {
      pd = getPageData();
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
    logBefore(this.logger, "修改OnlineManager");
    if (!Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) return null;
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/list"})
  public ModelAndView list(Page page)
  {
    logBefore(this.logger, "列表OnlineManager");
    
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      page.setPd(pd);
      mv.setViewName("system/onlinemanager/onlinemanager_list");
      mv.addObject("pd", pd);
      mv.addObject("QX", getHC());
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goAdd"})
  public ModelAndView goAdd()
  {
    logBefore(this.logger, "去新增OnlineManager页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      mv.setViewName("system/onlinemanager/onlinemanager_edit");
      mv.addObject("msg", "save");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  public Map<String, String> getHC()
  {
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    return (Map)session.getAttribute("QX");
  }
  
  @InitBinder
  public void initBinder(WebDataBinder binder)
  {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
  }
}


