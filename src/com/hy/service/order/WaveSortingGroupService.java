package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.WaveSortingGroup;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("waveSortingGroupService")
public class WaveSortingGroupService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public int saveOrderGroup(WaveSortingGroup wsg)
    throws Exception
  {
    Object obj = this.dao.save("WaveSortingGroupMapper.insert", wsg);
    return Integer.parseInt(obj.toString());
  }
  
  public List<WaveSortingGroup> waveSortingGroupNew(PageData pd) throws Exception { return (List)this.dao.findForList("WaveSortingGroupMapper.waveSortingGroupNew", pd); }
  
  public List<WaveSortingGroup> waveSortingGroupNew(Page page) throws Exception {
    return (List)this.dao.findForList("WaveSortingGroupMapper.waveSortingGroupNewlistPage", page);
  }
  
  public void updateState(PageData pd)
    throws Exception
  {
    this.dao.update("WaveSortingGroupMapper.updateWaveSortingState", pd);
  }
  
  public void updateStateOforderGroup(PageData pd)
    throws Exception
  {
    this.dao.update("WaveSortingGroupMapper.updateWaveSortingStateOfwaveSortingGroup", pd);
  }
  
  public void updateWaveSortingOrder(PageData pd)
    throws Exception
  {
    this.dao.update("WaveSortingGroupMapper.updateWaveSortingOrder", pd);
  }
}


