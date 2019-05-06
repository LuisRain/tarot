package com.hy.service.inventory;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("warehouseService")
public class WarehouseService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void save(PageData pd) throws Exception
  {
    this.dao.save("WarehouseMapper.save", pd);
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("WarehouseMapper.delete", pd);
  }
  
  public void edit(PageData pd)
    throws Exception
  {
    this.dao.update("WarehouseMapper.edit", pd);
  }
  
  public List<PageData> list(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("WarehouseMapper.datalistPage", page);
  }
  
  public List<PageData> listAll(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("WarehouseMapper.listAll", pd);
  }
  
  public PageData findById(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("WarehouseMapper.findById", pd);
  }
  
  public void deleteAll(String[] ArrayDATA_IDS)
    throws Exception
  {
    this.dao.delete("WarehouseMapper.deleteAll", ArrayDATA_IDS);
  }
  
  /*
	*仓库分类列表
	*/
	public List<PageData> warehouselist(Page page)throws Exception{
		return (List<PageData>)dao.findForList("WarehouseMapper.warehouselist", page);
	}
	/*
	* 修改
	*/
	public void updatewarehousetypelist(PageData pd)throws Exception{
		dao.update("WarehouseMapper.updatewarehousetypelist", pd);
	}
	
	public void typesave(PageData pd)throws Exception{
		dao.save("WarehouseMapper.typesave", pd);
	}
}


