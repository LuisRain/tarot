package com.hy.entity.product;

import java.io.Serializable;

public class MeasurementUnit implements Serializable {
  private static final long serialVersionUID = 1L;
  private long id;
  private String unitName;
  
  public long getId() {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getUnitName() {
    return this.unitName;
  }
  
  public void setUnitName(String unitName) { this.unitName = unitName; }
}


