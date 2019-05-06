package com.hy.controller.system.head;

import com.hy.controller.base.BaseController;
import com.hy.service.system.appuser.AppuserService;
import com.hy.service.system.user.UserService;
import com.hy.util.AppUtil;
import com.hy.util.Logger;
import com.hy.util.PageData;
import com.hy.util.SmsUtil;
import com.hy.util.Tools;
import com.hy.util.Watermark;
import com.hy.util.mail.SimpleMailSender;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/head"})
public class HeadController
  extends BaseController
{
  @Resource(name="userService")
  private UserService userService;
  @Resource(name="appuserService")
  private AppuserService appuserService;
  
  @RequestMapping({"/getUname"})
  @ResponseBody
  public Object getList()
  {
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      pd = getPageData();
      List<PageData> pdList = new ArrayList();
      
      Subject currentUser = SecurityUtils.getSubject();
      Session session = currentUser.getSession();
      
      PageData pds = new PageData();
      pds = (PageData)session.getAttribute("userpds");
      
      if (pds == null) {
        String USERNAME = session.getAttribute("USERNAME").toString();
        pd.put("USERNAME", USERNAME);
        pds = this.userService.findByUId(pd);
        session.setAttribute("userpds", pds);
      }
      
      pdList.add(pds);
      map.put("list", pdList);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    } finally {
      logAfter(this.logger);
    }
    return AppUtil.returnObject(pd, map);
  }
  
  @RequestMapping({"/setSKIN"})
  public void setSKIN(PrintWriter out)
  {
    PageData pd = new PageData();
    try {
      pd = getPageData();
      
      Subject currentUser = SecurityUtils.getSubject();
      Session session = currentUser.getSession();
      
      String USERNAME = session.getAttribute("USERNAME").toString();
      pd.put("USERNAME", USERNAME);
      this.userService.setSKIN(pd);
      session.removeAttribute("userpds");
      session.removeAttribute("USERROL");
      out.write("success");
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/editEmail"})
  public ModelAndView editEmail()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/head/edit_email");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/goSendSms"})
  public ModelAndView goSendSms()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/head/send_sms");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/sendSms"})
  @ResponseBody
  public Object sendSms()
  {
    PageData pd = new PageData();
    pd = getPageData();
    Map<String, Object> map = new HashMap();
    String msg = "ok";
    int count = 0;
    int zcount = 0;
    
    List<PageData> pdList = new ArrayList();
    
    String PHONEs = pd.getString("PHONE");
    String CONTENT = pd.getString("CONTENT");
    String isAll = pd.getString("isAll");
    String TYPE = pd.getString("TYPE");
    String fmsg = pd.getString("fmsg");
    
    if ("yes".endsWith(isAll)) {
      try {
        List<PageData> userList = new ArrayList();
        
        userList = "appuser".equals(fmsg) ? this.appuserService.listAllUser(pd) : this.userService.listAllUser(pd);
        
        zcount = userList.size();
        try {
          for (int i = 0; i < userList.size(); i++) {
            if (Tools.checkMobileNumber(((PageData)userList.get(i)).getString("PHONE"))) {
              if ("1".equals(TYPE)) {
                SmsUtil.sendSms1(((PageData)userList.get(i)).getString("PHONE"), CONTENT);
              } else {
                SmsUtil.sendSms2(((PageData)userList.get(i)).getString("PHONE"), CONTENT);
              }
              count++;
            }
          }
          
          msg = "ok";
        } catch (Exception e) {
          msg = "error";
        }
        
        PHONEs = PHONEs.replaceAll("；", ";");
      }
      catch (Exception e)
      {
        msg = "error";
      }
    }
    else {
      PHONEs = PHONEs.replaceAll(" ", "");
      String[] arrTITLE = PHONEs.split(";");
      zcount = arrTITLE.length;
      try {
        for (int i = 0; i < arrTITLE.length; i++) {
          if (Tools.checkMobileNumber(arrTITLE[i])) {
            if ("1".equals(TYPE)) {
              SmsUtil.sendSms1(arrTITLE[i], CONTENT);
            } else {
              SmsUtil.sendSms2(arrTITLE[i], CONTENT);
            }
            count++;
          }
        }
        
        msg = "ok";
      } catch (Exception e) {
        msg = "error";
      }
    }
    pd.put("msg", msg);
    pd.put("count", Integer.valueOf(count));
    pd.put("ecount", Integer.valueOf(zcount - count));
    pdList.add(pd);
    map.put("list", pdList);
    return AppUtil.returnObject(pd, map);
  }
  
  @RequestMapping({"/goSendEmail"})
  public ModelAndView goSendEmail()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    mv.setViewName("system/head/send_email");
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/sendEmail"})
  @ResponseBody
  public Object sendEmail()
  {
    PageData pd = new PageData();
    pd = getPageData();
    Map<String, Object> map = new HashMap();
    String msg = "ok";
    int count = 0;
    int zcount = 0;
    
    String strEMAIL = Tools.readTxtFile("admin/config/EMAIL.txt");
    
    List<PageData> pdList = new ArrayList();
    
    String toEMAIL = pd.getString("EMAIL");
    String TITLE = pd.getString("TITLE");
    String CONTENT = pd.getString("CONTENT");
    String TYPE = pd.getString("TYPE");
    String isAll = pd.getString("isAll");
    
    String fmsg = pd.getString("fmsg");
    
    if ((strEMAIL != null) && (!"".equals(strEMAIL))) {
      String[] strEM = strEMAIL.split(",fh,");
      if (strEM.length == 4) {
        if ("yes".endsWith(isAll)) {
          try {
            List<PageData> userList = new ArrayList();
            
            userList = "appuser".equals(fmsg) ? this.appuserService.listAllUser(pd) : this.userService.listAllUser(pd);
            
            zcount = userList.size();
            try {
              for (int i = 0; i < userList.size(); i++) {
                if (Tools.checkEmail(((PageData)userList.get(i)).getString("EMAIL"))) {
                  SimpleMailSender.sendEmail(strEM[0], strEM[1], strEM[2], strEM[3], ((PageData)userList.get(i)).getString("EMAIL"), TITLE, CONTENT, TYPE);
                  count++;
                }
              }
              
              msg = "ok";
            } catch (Exception e) {
              msg = "error";
            }
            
            toEMAIL = toEMAIL.replaceAll("；", ";");
          }
          catch (Exception e)
          {
            msg = "error";
          }
        }
        else {
          toEMAIL = toEMAIL.replaceAll(" ", "");
          String[] arrTITLE = toEMAIL.split(";");
          zcount = arrTITLE.length;
          try {
            for (int i = 0; i < arrTITLE.length; i++) {
              if (Tools.checkEmail(arrTITLE[i])) {
                SimpleMailSender.sendEmail(strEM[0], strEM[1], strEM[2], strEM[3], arrTITLE[i], TITLE, CONTENT, TYPE);
                count++;
              }
            }
            
            msg = "ok";
          } catch (Exception e) {
            msg = "error";
          }
        }
      } else {
        msg = "error";
      }
    } else {
      msg = "error";
    }
    pd.put("msg", msg);
    pd.put("count", Integer.valueOf(count));
    pd.put("ecount", Integer.valueOf(zcount - count));
    pdList.add(pd);
    map.put("list", pdList);
    return AppUtil.returnObject(pd, map);
  }
  
  @RequestMapping({"/goSystem"})
  public ModelAndView goEditEmail()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    pd.put("YSYNAME", Tools.readTxtFile("admin/config/SYSNAME.txt"));
    pd.put("COUNTPAGE", Tools.readTxtFile("admin/config/PAGE.txt"));
    String strEMAIL = Tools.readTxtFile("admin/config/EMAIL.txt");
    String strSMS1 = Tools.readTxtFile("admin/config/SMS1.txt");
    String strSMS2 = Tools.readTxtFile("admin/config/SMS2.txt");
    String strFWATERM = Tools.readTxtFile("admin/config/FWATERM.txt");
    String strIWATERM = Tools.readTxtFile("admin/config/IWATERM.txt");
    pd.put("Token", Tools.readTxtFile("admin/config/WEIXIN.txt"));
    String strWEBSOCKET = Tools.readTxtFile("admin/config/WEBSOCKET.txt");
    
    if ((strEMAIL != null) && (!"".equals(strEMAIL))) {
      String[] strEM = strEMAIL.split(",fh,");
      if (strEM.length == 4) {
        pd.put("SMTP", strEM[0]);
        pd.put("PORT", strEM[1]);
        pd.put("EMAIL", strEM[2]);
        pd.put("PAW", strEM[3]);
      }
    }
    
    if ((strSMS1 != null) && (!"".equals(strSMS1))) {
      String[] strS1 = strSMS1.split(",fh,");
      if (strS1.length == 2) {
        pd.put("SMSU1", strS1[0]);
        pd.put("SMSPAW1", strS1[1]);
      }
    }
    
    if ((strSMS2 != null) && (!"".equals(strSMS2))) {
      String[] strS2 = strSMS2.split(",fh,");
      if (strS2.length == 2) {
        pd.put("SMSU2", strS2[0]);
        pd.put("SMSPAW2", strS2[1]);
      }
    }
    
    if ((strFWATERM != null) && (!"".equals(strFWATERM))) {
      String[] strFW = strFWATERM.split(",fh,");
      if (strFW.length == 5) {
        pd.put("isCheck1", strFW[0]);
        pd.put("fcontent", strFW[1]);
        pd.put("fontSize", strFW[2]);
        pd.put("fontX", strFW[3]);
        pd.put("fontY", strFW[4]);
      }
    }
    
    if ((strIWATERM != null) && (!"".equals(strIWATERM))) {
      String[] strIW = strIWATERM.split(",fh,");
      if (strIW.length == 4) {
        pd.put("isCheck2", strIW[0]);
        pd.put("imgUrl", strIW[1]);
        pd.put("imgX", strIW[2]);
        pd.put("imgY", strIW[3]);
      }
    }
    if ((strWEBSOCKET != null) && (!"".equals(strWEBSOCKET))) {
      String[] strIW = strWEBSOCKET.split(",fh,");
      if (strIW.length == 4) {
        pd.put("WIMIP", strIW[0]);
        pd.put("WIMPORT", strIW[1]);
        pd.put("OLIP", strIW[2]);
        pd.put("OLPORT", strIW[3]);
      }
    }
    
    mv.setViewName("system/head/sys_edit");
    mv.addObject("pd", pd);
    
    return mv;
  }
  
  @RequestMapping({"/saveSys"})
  public ModelAndView saveSys()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    Tools.writeFile("admin/config/SYSNAME.txt", pd.getString("YSYNAME"));
    Tools.writeFile("admin/config/PAGE.txt", pd.getString("COUNTPAGE"));
    Tools.writeFile("admin/config/EMAIL.txt", pd.getString("SMTP") + ",fh," + pd.getString("PORT") + ",fh," + pd.getString("EMAIL") + ",fh," + pd.getString("PAW"));
    Tools.writeFile("admin/config/SMS1.txt", pd.getString("SMSU1") + ",fh," + pd.getString("SMSPAW1"));
    Tools.writeFile("admin/config/SMS2.txt", pd.getString("SMSU2") + ",fh," + pd.getString("SMSPAW2"));
    mv.addObject("msg", "OK");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/saveSys2"})
  public ModelAndView saveSys2()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    Tools.writeFile("admin/config/FWATERM.txt", pd.getString("isCheck1") + ",fh," + pd.getString("fcontent") + ",fh," + pd.getString("fontSize") + ",fh," + pd.getString("fontX") + ",fh," + pd.getString("fontY"));
    Tools.writeFile("admin/config/IWATERM.txt", pd.getString("isCheck2") + ",fh," + pd.getString("imgUrl") + ",fh," + pd.getString("imgX") + ",fh," + pd.getString("imgY"));
    Watermark.fushValue();
    mv.addObject("msg", "OK");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/saveSys3"})
  public ModelAndView saveSys3()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    Tools.writeFile("admin/config/WEIXIN.txt", pd.getString("Token"));
    Tools.writeFile("admin/config/WEBSOCKET.txt", pd.getString("WIMIP") + ",fh," + pd.getString("WIMPORT") + ",fh," + pd.getString("OLIP") + ",fh," + pd.getString("OLPORT"));
    mv.addObject("msg", "OK");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/goProductCode"})
  public ModelAndView goProductCode()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("system/head/productCode");
    return mv;
  }
}


