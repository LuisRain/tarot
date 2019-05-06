package com.hy.util.word;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

public class WordMain
{
  private Configuration configuration = null;
  
  public WordMain() {
    this.configuration = new Configuration();
    this.configuration.setDefaultEncoding("utf-8");
  }
  
  public void createDoc(Map<String, Object> dataMap, String fileName)
    throws UnsupportedEncodingException
  {
    this.configuration.setClassForTemplateLoading(getClass(), "/ftl/createWord");
    Template t = null;
    try
    {
      t = this.configuration.getTemplate("exorder.ftl");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    File outFile = new File(fileName);
    Writer out = null;
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(outFile);
      OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
      
      out = new BufferedWriter(oWriter);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    try
    {
      t.process(dataMap, out);
      out.close();
      fos.close();
    } catch (TemplateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


