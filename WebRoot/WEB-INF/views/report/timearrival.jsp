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

	<style type="text/css">
		.new-row-fluid{margin:15px 0;border:1px solid #f5f5f5;}
		.fl{width:15%;float:left;background:#f5f5f5;text-align:center;padding: 50px 0;}
		.fl1{width:15%;float:left;background:#f5f5f5;text-align:center;padding: 134px 0;}
		.fl2{width:15%;float:left;background:#f5f5f5;text-align:center;padding: 172px 0;}
		.fr{width:84%;float:right;padding-left:1%;margin-top:27px;}
		.fr1{width:84%;float:right;padding-left:1%;}
		.fr2{width:84%;float:right;padding-left:1%;}
		.new-fl{float:left;width:100%;}
		.new-fl1{float:left;width:100%;}
		.new-fl2{float:left;width:100%;}
		.btn-style{vertical-align:top;}
		.div1{margin-left:20px}
		.div2{margin-left: 20px;margin-top: 20px}
	</style>
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
	
			<div class="row-fluid new-row-fluid">
				<div class="fl1">
					压期到货导出
				</div>
				<div class="fr1">
					<input id="groupnum" name="groupnum" style="width: 200px" type="text" value="" placeholder="输入格式如：20180601" >
					<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="exprot();" title="导出到EXCEL">
									<i id="nav-search-icon" class="icon-download-alt"></i> </button>
					</div>
				</div>
  </div>
	
</div><!--/#page-content-->

		
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
		<script type="text/javascript" src="static/js/myjs/head.js" ></script>
		<script src="static/layer/layer.js"></script>
		<script type="text/javascript">
		$(top.hangge());
			function exprot(){
				var groupnum=$("#groupnum").val();
				if(groupnum!=null && groupnum!=""){
					window.location.href='<%=basePath%>reportController/timearrivalexcel.do?groupnum='+groupnum;
					layer.alert("导出成功",{icon:1});
				}else{
					layer.alert('请输入批次', {icon: 2});   
				}
				
			}
		</script>
	</body>
</html>

