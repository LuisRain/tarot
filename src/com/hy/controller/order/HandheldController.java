package com.hy.controller.order;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hy.controller.base.BaseController;
import com.hy.entity.system.User;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.product.CargoSpaceService;
import com.hy.service.product.MerchantService;
import com.hy.service.product.ProductService;
import com.hy.service.system.user.UserService;
import com.hy.util.AppUtil;
import com.hy.util.DateUtil;
import com.hy.util.PageData;
import com.hy.util.Tools;
import com.hy.util.sqlserver.DBConnectionManager;
/**
 * @author wps
 * 2017/8/24
 */
@Controller
@RequestMapping({"/handheld"})
public class HandheldController  extends BaseController{
	 @Resource(name="productService")
	  private ProductService productService;
	 @Resource(name="userService")
	  private UserService userService;
	 @Resource(name="merchantService")
	  private MerchantService merchantService;
	 @Resource(name="cargoSpaceService")
	  private  CargoSpaceService cargoSpaceService;
	 @Resource
	  private ProductinventoryService productinventoryService;
	
	 
	 
	 /**
	   * 查询站点信息
	   * 传入值：location  货位信息
	   * 回传json{result:false/true,store[{id:id,name:站点名称}]}
	   * handheld/findHandheldStore
	   * 例如：location=1001
	   */
	  @RequestMapping({"/findHandheldStore"})
	  @ResponseBody
	  public Object findHandheldStore()
	  {
	    JSONObject json = new JSONObject();
	    PageData pd = new PageData();
	    try {
	    	 pd = getPageData();
	    	 DBConnectionManager dbc=new DBConnectionManager();
	 	     PageData store=dbc.findHandheldStore(pd);
	 	    if(!store.containsKey("id")){
	 	    	json.put("result","false");
	 	    }else{
	 	    	json.put("result","true");
		 	    json.put("store",store);
	 	    }
		} catch (Exception e) {
			json.put("result","false");
		}
	    return json;
	  }
	 
	  
	  /**
	   * 修改或新增商品货位号
	   * 传入货位号信息：
	   * bar_code 条码，
	   * zone 区，第一级
	   * storey 层，第二级
	   * storey_num 所在层的编号
	   * json{result:false/true}
	   * @return
	   * @throws Exception
	   */
	  @RequestMapping({"/handheldUpdateProduct"})
	  @ResponseBody
	  public Object handheldUpdateProduct()throws Exception
	  {
	    JSONObject json = new JSONObject();
	    PageData pd = new PageData();
	    pd = getPageData();
		try {
			PageData cargoSpace=productService.handheldCargoSpace(pd);
			if(null==cargoSpace||!cargoSpace.containsKey("zone")){
				pd.put("cargo_space_num", "");
				cargoSpaceService.saveHandheld(pd);
			}else{
				pd.put("id",cargoSpace.getString("id"));
			}
			productinventoryService.updatehandheldInventory(pd);
			json.put("result","true");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result","false");
		}
	    return json;
	  }
	  
	  /**
	   * 扫码枪传入条码
	   * 回传商品规格，名称，条码，编码
	   * handheld/findProductScancode
	   * json{result:false/true,product:[{product_num:编码,bar_code:条码,product_name:名称,specification:商品规格,zone:区,storey:层,storey_num:所在层的编号}]}
	   * result=false,无product
	   * 例如：bar_code=6928804010220
	   * @return
	   * @throws Exception
	   */
	  @RequestMapping({"/findProductScancode"})
	  @ResponseBody
	  public Object findProductScancode(String bar_code)throws Exception
	  {
	    JSONObject json = new JSONObject();
	    PageData pd = new PageData();
	    pd = getPageData();
	    pd.put("bar_code",bar_code);
	    pd = productService.handheldfindProductScancode(pd);
	    if(null==pd||pd.equals("{}")||pd.equals("[]")){
	    	json.put("result","false");
	    }else{
	    	json.put("result","true");
	    	if(!pd.containsKey("zone")){
	    		pd.put("zone","");
	    		pd.put("storey","");
	    		pd.put("storey_num","");
	    	}
	    	json.put("product", pd);
	    }
	    return json;
	  }
	  /**
	   * 登录方法
	   * 传入值：userName，passWord
	   * 返回值：{result=success}
	   * 正确：success;
	   * 账号密码错误：usererror
	   * {result=身份验证失败!}
	   * @return
	   * 账号信息:session.setAttribute("sessionUser", user);
	   * @throws Exception
	   */
	  @RequestMapping({"/handheldLogin"})
	  @ResponseBody
	  public Object handheldLogin()throws Exception
	  {
		    Map<String, String> map = new HashMap();
		    PageData pd = new PageData();
		    pd = getPageData();
		    String errInfo = "";
		      Subject currentUser = SecurityUtils.getSubject();
		      Session session = currentUser.getSession();
		        String USERNAME = pd.getString("userName");
		        String PASSWORD = pd.getString("passWord");
		        pd.put("USERNAME", USERNAME);
		          String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();
		          pd.put("PASSWORD", passwd);
		          pd = this.userService.getUserByNameAndPwd(pd);
		          if (pd != null) {
		            pd.put("LAST_LOGIN", DateUtil.getTime().toString());
		            this.userService.updateLastLogin(pd);
		            User user = new User();
		            user.setUSER_ID(Long.parseLong(pd.getString("USER_ID")));
		            user.setUSERNAME(pd.getString("USERNAME"));
		            user.setPASSWORD(pd.getString("PASSWORD"));
		            user.setNAME(pd.getString("NAME"));
		            user.setRIGHTS(pd.getString("RIGHTS"));
		            user.setROLE_ID(pd.getString("ROLE_ID"));
		            user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
		            user.setIP(pd.getString("IP"));
		            user.setSTATUS(pd.getString("STATUS"));
		            user.setCkId(Integer.valueOf(pd.getString("ck_id")));
		            session.setAttribute("sessionUser", user);
		            Subject subject = SecurityUtils.getSubject();
		            UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, PASSWORD);
		            try {
		              subject.login(token);
		            } catch (AuthenticationException e) {
		              errInfo = "身份验证失败！";
		            }
		          }else {//账号或密码错误
		            errInfo = "usererror";
		          }
		        if (Tools.isEmpty(errInfo)) {
		          errInfo = "success";
		        }
		   
		    map.put("result", errInfo);
		    return AppUtil.returnObject(new PageData(), map);
	  }
	  
	  
	  
	  
	  
	  
}
