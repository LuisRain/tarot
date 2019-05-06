package com.hy.service.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.hy.dao.DaoSupport;
import com.hy.util.DoubleUtil;
import com.hy.util.PageData;
import com.hy.util.Tools;
@Service("archives")
public class archivesExcel extends AbstractExcelView {
	
	  @Resource(name="daoSupport")
	  public DaoSupport dao;

   public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
	Date date = new Date();
	String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
	PageData sup=(PageData) model.get("pd");
	List<PageData> suplist=(List<PageData>) dao.findForList("SupplierMapper.supplierexcel", sup);
	for(int su=0;su<suplist.size();su++){
		PageData pd=suplist.get(su);
		pd.put("supplier_id", pd.getString("id"));
		List<PageData> findcontract=(List<PageData>) dao.findForList("SupplierMapper.findcontract", pd);
		List<PageData> findbrand=(List<PageData>) dao.findForList("SupplierMapper.findbrand", pd);
		PageData findarchives=(PageData)this.dao.findForObject("SupplierMapper.findarchives", pd);
		PageData supp=(PageData)this.dao.findForObject("SupplierMapper.findById", pd);
		 HSSFSheet sheet = workbook.createSheet(pd.getString("supplier_name")+pd.getString("id"));
		 sheet.setDefaultColumnWidth(20);
		    sheet.setDefaultRowHeightInPoints(20);
		    HSSFRow row = sheet.createRow(0);
		    //row.setHeightInPoints(20);
		    HSSFCell cell = row.createCell(0);
		    int hang=0;
		    String title = "统采供应商档案卡";
			HSSFFont font = workbook.createFont();
			font.setFontName("仿宋_GB2312");
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			font.setFontHeightInPoints((short) 12);
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();  
			cellStyle.setFont(font);  	
			cell.setCellStyle(cellStyle);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
			
			HSSFCellStyle cellStyle2 = workbook.createCellStyle();  
			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中 
			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
			cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN); 
			
			
			
