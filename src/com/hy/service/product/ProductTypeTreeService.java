package com.hy.service.product;

import com.hy.dao.DaoSupport;
import com.hy.entity.product.ProductTypeTree;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("productTypeTreeService")
public class ProductTypeTreeService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public int addProductTypeTree(ProductTypeTree productTypeTree)
    throws Exception
  {
    Object ptt = this.dao.save("ProductTypeTreeMapper.addProductTypeTree", productTypeTree);
    if ((ptt != null) && (ptt != "")) {
      return Integer.parseInt(ptt.toString());
    }
    return 0;
  }
  
  public List<ProductTypeTree> getProductTypeTree(PageData pd) throws Exception {
    return (List)this.dao.findForList("ProductTypeTreeMapper.queryProductTypeTreeByState", pd);
  }
  
  public ProductTypeTree getProductTypeTreeForObject(PageData pd) throws Exception { return (ProductTypeTree)this.dao.findForObject("ProductTypeTreeMapper.queryProductTypeTreeByState", pd); }
  
  public int updateProductType(ProductTypeTree productTypeTree) throws Exception {
    int flag = 0;
    if (this.dao.findForList("ProductTypeTreeMapper.updateProductType", productTypeTree) != null) {
      flag = 1;
    }
    return flag;
  }
  
  public List<PageData> getProudctTypeList() throws Exception { PageData pd = new PageData();
    pd.put("state", "2");
    return (List)this.dao.findForList("ProductTypeTreeMapper.getProudctTypeList", pd);
  }
}


