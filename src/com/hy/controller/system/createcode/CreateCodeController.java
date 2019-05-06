package com.hy.controller.system.createcode;

import com.hy.controller.base.BaseController;
import com.hy.util.DelAllFile;
import com.hy.util.FileDownload;
import com.hy.util.FileZip;
import com.hy.util.Freemarker;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/createCode"})
public class CreateCodeController
  extends BaseController
{
  @RequestMapping({"/proCode"})
  public void proCode(HttpServletResponse response)
    throws Exception
  {
    PageData pd = new PageData();
    pd = getPageData();
    
    String packageName = pd.getString("packageName");
    String objectName = pd.getString("objectName");
    String tabletop = pd.getString("tabletop");
    tabletop = tabletop == null ? "" : tabletop.toUpperCase();
    String zindext = pd.getString("zindex");
    int zindex = 0;
    if ((zindext != null) && (!"".equals(zindext))) {
      zindex = Integer.parseInt(zindext);
    }
    List<String[]> fieldList = new ArrayList();
    for (int i = 0; i < zindex; i++) {
      fieldList.add(pd.getString("field" + i).split(",hy,"));
    }
    
    Map<String, Object> root = new HashMap();
    root.put("fieldList", fieldList);
    root.put("packageName", packageName);
    root.put("objectName", objectName);
    root.put("objectNameLower", objectName.toLowerCase());
    root.put("objectNameUpper", objectName.toUpperCase());
    root.put("tabletop", tabletop);
    root.put("nowDate", new Date());
    
    DelAllFile.delFolder(PathUtil.getClasspath() + "admin/ftl");
    
    String filePath = "admin/ftl/code/";
    String ftlPath = "createCode";
    
    Freemarker.printFile("controllerTemplate.ftl", root, "controller/" + packageName + "/" + objectName.toLowerCase() + "/" + objectName + "Controller.java", filePath, ftlPath);
    
    Freemarker.printFile("serviceTemplate.ftl", root, "service/" + packageName + "/" + objectName.toLowerCase() + "/" + objectName + "Service.java", filePath, ftlPath);
    
    Freemarker.printFile("mapperMysqlTemplate.ftl", root, "mybatis_mysql/" + packageName + "/" + objectName + "Mapper.xml", filePath, ftlPath);
    Freemarker.printFile("mapperOracleTemplate.ftl", root, "mybatis_oracle/" + packageName + "/" + objectName + "Mapper.xml", filePath, ftlPath);
    
    Freemarker.printFile("mysql_SQL_Template.ftl", root, "mysql数据库脚本/" + tabletop + objectName.toUpperCase() + ".sql", filePath, ftlPath);
    Freemarker.printFile("oracle_SQL_Template.ftl", root, "oracle数据库脚本/" + tabletop + objectName.toUpperCase() + ".sql", filePath, ftlPath);
    
    Freemarker.printFile("jsp_list_Template.ftl", root, "jsp/" + packageName + "/" + objectName.toLowerCase() + "/" + objectName.toLowerCase() + "_list.jsp", filePath, ftlPath);
    Freemarker.printFile("jsp_edit_Template.ftl", root, "jsp/" + packageName + "/" + objectName.toLowerCase() + "/" + objectName.toLowerCase() + "_edit.jsp", filePath, ftlPath);
    
    Freemarker.printFile("docTemplate.ftl", root, "说明.doc", filePath, ftlPath);
    
    FileZip.zip(PathUtil.getClasspath() + "admin/ftl/code", PathUtil.getClasspath() + "admin/ftl/code.zip");
    
    FileDownload.fileDownload(response, PathUtil.getClasspath() + "admin/ftl/code.zip", "code.zip");
  }
}


