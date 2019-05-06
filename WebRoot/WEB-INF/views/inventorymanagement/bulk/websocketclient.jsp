<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- jsp文件头和头部 -->
	<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
	
	</head> 
<body>
		
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			姓名：<input id="textss" type="text"/>
			发送内容：<br/><input id="text" type="text"/>
    <button onclick="send()">发送消息</button>
    <hr/>
    <button onclick="closeWebSocket()">关闭WebSocket连接</button>
    <hr/>
    <div id="message"></div>
			
	</div>
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		
		$(top.hangge());
		
		 var websocket = null;
		    //判断当前浏览器是否支持WebSocket
		    if ('WebSocket' in window) {
		        websocket = new WebSocket("ws://localhost:6060/order/websocket");;
		    }
		    else {
		        alert('当前浏览器 不支持WebSocket')
		    }

		    //连接发生错误的回调方法
		    websocket.onerror = function () {
		        setMessageInnerHTML("WebSocket连接发生错误");
		    };

		    //连接成功建立的回调方法
		    websocket.onopen = function () {
		        setMessageInnerHTML("WebSocket连接成功");
		    }

		    //接收到消息的回调方法
		    websocket.onmessage = function (event) {
		        setMessageInnerHTML(event.data);
		    }

		    //连接关闭的回调方法
		    websocket.onclose = function () {
		        setMessageInnerHTML("WebSocket连接关闭");
		    }

		    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
		    window.onbeforeunload = function () {
		        closeWebSocket();
		    }

		    //将消息显示在网页上
		    function setMessageInnerHTML(innerHTML) {
		    	if(innerHTML=="411323199105261733"){
		    	alert("收到41132319910526733信息并触发弹框");
		    	}
		        document.getElementById('message').innerHTML += innerHTML + '<br/>';
		    }

		    //关闭WebSocket连接
		    function closeWebSocket() {
		        websocket.close();
		    }

		    //发送消息
		    function send() {
		        var message =""+document.getElementById('textss').value+"说："+document.getElementById('text').value+"";
		        websocket.send(message);
		    }

		
		
		</script>
		
		
		
	</body>
</html>

