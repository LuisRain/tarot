package com.hy.util;

import com.hy.entity.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class LoginUtil
{
  public static User getLoginUser()
  {
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    User user = (User)session.getAttribute("sessionUser");
    
    return user;
  }
}


