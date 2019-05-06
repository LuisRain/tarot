package com.hy.service.pfckd;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;

@Service("ghsjsservice")
public class GHSJSService {
	
	 @Resource(name="daoSupport")
	 private DaoSupport dao;
	
	
	
	/**
	 * 供货商结算单
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findGHSJSlistpage(Page page) throws Exception{
		return (List<PageData>) dao.findForList("ghsjsMapper.findGHSJSlistPage", page);
	}
	/**
	 * 打印供货商结算单
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> printGHSJSlist(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ghsjsMapper.findGHSJSlist", pd);
	}
	
	/**
	 * 通过供应商id和月份获取批次信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> dzhprint(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("ghsjsMapper.findsupdzh", pd);
	}
	/**
	 * 通过供应商id获取供应商信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageData findsupid(PageData pd) throws Exception{
		return (PageData) dao.findForObject("ghsjsMapper.findsupid", pd);
	}
}
