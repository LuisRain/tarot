package com.hy.controller.system.menu;

import com.hy.controller.base.BaseController;
import com.hy.entity.system.Menu;
import com.hy.service.system.menu.MenuService;
import com.hy.util.Logger;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/menu"})
public class MenuController
  extends BaseController
{
  @Resource(name="menuService")
  private MenuService menuService;
  
  @RequestMapping
  public ModelAndView list()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    try {
      List<Menu> menuList = this.menuService.listAllParentMenu();
      mv.addObject("menuList", menuList);
      mv.setViewName("system/menu/menu_list");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    
    return mv;
  }
  
  @RequestMapping({"/toAdd"})
  public ModelAndView toAdd()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    try {
      List<Menu> menuList = this.menuService.listAllParentMenu();
      mv.addObject("menuList", menuList);
      mv.setViewName("system/menu/menu_add");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/add"})
  public ModelAndView add()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    Menu menu = new Menu();
    pd = getPageData();
    try {
      long PARENT_ID = 0L;
      if (!StringUtil.isEmpty(pd.getString("MENU_NAME"))) {
        menu.setMENU_NAME(pd.getString("MENU_NAME"));
        if (!StringUtil.isEmpty(pd.getString("PARENT_ID1"))) {
          PARENT_ID = Long.parseLong(pd.getString("PARENT_ID1"));
          if (PARENT_ID != 0L)
          {
            menu.setPARENT_ID(PARENT_ID);
            pd.put("MENU_ID", Long.valueOf(PARENT_ID));
            pd = this.menuService.getMenuById(pd);
            if (!StringUtil.isEmpty(pd.getString("MENU_TYPE"))) {
              menu.setMENU_TYPE(Integer.parseInt(pd.getString("MENU_TYPE")));
            }
          }
          else {
            menu.setMENU_TYPE(1);
          }
        }
        if (!StringUtil.isEmpty(pd.getString("MENU_ORDER"))) {
          menu.setMENU_ORDER(Integer.parseInt(pd.getString("MENU_ORDER")));
        }
        menu.setMENU_URL(pd.getString("MENU_URL"));
        menu.setMENU_ICON("icon-calendar");
      }
      this.menuService.saveMenu(menu);
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
      mv.addObject("msg", "failed");
    }
    
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/toEdit"})
  public ModelAndView toEdit(String MENU_ID)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (!StringUtil.isEmpty(MENU_ID)) {
        pd.put("MENU_ID", Long.valueOf(Long.parseLong(MENU_ID)));
      }
      pd = this.menuService.getMenuById(pd);
      List<Menu> menuList = this.menuService.listAllParentMenu();
      mv.addObject("menuList", menuList);
      mv.addObject("pd", pd);
      mv.setViewName("system/menu/menu_edit");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/toEditicon"})
  public ModelAndView toEditicon(String MENU_ID)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (!StringUtil.isEmpty(MENU_ID)) {
        pd.put("MENU_ID", Long.valueOf(Long.parseLong(MENU_ID)));
      }
      mv.addObject("pd", pd);
      mv.setViewName("system/menu/menu_icon");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/editicon"})
  public ModelAndView editicon()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      pd = this.menuService.editicon(pd);
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
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
      
      String PARENT_ID = pd.getString("PARENT_ID");
      if ((PARENT_ID == null) || ("".equals(PARENT_ID))) {
        PARENT_ID = "0";
        pd.put("PARENT_ID", Integer.valueOf(0));
      }
      
      if ("0".equals(PARENT_ID))
      {
        this.menuService.editType(pd);
      }
      
      pd = this.menuService.edit(pd);
      mv.addObject("msg", "success");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/sub"})
  public void getSub(@RequestParam String MENU_ID, HttpServletResponse response)
    throws Exception
  {
    try
    {
      List<Menu> subMenu = this.menuService.listSubMenuByParentId(Long.parseLong(StringUtil.isEmpty(MENU_ID) ? "0" : MENU_ID));
      JSONArray arr = JSONArray.fromObject(subMenu);
      
      response.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      String json = arr.toString();
      out.write(json);
      out.flush();
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/del"})
  public void delete(@RequestParam String MENU_ID, PrintWriter out)
    throws Exception
  {
    try
    {
      this.menuService.deleteMenuById(MENU_ID);
      out.write("success");
      out.flush();
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
}


