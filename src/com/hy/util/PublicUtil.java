package com.hy.util;

import java.io.PrintStream;

public class PublicUtil
{
  public static void main(String[] args)
  {
    //System.out.println("本机的ip=" + getIp());
  }
  
  public static String getPorjectPath() {
    String nowpath = "";
    nowpath = System.getProperty("user.dir") + "/";
    
    return nowpath;
  }
  
  public static String getIp()
  {
    String ip = "";
    try {
      java.net.InetAddress inet = java.net.InetAddress.getLocalHost();
      ip = inet.getHostAddress();
    }
    catch (java.net.UnknownHostException e) {
      e.printStackTrace();
    }
    
    return ip;
  }
}


