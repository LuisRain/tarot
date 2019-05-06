package com.hy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SmsUtil
{
  public static void main(String[] args)
  {
    sendSms2("13511111111", "您的验证码是：1111。请不要把验证码泄露给其他人。");
  }
  
  public static void sendSms1(String mobile, String code)
  {
    String account = "";String password = "";
    String strSMS1 = Tools.readTxtFile("admin/config/SMS1.txt");
    if ((strSMS1 != null) && (!"".equals(strSMS1))) {
      String[] strS1 = strSMS1.split(",hy,");
      if (strS1.length == 2) {
        account = strS1[0];
        password = strS1[1];
      }
    }
    String PostData = "";
    try {
      PostData = "account=" + account + "&password=" + password + "&mobile=" + mobile + "&content=" + URLEncoder.encode(code, "utf-8");
    } catch (UnsupportedEncodingException e) {
      //System.out.println("短信提交失败");
    }
    
    String ret = SMS(PostData, "http://sms.106jiekou.com/utf8/sms.aspx");
    //System.out.println(ret);
  }
  
  public static String SMS(String postData, String postUrl)
  {
    try
    {
      URL url = new URL(postUrl);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("Connection", "Keep-Alive");
      conn.setUseCaches(false);
      conn.setDoOutput(true);
      
      conn.setRequestProperty("Content-Length", "" + postData.length());
      OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
      out.write(postData);
      out.flush();
      out.close();
      
      if (conn.getResponseCode() != 200) {
        //System.out.println("connect failed!");
        return "";
      }
      
      String result = "";
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
      String line; while ((line = in.readLine()) != null) { 
        result = result + line + "\n";
      }
      in.close();
      return result;
    } catch (IOException e) {
      e.printStackTrace(System.out);
    }
    return "";
  }
  
  private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";
  
  public static void sendSms2(String mobile, String code)
  {
    HttpClient client = new HttpClient();
    PostMethod method = new PostMethod(Url);
    
    client.getParams().setContentCharset("UTF-8");
    method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
    
    String content = new String(code);
    
    String account = "";String password = "";
    String strSMS2 = Tools.readTxtFile("admin/config/SMS2.txt");
    if ((strSMS2 != null) && (!"".equals(strSMS2))) {
      String[] strS2 = strSMS2.split(",hy,");
      if (strS2.length == 2) {
        account = strS2[0];
        password = strS2[1];
      }
    }
    
    NameValuePair[] data = {
      new NameValuePair("account", account), 
      new NameValuePair("password", password), 
      new NameValuePair("mobile", mobile), 
      new NameValuePair("content", content) };
    
    method.setRequestBody(data);
    try
    {
      client.executeMethod(method);
      
      String SubmitResult = method.getResponseBodyAsString();
      
      Document doc = DocumentHelper.parseText(SubmitResult);
      Element root = doc.getRootElement();
      
      code = root.elementText("code");
      String msg = root.elementText("msg");
      String smsid = root.elementText("smsid");
      
      //System.out.println(code);
      //System.out.println(msg);
      //System.out.println(smsid);
      
      if (code == "2") {
        //System.out.println("短信提交成功");
      }
    }
    catch (HttpException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }
  
  public static void sendSmsAll(List<PageData> list)
  {
    for (int i = 0; i < list.size(); i++) {
      String code = ((PageData)list.get(i)).get("code").toString();
      String mobile = ((PageData)list.get(i)).get("mobile").toString();
      sendSms2(mobile, code);
    }
  }
}