		    //参数说明：1：开始行 2：结束行  3：开始列 4：结束列
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang+2,0,3));  //合并单元格
		    setText(cell, title);
		    hang=hang+3;
		    setText(getCell(sheet,hang, 0), "档案编号");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
		    setText(getCell(sheet,hang, 1), supp.getString("id"));
		    getCell(sheet,hang, 1).setCellStyle(cellStyle2);
		    setText(getCell(sheet,hang, 2), "建档日期");
		    getCell(sheet,hang, 2).setCellStyle(cellStyle2);
		    setText(getCell(sheet,hang, 3), supp.getString("CREATE_DATE"));
		    getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang+2,0,3));  //合并单元格
		    getCell(sheet,hang, 0).setCellStyle(cellStyle);
		    setText(getCell(sheet,hang, 0), "企业基本情况");
		    hang=hang+3;
		    
		    //getCell(sheet,hang, 0).
		    setText(getCell(sheet,hang, 0), "供应商名称");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
		    setText(getCell(sheet,hang, 1), supp.getString("SUPPLIER_NAME"));
		    getCell(sheet,hang, 1).setCellStyle(cellStyle2);
		    setText(getCell(sheet,hang, 2), "供应商编码");
		    getCell(sheet,hang,2).setCellStyle(cellStyle2);
		    setText(getCell(sheet,hang, 3),supp.getString("SUPPLIER_NUM"));
		    getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    

		    setText(getCell(sheet,hang, 0), "地址");
		    setText(getCell(sheet,hang, 1), supp.getString("ADDRESS"));
		    setText(getCell(sheet,hang, 2), "统一社会信息用代码");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	 setText(getCell(sheet,hang, 3), findarchives.getString("codes"));
		    }else{
		    	 setText(getCell(sheet,hang, 3), "");
		    }
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 1).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 2).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    
		    setText(getCell(sheet,hang, 0), "注册资金（万元）");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	setText(getCell(sheet,hang, 1),  findarchives.getString("registered_capital"));
		    }else{
		    	 setText(getCell(sheet,hang, 1), "");
		    }
		    
		    setText(getCell(sheet,hang, 2), "员工人数");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	 setText(getCell(sheet,hang, 3),  findarchives.getString("people"));
		    }else{
		    	 setText(getCell(sheet,hang, 3), "");
		    }
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 1).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 2).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    
		    setText(getCell(sheet,hang, 0), "法人代表");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	 setText(getCell(sheet,hang, 1),  findarchives.getString("representative"));
		    }else{
		    	 setText(getCell(sheet,hang, 1), "");
		    }
		    setText(getCell(sheet,hang, 2), "法人身份证号码");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	 setText(getCell(sheet,hang, 3),  findarchives.getString("id_number"));
		    }else{
		    	 setText(getCell(sheet,hang, 3), "");
		    }
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 1).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 2).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    
		    setText(getCell(sheet,hang, 0), "经营范围");
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang,1,3));  //合并单元格
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	setText(getCell(sheet,hang, 1), findarchives.getString("operation"));
		    }else{
		    	 setText(getCell(sheet,hang, 1), "");
		    }
		    
		    hang++;
		    
		    setText(getCell(sheet,hang, 2), "联系人（常用联系人）");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	 setText(getCell(sheet,hang, 3),  findarchives.getString("contacts"));
		    }else{
		    	 setText(getCell(sheet,hang, 3), "");
		    }
		    setText(getCell(sheet,hang, 0), "联系电话");
		    if(findarchives!=null&&!findarchives.equals("null")){
		    	 setText(getCell(sheet,hang, 1),  findarchives.getString("phone"));
		    }else{
		    	 setText(getCell(sheet,hang, 1), "");
		    }
		   
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 1).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 2).setCellStyle(cellStyle2);
		    getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang+2,0,3));  //合并单元格
		    getCell(sheet,hang, 0).setCellStyle(cellStyle);
		    setText(getCell(sheet,hang, 0), "商品基本情况");
		    hang=hang+3;
		    if(null!=findcontract){
		    	   for(int i=0;i<findcontract.size();i++){
				    	setText( getCell(sheet,hang, 0),"第"+(i+1)+"次签订合同年份");
				    	setText( getCell(sheet,hang,1),findcontract.get(i).getString("start_time").substring(0, 10));
				    	setText( getCell(sheet,hang, 2),"2018年合同的到期日");
				    	setText( getCell(sheet,hang, 3),findcontract.get(i).getString("end_time").substring(0, 10));
				    	getCell(sheet,hang, 0).setCellStyle(cellStyle2);
				    	getCell(sheet,hang, 1).setCellStyle(cellStyle2);
				    	getCell(sheet,hang, 2).setCellStyle(cellStyle2);
				    	getCell(sheet,hang, 3).setCellStyle(cellStyle2);
				    	hang++;
				    	setText( getCell(sheet,hang, 0),"目前合作情况(正常，停止)");
				    	if(findcontract.get(i).getString("state").equals("0")){
				    		setText( getCell(sheet,hang, 1),"正常");
				    	}else{
				    		setText( getCell(sheet,hang, 1),"停止");
				    	}
				    	setText( getCell(sheet,hang, 2),"合作预期");
				    	setText( getCell(sheet,hang, 3),findcontract.get(i).getString("end_time").substring(0, 10));
				    	getCell(sheet,hang, 0).setCellStyle(cellStyle2);
				    	getCell(sheet,hang, 1).setCellStyle(cellStyle2);
				    	getCell(sheet,hang, 2).setCellStyle(cellStyle2);
				    	getCell(sheet,hang, 3).setCellStyle(cellStyle2);
				    	hang++;
				    }
		    }
		    setText(getCell(sheet,hang,0),"合作品牌");
		    setText(getCell(sheet,hang,1),"经营品牌名称");
		    setText(getCell(sheet,hang,2),"生产厂家或经销商级别");
		    setText(getCell(sheet,hang,3),"授权销售的区域或范围");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    if(null!=findbrand){
		    	for(int i=0;i<findbrand.size();i++){
			    	setText(getCell(sheet,hang,0),"品牌"+(i+1));
			        setText(getCell(sheet,hang,1),findbrand.get(i).getString("brand"));
			        setText(getCell(sheet,hang,2),findbrand.get(i).getString("levels"));
			        setText(getCell(sheet,hang,3),findbrand.get(i).getString("ranges"));
			        getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			    	getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			    	getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			    	getCell(sheet,hang, 3).setCellStyle(cellStyle2);
			        hang++;
			    }
		    }
		    getCell(sheet,hang,0).setCellStyle(cellStyle);
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang+2,0,3));  //合并单元格
		    setText(getCell(sheet,hang,0),"合作基本情况");
		    hang=hang+3;
		    setText(getCell(sheet,hang,0),"商品数量（sku)");
		    setText(getCell(sheet,hang,1),"");
		    setText(getCell(sheet,hang,2),"月度平均采购量（万元）");
		    setText(getCell(sheet,hang,3),"");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    setText(getCell(sheet,hang,0),"平均每年促销次数");
		    setText(getCell(sheet,hang,1),"");
		    setText(getCell(sheet,hang,2),"返利政策（费用比例）");
		    setText(getCell(sheet,hang,3),"");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    setText(getCell(sheet,hang,0),"商品平均毛利率（%）");
		    setText(getCell(sheet,hang,1),"");
		    setText(getCell(sheet,hang,2),"商品最低毛利率");
		    setText(getCell(sheet,hang,3),"");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    setText(getCell(sheet,hang,0),"商品价格与超市、便利店对比情况");
		    setText(getCell(sheet,hang,1),"");
		    setText(getCell(sheet,hang,2),"商品调价机制");
		    setText(getCell(sheet,hang,3),"");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			getCell(sheet,hang, 3).setCellStyle(cellStyle2);
		    hang++;
		    getCell(sheet,hang,0).setCellStyle(cellStyle);
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang+2,0,5));  //合并单元格
		    setText(getCell(sheet,hang,0),"统采供应商采购记录");
		    List<PageData> pur=(List<PageData>) dao.findForList("SupplierMapper.findpurorder", pd);
		    
		    hang=hang+3;
		    setText(getCell(sheet,hang,0),"批次号");
		    setText(getCell(sheet,hang,1),"采购数量");
		    setText(getCell(sheet,hang,2),"到货数量");
		    setText(getCell(sheet,hang,3),"采购金额");
		    setText(getCell(sheet,hang,4),"到货金额（含税");
		    setText(getCell(sheet,hang,5),"到货率（按数量计算）");
		    getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			getCell(sheet,hang, 3).setCellStyle(cellStyle2);
			getCell(sheet,hang, 4).setCellStyle(cellStyle2);
			getCell(sheet,hang, 5).setCellStyle(cellStyle2);
		    hang++;
		    if(pur.size()>0){
		    	 for(int i=0;i<pur.size();i++){
			    	 setText(getCell(sheet,hang,0),pur.get(i).getString("group_num"));
			    	 setText(getCell(sheet,hang,1),pur.get(i).getString("quantity"));
			    	 setText(getCell(sheet,hang,2),pur.get(i).getString("final_quantity"));
			    	 setText(getCell(sheet,hang,3),pur.get(i).getString("cgje"));
			    	 setText(getCell(sheet,hang,4),pur.get(i).getString("dhje"));
			    	 setText(getCell(sheet,hang,5),DoubleUtil.div(Double.valueOf(pur.get(i).getString("final_quantity")), Double.valueOf(pur.get(i).getString("quantity")), 2).toString());
			    	 getCell(sheet,hang, 0).setCellStyle(cellStyle2);
			    		getCell(sheet,hang, 1).setCellStyle(cellStyle2);
			    		getCell(sheet,hang, 2).setCellStyle(cellStyle2);
			    		getCell(sheet,hang, 3).setCellStyle(cellStyle2);
			    		getCell(sheet,hang, 4).setCellStyle(cellStyle2);
			    		getCell(sheet,hang, 5).setCellStyle(cellStyle2);
			    	 hang++;
			    }
		    }
		   
		    getCell(sheet,hang,0).setCellStyle(cellStyle);
		    sheet.addMergedRegion(new CellRangeAddress(hang,hang+2,0,12));  //合并单元格
		    setText(getCell(sheet,hang,0),"统采商品信息及报价表");
		    List<PageData> productlist=(List<PageData>) dao.findForList("SupplierMapper.findtcproduct", pd);
		    hang=hang+3;
		    setText(getCell(sheet,hang,0),"序号");
			 setText(getCell(sheet,hang,1),"商品名称");
			 setText(getCell(sheet,hang,2),"商品编码");
			 setText(getCell(sheet,hang,3),"条形码");
			 setText(getCell(sheet,hang,4),"计量单位");
			 
			 setText(getCell(sheet,hang,5),"包装单位");
			 setText(getCell(sheet,hang,6),"包装数量");
			 setText(getCell(sheet,hang,7),"供货价格");
			 setText(getCell(sheet,hang,8),"建议零售价");
			 
			 setText(getCell(sheet,hang,9),"毛利率");
			 setText(getCell(sheet,hang,10),"大类");
			 setText(getCell(sheet,hang,11),"保质期天数");
			 setText(getCell(sheet,hang,12),"税率");
			 getCell(sheet,hang, 0).setCellStyle(cellStyle2);
				getCell(sheet,hang, 1).setCellStyle(cellStyle2);
				getCell(sheet,hang, 2).setCellStyle(cellStyle2);
				getCell(sheet,hang, 3).setCellStyle(cellStyle2);
				getCell(sheet,hang, 4).setCellStyle(cellStyle2);
				getCell(sheet,hang, 5).setCellStyle(cellStyle2);
				getCell(sheet,hang, 6).setCellStyle(cellStyle2);
				getCell(sheet,hang, 7).setCellStyle(cellStyle2);
				getCell(sheet,hang, 8).setCellStyle(cellStyle2);
				getCell(sheet,hang, 9).setCellStyle(cellStyle2);
				getCell(sheet,hang, 10).setCellStyle(cellStyle2);
				getCell(sheet,hang, 11).setCellStyle(cellStyle2);
				getCell(sheet,hang, 12).setCellStyle(cellStyle2);
			 hang++;
			 if(productlist.size()>0){
				  for(int i=0;i<productlist.size();i++){
				    	 setText(getCell(sheet,hang,0),String.valueOf((i+1)));
				    	 setText(getCell(sheet,hang,1),productlist.get(i).getString("product_name"));
				    	 setText(getCell(sheet,hang,2),productlist.get(i).getString("product_num"));
				    	 setText(getCell(sheet,hang,3),productlist.get(i).getString("bar_code"));
				    	 setText(getCell(sheet,hang,4),productlist.get(i).getString("unit_name"));
				    	 
				    	 setText(getCell(sheet,hang,5),"箱");
				    	 setText(getCell(sheet,hang,6),productlist.get(i).getString("box_number"));
				    	 setText(getCell(sheet,hang,7),productlist.get(i).getString("pur_price"));
				    	 setText(getCell(sheet,hang,8),productlist.get(i).getString("sale_price"));
				    	 
				    	 setText(getCell(sheet,hang,9),"");
				    	 setText(getCell(sheet,hang,10),productlist.get(i).getString("classify_name"));
				    	 setText(getCell(sheet,hang,11),productlist.get(i).getString("expire_days"));
				    	 setText(getCell(sheet,hang,12),productlist.get(i).getString("taxes"));
				    	 getCell(sheet,hang, 0).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 1).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 2).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 3).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 4).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 5).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 6).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 7).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 8).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 9).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 10).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 11).setCellStyle(cellStyle2);
				 		getCell(sheet,hang, 12).setCellStyle(cellStyle2);
				    	 hang++;
				    }
			 }
		  
		
		
		
	}
	
	
	
	
 

   
    
    
    
    
    

  }
}


