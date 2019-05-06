package com.hy.controller.order;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.PurchaseOrder;
import com.hy.service.order.PurchaseOrderSerivce;
import com.hy.service.product.SupplierService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping({"/purchaseOrder"})
public class PurchaseOrderController
        extends BaseController {
    @Resource(name = "purchaseOrderSerivce")
    private PurchaseOrderSerivce purchaseOrderSerivce;
    @Resource(name = "supplierService")
    private SupplierService supplierService;
    @Resource(name = "sysLogService")
    private SysLogService sysLogService;


    @RequestMapping({"/purexcel"})
    public ModelAndView purexcel() {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        try {
            String searchcriteria = pd.getString("searchcriteria");
            String keyword = pd.getString("keyword");
            if ((keyword != null) && (!"".equals(keyword))) {
                keyword = keyword.trim();
                pd.put("keyword", keyword);
                pd.put("searchcriteria", searchcriteria);
            }

            String lastLoginStart = pd.getString("lastLoginStart");
            String lastLoginEnd = pd.getString("lastLoginEnd");

            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                lastLoginStart = lastLoginStart + " 00:00:00";
                pd.put("lastLoginStart", lastLoginStart);
            }
            if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
                lastLoginEnd = lastLoginEnd + " 00:00:00";
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            Map<String, Object> dataMap = new HashMap();
            List<String> titles = new ArrayList();
            titles.add("供应商编码");
            titles.add("供应商名称");
            titles.add("商品编码");
            titles.add("商品条形码");
            titles.add("商品名称");
            titles.add("采购价格");
            titles.add("订购数量");
            titles.add("实际数量");
            titles.add("赠品数量");
            titles.add("入库日期");
            titles.add("订购金额");
            titles.add("入库金额");
            titles.add("备注");
            dataMap.put("titles", titles);
            List<PageData> purlist = this.purchaseOrderSerivce.purexcel(pd);
            List<PageData> varList = new ArrayList();
            String strName = null;
            PageData vpd = null;
            for (int i = 0; i < purlist.size(); i++) {
                vpd = new PageData();
                strName = ((PageData) purlist.get(i)).getString("商品名称");
                if (strName.indexOf("红牛") > -1 || strName.indexOf("战马") > -1|| strName.indexOf("昆悦") > -1 || strName.indexOf("武夷山") > -1 || strName.indexOf("东北冰原") > -1 || strName.indexOf("坚果帮") > -1 || strName.indexOf("武夷山") > -1
                ) {
                    vpd.put("var1", "80A1");
                    vpd.put("var2", "中石油昆仑好客有限公司（" + ((PageData) purlist.get(i)).getString("供应商名称") + "）");
                } else {
                    vpd.put("var1", ((PageData) purlist.get(i)).getString("供应商编码"));
                    vpd.put("var2", ((PageData) purlist.get(i)).getString("供应商名称"));
                }
                vpd.put("var3", ((PageData) purlist.get(i)).getString("商品编码"));
                vpd.put("var4", ((PageData) purlist.get(i)).getString("商品条形码"));
                vpd.put("var5", ((PageData) purlist.get(i)).getString("商品名称"));
                vpd.put("var6", ((PageData) purlist.get(i)).getString("采购价格"));
                vpd.put("var7", ((PageData) purlist.get(i)).getString("订购数量"));
                vpd.put("var8", ((PageData) purlist.get(i)).getString("实际数量"));
                vpd.put("var9", ((PageData) purlist.get(i)).getString("赠品数量"));
                vpd.put("var10", ((PageData) purlist.get(i)).getString("入库日期"));
                vpd.put("var11", ((PageData) purlist.get(i)).getString("订购金额"));
                vpd.put("var12", ((PageData) purlist.get(i)).getString("入库金额"));
                vpd.put("var13", ((PageData) purlist.get(i)).getString("备注"));
                //if(((PageData)purlist.get(i)).getString("supplier_name").indexOf("昆仑好客")==-1){
                //	  vpd.put("var10", "统采");
                // }else{
                //	  vpd.put("var10","急采");
                // }
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


    @RequestMapping({"/save"})
    public ModelAndView save()
            throws Exception {
        logBefore(this.logger, "新增PurchaseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        pd.put("group_num", StringUtil.getStringOfMillisecond(""));
        pd.put("order_num", "PO" + StringUtil.getStringOfMillisecond(""));
        pd.put("checked_state", "1");
        pd.put("is_printed", "2");
        pd.put("create_time", Tools.date2Str(new Date()));
        pd.put("order_date", Tools.date2Str(new Date()));
        this.purchaseOrderSerivce.save(pd);
        mv.addObject("msg", "success");
        this.sysLogService.saveLog("新增PurchaseOrder", "成功");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping({"/delete"})
    public void delete(PrintWriter out) {
        logBefore(this.logger, "删除PurchaseOrder");
        PageData pd = new PageData();
        try {
            pd = getPageData();
            this.purchaseOrderSerivce.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
    }

    @RequestMapping({"/edit"})
    public ModelAndView edit()
            throws Exception {
        logBefore(this.logger, "修改PurchaseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        this.purchaseOrderSerivce.edit(pd);
        mv.addObject("msg", "success");
        this.sysLogService.saveLog("修改PurchaseOrder", "成功");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping({"/purchaseOrdersList"})
    public ModelAndView list(Page page) {
        logBefore(this.logger, "列表PurchaseOrder");
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

            String lastLoginStart = pd.getString("lastLoginStart");
            String lastLoginEnd = pd.getString("lastLoginEnd");

            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                lastLoginStart = lastLoginStart + " 00:00:00";
                pd.put("lastLoginStart", lastLoginStart);
            }
            if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
                lastLoginEnd = lastLoginEnd + " 00:00:00";
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            String supplier_num = LoginUtil.getLoginUser().getUSERNAME();
            if (supplier_num != null && !"".equals(supplier_num)) {
                pd.put("supplier_num", supplier_num);
            }
            pd.put("checked_state", pd.getString("state"));
            pd.put("state", pd.getString("state"));
            pd.put("role_id", LoginUtil.getLoginUser().getROLE_ID());
            pd.put("USERNAME", LoginUtil.getLoginUser().getUSERNAME());
            page.setPd(pd);
            List<PageData> varList = this.purchaseOrderSerivce.listPdPagePurchaseOrder(page);
            mv.setViewName("procurement/purchaseOrder/purchaseOrder_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/goAdd"})
    public ModelAndView goAdd(Page page) {
        logBefore(this.logger, "去新增PurchaseOrder页面");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        try {
            List<PageData> supplerList = this.supplierService.listAll(page);
            mv.setViewName("procurement/purchaseOrder/purchaseorder_edit");
            mv.addObject("msg", "save");
            mv.addObject("pd", pd);
            mv.addObject("supplerList", supplerList);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/goEdit"})
    public ModelAndView goEdit(Page page) {
        logBefore(this.logger, "去修改PurchaseOrder页面");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        try {
            List<PageData> supplerList = this.supplierService.listAll(page);
            PurchaseOrder purchaseOrder = this.purchaseOrderSerivce.findById(pd);
            mv.setViewName("procurement/purchaseOrder/purchaseorder_edit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", purchaseOrder);
            mv.addObject("supplerList", supplerList);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/deleteAll"})
    @ResponseBody
    public Object deleteAll() {
        logBefore(this.logger, "批量删除PurchaseOrder");
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap();
        try {
            pd = getPageData();
            List<PageData> pdList = new ArrayList();
            String DATA_IDS = pd.getString("DATA_IDS");
            if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
                String[] ArrayDATA_IDS = DATA_IDS.split(",");
                this.purchaseOrderSerivce.deleteAll(ArrayDATA_IDS);
                pd.put("msg", "ok");
                this.sysLogService.saveLog("批量删除PurchaseOrder", "成功");
            } else {
                pd.put("msg", "no");
                this.sysLogService.saveLog("批量删除PurchaseOrder", "失败");
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

    @RequestMapping({"/goUploadExcel"})
    public ModelAndView goUploadExcel()
            throws Exception {
        ModelAndView mv = getModelAndView();
        mv.setViewName("procurement/purchaseOrder/uploadexcel");
        return mv;
    }

    @RequestMapping({"/downExcel"})
    public void downExcel(HttpServletResponse response)
            throws Exception {
        FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "SellingOrder.xls", "Tarot商储物流管理系统_Excel采购订单样例.xls");
    }

    @RequestMapping({"/readExcel"})
    public ModelAndView readExcel(@RequestParam(value = "excel", required = false) MultipartFile file)
            throws Exception {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();

        if ((file != null) && (!file.isEmpty())) {
            String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
            String fileName = FileUpload.fileUp(file, filePath, "userexcel");

            List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 2, 0, 0);

            List<PageData> listPdSave = new ArrayList();
            for (int i = 0; i < listPd.size(); i++) {
                pd = getPageData();
                pd.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
                pd.put("order_num", "SO_" + StringUtil.getStringOfMillisecond(""));
                pd.put("checked_state", "1");
                pd.put("is_printed", "2");
                pd.put("create_time", Tools.date2Str(new Date()));
                pd.put("order_date", Tools.date2Str(new Date()));

                listPdSave.add(pd);
            }

            mv.addObject("msg", "success");
        }
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping({"/excel"})
    public ModelAndView exportExcel(Page page)
            throws UnsupportedEncodingException {
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        page.setPd(pd);

        try {
            Map<String, Object> dataMap = new HashMap();
            List<String> titles = new ArrayList();

            titles.add("序");
            titles.add("商品条形码");
            titles.add("名称");
            titles.add("规格");
            titles.add("类别");

            if (pd.get("downloadType").equals("0")) {
                titles.add("库存数");
            }

            titles.add("建议订购数");
            titles.add("采购价");
            titles.add("订购数");
            titles.add("单位");
            dataMap.put("titles", titles);
            List<PageData> purchaseorderlist = this.purchaseOrderSerivce.excel(page);

            List<PageData> varList = new ArrayList();
            for (int i = 0; i < purchaseorderlist.size(); i++) {
                PageData vpd = new PageData();
                if (pd.get("downloadType").equals("0")) {
                    vpd.put("var1", Integer.valueOf(i + 1));
                    vpd.put("var2", ((PageData) purchaseorderlist.get(i)).getString("商品条形码"));
                    vpd.put("var3", ((PageData) purchaseorderlist.get(i)).getString("名称"));
                    vpd.put("var4", ((PageData) purchaseorderlist.get(i)).getString("规格"));
                    vpd.put("var5", ((PageData) purchaseorderlist.get(i)).getString("类别"));

                    vpd.put("var6", ((PageData) purchaseorderlist.get(i)).getString("库存数"));
                    vpd.put("var7", "");
                    vpd.put("var8", ((PageData) purchaseorderlist.get(i)).getString("采购价"));
                    vpd.put("var9", ((PageData) purchaseorderlist.get(i)).getString("订购数"));
                    vpd.put("var10", ((PageData) purchaseorderlist.get(i)).getString("单位"));
                } else {
                    vpd.put("var1", Integer.valueOf(i + 1));
                    vpd.put("var2", ((PageData) purchaseorderlist.get(i)).getString("商品条形码"));
                    vpd.put("var3", ((PageData) purchaseorderlist.get(i)).getString("名称"));
                    vpd.put("var4", ((PageData) purchaseorderlist.get(i)).getString("规格"));
                    vpd.put("var5", ((PageData) purchaseorderlist.get(i)).getString("类别"));
                    vpd.put("var6", "");
                    vpd.put("var7", ((PageData) purchaseorderlist.get(i)).getString("采购价"));
                    vpd.put("var8", ((PageData) purchaseorderlist.get(i)).getString("订购数"));
                    vpd.put("var9", ((PageData) purchaseorderlist.get(i)).getString("单位"));
                }

                varList.add(vpd);
            }
            dataMap.put("varList", varList);
            dataMap.put("purchaseorderlist", purchaseorderlist);
            dataMap.put("pd", pd);
            PurchaseOrderExcelView erv = new PurchaseOrderExcelView();
            mv = new ModelAndView(erv, dataMap);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 审核采购订单
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/examine"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String examine() throws Exception {
        logBefore(this.logger, "审核采购订单");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        String ids = pd.getString("DATA_IDS");
        String[] idss = ids.split(",");
        List list = Arrays.asList(idss);
        //更新销售订单状态
//    sellingOrderService.test();
        // purchaseOrderSerivce.updateBatch(list);
        //批量插入采购订单
        //  String result = sellingOrderService.audit(list);
        // mv.addObject("result", result);
        String result = purchaseOrderSerivce.audit(list);
        if ("true".equals(result)) {
            this.sysLogService.saveLog("审核采购订单", "成功");
        } else {
            try {
                purchaseOrderSerivce.updateBatch2(list);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.sysLogService.saveLog("审核采购订单", "失败");
        }
        return result;
    }
    /**
     * 查看采购单详情
     * @param page
     * @return
     */
    /**
     * 查看销售订单
     *
     * @return
     * @throws Exception
     */
    @RequestMapping({"/findorderitem"})
    public ModelAndView findorderitem() throws Exception {
        logBefore(this.logger, "查看销售订单详情");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();

        pd.put("order_num", pd.getString("order_num"));
        Object obj = purchaseOrderSerivce.findorder(pd);
        List list = purchaseOrderSerivce.findorderitem(pd);
        if (list.size() > 0) {
            mv.addObject("obj", obj);
            mv.addObject("msg", "success");
            mv.addObject("list", list);
            mv.addObject("pd", pd);
        }
        mv.setViewName("procurement/purchaseOrder/findorderitem");
        this.sysLogService.saveLog("查看销售订单详情", "成功");
        return mv;
    }

    @RequestMapping({"/print"})
    public ModelAndView print() throws Exception {
        logBefore(this.logger, "查看销售订单详情");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();

        pd.put("order_num", pd.getString("order_num"));
        Object obj = purchaseOrderSerivce.findorder(pd);
        List list = purchaseOrderSerivce.findorderitem(pd);
        if (list.size() > 0) {
            mv.addObject("obj", obj);
            mv.addObject("msg", "success");
            mv.addObject("list", list);
        }
        mv.setViewName("procurement/purchaseOrder/print");
        this.sysLogService.saveLog("查看销售订单详情", "成功");
        return mv;
    }

    /**
     * 查询采购订单汇总
     *
     * @return
     */
    @RequestMapping({"purchaseSum"})
    public ModelAndView purchaseSum(Page page) {
        logBefore(logger, "查询采购订单汇总");
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String keyword = pd.getString("keyword");
            String searchcriteria = pd.getString("searchcriteria");
            if ((keyword != null) && (!"".equals(keyword))) {
                keyword = keyword.trim();
                pd.put("keyword", keyword);
                pd.put("searchcriteria", searchcriteria);
            }
            String lastLoginStart = pd.getString("lastLoginStart");
            String lastLoginEnd = pd.getString("lastLoginEnd");

            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                lastLoginStart = lastLoginStart + " 00:00:00";
                pd.put("lastLoginStart", lastLoginStart);
            }
            if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
                lastLoginEnd = lastLoginEnd + " 00:00:00";
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            page.setPd(pd);
            List<PageData> listpd = purchaseOrderSerivce.purchaseorderhe(page);
            mv.addObject("listpd", listpd);
            mv.addObject("pd", pd);
            mv.setViewName("procurement/purchaseOrder/purchasesummary");
        } catch (Exception e) {
            // TODO: handle exception
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 采购汇总导出
     *
     * @return
     */
    @RequestMapping({"/purchaseSumexecl"})
    public ModelAndView purchaseSumexecl() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String keyword = pd.getString("keyword");
            String searchcriteria = pd.getString("searchcriteria");
            if (keyword != null && !"".equals(keyword)) {
                keyword = keyword.trim();
                pd.put("keyword", keyword);
                pd.put("searchcriteria", searchcriteria);
            }
            String lastLoginStart = pd.getString("lastLoginStart");
            String lastLoginEnd = pd.getString("lastLoginEnd");

            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                lastLoginStart = lastLoginStart + " 00:00:00";
                pd.put("lastLoginStart", lastLoginStart);
            }
            if ((lastLoginEnd != null) && (!"".equals(lastLoginEnd))) {
                lastLoginEnd = lastLoginEnd + " 00:00:00";
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            Map<String, Object> map = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("供应商名称");
            titles.add("供应商编码");
            titles.add("商品名称");
            titles.add("商品条码");
            titles.add("商品编码");
            titles.add("商品单位");
            titles.add("订货总数");
            titles.add("实际入库数");
            titles.add("赠品数");
            titles.add("采购价");
			/*titles.add("采购总价");
			titles.add("零售总价");*/
            map.put("titles", titles);
            List<PageData> listPd = purchaseOrderSerivce.purchaseexcel(pd);
            List<PageData> varList = new ArrayList<>();
            for (int i = 0; i < listPd.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", listPd.get(i).getString("supplier_name"));
                vpd.put("var2", listPd.get(i).getString("supplier_num"));
                vpd.put("var3", listPd.get(i).getString("product_name"));
                vpd.put("var4", listPd.get(i).getString("bar_code"));
                vpd.put("var5", listPd.get(i).getString("product_num"));
                vpd.put("var6", listPd.get(i).getString("unit_name"));
                vpd.put("var7", listPd.get(i).getString("zs"));
                vpd.put("var8", listPd.get(i).getString("sjs"));
                vpd.put("var9", listPd.get(i).getString("gift_quantity"));
                vpd.put("var10", listPd.get(i).getString("caigoujia"));
		         /* double a=Double.parseDouble(listPd.get(i).getString("caigoujia"));//采购价
		          double b=Double.parseDouble(listPd.get(i).getString("zs")); //采购数
		          double zp=Double.parseDouble(listPd.get(i).getString("gift_quantity")); //赠品数
		          double c=Double.parseDouble(listPd.get(i).getString("shuchujia")); //售出价
		          double zongshu=b+zp; //采购数加赠品数
		          double caigousum=a*zongshu; //采购总价
		          double lingshousum=zongshu*c; //售出总价
		          BigDecimal bg = new BigDecimal(caigousum).setScale(2, RoundingMode.UP); //采购总价
		          BigDecimal pg = new BigDecimal(lingshousum).setScale(2, RoundingMode.UP); //售出总价
		          vpd.put("var10", bg);
		          vpd.put("var11", pg);*/
                varList.add(vpd);
            }
            map.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, map);
        } catch (Exception e) {
            // TODO: handle exception
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 查询采购订单汇总
     *
     * @return
     */
    @RequestMapping({"regionpurchaseSum"})
    public ModelAndView regionpurchaseSum(Page page) {
        logBefore(logger, "查询采购订单汇总");
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String keyword = pd.getString("keyword");
            String searchcriteria = pd.getString("searchcriteria");
            if ((keyword != null) && (!"".equals(keyword))) {
                keyword = keyword.trim();
                pd.put("keyword", keyword);
                pd.put("searchcriteria", searchcriteria);
            }
            pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
            pd.put("area_num", LoginUtil.getLoginUser().getUSERNAME());
            page.setPd(pd);
            List<PageData> listpd = purchaseOrderSerivce.regionpurchaseSum(page);
            mv.addObject("listpd", listpd);
            mv.addObject("pd", pd);
            mv.setViewName("procurement/purchaseOrder/regionpurchasesummary");
        } catch (Exception e) {
            // TODO: handle exception
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    /**
     * 地区采购导出
     *
     * @return
     */
    @RequestMapping({"/regionpurchaseExport"})
    public ModelAndView regionpurchaseExport() {
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        try {
            pd = this.getPageData();
            String keyword = pd.getString("keyword");
            String searchcriteria = pd.getString("searchcriteria");
            if (keyword != null && !"".equals(keyword)) {
                keyword = keyword.trim();
                pd.put("keyword", keyword);
                pd.put("searchcriteria", searchcriteria);
            }
            pd.put("ROLE_ID", LoginUtil.getLoginUser().getROLE_ID());
            pd.put("area_num", LoginUtil.getLoginUser().getUSERNAME());
            Map<String, Object> map = new HashMap<>();
            List<String> titles = new ArrayList<>();
            titles.add("供应商名称");
            titles.add("供应商编码");
            titles.add("商品名称");
            titles.add("商品条码");
            titles.add("商品编码");
            titles.add("采购数");
            titles.add("实际入库数");
            titles.add("赠品数");
            /*titles.add("采购价");*/
            map.put("titles", titles);
            List<PageData> listPd = purchaseOrderSerivce.regionpurchaseExport(pd);
            List<PageData> varList = new ArrayList<>();
            for (int i = 0; i < listPd.size(); i++) {
                PageData vpd = new PageData();
                vpd.put("var1", listPd.get(i).getString("supplier_name"));
                vpd.put("var2", listPd.get(i).getString("supplier_num"));
                vpd.put("var3", listPd.get(i).getString("product_name"));
                vpd.put("var4", listPd.get(i).getString("bar_code"));
                vpd.put("var5", listPd.get(i).getString("product_num"));
                vpd.put("var6", listPd.get(i).getString("quantity"));
                vpd.put("var7", listPd.get(i).getString("final_quantity"));
                vpd.put("var8", listPd.get(i).getString("gift_quantity"));
                /*vpd.put("var9", listPd.get(i).getString("purchase_price"));*/
                varList.add(vpd);
            }
            map.put("varList", varList);
            ObjectExcelView erv = new ObjectExcelView();
            mv = new ModelAndView(erv, map);
        } catch (Exception e) {
            // TODO: handle exception
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

}


