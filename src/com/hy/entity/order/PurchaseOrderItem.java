package com.hy.entity.order;

import java.io.Serializable;

public class PurchaseOrderItem implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private double purchasePrice;
  private double suggestQuantity;
  private double quantity;
  private double finalQuantity;
  private double giftQuantity;
  private String purchaseTime;
  private String svolume;
  private String weight;
  private String creator;
  private String comment;
  private String createTime;
  private com.hy.entity.product.Product product;

  private Long goodId;
  private Long supplerId;

  public double getGiftQuantity() {
    return giftQuantity;
  }

  public void setGiftQuantity(double giftQuantity) {
    this.giftQuantity = giftQuantity;
  }

  public Long getSupplerId() {
    return supplerId;
  }

  public void setSupplerId(Long supplerId) {
    this.supplerId = supplerId;
  }

  public Long getGoodId() {
    return goodId;
  }

  public void setGoodId(Long goodId) {
    this.goodId = goodId;
  }

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
  
  public void setFinalQuantity(float finalQuantity) {
    this.finalQuantity = finalQuantity;
  }
  
  public String getPurchaseTime() { return this.purchaseTime; }
  
  public void setPurchaseTime(String purchaseTime) {
    this.purchaseTime = purchaseTime;
  }
  
  public String getSvolume() { return this.svolume; }
  
  public void setSvolume(String svolume) {
    this.svolume = svolume;
  }
  
  public String getWeight() { return this.weight; }
  
  public void setWeight(String weight) {
    this.weight = weight;
  }
  
  public String getCreator() { return this.creator; }
  
  public void setCreator(String creator) {
    this.creator = creator;
  }
  
  public String getComment() { return this.comment; }
  
  public void setComment(String comment) {
    this.comment = comment;
  }
  
  public String getCreateTime() { return this.createTime; }
  
  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
  
  public com.hy.entity.product.Product getProduct() { return this.product; }
  
  public void setProduct(com.hy.entity.product.Product product) {
    this.product = product;
  }
  
  public double getSuggestQuantity() { return this.suggestQuantity; }
  
  public void setSuggestQuantity(double suggestQuantity) {
    this.suggestQuantity = suggestQuantity;
  }
  
  public double getQuantity() { return this.quantity; }
  
  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }
  
  public double getFinalQuantity() { return this.finalQuantity; }
  
  public void setFinalQuantity(double finalQuantity) {
    this.finalQuantity = finalQuantity;
  }
}


