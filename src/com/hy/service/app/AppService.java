package com.hy.service.app;

import com.hy.entity.base.*;
import com.hy.entity.order.SellingOrderItem;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.ProductType;
import com.hy.entity.system.User;
import com.hy.mapper.ProdutMapper;
import com.hy.util.DateUtil;
import com.hy.util.StringUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sren
 * @create 2018-12-31 下午2:04
 **/
@Service("appService")
public class AppService {

    @Autowired
    private ProdutMapper produtMapper;

    public ApiResponse getListOne(Page page) {
        Integer offset = page.getPage() * page.getPageSize();
        Integer size = page.getPageSize();
        Integer categoryId = page.getCategoryId();
        List<Goods> goods = produtMapper.getList(offset, size, categoryId, null);
        Long count = produtMapper.getCount(categoryId);
        PageResult<Goods> pageResult = new PageResult<>();
        pageResult.setData(goods);
        pageResult.setPage(page.getPage());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalPage(count%page.getPageSize() == 0?count/page.getPageSize():count/page.getPageSize()+1);
        return ApiResponse.ofSuccess(pageResult);
    }

    public ApiResponse getList(Page page) {
        Integer offset = page.getPage() * page.getPageSize();
        Integer size = page.getPageSize();
        Integer categoryId = page.getCategoryId();
        page.setCategoryId(null);
        List<Goods> goods = produtMapper.getList(offset, size, null, null);
        Long count = produtMapper.getCount(categoryId);
        PageResult<Goods> pageResult = new PageResult<>();
        pageResult.setData(goods);
        pageResult.setPage(page.getPage());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalPage(count%page.getPageSize() == 0?count/page.getPageSize():count/page.getPageSize()+1);
        return ApiResponse.ofSuccess(pageResult);
    }


    public ApiResponse getCategory() {
        List<ProductType> types = produtMapper.getCategory();
        return ApiResponse.ofSuccess(types);
    }

    public ApiResponse getList(Page page, String search) {
        Integer offset = page.getPage() * page.getPageSize();
        Integer size = page.getPageSize();
        Integer categoryId = page.getCategoryId();
        List<Goods> goods = produtMapper.getList(offset, size, categoryId, search);
        Long count = produtMapper.getSearchCount(search);
        PageResult<Goods> pageResult = new PageResult<>();
        pageResult.setData(goods);
        pageResult.setPage(page.getPage());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalPage(count%page.getPageSize() == 0?count/page.getPageSize():count/page.getPageSize()+1);
        return ApiResponse.ofSuccess(pageResult);
    }


    public Goods getGoodsById(Integer id) {
        Goods goods = produtMapper.getById(id);
        return goods;
    }

