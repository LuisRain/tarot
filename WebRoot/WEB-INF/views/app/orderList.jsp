<%--
  Created by IntelliJ IDEA.
  User: sren
  Date: 18-12-7
  Time: 上午8:43
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
    <link rel="stylesheet" href="/plug-in/app/css/icon.css">
    <link rel="stylesheet" href="/plug-in/app/css/dialog.css">
    <link rel="stylesheet" href="/plug-in/app/css/shopCart.css">

</head>



<body>
<!-- 头部 -->
<header>
    <span>订单</span>
    <input type="button" value="编辑" class="edit">
</header>
<!-- 购物车内容 -->
<div class="main" id="contenter">
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
        <li  class="on">
            <a href="/app/orderList">
                <img src="/plug-in/app/image/public/shopCart.png">
                <p>订单</p>

            </a>
        </li>
        <!-- 我的 -->
        <li>
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
<script src="/plug-in/app/js/art-template.js"></script>
<script src="/plug-in/app/js/dialog.js"></script>
<script type="text/html" id="shopProduct">
    <section>
        <!-- 产品内容 -->
        <ul class="product">
            {{each data}}
            <li class="clearfix listBox">
                <%--<label class="fl check">
                    <span class="shopCart_icon unCheck_icon"></span>
                    <input type="checkbox">
                </label>--%>

                <a class="fr clearfix">
                    <%--<img src="/plug-in/app/image/home/1.jpg" class="orderImg"/>--%>
                        <img src="{{$value.image}}" class="orderImg"/>
                    <div class="listText">
                        <h3>{{$value.goodsName}}</h3>
                        <p>{{$value.goodsCode}}<span style="color: red">{{$value.isImport}}{{$value.activity}}</span></p>
                    </div>
                    <!-- 产品操作区域 -->
                    <span class="newPrice">￥{{$value.goodsUnitPrice}}</span>
                    <div class="operate">

                        <!-- cartId -->
                        <input type="hidden" class="cartId" value={{$value.orderId}}>
                        <!-- 产品id -->
                        <input type="hidden" class="goodsId" value={{$value.id}}>
                        <input type="hidden" class="minOrderNum" value={{$value.minOrderNum}}>
                        <input type="hidden" class="productTypeId" value={{$value.productTypeId}}>
                        <input type="button" class="minus shopCart_icon minus_icon" >
                        <input class="num inp shopCart_icon middle_icon" value="{{$value.goodsAmount}}"></input>
                        <input type="button" class="add shopCart_icon add_icon">

                    </div>
                </a>
                <div class="delete">
                    <input type="button" class="del shopCart_icon del_icon">
                </div>
            </li>
            {{/each}}
        </ul>
        <!-- 最终结算 -->
        <div class="cleanInfor">
            <span>合计:</span>
            <!-- 合计金额 -->
            <span class="sum">￥</span>
            <!-- 产品数量 -->
            |&nbsp;
            <span class="all_num">件</span>
            <input type="button" value="提交" class="clean" onclick="submit()" >
        </div>
    </section>
</script>
<script type="text/html" id="emptyCart">
    <div id="emptydiv" >
        <img src="../public/images/emptyCart.png">
        <p>购物车空空如也，快去看看买点什么~</p>
        <a href="../index.html">去逛逛</a>
    </div>
</script>
<script src="/plug-in/app/js/shopCart.js"></script>
<!-- 制作模板 -->

</html>
