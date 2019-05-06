package com.hy.util;

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
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ObjectExcelView
  extends AbstractExcelView
{
  protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Date date = new Date();
    String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
    
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
    HSSFSheet sheet = workbook.createSheet("sheet1");
    
    List<String> titles = (List)model.get("titles");
    int len = titles.size();
    HSSFCellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setAlignment((short)2);
    headerStyle.setVerticalAlignment((short)1);
    HSSFFont headerFont = workbook.createFont();
    headerFont.setBoldweight((short)700);
    headerFont.setFontHeightInPoints((short)11);
    headerStyle.setFont(headerFont);
    short width = 20;short height = 500;
    sheet.setDefaultColumnWidth(width);
    for (int i = 0; i < len; i++) {
      String title = (String)titles.get(i);
      HSSFCell cell = getCell(sheet, 0, i);
      cell.setCellStyle(headerStyle);
      setText(cell, title);
    }
    sheet.getRow(0).setHeight(height);
    
    HSSFCellStyle contentStyle = workbook.createCellStyle();
    contentStyle.setAlignment((short)2);
    List<PageData> varList = (List)model.get("varList");
    int varCount = varList.size();
    for (int i = 0; i < varCount; i++) {
      PageData vpd = (PageData)varList.get(i);
      for (int j = 0; j < len; j++) {
        String varstr = vpd.getString("var" + (j + 1)) != null ? vpd.getString("var" + (j + 1)) : "";
        HSSFCell cell = getCell(sheet, i + 1, j);
        cell.setCellStyle(contentStyle);
        setText(cell, varstr);
      }
    }
  }
}


