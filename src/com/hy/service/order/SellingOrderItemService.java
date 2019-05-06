package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.SellingOrderItem;
import com.hy.util.PageData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("sellingOrderItemService")
public class SellingOrderItemService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<PageData> listPdPagePurchaseOrder(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("SellingOrderItemMapper.sellingOrderItemlistPage", page);
  }
  
  public int saveList(List<SellingOrderItem> listSellingOrderItem)
    throws Exception
  {
    Object ob = this.dao.batchSave("SellingOrderItemMapper.saveSellingOrderItem", listSellingOrderItem);
    return Integer.parseInt(ob.toString());
  }
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("SellingOrderItemMapper.save", pd);
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("SellingOrderItemMapper.delete", pd);
  }
  
  public int deleteList(List<SellingOrderItem> listSellingOrderItem)
    throws Exception
  {
    Object ob = this.dao.batchDelete("SellingOrderItemMapper.deleteList", listSellingOrderItem);
    return Integer.parseInt(ob.toString());
  }
  
  public void edit(PageData pd)
    throws Exception { this.dao.update("SellingOrderItemMapper.edit", pd); }
  
  public ENOrder findById(PageData pd) throws Exception {
    ENOrder eNOrder = new ENOrder();
    return (ENOrder)this.dao.findForObject("SellingOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
  }
  
  public PageData updatesellitemproduct(PageData pd) throws Exception {
	  return (PageData) dao.findForObject("SellingOrderItemMapper.updatesellitemproduct",pd);
	
  }
  public void editorderitem(PageData pd) throws Exception{
	  dao.update("SellingOrderMapper.editorderitem", pd);
  }
  
  public void updateitemproduct(PageData pd) throws Exception {
	  dao.update("SellingOrderItemMapper.updateitemproduct",pd);
  }

  public void saveOrderItem(PageData pd) throws Exception {
    dao.save("SellingOrderItemMapper.saveOrderItem", pd);
  }

    public PageData checkProductById(PageData pd) throws Exception {
      return (PageData)dao.findForObject("SellingOrderItemMapper.checkProductById",pd);
    }
}


