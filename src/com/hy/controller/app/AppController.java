package com.hy.controller.app;

import com.hy.controller.base.BaseController;
import com.hy.entity.base.*;
import com.hy.entity.system.User;
import com.hy.service.app.AppService;
import com.hy.service.system.user.UserService;
import com.hy.util.DateUtil;
import com.hy.util.PageData;
import com.hy.util.StringUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author sren
 * @create 2018-12-31 下午12:39
 **/

@Controller
@RequestMapping(value = "/app")
public class AppController  extends BaseController {

    @Resource(name="userService")
    private UserService userService;

    @Resource(name = "appService")
    private AppService appService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "app/login";
    }


    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse checkUser(User user, HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        PageData pd = new PageData();
        pd.put("USERNAME", user.getUSERNAME());
        String passwd = new SimpleHash("SHA-1", user.getUSERNAME(), user.getPASSWORD()).toString();
        pd.put("PASSWORD", passwd);
        pd = this.userService.getUserByNameAndPwd(pd);
        if (pd != null) {
            pd.put("LAST_LOGIN", DateUtil.getTime().toString());
            this.userService.updateLastLogin(pd);
            user.setUSER_ID(Long.parseLong(pd.getString("USER_ID")));
            user.setNAME(pd.getString("NAME"));
            user.setRIGHTS(pd.getString("RIGHTS"));
            user.setROLE_ID(pd.getString("ROLE_ID"));
            user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
            user.setIP(pd.getString("IP"));
            user.setSTATUS(pd.getString("STATUS"));
            user.setCkId(Integer.valueOf(pd.getString("ck_id")));
            session.setAttribute("user", user);
            return ApiResponse.ofSuccess(null);
        }
        return ApiResponse.ofMessage(400, "用户名或密码错误");
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(HttpServletRequest request) {
        if (sessionUserIsNull(request)) {
            return "app/login";
        }
        return "app/home";
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request) {
        if (sessionUserIsNull(request)) {
            return "app/login";
        }
        return "app/home";
    }

    @RequestMapping(value = "/goods")
    @ResponseBody
    public ApiResponse getGoods(Page page, String search, HttpServletRequest request) {
        ApiResponse result = null;
        if (!StringUtil.isEmpty(search)) {
            page.setCategoryId(null);
            result= appService.getList(page, search);
        }else{
         result = appService.getListOne(page);
        }
        return result;
    }

    @RequestMapping(value = "/getCategory")
    @ResponseBody
    public ApiResponse getCategory() {
        ApiResponse result = appService.getCategory();
        return result;
    }

    @RequestMapping(value = "search")
    @ResponseBody
    public ApiResponse search(Page page,String search) {
        //search = search.substring(1);
        ApiResponse result = null;
        if (search == null || search == "") {
            result = appService.getList(page);
        } else {
            result = appService.getList(page, search);
        }
        return result;
    }


    @RequestMapping(value = "/toCommodityDetails")
    public String goodDetails(Model model, @RequestParam("id") Integer id, HttpServletRequest request) {
        if (sessionUserIsNull(request)) {
            return "app/login";
        }
        model.addAttribute("id", id);
        return "app/commodityDetails";
    }


    @RequestMapping(value = "/findById")
    @ResponseBody
    public Goods getGoodsById(Integer id) {
        Goods goods = appService.getGoodsById(id);
        return goods;
    }

    @RequestMapping(value = "/addCart")
    @ResponseBody
    public ApiResponse addCart(@RequestParam("id") Integer id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ApiResponse result = appService.addCart(id, user);
        return result;
    }

    @RequestMapping(value = "/orderList")
    public String orderList() {
        return "app/orderList";
    }

    @RequestMapping(value = "/getOrderList")
    @ResponseBody
    public ApiResponse getOrderList(HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ApiResponse result = appService.getOrderList(user.getUSER_ID());
        return result;
    }

    @RequestMapping(value = "/modifyCard")
    @ResponseBody
    public ApiResponse modifyCard(@RequestParam("orderGoodsId") Integer orderGoodsId, @RequestParam("num") Integer num) {
        ApiResponse result = appService.modifyCard(orderGoodsId, num);
        return result;
    }

    @RequestMapping(value = "/inputModifyCard")
    @ResponseBody
    public ApiResponse inputModifyCard(@RequestParam("orderGoodsId") Integer orderGoodsId, @RequestParam("num") Integer num) {
        ApiResponse result = appService.inputModifyCard(orderGoodsId, num);
        return result;
    }

    @RequestMapping(value = "/deleteOrderGoods")
    @ResponseBody
    public ApiResponse deleteOrderGoods(@RequestParam("orderGoodsId") Integer orderGoodsId) {
        ApiResponse result = appService.deleteOrderGoods(orderGoodsId);
        return result;
    }

    @RequestMapping(value = "/submit")
    @ResponseBody
    public ApiResponse submit(HttpServletRequest request, @RequestParam("orderId") Integer orderId) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ApiResponse result = appService.submit(user.getUSER_ID(), orderId);
        return result;
    }


    @RequestMapping(value = "/baseUser")
    @ResponseBody
    public ApiResponse getBaseUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ApiResponse result = appService.getBaseUser(user.getUSERNAME());
        return result;
    }

    @RequestMapping(value = "/getMyOrderList")
    @ResponseBody
    public ApiResponse getMyOrderList(Page page, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ApiResponse result = appService.getMyOrderList(page, user.getUSER_ID());
        return result;
    }


    @RequestMapping(value = "/personal")
    public String personal(HttpServletRequest request) {
        if (sessionUserIsNull(request)) {
            return "app/login";
        }
        return "app/personal";
    }


    /**
     * 退出
     * @return
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public ApiResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return ApiResponse.ofSuccess(null);
    }

    @RequestMapping(value = "/orderDetails")
    public ModelAndView orderDetails(@RequestParam("orderId") String orderId, HttpServletRequest request) {
        if (sessionUserIsNull(request)) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("app/login");
        }
        HttpSession session = request.getSession();
        session.setAttribute("orderId", orderId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("app/orderDetails");
        return modelAndView;
    }


    @RequestMapping(value = "/getOrderDetails")
    @ResponseBody
    public ApiResponse getOrderDetails(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String orderId = (String) session.getAttribute("orderId");
        ApiResponse result = appService.getOrderDetails(orderId);
        return result;
    }


    @RequestMapping(value = "/modifyPassword")
    public String modifyPassword() {
        return "app/password";
    }



    @RequestMapping(value = "/modifyPword")
    @ResponseBody
    public ApiResponse modifyPword(AppUser user, HttpServletRequest req) {
        /*TSUser tsUser = userService.checkUserExits(user.getUserName(), user.getOldPassword());
        if (tsUser == null) {
            return ApiResponse.ofMessage(5000, "用户名或密码错误");
        }
        try {
            tsUser.setPassword(PasswordUtil.encrypt(tsUser.getUserName(), user.getNewPasswprd(), PasswordUtil.getStaticSalt()));
            tsUser.setStatus(Globals.User_Normal);
            tsUser.setActivitiSync(tsUser.getActivitiSync());
            systemService.updateEntitie(tsUser);
            return ApiResponse.ofSuccess("修改成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.ofMessage(5000, "系统出错了...");
        }*/
        //查询用户是否存在

        ApiResponse result = appService.modifyPword(user);
        return result;
    }

    private Boolean sessionUserIsNull(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return user==null ? true : false;
    }
}
