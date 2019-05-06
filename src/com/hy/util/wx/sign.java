package com.hy.util.wx;

import com.hy.util.Tools;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class sign
{
  public static Map<String, String> sign(String code)
  {
    String url = Tools.readTxtFile("admin/config/wx/WXURL.txt");
    WechatTaskService wechatTaskService = new WechatTaskService();
    String jsapi_ticket = WechatTaskService.getJsapi_ticket();
    Map<String, String> ret = new HashMap();
    String nonce_str = create_nonce_str();
    String timestamp = create_timestamp();
    
    String signature = "";
    
    String string1 = "jsapi_ticket=" + jsapi_ticket + 
      "&noncestr=" + nonce_str + 
      "&timestamp=" + timestamp + 
      "&url=" + url + "?code=" + code;
    
    try
    {
      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(string1.getBytes("UTF-8"));
      signature = byteToHex(crypt.digest());
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    
    ret.put("url", url);
    ret.put("jsapi_ticket", jsapi_ticket);
    ret.put("nonceStr", nonce_str);
    ret.put("timestamp", timestamp);
    ret.put("signature", signature);
    
    return ret;
  }
  
  private static String byteToHex(byte[] hash) {
    Formatter formatter = new Formatter();
    byte[] arrayOfByte = hash;int j = hash.length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
      
      formatter.format("%02x", new Object[] { Byte.valueOf(b) });
    }
    String result = formatter.toString();
    formatter.close();
    return result;
  }
  
  private static String create_nonce_str() {
    return UUID.randomUUID().toString();
  }
  
  private static String create_timestamp() {
    return Long.toString(System.currentTimeMillis() / 1000L);
  }
}


