package com.hy.entity.order;

import com.hy.entity.product.Supplier;
import com.hy.entity.system.User;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseOrder
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String groupNum;
  private String orderNum;
  private int checkedState;
  private String orderDate;
  private String deliverDate;
  private int deliverStyle;
  private String deliverAddress;
  private String managerName;
  private String managerTel;
  private String comment;
  private double orderAmount;
  private double totalSvolume;
  private double totalWeight;
  private double paidAmount;
  private int clearingForm;
  private int isPrinted;
  private String createTime;
  private Supplier supplier;
  public int is_printed;
  private User user;
  private Long supplierId;
  private Long userId;

  public Long getSupplierId() {
    return supplierId;
  }

  public void setSupplierId(Long supplierId) {
    this.supplierId = supplierId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public int getIs_printed() {
	return is_printed;
}

public void setIs_printed(int is_printed) {
	this.is_printed = is_printed;
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
  
  public String getOrderDate() throws ParseException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat cc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = cc.parse(this.orderDate);
    return df.format(date);
  }
  
  public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
  
  public String getDeliverDate() throws ParseException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat cc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = cc.parse(this.deliverDate);
    return df.format(date);
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
  
  public int getIsPrinted() {
    return this.isPrinted;
  }
  
  public void setIsPrinted(int isPrinted) { this.isPrinted = isPrinted; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public Supplier getSupplier() {
    return this.supplier;
  }
  
  public void setSupplier(Supplier supplier) { this.supplier = supplier; }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) { this.user = user; }
  
  public int getClearingForm() {
    return this.clearingForm;
  }
  
  public void setClearingForm(int clearingForm) { this.clearingForm = clearingForm; }
}


