package com.hy.controller.app.appuser;

import com.aliyun.oss.OSSClient;
import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.system.appuser.WxService;
import com.hy.util.AppUtil;
import com.hy.util.DateUtil;
import com.hy.util.Logger;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.Tools;
import com.hy.util.UuidUtil;
import com.hy.util.wx.Sms;
import com.hy.util.wx.WechatTaskService;
import com.hy.util.wx.sign;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/weixin"})
public class WeixinController
  extends BaseController
{
  @Resource(name="wxService")
  private WxService wxService;
  
  @RequestMapping({"openDeliveryScan"})
  public ModelAndView openDeliveryScan(Page page)
  {
    PageData pd = new PageData();
    pd = getPageData();
    getSession().setAttribute("Userid", WechatTaskService.getUserid(pd.get("code").toString()));
    ModelAndView mv = getModelAndView();
    mv.addObject("wx", sign.sign(pd.get("code").toString()));
    mv.addObject("accesstoken", WechatTaskService.getAccessToken());
    mv.setViewName("wx/DeliveryScan_list");
    return mv;
  }
  
  @RequestMapping({"/saveorder"})
  @ResponseBody
  public Object saveorder() throws Exception {
    JSONObject json = new JSONObject();
    PageData pd = new PageData();
    pd = getPageData();
    String endpoint = Tools.readTxtFile("admin/config/oos/OOSURL.txt");
    
    String accessKeyId = Tools.readTxtFile("admin/config/oos/ACCESSKEYID.txt");
    String accessKeySecret = Tools.readTxtFile("admin/config/oos/ACCESSKEYSECRET.txt");
    
    String[] mediaId = pd.get("serverId").toString().split(",");
    OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    StringBuffer imagUrl = new StringBuffer();
    for (int i = 0; i < mediaId.length; i++) {
      String wxUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=" + WechatTaskService.getAccessToken() + "&media_id=" + mediaId[i];
      
      InputStream inputStream = new URL(wxUrl).openStream();
      String filename = UuidUtil.get32UUID() + DateUtil.getTimes() + ".jpg";
      ossClient.putObject("hywxfile", filename, inputStream);
      imagUrl.append(Tools.readTxtFile("admin/config/oos/OOSIMGURL.txt").toString() + filename + ",");
    }
    
    PageData wxUser = this.wxService.getWxUserByUserid(getSession().getAttribute("Userid").toString());
    pd.put("fileurl", imagUrl);
    pd.put("create_time", DateUtil.getTime());
    pd.put("name", wxUser.get("name"));
    pd.put("phone", wxUser.get("phone"));
    pd.put("number_plate", wxUser.get("number_plate"));
    pd.put("userid", wxUser.get("userid"));
    pd.put("driver_id", wxUser.get("id"));
    int state = Sms.sendSMS(pd.getString("phone_sms"), Sms.getSmsText(), "");
    pd.put("remark", Integer.valueOf(state));
    this.wxService.saveOrder(pd);
    
    ossClient.shutdown();
    json.put("ok", "ok");
    return json;
  }
  
  @RequestMapping({"updateorderlist"})
  public ModelAndView updateorderlist(Page page) throws Exception {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String searchcriteria = pd.getString("searchcriteria");
    String keyword = pd.getString("keyword");
    if ((keyword != null) && (!"".equals(keyword))) {
      keyword = keyword.trim();
      pd.put("keyword", keyword);
      pd.put("searchcriteria", searchcriteria);
    }
    page.setPd(pd);
    List<PageData> list = this.wxService.updateorderlistPage(page);
    mv.addObject("list", list);
    mv.addObject("pd", pd);
    mv.setViewName("wx/wxupdateorderlist");
    return mv;
  }
  
  @RequestMapping({"getupdateorder"})
  public ModelAndView getupdateorder(Page page) throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    PageData updateorder = this.wxService.getupdateorder(pd);
    List<String> imgList = new ArrayList();
    String[] img = updateorder.get("fileurl").toString().split(",");
    Collections.addAll(imgList, img);
    mv.addObject("updateorder", updateorder);
    mv.addObject("imgList", imgList);
    mv.setViewName("wx/getupdateorder");
    return mv;
  }
  
  @RequestMapping({"getWxUserListPage"})
  public ModelAndView getWxUserListPage(Page page) throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String searchcriteria = pd.getString("searchcriteria");
    String keyword = pd.getString("keyword");
    if ((keyword != null) && (!"".equals(keyword))) {
      keyword = keyword.trim();
      pd.put("keyword", keyword);
      pd.put("searchcriteria", searchcriteria);
    }
    page.setPd(pd);
    List<PageData> list = this.wxService.getWxUserListPage(page);
    mv.addObject("list", list);
    mv.addObject("pd", pd);
    mv.setViewName("wx/wxUserlist");
    return mv;
  }
  
  @RequestMapping({"openUserEdit"})
  public ModelAndView openUserEdit() throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.addObject("msg", "save");
    mv.setViewName("wx/wx_user_edit");
    return mv;
  }
  
  @RequestMapping({"/save"})
  public ModelAndView save()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    pd.put("userid", UuidUtil.get32UUID());
    pd.put("create_time", DateUtil.getTimes());
    if (WechatTaskService.createwxUser(pd)) {
      if (this.wxService.saveWxUser(pd).booleanValue()) {
        mv.addObject("msg", "success");
      } else {
        mv.addObject("msg", "failed");
      }
    }
    else {
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/edit"})
  public ModelAndView edit()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    PageData wxUser = this.wxService.getWxUserByUserid(pd.get("userid").toString());
    wxUser.put("number_plate", pd.get("number_plate").toString());
    wxUser.put("phone", pd.get("phone").toString());
    wxUser.put("name", pd.get("name").toString());
    if (WechatTaskService.updatewxUser(pd)) {
      this.wxService.updateWxUser(wxUser);
      mv.addObject("msg", "success");
    } else {
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/delete"})
  public ModelAndView delete()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    if (WechatTaskService.deletewxUser(pd)) {
      this.wxService.deleteWxUser(pd);
      mv.addObject("msg", "success");
    } else {
      mv.addObject("msg", "failed");
    }
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"openUpdateEdit"})
  public ModelAndView openUpdateEdit() throws Exception { ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    PageData wxUser = this.wxService.getWxUserByUserid(pd.get("userid").toString());
    mv.addObject("msg", "edit");
    mv.addObject("pd", wxUser);
    mv.setViewName("wx/wx_user_edit");
    return mv;
  }
  
  @RequestMapping({"/hasP"})
  @ResponseBody
  public Object hasP()
  {
    Map<String, String> map = new HashMap();
    String errInfo = "success";
    PageData pd = new PageData();
    try {
      pd = getPageData();
      if (pd.getString("tell").trim().equals(WechatTaskService.STATUS)) {
        this.wxService.findByphone();
      }
      if (this.wxService.findByphone(pd) != null) {
        errInfo = "error";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    map.put("result", errInfo);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"getSmsListPage"})
  public ModelAndView getSmsListPage(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    String searchcriteria = pd.getString("searchcriteria");
    String keyword = pd.getString("keyword");
    if ((keyword != null) && (!"".equals(keyword))) {
      keyword = keyword.trim();
      pd.put("keyword", keyword);
      pd.put("searchcriteria", searchcriteria);
    }
    page.setPd(pd);
    List<PageData> list = this.wxService.getWxSmslistPage(page);
    mv.addObject("list", list);
    mv.addObject("pd", pd);
    mv.setViewName("wx/wxSmslist");
    return mv;
  }
  
  @RequestMapping({"orderExcel"})
  public ModelAndView orderExcel(Page page) throws Exception
  {
    ModelAndView mv = getModelAndView();
    Map<String, Object> dataMap = new HashMap();
    List<String> titles = new ArrayList();
    titles.add("序号");
    titles.add("姓名");
    titles.add("车牌");
    titles.add("手机号");
    titles.add("日期");
    titles.add("门店编码");
    titles.add("短信状态");
    dataMap.put("titles", titles);
    List<PageData> list = this.wxService.updateorderlist();
    List<PageData> varList = new ArrayList();
    for (int i = 0; i < list.size(); i++) {
      PageData vpd = new PageData();
      vpd.put("var1", Integer.valueOf(i + 1));
      vpd.put("var2", ((PageData)list.get(i)).getString("name"));
      vpd.put("var3", ((PageData)list.get(i)).getString("number_plate"));
      vpd.put("var4", ((PageData)list.get(i)).getString("phone"));
      vpd.put("var5", ((PageData)list.get(i)).getString("create_time"));
      vpd.put("var6", ((PageData)list.get(i)).getString("site"));
      vpd.put("var7", ((PageData)list.get(i)).getString("remark"));
      varList.add(vpd);
    }
    dataMap.put("varList", varList);
    ObjectExcelView erv = new ObjectExcelView();
    mv = new ModelAndView(erv, dataMap);
    
    return mv;
  }

}


