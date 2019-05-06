package com.hy.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;

@Service("stageNumberService")
public class StageNumberService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 查询期数
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> stageNumberlistPage(Page page) throws Exception{
		return (List<PageData>) dao.findForList("StageNumberMapper.stageNumberlistPage", page);
	}
	
	public List<PageData> stageNumberlist(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("StageNumberMapper.stageNumberlist", pd);
	}
	
	public void save(PageData pd) throws Exception{
		dao.save("StageNumberMapper.save", pd);
	}
	
	public PageData findByName(PageData pd) throws Exception{
		return (PageData) dao.findForObject("StageNumberMapper.findByName", pd);
	}
	public PageData findById(PageData pd) throws Exception{
		return (PageData) dao.findForObject("StageNumberMapper.findById", pd);
	}
	
	public void edit(PageData pd) throws Exception{
		dao.update("StageNumberMapper.edit", pd);
	}
}
