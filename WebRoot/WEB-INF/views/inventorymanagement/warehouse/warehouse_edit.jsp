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
	var dianhua =/^0\d{2,3}-?\d{7,8}$/;//电话号码验证
	
	//保存
	function save(){
			if($("#warehouse_name").val()==""){
			$("#warehouse_name").tips({
				side:3,
	            msg:'请输入仓库名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#warehouse_name").focus();
			return false;
		}
		if($("#warehouse_tel").val()==""){
			$("#warehouse_tel").tips({
				side:3,
	            msg:'请输入仓库电话',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#warehouse_tel").focus();
			return false;
		}else if($("#warehouse_tel").val().length != 11 && !dianhua.test($("#warehouse_tel").val())){
			$("#warehouse_tel").tips({
				side:3,
	            msg:'手机号格式不正确',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#SUPPLIER_TEL").focus();
			return false;
		}
		
		
		
		if($("#warehouse_address").val()==""){
			$("#warehouse_address").tips({
				side:3,
	            msg:'请输入层仓库地址',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#warehouse_address").focus();
			return false;
		}
		if($("#warehouse_number").val()==""){
			$("#warehouse_number").tips({
				side:3,
	            msg:'请输入仓库编号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#warehouse_number").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="warehouse/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="id" id="id" value="${pd.id}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
		     <tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">仓库编号:</td>
				<td>
			<c:if test="${pd.warehouse_number ==null }">
				<input type="text" disabled="disabled"    value="自动生成" maxlength="32"  title="自动生成"/>
			</c:if>		
				<c:if test="${pd.warehouse_number !=null }">
				<input type="text"  disabled="disabled" name="warehouse_number" id="warehouse_number" value="${pd.warehouse_number}" maxlength="32"  title="自动生成"/>
				</c:if>		
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">仓库名称:</td>
				<td><input type="text" name="warehouse_name" id="warehouse_name" value="${pd.warehouse_name}" maxlength="32" placeholder="仓库名称" title="仓库名称"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">仓库电话:</td>
				<td><input type="text" name="warehouse_tel" id="warehouse_tel" value="${pd.warehouse_tel}" maxlength="32" placeholder="仓库电话" title="仓库电话"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">仓库地址:</td>
				<td><input type="text" name="warehouse_address" id="warehouse_address" value="${pd.warehouse_address}" maxlength="32" placeholder="仓库地址" title="仓库地址"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">仓库类型:</td>
				<td>
				<select name="warehouse_type" id="warehouse_type">
				<option value="1">一级仓库</option>
				<option value="2">二级仓库</option>
				</select>
				</td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
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
		$(function() {
		  $("#warehouse_type").val("${pd.warehouse_type}");
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>