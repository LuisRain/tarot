package com.hy.controller.inventory;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.inventory.WarehouseService;
import com.hy.util.Logger;
import com.hy.util.LoginUtil;
import com.hy.util.ObjectExcelView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/productinventroy"})
public class ProductInventroyController extends BaseController
{
  @Resource
  private ProductinventoryService productinventoryService;
  @Resource
  private WarehouseService warehouseService;
  
  @RequestMapping({"getProductInventroyinfo"})
  public ModelAndView getProductInventroyinfo(Page page) throws Exception
  {
    ModelAndView mv = getModelAndView();
    logBefore(this.logger, "新增warehouse");
    com.hy.util.PageData pd = new com.hy.util.PageData();
    pd = getPageData();
    pd.put("keyword", pd.getString("keyword").trim());
    String type = pd.getString("type");
    if (type.equals("")) {
      pd.put("type", Integer.valueOf(0));
    }
    pd.put("ck_id",LoginUtil.getLoginUser().getCkId());
    page.setPd(pd);
    java.util.List<com.hy.util.PageData> productInventoryList = this.productinventoryService.geProductByidhistorylistPage(page);
    //java.util.List<com.hy.util.PageData> productInventoryList = this.productinventoryService.getProductInventroyinfo(page);
    mv.setViewName("procurement/product/productInventoryList");
    mv.addObject("list", productInventoryList);
    mv.addObject("pd", pd);
    return mv;
  }
  
  @RequestMapping({"excel"})
  public ModelAndView exportExcel(Page page)
  {
    ModelAndView mv = getModelAndView();
    try
    {
      com.hy.util.PageData pd = new com.hy.util.PageData();
      pd = getPageData();
      String type = pd.getString("type");
      if (type.equals("")) {
        pd.put("type", Integer.valueOf(0));
      }
      page.setPd(pd);
      Map<String, Object> dataMap = new HashMap();
      java.util.List<String> titles = new ArrayList();
      
      titles.add("序");
      titles.add("入库日期");
      titles.add("单号");
      titles.add("商品名称");
      titles.add("供应商/客户");
      titles.add("仓库");
      titles.add("类型");
      titles.add("数量");
      titles.add("单位");
      dataMap.put("titles", titles);
      
      java.util.List<com.hy.util.PageData> productInventoryList = this.productinventoryService.getProductInventroyinfo(page);
      java.util.List<com.hy.util.PageData> varList = new ArrayList();
      for (int i = 0; i < productInventoryList.size(); i++) {
        com.hy.util.PageData vpd = new com.hy.util.PageData();
        vpd.put("var1", Integer.valueOf(i + 1));
        vpd.put("var2", ((com.hy.util.PageData)productInventoryList.get(i)).getString("en_time"));
        vpd.put("var3", ((com.hy.util.PageData)productInventoryList.get(i)).getString("order_num"));
        vpd.put("var4", ((com.hy.util.PageData)productInventoryList.get(i)).getString("product_name"));
        vpd.put("var5", ((com.hy.util.PageData)productInventoryList.get(i)).getString("supplier_name"));
        vpd.put("var6", ((com.hy.util.PageData)productInventoryList.get(i)).getString("warehouse_name"));
        String Inventorytype = ((com.hy.util.PageData)productInventoryList.get(i)).getString("type");
        if (Inventorytype.equals("1")) {
          vpd.put("var7", "入库");
        } else {
          vpd.put("var7", "出库");
        }
        vpd.put("var8", ((com.hy.util.PageData)productInventoryList.get(i)).getString("quantity"));
        vpd.put("var9", ((com.hy.util.PageData)productInventoryList.get(i)).getString("unit"));
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
}


