package com.hy.controller.pfckd;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.hy.util.PageData;

/**
 * 导出excel
 * @author Fanta
 *
 */
public class ExpExcel {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,List<PageData> values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }
        //合并单元格
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,7);  //起始行,结束行,起始列,结束列   //2018年6月统采供应商结算表
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(1,1,0,5); //单位名称：中国石油山西销售公司
        CellRangeAddress callRangeAddress2 = new CellRangeAddress(1,1,6,7); //单位：元
        
        CellRangeAddress callRangeAddress3 = new CellRangeAddress(2,4,0,0);	//序号
        CellRangeAddress callRangeAddress4 = new CellRangeAddress(2,4,1,1);	//单位名称
        CellRangeAddress callRangeAddress5 = new CellRangeAddress(2,2,2,3);	//应付帐款
        CellRangeAddress callRangeAddress6 = new CellRangeAddress(2,2,4,5);	//销售及库存情况
        CellRangeAddress callRangeAddress7 = new CellRangeAddress(2,3,6,6);	//申请付款金额
        CellRangeAddress callRangeAddress8 = new CellRangeAddress(2,4,7,7);	//备注
        
        CellRangeAddress callRangeAddress9 = new CellRangeAddress(3,3,2,2);	//含税金额
        CellRangeAddress callRangeAddress10 = new CellRangeAddress(3,3,3,3);	//不含税金额
        CellRangeAddress callRangeAddress11 = new CellRangeAddress(3,3,4,4);	//期末库存商品金额
        CellRangeAddress callRangeAddress12 = new CellRangeAddress(3,3,5,5);	//已销售商品金额
        
        CellRangeAddress callRangeAddress13 = new CellRangeAddress(4,4,2,2);	//（1）
        CellRangeAddress callRangeAddress14 = new CellRangeAddress(4,4,3,3);	//(2)=(1)/税率
        CellRangeAddress callRangeAddress15 = new CellRangeAddress(4,4,4,4);	//(3)
        CellRangeAddress callRangeAddress16 = new CellRangeAddress(4,4,5,5);	//=F5
        CellRangeAddress callRangeAddress17 = new CellRangeAddress(4,4,6,6);	//=G5
       
        HSSFCellStyle headStyle = createCellStyle(wb,(short)16,true,true,false);   //设置样式
        HSSFCellStyle colStyle = createCellStyle(wb,(short)12,true,true,false);
        HSSFCellStyle colorStyle = createCellStyle(wb,(short)12,true,true,true);
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        //加载合并单元格对象 
        sheet.addMergedRegion(callRangeAddress); 
        sheet.addMergedRegion(callRangeAddress1); 
        sheet.addMergedRegion(callRangeAddress2); 
        sheet.addMergedRegion(callRangeAddress3); 
        sheet.addMergedRegion(callRangeAddress4); 
        sheet.addMergedRegion(callRangeAddress5); 
        sheet.addMergedRegion(callRangeAddress6);
        sheet.addMergedRegion(callRangeAddress7); 
        sheet.addMergedRegion(callRangeAddress8); 
        sheet.addMergedRegion(callRangeAddress9);
        sheet.addMergedRegion(callRangeAddress10); 
        sheet.addMergedRegion(callRangeAddress11); 
        sheet.addMergedRegion(callRangeAddress12);
        sheet.addMergedRegion(callRangeAddress13); 
        sheet.addMergedRegion(callRangeAddress14); 
        sheet.addMergedRegion(callRangeAddress15);
        sheet.addMergedRegion(callRangeAddress16); 
        sheet.addMergedRegion(callRangeAddress17); 
        sheet.setDefaultColumnWidth(20);  
        //第一行
        HSSFRow row1 = sheet.createRow(0);
        //加载单元格样式  
        HSSFCell cell2 = row1.createCell(0); 
        cell2.setCellStyle(headStyle);  
        cell2.setCellValue("统采供应商结算表");
        
      //第二行
        HSSFRow row2 = sheet.createRow(1);  
        HSSFCell cellsan = row2.createCell(0);  
        HSSFCell cellsan2 = row2.createCell(6);  
        //加载单元格样式  
        cellsan.setCellValue("单位名称：中国石油山西销售公司");  
        cellsan2.setCellValue("单位：元"); 
        //第三行
        HSSFRow row3 = sheet.createRow(2);
        String[] titles = {"序号","单位名称","应付帐款","","销售及库存情况","","申请付款金额","备注"};
        for(int i=0;i<titles.length;i++)  
        {  
            HSSFCell cell3 = row3.createCell(i); 
            cell3.setCellStyle(colStyle);
            //加载单元格样式  
            cell3.setCellValue(titles[i]);  
        }  
        //第四行
        HSSFRow row4 = sheet.createRow(3);
        String[] titles2 = {"含税金额","不含税金额","期末库存商品金额","已销售商品金额"};
        for(int i=0;i<titles2.length;i++)  
        {  
            HSSFCell cell4 = row4.createCell(i+2);  
            cell4.setCellStyle(colStyle);
            //加载单元格样式  
            cell4.setCellValue(titles2[i]);  
        }  
        //第五行
        HSSFRow row5 = sheet.createRow(4);
        String[] titles3 = {"(1)","(2)=(1)/税率","(3)","(4)=(1)-(3)","(5)≦(4)"};
        for(int i=0;i<titles3.length;i++)  
        {  
            HSSFCell cell3 = row5.createCell(i+2); 
            cell3.setCellStyle(colorStyle);
            //加载单元格样式  
            cell3.setCellValue(titles3[i]);  
        } 
        //加载数据
        if(values.size() > 0){
        	 int num=1;  
        	for (int i = 0; i < values.size(); i++) {
        		 HSSFRow row6 = sheet.createRow(i+5);
        		 HSSFCell cella = row6.createCell(0);
        		 cella.setCellValue(num++);
        		 HSSFCell cellb = row6.createCell(1);
        		 cellb.setCellValue(values.get(i).getString("supplier_name"));
        		 HSSFCell cellc = row6.createCell(2);
        		 cellc.setCellValue(values.get(i).getString("TotalPrice"));
        		 HSSFCell celld = row6.createCell(3);
        		 celld.setCellValue("");
        		 HSSFCell celle = row6.createCell(4);
        		 celle.setCellValue("");
        		 HSSFCell cellf = row6.createCell(5);
        		 cellf.setCellValue("");
        		 HSSFCell cellg = row6.createCell(6);
        		 cellg.setCellValue("");
        		 HSSFCell cellh = row6.createCell(7);
        		 cellh.setCellValue(values.get(i).getString("comment"));
        		 
			}
        	
        }
        HSSFRow rownumber = sheet.createRow(values.size()+5);
        HSSFCell cellnumber = rownumber.createCell(1);  
        HSSFCell cellnumber1 = rownumber.createCell(3);
        HSSFCell cellnumber2 = rownumber.createCell(6);
        cellnumber.setCellStyle(colStyle);
        cellnumber.setCellValue("非油处：");
        cellnumber1.setCellStyle(colStyle);
        cellnumber1.setCellValue("复核：");
        cellnumber2.setCellStyle(colStyle);
        cellnumber2.setCellValue("制表：");
        return wb;
    }
    
    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook2(String sheetName,PageData values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }
        //合并单元格
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,6);  //起始行,结束行,起始列,结束列   
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(1,1,0,6); 
        CellRangeAddress callRangeAddress2 = new CellRangeAddress(2,2,0,6); 
        
        CellRangeAddress callRangeAddress3 = new CellRangeAddress(3,3,0,1);	
        CellRangeAddress callRangeAddress4 = new CellRangeAddress(3,3,2,4);	
        CellRangeAddress callRangeAddress5 = new CellRangeAddress(3,3,5,5);	
        CellRangeAddress callRangeAddress6 = new CellRangeAddress(3,3,6,6);
        
        CellRangeAddress callRangeAddress7 = new CellRangeAddress(4,4,0,1);	
        CellRangeAddress callRangeAddress8 = new CellRangeAddress(4,4,2,4);	
        CellRangeAddress callRangeAddress9 = new CellRangeAddress(4,4,5,5);	
        CellRangeAddress callRangeAddress10 = new CellRangeAddress(4,4,6,6);
        
     
        CellRangeAddress callRangeAddress11 = new CellRangeAddress(5,5,0,1);	
        CellRangeAddress callRangeAddress12 = new CellRangeAddress(5,5,2,6);	
        
        CellRangeAddress callRangeAddress13 = new CellRangeAddress(6,6,0,1);	
        CellRangeAddress callRangeAddress14 = new CellRangeAddress(6,6,2,4);	
        CellRangeAddress callRangeAddress15 = new CellRangeAddress(6,6,5,5);	
        CellRangeAddress callRangeAddress16 = new CellRangeAddress(6,6,6,6);
        
        CellRangeAddress callRangeAddress17 = new CellRangeAddress(7,7,0,1);	
        CellRangeAddress callRangeAddress18 = new CellRangeAddress(7,7,2,4);
        CellRangeAddress callRangeAddress19 = new CellRangeAddress(7,7,5,5);
        CellRangeAddress callRangeAddress20 = new CellRangeAddress(7,7,6,6);
        
        HSSFCellStyle headStyle = createCellStyle(wb,(short)16,true,true,false);   //设置样式
        HSSFCellStyle colStyle = createCellStyle(wb,(short)12,true,true,false);
        HSSFCellStyle colorStyle = createCellStyle(wb,(short)12,true,true,true);
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        //加载合并单元格对象 
        sheet.addMergedRegion(callRangeAddress); 
        sheet.addMergedRegion(callRangeAddress1); 
        sheet.addMergedRegion(callRangeAddress2); 
        sheet.addMergedRegion(callRangeAddress3); 
        sheet.addMergedRegion(callRangeAddress4); 
        sheet.addMergedRegion(callRangeAddress5); 
        sheet.addMergedRegion(callRangeAddress6);
        sheet.addMergedRegion(callRangeAddress7); 
        sheet.addMergedRegion(callRangeAddress8); 
        sheet.addMergedRegion(callRangeAddress9);
        sheet.addMergedRegion(callRangeAddress10); 
        sheet.addMergedRegion(callRangeAddress11); 
        sheet.addMergedRegion(callRangeAddress12);
        sheet.addMergedRegion(callRangeAddress13); 
        sheet.addMergedRegion(callRangeAddress14); 
        sheet.addMergedRegion(callRangeAddress15);
        sheet.addMergedRegion(callRangeAddress16); 
        sheet.addMergedRegion(callRangeAddress17); 
        sheet.addMergedRegion(callRangeAddress18);
        sheet.addMergedRegion(callRangeAddress19); 
        sheet.addMergedRegion(callRangeAddress20);
        sheet.setDefaultColumnWidth(25);  
        //第一行
        HSSFRow row1 = sheet.createRow(0);
        //加载单元格样式  
        HSSFCell cell2 = row1.createCell(0); 
        cell2.setCellStyle(headStyle);  
        cell2.setCellValue("中国石油山西销售分公司便利店付款审批单");
        
      //第二行
        HSSFRow row2 = sheet.createRow(1);  
        HSSFCell cellsan = row2.createCell(0);  
        //加载单元格样式  
        cellsan.setCellValue("");  
       
        //第三行
        HSSFRow row3 = sheet.createRow(2);
        HSSFCell cella = row3.createCell(0); 
        cella.setCellStyle(colStyle);
        cella.setCellValue(values.getString("year") + "年" + values.getString("month") + "月" + values.getString("day") + "日");  
      
        //第四行
        HSSFRow row4 = sheet.createRow(3);
        String[] titles2 = {"收款单位名称","",values.getString("supplier_name"),"","","付款方式",""};
        for(int i=0;i<titles2.length;i++)  
        {  
            HSSFCell cell4 = row4.createCell(i);  
            //加载单元格样式  
            cell4.setCellValue(titles2[i]);  
        }  
        //第五行
        HSSFRow row5 = sheet.createRow(4);
        String[] titles3 = {" 开户银行","","","","","账号",""};
        for(int i=0;i<titles3.length;i++)  
        {  
            HSSFCell cell3 = row5.createCell(i); 
            //加载单元格样式  
            cell3.setCellValue(titles3[i]);  
        } 
        
        HSSFRow row6 = sheet.createRow(5);
        String[] titles4 = {"付款事由","","","","","",""};
        for(int i=0;i<titles4.length;i++)  
        {  
            HSSFCell cell3 = row6.createCell(i); 
            //加载单元格样式  
            cell3.setCellValue(titles4[i]);  
        }
        
        HSSFRow row7 = sheet.createRow(6);
        String[] titles5 = {"付款金额（元、大写）","","","",""," 付款金额（元、小写）",""};
        for(int i=0;i<titles5.length;i++)  
        {  
            HSSFCell cell3 = row7.createCell(i); 
            //加载单元格样式  
            cell3.setCellValue(titles5[i]);  
        } 
        HSSFRow row8 = sheet.createRow(7);
        String[] titles6 = {"备     注","","","","","备     注",""};
        for(int i=0;i<titles6.length;i++)  
        {  
            HSSFCell cell3 = row8.createCell(i); 
            //加载单元格样式  
            cell3.setCellValue(titles6[i]);  
        } 
        
        HSSFRow rownumber = sheet.createRow(10);
        HSSFCell cellnumber = rownumber.createCell(1);  
        HSSFCell cellnumber1 = rownumber.createCell(4);
        HSSFCell cellnumber2 = rownumber.createCell(6);
        cellnumber.setCellStyle(colStyle);
        cellnumber.setCellValue("审核：");
        cellnumber1.setCellStyle(colStyle);
        cellnumber1.setCellValue("复核：");
        cellnumber2.setCellStyle(colStyle);
        cellnumber2.setCellValue("制表：");
        return wb;
    }
    /** 
     *  
     * @param workbook 
     * @param fontsize 
     * @return 单元格样式 
     */  
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize,boolean flag,boolean flag1,boolean flag2) {  
        // TODO Auto-generated method stub  
        HSSFCellStyle style = workbook.createCellStyle();  
        //是否水平居中  
        if(flag1){  
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中  
        }  
         
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中  
        //创建字体  
        HSSFFont font = workbook.createFont();  
        //是否加粗字体  
        if(flag){  
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        }
        if(flag2){
        	 font.setColor(HSSFColor.RED.index);	//设置为红色字体
        }
        font.setFontHeightInPoints(fontsize);  
        //加载字体  
        style.setFont(font);  
        return style;  
    }  
}
