package com.hy.controller.WarehousingInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.hy.controller.base.BaseController;
import com.hy.service.WarehousingInterface.WarehousingService;
import com.hy.util.PageData;

/**
 * 入库接口
 * @author gyy
 *
 */
@Controller
@RequestMapping({"/warehousingcontroller"})
public class WarehousingController extends BaseController{
	
	private HttpSession session;
	
	@Resource(name = "warehousingservice")
	public WarehousingService warehousingservice;
	
	/**
	 * 通过供应商编码获取供应商信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findsupplierinfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findsupplierinfo(HttpServletRequest request,HttpServletResponse response){
		String json = null;
		String valueStr = "";  
		try {  
       	    request.setCharacterEncoding("UTF-8");
   		    response.setContentType("text/json;charset=utf-8");
   		    response.setCharacterEncoding("UTF-8");
            StringBuffer sb = new StringBuffer();  
            InputStream inputstream = request.getInputStream();  
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
            String str = "";  
            while ((str = bufferedreader.readLine()) != null) {  
                sb.append(str);  
            }  
            valueStr = sb.toString();  
        } catch (IOException e) {  
            e.printStackTrace();  
            valueStr = "";  
        }  
		if(valueStr != null && !valueStr.equals("")){
			 Gson gson = new Gson();
             Map<String, Object> map = new HashMap<String, Object>();
             map = gson.fromJson(valueStr, map.getClass());
             String suppliernum = (String) map.get("suppliernum");
			 json = warehousingservice.findsupplierinfo(suppliernum);
		}else{
			json = warehousingservice.resultmsg("0");
		}
		return json;
	}
	/**
	 * 通过条形码查询商品信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findproduct",  produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findproduct(HttpServletResponse response,HttpServletRequest request){
		 String valueStr = "";  
		 String json = null;
         try {  
        	 request.setCharacterEncoding("UTF-8");
    		 response.setContentType("text/json;charset=utf-8");
    		 response.setCharacterEncoding("UTF-8");
             StringBuffer sb = new StringBuffer();  
             InputStream inputstream = request.getInputStream();  
             InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
             BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
             String str = "";  
             while ((str = bufferedreader.readLine()) != null) {  
                 sb.append(str);  
             }  
             valueStr = sb.toString();  
         } catch (IOException e) {  
             e.printStackTrace();  
             valueStr = "";  
         }  
         if(valueStr != null && !valueStr.equals("")){
        	 Gson gson = new Gson();
             Map<String, Object> map = new HashMap<String, Object>();
             map = gson.fromJson(valueStr, map.getClass());
             String barcode=(String) map.get("barcode");
 			 json = warehousingservice.findproduct(barcode);
 		}else{
 			json = warehousingservice.resultmsg("0");
 		}
		return json;
	}
	
	/**
	 * 扫码入库
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/warehousing", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String warehousing(HttpServletRequest request,HttpServletResponse response)  throws Exception{
		 String valueStr = "";  
		 String json = null;
         try {  
        	 request.setCharacterEncoding("UTF-8");
    		 response.setContentType("text/json;charset=utf-8");
    		 response.setCharacterEncoding("UTF-8");
             StringBuffer sb = new StringBuffer();  
             InputStream inputstream = request.getInputStream();  
             InputStreamReader inputstreamreader = new InputStreamReader(inputstream);  
             BufferedReader bufferedreader = new BufferedReader(inputstreamreader);  
             String str = "";  
             while ((str = bufferedreader.readLine()) != null) {  
                 sb.append(str);  
             }  
             valueStr = sb.toString();  
         } catch (IOException e) {  
             e.printStackTrace();  
             valueStr = "";           
        } 
        if(valueStr != null && !valueStr.equals("")){
        	 Gson gson = new Gson();
             Map<String, Object> map = new HashMap<String, Object>();
             map = gson.fromJson(valueStr, map.getClass());
             String suppliernum = (String) map.get("suppliernum");   //供应商唯一编码
            // String data = (String) map.get("data");				 //获取商品信息
             List<String> liststr = (List<String>) map.get("data"); 
           /*  if(suppliernum != null && !suppliernum.equals("")){*/
	            	 if(liststr.size() > 0 ){
	         			json = warehousingservice.warehousing(suppliernum,liststr);
	         		 }else{
	         			json = warehousingservice.resultmsg("0");
	         		 }
            /* }else{
            		json = warehousingservice.resultmsg("0");
             }*/
        }else{
        	json = warehousingservice.resultmsg("0");
        }
		return json;
	}
	
	/**
	 * 测试
	 * @return
	 */
	@RequestMapping(value = "/test")
	@ResponseBody
	public ModelAndView test(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test/test");
		return mv;
	}
}
