package com.hy.mapper;

import com.hy.entity.BuyGood;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author SuPengFei
 * @version V1.0
 * @Package com.hy.mapper
 * @date 2019/3/11 18:47
 */
public interface AutoAssignMapper {
    List<BuyGood> getAllBuyGoods(@Param("groupNum") String groupNum);

    Double getSumInventoryCount(@Param("goodId") Long goodId);

    void updateFinalBuyCounts(BuyGood good);

    List<BuyGood> getExOrderGood(@Param("groupNum")String groupNum, @Param("goodId")Long goodId);

    List<BuyGood> selectWarehouse(Long goodId);

    void updateWarehouse(BuyGood wareGood);

    void updateOrderStart(@Param("groupNum")String groupNum);

    List<BuyGood> getDifferenceValue(@Param("groupNum")String groupNum, @Param("goodId")Long goodId);
}
