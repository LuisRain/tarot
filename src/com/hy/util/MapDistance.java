package com.hy.util;

import java.io.PrintStream;
import java.util.HashMap;

public class MapDistance
{
  private static double EARTH_RADIUS = 6378.1369999999997D;
  
  private static double rad(double d) {
    return d * 3.141592653589793D / 180.0D;
  }
  
  public static String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str)
  {
    Double lat1 = Double.valueOf(Double.parseDouble(lat1Str));
    Double lng1 = Double.valueOf(Double.parseDouble(lng1Str));
    Double lat2 = Double.valueOf(Double.parseDouble(lat2Str));
    Double lng2 = Double.valueOf(Double.parseDouble(lng2Str));
    double patm = 2.0D;
    double radLat1 = rad(lat1.doubleValue());
    double radLat2 = rad(lat2.doubleValue());
    double difference = radLat1 - radLat2;
    double mdifference = rad(lng1.doubleValue()) - rad(lng2.doubleValue());
    double distance = patm * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / patm), patm) + 
      Math.cos(radLat1) * Math.cos(radLat2) * 
      Math.pow(Math.sin(mdifference / patm), patm)));
    distance *= EARTH_RADIUS;
    String distanceStr = String.valueOf(distance);
    return distanceStr;
  }
  
  public static java.util.Map getAround(String latStr, String lngStr, String raidus)
  {
    java.util.Map map = new HashMap();
    
    Double latitude = Double.valueOf(Double.parseDouble(latStr));
    Double longitude = Double.valueOf(Double.parseDouble(lngStr));
    
    Double degree = Double.valueOf(111293.63611111112D);
    double raidusMile = Double.parseDouble(raidus);
    
    Double mpdLng = Double.valueOf(Double.parseDouble
				((degree.doubleValue() * Math.cos(latitude * (Math.PI / 180))+"").replace("-", "")));
    Double dpmLng = Double.valueOf(1.0D / mpdLng.doubleValue());
    Double radiusLng = Double.valueOf(dpmLng.doubleValue() * raidusMile);
    
    Double minLat = Double.valueOf(longitude.doubleValue() - radiusLng.doubleValue());
    
    Double maxLat = Double.valueOf(longitude.doubleValue() + radiusLng.doubleValue());
    
    Double dpmLat = Double.valueOf(1.0D / degree.doubleValue());
    Double radiusLat = Double.valueOf(dpmLat.doubleValue() * raidusMile);
    
    Double minLng = Double.valueOf(latitude.doubleValue() - radiusLat.doubleValue());
    
    Double maxLng = Double.valueOf(latitude.doubleValue() + radiusLat.doubleValue());
    
    map.put("minLat", minLat);
    map.put("maxLat", maxLat);
    map.put("minLng", minLng);
    map.put("maxLng", maxLng);
    
    return map;
  }
  
  public static void main(String[] args)
  {
    //System.out.println(getDistance("116.97265", "36.694514", "116.597805", "36.738024"));
    
    //System.out.println(getAround("117.11811", "36.68484", "13000"));
  }
}


