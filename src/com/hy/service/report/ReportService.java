package com.hy.service.report;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.util.Const;
import com.hy.util.HttpUtil;
import com.hy.util.Logger;
import com.hy.util.PageData;

@Service("reportService")
public class ReportService{
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	protected  Logger logger = Logger.getLogger(this.getClass());
	
	public List<PageData> timearrivalexcel(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("report.timearrivalexcel", pd);
	}
	
	/**
	 * 门店到货报表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listReport(Page page) throws Exception{
		return (List<PageData>) dao.findForList("report.ReportlistPage", page);
	}
	
	/**
	 * 门店到货报表导出
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listReportexport(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("report.listReportexport", pd);
	}
	
	/**
	 * 门店站点月季情况表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listmonthlyreport(Page page) throws Exception{
		return (List<PageData>) dao.findForList("report.monthlyreportlistPage", page);
	}
	
	/**
	 * 月季情报报表导出
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listmonthlyreportexport(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("report.monthlyreportexport", pd);
	}
	/**
	 * 月度加急订单
	 * @param pd
	 * @return
	 * @throws Exception 
	 */
	public PageData monthlyjj(PageData pd) throws Exception{
		return (PageData) dao.findForObject("report.monthlyjj",pd);
		
	}
	
	  
	  
	/**
	 * 调用货运平台数据
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public JSONArray selectEx(List<PageData> exlist) throws Exception{
		JSONObject json=new JSONObject();
		json.put("jsonData",exlist);
		String data = json.toString();
		logger.error("data="+data);
		data = URLEncoder.encode(data, "UTF-8");
		String jsonData = "jsonData="+data;
		logger.error("--------推送地址："+Const.httpplan);
		String result = HttpUtil.httpPostRequest(Const.httpplan+"interfaces/querywaveorder.do",jsonData);
		result = URLDecoder.decode(result, "UTF-8");
		JSONArray arr=JSONArray.parseArray(JSONObject.parseObject(result).getString("msg"));
		return arr;
	}
	
	public List<PageData> selectmerchant() throws Exception{
		
		return (List<PageData>) dao.findForList("report.selectmerchant", null);
	}
	public List<PageData> selectExorder(PageData pd) throws Exception{
		
		return (List<PageData>) dao.findForList("report.selectEx", pd);
	}
	
	public List<PageData> planimplement(Page page) throws Exception{
		
		return (List<PageData>) dao.findForList("report.planimplementlistPage", page);
	}
	public List<PageData> planimplementExcel(PageData pd) throws Exception{
		
		return (List<PageData>) dao.findForList("report.planimplementExcel", pd);
	}
	public PageData plandown(PageData pd) throws Exception{
		
		return (PageData) dao.findForObject("report.plandown", pd);
		
	}
	
	/**
	 * 中央仓盘点商品类别明细
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findpdmx(Page page) throws Exception{
		
		return (List<PageData>) dao.findForList("report.findpdmxPage", page);
	}
	/**
	 * 导出中央仓盘点商品类别明细
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findpdmx2(PageData pd) throws Exception{
		
		return (List<PageData>) dao.findForList("report.findpdmx", pd);
	}
	
	/**
	 * 根据商品编码和条形码查询商品信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findpro(PageData pd) throws Exception{
		
		return (PageData) dao.findForObject("report.findpro", pd);
		
	}
	
	/**
	 * 根据商品条形码和商品日期查询库存
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findKC(PageData pd) throws Exception{
		
		return (PageData) dao.findForObject("report.findKC", pd);
		
	}
	/**
	 * 更新库存
	 * @param pd
	 * @throws Exception
	 */
	public void updateKC(PageData pd) throws Exception{
		 dao.update("report.updateKC", pd);
	}
	
	/**
	 * 批量导入销售单及详情
	 * @param page	销售单
	 * @param page2  销售单详情
	 * @return
	 */
	@Transactional
	public String importExcel(List<PageData> page,List<PageData> page2,List<PageData> page3){
		  String ret="";
          try {
			//清空hos表数据
			  dao.delete("report.deletehos", null);
			  dao.save("report.saveHOSList", page);
			  if(page2.size() >0){
				  dao.save("report.saveKC", page2);
			  }
			  if(page3.size() > 0){
				  dao.batchUpdate("report.updateKC", page3);
			  }
			  ret = "导入成功！";
		} catch (Exception e) {
			ret="导入失败";
			throw new  RuntimeException(e.toString());
		}
		  return ret;
	 }

}
