package com.hy.entity.order;

import com.hy.entity.system.User;
import java.io.Serializable;

public class OrderGroup implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long orderGroupId;
  private String orderGroupNum;
  private java.util.List<EXOrder> exos;
  private java.util.List<ENOrder> enos;
  private User user;
  private String createTime;
  private int state;
  private int groupType;
  private int ckId;
  private String ckname;
  private String groupNum;
  
  public String getGroupNum() {
	return groupNum;
}

public void setGroupNum(String groupNum) {
	this.groupNum = groupNum;
}

public int getCkId() {
	return ckId;
}

public String getCkname() {
	return ckname;
}

public void setCkname(String ckname) {
	this.ckname = ckname;
}

public void setCkId(int ckId) {
	this.ckId = ckId;
}

public String getOrderGroupNum()
  {
    return this.orderGroupNum;
  }
  
  public void setOrderGroupNum(String orderGroupNum) { this.orderGroupNum = orderGroupNum; }
  
  public long getOrderGroupId() {
    return this.orderGroupId;
  }
  
  public void setOrderGroupId(long orderGroupId) { this.orderGroupId = orderGroupId; }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) { this.user = user; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public java.util.List<EXOrder> getExos() {
    return this.exos;
  }
  
  public void setExos(java.util.List<EXOrder> exos) { this.exos = exos; }
  
  public java.util.List<ENOrder> getEnos() {
    return this.enos;
  }
  
  public void setEnos(java.util.List<ENOrder> enos) { this.enos = enos; }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) { this.state = state; }
  
  public int getGroupType() {
    return this.groupType;
  }
  
  public void setGroupType(int groupType) { this.groupType = groupType; }
}


