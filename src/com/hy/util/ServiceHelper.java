package com.hy.util;

import com.hy.service.system.menu.MenuService;
import com.hy.service.system.role.RoleService;
import com.hy.service.system.user.UserService;
import org.springframework.context.ApplicationContext;

public final class ServiceHelper
{
  public static Object getService(String serviceName)
  {
    return Const.WEB_APP_CONTEXT.getBean(serviceName);
  }
  
  public static UserService getUserService() {
    return (UserService)getService("userService");
  }
  
  public static RoleService getRoleService() {
    return (RoleService)getService("roleService");
  }
  
  public static MenuService getMenuService() {
    return (MenuService)getService("menuService");
  }
}


