package com.hy.entity.order;

import java.io.Serializable;

public class ENOrderItem implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private double purchasePrice;
  private double quantity;
  private double finalQuantity;
  private String svolume;
  private String weight;
  private int isSplitIvt;
  private int isIvtBK;
  private String eNTime;
  private String creator;
  private String comment;
  private String createTime;
  private com.hy.entity.product.Product product;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getGroupNum() {
    return this.groupNum;
  }
  
  public void setGroupNum(String groupNum) { this.groupNum = groupNum; }
  
  public String getOrderNum() {
    return this.orderNum;
  }
  
  public void setOrderNum(String orderNum) { this.orderNum = orderNum; }
  
  public double getPurchasePrice() {
    return this.purchasePrice;
  }
  
  public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
  
  public String getSvolume() {
    return this.svolume;
  }
  
  public void setSvolume(String svolume) { this.svolume = svolume; }
  
  public String getWeight() {
    return this.weight;
  }
  
  public void setWeight(String weight) { this.weight = weight; }
  
  public int getIsSplitIvt() {
    return this.isSplitIvt;
  }
  
  public void setIsSplitIvt(int isSplitIvt) { this.isSplitIvt = isSplitIvt; }
  
  public int getIsIvtBK() {
    return this.isIvtBK;
  }
  
  public void setIsIvtBK(int isIvtBK) { this.isIvtBK = isIvtBK; }
  
  public String geteNTime() {
    return this.eNTime;
  }
  
  public void seteNTime(String eNTime) { this.eNTime = eNTime; }
  
  public String getCreator() {
    return this.creator;
  }
  
  public void setCreator(String creator) { this.creator = creator; }
  
  public String getComment() {
    return this.comment;
  }
  
  public void setComment(String comment) { this.comment = comment; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public com.hy.entity.product.Product getProduct() {
    return this.product;
  }
  
  public void setProduct(com.hy.entity.product.Product product) { this.product = product; }
  
  public double getFinalQuantity() {
    return this.finalQuantity;
  }
  
  public void setFinalQuantity(double finalQuantity) { this.finalQuantity = finalQuantity; }
  
  public double getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(double quantity) { this.quantity = quantity; }
}


