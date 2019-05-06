package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.OrderGroup;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("orderGroupService")
public class OrderGroupService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public int saveOrderGroup(OrderGroup og)
    throws Exception
  {
    Object obj = this.dao.save("OrderGroupMapper.insert", og);
    return Integer.parseInt(obj.toString());
  }
  
  public List<PageData> orderGrouplistPage(Page page)
    throws Exception
  {
    return (List)this.dao.findForList(
      "OrderGroupMapper.orderGrouplistPage", page);
  }
  
  public List<OrderGroup> orderGroup2listPage(PageData pd) throws Exception {
    return (List)this.dao.findForList(
      "OrderGroupMapper.orderGroupNew", pd);
  }
  
  public List<OrderGroup> orderGroupOfGift(PageData pd) throws Exception {
    return (List)this.dao.findForList(
      "OrderGroupMapper.orderGroupOfGift", pd);
  }
  
  public List<OrderGroup> inferiOrderGrouplistPage(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("OrderGroupMapper.inferiOrderGrouplistPage", page);
  }
  
  public void updateState(PageData pd)
    throws Exception
  {
    this.dao.update("OrderGroupMapper.updateState", pd);
  }
  
  public void updateStateOforderGroup(PageData pd)
    throws Exception
  {
    this.dao.update("OrderGroupMapper.updateStateOforderGroup", pd);
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("OrderGroupMapper.delete", pd);
  }
  
  public String getOrderGroupNum() throws Exception
  {
    Object o = new Object();
    o = this.dao.findForObject("OrderGroupMapper.getOrderGroupNum", o);
    if(o != null){
    	 return o.toString();
    }
    return "false";
  }
}


