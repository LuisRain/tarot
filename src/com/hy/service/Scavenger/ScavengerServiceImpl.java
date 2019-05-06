package com.hy.service.Scavenger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.controller.base.BaseController;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.entity.order.ENOrderItem;
import com.hy.entity.product.Product;
import com.hy.entity.product.Supplier;
import com.hy.entity.system.User;
import com.hy.service.Scavenger.Storage.StorageService;
import com.hy.service.inventory.WarehouseService;
import com.hy.service.order.ENOrderItemService;
import com.hy.service.order.ENOrderService;
import com.hy.service.order.EnWarehouseOrderService;
import com.hy.service.product.ProductService;
import com.hy.service.system.user.UserService;
import com.hy.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.WebService;

import java.util.*;

import static org.apache.log4j.varia.ExternallyRolledFileAppender.OK;

@Service("scavengerService")
@WebService(endpointInterface = "com.hy.service.Scavenger.ScavengerService", targetNamespace = "http://tempuri.org/", portName = "SCAVENGER")
public class ScavengerServiceImpl extends BaseController implements ScavengerService {

    @Resource
    private StorageService storageService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "enwarehouseorderService")
    private EnWarehouseOrderService enwarehouseorderService;
    @Resource(name="productService")
    private ProductService productService;
    @Resource(name="eNOrderService")
    private ENOrderService eNOrderService;
    @Resource(name="eNOrderItemService")
    private ENOrderItemService eNOrderItemService;
    
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String M_DATAGRIDCOLUMNFORMAT = "单号/BillNo/240/String/-1/1|日期/BillTime/150/Date/-1/1|供应商/ClientName/250/String/-1/1";
    private static String D_DATAGRIDCOLUMNFORMAT = "商品名称/GoodsName/200/String/-1/1&编号/GoodsCode/150/String/-1/1|数量/Qty/100/Double/2/1,单价/Price/100/Double/2/1,金额/Amount/100/Double/2/1";

    
    /*
     * (non Javadoc) 
     * @Title: Update_Bill_Message
     * @Description: TODO
     * @param PDA_Number
     * @param SignCode
     * @param UserID
     * @param BillName
     * @param BillType
     * @param Primary_ID 主键值
     * @param MasterJson 主表数据
     * @param DetailedJson 明细表数据
     * @param ColorSizeJson 颜色尺码表数据
     * @param ObjJson 
     * @return 返回 OK为修改成功            返回　{\"ErrorString\":\"内容\"}　为失败
     * @see com.hy.service.Scavenger.ScavengerService#Update_Bill_Message(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String Update_Bill_Message(String PDA_Number, String SignCode, String UserID, String BillName,
                                      String BillType, String Primary_ID, String MasterJson, String DetailedJson,
                                      String ColorSizeJson, String ObjJson) {
		// 解析主表数据
		JSONObject masterData = JSON.parseObject(JSON.parseArray(MasterJson).getString(0));
		// 解析明细表数据
		JSONArray detailedData = JSON.parseArray(DetailedJson);
		try {
			Integer storeId = Integer.valueOf(masterData.getString("StoreID"));// 仓库ID
			String clientId = masterData.getString("ClientID"); // 供应商ID
			String contactPerson = masterData.getString("EmpName"); // 联系人
			String contactPersonMobile = "";
			Double totalQuantity = 0.0;
			Double totalSkuWeightEn = 0.0;
			Double totalSkVolumeEn = 0.0;
			Double totalAmount = 0.0;

			List<String> itemProductIds = new ArrayList<>();
			List<ENOrderItem> enOrderItems = new ArrayList<>();
			PageData enOrderUpdate = new PageData();
			
			PageData pd = new PageData();
			pd.put("orderNum", Primary_ID);
			pd.put("parent_id", Integer.valueOf(0));
			pd = enwarehouseorderService.getEnwarouseById(pd);
			
			for (int i = 0; i < detailedData.size(); i++) {
				
				Double skuWeightEn = 0.0;
				Double skVolumeEn = 0.0;
				
				JSONObject detailData = detailedData.getJSONObject(i);

				PageData itemOrderUpdate = new PageData();
				PageData pdM = new PageData();
				ENOrderItem enOrderItem = null;

				String barCode = detailData.getString("BarCode");// 商品编码
				String pid = detailData.getString("GoodsID"); // 商品ID
				String num = detailData.getString("Qty"); // 数量
				String amount = detailData.getString("Amount"); // 金额
				String remark = detailData.getString("Remark"); // 备注
				String createName = masterData.getString("CreateName"); // 制单人姓名
				String detailID = detailData.getString("DetailID"); // 明细ID，若为新纪录则为0
				String billID = detailData.getString("BillID"); // 同上

				pdM.put("barCode", barCode);
				pdM.put("id", Long.valueOf(pid));
				Product product = productService.findById(pdM);

				PageData pdPrice = new PageData();
				pdPrice.put("product_id", Long.valueOf(product.getId()));
				pdPrice.put("price_type", Integer.valueOf(1)); // 1=成本价；2=售出价
				List<PageData> pdPriceList = this.productService.findPriceOfProductList(pdPrice);

				PageData pdP = productService.findProductInfoById(Long.valueOf(pid));
				
				PageData itemWhereEntity = new PageData();
				itemWhereEntity.put("product_id", pid);
				itemWhereEntity.put("order_num", Primary_ID);
				itemWhereEntity.put("id", detailID);
				PageData orderItemInfo = eNOrderItemService.findItemById(itemWhereEntity);
                itemProductIds.add(orderItemInfo.getString("id"));

				totalAmount += Double.valueOf(amount);
				totalSkuWeightEn += Double.parseDouble(pdP.getString("sku_weight"));
				totalSkVolumeEn += Double.parseDouble(pdP.getString("sku_volume"));
				totalQuantity += Double.valueOf(num);

				if (detailID.equals("0") && billID.equals("0") && (orderItemInfo == null || orderItemInfo.size() == 0 )) {
					// 执行插入
					String groupNum = pd.getString("group_num");

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
					// 商品价格信息
					pdPrice = pdPriceList.get(0);
					// 入库详情数据封装
					enOrderItem = new ENOrderItem();
					enOrderItem.setOrderNum(Primary_ID);
					enOrderItem.setGroupNum(groupNum);
					enOrderItem.setProduct(product);
					enOrderItem.setPurchasePrice(Double.parseDouble(pdPrice.getString("product_price").toString()));
					enOrderItem.setQuantity(Double.valueOf(num));
					enOrderItem.setFinalQuantity(Double.valueOf(num));
					enOrderItem.setIsSplitIvt(2);
					enOrderItem.setSvolume(MathUtil.mul(skVolumeEn,Double.valueOf(num))+ "");
					enOrderItem.setWeight(MathUtil.mul(skuWeightEn,Double.valueOf(num))+ "");
					enOrderItem.setIsIvtBK(1);
					enOrderItem.seteNTime(DateUtil.getAfterDayDate("3"));
					enOrderItem.setCreator(createName);
					if (StringUtils.isNotBlank(remark)) {
						enOrderItem.setComment(remark);
					}
					enOrderItems.add(enOrderItem);
				} else {
					// 执行更新
                    //为当前数据库中 - 采购数量        paramater:num为本次录入数量
                    Double quantityOfDB = Double.valueOf(orderItemInfo.getString("quantity"));
                    Double totalQuantityOfDB = Double.valueOf(orderItemInfo.getString("final_quantity"));

					itemOrderUpdate.put("id", Double.valueOf(detailID));
					itemOrderUpdate.put("productId", Double.valueOf(pid));
					itemOrderUpdate.put("orderNum", Primary_ID);
					itemOrderUpdate.put("svolume",MathUtil.mul(Double.valueOf(pdP.getString("sku_volume")), Double.valueOf(num)) + "");
					itemOrderUpdate.put("weight",MathUtil.mul(Double.valueOf(pdP.getString("sku_weight")),Double.valueOf(num))+ "");

					if(isContains(itemProductIds)){
                        itemOrderUpdate.put("quantity", MathUtil.add(quantityOfDB,Double.valueOf(num)));
                        itemOrderUpdate.put("finalQuantity", MathUtil.add(totalQuantityOfDB,Double.valueOf(num)));
                    }else{
                        itemOrderUpdate.put("quantity", Double.valueOf(num));
                        itemOrderUpdate.put("finalQuantity", Double.valueOf(num));
                    }

					storageService.updateStorage(itemOrderUpdate);
				}
			}

			if (enOrderItems.size() > 0 && enOrderItems != null) {
				eNOrderItemService.saveList(enOrderItems);
			}
			
			// 入库基本信息表修改价钱、重量、体积、总金额
			enOrderUpdate.put("final_amount", totalAmount);
			enOrderUpdate.put("total_svolume", MathUtil.mul(totalSkVolumeEn, totalQuantity));
			enOrderUpdate.put("total_weight", MathUtil.mul(totalSkuWeightEn, totalQuantity));
			enOrderUpdate.put("user_id", UserID);
			enOrderUpdate.put("supplier_id", clientId);
			enOrderUpdate.put("manager_name", contactPerson);
			enOrderUpdate.put("manager_tel", contactPersonMobile);
			enOrderUpdate.put("amount", totalAmount);
			enOrderUpdate.put("id", Long.valueOf(pd.getString("id")));
			eNOrderService.edit(enOrderUpdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"ErrorString\":\"单据保存失败！！\"}";
		}

		return OK;
	}


	private boolean isContains(List<String> stringList){
        Boolean isContains = Boolean.FALSE;
        Set<String> containsList = new HashSet<>();
        for (String str: stringList) {
            if(containsList.contains(str)){
                isContains = Boolean.TRUE;
                break;
            }else {
                containsList.add(str);
            }
        }
        return isContains;
    }





    /*
     * (non Javadoc) 上传单据信息
     * @Title: Save_Bill_Message
     * @Description: TODO
     * @param PDA_Number PDA的SN号
     * @param SignCode 程序标识
     * @param UserID 操作员ID
     * @param BillName 单据名称
     * @param BillType 单据类型
     * @param MasterJson 主表数据
     * @param DetailedJson 明细表数据
     * @param ColorSizeJson 颜色尺码表数据
     * @param ObjJson ObjJson
     * @return 
     * @see com.hy.service.Scavenger.ScavengerService#Save_Bill_Message(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String Save_Bill_Message(String PDA_Number, String SignCode, String UserID, String BillName, String BillType,
                                    String MasterJson, String DetailedJson, String ColorSizeJson, String ObjJson) {
        //返回 上传后主表主键值　为保存成功
    	
        //返回　{\"ErrorString\":\"内容\"}　为失败
    	logger.info("PDA-新增/保存单据数据");
    	logger.info("主表数据：MasterJson( "+MasterJson+" )");
    	logger.info("明细表数据：DetailedJson( "+DetailedJson+" )");
    	//解析主表数据
    	JSONObject masterData = JSON.parseObject(JSON.parseArray(MasterJson).getString(0));
    	//解析明细表数据
    	JSONArray detailedData = JSON.parseArray(DetailedJson);
    	//TODO 解析颜色尺码表数据--目前尚未用到
    	//TODO 解析家具和序列号版本数据--目前尚未用到
    	ENOrder enOrder = null;
    	ENOrderItem enOrderItem = null;
    	List<ENOrder> enOrders = new ArrayList<>();
    	List<ENOrderItem> enOrderItems = new ArrayList<>();
    	try{
    		if(masterData != null && detailedData != null){
    			
    			enOrder = new ENOrder();
    			Supplier supplier = new Supplier();
    			
    			//生成批次号
				String groupNum = "PDA-GP_" + DateUtil.group();
    			
        		//提取主表数据
        		String billNo = masterData.getString("BillNo"); //入库单号
        		String storeId = masterData.getString("StoreID"); //仓库编号
//        		String storeName = masterData.getString("StoreName"); //仓库名
        		String clientId = masterData.getString("ClientID"); //供应商ID
//        		String clientName = masterData.getString("ClientName"); //供应商名称
        		String remarks = masterData.getString("Remark"); //备注信息
//        		String createId = masterData.getString("CreateID"); //制单人ID
        		String createName = masterData.getString("CreateName"); //制单人姓名
        		String contactPerson = "";
        		String contactPersonMobile = "";
        		Double skuWeightEn = 0.0;
        		Double skVolumeEn = 0.0;
        		Double totalQuantity = 0.0;
        		Double totalSkuWeightEn = 0.0;
        		Double totalSkVolumeEn = 0.0;
        		Double totalAmount = 0.0;
        		User user = new User();
        		user.setUSER_ID(Long.valueOf(UserID));
        		
        		//提取明细数据
        		for(int i = 0; i < detailedData.size(); i++){
        			PageData pdM = new PageData();
        			JSONObject detailData = detailedData.getJSONObject(i);
        			
        			String barCode = detailData.getString("BarCode");//商品编码
        			String pid = detailData.getString("GoodsID"); //商品ID
        			String num = detailData.getString("Qty"); //数量
        			String unitPrice = detailData.getString("Price"); //单价
        			String amount = detailData.getString("Amount"); //金额
        			String remark = detailData.getString("Remark"); //备注
        			
                    //商品信息
        			pdM.put("barCode", barCode);
        			pdM.put("id", Long.valueOf(pid));
                    Product product = productService.findById(pdM);
                    if(product != null && product.getId() == Long.valueOf(pid)){
                    	PageData pdP = productService.findProductInfoById(Long.valueOf(pid));
                    	
                    	totalAmount += Double.valueOf(amount);
                    	
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
                          
                          
                    	PageData pdPrice = new PageData();
                    	pdPrice.put("product_id", Long.valueOf(product.getId()));
                        pdPrice.put("price_type", Integer.valueOf(1)); //1=成本价；2=售出价
                        List<PageData> pdPriceList = this.productService.findPriceOfProductList(pdPrice);
                        //商品价格信息
                        pdPrice = pdPriceList.get(0);
                        //入库详情数据封装
                        enOrderItem = new ENOrderItem();
                        enOrderItem.setOrderNum(billNo);
                        enOrderItem.setGroupNum(groupNum);
                        enOrderItem.setProduct(product);
                        enOrderItem.setPurchasePrice(Double.parseDouble(pdPrice.getString("product_price").toString()));
                        enOrderItem.setQuantity(Double.valueOf(num));
                        enOrderItem.setFinalQuantity(Double.valueOf(num));
                        enOrderItem.setIsSplitIvt(2);
                        enOrderItem.setSvolume(MathUtil.mul(skVolumeEn, Double.valueOf(num))+"");
                        enOrderItem.setWeight(MathUtil.mul(skuWeightEn, Double.valueOf(num))+"");
                        enOrderItem.setIsIvtBK(1);
                        enOrderItem.seteNTime(DateUtil.getAfterDayDate("3"));
                        enOrderItem.setCreator(createName);
                        if(StringUtils.isNotBlank(remark)){
                        	enOrderItem.setComment(remark);
                        }
                        enOrderItems.add(enOrderItem);
                    }
        		}
        		if(enOrderItems.size() > 0 && enOrderItems != null){
        			for (ENOrderItem items : enOrderItems) {
        				totalQuantity += items.getQuantity();
        				totalSkuWeightEn += Double.valueOf(items.getWeight());
        				totalSkVolumeEn += Double.valueOf(items.getSvolume());
					}
        		}
        		supplier.setId(Long.valueOf(clientId));
        		enOrder.setFinalAmount(totalAmount);
        		enOrder.setAmount(totalAmount);
        		enOrder.setGroupNum(groupNum);
                enOrder.setOrderNum(billNo);
                enOrder.setOrderDate(Tools.date2Str(new Date()));
                enOrder.setCheckedState(1);
                enOrder.setManagerName(contactPerson);
                enOrder.setManagerTel(contactPersonMobile);
                enOrder.setPaidAmount(0.0D);
                enOrder.setIsIvtOrderPrint(0);
                enOrder.setIsTemporary(2);
                enOrder.setSupplier(supplier);
                enOrder.setUser(user);
                enOrder.setTotalSvolume(MathUtil.mul(totalSkVolumeEn, totalQuantity));
                enOrder.setTotalWeight(MathUtil.mul(totalSkuWeightEn, totalQuantity));
                enOrder.setOrderType(1);
                enOrder.setCkId(Integer.valueOf(storeId));
                if(StringUtils.isNotBlank(remarks)){
                	enOrder.setComment(remarks);
                }
                enOrders.add(enOrder);
                
                if((enOrders.size() > 0 && enOrders != null)&& (enOrderItems.size() > 0 && enOrderItems != null)){
                	eNOrderService.saveList(enOrders);
                	eNOrderItemService.saveList(enOrderItems);
                }
        	}else{
        		return "{\"ErrorString\":\"主表或明细表数据没空！！\"}";
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return OK;
    }


    @Override
    public String Get_SoftwareType(String PDA_Number, String SignCode) {
        String ReturnStr;

        ReturnStr = "0";

        return ReturnStr;
    }

    @Override
    public String Get_UserList(String PDA_Number, String SignCode) throws Exception {
/*

        ReturnStr = "[{\"UserID\":\"" + PDA_Number + "\",\"User_Code\":\"001\",\"User_Name\":\"张三\",\"User_Pw\":\"123\",\"IsUse\":\"1\"}," +

                "{\"UserID\":\"2\",\"User_Code\":\"002\",\"User_Name\":\"李四\",\"User_Pw\":\"456\",\"IsUse\":\"0\"}," +

                "{\"UserID\":\"3\",\"User_Code\":\"003\",\"User_Name\":\"赵五\",\"User_Pw\":\"789\",\"IsUse\":\"1\"}]";
*/
        PageData whereCondition = new PageData();
        whereCondition.put("isShow_status", "all");
        String errInfo = "";
        List<Map<String, Object>> list = new ArrayList<>();
        List<PageData> userInfoData = userService.listAllUser(whereCondition);
        Object json = JSON.toJSON(userInfoData);
        JSONArray jsonArray = JSON.parseArray(json.toString());
        if (userInfoData != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("UserID", Long.parseLong(jsonObject.getString("USER_ID")));
                map.put("User_Code", i);
                map.put("User_Name", jsonObject.getString("USERNAME"));
                map.put("User_Pw", jsonObject.getString("PASSWORD"));
                map.put("IsUse", "1");
                list.add(map);
            }
            String data = JSON.toJSONString(list);
            return data;
        } else {
            //在服务参数那查询出数据库信息 然后进行参数集体返回
            errInfo = "{\"ErrorString\":\"登录异常\"}";
        }
        return errInfo;
    }


    @Override
    public String Get_UserPassWord(String PDA_Number, String SignCode, String UserID, String PW) throws Exception {
        //System.out.println("进入方法：" + "Get_UserPassWord");
        
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        
        PageData pdUserID = new PageData();
        pdUserID.put("USER_ID", UserID);
        PageData userByNameAndPwd = userService.getUserByNameAndPwd(pdUserID);
        String passwd = new SimpleHash("SHA-1", userByNameAndPwd.getString("USERNAME"), PW).toString();
        
        User user = new User();
        user.setUSER_ID(Long.parseLong(userByNameAndPwd.getString("USER_ID")));
        user.setUSERNAME(userByNameAndPwd.getString("USERNAME"));
        user.setPASSWORD(userByNameAndPwd.getString("PASSWORD"));
        user.setNAME(userByNameAndPwd.getString("NAME"));
        user.setRIGHTS(userByNameAndPwd.getString("RIGHTS"));
        user.setROLE_ID(userByNameAndPwd.getString("ROLE_ID"));
        user.setLAST_LOGIN(userByNameAndPwd.getString("LAST_LOGIN"));
        user.setIP(userByNameAndPwd.getString("IP"));
        user.setSTATUS(userByNameAndPwd.getString("STATUS"));
        user.setCkId(Integer.valueOf(userByNameAndPwd.getString("ck_id")));
        session.setAttribute("sessionUser", user);
        session.removeAttribute("sessionSecCode");
        return passwd;
    }

    @Override
    public String Get_User_Function(String PDA_Number, String SignCode, String UserID) {

        //System.out.println("进入方法：" + "Get_User_Function");
        //System.out.println("PDA_Number: " + PDA_Number + " ; SignCode :" + SignCode + "; UserID :" + UserID);
        String ReturnStr = "[{\"FunctionName\":\"单据管理\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"引用验单\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"商品信息\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"系统设置\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"报表查询\",\"Value\":\"3\"}," +
                "{\"FunctionName\":\"信息维护\",\"Value\":\"3\"}," +
                "{\"FunctionName\":\"打印设置\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"小票格式\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"蓝牙配对\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"条码规则\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"版本更新\",\"Value\":\"1\"}," +
                "{\"FunctionName\":\"开启画面\",\"Value\":\"2\"}," +
                "{\"FunctionName\":\"上传日志\",\"Value\":\"1\"}]";
        return ReturnStr;


    }

    @Override
    public String Get_Bill_List(String PDA_Number, String SignCode, String UserID) {
        String ReturnStr = "[{\"BillName\":\"采购订单\"}," +

                "{\"BillName\":\"采购入库单\"}," +

                "{\"BillName\":\"销售订单\"}," +

                "{\"BillName\":\"销售出库单\"}]";

        //"{\"BillName\":\"调拨单\"}," +

        //"{\"BillName\":\"盘点单\"}]";

        return ReturnStr;
    }

    @Override
    public String Get_Bill_Number(String PDA_Number, String SignCode, String UserID, String BillName) {
        String ReturnStr = "";

        ReturnStr = "PDA-RK_" + StringUtil.getStringOfMillisecond("") + MathUtil.getSixNumber();

        return ReturnStr;
    }

    /*
     * (non Javadoc) 获取单据详细设置:返回格式{\"BillName\":\"单据管理\"}
     * @Title: Get_Bill_Detailed
     * @Description: TODO
     * @param PDA_Number
     * @param SignCode
     * @param UserID
     * @param BillName
     * @return 
     * @see com.hy.service.Scavenger.ScavengerService#Get_Bill_Detailed(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
	@Override
	public String Get_Bill_Detailed(String PDA_Number, String SignCode,String UserID, String BillName) {

		
		JSONObject infoData = new JSONObject();

		infoData.put("BillName", BillName);
		infoData.put("BillType", "InOrder");
		infoData.put("M_DataGridColumnFormat", M_DATAGRIDCOLUMNFORMAT);
		infoData.put("D_DataGridColumnFormat", D_DATAGRIDCOLUMNFORMAT);

		JSONArray masterControl = getMasterOrDetailStr(true);
		JSONArray detailedControl = getMasterOrDetailStr(false);

		infoData.put("MasterControl", masterControl);
		infoData.put("DetailedControl", detailedControl);

		return infoData.toJSONString();
    }
	
	
	/**
	 * 
	* <b>Description:</b><br> 获取单据主表设置信息 
	* RowNo：行号<br>
	* PDAColumnName:字段名称 ---不能改变　(必须是PDA主表中的列名)<br>
	* ShowName:显示的字段名称 ----可改变<br>
	* DefaultValue:默认值<br>
	* IsShow:此控件是否可见<br>
	* IsReadOnly: 此控件是否只读<br>
	* IsSelectBillFiler:是否作为查单条件<br>
	* IsNotEmpty:按新单按钮后，内容是否不清空<br>
	* @return
	* @Note
	* <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
	* <br><b>Date:</b> 2018年10月11日 上午10:10:36
	* <br><b>Version:</b> 1.0
	 */
	private JSONArray getMasterOrDetailStr(boolean isMaster) {
		String resultStr = "";
		if (isMaster) {
			resultStr = "[" +
                    "{ \"RowNo\": \"1\",\"PDAColumnName\": \"BillNo\", \"ShowName\":\"单号\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"1\", \"IsSelectBillFiler\": \"1\", \"IsNotEmpty\": \"0\" }," +
                    "{ \"RowNo\": \"2\",\"PDAColumnName\": \"BillTime\", \"ShowName\":\"日期\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"1\", \"IsSelectBillFiler\": \"1\", \"IsNotEmpty\": \"0\" }," +
                    "{ \"RowNo\": \"3\",\"PDAColumnName\": \"ClientID\", \"ShowName\":\"供应商\", \"CtrlType\": \"SelectText\", \"SelectCtrl_DisplayField\": \"编号/ClientCode/150/String/-1/1|名称/ClientName/150/String/-1/1|联系人/Contacts/150/String/-1/1|电话/Phone/150/String/-1/1\", \"SelectCtrl_Filer\": \"编号/ClientCode,名称/ClientName,联系人/Contacts,电话/Phone\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"1\", \"IsSelectBillFiler\": \"1\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"4\",\"PDAColumnName\": \"StoreID\", \"ShowName\":\"仓库\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"1\", \"IsSelectBillFiler\": \"1\" , \"IsNotEmpty\": \"1\"}," +
                    "{ \"RowNo\": \"5\",\"PDAColumnName\": \"StoreID2\", \"ShowName\":\"调拨仓库\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\", \"IsNotEmpty\": \"0\" }," +
                    "{ \"RowNo\": \"6\",\"PDAColumnName\": \"EmpID\", \"ShowName\":\"业务员\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\", \"IsNotEmpty\": \"0\" }," +
                    "{ \"RowNo\": \"7\",\"PDAColumnName\": \"CollectAmount\", \"ShowName\":\"付款金额\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"8\",\"PDAColumnName\": \"NoSmallAmount\", \"ShowName\":\"抹零金额\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"9\",\"PDAColumnName\": \"Remark\", \"ShowName\":\"备注\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"10\",\"PDAColumnName\": \"Comment\", \"ShowName\":\"摘要\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"11\",\"PDAColumnName\": \"CreateName\", \"ShowName\":\"制单人\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"12\",\"PDAColumnName\": \"Int3\", \"ShowName\":\"状态\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"13\",\"PDAColumnName\": \"String2\", \"ShowName\":\"自定义字符2\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"14\",\"PDAColumnName\": \"Number1\", \"ShowName\":\"自定义数量1\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}," +
                    "{ \"RowNo\": \"15\",\"PDAColumnName\": \"Number2\", \"ShowName\":\"自定义数量2\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsSelectBillFiler\": \"0\" , \"IsNotEmpty\": \"0\"}" +
                    "]";
		} else if (!isMaster) {
			resultStr = "[" +
                    "{ \"RowNo\": \"1\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"BarCode\", \"ShowName\":\"条码\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"2\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"GoodsID\", \"ShowName\":\"商品名称\", \"CtrlType\": \"SelectText\", \"SelectCtrl_DisplayField\": \"编号/GoodsCode/120/String/-1/1|名称/GoodsName/150/String/-1/1|规格/GoodsSpec/150/String/-1/1|条码/BarCode/220/String/-1/1\", \"SelectCtrl_Filer\": \"编号/GoodsCode/,名称/GoodsName/,规格/GoodsSpec/,条码/BarCode\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"1\", \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"1\" }," +
                    "{ \"RowNo\": \"3\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"GoodsCode\", \"ShowName\":\"商品编号\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"1\", \"IsNotNull\": \"0\", \"IsNotEmpty\": \"0\" , \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"4\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"GoodsSpec\", \"ShowName\":\"规格\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"1\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"5\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"StoreID\", \"ShowName\":\"仓库\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsNotEmpty\": \"0\" , \"Formula\": \"\" ,\"IsOnly\": \"1\"}," +
                    "{ \"RowNo\": \"6\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"UnitID\", \"ShowName\":\"单位\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"1\"}," +
                    "{ \"RowNo\": \"7\",\"ColumnNo\": \"2\",\"PDAColumnName\": \"Price\", \"ShowName\":\"单价\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\", \"IsNotEmpty\": \"0\" , \"Formula\": \"\" ,\"IsOnly\": \"1\"}," +
                    "{ \"RowNo\": \"8\",\"ColumnNo\": \"2\",\"PDAColumnName\": \"Qty\", \"ShowName\":\"数量\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"1\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"1\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"9\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"Discount\", \"ShowName\":\"折扣\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"100\",\"IsShow\": \"1\", \"IsReadOnly\": \"1\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"10\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"ColorID\", \"ShowName\":\"颜色\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"11\",\"ColumnNo\": \"2\",\"PDAColumnName\": \"SizeID\", \"ShowName\":\"尺码\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"12\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"Remark\", \"ShowName\":\"备注\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"1\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"13\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"IsGift\", \"ShowName\":\"是否赠品\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"1\"}," +
                    "{ \"RowNo\": \"14\",\"ColumnNo\": \"2\",\"PDAColumnName\": \"StoreQty\", \"ShowName\":\"库存\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"1\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"15\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String1\", \"ShowName\":\"自定义字符1\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"16\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String2\", \"ShowName\":\"自定义字符2\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"17\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String3\", \"ShowName\":\"自定义字符3\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"18\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String4\", \"ShowName\":\"自定义字符4\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"19\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String5\", \"ShowName\":\"自定义字符5\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"20\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String6\", \"ShowName\":\"自定义字符6\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"21\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String7\", \"ShowName\":\"自定义字符7\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\"}," +
                    "{ \"RowNo\": \"22\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"Number1\", \"ShowName\":\"自定义数量1\", \"CtrlType\": \"\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\",\"IsOnly\": \"\" }," +
                    "{ \"RowNo\": \"23\",\"ColumnNo\": \"1\",\"PDAColumnName\": \"String8\", \"ShowName\":\"测试Box\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsShow\": \"0\", \"IsReadOnly\": \"0\", \"IsNotNull\": \"0\" , \"IsNotEmpty\": \"0\", \"Formula\": \"\" ,\"IsOnly\": \"\" ,\"SelectName\": \"String7\"}" +
                    "]";

		}
		return resultStr == "" ? null : JSON.parseArray(resultStr);

	}
	

    /*
     * (non Javadoc) 制新单-获取Combox控件列表
     * @Title: Get_Combox_List
     * @Description: TODO
     * @param PDA_Number
     * @param SignCode
     * @param UserID
     * @param BillName
     * @param PDAColumnName
     * @param LabName
     * @return 
     * @see com.hy.service.Scavenger.ScavengerService#Get_Combox_List(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String Get_Combox_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName) {

        String ReturnStr = "";
        //System.out.println("进入方法：" + "Get_Combox_List");
        if (PDAColumnName == null) {
            PDAColumnName = "-1";
        }
        //System.out.println("PDAColumnName：" + PDAColumnName);
        switch (PDAColumnName){
            case "EmpID":
                ReturnStr = "{ \"IDName\": \"Column1\",\"ValueName\": \"Column2\",\"DefaultValue\": \"002\"," +

                        "\"TableMessage\":[{\"Column1\":\"001\",\"Column2\":\"业务员1\"}," +

                        "{\"Column1\":\"002\",\"Column2\":\"业务员2\"}," +

                        "{\"Column1\":\"003\",\"Column2\":\"业务员3\"}]}";
                break;

            case "StoreID":

                /*eturnStr = "{ \"IDName\": \"ClientID\",\"ValueName\": \"ClientName\",\"RowCount\": \"4\"," +

                        "\"TableMessage\":[" +

                        "{\"ClientID\":\"101\",\"ClientCode\":\"101\",\"ClientName\":\"供应商1\",\"Contacts\":\"联系人\",\"Phone\":\"133314\",\"Address\":\"地址\",\"Remark\":\"备注\",\"Receivable\":\"0\",\"Payable\":\"200\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                        "{\"ClientID\":\"001\",\"ClientCode\":\"001\",\"ClientName\":\"客户1\",\"Contacts\":\"联系人\",\"Phone\":\"133314\",\"Address\":\"地址\",\"Remark\":\"备注\",\"Receivable\":\"300\",\"Payable\":\"0\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                        "{\"ClientID\":\"002\",\"ClientCode\":\"002\",\"ClientName\":\"客户2\",\"Contacts\":\"联系人\",\"Phone\":\"133314\",\"Address\":\"地址\",\"Remark\":\"备注\",\"Receivable\":\"700\",\"Payable\":\"0\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                        "{\"ClientID\":\"003\",\"ClientCode\":\"003\",\"ClientName\":\"客户3\",\"Contacts\":\"联系人\",\"Phone\":\"133314\",\"Address\":\"地址\",\"Remark\":\"备注\",\"Receivable\":\"150\",\"Payable\":\"0\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +

                        "]}";

                break;*/

            case "StoreID2":

