package com.hy.util.wx;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hy.service.system.appuser.WxService;
import com.hy.util.PageData;
import com.hy.util.Tools;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class WechatTaskService {
  static final Logger logger = Logger.getLogger(WechatTaskService.class);
  private static String access_token;
  private static String jsapi_ticket;
  public final static String STATUS = "1";
  @Resource(name = "wxService")
  private WxService wxService;
  private String corpId = Tools.readTxtFile("admin/config/wx/CORPID.txt");
  private String corpsecret = Tools.readTxtFile("admin/config/wx/CORPSECRET.txt");

  public void refreshWechatAccessToken() {
  }

  public void saveSms()
          throws Exception {
  }

  public void getAccesstoken() {
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(getAccessTokenUrl());
    JsonParser jsonparer = new JsonParser();
    try {
      HttpResponse res = client.execute(get);
      String responseContent = null;
      HttpEntity entity = res.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
      JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();

      if (res.getStatusLine().getStatusCode() == 200) {
        if (json.get("errcode") != null) {
          //System.out.println("获取access_token出错，错误内容" + json.get("errcode"));
        } else {
          //System.out.println("---------access_token-------------多次出现不正常" + json.get("access_token").getAsString());
          setAccessToken(json.get("access_token").getAsString());
        }
      }
    } catch (Exception e) {
      logger.info("获取accessToken失败", e);
    }
  }

  public void getJsapiTicket() {
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(getJsapiTicketUrl());
    JsonParser jsonparer = new JsonParser();
    try {
      HttpResponse res = client.execute(get);
      String responseContent = null;
      HttpEntity entity = res.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
      JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();

      if (res.getStatusLine().getStatusCode() == 200) {
        setJsapi_ticket(json.get("ticket").getAsString());
      }
    } catch (Exception e) {
      //System.out.println("获取JsapiTicket出错，错误内容");
      logger.info("获取jsapi_ticket失败", e);
    }
  }

  private String getAccessTokenUrl() {
    StringBuffer sb = new StringBuffer();
    sb.append("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=");
    sb.append(this.corpId);
    sb.append("&corpsecret=");
    sb.append(this.corpsecret);
    return sb.toString();
  }

  private String getJsapiTicketUrl() {
    StringBuffer sb = new StringBuffer();
    sb.append("https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=");
    sb.append(getAccessToken());
    return sb.toString();
  }

  public static String getAccessToken() {
    return access_token;
  }

  public static void setAccessToken(String token) {
    access_token = token;
  }

  public static String getJsapi_ticket() {
    return jsapi_ticket;
  }

  public static void setJsapi_ticket(String jsapiTicket) {
    jsapi_ticket = jsapiTicket;
  }

  public static String getUserid(String code) {
    StringBuffer sb = new StringBuffer();
    sb.append("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=");
    sb.append(getAccessToken());
    sb.append("&code=" + code);
    //System.out.println("code-----------------------" + sb);
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(sb.toString());
    JsonParser jsonparer = new JsonParser();
    try {
      HttpResponse res = client.execute(get);
      String responseContent = null;
      HttpEntity entity = res.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
      JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();

      if (res.getStatusLine().getStatusCode() == 200) {
        return json.get("UserId").getAsString();
      }
    } catch (Exception e) {
      //System.out.println("获取useid出错，错误内容");
      logger.info("获取useid失败", e);
    }
    return null;
  }

  public static JsonObject getWxUser(String Userid) {
    StringBuffer sb = new StringBuffer();
    sb.append("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=");
    sb.append(getAccessToken());
    sb.append("&userid=" + Userid);
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(sb.toString());
    JsonParser jsonparer = new JsonParser();
    try {
      HttpResponse res = client.execute(get);
      String responseContent = null;
      HttpEntity entity = res.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
      JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();

      if (res.getStatusLine().getStatusCode() == 200) {
        return json;
      }
    } catch (Exception e) {
      //System.out.println("获取WxUser出错，错误内容");
      logger.info("获取WxUser失败", e);
    }
    return null;
  }

  public static boolean createwxUser(PageData pd) {
    MPerson mperson = new MPerson();
    int result = PostMessage("POST", mperson.CREATE_URL, MPerson.Create(pd));
    if (result == 0) {
      return true;
    }
    return false;
  }

  public static boolean updatewxUser(PageData pd) {
    MPerson mperson = new MPerson();
    int result = PostMessage("POST", mperson.UPDATA_URL, MPerson.Updata(pd));
    if (result == 0) {
      return true;
    }
    return false;
  }

  public static boolean deletewxUser(PageData pd) {
    MPerson mperson = new MPerson();
    int result = PostMessage("POST", mperson.DELETE_URL, MPerson.Delete(pd.get("userid").toString()));
    if (result == 0) {
      return true;
    }
    return false;
  }

  public static int PostMessage(String RequestMt, String RequestURL, String outstr) {
    int result = 0;
    RequestURL = RequestURL.replace("ACCESS_TOKEN", getAccessToken());
    JSONObject jsonobject = HttpRequest(RequestURL, RequestMt, outstr);
    //System.out.println(jsonobject);
    if ((jsonobject != null) &&
            (jsonobject.getInt("errcode") != 0)) {
      result = jsonobject.getInt("errcode");
      String str = String.format("操作失败 errcode:{} errmsg:{}", new Object[]{Integer.valueOf(jsonobject.getInt("errcode")), jsonobject.getString("errmsg")});
    }

    return result;
  }

  public static JSONObject HttpRequest(String request, String RequestMethod, String output) {
    JSONObject jsonObject = null;
    StringBuffer buffer = new StringBuffer();
    try {
      URL url = new URL(request);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setUseCaches(false);
      connection.setRequestMethod(RequestMethod);
      if (output != null) {
        OutputStream out = connection.getOutputStream();
        out.write(output.getBytes("UTF-8"));
        out.close();
      }

      InputStream input = connection.getInputStream();
      InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
      BufferedReader reader = new BufferedReader(inputReader);
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }

      reader.close();
      inputReader.close();
      input.close();
      input = null;
      connection.disconnect();
      jsonObject = JSONObject.fromObject(buffer.toString());
    } catch (Exception localException) {
    }
    return jsonObject;
  }
}


