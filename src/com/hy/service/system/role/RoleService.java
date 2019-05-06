package com.hy.service.system.role;

import com.hy.dao.DaoSupport;
import com.hy.entity.system.Role;
import com.hy.util.PageData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public List<Role> listAllERRoles()
    throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllERRoles", null);
  }
  
  public List<Role> listAllappERRoles()
    throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllappERRoles", null);
  }
  
  public List<Role> listAllRoles()
    throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllRoles", null);
  }
  
  public PageData findGLbyrid(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("RoleMapper.findGLbyrid", pd);
  }
  
  public PageData findYHbyrid(PageData pd) throws Exception
  {
    return (PageData)this.dao.findForObject("RoleMapper.findYHbyrid", pd);
  }
  
  public List<PageData> listAllUByRid(PageData pd) throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllUByRid", pd);
  }
  
  public List<PageData> listAllAppUByRid(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllAppUByRid", pd);
  }
  
  public List<Role> listAllRolesByPId(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllRolesByPId", pd);
  }
  
  public List<PageData> listAllkefu(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllkefu", pd);
  }
  
  public List<PageData> listAllGysQX(PageData pd) throws Exception
  {
    return (List)this.dao.findForList("RoleMapper.listAllGysQX", pd);
  }
  
  public void deleteKeFuById(String ROLE_ID) throws Exception
  {
    this.dao.delete("RoleMapper.deleteKeFuById", ROLE_ID);
  }
  
  public void deleteGById(String ROLE_ID) throws Exception
  {
    this.dao.delete("RoleMapper.deleteGById", ROLE_ID);
  }
  
  public void deleteRoleById(String ROLE_ID) throws Exception {
    this.dao.delete("RoleMapper.deleteRoleById", ROLE_ID);
  }
  
  public Role getRoleById(String roleId) throws Exception
  {
    return (Role)this.dao.findForObject("RoleMapper.getRoleById", roleId);
  }
  
  public void updateRoleRights(Role role) throws Exception
  {
    this.dao.update("RoleMapper.updateRoleRights", role);
  }
  
  public void updateQx(String msg, PageData pd)
    throws Exception
  {
    this.dao.update("RoleMapper." + msg, pd);
  }
  
  public void updateKFQx(String msg, PageData pd)
    throws Exception
  {
    this.dao.update("RoleMapper." + msg, pd);
  }
  
  public void gysqxc(String msg, PageData pd)
    throws Exception
  {
    this.dao.update("RoleMapper." + msg, pd);
  }
  
  public void setAllRights(PageData pd)
    throws Exception
  {
    this.dao.update("RoleMapper.setAllRights", pd);
  }
  
  public void add(PageData pd)
    throws Exception
  {
    this.dao.save("RoleMapper.insert", pd);
  }
  
  public void saveKeFu(PageData pd)
    throws Exception
  {
   this.dao.save("RoleMapper.saveKeFu", pd);
   
  }
  
  public void saveGYSQX(PageData pd)
    throws Exception
  {
    this.dao.save("RoleMapper.saveGYSQX", pd);
  }
  
  public PageData findObjectById(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("RoleMapper.findObjectById", pd);
  }
  
  public PageData edit(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("RoleMapper.edit", pd);
  }
}


