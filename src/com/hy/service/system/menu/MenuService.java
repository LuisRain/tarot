package com.hy.service.system.menu;

import com.hy.dao.DaoSupport;
import com.hy.entity.system.Menu;
import com.hy.util.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("menuService")
public class MenuService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void deleteMenuById(String MENU_ID)
    throws Exception
  {
    this.dao.save("MenuMapper.deleteMenuById", MENU_ID);
  }
  
  public PageData getMenuById(PageData pd) throws Exception
  {
    return (PageData)this.dao.findForObject("MenuMapper.getMenuById", pd);
  }
  
  public PageData findMaxId(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("MenuMapper.findMaxId", pd);
  }
  
  public List<Menu> listAllParentMenu() throws Exception
  {
    return (List)this.dao.findForList("MenuMapper.listAllParentMenu", null);
  }
  
  public void saveMenu(Menu menu) throws Exception
  {
    this.dao.save("MenuMapper.insertMenu", menu);
  }
  
  public List<Menu> listSubMenuByParentId(long parentId) throws Exception {
    return (List)this.dao.findForList("MenuMapper.listSubMenuByParentId", Long.valueOf(parentId));
  }
  
  public List<Menu> listAllMenu() throws Exception
  {
    List<Menu> rl = listAllParentMenu();
    for (Menu menu : rl) {
      List<Menu> subList = listSubMenuByParentId(menu.getMENU_ID());
      menu.setSubMenu(subList);
    }
    return rl;
  }
  
  public List<Menu> listAllSubMenu() throws Exception {
    return (List)this.dao.findForList("MenuMapper.listAllSubMenu", null);
  }
  
  public PageData edit(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("MenuMapper.updateMenu", pd);
  }
  
  public PageData editicon(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("MenuMapper.editicon", pd);
  }
  
  public PageData editType(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("MenuMapper.editType", pd);
  }
}


