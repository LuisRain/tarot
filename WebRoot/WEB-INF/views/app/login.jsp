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
    <link rel="stylesheet" href="/plug-in/app/css/login.css?t=201801310621">
</head>
<body>
<div class="header">
    <img src="/plug-in/app/image/login/logo.png"alt="中国石油" width="100%">
</div>
<div class="login">
    <form class="">
        <!-- 登陆页面电话号码 -->
        <div id="tel">
            <img src="/plug-in/app/image/login/mine.png" alt="">
            <input type="text" name="" id="userName" placeholder="请输入用户名">
        </div>
        <!-- 登陆页面密码 -->
        <div id="pwd">
            <img src="/plug-in/app/image/login/pw.png" alt="">
            <input type="password" name="" id="password" value="" placeholder="请输入密码">
        </div>
        <!-- 登陆页面登陆按钮-->
        <div id="login">
            <input type="button" value="登录">

        </div>


        <!-- 登陆页面注册按钮 -->
        <div id="resit">
            <a href="/app/modifyPassword">修改密码</a>
        </div>

    </form>
</div>
<p class="loginBottom">山西中石油加油站便利店订货系统</p>
</body>
<script src="/plug-in/app/js/rem.js"></script>
<script src="/plug-in/app/js/jquery1.7.2.js"></script>
<script src="/plug-in/app/js/dialog.js"></script>
<script src="/plug-in/app/js/login.js"></script>
</html>
