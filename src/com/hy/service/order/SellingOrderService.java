package com.hy.service.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.util.logging.resources.logging;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.ENOrderItem;
import com.hy.entity.order.EXOrder;
import com.hy.entity.order.EXOrderItem;
import com.hy.entity.order.OrderGroup;
import com.hy.entity.order.PurchaseOrder;
import com.hy.entity.order.SellingOrder;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.Product;
import com.hy.entity.product.Supplier;
import com.hy.entity.system.User;
import com.hy.service.product.MerchantService;
import com.hy.service.product.ProductService;
import com.hy.service.product.SupplierService;
import com.hy.service.system.syslog.SysLogService;
import com.hy.util.DateUtil;
import com.hy.util.DoubleUtil;
import com.hy.util.Logger;
import com.hy.util.LoginUtil;
import com.hy.util.MathUtil;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import com.hy.util.Tools;

@Service("sellingOrderService")
public class SellingOrderService
{
  @Resource(name="daoSupport")
  private DaoSupport dao;
  @Resource(name="sellingOrderItemService")
  private SellingOrderItemService sellingOrderItemService;
  @Resource(name="eNOrderService")
  private ENOrderService eNOrderService;
  @Resource(name="eNOrderItemService")
  private ENOrderItemService eNOrderItemService;
  @Resource(name="eXOrderService")
  private EXOrderService eXOrderService;
  @Resource(name="eXOrderItemService")
  private EXOrderItemService eXOrderItemService;
  @Resource(name="purchaseOrderSerivce")
  private PurchaseOrderSerivce purchaseOrderSerivce;
  @Resource(name="purchaseOrderItemService")
  private PurchaseOrderItemService purchaseOrderItemService;
  @Resource(name="supplierService")
  private SupplierService supplierService;
  @Resource(name="sysLogService")
  private SysLogService sysLogService;
  @Resource(name="productService")
  private ProductService productService;
  @Resource(name="merchantService")
  private MerchantService merchantService;
  @Resource(name="orderGroupService")
  private OrderGroupService orderGroupService;
  @Resource(name="SitestatService")
  private SitestatisticsService sitestatisticsService;
  @Resource(name="enwarehouseorderService")
  private EnWarehouseOrderService enwarehouseorderService;
	protected Logger logger = Logger.getLogger(getClass());
  
  public List<PageData> dqexcel(PageData pd)throws Exception {
	    return (List)this.dao.findForList("SellingOrderMapper.dqexcel", pd);
	  }
  
  public List<PageData> sellingorderdq(Page page)throws Exception {
	    return (List)this.dao.findForList("SellingOrderMapper.sellingorderdqlistPage", page);
	  }
  
  public List<PageData> listPdPagePurchaseOrder(Page page)throws Exception {
    return (List)this.dao.findForList("SellingOrderMapper.listPage", page);
  }
  public List<PageData> listPdPagePurchaseGroupOrder(Page page)throws Exception {
	    return (List)this.dao.findForList("SellingOrderMapper.GrouplistPage", page);
	  }
  public String saverankings(List<PageData> page){
	  String ret="导入成功！";
	  try {
		  dao.save("ProductMapper.saverankings", page);
	} catch (Exception e) {
		ret="导入失败";
	}
	  return ret;
  }
  
  
  public int saveList(List<SellingOrder> listSellingOrder)
    throws Exception
  {
    Object ob = this.dao.batchSave("SellingOrderMapper.saveSellingOrder", listSellingOrder);
    return Integer.parseInt(ob.toString());
  }
  
