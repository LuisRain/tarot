package com.hy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import static java.net.URLDecoder.decode;

public class HttpUtil {

	/** Created by 郭宇翔 on 2018/9/19
	 * 解码
	 */
	public static String decodePathVariable(String keyWord) {
		if (keyWord == null) return null;
		if (keyWord.equals("%"))
			return keyWord;
		try {
			return decode(keyWord, "UTF-8");
		} catch (Exception e) {
			return keyWord;
		}
	}

	/**
	 * http post请求
	 * @param url						地址
	 * @param postContent				post内容格式为param1=value¶m2=value2¶m3=value3
	 * @return
	 * @throws IOException
	 */
	public static String httpPostRequest(String url, String postContent) throws Exception{
		OutputStream outputstream = null;
		BufferedReader in = null;
		try{
			URL path = new URL(url);
			URLConnection httpurlconnection = path.openConnection();
			httpurlconnection.setConnectTimeout(10 * 1000);
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setUseCaches(false);
			OutputStreamWriter out = new OutputStreamWriter(httpurlconnection.getOutputStream(), "UTF-8");
			out.write(postContent);
			out.flush();
			
			StringBuffer result = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(httpurlconnection
					.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null){
				result.append(line);
			}
			return result.toString();
		}catch(Exception ex){
			throw new Exception("post请求异常：" + ex.getMessage());
		}finally{
			if (outputstream != null){
				try{
					outputstream.close();
				}catch (IOException e){
					outputstream = null;
				}
			}
			if (in != null){
				try{
					in.close();
				}catch (IOException e){in = null;}
			}
		}	
	}	
	
	/**
	 * 通过httpClient进行post提交
	 * @param url				提交url地址
	 * @param charset			字符集
	 * @param keys				参数名
	 * @param values			参数值
	 * @return
	 * @throws Exception
	 */
	public static String HttpClientPost(String url , String charset , Map<String,Object> paramMap) throws Exception{
		HttpClient client = null;
		PostMethod post = null;
		String result = "";
		int status = 200;
		try {
	           client = new HttpClient();                
               //PostMethod对象用于存放地址
             //总账户的测试方法
               post = new PostMethod(url);         
               //NameValuePair数组对象用于传入参数
               post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=" + charset);//在头文件中设置转码

               String value = null;
               NameValuePair temp = null;
               NameValuePair[] params = new NameValuePair[paramMap.size()];
               int i = 0;
               for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            	   Object v = entry.getValue();
            	   value = "";
            	   if(v != null){
            		   value = v.toString();
            	   }
            	   temp = new NameValuePair(entry.getKey() , value);   
            	   params[i] = temp;
            	   i++;
            	   temp = null;
               }
              post.setRequestBody(params); 
               //执行的状态
              status = client.executeMethod(post); 
              if(status == 200){
            	  result = post.getResponseBodyAsString();
              }
               
		} catch (Exception ex) {
			throw new Exception("通过httpClient进行post提交异常：" + ex.getMessage() + " status = " + status);
		}
		finally{
			post.releaseConnection(); 
		}
		return result;
	}
	/**
	 httpClient的get请求方式2
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, String charset)throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
		String response = "";
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("请求出错: "+ getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();//读取为字节数组
			response = new String(responseBody, charset);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return response;
	}
}