package com.hy.entity.product;

import java.io.Serializable;

public class CargoSpace implements Serializable {
  private static final long serialVersionUID = 1L;
  private long id;
  private String cargoSpaceNum;
  private String zone;
  private String storey;
  private String storeyNum;
  
  public long getId() {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getCargoSpaceNum() {
    return this.cargoSpaceNum;
  }
  
  public void setCargoSpaceNum(String cargoSpaceNum) { this.cargoSpaceNum = cargoSpaceNum; }
  
  public String getZone() {
    return this.zone;
  }
  
  public void setZone(String zone) { this.zone = zone; }
  
  public String getStorey() {
    return this.storey;
  }
  
  public void setStorey(String storey) { this.storey = storey; }
  
  public String getStoreyNum() {
    return this.storeyNum;
  }
  
  public void setStoreyNum(String storeyNum) { this.storeyNum = storeyNum; }
}


