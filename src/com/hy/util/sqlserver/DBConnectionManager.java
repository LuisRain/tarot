package com.hy.util.sqlserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jdk.internal.org.objectweb.asm.commons.Remapper;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.hy.entity.order.EXOrderItem;
import com.hy.entity.product.Merchant;
import com.hy.util.PageData;
import com.hy.util.StringUtil;

public class DBConnectionManager
{
/* private String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private String url = "jdbc:sqlserver://192.168.0.87:1433;DatabaseName=YouTaiDATA";
  private String user = "sa";
  private String password = "tiger";*/
	
	
  private String driverName = "com.mysql.jdbc.Driver";
 private String url = "jdbc:mysql://118.190.153.131/sxtarot";
  private String user = "root";
  private String password = "admin123";
  
  private static String message = "恭喜，数据库连接正常！";
  
  public void setDriverName(String newDriverName) { this.driverName = newDriverName; }
  
  public String getDriverName() {
    return this.driverName;
  }
  
  public void setUrl(String newUrl) {
    this.url = newUrl;
  }
  
  public String getUrl() { return this.url; }
  
  public void setUser(String newUser) {
    this.user = newUser;
  }
  
  public String getUser() { return this.user; }
  
  public void setPassword(String newPassword) {
    this.password = newPassword;
  }
  
  public String getPassword() { return this.password; }
  
