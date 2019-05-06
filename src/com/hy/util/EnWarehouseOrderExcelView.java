package com.hy.util;

import com.hy.service.order.EnWarehouseOrderService;
import java.util.ArrayList;
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
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class EnWarehouseOrderExcelView
  extends AbstractExcelView
{
  @Resource
  private EnWarehouseOrderService enWarehouseOrderService;
  
  protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    List<PageData> titlename = (List)model.get("list");
    PageData count = (PageData)model.get("统计");
    if (titlename.size() > 0) {
      Date date = new Date();
      
      response.setContentType("application/octet-stream");
      String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
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
      setText(cell, "入库单");
      cell = getCell(sheet, 1, 0);
      setText(cell, "入库单号:");
      cell = getCell(sheet, 1, 1);
      setText(cell, ((PageData)titlename.get(0)).getString("order_num"));
      
      cell = getCell(sheet, 1, 2);
      setText(cell, "供应商:");
      cell = getCell(sheet, 1, 3);
      setText(cell, ((PageData)titlename.get(0)).getString("supplier_name"));
      
      cell = getCell(sheet, 2, 0);
      setText(cell, "订购日期:");
      cell = getCell(sheet, 2, 1);
      setText(cell, ((PageData)titlename.get(0)).getString("order_date"));
      
      cell = getCell(sheet, 2, 2);
      setText(cell, "入库日期:");
      cell = getCell(sheet, 2, 3);
      setText(cell, ((PageData)titlename.get(0)).getString("order_date"));
      
      cell = getCell(sheet, 3, 0);
      setText(cell, "配送方式：:");
      cell = getCell(sheet, 3, 1);
      setText(cell, "");
      
      cell = getCell(sheet, 3, 2);
      setText(cell, "配送地址：");
      cell = getCell(sheet, 3, 3);
      setText(cell, ((PageData)titlename.get(0)).getString("address"));
      
      cell = getCell(sheet, 4, 0);
      setText(cell, "联系人:");
      cell = getCell(sheet, 4, 1);
      setText(cell, ((PageData)titlename.get(0)).getString("manager_name"));
      cell = getCell(sheet, 4, 2);
      setText(cell, "联系电话:");
      setText(cell, ((PageData)titlename.get(0)).getString("manager_tel"));
      cell = getCell(sheet, 5, 0);
      setText(cell, "备注:");
      List<String> titles = new ArrayList();
      titles.add("序");
      titles.add("商品条形码");
      titles.add("HOSCODE");
      titles.add("商品名称");
      titles.add("规格");
      titles.add("类别");
      titles.add("采购价");
      titles.add("订购数");
      titles.add("出库数");
      titles.add("单位");
      titles.add("总额");
      int len = titles.size();
      for (int i = 0; i < len; i++) {
        String title = (String)titles.get(i);
        cell = getCell(sheet, 6, i);
        cell.setCellStyle(headerStyle);
        setText(cell, title);
      }
      List<PageData> varList = new ArrayList();
      for (int i = 0; i < titlename.size(); i++) {
        PageData pd = new PageData();
        pd.put("var0", Integer.valueOf(i + 1));
        pd.put("var1", ((PageData)titlename.get(i)).get("bar_code"));
        pd.put("var2", ((PageData)titlename.get(i)).get("host_code"));
        pd.put("var3", ((PageData)titlename.get(i)).get("product_name"));
        pd.put("var4", ((PageData)titlename.get(i)).get("specification"));
        pd.put("var5", ((PageData)titlename.get(i)).get("classify_name"));
        pd.put("var6", ((PageData)titlename.get(i)).get("purchase_price"));
        pd.put("var7", ((PageData)titlename.get(i)).get("quantity"));
        pd.put("var8", ((PageData)titlename.get(i)).get("final_quantity"));
        pd.put("var9", ((PageData)titlename.get(i)).get("unit"));
        pd.put("var10", ((PageData)titlename.get(i)).get("subtotal"));
        varList.add(pd);
      }
      
      HSSFCellStyle contentStyle = workbook.createCellStyle();
      contentStyle.setAlignment((short)2);
      int varCount = varList.size();
      int b = varCount + 7;
      for (int i = 0; i < varCount; i++) {
        PageData vpd = (PageData)varList.get(i);
        for (int j = 0; j < len; j++) {
          String varstr = vpd.getString("var" + j) != null ? vpd.getString("var" + j) : "";
          cell = getCell(sheet, i + 7, j);
          cell.setCellStyle(contentStyle);
          setText(cell, varstr);
        }
      }
      cell = getCell(sheet, b, 5);
      setText(cell, "入库合计(实际)：");
      cell = getCell(sheet, b, 6);
      setText(cell, "数量" + count.get("入库合计").toString());
      
      cell = getCell(sheet, b, 7);
      setText(cell, "金额" + count.get("总额").toString());
    }
  }
}


