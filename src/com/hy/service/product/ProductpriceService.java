package com.hy.service.product;

import com.hy.dao.DaoSupport;
import com.hy.util.PageData;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("productpriceService")
public class ProductpriceService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("ProductpriceMapper.save", pd);
  }
  
  public void deleye(String productId)
    throws Exception
  {
    this.dao.delete("ProductpriceMapper.deleteproduct", productId);
  }
  
  public void edit(PageData pd) throws Exception { this.dao.update("ProductpriceMapper.edit", pd); }
  
  public PageData getSupplierPriceById(String id)
    throws Exception
  {
    return (PageData)this.dao.findForObject("ProductpriceMapper.getSupplierPriceById", id);
  }
  
  public PageData getSellingPriceById(String id)
    throws Exception
  {
    return (PageData)this.dao.findForObject("ProductpriceMapper.getSellingPriceById", id);
  }
}


