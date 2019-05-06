package com.hy.util;

import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class PurchaseOrderExcelView
  extends AbstractExcelView
{
  protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    List<PageData> titlename = (List)model.get("purchaseorderlist");
    if (titlename.size() > 0) {
      Date date = new Date();
      
      response.setContentType("application/octet-stream");
      PageData downloadType = (PageData)model.get("pd");
      String filename = downloadType.getString("downloadType");
      if (filename.equals("0")) {
        filename = "给供应商采购订单" + ((PageData)titlename.get(0)).getString("order_num");
        filename = EncodeFilename.encodeFilename(filename, request);
      } else {
        filename = "采购人员用采购订单" + ((PageData)titlename.get(0)).getString("order_num");
        filename = EncodeFilename.encodeFilename(filename, request);
      }
      response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
      HSSFSheet sheet = workbook.createSheet("sheet1");
      sheet.setDisplayGridlines(false);
      HSSFCellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setAlignment((short)2);
      headerStyle.setVerticalAlignment((short)1);
      HSSFFont headerFont = workbook.createFont();
      headerFont.setBoldweight((short)700);
      headerFont.setFontHeightInPoints((short)11);
      headerStyle.setFont(headerFont);
      
      sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$j$1"));
      
      HSSFCell cell = getCell(sheet, 0, 0);
      cell.setCellStyle(headerStyle);
      short width = 20;
      short height = 500;
      sheet.setDefaultColumnWidth(width);
      sheet.getRow(0).setHeight(height);
      setText(cell, ((PageData)titlename.get(0)).getString("名称"));
      //System.out.println(((PageData)titlename.get(0)).getString("名称"));
      cell = getCell(sheet, 1, 0);
      setText(cell, "订购日期:");
      cell = getCell(sheet, 1, 1);
      setText(cell, ((PageData)titlename.get(0)).getString("订购日期"));
      
      cell = getCell(sheet, 1, 2);
      setText(cell, "送货日期:");
      cell = getCell(sheet, 1, 3);
      setText(cell, ((PageData)titlename.get(0)).getString("送货时间"));
      
      String deliverStyle = ((PageData)titlename.get(0)).getString("配送方式");
      if (deliverStyle.equals("0")) {
        deliverStyle = "送货上门";
      } else {
        deliverStyle = "上门自取";
      }
      cell = getCell(sheet, 2, 0);
      setText(cell, "配送方式:");
      cell = getCell(sheet, 2, 1);
      setText(cell, deliverStyle);
      
      cell = getCell(sheet, 2, 2);
      setText(cell, "配送地址:");
      cell = getCell(sheet, 2, 3);
      setText(cell, ((PageData)titlename.get(0)).getString("配送地址"));
      
      cell = getCell(sheet, 3, 0);
      setText(cell, "供应商联系人:");
      cell = getCell(sheet, 3, 1);
      setText(cell, ((PageData)titlename.get(0)).getString("供应商联系人"));
      
      cell = getCell(sheet, 3, 2);
      setText(cell, "联系电话：");
      cell = getCell(sheet, 3, 3);
      setText(cell, ((PageData)titlename.get(0)).getString("联系电话"));
      
      cell = getCell(sheet, 4, 0);
      setText(cell, "备注:");
      cell = getCell(sheet, 4, 1);
      setText(cell, "");
      
      List<String> titles = (List)model.get("titles");
      int len = titles.size();
      for (int i = 0; i < len; i++) {
        String title = (String)titles.get(i);
        cell = getCell(sheet, 5, i);
        cell.setCellStyle(headerStyle);
        setText(cell, title);
      }
      
      HSSFCellStyle contentStyle = workbook.createCellStyle();
      contentStyle.setAlignment((short)2);
      List<PageData> varList = (List)model.get("varList");
      int varCount = varList.size();
      int c = varCount + 7;
      for (int i = 0; i < varCount; i++) {
        PageData vpd = (PageData)varList.get(i);
        for (int j = 0; j < len; j++) {
          String varstr = vpd.getString("var" + (j + 1)) != null ? vpd.getString("var" + (j + 1)) : "";
          //System.out.println("var" + (j + 1));
          //System.out.println(varstr);
          cell = getCell(sheet, i + 6, j);
          
          cell.setCellStyle(contentStyle);
          setText(cell, varstr);
        }
      }
      
      c--;
      cell = getCell(sheet, c, 0);
      setText(cell, "采购金额合计：" + ((PageData)titlename.get(0)).getString("总计") + "元");
    }
  }
}


