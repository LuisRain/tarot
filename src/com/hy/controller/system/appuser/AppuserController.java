package com.hy.controller.system.appuser;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.system.Role;
import com.hy.service.system.appuser.AppuserService;
import com.hy.service.system.role.RoleService;
import com.hy.util.AppUtil;
import com.hy.util.Jurisdiction;
import com.hy.util.Logger;
import com.hy.util.MD5;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/happuser"})
public class AppuserController
  extends BaseController
{
  String menuUrl = "happuser/listUsers.do";
  @Resource(name="appuserService")
  private AppuserService appuserService;
  @Resource(name="roleService")
  private RoleService roleService;
  
  @RequestMapping({"/saveU"})
  public ModelAndView saveU(PrintWriter out)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
    pd.put("RIGHTS", "");
    pd.put("LAST_LOGIN", "");
    pd.put("IP", "");
    
    pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
    
    if (this.appuserService.findByUId(pd) == null) {
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "add")) this.appuserService.saveU(pd);
      mv.addObject("msg", "success");
    } else {
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/editU"})
  public ModelAndView editU(PrintWriter out)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    if ((pd.getString("PASSWORD") != null) && (!"".equals(pd.getString("PASSWORD")))) {
      pd.put("PASSWORD", MD5.md5(pd.getString("PASSWORD")));
    }
    if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) this.appuserService.editU(pd);
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/hasU"})
  @ResponseBody
  public Object hasU()
  {
    Map<String, String> map = new HashMap();
    String errInfo = "success";
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (this.appuserService.findByUId(pd) != null) {
        errInfo = "error";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/hasE"})
  @ResponseBody
  public Object hasE()
  {
    Map<String, String> map = new HashMap();
    String errInfo = "success";
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (this.appuserService.findByUE(pd) != null) {
        errInfo = "error";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/hasN"})
  @ResponseBody
  public Object hasN()
  {
    Map<String, String> map = new HashMap();
    String errInfo = "success";
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (this.appuserService.findByUN(pd) != null) {
        errInfo = "error";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/goEditU"})
  public ModelAndView goEditU()
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      List<Role> roleList = this.roleService.listAllappERRoles();
      pd = this.appuserService.findByUiId(pd);
      mv.setViewName("system/appuser/appuser_edit");
      mv.addObject("msg", "editU");
      mv.addObject("pd", pd);
      mv.addObject("roleList", roleList);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goAddU"})
  public ModelAndView goAddU()
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      List<Role> roleList = this.roleService.listAllappERRoles();
      mv.setViewName("system/appuser/appuser_edit");
      mv.addObject("msg", "saveU");
      mv.addObject("pd", pd);
      mv.addObject("roleList", roleList);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/listUsers"})
  public ModelAndView listUsers(Page page)
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      
      String USERNAME = pd.getString("USERNAME");
      
      if ((USERNAME != null) && (!"".equals(USERNAME))) {
        USERNAME = USERNAME.trim();
        pd.put("USERNAME", USERNAME);
      }
      
      page.setPd(pd);
      List<PageData> userList = this.appuserService.listPdPageUser(page);
      List<Role> roleList = this.roleService.listAllappERRoles();
      
      mv.setViewName("system/appuser/appuser_list");
      mv.addObject("userList", userList);
      mv.addObject("roleList", roleList);
      mv.addObject("pd", pd);
      mv.addObject("QX", getHC());
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    
    return mv;
  }
  
  @RequestMapping({"/deleteU"})
  public void deleteU(PrintWriter out)
  {
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "del")) this.appuserService.deleteU(pd);
      out.write("success");
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/deleteAllU"})
  @ResponseBody
  public Object deleteAllU()
  {
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      pd = getPageData();
      List<PageData> pdList = new ArrayList();
      String USER_IDS = pd.getString("USER_IDS");
      
      if ((USER_IDS != null) && (!"".equals(USER_IDS))) {
        String[] ArrayUSER_IDS = USER_IDS.split(",");
        if (Jurisdiction.buttonJurisdiction(this.menuUrl, "del")) this.appuserService.deleteAllU(ArrayUSER_IDS);
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
  
  @RequestMapping({"/excel"})
  public ModelAndView exportExcel()
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "cha"))
      {
        String USERNAME = pd.getString("USERNAME");
        if ((USERNAME != null) && (!"".equals(USERNAME))) {
          USERNAME = USERNAME.trim();
          pd.put("USERNAME", USERNAME);
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
        
        Map<String, Object> dataMap = new HashMap();
        List<String> titles = new ArrayList();
        
        titles.add("用户名");
        titles.add("编号");
        titles.add("姓名");
        titles.add("手机号");
        titles.add("身份证号");
        titles.add("等级");
        titles.add("邮箱");
        titles.add("最近登录");
        titles.add("到期时间");
        titles.add("上次登录IP");
        
        dataMap.put("titles", titles);
        
        List<PageData> userList = this.appuserService.listAllUser(pd);
        List<PageData> varList = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
          PageData vpd = new PageData();
          vpd.put("var1", ((PageData)userList.get(i)).getString("USERNAME"));
          vpd.put("var2", ((PageData)userList.get(i)).getString("NUMBER"));
          vpd.put("var3", ((PageData)userList.get(i)).getString("NAME"));
          vpd.put("var4", ((PageData)userList.get(i)).getString("PHONE"));
          vpd.put("var5", ((PageData)userList.get(i)).getString("SFID"));
          vpd.put("var6", ((PageData)userList.get(i)).getString("ROLE_NAME"));
          vpd.put("var7", ((PageData)userList.get(i)).getString("EMAIL"));
          vpd.put("var8", ((PageData)userList.get(i)).getString("LAST_LOGIN"));
          vpd.put("var9", ((PageData)userList.get(i)).getString("END_TIME"));
          vpd.put("var10", ((PageData)userList.get(i)).getString("IP"));
          varList.add(vpd);
        }
        
        dataMap.put("varList", varList);
        
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @InitBinder
  public void initBinder(WebDataBinder binder)
  {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
  }
  
  public Map<String, String> getHC()
  {
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    return (Map)session.getAttribute("QX");
  }
}


