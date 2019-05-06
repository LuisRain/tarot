package com.hy.util;

import java.io.PrintStream;

public class MD5
{
  public static String md5(String str) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      md.update(str.getBytes());
      byte[] b = md.digest();
      
      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        int i = b[offset];
        if (i < 0)
          i += 256;
        if (i < 16)
          buf.append("0");
        buf.append(Integer.toHexString(i));
      }
      str = buf.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return str;
  }
  
  public static void main(String[] args) {
    //System.out.println(md5("USERNAME,hy,"));
    PageData pd = new PageData();
    //System.out.println(pd.getString("HYKEY"));
  }
}


