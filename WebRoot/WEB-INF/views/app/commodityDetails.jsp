<%--
  Created by IntelliJ IDEA.
  User: jxkj
  Date: 2018/12/7
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head lang="en" style="height:100%">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <!-- 对不识别viewport设备优化 -->
    <meta name="HandheldFriendly" content="true">

    <title>商品详情</title>
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
    <link rel="stylesheet" href="/plug-in/app/css/reset.css">
    <link rel="stylesheet" href="/plug-in/app/css/footer.css">
    <link rel="stylesheet" href="/plug-in/app/css/header.css">
    <link rel="stylesheet" href="/plug-in/app/css/dialog.css">
    <link rel="stylesheet" href="/plug-in/app/css/commodifyDetails.css">

</head>

<body>
<div class="scrollMain" style="height:100%;overflow-y:scroll">
    <header>
        <a onclick="javascript:window.history.back(-1);return false" class="back">
            <img src="/plug-in/app/image/public/leftArrow.png" alt="">
        </a>
        <span>商品详情</span>
    </header>
    <div class="area">
        <input id="commodify_id" type="hidden" value="${id }"/>
        <div id="commodify">
           <%-- <ul class="listBox">
            <li>
            <div class="listLeft">
            <img src="image/home/1.jpg"/>
            <div class="listText">
            <h3>商品商品商品</h3>
            <p>2018-12-12</p>
            </div>
            </div>
            <span class="listRight" >
            ￥12.00
            </span>
            </li>
            </ul>--%>
        </div>

        <div class="details" id="details">
           <%-- <ul>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            <li>商品编码：111</li>
            </ul>--%>
        </div>
        <span class="orderBottom" onclick="addShop1()">加入订单</span>
    </div>
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
</body>
<%--<script src="js/rem.js"></script>--%>
<script src="/plug-in/app/js/jquery1.7.2.js"></script>
<script src="/plug-in/app/js/template-web.js"></script>
<script src="/plug-in/app/js/dialog.js"></script>
<%--<script src="js/myOrder.js"></script>--%>
<script src="/plug-in/app/js/commodityDetails.js"></script>
<!--商品详情数据模板-->
<script type="text/html" id="detailsTem">
    <ul>
        <li>大类：{{$data.type}}</li>
		<li>条形码：{{$data.goodsBarcode}}</li>
		<li>箱装数量：{{$data.standard}}</li>
		<li>最小订货量：{{$data.minOrderNum}}</li>
        <li>计量单位：{{$data.goodsSaleUnit}}
        </li>		
        <li>包装单位：{{$data.packUnit}}</li>
        <li>供应商编码：{{$data.supplierCode}}</li>
		<li>供应商名称：{{$data.supplierName}}</li>    
        <li>建议零售价：{{$data.goodsRetailPrice}}</li>
        <li>毛利率：{{$data.grossMargin}}</li>
        <li>保质期天数：{{$data.goodsShelfLife}}</li>        
        <li>税率：{{$data.goodsTaxRate}}</li>
        <%--<li>商品状态：{{$data.saleType}}</li>--%>       
        <li>备注：{{$data.remark}}</li>
    </ul>
</script>
<!--商品列表模板-->
<script type="text/html" id="commodifyTem">
    <ul class="listBox">
        <li>
            <div class="listLeft">
                <%--<img src="/plug-in/app/image/home/1.jpg"/>--%>
                    <img src="{{$data.image}}"/>
                <div class="listText">
                    <h3>{{$data.goodsName}}</h3>
                    <p>{{$data.goodsCode}}<span style="color: red">{{$data.isImport}}</span></p>
                </div>
            </div>
            <span class="listRight" >
                <h3>￥{{$data.goodsRetailPrice}}</h3>
               <%-- <img src="plug-in/app/image/home/shopCart_un.png"/>--%>
            </span>
        </li>
    </ul>
</script>
</html>
