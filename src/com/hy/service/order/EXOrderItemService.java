package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.EXOrderItem;
import com.hy.entity.system.User;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("eXOrderItemService")
public class EXOrderItemService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public List<PageData> listPdPagePurchaseOrder(Page page) throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.eXOrderItemlistPage", page);
	}

	public List<PageData> getExOrderItemByProductAndGroupNum(PageData pd)
			throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.getExOrderItemByProductAndGroupNum", pd);
	}

	public int save(PageData pd) throws Exception {
		pd.put("creator", LoginUtil.getLoginUser().getNAME());

		Object ob = this.dao.save("EXOrderItemMapper.save", pd);
		return Integer.parseInt(ob.toString());
	}

	public int saveList(List<EXOrderItem> listEXOrderItem) throws Exception {
		Object ob = this.dao.batchSave("EXOrderItemMapper.saveEXOrderItem",
				listEXOrderItem);
		return Integer.parseInt(ob.toString());
	}

	public int savegiftEXOrderItem(List<EXOrderItem> listEXOrderItem)
			throws Exception {
		Object ob = this.dao.batchSave("EXOrderItemMapper.savegiftEXOrderItem",
				listEXOrderItem);
		return Integer.parseInt(ob.toString());
	}

	public void delete(PageData pd) throws Exception {
		this.dao.delete("EXOrderItemMapper.delete", pd);
	}

	public int deleteList(List<EXOrderItem> listEXOrderItem) throws Exception {
		Object ob = this.dao.batchDelete("EXOrderItemMapper.deleteList",
				listEXOrderItem);
		return Integer.parseInt(ob.toString());
	}

	public int edit(PageData pd) throws Exception {
		Object ob = this.dao.update("EXOrderItemMapper.edit", pd);
		return Integer.parseInt(ob.toString());
	}

	public int editIvt(PageData pd) throws Exception {
		Object ob = this.dao.update("EXOrderItemMapper.editIvt", pd);
		return Integer.parseInt(ob.toString());
	}

	public List<PageData> findByIdOrOrderNumOrGroupNum(PageData pd)
			throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
	}

	public PageData findPackingByParentId(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject(
				"EXOrderItemMapper.findPackingByParentId", pd);
	}

	public ENOrder findById(PageData pd) throws Exception {
		ENOrder eNOrder = new ENOrder();
		return (ENOrder) this.dao.findForObject(
				"EXOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
	}

	public List<PageData> getOrderItemlistPageProductById(Page page)
			throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.getOrderItemlistProductlistPage", page);
	}
	public List<PageData> getOrderItemlistPageProductByIdForPrint(PageData pd)
			throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.getOrderItemlistProductByIdForPrint", pd);
	}

	public List<PageData> getOrderItemlistProduct(PageData pd)
			throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.getOrderItemlistProduct", pd);
	}

	public List<PageData> getFjItemById(PageData pd) throws Exception {
		return (List) this.dao.findForList("EXOrderItemMapper.getFjItemById",
				pd);
	}

	public List<PageData> getOrderItemlistPageById(PageData pd)
			throws Exception {
		return (List) this.dao.findForList(
				"EXOrderItemMapper.getOrderItemlistById", pd);
	}

	public PageData fencangCount(PageData pd) throws Exception {
		return (PageData) dao.findForObject("EXOrderItemMapper.fencangCount",
				pd);
	}

	/*
	 * public List<PageData> printquantity(List<PageData> list) throws Exception
	 * { return (List) this.dao.findForList("EXOrderItemMapper.printquantity",
	 * list); }
	 */

	public PageData selectSumExOrder(String orderNum) throws Exception {
		return (PageData) this.dao.findForObject(
				"EXOrderItemMapper.selectSumExOrder", orderNum);
	}

	public PageData findItemById(PageData pd) throws Exception {
		return (PageData) this.dao.findForObject(
				"EXOrderItemMapper.findByIdOrOrderNumOrGroupNum", pd);
	}

	public int editQuantity(PageData pd) throws Exception {
		Object ob = this.dao.update("EXOrderItemMapper.editQuantity", pd);
		return Integer.parseInt(ob.toString());
	}

	public List<PageData> exDataCenterlistPage(Page pg) throws Exception {
		return (List) this.dao.findForList(

		"EXOrderItemMapper.exDataCenterlistPage", pg);
	}

	public List<PageData> dataCenterProductQuantitylistPage(Page pg)
			throws Exception {
		return (List) this.dao.findForList(

		"EXOrderItemMapper.dataCenterProductQuantitylistPage", pg);
	}
	/**
	 * 根据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findOrderItemByOrderNum(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("EXOrderItemMapper.findOrderItemByOrderNum", pd);
	}
	
	/**
	 * 保存订单详情
	 * @param pd
	 * @throws Exception
	 */
	public void saveitem(PageData pd) throws Exception{
		dao.save("EXOrderItemMapper.saveitem", pd);
	}
	
	public int editgiftQuantity(PageData pd) throws Exception {
		Object ob = this.dao.update("EXOrderItemMapper.editgiftQuantity", pd);
		return Integer.parseInt(ob.toString());
	}
	/**
	 * 更新商品生产日期和出库日期
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int edittime(PageData pd) throws Exception {
		Object ob = this.dao.update("EXOrderItemMapper.edittime", pd);
		return Integer.parseInt(ob.toString());
	}
}
