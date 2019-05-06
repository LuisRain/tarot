package com.hy.service.Scavenger.Storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hy.dao.DaoSupport;
import com.hy.util.PageData;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 郭宇翔 on 2018/9/26 16:49
 */
@Service("storageService")
public class StorageService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 
    * <b>Description:</b><br> PDA-订单匹配查询   单号查询、商品查询
    * @param startTimeValue 开始时间
    * @param endTimeValue 结束时间
    * @param billValue 订单编号
    * @param billIDValue 订单编号
    * @param clientId 供应商ID
    * @param Match 过滤条件
    * @param PDAColumnName PDA编号
    * @return 订单结果集/商品结果集
    * @Note
    * <b>Author:</b> <a href="https://blog.csdn.net/weixin_41286794" target="_blank">Geoffrey</a>
    * <br><b>Date:</b> 2018年10月11日 下午4:51:07
    * <br><b>Version:</b> 1.0
     */
    public String querywares(String startTimeValue, String endTimeValue, String billValue, String billIDValue, String clientId, String Match, String PDAColumnName) {
        List<Map<String, Object>> listdata = new ArrayList<>();
        String data = "";
        try {
            PageData pd = new PageData();
            //Select_Bill_Master --查单
            if (PDAColumnName.equals("Select_Bill_Master")) {
                pd.put("startDate", StringUtils.isNotBlank(startTimeValue) ? startTimeValue+" 00:00:00" : startTimeValue);
                pd.put("endDate", StringUtils.isNotBlank(endTimeValue) ? endTimeValue+" 23:59:59" : endTimeValue);
                pd.put("order_num", billValue == null ? billIDValue : billValue);
                List<PageData> msg = (List<PageData>) dao.findForList("StorageMapper.queryenOrder", pd);
                //System.out.println(msg);
                Object json = JSON.toJSON(msg);
                JSONArray objects = JSON.parseArray(json.toString());
                for (int i = 0; i < objects.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    JSONObject jsonObject = objects.getJSONObject(i);
                    map.put("BillID", jsonObject.get("order_num"));
                    map.put("BillType", "InOrder");
                    map.put("BillNo", jsonObject.get("order_num"));
                    map.put("BillTime", jsonObject.get("create_time"));
                    map.put("StoreID", "1");
                    map.put("StoreName", "中央仓");
                    map.put("ClientID", jsonObject.get("ts_id"));
                    map.put("ClientName", jsonObject.get("supplier_name"));
                    map.put("CreateName", jsonObject.get("NAME"));
                    map.put("EmpName", jsonObject.get("contact_person"));
                    map.put("Comment", jsonObject.get("comment"));
                    listdata.add(map);
                }
                data = JSON.toJSONString(listdata);
                //System.out.println(data);
                return data;
                
                //Get_SelectText_List
                //商品信息查询  条件（全/左右模糊查询、精确查询、扫码查询）
                //参数：Get_SelectText_List-GoodsID：全/左右、精确查询
                //Get_SelectText_List-BarCode: 条形码查询
                //Get_SelectText_List-Enter：回车查询，数据为商品名称
            } else if (PDAColumnName.equals("Get_SelectText_List-GoodsID") || PDAColumnName.equals("Get_SelectText_List-BarCode") || PDAColumnName.equals("Get_SelectText_List-Enter")) {
                if (Match.equals("全模糊")) {
                    if (PDAColumnName.equals("Get_SelectText_List-Enter")) {
                        pd.put("enter_name", billValue);
                    } else {
                        pd.put("tp_id", billValue.trim());
                    }
                } else if (!billValue.equals("") && Match.equals("左模糊")) {
                    pd.put("tp_id_left", billValue.trim());
                } else if (!billValue.equals("") && Match.equals("右模糊")) {
                    pd.put("tp_id_right", billValue.trim());
                } else if (!billValue.equals("") && Match.equals("精确查询")) {
                    if (PDAColumnName.equals("Get_SelectText_List-GoodsID")) {
                        pd.put("tp_id_accurate", billValue.trim());
                    } else if (PDAColumnName.equals("Get_SelectText_List-BarCode")) {
                        pd.put("bar_code", billValue.trim());
                    }
                } else {
                    JSONObject jsons = new JSONObject();
                    jsons.put("ErrorString", "请选择编号查询");
                    data = JSON.toJSONString(jsons);
                    return data;
                }

                pd.put("clientId", clientId);
                List<PageData> msg = (List<PageData>) dao.findForList("StorageMapper.queryenCommodity", pd);
                //System.out.println(msg);

                JSONObject jsonGoodsID = new JSONObject();
                jsonGoodsID.put("IDName", "");
                jsonGoodsID.put("GoodsID", "");
                jsonGoodsID.put("ValueName", "");
                jsonGoodsID.put("GoodsName", "");
                Object json = JSON.toJSON(msg);
                JSONArray objects = JSON.parseArray(json.toString());
                for (int i = 0; i < objects.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    JSONObject jsonObject = objects.getJSONObject(i);
                    map.put("GoodsID", jsonObject.get("tp_id"));
                    map.put("GoodsCode", jsonObject.get("tp_id"));
                    map.put("GoodsName", jsonObject.get("product_name"));
                    map.put("GoodsSpec", jsonObject.get("specification"));
                    map.put("BarCode", jsonObject.get("bar_code"));
                    map.put("UnitName", jsonObject.get("unit_name"));
                    map.put("URate", jsonObject.get("product_quantity"));
                    listdata.add(map);
                }
                jsonGoodsID.put("RowCount", listdata.size());
                jsonGoodsID.put("TableMessage", listdata);
                data = JSON.toJSONString(jsonGoodsID);
                return data;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }

    //根据商品RK__来单号查询商品
    public String queryProductOrderNumber(String MasterID) {
        String data = "";
        try {
            PageData pd = new PageData();
            pd.put("goodsID", MasterID);
            List<Map<String, Object>> list = new ArrayList<>();
            List<PageData> msg = (List<PageData>) dao.findForList("StorageMapper.queryProductOrderNumber", pd);
            Object json = JSON.toJSON(msg);
            JSONArray objects = JSON.parseArray(json.toString());
            for (int i = 0; i < objects.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsons = objects.getJSONObject(i);
                map.put("DetailID", jsons.getString("id"));
                map.put("BillID", jsons.getString("order_num"));
                map.put("GoodsName", jsons.get("product_name"));
                map.put("GoodsID", jsons.get("tp_id"));
                map.put("GoodsCode", jsons.get("tp_id"));
                map.put("GoodsSpec", jsons.getString("specification"));
                map.put("Qty", jsons.get("quantity"));
                map.put("Price", jsons.get("product_price"));
                map.put("Amount", jsons.get("price1"));
                map.put("BillType", "InOrder");
                map.put("BarCode", jsons.get("bar_code"));
                map.put("StoreName", "中央仓");
                map.put("CreateTime", jsons.get("product_time"));
                map.put("URate", jsons.get("product_quantity"));
                map.put("UnitName", jsons.get("unit_name"));
                //PDA 折扣 如果隐藏就出现问题 此处的折扣默认设置为100
                map.put("Discount", "100");
                list.add(map);
            }
            data = JSON.toJSONString(list);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }

    public String queryUnit(String GoodsID) {
        String data = "";
        try {
            PageData pd = new PageData();
            pd.put("goodsID", GoodsID);
            List<Map<String, Object>> list = new ArrayList<>();
            List<PageData> msg = (List<PageData>) dao.findForList("StorageMapper.queryUnit", pd);
            Object json = JSON.toJSON(msg);
            JSONArray objects = JSON.parseArray(json.toString());
            for (int i = 0; i < objects.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsons = objects.getJSONObject(i);
                map.put("UnitID", i);
                map.put("UnitName", jsons.get("unit_name"));
                map.put("URate", "1");
                list.add(map);
            }
            data = JSON.toJSONString(list);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }

    public String productIDInquirySalePrice(String goodsID) {
        String data = "";
        try {
            PageData pd = new PageData();
            pd.put("goodsID", goodsID);
            List<Map<String, Object>> list = new ArrayList<>();
            List<PageData> msg = (List<PageData>) dao.findForList("StorageMapper.productIDInquirySalePrice", pd);
            Object json = JSON.toJSON(msg);
            JSONArray objects = JSON.parseArray(json.toString());
            for (int i = 0; i < objects.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsons = objects.getJSONObject(i);
                map.put("Price", jsons.get("product_price"));
                map.put("PriceType", "最新售价");
                list.add(map);
            }
            data = JSON.toJSONString(list);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }


    /**
     * 接口：查询供应商
     */
    public String querysupplier(String Match, String billValue, String columnName) {
        JSONObject jsons = new JSONObject();
        jsons.put("IDName", "");
        jsons.put("ClientID", "");
        jsons.put("ValueName", "");
        jsons.put("ClientName", "");

        List<Map<String, Object>> list = new ArrayList<>();

        String data = "";
        try {
            PageData pd = new PageData();
            if (Match.equals("全模糊")) {
                if (columnName.equals("ClientName")) {
                    pd.put("supplier_name", billValue.trim());
                } else {
                    pd.put("supplier_num", billValue.trim());
                }
            } else if (!billValue.equals("") && Match.equals("左模糊")) {
                pd.put("supplier_num_left", billValue.trim());
            } else if (!billValue.equals("") && Match.equals("右模糊")) {
                pd.put("supplier_num_right", billValue.trim());
            } else if (!billValue.equals("") && Match.equals("精确查询")) {
                pd.put("supplier_num_accurate", billValue.trim());
            } else {
                jsons.put("ErrorString", "请选择编号查询");
                data = JSON.toJSONString(jsons);
                return data;
            }
            List<PageData> msg = (List<PageData>) dao.findForList("StorageMapper.querysupplier", pd);
            Object json = JSON.toJSON(msg);
            JSONArray objects = JSON.parseArray(json.toString());
            for (int i = 0; i < objects.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jsonObject = objects.getJSONObject(i);
                map.put("ClientID", jsonObject.get("id"));
                map.put("ClientCode", jsonObject.get("supplier_num"));
                map.put("ClientName", jsonObject.get("supplier_name"));
                map.put("Contacts", jsonObject.get("contact_person"));
                map.put("Phone", jsonObject.get("contact_person_mobile"));
                map.put("Address", jsonObject.get("address"));
                map.put("Remark", jsonObject.get("remarks"));
                list.add(map);
            }
            jsons.put("RowCount", list.size());
            jsons.put("TableMessage", list);
            data = JSON.toJSONString(jsons);
            //System.out.println(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }


    /**
     * 查询商品订单号
     *
     * @param primary_ID
     */
    public PageData findProductInformation(String primary_ID) throws Exception {
        PageData primaryIdOrder = new PageData();
        primaryIdOrder.put("order_num", primary_ID);
        PageData forObject = (PageData) dao.findForObject("StorageMapper.queryenOrder", primaryIdOrder);
        return forObject;
    }

    @Transactional
    public int updateStorage(PageData pd) throws Exception {
        Object o = this.dao.update("StorageMapper.updateStorage", pd);
        return Integer.parseInt(o.toString());
    }

    public PageData findStorageOne(PageData pd)
            throws Exception {
        return (PageData) this.dao.findForObject("StorageMapper.queryProductOrderNumber", pd);
    }

    @Transactional
    public int save(PageData pd) throws Exception {
//        pd.put("creator", LoginUtil.getLoginUser().getNAME());
        Object ob = this.dao.save("StorageMapper.save", pd);
        return Integer.parseInt(ob.toString());
    }

    /**
     * 新增表头信息
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public int saveNewOrder(PageData pd) throws Exception {
        //        pd.put("creator", LoginUtil.getLoginUser().getNAME());
        Object ob = this.dao.save("StorageMapper.saveNewOrder", pd);
        return Integer.parseInt(ob.toString());

    }

    /**
     * 查询货物体积
     *
     * @param primary_ID
     */
    public PageData productIDAndVolume(String primary_ID) throws Exception {
        PageData primaryIdOrder = new PageData();
        primaryIdOrder.put("goodsID", primary_ID);
        PageData forObject = (PageData) dao.findForObject("StorageMapper.productIDInquirySalePrice", primaryIdOrder);
        return forObject;
    }

    public PageData selectStorage(String order_num, String goodsID) throws Exception {
        PageData primaryIdOrder = new PageData();
        primaryIdOrder.put("order_num", order_num);
        primaryIdOrder.put("goodsID", goodsID);
        PageData forObject = (PageData) dao.findForObject("StorageMapper.selectStorage", primaryIdOrder);
        return forObject;
    }
}
