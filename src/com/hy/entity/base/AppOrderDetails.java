package com.hy.entity.base;

import java.util.List;

public class AppOrderDetails {

    private List<OrderItem> goods;

    private Integer count;

    private Double totalMoney;


    public List<OrderItem> getGoods() {
        return goods;
    }

    public void setGoods(List<OrderItem> goods) {
        this.goods = goods;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
