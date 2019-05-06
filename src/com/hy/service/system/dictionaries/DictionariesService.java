package com.hy.service.system.dictionaries;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("dictionariesService")
public class DictionariesService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("DictionariesMapper.save", pd);
  }
  
  public void edit(PageData pd) throws Exception
  {
    this.dao.update("DictionariesMapper.edit", pd);
  }
  
  public PageData findById(PageData pd) throws Exception
  {
    return (PageData)this.dao.findForObject("DictionariesMapper.findById", pd);
  }
  
  public PageData findCount(PageData pd) throws Exception
  {
    return (PageData)this.dao.findForObject("DictionariesMapper.findCount", pd);
  }
  
  public PageData findBmCount(PageData pd) throws Exception
  {
    return (PageData)this.dao.findForObject("DictionariesMapper.findBmCount", pd);
  }
  
  public List<PageData> dictlistPage(Page page) throws Exception
  {
    return (List)this.dao.findForList("DictionariesMapper.dictlistPage", page);
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("DictionariesMapper.delete", pd);
  }
}


