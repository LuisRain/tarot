package com.hy.controller.product;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.service.inventory.ProductinventoryService;
import com.hy.service.product.CargoSpaceService;
import com.hy.service.product.ProductService;
import com.hy.service.product.ProductTypeService;
import com.hy.service.product.ProductpriceService;
import com.hy.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping({"/importingproduct"})
public class ImportingProductConteroller
  extends BaseController
{
  @Resource(name="productpriceService")
  private ProductpriceService productpriceService;
  @Resource(name="productService")
  private ProductService productService;
  @Resource(name="productTypeService")
  private ProductTypeService productTypeService;
  @Resource(name="productinventoryService")
  private ProductinventoryService productinventoryService;
  @Resource
  private CargoSpaceService cargoSpaceService;
  
  @RequestMapping({"/goUploadExcel"})
  public ModelAndView goUploadExcel()
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    mv.setViewName("procurement/product/uploadexcel");
    return mv;
  }
  
  @RequestMapping({"/readExcel"})
  public ModelAndView readExcel(@RequestParam(value="excel", required=false) MultipartFile file)
    throws Exception
  {
    ModelAndView mv = getModelAndView();
    if ((file != null) && (!file.isEmpty())) {
      String filePath = PathUtil.getClasspath() + "uploadFiles/file/";
      String fileName = FileUpload.fileUp(file, filePath, "productexcel");
      List<PageData> listPd = (List)ObjectExcelRead.readExcel(filePath, fileName, 2, 0, 0);
      
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      for (int i = 0; i < listPd.size(); i++) {
        PageData product = new PageData();
        PageData cargo = new PageData();
        PageData productinvent = new PageData();
        PageData productprice = new PageData();
        product.put("BAR_CODE", ((PageData)listPd.get(i)).getString("var1"));
        product.put("HOST_CODE", ((PageData)listPd.get(i)).getString("var2"));
        product.put("PRODUCT_NAME", ((PageData)listPd.get(i)).getString("var3"));
        product.put("MIN_STOCK_NUM", ((PageData)listPd.get(i)).getString("var9"));
        product.put("MAX_STOCK_NUM", ((PageData)listPd.get(i)).getString("var10"));
        product.put("UNIT", ((PageData)listPd.get(i)).getString("var14"));
        product.put("PRODUCT_NUM", "PRD" + StringUtil.getStringOfMillisecond(""));
        product.put("CREATE_TIME", df.format(new Date()));
        product.put("IS_SHELVE", Integer.valueOf(2));
        
        Long proudctTypeId = this.productTypeService.findNameById(((PageData)listPd.get(i)).getString("var5"));
        if (proudctTypeId != null)
        {
          product.put("product_type_id", proudctTypeId);
          cargo.put("zone", ((PageData)listPd.get(i)).getString("var4"));
          this.cargoSpaceService.save(cargo);
          this.productService.save(product);
          productinvent.put("cargo_space_id", cargo.get("id"));
          productinvent.put("product_id", product.get("id"));
          if (((PageData)listPd.get(i)).getString("var11").equals("")) {
            productinvent.put("product_quantity", Integer.valueOf(0));
          } else {
            productinvent.put("product_quantity", ((PageData)listPd.get(i)).getString("var11"));
          }
          
          productinvent.put("warehouse_id", "1");
          productinvent.put("state", "1");
          this.productinventoryService.save(productinvent);
          if (((PageData)listPd.get(i)).getString("var12").equals("")) {
            productinvent.put("product_quantity", Integer.valueOf(0));
          } else {
            productinvent.put("product_quantity", ((PageData)listPd.get(i)).getString("var12"));
          }
          
          productinvent.put("warehouse_id", "2");
          this.productinventoryService.save(productinvent);
          productprice.put("cost_price", ((PageData)listPd.get(i)).getString("var6"));
          productprice.put("wholesale_price", ((PageData)listPd.get(i)).getString("var7"));
          productprice.put("product_id", product.get("id"));
          this.productpriceService.save(productprice);
        }
      }
      
      mv.addObject("msg", "success");
    }
    
    mv.setViewName("save_result");
    return mv;
  }
  
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
    if (LoginUtil.getLoginUser().getROLE_ID().equals("24") ) {
      String username = LoginUtil.getLoginUser().getUSERNAME();
      pd.put("USERNAME",username);
    }
    page.setPd(pd);
    try {
      Map<String, Object> dataMap = new HashMap();
      List<String> titles = new ArrayList();
      titles.add("商品编码");
      titles.add("商品名称");
      titles.add("商品条形码");
      titles.add("商品最小起订量");
      titles.add("商品箱装数");
      titles.add("单位名称");
      titles.add("规格");
      titles.add("状态");
      titles.add("零售价");
      titles.add("供货价");
      titles.add("集采/统采");
      titles.add("供应商编码");
      titles.add("供应商名称");
      titles.add("供应商联系人");
      titles.add("供应商联系人电话");
      dataMap.put("titles", titles);
      List<PageData> productlist = this.productService.excel(page);
      List<PageData> varList = new ArrayList();
      for (int i = 0; i < productlist.size(); i++) {
        PageData vpd = new PageData();
        vpd.put("var1", ((PageData)productlist.get(i)).getString("商品编码"));
        vpd.put("var2", ((PageData)productlist.get(i)).getString("商品名称"));
        vpd.put("var3", ((PageData)productlist.get(i)).getString("商品条形码"));
        vpd.put("var4", ((PageData)productlist.get(i)).getString("商品最小起订量"));
        vpd.put("var5", ((PageData)productlist.get(i)).getString("商品箱装数"));
        vpd.put("var6", ((PageData)productlist.get(i)).getString("单位名称"));
        vpd.put("var7", ((PageData)productlist.get(i)).getString("规格"));
        vpd.put("var8", ((PageData)productlist.get(i)).getString("状态"));
        vpd.put("var9", ((PageData)productlist.get(i)).getString("零售价"));
        vpd.put("var10", ((PageData)productlist.get(i)).getString("供货价"));
        vpd.put("var11", ((PageData)productlist.get(i)).getString("集采/统采"));
        vpd.put("var12", ((PageData)productlist.get(i)).getString("供应商编码"));
        vpd.put("var13", ((PageData)productlist.get(i)).getString("供应商名称"));
        vpd.put("var14", ((PageData)productlist.get(i)).getString("供应商联系人"));
        vpd.put("var15", ((PageData)productlist.get(i)).getString("供应商联系人电话"));
        /*if(productlist.get(i).getString("is_shelve").trim().equals("1") || productlist.get(i).getString("is_shelve").trim().equals("")){
        	vpd.put("var23", "上架");
        }else{
        	vpd.put("var23", "下架");
        }*/
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


