package com.hy.service.Statistics;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.PageData;

/**
 * 供应商到货率service
 * @author gyy
 *
 */
@Service("supplierarrivalrateservice")
public class SupplierArrivalrateService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 查询供货商到货率list
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findlistPage(Page page) throws Exception {
		return (List) this.dao.findForList("SupplierArrivalrate.findlistPage", page);
	}
	
	public List<PageData> findarrivalate(PageData pd) throws Exception {
		return (List) this.dao.findForList("SupplierArrivalrate.findarrivalate", pd);
	}

}
