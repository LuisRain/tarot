package com.hy.entity.order;

import com.hy.entity.product.Merchant;
import java.io.Serializable;

public class EXOrder implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private int checkedState;
  private String orderDate;
  private String managerName;
  private String managerTel;
  private String comment;
  private double finalAmount;
  private double amount;
  private String deliverDate;
  private int deliverStyle;
  private String deliverAddress;
  private double totalSvolume;
  private double totalWeight;
  private double paidAmount;
  private int is_ivt_order_print;
  private int is_temporary;
  private int is_order_print;
  private int ivt_state;
  private String createTime;
  private Merchant merchant;
  private com.hy.entity.system.User user;
  private int orderType;
  private int ckId;

  private Long merchantId;
  private Long userId;

  public Long getGoodId() {
    return goodId;
  }

  public void setGoodId(Long goodId) {
    this.goodId = goodId;
  }

  private Long goodId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public int getCkId() {
	return ckId;
}

public void setCkId(int ckId) {
	this.ckId = ckId;
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
  
  public int getCheckedState() {
    return this.checkedState;
  }
  
  public void setCheckedState(int checkedState) { this.checkedState = checkedState; }
  
  public String getOrderDate() {
    return this.orderDate;
  }
  
  public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
  
  public String getManagerName() {
    return this.managerName;
  }
  
  public void setManagerName(String managerName) { this.managerName = managerName; }
  
  public String getManagerTel() {
    return this.managerTel;
  }
  
  public void setManagerTel(String managerTel) { this.managerTel = managerTel; }
  
  public String getComment() {
    return this.comment;
  }
  
  public void setComment(String comment) { this.comment = comment; }
  
  public double getFinalAmount() {
    return this.finalAmount;
  }
  
  public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
  
  public double getTotalSvolume() {
    return this.totalSvolume;
  }
  
  public void setTotalSvolume(double totalSvolume) { this.totalSvolume = totalSvolume; }
  
  public double getTotalWeight() {
    return this.totalWeight;
  }
  
  public void setTotalWeight(double totalWeight) { this.totalWeight = totalWeight; }
  
  public double getPaidAmount() {
    return this.paidAmount;
  }
  
  public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
  
  public int getIs_ivt_order_print() {
    return this.is_ivt_order_print;
  }
  
  public void setIs_ivt_order_print(int is_ivt_order_print) { this.is_ivt_order_print = is_ivt_order_print; }
  
  public int getIs_temporary() {
    return this.is_temporary;
  }
  
  public void setIs_temporary(int is_temporary) { this.is_temporary = is_temporary; }
  
  public int getIs_order_print() {
    return this.is_order_print;
  }
  
  public void setIs_order_print(int is_order_print) { this.is_order_print = is_order_print; }
  
  public int getIvt_state() {
    return this.ivt_state;
  }
  
  public void setIvt_state(int ivt_state) { this.ivt_state = ivt_state; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public Merchant getMerchant() {
    return this.merchant;
  }
  
  public void setMerchant(Merchant merchant) { this.merchant = merchant; }
  
  public com.hy.entity.system.User getUser() {
    return this.user;
  }
  
  public void setUser(com.hy.entity.system.User user) { this.user = user; }
  
  public String getDeliverDate() {
    return this.deliverDate;
  }
  
  public void setDeliverDate(String deliverDate) { this.deliverDate = deliverDate; }
  
  public int getDeliverStyle() {
    return this.deliverStyle;
  }
  
  public void setDeliverStyle(int deliverStyle) { this.deliverStyle = deliverStyle; }
  
  public String getDeliverAddress() {
    return this.deliverAddress;
  }
  
  public void setDeliverAddress(String deliverAddress) { this.deliverAddress = deliverAddress; }
  
  public double getAmount() {
    return this.amount;
  }
  
  public void setAmount(double amount) { this.amount = amount; }
  
  public int getOrderType() {
    return this.orderType;
  }
  
  public void setOrderType(int orderType) { this.orderType = orderType; }
}


