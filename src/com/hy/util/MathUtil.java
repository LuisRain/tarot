package com.hy.util;

import java.math.BigDecimal;
import java.util.Random;

public class MathUtil
{
  private static final int DEF_DIV_SCALE = 10;
  
  public static double add(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.add(b2).doubleValue();
  }
  
  public static double sub(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.subtract(b2).doubleValue();
  }
  
  public static double mul(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.multiply(b2).doubleValue();
  }
  
  public static double mulAndAdd(double v1, double v2, double v3)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    BigDecimal b4 = new BigDecimal(Double.toString(b1.multiply(b2).doubleValue()));
    
    BigDecimal b3 = new BigDecimal(Double.toString(v3));
    return b3.add(b4).doubleValue();
  }
  
  public static double div(double v1, double v2)
  {
    return div(v1, v2, 10);
  }
  
  public static double div(double v1, double v2, int scale)
  {
    if (scale < 0) {
      throw new IllegalArgumentException(
        "The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.divide(b2, scale, 4).doubleValue();
  }
  
  public static double round(double v, int scale)
  {
    if (scale < 0) {
      throw new IllegalArgumentException(
        "The scale must be a positive integer or zero");
    }
    BigDecimal b = new BigDecimal(Double.toString(v));
    BigDecimal one = new BigDecimal("1");
    return b.divide(one, scale, 4).doubleValue();
  }
  
  public static int getSixNumber() { int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    Random rand = new Random();
    for (int i = 10; i > 1; i--) {
      int index = rand.nextInt(i);
      int tmp = array[index];
      array[index] = array[(i - 1)];
      array[(i - 1)] = tmp;
    }
    int result = 0;
    for (int i = 0; i < 6; i++)
      result = result * 10 + array[i];
    return result;
  }
}


