package com.hy.controller.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.product.StageNumberService;
import com.hy.util.PageData;

@Controller
@RequestMapping({ "/stageNumber" })
public class StageNumberController extends BaseController {
	@Resource(name = "stageNumberService")
	private StageNumberService stageNumberService;
	
	@RequestMapping({"stageNumberlistPage"})
	public ModelAndView stageNumberlistPage(Page page){
		logBefore(this.logger, "查询期数页面");
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			String keyword=pd.getString("keyword");
			if(keyword!=null && !"".equals(keyword)){
				keyword=keyword.trim();
				pd.put("keyword", keyword);
			}
			page.setPd(pd);
			List<PageData> listpd=stageNumberService.stageNumberlistPage(page);
			mv.addObject("listpd", listpd);
			mv.addObject("pd", pd);
			mv.setViewName("inventorymanagement/stagenumber/stagenumber_list");
		} catch (Exception e) {
			// TODO: handle exception
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({ "/goAdd" })
	public ModelAndView goAdd() {
		logBefore(this.logger, "去新增页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			mv.setViewName("inventorymanagement/stagenumber/stagenumber_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({"/save"})
	public ModelAndView save(){
		ModelAndView mv=new ModelAndView();
		PageData pd=new PageData();
		try {
			pd=this.getPageData();
			PageData pdd=stageNumberService.findByName(pd);
			if(pdd==null){
				stageNumberService.save(pd);
			}
			mv.addObject("msg", "success");
			mv.setViewName("save_result");
		} catch (Exception e) {
			// TODO: handle exception
			this.logger.error(e.toString(), e);
		}
		return mv;
	} 
	
	@RequestMapping({ "/goEdit" })
	public ModelAndView goEdit() {
		logBefore(this.logger, "去修改页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd =this.getPageData();
			pd =stageNumberService.findById(pd);
			mv.setViewName("inventorymanagement/stagenumber/stagenumber_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@RequestMapping({ "/edit" })
	public ModelAndView edit() throws Exception {
		logBefore(this.logger, "修改Supplier");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData pdd=stageNumberService.findByName(pd);
			if(pdd==null){
				stageNumberService.edit(pd);
			}
			mv.addObject("msg", "success");
			mv.setViewName("save_result");
		} catch (Exception e) {
			// TODO: handle exception
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
}
