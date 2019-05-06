package com.hy.controller.system.tools;

import com.hy.controller.base.BaseController;
import com.hy.util.AppUtil;
import com.hy.util.MapDistance;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import com.hy.util.TwoDimensionCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/tool"})
public class ToolController
  extends BaseController
{
  @RequestMapping({"/interfaceTest"})
  public ModelAndView editEmail()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/interfaceTest");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/severTest"})
  @ResponseBody
  public Object severTest()
  {
    Map<String, String> map = new HashMap();
    PageData pd = new PageData();
    pd = getPageData();
    String errInfo = "success";String str = "";String rTime = "";
    try {
      long startTime = System.currentTimeMillis();
      URL url = new URL(pd.getString("serverUrl"));
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod(pd.getString("requestMethod"));
      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
      long endTime = System.currentTimeMillis();
      String temp = "";
      while ((temp = in.readLine()) != null) {
        str = str + temp;
      }
      rTime = String.valueOf(endTime - startTime);
    }
    catch (Exception e) {
      errInfo = "error";
    }
    map.put("errInfo", errInfo);
    map.put("result", str);
    map.put("rTime", rTime);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/goSendEmail"})
  public ModelAndView goSendEmail()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/email");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/goTwoDimensionCode"})
  public ModelAndView goTwoDimensionCode()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/twoDimensionCode");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/createTwoDimensionCode"})
  @ResponseBody
  public Object createTwoDimensionCode()
  {
    Map<String, String> map = new HashMap();
    PageData pd = new PageData();
    pd = getPageData();
    String errInfo = "success";String encoderImgId = get32UUID() + ".png";
    String encoderContent = pd.getString("encoderContent");
    if (encoderContent == null) {
      errInfo = "error";
    } else {
      try {
        String filePath = PathUtil.getClasspath() + "uploadFiles/twoDimensionCode/" + encoderImgId;
        TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");
      } catch (Exception e) {
        errInfo = "error";
      }
    }
    map.put("result", errInfo);
    map.put("encoderImgId", encoderImgId);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/readTwoDimensionCode"})
  @ResponseBody
  public Object readTwoDimensionCode()
  {
    Map<String, String> map = new HashMap();
    PageData pd = new PageData();
    pd = getPageData();
    String errInfo = "success";String readContent = "";
    String imgId = pd.getString("imgId");
    if (imgId == null) {
      errInfo = "error";
    } else {
      try {
        String filePath = PathUtil.getClasspath() + "uploadFiles/twoDimensionCode/" + imgId;
        readContent = TwoDimensionCode.decoderQRCode(filePath);
      } catch (Exception e) {
        errInfo = "error";
      }
    }
    map.put("result", errInfo);
    map.put("readContent", readContent);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/ztree"})
  public ModelAndView ztree()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/ztree");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/map"})
  public ModelAndView map()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/map");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/mapXY"})
  public ModelAndView mapXY()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/mapXY");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/getDistance"})
  @ResponseBody
  public Object getDistance()
  {
    Map<String, String> map = new HashMap();
    PageData pd = new PageData();
    pd = getPageData();
    String errInfo = "success";String distance = "";
    try {
      distance = MapDistance.getDistance(pd.getString("ZUOBIAO_Y"), pd.getString("ZUOBIAO_X"), pd.getString("ZUOBIAO_Y2"), pd.getString("ZUOBIAO_X2"));
    } catch (Exception e) {
      errInfo = "error";
    }
    map.put("result", errInfo);
    map.put("distance", distance);
    return AppUtil.returnObject(new PageData(), map);
  }
  
  @RequestMapping({"/printTest"})
  public ModelAndView printTest()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/printTest");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/printPage"})
  public ModelAndView printPage()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/tools/printPage");
    mv.addObject("pd", pd);
    return mv;
  }
}


