package com.hy.entity.base;

import java.util.Date;

/**
 * @author sren
 * @create 2019-01-02 上午9:08
 **/

public class OrderItem {

    private Long id;
    private Integer isShelve;

    private Long productId;

    private Long minOrderNum;
    private Long productTypeId;
    private String image;

    private String goodsName;

    private String goodsCode;

    private String goodsUnitPrice;

    private String goodsAmount;

    private String orderNum;

    private String groupNum;

    private String isImport;

    private String activity;

    private Long orderId;

    private Date createTime;

    private Double quantity;

    private Double giftQuantity;

    public Integer getIsShelve() {
        return isShelve;
    }

    public void setIsShelve(Integer isShelve) {
        this.isShelve = isShelve;
    }

    public Long getMinOrderNum() {
        return minOrderNum;
    }

    public void setMinOrderNum(Long minOrderNum) {
        this.minOrderNum = minOrderNum;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getGiftQuantity() {
        return giftQuantity;
    }

    public void setGiftQuantity(Double giftQuantity) {
        this.giftQuantity = giftQuantity;
    }

    public String getIsImport() {
        return isImport;
    }

    public void setIsImport(String isImport) {
        this.isImport = isImport;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getGoodsUnitPrice() {
        return goodsUnitPrice;
    }

    public void setGoodsUnitPrice(String goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
