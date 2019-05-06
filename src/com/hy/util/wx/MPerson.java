package com.hy.util.wx;

import com.hy.util.PageData;

public class MPerson {
  public String CREATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + WechatTaskService.getAccessToken();
  
  public String UPDATA_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=" + WechatTaskService.getAccessToken();
  
  public String DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + WechatTaskService.getAccessToken() + "&userid=ID";
  
  public String GET_PERSON_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + WechatTaskService.getAccessToken() + "&userid=ID";
  
  public String GET_GROUP_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" + WechatTaskService.getAccessToken() + "&department_id=ID&fetch_child=0&status=0";
  
  public static String Create(PageData pd)
  {
    String PostData = "{\"userid\": \"%s\",\"name\": \"%s\",\"mobile\": \"%s\",\"department\": %s}";
    return String.format(PostData, new Object[] { pd.get("userid").toString(), pd.get("name").toString(), pd.get("phone").toString(), Integer.valueOf(13) });
  }
  
  public static String Updata(PageData pd)
  {
    String PostData = "{\"userid\": \"%s\",\"name\": \"%s\",\"mobile\": \"%s\",\"department\": %s}";
    return String.format(PostData, new Object[] { pd.get("userid").toString(), pd.get("name").toString(), pd.get("phone").toString(), Integer.valueOf(13) });
  }
  
  public static String Delete(String userid)
  {
    MPerson mperson = new MPerson();
    String delete_url = mperson.DELETE_URL.replace("ID", userid);
    return delete_url;
  }
  
  public static String GPerson(String userid)
  {
    MPerson mperson = new MPerson();
    String getperson_url = mperson.GET_PERSON_URL.replace("ID", userid);
    return getperson_url;
  }
  
  public static String GGroup(String department_id)
  {
    MPerson mperson = new MPerson();
    String getgroup_url = mperson.GET_GROUP_URL.replace("ID", department_id);
    return getgroup_url;
  }
}


