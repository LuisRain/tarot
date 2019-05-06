package com.hy.controller.product;

import com.hy.controller.base.BaseController;
import com.hy.entity.product.ProductType;
import com.hy.entity.product.ProductTypeTree;
import com.hy.service.product.ProductTypeService;
import com.hy.service.product.ProductTypeTreeService;
import com.hy.util.AppUtil;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/productTypeTree"})
public class ProductTypeTreeController
  extends BaseController
{
  private static final Logger logger = LoggerFactory.getLogger(ProductTypeTreeController.class);
  
  @Resource(name="productTypeService")
  private ProductTypeService productTypeService;
  
  @Resource(name="productTypeTreeService")
  private ProductTypeTreeService productTypeTreeService;
  
  @RequestMapping({"/createProductTypeTree"})
  @ResponseBody
  public Object createProductTypeTree(ModelMap model, HttpServletRequest request, HttpServletResponse response)
  {
    logger.info("---------------createProductTypeTree----------------");
    int addMenu = 0;
    PrintWriter out = null;
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      List<ProductType> ptypes = new ArrayList();
      List<ProductType> ptypes2 = null;
      List<ProductType> ptypes3 = null;
      
      pd.put("parent_id", Integer.valueOf(0));
      ptypes = this.productTypeService.findProductTypeByParentId(pd);
      if ((ptypes != null) && (ptypes.size() > 0)) {
        for (int i = 0; i < ptypes.size(); i++) {
          pd.put("parent_id", Long.valueOf(((ProductType)ptypes.get(i)).getId()));
          ptypes2 = this.productTypeService
            .findProductTypeByParentId(pd);
          if ((ptypes2 != null) && (ptypes2.size() > 0)) {
            ((ProductType)ptypes.get(i)).setPts(ptypes2);
            for (int j = 0; j < ptypes2.size(); j++) {
              pd.put("parent_id", Long.valueOf(((ProductType)ptypes2.get(j)).getId()));
              ptypes3 = this.productTypeService
                .findProductTypeByParentId(pd);
              if ((ptypes3 != null) && (ptypes3.size() > 0)) {
                ((ProductType)ptypes2.get(j)).setPts(ptypes3);
                for (int n = 0; n < ptypes3.size(); n++) {}
              }
            }
          }
        }
        
        ProductTypeTree ptt = null;
        
        String menuTreeString = StringUtil.splicingProductTypeTree(ptypes);
        if (!StringUtil.isEmpty(menuTreeString))
        {
          pd.put("state", Integer.valueOf(1));
          List<ProductTypeTree> ptts = this.productTypeTreeService
            .getProductTypeTree(pd);
          if ((ptts != null) && (ptts.size() > 0)) {
            for (int i = 0; i < ptts.size(); i++) {
              ptt = new ProductTypeTree();
              ptt = (ProductTypeTree)ptts.get(i);
              ptt.setState(2);
              logger.info("==id==" + ptt.getId());
              this.productTypeTreeService.updateProductType(ptt);
            }
          }
          
          ptt = new ProductTypeTree();
          ptt.setMenuTree(menuTreeString);
          ptt.setState(1);
          addMenu = this.productTypeTreeService.addProductTypeTree(ptt);
          if (addMenu > 0) {
            addMenu = 1;
            logger.info("最新分类菜单创建成功。");
            map.put("menuTree", menuTreeString);
            map.put("state", Integer.valueOf(1));
            pd.put("msg", "ok");
          } else {
            logger.info("最新分类菜单创建失败。");
            pd.put("msg", "no");
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return AppUtil.returnObject(pd, map);
  }
  
  @RequestMapping({"/getProductTypeTree"})
  @ResponseBody
  public Object getProductTypeTree(ModelMap model, HttpServletRequest request, HttpServletResponse response)
  {
    logger.info("---------------getProductTypeTree----------------");
    
    PageData pd = new PageData();
    Map<String, Object> map = new HashMap();
    try {
      pd.put("state", Integer.valueOf(1));
      List<ProductTypeTree> ptts = this.productTypeTreeService.getProductTypeTree(pd);
      if ((ptts != null) && (ptts.size() > 0)) {
        logger.info("----------获取菜单树成功。");
        map.put("menuTree", ((ProductTypeTree)ptts.get(0)).getMenuTree());
        pd.put("msg", "ok");
      } else {
        logger.info("最新分类菜单创建失败。");
        pd.put("msg", "no");
        logger.info("----------获取全部分类菜单失败。");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return AppUtil.returnObject(pd, map);
  }
  
  @RequestMapping({"/getProudctTypeList"})
  @ResponseBody
  public Object getProudctTypeList() throws Exception { Map<String, Object> map = new HashMap();
    PageData pd = new PageData();
    List<PageData> proudctTypeTreeList = this.productTypeTreeService.getProudctTypeList();
    map.put("menuTree", proudctTypeTreeList);
    pd.put("msg", "ok");
    return AppUtil.returnObject(pd, map);
  }
}


