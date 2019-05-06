package com.hy.service.system.appuser;

import com.hy.controller.app.appuser.WeixinController;
import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.FileUtil;
import com.hy.util.PageData;
import com.hy.util.wx.Sms;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("wxService")
public class WxService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void saveOrder(PageData pd)
    throws Exception
  {
    this.dao.save("wx.saveOrder", pd);
  }
  
  public List<PageData> updateorderlistPage(Page pd) throws Exception { return (List)this.dao.findForList("wx.updateorderlistPage", pd); }
  
  public List<PageData> updateorderlist() throws Exception
  {
    return (List)this.dao.findForList("wx.updateorderlist", null);
  }
  
  public PageData getupdateorder(PageData pd) throws Exception {
    return (PageData)this.dao.findForObject("wx.getupdateorder", pd);
  }
  
  public List<PageData> getWxUserListPage(Page pd)
    throws Exception
  {
    return (List)this.dao.findForList("wx.getWxUserlistPage", pd);
  }
  
  public Boolean saveWxUser(PageData pd)
    throws Exception
  {
    this.dao.save("wx.saveWxUser", pd);
    int result = Integer.parseInt(pd.get("id").toString());
    if (result > 0) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  public PageData findByphone(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("wx.findByphone", pd);
  }
  
  public PageData getWxUserByUserid(String Userid)
    throws Exception
  {
    return (PageData)this.dao.findForObject("wx.getWxUserByUserid", Userid);
  }
  
  public void updateWxUser(PageData pd)
    throws Exception
  {
    this.dao.update("wx.updateWxUser", pd);
  }
  
  public void deleteWxUser(PageData pd)
    throws Exception
  {
    this.dao.update("wx.deleteWxUser", pd);
  }
  public void findByphone()
    throws Exception
  {
    FileUtil.findByphone();
  }
  public List<PageData> getWxSmslistPage(Page pd)
    throws Exception
  {
    return (List)this.dao.findForList("wx.getWxSmslistPage", pd);
  }
  
  public void saveSms()
    throws Exception
  {
    String str = Sms.getSMS();
    if (str != null) {
      String[] strList = str.split("\\|\\|");
      for (int i = 1; i < strList.length; i++) {
        String[] sms = strList[i].split("#");
        PageData pd = new PageData();
        pd.put("phone", sms[0]);
        pd.put("content", sms[1]);
        pd.put("create_time", sms[2]);
        this.dao.save("wx.saveSms", pd);
      }
    }
  }
}


