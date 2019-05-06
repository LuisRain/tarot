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
	<link rel="stylesheet" href="static/viewer/viewer.css" type="text/css">
	<style type="text/css">
		.clear{clear: both;}
		.img-list{width: 100%;}
		.input-row{width: 100%;}
		.areatxt{width: 96%;height: 500px;border: 1px solid #eee;margin: 1% 2%;}
	</style>
	<!-- jsp文件头和头部 -->
	<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
	<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&key=ZDVBZ-C4Y3V-DDZPS-UIDQC-HY3GO-5MFIK"></script>
	<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="static/viewer/viewer.js"></script>
	<script type="text/javascript">
   $(function(){
	         var geocoder,map, marker = null;
	    	 var center = new qq.maps.LatLng("${updateorder.latitude}", "${updateorder.longitude}");
	         map = new qq.maps.Map(document.getElementById('container'),{
	             center: center,
	             zoom: 15
	         });
	         var info = new qq.maps.InfoWindow({map: map});
	         geocoder = new qq.maps.Geocoder({
	             complete : function(result){
	                 map.setCenter(result.detail.location);
	                 var marker = new qq.maps.Marker({
	                     map:map,
	                     position: result.detail.location
	                 });
	                 //添加监听事件 当标记被点击了  设置图层
	                 qq.maps.event.addListener(marker, 'click', function() {
	                     info.open();
	                     info.setContent('<div style="width:280px;height:100px;">'+
	                         result.detail.address+'</div>');
	                     info.setPosition(result.detail.location);
	                 });
	             }
	         });
	       geocoder.getAddress(center);
	    
	       $('.image').viewer();
	   
   });

	</script>
	</head> 
<body>
<div class="img-list">
		<ul style="list-style: none;padding: 0;margin: 0 1%;width: 98%;">
	   <c:forEach items="${imgList}" var="list">
           <li style="display: block;float: left;margin: 1%;width: 30%;"><img style="display: block;width: 100%;height:300px;border: 1px solid #eee;" class="image" src="${list}"  alt="订单图片"></li>
          </c:forEach>
		</ul>
		<div class="clear"></div>
	</div>
	<div class="input-row" id="container" >
		<textarea id="textarea" class="areatxt" rows="10" placeholder=""></textarea>
	</div>
	</body>
	
</html>

