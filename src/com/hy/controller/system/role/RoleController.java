package com.hy.controller.system.role;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.system.Menu;
import com.hy.entity.system.Role;
import com.hy.service.system.menu.MenuService;
import com.hy.service.system.role.RoleService;
import com.hy.util.AppUtil;
import com.hy.util.Jurisdiction;
import com.hy.util.Logger;
import com.hy.util.PageData;
import com.hy.util.RightsHelper;
import com.hy.util.StringUtil;
import com.hy.util.Tools;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/role"})
public class RoleController
  extends BaseController
{
  String menuUrl = "role.do";
  
  @Resource(name="menuService")
  private MenuService menuService;
  @Resource(name="roleService")
  private RoleService roleService;
  
  @RequestMapping({"/qx"})
  public ModelAndView qx()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String msg = pd.getString("msg");
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) this.roleService.updateQx(msg, pd);
      mv.setViewName("save_result");
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/kfqx"})
  public ModelAndView kfqx()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String msg = pd.getString("msg");
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) this.roleService.updateKFQx(msg, pd);
      mv.setViewName("save_result");
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/gysqxc"})
  public ModelAndView gysqxc()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String msg = pd.getString("msg");
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) this.roleService.gysqxc(msg, pd);
      mv.setViewName("save_result");
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping
  public ModelAndView list(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
    String roleId = pd.getString("ROLE_ID");
    if ((roleId == null) || ("".equals(roleId))) {
      pd.put("ROLE_ID", Integer.valueOf(1));
    }
    List<Role> roleList = this.roleService.listAllRoles();
    List<Role> roleList_z = this.roleService.listAllRolesByPId(pd);
    
    List<PageData> kefuqxlist = this.roleService.listAllkefu(pd);
    List<PageData> gysqxlist = this.roleService.listAllGysQX(pd);
    pd = this.roleService.findObjectById(pd);
    mv.addObject("pd", pd);
    mv.addObject("kefuqxlist", kefuqxlist);
    mv.addObject("gysqxlist", gysqxlist);
    mv.addObject("roleList", roleList);
    mv.addObject("roleList_z", roleList_z);
    mv.setViewName("system/role/role_list");
    mv.addObject("QX", getHC());
    
    return mv;
  }
  
  @RequestMapping({"/toAdd"})
  public ModelAndView toAdd(Page page)
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      mv.setViewName("system/role/role_add");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping(value={"/add"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView add()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      
      String parent_id = pd.getString("PARENT_ID");
      if (!StringUtil.isEmpty(parent_id)) {
        pd.put("ROLE_ID", Long.valueOf(Long.parseLong(parent_id)));
      }
      if ("0".equals(parent_id)) {
        pd.put("RIGHTS", "");
      } else {
        String rights = this.roleService.findObjectById(pd).getString("RIGHTS");
        pd.put("RIGHTS", rights == null ? "" : rights);
      }
      
      pd.put("QX_ID", Integer.valueOf(0));
      pd.put("FX_QX", Integer.valueOf(0));
      pd.put("FW_QX", Integer.valueOf(0));
      pd.put("QX1", Integer.valueOf(0));
      pd.put("QX2", Integer.valueOf(0));
      pd.put("QX3", Integer.valueOf(0));
      pd.put("QX4", Integer.valueOf(0));
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "add")) { 
    	  this.roleService.saveKeFu(pd);
    	  pd.put("QX_ID", pd.getString("GL_ID"));
    	  pd.put("U_ID", pd.getString("GL_ID"));
      }
      pd.put("C1", Integer.valueOf(0));
      pd.put("C2", Integer.valueOf(0));
      pd.put("C3", Integer.valueOf(0));
      pd.put("C4", Integer.valueOf(0));
      pd.put("Q1", Integer.valueOf(0));
      pd.put("Q2", Integer.valueOf(0));
      pd.put("Q3", Integer.valueOf(0));
      pd.put("Q4", Integer.valueOf(0));
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "add")) { this.roleService.saveGYSQX(pd);
      }
      pd.put("ADD_QX", "0");
      pd.put("DEL_QX", "0");
      pd.put("EDIT_QX", "0");
      pd.put("CHA_QX", "0");
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "add")) this.roleService.add(pd);
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/toEdit"})
  public ModelAndView toEdit(String ROLE_ID)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (!StringUtil.isEmpty(ROLE_ID)) {
        pd.put("ROLE_ID", Long.valueOf(Long.parseLong(ROLE_ID)));
      }
      pd = this.roleService.findObjectById(pd);
      mv.setViewName("system/role/role_edit");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/edit"})
  public ModelAndView edit()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) pd = this.roleService.edit(pd);
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/auth"})
  public String auth(@RequestParam String ROLE_ID, Model model)
    throws Exception
  {
    try
    {
      List<Menu> menuList = this.menuService.listAllMenu();
      Role role = this.roleService.getRoleById(ROLE_ID);
      String roleRights = role.getRIGHTS();
      if (Tools.notEmpty(roleRights)) {
        for (Menu menu : menuList) {
          menu.setHasMenu(RightsHelper.testRights(roleRights, Integer.parseInt(String.valueOf(menu.getMENU_ID()))));
          if (menu.isHasMenu()) {
            List<Menu> subMenuList = menu.getSubMenu();
            for (Menu sub : subMenuList) {
              sub.setHasMenu(RightsHelper.testRights(roleRights, Integer.parseInt(String.valueOf(sub.getMENU_ID()))));
            }
          }
        }
      }
      JSONArray arr = JSONArray.fromObject(menuList);
      String json = arr.toString();
      json = json.replaceAll("MENU_ID", "id").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
      model.addAttribute("zTreeNodes", json);
      model.addAttribute("roleId", ROLE_ID);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    
    return "authorization";
  }
  
  @RequestMapping({"/button"})
  public ModelAndView button(@RequestParam String ROLE_ID, @RequestParam String msg, Model model)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    try {
      List<Menu> menuList = this.menuService.listAllMenu();
      Role role = this.roleService.getRoleById(ROLE_ID);
      
      String roleRights = "";
      if ("add_qx".equals(msg)) {
        roleRights = role.getADD_QX();
      } else if ("del_qx".equals(msg)) {
        roleRights = role.getDEL_QX();
      } else if ("edit_qx".equals(msg)) {
        roleRights = role.getEDIT_QX();
      } else if ("cha_qx".equals(msg)) {
        roleRights = role.getCHA_QX();
      }
      
      if (Tools.notEmpty(roleRights)) {
        for (Menu menu : menuList) {
          menu.setHasMenu(RightsHelper.testRights(roleRights, Integer.parseInt(String.valueOf(menu.getMENU_ID()))));
          if (menu.isHasMenu()) {
            List<Menu> subMenuList = menu.getSubMenu();
            for (Menu sub : subMenuList) {
              sub.setHasMenu(RightsHelper.testRights(roleRights, Integer.parseInt(String.valueOf(sub.getMENU_ID()))));
            }
          }
        }
      }
      JSONArray arr = JSONArray.fromObject(menuList);
      String json = arr.toString();
      
      json = json.replaceAll("MENU_ID", "id").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
      mv.addObject("zTreeNodes", json);
      mv.addObject("roleId", ROLE_ID);
      mv.addObject("msg", msg);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    mv.setViewName("system/role/role_button");
    return mv;
  }
  
  @RequestMapping({"/auth/save"})
  public void saveAuth(@RequestParam String ROLE_ID, @RequestParam String menuIds, PrintWriter out)
    throws Exception
  {
    PageData pd = new PageData();
    try {
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) {
        if ((menuIds != null) && (!"".equals(menuIds.trim()))) {
          BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
          Role role = this.roleService.getRoleById(ROLE_ID);
          role.setRIGHTS(rights.toString());
          this.roleService.updateRoleRights(role);
          pd.put("rights", rights.toString());
        } else {
          Role role = new Role();
          role.setRIGHTS("");
          role.setROLE_ID(ROLE_ID);
          this.roleService.updateRoleRights(role);
          pd.put("rights", "");
        }
        
        if (!StringUtil.isEmpty(ROLE_ID)) {
          pd.put("roleId", Long.valueOf(Long.parseLong(ROLE_ID)));
        }
        this.roleService.setAllRights(pd);
      }
      out.write("success");
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/roleButton/save"})
  public void orleButton(@RequestParam String ROLE_ID, @RequestParam String menuIds, @RequestParam String msg, PrintWriter out)
    throws Exception
  {
    PageData pd = new PageData();
    pd = getPageData();
    try {
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "edit")) {
        if ((menuIds != null) && (!"".equals(menuIds.trim()))) {
          BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
          pd.put("value", rights.toString());
        } else {
          pd.put("value", "");
        }
        if (!StringUtil.isEmpty(ROLE_ID)) {
          pd.put("ROLE_ID", Long.valueOf(Long.parseLong(ROLE_ID)));
        }
        this.roleService.updateQx(msg, pd);
      }
      out.write("success");
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/delete"})
  @ResponseBody
  public Object deleteRole(@RequestParam String ROLE_ID)
    throws Exception
  {
    Map<String, String> map = new HashMap();
    PageData pd = new PageData();
    String errInfo = "";
    try {
      if (Jurisdiction.buttonJurisdiction(this.menuUrl, "del")) {
        if (!StringUtil.isEmpty(ROLE_ID)) {
          pd.put("ROLE_ID", Long.valueOf(Long.parseLong(ROLE_ID)));
        }
        List<Role> roleList_z = this.roleService.listAllRolesByPId(pd);
        if (roleList_z.size() > 0) {
          errInfo = "false";
        }
        else {
          List<PageData> userlist = this.roleService.listAllUByRid(pd);
          List<PageData> appuserlist = this.roleService.listAllAppUByRid(pd);
          if ((userlist.size() > 0) || (appuserlist.size() > 0)) {
            errInfo = "false2";
          } else {
            this.roleService.deleteRoleById(ROLE_ID);
            this.roleService.deleteKeFuById(ROLE_ID);
            this.roleService.deleteGById(ROLE_ID);
            errInfo = "success";
          }
        }
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  public Map<String, String> getHC()
  {
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    return (Map)session.getAttribute("QX");
  }
}


