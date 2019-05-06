package com.hy.util.wx;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

public class Sms
{
  public static String getSmsText()
  {
    StringBuffer str = new StringBuffer();
    str.append("请对我们这次的配送服务进行评价");
    str.append("   1：满意");
    str.append("   2：基本满意");
    str.append("   3：不满意");
    str.append("   不回复视为满意。");
    str.append("   【邮政速递】");
    return str.toString();
  }
  
  public static int sendSMS(String Mobile, String Content, String send_time) throws MalformedURLException, UnsupportedEncodingException {
    java.net.URL url = null;
    String CorpID = "XZD09096";
    String Pwd = "123456";
    String send_content = URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");
    url = new java.net.URL("  http://sms.xzd106.com/ws/BatchSend.aspx?CorpID=" + CorpID + "&Pwd=" + Pwd + "&Mobile=" + Mobile + "&Content=" + send_content + "&Cell=&SendTime=" + send_time);
    java.io.BufferedReader in = null;
    int inputLine = 0;
    try {
      //System.out.println("开始发送短信手机号码为 ：" + Mobile);
      in = new java.io.BufferedReader(new InputStreamReader(url.openStream()));
      inputLine = new Integer(in.readLine()).intValue();
    } catch (Exception e) {
      //System.out.println("网络异常,发送短信失败！");
      inputLine = -2;
    }
    //System.out.println("结束发送短信返回值：  " + inputLine);
    return inputLine;
  }
  
  public static String getSMS()
    throws MalformedURLException, UnsupportedEncodingException
  {
    java.net.URL url = null;
    String CorpID = "XZD09096";
    String Pwd = "123456";
    
    url = new java.net.URL("http://sms.xzd106.com/ws/Get.aspx?CorpID=" + CorpID + "&Pwd=" + Pwd);
    java.io.BufferedReader in = null;
    String inputLine = "0";
    try {
      in = new java.io.BufferedReader(new InputStreamReader(url.openStream()));
      inputLine = in.readLine();
      //System.out.println(in.readLine());
    } catch (Exception e) {
      //System.out.println("网络异常,发送短信失败！");
      inputLine = "-2";
    }
    //System.out.println("结束发送短信返回值：  " + inputLine);
    return inputLine;
  }
}


