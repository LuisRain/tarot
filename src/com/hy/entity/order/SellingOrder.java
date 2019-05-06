package com.hy.entity.order;

import com.hy.entity.inventory.Warehouse;
import com.hy.entity.product.Merchant;
import com.hy.entity.system.User;
import java.io.Serializable;

public class SellingOrder implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private int checkedState;
  private String orderDate;
  private String deliverDate;
  private int deliverStyle;
  private String managerName;
  private String managerTel;
  private String comment;
  private double orderAmount;
  private double totalSvolume;
  private double totalWeight;
  private double paidAmount;
  private int isMonthClearing;
  private int isPrinted;
  private String createTime;
  private String deliverAddress;
  private Warehouse supplier;
  private User user;
  private Merchant merchant;
  
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
  
  public String getDeliverDate() {
    return this.deliverDate;
  }
  
  public void setDeliverDate(String deliverDate) { this.deliverDate = deliverDate; }
  
  public int getDeliverStyle() {
    return this.deliverStyle;
  }
  
  public void setDeliverStyle(int deliverStyle) { this.deliverStyle = deliverStyle; }
  
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
  
  public double getOrderAmount() {
    return this.orderAmount;
  }
  
  public void setOrderAmount(double orderAmount) { this.orderAmount = orderAmount; }
  
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
  
  public int getIsMonthClearing() {
    return this.isMonthClearing;
  }
  
  public void setIsMonthClearing(int isMonthClearing) { this.isMonthClearing = isMonthClearing; }
  
  public int getIsPrinted() {
    return this.isPrinted;
  }
  
  public void setIsPrinted(int isPrinted) { this.isPrinted = isPrinted; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public Warehouse getSupplier() {
    return this.supplier;
  }
  
  public void setSupplier(Warehouse supplier) { this.supplier = supplier; }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) { this.user = user; }
  
  public Merchant getMerchant() {
    return this.merchant;
  }
  
  public void setMerchant(Merchant merchant) { this.merchant = merchant; }
  
  public String getDeliverAddress() {
    return this.deliverAddress;
  }
  
  public void setDeliverAddress(String deliverAddress) { this.deliverAddress = deliverAddress; }
}


