package com.hy.util;

import java.io.PrintStream;

public class UuidUtil
{
  public static String get32UUID() {
    String uuid = java.util.UUID.randomUUID().toString().trim().replaceAll("-", "");
    return uuid;
  }
  
  public static void main(String[] args) {
    //System.out.println(get32UUID());
  }
}


