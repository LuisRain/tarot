package com.hy.controller.inventory;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.OrderGroup;
import com.hy.entity.product.Product;
import com.hy.entity.system.User;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.order.ENOrderItemService;
import com.hy.service.order.EnWarehouseOrderService;
import com.hy.service.order.OrderGroupService;
import com.hy.service.product.ProductService;
import com.hy.util.FileUpload;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcelRead;
import com.hy.util.OrderNum;
import com.hy.util.PageData;
import com.hy.util.PathUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/importentempwarehouseorder"})
public class ImportEnTempWarehouseOrderController
  extends BaseController
{
  @Resource(name="enwarehouseorderService")
  private EnWarehouseOrderService enwarehouseorderService;
  @Resource(name="productService")
  private ProductService productService;
  @Resource(name="orderGroupService")
  private OrderGroupService orderGroupService;
  @Resource(name="productinventoryService")
  private ProductinventoryService productinventoryService;
  @Resource(name="eNOrderItemService")
  private ENOrderItemService eNOrderItemService;
  
  @RequestMapping({"/goImportTempExcel"})
  public ModelAndView goImportTempExcel(String type)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("inventorymanagement/entempwarehouseorder/importtempexcel");
    mv.addObject("type", type);
    return mv;
  }
  
  @RequestMapping(value={"/readTempExcel"}, produces={"application/json;charset=UTF-8"})
  @ResponseBody
  public String readTempExcel(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    String result = "false";
    
    if ((file != null) && (!file.isEmpty()))
    {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
      String fileName = FileUpload.fileUp(file, filePath, "EetempExcel");
      readExcel(filePath, fileName);
      result = "true";
    }
    return result;
  }
  
  @RequestMapping({"importTempOrder"})
  @ResponseBody
  public String importTempOrder(Page page, String inventoryType)
  {
    boolean flg = false;
    try
    {
      flg = this.productinventoryService.saveTempInventory(page, inventoryType);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return flg + inventoryType;
  }
  
  private void readExcel(String filePath, String filename) throws Exception
  {
    List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, 
      filename, 1, 0, 0);
    if (listPd.size() > 0) {
      List<PageData> templist = new ArrayList();
      for (int i = 0; i < listPd.size(); i++) {
        Product product = this.productService.findProductByBarCode(
          ((PageData)listPd.get(i)).getString("var0"));
        PageData pageData = new PageData();
        if (product != null) {
          pageData.put("excelrow", Integer.valueOf(i));
          pageData.put("barcode", ((PageData)listPd.get(i)).get("var0"));
          pageData.put("productname", product.getProductName());
          pageData.put("quantity", ((PageData)listPd.get(i)).get("var4"));
          pageData.put("type", "1");
          pageData.put("reason", ((PageData)listPd.get(i)).get("var6"));
          pageData.put("productid", Long.valueOf(product.getId()));
        }
        else {
          pageData.put("excelrow", Integer.valueOf(i));
          pageData.put("barcode", ((PageData)listPd.get(i)).get("var0"));
          pageData.put("productname", "");
          pageData.put("quantity", ((PageData)listPd.get(i)).get("var4"));
          pageData.put("reason", ((PageData)listPd.get(i)).get("var6"));
          pageData.put("type", "0");
          pageData.put("productid", "");
        }
        
        templist.add(pageData);
      }
      this.productinventoryService.crateTempTable();
      this.productinventoryService.tempsave(templist);
    }
  }
  
  @RequestMapping({"/openExcel"})
  public ModelAndView openExcel(Page page)
    throws Exception
  {
    logBefore(this.logger, "新增EntempWarehouseOrder");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    List<PageData> list = this.productinventoryService.listTemp(page);
    mv.setViewName("inventorymanagement/entempwarehouseorder/showtempexcel");
    mv.addObject("pd", pd);
    mv.addObject("productlist", list);
    return mv;
  }
  
  public ModelAndView readExcel(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    if ((file != null) && (!file.isEmpty())) {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
      String fileName = FileUpload.fileUp(file, filePath, "entempexcel");
      List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, 
        fileName, 1, 0, 0);
      
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      PageData enTempOrderParam = new PageData();
      String orderGroupNum = OrderNum.getOrderGourpNum();
      OrderGroup og = new OrderGroup();
      og.setOrderGroupNum(orderGroupNum);
      og.setUser(LoginUtil.getLoginUser());
      og.setGroupType(1);
      this.orderGroupService.saveOrderGroup(og);
      String orderNum = OrderNum.getEnTempOrderNum();
      enTempOrderParam.put("order_num", orderNum);
      enTempOrderParam.put("is_order_print", Integer.valueOf(1));
      enTempOrderParam.put("ivt_state", Integer.valueOf(2));
      enTempOrderParam.put("is_temporary", Integer.valueOf(1));
      enTempOrderParam.put("user_id", 
        Long.valueOf(LoginUtil.getLoginUser().getUSER_ID()));
      enTempOrderParam.put("group_num", orderGroupNum);
      this.enwarehouseorderService.save(enTempOrderParam);
      
      for (PageData p : listPd) {
        PageData productParam = new PageData();
        
        productParam.put("bar_code", p.get("var0"));
        Product product = this.productService
          .findProductByBarCode(productParam);
        
        PageData orderItem = new PageData();
        orderItem.put("product_id", Long.valueOf(product.getId()));
        orderItem.put("group_num", orderGroupNum);
        orderItem.put("order_num", orderNum);
        orderItem.put("quantity", p.get("var4"));
        orderItem.put("reason", p.get("var6"));
        orderItem.put("comment", p.get("var7"));
        orderItem.put("is_ivt_BK", Integer.valueOf(1));
        this.eNOrderItemService.save(orderItem);
        
        PageData productInvertory = new PageData();
        productInvertory.put("productId", Long.valueOf(product.getId()));
        productInvertory.put("newQuantity", p.get("var5"));
        productInvertory.put("warhouseId", Integer.valueOf(1));
        this.productinventoryService
          .updateProductinventoryAdd(productInvertory);
      }
      
      mv.addObject("msg", "success");
    }
    
    mv.setViewName("save_result");
    return mv;
  }
}


