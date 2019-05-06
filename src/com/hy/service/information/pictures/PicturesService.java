package com.hy.service.information.pictures;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import com.hy.util.sqlserver.DBConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("picturesService")
public class PicturesService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("PicturesMapper.save", pd);
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("PicturesMapper.delete", pd);
  }
  
  public void edit(PageData pd)
    throws Exception
  {
    this.dao.update("PicturesMapper.edit", pd);
  }
  
  public List<PageData> list(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("PicturesMapper.datalistPage", page);
  }
  
  public List<PageData> listAll(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("PicturesMapper.listAll", pd);
  }
  
  public PageData findById(PageData pd)
    throws Exception
  {
    return (PageData)this.dao.findForObject("PicturesMapper.findById", pd);
  }
  
  public void deleteAll(String[] ArrayDATA_IDS)
    throws Exception
  {
    this.dao.delete("PicturesMapper.deleteAll", ArrayDATA_IDS);
  }
  
  public List<PageData> getAllById(String[] ArrayDATA_IDS)
    throws Exception
  {
    return (List)this.dao.findForList("PicturesMapper.getAllById", ArrayDATA_IDS);
  }
  
  public void delTp(PageData pd)
    throws Exception
  {
    this.dao.update("PicturesMapper.delTp", pd);
  }
  
  public void insertorder(List<PageData> pd) throws Exception{
	  dao.save("PicturesMapper.insertorder",pd);
  }
  public void insertstore(List<PageData> pd) throws Exception{
	  dao.save("PicturesMapper.insertstore",pd);
  }
  /**
   * 分拣商品信息查询
   * @param searchcriteria
   * @param laneway
   * @param keyword
   * @return
   * @throws SQLException
   */
  public java.util.List<PageData> findorderstore(Page pd) throws SQLException {
	
	  StringBuffer sb = null;
	  java.util.List<PageData> pds = null;
	  try {
		pds=(List<PageData>) dao.findForList("PicturesMapper.findorderstorelistPage", pd);
		  
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	  
	  return pds;
	}
  
  /**
   * 导出分拣商品信息查询
   * @param searchcriteria
   * @param laneway
   * @param keyword
   * @return
   * @throws SQLException
   */
  public java.util.List<PageData> excelOfMerchant(PageData pd) throws SQLException {
	
	  StringBuffer sb = null;
	  java.util.List<PageData> pds = null;
	  try {
		pds=(List<PageData>) dao.findForList("PicturesMapper.excelOfMerchant", pd);
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	  
	  return pds;
	}
  
}


