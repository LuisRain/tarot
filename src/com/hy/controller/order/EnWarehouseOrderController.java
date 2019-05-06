package com.hy.controller.order;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.ENOrderItem;
import com.hy.entity.order.OrderGroup;
import com.hy.entity.product.Product;
import com.hy.entity.product.Supplier;
import com.hy.entity.system.User;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.inventory.WarehouseService;
import com.hy.service.order.ENOrderItemService;
import com.hy.service.order.ENOrderService;
import com.hy.service.order.EnWarehouseOrderService;
import com.hy.service.order.OrderGroupService;
import com.hy.service.product.MerchantService;
import com.hy.service.product.ProductService;
import com.hy.service.product.ProductTypeService;
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
import java.util.*;

@Controller
@RequestMapping({"/enwarehouseorder"})
public class EnWarehouseOrderController extends BaseController {
    @Resource(name = "enwarehouseorderService")
    private EnWarehouseOrderService enwarehouseorderService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "orderGroupService")
    private OrderGroupService orderGroupService;
    @Resource(name = "productinventoryService")
    private ProductinventoryService productinventoryService;
    @Resource(name = "eNOrderItemService")
    private ENOrderItemService eNOrderItemService;
    @Resource(name = "productTypeService")
    private ProductTypeService productTypeService;
    @Resource(name = "sysLogService")
    private SysLogService sysLogService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private SupplierService supplierService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private ENOrderService enOrderService;

    /** 分仓订单 ****//*
     * @RequestMapping({"saveEnOrder"}) public ModelAndView saveEnOrder(Page page) {
     * PageData pd = new PageData(); pd = getPageData(); ModelAndView mv =
     * getModelAndView(); try { PageData productInvertory = new PageData();
     * productInvertory.put("productId", pd.get("productId"));
     * productInvertory.put("newQuantity", pd.get("newQuantity"));
     * productInvertory.put("warehouseId", pd.get("warehouseId"));
     *
     * if
     * (this.productinventoryService.updateProductinventoryReduce(productInvertory))
     * { saveTempOrder(page); } } catch (Exception e) { e.printStackTrace(); }
     *
     * mv.setViewName(
     * "inventorymanagement/enwarehouseorder/enwarehouseorder_listexcel");
     *
     * return mv; }
     */

    /**
     * 分仓导入
     ***/
    @RequestMapping({"/goEnUploadExcel"})
    public ModelAndView goEnUploadExcel(String type) throws Exception {
        ModelAndView mv = getModelAndView();
        mv.addObject("type", type);
        mv.setViewName("inventorymanagement/enwarehouseorder/importExcelPage");
        return mv;
    }

    @RequestMapping({"goSaveTemp"})
    public ModelAndView goSaveTemp(String type) {
        ModelAndView mv = getModelAndView();

        PageData pd = new PageData();
        pd = getPageData();
        pd.put("parent_id", Integer.valueOf(0));
        try {
            PageData warhouse = new PageData();
            List<PageData> wlist = this.warehouseService.listAll(warhouse);

            List<PageData> productType = this.productTypeService.findByParentId(pd);

            mv.addObject("productType", productType);
            mv.addObject("wlist", wlist);
            if (type.equals("2")) {
                mv.setViewName("inventorymanagement/enwarehouseorder/entempwarehouseorder_edit");
            } else {
                mv.setViewName("inventorymanagement/entempwarehouseorder/entempwarehouseorder_edit");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping({"enTemplistPage"})
    public ModelAndView enTemplistPage(Page page, String type) {
        logBefore(this.logger, "分页查询临时出库单");
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd = getPageData();
            String searchcriteria = pd.getString("searchcriteria");
            String keyword = pd.getString("keyword");
            if ((keyword != null) && (!"".equals(keyword))) {
                keyword = keyword.trim();
                pd.put("keyword", keyword);
                pd.put("searchcriteria", searchcriteria);
            }
            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            } else {
                pd.remove("order_data");
            }

            String reason = pd.getString("reason");
            if ((reason != null) && (!"".equals(reason))) {
                pd.put("reason", reason);
            } else {
                pd.put("reason", "");
            }
            page.setPd(pd);

            List<PageData> varList = this.enwarehouseorderService.enTemplistPage(page);
            if (type.equals("2")) {
                mv.setViewName("inventorymanagement/enwarehouseorder/entempwarehouseorder_list");
            } else {
                mv.setViewName("inventorymanagement/entempwarehouseorder/entempwarehouseorder_list");
            }
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping({"/save"})
    public ModelAndView save() throws Exception {
        logBefore(this.logger, "新增EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        pd.put("ENWAREHOUSEORDER_ID", get32UUID());
        pd.put("GROUP_NUM", "");
        pd.put("ORDER_NUM", "");
        pd.put("SUPPLIER_ID", "");
        pd.put("IS_IVT_ORDER_PRINT", "");
        pd.put("USER_ID", "");
        pd.put("CREATE_TIME", Tools.date2Str(new Date()));
        this.enwarehouseorderService.save(pd);
        this.sysLogService.saveLog("新增EnWarehouseOrder", "成功");
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping({"/delete"})
    public void delete(PrintWriter out) {
        logBefore(this.logger, "删除EnWarehouseOrder");
        PageData pd = new PageData();
        try {
            pd = getPageData();
            this.enwarehouseorderService.delete(pd);
            out.write("success");
            out.close();
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
    }

    @RequestMapping({"/edit"})
    public ModelAndView edit() throws Exception {
        logBefore(this.logger, "修改EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        this.enwarehouseorderService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    @RequestMapping({"/listOfGift"})
    public ModelAndView listOfGift(Page page) {
        logBefore(this.logger, "列表EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        try {
            pd = getPageData();
            page.setPd(pd);
            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            if (!StringUtil.isEmpty(pd.getString("order_type"))) {
                pd.put("order_type", pd.getString("order_type"));
            }
            if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
                pd.put("ivt_state", pd.getString("ivt_state"));
            }
            List<PageData> varList = this.enwarehouseorderService.listOfGift(page);
            mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorderOfGift_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/list"})
    public ModelAndView list(Page page) {
        logBefore(this.logger, "列表EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        try {
            pd = getPageData();

            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            if (!StringUtil.isEmpty(pd.getString("order_type"))) {
                pd.put("order_type", pd.getString("order_type"));
            }
            if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
                pd.put("ivt_state", pd.getString("ivt_state"));
            }
            pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
            page.setPd(pd);
            List<PageData> varList = this.enwarehouseorderService.list(page);
            mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorder_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/fencanglist"})
    public ModelAndView fencanglist(Page page) {
        logBefore(this.logger, "列表EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        try {
            pd = getPageData();

            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            if (!StringUtil.isEmpty(pd.getString("order_type"))) {
                pd.put("order_type", pd.getString("order_type"));
            }
            if (!StringUtil.isEmpty(pd.getString("ivt_state"))) {
                pd.put("ivt_state", pd.getString("ivt_state"));
            }
            pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
            page.setPd(pd);
            List<PageData> varList = this.enwarehouseorderService.list(page);
            mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorder_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/listNew"})
    public ModelAndView listNew(Page page) {
        logBefore(this.logger, "列表EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        try {
            pd = getPageData();
            page.setPd(pd);
            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            if (!StringUtil.isEmpty(pd.getString("order_date"))) {
                pd.put("order_type", pd.getString("order_date"));
            }
            List<PageData> varList = this.enwarehouseorderService.list(page);
            mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorder_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"goInferio"})
    public ModelAndView goInferio() {
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd.put("parent_id", Integer.valueOf(0));

            List<PageData> productType = this.productTypeService.findByParentId(pd);
            mv.addObject("productType", productType);
            mv.setViewName("inventorymanagement/enwarehouseorder/inferiorEnwarehouseorder_edit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping({"/inferiorList"})
    public ModelAndView inferiorList(Page page, String type) {
        logBefore(this.logger, "列表EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        List<PageData> varList = null;
        try {
            pd = getPageData();
            page.setPd(pd);
            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            if (type.equals("1")) {
                varList = this.enwarehouseorderService.inferiDatalistPage(page);
                mv.setViewName("inventorymanagement/enwarehouseorder/inferiorEnwarehouseorder_list");
            } else {
                pd.put("pd.ivt_state", Integer.valueOf(1));
                varList = this.enwarehouseorderService.inferiDatalistPage(page);
                mv.setViewName("inventorymanagement/enwarehouseorder/inferiorEnwarehouseorder_list2");
            }
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/goAdd"})
    public ModelAndView goAdd() {
        logBefore(this.logger, "去新增EnWarehouseOrder页面");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        try {
            mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorder_edit");
            mv.addObject("msg", "save");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"/goEdit"})
    public ModelAndView goEdit() {
        logBefore(this.logger, "去修改EnWarehouseOrder页面");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        try {
            pd = this.enwarehouseorderService.findById(pd);
            mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorder_edit");
            mv.addObject("msg", "edit");
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping(value = {"/reviewedAll"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String reviewedAll() {
        logBefore(this.logger, "批量审核EnWarehouseOrder");
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap();
        String result = "false";
        try {
            pd = getPageData();
            String DATA_IDS = pd.getString("DATA_IDS");
            String str1;
            if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
                String[] ArrayDATA_IDS = DATA_IDS.split(",");
                String st = this.enwarehouseorderService.reviewedAll(ArrayDATA_IDS);
                if (st.equals("true")) {
                    this.sysLogService.saveLog("批量审核EnWarehouseOrder", "成功");
                    return "true";
                }
            } else {
                this.sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
                str1 = result;
                return str1;
            }
        } catch (Exception e) {
            result = e.toString();
            this.logger.error(e.toString(), e);
        }
        return result;
    }

    /**
     * 赠品审核
     **/
    @RequestMapping(value = {"/giftreviewedAll"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String giftReviewedAll() {
        logBefore(this.logger, "批量赠品审核EnWarehouseOrder");
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap();
        String result = "false";
        try {
            pd = getPageData();
            String DATA_IDS = pd.getString("DATA_IDS");
            String str1;
            if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
                String[] ArrayDATA_IDS = DATA_IDS.split(",");
                String st = this.enwarehouseorderService.giftReviewedAll(ArrayDATA_IDS);
                if (st.equals("true")) {
                    this.sysLogService.saveLog("批量审核EnWarehouseOrder", "成功");
                    return "true";
                }
            } else {
                this.sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
                str1 = result;
                return str1;
            }
        } catch (Exception e) {
            result = e.toString();
            this.logger.error(e.toString(), e);
        }
        return result;
    }

    @RequestMapping({"addTempOrder"})
    public ModelAndView addTempOrder(Page page) {
        PageData pd = new PageData();
        pd = getPageData();
        ModelAndView mv = getModelAndView();
        try {
            PageData productInvertory = new PageData();
            productInvertory.put("productId", pd.get("productId"));
            productInvertory.put("newQuantity", pd.get("newQuantity"));
            productInvertory.put("warehouseId", pd.get("warehouseId"));

            if (this.productinventoryService.updateProductinventoryAdd(productInvertory)) {
                saveTempOrder(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.setViewName("redirect:enTemplistPage.do?type=" + pd.get("type"));
        return mv;
    }

    private void saveTempOrder(Page page) throws Exception {
        PageData pd = new PageData();
        pd = getPageData();
        String orderGroupNum = "GP_" + StringUtil.getStringOfMillisecond("");
        OrderGroup og = new OrderGroup();
        PageData orderItem = new PageData();
        String orderNum = OrderNum.getEnTempOrderNum();
        orderItem.put("order_num", orderNum);
        orderItem.put("product_id", pd.get("productId"));
        orderItem.put("group_num", orderGroupNum);
        orderItem.put("final_quantity", pd.get("newQuantity"));
        orderItem.put("quantity", pd.get("newQuantity"));
        orderItem.put("reason", pd.get("reason"));
        orderItem.put("state", Integer.valueOf(1));
        orderItem.put("comment", pd.get("comment"));
        orderItem.put("is_ivt_BK", pd.get("warehouseId"));

        this.eNOrderItemService.save(orderItem);

        PageData enTempOrderParam = new PageData();
        enTempOrderParam.put("order_num", orderNum);
        enTempOrderParam.put("is_order_print", Integer.valueOf(1));
        enTempOrderParam.put("ivt_state", Integer.valueOf(2));
        if (pd.get("type").equals("2")) {
            enTempOrderParam.put("is_temporary", Integer.valueOf(2));
            enTempOrderParam.put("order_type", Integer.valueOf(4));
        } else {
            enTempOrderParam.put("is_temporary", Integer.valueOf(1));
        }
        enTempOrderParam.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
        enTempOrderParam.put("state", Integer.valueOf(1));
        enTempOrderParam.put("group_num", orderGroupNum);
        this.enwarehouseorderService.save(enTempOrderParam);
        og.setOrderGroupNum(orderGroupNum);
        og.setUser(LoginUtil.getLoginUser());
        og.setGroupType(7);
        this.orderGroupService.saveOrderGroup(og);
    }

    @RequestMapping({"/goUploadExcel"})
    public ModelAndView goUploadExcel(String type) throws Exception {
        ModelAndView mv = getModelAndView();

        mv.addObject("type", type);
        mv.setViewName("inventorymanagement/enwarehouseorder/uploadexcel");

        return mv;
    }

    /*** 次品导入 ***/
    @RequestMapping(value = {"/readExcel"}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String readExcel(@RequestParam(value = "excel", required = false) MultipartFile file, String type)
            throws Exception {
        String result = "false";
        if ((file != null) && (!file.isEmpty())) {
            String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
            String fileName = FileUpload.fileUp(file, filePath, "productexcel");
            this.enwarehouseorderService.readExcel(filePath, fileName);
            result = "true";
        }

        return result;
    }

    @RequestMapping({"/openExcel"})
    public ModelAndView openExcel(Page page) throws Exception {
        logBefore(this.logger, "新增EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        List<PageData> list = this.productinventoryService.listTemp(page);
        mv.setViewName("inventorymanagement/enwarehouseorder/showexcel");
        mv.addObject("pd", pd);
        mv.addObject("productlist", list);
        return mv;
    }

    @RequestMapping(value = {"/addTheInventory"}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addTheInventory() throws Exception {
        logBefore(this.logger, "excel批量添加库存");
        String result = "操作失败请联系管理员";
        Page page = new Page();
        PageData pageData = new PageData();
        String keyword = "1";
        pageData.put("keyword", keyword);
        page.setPd(pageData);
        String type = this.productinventoryService.addTheInventory(page);
        if (type.equals("1")) {
            result = "入库已完成!";
        } else if (type.equals("0")) {
            result = "出库已完成!";
        }

        return result;
    }

    @RequestMapping({"/excel"})
    public ModelAndView exportExcel(String id) throws Exception {
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("list", this.enwarehouseorderService.getExcel(id));
        dataMap.put("统计", this.enwarehouseorderService.getExcelCount(id));
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        pd = getPageData();
        EnWarehouseOrderExcelView erv = new EnWarehouseOrderExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    @RequestMapping({"goEnwareorderProductEdit"})
    public ModelAndView goEnwareorderProductEdit(Page page, String orderId, String type) {
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd = getPageData();
            pd.put("orderNum", orderId);
            page.setPd(pd);
            pd.put("parent_id", Integer.valueOf(0));

            List<PageData> productType = this.productTypeService.findByParentId(pd);
            mv.addObject("productType", productType);
            pd = this.enwarehouseorderService.getEnwarouseById(pd);
            List<PageData> pageDate = this.eNOrderItemService.getOrderItemlistPageProductById(page);
            if ("1".equals(type)) {
                mv.setViewName("inventorymanagement/enwarehouseorder/enwarehouseorder_product_edit");
            } else {
                mv.setViewName("inventorymanagement/salesReturn/saleReturn_info");
            }

            mv.addObject("orderItemList", pageDate);
            mv.addObject("enwarhouse", pd);
            //System.out.print(type);
            mv.addObject("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping({"print"})
    public ModelAndView print(Page page, String orderId, String type) {
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd = getPageData();
            pd.put("orderNum", orderId);
            page.setPd(pd);
            pd.put("parent_id", Integer.valueOf(0));
            List<PageData> productType = this.productTypeService.findByParentId(pd);
            mv.addObject("productType", productType);
            pd = this.enwarehouseorderService.getEnwarouseById(pd);
            //创建时间
            String createTime = pd.getString("create_time");
            List<PageData> pageDate = this.eNOrderItemService.getOrderItemlistPageProductById(page);
            mv.addObject("create_time", createTime);
            
            //根据创建时间获取批次号
            String groupNum = pd.getString("group_num");
            groupNum = groupNum.substring(groupNum.length()-4);
            //月份 去前面的0
            String month = Integer.parseInt(groupNum.substring(0, 2)) + "月(";
            String batchNum = month + groupNum.substring(3) + ")";
            mv.addObject("batchNum", batchNum);
            
            if ("1".equals(type)) {
                mv.setViewName("inventorymanagement/enwarehouseorder/print");
            } else {
                mv.setViewName("inventorymanagement/salesReturn/saleReturn_info");
            }
            mv.addObject("orderItemList", pageDate);
            mv.addObject("enwarhouse", pd);
            mv.addObject("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping({"goEnwareorderPurchaseProductEdit"})
    public ModelAndView goEnwareorderPurchaseProductEdit(Page page, String orderId, String type) {
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd = getPageData();
            pd.put("orderNum", orderId);
            page.setPd(pd);
            pd.put("parent_id", Integer.valueOf(0));

            List<PageData> productType = this.productTypeService.findByParentId(pd);
            mv.addObject("productType", productType);
            pd = this.enwarehouseorderService.getEnwarouseById(pd);
            List<PageData> pageDate = this.eNOrderItemService.getOrderItemlistPageProductById(page);
            List<PageData> warhouseList = this.warehouseService.listAll(pd);
            mv.addObject("wlist", warhouseList);
            mv.setViewName("inventorymanagement/salesReturn/purchaseReturn_info");
            mv.addObject("orderItemList", pageDate);
            mv.addObject("enwarhouse", pd);
            //System.out.print(type);
            mv.addObject("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = {"saveEnOrderItem"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String saveEnOrderItem(Page page, String bkType) {
        String result = "false";
        PageData pd = getPageData();
        try {
            pd.put("is_ivt_BK", bkType);
            if (this.eNOrderItemService.save(pd) > 0) {
                double quantity = Double.parseDouble(pd.get("quantity").toString());
                double price = Double.parseDouble(pd.get("purchase_price").toString());
                double sku_volume = Double.parseDouble(pd.get("sku_volume").toString());
                double sku_weight = Double.parseDouble(pd.get("sku_weight").toString());
                double sku_volumeLine = NumberUtil.mul(quantity, sku_volume);
                double sku_weightLine = NumberUtil.mul(quantity, sku_weight);
                double finalAmount = NumberUtil.mul(quantity, price);
                PageData enwarhouseOrder = new PageData();
                enwarhouseOrder.put("id", pd.get("orderId"));
                enwarhouseOrder = this.enwarehouseorderService.findById(enwarhouseOrder);
                double oldFinalAmount = Double.valueOf(enwarhouseOrder.get("final_amount").toString()).doubleValue();
                double oldVolume = Double.valueOf(enwarhouseOrder.get("total_svolume").toString()).doubleValue();
                double oldWeight = Double.valueOf(enwarhouseOrder.get("total_weight").toString()).doubleValue();
                PageData enwarhouseOrder1 = new PageData();
                enwarhouseOrder1.put("id", pd.get("orderId"));
                enwarhouseOrder1.put("final_amount", Double.valueOf(NumberUtil.add(finalAmount, oldFinalAmount)));
                enwarhouseOrder1.put("total_svolume", Double.valueOf(NumberUtil.add(sku_volumeLine, oldVolume)));
                enwarhouseOrder1.put("total_weight", Double.valueOf(NumberUtil.add(sku_weightLine, oldWeight)));
                if (this.enwarehouseorderService.updateFinalAmount(enwarhouseOrder1) > 0) {
                    PageData product = this.productService
                            .getproduct(Long.valueOf(Long.parseLong(pd.get("product_id").toString())));
                    return product.getString("product_name") + "," + product.getString("bar_code") + ","
                            + product.getString("specification") + "," + product.getString("product_quantity") + ","
                            + product.getString("product_price") + "," + pd.get("quantity");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "false";
        }

        return result;
    }

    @RequestMapping(value = {"savePurchaseOrderItem"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String savePurchaseOrderItem(Page page, String bkType) {
        String result = "false";
        PageData pd = getPageData();
        try {
            pd.put("is_ivt_BK", bkType);
            if (this.eNOrderItemService.save(pd) > 0) {
                double quantity = Double.parseDouble(pd.get("quantity").toString());
                double price = Double.parseDouble(pd.get("purchase_price").toString());
                double sku_volume = Double.parseDouble(pd.get("sku_volume").toString());

                double sku_weight = Double.parseDouble(pd.get("sku_weight").toString());

                double sku_volumeLine = NumberUtil.mul(quantity, sku_volume);
                double sku_weightLine = NumberUtil.mul(quantity, sku_weight);

                double finalAmount = NumberUtil.mul(quantity, price);
                PageData enwarhouseOrder = new PageData();
                enwarhouseOrder.put("id", pd.get("orderId"));
                enwarhouseOrder = this.enwarehouseorderService.findById(enwarhouseOrder);
                double oldFinalAmount = Double.valueOf(enwarhouseOrder.get("final_amount").toString()).doubleValue();
                double oldVolume = Double.valueOf(enwarhouseOrder.get("total_svolume").toString()).doubleValue();
                double oldWeight = Double.valueOf(enwarhouseOrder.get("total_weight").toString()).doubleValue();
                PageData enwarhouseOrder1 = new PageData();
                enwarhouseOrder1.put("id", pd.get("orderId"));
                enwarhouseOrder1.put("final_amount", Double.valueOf(NumberUtil.add(finalAmount, oldFinalAmount)));
                enwarhouseOrder1.put("total_svolume", Double.valueOf(NumberUtil.add(sku_volumeLine, oldVolume)));
                enwarhouseOrder1.put("total_weight", Double.valueOf(NumberUtil.add(sku_weightLine, oldWeight)));
                if (this.enwarehouseorderService.updateFinalAmount(enwarhouseOrder1) > 0) {
                    PageData product = this.productService
                            .getproduct(Long.valueOf(Long.parseLong(pd.get("product_id").toString())));
                    return product.getString("product_name") + "," + product.getString("bar_code") + ","
                            + product.getString("specification") + "," + product.getString("product_quantity") + ","
                            + product.getString("product_price") + "," + pd.get("quantity");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "false";
        }

        return result;
    }

    @RequestMapping(value = {"saveInferior"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String saveInferior(Integer supplierId, String comment) {
        try {
            PageData inferiorEnwarhouse = new PageData();
            inferiorEnwarhouse.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
            inferiorEnwarhouse.put("order_num", "RK_" + StringUtil.getStringOfMillisecond(""));
            inferiorEnwarhouse.put("supplier_id", supplierId);

            inferiorEnwarhouse.put("comment", comment);
            inferiorEnwarhouse.put("final_amount", "0.00");
            inferiorEnwarhouse.put("total_svolume", "0.00");
            inferiorEnwarhouse.put("total_weight", "0.00");
            inferiorEnwarhouse.put("is_ivt_order_print", Integer.valueOf(1));
            inferiorEnwarhouse.put("is_temporary", Integer.valueOf(2));
            inferiorEnwarhouse.put("ivt_state", Integer.valueOf(1));
            inferiorEnwarhouse.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
            inferiorEnwarhouse.put("order_type", Integer.valueOf(4));
            this.enwarehouseorderService.save(inferiorEnwarhouse);
            return inferiorEnwarhouse.get("order_num").toString() + "," + inferiorEnwarhouse.getString("group_num")
                    + "," + inferiorEnwarhouse.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = {"Generatethestorereceipt"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String Generatethestorereceipt(Integer supplierId, String comment, String ordernum, String orderdate) {
        try {
            if ((ordernum != null) && (!ordernum.equals(""))) {
                PageData pd = new PageData();
                pd.put("ordernum", ordernum);
                pd = this.enwarehouseorderService.getEnwarouseByorderNum(pd);
                pd.put("merchant_id", supplierId);
                pd.put("comment", comment);
                this.enwarehouseorderService.edit(pd);
                this.enwarehouseorderService.deleteEnOrderItemByOrderNum(pd);
                return pd.get("order_num").toString() + "," + pd.getString("group_num") + "," + pd.getString("id");
            }
            PageData inferiorEnwarhouse = new PageData();
            inferiorEnwarhouse.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
            inferiorEnwarhouse.put("order_num", "RK_" + StringUtil.getStringOfMillisecond(""));
            inferiorEnwarhouse.put("supplier_id", supplierId);

            inferiorEnwarhouse.put("comment", comment);
            inferiorEnwarhouse.put("final_amount", "0.00");
            inferiorEnwarhouse.put("total_svolume", "0.00");
            inferiorEnwarhouse.put("total_weight", "0.00");
            inferiorEnwarhouse.put("is_ivt_order_print", Integer.valueOf(1));
            inferiorEnwarhouse.put("is_temporary", Integer.valueOf(2));
            inferiorEnwarhouse.put("ivt_state", Integer.valueOf(1));
            inferiorEnwarhouse.put("order_date", orderdate);
            inferiorEnwarhouse.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
            inferiorEnwarhouse.put("ck_id", Long.valueOf(LoginUtil.getLoginUser().getCkId()));
            inferiorEnwarhouse.put("order_type", Integer.valueOf(2));
            this.enwarehouseorderService.save(inferiorEnwarhouse);
            return inferiorEnwarhouse.get("order_num").toString() + "," + inferiorEnwarhouse.getString("group_num")
                    + "," + inferiorEnwarhouse.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = {"updateEnOrderProduct"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String updateEnOrderProduct(Page page) {
        String result = "true";
        try {
            PageData pd = getPageData();

            String ids = pd.getString("ids");
            String numbers = pd.getString("numbers");
            String productDate = pd.getString("product_time");
            String productgroupnum = pd.getString("productgroupnum");
            String gift_quantity = pd.getString("gift_quantity");
            if (!"".equals(ids)) {
                String orderId = pd.get("orderId").toString();
                String productDates = pd.getString("product_time");
                PageData enwarhouseOrder = new PageData();
                enwarhouseOrder.put("id", orderId);

                enwarhouseOrder = this.enwarehouseorderService.findById(enwarhouseOrder);
                Double finalAmount = Double.valueOf(enwarhouseOrder.get("final_amount").toString());

                double total_svolume = Double.valueOf(enwarhouseOrder.get("total_svolume").toString()).doubleValue();

                double total_weight = Double.valueOf(enwarhouseOrder.get("total_weight").toString()).doubleValue();
                String[] idsStr = ids.split(",");
                String[] numbersStr = numbers.split(",");
                String[] product_time = null;
                String[] productGroup = productgroupnum.split(",");
                String[] giftquantity = gift_quantity.split(",");
                if (!productDate.equals("")) {
                    product_time = productDate.split(",");
                }
                for (int i = 0; i < idsStr.length; i++) {
                    PageData orderItem = new PageData();
                    orderItem.put("id", idsStr[i]);
                    orderItem = this.eNOrderItemService.findItemById(orderItem);
                    long productId = Long.parseLong(orderItem.get("product_id").toString());
                    PageData product = this.productService.findProductTZById(productId);
                    double oldQuantity = Double.valueOf(orderItem.get("final_quantity").toString()).doubleValue();
                    double quantity = Double.valueOf(orderItem.get("quantity").toString()).doubleValue();
                    double newQuantity = Double.valueOf(numbersStr[i]).doubleValue();
                    Double purchasePrice = Double.valueOf(orderItem.get("purchase_price").toString());

                    double sku_weight = Double.parseDouble(product.get("sku_weight").toString());
                    double sku_volume = Double.parseDouble(product.get("sku_volume").toString());
                    if (newQuantity <= quantity) {
                        if (oldQuantity > newQuantity) {
                            double subQuantity = NumberUtil.sub(oldQuantity, newQuantity);
                            Double newTotalPrice = Double.valueOf(NumberUtil.mul(subQuantity, purchasePrice.doubleValue()));
                            finalAmount = Double
                                    .valueOf(NumberUtil.sub(finalAmount.doubleValue(), newTotalPrice.doubleValue()));

                            Double newTotalWeight = Double.valueOf(NumberUtil.mul(sku_weight, subQuantity));
                            total_weight = NumberUtil.sub(total_weight, newTotalWeight.doubleValue());

                            Double newTotalVolume = Double.valueOf(NumberUtil.mul(sku_volume, subQuantity));
                            total_svolume = NumberUtil.sub(total_svolume, newTotalVolume.doubleValue());
                        } else {
                            double addQuantity = NumberUtil.sub(newQuantity, oldQuantity);
                            Double newTotalPrice = Double.valueOf(NumberUtil.mul(addQuantity, purchasePrice.doubleValue()));
                            finalAmount = Double
                                    .valueOf(NumberUtil.add(finalAmount.doubleValue(), newTotalPrice.doubleValue()));

                            Double newTotalWeight = Double.valueOf(NumberUtil.mul(sku_weight, addQuantity));
                            total_weight = NumberUtil.add(total_weight, newTotalWeight.doubleValue());

                            Double newTotalVolume = Double.valueOf(NumberUtil.mul(sku_volume, addQuantity));
                            total_svolume = NumberUtil.add(total_svolume, newTotalVolume.doubleValue());
                        }
                        enwarhouseOrder.put("final_amount", finalAmount);
                        enwarhouseOrder.put("total_svolume", Double.valueOf(total_svolume));
                        enwarhouseOrder.put("total_weight", Double.valueOf(total_weight));
                        if (this.enwarehouseorderService.edit(enwarhouseOrder) > 0) {
                            orderItem.remove("final_quantity");
                            if (newQuantity <= quantity) {
                                orderItem.put("final_quantity", numbersStr[i]);
                            } else {
                                orderItem.put("final_quantity", 0);
                            }
                            orderItem.put("gift_quantity", giftquantity[i]);
                            if (product_time.length > i) {
                                orderItem.put("product_time", product_time[i]);
                            }
                            if (productGroup.length > i) {
                                orderItem.put("productGroup", productGroup[i]);
                            }

                            if (this.eNOrderItemService.editQuantity(orderItem) != 1) {
                                result = "false";
                            }
                        }
                    }
                }
            } else {
                result = "false";
                this.sysLogService.saveLog("修改订单数量以及订单商品数量", "失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "false";
        }
        return result;
    }

    @RequestMapping({"StorageWarehouseList"})
    public ModelAndView StorageWarehouseList(Page page) {
        logBefore(this.logger, "列表StorageWarehouseList");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        try {
            pd = getPageData();
            page.setPd(pd);
            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            pd.put("ck_id", LoginUtil.getLoginUser().getCkId());
            List<PageData> varList = this.enwarehouseorderService.StorageWarehouseList(page);
            mv.setViewName("inventorymanagement/enwarehouseorder/StorageWarehouse_List");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping({"addStorageoBeputinstorage"})
    public ModelAndView addStorageoBeputinstorage(Page page, String orderId, String type) {
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd = getPageData();
            pd.put("orderNum", orderId);
            page.setPd(pd);
            pd.put("parent_id", Integer.valueOf(0));

            List<PageData> productType = this.productTypeService.findByParentId(pd);
            mv.addObject("productType", productType);
            pd = this.enwarehouseorderService.getEnwarouseById(pd);
            List<PageData> pageDate = this.eNOrderItemService.getOrderItemlistPageProductById(page);
            mv.setViewName("inventorymanagement/enwarehouseorder/addStorageoBeputinstorage");
            mv.addObject("orderItemList", pageDate);
            mv.addObject("enwarhouse", pd);
            mv.addObject("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    @RequestMapping({"addPurchaseOrder"})
    public ModelAndView addPurchaseOrder(Page page, String orderId, String type) {
        ModelAndView mv = getModelAndView();
        try {
            PageData pd = new PageData();
            pd = getPageData();
            pd.put("orderNum", orderId);
            page.setPd(pd);
            pd.put("parent_id", Integer.valueOf(0));

            List<PageData> productType = this.productTypeService.findByParentId(pd);
            mv.addObject("productType", productType);
            List<PageData> warhouseList = this.warehouseService.listAll(pd);
            mv.addObject("wlist", warhouseList);
            mv.addObject("type", type);

            mv.setViewName("inventorymanagement/salesReturn/purchaseReturn_edit");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping({"/purchaseReturnlistpage"})
    public ModelAndView purchaseReturnlistpage(Page page) {
        logBefore(this.logger, "列表EnWarehouseOrder");
        ModelAndView mv = getModelAndView();
        PageData pd = new PageData();
        try {
            pd = getPageData();
            page.setPd(pd);
            String lastLoginStart = pd.getString("order_date");
            if ((lastLoginStart != null) && (!"".equals(lastLoginStart))) {
                String lastLoginEnd = lastLoginStart;
                lastLoginStart = lastLoginStart + " 00:00:00";
                lastLoginEnd = lastLoginEnd + " 23:59:59";
                pd.put("lastLoginStart", lastLoginStart);
                pd.put("lastLoginEnd", lastLoginEnd);
            }
            String ivt_state = pd.getString("ivt_state");
            if ((ivt_state != null) && (!"".equals("ivt_state"))) {
                pd.put("ivt_state", ivt_state);
            } else {
                pd.put("ivt_state", "");
            }
            List<PageData> varList = this.enwarehouseorderService.purchaseReturnlistpage(page);
            mv.setViewName("inventorymanagement/salesReturn/purchaseReturn_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
        }
        return mv;
    }

    @RequestMapping(value = {"savePurchaseOrder"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String savePurchaseOrder(Integer supplierId, String comment, String ordernum, String orderdate,
                                    String managerName, String managerTel) {
        try {
            if ((ordernum != null) && (!ordernum.equals(""))) {
                PageData pd = new PageData();
                pd.put("ordernum", ordernum);
                pd = this.enwarehouseorderService.getEnwarouseByorderNum(pd);
                return pd.get("order_num").toString() + "," + pd.getString("group_num") + "," + pd.getString("id");
            }
            PageData inferiorEnwarhouse = new PageData();
            inferiorEnwarhouse.put("group_num", "GP_" + StringUtil.getStringOfMillisecond(""));
            inferiorEnwarhouse.put("order_num", "RK_" + StringUtil.getStringOfMillisecond(""));
            inferiorEnwarhouse.put("supplier_id", supplierId);
            inferiorEnwarhouse.put("manager_name", managerName);
            inferiorEnwarhouse.put("order_date", orderdate);
            inferiorEnwarhouse.put("manager_tel", managerTel);
            inferiorEnwarhouse.put("comment", comment);
            inferiorEnwarhouse.put("final_amount", "0.00");
            inferiorEnwarhouse.put("total_svolume", "0.00");
            inferiorEnwarhouse.put("total_weight", "0.00");
            inferiorEnwarhouse.put("is_ivt_order_print", Integer.valueOf(1));
            inferiorEnwarhouse.put("is_temporary", Integer.valueOf(2));
            inferiorEnwarhouse.put("ivt_state", Integer.valueOf(1));
            inferiorEnwarhouse.put("order_date", orderdate);
            inferiorEnwarhouse.put("user_id", Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
            inferiorEnwarhouse.put("order_type", Integer.valueOf(6));
            this.enwarehouseorderService.save(inferiorEnwarhouse);
            return inferiorEnwarhouse.get("order_num").toString() + "," + inferiorEnwarhouse.getString("group_num")
                    + "," + inferiorEnwarhouse.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = {"/purchaseReviewedAll"}, produces = {"application/text;charset=UTF-8"})
    @ResponseBody
    public String purchaseReviewedAll() {
        logBefore(this.logger, "批量审核EnWarehouseOrder");
        PageData pd = new PageData();
        String result = "true";
        Map<String, Object> map = new HashMap();
        try {
            String orderMsg = "";
            String productMsg = "";
            pd = getPageData();
            List<PageData> pdList = new ArrayList();
            String DATA_IDS = pd.getString("DATA_IDS");
            if ((DATA_IDS != null) && (!"".equals(DATA_IDS))) {
                String[] ArrayDATA_IDS = DATA_IDS.split(",");

                if (!this.enwarehouseorderService.purchaseeviewedAll(ArrayDATA_IDS, orderMsg, productMsg)) {
                    result = "false";
                    this.sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
                } else {
                    pd.put("productMsg", productMsg);
                    pd.put("orderMsg", orderMsg);
                    this.sysLogService.saveLog("批量审核EnWarehouseOrder", "成功");
                }
            } else {
                result = "false";
                this.sysLogService.saveLog("批量审核EnWarehouseOrder", "失败");
            }
            pdList.add(pd);
            map.put("list", pdList);
        } catch (Exception e) {
            this.logger.error(e.toString(), e);
            result = "false";
        } finally {
            logAfter(this.logger);
        }
        return result;
    }

    @RequestMapping({"/downEnOrderExcel"})
    public void downEnOrderExcel(HttpServletResponse response) throws Exception {
        FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/file/" + "EnWarehouseOrder.xlsx",
                "Tarot系统-导入入库单模版.xlsx");
    }

    @RequestMapping({"/readENOrderExcel"})
    public ModelAndView readENOrderExcel(@RequestParam(value = "excel", required = false) MultipartFile file)
            throws Exception {
        ModelAndView mv = getModelAndView();
        String operationMsg = "导入 入库单到数据库";
        logBefore(logger, operationMsg);
        PageData pd = new PageData();
        try {
            User user = LoginUtil.getLoginUser();
            if ((file != null) && (!file.isEmpty())) {
                String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
                String fileName = FileUpload.fileUp(file, filePath, "ENOrderExcel");
                List<PageData> listPd = (List) ObjectExcel.readExcel(filePath, fileName, 1, 0, 0);

                if ((listPd != null) && (listPd.size() > 0)) {
                    List<ENOrder> listENOrder = new ArrayList();
                    List<ENOrderItem> listENOrderItem = new ArrayList();

                    ENOrder eno = null;
                    ENOrderItem enoi = null;
                    Supplier supplier = null;
                    PageData pdE = null;
                    List<PageData> pdEList = new ArrayList();
                    Product product = null;
                    List<Product> listProduct = new ArrayList();
                    boolean pflag = false;
                    List<PageData> pdPriceList = new ArrayList();
                    PageData pdPrice = null;
                    PageData pdM = null;
                    String groupNum = "GP_" + StringUtil.getStringOfMillisecond("");
                    PageData pdOfOrderGroup = new PageData();
                    OrderGroup og = new OrderGroup();
                    og.setOrderGroupNum(groupNum);
                    og.setUser(user);
                    og.setCkId(user.getCkId());
                    og.setGroupType(1);
                    int k = -1;
                    int u = -1;
                    PageData pdP = null;
                    String contactPerson = "";
                    String contactPersonMobile = "";
                    String comment = "";
                    double skuWeightEn = 0.0D;
                    double skVolumeEn = 0.0D;

                    double quantityOfProduct = 0.0D;
                    double priceOfProduct1 = 0.0D;
                    double priceOfProduct2 = 0.0D;
                    StringBuffer rowMes = null;
                    StringBuffer reasonMes = null;
                    for (int i = 0; i < listPd.size(); i++) {
                        product = new Product();
                        pdE = new PageData();
                        pdM = new PageData();
                        k = -1;
                        u = -1;
                        rowMes = new StringBuffer();
                        reasonMes = new StringBuffer();
                        if (listPd.get(i).getString("var0").trim().equals("")) {
                            pdE.put("line", Integer.valueOf(i + 2));
                            pdEList.add(pdE);
                            mv.addObject("varList", pdEList);
                            mv.setViewName("procurement/ordergroup/importExcelPage");
                            return mv;
                        }
                        if ((!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var0").toString()))
                                && (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var1").toString()))) {
                            pdM.put("barCode", ((PageData) listPd.get(i)).get("var0").toString());
                            pdM.put("hostCode", ((PageData) listPd.get(i)).get("var1").toString());
                            product = this.productService.findById(pdM);
                            if (product != null) {
                                pdPrice = new PageData();
                                pdPrice.put("product_id", Long.valueOf(product.getId()));
                                pdPrice.put("price_type", Integer.valueOf(1));
                                pdPriceList = productService.findPriceOfProductList(pdPrice);
                                pdPrice = (PageData) pdPriceList.get(0);
                                // 商品价格
                                if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
                                    priceOfProduct1 = Double.parseDouble(pdPrice.getString("product_price").toString());
                                    pflag = false;
                                    if ((listProduct != null) && (listProduct.size() > 0)) {
                                        for (int pi = 0; pi < listProduct.size(); pi++) {
                                            if (product.getId() == ((Product) listProduct.get(pi)).getId()) {
                                                pflag = true;
                                                break;
                                            }
                                        }
                                    }
                                    pdPrice.put("product_id", Long.valueOf(product.getId()));
                                    pdPrice.put("price_type", Integer.valueOf(2));
                                    pdPriceList = this.productService.findPriceOfProductList(pdPrice);
                                    pdPrice = (PageData) pdPriceList.get(0);
                                    if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
                                        priceOfProduct2 = Double
                                                .parseDouble(pdPrice.getString("product_price").toString());
                                    }
                                    if (!pflag) {
                                        listProduct.add(product);
                                    }
                                    if (k > -1) {
                                        for (int en = 0; en < listENOrderItem.size(); en++) {
                                            if ((eno.getOrderNum() == ((ENOrderItem) listENOrderItem.get(en))
                                                    .getOrderNum())
                                                    && ((listENOrderItem.get(en)).getProduct().getId() == product
                                                    .getId())) {
                                                u = en;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    pdE.put("line", Integer.valueOf(i + 2));
                                    pdE.put("rowP", "I和K");
                                    pdE.put("reasonP", "商品不存在");
                                }
                            } else {
                                pdE.put("line", Integer.valueOf(i + 2));
                                pdE.put("rowP", "I和K");
                                pdE.put("reasonP", "商品不存在");
                            }
                        } else {
                            pdE.put("line", Integer.valueOf(i + 2));
                            pdE.put("rowP", "I和K");
                            pdE.put("reasonP", "商品数据为空");
                        }
                        // 商品数量
                        if ((((PageData) listPd.get(i)).get("var3") != null)
                                && (((PageData) listPd.get(i)).get("var3") != "")
                                && (!StringUtil.isEmpty(((PageData) listPd.get(i)).get("var3").toString()))) {

                            quantityOfProduct = Double.parseDouble(((PageData) listPd.get(i)).get("var3").toString());
                            if (u > -1) {
                                quantityOfProduct += (listENOrderItem.get(u)).getQuantity();
                            }
                        } else {
                            pdE.put("line", Integer.valueOf(i + 2));
                            pdE.put("rowC", "R");
                            pdE.put("reasonC", "商品订购数量为空");
                        }

                        if (listPd.get(i).get("var4") != null) {
                            comment = "";
                            if (k > -1) {
                                if (!listENOrder.get(k).getComment().contains(listPd.get(i).get("var4").toString())) {
                                    comment = listENOrder.get(k).getComment() + listPd.get(i).get("var4").toString();
                                } else {
                                    comment = listPd.get(i).get("var4").toString();
                                }
                            } else {
                                comment = (listPd.get(i)).get("var4").toString();
                            }
                        }

                        if (!StringUtil.isEmpty(pdE.getString("rowP"))) {
                            rowMes.append(pdE.getString("rowP") + "，");
                        }
                        if (!StringUtil.isEmpty(pdE.getString("rowC"))) {
                            rowMes.append(pdE.getString("rowC") + "，");
                        }
                        if (!StringUtil.isEmpty(pdE.getString("rowS"))) {
                            rowMes.append(pdE.getString("rowS") + "；");
                        }
                        if (!StringUtil.isEmpty(pdE.getString("reasonP"))) {
                            reasonMes.append(pdE.getString("reasonP") + "，");
                        }
                        if (!StringUtil.isEmpty(pdE.getString("reasonC"))) {
                            reasonMes.append(pdE.getString("reasonC") + "，");
                        }
                        if (!StringUtil.isEmpty(pdE.getString("reasonS"))) {
                            reasonMes.append(pdE.getString("reasonS") + "；");
                        }

                        if ((pdE != null) && (pdE.getString("line") != null) && (pdE.getString("line") != "")) {
                            pdE.put("row", rowMes);
                            pdE.put("reason", reasonMes);

                            pdEList.add(pdE);
                        }
                        // 判断listProduct是否为空
                        if (listProduct != null && listProduct.size() > 0) {

                            supplier = new Supplier();
                            eno = new ENOrder();
                            enoi = new ENOrderItem();
                            contactPerson = "";
                            contactPersonMobile = "";
                            pdP = new PageData();

                            // 根据商品价格查出来的集合
                            pdP = this.productService
                                    .findProductInfoById(Long.parseLong((pdPriceList.get(0).getString("tpId"))));
                            if (listENOrder != null && listENOrder.size() > 0) {
                                for (int en = 0; en < listENOrder.size(); en++) {
                                    if (((listENOrder.get(en)).getSupplier().getId() == 42873290L) && pdP == null) {
                                        //System.out.println("");
                                    }
                                    if (Long.parseLong(pdP.getString("tsId")) == ((ENOrder) listENOrder.get(en))
                                            .getSupplier().getId()) {
                                        eno = (ENOrder) listENOrder.get(en);
                                        k = en;
                                        break;
                                    }
                                }
                            }
                            if (!StringUtil.isEmpty(pdP.getString("contact_person"))) {
                                contactPerson = pdP.getString("contact_person");
                            }
                            if (!StringUtil.isEmpty(pdP.getString("contact_person_mobile"))) {
                                contactPersonMobile = pdP.getString("contact_person_mobile");
                            }
                            if (!StringUtil.isEmpty(pdP.getString("sku_weight"))) {
                                skuWeightEn = Double.parseDouble(pdP.getString("sku_weight"));
                            }
                            if (!StringUtil.isEmpty(pdP.getString("sku_volume"))) {
                                skVolumeEn = Double.parseDouble(pdP.getString("sku_volume"));
                            }
                            if (k == -1) {// 创建新的入库单
                                supplier.setId(Long.parseLong(pdP.getString("tsId")));
                                eno.setGroupNum(groupNum);
                                eno.setOrderNum(
                                        "RK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
                                eno.setOrderDate(Tools.date2Str(new Date()));
                                eno.setCheckedState(1);
                                eno.setManagerName(contactPerson);
                                eno.setManagerTel(contactPersonMobile);
                                eno.setPaidAmount(0.0D);
                                eno.setIsIvtOrderPrint(0);
                                eno.setIsTemporary(2);
                                eno.setSupplier(supplier);
                                eno.setUser(user);
                                eno.setTotalSvolume(MathUtil.mul(skVolumeEn, quantityOfProduct));
                                eno.setTotalWeight(MathUtil.mul(skuWeightEn, quantityOfProduct));
                                eno.setOrderType(1);
                                eno.setComment(comment);
                                eno.setCkId(user.getCkId());
                            }
                            if (u == -1) { // 不用创建入库订单,创建新的入库订单条目
                                enoi.setGroupNum(eno.getGroupNum());
                                enoi.setOrderNum(eno.getOrderNum());
                                enoi.setPurchasePrice(priceOfProduct1);
                                enoi.setQuantity(quantityOfProduct);
                                enoi.setFinalQuantity(quantityOfProduct);
                                enoi.setIsSplitIvt(2);
                                enoi.setSvolume(MathUtil.mul(skVolumeEn, quantityOfProduct) + "");
                                enoi.setWeight(MathUtil.mul(skuWeightEn, quantityOfProduct) + "");
                                enoi.setIsIvtBK(1);
                                enoi.seteNTime(DateUtil.getAfterDayDate("3"));
                                enoi.setCreator(user.getNAME());
                                enoi.setProduct(product);
                                listENOrderItem.add(enoi);
                            }
                            if (u > -1) {// 1不用创建销售订单条目 只更新该条目中商品数量、备注即可
                                listENOrderItem.get(u).setQuantity(quantityOfProduct);
                                listENOrderItem.get(u).setFinalQuantity(quantityOfProduct);
                            }
                            if (k > -1) {
                                ((ENOrder) listENOrder.get(k)).setFinalAmount(MathUtil.mulAndAdd(quantityOfProduct,
                                        priceOfProduct1, ((ENOrder) listENOrder.get(k)).getFinalAmount()));
                                ((ENOrder) listENOrder.get(k)).setAmount(MathUtil.mulAndAdd(quantityOfProduct,
                                        priceOfProduct1, ((ENOrder) listENOrder.get(k)).getFinalAmount()));
                                ((ENOrder) listENOrder.get(k))
                                        .setTotalSvolume(MathUtil.add(((ENOrder) listENOrder.get(k)).getTotalSvolume(),
                                                Double.parseDouble(enoi.getSvolume())));
                                ((ENOrder) listENOrder.get(k))
                                        .setTotalWeight(MathUtil.add(((ENOrder) listENOrder.get(k)).getTotalWeight(),
                                                Double.parseDouble(enoi.getWeight())));
                            }
                            if (k == -1) {
                                eno.setAmount(MathUtil.mul(enoi.getPurchasePrice(), quantityOfProduct));
                                eno.setFinalAmount(MathUtil.mul(enoi.getPurchasePrice(), quantityOfProduct));
                                listENOrder.add(eno);
                            }

                        }
                    }
                    if ((pdEList != null) && (pdEList.size() > 0) && (pdEList.get(0) != null)
                            && (!StringUtil.isEmpty(((PageData) pdEList.get(0)).getString("line")))) {
                        mv.addObject("varList", pdEList);
                    }
                    if ((pdEList == null) || (pdEList.size() <= 0)
                            || (StringUtil.isEmpty(((PageData) pdEList.get(0)).getString("line")))) {
                        pdEList = null;
                    }
                    int saveFlag = 1;
                    int reFlag = 0;
                    if (pdEList == null) {
                        if ((listENOrder != null) && (listENOrder.size() > 0)) {
                            logMidway(this.logger, "入库单数组大小为：" + listENOrder.size());
                            reFlag = this.enOrderService.saveList(listENOrder);
                            saveFlag = 2;
                            if ((listENOrderItem != null) && (listENOrderItem.size() > 0)) {
                                reFlag = this.eNOrderItemService.saveList(listENOrderItem);
                                saveFlag = 3;
                                if (pdOfOrderGroup != null) {
                                    reFlag = this.orderGroupService.saveOrderGroup(og);
                                    saveFlag = 1;
                                }
                            } else {
                                this.logMidway(logger, " 入库订单数组条目为空，请检查处理过程");
                            }
                        } else {
                            this.logMidway(logger, " 入库订单数组为空，请检查处理过程");
                        }
                    }
                    if (saveFlag > 1) {
                        this.enOrderService.deleteList(listENOrder);
                    }
                    if (saveFlag > 2) {
                        this.eNOrderItemService.deleteList(listENOrderItem);
                    }
                    logMidway(this.logger, "-导入入库单，返回状态为-saveFlag=" + saveFlag);
                    listENOrder = null;
                    listENOrderItem = null;
                    eno = null;
                    enoi = null;
                    supplier = null;
                    pdE = null;
                    product = null;
                    listProduct = null;
                    pdM = null;
                    if (saveFlag == 1 && pdEList == null) {
                        mv.addObject("errorMsg", "数据导入成功。");
                        mv.addObject("msg", "success");
                    } else {
                        mv.addObject("errorMsg", "请检查导入Excel表格式或数据是否为空后重新操作。");
                    }
                } else {
                    mv.addObject("errorMsg", "数据表中没有数据");
                }
            }
            sysLogService.saveLog(operationMsg, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            logMidway(logger, operationMsg + "，出现错误：" + e.toString());
        } finally {
            logEnd(logger, operationMsg);
        }
        mv.setViewName("procurement/ordergroup/importExcelPage");
        return mv;
    }

}
