package com.hy.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;

public class IpUtil
{
  public static String getIp(HttpServletRequest request)
  {
    String ip = request.getHeader("x-forwarded-for");
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
  
  public static String getIpAddress(HttpServletRequest request) {
    String str = getJsonContent("http://ip.taobao.com/service/getIpInfo.php?ip=" + 
      getIp(request));
    JSONObject obj = JSONObject.fromObject(str);
    JSONObject obj2 = (JSONObject)obj.get("data");
    Integer code = (Integer)obj.get("code");
    String resout = "";
    if (code.equals(Integer.valueOf(0))) {
      if ((obj2.get("county_id") != "") && (obj2.get("county_id") != "-1")) {
        return obj2.get("county_id").toString();
      }
      if ((obj2.get("city_id") != "") && (obj2.get("city_id") != "-1")) {
        return obj2.get("city_id").toString();
      }
      if ((obj2.get("region_id") != "") && (obj2.get("region_id") != "-1")) {
        return obj2.get("county_id").toString();
      }
    } else {
      resout = "9999";
    }
    return resout;
  }
  
  public static String getJsonContent(String urlStr) {
    try {
      URL url = new URL(urlStr);
      HttpURLConnection httpConn = (HttpURLConnection)url
        .openConnection();
      
      httpConn.setConnectTimeout(3000);
      httpConn.setDoInput(true);
      httpConn.setRequestMethod("POST");
      
      int respCode = httpConn.getResponseCode();
      if (respCode == 200) {
        return ConvertStream2Json(httpConn.getInputStream());
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
  
  private static String ConvertStream2Json(InputStream inputStream) {
    String jsonStr = "";
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len = 0;
    try
    {
      while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
        out.write(buffer, 0, len);
      }
      
      jsonStr = new String(out.toByteArray());
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return jsonStr;
  }
}


