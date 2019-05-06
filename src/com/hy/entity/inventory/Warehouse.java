package com.hy.entity.inventory;

import java.io.Serializable;

public class Warehouse
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private char warehouseName;
  private char warehouseAddress;
  private char warehouseTel;
  private char warehouseNumber;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public char getWarehouseName() {
    return this.warehouseName;
  }
  
  public void setWarehouseName(char warehouseName) { this.warehouseName = warehouseName; }
  
  public char getWarehouseAddress() {
    return this.warehouseAddress;
  }
  
  public void setWarehouseAddress(char warehouseAddress) { this.warehouseAddress = warehouseAddress; }
  
  public char getWarehouseTel() {
    return this.warehouseTel;
  }
  
  public void setWarehouseTel(char warehouseTel) { this.warehouseTel = warehouseTel; }
  
  public char getWarehouseNumber() {
    return this.warehouseNumber;
  }
  
  public void setWarehouseNumber(char warehouseNumber) { this.warehouseNumber = warehouseNumber; }
  
  public static long getSerialversionuid() {
    return 1L;
  }
}


