package com.hy.entity.inventory;

import com.hy.entity.product.CargoSpace;
import com.hy.entity.product.Product;
import java.io.Serializable;

public class Productinventory
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private Product product;
  private Warehouse warehouse;
  private CargoSpace cargoSpace;
  private Long productQuantity;
  private Long state;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public Product getProduct() {
    return this.product;
  }
  
  public void setProduct(Product product) { this.product = product; }
  
  public Warehouse getWarehouse() {
    return this.warehouse;
  }
  
  public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
  
  public CargoSpace getCargoSpace() {
    return this.cargoSpace;
  }
  
  public void setCargoSpace(CargoSpace cargoSpace) { this.cargoSpace = cargoSpace; }
  
  public Long getProductQuantity() {
    return this.productQuantity;
  }
  
  public void setProductQuantity(Long productQuantity) { this.productQuantity = productQuantity; }
  
  public Long getState() {
    return this.state;
  }
  
  public void setState(Long state) { this.state = state; }
  
  public static long getSerialversionuid() {
    return 1L;
  }
}


