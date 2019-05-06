package com.hy.util;

import java.io.PrintStream;
import java.net.URLEncoder;
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

public class ObjectExcelViewFromMap
  extends AbstractExcelView
{
  protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String filename = "";
    if (model.get("filename") != null) {
      filename = model.get("filename").toString() + "-" + Tools.date2Str(new Date(), "yyyyMMddHHmmss");
    } else {
      filename = Tools.date2Str(new Date(), "yyyyMMddHHmmss");
    }
    
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8") + ".xls");
    List<String> titles = null;
    String title = "";
    List<String> sheets = null;
    List<PageData> varList = null;
    titles = (List)model.get("titles");
    int len = 0;
    short width = 20;short height = 500;
    HSSFCellStyle contentStyle = null;
    PageData vpd = null;
    String varstr = "";
    int i = 0;
    int varCount = 0;
    int m = 0;
    if (model.get("sheetNum") != null) {
      //System.out.println("======2======sheetNum=====:" + model.get("sheetNum"));
      for (int j = 0; j < Integer.parseInt(model.get("sheetNum").toString()); j++) {
        HSSFSheet sheet = workbook.createSheet(model.get("sheetName" + j)
          .toString());
        titles = (List)model.get("titles");
        len = titles.size();
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment((short)2);
        headerStyle
          .setVerticalAlignment((short)1);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight((short)700);
        headerFont.setFontHeightInPoints((short)11);
        headerStyle.setFont(headerFont);
        sheet.setDefaultColumnWidth(width);
        for (i = 0; i < len; i++) {
          title = (String)titles.get(i);
          HSSFCell cell = getCell(sheet, 0, i);
          cell.setCellStyle(headerStyle);
          setText(cell, title);
        }
        sheet.getRow(0).setHeight(height);
        contentStyle = workbook.createCellStyle();
        contentStyle.setAlignment((short)2);
        varList = (List)model.get("varList" + j);
        varCount = varList.size();
        for (i = 0; i < varCount; i++) {
          vpd = (PageData)varList.get(i);
          for (m = 0; m < len; m++) {
            varstr = vpd.getString("var" + (m + 1)) != null ? vpd
              .getString("var" + (m + 1)) : "";
            HSSFCell cell = getCell(sheet, i + 1, m);
            cell.setCellStyle(contentStyle);
            setText(cell, varstr);
          }
        }
      }
    }
  }
}


