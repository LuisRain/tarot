package com.hy.util;

public class OrderNum
{
  public static String getOrderGourpNum()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("GP_");
    sb.append(StringUtil.getStringOfMillisecond(""));
    return sb.toString();
  }
  
  public static String getEnTempOrderNum()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("RK_");
    sb.append(DateUtil.getTimes());
    sb.append("495");
    return sb.toString();
  }

  public static String exTempOrderNum()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("CK_");
    sb.append(DateUtil.getTimes());
    sb.append("496");
    return sb.toString();
  }

  public static void main(String[] args) {
    System.out.println(getOrderGourpNum());
  }
}


