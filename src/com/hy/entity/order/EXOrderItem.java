package com.hy.entity.order;

import java.io.Serializable;

public class EXOrderItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String groupNum;
	private String orderNum;
	private double purchasePrice;
	private double salePrice;
	private double quantity;
	private double finalQuantity;

	private double giftQuantity;

	private double totalFinalQuantity;
	private double perFinalQuantity;
	private String svolume;
	private String weight;
	private String eXTime;
	private String creator;
	private String comment;
	private String createTime;
	private com.hy.entity.product.Product product;
	private String zyOrderNum;
	private String perCount;
	private String totalCount;
	private Integer isivtBK;
	private String fc_order_num;
	private Long goodId;

	public double getGiftQuantity() {
		return giftQuantity;
	}

	public void setGiftQuantity(double giftQuantity) {
		this.giftQuantity = giftQuantity;
	}

	public Long getGoodId() {
		return goodId;
	}

	public void setGoodId(Long goodId) {
		this.goodId = goodId;
	}

	public String getFc_order_num() {
		return fc_order_num;
	}

	public void setFc_order_num(String fc_order_num) {
		this.fc_order_num = fc_order_num;
	}

	public Integer getIsivtBK() {
		return isivtBK;
	}

	public void setIsivtBK(Integer isivtBK) {
		this.isivtBK = isivtBK;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupNum() {
		return this.groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public String getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public double getSalePrice() {
		return this.salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getFinalQuantity() {
		return this.finalQuantity;
	}

	public void setFinalQuantity(double finalQuantity) {
		this.finalQuantity = finalQuantity;
	}

	public String getSvolume() {
		return this.svolume;
	}

	public void setSvolume(String svolume) {
		this.svolume = svolume;
	}

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public com.hy.entity.product.Product getProduct() {
		return this.product;
	}

	public void setProduct(com.hy.entity.product.Product product) {
		this.product = product;
	}

	public String geteXTime() {
		return this.eXTime;
	}

	public void seteXTime(String eXTime) {
		this.eXTime = eXTime;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getZyOrderNum() {
		return this.zyOrderNum;
	}

	public void setZyOrderNum(String zyOrderNum) {
		this.zyOrderNum = zyOrderNum;
	}

	public double getTotalFinalQuantity() {
		return this.totalFinalQuantity;
	}

	public void setTotalFinalQuantity(double totalFinalQuantity) {
		this.totalFinalQuantity = totalFinalQuantity;
	}

	public double getPerFinalQuantity() {
		return this.perFinalQuantity;
	}

	public void setPerFinalQuantity(double perFinalQuantity) {
		this.perFinalQuantity = perFinalQuantity;
	}

	public String getPerCount() {
		return this.perCount;
	}

	public void setPerCount(String perCount) {
		this.perCount = perCount;
	}

	public String getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
}