    public ApiResponse addCart(Integer id, User user) {
        //查询是否生产订单
        List<SellingOrder> orderList = produtMapper.getSellByUserId(user.getUSER_ID());
        Merchant merchant = produtMapper.getMerchantByName(user.getUSERNAME());
        Goods goods = produtMapper.getById(id);
        String message = "加入采购申请单成功";
        if (CollectionUtils.isEmpty(orderList)) {
            try {
                //新建订单
                SellingOrder sellingOrder = new SellingOrder();
                sellingOrder.setOrderNum("SO_" + StringUtil.getStringOfMillisecond(""));
                String groupNum = "GP_" +DateUtil.group();
                sellingOrder.setGroupNum(groupNum);
                sellingOrder.setCheckedState(11);
                sellingOrder.setOrderDate(new Date());
                sellingOrder.setDeliverStyle(1);
                sellingOrder.setDeliverAddress(merchant.getAddress());
                sellingOrder.setManagerName(merchant.getMerchantName());
                sellingOrder.setManagerTel(merchant.getMobile());
                sellingOrder.setCreateTime(new Date());
                sellingOrder.setUserId(user.getUSER_ID());
                sellingOrder.setMerchantId(merchant.getId());
                sellingOrder.setDeliverDate(new Date());
                sellingOrder.setOrderType(1);
                sellingOrder.setCkId(1);
                sellingOrder.setType(2);
                produtMapper.save(sellingOrder);
                //添加订单项
                SellingOrderItem sellingOrderItem = new SellingOrderItem();
                sellingOrderItem.setOrderNum(sellingOrder.getOrderNum());
                sellingOrderItem.setGroupNum(sellingOrder.getGroupNum());
                sellingOrderItem.setProductId(Long.valueOf(goods.getId()));
                sellingOrderItem.setPurchasePrice(goods.getSupplyPrice());
                sellingOrderItem.setSalePrice(goods.getGoodsRetailPrice());
                if(!"".equals(goods.getMinOrderNum())&&goods.getMinOrderNum()!=null){
                    sellingOrderItem.setFinalQuantity(Double.parseDouble(goods.getMinOrderNum()));
                    sellingOrderItem.setQuantity(Double.parseDouble(goods.getMinOrderNum()));
                }else {
                    sellingOrderItem.setFinalQuantity(1);
                    sellingOrderItem.setQuantity(1);
                }
                sellingOrderItem.setCreator(user.getUSERNAME());
                sellingOrderItem.setState(1);
                produtMapper.saveOrderItem(sellingOrderItem);
                return ApiResponse.ofMessage(200, message);
            }catch (Exception e) {
                e.printStackTrace();
                message = "加入采购申请单失败";
                return ApiResponse.ofMessage(500, "加入采购申请单失败");
            }


        }else {
            try {
                SellingOrder sellingOrder = orderList.get(0);
                //查询是否重复添加商品
                int count = produtMapper.getOrderItemByOrderNum(sellingOrder.getOrderNum(), id);
                if (count > 0) {
                    return ApiResponse.ofMessage(200, "该商品已存在于采购申请单中");
                }
                //添加订单项
                SellingOrderItem sellingOrderItem = new SellingOrderItem();
                sellingOrderItem.setOrderNum(sellingOrder.getOrderNum());
                sellingOrderItem.setGroupNum(sellingOrder.getGroupNum());
                sellingOrderItem.setProductId(Long.valueOf(goods.getId()));
                sellingOrderItem.setPurchasePrice(goods.getSupplyPrice());
                sellingOrderItem.setSalePrice(goods.getGoodsRetailPrice());
                if(!"".equals(goods.getMinOrderNum())&&goods.getMinOrderNum()!=null){
                    sellingOrderItem.setFinalQuantity(Double.parseDouble(goods.getMinOrderNum()));
                    sellingOrderItem.setQuantity(Double.parseDouble(goods.getMinOrderNum()));
                }else {
                    sellingOrderItem.setFinalQuantity(1);
                    sellingOrderItem.setQuantity(1);
                }
                sellingOrderItem.setCreator(user.getUSERNAME());
                sellingOrderItem.setState(1);
                produtMapper.saveOrderItem(sellingOrderItem);
                return ApiResponse.ofMessage(200, message);
            }catch (Exception e) {
                e.printStackTrace();
                message = "加入采购申请单失败";
                return ApiResponse.ofMessage(500, "加入采购申请单失败");
            }

        }
    }


    public ApiResponse getOrderList(long userId) {
        try {
            List<OrderItem> orderItems = produtMapper.getOrderItems(userId);
            return ApiResponse.ofSuccess(orderItems);
        } catch (Exception e) {
            return ApiResponse.ofMessage(500, "获取订单失败");
        }
    }

    public ApiResponse modifyCard(Integer id, Integer num) {
        produtMapper.updateOrderItem(id, num);
        return ApiResponse.ofSuccess(null);
    }

    public ApiResponse inputModifyCard(Integer orderGoodsId, Integer num) {
        return this.modifyCard(orderGoodsId, num);
    }

    public ApiResponse deleteOrderGoods(Integer id) {
        produtMapper.deleteOrderItem(id);
        return ApiResponse.ofSuccess(null);
    }

