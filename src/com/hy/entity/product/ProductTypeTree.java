package com.hy.entity.product;

import java.io.Serializable;

public class ProductTypeTree
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String menuTree;
  private int state;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getMenuTree() {
    return this.menuTree;
  }
  
  public void setMenuTree(String menuTree) { this.menuTree = menuTree; }
  
  public int getState() {
    return this.state;
  }
  
  public void setState(int state) { this.state = state; }
}


