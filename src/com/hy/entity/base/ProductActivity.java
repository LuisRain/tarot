package com.hy.entity.base;

/**
 * @author SuPengFei
 * @version V1.0
 * @Package com.hy.entity.base
 * @date 2019/3/11 8:35
 */
public class ProductActivity {
    private Long id;
    private Long productId;
    private Double quantity;
    private Long productActivity;
    private Double finalQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Long getProductActivity() {
        return productActivity;
    }

    public void setProductActivity(Long productActivity) {
        this.productActivity = productActivity;
    }

    public Double getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(Double finalQuantity) {
        this.finalQuantity = finalQuantity;
    }
}
