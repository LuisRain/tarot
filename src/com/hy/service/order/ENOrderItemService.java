package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.ENOrderItem;
import com.hy.entity.system.User;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("eNOrderItemService")
public class ENOrderItemService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<PageData> listPdPagePurchaseOrder(Page page)
    throws Exception
  {
    return (List)this.dao.findForList(
      "ENOrderItemMapper.eNOrderItemlistPage", page);
  }
  
  public boolean saveEnorderItem(PageData pd)
    throws Exception
  {
    return false;
  }
  
  public List<PageData> enDataCenterlistPage(Page pg)
    throws Exception
  {
    return (List)this.dao.findForList(
    
      "ENOrderItemMapper.enDataCenterlistPage", pg);
  }
  	//导出到excel
  	public List<PageData> excel(PageData pd) throws Exception{
  		return (List)this.dao.findForList("ENOrderItemMapper.enDataCenterexcel", pd);
  		}
  
  public int save(PageData pd)
    throws Exception
  {
    pd.put("creator", LoginUtil.getLoginUser().getNAME());
    Object ob = this.dao.save("ENOrderItemMapper.save", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public int saveList(List<ENOrderItem> listENOrderItem)
    throws Exception
  {
    Object ob = this.dao.batchSave("ENOrderItemMapper.saveENOrderItem", 
      listENOrderItem);
    return Integer.parseInt(ob.toString());
  }
  
  public int delete(PageData pd)
    throws Exception
  {
    Object ob = this.dao.delete("ENOrderItemMapper.delete", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public int deleteList(List<ENOrderItem> listENOrderItem)
    throws Exception
  {
    Object ob = this.dao.batchDelete("ENOrderItemMapper.deleteList", 
      listENOrderItem);
    return Integer.parseInt(ob.toString());
  }
  
  public int edit(PageData pd)
    throws Exception
  {
    Object ob = this.dao.update("ENOrderItemMapper.edit", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public int editState(PageData pd)
    throws Exception
  {
    Object o = this.dao.update("ENOrderItemMapper.editState", pd);
    return Integer.parseInt(o.toString());
  }
  
  public int editQuantity(PageData pd) throws Exception
  {
    Object ob = this.dao.update("ENOrderItemMapper.editQuantity", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public ENOrder findById(PageData pd) throws Exception {
    ENOrder eNOrder = new ENOrder();
    return (ENOrder)this.dao.findForObject(
      "ENOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
  }
  
  public PageData findItemById(PageData pd) throws Exception
  {
    return (PageData)this.dao.findForObject(
      "ENOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
  }
  
  public List<PageData> getOrderItemlistPageProductById(Page page)
    throws Exception
  {
    return (List)this.dao.findForList(
    
      "ENOrderItemMapper.getOrderItemlistProductById", page);
  }
  
  public List<PageData> dataCenterSupplierlistPage(Page page)
    throws Exception
  {
    return (List)this.dao.findForList(
    
      "ENOrderItemMapper.dataCenterSupplierlistPage", page);
  }
}