//                ReturnStr = "{ \"IDName\": \"Column1\",\"ValueName\": \"Column1\",\"DefaultValue\": \"002\"," +
//
//                        "\"TableMessage\":[{\"Column1\":\"1\",\"Column2\":\"中央仓\"}]}";
            	
            	ReturnStr = "{ \"IDName\": \"Column1\",\"ValueName\": \"Column2\",\"DefaultValue\": \"002\"," +

            	"\"TableMessage\":[{\"Column1\":\"1\",\"Column2\":\"中央仓\"}]}";

                break;
        }

        return ReturnStr;

    }

    /*
     * (non Javadoc) 获取selectText控件结果列表 
     * @Title: Get_SelectText_List
     * @Description: TODO
     * @param PDA_Number
     * @param SignCode
     * @param UserID
     * @param BillName
     * @param PDAColumnName
     * @param LabName
     * @param Match
     * @param Filter
     * @param ClientID
     * @return 
     * @see com.hy.service.Scavenger.ScavengerService#Get_SelectText_List(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String Get_SelectText_List(String PDA_Number, String SignCode, String UserID, String BillName,
                                      String PDAColumnName, String LabName, String Match, String Filter, String ClientID) {
        //Filter 的格式的 Json  "[{\"ColumnName\":\"\",\"LabName\":\"\",\"Value\":\"\"}," +
        // "{\"ColumnName\":\"\",\"LabName\":\"\",\"Value\":\"\"}," +
        // "{\"ColumnName\":\"\",\"LabName\":\"\",\"Value\":\"\"}]";
        //System.out.println("进入方法：" + "Get_SelectText_List");

        String ReturnStr = "";

        if (PDAColumnName == null) {
            PDAColumnName = "-1";
        }
        //具体的获取相对的字符值
        String columnName = "";
        String labName = "";
        String billValue = "";
        String enterValue = "";
        JSONArray jsonFilter = JSON.parseArray(Filter);
        for (int i = 0; i < jsonFilter.size(); i++) {
            JSONObject json = jsonFilter.getJSONObject(i);
            columnName = json.getString("ColumnName");
            labName = json.getString("LabName");
            billValue = json.getString("Value");


        }
        switch (PDAColumnName) {
            //往来单位
            case "ClientID":
                try {
                    ReturnStr = storageService.querysupplier(Match, billValue, columnName);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ReturnStr.toString();//系统异常
                }

                break;
            //商品信息
            case "GoodsID":
                if (columnName.equals("GoodsName") && labName.equals("回车")) {
                    PDAColumnName = "Get_SelectText_List-Enter";
                } else {
                    PDAColumnName = "Get_SelectText_List-GoodsID";
                }

                ReturnStr = this.storageService.querywares(null, null, billValue, null, ClientID, Match, PDAColumnName);
                break;
            case "GoodsCode":
                break;
            case "BarCode":

                /*ReturnStr = "{ \"IDName\": \"GoodsID\",\"ValueName\": \"GoodsName\",\"RowCount\": \"100000\"," +
                        "\"TableMessage\":[" +
                        "{\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsName\":\"糖果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"001\",\"URate\":\"1\",\"UnitName\":\"个\",\"BarCode\":\"123456\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +
                        "{\"GoodsID\":\"002\",\"GoodsCode\":\"002\",\"GoodsName\":\"水果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"002\",\"URate\":\"1\",\"UnitName\":\"包\",\"BarCode\":\"111111\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +
                        "]}";*/
                if (Filter.contains("BarCode")) {
                    PDAColumnName = "Get_SelectText_List-BarCode";
                    ReturnStr = this.storageService.querywares(null, null, billValue, null, null, Match, PDAColumnName);
                } else {
                    ReturnStr = "{ \"IDName\": \"GoodsID\",\"ValueName\": \"GoodsName\",\"RowCount\": \"2\",\"TableMessage\":[]}";
                }


                break;
            default:
                ReturnStr = "{ \"IDName\": \"Column1\",\"ValueName\": \"Column2\",\"RowCount\": \"3\"," +
                        "\"TableMessage\":[" +
                        "{\"Column1\":\"001\",\"Column2\":\"\",\"Column3\":\"\",\"Column4\":\"\",\"Column5\":\"\",\"Column6\":\"\",\"Column7\":\"\",\"Column8\":\"\",\"Column9\":\"\",\"Column10\":\"\"}," +
                        "{\"Column1\":\"002\",\"Column2\":\"\",\"Column3\":\"\",\"Column4\":\"\",\"Column5\":\"\",\"Column6\":\"\",\"Column7\":\"\",\"Column8\":\"\",\"Column9\":\"\",\"Column10\":\"\"}," +
                        "{\"Column1\":\"003\",\"Column2\":\"\",\"Column3\":\"\",\"Column4\":\"\",\"Column5\":\"\",\"Column6\":\"\",\"Column7\":\"\",\"Column8\":\"\",\"Column9\":\"\",\"Column10\":\"\"}]}";
                break;

        }

        return ReturnStr;


    }

    @Override
    public String Get_GoodsUnit_List(String PDA_Number, String SignCode, String UserID, String BillName,
                                     String PDAColumnName, String LabName, String GoodsID) {
        String ReturnStr = /*"[{\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\"}," +

                "{\"UnitID\":\"002\",\"UnitName\":\"包\",\"URate\":\"10\"}," +

                "{\"UnitID\":\"003\",\"UnitName\":\"箱\",\"URate\":\"40\"}]"*/
                storageService.queryUnit(GoodsID);


        return ReturnStr;


    }

    @Override
    public String Get_GoodsBatchNoList(String PDA_Number, String SignCode, String UserID, String BillName,
                                       String PDAColumnName, String LabName, String StoreID, String GoodsID) {
        //返回 ""　为不是批号商品

        String ReturnStr = "[{\"FBatchNo\":\"A0001\",\"FQty\":\"5\",\"ProductionDate\":\"\",\"ValidityDate\":\"\"}," +

                "{\"FBatchNo\":\"A0002\",\"FQty\":\"10\",\"ProductionDate\":\"\",\"ValidityDate\":\"\"}," +

                "{\"FBatchNo\":\"A0003\",\"FQty\":\"20\",\"ProductionDate\":\"\",\"ValidityDate\":\"\"}," +

                "{\"FBatchNo\":\"A0004\",\"FQty\":\"9\",\"ProductionDate\":\"\",\"ValidityDate\":\"\"}]";

        return ReturnStr;

    }

    @Override
    public String Get_GoodsPriceList(String PDA_Number, String SignCode, String UserID, String BillName,
                                     String PDAColumnName, String LabName, String ClientID, String StoreID, String GoodsID, String UnitID,
                                     String URate) {
        //单价取值按顺序往下取
        //System.out.println("PDAColumnName：" + PDAColumnName);
        JSONObject json = new JSONObject();
        String ReturnStr = "";

        ReturnStr = /*"[{\"Price\":\"6\",\"PriceType\":\"最近售价\"}," +

                "{\"Price\":\"8\",\"PriceType\":\"售价1\"}," +

                "{\"Price\":\"9\",\"PriceType\":\"售价2\"}," +

                "{\"Price\":\"10\",\"PriceType\":\"售价3\"}]";*/
                storageService.productIDInquirySalePrice(GoodsID);

        return ReturnStr;

    }

    @Override
    public String Get_Detailed_ComBox_List(String PDA_Number, String SignCode, String UserID, String BillName,
                                           String PDAColumnName, String LabName, String ClientID, String StoreID, String GoodsID) {
        String ReturnStr = "";

        switch (PDAColumnName) {
            case "IsGift":
                ReturnStr = "[{\"Column1\":\"0\",\"Column2\":\"否\"}," +
                        "{\"Column1\":\"1\",\"Column2\":\"是\"}]";
                break;
            case "StoreID":
            case "StoreID2":
                ReturnStr = "[{\"Column1\":\"001\",\"Column2\":\"中央仓\"}]}";
                break;
            case "String8":
                ReturnStr = "[{\"Column1\":\"001\",\"Column2\":\"明细7\"}," +
                        "{\"Column1\":\"002\",\"Column2\":\"明细17\"}," +
                        "{\"Column1\":\"003\",\"Column2\":\"明细27\"}]}";
                break;
            default:
                ReturnStr = "[{\"Column1\":\"01\",\"Column2\":\"测试ComBox1\"}," +
                        "{\"Column1\":\"02\",\"Column2\":\"测试ComBox2\"}," +
                        "{\"Column1\":\"03\",\"Column2\":\"测试ComBox3\"}," +
                        "{\"Column1\":\"04\",\"Column2\":\"测试ComBox4\"}]";
                break;
        }

        return ReturnStr;
    }

    /**
     * 库存
     *
     * @param PDA_Number
     * @param SignCode
     * @param UserID
     * @param BillName
     * @param PDAColumnName
     * @param LabName
     * @param ClientID
     * @param StoreID
     * @param GoodsID
     * @param UnitID
     * @param URate
     * @param BatchNo
     * @param ColorID
     * @param SizeID
     * @return
     */
    @Override
    public String Get_GoodsStockNumber(String PDA_Number, String SignCode, String UserID, String BillName,
                                       String PDAColumnName, String LabName, String ClientID, String StoreID, String GoodsID, String UnitID,
                                       String URate, String BatchNo, String ColorID, String SizeID) {
        String ReturnStr = URate;


        return ReturnStr;

    }

    @Override
    public String Get_Set_Goods_Detail_Obj(String PDA_Number, String SignCode, String UserID, String BillName, String GoodsID) {
        return "{\"SelectFilter\":\"条码/BarCode,编码/GoodsCode,商品名称/GoodsName,规格/GoodsSpec\","
                + "\"Select_DataGridColumnFormat\":\"编号/GoodsCode/200/String/-1/1&包件名称/goodsname/200/String/-1/1|规格/GoodsSpec/250/String/-1/1&条码/barcode/250/String/-1/1\","
                + "\"Show_DataGridColumnFormat\":\"编号/GoodsCode/200/String/-1/1&包件名称/goodsname/200/String/-1/1|规格/GoodsSpec/250/String/-1/1&数量/Qty/100/String/-1/1\"}";
    }

    @Override
    public String Get_Goods_ColorSize_StockNumber(String PDA_Number, String SignCode, String UserID,
                                                  String BillNamestring, String ClientID, String StoreID, String GoodsID, String UnitID, String URate,
                                                  String BatchNo) {
        String ReturnStr = "";

        ReturnStr = "[{\"GoodsID\":\"" + GoodsID + "\",\"ColorID\":\"1\",\"SizeID\":\"1\",\"QTY\":\"10\"}," +

                "{\"GoodsID\":\"" + GoodsID + "\",\"ColorID\":\"1\",\"SizeID\":\"2\",\"QTY\":\"5\"}," +

                "{\"GoodsID\":\"" + GoodsID + "\",\"ColorID\":\"1\",\"SizeID\":\"3\",\"QTY\":\"6\"}," +

                "{\"GoodsID\":\"" + GoodsID + "\",\"ColorID\":\"2\",\"SizeID\":\"1\",\"QTY\":\"8\"}]";

        return ReturnStr;


    }

    @Override
    public String Get_GoodsColor_List(String PDA_Number, String SignCode, String UserID, String BillName,
                                      String PDAColumnName, String LabName, String GoodsID) {

        String ReturnStr = "[{\"ColorID\":\"1\",\"ColorName\":\"白色\",\"ColorCode\":\"101\"}," +

                "{\"ColorID\":\"2\",\"ColorName\":\"黄色\",\"ColorCode\":\"102\"}," +

                "{\"ColorID\":\"3\",\"ColorName\":\"金色\",\"ColorCode\":\"103\"}]";

        return ReturnStr;


    }

    @Override
    public String Get_GoodsSize_List(String PDA_Number, String SignCode, String UserID, String BillName,
                                     String PDAColumnName, String LabName, String GoodsID) {
        String ReturnStr = "[{\"SizeID\":\"1\",\"SizeName\":\"S\",\"SizeCode\":\"201\"}," +

                "{\"SizeID\":\"2\",\"SizeName\":\"X\",\"SizeCode\":\"202\"}," +

                "{\"SizeID\":\"3\",\"SizeName\":\"XL\",\"SizeCode\":\"203\"}," +

                "{\"SizeID\":\"4\",\"SizeName\":\"L\",\"SizeCode\":\"204\"}]";

        return ReturnStr;
    }


    @Override
    public String Bill_Posting(String PDA_Number, String SignCode, String UserID, String BillName, String BillType,
                               String Primary_ID) {
        String ReturnStr = "{\"ErrorString\":\"此功能暂未开通 \"}";

        return ReturnStr;

    }

    @Override
    public String Bill_Print(String PDA_Number, String SignCode, String UserID, String BillName, String BillType,
                             String Primary_ID) {
        // 返回 上传后主表主键值为保存成功

        //返回　{\"ErrorString\":\"内容\"}　为失败

        String ReturnStr = "";


        return ReturnStr;

    }

    @Override
    public String Select_Bill_Master(String PDA_Number, String SignCode, String UserID, String BillName,
                                     String BillType, String Filter) {

//        //System.out.println("PDA_Number: " + PDA_Number + " ; SignCode :" + SignCode + "; UserID:" + UserID + " ; BillName:" + BillName + "  ; Filter:" + Filter);
        logger.info("进入Select_Bill_Master方法，查询在线单据主表-------- ");
        String columnName = "";
        String labName = "";
        String value = "";
        //开始时间
        String startTimeValue = "";
        //结束时间
        String endTimeValue = "";
        //单号
        String billValue = "";
        //往来单位
        String unitValue = "";
        //返回时单号
        String billIDValue = "";

        JSONArray jsonFilter = JSON.parseArray(Filter);
        for (int i = 0; i < jsonFilter.size(); i++) {
            JSONObject json = jsonFilter.getJSONObject(i);
            columnName = json.getString("ColumnName");
            labName = json.getString("LabName");
            value = json.getString("Value");

            if (columnName.equals("BillTime")) {
                //封装开始和结束时间
                //TODO:具体的需要做判断
                if (labName.equals("开始日期")) {
                    startTimeValue = value;
                } else if (labName.equals("结束日期")) {
                    endTimeValue = value;
                }
            } else if (columnName.equals("ClientID")) {
                unitValue = value;
            } else if (columnName.equals("BillNo")) {
                billValue = value;
            } else if (columnName.equals("BillID")) {
                billIDValue = value;
                billValue = billIDValue;
            }
        }
        //查询<采购入库单>
        if (StringUtils.isBlank(billValue)) {
            return "{\"ErrorString\":\"哦哦,请输入单号查询!!!!\"}";
        }
        if (columnName.equals("BillTime") || columnName.equals("BillNo") || columnName.equals("ClientID") || columnName.equals("BillID")) {
            String PDAColumnName = "Select_Bill_Master";

            String returnObj = storageService.querywares(startTimeValue, endTimeValue, billValue, billIDValue, unitValue, null, PDAColumnName);
            return returnObj;
        }

        return null;
    }

    @Override
    public String Select_Bill_Detail(String PDA_Number, String SignCode, String UserID, String BillName,
                                     String BillType, String MasterID) {
        logger.info("进入方法Select_Bill_Detail，查询在线单据从表 --- start");
        return storageService.queryProductOrderNumber(MasterID);
    }

    @Override
    public String Select_Bill_ColorSize(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String MasterID) {
        String ReturnStr = "[{\"BillType\":\"InOrder\",\"BillID\":\"1\",\"DetailID\":\"1\",\"GoodsID\":\"001\",\"ColorID\":\"001\",\"SizeID\":\"002\",\"GoodsName\":\"糖果\","
                + "\"ColorName\":\"白色\",\"SizeName\":\"X\",\"Qty\":\"3\",\"IsGift\":\"0\",\"CreateTime\":\"2017-12-26 11:05:01\",\"BatchNo\":\"\",\"ProductionDate\":\"\","
                + "\"ValidityDate\":\"\",\"CreateTime\":\"\",\"Price\":\"\"}," +
                "{\"BillType\":\"InOrder\",\"BillID\":\"1\",\"DetailID\":\"2\",\"GoodsID\":\"002\",\"ColorID\":\"001\",\"SizeID\":\"003\",\"GoodsName\":\"水果\",\"ColorName\":\"黄色\","
                + "\"SizeName\":\"L\",\"Qty\":\"8\",\"IsGift\":\"0\",\"CreateTime\":\"2017-12-26 11:05:01\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"CreateTime\":\"\",\"Price\":\"\"}]";
        return ReturnStr;
    }

    @Override
    public String Select_GoodsMeaage_Filter(String PDA_Number, String SignCode, String UserID) {

        return "{\"SelectFilter\":\"条码/BarCode,编码/GoodsCode,商品名称/GoodsName,规格/GoodsSpec\",\"DataGridColumnFormat\":\"编号/GoodsCode/200/String/-1/1&"
                + "名称/goodsname/200/String/-1/1|"
                + "规格/GoodsSpec/250/String/-1/1&条码/barcode/250/String/-1/1\",\"Detailed\":\"商品名称/GoodsName,编码/GoodsCode,规格/GoodsSpec,条码/BarCode,单位/UnitName,备注/Remark\"}";
    }

    @Override
    public String Select_GoodsMessage(String PDA_Number, String SignCode, String UserID, String Match, String Filter) {
        //Filter 的格式的 Json  "[{\"ColumnName\":\"\",\"LabName\":\"\",\"Value\":\"\"}," +
        // "{\"ColumnName\":\"\",\"LabName\":\"\",\"Value\":\"\"}," +
        // "{\"ColumnName\":\"\",\"LabName\":\"\",\"Value\":\"\"}]";

        String ReturnStr = "{ \"IDName\": \"GoodsID\",\"ValueName\": \"GoodsName\",\"RowCount\": \"2\"," +
                "\"TableMessage\":[" +
                "{\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsName\":\"糖果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"001\",\"URate\":\"1\",\"UnitName\":\"个\",\"BarCode\":\"123456\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +
                "{\"GoodsID\":\"002\",\"GoodsCode\":\"002\",\"GoodsName\":\"水果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"002\",\"URate\":\"1\",\"UnitName\":\"包\",\"BarCode\":\"111111\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +
                "]}";
        return ReturnStr;
    }

    @Override
    public String Select_GoodsMessage_Stock(String PDA_Number, String SignCode, String UserID, String GoodsID,
                                            String UnitID) {
        String ReturnStr = "[{\"WarehouseName\":\"总仓\",\"Number\":\"20\"}," +
                "{\"WarehouseName\":\"1号仓\",\"Number\":\"30\"}," +
                "{\"WarehouseName\":\"2号仓\",\"Number\":\"50\"}," +
                "{\"WarehouseName\":\"3号仓\",\"Number\":\"10\"}]";
        return ReturnStr;
    }

    @Override
    public String Get_ReportFormList(String PDA_Number, String SignCode, String UserID) {
        String ReturnStr = "[{\"FunctionName\":\"客户报表\",\"PrimaryColumn\":\"ClientID\",\"IsClickShowDetailed\":\"1\",\"SelectFilter\":\"日期/DTime/DateTime,"
                + "仓库/StoreID/ComBox,编号/Code/String,名称/ClientName/SelectText/客户名称|CName&联系人|Man&地址|Address,电话/Photo/String\","
                + "\"DataGridColumnFormat\":\"编号/Code/200/String/-1/1,名称/ClientName/200/String/-1/1,电话/Photo/150/String/-1/1,联系人/man/150/String/-1/1,"
                + "金额/Total/150/Double/2/1/1\"}," +

                "{\"FunctionName\":\"库存报表\",\"PrimaryColumn\":\"ClientID\",\"IsClickShowDetailed\":\"0\",\"SelectFilter\":\"\",\"DataGridColumnFormat\":\"\"}," +

                "{\"FunctionName\":\"零售报表\",\"PrimaryColumn\":\"ClientID\",\"IsClickShowDetailed\":\"0\",\"SelectFilter\":\"\",\"DataGridColumnFormat\":\"\"}]";

        return ReturnStr;


    }

    @Override
    public String Get_Report_Combox_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName) {
        String ReturnStr = "";

        switch (PDAColumnName.toLowerCase()) {
            case "storeid":
            case "storeid2":
                ReturnStr = "{ \"IDName\": \"Column1\",\"ValueName\": \"Column2\",\"DefaultValue\": \"002\"," +
                        "\"TableMessage\":[{\"Column1\":\"001\",\"Column2\":\"总仓\"}," +
                        "{\"Column1\":\"002\",\"Column2\":\"分仓1\"}," +
                        "{\"Column1\":\"003\",\"Column2\":\"BBBBBBBB\"}]}";
                break;

        }

        return ReturnStr;
    }

    @Override
    public String Get_Report_SelectText_List(String PDA_Number, String SignCode, String UserID, String ReportName, String PDAColumnName, String LabName, String Match, String Filter) {
        String ReturnStr = "{\"IDName\": \"GoodsID\",\"ValueName\": \"GoodsName\", \"DataGridColumnFormat\": \"编号/GoodsCode/120/String/-1/1,名称/GoodsName/150/String/-1/1,规格/GoodsSpec/150/String/-1/1,条码/BarCode/220/String/-1/1,库存/GoodsQty/220/Double/2/1/1\"," +
                "\"TableMessage\":[" +
                "{\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsName\":\"糖果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"001\",\"URate\":\"1\",\"UnitName\":\"个\",\"BarCode\":\"123456\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +
                "{\"GoodsID\":\"002\",\"GoodsCode\":\"002\",\"GoodsName\":\"水果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"002\",\"URate\":\"1\",\"UnitName\":\"包\",\"BarCode\":\"111111\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +
                "]}";
        return ReturnStr;
    }

    @Override
    public String Get_ReportForm_SelelctData(String PDA_Number, String SignCode, String UserID, String ReportName,
                                             String Match, String Filter) {
        String ReturnStr = "[{\"ClientID\":\"1\",\"Code\":\"0011\",\"ClientName\":\"客户1\",\"Photo\":\"135481487\",\"man\":\"张三\",\"Total\":\"100\"}," +

                "{\"ClientID\":\"2\",\"Code\":\"0012\",\"ClientName\":\"客户2\",\"Photo\":\"88975487\",\"man\":\"黄一\",\"Total\":\"200\"}]";

        return ReturnStr;


    }

    @Override
    public String Get_ReportForm_ClickDataRow(String PDA_Number, String SignCode, String UserID, String ReportName,
                                              String PrimaryID, String SelectIndex, String SelectData, String Match, String Filter) {
        String ReturnStr = "{ \"DataGridColumnFormat\": \"编号/GoodsCode/120/String/-1/1,名称/GoodsName/150/String/-1/1,规格/GoodsSpec/150/String/-1/1,"
                + "条码/BarCode/220/String/-1/1,库存/GoodsQty/220/Double/2/1/1\"," +

                "\"TableMessage\":[" +

                "{\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsName\":\"糖果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"001\",\"URate\":\"1\",\"UnitName\":\"个\","
                + "\"BarCode\":\"123456\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\","
                + "\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                "{\"GoodsID\":\"002\",\"GoodsCode\":\"002\",\"GoodsName\":\"水果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"002\",\"URate\":\"1\",\"UnitName\":\"包\","
                + "\"BarCode\":\"111111\",\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\","
                + "\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +

                "]}";

        return ReturnStr;


    }

    @Override
    public String Get_Set_AddMessage(String PDA_Number, String SignCode, String UserID) {
        String ReturnStr = "[{\"FunctionName\": \"商品资料\",\"IsNew\": \"1\",\"IsChange\": \"1\",\"PrimaryColumn\":\"GoodsID\",\"Change_SelectFilter\": \"编号/Code,名称/Name,"
                + "规格/Spec,条码/BarCode\",\"Change_ColumnFormat\": \"编号/Code/120/String/-1/1,名称/Name/150/String/-1/1,规格/Spec/150/String/-1/1,条码/BarCode/220/String/-1/1,"
                + "库存/GoodsQty/220/Double/2/1\"}," +

                "{\"FunctionName\": \"客户档案\",\"IsNew\": \"1\",\"IsChange\": \"0\",\"PrimaryColumn\":\"ClientID\",\"Change_SelectFilter\": \"\","
                + "\"Change_ColumnFormat\": \"\"}," +

                "{\"FunctionName\": \"供应商档案\",\"IsNew\": \"1\",\"IsChange\": \"0\",\"PrimaryColumn\":\"SupID\",\"Change_SelectFilter\": \"\","
                + "\"Change_ColumnFormat\": \"\"}]";


        return ReturnStr;


    }

    @Override
    public String Get_Ctrl_AddMessage(String PDA_Number, String SignCode, String UserID, String FunctionName) {
        String ReturnStr = "";


        if (FunctionName == "商品资料")

        {

            ReturnStr = "[{ \"RowNo\": \"1\",\"ColumnName\": \"Code\", \"ShowName\":\"编号\", \"DataType\": \"\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", "
                    + "\"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\",\"ChangeReadOnly\": \"1\" }," +

                    "{ \"RowNo\": \"2\",\"ColumnName\": \"Name\", \"ShowName\":\"名称\", \"DataType\": \"\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", "
                    + "\"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\",\"ChangeReadOnly\": \"0\" }," +

                    "{ \"RowNo\": \"3\",\"ColumnName\": \"Spec\", \"ShowName\":\"规格\", \"DataType\": \"\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", "
                    + "\"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\",\"ChangeReadOnly\": \"0\" }," +

                    "{ \"RowNo\": \"4\",\"ColumnName\": \"UnitID\", \"ShowName\":\"单位\", \"DataType\": \"\", \"CtrlType\": \"ComBox\", \"SelectCtrl_DisplayField\": \"\","
                    + " \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\",\"ChangeReadOnly\": \"0\" }," +

                    "{ \"RowNo\": \"5\",\"ColumnName\": \"BarCode\", \"ShowName\":\"条码\", \"DataType\": \"\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\", "
                    + "\"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\",\"ChangeReadOnly\": \"0\" }," +

                    "{ \"RowNo\": \"6\",\"ColumnName\": \"ClientID\", \"ShowName\":\"默认供应商\", \"DataType\": \"\", \"CtrlType\": \"SelectText\","
                    + " \"SelectCtrl_DisplayField\": \"编号/ClientCode/150/String/-1/1|名称/ClientName/150/String/-1/1|联系人/Contacts/150/String/-1/1|电话/Phone/150/String/-1/1\", "
                    + "\"SelectCtrl_Filer\": \"编号/ClientCode,名称/ClientName,联系人/Contacts,电话/Phone\", \"DefaultValue\": \"\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\","
                    + "\"ChangeReadOnly\": \"0\" }," +

                    "{ \"RowNo\": \"7\",\"ColumnName\": \"BuyPrice\", \"ShowName\":\"进价\", \"DataType\": \"Double\", \"CtrlType\": \"Text\", "
                    + "\"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\","
                    + "\"ChangeReadOnly\": \"0\" }," +

                    "{ \"RowNo\": \"8\",\"ColumnName\": \"SalePrice\", \"ShowName\":\"售价\", \"DataType\": \"Double\", \"CtrlType\": \"Text\", "
                    + "\"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"0\",\"IsNotNull\": \"1\",\"IsNotEmpty\": \"0\" ,"
                    + "\"ChangeReadOnly\": \"0\"}," +

                    "{ \"RowNo\": \"9\",\"ColumnName\": \"Remark\", \"ShowName\":\"备注\", \"DataType\": \"\", \"CtrlType\": \"Text\", \"SelectCtrl_DisplayField\": \"\","
                    + " \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"\",\"IsNotNull\": \"0\",\"IsNotEmpty\": \"0\" ,\"ChangeReadOnly\": \"0\"}," +

                    "{ \"RowNo\": \"10\",\"ColumnName\": \"CreateTime\", \"ShowName\":\"日期\", \"DataType\": \"DateTime\", \"CtrlType\": \"Text\", "
                    + "\"SelectCtrl_DisplayField\": \"\", \"SelectCtrl_Filer\": \"\", \"DefaultValue\": \"GetDateTime\",\"IsNotNull\": \"0\",\"IsNotEmpty\": \"0\" ,"
                    + "\"ChangeReadOnly\": \"0\"}" +

                    "]";

        }

        return ReturnStr;


    }

    @Override
    public String Get_AddMessage_ComBox(String PDA_Number, String SignCode, String UserID, String FunctionName,
                                        String ColumnName) {
        String ReturnStr = "";

        if (ColumnName == "UnitID")

        {

            ReturnStr = "{ \"IDName\": \"UnitID\",\"ValueName\": \"UnitName\",\"RowCount\": \"2\"," +

                    "\"TableMessage\":[" +

                    "{\"UnitID\":\"001\",\"UnitName\":\"包\"}," +

                    "{\"UnitID\":\"002\",\"UnitName\":\"件\"}" +

                    "]}";

        }


        return ReturnStr;


    }

    @Override
    public String Get_AddMessage_SelectText(String PDA_Number, String SignCode, String UserID, String FunctionName,
                                            String ColumnName, String Match, String Filter) {
        String ReturnStr = "";

        if (ColumnName == "ClientID")

        {

            ReturnStr = "{ \"IDName\": \"ClientID\",\"ValueName\": \"ClientName\",\"RowCount\": \"4\"," +

                    "\"TableMessage\":[" +

                    "{\"ClientID\":\"101\",\"ClientCode\":\"101\",\"ClientName\":\"供应商1\",\"Contacts\":\"联系人\",\"Phone\":\"1452452\",\"Address\":\"地址\",\"Remark\":\"备注\","
                    + "\"Receivable\":\"0\",\"Payable\":\"200\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                    "{\"ClientID\":\"001\",\"ClientCode\":\"001\",\"ClientName\":\"供应商2\",\"Contacts\":\"联系人\",\"Phone\":\"12323\",\"Address\":\"地址\",\"Remark\":\"备注\","
                    + "\"Receivable\":\"300\",\"Payable\":\"0\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                    "{\"ClientID\":\"002\",\"ClientCode\":\"002\",\"ClientName\":\"供应商3\",\"Contacts\":\"联系人\",\"Phone\":\"13454543314\",\"Address\":\"地址\","
                    + "\"Remark\":\"备注\",\"Receivable\":\"700\",\"Payable\":\"0\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                    "{\"ClientID\":\"003\",\"ClientCode\":\"003\",\"ClientName\":\"供应商4\",\"Contacts\":\"联系人\",\"Phone\":\"95655\",\"Address\":\"地址\",\"Remark\":\"备注\","
                    + "\"Receivable\":\"150\",\"Payable\":\"0\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +

                    "]}";

        }


        if (FunctionName == "商品资料" && ColumnName == "修改查询")

        {

            ReturnStr = "{ \"IDName\": \"GoodsID\",\"ValueName\": \"GoodsName\",\"RowCount\": \"2\"," +

                    "\"TableMessage\":[" +

                    "{\"GoodsID\":\"001\",\"Code\":\"001\",\"Name\":\"糖果\",\"Spec\":\"规格\",\"UnitID\":\"001\",\"URate\":\"1\",\"UnitName\":\"个\",\"BarCode\":\"123456\","
                    + "\"BuyPrice\":\"5\",\"SalePrice\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\","
                    + "\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}," +

                    "{\"GoodsID\":\"002\",\"Code\":\"002\",\"Name\":\"水果\",\"Spec\":\"规格\",\"UnitID\":\"002\",\"URate\":\"1\",\"UnitName\":\"包\",\"BarCode\":\"111111\","
                    + "\"BuyPrice\":\"5\",\"SalePrice\":\"12\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\","
                    + "\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\",\"obj5\":\"\"}" +

                    "]}";

        }

        return ReturnStr;

    }

    @Override
    public String Save_AddMessage(String PDA_Number, String SignCode, String UserID, String FunctionName,
                                  String AddOrChange, String Primary_ID, String JsonData) {
        String ReturnStr = "";


        if (AddOrChange == "Add")

        {

            ReturnStr = "OK";

        } else

        {

            ReturnStr = "{\"ErrorString\":\"编号重复\"}";

        }

        return ReturnStr;


    }

    @Override
    public String Get_Check_Select_SourceBill(String PDA_Number, String SignCode, String UserID, String BillName,
                                              String SourceBillName) {
        //SourceBillName 上级单据名称


        String ReturnStr = "";

        if (BillName == "采购订单" && SourceBillName == "采购申请单") {
            ReturnStr = "{\"MasterRelationList\":\"\",\"DetailRelationList\":\"BillID/String6,BillType/String7,DetailID/String8\","
                    + "\"SelectFilter\":\"日期/DTime/DateTime,仓库/StoreID/ComBox,编号/Code/String,名称/ClientName/SelectText/客户名称|CName&联系人|Man&地址|Address,电话/Photo/String\"," +
                    "\"DataGridColumnFormat\":\"编号/GoodsCode/200/String/-1/1,名称/GoodsName/200/String/-1/1,规格/GoodsSpec/150/String/-1/1,数量/Qty/150/String/-1/1,"
                    + "金额/Total/150/Double/2/1/1\"}";


        }


        return ReturnStr;
    }

    @Override
    public String Get_Check_Select_SourceBill_Combox_List(String PDA_Number, String SignCode, String UserID,
                                                          String BillName, String SourceBillName, String PDAColumnName, String LabName) {
        String ReturnStr = "";

        switch (PDAColumnName.toLowerCase()) {
            case "storeid":
            case "storeid2":
                ReturnStr = "{ \"IDName\": \"Column1\",\"ValueName\": \"Column2\",\"DefaultValue\": \"002\"," +
                        "\"TableMessage\":[{\"Column1\":\"001\",\"Column2\":\"总仓\"}," +
                        "{\"Column1\":\"002\",\"Column2\":\"分仓1\"}," +
                        "{\"Column1\":\"003\",\"Column2\":\"BBBBBBBB\"}]}";
                break;

        }

        return ReturnStr;
    }

    @Override
    public String Get_Check_Select_SourceBill_SelectText_List(String PDA_Number, String SignCode, String UserID,
                                                              String BillName, String SourceBillName, String PDAColumnName, String LabName, String Match, String Filter) {
        String ReturnStr = "{\"IDName\": \"GoodsID\",\"ValueName\": \"GoodsName\", \"DataGridColumnFormat\": \"编号/GoodsCode/120/String/-1/1,名称/GoodsName/150/String/-1/1,"
                + "规格/GoodsSpec/150/String/-1/1,条码/BarCode/220/String/-1/1,库存/GoodsQty/220/Double/2/1/1\"," +
                "\"TableMessage\":[" +
                "{\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsName\":\"糖果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"001\",\"URate\":\"1\",\"UnitName\":\"个\",\"BarCode\":\"123456\","
                + "\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\","
                + "\"obj5\":\"\"}," +
                "{\"GoodsID\":\"002\",\"GoodsCode\":\"002\",\"GoodsName\":\"水果\",\"GoodsSpec\":\"规格\",\"UnitID\":\"002\",\"URate\":\"1\",\"UnitName\":\"包\",\"BarCode\":\"111111\","
                + "\"BuyPrice\":\"5\",\"SalePrice1\":\"11\",\"SalePrice2\":\"12\",\"SalePrice3\":\"13\",\"GoodsQty\":\"20\",\"Remark\":\"商品备注\",\"obj1\":\"\",\"obj2\":\"\",\"obj3\":\"\",\"obj4\":\"\","
                + "\"obj5\":\"\"}" +
                "]}";
        return ReturnStr;
    }

    @Override
    public String Check_Select_Bill_Master(String PDA_Number, String SignCode, String UserID, String BillName,
                                           String SourceBillName, String Filter) {
        String ReturnStr = "[{\"BillID\":\"1\",\"BillType\":\"InOrder\",\"BillNo\":\"Test0001\",\"BillTime\":\"2017-12-26\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\","
                + "\"StoreName2\":\"\",\"ClientID\":\"002\",\"ClientName\":\"客户2\",\"EmpID\":\"\",\"EmpName\":\"\",\"Remark\":\"\",\"CollectAmount\":\"0\",\"NoSmallAmount\":\"0\","
                + "\"CreateID\":\"001\",\"CreateName\":\"张三1\",\"CreateTime\":\"2017-12-26 11:05:01\",\"Comment\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\","
                + "\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
                "{\"BillID\":\"2\",\"BillType\":\"InOrder\",\"BillNo\":\"Test0002\",\"BillTime\":\"2017-12-26\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\",\"StoreName2\":\"\","
                + "\"ClientID\":\"002\",\"ClientName\":\"客户2\",\"EmpID\":\"\",\"EmpName\":\"\",\"Remark\":\"\",\"CollectAmount\":\"0\",\"NoSmallAmount\":\"0\",\"CreateID\":\"002\","
                + "\"CreateName\":\"张三2\",\"CreateTime\":\"2017-12-26 11:05:01\",\"Comment\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\","
                + "\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
                "{\"BillID\":\"3\",\"BillType\":\"InOrder\",\"BillNo\":\"Test0003\",\"BillTime\":\"2017-12-26\",\"StoreID\":\"002\",\"StoreName\":\"分仓1\",\"StoreID2\":\"\",\"StoreName2\":\"\","
                + "\"ClientID\":\"002\",\"ClientName\":\"客户2\",\"EmpID\":\"\",\"EmpName\":\"\",\"Remark\":\"\",\"CollectAmount\":\"0\",\"NoSmallAmount\":\"0\",\"CreateID\":\"003\","
                + "\"CreateName\":\"张三3\",\"CreateTime\":\"2017-12-26 11:05:01\",\"Comment\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\","
                + "\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}]";

        // ReturnStr = "[]";

        return ReturnStr;
    }

    @Override
    public String Check_Select_Bill_Detail_Other(String PDA_Number, String SignCode, String UserID, String BillName,
                                                 String SourceBillName, String Filter) {
//        String ReturnStr = "[{\"DetailID\":\"1\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsSpec\":\"\","
//                + "\"GoodsName\":\"糖果\",\"BarCode\":\"123456\",\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\",\"Qty\":\"3\",\"Discount\":\"100\","
//                + "\"Price\":\"6\",\"Amount\":\"18\",\"Remark\":\"\",\"IsGift\":\"0\",\"CreateTime\":\"2017-12-26 11:05:01\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\","
//                + "\"StoreName2\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\","
//                + "\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
//                "{\"DetailID\":\"2\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"002\",\"GoodsCode\":\"001\",\"GoodsSpec\":\"\",\"GoodsName\":\"水果\","
//                + "\"BarCode\":\"111111\",\"UnitID\":\"002\",\"UnitName\":\"件\",\"URate\":\"1\",\"Qty\":\"8\",\"Discount\":\"100\",\"Price\":\"6\",\"Amount\":\"48\","
//                + "\"Remark\":\"\",\"IsGift\":\"0\",\"CreateTime\":\"2017-12-26 11:05:01\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\",\"StoreName2\":\"\",\"BatchNo\":\"\","
//                + "\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\","
//                + "\"Number4\":\"\","
//                + "\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}]";

        //ReturnStr = "";

        return null;
    }

    @Override
    public String Check_Select_Bill_Detail(String PDA_Number, String SignCode, String UserID, String BillName,
                                           String SourceBillName, String MasterID) {
//        String ReturnStr = "[{\"DetailID\":\"1\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsSpec\":\"\",\"GoodsName\":\"糖果\","
//                + "\"BarCode\":\"123456\",\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\",\"Qty\":\"3\",\"Discount\":\"100\",\"Price\":\"6\",\"Amount\":\"18\",\"Remark\":\"\",\"IsGift\":\"0\","
//                + "\"CreateTime\":\"2017-12-26 11:05:01\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\",\"StoreName2\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\","
//                + "\"ValidityDate\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\","
//                + "\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
//                "{\"DetailID\":\"2\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"002\",\"GoodsCode\":\"001\",\"GoodsSpec\":\"\",\"GoodsName\":\"水果\",\"BarCode\":\"111111\","
//                + "\"UnitID\":\"002\",\"UnitName\":\"件\",\"URate\":\"1\",\"Qty\":\"8\",\"Discount\":\"100\",\"Price\":\"6\",\"Amount\":\"48\",\"Remark\":\"\",\"IsGift\":\"0\","
//                + "\"CreateTime\":\"2017-12-26 11:05:01\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\",\"StoreName2\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\","
//                + "\"ValidityDate\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\","
//                + "\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}]";
//
//        if (MasterID == "2") {
//            ReturnStr = "[{\"DetailID\":\"1\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"001\",\"GoodsCode\":\"001\",\"GoodsSpec\":\"\",\"GoodsName\":\"糖果\",\"BarCode\":\"123456\","
//                    + "\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\",\"Qty\":\"10\",\"Discount\":\"100\",\"Price\":\"7\",\"Amount\":\"70\",\"Remark\":\"\",\"IsGift\":\"0\","
//                    + "\"CreateTime\":\"2017-12-26 11:05:01\",\"StoreID\":\"001\",\"StoreName\":\"总仓\",\"StoreID2\":\"\",\"StoreName2\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\","
//                    + "\"ValidityDate\":\"\",\"Int1\":\"\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\","
//                    + "\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}]";
//
//
//        }

        //ReturnStr = "";

        return null;
    }

    @Override
    public String Check_Select_Bill_ColorSize(String PDA_Number, String SignCode, String UserID, String BillName,
                                              String SourceBillName, String MasterID) {
        String ReturnStr = "[{\"BillType\":\"InOrder\",\"BillID\":\"1\",\"DetailID\":\"1\",\"GoodsID\":\"001\",\"ColorID\":\"001\",\"SizeID\":\"002\",\"GoodsName\":\"糖果\","
                + "\"ColorName\":\"白色\",\"SizeName\":\"X\",\"Qty\":\"3\",\"IsGift\":\"0\",\"CreateTime\":\"2017-12-26 11:05:01\",\"BatchNo\":\"\",\"ProductionDate\":\"\","
                + "\"ValidityDate\":\"\",\"CreateTime\":\"\",\"Price\":\"\"}," +
                "{\"BillType\":\"InOrder\",\"BillID\":\"1\",\"DetailID\":\"2\",\"GoodsID\":\"002\",\"ColorID\":\"001\",\"SizeID\":\"003\",\"GoodsName\":\"水果\",\"ColorName\":\"黄色\","
                + "\"SizeName\":\"L\",\"Qty\":\"8\",\"IsGift\":\"0\",\"CreateTime\":\"2017-12-26 11:05:01\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"CreateTime\":\"\","
                + "\"Price\":\"\"}]";
        return ReturnStr;
    }

    @Override
    public String Add_Goods_Detail_Obj(String PDA_Number, String SignCode, String UserID, String BillName,
                                       String BillType, String GoodsID, String Number, String OtherOnlyColumn) {
        //OtherOnlyColumn 格式为  "ColumnName|值,ColumnName|值"


        String ReturnStr = "[{\"DetailID\":\"0\",\"BillID\":\"0\",\"BillType\":\"\",\"GoodsID\":\"1004\",\"GoodsCode\":\"104\",\"GoodsSpec\":\"\","
                + "\"GoodsName\":\"包件1\",\"BarCode\":\"123456\",\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\",\"Qty\":\"2\",\"Remark\":\"\","
                + "\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"Int1\":\"2\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\","
                + "\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
                "{\"DetailID\":\"0\",\"BillID\":\"0\",\"BillType\":\"\",\"GoodsID\":\"1005\",\"GoodsCode\":\"105\",\"GoodsSpec\":\"\",\"GoodsName\":\"包件2\","
                + "\"BarCode\":\"111111\",\"UnitID\":\"002\",\"UnitName\":\"件\",\"URate\":\"1\",\"Qty\":\"8\",\"Remark\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\","
                + "\"ValidityDate\":\"\",\"Int1\":\"4\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\","
                + "\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}]";


        return ReturnStr;
    }

    @Override
    public String Add_Goods_Detail_Obj_Piece(String PDA_Number, String SignCode, String UserID, String BillName,
                                             String GoodsID, String Number, String OtherOnlyColumn, String Match, String Filter) {
        //OtherOnlyColumn 格式为  "ColumnName|值,ColumnName|值"

        String ReturnStr = "[{\"DetailID\":\"0\",\"BillID\":\"0\",\"BillType\":\"\",\"GoodsID\":\"1006\",\"GoodsCode\":\"106\",\"GoodsSpec\":\"\",\"GoodsName\":\"包件3\","
                + "\"BarCode\":\"123456\",\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\",\"Qty\":\"2\",\"Remark\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\","
                + "\"Int1\":\"1\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\","
                + "\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
                "{\"DetailID\":\"0\",\"BillID\":\"0\",\"BillType\":\"\",\"GoodsID\":\"1007\",\"GoodsCode\":\"107\",\"GoodsSpec\":\"\",\"GoodsName\":\"包件4\",\"BarCode\":\"111111\","
                + "\"UnitID\":\"002\",\"UnitName\":\"件\",\"URate\":\"1\",\"Qty\":\"8\",\"Remark\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"Int1\":\"2\","
                + "\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\","
                + "\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}]";


        return ReturnStr;
    }

    @Override
    public String Select_Bill_Detail_Obj(String PDA_Number, String SignCode, String UserID, String BillName,
                                         String BillType, String MasterID) {
        String ReturnStr = "[{\"DetailID\":\"1\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"1006\",\"GoodsCode\":\"106\",\"GoodsSpec\":\"\",\"GoodsName\":\"包件3\","
                + "\"BarCode\":\"123456\",\"UnitID\":\"001\",\"UnitName\":\"个\",\"URate\":\"1\",\"Qty\":\"3\",\"Remark\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\","
                + "\"Int1\":\"1\",\"Int2\":\"\",\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\","
                + "\"String2\":\"\",\"String3\":\"\",\"String4\":\"\",\"String5\":\"\"}," +
                "{\"DetailID\":\"2\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"1007\",\"GoodsCode\":\"107\",\"GoodsSpec\":\"\",\"GoodsName\":\"包件4\",\"BarCode\":\"111111\","
                + "\"UnitID\":\"002\",\"UnitName\":\"件\",\"URate\":\"1\",\"Qty\":\"8\",\"Remark\":\"\",\"BatchNo\":\"\",\"ProductionDate\":\"\",\"ValidityDate\":\"\",\"Int1\":\"2\",\"Int2\":\"\","
                + "\"Int3\":\"\",\"Int4\":\"\",\"Int5\":\"\",\"Number1\":\"\",\"Number2\":\"\",\"Number3\":\"\",\"Number4\":\"\",\"Number5\":\"\",\"String1\":\"\",\"String2\":\"\",\"String3\":\"\","
                + "\"String4\":\"\",\"String5\":\"\"}]";


        return ReturnStr;
    }

    @Override
    public String Select_SerialNumber(String PDA_Number, String SignCode, String UserID, String BillName,
                                      String BillType, String SNCode, String ClientID, String StoreID, String M_RowJson) {
        //扫描内容不是序列号时，GoodsID为空 {\"GoodsID\":\"\",\"StoreID\":\"\"}
        String ReturnStr = "{\"GoodsID\":\"001\",\"StoreID\":\"002\"}";
        return ReturnStr;
    }


    @Override
    public String Goods_Is_SerialNumber(String PDA_Number, String SignCode, String UserID, String BillName,
                                        String BillType, String GoodsID) {
        //返回 "1"　为序列号商品
        String ReturnStr = "1";
        return ReturnStr;
    }

    @Override
    public String Select_Goods_SerialNumber(String PDA_Number, String SignCode, String UserID, String BillName,
                                            String BillType, String GoodsID, String ClientID, String StoreID) {
        //没有值时返回 []
        String ReturnStr = "[{\"SerialNumber\":\"1800001\"},{\"SerialNumber\":\"1800002\"},{\"SerialNumber\":\"1800003\"},{\"SerialNumber\":\"1800004\"}]";
        return ReturnStr;
    }

    @Override
    public String Select_SerialNumber_IsAdd(String PDA_Number, String SignCode, String UserID, String BillName,
                                            String BillType, String SNCode, String GoodsID, String ClientID, String StoreID, String M_RowJson,
                                            String D_RowJson) {
        //返回 "1" 为可以录入
        String ReturnStr = "1";
        return ReturnStr;
    }

    @Override
    public String Select_Bill_Detail_SN(String PDA_Number, String SignCode, String UserID, String BillName,
                                        String BillType, String MasterID) {
        String ReturnStr = "[{\"DetailID\":\"1\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"001\",\"GoodsName\":\"糖果\",\"SNCode\":\"1001010\",\"Qty\":\"1\",\"Remark\":\"\"}," +
                "{\"DetailID\":\"2\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"002\",\"GoodsName\":\"糖果\",\"SNCode\":\"2020202\",\"Qty\":\"1\",\"Remark\":\"\"}]";

        if (MasterID == "2") {
            ReturnStr = "[{\"DetailID\":\"1\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"001\",\"GoodsName\":\"糖果\",\"SNCode\":\"666666\",\"Qty\":\"1\",\"Remark\":\"\"}," +
                    "{\"DetailID\":\"2\",\"BillID\":\"1\",\"BillType\":\"InOrder\",\"GoodsID\":\"002\",\"GoodsName\":\"水果\",\"SNCode\":\"7777777\",\"Qty\":\"1\",\"Remark\":\"\"}]";

        }

        //ReturnStr = "";

        return ReturnStr;
    }

    @Override
    public void Down_Data(String PDA_Number, String SignCode, String UserID, String DownList) {

		/*

		//DownList 格式为 [{\"BillName\":\"\",\"IsType\":\"条码\",\"ColumnName\":\"BarCode\"},{\"BillName\":\"\",\"IsType\":\"库存\",\"ColumnName\":\"StoreQty\"}]
        WebClass.CreateTxt("DownProgressSum" + PDA_Number, "0/0", Server.MapPath("/"));
        WebClass.CreateTxt("DownProgress" + PDA_Number, "0/0", Server.MapPath("/"));
        System.Data.SQLite.SQLiteConnection _sqliteConn = null;
        System.Data.SqlClient.SqlConnection _Conn = null;
        try
        {
            Newtonsoft.Json.Linq.JArray D_jArray = (Newtonsoft.Json.Linq.JArray)JsonConvert.DeserializeObject(DownList);
            //用法

            if (D_jArray.Count > 0)
            {

                System.IO.File.Delete(Server.MapPath("/") + "WebSqlite" + PDA_Number + ".db");

                _Conn = new System.Data.SqlClient.SqlConnection("Data Source=192.168.1.111,1433;User Id=sa;Initial Catalog=Framework2;Password=123456789");
                _Conn.Open();

                System.Data.SqlClient.SqlDataAdapter _DataAdapter = new System.Data.SqlClient.SqlDataAdapter("", _Conn);


                _sqliteConn = new System.Data.SQLite.SQLiteConnection("data source=" + Server.MapPath("/") + "WebSqlite" + PDA_Number + ".db");


                _sqliteConn.Open();



                System.Data.SQLite.SQLiteCommand sqlite_Comm = new System.Data.SQLite.SQLiteCommand("", _sqliteConn);

                sqlite_Comm.CommandText = "Create Table [RelationTable] ([Relation_BillName] varchar(4000),[Relation_Column] varchar(4000),[TableName] varchar(4000))";
                sqlite_Comm.ExecuteNonQuery();

                for (int i = 0; i < D_jArray.Count; i++)
                {
                    WebClass.CreateTxt("DownProgressSum" + PDA_Number, "" + (i + 1) + "/" + D_jArray.Count + "", Server.MapPath("/"));


                    string _TableName = "";
                    string _SelectSql = "";
                    switch (D_jArray[i]["IsType"].ToString())
                    {
                        case "条码":
                            _TableName = "GoodsBarCode";
                            _SelectSql = "select GoodsID,GoodsCode,GoodsName,GoodsSpec,UnitID,UnitName,URate,BarCode,Remark,GoodsQty,0 Int1,0 Int2,0 Int3,0.0 Number1,0.0 Number2,0.0 Number3,'' String1,'' String2,'' String3,'' String4,'' String5 from Goods";
                            break;
                        default:
                            switch (D_jArray[i]["ColumnName"].ToString())
                            {
                                case "ClientID":
                                    _TableName = "Client";
                                    _SelectSql = "select ClientID,ClientCode,ClientName,Contacts,Phone,Address,Remark,0 Int1,0 Int2,0 Int3,0.0 Number1,0.0 Number2,0.0 Number3,'' String1,'' String2,'' String3,'' String4,'' String5 from Client";
                                    break;
                                case "StoreID":
                                case "StoreID2":
                                    _TableName = "Warehouse";
                                    _SelectSql = "select WarehouseID as Column1,WarehouseName Column2 from Warehouse";
                                    break;
                                case "EmpID":
                                    _TableName = "EmpTable";
                                    _SelectSql = "select EmpID Column1,EmpName Column2 from [Salesman]";
                                    break;
                                case "GoodsID":
                                    _TableName = "GoodsBarCode";
                                    _SelectSql = "select GoodsID,GoodsCode,GoodsName,GoodsSpec,UnitID,UnitName,URate,BarCode,Remark,GoodsQty,0 Int1,0 Int2,0 Int3,0.0 Number1,0.0 Number2,0.0 Number3,'' String1,'' String2,'' String3,'' String4,'' String5 from Goods";
                                    break;
                                case "Price":
                                    _TableName = "GoodsPrice";
                                    _SelectSql = "select GoodsID,UnitID,PriceType,Price from GoodsPrice";
                                    break;
                                case "UnitID":
                                    _TableName = "GoodsUnit";
                                    _SelectSql = "select GoodsID,UnitID,UnitName,URate from GoodsUnit";
                                    break;
                                case "BatchNo":
                                    _TableName = "BatchNo";
                                    _SelectSql = "select '' FBatchNo,'' FQty,'' ProductionDate,'' ValidityDate";

                                    break;
                                case "SizeID":
                                    _TableName = "SizeTable";
                                    _SelectSql = "select '001' GoodsID,0 SizeID,'S01' SizeCode,'38' SizeName";
                                    break;
                                case "ColorID":
                                    _TableName = "ColorTable";
                                    _SelectSql = "select '001' GoodsID,0 ColorID,'C01' ColorCode,'金色' ColorName";
                                    break;
                                case "StoreQty":
                                    _TableName = "GoodsStore";
                                    _SelectSql = "select GoodsID,StoreID,WarehouseName ,Qty Number from GoodsStore left join Warehouse on GoodsStore.StoreID= Warehouse.WarehouseID";
                                    break;
                                case "ColorSizeQty":
                                    _TableName = "ColorSizeStore";
                                    _SelectSql = "select GoodsID,StoreID,'' ColorID,'' SizeID,WarehouseName ,Qty Number from GoodsStore left join Warehouse on GoodsStore.StoreID= Warehouse.WarehouseID";
                                    break;
                                case "IsSN":
                                    _TableName = "GoodsIsSN";
                                    _SelectSql = "select '001' GoodsID,'1' IsSN";
                                    break;
                                case "SN":
                                    _TableName = "GoodsSN";
                                    _SelectSql = "select '' GoodsID,'' StoreID,'' SN";

                                    break;
                            }
                            break;
                    }

                    if (_TableName != "" && _SelectSql != "")
                    {
                        sqlite_Comm.CommandText = "SELECT count(*) AAC FROM sqlite_master WHERE type='table' and name='" + _TableName + "' COLLATE NOCASE";
                        int _Count = Convert.ToInt32(sqlite_Comm.ExecuteScalar());
                        if (_Count > 0)
                        {
                            sqlite_Comm.CommandText = "Update [RelationTable] set Relation_BillName=Relation_BillName||'<" + D_jArray[i]["BillName"].ToString() + ">',Relation_Column=Relation_Column||'<" + D_jArray[i]["ColumnName"].ToString() + ">' where [TableName]='" + _TableName + "'";
                            sqlite_Comm.ExecuteNonQuery();
                        }
                        else
                        {
                            DataTable _table = new DataTable();
                            _DataAdapter.SelectCommand.CommandText = _SelectSql;
                            _DataAdapter.Fill(_table);
                            WebClass.Table_InsertToSqlite(PDA_Number, ref sqlite_Comm, _table, _TableName, D_jArray[i]["BillName"].ToString(), D_jArray[i]["ColumnName"].ToString(), Server.MapPath("/"));
                            _table.Dispose();
                            _table = null;
                        }
                    }



                }

                _sqliteConn.Close();
                _Conn.Close();

                WebClass.CreateTxt("DownProgressSum" + PDA_Number, "下载完成", Server.MapPath("/"));




            }
        }
        catch (Exception ee)
        {
            try
            {
                if (_Conn != null)
                    _Conn.Close();
            }
            catch { }
            try
            {
                if (_sqliteConn != null)
                    _sqliteConn.Close();
            }
            catch { }
            WebClass.CreateTxt("DownProgressSum" + PDA_Number, "<错误>" + ee.Message, Server.MapPath("/"));
        }
        */
    }

    @Override
    public String Down_Data_Progress(String PDA_Number, String SignCode, String UserID, String IsNewDown) {
        //current
        String ReturnStr = "";
/*
        string DownProgressSum = WebClass.ReadFileTxt("DownProgressSum" + PDA_Number, Server.MapPath("/"));
       string DownProgress = WebClass.ReadFileTxt("DownProgress" + PDA_Number, Server.MapPath("/"));

       if (DownProgressSum.Contains("<错误>"))
        {
          ReturnStr = "{\"ErrorString\":\"" + DownProgressSum + "\"}";
        }
        else
       {
            if (DownProgressSum == "下载完成" || IsNewDown == "否")
            {
                ReturnStr = "{\"CurrentProgress\":\"\",\"DownType\":\"HTTP\",\"DownAddress\":\"http://192.168.1.111:9089//WebSqlite" + PDA_Number + ".db\"}";
            }
            else
            {
                ReturnStr = "{\"CurrentProgress\":\"" + DownProgressSum + "|" + DownProgress + "\",\"DownType\":\"\",\"DownAddress\":\"\"}";
            }
        }
*/
        return ReturnStr;
    }

    @Override
    public String Bill_SumColumnList(String PDA_Number, String SignCode, String UserID, String BillName) {
        //格式  中文名/列名,中文名/列名,中文名/列名
        String ReturnStr = "";
        if (BillName == "采购订单") {
            ReturnStr = "数量/Qty,重量/Int1";
        }


        return ReturnStr;
    }

    @Override
    public String Set_OtherBarCode(String PDA_Number, String SignCode, String UserID) {
        //ImmediatelyUpload为1时，在线模式下每添加一次上传一次

        String ReturnStr = "";

        ReturnStr = "[{\"FunctionName\":\"条码采集\",\"ImmediatelyUpload\":\"0\"}," +
                "{\"FunctionName\":\"货位采集\",\"ImmediatelyUpload\":\"1\"}," +
                "{\"FunctionName\":\"其他功能\",\"ImmediatelyUpload\":\"0\"}]";


        return ReturnStr;
    }


    @Override
    public String Set_OtherBarCode_Ctrl(String PDA_Number, String SignCode, String UserID, String FunctionName) {
        //DataType这个参数值范围　String1~String10，Int1~Int5，Float1~Float5

        String ReturnStr = "";
        ReturnStr = "[{\"LabName\":\"货架号\",\"DataType\":\"String1\",\"DefaultValue\":\"\", \"IsShow\": \"1\", \"IsNotNull\": \"0\" ,\"IsNotEmpty\": \"0\" ,\"IsOnly\": \"1\" ,\"IsSum\": \"0\"}," +
                "{\"LabName\":\"条码\",\"DataType\":\"String2\",\"DefaultValue\":\"\", \"IsShow\": \"1\", \"IsNotNull\": \"0\" ,\"IsNotEmpty\": \"0\" ,\"IsOnly\": \"1\" ,\"IsSum\": \"0\"}," +
                "{\"LabName\":\"数量\",\"DataType\":\"Int1\",\"DefaultValue\":\"1\", \"IsShow\": \"1\", \"IsNotNull\": \"0\" ,\"IsNotEmpty\": \"0\" ,\"IsOnly\": \"0\" ,\"IsSum\": \"1\"}]";


        return ReturnStr;
    }


    @Override
    public String Save_OtherBarCode(String PDA_Number, String SignCode, String UserID, String FunctionName,
                                    String JsonData) {
        //返回 "1" 为成功

        return "1";
    }


    @Override
    public String Set_OtherSelectData(String PDA_Number, String SignCode, String UserID) {
        //ShowData  逗号为分行, | 为分列
        //InputData  货架号/1  1表示必填
        String ReturnStr = "";

        ReturnStr = "[{\"FunctionName\":\"商品上架\",\"SelectFiler\":\"入库单号/BillNo,商品/GoodsName\",\"ShowData\":\"商品名称/GoodsName,规格/Column1,自定义2/Column2|自定义3/Column3"
                + ",自定义4/Column4\",\"InputData\":\"货架号/1,数量/1\",\"ButtonData\":\"保存,修改\",\"IsSaveClear\":\"1\"}," +
                "{\"FunctionName\":\"货位采集\",\"SelectFiler\":\"\"}," +
                "{\"FunctionName\":\"其他功能\",\"SelectFiler\":\"\"}]";


        return ReturnStr;
    }


    @Override
    public String Get_OtherSelect_Table(String PDA_Number, String SignCode, String UserID, String FunctionName,
                                        String Filter) {
        String ReturnStr = "";

        //String _Fstr = "";

		/*
		if (Filter != "")
         {
             JsonSerializer serializer = new JsonSerializer();
             StringReader sr = new StringReader(Filter);
             object o = serializer.Deserialize(new JsonTextReader(sr), typeof(List<Web_FilterList>));
             List<Web_FilterList> _FilterList = o as List<Web_FilterList>;

             for (int k = 0; k < _FilterList.Count; k++)
             {

                 _Fstr = _Fstr == "" ? _FilterList[k].ColumnName + "='" + _FilterList[k].Value + "'" : "," + _FilterList[k].ColumnName + "='" + _FilterList[k].Value + "'";
             }
         }
*/
        //测试的，实际开发时，要根据条件查询
        ReturnStr = "{\"GoodsID\":\"001\",\"GoodsName\":\"测试名称\",\"Column1\":\"ABC\",\"Column2\":\"111\",\"Column3\":\"222\",\"Column4\":\"333333\"}";

        return ReturnStr;
    }


    @Override
    public String UpLoad_OtherSelectData(String PDA_Number, String SignCode, String UserID, String FunctionName,
                                         String ButtonText, String SelectJsonData, String JsonData) {


        return "OK";

    }


    @Override
    public String Get_ReportForm_ClickDataRow2(String PDA_Number, String SignCode, String UserID, String ReportName,
                                               String PrimaryID, String M_SelectData, String D_SelectData) {
        String ReturnStr = "{\"FunctionName\":\"" + ReportName + "\",\"ShowData\":\"商品名称/GoodsName,规格/GoodsSpec,单位/UnitID|进价/BuyPrice,条码/BarCode,售价1/SalePrice1\",\"InputData\":\"条码/0,进价/1\",\"ButtonData\":\"提交\",\"IsSaveClear\":\"1\"}";

        return ReturnStr;
    }
}
