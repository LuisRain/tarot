package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.PurchaseOrderItem;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("purchaseOrderItemService")
public class PurchaseOrderItemService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<PageData> listPdPagePurchaseOrder(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("PurchaseOrderItemMapper.purchaseOrderItemlistPage", page);
  }
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("PurchaseOrderItemMapper.save", pd);
  }
  
  public int saveList(List<PurchaseOrderItem> listPurchaseOrderItem)
    throws Exception
  {
    Object ob = this.dao.batchSave("PurchaseOrderItemMapper.savePurchaseOrderItem", listPurchaseOrderItem);
    return Integer.parseInt(ob.toString());
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("PurchaseOrderItemMapper.delete", pd);
  }
  
  public int deleteList(List<PurchaseOrderItem> listPurchaseOrderItem)
    throws Exception
  {
    Object ob = this.dao.batchDelete("PurchaseOrderItemMapper.deleteList", listPurchaseOrderItem);
    return Integer.parseInt(ob.toString());
  }
  
  public void edit(PageData pd)
    throws Exception { this.dao.update("PurchaseOrderItemMapper.edit", pd); }
  
  public ENOrder findById(PageData pd) throws Exception {
    ENOrder eNOrder = new ENOrder();
    return (ENOrder)this.dao.findForObject("PurchaseOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
  }
}


