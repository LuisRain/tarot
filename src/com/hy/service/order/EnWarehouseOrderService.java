package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.product.Product;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.product.ProductService;
import com.hy.util.NumberUtil;
import com.hy.util.ObjectExcelRead;
import com.hy.util.PageData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("enwarehouseorderService")
public class EnWarehouseOrderService {
	@Resource(name = "productService")
	private ProductService productService;
	@Resource
	private ENOrderItemService enOrderItemService;
	@Resource
	private EXOrderItemService eXOrderItemService;
	@Resource(name = "productinventoryService")
	private ProductinventoryService productinventoryService;
	@Resource
	private ExWarehouseOrderService exWarehouseOrderService;
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public void save(PageData pd) throws Exception {
		this.dao.save("EnWarehouseOrderMapper.save", pd);
	}

	public List<PageData> purchaseReturnlistpage(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.purchaseReturnlistPage", page);
	}

	public List<PageData> dataCeterEnOrderlistPage(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.dataCeterEnOrderlistPage", page);
	}

	public PageData getEnwarouseByorderNum(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject(
				"EnWarehouseOrderMapper.getEnwarouseByorderNum", pd);
	}

	public List<PageData> inferiDatalistPage(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.inferiDatalistPage", page);
	}

	public int updateFinalAmount(PageData pd) throws Exception {
		Object p = this.dao.update("EnWarehouseOrderMapper.updateFinalAmount",
				pd);
		return Integer.parseInt(p.toString());
	}

	public void deleteEnOrderItemByOrderNum(PageData pd) throws Exception {
		this.dao.delete("EnWarehouseOrderMapper.deleteEnOrderItemByOrderNum",
				pd);
	}

	public void delete(PageData pd) throws Exception {
		this.dao.delete("EnWarehouseOrderMapper.delete", pd);
	}

	public int edit(PageData pd) throws Exception {
		Object o = this.dao.update("EnWarehouseOrderMapper.edit", pd);
		return Integer.parseInt(o.toString());
	}

	public List<PageData> list(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.dataAlllistPage", page);
	}

