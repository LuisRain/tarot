package com.hy.entity.system;

import java.io.Serializable;

public class Log
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String opModel;
  private String result;
  private String operName;
  private String createDate;
  private String ip;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getCreateDate() {
    return this.createDate;
  }
  
  public void setCreateDate(String createDate) { this.createDate = createDate; }
  
  public String getIp() {
    return this.ip;
  }
  
  public void setIp(String ip) { this.ip = ip; }
  
  public String getOpModel() {
    return this.opModel;
  }
  
  public void setOpModel(String opModel) { this.opModel = opModel; }
  
  public String getResult() {
    return this.result;
  }
  
  public void setResult(String result) { this.result = result; }
  
  public String getOperName() {
    return this.operName;
  }
  
  public void setOperName(String operName) { this.operName = operName; }
}


