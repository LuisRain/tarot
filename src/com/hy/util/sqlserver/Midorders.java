package com.hy.util.sqlserver;

import java.io.Serializable;

public class Midorders implements Serializable {
  private static final long serialVersionUID = 1L;
  private String wmsorderId;
  private String barcode;
  private String sku;
  private String goodsname;
  private String goodssize;
  private int quantity;
  private String shopid;
  private String shopname;
  private String orderId;
  private String oprationtime;
  
  public String getWmsorderId() {
    return this.wmsorderId;
  }
  
  public void setWmsorderId(String wmsorderId) { this.wmsorderId = wmsorderId; }
  
  public String getBarcode() {
    return this.barcode;
  }
  
  public void setBarcode(String barcode) { this.barcode = barcode; }
  
  public String getSku() {
    return this.sku;
  }
  
  public void setSku(String sku) { this.sku = sku; }
  
  public String getGoodsname() {
    return this.goodsname;
  }
  
  public void setGoodsname(String goodsname) { this.goodsname = goodsname; }
  
  public String getGoodssize() {
    return this.goodssize;
  }
  
  public void setGoodssize(String goodssize) { this.goodssize = goodssize; }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(int quantity) { this.quantity = quantity; }
  
  public String getShopid() {
    return this.shopid;
  }
  
  public void setShopid(String shopid) { this.shopid = shopid; }
  
  public String getShopname() {
    return this.shopname;
  }
  
  public void setShopname(String shopname) { this.shopname = shopname; }
  
  public String getOrderId() {
    return this.orderId;
  }
  
  public void setOrderId(String orderId) { this.orderId = orderId; }
  
  public String getoprationtime() {
    return this.oprationtime;
  }
  
  public void setoprationtime(String oprationtime) {
    this.oprationtime = oprationtime;
  }
}


