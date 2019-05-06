package com.hy.service.product;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.product.Product;
import com.hy.entity.system.User;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.util.DateUtil;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;
import com.hy.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("productService")
public class ProductService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource
	private CargoSpaceService cargoSpaceService;
	@Resource
	private ProductpriceService productpriceService;
	@Resource
	ProductinventoryService productinventoryService;
	@Resource
	private SupplierService supplierService;
	
	public void saveActivityproduct(PageData pd) throws Exception{
		dao.save("ProductMapper.saveActivityproduct",pd);
	}
	
	public void updateActivityproduct(PageData pd) throws Exception{
		dao.update("ProductMapper.updateActivityproduct",pd);
	}
	
	/**
	 * 查询活动商品
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getActivityproductlist(PageData pd) throws Exception{
		return  (List<PageData>) dao.findForList("ProductMapper.getActivityproductlist", pd);
	}
	
	public void updateActivityState(PageData pd) throws Exception{
		dao.update("ProductMapper.updateProductstate",pd);
	}
	
	/**
	 * 根据供应商查询商品
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> saleReturnProductlist(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ProductMapper.saleReturnProductlist", pd);
	}
	
	public PageData handheldCargoSpace(PageData pd) throws Exception {

		return (PageData) this.dao.findForObject("ProductMapper.handheldCargoSpace", pd);
	}

	public PageData handheldfindProductScancode(PageData pd) throws Exception {

		return (PageData) this.dao.findForObject("ProductMapper.handheldfindProductScancode", pd);
	}

	public List<PageData> dataCenterProductlistPage(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.dataCenterProductlistPage", page);
	}

	public List<PageData> listPdPageInventoryOfProduct(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.inventoryOfProcductlistPage", page);
	}

	public PageData findNameNumById(long id) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findNameNumById", Long.valueOf(id));
	}

	public List<PageData> listPdPageProduct(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.procductlistPage", page);
	}
	/**
	 * 查询商品库存详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> fundGroupProduct(PageData pd) throws Exception{
		return (List) this.dao.findForList("ProductMapper.fundGroupProduct", pd);
	}
	
	/**
	   * 根据库存编号 查询出 2/1 3/1 4/1 
	   * @param productNum
	   * @return
	   * @throws Exception
	   */
	  public PageData getInventoryCount(PageData pd) throws Exception{
			return  (PageData) dao.findForObject("ProductMapper.getInventoryCount", pd);
	  }
	  
	public void save(PageData pd) throws Exception {
		pd.put("cargo_space_num", Integer.valueOf(1));
		this.cargoSpaceService.save(pd);
		int csid = Integer.valueOf((String) pd.get("id")).intValue();
		pd.put("id", "");
		pd.put("PRODUCT_NUM", "PRD" + StringUtil.getStringOfMillisecond(""));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pd.put("CREATE_TIME", df.format(new Date()));
		pd.put("IS_SHELVE", Integer.valueOf(1));
		PageData packing = new PageData();
		packing.put("meterage_unit_id", pd.get("UNIT"));
		packing.put("parent_id", "1");
		packing.put("packing_util_num", pd.get("box_number"));
		packing.put("is_choosed", "1");
		this.dao.save("ProductMapper.saveProductPacking", packing);
		pd.put("CREATOR", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		pd.put("packing_measurement_id", packing.get("id"));
		pd.put("HOST_CODE", pd.get("HOST_CODE"));
		if ((pd.getString("HOST_CODE") != null) && (pd.getString("HOST_CODE").toString().length() > 0)
				&& (pd.getString("HOST_CODE").toString().length() < 5)) {
			StringBuffer sb = new StringBuffer();
			for (int lei = 1; lei < 5 - pd.getString("HOST_CODE").toString().length() + 1; lei++) {
				sb.append("0");
			}
			sb.append(pd.getString("HOST_CODE"));
			pd.put("HOST_CODE", sb.toString());
		}
		pd.put("unit", pd.get("meterage_unit_id"));
		pd.put("MIN_STOCK_NUM", Integer.valueOf(1));
		pd.put("MAX_STOCK_NUM", Integer.valueOf(100));
		pd.put("WEIGHT_UNIT", "千克");
		pd.put("TAXES", "1");
		pd.put("STORE_METHOD", "常温保存");
		pd.put("STORE_METHOD_CONSULTING_TELEPHONE", "13899996666");
		pd.put("USAGE_MODE", "即食");
		pd.put("ORIGIN_PLACE", "中国");
		pd.put("REMARKS", "新增");
		pd.put("VOLUME_UNIT", "立方米");
		this.dao.save("ProductMapper.save", pd);

		String productid = (String) pd.get("id");

		PageData supplierPrice = new PageData();
		supplierPrice.put("relation_id", pd.get("supplierid1"));
		supplierPrice.put("product_price", pd.get("supplierprice1"));
		supplierPrice.put("product_id", productid);
		supplierPrice.put("price_type", "1");
		supplierPrice.put("supplier_priority", "1");
		this.productpriceService.save(supplierPrice);

		PageData sellingprice = new PageData();
		sellingprice.put("relation_id", 0);
		sellingprice.put("product_price", pd.get("price1"));
		sellingprice.put("product_id", productid);
		sellingprice.put("price_type", "2");
		sellingprice.put("supplier_priority", "99");
		this.productpriceService.save(sellingprice);

		PageData productinventoty = new PageData();
		productinventoty.put("product_id", productid);
		productinventoty.put("warehouse_id", pd.get("WAREHOUSE_ID"));
		productinventoty.put("cargo_space_id", Integer.valueOf(csid));
		productinventoty.put("product_quantity", "0");
		productinventoty.put("state", "1");
		this.productinventoryService.save(productinventoty);
	}

	public void saveOfP(PageData pd) throws Exception {
		/*this.cargoSpaceService.save(pd);
		int csid = Integer.valueOf((String) pd.get("id")).intValue();*/
		PageData Cargo = cargoSpaceService.findCargo(pd);
		if (null == Cargo) {// 更新商品货位号
			Cargo = new PageData();
			Cargo.put("cargo_space_num", "1");
			Cargo.put("zone", pd.get("zone"));
			Cargo.put("storey", pd.get("storey"));
			Cargo.put("storey_num", pd.get("storey_num"));
			cargoSpaceService.saveHandheld(Cargo);
		}
		pd.put("cargo_space_id", Cargo.getString("id"));
		pd.put("id", "");
		pd.put("PRODUCT_NUM", pd.getString("product_num"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pd.put("CREATE_TIME", df.format(new Date()));
		pd.put("IS_SHELVE", Integer.valueOf(1));
		PageData packing = new PageData();
		packing.put("meterage_unit_id", pd.get("meterage_unit_id"));  //单位id
		packing.put("parent_id", "1");
		packing.put("packing_util_num", pd.get("box_number"));  //箱装数
		packing.put("is_choosed", "1");
		this.dao.save("ProductMapper.saveProductPacking", packing);
		pd.put("CREATOR", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		pd.put("packing_measurement_id", packing.get("id"));
		pd.put("host_code",  pd.getString("product_num"));
		/*if ((pd.getString("host_code") != null) && (pd.getString("host_code").toString().length() > 0)
				&& (pd.getString("host_code").toString().length() < 5)) {
			StringBuffer sb = new StringBuffer();
			for (int lei = 1; lei < 5 - pd.getString("host_code").toString().length() + 1; lei++) {
				sb.append("0");
			}
			sb.append(pd.getString("host_code"));
			pd.put("host_code", sb.toString());
		}*/
		pd.put("unit", pd.get("meterage_unit_id"));
		pd.put("MIN_STOCK_NUM", Integer.valueOf(1));
		pd.put("MAX_STOCK_NUM", Integer.valueOf(100));
		pd.put("WEIGHT_UNIT", "千克");
		pd.put("TAXES", "1");
		pd.put("STORE_METHOD", "常温保存");
		pd.put("STORE_METHOD_CONSULTING_TELEPHONE", "13899996666");
		pd.put("USAGE_MODE", "即食");
		pd.put("ORIGIN_PLACE", "中国");
		pd.put("REMARKS", "批量导入");
		pd.put("VOLUME_UNIT", "立方米");
		this.dao.save("ProductMapper.saveOfp", pd);

		String productid = (String) pd.get("id");

		PageData supplierPrice = new PageData();
		supplierPrice.put("relation_id", pd.get("sup_id"));
		supplierPrice.put("product_price", pd.get("pur_price"));
		supplierPrice.put("product_id", productid);
		supplierPrice.put("price_type", "1");
		supplierPrice.put("supplier_priority", "1");
		this.productpriceService.save(supplierPrice);

		PageData sellingprice = new PageData();
		sellingprice.put("relation_id", "0");
		sellingprice.put("product_price", pd.get("sale_price"));
		sellingprice.put("product_id", productid);
		sellingprice.put("price_type", "2");
		sellingprice.put("supplier_priority", "99");
		this.productpriceService.save(sellingprice);

		/*PageData productinventoty = new PageData();
		productinventoty.put("product_id", productid);
		productinventoty.put("warehouse_id", Integer.valueOf(1));
		productinventoty.put("cargo_space_id", Integer.valueOf(csid));
		productinventoty.put("product_quantity", "0");
		productinventoty.put("state", "1");
		productinventoty.put("ck_id", LoginUtil.getLoginUser().getCkId());
		this.productinventoryService.save(productinventoty);*/
	}

	public Product findById(PageData pd) throws Exception {
		Product product = new Product();
		return product = (Product) this.dao.findForObject("ProductMapper.findByIdOrBarCode", pd);
	}

	public PageData findProductInfoById(long productId) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findProductInfoById", Long.valueOf(productId));
	}

	public List<PageData> findProductByBarcodeAndHcode(PageData pd) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findProductByBarcodeAndHcode", pd);
	}

	public PageData findPriceOfProduct(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findPriceOfProduct", pd);
	}

	public List<PageData> findPriceOfProductList(PageData pd) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findPriceOfProduct", pd);
	}

	public List<PageData> findPrice2OfProductList(PageData pd) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findPriceOfProduct2", pd);
	}

	public List<PageData> findProductByWarhouseAndQuantitylistPage(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findProductByWarhouseAndQuantitylistPage", page);
	}

	public PageData getproduct(Long productId) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.finProductById", productId);
	}

	public PageData getproduct(PageData productId) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.finProductByIdupdate", productId);
	}

	public PageData findProductTZById(long id) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findProductTZById", Long.valueOf(id));
	}
	public List<PageData> getunitList() throws Exception{
		
		return (List<PageData>) dao.findForList("ProductMapper.findproductunitname", null);
	}	
	public Product findProductByBarCode(PageData pd) throws Exception {
		return (Product) this.dao.findForObject("ProductMapper.findProductByBarCode", pd);
	}
	public PageData findProductByTypeId(String product_type_id) throws Exception {
		PageData pd = new PageData();
		pd.put("product_type_id", product_type_id);
		return (PageData) this.dao.findForObject("ProductMapper.findProductByTypeId", pd);
	}

	public Product findProductByBarCode(String barcode) throws Exception {
		PageData pd = new PageData();
		pd.put("barcode", barcode);
		return (Product) this.dao.findForObject("ProductMapper.findProudctByBarCode", pd);
	}

	public void edit(PageData pd) throws Exception {
		this.dao.update("ProductMapper.edit", pd);
	}
	
	/**
	 * 更新商品日期为最新日期
	 * @param pd
	 * @throws Exception
	 */
	public void updateProductTime(PageData pd) throws Exception{
		dao.update("ProductMapper.updateProductTime", pd);
	}
	
	public void newedit(PageData pd) throws Exception {
		this.dao.update("ProductMapper.newedit", pd);
	}
	public void updateProductPacking(PageData pd) throws Exception {
		this.dao.update("ProductMapper.updateProductPacking", pd);
	}

	public void deleteProudct(PageData pd) throws Exception {
		this.dao.delete("ProductMapper.deleteproduct", pd);
	}

	public void deleteAllProduct(String[] SUPPLE_IDS) throws Exception {
		this.dao.delete("ProductMapper.deleteAllProduct", SUPPLE_IDS);
	}

	public List<PageData> excel(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.excel", page);
	}

	public List<PageData> findProductByTypeId(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findProductlistPage", page);
	}

	public List<PageData> StorageProductByTypeId(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.StorageSelectProductlistPage", page);
	}
	public List<PageData> getProductByIsShelve(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findProductlistisShelvePage", page);
	}
	public List<PageData> getActivityproduct(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.getActivityproductlistPage", page);
	}
	public List<PageData> getManagementproductlistPage(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.getManagementproductlistPage", page);
	}

	public PageData findProductById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findProductById", pd);
	}

	public PageData findQuantityById(String id) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findQuantityById", id);
	}

	public List<PageData> getUnit() throws Exception {
		return (List) this.dao.findForList("ProductMapper.getUnit", null);
	}

	public PageData getProductInfoById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.getProductInfoById", pd);
	}

	public List<PageData> findProudctBytypeId(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.findProudctBytypeId", page);
	}
	/**
	 * 查询活动商品
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getproductactivity(Page page) throws Exception {
		return (List) this.dao.findForList("ProductMapper.getproductActivitylistPage", page);
	}
	
	public List<PageData> findproductActivity(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ProductMapper.findproductActivity",pd);
	}
	
	public PageData findProductScancode(PageData id) throws Exception {
		return (PageData) this.dao.findForObject("ProductMapper.findProductScancode", id);
	}
	
	/**
	 * 导出到excel表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getProductExcelInfo(Page page) throws Exception{
		return (List)dao.findForList("ProductMapper.getProductExcelInfo", page);
	}
	
	/**
	 * 查询商品列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listproduct(Page page) throws Exception{
		return (List<PageData>) dao.findForList("ProductMapper.listProductlistPage", page);
	}
	
	/**
	 * 保存商品信息
	 * @param pd
	 * @throws Exception
	 */
	@Transactional
	public void newsave(PageData pd) throws Exception {
		pd.put("id", "");
		/*pd.put("PRODUCT_NUM", "PRD" + StringUtil.getStringOfMillisecond(""));*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pd.put("CREATE_TIME", df.format(new Date()));
		pd.put("IS_SHELVE", Integer.valueOf(2)); //2为下架
		PageData packing = new PageData();
		packing.put("meterage_unit_id", pd.get("UNIT"));
		packing.put("parent_id", "1");
		packing.put("packing_util_num", pd.get("box_number"));
		packing.put("is_choosed", "1");
		this.dao.save("ProductMapper.saveProductPacking", packing);
		pd.put("CREATOR", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		pd.put("packing_measurement_id", packing.get("id"));
		pd.put("unit", pd.get("meterage_unit_id"));
		pd.put("MIN_STOCK_NUM", Integer.valueOf(1));
		pd.put("MAX_STOCK_NUM", Integer.valueOf(100));
		pd.put("WEIGHT_UNIT", "千克");
		pd.put("TAXES", "1");
		pd.put("STORE_METHOD", "常温保存");
		pd.put("STORE_METHOD_CONSULTING_TELEPHONE", "13899996666");
		pd.put("USAGE_MODE", "即食");
		pd.put("ORIGIN_PLACE", "中国");
		pd.put("REMARKS", "新增");
		pd.put("VOLUME_UNIT", "立方米");
		this.dao.save("ProductMapper.savenew", pd);

		String productid = (String) pd.get("id");

		PageData supplierPrice = new PageData();
		supplierPrice.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
		PageData ps=supplierService.findByGetId(supplierPrice);
		//保存到product_price price_type为1成本价
		if(ps==null){
			supplierPrice.put("relation_id", "0");
			supplierPrice.put("product_price", pd.get("price1"));
			supplierPrice.put("product_id", productid);
			supplierPrice.put("price_type", "1");
			supplierPrice.put("supplier_priority", "1");
			this.productpriceService.save(supplierPrice);
		}else{
			supplierPrice.put("relation_id", ps.get("id"));
			supplierPrice.put("product_price", pd.get("price1"));
			supplierPrice.put("product_id", productid);
			supplierPrice.put("price_type", "1");
			supplierPrice.put("supplier_priority", "1");
			this.productpriceService.save(supplierPrice);
		}

		//保存到product_price price_type为2售出价
		PageData sellingprice = new PageData();
		sellingprice.put("relation_id", 0);
		sellingprice.put("product_price", "0");
		sellingprice.put("product_id", productid);
		sellingprice.put("price_type", "2");
		sellingprice.put("supplier_priority", "99");
		this.productpriceService.save(sellingprice);

		//保存一条商品信息到库存表
		PageData productinventoty = new PageData();
		productinventoty.put("product_id", productid);
		productinventoty.put("warehouse_id",1);
		productinventoty.put("cargo_space_id", Integer.valueOf(1));
		productinventoty.put("product_quantity", "0");
		productinventoty.put("state", "1");
		productinventoty.put("ck_id", LoginUtil.getLoginUser().getCkId());
		this.productinventoryService.save(productinventoty);
	}
	
	/**
	 * 审核活动订单
	 * 更新活动生效日期，更新生效状态
	 * @param ida
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public String shenheactivity(String [] ida) throws Exception{
		PageData pdd=null;
		String result="false";
		for (String id : ida) {
			pdd=new PageData();
			PageData pd=new PageData();
			pd.put("id", id);
			pd=findproductActivity(pd).get(0);
			if(Integer.parseInt(pd.getString("type"))==1){
			pdd.put("id", id);
			pdd.put("state",1);
			dao.update("ProductMapper.shenheactivity", pdd);
			result="true";
			}
		}
		return result;
	}
	
}
