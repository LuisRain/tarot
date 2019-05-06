package com.hy.controller.app.appuser;

import oracle.net.ano.SupervisorService;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.util.Const;
import com.hy.util.Tools;

public class WxMenuUtils {
 static String url="https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=C8NXC10XSba-03wOa1_-Mwrgx2IkOaNaJtsvCMP1A8xpaiatVCXsRt9EEPdluWyV&agentid=2";
 
	 
	 /**
	  * 创建菜单
	  */
	 public static String createMenu() throws Exception {
	  HttpRequest httpRequest=new HttpRequest();
	  String result= httpRequest.sendPost(url, Tools.readTxtFile(Const.WXMENU));
	  JSONObject object = JSON.parseObject(result);
	  //System.out.println(object.getString("errmsg"));
	  return object.getString("errmsg");
	 }
	 
//	 /**
//	  * 查询菜单
//	  */
//	 public static String getMenuInfo(String accessToken) throws Exception {
//	  HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken);
//	  HttpResponse response = httpclient.execute(get);
//	  String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
//	  return jsonStr;
//	 }
//	 
//	 /**
//	  * 删除自定义菜单
//	  */
//	 public static String getAccessToken(String accessToken) throws Exception {
//	  HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken);
//	  HttpResponse response = httpclient.execute(get);
//	  String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
//	  JSONObject object = JSON.parseObject(jsonStr);
//	  return object.getString("errmsg");
//	 }
//	}


	 
}
