package com.hy.service.inventory;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.OrderGroup;
import com.hy.entity.system.User;
import com.hy.service.order.ENOrderItemService;
import com.hy.service.order.EXOrderItemService;
import com.hy.service.order.EnWarehouseOrderService;
import com.hy.service.order.ExWarehouseOrderService;
import com.hy.service.order.OrderGroupService;
import com.hy.service.product.ProductService;
import com.hy.util.LoginUtil;
import com.hy.util.NumberUtil;
import com.hy.util.OrderNum;
import com.hy.util.PageData;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("productinventoryService")

public class ProductinventoryService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  @Resource
  private ProductService productService;
  @Resource
  private OrderGroupService orderGroupService;
  @Resource
  private ENOrderItemService eNOrderItemService;
  @Resource
  private EXOrderItemService eXOrderItemService;
  @Resource
  private ProductinventoryService productinventoryService;
  @Resource
  private EnWarehouseOrderService enwarehouseorderService;
  @Resource
  private ExWarehouseOrderService exwarehouseorderService;
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("ProductinventoryMapper.save", pd);
  }
  
  public String addTheInventory(Page page)
  {
    String type = "";
    try {
      List<PageData> list = listTemp(page);
      if (list.size() > 0)
      {
        type = ((PageData)list.get(0)).get("status").toString();
        for (int i = 0; i < list.size(); i++) {
          PageData pd = new PageData();
          pd = this.productService.findQuantityById(((PageData)list.get(i))
            .get("productid").toString());
          pd.put("newQuantity", ((PageData)list.get(i)).get("quantity"));
          pd.put("product_quantity", pd.get("quantitya"));
          pd.put("warehouseId", "1");
          if (type.equals("1")) {
            updateProductinventoryAdd(pd);
          } else if (type.equals("0")) {
            updateProductinventoryReduce(pd);
          }
        }
        
        crateTempTable();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return type;
  }
  
  public boolean updateProductinventoryAdd(PageData pd)
    throws Exception
  {
    boolean result = false;
    try
    {
      PageData page = findProductInventory(pd);
      if(null!=page){ 
    	  if ((!"".equals(pd.get("newQuantity"))) || (pd.get("newQuantity") != null)){
            double newQuantity = Double.parseDouble((String)pd.get("newQuantity"));
            double oldQuantity = Double.parseDouble(page.get("product_quantity").toString());
            double quantity = NumberUtil.add(newQuantity, oldQuantity);
            pd.put("quantity", Double.valueOf(quantity));
            this.dao.update("ProductinventoryMapper.updateProductinventory", pd);
            pd.put("product_id",pd.get("productId"));
            pd.put("user_id",LoginUtil.getLoginUser().getUSER_ID());
            pd.put("comment","入库后数量为"+quantity);
            pd.put("quantity", Double.parseDouble((String)pd.get("newQuantity")));
            pd.put("rck_type", 1);
            dao.save("ProductinventoryMapper.savehistory", pd);
            result = true;
          } else {
            result = false;
          }
      }else{
    	  //添加库存信息
    	  double newQuantity = Double.parseDouble((String)pd.get("newQuantity"));
    	  pd.put("product_quantity", newQuantity);
    	  pd.put("state", 1);
    	  pd.put("cargo_space_id","1");
    	  pd.put("product_id",pd.get("productId"));
    	  pd.put("warehouse_id",pd.get("warehouseId"));
    	  dao.save("ProductinventoryMapper.save", pd);
    	  pd.put("comment","入库后数量为"+newQuantity);
    	  pd.put("quantity", newQuantity);
    	  pd.put("user_id",LoginUtil.getLoginUser().getUSER_ID());
    	  pd.put("rck_type", 1);
    	  dao.save("ProductinventoryMapper.savehistory", pd);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException();
    }
    return result;
  }
  
  public boolean updateProductinventoryReduce(PageData pd)
    throws Exception
  {
    boolean result = false;
    PageData page = findProductInventory(pd);
    if ((!"".equals(pd.get("newQuantity"))) || (pd.get("newQuantity") != null))
    {
    	if(null!=page){
      if ((!"".equals(page.get("product_quantity"))) || (page.get("product_quantity") != null)) {
        double newQuantity = Double.parseDouble(pd.get("newQuantity").toString());
        double oldQuantity = Double.parseDouble(page.get("product_quantity").toString());
        BigDecimal data1 = new BigDecimal(oldQuantity);
        BigDecimal data2 = new BigDecimal(newQuantity);
        int i = data1.compareTo(data2);
        //System.out.print(i);
        if (i != -1) {
          double quantity = NumberUtil.sub(oldQuantity, newQuantity);
          pd.put("quantity", Double.valueOf(quantity));
          this.dao.update("ProductinventoryMapper.updateProductinventory", pd);
          pd.put("quantity", Double.valueOf(pd.getString("newQuantity")));
          pd.put("product_id",pd.get("productId"));
          pd.put("user_id",LoginUtil.getLoginUser().getUSER_ID());
          pd.put("comment","出库后数量为"+quantity);
          pd.put("rck_type", 2);
          dao.save("ProductinventoryMapper.savehistory", pd);
        } else {
          throw new Exception();
        }
      }else {
        result = false;
      }
    	}
    }
    else {
      result = false;
    }
    
    return result;
  }
  
  
  
 /* public void updateProductinverCo(PageData pd) throws Exception{
	  
	  dao.update("ProductinventoryMapper.updateProductinverCo", pd);
  }*/
 public PageData findProductinverCo(PageData pd) throws Exception{
	  
	  return (PageData) dao.findForObject("ProductinventoryMapper.findProductinverCo", pd);
  }
  public void crateTempTable()
    throws Exception
  {
    this.dao.update("ProductinventoryMapper.dropTable", null);
    this.dao.update("ProductinventoryMapper.createTable", null);
  }
  
  public void tempsave(List<PageData> pd)
    throws Exception
  {
    this.dao.save("ProductinventoryMapper.tempsave", pd);
  }
  
  public List<PageData> listTemp(Page page) throws Exception {
    return (List)this.dao.findForList(
      "ProductinventoryMapper.TemplistPage", page);
  }
  
  public List<PageData> getProductInventroyinfo(Page page)
    throws Exception
  {
    return (List)this.dao.findForList(
      "ProductinventoryMapper.geProductByidlistPage", page);
  }
  
  	public List<PageData> geProductByidhistorylistPage(Page page)throws Exception{
		    return (List)this.dao.findForList("ProductinventoryMapper.geProductByidhistorylistPage", page);

	}



	public void updatehandheldInventory(PageData pd) throws Exception {
		this.dao.update("ProductinventoryMapper.updatehandheldInventory", pd);
	}

	public void updateProductinventory(PageData pd) throws Exception {
		this.dao.update("ProductinventoryMapper.updateProductinventory", pd);
	}

	/** 导入次品出入/出库单 ***/
	public boolean saveTempInventory(Page page, String inventoryType)
			throws Exception {
		boolean result = false;
		List<PageData> pd = listTemp(page);
		String orderGroupNum = OrderNum.getOrderGourpNum();
		OrderGroup og = new OrderGroup();
		og.setOrderGroupNum(orderGroupNum);
		og.setUser(LoginUtil.getLoginUser());
		og.setGroupType(5);
		this.orderGroupService.saveOrderGroup(og);
		String orderNumex = OrderNum.exTempOrderNum();
		String orderNumen = OrderNum.getEnTempOrderNum();
		for (PageData pdTemp : pd) {
			if (inventoryType.equals("1")) {
				PageData orderItem = new PageData();
				orderItem.put("order_num", orderNumen);
				orderItem.put("product_id", pdTemp.get("productid"));
				orderItem.put("group_num", orderGroupNum);
				orderItem.put("final_quantity", pdTemp.get("quantity"));
				orderItem.put("reason", pdTemp.get("reason"));
				orderItem.put("state", Integer.valueOf(1));
				orderItem.put("comment", pdTemp.get("comment"));
				orderItem.put("is_ivt_BK", Integer.valueOf(2));
				this.eNOrderItemService.save(orderItem);
				PageData productInvertory = new PageData();
				productInvertory.put("productId", pdTemp.get("productid"));
				productInvertory.put("newQuantity", pdTemp.get("quantity"));
				productInvertory.put("warehouseId", Integer.valueOf(2));
				productInvertory.put("order_num", orderNumen);
				productInvertory.put("ck_id", LoginUtil.getLoginUser()
						.getCkId());
				result = updateProductinventoryAdd(productInvertory);
			} else {
				PageData orderItem = new PageData();
				orderItem.put("order_num", orderNumex);
				orderItem.put("product_id", pdTemp.get("productid"));
				orderItem.put("group_num", orderGroupNum);
				orderItem.put("quantity", pdTemp.get("quantity"));
				orderItem.put("final_quantity", pdTemp.get("quantity"));
				orderItem.put("reason", pdTemp.get("reason"));
				orderItem.put("state", Integer.valueOf(1));
				orderItem.put("comment", pdTemp.get("comment"));
				orderItem.put("is_ivt_BK", Integer.valueOf(2));
				this.eXOrderItemService.save(orderItem);
				PageData productInvertory = new PageData();
				productInvertory.put("productId", pdTemp.get("productid"));
				productInvertory.put("newQuantity", pdTemp.get("quantity"));
				productInvertory.put("warehouseId", Integer.valueOf(2));
				productInvertory.put("order_num", orderNumen);
				productInvertory.put("ck_id", LoginUtil.getLoginUser()
						.getCkId());
				result = updateProductinventoryReduce(productInvertory);
			}
		}

		if (inventoryType.equals("1")) {
			PageData enTempOrderParam = new PageData();
			enTempOrderParam.put("order_num", orderNumen);
			enTempOrderParam.put("is_order_print", Integer.valueOf(1));
			enTempOrderParam.put("ivt_state", Integer.valueOf(2));
			enTempOrderParam.put("is_temporary", Integer.valueOf(2));
			enTempOrderParam.put("user_id",
					Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
			enTempOrderParam.put("state", Integer.valueOf(1));
			enTempOrderParam.put("group_num", orderGroupNum);
			enTempOrderParam.put("ck_id", LoginUtil.getLoginUser().getCkId());
			enTempOrderParam.put("order_type", 4);
			this.enwarehouseorderService.save(enTempOrderParam);
		} else {
			PageData enTempOrderParam = new PageData();
			enTempOrderParam.put("order_num", orderNumex);
			enTempOrderParam.put("is_order_print", Integer.valueOf(1));
			enTempOrderParam.put("ivt_state", Integer.valueOf(2));
			enTempOrderParam.put("is_temporary", Integer.valueOf(2));
			enTempOrderParam.put("user_id",
					Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
			enTempOrderParam.put("state", Integer.valueOf(1));
			enTempOrderParam.put("group_num", orderGroupNum);
			enTempOrderParam.put("order_type", 4);
			enTempOrderParam.put("ck_id", LoginUtil.getLoginUser().getCkId());
			this.exwarehouseorderService.save(enTempOrderParam);
		}
		return result;
	}

	

	public boolean giftUpdateProductinventoryAdd(PageData pd) throws Exception {
		boolean result = false;
		try {
			PageData page = findProductInventory(pd);
			if (null != page) {
				if ((!"".equals(pd.get("newQuantity")))
						|| (pd.get("newQuantity") != null)) {
					double newQuantity = Double.parseDouble((String) pd
							.get("newQuantity"));
					double oldQuantity = Double.parseDouble(page.get(
							"product_quantity").toString());
					double quantity = NumberUtil.add(newQuantity, oldQuantity);
					pd.put("quantity", Double.valueOf(quantity));
					this.dao.update(
							"ProductinventoryMapper.updateProductinventory", pd);
					pd.put("product_id", pd.get("productId"));
					pd.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
					pd.put("comment", "入库后数量为" + quantity);
					pd.put("rck_type", 1);
					pd.put("quantity",
							Double.parseDouble((String) pd.get("newQuantity")));
					dao.save("ProductinventoryMapper.savehistory", pd);
					result = true;
				} else {
					result = false;
				}
			} else {
				// 添加库存信息
				double newQuantity = Double.parseDouble((String) pd
						.get("newQuantity"));
				pd.put("product_quantity", newQuantity);
				pd.put("state", 1);
				pd.put("cargo_space_id", "1");
				pd.put("product_id", pd.get("productId"));
				pd.put("warehouse_id", pd.get("warehouseId"));
				dao.save("ProductinventoryMapper.save", pd);
				pd.put("comment", "入库后数量为" + newQuantity);
				pd.put("quantity", newQuantity);
				pd.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
				pd.put("rck_type", 1);
				dao.save("ProductinventoryMapper.savehistory", pd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}

	

	public PageData findProductInventory(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject(
				"ProductinventoryMapper.findProductInventory", pd);
	}
	/**
	 * 查询商品库存数
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findProductInventoryData(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ProductinventoryMapper.findProductInventoryData", pd);
	}
	
	/**
	 * 查询商品总库存
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData sumquantity(PageData pd) throws Exception {
		return (PageData) dao.findForObject("ProductinventoryMapper.sumquantity", pd);
	}
	
	public void updateProductinverCo(PageData pd) throws Exception {

		dao.update("ProductinventoryMapper.updateProductinverCo", pd);
	}

	


	public List<PageData> dataCenterPIlistPage(Page page) throws Exception {
		return (List) this.dao.findForList(
				"ProductinventoryMapper.dataCenterPIlistPage", page);
	}
	
	/**
	 * 保存库存历史记录
	 * @param pd
	 * @throws Exception
	 */
	public void savehistory(PageData pd) throws Exception{
		dao.save("ProductinventoryMapper.savehistory", pd);
	}
	/**
	 * 根据商品日期更新
	 * @param pd
	 * @throws Exception
	 */
	public void updateProducData(PageData pd) throws Exception {
		this.dao.update("ProductinventoryMapper.updateProducData", pd);
	}
}
