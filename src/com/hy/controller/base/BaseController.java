package com.hy.controller.base;

import java.util.Map;

import com.hy.entity.Page;
import com.hy.util.Logger;
import com.hy.util.PageData;
import com.hy.util.UuidUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {
	protected Logger logger = Logger.getLogger(getClass());

	public PageData getPageData() {
		return new PageData(getRequest());
	}

	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}

	public String get32UUID() {
		return UuidUtil.get32UUID();
	}

	public Page getPage() {
		return new Page();
	}

	public static void logBefore(Logger logger, String interfaceName) {
		logger.info("");
		logger.info("----------start----------");
		logger.info(interfaceName);
	}

	public void logMidway(Logger logger, String interfaceName) {
		logger.info("----------" + interfaceName);
	}

	public static void logAfter(Logger logger) {
		logger.info("end");
		logger.info("");
	}

	/**
	 * @Description: 操作步骤后打印日志至日志文件
	 * @param @param logger
	 * @param @param initialDescription 打印内容
	 * @throws @author
	 * @date
	 */
	public static void logEnd(Logger logger, String initialDescription) {
		try {
			logger.debug("==end-->" + initialDescription);
		} catch (Exception e) {
			logger.debug("==操作步骤后打印日志至日志文件，错误信息-->" + e.toString());
		}
	}
	 public Map<String, String> getHC()
	  {
	    Subject currentUser = SecurityUtils.getSubject();
	    Session session = currentUser.getSession();
	    return (Map)session.getAttribute("QX");
	  }
	 
	
	 
}
