<%--
  Created by IntelliJ IDEA.
  User: sren
  Date: 18-12-6
  Time: 下午1:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head lang="en" style="height:100%">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <!-- 对不识别viewport设备优化 -->
    <meta name="HandheldFriendly" content="true">
    <!-- windows phone 点击无高光 -->
    <meta name="msapplication-tap-highlight" content="no">
    <title>首页 | 加油山西</title>
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
    <link rel="stylesheet" href="/plug-in/app/css/footer.css">
    <link rel="stylesheet" href="/plug-in/app/css/reset.css">
    <link rel="stylesheet" href="/plug-in/app/css/dialog.css">
    <link rel="stylesheet" href="/plug-in/app/css/home.css?t=201802021359">

</head>


<body style="height:100%">
<div class="scrollMain" style="height:100%;overflow-y:scroll">
    <div class="header">
        <!-- 顶部导航栏 -->
        <div id="nav">
            <div id="search">
                <input type="search" placeholder="请输入商品名称">
                <span class="searchText" onclick="search()">搜索</span>
            </div>
        </div>
    </div>
    <!-- 商品部分 -->
    <div class="variety">
        <div class="variety_left " id="class">
            <!--<ul>-->
            <!--<li class="active" id="1">分类</li>-->
            <!--<li id="2">分类</li>-->
            <!--<li id="3">分类</li>-->
            <!--<li id="4">分类</li>-->
            <!--</ul>-->
        </div>
        <div  id="commodity" class="variety_right  scrollMain" style="height:100%;overflow-y:scroll;padding-bottom: 53px;"></div>
    </div>

    <!-- 底部 -->
    <footer>
        <ul>
            <!-- 主页 -->
            <li class="on">
                <a href="/app/home">
                    <img src="/plug-in/app/image/public/home.png">
                    <p>商品</p>
                </a>
            </li>
            <li>
                <a href="/app/orderList">
                    <img src="/plug-in/app/image/public/shopCart_un.png">
                    <p>订单</p>
                    <i class="cartNum">11</i>
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
</div>
</body>
<script src="/plug-in/app/js/jquery1.7.2.js"></script>
<script src="/plug-in/app/js/dialog.js"></script>
<script src="/plug-in/app/js/iscroll.js"></script>
<script src="/plug-in/app/js/rem.js"></script>
<script src="/plug-in/app/js/template-web.js"></script>
<script src="/plug-in/app/js/home.js?t=201802021359"></script>
<!-- 制作模板 -->
<script type="text/html" id="commodityTemp">
    <ul class="listBox">
        {{each data}}
            <li>
                <div class="listLeft">
                   <%-- <img src="/plug-in/app/image/home/1.jpg"/>--%>
                       <img src="{{$value.image}}"/>
                    <div class="listText" onclick="openDetails('{{$value.id}}')">
                        <h3>{{$value.goodsName}}</h3>
                        <p>{{$value.goodsCode}}<span style="color: red">{{$value.isImport}}{{$value.activity}}</span></p>
                    </div>
                </div>
                <div class="listRight">
                    <h3>￥{{$value.goodsRetailPrice}}</h3>
                    <img src="/plug-in/app/image/home/shopCart_un.png" onclick="addShop('{{$value.id}}')"/>
                </div>
            </li>
        {{/each}}
    </ul>

</script>
<script type="text/html" id="classTemp">
    <ul id="category">
        {{each data}}
        <li id="{{$value.id}}">{{$value.classifyName}}</li>
        <%--<li id="2">分类</li>
        <li id="3">分类</li>
        <li id="4">分类</li>--%>
        {{/each}}
    </ul>
</script>

</html>
