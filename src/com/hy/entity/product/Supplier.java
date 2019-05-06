package com.hy.entity.product;

import java.io.Serializable;
import java.sql.Date;

public class Supplier implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String supplierName;
  private String contactPerson;
  private String contactPerson_mobile;
  private String address;
  private int state;
  private String remarks;
  private Date createDate;
  private String supplierTel;
  private String supplierNum;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getSupplierName() {
    return this.supplierName;
  }
  
  public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
  
  public String getContactPerson() {
    return this.contactPerson;
  }
  
  public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
  
  public String getContactPerson_mobile() {
    return this.contactPerson_mobile;
  }
  
  public void setContactPerson_mobile(String contactPerson_mobile) { this.contactPerson_mobile = contactPerson_mobile; }
  
  public String getAddress() {
    return this.address;
  }
  
  public void setAddress(String address) { this.address = address; }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) { this.state = state; }
  
  public String getRemarks() {
    return this.remarks;
  }
  
  public void setRemarks(String remarks) { this.remarks = remarks; }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(Date createDate) { this.createDate = createDate; }
  
  public String getSupplierTel() {
    return this.supplierTel;
  }
  
  public void setSupplierTel(String supplierTel) { this.supplierTel = supplierTel; }
  
  public String getSupplierNum() {
    return this.supplierNum;
  }
  
  public void setSupplierNum(String supplierNum) { this.supplierNum = supplierNum; }
  
  public static long getSerialversionuid() {
    return 1L;
  }
}


