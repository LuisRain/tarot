package com.hy.entity.product;

import com.hy.entity.inventory.Warehouse;
import java.io.Serializable;

public class Product implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String productName;
  private String productNum;
  private String barCode;
  private String unit;
  private int expireDays;
  private String creator;
  private String createTime;
  private int minStockNum;
  private int maxStockNum;
  private String hostCode;
  private int isShelve;
  private int weight;
  private String weightUnit;
  private String taxes;
  private String skuVolume;
  private String fclVolume;
  private String skuWeight;
  private String fclWeight;
  private String storeMethod;
  private String storeMethodConsultingTelephone;
  private String usageMode;
  private String originPlace;
  private String remarks;
  private ProductType ptype;
  private String boxNumber;
  private CargoSpace cargoSpace;
  private Warehouse warehouse;
  private Supplier supplier;
  private double totalQuantity;
  private java.util.List<Merchant> merchants;
  private MeasurementUnit measurementUnit;
  private double salePrice;
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id) { this.id = id; }
  
  public String getProductName() {
    return this.productName;
  }
  
  public void setProductName(String productName) { this.productName = productName; }
  
  public String getProductNum() {
    return this.productNum;
  }
  
  public void setProductNum(String productNum) { this.productNum = productNum; }
  
  public String getBarCode() {
    return this.barCode;
  }
  
  public void setBarCode(String barCode) { this.barCode = barCode; }
  
  public String getUnit() {
    return this.unit;
  }
  
  public void setUnit(String unit) { this.unit = unit; }
  
  public int getExpireDays() {
    return this.expireDays;
  }
  
  public void setExpireDays(int expireDays) { this.expireDays = expireDays; }
  
  public String getCreator() {
    return this.creator;
  }
  
  public void setCreator(String creator) { this.creator = creator; }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) { this.createTime = createTime; }
  
  public int getMinStockNum() {
    return this.minStockNum;
  }
  
  public void setMinStockNum(int minStockNum) { this.minStockNum = minStockNum; }
  
  public int getMaxStockNum() {
    return this.maxStockNum;
  }
  
  public void setMaxStockNum(int maxStockNum) { this.maxStockNum = maxStockNum; }
  
  public String getHostCode() {
    return this.hostCode;
  }
  
  public void setHostCode(String hostCode) { this.hostCode = hostCode; }
  
  public int getIsShelve() {
    return this.isShelve;
  }
  
  public void setIsShelve(int isShelve) { this.isShelve = isShelve; }
  
  public int getWeight() {
    return this.weight;
  }
  
  public void setWeight(int weight) { this.weight = weight; }
  
  public String getWeightUnit() {
    return this.weightUnit;
  }
  
  public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
  
  public String getTaxes() {
    return this.taxes;
  }
  
  public void setTaxes(String taxes) { this.taxes = taxes; }
  
  public String getSkuVolume() {
    return this.skuVolume;
  }
  
  public void setSkuVolume(String skuVolume) { this.skuVolume = skuVolume; }
  
  public String getFclVolume() {
    return this.fclVolume;
  }
  
  public void setFclVolume(String fclVolume) { this.fclVolume = fclVolume; }
  
  public String getStoreMethod() {
    return this.storeMethod;
  }
  
  public void setStoreMethod(String storeMethod) { this.storeMethod = storeMethod; }
  
  public String getStoreMethodConsultingTelephone() {
    return this.storeMethodConsultingTelephone;
  }
  
  public void setStoreMethodConsultingTelephone(String storeMethodConsultingTelephone) {
    this.storeMethodConsultingTelephone = storeMethodConsultingTelephone;
  }
  
  public String getUsageMode() { return this.usageMode; }
  
  public void setUsageMode(String usageMode) {
    this.usageMode = usageMode;
  }
  
  public String getOriginPlace() { return this.originPlace; }
  
  public void setOriginPlace(String originPlace) {
    this.originPlace = originPlace;
  }
  
  public String getRemarks() { return this.remarks; }
  
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  
  public ProductType getPtype() {
    return this.ptype;
  }
  
  public void setPtype(ProductType ptype) { this.ptype = ptype; }
  
  public CargoSpace getCargoSpace() {
    return this.cargoSpace;
  }
  
  public void setCargoSpace(CargoSpace cargoSpace) { this.cargoSpace = cargoSpace; }
  
  public Warehouse getWarehouse() {
    return this.warehouse;
  }
  
  public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
  
  public Supplier getSupplier() {
    return this.supplier;
  }
  
  public void setSupplier(Supplier supplier) { this.supplier = supplier; }
  
  public double getTotalQuantity() {
    return this.totalQuantity;
  }
  
  public void setTotalQuantity(double totalQuantity) { this.totalQuantity = totalQuantity; }
  
  public java.util.List<Merchant> getMerchants() {
    return this.merchants;
  }
  
  public void setMerchants(java.util.List<Merchant> merchants) { this.merchants = merchants; }
  
  public MeasurementUnit getMeasurementUnit() {
    return this.measurementUnit;
  }
  
  public void setMeasurementUnit(MeasurementUnit measurementUnit) { this.measurementUnit = measurementUnit; }
  
  public String getBoxNumber() {
    return this.boxNumber;
  }
  
  public void setBoxNumber(String boxNumber) { this.boxNumber = boxNumber; }
  
  public double getSalePrice() {
    return this.salePrice;
  }
  
  public void setSalePrice(double salePrice) { this.salePrice = salePrice; }
  
  public String getSkuWeight() {
    return this.skuWeight;
  }
  
  public void setSkuWeight(String skuWeight) { this.skuWeight = skuWeight; }
  
  public String getFclWeight() {
    return this.fclWeight;
  }
  
  public void setFclWeight(String fclWeight) { this.fclWeight = fclWeight; }
}


