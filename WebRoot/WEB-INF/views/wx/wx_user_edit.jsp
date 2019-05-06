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
		<meta charset="utf-8" />
		<title></title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/font-awesome.min.css" />
		<!-- 下拉框 -->
		<link rel="stylesheet" href="static/css/chosen.css" />
		
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	var shouji = /^(((13[0-9]{1})|159)+\d{8})$/; //手机号验证
	
	//保存
	function save(){
		if($("#name").val()==""){
			$("#name").tips({
				side:3,
	            msg:'请输入姓名',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#name").focus();
			return false;
		}
			
			if($("#phone").val()==""){
				$("#phone").tips({
					side:3,
		            msg:'请输入手机号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#phone").focus();
				return false;
			}
			else if($("#phone").val().length != 11 && !shouji.test($("#phone").val())){
				$("#phone").tips({
					side:3,
		            msg:'手机号格式不正确',
		            bg:'#AE81FF',
		            time:3
		        });
				$("#phone").focus();
				return false;

			if($("#number_plate").val()==""){
				$("#number_plate").tips({
					side:3,
		            msg:'请输入车牌号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#number_plate").focus();
				return false;
			}
			}
			
			if($("#userid").val()==""){
				hasP();
			}else{
				$("#Form").submit();
				$("#zhongxin").hide();
				$("#zhongxin2").show();
			}
			
			
			//判断用户名是否存在
			function hasP(){
				var phone = $.trim($("#phone").val());
				$.ajax({
					type: "POST",
					url: '<%=basePath%>weixin/hasP.do',
			    	data: {phone:phone,tm:new Date().getTime()},
					dataType:'json',
					cache: false,
					success: function(data){
						 if("success" == data.result){
							$("#Form").submit();
							$("#zhongxin").hide();
							$("#zhongxin2").show();
						 }else{
							$("#phone").css("background-color","#D16E6C");
							setTimeout("$('#phone').val('该手机号已存在!')",500);
						 }
					}
				});
			}
			
	}
	
</script>
	</head>
<body>
	<form action="weixin/${msg}.do" name="Form" id="Form" method="post">
	<input type="hidden" name="userid" id="userid" value="${pd.userid}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">姓名:</td>
				<td><input type="text" name="name" id="name" value="${pd.name}" maxlength="32" placeholder="这里输入姓名" title="姓名"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">手机号:</td>
				<td><input type="text" name="phone" id="phone" value="${pd.phone}" maxlength="32" placeholder="这里输入手机号" title="手机号"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">车牌号:</td>
				<td><input type="text" name="number_plate" id="number_plate" value="${pd.number_plate}" maxlength="32" placeholder="这里输入车牌号"  title="车牌号"/></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save()">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
	</form>
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
	<script type="text/javascript">
		$(top.hangge());
		</script>
</body>
</html>