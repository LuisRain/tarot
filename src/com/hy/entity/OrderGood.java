package com.hy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.entity
 * @Email 1023012015@qq.com
 * @date 2019/3/28 8:46
 */
public class OrderGood implements Serializable {
    private long id;
    private long supplierId;
    private String groupNum;
    private String orderNum;
    private double sumBuyCounts;
    private double sumGiftQuantity;
    private double quantity;
    private Long goodId;
    private double productPrice;

    public double getSumGiftQuantity() {
        return sumGiftQuantity;
    }

    public void setSumGiftQuantity(double sumGiftQuantity) {
        this.sumGiftQuantity = sumGiftQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public double getSumBuyCounts() {
        return sumBuyCounts;
    }

    public void setSumBuyCounts(double sumBuyCounts) {
        this.sumBuyCounts = sumBuyCounts;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }
}
