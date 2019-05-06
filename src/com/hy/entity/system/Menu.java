package com.hy.entity.system;

import java.io.Serializable;
import java.util.List;

public class Menu
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long MENU_ID;
  private String MENU_NAME;
  private String MENU_URL;
  private long PARENT_ID;
  private int MENU_ORDER;
  private String MENU_ICON;
  private int MENU_TYPE;
  private String target;
  private Menu parentMenu;
  private List<Menu> subMenu;
  private boolean hasMenu = false;
  
  public long getMENU_ID()
  {
    return this.MENU_ID;
  }
  
  public void setMENU_ID(long mENU_ID) { this.MENU_ID = mENU_ID; }
  
  public String getMENU_NAME() {
    return this.MENU_NAME;
  }
  
  public void setMENU_NAME(String mENU_NAME) { this.MENU_NAME = mENU_NAME; }
  
  public String getMENU_URL() {
    return this.MENU_URL;
  }
  
  public void setMENU_URL(String mENU_URL) { this.MENU_URL = mENU_URL; }
  
  public Menu getParentMenu() {
    return this.parentMenu;
  }
  
  public void setParentMenu(Menu parentMenu) { this.parentMenu = parentMenu; }
  
  public List<Menu> getSubMenu() {
    return this.subMenu;
  }
  
  public void setSubMenu(List<Menu> subMenu) { this.subMenu = subMenu; }
  
  public boolean isHasMenu() {
    return this.hasMenu;
  }
  
  public void setHasMenu(boolean hasMenu) { this.hasMenu = hasMenu; }
  
  public String getTarget() {
    return this.target;
  }
  
  public void setTarget(String target) { this.target = target; }
  
  public String getMENU_ICON() {
    return this.MENU_ICON;
  }
  
  public void setMENU_ICON(String mENU_ICON) { this.MENU_ICON = mENU_ICON; }
  
  public long getPARENT_ID() {
    return this.PARENT_ID;
  }
  
  public void setPARENT_ID(long pARENT_ID) { this.PARENT_ID = pARENT_ID; }
  
  public int getMENU_ORDER() {
    return this.MENU_ORDER;
  }
  
  public void setMENU_ORDER(int mENU_ORDER) { this.MENU_ORDER = mENU_ORDER; }
  
  public int getMENU_TYPE() {
    return this.MENU_TYPE;
  }
  
  public void setMENU_TYPE(int mENU_TYPE) { this.MENU_TYPE = mENU_TYPE; }
}


