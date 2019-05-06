package com.hy.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
* @ClassName: ObjectExcel
* @Description: TODO(Excel与数据库交互)
* @author guoweifeng
* @date 2016年5月20日 上午10:34:32
 */
public class ObjectExcel {
	protected static Logger logger = Logger.getLogger(ObjectExcel.class);

	/**
	 * @Description: 读取销售订单
	 * @param filepath //文件路径
	 * @param filename //文件名
	 * @param startrow //开始行号
	 * @param startcol //开始列号
	 * @param sheetnum //sheet
	 * @return List<Object>  
	 * @throws
	 * @author guoweifeng
	 * @date 2016年5月20日
	 */
	public static List<Object> readExcel(String filepath, String filename, int startrow, int startcol, int sheetnum) {
		List<Object> varList = new ArrayList<Object>();
		DecimalFormat df = new DecimalFormat("0");
		try {
			File target = new File(filepath, filename);
			FileInputStream fi = new FileInputStream(target);
			if(filename.endsWith("xls")){
				HSSFWorkbook wb = new HSSFWorkbook(fi);
				HSSFSheet sheet = wb.getSheetAt(sheetnum);
				int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
				for (int i = startrow; i < rowNum; i++) {					//行循环开始
					PageData varpd = new PageData();
					HSSFRow row = sheet.getRow(i); 							//行
					int cellNum = row.getLastCellNum(); 					//每行的最后一个单元格位置
					for (int j = startcol; j < cellNum; j++) {				//列循环开始
						HSSFCell cell = row.getCell(Short.parseShort(j + ""));
						String cellValue = null;
						if (null != cell) {     
							switch (cell.getCellType()) {     
							case HSSFCell.CELL_TYPE_NUMERIC: // 数字   
								HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
								cellValue = String.valueOf(dataFormatter.formatCellValue(cell));
								break;     
							case HSSFCell.CELL_TYPE_STRING: // 字符串     
								cellValue = cell.getStringCellValue(); 
								break;     
							case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean     
								cellValue = String.valueOf(cell.getBooleanCellValue());
								break;     
							case HSSFCell.CELL_TYPE_FORMULA: // 公式     
								cellValue = String.valueOf(cell.getCellFormula());   
								break;     
							case HSSFCell.CELL_TYPE_BLANK: // 空值     
								cellValue = "";
								break;     
							case HSSFCell.CELL_TYPE_ERROR: // 故障     
								cellValue = "error";
								break;     
							default:     
								cellValue = "error";//未知类型
								break;     
							}    
						} else {
							cellValue = "";
						}
						varpd.put("var"+j, cellValue);
					}
					varList.add(varpd);
				}
			}else if(filename.endsWith("xlsx")){
				XSSFWorkbook wb = new XSSFWorkbook(fi);
				XSSFSheet sheet = wb.getSheetAt(sheetnum); 
				int rowNum = sheet.getLastRowNum() + 1; 					//取得最后一行的行号
				for (int i = startrow; i < rowNum; i++) {					//行循环开始
					PageData varpd = new PageData();
					XSSFRow row = sheet.getRow(i); 							//行
					int cellNum = row.getLastCellNum(); 					//每行的最后一个单元格位置
					for (int j = startcol; j < cellNum; j++) {				//列循环开始
						XSSFCell cell = row.getCell(Short.parseShort(j + ""));
						String cellValue = null;
						if (null != cell) {     
							switch (cell.getCellType()) {     
							case XSSFCell.CELL_TYPE_NUMERIC: // 数字   
								
								  // 获取单元格的样式值，即获取单元格格式对应的数值  
				                int style = cell.getCellStyle().getDataFormat();  
				                // 判断是否是日期格式  
				                if (HSSFDateUtil.isCellDateFormatted(cell)) {  
				                    Date date = cell.getDateCellValue();  
				                    // 对不同格式的日期类型做不同的输出，与单元格格式保持一致  
				                    switch (style) {  
				                    case 178:  
				                    	cellValue = new SimpleDateFormat("yyyy'年'M'月'd'日'").format(date);  
				                        break;  
				                    case 14:  
				                    	cellValue = new SimpleDateFormat("yyyy/MM/dd").format(date);  
				                        break;  
				                    case 179:  
				                    	cellValue = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);  
				                        break;  
				                    case 181:  
				                    	cellValue = new SimpleDateFormat("yyyy/MM/dd HH:mm a ").format(date);  
				                        break;  
				                    case 22:  
				                    	cellValue = new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss ").format(date);  
				                        break;  
				                    default:  
				                        break;  
				                    }  
				                } else {  
				                    switch (style) {  
				                    // 单元格格式为百分比，不格式化会直接以小数输出  
				                    case 9:  
				                    	cellValue = new DecimalFormat("0.00%").format(cell.getNumericCellValue());  
				                        break;  
				                    // DateUtil判断其不是日期格式，在这里也可以设置其输出类型  
				                    case 57:  
				                    	cellValue = new SimpleDateFormat(" yyyy'年'MM'月' ").format(cell.getDateCellValue());  
				                        break;  
				                    default:  
				                    	HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
										cellValue = String.valueOf(dataFormatter.formatCellValue(cell));
				                        break;  
				                    }  
				                }  
								break;     
							case XSSFCell.CELL_TYPE_STRING: // 字符串     
								cellValue = cell.getStringCellValue(); 
								break;     
							case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean     
								cellValue = String.valueOf(cell.getBooleanCellValue());
								break;     
							case XSSFCell.CELL_TYPE_FORMULA: // 公式     
								cellValue = String.valueOf(cell.getCellFormula());   
								break;
							case XSSFCell.CELL_TYPE_BLANK: // 空值     
								cellValue = "";
								break;     
							case XSSFCell.CELL_TYPE_ERROR: // 故障     
								cellValue = "error";
								break;     
							default:     
								cellValue = "error";//未知类型
								break;     
							}    
						} else {
							cellValue = "";
						}
						varpd.put("var"+j, cellValue);
					}
					varList.add(varpd);
				}

			}
		} catch (Exception e) {
			logger.info(e); 
		}
		return varList;
	}
}
