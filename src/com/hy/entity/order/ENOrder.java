package com.hy.entity.order;

import com.hy.entity.product.Supplier;
import java.io.Serializable;

public class ENOrder implements Serializable
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
  private double totalSvolume;
  private double totalWeight;
  private double paidAmount;
  private int isIvtOrderPrint;
  private int isTemporary;
  private int isOrderPrint;
  private int ivtState;
  private String createTime;
  private Supplier supplier;
  private com.hy.entity.system.User user;
  private int orderType;
  private int ckId;
  
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
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public Supplier getSupplier() {
    return this.supplier;
  }
  
  public void setSupplier(Supplier supplier) { this.supplier = supplier; }
  
  public com.hy.entity.system.User getUser() {
    return this.user;
  }
  
  public void setUser(com.hy.entity.system.User user) { this.user = user; }
  
  public int getIsTemporary() {
    return this.isTemporary;
  }
  
  public void setIsTemporary(int isTemporary) { this.isTemporary = isTemporary; }
  
  public int getIsOrderPrint() {
    return this.isOrderPrint;
  }
  
  public void setIsOrderPrint(int isOrderPrint) { this.isOrderPrint = isOrderPrint; }
  
  public int getIvtState() {
    return this.ivtState;
  }
  
  public void setIvtState(int ivtState) { this.ivtState = ivtState; }
  
  public int getIsIvtOrderPrint() {
    return this.isIvtOrderPrint;
  }
  
  public void setIsIvtOrderPrint(int isIvtOrderPrint) { this.isIvtOrderPrint = isIvtOrderPrint; }
  
  public double getAmount() {
    return this.amount;
  }
  
  public void setAmount(double amount) { this.amount = amount; }
  
  public int getOrderType() {
    return this.orderType;
  }
  
  public void setOrderType(int orderType) { this.orderType = orderType; }
}


