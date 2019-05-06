package com.hy.threadpool.threadimpl;

import com.hy.entity.order.EXOrderItem;
import com.hy.entity.order.WaveSortingGroup;
import com.hy.entity.product.CargoSpace;
import com.hy.entity.product.Merchant;
import com.hy.entity.product.Product;
import com.hy.service.order.EXOrderService;
import com.hy.threadpool.ResultDto;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import com.hy.util.sqlserver.DBConnectionManager;
import com.hy.util.sqlserver.Midorders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool
 * @Email 1023012015@qq.com
 * @date 2019/3/25 16:32
 */
public class CheckExwareOrderofPer implements Callable<ResultDto> {
    private EXOrderItem exoi = null;
    private List<EXOrderItem> exoiList = new ArrayList();
    private List<PageData> pds = null;
    private Product product = null;
    private CargoSpace cargoSpace = null;
    private Merchant merchant = null;
    private List<Merchant> merchantList = new ArrayList();
    private List<Merchant> merchantListP = null;
    private Midorders midorders = null;
    private List<Midorders> midordersList = new ArrayList();
    private boolean prsFlag = false;
    private boolean mFlag = false;
    private DBConnectionManager dcm = new DBConnectionManager();
    private int pint = -1;
    private double totalQuantity = 0.0D;
    private int boxNum = 0;
    private int splitTotalCount = 0;
    private int splitPerCount = 0;
    private int pdsi = 0;
    private PageData pdt;
    private List<Product> prList = new ArrayList();
    private WaveSortingGroup wsg;
    private PageData pde;
    private EXOrderService eXOrderService;

    public CheckExwareOrderofPer(PageData pdt,  WaveSortingGroup wsg,  EXOrderService eXOrderService) {
        this.pdt = pdt;
        this.wsg = wsg;
        this.eXOrderService = eXOrderService;
    }

