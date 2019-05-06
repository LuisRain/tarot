package com.hy.entity.base;

/**
 * @author sren
 * @create 2018-12-31 下午2:29
 **/

public class Goods {

    private Integer id;

    private String image;

    private String goodsName;

    private String goodsCode;

    private Double goodsRetailPrice;
    private Double supplyPrice;

    private String type;

    private String goodsSaleUnit;

    private String packUnit;

    private String standard;
    private String minOrderNum;


    private String grossMargin;


    private String goodsShelfLife;

    private String goodsMiniOrder;

    private double goodsTaxRate;

    private String supplierCode;

    private String goodsBarcode;

    private String supplierName;

    private String remark;

    private String isImport;
    private String activity;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getIsImport() {
        return isImport;
    }

    public void setIsImport(String isImport) {
        this.isImport = isImport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Double getGoodsRetailPrice() {
        return goodsRetailPrice;
    }

    public void setGoodsRetailPrice(Double goodsRetailPrice) {
        this.goodsRetailPrice = goodsRetailPrice;
    }

    public Double getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodsSaleUnit() {
        return goodsSaleUnit;
    }

    public void setGoodsSaleUnit(String goodsSaleUnit) {
        this.goodsSaleUnit = goodsSaleUnit;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }
    public String getMinOrderNum() {
        return minOrderNum;
    }

    public void setMinOrderNum(String minOrderNum) {
        this.minOrderNum = minOrderNum;
    }

    public String getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(String grossMargin) {
        this.grossMargin = grossMargin;
    }

    public String getGoodsShelfLife() {
        return goodsShelfLife;
    }

    public void setGoodsShelfLife(String goodsShelfLife) {
        this.goodsShelfLife = goodsShelfLife;
    }

    public String getGoodsMiniOrder() {
        return goodsMiniOrder;
    }

    public void setGoodsMiniOrder(String goodsMiniOrder) {
        this.goodsMiniOrder = goodsMiniOrder;
    }

    public double getGoodsTaxRate() {
        return goodsTaxRate;
    }

    public void setGoodsTaxRate(double goodsTaxRate) {
        this.goodsTaxRate = goodsTaxRate;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