  public Connection getConnection() {
    try {
      Class.forName(this.driverName);
      return DriverManager.getConnection(this.url, this.user, this.password);
    } catch (Exception e) {
      e.printStackTrace();
      message = "数据库连接失败！"; }
    return null;
  }
  
  
  /**
   * 根据货位信息查询站点信息
   * @param merchants
   */
  public PageData findHandheldStore(PageData pd) throws SQLException{
	DBConnectionManager dcm = new DBConnectionManager();
    Connection conn = null;
    Statement stmt = null;
    String sql = "";
    Merchant merchant = null;
    PageData pdd=new PageData();
    try
    {
      conn = dcm.getConnection();
      stmt = conn.createStatement();
      sql = "select id,name,location from store where location='"+pd.getString("location")+"'";
      ResultSet rs = stmt.executeQuery(sql);
      
      while (rs.next()) {
    	  pdd.put("name",rs.getString("name"));
    	  pdd.put("id",rs.getString("id"));
    	  pdd.put("location",rs.getString("location"));
      }
    } catch (SQLException e) {
      //System.out.println(e.getMessage());
    } finally {
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    return pdd;
  }
 
  
  
  public void insertMerchantListByBatchPrepareStat(java.util.List<Merchant> merchants)
  {
    try
    {
      DBConnectionManager dcm = new DBConnectionManager();
      Connection conn = dcm.getConnection();
      conn.setAutoCommit(false);
      String sql = "INSERT INTO Store(id,name,location,sorgname,address,linktel,linkman) VALUES(?,?,?,0,0,0,0)";
      PreparedStatement prest = conn.prepareStatement(sql, 1005, 1007);
      int location = 1001;
      merchants = sortList(merchants);
      for (Merchant merchant : merchants) {
        if ("1".equals(merchant.getImageUrl()))
        {
          prest.setString(1, merchant.getMerchantNum());
          prest.setString(2, merchant.getMerchantName());
          prest.setString(3, location+"");
          prest.addBatch();
          
          location++;
        }
      }
      //System.out.println("门店信息插入完成.");
      prest.executeBatch();
      conn.commit();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      //System.out.println("插入sql server数据库遇到错误:" + ex);
    }
  }
  
  public void insertMidordersListByBatchPrepareStat(java.util.List<Midorders> midordersList)
  {
    try
    {
      DBConnectionManager dcm = new DBConnectionManager();
      Connection conn = dcm.getConnection();
      conn.setAutoCommit(false);
      String sql = "INSERT INTO Midorders(oprationtime,wmsorderid,barcode,sku,goodsname,goodssize,quantity,shopid,shopname,orderid,state,truequantity,oprationstate,tasktype,block,checkquantity) VALUES(?,?,?,?,?,?,?,?,?,?,0,0,0,'Y',1,0)";
      
      PreparedStatement prest = conn.prepareStatement(sql, 1005, 1007);
      for (Midorders midorders : midordersList)
      {
        if (midorders.getQuantity() != 0) {
          prest.setString(1, midorders.getoprationtime());
          prest.setString(2, midorders.getWmsorderId());
          prest.setString(3, midorders.getBarcode());
          prest.setString(4, midorders.getSku());
          prest.setString(5, midorders.getGoodsname());
          prest.setString(6, midorders.getGoodssize());
          prest.setInt(7, midorders.getQuantity());
          prest.setString(8, midorders.getShopid());
          prest.setString(9, midorders.getShopname());
          prest.setString(10, StringUtil.getStringOfMillisecond(""));
          
          prest.addBatch();
        }
      }
      //System.out.println("订单条目插入散货分拣系统完成。");
      prest.executeBatch();
      conn.commit();
      conn.close();
    } catch (SQLException ex) { ex.printStackTrace();
      //System.out.println("插入散货分拣系统遇到错误:" + ex);
    }
  }
  
  public java.util.List<EXOrderItem> selectRecords(java.util.List<EXOrderItem> exoiList) throws SQLException {
    DBConnectionManager dcm = new DBConnectionManager();
    Connection conn = null;
    Statement stmt = null;
    String sql = "";
    try
    {
      conn = dcm.getConnection();
      stmt = conn.createStatement();
      
      if ((exoiList != null) && (exoiList.size() > 0)) {
        int j = 0;
        for (int i = 0; i < exoiList.size(); i++) {
          ((EXOrderItem)exoiList.get(0)).setComment("");
          sql = "SELECT wmsorderid,truequantity,barcode,sku,goodsname,goodssize,quantity,shopid,shopname,orderid,state FROM orders WHERE state=3 and quantity>0 and wmsorderid ='" + 
            ((EXOrderItem)exoiList.get(i)).getId() + "'";
          //System.out.println(sql);
          ResultSet rs = stmt.executeQuery(sql);
          if ("0".equals(((EXOrderItem)exoiList.get(i)).getPerCount())) {
            if (i == j) {
              ((EXOrderItem)exoiList.get(0)).setComment("ok");
            }
            j++;
          }
          while ((!"0".equals(((EXOrderItem)exoiList.get(i)).getPerCount())) && (rs.next())) {
            if (!StringUtil.isEmpty(rs.getString("truequantity"))) {
              ((EXOrderItem)exoiList.get(i)).setQuantity(Double.parseDouble(rs.getString("truequantity")));
            }
            if (i == j) {
              ((EXOrderItem)exoiList.get(0)).setComment("ok");
            }
            j++;
          }
        }
      }
    } catch (SQLException e) {
      //System.out.println(e.getMessage());
    } finally {
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    return exoiList;
  }
  
  public java.util.List<Merchant> selectStoresInfo() throws SQLException {
	  DBConnectionManager dcm = new DBConnectionManager();
    Connection conn = null;
    Statement stmt = null;
    String sql = "";
    java.util.List<Merchant> merchantList = new ArrayList();
    Merchant merchant = null;
    try
    {
      conn = dcm.getConnection();
      stmt = conn.createStatement();
      sql = "SELECT id,name,location FROM store ORDER BY LOCATION ASC";
      //System.out.println(sql);
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        merchant = new Merchant();
        merchant.setMerchantNum(rs.getString("id"));
        merchant.setMerchantName(rs.getString("name"));
        merchant.setImageUrl(rs.getString("location"));
        merchantList.add(merchant);
      }
    } catch (SQLException e) {
      //System.out.println(e.getMessage());
    } finally {
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    return merchantList;
  }
  
  public static java.util.List<Merchant> sortList(java.util.List<Merchant> merchantList) { 
				Collections.sort(merchantList, new Comparator<Merchant>() {
      public int compare(Merchant arg0, Merchant arg1) {
        return arg0.getId() > arg1.getId() ? 1 : -1;
      }
      
    });
    return merchantList;
  }
  
  public java.util.List<PageData> selectProductOrMerchantInfos(String searchcriteria, String laneway, String keyword) throws SQLException { DBConnectionManager dcm = new DBConnectionManager();
    Connection conn = null;
    Statement stmt = null;
    StringBuffer sb = null;
    java.util.List<PageData> pds = null;
    try {
      conn = dcm.getConnection();
      stmt = conn.createStatement();
      
      //System.out.println("searchcriteria=" + searchcriteria);
      if (!StringUtil.isEmpty(searchcriteria)) {
        sb = new StringBuffer();
        if ((Integer.parseInt(searchcriteria) < 5) && (Integer.parseInt(searchcriteria) > 0)) {
          sb.append("SELECT o.oprationtime oprationtime,o.barcode barcode,o.goodsname goodsname,o.goodssize goodssize,o.quantity quantity,o.shopid shopid,s.name shopname,o.oprationstate state,s.location  location FROM orders o,Store s WHERE o.tolocation=s.location ");
          
          if (Integer.parseInt(searchcriteria) == 1) {
            sb.append(" AND o.oprationstate = 0   ");
          }
          if (Integer.parseInt(searchcriteria) == 2) {
            sb.append(" AND o.oprationstate = '3' AND o.quantity <> 0 ");
          }
          if ((Integer.parseInt(searchcriteria) == 3) && (!StringUtil.isEmpty(laneway)) && (Integer.parseInt(laneway) > 0)) {
            sb.append(" AND o.block = '" + Integer.parseInt(laneway) + "' ");
          }
          if ((Integer.parseInt(searchcriteria) == 1) && (!StringUtil.isEmpty(keyword))) {
            sb.append(" AND o.barcode like '%" + keyword + "%' ");
          }
          if ((Integer.parseInt(searchcriteria) == 2) && (!StringUtil.isEmpty(keyword))) {
            sb.append(" AND o.barcode like '%" + keyword + "%' ");
          }
          if ((Integer.parseInt(searchcriteria) == 3) && (!StringUtil.isEmpty(keyword))) {
            sb.append(" AND o.barcode like '%" + keyword + "%' ");
          }
          if ((Integer.parseInt(searchcriteria) == 4) && (!StringUtil.isEmpty(keyword))) {
            sb.append(" AND s.name like '%" + keyword + "%' ");
          }
          
          sb.append(" ORDER BY s.location,o.barcode ASC   ");
        }
        if ((Integer.parseInt(searchcriteria) == 5) || (Integer.parseInt(searchcriteria) == 6)) {
          sb.append("SELECT s.name shopname,s.location location FROM Store s   WHERE 1=1 ");
          
          if (Integer.parseInt(searchcriteria) == 5) {
            sb.append(" AND s.name like '%" + keyword + "%' ");
          }
          if (Integer.parseInt(searchcriteria) == 6) {
            sb.append(" AND s.location like '%" + keyword + "%' ");
          }
          sb.append(" ORDER BY s.location  ASC   ");
        }
      }
      //System.out.println("sb.toString()=" + sb.toString());
      if (sb != null) {
        ResultSet rs = stmt.executeQuery(sb.toString());
        pds = new ArrayList();
        PageData pdR = null;
        while (rs.next()) {
          pdR = new PageData();
          if ((Integer.parseInt(searchcriteria) < 5) && (Integer.parseInt(searchcriteria) > 0)) {
            pdR.put("oprationtime", rs.getString("oprationtime"));
            pdR.put("barcode", rs.getString("barcode"));
            pdR.put("goodsname", rs.getString("goodsname"));
            pdR.put("goodssize", rs.getString("goodssize"));
            pdR.put("quantity", rs.getString("quantity"));
            pdR.put("shopid", rs.getString("shopid"));
            pdR.put("shopname", rs.getString("shopname"));
            pdR.put("state", rs.getString("state"));
            pdR.put("location", rs.getString("location"));
          } else {
            pdR.put("shopname", rs.getString("shopname"));
            pdR.put("location", rs.getString("location"));
          }
          pds.add(pdR);
        }
      }
    } catch (SQLException e) {
      //System.out.println(e.getMessage());
    } finally {
      if (stmt != null) {
        stmt.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
    return pds;
  }
  
  public static void main(String[] args) {
	  DBConnectionManager dcm = new DBConnectionManager();
	    Connection conn = null;
	    Statement stmt = null;
	    Merchant merchant = null;
	    PageData pdd=new PageData();
    try { 
    	//供应商  24  门店26
    	 conn = dcm.getConnection();
         stmt = conn.createStatement();
         String usql="select * from t_merchant";
         ResultSet rs = stmt.executeQuery(usql);
         while (rs.next()) {
        	 //System.out.println("1"+rs.getString("merchant_num"));
        	 if(null!=rs.getString("merchant_num")&&!rs.getString("merchant_num").equals("")&&!rs.getString("merchant_num").equals("null")){
        		 String sql="insert into sys_user (USERNAME,name,PASSWORD,ROLE_ID,ck_id,STATUS) values('"+rs.getString("merchant_num")+"','"+rs.getString("merchant_name")
        				 +"','"+new SimpleHash("SHA-1", rs.getString("merchant_num"), "123456").toString()+"','"+26+"',1,0)";
        		 Connection insconn = dcm.getConnection();
        		 Statement st=insconn.createStatement();
        		 int count = st.executeUpdate(sql);
        		 st.close();
        		 insconn.close();
        	 }
         }
         conn.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}


