package com.hy.entity.order;

import com.hy.entity.system.User;
import java.io.Serializable;
import java.util.List;

public class WaveSortingGroup implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long waveSortingGroupId;
  private String waveSortingGroupNum;
  private List<EXOrder> exos;
  private User user;
  private String createTime;
  private int waveSortingGroupState;
  private int waveSortingGroupType;
  private String exOrderIdsString;
  private int waveOrder;
  private String groupNum;
  
  public long getWaveSortingGroupId()
  {
    return this.waveSortingGroupId;
  }
  
  public void setWaveSortingGroupId(long waveSortingGroupId) { this.waveSortingGroupId = waveSortingGroupId; }
  
  public List<EXOrder> getExos() {
    return this.exos;
  }
  
  public void setExos(List<EXOrder> exos) { this.exos = exos; }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) { this.user = user; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public int getWaveSortingGroupType() {
    return this.waveSortingGroupType;
  }
  
  public void setWaveSortingGroupType(int waveSortingGroupType) { this.waveSortingGroupType = waveSortingGroupType; }
  
  public int getWaveSortingGroupState() {
    return this.waveSortingGroupState;
  }
  
  public void setWaveSortingGroupState(int waveSortingGroupState) { this.waveSortingGroupState = waveSortingGroupState; }
  
  public String getExOrderIdsString() {
    return this.exOrderIdsString;
  }
  
  public void setExOrderIdsString(String exOrderIdsString) { this.exOrderIdsString = exOrderIdsString; }
  
  public String getWaveSortingGroupNum() {
    return this.waveSortingGroupNum;
  }
  
  public void setWaveSortingGroupNum(String waveSortingGroupNum) { this.waveSortingGroupNum = waveSortingGroupNum; }
  
  public int getWaveOrder() {
    return this.waveOrder;
  }
  
  public void setWaveOrder(int waveOrder) { this.waveOrder = waveOrder; }
  
  public String getGroupNum() {
    return this.groupNum;
  }
  
  public void setGroupNum(String groupNum) { this.groupNum = groupNum; }
}


