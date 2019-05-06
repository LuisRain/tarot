<%--
  Created by IntelliJ IDEA.
  User: sren
  Date: 18-12-7
  Time: 下午4:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head lang="en" style="height:100%">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <!-- 对不识别viewport设备优化 -->
    <meta name="HandheldFriendly" content="true">

    <title>订单列表</title>
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
    <link rel="stylesheet" href="/plug-in/app/css/reset.css">
    <link rel="stylesheet" href="/plug-in/app/css/footer.css">
    <link rel="stylesheet" href="/plug-in/app/css/header.css">
    <link rel="stylesheet" href="/plug-in/app/css/dialog.css">
    <link rel="stylesheet" href="/plug-in/app/css/myorder.css">

</head>



<body style="height:100%;overflow: auto">
<div style="overflow-y:scroll;height:100%;" >
    <header>
        <span>我的</span>
    </header>

    <div class="area">
        <div class="orderTop">
            <span class="site"></span>
            <span class="userName"></span>
            <p class="address"></p>
        </div>


        <div class="setting">
            <ul>

                <li class="comment">
                    <a href="#">关于我们</a>
                    <!-- <img src="../public/images/rightArrow.png"> -->
                </li>
                <li>
                    版本号
                    <span>1.0</span>
                </li>

            </ul>
            <a onclick="logout();">退出登录</a>
            <%--<span>修改密码</span>--%>
        </div>

        <div class="myOrder">
            <h3 class="title">历史订单</h3>
            <div id="myOrder" class="scrollMain" style="overflow-y:scroll;padding-bottom: 53px;" ></div>

        </div>
    </div>


</div>
<!-- 底部 -->
<footer>
    <ul>
        <!-- 主页 -->
        <li>
            <a href="/app/home">
                <img src="/plug-in/app/image/public/home_un.png">
                <p>商品</p>
            </a>
        </li>
        <li >
            <a href="/app/orderList">
                <img src="/plug-in/app/image/public/shopCart_un.png">
                <p>订单</p>

            </a>
        </li>
        <!-- 我的 -->
        <li class="on">
            <a href="/app/personal">
                <img src="/plug-in/app/image/public/mine.png">
                <p>我的</p>
            </a>
        </li>
    </ul>
</footer>
</body>
<script src="/plug-in/app/js/rem.js"></script>
<script src="/plug-in/app/js/jquery1.7.2.js"></script>
<script src="/plug-in/app/js/template-web.js"></script>
<script src="/plug-in/app/js/dialog.js"></script>
<script src="/plug-in/app/js/myOrder.js"></script>
<script type="text/html" id="myOrderTem">
    <ul class="listBox">
        {{each data}}
        <li class="clearfix">
            <div class="listLeft">
                <img src="/plug-in/app/image/home/1.jpg"/>
                <div class="listText">
                    <h3>{{$value.code}}</h3>
                    <p>{{$value.createDate}}</p>
                </div>
            </div>
            <a class="listRight" href="/app/orderDetails?orderId={{$value.id}}">
                查看详情&nbsp;&nbsp;&gt;
            </a>
        </li>
        {{/each}}
    </ul>
</script>



</html>