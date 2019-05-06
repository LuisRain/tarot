<%--
  Created by IntelliJ IDEA.
  User: sren
  Date: 18-12-6
  Time: 上午8:33
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
    <title>登录 | 山西中国石油</title>
    <link rel="icon" type="image/x-icon" href="../favicon.ico" />
    <link rel="stylesheet" href="/plug-in/app/css/reset.css">
    <link rel="stylesheet" href="/plug-in/app/css/dialog.css">
    <link rel="stylesheet" href="/plug-in/app/css/password.css?t=201801261140">

</head>
<body>
<div class="header">
    <a class="a">
        <img src="/plug-in/app/image/public/leftArrow.png" alt="返回上一页">
    </a>
    修改密码
</div>
<div class="login">
    <form method="post" class="">
        <!-- 找回密码页面电话号码 -->
        <div id="tel">
            <span>用户名:</span>
            <input type="text" name="" id="userName" value="" placeholder="请输入用户名">
        </div>
        <!-- 找回密码页面验证码 -->
        <div id="pwd">
            <span>旧密码:</span>
            <input type="password" name="" id="oldPassword" value="" placeholder="输入旧密码">

        </div>
        <!-- 新密码 -->
        <div id="newpwd">
            <span>新密码:</span>
            <input type="password" name="" id="password" value="" placeholder="输入新密码">
        </div>

    </form>
</div>
<!-- 找回密码确认按钮 -->
<div id="sure">
    <a href="javascript:;" class="confirm">确认</a>
</div>
</body>
<script src="/plug-in/app/js/rem.js"></script>
<script src="/plug-in/app/js/jquery1.7.2.js"></script>
<script src="/plug-in/app/js/dialog.js"></script>
<script src="/plug-in/app/js/fp.js?t=201801300957"></script>
</html>
