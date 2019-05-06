package com.hy.service.system.appuser;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("appuserService")
public class AppuserService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public PageData findByUiId(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("AppuserMapper.findByUiId", pd);
  }
  
  public PageData findByUId(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("AppuserMapper.findByUId", pd);
  }
  
  public PageData findByUE(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("AppuserMapper.findByUE", pd);
  }
  
  public PageData findByUN(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("AppuserMapper.findByUN", pd);
  }
  
  public void saveU(PageData pd)
    throws Exception
  {
    this.dao.save("AppuserMapper.saveU", pd);
  }
  
  public void editU(PageData pd)
    throws Exception
  {
    this.dao.update("AppuserMapper.editU", pd);
  }
  
  public void deleteU(PageData pd)
    throws Exception
  {
    this.dao.delete("AppuserMapper.deleteU", pd);
  }
  
  public void deleteAllU(String[] USER_IDS)
    throws Exception
  {
    this.dao.delete("AppuserMapper.deleteAllU", USER_IDS);
  }
  
  public List<PageData> listAllUser(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("AppuserMapper.listAllUser", pd);
  }
  
  public List<PageData> listPdPageUser(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("AppuserMapper.userlistPage", page);
  }
  
  public void saveIP(PageData pd)
    throws Exception
  {
    this.dao.update("AppuserMapper.saveIP", pd);
  }
  
  public PageData getUserByNameAndPwd(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("AppuserMapper.getUserInfo", pd);
  }
  
  public void updateLastLogin(PageData pd)
    throws Exception
  {
    this.dao.update("AppuserMapper.updateLastLogin", pd);
  }
}


