package com.hy.entity.order;

import com.hy.entity.product.Product;
import java.io.Serializable;

public class SellingOrderItem implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private double purchasePrice;
  private double salePrice;
  private double finalQuantity;
  private double quantity;
  private double giftQuantity;
  private String creator;
  private String createTime;
  private Product product;
  private String comment;
  private Long productId;
  private Integer state;
  private Long merchantId;


  public double getGiftQuantity() {
    return giftQuantity;
  }

  public void setGiftQuantity(double giftQuantity) {
    this.giftQuantity = giftQuantity;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
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
  
  public double getSalePrice() {
    return this.salePrice;
  }
  
  public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
  
  public double getFinalQuantity() {
    return this.finalQuantity;
  }
  
  public void setFinalQuantity(double finalQuantity) { this.finalQuantity = finalQuantity; }
  
  public double getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(double quantity) { this.quantity = quantity; }
  
  public String getCreator() {
    return this.creator;
  }
  
  public void setCreator(String creator) { this.creator = creator; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public Product getProduct() {
    return this.product;
  }
  
  public void setProduct(Product product) { this.product = product; }
  
  public String getComment() {
    return this.comment;
  }
  
  public void setComment(String comment) { this.comment = comment; }
}


