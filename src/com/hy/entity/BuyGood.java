package com.hy.entity;

/**
 * @author SuPengFei
 * @version V1.0
 * @Package com.hy.entity
 * @date 2019/3/11 18:52
 */
public class BuyGood {
    private Long id;
    private Long goodId;
    private Long warehouseGoodId;
    private Integer minOrderNum;
    private Double warehouseGoodCounts;
    private Double sumBuyCounts;
    private Double sumGiftCounts;
    private Double buyCounts;
    private Double finalBuyCounts;
    private Double giftCounts;
    private Double differenceValue;
    private String groupNum;


    public Double getDifferenceValue() {
        return differenceValue;
    }

    public void setDifferenceValue(Double differenceValue) {
        this.differenceValue = differenceValue;
    }

    public Integer getMinOrderNum() {
        return minOrderNum;
    }

    public void setMinOrderNum(Integer minOrderNum) {
        this.minOrderNum = minOrderNum;
    }

    public Long getWarehouseGoodId() {
        return warehouseGoodId;
    }

    public void setWarehouseGoodId(Long warehouseGoodId) {
        this.warehouseGoodId = warehouseGoodId;
    }

    public Double getWarehouseGoodCounts() {
        return warehouseGoodCounts;
    }

    public void setWarehouseGoodCounts(Double warehouseGoodCounts) {
        this.warehouseGoodCounts = warehouseGoodCounts;
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public Double getBuyCounts() {
        return buyCounts;
    }

    public void setBuyCounts(Double buyCounts) {
        this.buyCounts = buyCounts;
    }

    public Double getFinalBuyCounts() {
        return finalBuyCounts;
    }

    public void setFinalBuyCounts(Double finalBuyCounts) {
        this.finalBuyCounts = finalBuyCounts;
    }

    public Double getGiftCounts() {
        return giftCounts;
    }

    public void setGiftCounts(Double giftCounts) {
        this.giftCounts = giftCounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Double getSumBuyCounts() {
        return sumBuyCounts;
    }

    public void setSumBuyCounts(Double sumBuyCounts) {
        this.sumBuyCounts = sumBuyCounts;
    }

    public Double getSumGiftCounts() {
        return sumGiftCounts;
    }

    public void setSumGiftCounts(Double sumGiftCounts) {
        this.sumGiftCounts = sumGiftCounts;
    }
}
