package com.hy.entity.product;

import java.io.Serializable;

public class Merchant implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String merchantNum;
  private String merchantName;
  private int mechantType;
  private long centerStoreId;
  private String contactPerson;
  private String mobile;
  private String address;
  private int state;
  private String createTime;
  private String account;
  private String password;
  private String imageUrl;
  private double accountBalance;
  private String remarks;
  private String productCount;
  private String splitTotalCount;
  private String splitPerCount;
  private long texoiId;
  private String oprationtime;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getMerchantNum() {
    return this.merchantNum;
  }
  
  public void setMerchantNum(String merchantNum) {
    this.merchantNum = merchantNum;
  }
  
  public String getMerchantName() {
    return this.merchantName;
  }
  
  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }
  
  public String getContactPerson() {
    return this.contactPerson;
  }
  
  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public String getAddress() {
    return this.address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) {
    this.state = state;
  }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
  
  public String getAccount() {
    return this.account;
  }
  
  public void setAccount(String account) {
    this.account = account;
  }
  
  public String getImageUrl() {
    return this.imageUrl;
  }
  
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
  
  public double getAccountBalance() {
    return this.accountBalance;
  }
  
  public void setAccountBalance(double accountBalance) {
    this.accountBalance = accountBalance;
  }
  
  public String getRemarks() {
    return this.remarks;
  }
  
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public int getMechantType() {
    return this.mechantType;
  }
  
  public void setMechantType(int mechantType) {
    this.mechantType = mechantType;
  }
  
  public long getCenterStoreId() {
    return this.centerStoreId;
  }
  
  public void setCenterStoreId(long centerStoreId) {
    this.centerStoreId = centerStoreId;
  }
  
  public String getProductCount() {
    return this.productCount;
  }
  
  public void setProductCount(String productCount) {
    this.productCount = productCount;
  }
  
  public String getSplitTotalCount() {
    return this.splitTotalCount;
  }
  
  public void setSplitTotalCount(String splitTotalCount) {
    this.splitTotalCount = splitTotalCount;
  }
  
  public String getSplitPerCount() {
    return this.splitPerCount;
  }
  
  public void setSplitPerCount(String splitPerCount) {
    this.splitPerCount = splitPerCount;
  }
  
  public long getTexoiId() {
    return this.texoiId;
  }
  
  public void setTexoiId(long texoiId) {
    this.texoiId = texoiId;
  }
  
  public String getoprationtime() {
    return this.oprationtime;
  }
  
  public void setoprationtime(String oprationtime) {
    this.oprationtime = oprationtime;
  }
}


