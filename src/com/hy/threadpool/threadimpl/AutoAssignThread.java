package com.hy.threadpool.threadimpl;

import com.hy.entity.BuyGood;
import com.hy.mapper.AutoAssignMapper;
import com.hy.threadpool.ResultDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool
 * @Email 1023012015@qq.com
 * @date 2019/3/22 9:11
 */
public class AutoAssignThread implements Callable<ResultDto> {
    private AutoAssignMapper autoAssignMapper;
    //某商品商品库存总数
    private Double sumInventory = null;
    //采购某商品总数
    private Double sunBuyCounts = null;
    //某商品到货率
    private Double k = 0.0;

    //所有门店 改批次的采购商品的集合
    List<BuyGood> exGoods = null;
    //要分配的商品
    private BuyGood  buyGood ;

    private String groupNum;

    public AutoAssignThread(BuyGood buyGood,String groupNum,AutoAssignMapper autoAssignMapper) {
        this.buyGood = buyGood;
        this.groupNum = groupNum;
        this.autoAssignMapper = autoAssignMapper;
    }

    /**
     * @author SuPengFei
     * @DESCRIPTION: //TODO 计算实际配送数
     * @params: [buyCounts, k]
     * @return: java.lang.Double
     * @Date: 2019/3/12 11:53
     * @Modified By:
     */
    private Double getFinalBuyCount(Double buyCounts, Double k) {
        Double finalBuyCount = new BigDecimal(buyCounts).multiply(new BigDecimal(k)).doubleValue();
        return  new BigDecimal(finalBuyCount).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * @author SuPengFei
     * @DESCRIPTION: //TODO 计算到货率
     * @params: [sumInventory, sumBuyCounts]
     * @return: java.lang.Double
     * @Date: 2019/3/12 11:53
     * @Modified By:
     */
    private Double getK(Double sumInventory, Double sumBuyCounts) {
        if (sumBuyCounts != null && sumInventory != null && sumBuyCounts != 0 && sumInventory != 0) {
            Double k = new BigDecimal(sumInventory).divide(new BigDecimal(sumBuyCounts),4,BigDecimal.ROUND_HALF_UP).doubleValue();
            return k > 1 ? 1 : k;
        }
        return 0.0;
    }

    @Override
    public ResultDto call()   {
        boolean flag = true;
        try {
            sumInventory =  autoAssignMapper.getSumInventoryCount(buyGood.getGoodId());
            sumInventory = sumInventory == null ? 0.0 : sumInventory;
            //计算改商品到货率
            sunBuyCounts = buyGood.getSumBuyCounts();
            k = getK(sumInventory, sunBuyCounts);
            //获取所有门店 买批次的采购该商品
            exGoods = autoAssignMapper.getExOrderGood(groupNum,buyGood.getGoodId());
            if (exGoods.size() <= 0) {
                return  new ResultDto(flag);
            }
            //k>1 所有站点都满足
            if (k >= 1) {
                for (BuyGood exGood : exGoods) {
                    exGood.setFinalBuyCounts(exGood.getBuyCounts());
                    autoAssignMapper.updateFinalBuyCounts(exGood);
                }

            } else if (k == 0) {
                for (BuyGood exGood : exGoods) {
                    exGood.setFinalBuyCounts(0.0);
                    autoAssignMapper.updateFinalBuyCounts(exGood);
                }
            } else {
                autoOrderByK(k);
            }

        } catch (Exception e) {
            flag = false;
        }
        return new ResultDto(flag);
    }
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 以下是0<k<1的情况 的分配
     * @params: []
     * @return: void
     * @Date: 2019/3/27 10:26
     * @Modified By:
     * @param k
    */
    private void autoOrderByK(Double k){
        //实际配送数
        Double finalBuyCount = 0.0;
        BuyGood good = new BuyGood();
        //仅有一个站点采购 到货数全部给该站点
        if (exGoods.size() == 1) {
            good = exGoods.get(0);
            good.setFinalBuyCounts(sumInventory);
            autoAssignMapper.updateFinalBuyCounts(good);
        } else {
            //按最小起订量铺货所需库存数
            int first = exGoods.size() * exGoods.get(0).getMinOrderNum();
            double result = sumInventory - first;
            //最小起订量
            Integer minOrderNum = 0;
            if (result <= 0) {
                //按订货量从高到低，每个站仅配送最小起订量 送完为止
                for (BuyGood exGood : exGoods) {
                    //最小起订量
                    minOrderNum = exGood.getMinOrderNum();
                    if (sumInventory <= 0) {
                        exGood.setFinalBuyCounts(0.0);
                        autoAssignMapper.updateFinalBuyCounts(exGood);
                        continue;
                    }
                    if (sumInventory < minOrderNum) {
                        exGood.setFinalBuyCounts(sumInventory);
                    } else {
                        exGood.setFinalBuyCounts(Double.valueOf(minOrderNum));
                    }
                    autoAssignMapper.updateFinalBuyCounts(exGood);
                    sumInventory -= minOrderNum;
                }
            } else {
                sumInventory = sumInventory - first;
                sunBuyCounts = sunBuyCounts - first;
                //剩余应分配数
                double v;
                for (BuyGood exGood : exGoods) {
                    minOrderNum = exGood.getMinOrderNum();
                    if (sumInventory<=0) {
                        exGood.setFinalBuyCounts(Double.valueOf(minOrderNum));
                        autoAssignMapper.updateFinalBuyCounts(exGood);
                        continue;
                    }
                    v = exGood.getBuyCounts() - minOrderNum;
                    finalBuyCount = getFinalBuyCount(v, k);
                    finalBuyCount= Math.ceil(finalBuyCount / minOrderNum) * minOrderNum;
                    finalBuyCount = finalBuyCount > sumInventory ? sumInventory : finalBuyCount;
                    finalBuyCount = finalBuyCount > v ?  getFinalBuyCount(v, k) : finalBuyCount;
                    exGood.setFinalBuyCounts(finalBuyCount + minOrderNum);
                    autoAssignMapper.updateFinalBuyCounts(exGood);
                    //重新计算库存商品数
                    sumInventory -=  finalBuyCount;
                    //重新计算总采购数
                    sunBuyCounts -=  finalBuyCount;
                }
                //计算问题，没有分配完。把多余的库存再次分配，按照差额的大小；
                if (sumInventory > 0) {
                    autoDifference();
                }
            }
        }
    }
    /**
     * @author MirSu
     * @DESCRIPTION: //TODO 把多余的库存再次分配，按照差额的大小；
     * @params: []
     * @return: void
     * @Date: 2019/3/27 11:23
     * @Modified By:
    */
    private void autoDifference(){
        List<BuyGood> goodList = autoAssignMapper.getDifferenceValue(groupNum,buyGood.getGoodId());
        if (goodList != null && goodList.size() > 0) {
            double differenceValue = 0.0;
            double value = 0.0;
            for (BuyGood buyGood : goodList) {
                if (sumInventory <= 0) {
                    break;
                }
                differenceValue = buyGood.getDifferenceValue();
                value = sumInventory - differenceValue > 0 ? differenceValue : sumInventory;
                buyGood.setFinalBuyCounts(buyGood.getFinalBuyCounts()+value);
                autoAssignMapper.updateFinalBuyCounts(buyGood);
                sumInventory -= differenceValue;
            }
        }
    }
}
