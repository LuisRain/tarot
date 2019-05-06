package com.hy.controller.product;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hy.util.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.product.SupplierService;
import com.hy.service.product.archivesExcel;
import com.hy.service.system.user.UserService;


@Controller
@RequestMapping({ "/supplier" })
public class SupplierController extends BaseController {
	String menuUrl = "supplier/list.do";
	@Resource(name = "supplierService")
	private SupplierService supplierService;
	@Resource(name="userService")
	private UserService userService;
	@Resource
	private archivesExcel archivesExcel;
	@RequestMapping({ "savearchives" })
	public String savearchives() {
		logBefore(this.logger, "添加档案信息");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			if(pd.containsKey("id")&&!pd.getString("id").equals("")){
				supplierService.updatearchives(pd);
			}else{
				supplierService.savearchives(pd);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:getProductInfoById.do?supplier_id="+pd.getString("supplier_id");
	}

	@RequestMapping(value = "/savebrand")
	@ResponseBody
	public Object savebrand() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		String operationMsg = "添加品牌信息";
		logBefore(logger, operationMsg);
		try {
			pd = this.getPageData();
			supplierService.savebrand(pd);
			map.put("error", "添加成功");
		} catch (Exception e) {
			map.put("error", "添加失败");
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	@RequestMapping(value = "/savecontract")
	@ResponseBody
	public Object savecontract() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		String operationMsg = "查询供应商信息";
		logBefore(logger, operationMsg);
		try {
			pd = this.getPageData();
			supplierService.savecontract(pd);
			supplierService.updatecontract(pd);
			map.put("error", "添加成功");
		} catch (Exception e) {
			map.put("error", "添加失败");
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	
	@RequestMapping({ "getProductInfoById" })
	public ModelAndView getProductInfoById(Page page) {
		logBefore(this.logger, "查看商品详细信息");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		try {
			pd = getPageData();
			List<PageData> findcontract=supplierService.findcontract(pd);
			List<PageData> findbrand=supplierService.findbrand(pd);
			PageData findarchives=supplierService.findarchives(pd);
			pd.put("id", pd.getString("supplier_id"));
			PageData supp=supplierService.findById(pd);
			mv.addObject("findcontract", findcontract);
			mv.addObject("findbrand", findbrand);
			mv.addObject("findarchives", findarchives);
			mv.addObject("supp", supp);
			mv.setViewName("procurement/supplier/supplier_brand");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	@RequestMapping({ "/list" })
	public ModelAndView list(Page page) throws Exception {
		logBefore(this.logger, "列表supplier");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		List<PageData> supplerList = this.supplierService.listAll(page);
		mv.setViewName("procurement/supplier/supplier_list");
		mv.addObject("supplerList", supplerList);
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping({ "/supplierlist" })
	public ModelAndView supplierList(Page page) throws Exception {
		logBefore(this.logger, "列表supplier");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		List<PageData> supplerList = this.supplierService.listAll(page);
		mv.setViewName("procurement/product/select_supplier");
		mv.addObject("supplerList", supplerList);
		mv.addObject("pd", pd);
		return mv;
	}

	@RequestMapping({ "/deleteSupplier" })
	public void deleteSupplier(PrintWriter out) {
		PageData pd = new PageData();
		try {
			pd = getPageData();
			this.supplierService.deleteSupplier(pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
	}

	@RequestMapping({ "/deleteAllSupller" })
	@ResponseBody
	public Object deleteAllSuppler() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap();
		try {
			pd = getPageData();
			List<PageData> pdList = new ArrayList();
			String SUPPLE_IDS = pd.getString("SUPPLE_IDS");
			if ((SUPPLE_IDS != null) && (!"".equals(SUPPLE_IDS))) {
				String[] ArraySUPPLE_IDS = SUPPLE_IDS.split(",");
				this.supplierService.deleteAllSupller(ArraySUPPLE_IDS);
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

	@RequestMapping({ "/goAdd" })
	public ModelAndView goAdd() {
		logBefore(this.logger, "去新增Supplier页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		try {
			mv.setViewName("procurement/supplier/supplier_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	@RequestMapping({ "/save" })
	public ModelAndView save() throws Exception {
		logBefore(this.logger, "新增Supplier");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		PageData pdR = new PageData();
		PageData pdd=new PageData();
		pdd.put("USERNAME", pd.getString("CONTACT_PERSON_MOBILE"));
		pdd.put("PASSWORD", new SimpleHash("SHA-1",pd.getString("CONTACT_PERSON_MOBILE"), "123456").toString());
		pdd.put("NAME", pd.getString("CONTACT_PERSON"));
		pdd.put("RIGHTS", "");
		pdd.put("ROLE_ID", 24); //供应商ROLE_ID 23
		pdd.put("LAST_LOGIN","");//最后登录时间
		pdd.put("IP", "");
		pdd.put("STATUS",0);
		pdd.put("BZ", "");
		pdd.put("SKIN","default");
		pdd.put("EMAIL","");
		pdd.put("NUMBER", pd.getString("SUPPLIER_ID"));
		pdd.put("PHONE", pd.getString("CONTACT_PERSON_MOBILE"));
		pdd.put("ck_id", 1);
		if(userService.findByUId(pdd)==null){
			if(Jurisdiction.buttonJurisdiction(menuUrl, "add")){
				userService.saveU(pdd);
			}
		}
		String id = pd.getString("SUPPLIER_ID");
		pdR = this.supplierService.findMById(id);
		if ((pdR != null) && (!StringUtil.isEmpty(pdR.getString("SUPPLIER_NUM")))) {
			pd.put("SUPPLIER_NUM", pdR.getString("SUPPLIER_NUM"));
			pd.put("CREATE_DATE", Tools.date2Str(new Date()));
			this.supplierService.editM(pd);
		} else {
			pd.put("STATE", "1");
			pd.put("CREATE_DATE", Tools.date2Str(new Date()));
			//pd.put("SUPPLIER_NUM","SP_" + StringUtil.getStringOfMillisecond(""));
			this.supplierService.save(pd);
		}
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping({ "/edit" })
	public ModelAndView edit() throws Exception {
		logBefore(this.logger, "修改Supplier");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		if(!pd.getString("oldphone").equals(pd.getString("CONTACT_PERSON_MOBILE"))){  //更改手机号
			pd.put("USERNAME", pd.getString("oldphone"));
			PageData user=userService.findByUId(pd);
			user.put("USERNAME", pd.getString("CONTACT_PERSON_MOBILE"));
			user.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("CONTACT_PERSON_MOBILE"), "123456").toString());
			userService.updateusername(user);
		}
		this.supplierService.edit(pd);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	@RequestMapping({ "/goEdit" })
	public ModelAndView goEdit() {
		logBefore(this.logger, "去修改Supplier页面");
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		PageData pdd=new PageData();
		pd = getPageData();
		try {
			pd = this.supplierService.findById(pd);
			mv.setViewName("procurement/supplier/supplier_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
			mv.addObject("pdd", pdd);
		} catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 展示供应商 列表数据
	 * @return
	 */
	@RequestMapping(value = "/findSupplierList")
	@ResponseBody
	public Object findSupplierList() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		String operationMsg = "查询供应商信息";
		logBefore(logger, operationMsg);
		try {
			pd = this.getPageData();
			String supplierName = pd.getString("supplierName");
			if(null != supplierName && !"".equals("supplierName")){
				pd.put("supplier_name", supplierName);
			}
			
			List<PageData> supplierList = supplierService.findSupplier(pd);
			map.put("list", supplierList);
		} catch (Exception e) {
			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
		} finally {
			logEnd(logger, operationMsg);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	 @RequestMapping(value="/findarchivesExcel")
	 	public ModelAndView findarchivesExcel(){
	 		ModelAndView mv=new ModelAndView();
	 		PageData pd=new PageData();
	 		String operationMsg="ProductExcel导出库存操作";
	 		logBefore(logger, operationMsg);
	 		try {
	 			pd=this.getPageData();
	 			String orderdate=pd.getString("order_date");
	 			Map<String,Object> map=new HashMap<String, Object>();
	 			map.put("pd", pd);
	 			mv=new ModelAndView(archivesExcel,map);
	 		} catch (Exception e) {
	 			// TODO: handle exception
	 			e.printStackTrace();
	 			logMidway(logger, operationMsg + "，出现错误：" + e.toString());
	 		}finally{
	 			logEnd(logger, operationMsg);
	 		}
	 		return mv;
	 	}

	/**
	 * 导出供应商信息
	 * @param page
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping({"/excel"})
	public ModelAndView exportExcel(Page page)
			throws UnsupportedEncodingException
	{
		ModelAndView mv = getModelAndView();
		PageData pd = new PageData();
		pd = getPageData();
		String searchcriteria = pd.getString("searchcriteria");
		String keyword = pd.getString("keyword");
		if ((keyword != null) && (!"".equals(keyword))) {
			keyword = URLDecoder.decode(keyword, "utf-8");
			keyword = keyword.trim();
			pd.put("keyword", keyword);
			pd.put("searchcriteria", searchcriteria);
		}
		page.setPd(pd);
		try {
			Map<String, Object> dataMap = new HashMap();
			List<String> titles = new ArrayList();
			titles.add("供应商ID");
			titles.add("供应商名称");
			titles.add("编码");
			titles.add("简称");
			titles.add("联系人");
			titles.add("联系人手机");
			titles.add("地址");
			titles.add("EMail");
			dataMap.put("titles", titles);
			List<PageData> supplierlist = this.supplierService.excel(page);
			List<PageData> varList = new ArrayList();
			for (int i = 0; i < supplierlist.size(); i++) {
				PageData vpd = new PageData();
				vpd.put("var1", ((PageData)supplierlist.get(i)).getString("供应商ID"));
				vpd.put("var2", ((PageData)supplierlist.get(i)).getString("供应商名称"));
				vpd.put("var3", ((PageData)supplierlist.get(i)).getString("编码"));
				vpd.put("var4", ((PageData)supplierlist.get(i)).getString("简称"));
				vpd.put("var5", ((PageData)supplierlist.get(i)).getString("联系人"));
				vpd.put("var6", ((PageData)supplierlist.get(i)).getString("联系人手机"));
				vpd.put("var7", ((PageData)supplierlist.get(i)).getString("地址"));
				vpd.put("var8", ((PageData)supplierlist.get(i)).getString("EMail"));
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv, dataMap);
		}
		catch (Exception e) {
			this.logger.error(e.toString(), e);
		}
		return mv;
	}
	
}
