package com.hy.mapper;

import com.hy.entity.base.*;
import com.hy.entity.order.SellingOrderItem;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.ProductType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sren
 * @create 2018-12-31 下午2:24
 **/

public interface ProdutMapper {

    List<Goods> getList(@Param("offset") Integer offset, @Param("size") Integer size, @Param("categoryId") Integer categoryId, @Param("search") String search);

    List<ProductType> getCategory();

    Long getCount(@Param("categoryId") Integer categoryId);

    Long getSearchCount(@Param("search") String search);

    Goods getById(@Param("id") Integer id);

    List<SellingOrder> getSellByUserId(@Param("userId") long userId);

    Merchant getMerchantByName(@Param("username") String username);

    void save(@Param("sellingOrder") SellingOrder sellingOrder);

    void saveOrderItem(@Param("sellingOrderItem") SellingOrderItem sellingOrderItem);

    int getOrderItemByOrderNum(@Param("orderNum") String orderNum, @Param("id") Integer id);

    List<OrderItem> getOrderItems(@Param("userId") long userId);

    void updateOrderItem(@Param("id") Integer id, @Param("num") Integer num);

    void deleteOrderItem(@Param("id") Integer id);

    boolean submitOrder(SellingOrder sellingOrder);

    List<AppOrder> getOrderList(@Param("userId") long userId);

    List<OrderItem> getOrderItemByNum(@Param("orderNum") String orderNum);

    int hasUser(@Param("userName") String userName, @Param("ps") String ps);

    void updateUser(@Param("userName") String userName, @Param("newPw") String newPw);

    SellingOrder getOrderById(@Param("orderId") Integer orderId);

    void updateOrderItemById(OrderItem orderItem);

    ProductActivity getProductActivity(@Param("productId") Long productId);
}
