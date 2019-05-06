package com.hy.controller.inventory;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.product.Merchant;
import com.hy.service.information.pictures.PicturesService;
import com.hy.util.ObjectExcelView;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import com.hy.util.sqlserver.DBConnectionManager;

@Controller
@RequestMapping({"/bulk"})
public class BulkController extends BaseController {
	
	@Resource
  private PicturesService picturesService;
	
	
  @RequestMapping({"/list"})
  public ModelAndView list(Page page)
  {
    logBefore(this.logger, "散货分拣查询");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    DBConnectionManager dbcm = new DBConnectionManager();
    List<PageData> pds = null;
    try {
      pd = getPageData();
      pds = new ArrayList();
      page.setPd(pd);
      String searchcriteria = pd.getString("searchcriteria");
      String laneway = pd.getString("laneway");
      String keyword = pd.getString("keyword");
      this.logger.info("----------searchcriteria=" + searchcriteria + ",laneway=" + laneway + ",keyword=" + keyword);
      if (!StringUtil.isEmpty(searchcriteria)) {
        pds = dbcm.selectProductOrMerchantInfos(searchcriteria, laneway, keyword);
      }
      if (("1".equals(searchcriteria)) || ("2".equals(searchcriteria)) || ("3".equals(searchcriteria)) || ("4".equals(searchcriteria))) {
        mv.addObject("pList", pds);
      }
      if (("5".equals(searchcriteria)) || ("6".equals(searchcriteria))) {
        mv.addObject("mList", pds);
      }
      mv.addObject("pd", pd);
      mv.setViewName("inventorymanagement/bulk/bulk_list");
    }
    catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  @RequestMapping({"/listtwo"})
  public ModelAndView listtwo(Page page)
  {
    logBefore(this.logger, "分拣商品信息查询");
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    DBConnectionManager dbcm = new DBConnectionManager();
    List<PageData> pds = null;
    try {
      pd = getPageData();
      pds = new ArrayList();
      page.setPd(pd);
      String searchcriteria = pd.getString("searchcriteria");
      String laneway = pd.getString("laneway");
      String keyword = pd.getString("keyword");
      this.logger.info("----------searchcriteria=" + searchcriteria + ",laneway=" + laneway + ",keyword=" + keyword);
      page.setPd(pd);
      if (!StringUtil.isEmpty(searchcriteria)) {
        pds =picturesService.findorderstore(page);
      }
      if (("1".equals(searchcriteria)) || ("2".equals(searchcriteria)) || ("3".equals(searchcriteria)) || ("4".equals(searchcriteria))) {
        mv.addObject("pList", pds);
      }
      if (("5".equals(searchcriteria)) || ("6".equals(searchcriteria))) {
        mv.addObject("mList", pds);
      }
      mv.addObject("pd", pd);
      mv.setViewName("inventorymanagement/bulk/bulk_lists");
    }
    catch (Exception e) {
      this.logger.error(e.toString(), e);
    }
    return mv;
  }
  
  
  /**
   * 写入分拣信息
   * @param midordersList
   */
  @RequestMapping({"/insertsqlserver"})
  public void insertsqlserver(PrintWriter out)
  {
    try
    {
      DBConnectionManager dcm = new DBConnectionManager();
      Connection conn = dcm.getConnection();
      //conn.setAutoCommit(false);
      String sql = "select * from Store";
      String sql2 = "select * from Orders";
      conn = dcm.getConnection();
      Statement stmt = conn.createStatement();
     // PreparedStatement prest = conn.prepareStatement(sql, 1005, 1007);
      ResultSet rs = stmt.executeQuery(sql);
      List<PageData> storelist=new ArrayList<PageData>();
      while (rs.next()) {
         PageData store=new PageData();
         store.put("id",rs.getString("id"));
         store.put("name",rs.getString("name"));
         store.put("sorgname",rs.getString("sorgname"));
         store.put("address",rs.getString("address"));
         store.put("linktel",rs.getString("linktel"));
         store.put("linkman",rs.getString("linkman"));
         store.put("location",rs.getString("location"));
         storelist.add(store);
        }
      picturesService.insertstore(storelist);
      
      rs = stmt.executeQuery(sql2);
      List<PageData> orderlist=new ArrayList<PageData>();
      while (rs.next()) {
         PageData order=new PageData();
         order.put("orderid",rs.getString("orderid"));
         order.put("sku",rs.getString("sku"));
         order.put("matchid",rs.getString("matchid"));
         order.put("quantity",rs.getString("quantity"));
         order.put("tolocation",rs.getString("tolocation"));
         order.put("area",rs.getString("area"));
         order.put("way",rs.getString("way"));
         order.put("address",rs.getString("address"));
         order.put("batchid",rs.getString("batchid"));
         order.put("fromlocation",rs.getString("fromlocation"));
         order.put("state",rs.getString("state"));
         order.put("truequantity",rs.getString("truequantity"));
         order.put("checkquantity",rs.getString("checkquantity"));
         order.put("downdate",rs.getString("downdate"));
         order.put("readtime",rs.getString("readtime"));
         order.put("returntime",rs.getString("returntime"));
         order.put("lighttime",rs.getString("lighttime"));
         order.put("oprationtime",rs.getString("oprationtime"));
         order.put("oprationstate",rs.getString("oprationstate"));
         order.put("shopid",rs.getString("shopid"));
         order.put("classes",rs.getString("classes"));
         order.put("goodssize",rs.getString("goodssize"));
         order.put("oprationcode",rs.getString("oprationcode"));
         order.put("boxcode",rs.getString("boxcode"));
         order.put("goodscode",rs.getString("goodscode"));
         order.put("corlorcode",rs.getString("corlorcode"));
         order.put("sizecode",rs.getString("sizecode"));
         order.put("price",rs.getString("price"));
         order.put("discount",rs.getString("discount"));
         order.put("suppliercode",rs.getString("suppliercode"));
         order.put("clientcode",rs.getString("clientcode"));
         order.put("partcode",rs.getString("partcode"));
         order.put("barcode",rs.getString("barcode"));
         order.put("goodsname",rs.getString("goodsname"));
         order.put("goodstype",rs.getString("goodstype"));
         order.put("prodarea",rs.getString("prodarea"));
         order.put("lotno",rs.getString("lotno"));
         order.put("tasktype",rs.getString("tasktype"));
         order.put("gateid",rs.getString("gateid"));
         order.put("checkuser",rs.getString("checkuser"));
         order.put("prino",rs.getString("prino"));
         order.put("block",rs.getString("block"));
         order.put("wmsboxcode",rs.getString("wmsboxcode"));
         order.put("orderway",rs.getString("orderway"));
         order.put("boxweight",rs.getString("boxweight"));
         order.put("clientname",rs.getString("clientname"));
         order.put("bkbarcode",rs.getString("bkbarcode"));
         order.put("wmsorderid",rs.getString("wmsorderid"));
         order.put("shopname",rs.getString("shopname"));
         orderlist.add(order);
        }
      picturesService.insertorder(orderlist);
      //System.out.println("订单条目插入散货分拣系统完成。");
      conn.commit();
      conn.close();
      out.write("success");
      out.close();
    } catch (Exception ex) { ex.printStackTrace();
      //System.out.println("插入散货分拣系统遇到错误:" + ex);
    }
    out.write("error");
    out.close();
  }
  
  @RequestMapping({"/excelOfMerchant"})
  public ModelAndView excelOfMerchant()
  {
    ModelAndView mv = getModelAndView();
    PageData pd = new PageData();
    pd = getPageData();
    try
    {
      Map<String, Object> dataMap = new HashMap<String, Object>();
      List<String> titles = new ArrayList<String>();
      String searchcriteria = pd.getString("searchcriteria");
      if(searchcriteria.equals("5")||searchcriteria.equals("6")){
    	  titles.add("便利店名称");
          titles.add("对应储位");
      }else{
    	  titles.add("商品条码");
          titles.add("商品名称");
          titles.add("商品数量");
          titles.add("分拣状态");
          titles.add("所属便利店");
          titles.add("门店储位编号");
          titles.add("扫描时间");
      }
      dataMap.put("titles", titles);
     
      List<PageData> varList = new ArrayList<PageData>();

      List<PageData> pds = new ArrayList<PageData>();
      if(pd.getString("keyword").equals("undefined")){
    	  pd.put("keyword","");
      }
      if (!StringUtil.isEmpty(searchcriteria)) {
        pds =picturesService.excelOfMerchant(pd);
      }
      
      for(PageData mer:pds){
    	  PageData vpd = new PageData();
    	  if(searchcriteria.equals("5")||searchcriteria.equals("6")){
    		  vpd.put("var1",mer.getString("shopname"));
    		  vpd.put("var2", mer.getString("location"));
    	  }else{
    		  vpd.put("var1",mer.getString("barcode"));
    		  vpd.put("var2",mer.getString("goodsname"));
    		  vpd.put("var3", mer.getString("quantity"));
    		  if(mer.getString("state").equals("0")){
    			  vpd.put("var4", "未分拣");
    		  }else{
    			  vpd.put("var4", "已分拣");
    		  }
    		  vpd.put("var5", mer.getString("shopname"));
    		  vpd.put("var6", mer.getString("location"));
    		  vpd.put("var7", mer.getString("oprationtime"));
    	  }
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


