package com.hy.entity.product;

import java.io.Serializable;
import java.util.List;

public class ProductType
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String classifyName;
  private int level;
  private int state;
  private long parentId;
  private List<ProductType> pts;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public int getLevel() {
    return this.level;
  }
  
  public void setLevel(int level) { this.level = level; }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) { this.state = state; }
  
  public String getClassifyName() {
    return this.classifyName;
  }
  
  public void setClassifyName(String classifyName) { this.classifyName = classifyName; }
  
  public long getParentId() {
    return this.parentId;
  }
  
  public void setParentId(long parentId) { this.parentId = parentId; }
  
  public List<ProductType> getPts() {
    return this.pts;
  }
  
  public void setPts(List<ProductType> pts) { this.pts = pts; }
}


