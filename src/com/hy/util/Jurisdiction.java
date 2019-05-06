package com.hy.util;

import com.hy.entity.system.Menu;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class Jurisdiction
{
  public static boolean hasJurisdiction(String menuUrl)
  {
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    Boolean b = Boolean.valueOf(true);
    List<Menu> menuList = (List)session.getAttribute("allmenuList");
    
    for (int i = 0; i < menuList.size(); i++) {
      for (int j = 0; j < ((Menu)menuList.get(i)).getSubMenu().size(); j++) {
        if (((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_URL().split(".do")[0].equals(menuUrl.split(".do")[0])) {
          if (!((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).isHasMenu()) {
            return false;
          }
          Map<String, String> map = (Map)session.getAttribute("QX");
          map.remove("add");
          map.remove("del");
          map.remove("edit");
          map.remove("cha");
          long MENU_ID = ((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_ID();
          String USERNAME = session.getAttribute("USERNAME").toString();
          Boolean isAdmin = Boolean.valueOf("admin".equals(USERNAME));
          map.put("add", (RightsHelper.testRights((String)map.get("adds"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue()) ? "1" : "0");
          map.put("del", (RightsHelper.testRights((String)map.get("dels"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue()) ? "1" : "0");
          map.put("edit", (RightsHelper.testRights((String)map.get("edits"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue()) ? "1" : "0");
          map.put("cha", (RightsHelper.testRights((String)map.get("chas"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue()) ? "1" : "0");
          session.removeAttribute("QX");
          session.setAttribute("QX", map);
        }
      }
    }
    
    return true;
  }
  
  public static boolean buttonJurisdiction(String menuUrl, String type)
  {
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    Boolean b = Boolean.valueOf(true);
    List<Menu> menuList = (List)session.getAttribute("allmenuList");
    
    for (int i = 0; i < menuList.size(); i++) {
      for (int j = 0; j < ((Menu)menuList.get(i)).getSubMenu().size(); j++) {
        if (((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_URL().split(".do")[0].equals(menuUrl.split(".do")[0])) {
          if (!((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).isHasMenu()) {
            return false;
          }
          Map<String, String> map = (Map)session.getAttribute("QX");
          long MENU_ID = ((Menu)((Menu)menuList.get(i)).getSubMenu().get(j)).getMENU_ID();
          String USERNAME = session.getAttribute("USERNAME").toString();
          Boolean isAdmin = Boolean.valueOf("admin".equals(USERNAME));
          if ("add".equals(type))
            return (RightsHelper.testRights((String)map.get("adds"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue());
          if ("del".equals(type))
            return (RightsHelper.testRights((String)map.get("dels"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue());
          if ("edit".equals(type))
            return (RightsHelper.testRights((String)map.get("edits"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue());
          if ("cha".equals(type)) {
            return (RightsHelper.testRights((String)map.get("chas"), Integer.parseInt(String.valueOf(MENU_ID == 0L ? 0L : MENU_ID)))) || (isAdmin.booleanValue());
          }
        }
      }
    }
    
    return true;
  }
}


