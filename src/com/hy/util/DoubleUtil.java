package com.hy.util;

import java.math.BigDecimal;

public class DoubleUtil {

	
	
	public static double add(double b1,double b2){
		BigDecimal bd1 = new BigDecimal(Double.toString(b1));
		BigDecimal bd2 = new BigDecimal(Double.toString(b2));
		return bd1.add(bd2).doubleValue();
	}
	
	
	/**
	 * 提供精确的减法运算
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	/**
	 * 提供精确的乘法运算
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double multiply(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(v1);  
		BigDecimal b2 = new BigDecimal(v2);  
		
		return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}
	

	 /**
	     * 两个Double数相除，并保留scale位小数
	     * @param v1
	     * @param v2
	     * @param scale
	     * @return Double
	     */
	    public static Double div(Double v1,Double v2,int scale){
	        if(scale<0){
	            throw new IllegalArgumentException(
	            "The scale must be a positive integer or zero");
	        }
	        BigDecimal b1 = new BigDecimal(v1.toString());
	        BigDecimal b2 = new BigDecimal(v2.toString());
	        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	    }


	
	
	
}
