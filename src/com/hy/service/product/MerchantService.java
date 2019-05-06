package com.hy.service.product;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("merchantService")
public class MerchantService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
 /* public PageData handheldMerchantCargo(PageData pd) throws Exception
		  {
		    return (PageData)this.dao.findForObject("MerchantMapper.handheldMerchantCargo", pd);
		  }
  */
  
  public List<PageData> listAll(Page page)
    throws Exception
  {
    return (List)this.dao.findForList(
      "MerchantMapper.merchantlistPage", page);
  }
  
  public List<PageData> areaList(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("MerchantMapper.areaList", pd);
  }
  
  public void deleteMerchant(PageData pd)
    throws Exception
  {
    this.dao.delete("MerchantMapper.deleteMerchant", pd);
  }
  
  public void deleteMerchantByOldId(String oldId) throws Exception { this.dao.delete("MerchantMapper.deleteMerchant", oldId); }
  
  public void deleteAllMerchant(String[] SUPPLE_IDS)
    throws Exception
  {
    this.dao.delete("MerchantMapper.deleteAllMerchant", SUPPLE_IDS);
  }
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("MerchantMapper.save", pd);
  }
  
  public void edit(PageData pd)
    throws Exception
  {
    this.dao.update("MerchantMapper.edit", pd);
  }
  
  public void editmerchant(PageData pd)
		    throws Exception
		  {
		    this.dao.update("MerchantMapper.editmerchant", pd);
		  }
  
  public PageData findById(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("MerchantMapper.findById", pd);
  }
  
  public List<PageData> findMerchantById(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("MerchantMapper.findMerchantById", pd);
  }
  
  public List<PageData> findByName(PageData pd)
    throws Exception
  {
    return (List)this.dao
      .findForList("MerchantMapper.findByName", pd);
  }
  
  public PageData getMerchantById(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("MerchantMapper.getMerchantById", pd);
  }
  
  public List<PageData> excel(Page page)
		    throws Exception
		  {
		    return (List)this.dao.findForList("MerchantMapper.excel", page);
		  }
  
  public static void main(String[] args) {}
  
  	public PageData findareaname(PageData pd) throws Exception{
  		return (PageData) dao.findForObject("MerchantMapper.findareaname", pd);
  	}
}


