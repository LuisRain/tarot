package com.hy.service.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hy.util.PageData;

@Service("cargoSpaceService")
public class CargoSpaceService
{
  @Resource(name="daoSupport")
  private com.hy.dao.DaoSupport dao;
  
  public int save(com.hy.util.PageData pd) throws Exception
  {
    return ((Integer)this.dao.save("CargoSpace.save", pd)).intValue();
  }
  
  public void saveHandheld(com.hy.util.PageData pd) throws Exception
  {
     dao.save2("CargoSpace.saveHandheld", pd);
  }
  
  public void edit(com.hy.util.PageData pd)
    throws Exception
  {
    this.dao.update("CargoSpace.edit", pd);
  }
  public PageData findCargo(PageData pd) throws Exception{
	  return (PageData) dao.findForObject("CargoSpace.findCargo",pd);
	  
  }
  public void deleteCargoSpace(com.hy.util.PageData pd)
    throws Exception
  {
    this.dao.delete("CargoSpace.deleteCargoSpace", pd);
  }
  
  public void deleteAllCargoSpace(String[] cs_ids)
    throws Exception
  {
    this.dao.delete("CargoSpace.deleteAllCargoSpace", cs_ids);
  }
}


