<%--
  Created by IntelliJ IDEA.
  User: sren
  Date: 18-12-7
  Time: 下午5:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head lang="en" style="height:100%">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <!-- 对不识别viewport设备优化 -->
    <meta name="HandheldFriendly" content="true">

    <title>订单详情</title>
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
    <link rel="stylesheet" href="/plug-in/app/css/reset.css">
    <link rel="stylesheet" href="/plug-in/app/css/footer.css">
    <link rel="stylesheet" href="/plug-in/app/css/header.css">
    <link rel="stylesheet" href="/plug-in/app/css/dialog.css">
    <link rel="stylesheet" href="/plug-in/app/css/orderDetails.css">

</head>



<body>
<div class="scrollMain" >
    <header>
        <span>订单详情</span>
    </header>
    <div class="area" style="height:100%;overflow-y:scroll;">
        <div class="myOrder" style="padding-bottom: 53px;">
            <div id="orderDetails"></div>
        </div>
        <div class="orderBottom">
            <span>共<i id="commodityNum"></i>件</span>
            <span>合计：<i id="commodityPrice">{{totalMoney}}</i>元</span>
        </div>
    </div>
</div>
<!-- 底部 -->
<footer>
    <ul>
        <!-- 主页 -->
        <li>
            <a href="/app/home">
                <img src="/plug-in/app/image/public/home.png">
                <p>商品</p>
            </a>
        </li>
        <li  >
            <a href="/app/orderList">
                <img src="/plug-in/app/image/public/shopCart_un.png">
                <p>订单</p>

            </a>
        </li>
        <!-- 我的 -->
        <li class="on">
            <a href="/app/personal">
                <img src="/plug-in/app/image/public/mine_un.png">
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
<script src="/plug-in/app/js/orderDetails.js"></script>
<script type="text/html" id="orderDetailsTemp">
    <ul class="listBox">

        {{each goods}}
        <li>
            <div class="listLeft">
                <%--<img src="/plug-in/app/image/home/1.jpg"/>--%>
                    <img src="{{$value.image}}"/>
                <div class="listText">
                    <h3>{{$value.goodsName}}</h3>
                    <p>{{$value.goodsCode}}</p>
                </div>
            </div>
            <div class="listRight">
                <h3>￥{{$value.goodsUnitPrice}}</h3>
                <h4>x{{$value.goodsAmount}}</h4>
            </div>
        </li>
        {{/each}}
    </ul>
</script>



</html>
