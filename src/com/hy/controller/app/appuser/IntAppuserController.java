package com.hy.controller.app.appuser;

import com.hy.controller.base.BaseController;
import com.hy.service.system.appuser.AppuserService;
import com.hy.util.AppUtil;
import com.hy.util.Logger;
import com.hy.util.PageData;
import com.hy.util.Tools;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/appuser"})
public class IntAppuserController
  extends BaseController
{
  @Resource(name="appuserService")
  private AppuserService appuserService;
  
  @RequestMapping({"/getAppuserByUm"})
  @ResponseBody
  public Object getAppuserByUsernmae()
  {
    logBefore(this.logger, "根据用户名获取会员信息");
    Map<String, Object> map = new HashMap();
    PageData pd = new PageData();
    pd = getPageData();
    String result = "00";
    try {
      if (Tools.checkKey("USERNAME", pd.getString("HYKEY"))) {
        if (AppUtil.checkParam("getAppuserByUsernmae", pd)) {
          pd = this.appuserService.findByUId(pd);
          
          map.put("pd", pd);
          result = pd == null ? "02" : "01";
        }
        else {
          result = "03";
        }
      } else {
        result = "05";
      }
    } catch (Exception e) {
      this.logger.error(e.toString(), e);
    } finally {
      map.put("result", result);
      logAfter(this.logger);
    }
    return AppUtil.returnObject(new PageData(), map);
  }
}


