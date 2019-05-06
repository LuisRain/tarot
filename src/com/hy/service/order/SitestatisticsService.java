package com.hy.service.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;

@Service("SitestatService")
public class SitestatisticsService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  
  
  
  
  public List<PageData> findSiteMerchantlitPage(Page page) throws Exception{
	  
	  return (List<PageData>) this.dao.findForList("SitestatisticsMapper.findSiteMerchantlistPage", page);
  }
  /***详情***/
  public List<PageData> findSiteitemlitPage(Page page) throws Exception{
	  
	  return (List<PageData>) dao.findForList("SitestatisticsMapper.findSiteitemlistPage", page);
  }
  public List<PageData> findsitestat() throws Exception{
	  return (List<PageData>) dao.findForList("SitestatisticsMapper.findsitestat", null);
  }
  
  public PageData findsitebyid(PageData pd) throws Exception{
	  
	  return (PageData) dao.findForObject("SitestatisticsMapper.findsitebyid", pd);
  }
  
  
  public void saveSite(PageData pd) throws Exception{
	  
	  dao.save("SitestatisticsMapper.saveSite", pd);
  }
  
  public void saveSiteItem(PageData pd) throws Exception{
	  
	  dao.save("SitestatisticsMapper.saveSiteItem", pd);
  }

  public void updateSiteInventory(PageData prSite) throws Exception{
	  dao.update("SitestatisticsMapper.updateSiteInventory", prSite);
  }
  
  /********批量操作**************/
  public void saveSiteList(List<PageData> pd) throws Exception{
	  dao.save("SitestatisticsMapper.saveSiteList",pd);
  }
  public void saveSiteItemList(List<PageData> pd) throws Exception{
	  dao.save("SitestatisticsMapper.saveSiteItemList",pd);
  }
  public void updateSiteInventoryList(List<PageData> pd) throws Exception{
	  dao.update("SitestatisticsMapper.updateSiteInventoryList",pd);
  }
  
  
}


