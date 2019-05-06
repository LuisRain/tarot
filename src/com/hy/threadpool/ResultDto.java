package com.hy.threadpool;

import com.hy.entity.OrderGood;
import com.hy.entity.product.Product;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author MirSu
 * @version V1.0
 * @Package com.hy.threadpool
 * @Email 1023012015@qq.com
 * @date 2019/3/22 11:44
 */
public class ResultDto <T>{
    private Boolean flag;

    private List<Product> pd;

    private T obj;

    public ResultDto(boolean flag ) {
        this.flag = flag;
    }

    public ResultDto(Boolean flag, List<Product> pd) {
        this.flag = flag;
        this.pd = pd;
    }

    public ResultDto(Boolean flag, T obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<Product> getPd() {
        return pd;
    }

    public void setPd(List<Product> pd) {
        this.pd = pd;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
