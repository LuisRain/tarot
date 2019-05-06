package com.hy.service.product;

import com.hy.dao.DaoSupport;
import com.hy.entity.product.ProductType;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("productTypeService")
public class ProductTypeService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<PageData> findByParentId(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("ProductTypeMapper.findByParentId", pd);
  }
  
  
  
  public List<ProductType> findProductTypeByParentId(PageData pd) throws Exception {
    return (List)this.dao.findForList("ProductTypeMapper.findByParentId", pd);
  }
  
  public String[] findByProudctTypeId(long pd)
    throws Exception
  {
    String[] id = new String[3];
    PageData parameter = new PageData();
    PageData pdd = new PageData();
    pdd.put("id", Long.valueOf(pd));
    
    id[0] = String.valueOf(pd);
    
    PageData product = (PageData)this.dao.findForObject("ProductTypeMapper.findByid", pdd);
    parameter.put("id", product.get("parent_id"));
    id[1] = ((String)product.get("id"));
    
    product = (PageData)this.dao.findForObject("ProductTypeMapper.findByid", parameter);
    id[2] = ((String)product.get("id"));
    
    return id;
  }
  
  public Long findNameById(String name)
    throws Exception
  {
    ProductType productType = (ProductType)this.dao.findForObject("ProductTypeMapper.findNameById", name);
    if (productType != null) {
      return Long.valueOf(productType.getId());
    }
    return null;
  }
}


