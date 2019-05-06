package com.hy.entity.base;

import java.io.Serializable;
import java.util.Date;

public class SellingOrder implements Serializable {
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private int checkedState;
  private Date orderDate;
  private Date deliverDate;
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
  private Date createTime;
  private String deliverAddress;
  private Long userId;
  private Long merchantId;
  private Integer orderType;
  private Integer ckId;
  private Integer type;

  public Integer getOrderType() {
    return orderType;
  }

  public void setOrderType(Integer orderType) {
    this.orderType = orderType;
  }

  public Integer getCkId() {
    return ckId;
  }

  public void setCkId(Integer ckId) {
    this.ckId = ckId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGroupNum() {
    return groupNum;
  }

  public void setGroupNum(String groupNum) {
    this.groupNum = groupNum;
  }

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public int getCheckedState() {
    return checkedState;
  }

  public void setCheckedState(int checkedState) {
    this.checkedState = checkedState;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Date getDeliverDate() {
    return deliverDate;
  }

  public void setDeliverDate(Date deliverDate) {
    this.deliverDate = deliverDate;
  }

  public int getDeliverStyle() {
    return deliverStyle;
  }

  public void setDeliverStyle(int deliverStyle) {
    this.deliverStyle = deliverStyle;
  }

  public String getManagerName() {
    return managerName;
  }

  public void setManagerName(String managerName) {
    this.managerName = managerName;
  }

  public String getManagerTel() {
    return managerTel;
  }

  public void setManagerTel(String managerTel) {
    this.managerTel = managerTel;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public double getOrderAmount() {
    return orderAmount;
  }

  public void setOrderAmount(double orderAmount) {
    this.orderAmount = orderAmount;
  }

  public double getTotalSvolume() {
    return totalSvolume;
  }

  public void setTotalSvolume(double totalSvolume) {
    this.totalSvolume = totalSvolume;
  }

  public double getTotalWeight() {
    return totalWeight;
  }

  public void setTotalWeight(double totalWeight) {
    this.totalWeight = totalWeight;
  }

  public double getPaidAmount() {
    return paidAmount;
  }

  public void setPaidAmount(double paidAmount) {
    this.paidAmount = paidAmount;
  }

  public int getIsMonthClearing() {
    return isMonthClearing;
  }

  public void setIsMonthClearing(int isMonthClearing) {
    this.isMonthClearing = isMonthClearing;
  }

  public int getIsPrinted() {
    return isPrinted;
  }

  public void setIsPrinted(int isPrinted) {
    this.isPrinted = isPrinted;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getDeliverAddress() {
    return deliverAddress;
  }

  public void setDeliverAddress(String deliverAddress) {
    this.deliverAddress = deliverAddress;
  }
}