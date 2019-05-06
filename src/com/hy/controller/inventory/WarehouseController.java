package com.hy.controller.inventory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.inventory.WarehouseService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.AppUtil;
import com.hy.util.Const;
import com.hy.util.Logger;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/warehouse"})
public class WarehouseController
  extends BaseController
{
  @Resource(name="warehouseService")
  private WarehouseService warehouseService;
  @Resource(name="sysLogService")
  private SysLogService sysLogService;
  
  @RequestMapping({"/save"})
  public ModelAndView save()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    try {
      logBefore(this.logger, "新增warehouse");
      PageData pd = new PageData();
      pd = getPageData();
      pd.put("warehouse_number", "LVT_" + StringUtil.getStringOfMillisecond(""));
      this.warehouseService.save(pd);
      mv.addObject("msg", "success");
      mv.setViewName("save_result");
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/delete"})
  public void delete(PrintWriter out)
  {
    logBefore(this.logger, "删除warehouse");
    PageData pd = new PageData();
    try {
      pd = getPageData();
      this.warehouseService.delete(pd);
      out.write("success");
      out.close();
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
  }
  
  @RequestMapping({"/edit"})
  public ModelAndView edit()
    throws Exception
  {
    logBefore(this.logger, "修改warehouse");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    this.warehouseService.edit(pd);
    mv.addObject("msg", "success");
    mv.setViewName("save_result");
    return mv;
  }
  
  @RequestMapping({"/list"})
  public ModelAndView list(Page page)
  {
    logBefore(this.logger, "列表warehouse");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    try {
      pd = getPageData();
      page.setPd(pd);
      String searchcriteria = pd.getString("searchcriteria");
      String keyword = pd.getString("keyword");
      if ((keyword != null) && (!"".equals(keyword))) {
        keyword = keyword.trim();
        pd.put("keyword", keyword);
        pd.put("searchcriteria", searchcriteria);
      }
      List<PageData> varList = this.warehouseService.list(page);
      mv.setViewName("inventorymanagement/warehouse/warehouse_list");
      mv.addObject("varList", varList);
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goAdd"})
  public ModelAndView goAdd()
  {
    logBefore(this.logger, "去新增warehouse页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      mv.setViewName("inventorymanagement/warehouse/warehouse_edit");
      mv.addObject("msg", "save");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/goEdit"})
  public ModelAndView goEdit()
  {
    logBefore(this.logger, "去修改warehouse页面");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try {
      pd = this.warehouseService.findById(pd);
      mv.setViewName("inventorymanagement/warehouse/warehouse_edit");
      mv.addObject("msg", "edit");
      mv.addObject("pd", pd);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/deleteAll"})
  @ResponseBody
  public Object deleteAll()
  {
    logBefore(this.logger, "批量删除warehouse");
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      pd = getPageData();
      List<PageData> pdList = new ArrayList();
      String DATA_IDS = pd.getString("DATA_IDS");
      if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
        String[] ArrayDATA_IDS = DATA_IDS.split(",");
        this.warehouseService.deleteAll(ArrayDATA_IDS);
        pd.put("msg", "ok");
      } else {
        pd.put("msg", "no");
      }
      pdList.add(pd);
      map.put("list", pdList);
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    } finally {
      logAfter(this.logger);
    }
    return AppUtil.returnObject(pd, map);
  }
  
	/**
	 * 仓库分类列表
	 */
	@RequestMapping(value="/warehouselist")
	public ModelAndView warehouselist(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		String operationMsg = "列表仓库信息";
		logBefore(logger, operationMsg);
		try{
			mv.setViewName("inventorymanagement/warehouse/warehousetypelist");
			mv.addObject("pd", pd);
			//mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
		} catch(Exception e){
			//logMidway(logger, operationMsg+"，出现错误："+e.toString(),1);
		}finally {
			//logEnd(logger, operationMsg);
		}
		return mv;
	}

	/**
	 * 仓库分类列表树形菜单
	 */
	@RequestMapping(value = "/warehousetypelist", produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String eqAnalysisChart() {
		JSONObject obj=new JSONObject();
		try {
			PageData pd=this.getPageData();
			List<PageData>	varList = warehouseService.warehouselist(null);
			Map<String,Object> map=new HashMap<String,Object>();
			for(int i=1;i<5;i++){
				for(int j=0;j<varList.size();j++){
					PageData var=varList.get(j);
					if(Integer.valueOf(var.getString("level_id"))==1){
						map.put("name",var.getString("id")+"---"+var.getString("name"));
						map.put("id",var.getString("id"));
						map.put("level_id",var.getString("level_id"));
						map.put("spread","true");
						map.put("parent_id",var.getString("parent_id"));
						map.put("children", retuData(var.getString("id"),var.getString("name"),varList));
					}
				}
			}
			obj.put("data",map);
			obj.put("li",varList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return obj.toString();
	}
	/**
	 * 回调方法
	 * @param id
	 * @param name
	 * @param list
	 * @return
	 */
	public JSONArray retuData(String id,String name,List<PageData> list){
		JSONArray js=new JSONArray();
		for(int i=0;i<list.size();i++){
			PageData var=list.get(i);
			if(id.equals(var.getString("parent_id"))){
				JSONObject obj=new JSONObject();
				obj.put("name",var.getString("id")+"---"+var.getString("name"));
				obj.put("id",var.getString("id"));
				obj.put("level_id",var.getString("level_id"));
				obj.put("parent_name",name);
				obj.put("spread","true");
				obj.put("parent_id",var.getString("parent_id"));
				obj.put("children", retuData(var.getString("id"),var.getString("name"),list));
				js.add(obj);
			}
		}
		return js;
	}
	
	/**
	 * 仓库分类修改上级仓库
	 */
	@RequestMapping(value = "/updatewarehousetypelist", produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String updatewarehousetypelist() {
		JSONObject obj=new JSONObject();
		try {
			PageData pd=this.getPageData();
			warehouseService.updatewarehousetypelist(pd);
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		} finally {
		}
	}
	/**
	 * 仓库分类列表
	 */
	@RequestMapping(value = "/warehousetypecklist", produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String warehousetypecklist() {
		JSONObject obj=new JSONObject();
		try {
			List<PageData>	varList = warehouseService.warehouselist(null);
			obj.put("data",varList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return obj.toString();
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/gotypeAdd")
	public ModelAndView gotypeAdd(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String operationMsg = "去新增仓库信息页面";
		logBefore(logger, operationMsg);
		try {
			List<PageData>	varList = warehouseService.warehouselist(null);
			mv.addObject("varList", varList);
			mv.setViewName("inventorymanagement/warehouse/warehousetype_add");
			mv.addObject("msg", "typesave");
			mv.addObject("pd", pd);
			//sysLogService.saveLog(operationMsg, "成功",2);
		} catch(Exception e){
			//logMidway(logger, operationMsg+"，出现错误："+e.toString(),1);
		}finally {
			//logEnd(logger, operationMsg);
		}						
		return mv;
	}	
	/**
	 * 新增
	 */
	@RequestMapping(value="/typesave")
	public ModelAndView typesave() {
		ModelAndView mv = this.getModelAndView();
		String operationMsg = "新增仓库信息";
		logBefore(logger, operationMsg);
		try{
		PageData pd = new PageData();
		pd=this.getPageData();
		String[] wareh=pd.getString("warehouse_id").split(",");
		pd.put("warehouse_id",wareh[0]);
		pd.put("level_id",Integer.valueOf(wareh[1])+1 );
		warehouseService.typesave(pd);
		//logMidwayOfSuccessOrFail(logger,operationMsg, "成功");
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		//sysLogService.saveLog(operationMsg+pd.getString("warehouse_number"), "成功",2);
		} catch(Exception e){
			//logMidway(logger, operationMsg+"，出现错误："+e.toString(),1);
		}finally {
			//logEnd(logger, operationMsg);
		}
		return mv;
	}
	
  
}


