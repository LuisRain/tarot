package com.hy.controller.system.syslog;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.Logger;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/sysLog"})
public class SysLogController
  extends BaseController
{
  @Resource(name="sysLogService")
  private SysLogService sysLogService;
  
  @RequestMapping({"/listAllSysLogs"})
  public ModelAndView listtabSysLogs(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    page.setPd(pd);
    List<PageData> sysLogList = this.sysLogService.listAllSysLog(pd);
    mv.setViewName("system/sysLog/sysLog_list");
    mv.addObject("sysLogList", sysLogList);
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/listSysLogs"})
  public ModelAndView listSysLogs(Page page)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    
    String operName = pd.getString("operName");
    String startDate = pd.getString("startDate");
    String endDate = pd.getString("endDate");
    if (!StringUtil.isEmpty(startDate)) {
      startDate = startDate + " 00:00:00";
    }
    if (!StringUtil.isEmpty(endDate)) {
      endDate = endDate + " 00:00:00";
    }
    pd.put("operName", operName);
    pd.put("startDate", startDate);
    pd.put("endDate", endDate);
    page.setPd(pd);
    
    List<PageData> sysLogList = this.sysLogService.listPdPageSysLog(page);
    mv.setViewName("system/sysLog/sysLog_list");
    mv.addObject("sysLogList", sysLogList);
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"/excel"})
  public ModelAndView exportExcel()
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      String operName = pd.getString("operName");
      String startDate = pd.getString("startDate");
      String endDate = pd.getString("endDate");
      if (!StringUtil.isEmpty(startDate)) {
        startDate = startDate + " 00:00:00";
      }
      if (!StringUtil.isEmpty(endDate)) {
        endDate = endDate + " 00:00:00";
      }
      pd.put("operName", operName);
      pd.put("startDate", startDate);
      pd.put("endDate", endDate);
      
      Map<String, Object> dataMap = new HashMap();
      List<String> titles = new ArrayList();
      
      titles.add("操作模块");
      titles.add("操作结果");
      titles.add("操作人员");
      titles.add("登录IP");
      titles.add("操作时间");
      
      dataMap.put("titles", titles);
      
      List<PageData> sysLogList = this.sysLogService.listAllUser(pd);
      List<PageData> varList = new ArrayList();
      for (int i = 0; i < sysLogList.size(); i++) {
        PageData vpd = new PageData();
        vpd.put("var1", ((PageData)sysLogList.get(i)).getString("op_model"));
        vpd.put("var2", ((PageData)sysLogList.get(i)).getString("result"));
        vpd.put("var3", ((PageData)sysLogList.get(i)).getString("oper_name"));
        vpd.put("var4", ((PageData)sysLogList.get(i)).getString("login_ip"));
        vpd.put("var5", ((PageData)sysLogList.get(i)).getString("create_date"));
        varList.add(vpd);
      }
      dataMap.put("varList", varList);
      ObjectExcelView erv = new ObjectExcelView();
      mv = new ModelAndView(erv, dataMap);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
}


