package com.hy.service.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.PurchaseOrder;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

@Service("purchaseOrderSerivce")
public class PurchaseOrderSerivce
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  
  	/**
  	 * 查询采购订单汇总
  	 * @param page
  	 * @return
  	 * @throws Exception
  	 */
  	public List<PageData> purchaseorderhe(Page page) throws Exception{
  		return (List<PageData>) dao.findForList("PurchaseOrderMapper.purchaseorderhelistPage", page);
  	}
  	
  	/**
  	 * 地区查询采购订单
  	 * @param page
  	 * @return
  	 * @throws Exception
  	 */
  	public List<PageData> regionpurchaseSum(Page page) throws Exception{
  		return (List<PageData>) dao.findForList("PurchaseOrderMapper.regionpurchaselistPage", page);
  	}
  	
  	/**
  	 * 地区查询采购订单导出
  	 * @param page
  	 * @return
  	 * @throws Exception
  	 */
  	public List<PageData> regionpurchaseExport(PageData pd) throws Exception{
  		return (List<PageData>) dao.findForList("PurchaseOrderMapper.regionpurchaseExport", pd);
  	}
  	
  	public List<PageData> purchaseexcel(PageData pd) throws Exception{
  		return (List<PageData>) dao.findForList("PurchaseOrderMapper.purchaseexcel", pd);
  	}

  public List<PageData> listPdPagePurchaseOrder(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("PurchaseOrderMapper.purchaseOrderlistPage", page);
  }
  
  public List<PageData> purexcel(PageData pd)
		    throws Exception
		  {
		    return (List)this.dao.findForList("PurchaseOrderMapper.purexcel", pd);
		  }
  
  public void save(PageData pd)
    throws Exception
  {
    this.dao.save("PurchaseOrderMapper.save", pd);
  }
  
  public int saveList(List<PurchaseOrder> listPurchaseOrder)
    throws Exception
  {
    Object ob = this.dao.batchSave("PurchaseOrderMapper.savePurchaseOrder", listPurchaseOrder);
    return Integer.parseInt(ob.toString());
  }
  
  public void saveSOList(List<PageData> pds)
    throws Exception
  {
    this.dao.save("PurchaseOrderMapper.save", pds);
  }
  
  public void delete(PageData pd)
    throws Exception
  {
    this.dao.delete("PurchaseOrderMapper.delete", pd);
  }
  
  public int deleteList(List<PurchaseOrder> listPurchaseOrder)
    throws Exception
  {
    Object ob = this.dao.batchDelete("PurchaseOrderMapper.deleteList", listPurchaseOrder);
    return Integer.parseInt(ob.toString());
  }
  
  public void edit(PageData pd)
    throws Exception
  {
    this.dao.update("PurchaseOrderMapper.edit", pd);
  }
  
  public List<PageData> listAll(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("PurchaseOrderMapper.listAll", pd);
  }
  
  public PurchaseOrder findById(PageData pd)
    throws Exception
  {
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    return (PurchaseOrder)this.dao.findForObject("PurchaseOrderMapper.findById", pd);
  }
  public PageData findById2(PageData pd)
		    throws Exception
		  {
		    return (PageData) this.dao.findForObject("PurchaseOrderMapper.findById2", pd);
		  }
  public void deleteAll(String[] ArrayDATA_IDS)
    throws Exception
  {
    this.dao.delete("PurchaseOrderMapper.deleteAll", ArrayDATA_IDS);
  }
  
  public List<PageData> excel(Page page)
    throws Exception
  {
    return (List)this.dao.findForList("PurchaseOrderMapper.excel", page);
  }
  
  /**
   * 批量更新销售订单状态
   * @param list
   * @throws Exception
   */
  public void updateBatch(List list)
		    throws Exception
		  {
		    this.dao.update("PurchaseOrderMapper.updateBatch",list );
}
  /**
   * 批量更新销售订单状态
   * @param list
   * @throws Exception
   */
  public void updateBatch2(List list)
		    throws Exception
		  {
		    this.dao.update("PurchaseOrderMapper.updateBatch2",list );
}
  public List<PageData> findForList(PageData pd)
		    throws Exception
		  {
		    return (List<PageData>) this.dao.findForList("PurchaseOrderMapper.findpurchase_order_item",pd);
		  }
  /**
   * 根据销售单码查询订单基本信息
   * @param pd
   * @return
   * @throws Exception
   */
  public Object findorder(PageData pd)
		    throws Exception
		  {
		    return this.dao.findForObject("PurchaseOrderMapper.findorder", pd);
  }
  /**
   * 根据销售单码查询订单详情
   * @param pd
   * @return
   * @throws Exception
   */
  public List<PageData> findorderitem(PageData pd)
		    throws Exception
		  {
		    return (List)this.dao.findForList("PurchaseOrderMapper.findorderitem", pd);
  }
  /**
   * 审核
   * @param list
   * @return
   */
    @Transactional
	public String audit(List list){
		String result = "false";
		try {
			
			List <PageData> enorder = new ArrayList<PageData>();
			List <PageData> enorderitem = new ArrayList<PageData>();
			if(list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					//根据id 查询采购单详情
					 PageData pd = new PageData();
			  		 pd.put("id", list.get(i));
			  		 PageData pdd = this.findById2(pd);
			  		 //封装入库单
			  		 String orderNum = "RK"+pdd.getString("order_num").substring(2);;
			  		 PageData pa = saveEnOrder(pdd,orderNum);
			  		 //保存入库单
			  		 enorder.add(pa);
			  		 //根据采购单码查询采购单详情
			  		 pd.put("order_num", orderNum);
			  		 List <PageData> listpd = this.findForList(pdd);
			  		 //封装采购单详情
			  		 for(PageData pdt:listpd){
						  enorderitem.add(saveEnOrderItem(pdt,orderNum));//添加入库条目
			  		 }
				}
			}
			this.updateBatch(list);
			dao.save("ENOrderMapper.saveEnOrderlist", enorder);
			//保存采购单详情
			dao.save("ENOrderMapper.saveEnOrderItemlsit", enorderitem);
			result="true";
		} catch (Exception e) {
			result = "false";
			e.printStackTrace();
			throw new RuntimeException("审核失败！");
		}
		return result;
	}
	/**
	 * 封装入库单
	 * @param pdd
	 * @param orderNum
	 * @return
	 */
	  public PageData saveEnOrder(PageData pdd,String orderNum){
		  PageData enOrder=new PageData();
		  enOrder.put("group_num", pdd.getString("group_num"));
		  enOrder.put("order_num",orderNum);
		  enOrder.put("checked_state", 1);
		  enOrder.put("supplier_id", pdd.getString("supplier_id"));
		  enOrder.put("manager_name", pdd.getString("manager_name"));
		  enOrder.put("manager_tel", pdd.getString("manager_tel"));
		  enOrder.put("comment", pdd.getString("comment"));
		  enOrder.put("final_amount", 0);
		  enOrder.put("total_svolume", 0);
		  enOrder.put("total_weight", 0);
		  enOrder.put("is_ivt_order_print", 0);
		  enOrder.put("is_temporary", 2);
		  enOrder.put("is_order_print", 1);
		  enOrder.put("ivt_state",1);
		  enOrder.put("state", 1);
		  enOrder.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
		  enOrder.put("amount", 0);
		  enOrder.put("order_type",1);
		  enOrder.put("ck_id", 1);
		  return enOrder;
	  }
	  /***封装入库单详情数据***/
	  public PageData saveEnOrderItem(PageData pdd,String orderNum){
		  PageData enorderItem=new PageData();
		  enorderItem.put("group_num", pdd.getString("group_num"));
		  enorderItem.put("order_num", orderNum);
		  enorderItem.put("product_id", pdd.getString("product_id"));
		  enorderItem.put("purchase_price", pdd.getString("purchase_price"));
		  enorderItem.put("quantity", pdd.getString("quantity"));
		  enorderItem.put("final_quantity", 0);
		  enorderItem.put("svolume", pdd.getString("svolume"));
		  enorderItem.put("weight", pdd.getString("weight"));
		  enorderItem.put("is_split_ivt", 2);
		  enorderItem.put("is_ivt_BK", 1);
		  enorderItem.put("creator",LoginUtil.getLoginUser().getUSERNAME());
		  enorderItem.put("state", 1);
		  enorderItem.put("product_time",null);
		  enorderItem.put("gift_quantity",pdd.getString("gift_quantity"));
		  return enorderItem;
	  }

}


