package com.hy.service.Scavenger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
/**
 * Created by 郭宇翔 on 2018/9/13 14:34 扫码机登录层
 */
@WebService(targetNamespace = "http://tempuri.org/")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public interface ScavengerService {

	/**
	 * 
	* <b>Description:</b><br> 0为通用类,1为服装类,2为序列号版, 3为家具版
	* @param PDA_Number
	* @param SignCode
	* @return 返回软件类型
	* @Note
	* <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
	* <br><b>Date:</b> 2018年9月18日 下午4:40:21
	* <br><b>Version:</b> 1.0
	 */
	@WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_SoftwareType")
    @XmlElement(required = true)
    public String Get_SoftwareType(String PDA_Number, String SignCode);


    
    /**
     * 
    * <b>Description:</b><br> 获取登录用户列表
    * @param PDA_Number
    * @param SignCode
    * @return 返回格式{\"UserID\":\"\",\"User_Code\":\"\",\"User_Name\":\"\",\"User_Pw\":\"\",\"IsUse\":\"\"}
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:40:48
    * <br><b>Version:</b> 1.0
     */
	@WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_UserList")
    @XmlElement(required = true)
    public String Get_UserList(String PDA_Number, String SignCode) throws Exception;
    

    
    /**
     * 
    * <b>Description:</b><br> 登录时用户密码处理
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param PW
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:41:09
    * <br><b>Version:</b> 1.0
     */
	@WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_UserPassWord")
    @XmlElement(required = true)
    public String Get_UserPassWord(String PDA_Number, String SignCode, String UserID, String PW) throws Exception;
    

    
    /**
     * 
    * <b>Description:</b><br> 获取用户功能列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return 返回格式{\"FunctionName\":\"单据管理\",\"Value\":\"1\"} Value值1为在线使用,2为离线和在线使用,0为隐藏
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:41:21
    * <br><b>Version:</b> 1.0
     */
	@WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_User_Function")
    @XmlElement(required = true)
    public String Get_User_Function(String PDA_Number, String SignCode, String UserID);
    
    

    /**
     * 
    * <b>Description:</b><br> 获取用户可用单据列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return 返回格式{\"BillName\":\"单据管理\"}
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:41:41
    * <br><b>Version:</b> 1.0
     */
	@WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Bill_List")
    @XmlElement(required = true)
    public String Get_Bill_List(String PDA_Number, String SignCode, String UserID);
    

    
   /**
    * 
   * <b>Description:</b><br> 获取单号
   * @param PDA_Number
   * @param SignCode
   * @param UserID
   * @param BillName
   * @return 返回格式string
   * @Note
   * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
   * <br><b>Date:</b> 2018年9月18日 下午4:41:57
   * <br><b>Version:</b> 1.0
    */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Bill_Number")
    @XmlElement(required = true)
    public String Get_Bill_Number(String PDA_Number, String SignCode, String UserID, String BillName);
    

    
    /**
     * 
    * <b>Description:</b><br> 获取单据详细设置
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @return 返回格式{\"BillName\":\"单据管理\"}
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:42:10
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Bill_Detailed")
    @XmlElement(required = true)
    public String Get_Bill_Detailed(String PDA_Number, String SignCode, String UserID, String BillName);  
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取Combox控件结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:42:26
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Combox_List")
    @XmlElement(required = true)
    public String Get_Combox_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName);
    
    

    /**
     * 
    * <b>Description:</b><br> 获取SelectText控件结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:42:36
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_SelectText_List")
    @XmlElement(required = true)
    public String Get_SelectText_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName,String Match, String Filter,String ClientID);
    
    

    /**
     * 
    * <b>Description:</b><br> 获取商品多单位
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @param GoodsID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:42:44
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_GoodsUnit_List")
    @XmlElement(required = true)
    public String Get_GoodsUnit_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String GoodsID);
    
    

   /**
    * 
   * <b>Description:</b><br> 获取商品批号列表
   * @param PDA_Number
   * @param SignCode
   * @param UserID
   * @param BillName
   * @param PDAColumnName
   * @param LabName
   * @param StoreID
   * @param GoodsID
   * @return
   * @Note
   * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
   * <br><b>Date:</b> 2018年9月18日 下午4:42:52
   * <br><b>Version:</b> 1.0
    */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_GoodsBatchNoList")
    @XmlElement(required = true)
    public String Get_GoodsBatchNoList(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String StoreID,
    		String GoodsID);
    
    
   /**
    * 
   * <b>Description:</b><br> 获取商品单价列表
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
   * @return
   * @Note
   * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
   * <br><b>Date:</b> 2018年9月18日 下午4:42:59
   * <br><b>Version:</b> 1.0
    */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_GoodsPriceList")
    @XmlElement(required = true)
    public String Get_GoodsPriceList(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String ClientID, 
    		String StoreID, String GoodsID, String UnitID, String URate) ;
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取抄单列表-ComBox列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @param ClientID
    * @param StoreID
    * @param GoodsID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:50:38
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Detailed_ComBox_List")
    @XmlElement(required = true)
    public String Get_Detailed_ComBox_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String ClientID,
    		String StoreID, String GoodsID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取商品总库存数量
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
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:50:30
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_GoodsStockNumber")
    @XmlElement(required = true)
    public String Get_GoodsStockNumber(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String ClientID, 
    		String StoreID, String GoodsID, String UnitID, String URate, String BatchNo, String ColorID, String SizeID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 在线单据从从表设置---家具版本用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param GoodsID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:50:22
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Set_Goods_Detail_Obj")
    @XmlElement(required = true)
    public String Get_Set_Goods_Detail_Obj(String PDA_Number, String SignCode, String UserID, String BillName, String GoodsID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取商品-颜色尺码 库存数量 --服装版本使用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillNamestring
    * @param ClientID
    * @param StoreID
    * @param GoodsID
    * @param UnitID
    * @param URate
    * @param BatchNo
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:50:15
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Goods_ColorSize_StockNumber")
    @XmlElement(required = true)
    public String Get_Goods_ColorSize_StockNumber(String PDA_Number, String SignCode, String UserID, String BillNamestring, String ClientID, String StoreID, 
    		String GoodsID, String UnitID, String URate, String BatchNo);


    
    
    /**
     * 
    * <b>Description:</b><br> 获取商品颜色列表 --服装版本使用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @param GoodsID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:50:07
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_GoodsColor_List")
    @XmlElement(required = true)
    public String Get_GoodsColor_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String GoodsID);
    
    
    
   
    /**
     * 
    * <b>Description:</b><br> 获取商品尺码列表 --服装版本使用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @param GoodsID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:50:01
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_GoodsSize_List")
    @XmlElement(required = true)
    public String Get_GoodsSize_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName, String GoodsID);
    
 
    
   
    /**
     * 
    * <b>Description:</b><br> 上传单据信息  ColorSizeJson参数为服装版本使用, ObjJson为家具和序列号版本用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param MasterJson
    * @param DetailedJson
    * @param ColorSizeJson
    * @param ObjJson
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:54
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Save_Bill_Message")
    @XmlElement(required = true)
    public String Save_Bill_Message(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String MasterJson, String DetailedJson,
    		String ColorSizeJson, String ObjJson);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 修改单据信息 ColorSizeJson参数为服装版本使用, ObjJson为家具和序列号版本用"
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param Primary_ID
    * @param MasterJson
    * @param DetailedJson
    * @param ColorSizeJson
    * @param ObjJson
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:45
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Update_Bill_Message")
    @XmlElement(required = true)
    public String Update_Bill_Message(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String Primary_ID, String MasterJson,
    		String DetailedJson, String ColorSizeJson,String ObjJson);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 单据审核
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param Primary_ID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:36
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Bill_Posting")
    @XmlElement(required = true)
    public String Bill_Posting(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String Primary_ID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 单据打印
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param Primary_ID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:29
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Bill_Print")
    @XmlElement(required = true)
    public String Bill_Print(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String Primary_ID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 查询在线单据主表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:22
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_Bill_Master")
    @XmlElement(required = true)
    public String Select_Bill_Master(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String Filter);
    
    
    
   
    /**
     * 
    * <b>Description:</b><br> 查询在线单据从表, 如果是序列号版本
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param MasterID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:15
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_Bill_Detail")
    @XmlElement(required = true)
    public String Select_Bill_Detail(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String MasterID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 查询在线单据从表, 如果是序列号版本
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param MasterID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:07
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_Bill_ColorSize")
    @XmlElement(required = true)
    public String Select_Bill_ColorSize(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String MasterID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 商品信息功能中,查询条件和显示内容
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:49:00
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_GoodsMeaage_Filter")
    @XmlElement(required = true)
    public String Select_GoodsMeaage_Filter(String PDA_Number, String SignCode, String UserID);
    
    
    public class Web_FilterList
    {
        
		public String ColumnName;
        public String LabName;
        public String Value;
        
        public String getColumnName() {
			return ColumnName;
		}
		public void setColumnName(String columnName) {
			ColumnName = columnName;
		}
		public String getLabName() {
			return LabName;
		}
		public void setLabName(String labName) {
			LabName = labName;
		}
		public String getValue() {
			return Value;
		}
		public void setValue(String value) {
			Value = value;
		}
    }
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 商品信息功能中，查询商品接口
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:48:47
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_GoodsMessage")
    @XmlElement(required = true)
    public String Select_GoodsMessage(String PDA_Number, String SignCode, String UserID, String Match, String Filter);
    
    
    
    /**
     * 
    * <b>Description:</b><br> 商品信息功能中，查询库存列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param GoodsID
    * @param UnitID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:48:40
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_GoodsMessage_Stock")
    @XmlElement(required = true)
    public String Select_GoodsMessage_Stock(String PDA_Number, String SignCode, String UserID, String GoodsID, String UnitID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 返回报表功能
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:48:32
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_ReportFormList")
    @XmlElement(required = true)
    public String Get_ReportFormList(String PDA_Number, String SignCode, String UserID);
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取Report_SelectText控件结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param ReportName
    * @param PDAColumnName
    * @param LabName
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:48:24
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Report_SelectText_List")
    @XmlElement(required = true)
    public String Get_Report_SelectText_List(String PDA_Number, String SignCode, String UserID, String ReportName, String PDAColumnName, String LabName, String Match, String Filter);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取Report_Combox控件结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param PDAColumnName
    * @param LabName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:48:16
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Report_Combox_List")
    @XmlElement(required = true)
    public String Get_Report_Combox_List(String PDA_Number, String SignCode, String UserID, String BillName, String PDAColumnName, String LabName);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 返回报表查询返回结果功能
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param ReportName
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:48:05
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_ReportForm_SelelctData")
    @XmlElement(required = true)
    public String Get_ReportForm_SelelctData(String PDA_Number, String SignCode, String UserID, String ReportName, String Match, String Filter);
    
     
    
    
    /**
     * 
    * <b>Description:</b><br> 报表单击数据行后-显示明细数据
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param ReportName
    * @param PrimaryID
    * @param SelectIndex
    * @param SelectData
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:57
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_ReportForm_ClickDataRow")
    @XmlElement(required = true)
    public String Get_ReportForm_ClickDataRow(String PDA_Number, String SignCode, String UserID, String ReportName, String PrimaryID, String SelectIndex, 
    		String SelectData, String Match, String Filter);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 新增信息和修改信息－设置格式
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:50
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Set_AddMessage")
    @XmlElement(required = true)
    public String Get_Set_AddMessage(String PDA_Number, String SignCode, String UserID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 新增信息和修改信息－设置控件内容
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:44
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Ctrl_AddMessage")
    @XmlElement(required = true)
    public String Get_Ctrl_AddMessage(String PDA_Number, String SignCode, String UserID, String FunctionName);
    
    
    
   
    /**
     * 
    * <b>Description:</b><br> 新增信息和修改信息－返回ComBox控件内容
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @param ColumnName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:37
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_AddMessage_ComBox")
    @XmlElement(required = true)
    public String Get_AddMessage_ComBox(String PDA_Number, String SignCode, String UserID, String FunctionName, String ColumnName);
    
    
    
   
    /**
     * 
    * <b>Description:</b><br> 新增信息和修改信息－返回SelectText控件内容
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @param ColumnName
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:30
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_AddMessage_SelectText")
    @XmlElement(required = true)
    public String Get_AddMessage_SelectText(String PDA_Number, String SignCode, String UserID, String FunctionName, String ColumnName, String Match, String Filter);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 新增信息和修改信息－保存信息
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @param AddOrChange
    * @param Primary_ID
    * @param JsonData
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:23
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Save_AddMessage")
    @XmlElement(required = true)
    public String Save_AddMessage(String PDA_Number, String SignCode, String UserID, String FunctionName, String AddOrChange, String Primary_ID, String JsonData);
    

    
    
    /**
     * 
    * <b>Description:</b><br> 验单时-查询源单设置
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:16
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Check_Select_SourceBill")
    @XmlElement(required = true)
    public String Get_Check_Select_SourceBill(String PDA_Number, String SignCode, String UserID, String BillName, String SourceBillName);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取引用上级单据时查询条件中Combox控件结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @param PDAColumnName
    * @param LabName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:09
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Check_Select_SourceBill_Combox_List")
    @XmlElement(required = true)
    public String Get_Check_Select_SourceBill_Combox_List(String PDA_Number, String SignCode, String UserID, String BillName, String SourceBillName, String PDAColumnName
    		, String LabName);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取引用上级单据时查询条件中SelectText控件结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @param PDAColumnName
    * @param LabName
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:47:02
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_Check_Select_SourceBill_SelectText_List")
    @XmlElement(required = true)
    public String Get_Check_Select_SourceBill_SelectText_List(String PDA_Number, String SignCode, String UserID, String BillName, 
    		String SourceBillName, String PDAColumnName, String LabName, String Match, String Filter);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取引用上级单据时 -查询在线单据主表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:46:51
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Check_Select_Bill_Master")
    @XmlElement(required = true)
    public String Check_Select_Bill_Master(String PDA_Number, String SignCode, String UserID, String BillName, String SourceBillName, String Filter);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取引用上级单据时 -特殊查询在线单据－直接查询从表明细
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:46:43
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Check_Select_Bill_Detail_Other")
    @XmlElement(required = true)
    public String Check_Select_Bill_Detail_Other(String PDA_Number, String SignCode, String UserID, String BillName, String SourceBillName, String Filter);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取引用上级单据时 -查询在线单据从表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @param MasterID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:46:35
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Check_Select_Bill_Detail")
    @XmlElement(required = true)
    public String Check_Select_Bill_Detail(String PDA_Number, String SignCode, String UserID, String BillName, String SourceBillName, String MasterID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 获取引用上级单据时 -查询在线单据颜色尺码表 --服装版本使用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param SourceBillName
    * @param MasterID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:46:27
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Check_Select_Bill_ColorSize")
    @XmlElement(required = true)
    public String Check_Select_Bill_ColorSize(String PDA_Number, String SignCode, String UserID, String BillName, String SourceBillName, String MasterID);
 
    
    


    /**
     * 
    * <b>Description:</b><br> 增加明细商品，带出包件列表---家具版本用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param GoodsID
    * @param Number
    * @param OtherOnlyColumn
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:46:16
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Add_Goods_Detail_Obj")
    @XmlElement(required = true)
    public String Add_Goods_Detail_Obj(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String GoodsID, String Number, String OtherOnlyColumn);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 增加包件---家具版本用(Int1为比例数量)
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param GoodsID
    * @param Number
    * @param OtherOnlyColumn
    * @param Match
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:46:06
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Add_Goods_Detail_Obj_Piece")
    @XmlElement(required = true)
    public String Add_Goods_Detail_Obj_Piece(String PDA_Number, String SignCode, String UserID, String BillName, String GoodsID, String Number, String OtherOnlyColumn, 
    		String Match, String Filter);
    

    
    
    /**
     * 
    * <b>Description:</b><br> 查询在线单据从从表---家具版本用
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param MasterID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:45:57
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_Bill_Detail_Obj")
    @XmlElement(required = true)
    public String Select_Bill_Detail_Obj(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String MasterID);
  
    
    
    
    /**
     * 
    * <b>Description:</b><br> 序列号版本---扫描序列号查询是否可用--返回商品ID和仓库ID
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param SNCode
    * @param ClientID
    * @param StoreID
    * @param M_RowJson
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:45:49
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_SerialNumber")
    @XmlElement(required = true)
    public String Select_SerialNumber(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String SNCode, String ClientID, String StoreID, String M_RowJson);
    
    
    


    /**
     * 
    * <b>Description:</b><br> 序列号版本---判断是否序列号商品
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param GoodsID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:45:37
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Goods_Is_SerialNumber")
    @XmlElement(required = true)
    public String Goods_Is_SerialNumber(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String GoodsID);
    
  
    
    
    /**
     * 
    * <b>Description:</b><br> 序列号版本---查询商品已有序列号
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param GoodsID
    * @param ClientID
    * @param StoreID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:45:24
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_Goods_SerialNumber")
    @XmlElement(required = true)
    public String Select_Goods_SerialNumber(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String GoodsID, String ClientID, String StoreID);
    
  
    
    
    /**
     * 
    * <b>Description:</b><br> 序列号版本---判断商品序列号是否可以添加到明细
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param SNCode
    * @param GoodsID
    * @param ClientID
    * @param StoreID
    * @param M_RowJson
    * @param D_RowJson
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:45:01
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_SerialNumber_IsAdd")
    @XmlElement(required = true)
    public String Select_SerialNumber_IsAdd(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String SNCode, String GoodsID, String ClientID, String StoreID,
    		String M_RowJson, String D_RowJson);
    
  
    
    
    /**
     * 
    * <b>Description:</b><br> 查询在线单据序列号表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @param BillType
    * @param MasterID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:44:50
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Select_Bill_Detail_SN")
    @XmlElement(required = true)
    public String Select_Bill_Detail_SN(String PDA_Number, String SignCode, String UserID, String BillName, String BillType, String MasterID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 离线数据下载---开始
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param DownList
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:44:41
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Down_Data")
    @XmlElement(required = true)
    public void Down_Data(String PDA_Number, String SignCode, String UserID, String DownList);
    
    
    
   
    /**
     * 
    * <b>Description:</b><br> 离线数据下载---返回数据下载的进度
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param IsNewDown
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:44:32
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Down_Data_Progress")
    @XmlElement(required = true)
    public String Down_Data_Progress(String PDA_Number, String SignCode, String UserID, String IsNewDown);
   
    
    
   
    /**
     * 
    * <b>Description:</b><br> 开单时，汇总明细的列－－默认数量和金额列
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param BillName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:44:25
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Bill_SumColumnList")
    @XmlElement(required = true)
    public String Bill_SumColumnList(String PDA_Number, String SignCode, String UserID, String BillName);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 设置其他扫描功能
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:44:17
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Set_OtherBarCode")
    @XmlElement(required = true)
    public String Set_OtherBarCode(String PDA_Number, String SignCode, String UserID);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 设置其他扫描功能的内容
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:44:07
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Set_OtherBarCode_Ctrl")
    @XmlElement(required = true)
    public String Set_OtherBarCode_Ctrl(String PDA_Number, String SignCode, String UserID, String FunctionName);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 上传扫描数据
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @param JsonData
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:43:59
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Save_OtherBarCode")
    @XmlElement(required = true)
    public String Save_OtherBarCode(String PDA_Number, String SignCode, String UserID, String FunctionName, String JsonData);
    
    
    
    
    /**
     * 
    * <b>Description:</b><br> 设置其他功能---以查询内容显示后，输入内容提交
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:43:50
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Set_OtherSelectData")
    @XmlElement(required = true)
    public String Set_OtherSelectData(String PDA_Number, String SignCode, String UserID);
    
    
   
    /**
     * 
    * <b>Description:</b><br> 获取Set_OtherSelectData结果列表
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @param Filter
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:43:36
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_OtherSelect_Table")
    @XmlElement(required = true)
    public String Get_OtherSelect_Table(String PDA_Number, String SignCode, String UserID, String FunctionName, String Filter);
    
    
    
    /**
     * 
    * <b>Description:</b><br> 其他功能输入内容提交
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param FunctionName
    * @param ButtonText
    * @param SelectJsonData
    * @param JsonData
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:43:27
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/UpLoad_OtherSelectData")
    @XmlElement(required = true)
    public String UpLoad_OtherSelectData(String PDA_Number, String SignCode, String UserID, String FunctionName, String ButtonText, String SelectJsonData, String JsonData);
    
    
    
    /**
     * 
    * <b>Description:</b><br> 单击报表明细数据一行时，显示并修改内容
    * @param PDA_Number
    * @param SignCode
    * @param UserID
    * @param ReportName
    * @param PrimaryID
    * @param M_SelectData
    * @param D_SelectData
    * @return
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年9月18日 下午4:43:14
    * <br><b>Version:</b> 1.0
     */
    @WebResult( targetNamespace = "http://tempuri.org/" )
    @WebMethod(action="http://tempuri.org/Get_ReportForm_ClickDataRow2")
    @XmlElement(required = true)
    public String Get_ReportForm_ClickDataRow2(String PDA_Number, String SignCode, String UserID, String ReportName, String PrimaryID, String M_SelectData, String D_SelectData);
    

}
