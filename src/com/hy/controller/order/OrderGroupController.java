package com.hy.controller.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.OrderGroup;
import com.hy.service.order.OrderGroupService;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

@Controller
@RequestMapping({"/orderGroup"})
public class OrderGroupController
  extends BaseController
{
  @Resource(name="orderGroupService")
  private OrderGroupService orderGroupService;
  /**周转库批次管理***/
  @RequestMapping({"/orderGroupList"})
  public ModelAndView orderGroupList(Page page)
  {
    logBefore(this.logger, "列表orderGroup");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
      
      String startDate = pd.getString("startDate");
      String endDate = pd.getString("endDate");
      if ((startDate != null) && (!"".equals(startDate))) {
        startDate = startDate + " 00:00:00";
        pd.put("startDate", startDate);
      }
      if ((endDate != null) && (!"".equals(endDate))) {
        endDate = endDate + " 00:00:00";
        pd.put("endDate", endDate);
      }
      pd.put("ck_id",LoginUtil.getLoginUser().getCkId());
      page.setPd(pd);
      List<OrderGroup> varList = this.orderGroupService.orderGroup2listPage(pd);
      mv.setViewName("procurement/ordergroup/orderGroup_list");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  /**赠品批次管理***/
  @RequestMapping({"/orderGroupOfGift"})
  public ModelAndView orderGroupOfGift(Page page) { logBefore(this.logger, "赠品列表orderGroup");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
      
      String startDate = pd.getString("startDate");
      String endDate = pd.getString("endDate");
      if ((startDate != null) && (!"".equals(startDate))) {
        startDate = startDate + " 00:00:00";
        pd.put("startDate", startDate);
      }
      if ((endDate != null) && (!"".equals(endDate))) {
        endDate = endDate + " 00:00:00";
        pd.put("endDate", endDate);
      }
      pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
      page.setPd(pd);
      List<OrderGroup> varList = this.orderGroupService.orderGroupOfGift(pd);
      mv.setViewName("procurement/ordergroup/orderGroupOfGift_list");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  /**次品批次管理***/
  @RequestMapping({"/inferiOrderGroupList"})
  public ModelAndView inferiOrderGroupList(Page page)
  {
    logBefore(this.logger, "列表orderGroup");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
      String startDate = pd.getString("startDate");
      String endDate = pd.getString("endDate");
      if ((startDate != null) && (!"".equals(startDate))){
        startDate = startDate + " 00:00:00";
        pd.put("startDate", startDate);
      }
      if ((endDate != null) && (!"".equals(endDate))){
        endDate = endDate + " 00:00:00";
        pd.put("endDate", endDate);
      }
      pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
      page.setPd(pd);
      List<OrderGroup> varList = this.orderGroupService.inferiOrderGrouplistPage(page);
      mv.setViewName("procurement/ordergroup/inferiorOrderGroup_list");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/orderGroupList2"})
  public ModelAndView orderGroupList2(Page page) { logBefore(this.logger, "列表orderGroup");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
      
      String startDate = pd.getString("startDate");
      String endDate = pd.getString("endDate");
      if ((startDate != null) && (!"".equals(startDate))) {
        startDate = startDate + " 00:00:00";
        pd.put("startDate", startDate);
      }
      if ((endDate != null) && (!"".equals(endDate))) {
        endDate = endDate + " 00:00:00";
        pd.put("endDate", endDate);
      }
      page.setPd(pd);
      List<PageData> varList = this.orderGroupService.orderGrouplistPage(page);
      mv.setViewName("procurement/ordergroup/orderGroup_list");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
}


