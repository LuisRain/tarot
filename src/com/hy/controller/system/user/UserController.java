package com.hy.controller.system.user;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.system.Role;
import com.hy.service.inventory.WarehouseService;
import com.hy.service.system.menu.MenuService;
import com.hy.service.system.role.RoleService;
import com.hy.service.system.user.UserService;
import com.hy.util.AppUtil;
import com.hy.util.FileDownload;
import com.hy.util.FileUpload;
import com.hy.util.GetPinyin;
import com.hy.util.Jurisdiction;
import com.hy.util.Logger;
import com.hy.util.ObjectExcelRead;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import com.hy.util.Tools;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/user"})
public class UserController
  extends BaseController
{
  String menuUrl = "user/listUsers.do";
  
  @Resource(name="userService")
  private UserService userService;
  
  @Resource(name="roleService")
  private RoleService roleService;
  @Resource(name="menuService")
  private MenuService menuService;
  @Resource(name="warehouseService")
  private WarehouseService warehouseService;
  
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
    pd.put("STATUS", "0");
    pd.put("SKIN", "default");
    
    pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());
    
    if (this.userService.findByUId(pd) == null) {
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "add")) this.userService.saveU(pd);
      mv.addObject("msg", "success");
    } else {
      mv.addObject("msg", "failed");
    }
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
      if (this.userService.findByUId(pd) != null) {
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
      
      if (this.userService.findByUE(pd) != null) {
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
      if (this.userService.findByUN(pd) != null) {
        errInfo = "error";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/editU"})
  public ModelAndView editU()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    if ((pd.getString("PASSWORD") != null) && (!"".equals(pd.getString("PASSWORD")))) {
      pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());
    }
    /*if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) this.userService.editU(pd);*/
    this.userService.editU(pd);
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/goEditU"})
  public ModelAndView goEditU()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
    String fx = pd.getString("fx");
    if ("head".equals(fx)) {
      mv.addObject("fx", "head");
    } else {
      mv.addObject("fx", "user");
    }
    List<PageData> wli= warehouseService.warehouselist(null);
    List<Role> roleList = this.roleService.listAllERRoles();
    pd = this.userService.findByUiId(pd);
    mv.setViewName("system/user/user_edit");
    mv.addObject("msg", "editU");
    mv.addObject("pd", pd);
    mv.addObject("warehouse",wli);
    mv.addObject("roleList", roleList);
    
    return mv;
  }
  
  @RequestMapping({"/goAddU"})
  public ModelAndView goAddU()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    List<Role> roleList = this.roleService.listAllERRoles();
    List<PageData> wli= warehouseService.warehouselist(null);
    mv.setViewName("system/user/user_edit");
    mv.addObject("msg", "saveU");
    mv.addObject("pd", pd);
    mv.addObject("warehouse",wli);
    mv.addObject("roleList", roleList);
    
    return mv;
  }
  
  @RequestMapping({"/listUsers"})
  public ModelAndView listUsers(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
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
    page.setPd(pd);
    List<PageData> userList = this.userService.listPdPageUser(page);
    List<Role> roleList = this.roleService.listAllERRoles();
    
    mv.setViewName("system/user/user_list");
    mv.addObject("userList", userList);
    mv.addObject("roleList", roleList);
    mv.addObject("pd", pd);
    mv.addObject("QX", getHC());
    return mv;
  }
  
  @RequestMapping({"/listtabUsers"})
  public ModelAndView listtabUsers(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    List<PageData> userList = this.userService.listAllUser(pd);
    mv.setViewName("system/user/user_tb_list");
    mv.addObject("userList", userList);
    mv.addObject("pd", pd);
    mv.addObject("QX", getHC());
    return mv;
  }
  
  @RequestMapping({"/deleteU"})
  public void deleteU(PrintWriter out)
  {
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "del")) this.userService.deleteU(pd);
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
        if (Jurisdiction.buttonJurisdiction(this.menuUrl, "del")) this.userService.deleteAllU(ArrayUSER_IDS);
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
        titles.add("职位");
        titles.add("手机");
        titles.add("邮箱");
        titles.add("最近登录");
        titles.add("上次登录IP");
        
        dataMap.put("titles", titles);
        
        List<PageData> userList = this.userService.listAllUser(pd);
        List<PageData> varList = new ArrayList();
        for (int i = 0; i < userList.size(); i++) {
          PageData vpd = new PageData();
          vpd.put("var1", ((PageData)userList.get(i)).getString("USERNAME"));
          vpd.put("var2", ((PageData)userList.get(i)).getString("NUMBER"));
          vpd.put("var3", ((PageData)userList.get(i)).getString("NAME"));
          vpd.put("var4", ((PageData)userList.get(i)).getString("ROLE_NAME"));
          vpd.put("var5", ((PageData)userList.get(i)).getString("PHONE"));
          vpd.put("var6", ((PageData)userList.get(i)).getString("EMAIL"));
          vpd.put("var7", ((PageData)userList.get(i)).getString("LAST_LOGIN"));
          vpd.put("var8", ((PageData)userList.get(i)).getString("IP"));
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
  
  @RequestMapping({"/goUploadExcel"})
  public ModelAndView goUploadExcel()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("system/user/uploadexcel");
    return mv;
  }
  
  @RequestMapping({"/downExcel"})
  public void downExcel(HttpServletResponse response)
    throws Exception
  {
    FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "Users.xls", "Users.xls");
  }
  
  @RequestMapping({"/readExcel"})
  public ModelAndView readExcel(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    if (!Jurisdiction.buttonJurisdiction(this.menuUrl, "add")) return null;
    if ((file != null) && (!file.isEmpty())) {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
      String fileName = FileUpload.fileUp(file, filePath, "userexcel");
      
      List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);
      
      pd.put("RIGHTS", "");
      pd.put("LAST_LOGIN", "");
      pd.put("IP", "");
      pd.put("STATUS", "0");
      pd.put("SKIN", "default");
      
      List<Role> roleList = this.roleService.listAllERRoles();
      
      pd.put("ROLE_ID", ((Role)roleList.get(0)).getROLE_ID());
      
      for (int i = 0; i < listPd.size(); i++) {
        pd.put("NAME", ((PageData)listPd.get(i)).getString("var1"));
        
        String USERNAME = GetPinyin.getPingYin(((PageData)listPd.get(i)).getString("var1"));
        pd.put("USERNAME", USERNAME);
        if (this.userService.findByUId(pd) != null) {
          USERNAME = GetPinyin.getPingYin(((PageData)listPd.get(i)).getString("var1")) + Tools.getRandomNum();
          pd.put("USERNAME", USERNAME);
        }
        pd.put("BZ", ((PageData)listPd.get(i)).getString("var4"));
        if (Tools.checkEmail(((PageData)listPd.get(i)).getString("var3"))) {
          pd.put("EMAIL", ((PageData)listPd.get(i)).getString("var3"));
          if (this.userService.findByUE(pd) == null)
          {
            pd.put("NUMBER", ((PageData)listPd.get(i)).getString("var0"));
            pd.put("PHONE", ((PageData)listPd.get(i)).getString("var2"));
            pd.put("PASSWORD", new SimpleHash("SHA-1", USERNAME, "123").toString());
            if (this.userService.findByUN(pd) == null)
            {
              this.userService.saveU(pd); }
          }
        }
      }
      mv.addObject("msg", "success");
    }
    
    mv.setViewName("save_result");
    return mv;
  }
  
  @InitBinder
  public void initBinder(WebDataBinder binder) {
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