    @Transactional
    public ApiResponse submit(long userId, Integer orderId) {
        String message = "提交采购申请成功";
        try {
            //获取当前订单信息
            SellingOrder sellingOrder = produtMapper.getOrderById(orderId);
            //获取当前订单item信息
            List<OrderItem> orderItemList = produtMapper.getOrderItemByNum(sellingOrder.getOrderNum());
            //生成新的订单号和批次号
            String groupNum = "GP_" +DateUtil.group();
            String orderNum = "SO_" + StringUtil.getStringOfMillisecond("");
            //订单总价信息
            double orderAmount = 0.0;
            for (OrderItem orderItem : orderItemList) {
                //已下架商品
                if (orderItem.getIsShelve() == 2) {
                    produtMapper.deleteOrderItem(Integer.parseInt(orderItem.getId()+""));
                    continue;
                }
                //活动表查询活动关联信息 t_product_activity
                ProductActivity productActivity = produtMapper.getProductActivity(orderItem.getProductId());
                //如果有 且是买赠
                Double giftQuantity = 0.0;
                if (productActivity != null && productActivity.getFinalQuantity() != 0) {
                    //根据采购商品达标情况,计算赠品数量
                     giftQuantity = new BigDecimal(orderItem.getGoodsAmount()).divide(new BigDecimal(productActivity.getQuantity()),0,BigDecimal.ROUND_FLOOR).doubleValue();
                    //giftQuantity = Math.floor(giftQuantity);
                    giftQuantity = giftQuantity * productActivity.getFinalQuantity();
                }
                orderItem.setGiftQuantity(giftQuantity);
                orderAmount += new BigDecimal(orderItem.getGoodsUnitPrice()).multiply(new BigDecimal(orderItem.getGoodsAmount())).doubleValue();
                orderItem.setGroupNum(groupNum);
                orderItem.setOrderNum(orderNum);
                orderItem.setCreateTime(new Date());
                produtMapper.updateOrderItemById(orderItem);
            }
            sellingOrder.setGroupNum(groupNum);
            sellingOrder.setOrderNum(orderNum);
            sellingOrder.setCreateTime(new Date());
            sellingOrder.setOrderAmount(orderAmount);
            sellingOrder.setUserId(userId);
            boolean flag = produtMapper.submitOrder(sellingOrder);
            if (flag) {
                return ApiResponse.ofSuccess(message);
            } else {
                message = "提交采购申请失败";
                return ApiResponse.ofMessage(500, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "提交采购申请失败";
            return ApiResponse.ofMessage(500, message);
        }
    }

    public ApiResponse getBaseUser(String username) {
        Merchant merchant = produtMapper.getMerchantByName(username);
        return ApiResponse.ofSuccess(merchant);
    }

    public ApiResponse getMyOrderList(Page page, long userId) {
        List<AppOrder> orders = produtMapper.getOrderList(userId);
        return ApiResponse.ofSuccess(orders);
    }

    public ApiResponse getOrderDetails(String orderNum) {
        List<OrderItem> orderItems = produtMapper.getOrderItemByNum(orderNum);
        AppOrderDetails details = new AppOrderDetails();
        details.setGoods(orderItems);
        if (!CollectionUtils.isEmpty(orderItems)) {
            BigDecimal count = orderItems.stream()
                    .map(e -> new BigDecimal(e.getGoodsAmount()))
                    .reduce(BigDecimal::add).get();
            details.setCount(count.intValue());
            BigDecimal money = orderItems.stream()
                    .map(e -> new BigDecimal(e.getGoodsAmount()).multiply(new BigDecimal(e.getGoodsUnitPrice())))
                    .reduce(BigDecimal::add).get();
            details.setTotalMoney(money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return ApiResponse.ofSuccess(details);
    }

    public ApiResponse modifyPword(AppUser user) {
        try {
            String ps = new SimpleHash("SHA-1", user.getUserName(), user.getOldPassword()).toString();
            int count = produtMapper.hasUser(user.getUserName(), ps);
            if (count == 0) {
                return ApiResponse.ofMessage(5000, "用户名或密码错误");
            } else {
                String newPw = new SimpleHash("SHA-1", user.getUserName(), user.getNewPasswprd()).toString();
                produtMapper.updateUser(user.getUserName(), newPw);
                return ApiResponse.ofSuccess("修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.ofMessage(5000, "系统出错了...");
        }
    }

}

