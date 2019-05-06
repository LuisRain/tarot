package com.hy.service.system.syslog;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.system.User;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service("sysLogService")
public class SysLogService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void saveLog(String opAct, String result)
    throws Exception
  {
    PageData pd = new PageData();
    
    Subject currentUser = SecurityUtils.getSubject();
    Session session = currentUser.getSession();
    User user = (User)session.getAttribute("sessionUser");
    if (user != null) {
      pd.put("op_model", opAct);
      pd.put("result", result);
      pd.put("oper_name", user.getNAME());
      pd.put("login_ip", user.getIP());
    }
    this.dao.save("LogMapper.saveLog", pd);
  }
  
  public List<PageData> listPdPageSysLog(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("LogMapper.sysLoglistPage", page);
  }
  
  public List<PageData> listAllSysLog(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("LogMapper.listAllSysLoglistPage", pd);
  }
  
  public List<PageData> listAllUser(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("LogMapper.listAllSysLog", pd);
  }
}


