package com.hy.controller.product;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.system.User;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.inventory.WarehouseService;
import com.hy.service.product.CargoSpaceService;
import com.hy.service.product.ProductService;
import com.hy.service.product.ProductTypeService;
import com.hy.service.product.ProductpriceService;
import com.hy.service.product.StageNumberService;
import com.hy.service.product.SupplierService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.AppUtil;
import com.hy.util.FileDownload;
import com.hy.util.FileUpload;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcel;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import com.hy.util.StringUtil;

@Controller
@RequestMapping({ "/product" })
public class ProductController extends BaseController {
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "productTypeService")
	private ProductTypeService productTypeService;
	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;
	@Resource
	private CargoSpaceService cargoSpaceService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private ProductpriceService productpriceService;
	@Resource
	private ProductinventoryService productinventoryService;
	@Resource(name = "sysLogService")
	private SysLogService sysLogService;
	@Resource(name="stageNumberService")
	private StageNumberService stageNumberService;
	
	/**
	 * 销量管理
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/getManagementproduct" })
	public ModelAndView getManagementproduct(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		List<PageData> productList = this.productService.getManagementproductlistPage(page);
		mv.setViewName("procurement/product/product_management");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**
	 * 活动商品
	 * @return
	 */
	@RequestMapping(value = "/productActivity")
	public ModelAndView productActivity(Page page) {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "进入到商品活动页面";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			String state=pd.getString("state");
			if(state!=null && !"".equals(state)){
				pd.put("state", state);
			}
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			page.setPd(pd);
			List<PageData> listpd=productService.getproductactivity(page);
			mv.setViewName("procurement/product/product_activity");
			mv.addObject("varlist",listpd);
			mv.addObject("pd",pd);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mv;
	}

	@RequestMapping({"/productActivityedit"})
	  public ModelAndView goAddU()
	    throws Exception
	  {
	    ModelAndView mv = getModelAndView();
	    PageData pd = new PageData();
	    pd = getPageData();
	    List<PageData> listpd=stageNumberService.stageNumberlist(pd);
	    if(pd.containsKey("id")&&!pd.getString("id").equals("")){
	    	pd=productService.findproductActivity(pd).get(0);
	    	 mv.addObject("pd", pd);
	    	 mv.addObject("listpd", listpd);
	    }else{
	    	mv.addObject("pd", pd);
	    	mv.addObject("listpd", listpd);
	    }
	    
	    mv.setViewName("procurement/product/product_activity_edit");
	    return mv;
	  }
	
	
	@RequestMapping({ "/getActivityproduct" })
	public ModelAndView getActivityproduct(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
		page.setPd(pd);
		List<PageData> productList = this.productService.getActivityproduct(page);
		mv.setViewName("inventorymanagement/entempwarehouseorder/select_product");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 保存活动商品
	 * @param out
	 * @return
	 * @throws Exception
	 */
	  @RequestMapping({"/saveActivityproduct"})
	  public ModelAndView saveActivityproduct(PrintWriter out)
	    throws Exception
	  {
	    ModelAndView mv = getModelAndView();
	    PageData pd = new PageData();
	    pd = getPageData();
	    try {
	    	 if(pd.containsKey("id")&&!pd.getString("id").equals("")){
	    		productService.updateActivityproduct(pd);
	    	}else{
	    		//查询活动商品 
	    		List<PageData> listpd=productService.getActivityproductlist(pd);
	    		PageData pdd=null;
	    		if(listpd.size()>0 && listpd!=null) {
	    			for (int i = 0; i < listpd.size(); i++) {
						pdd=new PageData();
						pdd.put("product_id", listpd.get(i).getString("product_id"));
						productService.updateActivityState(pdd); //更新生效状态和活动状态
					}
	    			pd.put("creator",LoginUtil.getLoginUser().getUSERNAME());
			    	productService.saveActivityproduct(pd); 
	    		}else{
	    			pd.put("creator",LoginUtil.getLoginUser().getUSERNAME());
			    	productService.saveActivityproduct(pd); 
	    		}
	    		/*if(pd.getString("product_id").equals(listpd.getString("product_id"))){
	    			
	    		}*/
	    	}
	 	    mv.addObject("msg", "success");
		} catch (Exception e) {
			mv.addObject("msg", "failed");
		}
	    mv.setViewName("save_result");
	    return mv;
	  }

	  
	  
	
	/**
	 * 进入到商品导入页面
	 */
	@RequestMapping(value = "/goCommodityExcel")
	public ModelAndView goCommodityExcel() {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "进入到商品导入页面";
		mv.setViewName("procurement/product/importCommodity");
		return mv;
	}
	/****上传图片*****/
	  @RequestMapping(value = { "/uploadimg" }, method = RequestMethod.POST)
	  @ResponseBody
		public String uploadimg(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
			String result = "";
			logBefore(this.logger, "上传图片操作人："+LoginUtil.getLoginUser().getUSERNAME());
			try {
				//如果文件不为空，写入上传路径
		        if(!file.isEmpty()) {
		            //上传文件路径
		            String path = request.getServletContext().getRealPath("/static/uploadimg/");
		            //上传文件名
		            String filename = file.getOriginalFilename();
		            File filepath = new File(path,filename);
		            //判断路径是否存在，如果不存在就创建一个
		            if (!filepath.getParentFile().exists()) { 
		                filepath.getParentFile().mkdirs();
		            }
		            String productName=StringUtil.getStringOfMillisecond("")+filename.substring(filename.length()- 4,filename.length());
		           // //System.out.println(productName);
		            //将上传文件保存到一个目标文件当中
		            file.transferTo(new File(path + File.separator +productName));
		            return "static/uploadimg/"+productName;
		        } else {
		            return "error";
		        }
			} catch (Exception e) {
				this.logger.error(e.toString(), e);
				result = e.toString();
			} finally {
				logAfter(this.logger);
			}
			return result;
		}
	@RequestMapping({ "/findProudctBytypeId" })
	public ModelAndView findProudctBytypeId(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		if (pd.get("typeId") == null) {
			pd.put("typeId", "0");
		}
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		List<PageData> productList = this.productService.findProudctBytypeId(page);
		mv.setViewName("inventorymanagement/extempwarehouseorder/select_product_by_warehouse");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	

	@RequestMapping({ "findProductByWarhouseAndQuantitylistPage" })
	public ModelAndView findProductByWarhouseAndQuantitylistPage(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		List<PageData> productList = this.productService.findProductByWarhouseAndQuantitylistPage(page);
		mv.setViewName("inventorymanagement/extempwarehouseorder/select_product_by_warehouseId");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping({ "/getProductByIsShelve" })
	public ModelAndView getProductByIsShelve(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		if (pd.get("typeId") == null) {
			pd.put("typeId", "0");
		}
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		if (LoginUtil.getLoginUser().getROLE_ID().equals("24")) {
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
		}
		page.setPd(pd);
		List<PageData> productList = this.productService.getProductByIsShelve(page);
		mv.setViewName("inventorymanagement/entempwarehouseorder/select_product_is_shelve");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}
	@RequestMapping({ "/getProductByTypeId" })
	public ModelAndView getProductByTypeId(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		if (pd.get("typeId") == null) {
			pd.put("typeId", "0");
		}
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		if (LoginUtil.getLoginUser().getROLE_ID().equals("24")) {
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
		}
		page.setPd(pd);
		List<PageData> productList = this.productService.findProductByTypeId(page);
		mv.setViewName("inventorymanagement/entempwarehouseorder/select_product");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping({ "/getStorageProductByTypeId" })
	public ModelAndView getStorageProductByTypeId(Page page) throws Exception {
		logBefore(this.logger, "商品列表");

		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		if (pd.get("typeId") == null) {
			pd.put("typeId", "0");
		}
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		List<PageData> productList = this.productService.StorageProductByTypeId(page);
		mv.setViewName("inventorymanagement/entempwarehouseorder/select_product");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping({ "/listProducts" })
	public ModelAndView list(Page page) throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
		page.setPd(pd);
		List<PageData> productList = this.productService.listPdPageProduct(page);
		mv.setViewName("procurement/product/product_list");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 商品查询
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "/listProduct" })
	public ModelAndView listProduct(Page page) throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
		pd.put("ROLE_ID",LoginUtil.getLoginUser().getROLE_ID());
		pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
		page.setPd(pd);
		List<PageData> productList = this.productService.listPdPageProduct(page);
		mv.setViewName("suppliermanagement/product_list");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping({ "/goEditP" })
	public ModelAndView goEditP() {
		logBefore(this.logger, "去修改Product页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			// String productid = (String)pd.get("productId");
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			pd.put("ROLE_ID",LoginUtil.getLoginUser().getROLE_ID());
			PageData proudct = this.productService.getproduct(pd);
			List<PageData> warehouseList = this.warehouseService.listAll(pd);
			mv.setViewName("suppliermanagement/product_edit");
			mv.addObject("msg", "edits");
			mv.addObject("product", proudct);
			mv.addObject("pd",pd);
			mv.addObject("warehouseList", warehouseList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({ "/edits" })
	public ModelAndView edits() throws Exception {
		logBefore(this.logger, "修改商品");
		ModelAndView mv = getModelAndView();

		PageData pd = new PageData();
		pd = getPageData();

		double fclVolume = Double.valueOf((String) pd.get("FCL_VOLUME")).doubleValue();

		double boxnumber = Double.valueOf((String) pd.get("box_number")).doubleValue();
		if ((fclVolume != 0.0D) && (boxnumber != 0.0D)) {
			double skuVolume = fclVolume / boxnumber;
			double f = skuVolume;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("sku_volume", Double.valueOf(f1));
		}

		double fcl_weight = Double.valueOf((String) pd.get("FCL_WEIGHT")).doubleValue();

		if ((fcl_weight != 0.0D) && (fcl_weight != 0.0D)) {
			double sku_weight = fcl_weight / boxnumber;
			double f = sku_weight;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("sku_weight", Double.valueOf(f1));
		}
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
		pd.put("CREATOR", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		pd.put("MIN_STOCK_NUM", Integer.valueOf(1));
		pd.put("MAX_STOCK_NUM", Integer.valueOf(100));
		pd.put("WEIGHT_UNIT", "千克");
		pd.put("TAXES", "1");
		pd.put("STORE_METHOD", "常温保存");
		pd.put("STORE_METHOD_CONSULTING_TELEPHONE", "13899996666");
		pd.put("USAGE_MODE", "即食");
		pd.put("ORIGIN_PLACE", "中国");
		pd.put("VOLUME_UNIT", "立方米");
		pd.put("product_num", pd.getString("product_num"));
		String prodictid = (String) pd.get("id");
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
		/*Cargo.put("product_id", pd.get("id"));
		Cargo.put("ck_id", LoginUtil.getLoginUser().getCkId());
		Cargo.put("warehouse_id", pd.get("WAREHOUSE_ID"));*/
		/*PageData Prod = productinventoryService.findProductinverCo(Cargo);
		if (null == Prod) {
			Cargo.put("cargo_space_id", Cargo.get("id"));
			Cargo.put("product_quantity", 0);
			Cargo.put("state", 1);
			productinventoryService.save(Cargo);
		} else {
			productinventoryService.updateProductinverCo(Cargo);
		}*/
		//this.cargoSpaceService.edit(pd);
		
		this.productService.edit(pd);
		this.productService.updateProductPacking(pd);

		PageData sellingPrice = new PageData();
		sellingPrice = this.productpriceService.getSellingPriceById(prodictid);
		sellingPrice.put("relation_id", "1");
		sellingPrice.put("product_price", pd.get("price1"));
		this.productpriceService.edit(sellingPrice);

		PageData supplierPrice = new PageData();
		supplierPrice = this.productpriceService.getSupplierPriceById(prodictid);
		supplierPrice.put("relation_id", pd.get("supplierid1"));
		supplierPrice.put("product_price", pd.get("supplierprice1"));
		this.productpriceService.edit(supplierPrice);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		this.sysLogService.saveLog("修改Supplier", "成功");
		return mv;
	}
	
	@RequestMapping({ "/getProductType" })
	@ResponseBody
	public Object getProductType() throws Exception {
		JSONObject json = new JSONObject();
		PageData pd = new PageData();
		pd = getPageData();
		List<PageData> productType = this.productTypeService.findByParentId(pd);
		json.put("productType", productType);
		return json;
	}

	@RequestMapping({ "/goAddProduct" })
	public ModelAndView goAddProduct() throws Exception {
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		pd.put("parent_id", Integer.valueOf(0));

		List<PageData> productType = this.productTypeService.findByParentId(pd);

		List<PageData> warehouseList = this.warehouseService.listAll(pd);

		List<PageData> unitList = this.productService.getUnit();
		mv.addObject("productType", productType);
		mv.addObject("warehouseList", warehouseList);
		mv.addObject("unitList", unitList);
		mv.setViewName("procurement/product/product_edit");
		mv.addObject("msg", "saveProduct");
		mv.addObject("pd", pd);

		return mv;
	}

	@RequestMapping({ "/saveProduct" })
	public ModelAndView saveProduct( @RequestParam(value="file",required=false) MultipartFile file) throws Exception {
		logBefore(this.logger, "新增商品");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();

		double fclVolume = Double.valueOf((String) pd.get("FCL_VOLUME")).doubleValue();

		double boxnumber = Double.valueOf((String) pd.get("box_number")).doubleValue();
		if ((fclVolume != 0.0D) && (boxnumber != 0.0D)) {
			double skuVolume = fclVolume / boxnumber;
			double f = skuVolume;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("SKU_VOLUME", Double.valueOf(f1));
		}

		double fcl_weight = Double.valueOf((String) pd.get("FCL_WEIGHT")).doubleValue();

		if ((fcl_weight != 0.0D) && (fcl_weight != 0.0D)) {
			double sku_weight = fcl_weight / boxnumber;
			double f = sku_weight;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("SKU_WEIGHT", Double.valueOf(f1));
		}
		try {
			this.productService.save(pd);
			mv.addObject("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.sysLogService.saveLog("新增商品", "成功");
		return new ModelAndView("redirect:/product/listProducts.do");
	}

	@RequestMapping({ "/goEdit" })
	public ModelAndView goEdit() {
		logBefore(this.logger, "去修改Product页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			// String productid = (String)pd.get("productId");
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			PageData proudct = this.productService.getproduct(pd);
			List<PageData> warehouseList = this.warehouseService.listAll(pd);
			List<PageData> unitList = productService.getunitList();
			mv.setViewName("procurement/product/product_edit");
			mv.addObject("msg", "edit");
			mv.addObject("product", proudct);
			mv.addObject("unitList",unitList);
			mv.addObject("warehouseList", warehouseList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/edit" })
	public ModelAndView edit() throws Exception {
		logBefore(this.logger, "修改商品");
		ModelAndView mv = getModelAndView();

		PageData pd = new PageData();
		pd = getPageData();

		double fclVolume = Double.valueOf((String) pd.get("FCL_VOLUME")).doubleValue();

		double boxnumber = Double.valueOf((String) pd.get("box_number")).doubleValue();
		if ((fclVolume != 0.0D) && (boxnumber != 0.0D)) {
			double skuVolume = fclVolume / boxnumber;
			double f = skuVolume;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			pd.put("sku_volume", Double.valueOf(f1));
		}

		double fcl_weight = Double.valueOf((String) pd.get("FCL_WEIGHT")).doubleValue();

		if ((fcl_weight != 0.0D) && (fcl_weight != 0.0D)) {
			double sku_weight = fcl_weight / boxnumber;
			double f = sku_weight;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
			pd.put("sku_weight", Double.valueOf(f1));
		}
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
		pd.put("CREATOR", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		pd.put("MIN_STOCK_NUM", Integer.valueOf(1));
		pd.put("MAX_STOCK_NUM", Integer.valueOf(100));
		pd.put("WEIGHT_UNIT", "千克");
		pd.put("TAXES", "1");
		pd.put("STORE_METHOD", "常温保存");
		pd.put("STORE_METHOD_CONSULTING_TELEPHONE", "13899996666");
		pd.put("USAGE_MODE", "即食");
		pd.put("ORIGIN_PLACE", "中国");
		pd.put("VOLUME_UNIT", "立方米");
		String prodictid = (String) pd.get("id");
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
		/*Cargo.put("product_id", pd.get("id"));
		Cargo.put("ck_id", LoginUtil.getLoginUser().getCkId());
		Cargo.put("warehouse_id", pd.get("WAREHOUSE_ID"));*/
		/*PageData Prod = productinventoryService.findProductinverCo(Cargo);
		if (null == Prod) {
			Cargo.put("cargo_space_id", Cargo.get("id"));
			Cargo.put("product_quantity", 0);
			Cargo.put("state", 1);
			productinventoryService.save(Cargo);
		} else {
			productinventoryService.updateProductinverCo(Cargo);
		}*/
		//this.cargoSpaceService.edit(pd);
		
		this.productService.edit(pd);
		this.productService.updateProductPacking(pd);

		PageData sellingPrice = new PageData();
		sellingPrice = this.productpriceService.getSellingPriceById(prodictid);
		if(sellingPrice==null){
			sellingPrice=new PageData();
			sellingPrice.put("relation_id", "0");
			sellingPrice.put("product_price", pd.get("price1"));
			sellingPrice.put("product_id", pd.getString("id"));
			sellingPrice.put("price_type", "2");
			sellingPrice.put("supplier_priority", "99");
			productpriceService.save(sellingPrice);
		}else{
			sellingPrice.put("relation_id", "0");
			sellingPrice.put("product_price", pd.get("price1"));
			this.productpriceService.edit(sellingPrice);
		}
		PageData supplierPrice = new PageData();
		supplierPrice = this.productpriceService.getSupplierPriceById(prodictid);
		if(supplierPrice==null){
			supplierPrice = new PageData();
			supplierPrice.put("relation_id", pd.get("supplierid1"));
			supplierPrice.put("product_price", pd.get("supplierprice1"));
			sellingPrice.put("product_id", pd.getString("id"));
			sellingPrice.put("price_type", "1");
			sellingPrice.put("supplier_priority", "1");
			productpriceService.save(sellingPrice);
		}else{
			supplierPrice.put("relation_id", pd.get("supplierid1"));
			supplierPrice.put("product_price", pd.get("supplierprice1"));
			this.productpriceService.edit(supplierPrice);
		}
		
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		this.sysLogService.saveLog("修改Supplier", "成功");
		return mv;
	}

	@RequestMapping({ "/deleteAllProduct" })
	@ResponseBody
	public Object deleteAllSuppler() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		try {
			pd = getPageData();
			List<PageData> pdList = new ArrayList();
			String SUPPLE_IDS = pd.getString("SUPPLE_IDS");
			String csid = pd.getString("csid");
			if ((SUPPLE_IDS != null) && (!"".equals(SUPPLE_IDS))) {
				String[] ArraySUPPLE_IDS = SUPPLE_IDS.split(",");
				String[] Arraycsid = csid.split(",");
				this.productService.deleteAllProduct(ArraySUPPLE_IDS);
				this.cargoSpaceService.deleteAllCargoSpace(Arraycsid);
				pd.put("msg", "ok");
				this.sysLogService.saveLog("批量删除Supplier", "成功");
			} else {
				pd.put("msg", "no");
				this.sysLogService.saveLog("批量删除Supplier", "失败");
			}

			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		} finally {
			logAfter(this.logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	@RequestMapping({ "findProductById" })
	@ResponseBody
	public Object findProductById(Page page) {
		JSONObject json = new JSONObject();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			pd = this.productService.findProductById(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.put("product", pd);
		return json;
	}

	@RequestMapping({ "getProductInfoById" })
	public ModelAndView getProductInfoById(Page page) {
		logBefore(this.logger, "查看商品详细信息");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();

			pd = this.productService.getProductInfoById(pd);

			mv.addObject("product", pd);

			mv.setViewName("procurement/product/product_info");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping({ "/findInventoryOfProduct" })
	public ModelAndView findInventoryOfProduct(Page page) throws Exception {
		logBefore(this.logger, "查询商品库存信息");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
		pd =this.getPageData();
		/*if (!StringUtil.isEmpty(pd.getString("warehouse_id"))) {
			pd.put("warehouse_id", pd.getString("warehouse_id"));
		}*/
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		/*pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
*/		pd.put("warehouse_id", 1);
		page.setPd(pd);
		List<PageData> productList = this.productService.listPdPageInventoryOfProduct(page);
		mv.setViewName("procurement/product/inventoryOfProduct_list");
		mv.addObject("productList", productList);
		mv.addObject("pd", pd);
		} catch (Exception e) {
			// TODO: handle exception
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 导出商品库存到Excel表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView ProductExcel(Page page){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="ProductExcel导出库存操作";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			/*pd.put("ck_id", LoginUtil.getLoginUser().getCkId());*/
			pd.put("warehouse_id", 1);
			page.setPd(pd);
			Map<String,Object> map=new HashMap<String, Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("序号"); // 1
			titles.add("商品条形码"); // 2
			titles.add("商品名称"); // 3
			titles.add("货位"); // 4
			titles.add("商品类别"); // 5
			titles.add("单位"); // 6
			titles.add("安全库存"); // 7
			titles.add("采购价"); // 8
			titles.add("总库存"); // 9
/*			titles.add("库存金额（元）"); // 10
*/			map.put("titles", titles);
			List<PageData> listpd=productService.getProductExcelInfo(page);
			List<PageData> varList=new ArrayList<PageData>();
			for (int i = 0; i < listpd.size(); i++) {
				PageData pdd=new PageData();
				pdd.put("var1", listpd.get(i).getString("id"));
				pdd.put("var2", listpd.get(i).getString("bar_code"));
				pdd.put("var3", listpd.get(i).getString("product_name"));
				pdd.put("var4", listpd.get(i).getString("zone")+"区"+listpd.get(i).getString("storey")+"层"+listpd.get(i).getString("storey_num")+"号");
				pdd.put("var5", listpd.get(i).getString("classify_name"));
				pdd.put("var6", listpd.get(i).getString("unit_name"));
				pdd.put("var7", listpd.get(i).getString("min_stock_num")+"-"+listpd.get(i).getString("max_stock_num"));
				pdd.put("var8", listpd.get(i).getString("product_price"));
				pdd.put("var9", listpd.get(i).getString("sumproduct_quantity"));
/*				pdd.put("var10", listpd.get(i).getString("final_price"));*/
				varList.add(pdd);
			}
			map.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv=new ModelAndView(erv,map);
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	@RequestMapping({ "/goImportExcelPage" })
	public ModelAndView goImportExcelPage() throws Exception {
		ModelAndView mv = getModelAndView();
		mv.setViewName("procurement/product/importExcelPage");
		return mv;
	}

	@RequestMapping({ "/downExcel" })
	public void downExcel(HttpServletResponse response) throws Exception {
		FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "product.xls",
				"Tarot系统-批量导入商品信息模版.xls");
	}

	@RequestMapping({ "/readExcel" })
	public ModelAndView readExcel(@RequestParam(value = "excel", required = false) MultipartFile file)
			throws Exception {
		ModelAndView mv = getModelAndView();
		String operationMsg = "导入商品信息";
		logBefore(logger, operationMsg);
		PageData pd = null;
		try {
			User user = LoginUtil.getLoginUser();

			if ((file != null) && (!file.isEmpty())) {
				String filePath = PathUtil.getClasspath() + "uploadFiles/file/";

				String fileName = FileUpload.fileUp(file, filePath, "sellingOrderExcel");

				List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);

				List<PageData> pdPList = new ArrayList();
				PageData pdP = null;

				PageData pdE = null;
				List<PageData> pdEList = new ArrayList();
				PageData pdA=null;
				PageData pdC=null;
				PageData pdN=null;
				StringBuffer rowMes = null;
				StringBuffer reasonMes = null;
				List<PageData> pdlist = null;
				List<PageData> pdMlist = null;
				List<PageData> pdUlist = null;
				boolean flagP = false;
				boolean flagM = false;
				int rowNumber = 17;
				int box_number = 9999;
				if ((listPd != null) && (listPd.size() > 0)) {
					for (int i = 0; i < listPd.size(); i++) {
						pd=new PageData();
						pdP = new PageData();
						pdE = new PageData();
						pdA=new PageData();
						pdC=new PageData();
						pdN=new PageData();
						pdMlist = new ArrayList();
						pdlist = new ArrayList();
						pdUlist = new ArrayList();
						flagP = false;
						flagM = false;
						box_number = 9999;

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var1").toString())) {
							pdP.put("product_name", ((PageData) listPd.get(i)).get("var1").toString());
							pd.put("product_name", ((PageData) listPd.get(i)).get("var1").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row2", "B");
							pdE.put("reason2", "商品名称数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var2").toString())) {
							pdP.put("box_number", ((PageData) listPd.get(i)).get("var2").toString());
							box_number = Integer.parseInt(((PageData) listPd.get(i)).get("var2").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row3", "C");
							pdE.put("reason3", "商品箱装数数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var3").toString())) {
							pdP.put("product_num", ((PageData) listPd.get(i)).get("var3").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "D");
							pdE.put("reason0", "商品编码数据为空");
						}
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var21").toString())) {
							pdP.put("type", ((PageData) listPd.get(i)).get("var21").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row0", "V");
							pdE.put("reason0", "配送方式不能为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var4").toString())) {
							pdP.put("bar_code", ((PageData) listPd.get(i)).get("var4").toString());
							pdlist = this.productService.findProductByBarcodeAndHcode(pdP);
							if ((pdlist != null) && (pdlist.size() > 0)) {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row4", "D和E");
								pdE.put("reason4", "商品信息已存在");
							} else {
								flagP = true;
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row4", "E");
							pdE.put("reason4", "商品条形码数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var5").toString())) {
							pdP.put("specification", ((PageData) listPd.get(i)).get("var5").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row5", "F");
							pdE.put("reason5", "商品规格数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var6").toString())) {
							pdP.put("fcl_weight", ((PageData) listPd.get(i)).get("var6").toString());
							pdP.put("sku_weight", Double.valueOf(new BigDecimal(
									Double.parseDouble(((PageData) listPd.get(i)).get("var6").toString()) / box_number)
											.setScale(3, 4).doubleValue()));
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row9", "G");
							pdE.put("reason9", "商品整箱重量数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var7").toString())) {
							pdP.put("fcl_volume", ((PageData) listPd.get(i)).get("var7").toString());
							pdP.put("sku_volume", Double.valueOf(new BigDecimal(
									Double.parseDouble(((PageData) listPd.get(i)).get("var7").toString()) / box_number)
											.setScale(3, 4).doubleValue()));
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row8", "H");
							pdE.put("reason8", "商品整箱体积数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var8").toString())) {
							pdP.put("expire_days", ((PageData) listPd.get(i)).get("var8").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row16", "I");
							pdE.put("reason16", "保质期数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var9").toString())) {
							pdP.put("pur_price", ((PageData) listPd.get(i)).get("var9").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row6", "J");
							pdE.put("reason6", "商品采购单价数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var10").toString())) {
							pdP.put("sale_price", ((PageData) listPd.get(i)).get("var10").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row7", "K");
							pdE.put("reason7", "商品订购单价数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var11").toString())) {
							pdP.put("sup_id", ((PageData) listPd.get(i)).get("var11").toString());
							pdMlist = this.supplierService.findSupsById(pdP);
							if ((pdMlist != null) && (pdMlist.size() > 0)) {
								flagM = true;
							} else {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row4", "L");
								pdE.put("reason4", "供应商信息不存在");
							}
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row10", "L");
							pdE.put("reason10", "供应商ID数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var13").toString())) {
							pdP.put("meterage_unit_id", ((PageData) listPd.get(i)).get("var13").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row11", "N");
							pdE.put("reason11", "商品单位ID数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var15").toString())) {
							pdP.put("product_type_id", ((PageData) listPd.get(i)).get("var15").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row12", "P");
							pdE.put("reason12", "商品分类ID数据为空");
						}

						if(!StringUtil.isEmpty(listPd.get(i).getString("var17").toString())) {
							pdP.put("cargo_space_num", listPd.get(i).getString("var17").toString());
						}else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row13", "W");
							pdE.put("reason13", "货位标示号");
						}
						
						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var18").toString())) {
							pdP.put("zone", ((PageData) listPd.get(i)).get("var18").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row14", "S");
							pdE.put("reason14", "商品区数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var19").toString())) {
							pdP.put("storey", ((PageData) listPd.get(i)).get("var19").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row15", "T");
							pdE.put("reason15", "商品层数据为空");
						}

						if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var20").toString())) {
							pdP.put("storey_num", ((PageData) listPd.get(i)).get("var20").toString());
						} else {
							pdE.put("line", Integer.valueOf(i + 2));
							pdE.put("row16", "U");
							pdE.put("reason16", "商品所在层编号数据为空");
						}
						
						
						if ((pdE != null) && (pdE.getString("line") != null) && (pdE.getString("line") != "")) {
							rowMes = new StringBuffer();
							reasonMes = new StringBuffer();
							for (int ri = 0; ri < rowNumber; ri++) {
								if (!StringUtil.isEmpty(pdE.getString("row" + ri))) {
									rowMes.append(pdE.getString(new StringBuilder("row").append(ri).toString()) + "，");
									reasonMes.append(
											pdE.getString(new StringBuilder("reason").append(ri).toString()) + "，");
								}
							}
							pdE.put("row", rowMes);
							pdE.put("reason", reasonMes);

							pdEList.add(pdE);
						}
						pdPList.add(pdP);
					}
					
					if ((pdEList == null) || (pdEList.size() <= 0)) {
						if ((pdPList != null) && (pdPList.size() > 0)) {
							for (int pbpi = 0; pbpi < pdPList.size(); pbpi++) {
								this.productService.saveOfP((PageData) pdPList.get(pbpi));
							}
						}
						else {
							this.logMidway(logger, " 商品数组为空，请检查处理过程");
						}
					}
					/*pd=cargoSpaceService.findCargo(pdA);
					pdC.put("id", pd.get("id"));
					pdN=productService.findProductByTypeId(pdP.getString("product_type_id"));
					if (saveFlag > 1) {
						productService.deleteProudct(pdN);
						
						cargoSpaceService.deleteCargoSpace(pdC);
					}*/
					if ((pdEList != null) && (pdEList.size() > 0) && (pdEList.get(0) != null)
							&& (!StringUtil.isEmpty(((PageData) pdEList.get(0)).getString("line")))) {
						mv.addObject("varList", pdEList);
					}
					if ((pdEList == null) || (pdEList.size() <= 0)
							|| (StringUtil.isEmpty(((PageData) pdEList.get(0)).getString("line")))) {
						pdEList = null;
					}
					if (pdEList == null) {
						mv.addObject("errorMsg", "数据导入成功。");
						mv.addObject("msg", "success");
					} else {
						mv.addObject("errorMsg", "请检查导入Excel表格式或数据是否为空后重新操作。");
					}
					
				} else {
					mv.addObject("errorMsg", "数据表中没有数据");
				}
			}
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mv.addObject("errorMsg", "请检查商品：" + pd.getString("product_name") + "数据是否准确,重新操作。");
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		mv.setViewName("procurement/product/importExcelPage");
		return mv;
	}
	
	/**
	 * 添加新商品
	 * @return
	 */
	@RequestMapping(value="/newproductlist")
	public ModelAndView newproductlist(Page page){
		String operationMsg="新品查询";
		logBefore(logger, operationMsg);
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if(keyword!=null && keyword!=""){
				keyword=keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			pd.put("role_id", LoginUtil.getLoginUser().getROLE_ID());
			page.setPd(pd);
			List<PageData> listpd=productService.listproduct(page);
			mv.addObject("productList", listpd);
			mv.addObject("pd", pd);
			mv.setViewName("procurement/commodityReport/newproduct_list");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mv;
	}
	
	@RequestMapping({ "/gonewEdit" })
	public ModelAndView gonewEdit() {
		logBefore(this.logger, "去修改Product页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			// String productid = (String)pd.get("productId");
			pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
			pd.put("ROLE_ID",LoginUtil.getLoginUser().getROLE_ID());
			PageData proudct = this.productService.getproduct(pd);
			List<PageData> warehouseList = this.warehouseService.listAll(pd);
			mv.setViewName("procurement/commodityReport/newproduct_edit");
			mv.addObject("msg", "newedits");
			mv.addObject("product", proudct);
			mv.addObject("pd",pd);
			mv.addObject("warehouseList", warehouseList);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({ "/newedits" })
	public ModelAndView newedits() throws Exception {
		logBefore(this.logger, "修改商品");
		ModelAndView mv = getModelAndView();

		PageData pd = new PageData();
		pd = getPageData();

		double fclVolume = Double.valueOf((String) pd.get("FCL_VOLUME")).doubleValue();

		double boxnumber = Double.valueOf((String) pd.get("box_number")).doubleValue();
		if ((fclVolume != 0.0D) && (boxnumber != 0.0D)) {
			double skuVolume = fclVolume / boxnumber;
			double f = skuVolume;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("sku_volume", Double.valueOf(f1));
		}

		double fcl_weight = Double.valueOf((String) pd.get("FCL_WEIGHT")).doubleValue();

		if ((fcl_weight != 0.0D) && (fcl_weight != 0.0D)) {
			double sku_weight = fcl_weight / boxnumber;
			double f = sku_weight;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("sku_weight", Double.valueOf(f1));
		}
		pd.put("HOST_CODE", pd.get("product_num"));
		/*if ((pd.getString("HOST_CODE") != null) && (pd.getString("HOST_CODE").toString().length() > 0)
				&& (pd.getString("HOST_CODE").toString().length() < 5)) {
			StringBuffer sb = new StringBuffer();
			for (int lei = 1; lei < 5 - pd.getString("HOST_CODE").toString().length() + 1; lei++) {
				sb.append("0");
			}
			sb.append(pd.getString("HOST_CODE"));
			pd.put("HOST_CODE", sb.toString());
		}*/
		pd.put("CREATOR", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
		pd.put("MIN_STOCK_NUM", Integer.valueOf(1));
		pd.put("MAX_STOCK_NUM", Integer.valueOf(100));
		pd.put("WEIGHT_UNIT", "千克");
		pd.put("TAXES", "1");
		pd.put("STORE_METHOD", "常温保存");
		pd.put("STORE_METHOD_CONSULTING_TELEPHONE", "13899996666");
		pd.put("USAGE_MODE", "即食");
		pd.put("ORIGIN_PLACE", "中国");
		pd.put("VOLUME_UNIT", "立方米");
		pd.put("product_num", pd.getString("product_num"));
		pd.put("newproduct_time", pd.getString("newproduct_time"));
		String prodictid = (String) pd.get("id");
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
		/*Cargo.put("product_id", pd.get("id"));
		Cargo.put("ck_id", LoginUtil.getLoginUser().getCkId());
		Cargo.put("warehouse_id", pd.get("WAREHOUSE_ID"));*/
		/*PageData Prod = productinventoryService.findProductinverCo(Cargo);
		if (null == Prod) {
			Cargo.put("cargo_space_id", Cargo.get("id"));
			Cargo.put("product_quantity", 0);
			Cargo.put("state", 1);
			productinventoryService.save(Cargo);
		} else {
			productinventoryService.updateProductinverCo(Cargo);
		}*/
		//this.cargoSpaceService.edit(pd);
		
		this.productService.newedit(pd);
		this.productService.updateProductPacking(pd);

		PageData sellingPrice = new PageData();
		sellingPrice = this.productpriceService.getSellingPriceById(prodictid);
		sellingPrice.put("relation_id", "0");
		sellingPrice.put("product_price", pd.get("price1"));
		this.productpriceService.edit(sellingPrice);

		PageData supplierPrice = new PageData();
		supplierPrice = this.productpriceService.getSupplierPriceById(prodictid);
		supplierPrice.put("relation_id", pd.get("supplierid1"));
		supplierPrice.put("product_price", pd.get("supplierprice1"));
		this.productpriceService.edit(supplierPrice);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		this.sysLogService.saveLog("修改Supplier", "成功");
		return mv;
	}
	
	/**
	 * 跳转添加新商品
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goAddNewProduct")
	public ModelAndView goAddNewProduct() throws Exception {
		String operationMsg="去新增页面";
		logBefore(logger, operationMsg);
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
		pd = getPageData();
		pd.put("parent_id", Integer.valueOf(0));
		pd.put("ROLE_ID",LoginUtil.getLoginUser().getROLE_ID());
		List<PageData> productType = this.productTypeService.findByParentId(pd);
		List<PageData> warehouseList = this.warehouseService.listAll(pd);
		List<PageData> unitList = this.productService.getUnit();
		mv.addObject("productType", productType);
		mv.addObject("warehouseList", warehouseList);
		mv.addObject("unitList", unitList);
		mv.setViewName("procurement/commodityReport/newproduct_add");
		mv.addObject("msg", "savenewProduct");
		mv.addObject("pd", pd);
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg+"，出现错误："+e.toString());
		}
		return mv;
	}
	/**
	 * 添加新商品
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/savenewProduct")
	public ModelAndView savenewProduct() throws Exception {
		String operationMsg="增加商品";
		logBefore(this.logger, operationMsg);
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
		pd = this.getPageData();

		double fclVolume = Double.valueOf((String) pd.get("FCL_VOLUME")).doubleValue();

		double boxnumber = Double.valueOf((String) pd.get("box_number")).doubleValue();
		if ((fclVolume != 0.0D) && (boxnumber != 0.0D)) {
			double skuVolume = fclVolume / boxnumber;
			double f = skuVolume;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("SKU_VOLUME", Double.valueOf(f1));
		}

		double fcl_weight = Double.valueOf((String) pd.get("FCL_WEIGHT")).doubleValue();

		if ((fcl_weight != 0.0D) && (fcl_weight != 0.0D)) {
			double sku_weight = fcl_weight / boxnumber;
			double f = sku_weight;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(3, 4).doubleValue();
			pd.put("SKU_WEIGHT", Double.valueOf(f1));
		}
		
			this.productService.newsave(pd);
			mv.addObject("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.sysLogService.saveLog(operationMsg, "成功");
		return new ModelAndView("redirect:/product/newproductlist.do");
	}
	
	/**
	 *查询商品信息 
	 * @return
	 */
	@RequestMapping(value = "/saleReturnProductlist")
	@ResponseBody
	public Object saleReturnProductlistPage() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		String operationMsg = "商品条件查询搜索";
		logBefore(logger, operationMsg);
		try {
			pd = this.getPageData();
			/*pd.put("contact_person_mobile",LoginUtil.getLoginUser().getUSERNAME());*/
			if(pd.containsKey("product_name")){
				if(pd.getString("product_name")!=null&&!pd.getString("product_name").equals("")){
					pd.put("product_name",pd.getString("product_name"));
				}
			}
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			List<PageData> productList = productService.saleReturnProductlist(pd);
			map.put("list", productList);
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 活动商品
	 * @return
	 */
	@RequestMapping(value = "/supplierproductActivity")
	public ModelAndView supplierproductActivity(Page page) {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "进入到商品活动页面";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			if ((keyword != null) && (!"".equals(keyword))) {
				keyword = keyword.trim();
				pd.put("keyword", keyword);
				pd.put("searchcriteria", searchcriteria);
			}
			pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
			page.setPd(pd);
			List<PageData> listpd=this.productService.getproductactivity(page);
			mv.setViewName("procurement/product/supplierproduct_activity");
			mv.addObject("varlist",listpd);
			mv.addObject("pd",pd);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mv;
	}
	
	/**
	 * 审核活动商品
	 * @return
	 */
	@RequestMapping(value = { "/productexamine" }, produces = { "application/text;charset=UTF-8" })
	@ResponseBody
	public String productexamine(){
		String operationMsg = "开始审核";
		String result="";
		logBefore(logger, operationMsg);
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			String ids=pd.getString("DATA_IDS");
			if(ids!=null && !"".equals(ids)){
				String ID [] =ids.split(",");
				result=productService.shenheactivity(ID);
				if(result.equals("true")){
					sysLogService.saveLog("批量审核EnWarehouseOrder", "成功");
				}else{
					sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
		
	}

	
	/**
	 * 查询商品库存详情
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/fundGroupProduct")
	public ModelAndView fundGroupProduct() throws Exception {
		String operationMsg="查询商品库存详情";
		logBefore(logger, operationMsg);
		ModelAndView mv = this.getModelAndView();
		try {
			PageData pd = this.getPageData();
			pd.put("ck_id",LoginUtil.getLoginUser().getCkId());
			List<PageData> typeInventList = productService.fundGroupProduct(pd);
		
			PageData  staleDated = productService.getInventoryCount(pd);
			if(staleDated==null){
				staleDated=new PageData();
			}
			if(typeInventList.size()>0){
				staleDated.put("tun", typeInventList.get(0).getString("tun"));	
			}
			mv.addObject("list", typeInventList);
			mv.addObject("staleDated", staleDated);
			mv.setViewName("procurement/product/TypeInventoryLists");
			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
		
	}
	
}
