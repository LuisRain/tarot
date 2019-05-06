package com.hy.util;

import com.hy.entity.product.ProductType;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StringUtil
{
  public static String getStringOfMillisecond(String threeData)
  {
    Date date = new Date();
    DateFormat df = new SimpleDateFormat(
      "yyMMddHHmmssSSS");
    return df.format(date) + threeData;
  }
	/* 
	 * 返回长度为【strLength】的随机数，在前面补0 
	 */  
	public static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  
	    
	} 
  public static String[] StrList(String valStr)
  {
    int i = 0;
    String TempStr = valStr;
    String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
    valStr = valStr + ",";
    while (valStr.indexOf(',') > 0)
    {
      returnStr[i] = valStr.substring(0, valStr.indexOf(44));
      valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());
      
      i++;
    }
    return returnStr;
  }
  
  public static boolean isEmpty(String str) {
    boolean flag = false;
    if ((str == null) || ("".equals(str))) {
      flag = true;
    }
    return flag;
  }
  
  public static String splicingProductTypeTree(List<ProductType> ptypes) {
    StringBuffer menuBuffer = new StringBuffer();
    ProductType pType = null;
    List<ProductType> ptypes2 = null;
    String commaString = ",";
    if ((ptypes != null) && (ptypes.size() > 0)) {
      menuBuffer.append("[{id:0,pId:-1,name:\"请选择商品分类\", open:false},");
      for (int i = 0; i < ptypes.size(); i++) {
        pType = new ProductType();
        pType = (ProductType)ptypes.get(i);
        menuBuffer.append("{id:" + ((ProductType)ptypes.get(i)).getId() + ",pId:0,name:\"" + ((ProductType)ptypes.get(i)).getClassifyName() + "\", open:false, target:\"treeFrame1\"},");
        ptypes2 = pType.getPts();
        if ((ptypes2 != null) && (ptypes2.size() > 0)) {
          for (int j = 0; j < ptypes2.size(); j++) {
            menuBuffer.append("{id:" + ((ProductType)ptypes2.get(j)).getId() + ",pId:" + ((ProductType)ptypes.get(i)).getId() + ",name:\"" + ((ProductType)ptypes2.get(j)).getClassifyName() + "\", target:\"treeFrame2\"}");
            if ((i == ptypes.size() - 1) && (j == ptypes2.size() - 1)) {
              commaString = "";
            }
            menuBuffer.append(commaString);
          }
        }
      }
      
      menuBuffer.append("]");
    }
    return menuBuffer.toString();
  }
  
  public static String replace(String from, String to, String source)
  {
    if ((source == null) || (from == null) || (to == null))
      return null;
    StringBuffer bf = new StringBuffer("");
    int index = -1;
    while ((index = source.indexOf(from)) != -1) {
      bf.append(source.substring(0, index) + to);
      source = source.substring(index + from.length());
      index = source.indexOf(from);
    }
    bf.append(source);
    return bf.toString();
  }
  
  public static void main(String[] args) { String ss = "撒大声地";
    //System.out.println(ss.length());
    String[] sss = StrList(ss);
    for (int i = 0; i < sss.length; i++) {
      //System.out.println("拆分的数据============" + sss[i]);
    }
  }
}


