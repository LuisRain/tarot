package com.hy.service.product;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("supplierService")
public class SupplierService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<PageData> listAll(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("SupplierMapper.supplierlistPage", page);
  }
  
  public void deleteSupplier(PageData pd)
    throws Exception
  {
    this.dao.delete("SupplierMapper.deleteSupplier", pd);
  }
  
  public void deleteAllSupller(String[] SUPPLE_IDS)
    throws Exception
  {
    this.dao.delete("SupplierMapper.deleteAllSupller", SUPPLE_IDS);
  }
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("SupplierMapper.save", pd);
  }
  
  public void edit(PageData pd)
    throws Exception
  {
    this.dao.update("SupplierMapper.edit", pd);
  }
  
  public void editM(PageData pd)
    throws Exception
  {
    this.dao.update("SupplierMapper.editM", pd);
  }
  
  public PageData findById(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("SupplierMapper.findById", pd);
  }
  
  public PageData findMById(String id)
    throws Exception
  {
    return (PageData)this.dao.findForObject("SupplierMapper.findMById", id);
  }
  
  public PageData findByName(String name)
    throws Exception
  {
    return (PageData)this.dao.findForObject("SupplierMapper.findByName", name);
  }
  
  public List<PageData> findSupsById(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("SupplierMapper.findSupsById", pd);
  }
  	/**
  	 * 根据供应商登录查询
  	 * @param pd
  	 * @return
  	 * @throws Exception 
  	 */
  	public PageData findByGetId(PageData pd) throws Exception{
  		return (PageData) dao.findForObject("SupplierMapper.findByGetId", pd);
  	}
  	
  	/**
  	 * 查询供应商
  	 * @param pd
  	 * @return
  	 * @throws Exception
  	 */
  	public List<PageData> findSupplier(PageData pd) throws Exception{
  		return (List<PageData>) dao.findForList("SupplierMapper.findSuppById", pd);
  	}
  	
  	public PageData findarchives(PageData pd) throws Exception{
  		return (PageData) dao.findForObject("SupplierMapper.findarchives", pd);
  	}
  	
  	public List<PageData> findbrand(PageData pd) throws Exception{
  		return (List<PageData>) dao.findForList("SupplierMapper.findbrand", pd);
  	}
  	
  	public List<PageData> findcontract(PageData pd) throws Exception{
  		return (List<PageData>) dao.findForList("SupplierMapper.findcontract", pd);
  	}
  	
  	public void savecontract(PageData pd) throws Exception{
  		dao.save("SupplierMapper.savecontract",pd);
  	}
  	
  	public void savebrand(PageData pd) throws Exception{
  		dao.save("SupplierMapper.savebrand",pd);
  	}
	public void savearchives(PageData pd) throws Exception{
  		dao.save("SupplierMapper.savearchives",pd);
  	}
	public void updatearchives(PageData pd) throws Exception{
  		dao.save("SupplierMapper.updatearchives",pd);
  	}
  	public void updatecontract(PageData pd) throws Exception{
  		dao.save("SupplierMapper.updatecontract",pd);
  	}
  	
  	public PageData findbyNum(String pd) throws Exception{
  		return (PageData) dao.findForObject("SupplierMapper.findByGetNum", pd);
  	}

	public List<PageData> excel(Page page) throws Exception {
		return (List) this.dao.findForList("SupplierMapper.excel", page);
	}
}


