package com.hy.service.pfckd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hy.dao.DaoSupport;
import com.hy.util.DoubleUtil;
import com.hy.util.PageData;

@Service("pfackdservice")
public class PfackdService {
	
	 @Resource(name="daoSupport")
	 private DaoSupport dao;
	
	/**
	 * 批量导入销售单及详情
	 * @param page	销售单
	 * @param page2  销售单详情
	 * @return
	 */
	@Transactional
	public String importExcelOfEXOrder(List<PageData> page,List<PageData> page2,List<PageData> page3,List<PageData> page4,List<PageData> page5){
		  String ret="";
		try {
			if(page.size() > 0){
				List<PageData> listpd = groupMdorder(page);
				dao.save("pfackdMapper.saveExOrderList", listpd); //批量插入出库单
			}
			if(page2.size() > 0) {
				//批量插入出库单详情
				List<PageData> listpd2 = groupMdorderitem(page2,page);
			    dao.save("pfackdMapper.saveExOrderItemsupplier", listpd2); 
			}
			if(page3.size() > 0){
				dao.update("pfackdMapper.updateForBatch", page3); 
			}
			if(page4.size() > 0){
				dao.update("pfackdMapper.updateOrderitemForBatch", page4); //批量更新出库单详情
			}
			if(page5.size() > 0){
				 dao.save("pfackdMapper.saveExOrderItemsupplier", page5);	//在该门店有出库单，但出库单详情中没有该商品出库信息的基础上新增出库单详情
			}
			//根据订单详情表中的数据更新订单   
			List<PageData> listpd = (List<PageData>) dao.findForList("pfackdMapper.findsumprice", null);
			if(listpd.size() > 0){
				dao.update("pfackdMapper.updateForBatch2", listpd); 
			}
			dao.update("pfackdMapper.updateproductprice", null);
			ret = "导入成功！";
		} catch (Exception e) {
			ret="导入失败";
			throw new  RuntimeException(e.toString());
		}
		  return ret;
	 }
	
	/**
	 * 处理门店出库单订单数据
	 * @param page
	 * @return
	 */
	public List<PageData> groupMdorder(List<PageData> page){
		List<PageData> pdlist = new ArrayList<PageData>();
		List<Object> mdidlist = new ArrayList<Object>();
		for (int i = 0; i < page.size(); i++) {
			String merchant_id = page.get(i).getString("merchant_id");
			mdidlist.add(merchant_id);
		}
		if(mdidlist.size() > 0){
			mdidlist = mdidlist.stream().distinct().collect(Collectors.toList());  
			for (int i = 0; i < mdidlist.size(); i++) {
				String mdid = mdidlist.get(i).toString();
				double zje = 0.00;
				double zzl = 0.00;
				double ztj = 0.00;
				PageData orderpd = new PageData();
				for (int j = 0; j < page.size(); j++) {
					if(mdid.equals(page.get(j).getString("merchant_id"))){
						orderpd = page.get(j);
						zje =  DoubleUtil.add(zje, Double.parseDouble(page.get(j).getString("final_amount")));
						ztj =  DoubleUtil.add(ztj, Double.parseDouble(page.get(j).getString("total_svolume")));
						zzl =  DoubleUtil.add(zzl, Double.parseDouble(page.get(j).getString("total_weight")));
					}
				}
				orderpd.put("final_amount", zje);
				orderpd.put("total_svolume", ztj);
				orderpd.put("total_weight", zzl);
				pdlist.add(orderpd);
			}
		}
		return pdlist;
	}
	
	/**
	 * 处理门店出库单详情数据
	 * @param page 出库单详情数据  page2 出库单数据
	 * @return
	 */
	public List<PageData> groupMdorderitem(List<PageData> page,List<PageData> page2){
		List<PageData> pdlist = new ArrayList<PageData>();
		List<Object> mdidlist = new ArrayList<Object>();
		for (int i = 0; i < page2.size(); i++) {
			String merchant_id = page2.get(i).getString("merchant_id");		//获取门店id
			mdidlist.add(merchant_id);
		}
		if(mdidlist.size() > 0){
			mdidlist = mdidlist.stream().distinct().collect(Collectors.toList());  //去重
			//根据门店id 获取编号批次号
			for (int i = 0; i < mdidlist.size(); i++) {
				String mdid = mdidlist.get(i).toString();	//获取门店编号
				for (int j = 0; j < page2.size(); j++) {
					PageData orderpd = page2.get(j);
					if(mdid.equals(orderpd.getString("merchant_id"))){
						String ordernum = orderpd.getString("order_num");	//获取出库单号
						String group_num = orderpd.getString("group_num");
						for (int k = 0; k < page.size(); k++) {
							PageData itempd = page.get(k);
							if(mdid.equals(itempd.getString("merchant_id"))){
								itempd.put("order_num", ordernum);
								itempd.put("group_num", group_num);
								pdlist.add(itempd);
							}
						}
					}
				}
			}
		}
		//去重
		for (int i = 0; i < pdlist.size(); i++) {
		      PageData pd = pdlist.get(i);
		      String order_num = pd.getString("order_num");
		      String product_id = pd.getString("product_id");
		      for (int j = i+1; j < pdlist.size(); j++) {
		    	  PageData pd2 = pdlist.get(j);
		    	  String order_num2 = pd2.getString("order_num");
			      String product_id2 = pd2.getString("product_id");
			      if(order_num.equals(order_num2) && product_id.equals(product_id2)){
			    	  pdlist.remove(j);
			    	  j--;
			      }
		      }
		}
		return pdlist;
	}
	/**
	 * 保存出库单
	 * @param pd
	 * @throws Exception
	 */
	 @Transactional
	 public void saveExOrder(PageData pd){
		 try {
			 dao.save("pfackdMapper.saveExOrder", pd);
		} catch (Exception e) {
			throw new  RuntimeException(e.toString());
		}
	 }
	 /**
		 * 保存出库单详情
		 * @param pd
		 * @throws Exception
		 */
		 @Transactional
		 public void saveExOrderItem(PageData pd){
			 try {
				 dao.save("pfackdMapper.saveExOrderItem", pd);
			} catch (Exception e) {
				throw new  RuntimeException(e.toString());
			}
		 }
	/**
	 * 根据门店简称和门店编码查询门店信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMerchant(PageData pd) throws Exception{
		return (PageData) dao.findForObject("pfackdMapper.findMerchant", pd);
	}
	/**
	 * 根据商品id 查询商品成本价格
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findprice(PageData pd) throws Exception{
		return (PageData) dao.findForObject("pfackdMapper.findprice", pd);
	}
	/**
	 * 根据门店id 查询门店最新的出库单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findMdExorder(PageData pd) throws Exception{
		return (PageData) dao.findForObject("pfackdMapper.findMdExorder", pd);
	}
	/**
	 * 根据出库单号和商品id 查询出库详情
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findexorderitem(PageData pd) throws Exception{
		return (PageData) dao.findForObject("pfackdMapper.findexorderitem", pd);
	}
	
}
