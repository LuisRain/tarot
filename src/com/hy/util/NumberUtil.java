package com.hy.util;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil
{
  public static void main(String[] args)
  {
    double finalAmount = 10.0D;
    double addQuantity = sub(20.0D, 
      10.0D);
    Double newTotalPrice = Double.valueOf(mul(addQuantity, 
      1.19D));
    finalAmount = 
      add(finalAmount, newTotalPrice.doubleValue());
    //System.out.print(finalAmount);
  }
  
  public static double add(double v1, double v2) {
    java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
    java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
    return b1.add(b2).doubleValue();
  }
  
  public static double sub(double v1, double v2) {
    java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
    java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
    return b1.subtract(b2).doubleValue();
  }
  
  public static double mul(double v1, double v2) {
    java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
    java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
    return b1.multiply(b2).doubleValue();
  }
  
  public static double div(double v1, double v2) {
    java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
    java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
    return b1.divide(b2, 3, 4).doubleValue();
  }
  
  public static double round(double v) {
    java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
    java.math.BigDecimal one = new java.math.BigDecimal("1");
    return b.divide(one, 3, 4).doubleValue();
  }
  
  public static String decimalFormat(String pattern, double value) {
    return new java.text.DecimalFormat(pattern).format(value);
  }
  
  public static String decimalFormat(double value) {
    return new java.text.DecimalFormat("0.00").format(value);
  }
  
  public static String decimalFormat(double value, String pattern) {
    return new java.text.DecimalFormat(pattern).format(value);
  }
  
  public static String decimalBlankFormat(double value) {
    return new java.text.DecimalFormat("0").format(value);
  }
  
  public static boolean isNumber(String value) {
    String patternStr = "^\\d+$";
    Pattern p = Pattern.compile(patternStr, 2);
    Matcher m = p.matcher(value);
    return m.find();
  }
}


