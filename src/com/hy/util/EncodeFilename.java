package com.hy.util;

import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class EncodeFilename
{
	     
  public static String encodeFilename(String filename, HttpServletRequest request)
  {
    String agent = request.getHeader("USER-AGENT");
    try {
      if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
        String newFileName = URLEncoder.encode(filename, "UTF-8");
        newFileName = StringUtils.replace(newFileName, "+", "%20");
        if (newFileName.length() > 150)
          newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
        return StringUtils.replace(newFileName, " ", "%20");
      }
      
      if ((agent != null) && (-1 != agent.indexOf("Mozilla"))) {
        return MimeUtility.encodeText(filename, "UTF-8", "B");
      }
      return filename;
    } catch (Exception ex) {}
    return filename;
  }
}


