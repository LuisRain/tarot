package com.hy.controller.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.WaveSortingGroup;
import com.hy.service.order.WaveSortingGroupService;
import com.hy.util.LoginUtil;
import com.hy.util.PageData;

@Controller
@RequestMapping({"/waveSortingGroup"})
public class WaveSortingGroupController
  extends BaseController
{
  @Resource(name="waveSortingGroupService")
  private WaveSortingGroupService waveSortingGroupService;
  
  @RequestMapping({"/waveSortingGroupList"})
  public ModelAndView orderGroupList(Page page)
  {
    logBefore(this.logger, "列表waveSortingGroup");
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
      List<WaveSortingGroup> varList = this.waveSortingGroupService.waveSortingGroupNew(page);
      
      mv.setViewName("procurement/wavesorting/waveSortingGroup_list");
      mv.addObject("varList", varList);
      pd.put("flag", Integer.valueOf(0));
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
}


