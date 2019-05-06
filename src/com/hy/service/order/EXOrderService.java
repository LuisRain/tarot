package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.EXOrder;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("eXOrderService")
public class EXOrderService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<PageData> listPdPagePurchaseOrder(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("EXOrderMapper.eXOrderlistPage", page);
  }
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("EXOrderMapper.save", pd);
  }
  
  public int saveList(List<EXOrder> listEXOrder)
    throws Exception
  {
    Object ob = this.dao.batchSave("EXOrderMapper.saveEXOrder", listEXOrder);
    return Integer.parseInt(ob.toString());
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("EXOrderMapper.delete", pd);
  }
  
  public int deleteList(List<EXOrder> listEXOrder)
    throws Exception
  {
    Object ob = this.dao.batchDelete("EXOrderMapper.deleteList", listEXOrder);
    return Integer.parseInt(ob.toString());
  }
  
  public void edit(PageData pd)
    throws Exception
  {
    this.dao.update("EXOrderMapper.edit", pd);
  }
  
  public void editState(List<PageData> pds)
    throws Exception
  {
    this.dao.batchUpdate("ExWarehouseOrderMapper.editState", pds);
  }
  
  public void editTotalCount(List<PageData> pds) throws Exception { this.dao.batchUpdate("ExWarehouseOrderMapper.editTotalCount", pds); }
  
  public EXOrder findById(PageData pd) throws Exception {
    EXOrder eXOrder = new EXOrder();
    return (EXOrder)this.dao.findForObject("EXOrderMapper.findByIdOrOrderNum", pd);
  }
  
  public void editWaveSotringNum(PageData pd) throws Exception { this.dao.findForObject("EXOrderMapper.editWaveSotringNum", pd); }
  
  public List<PageData> listEXOrderNew(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("EXOrderMapper.eXOrderNew", pd);
  }
  
  public void editIvtState(PageData pd) throws Exception { this.dao.update("EXOrderMapper.editIvtState", pd); }
  
  public void editIvtStateAndFinalAmount(PageData pd) throws Exception {
    this.dao.update("EXOrderMapper.editIvtStateAndFinalAmount", pd);
  }
  
  public void editPerCount(PageData pd) throws Exception { this.dao.update("EXOrderMapper.editPerCount", pd); }
  
  public void editTotalCount(PageData pd) throws Exception {
    this.dao.update("EXOrderMapper.editTotalCount", pd);
  }
  
  public void editPerCountAndTotalCount(PageData pd) throws Exception { this.dao.update("EXOrderMapper.editPerCountAndTotalCount", pd); }
  
  public void editFinalQuantity(PageData pd) throws Exception {
    this.dao.update("EXOrderMapper.editFinalQuantity", pd);
  }
  
  public List<PageData> findEXOrderByIds(String[] ArrayDATA_IDS)
    throws Exception
  {
    return (List)this.dao.findForList("EXOrderMapper.findEXOrderByIds", ArrayDATA_IDS);
  }
  
  public List<PageData> findEXOrderItemByIds(String[] ArrayDATA_IDS)
    throws Exception
  {
    return (List)this.dao.findForList("EXOrderMapper.findEXOrderItemByIds", ArrayDATA_IDS);
  }
  
  public List<PageData> findSplitTotalCountOfEXOrderItemByIds(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("EXOrderMapper.findSplitTotalCountOfEXOrderItemByIds", pd);
  }
  
  public List<PageData> findSplitTotalCountOfEXOrderItemByIdsBq(PageData pd) throws Exception { return (List)this.dao.findForList("EXOrderMapper.findSplitTotalCountOfEXOrderItemByIdsBq", pd); }
}


