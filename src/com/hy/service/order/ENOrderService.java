package com.hy.service.order;

import com.hy.dao.DaoSupport;
import com.hy.entity.Page;
import com.hy.entity.order.ENOrder;
import com.hy.util.Logger;
import com.hy.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("eNOrderService")
public class ENOrderService {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    protected Logger logger = Logger.getLogger(getClass());

    public List<PageData> listPdPagePurchaseOrder(Page page)
            throws Exception {
        return (List) this.dao.findForList(
                "ENOrderMapper.eNOrderlistPage", page);
    }

    public int save(PageData pd)
            throws Exception {
        Object ob = this.dao.save("ENOrderMapper.save", pd);
        return Integer.parseInt(ob.toString());
    }

    public int saveList(List<ENOrder> listENOrder)
            throws Exception {
        Object ob = this.dao.batchSave("ENOrderMapper.saveENOrder", listENOrder);
        return Integer.parseInt(ob.toString());
    }

    public int delete(PageData pd)
            throws Exception {
        Object ob = this.dao.delete("ENOrderMapper.delete", pd);
        return Integer.parseInt(ob.toString());
    }

    public int deleteList(List<ENOrder> listENOrder)
            throws Exception {
        Object ob = this.dao.batchDelete("ENOrderMapper.deleteList", listENOrder);
        return Integer.parseInt(ob.toString());
    }

    public int edit(PageData pd)
            throws Exception {
        Object ob = this.dao.update("ENOrderMapper.edit", pd);
        return Integer.parseInt(ob.toString());
    }

    public ENOrder findById(PageData pd) throws Exception {
        ENOrder eNOrder = new ENOrder();
        return (ENOrder) this.dao.findForObject("ENOrderMapper.findByIdOrOrderNum",
                pd);
    }

}