  public int save(PageData pd)
    throws Exception
  {
    Object ob = this.dao.save("SellingOrderMapper.save", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public int delete(PageData pd)
    throws Exception
  {
    Object ob = this.dao.delete("SellingOrderMapper.delete", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public int deleteList(List<SellingOrder> listSellingOrder)
    throws Exception
  {
    Object ob = this.dao.batchDelete("SellingOrderMapper.deleteList", listSellingOrder);
    return Integer.parseInt(ob.toString());
  }
  
  public int edit(PageData pd)
    throws Exception
  {
    Object ob = this.dao.update("SellingOrderMapper.edit", pd);
    return Integer.parseInt(ob.toString());
  }
  
  public List<PageData> listAll(PageData pd)
    throws Exception
  {
    return (List)this.dao.findForList("SellingOrderMapper.listAll", pd);
  }
  
  /**
   * 根据销售单码查询订单详情
   * @param pd
   * @return
   * @throws Exception
   */
  public List<PageData> findorderitem(PageData pd)
		    throws Exception
		  {
		    return (List)this.dao.findForList("SellingOrderMapper.findorderitem", pd);
  }
  /**
   * 根据销售单码查询订单详情
   * @param pd
   * @return
   * @throws Exception
   */
  public List<PageData> findMDorderitem(PageData pd)
		    throws Exception
		  {
		    return (List)this.dao.findForList("SellingOrderMapper.findMDorderitem", pd);
  }
  /**
   * 批量更新销售订单状态
   * @param list
   * @throws Exception
   */
  public void updateBatch(List list)
		    throws Exception
		  {
		    this.dao.update("SellingOrderMapper.updateBatch",list );
}
  /**
   * 批量更新销售订单状态
   * @param list
   * @throws Exception
   */
  public void updateBatch2(List list)
		    throws Exception
		  {
		    this.dao.update("SellingOrderMapper.updateBatch2",list );
}
  /**
   * 根据门店id查询订单基本信息
   * @param pd
   * @return
   * @throws Exception
   */
  public Object findMDorder(PageData pd)
		    throws Exception
		  {
		    return this.dao.findForObject("SellingOrderMapper.findMDorder", pd);
  }
  
  /**
   * 根据销售单码查询订单基本信息
   * @param pd
   * @return
   * @throws Exception
   */
  public Object findorder(PageData pd)
		    throws Exception
		  {
		    return this.dao.findForObject("SellingOrderMapper.findorder", pd);
  }
  public PurchaseOrder findById(PageData pd)
    throws Exception
  {
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    return (PurchaseOrder)this.dao.findForObject("SellingOrderMapper.findById", pd);
  }
  
  public int deleteAll(String[] ArrayDATA_IDS)
    throws Exception
  {
    Object ob = this.dao.delete("SellingOrderMapper.deleteAll", ArrayDATA_IDS);
    return Integer.parseInt(ob.toString());
  }
  /**
   * 根据group后的销售订单id 查询这些门店下的未审核销售订单id
   * @param ids group后的销售订单id
   * @return  id 集合
   * @throws Exception
   */
  public List findOrderById(PageData pd) throws Exception{
	  return  (List) dao.findForList("SellingOrderMapper.findOrderById", pd);
  }
  /**
   * 修改订单数量
   * @param pd
   * @return
   * @throws Exception
   */
  public String  editorderitem(PageData pd) throws Exception{
	  String msg = "false";
	  List list = new ArrayList<>();
	  String data = pd.getString("data");
	  JSONArray array = JSONArray.fromObject(data);
	  List<String> idlist = new ArrayList<String>();
	  List<String> quantitylist = new ArrayList<String>();
	  if(array.size() > 0 ){
		  for (int i = 0; i < array.size(); i++) {
			  JSONObject job = array.getJSONObject(i);
			  String obj = job.getString("name");
			 
			  if(obj.equals("id")){
				  String id = job.getString("value");
				  idlist.add(id);  
			  }
			  if(obj.equals("quantity")){
				  String  quantity = job.getString("value");
				  quantitylist.add(quantity);
			  }
		}
	  }
	  if(idlist.size() > 0 && quantitylist.size() > 0 && idlist.size() == quantitylist.size()){
		 for (int i = 0; i < idlist.size(); i++) {
			PageData pda= new PageData();
			pda.put("id", idlist.get(i));
			pda.put("quantity", quantitylist.get(i));
			dao.update("SellingOrderMapper.editorderitem", pda);
			msg = "true";
		}
	  }
	  
	  return msg;
  }
  @Transactional
  public String readExcel(List<PageData> listPd){
	  
	 try {
		 User user = LoginUtil.getLoginUser();
		 List<ENOrder> listENOrder = new ArrayList<ENOrder>();
	     List<ENOrderItem> listENOrderItem = new ArrayList<ENOrderItem>();
	     List<EXOrder> listEXOrder = new ArrayList<EXOrder>();
	     List<EXOrderItem> listEXOrderItem = new ArrayList<EXOrderItem>();
	     List<Product> listProduct = new ArrayList<Product>();
	     List<PageData> pdPriceList = new ArrayList<PageData>();
	     List<PageData> sizeItem=new ArrayList<PageData>();
	     List<PageData> addSize=new ArrayList<PageData>();//添加集合
	     List<PageData> upSize=new ArrayList<PageData>();//修改集合
	     List<PageData> addSizeItem=new ArrayList<PageData>();
	     
	     String groupNum = "GP_" + StringUtil.getStringOfMillisecond("");
	     OrderGroup og = new OrderGroup();
	     og.setOrderGroupNum(groupNum);
	     og.setUser(user);
	     og.setCkId(user.getCkId());
	     og.setGroupType(1);
	     for (int i = 0; i < listPd.size(); i++){
	    	 PageData pdM = new PageData();
	    	 Product product = new Product();
	    	 if(listPd.get(i).getString("var0").trim().equals("")){
	    		  throw new RuntimeException("数据表中有空行，在"+Integer.valueOf(i + 2));
	    	 }
	    	 if (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var2").toString())) {
		            pdM.put("id", ((PageData)listPd.get(i)).get("var2").toString());
		            if(null==this.merchantService.findById(pdM)){
		            	throw new RuntimeException("商户便利店信息不存在，在"+Integer.valueOf(i + 2)+"行");
		            }	
		            pdM.put("id","");
	    	 }
	    	 if ((!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var10").toString())) && 
			            (!StringUtil.isEmpty(((PageData)listPd.get(i)).get("var8").toString()))) {
			     pdM.put("barCode", ((PageData)listPd.get(i)).get("var10").toString());
			     pdM.put("hostCode", ((PageData)listPd.get(i)).get("var8").toString());
			     product = this.productService.findById(pdM);
			     if (product != null) {	    	
			          if (StringUtil.isEmpty(product.getSkuVolume())) {
			        	  throw new RuntimeException("商品没有体积，在"+Integer.valueOf(i + 2)+"行"); 
			          }
			          if (StringUtil.isEmpty(product.getSkuWeight())) {
			        	  throw new RuntimeException("商品没有重量，在"+Integer.valueOf(i + 2)+"行"); 
			          }
			          PageData pdPrice = new PageData();
			          pdPrice.put("product_id", Long.valueOf(product.getId()));
			          pdPrice.put("price_type", Integer.valueOf(1));
			          pdPriceList = this.productService.findPriceOfProductList(pdPrice);//采购价格
			          pdPrice = (PageData)pdPriceList.get(0);
			          if ((pdPrice== null) && (pdPrice.getString("product_price")== null)) {
			        	  throw new RuntimeException("商品没有价格信息，在"+Integer.valueOf(i + 2)+"行");
			          }
			     } else{
			    	 throw new RuntimeException("商品不存在，在"+Integer.valueOf(i + 2)+"行");}
			 } else {
				 throw new RuntimeException("商品数据为空，在"+Integer.valueOf(i + 2)+"行"); }
	    	 if ((((PageData)listPd.get(i)).get("var17") == null) && (((PageData)listPd.get(i)).get("var17") == "") &&
					 (StringUtil.isEmpty(((PageData)listPd.get(i)).get("var17").toString()))) {
	    		 throw new RuntimeException("商品订购数量为空，在"+Integer.valueOf(i + 2)+"行"); 
		    } 
	     }
	     //获取门店库存信息
	     sizeItem=sitestatisticsService.findsitestat();
		 for (int i = 0; i < listPd.size(); i++){
			 /**初始化**/
			 EXOrder exo = new EXOrder();
			 EXOrderItem exoi =new EXOrderItem();
			 PageData pdPrice =new PageData();
			 Merchant merchant =new Merchant();
			 Product product = new Product();
			 PageData pdM = new PageData();
			 PageData pds=new PageData();
			 double svolume=0;//体积
			 double weight=0;//重量
			 double purchase_price=0;//采购价格
			 double sale_price=0;//销售价格
			 double quantity=0; //数量
		     pdM.put("id", ((PageData)listPd.get(i)).get("var2").toString());
		     pds = this.merchantService.findById(pdM);
		     pdM.put("id","");
		     if (!StringUtil.isEmpty(pds.getString("contact_person"))) {
		         exo.setManagerName(pds.getString("contact_person"));
		     }
		     if (!StringUtil.isEmpty(pds.getString("mobile"))) {
		         exo.setManagerTel(pds.getString("mobile"));
		     }
		     if (!StringUtil.isEmpty(pds.getString("address"))) {
		         exo.setDeliverAddress(pds.getString("address"));
		     }
		     merchant.setId(Long.parseLong(pds.get("id").toString()));
			
			 pdM.put("barCode", ((PageData)listPd.get(i)).get("var10").toString());
			 pdM.put("hostCode", ((PageData)listPd.get(i)).get("var8").toString());
			 product = this.productService.findById(pdM);
			 if (product != null) {	    	
			    svolume = Double.parseDouble(product.getSkuVolume());
			    weight = Double.parseDouble(product.getSkuWeight());
			    pdPrice = new PageData();
			    pdPrice.put("product_id", Long.valueOf(product.getId()));
			    pdPrice.put("price_type", Integer.valueOf(1));
			    pdPriceList = this.productService.findPriceOfProductList(pdPrice);//采购价格
			    pdPrice = (PageData)pdPriceList.get(0);
			    if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
			         purchase_price = Double.parseDouble(pdPrice.getString("product_price").toString());
			         pdPrice.put("product_id", Long.valueOf(product.getId()));
			         pdPrice.put("price_type", Integer.valueOf(2));
			         pdPriceList = this.productService.findPriceOfProductList(pdPrice);
			         pdPrice = (PageData)pdPriceList.get(0);
			         if ((pdPrice != null) && (pdPrice.getString("product_price") != null)) {
			             sale_price = Double.parseDouble(pdPrice.getString("product_price").toString());
			         }
			    }
			    int vvt=-1;
			    if(listProduct.size()>0){
			       for(int vv=0;vv<listProduct.size();vv++){
			    	   if(listProduct.get(vv).getId()==product.getId()){
			    		   vvt=vv;
			    	   }
			       }
			    }
			    if(vvt==-1){
		        	listProduct.add(product);
		        }
			 }
			 String comment = "";
			 if (((PageData)listPd.get(i)).get("var20") != null) {
		         comment = ((PageData)listPd.get(i)).get("var20").toString();
		     }
			 String zyOrderNum="";
			 if (((PageData)listPd.get(i)).get("var21") != null) {
		         zyOrderNum = ((PageData)listPd.get(i)).get("var21").toString();
		     }
			 int k=-1;
			 int u=-1;
			 //数量				
			 if ((listEXOrder != null) && (listEXOrder.size() > 0)) {
	               for (int m = 0; m < listEXOrder.size(); m++) {//循环出库单数组 比对是否存在
	                    if (((EXOrder)listEXOrder.get(m)).getMerchant().getId() == merchant.getId()) {
	                    	k=m;
	                      ((EXOrder)listEXOrder.get(m)).setAmount(MathUtil.mulAndAdd(Double.parseDouble(((PageData)listPd.get(i)).get("var17").toString()), 
	                              sale_price,((EXOrder)listEXOrder.get(m)).getFinalAmount()));
	                      ((EXOrder)listEXOrder.get(m)).setFinalAmount(((EXOrder)listEXOrder.get(m)).getAmount());
	                      ((EXOrder)listEXOrder.get(m)).setTotalSvolume(MathUtil.mulAndAdd(Double.valueOf((listPd.get(i)).getString("var17")), svolume, ((EXOrder)listEXOrder.get(m)).getTotalSvolume()));
	                      ((EXOrder)listEXOrder.get(m)).setTotalWeight(MathUtil.mulAndAdd(Double.valueOf((listPd.get(i)).getString("var17")), weight, ((EXOrder)listEXOrder.get(m)).getTotalWeight()));
	                      ((EXOrder)listEXOrder.get(m)).setComment(comment);
	                      exo=listEXOrder.get(m);
	                      break;
	                    }
	                  }
	         }
			 if(k!=-1){
					 for (int ex = 0; ex < listEXOrderItem.size(); ex++) {  //根据订单单号与出库单明细商品id确定是否有重复商品
 		                 if ((listEXOrder.get(k).getOrderNum() == ((EXOrderItem)listEXOrderItem.get(ex)).getOrderNum()) && 
 		                      (((EXOrderItem)listEXOrderItem.get(ex)).getProduct().getId() == product.getId())){
 		                	 u=ex;
 		                	 quantity=listEXOrderItem.get(ex).getQuantity()+Double.parseDouble(((PageData)listPd.get(i)).getString("var17"));
 		                	 listEXOrderItem.get(ex).setQuantity(quantity);
 		                	 listEXOrderItem.get(ex).setFinalQuantity(quantity);
 		                	  if (((PageData)listPd.get(i)).get("var20") != null) {
 		                          ((EXOrderItem)listEXOrderItem.get(ex)).setComment(((EXOrderItem)listEXOrderItem.get(ex)).getComment() + ";" + comment);
 		                      }
 		                      if (((PageData)listPd.get(i)).get("var21") != null) {
 		                        ((EXOrderItem)listEXOrderItem.get(ex)).setZyOrderNum(((EXOrderItem)listEXOrderItem.get(ex)).getZyOrderNum() + ";" + zyOrderNum);
 		                      }
 		                     break;
 		                 }
 		             }
			}
			if(u==-1){
				quantity = Double.parseDouble(((PageData)listPd.get(i)).get("var17").toString());
			}
		    if(k==-1){
				 exo.setGroupNum(groupNum);
	             exo.setOrderNum("CK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
	             exo.setCheckedState(1);
	             exo.setOrderDate(Tools.date2Str(new Date()));
	             exo.setIs_ivt_order_print(0);
	             exo.setIs_temporary(2);
	             exo.setUser(user);
	             exo.setMerchant(merchant);
	             exo.setOrderType(1);
	             exo.setComment(comment);
	             exo.setCkId(user.getCkId());
	             exo.setFinalAmount(MathUtil.mul(quantity,sale_price));
	             exo.setAmount(MathUtil.mul(quantity,sale_price));
	             exo.setTotalSvolume(MathUtil.mul(quantity, svolume));
	             exo.setTotalWeight(MathUtil.mul(quantity, weight));
	             listEXOrder.add(exo);
			 }
			 if(u==-1){
				 exoi.setGroupNum(exo.getGroupNum());
	             exoi.setOrderNum(exo.getOrderNum());
	             exoi.setPurchasePrice(purchase_price);
	             exoi.setSalePrice(sale_price);
	             exoi.seteXTime(DateUtil.getAfterDayDate("3"));
	             exoi.setProduct(product);
	             exoi.setCreator(user.getNAME());
	             exoi.setZyOrderNum(zyOrderNum);
	             if (((PageData)listPd.get(i)).get("var20") != null) {
	               exoi.setComment(((PageData)listPd.get(i)).get("var20").toString());
	             }
	             exoi.setProduct(product);
	             exoi.setQuantity(quantity);
	             exoi.setFinalQuantity(quantity);
	             exoi.setFc_order_num(((PageData)listPd.get(i)).getString("var1"));
	             listEXOrderItem.add(exoi);
			 }
			 /***添加库存历史记录***/
			 PageData prSite=new PageData();
        	 prSite.put("merchant_id", merchant.getId());
        	 prSite.put("product_id",product.getId());
        	 int tr=-1;
        	 int utr=-1;
        	 for(int jj=0;jj<sizeItem.size();jj++){
        		 PageData si=sizeItem.get(jj);
        		 if(prSite.getString("merchant_id").equals(si.getString("merchant_id"))&&
        				 prSite.getString("product_id").equals(si.getString("product_id"))){
        			 tr=jj;
        		 }
        	 }
        	 //获取更新列表中的下标
        	 for(int kk=0;kk<upSize.size();kk++){
        		 PageData si=upSize.get(kk);
        		 if(prSite.getString("merchant_id").equals(si.getString("merchant_id"))&&
        				 prSite.getString("product_id").equals(si.getString("product_id"))){
        			 utr=kk;
        		 }
        	 }
        	 if(tr>-1){//修改
        		 if(utr>-1){
        			 prSite=upSize.get(utr);//获取更新列表数据
            		 if(prSite.getString("type").equals("1")){
            			 double inventory=Double.valueOf(prSite.getString("inventory"));
                    	 prSite.put("inventorys", exoi.getQuantity());
                    	 prSite.put("state", 1);
                    	 prSite.put("group_num", groupNum);
                    	 addSizeItem.add(prSite);
                    	 //sitestatisticsService.saveSiteItem(prSite);
                    	 upSize.get(utr).put("inventory",DoubleUtil.add(inventory,exoi.getQuantity()));
                    	 //prSite.put("inventory",DoubleUtil.add(inventory,exoi.getQuantity()));
                    	 //addSize.add(prSite);
                    	 //sitestatisticsService.updateSiteInventory(prSite);
            		 }else{
            			 double inventory=Double.valueOf(prSite.getString("inventory"));
                    	 prSite.put("inventorys", exoi.getQuantity());
                    	 prSite.put("state",2);
                    	 prSite.put("group_num", groupNum);
                    	 addSizeItem.add(prSite);
                    	 //sitestatisticsService.saveSiteItem(prSite);
                    	 upSize.get(utr).put("inventory",DoubleUtil.sub(inventory,exoi.getQuantity()));
                    	 //prSite.put("inventory",DoubleUtil.sub(inventory,exoi.getQuantity()));
                    	 //sitestatisticsService.updateSiteInventory(prSite);
            		 }
        		 }else{
        			 prSite=sizeItem.get(tr);//获取
            		 if(prSite.getString("type").equals("1")){
            			 double inventory=Double.valueOf(prSite.getString("inventory"));
                    	 prSite.put("inventorys", exoi.getQuantity());
                    	 prSite.put("state", 1);
                    	 prSite.put("group_num", groupNum);
                    	 addSizeItem.add(prSite);
                    	 //sitestatisticsService.saveSiteItem(prSite);
                    	 prSite.put("inventory",DoubleUtil.add(inventory,exoi.getQuantity()));
                    	 upSize.add(prSite);
                    	 //sitestatisticsService.updateSiteInventory(prSite);
            		 }else{
            			 double inventory=Double.valueOf(prSite.getString("inventory"));
                    	 prSite.put("inventorys", exoi.getQuantity());
                    	 prSite.put("state",2);
                    	 prSite.put("group_num", groupNum);
                    	// //System.out.println(prSite.getString("inventory"));
                    	 addSizeItem.add(prSite);
                    	 //sitestatisticsService.saveSiteItem(prSite);
                    	 prSite.put("inventory",DoubleUtil.sub(inventory,exoi.getQuantity()));
                    	 upSize.add(prSite);
                    	 //sitestatisticsService.updateSiteInventory(prSite);
            		 }
        		 }
        	 }else{//新增
        		 prSite=new PageData();
        		 prSite.put("merchant_id", merchant.getId());
            	 prSite.put("product_id",product.getId());
            	 prSite.put("inventory", exoi.getQuantity());
            	 prSite.put("state", 1);
            	 addSize.add(prSite);
            	 prSite.put("state", 1);
            	 prSite.put("group_num", groupNum);
            	 prSite.put("inventorys", exoi.getQuantity());
            	 addSizeItem.add(prSite);
        	 }
        	 
        	 
        	 
        	 /*prSite=(PageData) sitestatisticsService.findsitebyid(prSite);
        	 if(prSite==null){ 
        		 prSite=new PageData();
        		 prSite.put("merchant_id", merchant.getId());
            	 prSite.put("product_id",product.getId());
            	 prSite.put("inventory", exoi.getQuantity());
            	 prSite.put("state", 1);
            	 sitestatisticsService.saveSite(prSite);
            	 prSite.put("state", 1);
            	 prSite.put("group_num", groupNum);
            	 sitestatisticsService.saveSiteItem(prSite);
        	 }else{
        		 if(prSite.getString("type").equals("1")){
        			 double inventory=Double.valueOf(prSite.getString("inventory"));
                	 prSite.put("inventory", exoi.getQuantity());
                	 prSite.put("state", 1);
                	 prSite.put("group_num", groupNum);
                	 sitestatisticsService.saveSiteItem(prSite);
                	 prSite.put("inventory",DoubleUtil.add(inventory,exoi.getQuantity()));
                	 sitestatisticsService.updateSiteInventory(prSite);
        		 }else{
        			 double inventory=Double.valueOf(prSite.getString("inventory"));
                	 prSite.put("inventory", exoi.getQuantity());
                	 prSite.put("state",2);
                	 prSite.put("group_num", groupNum);
                	 sitestatisticsService.saveSiteItem(prSite);
                	 prSite.put("inventory",DoubleUtil.sub(inventory,exoi.getQuantity()));
                	 sitestatisticsService.updateSiteInventory(prSite);
        		 }
        	 }*/
		 }  
		 if(listEXOrderItem.size()>0){
			 for (int pi = 0; pi < listProduct.size(); pi++) {
				 ENOrder eno = new ENOrder();
				 ENOrderItem enoi =new ENOrderItem();
				 Supplier supplier = new Supplier();
				 PageData pdP = new PageData();
				 double purchase_price=0;//采购价格
				 double quantity=0; //数量
				 for (int exoii = 0; exoii < listEXOrderItem.size(); exoii++) {
		            if (((Product)listProduct.get(pi)).getId() == ((EXOrderItem)listEXOrderItem.get(exoii)).getProduct().getId()) {
		                quantity += ((EXOrderItem)listEXOrderItem.get(exoii)).getQuantity();
		                purchase_price = ((EXOrderItem)listEXOrderItem.get(exoii)).getPurchasePrice();
		            }
		         }
				  pdP = new PageData();
	              pdP = this.productService.findProductInfoById(((Product)listProduct.get(pi)).getId());
	              int enk = -1;
	              if ((listENOrder != null) && (listENOrder.size() > 0)) {
	                for (int en = 0; en < listENOrder.size(); en++) {
	                  if ((((ENOrder)listENOrder.get(en)).getSupplier().getId() == 42873290L) && (pdP == null)) {
	                    ////System.out.println("");
	                  }
	                  if (Long.parseLong(pdP.getString("tsId")) == ((ENOrder)listENOrder.get(en)).getSupplier().getId())
	                  {
	                    eno = (ENOrder)listENOrder.get(en);
	                    enk = en;
	                    ((ENOrder)listENOrder.get(enk)).setFinalAmount(MathUtil.mulAndAdd(quantity, purchase_price, ((ENOrder)listENOrder.get(enk)).getFinalAmount()));
	                    ((ENOrder)listENOrder.get(enk)).setAmount(MathUtil.mulAndAdd(quantity, purchase_price, ((ENOrder)listENOrder.get(enk)).getFinalAmount()));
	                    ((ENOrder)listENOrder.get(enk)).setTotalSvolume(MathUtil.add(((ENOrder)listENOrder.get(enk)).getTotalSvolume(), Double.parseDouble(pdP.getString("sku_volume"))));
	                    ((ENOrder)listENOrder.get(enk)).setTotalWeight(MathUtil.add(((ENOrder)listENOrder.get(enk)).getTotalWeight(), Double.parseDouble(pdP.getString("sku_weight"))));
	                    break;
	                  }
	                }
	              }
	              if (enk == -1) {
	                  supplier.setId(Long.parseLong(pdP.getString("tsId")));
	                  eno.setGroupNum(groupNum);
	                  eno.setOrderNum("RK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber());
	                  eno.setOrderDate(Tools.date2Str(new Date()));
	                  eno.setCheckedState(1);
	                  eno.setManagerName(pdP.getString("contact_person"));
	                  eno.setManagerTel(pdP.getString("contact_person_mobile"));
	                  eno.setPaidAmount(0.0D);
	                  eno.setIsIvtOrderPrint(0);
	                  eno.setIsTemporary(2);
	                  eno.setSupplier(supplier);
	                  eno.setUser(user);
	                  eno.setTotalSvolume(MathUtil.mul(Double.parseDouble(pdP.getString("sku_volume")), quantity));
	                  eno.setTotalWeight(MathUtil.mul(Double.parseDouble(pdP.getString("sku_weight")), quantity));
	                  eno.setOrderType(1);
	                  eno.setCkId(user.getCkId());
	                }
	              enoi.setGroupNum(groupNum);
	              enoi.setOrderNum(eno.getOrderNum());
	              enoi.setPurchasePrice(purchase_price);
	              enoi.setQuantity(quantity);
	              enoi.setFinalQuantity(quantity);
	              enoi.setIsSplitIvt(2);
	              enoi.setSvolume(MathUtil.mul(Double.parseDouble(pdP.getString("sku_volume")), quantity)+"");
	              enoi.setWeight(MathUtil.mul(Double.parseDouble(pdP.getString("sku_weight")), quantity)+"");
	              enoi.setIsIvtBK(1);
	              enoi.seteNTime(DateUtil.getAfterDayDate("3"));
	              enoi.setCreator(user.getNAME());
	              enoi.setProduct((Product)listProduct.get(pi));
	              listENOrderItem.add(enoi);
	              if (enk == -1)
	              {
	                eno.setAmount(MathUtil.mul(enoi.getPurchasePrice(), quantity));
	                eno.setFinalAmount(MathUtil.mul(enoi.getPurchasePrice(), quantity));
	                listENOrder.add(eno);
	              }
			 }
		 }else{
			 throw new RuntimeException("出错了！");
		 }
		 this.eXOrderService.saveList(listEXOrder);
		 this.eXOrderItemService.saveList(listEXOrderItem);
		 this.eNOrderItemService.saveList(listENOrderItem);
		 this.eNOrderService.saveList(listENOrder);
		 this.orderGroupService.saveOrderGroup(og);
		 sitestatisticsService.saveSiteItemList(addSizeItem);
		 if(addSize.size()>0){
			 sitestatisticsService.saveSiteList(addSize);
		 }
		 if(upSize.size()>0){
			 sitestatisticsService.updateSiteInventoryList(upSize);
		 }
		 dao.update("SitestatisticsMapper.updateSiteType",groupNum);
	} catch (Exception e) {
		e.printStackTrace();
		 throw new RuntimeException(e.toString());
	}
	  return "上传成功";
  }
  public PageData saveExOrder(PageData pdd){
	  PageData exOrder=new PageData();
	  exOrder.put("group_num", pdd.getString("group_num"));
	  exOrder.put("order_num", pdd.getString("orderNum"));
	  exOrder.put("checked_state", 1);
	  exOrder.put("merchant_id", pdd.getString("merchant_id"));
	  exOrder.put("manager_name", pdd.getString("manager_name"));
	  exOrder.put("manager_tel", pdd.getString("manager_tel"));
	  exOrder.put("deliver_address", pdd.getString("deliver_address"));
	  exOrder.put("comment", pdd.getString("comment"));
	  exOrder.put("final_amount",0);
	  exOrder.put("total_svolume", 0);
	  exOrder.put("total_weight", 0);
	  exOrder.put("paid_amount", 0);
	  exOrder.put("is_ivt_order_print", 0);
	  exOrder.put("is_temporary", 2);
	  exOrder.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
	  exOrder.put("is_order_print", 1);
	  exOrder.put("ivt_state", 1);
	  exOrder.put("amount", 0);
	  exOrder.put("order_type",1);
	  exOrder.put("ck_id", 1);
	  exOrder.put("type", pdd.getString("type"));
	  return exOrder;
  }
  public PageData saveExOrderItem(PageData pdd){
	  PageData exOrderItem=new PageData();
	  exOrderItem.put("group_num", pdd.getString("group_num"));
	  exOrderItem.put("order_num", pdd.getString("orderNum"));
	  exOrderItem.put("product_id", pdd.getString("product_id"));
	  exOrderItem.put("sale_price", pdd.getString("sale_price"));
	  exOrderItem.put("purchase_price", pdd.getString("purchase_price"));
	  exOrderItem.put("quantity", pdd.getString("quantity"));
	  exOrderItem.put("final_quantity", pdd.getString("final_quantity"));
	  String sku_volume = pdd.getString("sku_volume");
	  if(sku_volume == null || sku_volume.equals("")){
		  sku_volume = "0.00";
	  }
	  String sku_weight = pdd.getString("sku_weight");
	  if(sku_weight == null || sku_weight.equals("")){
		  sku_weight = "0.00";
	  }
	  exOrderItem.put("svolume", DoubleUtil.multiply(Double.valueOf(sku_volume), Double.valueOf(pdd.getString("final_quantity"))));
	  exOrderItem.put("weight", DoubleUtil.multiply(Double.valueOf(sku_weight), Double.valueOf(pdd.getString("final_quantity"))));
	  exOrderItem.put("creator", LoginUtil.getLoginUser().getUSERNAME());
	  exOrderItem.put("comment", pdd.getString("comment"));
	  exOrderItem.put("total_count", DoubleUtil.div(Double.valueOf(exOrderItem.getString("final_quantity")), Double.valueOf(pdd.getString("box_number")), 0));
	  exOrderItem.put("state",1);
	  exOrderItem.put("is_ivt_BK", 1);
	  exOrderItem.put("gift_quantity",pdd.getString("gift_quantity"));
	  return exOrderItem;
  }

	/**
	 * 销售订单审核-地市审核改变状态为1
	 * @param ids
	 * @throws ParseException
	 */
	@Transactional
	public String audit12(String  ids) {
		String result = "false";
		//根据销售单号 查询销售单详情
		if(ids!=null&&!"".equals(ids)&&ids.length() > 0){
			String[] idsS = null;
			idsS=ids.split(",");
			try {
				PageData sellorderitem =  null;
				for (int k = 0; k < idsS.length; k++) {
					sellorderitem =  new PageData();
					//System.out.println("ids.[k]="+idsS[k]);
					//sellorderitem.put("check_state", 1); //改变状态
					sellorderitem.put("id", idsS[k]);
					this.dao.update("SellingOrderMapper.editsell_order_check_state", sellorderitem);
					result = "true" ;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new  RuntimeException(e.toString());
			}
		}
		return result;
	}
  /**
   * 销售订单审核-省级审核
   * @param list
 * @throws ParseException 
   */
  @Transactional
  public String audit(List list){
	  
//	  int ex = 1/0;
	  
	String result = "false";
	  //根据销售单号 查询销售单详情
	  	if(list.size() > 0){
	  		//生成新的仓配批次号
	  		String groupNum = findGroupNum();
			PageData pd = null;
			for (int i = 0; i < list.size(); i++) {
				pd = new PageData();
	  		  pd.put("id", list.get(i));
	  		  PurchaseOrder order;
				try {
				    List<PageData> exorder=new ArrayList<PageData>();
					//根据销售单id 查询销售单
					order = this.findById(pd);
//					String orderNum= "CG_"+StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber();
//					PageData pdate = encapsulation_PurchaseOrder(order,orderNum); //封装采购单
					PageData sell= new PageData();
					sell.put("order_num", order.getOrderNum());
					sell=(PageData) dao.findForObject("SellingOrderMapper.findorder",sell);
					String orderNums= "CK"+sell.getString("order_num").substring(2);
		  			sell.put("orderNum", "CK"+sell.getString("order_num").substring(2));
		  			List<PageData> pexorder=(List<PageData>) dao.findForList("EXOrderMapper.findexordercz",sell);
		  			if(pexorder.size()>1){
		  				throw new RuntimeException(sell.getString("manager_name")+"该门店下有多个未审核的出库订单");
		  			}
		  			if(pexorder.size()==0){
		  				exorder.add(saveExOrder(sell));
		  				dao.save("EXOrderMapper.saveExOrderList", exorder);
		  			}
		  			if(pexorder.size()==1){
		  				orderNums=pexorder.get(0).getString("order_num");
		  			}
					if(order != null ){
			  			pd.put("order_num", order.getOrderNum());
			  			List<PageData> orderitem = (List<PageData>) this.dao.findForList("SellingOrderMapper.findorderitem", pd); //根据销售单查询销售单详情
			  			if(orderitem.size() > 0){
			  				double zje = 0.00;	//总金额	
			  				double ztj = 0.00;	//总体积
			  				double zzl = 0.00;	//总重量
			  				for (int j = 0; j < orderitem.size(); j++) {
			  					List<PageData> exorderitem=new ArrayList<PageData>();
			  					PageData pagedate = (PageData) orderitem.get(j);
			  					pagedate.put("orderNum",orderNums);
			  					/*if(orderitem.get(j).getString("product_id").equals("96123")){
			  						logger.error("-----错误商品数量96123"+orderitem.get(j).getString("order_num"));
			  					}*/
			  					PageData ite=(PageData) dao.findForObject("EXOrderMapper.findexitemcz", pagedate);
			  					if(null!=ite){
			  						ite.put("final_quantity",DoubleUtil.add(Double.valueOf(ite.getString("final_quantity")),Double.valueOf(pagedate.getString("final_quantity"))));
			  						ite.put("quantity",DoubleUtil.add(Double.valueOf(ite.getString("quantity")),Double.valueOf(pagedate.getString("quantity"))));
			  						ite.put("gift_quantity",DoubleUtil.add(Double.valueOf(ite.getString("gift_quantity")),Double.valueOf(pagedate.getString("gift_quantity"))));
			  						ite.put("total_count", DoubleUtil.div(Double.valueOf(ite.getString("final_quantity")), Double.valueOf(pagedate.getString("box_number")), 0));
			  						dao.update("EXOrderMapper.updateexitemcz",ite);
			  						dao.update("EXOrderMapper.updateExOrdercz", pagedate);
			  					}else{
			  						exorderitem.add(saveExOrderItem(pagedate));
			  						dao.save("EXOrderMapper.saveExOrderItemsupplier", exorderitem);
			  						dao.update("EXOrderMapper.updateExOrdercz", pagedate);
			  					}
			  					 //根据商品条形码查询供货商等信息
			  					String barcode = pagedate.getString("bar_code");
			  					String gift_quantity =  pagedate.getString("gift_quantity");
			  					double quantity = Double.parseDouble(pagedate.getString("quantity"));
			  					pagedate.put("quantity", quantity);
			  					PageData pgd = new PageData();
			  					pgd.put("bar_code", barcode);
			  					PageData pdobject = (PageData) dao.findForObject("SellingOrderMapper.findsupplier", pgd);
			  					if(pdobject != null){
//			  						PageData purchaseorderitem = encapsulation_PurchaseOrderItem(pagedate,orderNum);  //封装采购单详情
//			  						purchaseorderitem.put("product_id", pdobject.getString("product_id"));	//商品id
//		  							purchaseorderitem.put("purchase_price", pdobject.getString("product_price"));	//价格
//		  							purchaseorderitem.put("svolume", pdobject.getString("sku_volume")); //体积
//		  							purchaseorderitem.put("weight", pdobject.getString("sku_weight"));	//重量
			  						//根据供货商id 查询是否存在同一供货商未审核的采购单
			  						String supid = pdobject.getString("id"); //供货商id
			  						pdobject.put("supid", supid);
			  					//	pdate.put("supplier_id", supid); 
			  						PageData purchaseorder = (PageData) dao.findForObject("SellingOrderMapper.findpurchaseorder", pdobject);
			  						//如果存在则更新采购单详情数量
			  						if(purchaseorder != null){
			  							//更新采购单
			  							//体积（商品单个体积 * 订购数量）
				  						ztj = Double.parseDouble(purchaseorder.getString("total_svolume")) + Double.parseDouble(pdobject.getString("sku_volume")) * quantity ;
				  						//金额 (商品单价 * 订购数量)
				  						double order_amount = Double.parseDouble(purchaseorder.getString("order_amount"));
				  						double product_price = Double.parseDouble(pdobject.getString("product_price"));
				  						double zj = DoubleUtil.multiply(product_price, quantity);
				  						zje =  DoubleUtil.add(zj,order_amount);
				  						//zje = Double.parseDouble(purchaseorder.getString("order_amount")) + Double.parseDouble(pdobject.getString("product_price")) * quantity;
				  						//重量 (商品单个重量 * 订购数量)
				  						zzl = Double.parseDouble(purchaseorder.getString("total_weight")) + Double.parseDouble(pdobject.getString("sku_weight")) * quantity;
				  						PageData pdate = new PageData();
				  						pdate.put("order_amount", zje); //总金额
			  							pdate.put("total_svolume", ztj); //总体积
			  							pdate.put("total_weight", zzl); //总重量
			  							pdate.put("id", purchaseorder.getString("id"));
			  							dao.update("SellingOrderMapper.updatepurchase_order", pdate);
			  							//根据采购单num 查询采购单详情
			  							String order_num = purchaseorder.getString("order_num");
			  							String product_id = pdobject.getString("product_id");
			  							PageData newpd = new PageData();
			  							newpd.put("order_num", order_num);
			  							newpd.put("product_id", product_id);
			  							PageData purchase_order_item = (PageData) dao.findForObject("SellingOrderMapper.findpurchase_order_item", newpd);
			  							if(purchase_order_item != null){
			  								for (int k = 0; k < purchase_order_item.size(); k++) {
			  									PageData purchaseorderitem =  new PageData();
			  									purchaseorderitem.put("suggest_quantity",String.format("%.2f", Double.parseDouble(purchase_order_item.getString("suggest_quantity")) + quantity)); //建议订购数
		  										purchaseorderitem.put("quantity", String.format("%.2f", Double.parseDouble(purchase_order_item.getString("quantity")) + quantity));	//订单数量
		  										purchaseorderitem.put("final_quantity", String.format("%.2f", Double.parseDouble(purchase_order_item.getString("final_quantity")) + quantity)); //入库数量
		  										purchaseorderitem.put("gift_quantity", String.format("%.2f", Double.parseDouble(purchase_order_item.getString("gift_quantity")) + Double.parseDouble(gift_quantity))); //赠品数量
		  										purchaseorderitem.put("id", purchase_order_item.getString("id"));
		  										dao.update("SellingOrderMapper.editpurchase_order_item", purchaseorderitem);
											}
			  							}else{
				  							//生成采购单详情
			  								String orderNum = purchaseorder.getString("order_num");
			  								String groupnum = purchaseorder.getString("group_num");
			  								PageData purchaseorderitem = encapsulation_PurchaseOrderItem(pagedate,orderNum,groupNum);  //封装采购单详情
			  								purchaseorderitem.put("product_id", pdobject.getString("product_id"));	//商品id
				  							purchaseorderitem.put("purchase_price", pdobject.getString("product_price"));	//价格
				  							purchaseorderitem.put("svolume", pdobject.getString("sku_volume")); //体积
				  							purchaseorderitem.put("weight", pdobject.getString("sku_weight"));	//重量
				  							dao.save("PurchaseOrderItemMapper.save", purchaseorderitem);
			  							}
			  						}else{
			  							String orderNum= "CG_"+StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber();
			  							PageData pdate = encapsulation_PurchaseOrder(order,orderNum,groupNum); //封装采购单
			  							pdate.put("supplier_id", supid); 
			  							
			  							PageData purchaseorderitem = encapsulation_PurchaseOrderItem(pagedate,orderNum,groupNum);  //封装采购单详情
				  						purchaseorderitem.put("product_id", pdobject.getString("product_id"));	//商品id
			  							purchaseorderitem.put("purchase_price", pdobject.getString("product_price"));	//价格
			  							purchaseorderitem.put("svolume", pdobject.getString("sku_volume")); //体积
			  							purchaseorderitem.put("weight", pdobject.getString("sku_weight"));	//重量
			  							//如果没有则新增采购单和采购单详情
			  							pdate.put("supplier_id", supid);	//供应商id
			  							pdate.put("deliver_address", "太原市清徐县赵家堡工业园（联坤仓储物流园）");//配送地址
			  							pdate.put("manager_name",  pdobject.getString("contact_person"));	//供应商联系人
			  							pdate.put("manager_tel",  pdobject.getString("supplier_tel"));	//供应商电话
			  							//生成采购单详情
			  							dao.save("PurchaseOrderItemMapper.save", purchaseorderitem);
			  							//生成采购单
			  							//体积（商品单个体积 * 订购数量）
				  						ztj = Double.parseDouble(pdobject.getString("sku_volume")) * quantity ;
				  						//金额 (商品单价 * 订购数量)
				  						
				  						//zje = Double.parseDouble(pdobject.getString("product_price")) * quantity;
				  						zje = DoubleUtil.multiply(Double.parseDouble(pdobject.getString("product_price")),quantity);
				  						//重量 (商品单个重量 * 订购数量)
				  						zzl = Double.parseDouble(pdobject.getString("sku_weight")) * quantity;
				  						pdate.put("order_amount", zje); //总金额
			  							pdate.put("total_svolume", ztj); //总体积
			  							pdate.put("total_weight", zzl); //总重量
				  						dao.save("PurchaseOrderMapper.save", pdate);
			  						}
			  					}else{
			  						//System.out.println("===========该商品没有供应商！==============");
			  					}
							}
			  				
			  			}else{
			  				//System.out.println("========该销售单详情为空=============");
			  			}
			  		  }
					result = "true";
				} catch (Exception e) {
					e.printStackTrace();
					throw new  RuntimeException(e.toString());
				}
			}
	  		
	  		try {
				this.updateBatch(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
	  	}
	  	return result;
  }
  /**
   * 封装采购单
   * @param order
   * @return
   */
  public PageData encapsulation_PurchaseOrder(PurchaseOrder order,String orderNum,String groupNum){
	  PageData pd = new PageData();
	  SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd");
	  SimpleDateFormat fat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	  pd.put("group_num", groupNum);
	  pd.put("order_num", orderNum);
	  pd.put("checked_state", 1);
	  pd.put("order_date", fmat.format(new Date()));
	  pd.put("deliver_date", fmat.format(getSomeDay(new Date(),3)));
	  pd.put("deliver_style", 1);
	  pd.put("comment", "");
	//  pd.put("supplier_id", order);	//供应商id
	  pd.put("deliver_address", "太原市清徐县赵家堡工业园（联坤仓储物流园）");//配送地址
		//  pd.put("manager_name", order);	//供应商联系人
		//  pd.put("manager_tel", order);	//供应商电话
		 
		//  pd.put("order_amount", order); //总金额
		//  pd.put("total_svolume", order); //总体积
		//  pd.put("total_weight", order); //总重量
	  pd.put("paid_amount", 0);
	  pd.put("clearing_form", "");
	  pd.put("is_printed", 2);
	  pd.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
	  pd.put("create_time", fat.format(new Date()));
	  pd.put("status", "1");
	  return pd;
  }
  /**
   * 封装采购单详情
   * @param order
   * @return
   */
  public PageData encapsulation_PurchaseOrderItem(PageData pagedate,String orderNum,String groupnum){
	  PageData pd = new PageData();
	  SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd");
	  SimpleDateFormat fat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	  if(groupnum != null && !groupnum.equals("")) {
		  pd.put("group_num", groupnum);
	  }else{
		  pd.put("group_num", pagedate.getString("group_num"));
	  }
	  pd.put("order_num", orderNum);
	  pd.put("suggest_quantity",pagedate.getString("quantity") ); //建议订购数
	  pd.put("quantity", pagedate.getString("quantity"));	//订单数量
	  pd.put("final_quantity", pagedate.getString("quantity")); //入库数量
	  pd.put("purchase_time", fat.format(new Date()));
	  pd.put("creator",LoginUtil.getLoginUser().getUSER_ID()+""); //创建人
	  pd.put("comment", "");
	  pd.put("create_time", fat.format(new Date()));
	  pd.put("state", "1");
	  pd.put("gift_quantity", pagedate.get("gift_quantity"));
	  return pd;
  }
  /**
   * 想要获取的日期与传入日期的差值 比如想要获取传入日期前四天的日期 day=-4即可
   * @param date
   * @param day
   * @return
   */
  public static Date getSomeDay(Date date, int day){
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.DATE, day);
	        return calendar.getTime();
  }
  public void test(){
	  try {
		//System.out.println("测试开始");
		 Date date = this.getSomeDay(new Date(),3);
		SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd");
		String dat = fmat.format(date);
		PageData purchaseorder = (PageData) dao.findForObject("SellingOrderMapper.findpurchaseorder", "44133920");
		//System.out.println("测试结束");
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  public PageData saveInventHistory(PageData item,double quantity,int rck_type,String com){
		 
	  PageData inventHistory=new PageData();
	  double final_quantity=Double.valueOf(item.getString("final_quantity"));
	  double gift_quantity=Double.valueOf(item.getString("gift_quantity"));
	  double sum_quantity=final_quantity+gift_quantity;
	  inventHistory.put("quantity", sum_quantity);
	  inventHistory.put("rck_type",rck_type );
	  inventHistory.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
	  inventHistory.put("ck_id",  1);
	  if(quantity==0){
		  inventHistory.put("comment",com+"数量为："+sum_quantity);
	  }else{
		  inventHistory.put("comment",com+"数量为："+quantity);
	  }
	  inventHistory.put("product_id", item.getString("product_id"));
	  inventHistory.put("order_num",item.getString("order_num"));
	  inventHistory.put("warehouseId",1);
	  return inventHistory;
  }
  public PageData saveInvent(PageData item){
	  PageData invent=new PageData();
	  invent.put("product_id", item.getString("product_id"));
	  invent.put("warehouse_id",1 );
	  double final_quantity=Double.valueOf(item.getString("final_quantity"));
	  double gift_quantity=Double.valueOf(item.getString("gift_quantity"));
	  double sum_quantity=final_quantity+gift_quantity;
	  invent.put("product_quantity", sum_quantity);
	  invent.put("cargo_space_id",  item.getString("cargo_space_id"));
	  invent.put("state",1 );
	  invent.put("ck_id", 1);
	  invent.put("product_date",item.getString("product_time"));
	  return invent;
  }
  /**销售入库审核
 * @throws Exception **/
  @Transactional
  public String xsExamine(String Ids[]) throws Exception {
	  for (String id : Ids) {
		  PageData eno = new PageData();
		  PageData pa = new PageData();
		  pa.put("id", id);
		  PageData pdd = enwarehouseorderService.findById(pa);
		  eno.put("order_num", pdd.getString("order_num"));
		  List<PageData> enItem = (List<PageData>) dao.findForList("ENOrderMapper.findenorderitem", eno);//获取销售退货单明细
		  for (PageData item : enItem) {
			  double final_quantity = Double.valueOf(item.getString("final_quantity"));  //数量
			  double gift_quantity = Double.valueOf(item.getString("gift_quantity")); //赠品数量
			  List<PageData> invent = (List<PageData>) dao.findForList("ProductinventoryMapper.findproductin", item);//获取入库记录
			  double quantity = 0;
			  double sum_quantity = final_quantity + gift_quantity;
			  if (null != invent) {
				  for (PageData inv : invent) {
					  quantity += Double.valueOf(inv.getString("product_quantity"));
					  String product_date = inv.getString("product_date");
					  if (item.getString("product_time") != "" && item.getString("product_time") != null) {
						  if (DateUtil.getDaySub(item.getString("product_time"), product_date) == 0) {  //日期相等  累计
							  inv.put("quantity", DoubleUtil.add(sum_quantity, Double.valueOf(inv.getString("product_quantity"))));
							  dao.update("ProductinventoryMapper.updateQuantity", inv);
							  quantity += sum_quantity;
							  sum_quantity = 0;
						  }
					  } else {
						  throw new RuntimeException("订单号：" + item.getString("order_num") + "，商品：" + item.getString("product_name") + "生产日期：" + item.getString("product_time") + "为空");
					  }
				  }
			  }

			  if (sum_quantity != 0) {
				  dao.save("ProductinventoryMapper.save", saveInvent(item));  //添加
			  }
			  //添加库存历史
			  dao.save("ProductinventoryMapper.savehistory", saveInventHistory(item, quantity, 1, "入库后，"));
		  }
		  eno.put("checked_state", 2);
		  dao.update("ENOrderMapper.updateentype", eno);
	  }
	  return "true";
  }
  
  /**
   * 生成入库单
   * @param pdd
   * @throws Exception
   */
  @Transactional
  public void saveenorder(PageData pdd) throws Exception{
		PageData order=new PageData();
	 	order.put("product_id", pdd.getString("product_id"));
		order.put("order_num",pdd.getString("order_num"));
		order.put("group_num",pdd.getString("group_num"));
		order.put("checked_state",1);
		order.put("supplier_id",pdd.get("supplier_id"));
		order.put("order_date", com.hy.util.DateUtil.getAfterDayDate("0"));
		order.put("manager_name",pdd.get("manager_name"));
		order.put("manager_tel",pdd.get("manager_tel"));
		order.put("comment","");
		order.put("final_amount",DoubleUtil.multiply(Double.valueOf(pdd.getString("final_quantity")), Double.valueOf(pdd.getString("purchase_price"))));
		order.put("is_ivt_order_print", 0);
		order.put("is_temporary", 2);
		order.put("user_id", LoginUtil.getLoginUser().getUSER_ID());
		order.put("is_order_print", 1);
		order.put("order_type",1);
		order.put("ivt_state", 1);
		order.put("ck_id", 1);
		dao.save("EnWarehouseOrderMapper.save",order);
		//添加入库详情
		PageData orderitem=new PageData();
		orderitem.put("group_num",pdd.get("group_num"));
		orderitem.put("order_num", pdd.get("order_num"));
		orderitem.put("product_id",pdd.get("product_id"));
		orderitem.put("purchase_price",pdd.getString("purchase_price"));
		orderitem.put("quantity", pdd.getString("quantity"));
		orderitem.put("final_quantity", pdd.getString("final_quantity"));
		orderitem.put("svolume", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_volume")), Double.valueOf(pdd.getString("final_quantity"))));
		orderitem.put("weight", DoubleUtil.multiply(Double.valueOf(pdd.getString("sku_weight")), Double.valueOf(pdd.getString("final_quantity"))));
		orderitem.put("is_split_ivt", 2);
		orderitem.put("is_ivt_BK",1);
		orderitem.put("comment", "");
		orderitem.put("creator", LoginUtil.getLoginUser().getUSERNAME());
		orderitem.put("reason", "");
		dao.save("ENOrderItemMapper.save",orderitem);
		dao.update("EnWarehouseOrderMapper.updateenordergroupnum", pdd);
	}
  /**
   * 订单合并
   */
  public void orderMerger(){
	 
	// dao.findForList("SellingOrderMapper.listPage", page);
  }
  
  /**
   * 获取仓配批次号
 * @throws
   */
  public String findGroupNum(){

	  String groupnum= "GP_" +DateUtil.group();
      return groupnum;
  }
 
}