    @Override
    public ResultDto call() throws Exception {
        try {
            //获取出库单码order_num
            String order_num = pdt.getString("order_num");
            PageData pa = new PageData();
            pa.put("order_num", order_num);
            //根据订单号码查询出库单详情
            //	PageData  pg = exwarehouseorderService.findexorderitem(pdt);
            //获取商品id
            mFlag = false;
            prsFlag = false;
            product = new Product();
            product.setId(
                    Long.parseLong(StringUtil.isEmpty(pdt.getString("pid")) ? "0" : pdt.getString("pid")));
            merchant = new Merchant();
            merchant.setId(
                    Long.parseLong(StringUtil.isEmpty(pdt.getString("mid")) ? "0" : pdt.getString("mid")));
            merchant.setMerchantName(
                    StringUtil.isEmpty(pdt.getString("mname")) ? "0" : pdt.getString("mname"));
            merchant.setMerchantNum(
                    StringUtil.isEmpty(pdt.getString("mnum")) ? "0" : pdt.getString("mnum"));
            merchant.setProductCount(StringUtil.isEmpty(pdt.getString("fq")) ? "0" : pdt.getString("fq"));
            merchant.setTexoiId(Long.parseLong(
                    StringUtil.isEmpty(pdt.getString("texoiId")) ? "0" : pdt.getString("texoiId")));
            if (!StringUtil.isEmpty(pdt.getString("boxNumber"))) {
                boxNum = Integer.parseInt(pdt.getString("boxNumber"));
                //System.out.println("数量===" + merchant.getProductCount());
                //System.out.println("箱装数===" + boxNum);
                if (boxNum > 0) {
                    splitTotalCount = new Double(Double.parseDouble(merchant.getProductCount())).intValue()
                            / boxNum;
                    //System.out.println("splitTotalCount====" + splitTotalCount);
                    splitPerCount = new Double(Double.parseDouble(merchant.getProductCount())).intValue()
                            % boxNum;
                    //System.out.println("splitPerCount====" + splitPerCount);
                }
            }
            merchant.setSplitTotalCount(splitTotalCount + "");
            merchant.setSplitPerCount(splitPerCount + "");
            if ((prList != null) && (prList.size() > 0)) {
                for (int prsi = 0; prsi < prList.size(); prsi++) {
                    if (product.getId() == ((Product) prList.get(prsi)).getId()) {
                        prsFlag = true;
                        totalQuantity = ((Product) prList.get(prsi)).getTotalQuantity() + new Double(
                                Double.parseDouble(StringUtil.isEmpty(pdt.getString("fq")) ? "0"
                                        : pdt.getString("fq"))).intValue();
                        ((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
                        ((Product) prList.get(prsi)).getMerchants().add(merchant);
                        break;
                    }
                }
            }
            if ((!prsFlag) || (pdsi == 0)) {
                cargoSpace = new CargoSpace();
                merchantListP = new ArrayList();
                cargoSpace.setId(Long.parseLong(StringUtil.isEmpty(pdt.getString("cargoSpace_id")) ? "0"
                        : pdt.getString("cargoSpace_id")));
                cargoSpace.setZone(StringUtil.isEmpty(pdt.getString("zone")) ? "0" : pdt.getString("zone"));
                cargoSpace.setStorey(
                        StringUtil.isEmpty(pdt.getString("storey")) ? "0" : pdt.getString("storey"));
                cargoSpace.setStoreyNum(
                        StringUtil.isEmpty(pdt.getString("storeyNum")) ? "0" : pdt.getString("storeyNum"));
                product.setBarCode(
                        StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
                product.setBoxNumber(
                        StringUtil.isEmpty(pdt.getString("boxNumber")) ? "0" : pdt.getString("boxNumber"));
                product.setProductName(
                        StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
                product.setUnit(
                        StringUtil.isEmpty(pdt.getString("unitName")) ? "0" : pdt.getString("unitName"));
                product.setSalePrice(
                        Double.parseDouble(StringUtil.isEmpty(pdt.getString("product_price")) ? "0"
                                : pdt.getString("product_price")));
                product.setCargoSpace(cargoSpace);
                product.setTaxes(wsg.getWaveSortingGroupNum());

                merchantListP.add(merchant);
                product.setMerchants(merchantListP);
                prList.add(product);
            }

            if ((prList != null) && (prList.size() > 0)) {
                for (int prsi = 0; prsi < prList.size(); prsi++) {
                    if (((Product) prList.get(prsi)).getMerchants() != null) {
                        if (((Product) prList.get(prsi)).getMerchants().size() > 0) {
                            totalQuantity = 0.0D;
                            for (int pmi = 0; pmi < ((Product) prList.get(prsi)).getMerchants()
                                    .size(); pmi++) {
                                totalQuantity += Double.parseDouble(
                                        ((Merchant) ((Product) prList.get(prsi)).getMerchants().get(pmi))
                                                .getSplitPerCount());
                            }
                            ((Product) prList.get(prsi)).setTotalQuantity(totalQuantity);
                        }
                    }
                }
            }
            if ((merchantList != null) && (merchantList.size() > 0)) {
                for (int msi = 0; msi < merchantList.size(); msi++) {
                    if (merchant.getId() == ((Merchant) merchantList.get(msi)).getId()) {
                        mFlag = true;
                        if (Integer.parseInt(merchant.getSplitPerCount()) <= 0)
                            break;
                        ((Merchant) merchantList.get(msi)).setImageUrl("1");
                        break;
                    }
                }
            }
            if ((!mFlag) || (pdsi == 0)) {
                if (Integer.parseInt(merchant.getSplitPerCount()) > 0) {
                    merchant.setImageUrl("1");
                }
                merchantList.add(merchant);
            }
            midorders = new Midorders();
            midorders.setBarcode(
                    StringUtil.isEmpty(pdt.getString("pbarcode")) ? "0" : pdt.getString("pbarcode"));
            midorders.setGoodsname(
                    StringUtil.isEmpty(pdt.getString("pname")) ? "0" : pdt.getString("pname"));
            midorders.setGoodssize(pdt.getString("unitName"));
            midorders.setOrderId(pdt.getString("zy_order_num"));
            midorders.setQuantity(Integer.parseInt(
                    StringUtil.isEmpty(merchant.getSplitPerCount()) ? "0" : merchant.getSplitPerCount()));
            midorders.setShopid(merchant.getMerchantNum());
            midorders.setShopname(merchant.getMerchantName());
            midorders.setSku(pdt.getString("pnum"));
            midorders.setWmsorderId(pdt.getString("texoiId"));
            midordersList.add(midorders);

            pdsi++;

            exoi = new EXOrderItem();
            exoi.setId(merchant.getTexoiId());
            exoi.setPerCount(merchant.getSplitPerCount());
            exoi.setTotalCount(merchant.getSplitTotalCount());
            pde = new PageData();
            pde.put("id", Long.valueOf(merchant.getTexoiId()));
            pde.put("per_count", merchant.getSplitPerCount());
            pde.put("total_count", merchant.getSplitTotalCount());
            this.eXOrderService.editPerCountAndTotalCount(pde);
        } catch (Exception e) {
            return new ResultDto(false);
        }
        return new ResultDto(true, prList);
    }
}
