package com.hy.controller.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.product.Product;
import com.hy.entity.system.User;
import com.hy.service.report.ReportService;
import com.hy.util.Const;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.DoubleUtil;
import com.hy.util.FileDownload;
import com.hy.util.FileUpload;
import com.hy.util.LoginUtil;
import com.hy.util.MathUtil;
import com.hy.util.ObjectExcel;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import com.hy.util.StringUtil;

/**
 *报表
 * @author wps
 *
 */
@Controller
@RequestMapping({ "/reportController" })
public class ReportController extends BaseController{
	@Resource(name="reportService")
	private ReportService reportService;
	@Resource(name = "sysLogService")
	private SysLogService sysLogService;
	
	
	
	@RequestMapping(value="/timearrivalexcel")
	public ModelAndView timearrivalexcel(){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="导出出库订单信息";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String group_num=pd.getString("groupnum");
			if(group_num!=null && group_num!="") {
				pd.put("group_num", group_num);
			}
			Map<String, Object> map=new HashMap<String,Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("批次号");
 			titles.add("订单号");
 			titles.add("商品编码");
 			titles.add("商品条形码");
 			titles.add("商品名称");
 			titles.add("订购数量");
 			titles.add("实际数量");
 			titles.add("赠品数量");
 			titles.add("门店编码");
 			titles.add("门店名称");
 			titles.add("门店简称");
 			map.put("titles", titles);
 			List<PageData> listpd=reportService.timearrivalexcel(pd);
 			List<PageData> varList=new ArrayList<PageData>();
 			if(listpd.size()>0) {
 				for (int i = 0; i < listpd.size(); i++) {
 					PageData pdd=new PageData();
	 				pdd.put("var1", listpd.get(i).getString("group_num"));
	 				pdd.put("var2", listpd.get(i).getString("order_num"));
	 				pdd.put("var3", listpd.get(i).getString("product_num"));
	 				pdd.put("var4", listpd.get(i).getString("bar_code"));
	 				pdd.put("var5", listpd.get(i).getString("product_name"));
	 				pdd.put("var6", listpd.get(i).getString("quantity"));
	 				pdd.put("var7", listpd.get(i).getString("final_quantity"));
	 				pdd.put("var8", listpd.get(i).getString("gift_quantity"));
	 				pdd.put("var9", listpd.get(i).getString("merchant_num"));
	 				pdd.put("var10", listpd.get(i).getString("merchant_name"));
	 				pdd.put("var11", listpd.get(i).getString("short_name"));
	 				varList.add(pdd);
 				}
	 			map.put("varList", varList);
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
 			}else{
 				map.put("varList", new ArrayList());
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 			sysLogService.saveLog(operationMsg, "成功");
 			}
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 压期到货
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/timearrival")
	public ModelAndView  timearrival(Page page){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="查询门店到货报表";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			
			mv.setViewName("report/timearrival");
		} catch (Exception e) {
			// TODO: handle exception
			//logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}
		return mv;
	}
	
	
	
	
	
	/**
	 * 门店报表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/storeReport")
	public ModelAndView storeReport(Page page){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="查询门店到货报表";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String searchcriteria=pd.getString("searchcriteria");
			String keyword=pd.getString("keyword").trim();
			if(keyword!=null && keyword!=""){
				pd.put("searchcriteria", searchcriteria);
				pd.put("keyword", keyword);
			}
			if(!pd.containsKey("type")){
				pd.put("type",0);
			}
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			pd.put("area_num", LoginUtil.getLoginUser().getUSERNAME());
			page.setPd(pd);
			List<PageData> listpd=reportService.listReport(page);
			mv.addObject("listpd", listpd);
			mv.addObject("pd", pd);
			mv.setViewName("report/storeReport");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 门店到货导出
	 * @return
	 */
	@RequestMapping(value="/exportexcel")
	public ModelAndView exportexcel(){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="导出门店到货Excel";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String searchcriteria=pd.getString("searchcriteria");
			String keyword=pd.getString("keyword").trim();
			if(keyword!=null && keyword!=""){
				pd.put("searchcriteria", searchcriteria);
				pd.put("keyword", keyword);
			}
			if(!pd.containsKey("type")){
				pd.put("type",0);
			}
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			pd.put("area_num", LoginUtil.getLoginUser().getUSERNAME());
			Map<String, Object> map=new HashMap<String,Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("门店");
			titles.add("门店编码");
			titles.add("简称");
			titles.add("订单总额");
			map.put("titles", titles);
			List<PageData> listpd=reportService.listReportexport(pd);
			List<PageData> varlist=new ArrayList<PageData>();
			if(listpd.size()>0){
				for (int i = 0; i < listpd.size(); i++) {
					PageData pdd=new PageData();
					pdd.put("var1",listpd.get(i).getString("merchant_name"));
					pdd.put("var2",listpd.get(i).getString("merchant_num"));
					pdd.put("var3",listpd.get(i).getString("short_name"));
					pdd.put("var4",listpd.get(i).getString("sumprice"));
					varlist.add(pdd);
				}
			}
			map.put("varList", varlist);
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
	
	/**
	 * 月季情况报表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/listmonthlyreport")
	public ModelAndView listmonthlyreport(Page page){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="月季情况报表";
		try {
			pd=this.getPageData();
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			pd.put("area_num", LoginUtil.getLoginUser().getUSERNAME());
			String searchcriteria=pd.getString("searchcriteria");
			String keyword=pd.getString("keyword").trim();
			if(keyword!=null && keyword!=""){
				pd.put("searchcriteria", searchcriteria);
				pd.put("keyword", keyword);
			}
			page.setPd(pd);
			List<PageData> listpd=reportService.listmonthlyreport(page);
			mv.addObject("listpd", listpd);
			mv.addObject("pd", pd);
			mv.setViewName("report/monthlyReport");
		} catch (Exception e) {
			// TODO: handle exception
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 月季情况报表导出
	 * @return
	 */
	@RequestMapping(value="/monthlyexport")
	public ModelAndView monthlyexport(){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		String operationMsg="导出月季情况报表Excel";
		logBefore(logger, operationMsg);
		try {
			pd=this.getPageData();
			String searchcriteria=pd.getString("searchcriteria");
			String keyword=pd.getString("keyword").trim();
			if(keyword!=null && keyword!=""){
				pd.put("searchcriteria", searchcriteria);
				pd.put("keyword", keyword);
			}
			pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
			pd.put("area_num", LoginUtil.getLoginUser().getUSERNAME());
			Map<String, Object> map=new HashMap<String,Object>();
			List<String> titles=new ArrayList<String>();
			titles.add("期次");
			titles.add("站点数量");
			titles.add("订购总额");
			titles.add("实际总额");
			titles.add("金额到货率");
			titles.add("数量到货率");
			map.put("titles", titles);
			List<PageData> listpd=reportService.listmonthlyreportexport(pd);
			List<PageData> varlist=new ArrayList<PageData>();
			if(listpd.size()>0){
				for (int i = 0; i < listpd.size(); i++) {
					PageData pdd=new PageData();
					pdd.put("var1",listpd.get(i).getString("b")+"月"+"第"+listpd.get(i).getString("a")+"期");
					pdd.put("var2",listpd.get(i).getString("zdsl")); //门店站点
					pdd.put("var3",listpd.get(i).getString("sumprice"));//订单总额
					pdd.put("var4",listpd.get(i).getString("sale_final_quantity"));//订单总额
					double jine=DoubleUtil.div(Double.valueOf(listpd.get(i).getString("sale_final_quantity")), Double.valueOf(listpd.get(i).getString("sumprice")),3);
					pdd.put("var5",(jine*100)+"%");//订单总额
					double quantity=DoubleUtil.div(Double.valueOf(listpd.get(i).getString("final_quantity")), Double.valueOf(listpd.get(i).getString("quantity")), 3);
					pdd.put("var6",(quantity*100)+"%");//订单总额
					varlist.add(pdd);
				}
			}
			PageData jj=reportService.monthlyjj(pd);
			jj.put("var1","加急订单"); //门店站点
			jj.put("var2",jj.getString("qb")); //门店站点
			jj.put("var3",jj.getString("je")); //门店站点
			
			varlist.add(jj);
			map.put("varList", varlist);
 			ObjectExcelView erv = new ObjectExcelView();
 			mv=new ModelAndView(erv,map);
 			sysLogService.saveLog(operationMsg, "成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		}finally{
			logEnd(logger, operationMsg);
		}
		return mv;
	}
	
	/**
	 * 站点收货情况
	 * @param page
	 * @param type
	 * @return
	 */
	@RequestMapping({ "Collectgoods" })
	public ModelAndView Collectgoods(Page page) {
		ModelAndView mv = getModelAndView();
		try {
			PageData pd = new PageData();
			List<PageData> listitem=new ArrayList<PageData>();
			pd = getPageData();
			String searchcriteria = pd.getString("searchcriteria");
			String keyword = pd.getString("keyword");
			//{keyword=, lastLoginEnd=2018-07-05, lastLoginStart=2018-06-01, searchcriteria=2}
			if((keyword != null) && (!"".equals(keyword))||!pd.getString("lastLoginEnd").equals("")||!pd.getString("lastLoginStart").equals("")){
				if ((keyword != null) && (!"".equals(keyword))) {
					keyword = keyword.trim();
					pd.put("keyword", keyword);
					pd.put("searchcriteria", searchcriteria);
				}
				List<PageData> list=reportService.selectExorder(pd);
				int num=0;
				JSONArray arr = this.reportService.selectEx(list);
				if(arr.size()>0){
					List<PageData> merchantlist=reportService.selectmerchant();
					for(int i=0;i<arr.size();i++){
						JSONObject oo=arr.getJSONObject(i);
						PageData pg=new PageData();
						pg.put("plan_order",oo.getString("ordernumber"));
						pg.put("order_num",oo.getString("wave_number"));
						pg.put("merchant_num",oo.getString("site"));
						if(oo.containsKey("create_time")){
							pg.put("create_time", oo.getString("create_time"));
						}
						for(PageData merchant:merchantlist){
							if(pg.getString("merchant_num").equals(merchant.getString("merchant_num"))){
								pg.put("merchant_name",merchant.getString("merchant_name"));
							}
						}
						if(oo.getInteger("state")>=2){
							num++;
						}
						pg.put("type",oo.getString("state"));
						listitem.add(pg);
					}
				}
				mv.addObject("varList", listitem);
				pd.put("qb",list.size());
				pd.put("yf",arr.size());
				pd.put("ws",num);
				if(num==0){
					pd.put("dy", "0");
				}else{
					pd.put("dy", DoubleUtil.div(Double.valueOf(num), Double.valueOf(list.size()), 2));
				}
			}
			mv.setViewName("report/collect_list");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	@RequestMapping(value="/CollectgoodsExcel")
 	public ModelAndView CollectgoodsExcel(){
 		ModelAndView mv=new ModelAndView();
 		PageData pd=new PageData();
 		String operationMsg="加油站收货情况";
 		logBefore(logger, operationMsg);
 		try {
 			pd=this.getPageData();
 			Map<String,Object> map=new HashMap<String, Object>();
 			List<String> titles=new ArrayList<String>();
 			titles.add("计划单号"); // 3
 			titles.add("出库单号"); // 3
 			titles.add("站点名称"); // 1
 			titles.add("门店编码"); // 5
 			titles.add("是否收货"); // 5
 			titles.add("收货时间"); // 5
 			titles.add("是否评价"); // 5
 			titles.add("运输状态"); // 5
 			map.put("titles", titles);
 			List<PageData> listitem=new ArrayList<PageData>();
 			List<PageData> list=reportService.selectExorder(pd);
			int num=0;
			JSONArray arr = this.reportService.selectEx(list);
			if(arr.size()>0){
				List<PageData> merchantlist=reportService.selectmerchant();
				for(int i=0;i<arr.size();i++){
					JSONObject oo=arr.getJSONObject(i);
					PageData pg=new PageData();
					pg.put("var1",oo.getString("ordernumber"));
					pg.put("var2",oo.getString("wave_number"));
					pg.put("var4",oo.getString("site"));
					for(PageData merchant:merchantlist){
						if(pg.getString("merchant_num").equals(merchant.getString("merchant_num"))){
							pg.put("var3",merchant.getString("merchant_name"));
						}
					}
					if(oo.getInteger("state")>=2){
						num++;
						pg.put("var5","已收货");
						pg.put("var6",oo.getString("create_time"));
						if(oo.getInteger("state")==2){
							pg.put("var7","未评价");
						}else{
							pg.put("var7","以评价");
						}
						pg.put("var8","已送达");
					}else{
						pg.put("var5","未收货");
						pg.put("var6","");
						pg.put("var7","未评价");
						pg.put("var8","未送达");
					}
					pg.put("type",oo.getString("state"));
					listitem.add(pg);
				}
			}
			PageData pg2=new PageData();
			pg2.put("var1", "总须发货"+list.size()+"站，");
			pg2.put("var2", "已发货"+arr.size()+"站，");
			pg2.put("var3", "已收货"+num+"站，");
			if(num==0){
				pg2.put("var4", "0%");
			}else{
				pg2.put("var4", "完成率"+DoubleUtil.div(Double.valueOf(num), Double.valueOf(list.size()), 2)*100+"%");
			}
			
 			listitem.add(pg2);
			map.put("varList", listitem);
 			ObjectExcelView erv = new ObjectExcelView();
 			mv=new ModelAndView(erv,map);
 		} catch (Exception e) {
 			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
 		}finally{
 			logEnd(logger, operationMsg);
 		}
 		return mv;
 	}
	
	
	/**
	 * 计划单执行情况
	 * @param page
	 * @param type
	 * @return
	 */
	@RequestMapping({ "planimplement" })
	public ModelAndView planimplement(Page page) {
		ModelAndView mv = getModelAndView();
		try {
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
			List<PageData> varList = this.reportService.planimplement(page);
			PageData pg=reportService.plandown(pd);
			mv.setViewName("report/planimplement");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
			mv.addObject("pg", pg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	 @RequestMapping(value="/planimplementExcel")
	 	public ModelAndView planimplementExcel(){
	 		ModelAndView mv=new ModelAndView();
	 		PageData pd=new PageData();
	 		String operationMsg="ProductExcel导出库存操作";
	 		logBefore(logger, operationMsg);
	 		try {
	 			pd=this.getPageData();
	 			Map<String,Object> map=new HashMap<String, Object>();
	 			List<String> titles=new ArrayList<String>();
	 			titles.add("门店名称"); // 1
	 			titles.add("门店简称"); // 2
	 			titles.add("门店编码"); // 5
	 			titles.add("出库单号"); // 3
	 			titles.add("计划单号"); // 3
	 			titles.add("状态"); // 2
	 			map.put("titles", titles);
	 			List<PageData> listpd=reportService.planimplementExcel(pd);
	 			PageData pg=reportService.plandown(pd);
	 			List<PageData> varList=new ArrayList<PageData>();
	 			Double lv=DoubleUtil.div(Double.valueOf(pg.getString("ws")), Double.valueOf(pg.getString("qb")), 4);
	 			pg.put("var1", "发货"+pg.getString("qb")+"站，");
	 			pg.put("var2", "已收货"+pg.getString("ws")+"站，");
	 			pg.put("var3", "完成率"+lv*100+"%");
	 			varList.add(pg);
	 			for (int i = 0; i < listpd.size(); i++) {
	 				PageData pdd=new PageData();
	 				pdd.put("var1", listpd.get(i).getString("merchant_name"));
	 				pdd.put("var2", listpd.get(i).getString("short_name"));
	 				pdd.put("var3", listpd.get(i).getString("merchant_num"));
	 				pdd.put("var4", listpd.get(i).getString("order_num"));
	 				pdd.put("var5", listpd.get(i).getString("plan_order"));
	 				if(listpd.get(i).getString("type").equals("3")){
	 					pdd.put("var6", "已完成");
	 				}else{
	 					pdd.put("var6", "未完成");
	 				}
	 				varList.add(pdd);
	 			}
	 			map.put("varList", varList);
	 			ObjectExcelView erv = new ObjectExcelView();
	 			mv=new ModelAndView(erv,map);
	 		} catch (Exception e) {
	 			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
	 		}finally{
	 			logEnd(logger, operationMsg);
	 		}
	 		return mv;
	 	}
	 
	 /******** 开始*********************/
	 	/**
	 	 * 中央仓盘点商品类别明细
	 	 * @param page
	 	 * @return
	 	 * @author gyy
	 	 */
	 	@RequestMapping({ "findpdmx" })
		public ModelAndView findpdmx(Page page) {
			ModelAndView mv = getModelAndView();
			try {
				PageData pd = new PageData();
				pd = getPageData();
			
				page.setPd(pd);
				List<PageData> varList = this.reportService.findpdmx(page);
				mv.setViewName("report/zycpdmx");
				mv.addObject("varList", varList);
				mv.addObject("pd", pd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mv;
		}
	 	/**
	 	 * 导出中央仓盘点商品类别明细
	 	 * @return
	 	 */
	  	@RequestMapping({"/excel"})
	  	public ModelAndView excel(){
	  		ModelAndView mv=new ModelAndView();
	  		PageData pd=new PageData();
	  		try {
				pd=this.getPageData();
				
				Map<String,Object> map=new HashMap<>();
				List<String> titles=new ArrayList<>();
				titles.add("商品类别");
				titles.add("HOS数量");
				titles.add("HOS金额");
				titles.add("实盘数量");
				titles.add("实盘金额");
				titles.add("盈亏数量");
				titles.add("盈亏金额");
				map.put("titles", titles);
				List<PageData> listPd=reportService.findpdmx2(pd);
				List<PageData> varList=new ArrayList<>();
				for (int i = 0; i < listPd.size(); i++) {
			          PageData vpd = new PageData();
			          vpd.put("var1", listPd.get(i).getString("classify_name"));
			          vpd.put("var2", listPd.get(i).getString("pro_num"));
			          vpd.put("var3", listPd.get(i).getString("hos_amount"));
			          vpd.put("var4", listPd.get(i).getString("product_quantity"));
			          vpd.put("var5", listPd.get(i).getString("price"));
			          vpd.put("var6", listPd.get(i).getString("yksl"));
			          vpd.put("var7", listPd.get(i).getString("ykje"));
			          varList.add(vpd);
			    }
				map.put("varList", varList);
				ObjectExcelView erv = new ObjectExcelView();
				mv = new ModelAndView(erv, map);
			} catch (Exception e) {
				this.logger.error(e.toString(), e);
			}
	  		return mv;
	  	}
	  	/**
	  	 * 跳转导入页面
	  	 * @return
	  	 * @throws Exception
	  	 */
	  	@RequestMapping({ "/goImportExcelPage" })
		public ModelAndView goImportExcelPage() throws Exception {
			ModelAndView mv = getModelAndView();
			mv.setViewName("report/impExcel");
			return mv;
		}
	  	
	  	/**
	  	 * 下载导入模板
	  	 * @param response
	  	 * @throws Exception
	  	 */
		@RequestMapping({ "/downExcel" })
		public void downExcel(HttpServletResponse response) throws Exception {
			FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "zyckcpd.xlsx",
					"Tarot系统-批量导入中央仓盘点商品信息模版.xls");
		}
		
	
		 /**
		  * 配送导入 
		  * @param file
		  * @return
		  */
		@RequestMapping(value = "/readExcel")
		public ModelAndView readExcel(
				@RequestParam(value = "excel", required = false) MultipartFile file) {
			ModelAndView mv = this.getModelAndView();
			String operationMsg = "导入配发出库单到数据库";
			logBefore(logger, operationMsg);
			try {
				User user = (User) LoginUtil.getLoginUser();
				if (null != file && !file.isEmpty()) {
					String filePath = PathUtil.getClasspath() + Const.FILEPATHFILE;
					String fileName = FileUpload.fileUp(file, filePath,"readExcel");
					// 执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
					List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);
					/**
					 * var0 :（HOS）数量  var1 :（HOS）金额  var2 :商品编码（必填）  var3 :条形码（必填）  var4 :数量  var5:生产日期（必填）
					 */
					List<PageData> errors = new ArrayList<PageData>();  //错误信息
					List<PageData> pdM=new ArrayList<PageData>();  //hos表信息
					List<PageData> pdS=new ArrayList<PageData>();	//保存商品库存信息
					List<PageData> pdS2=new ArrayList<PageData>();	//更新商品库存信息
					if (listPd != null && listPd.size() > 0) {
						for (int i = 0; i < listPd.size(); i++) {
							Product product=null;
							PageData pdE=new PageData();  //收集返回信息
							PageData pdE2=new PageData();  //收集错误信息
							PageData pdP=new PageData();  //保存信息
							PageData hosinfo = new PageData();  //保存hos信息
							PageData kcinfo = new PageData();  //保存库存信息
							PageData page=listPd.get(i);
							String mendname = "";
							if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var0").toString())) {
								hosinfo.put("hos_number",page.get("var0").toString()); 
							} else {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row0", "A");
								pdE.put("reason0", "hos数量为空，默认为0");
								hosinfo.put("hos_number",0);
							}
							if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var1").toString())) {
								hosinfo.put("hos_amount",page.get("var1").toString()); 
							} else {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row0", "B");
								pdE.put("reason0", "hos金额为空，默认为0");
								hosinfo.put("hos_amount",0);
							}
							
							if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var2").toString())) {
								pdP.put("product_num",page.get("var2").toString());  
							} else {
								pdE2.put("line", Integer.valueOf(i + 2));
								pdE2.put("row0", "C");
								pdE2.put("reason0", "商品编码为空");
							}
							
							if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var3").toString())) {
								pdP.put("bar_code",page.get("var3").toString()); 
							} else {
								pdE2.put("line", Integer.valueOf(i + 2));
								pdE2.put("row0", "D");
								pdE2.put("reason0", "条形码为空");
							}
							//判断商品编码和条码都不为空 则查询商品信息
							if(!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var2").toString()) && 
									!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var3").toString())){
								String pronum =  listPd.get(i).get("var2").toString();
								String barcode = listPd.get(i).get("var3").toString();
								PageData pgd = new PageData();
								pgd.put("pronum", pronum);
								pgd.put("barcode", barcode);
								PageData pda = reportService.findpro(pgd);
								if(pda == null){
									pdE2.put("line", Integer.valueOf(i + 2));
									pdE2.put("row0", "C、D");
									pdE2.put("reason0", "查无该商品信息");
								}else{
									hosinfo.put("pro_id", pda.getString("id"));
									hosinfo.put("pro_num", pda.getString("product_num"));
									pdM.add(hosinfo);  //hos表信息
									kcinfo.put("product_id", pda.getString("id"));
									kcinfo.put("warehouse_id", 1);
									kcinfo.put("cargo_space_id", pda.getString("cargo_space_id"));
								}
							}else {
								pdE2.put("line", Integer.valueOf(i + 2));
								pdE2.put("row0", "C、D");
								pdE2.put("reason0", "商品编码或商品条形码为空");
							}
							
							
							if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var4").toString())) {
								kcinfo.put("product_quantity", page.get("var4").toString());
							//	kcinfo.put("total_price", page.get("var4").toString());
							} else {
								pdE.put("line", Integer.valueOf(i + 2));
								pdE.put("row0", "E");
								pdE.put("reason0", "数量为空,默认为0");
								kcinfo.put("product_quantity", 0);
								kcinfo.put("total_price", 0);
							}
							if (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var5").toString())) {
								kcinfo.put("product_date", page.get("var5").toString());
								//如果商品id不为空 则查询库存表
								if(kcinfo.getString("product_id") != null){
									PageData pad = reportService.findKC(kcinfo);
									if(pad != null){
										//更新的库存信息
									//	reportService.updateKC(kcinfo);
										pdS2.add(kcinfo);
									}else{
										pdS.add(kcinfo);   //需要新增的库存信息
									}
								}
							} else {
								pdE2.put("line", Integer.valueOf(i + 2));
								pdE2.put("row0", "F");
								pdE2.put("reason0", "商品生产日期位为空");
							}
							if ((pdE2 != null) && (pdE2.getString("line") != null) && (pdE2.getString("line") != "")) {
								errors.add(pdE2);
							}
						}
						if(errors.size()>0){
							pdS.clear();  //清空数组
							pdM.clear();
							mv.addObject("varList2",errors);
						}
						if(pdM.size() > 0){
							mv.addObject("Msg", reportService.importExcel(pdM, pdS,pdS2));
						}
					} else {
						// 表格中数据为空
						mv.addObject("Msg", "数据表中没有数据");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				//logMidway(logger, operationMsg + "，出现错误：" + e.toString(), 1);
			} finally {
				mv.setViewName("report/impExcel");
				logEnd(logger, operationMsg);
			}
			return mv;
		}
	  /******** end *********************/
}
