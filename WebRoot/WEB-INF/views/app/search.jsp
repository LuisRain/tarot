<%--
  Created by IntelliJ IDEA.
  User: sren
  Date: 18-12-6
  Time: 下午1:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <!-- 对不识别viewport设备优化 -->
    <meta name="HandheldFriendly" content="true">
    <!-- windows phone 点击无高光 -->
    <meta name="msapplication-tap-highlight" content="no">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <title>搜索 | 中国石油</title>
    <link rel="icon" type="image/x-icon" href="../favicon.ico" />
    <link rel="stylesheet" type="text/css" href="plug-in/app/css/reset.css">
    <link rel="stylesheet" href="plug-in/app/css/dialog.css">
    <link rel="stylesheet" type="text/css" href="plug-in/app/css/search.css?t=201801261140">
</head>

<body>
<!-- 搜索框 -->
<div class="search">
    <a onclick="javascript:window.history.back()">
        <img src="plug-in/app/images/leftArrow.png" alt="" class="back">
    </a>
    <form target="frameFile" action="#">
        <input type="search" placeholder="请输入商品、店铺">
        <iframe name='frameFile' style="display: none;"></iframe>
    </form>
</div>
<!-- 搜索历史 -->
<div class="log_title clearfix">
    <div class="fl">历史搜索</div>
    <!-- 删除图标 -->
    <div class="delete fr">
        <img src="./images/delete.png" alt="">
    </div>
</div>
<!-- 搜索历史内容 -->
<div class="log">
    <ul class="clearfix"></ul>
</div>
</body>
<script src="plug-in/app/js/rem.js"></script>
<script src="plug-in/app/js/jquery1.7.2.js"></script>
<script src="plug-in/app/js/dialog.js"></script>
<script src="plug-in/app/js/art-template.js"></script>
<script src="plug-in/app/js/search.js?t=201801301509"></script>
<script>

</script>

</html>