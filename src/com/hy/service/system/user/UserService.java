package com.hy.service.system.user;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.system.User;
import com.hy.util.PageData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("userService")
public class UserService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public PageData findByUiId(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("UserXMapper.findByUiId", pd);
  }
  
  public PageData findByUId(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("UserXMapper.findByUId", pd);
  }
  
  public void updateusername(PageData pd) throws Exception{
	  
	  dao.update("UserXMapper.updateusername", pd);
  }
  
  public PageData findByUE(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("UserXMapper.findByUE", pd);
  }
  
  public PageData findByUN(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("UserXMapper.findByUN", pd);
  }
  
  public void saveU(PageData pd)
    throws Exception
  {
    this.dao.save("UserXMapper.saveU", pd);
  }
  
  public void editU(PageData pd)
    throws Exception
  {
    this.dao.update("UserXMapper.editU", pd);
  }
  
  public void setSKIN(PageData pd)
    throws Exception
  {
    this.dao.update("UserXMapper.setSKIN", pd);
  }
  
  public void deleteU(PageData pd)
    throws Exception
  {
    this.dao.delete("UserXMapper.deleteU", pd);
  }
  
  public void deleteAllU(String[] USER_IDS)
    throws Exception
  {
    this.dao.delete("UserXMapper.deleteAllU", USER_IDS);
  }
  
  public List<PageData> listPdPageUser(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("UserXMapper.userlistPage", page);
  }
  
  public List<PageData> listAllUser(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("UserXMapper.listAllUser", pd);
  }
  
  public List<PageData> listGPdPageUser(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("UserXMapper.userGlistPage", page);
  }
  
  public void saveIP(PageData pd)
    throws Exception
  {
    this.dao.update("UserXMapper.saveIP", pd);
  }
  
  public PageData getUserByNameAndPwd(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("UserXMapper.getUserInfo", pd);
  }
  
  public void updateLastLogin(PageData pd)
    throws Exception
  {
    this.dao.update("UserXMapper.updateLastLogin", pd);
  }
  
  public User getUserAndRoleById(long USER_ID)
    throws Exception
  {
    return (User)this.dao.findForObject("UserMapper.getUserAndRoleById", Long.valueOf(USER_ID));
  }
}


