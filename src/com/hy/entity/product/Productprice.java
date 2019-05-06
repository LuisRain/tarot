package com.hy.entity.product;

import java.io.Serializable;

public class Productprice
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Product product;
  private double costPrice;
  private double wholesalePrice;
  
  public Product getProduct()
  {
    return this.product;
  }
  
  public void setProduct(Product product) { this.product = product; }
  
  public double getCostPrice() {
    return this.costPrice;
  }
  
  public void setCostPrice(double costPrice) { this.costPrice = costPrice; }
  
  public double getWholesalePrice() {
    return this.wholesalePrice;
  }
  
  public void setWholesalePrice(double wholesalePrice) { this.wholesalePrice = wholesalePrice; }
  
  public static long getSerialversionuid() {
    return 1L;
  }
}