	/**
	 * 销售入库单
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> sellingOrderlistPage(Page page) throws Exception{
		return (List<PageData>) dao.findForList("EnWarehouseOrderMapper.sellingOrderlistPage", page);
	}
	
	public List<PageData> sellingOrderlistexcel(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("EnWarehouseOrderMapper.sellingOrderlistexcel", pd);
	}
	
	public List<PageData> listOfGift(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.dataAllOfGiftlistPage", page);
	}

	public List<PageData> enTemplistPage(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.enTemplistPage", page);
	}

	public List<PageData> StorageWarehouseList(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.StorageWarehouselistPage", page);
	}

	public List<PageData> listAll(PageData pd) throws Exception {
		return (List) this.dao
				.findForList("EnWarehouseOrderMapper.listAll", pd);
	}

	public PageData findById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject(
				"EnWarehouseOrderMapper.findById", pd);
	}

	@Transactional
	public String reviewedAll(String[] ArrayDATA_IDS) {
		String msg = "";
		try {
			PageData enwarhouseOrder = null;
			PageData productInvertory = null;
			PageData enOrderItem = null;
			for (String id : ArrayDATA_IDS) {
				enwarhouseOrder = new PageData();
				enwarhouseOrder.put("id", id);

				enwarhouseOrder = (PageData) this.dao.findForObject(
						"EnWarehouseOrderMapper.findById", enwarhouseOrder);

				List<PageData> enOrderItemList = (List) this.dao.findForList(
						"ENOrderItemMapper.findOrderItemByOrderNum",
						enwarhouseOrder);

				for (PageData page : enOrderItemList) {
					long quantity = Math.round(Double.parseDouble(page.get(
							"quantity").toString()));
					long finalQuantity = Math.round(Double.parseDouble(page
							.get("final_quantity").toString()));
					productInvertory = new PageData();
					productInvertory.put("productId", page.get("product_id"));
					productInvertory.put("newQuantity",
							page.get("final_quantity"));
					productInvertory.put("warehouseId", page.get("is_ivt_BK"));
					productInvertory.put("ck_id",
							enwarhouseOrder.getString("ck_id"));
					productInvertory.put("order_num",
							enwarhouseOrder.getString("order_num"));
					enOrderItem = new PageData();
					enOrderItem.put("id", page.get("id"));
					enOrderItem.put("is_split_ivt", Integer.valueOf(1));
					this.productinventoryService
							.updateProductinventoryAdd(productInvertory);
					this.enOrderItemService.editState(enOrderItem);
					if (quantity <= finalQuantity) {
						distributionExOrderItem(page);
					} else {
						msg = "入库数不能小于采购数量";
						throw new RuntimeException();
					}
				}
				enwarhouseOrder.remove("ivt_state");
				enwarhouseOrder.put("ivt_state", Integer.valueOf(2));
				edit(enwarhouseOrder);
			}
		} catch (Exception e) {
			throw new RuntimeException(msg);
		}
		return "true";
	}

	@Transactional
	public String giftReviewedAll(String[] ArrayDATA_IDS) {
		String msg = "";
		try {
			PageData enwarhouseOrder = null;
			PageData productInvertory = null;
			PageData enOrderItem = null;
			for (String id : ArrayDATA_IDS) {
				enwarhouseOrder = new PageData();
				enwarhouseOrder.put("id", id);

				enwarhouseOrder = (PageData) this.dao.findForObject(
						"EnWarehouseOrderMapper.findById", enwarhouseOrder);

				List<PageData> enOrderItemList = (List) this.dao.findForList(
						"ENOrderItemMapper.findOrderItemByOrderNum",
						enwarhouseOrder);

				for (PageData page : enOrderItemList) {
					long quantity = Math.round(Double.parseDouble(page.get(
							"quantity").toString()));
					long finalQuantity = Math.round(Double.parseDouble(page
							.get("final_quantity").toString()));
					productInvertory = new PageData();
					productInvertory.put("productId", page.get("product_id"));
					productInvertory.put("newQuantity",
							page.get("final_quantity"));
					productInvertory.put("warehouseId", page.get("is_ivt_BK"));
					productInvertory.put("ck_id",
							enwarhouseOrder.getString("ck_id"));
					productInvertory.put("order_num",
							enwarhouseOrder.getString("order_num"));
					enOrderItem = new PageData();
					enOrderItem.put("id", page.get("id"));
					enOrderItem.put("is_split_ivt", Integer.valueOf(1));
					this.productinventoryService
							.giftUpdateProductinventoryAdd(productInvertory);
					this.enOrderItemService.editState(enOrderItem);
					if (quantity <= finalQuantity) {
						distributionExOrderItem(page);
					} else {
						msg = "入库数不能小于采购数量";
						throw new RuntimeException();
					}
				}
				enwarhouseOrder.remove("ivt_state");
				enwarhouseOrder.put("ivt_state", Integer.valueOf(2));
				edit(enwarhouseOrder);
			}
		} catch (Exception e) {
			throw new RuntimeException(msg);
		}
		return "true";
	}

	@Transactional(rollbackFor = { Exception.class })
	public boolean purchaseeviewedAll(String[] ArrayDATA_IDS, String orderMsg,
			String productMsg) throws Exception {
		for (String id : ArrayDATA_IDS) {
			PageData enwarhouseOrder = new PageData();
			enwarhouseOrder.put("id", id);
			enwarhouseOrder = (PageData) this.dao.findForObject(
					"EnWarehouseOrderMapper.findById", enwarhouseOrder);

			List<PageData> enOrderItemList = (List) this.dao.findForList(
					"ENOrderItemMapper.findOrderItemByOrderNum",
					enwarhouseOrder);
			if (purchaseProduct(enOrderItemList, orderMsg, productMsg)) {
				enwarhouseOrder.remove("ivt_state");
				enwarhouseOrder.put("ivt_state", Integer.valueOf(2));
				if (edit(enwarhouseOrder) <= 0) {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	private boolean purchaseProduct(List<PageData> pageData, String orderMsg,
			String productMsg) throws Exception {
		for (PageData page : pageData) {
			long quantity = Math.round(Double.parseDouble(page.get("quantity")
					.toString()));
			long finalQuantity = Math.round(Double.parseDouble(page.get(
					"final_quantity").toString()));
			PageData productInvertory = new PageData();
			productInvertory.put("productId", page.get("product_id"));
			productInvertory.put("newQuantity", page.get("final_quantity"));
			productInvertory.put("warehouseId", page.get("is_ivt_BK"));
			PageData enOrderItem = new PageData();
			enOrderItem.put("id", page.get("id"));
			enOrderItem.put("is_split_ivt", Integer.valueOf(2));

			if (!this.productinventoryService
					.updateProductinventoryReduce(productInvertory)) {
				PageData product = this.productService.findNameNumById(Long
						.parseLong(page.get("product_id").toString()));
				productMsg = product.getString("product_name");
				orderMsg = page.getString("order_num");
				throw new Exception();
			}
			if (this.enOrderItemService.editState(enOrderItem) <= 0) {
				return false;
			}
		}

		return true;
	}

	/** 修改出库单数量，修改重量体积价格 **/
	private boolean distributionExOrderItem(PageData page) {
		boolean result = true;
		long finalQuantity = Math.round(Double.parseDouble(page.get(
				"final_quantity").toString()));
		try {
			/** 根据商品ID 以及批次号 查询,所对应的出库单行信息 **/
			List<PageData> eXorderItems = this.eXOrderItemService
					.getExOrderItemByProductAndGroupNum(page);
			if (eXorderItems.size() != 0) {
				long remainderQuantity = finalQuantity;
				long lineFinalQuantity = 0L;
				for (PageData eXOrderLine : eXorderItems) {
					// 出库单数量
					lineFinalQuantity = Math.round(Double
							.parseDouble(eXOrderLine.get("final_quantity")
									.toString()));

					if (remainderQuantity - lineFinalQuantity > 0L) {// 入库数量-出库数量>0
						remainderQuantity = remainderQuantity
								- lineFinalQuantity;
					} else if (remainderQuantity - lineFinalQuantity < 0L) {// 入库数量-出库数量<0
						eXOrderLine.put("final_quantity",
								Long.valueOf(remainderQuantity));
						remainderQuantity = 0L;
					} else if (remainderQuantity == 0L) {// 入库数量=0
						eXOrderLine.put("final_quantity", Integer.valueOf(0));
					}
					this.eXOrderItemService.editQuantity(eXOrderLine);// 修改商品数量

					long updateQuantity = Math.round(Double
							.parseDouble(eXOrderLine.get("final_quantity")
									.toString())); // 出库数量
					long reduceQuantity = lineFinalQuantity - updateQuantity;
					PageData exwarhouse = new PageData();
					exwarhouse.put("orderNum", eXOrderLine.get("order_num"));
					exwarhouse = this.exWarehouseOrderService
							.getExwarouseById(exwarhouse);
					long productId = Long.parseLong(eXOrderLine.get(
							"product_id").toString());
					PageData product = this.productService
							.findProductTZById(productId);
					double sku_weight = Double.parseDouble(product.get(
							"sku_weight").toString());
					double sku_volume = Double.parseDouble(product.get(
							"sku_volume").toString());
					double productPrice = Double.parseDouble(product.get(
							"product_price").toString());
					double reduceWeight = NumberUtil.mul(sku_weight,
							reduceQuantity);
					double reduceVolume = NumberUtil.mul(sku_volume,
							reduceQuantity);
					double reducePrice = NumberUtil.mul(productPrice,
							reduceQuantity);
					double total_svolume = Double.parseDouble(exwarhouse.get(
							"total_svolume").toString());
					double total_weight = Double.parseDouble(exwarhouse.get(
							"total_weight").toString());
					double final_amount = Double.parseDouble(exwarhouse.get(
							"final_amount").toString());
					exwarhouse.put("total_svolume", Double.valueOf(NumberUtil
							.sub(total_svolume, reduceVolume)));
					exwarhouse.put("total_weight", Double.valueOf(NumberUtil
							.sub(total_weight, reduceWeight)));
					exwarhouse.put("final_amount", Double.valueOf(NumberUtil
							.sub(final_amount, reducePrice)));
					this.exWarehouseOrderService.editEx(exwarhouse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
			// return false;
		}

		return result;
	}

	public void readExcel(String filepath, String filename) throws Exception {
		List<PageData> listPd = (List) ObjectExcelRead.readExcel(filepath,
				filename, 1, 0, 0);
		if (listPd.size() > 0) {
			List<PageData> templist = new ArrayList();
			for (int i = 0; i < listPd.size(); i++) {
				Product product = this.productService
						.findProductByBarCode(((PageData) listPd.get(i))
								.getString("var0"));
				PageData pageData = new PageData();
				if (product != null) {
					pageData.put("excelrow", Integer.valueOf(i));
					pageData.put("barcode",
							((PageData) listPd.get(i)).get("var0"));
					pageData.put("productname", product.getProductName());
					pageData.put("quantity",
							((PageData) listPd.get(i)).get("var4"));
					pageData.put("type", "1");
					pageData.put("productid", Long.valueOf(product.getId()));
					pageData.put("reason", null);
				} else {
					pageData.put("excelrow", Integer.valueOf(i));
					pageData.put("barcode",
							((PageData) listPd.get(i)).get("var0"));
					pageData.put("productname", "");
					pageData.put("quantity",
							((PageData) listPd.get(i)).get("var4"));
					pageData.put("type", "0");
					pageData.put("productid", "");
					pageData.put("reason", null);
				}
				templist.add(pageData);
			}
			this.productinventoryService.crateTempTable();
			this.productinventoryService.tempsave(templist);
		}
	}

	public List<PageData> getExcel(String id) throws Exception {
		return (List) this.dao.findForList("EnWarehouseOrderMapper.getexcel",
				id);
	}

	public PageData getExcelCount(String id) throws Exception {
		return (PageData) this.dao.findForObject(
				"EnWarehouseOrderMapper.getexcelcount", id);
	}

	public PageData getEnwarouseById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject(
				"EnWarehouseOrderMapper.getEnwarouseById", pd);
	}

	public List<PageData> fencanglist(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EnWarehouseOrderMapper.fencanglistPage", page);
	}
}
