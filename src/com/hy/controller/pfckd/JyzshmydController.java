package com.hy.controller.pfckd;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hy.controller.base.BaseController;
import com.hy.util.Const;
import com.hy.util.HttpUtil;
import com.hy.util.PageData;

/**
 * 加油站收货满意率
 * @author gyy
 *
 */
@Controller
@RequestMapping("jyzshmydcontroller")
public class JyzshmydController extends BaseController {
	
	/**
	 * 获取货运平台加油站满意率
	 * @return
	 */
	@RequestMapping({ "findmyd" })
	public ModelAndView findMyd(){
		ModelAndView mv = new ModelAndView();
		try {
			 Map<String,Object> map = new  HashMap<String,Object>();
			 PageData pd = this.getPageData();
			 String month = pd.getString("month");
			 if(month != null && !month.equals("")){
				 map.put("month", month);
			 }else{
				 map.put("month", "201807");
			 }
			String result = HttpUtil.HttpClientPost(Const.httpplan+"interfaces/queryevaluation.do","urf-8", map); //调用货运平台接口获取相关数据
			result = URLDecoder.decode(result, "UTF-8");		//对获取到的数据进行解码
			JsonObject  json = (JsonObject) new JsonParser().parse(result);	//使用gosn的方式转换json对象
			JsonArray msg = json.get("msg").getAsJsonArray();
			List<String> groupNumList = new ArrayList<String>();
			List<String> myd = new ArrayList<String>();
			for (int i = 0; i < msg.size(); i++) {
				JsonObject jsonobject = msg.get(i).getAsJsonObject();
				String groupnum = jsonobject.get("group_number").toString();		//批次号
				groupNumList.add(groupnum);
				double cpnum = Double.parseDouble(jsonobject.get("cpnum").getAsString());  //投诉数量
				double total = Double.parseDouble(jsonobject.get("total").getAsString()); //门店总数
				double test = cpnum/total;
				int fs = 0;  //得分数
				// 客户满意度 =  投诉数/本批次门店总数 ； 如果大于 98% 的20分，如果小于98%得分为：客户满意度/98%*20； 
				if(cpnum == 0.0){
					fs = 20;
				}else{
					if(test >= 0.98){
						fs = 20;
					}else if(test < 0.98){
						fs = (int) (test/0.98*20);
					}
				}
				myd.add(fs+"");
			}
			mv.addObject("groupNumList", groupNumList);
			mv.addObject("myd", myd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("pfckd/findMyd");
		return mv;
	}
	
}
