package com.hy.interceptor;

import com.hy.entity.system.User;
import com.hy.util.Jurisdiction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginHandlerInterceptor
  extends HandlerInterceptorAdapter
{
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    throws Exception
  {
    String path = request.getServletPath();
    if (path.matches(".*/((login)|(logout)|(planOrder)|(code)|(app)|(weixin)|(static)|(main)|(websocket)|(handheld)|(warehousingcontroller)).*")) {
      return true;
    }
    
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    User user = (User)session.getAttribute("sessionUser");
    if (user != null) {
      path = path.substring(1, path.length());
      boolean b = Jurisdiction.hasJurisdiction(path);
      if (!b) {
        response.sendRedirect(request.getContextPath() + "/login_toLogin.do");
      }
      return b;
    }
    
    response.sendRedirect(request.getContextPath() + "/login_toLogin.do");
    return false;
  }
}


