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
	
	
	//保存
	function save(){
		var number=/^(:?(:?\d+.\d+)|(:?\d+))$/;//只能输入任意数字
		var dianhua =/^0\d{2,3}-?\d{7,8}$/;//电话号码验证
		if($("#deliver_date").val()==""){
			$("#deliver_date").tips({
				side:3,
	            msg:'请输入送货时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#deliver_date").focus();
			return false;
		}
		if($("#DELIVERSTYLE").val()==""){
			$("#DELIVERSTYLE").tips({
				side:3,
	            msg:'请输入送货方式',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DELIVERSTYLE").focus();
			return false;
		}
		if($("#deliver_address").val()==""){
			$("#deliver_address").tips({
				side:3,
	            msg:'请输入送货地址',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#deliver_address").focus();
			return false;
		}
		if($("#manager_name").val()==""){
			$("#manager_name").tips({
				side:3,
	            msg:'请输入联系人',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#manager_name").focus();
			return false;
		}
		if($("#manager_tel").val()==""){
			$("#manager_tel").tips({
				side:3,
	            msg:'请输入联系人电话',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#manager_tel").focus();
			return false;
		}
		else if($("#manager_tel").val().length != 11 && !dianhua.test($("#manager_tel").val())){
			$("#manager_tel").tips({
				side:3,
	            msg:'电话号格式不正确',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#manager_tel").focus();
			return false;
		}
		if($("#COMMENT").val()==""){
			$("#COMMENT").tips({
				side:3,
	            msg:'请输入订单备注',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#COMMENT").focus();
			return false;
		}
		if($("#order_amount").val()==""){
			$("#order_amount").tips({
				side:3,
	            msg:'请输入订单总金额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#order_amount").focus();
			return false;
		}
		else if($("#order_amount").val().length != 11 && !number.test($("#order_amount").val())){
			$("#order_amount").tips({
				side:3,
	            msg:'订单总金额只能填写数字',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#order_amount").focus();
			return false;
		}
		if($("#total_svolume").val()==""){
			$("#total_svolume").tips({
				side:3,
	            msg:'请输入总体积',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#total_svolume").focus();
			return false;
		}
		else if($("#total_svolume").val().length != 11 && !number.test($("#total_svolume").val())){
			$("#total_svolume").tips({
				side:3,
	            msg:'总体积只能填写数字',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#total_svolume").focus();
			return false;
		}
		if($("#total_weight").val()==""){
			$("#total_weight").tips({
				side:3,
	            msg:'请输入总重量',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#total_weight").focus();
			return false;
		}
		else if($("#total_weight").val().length != 11 && !number.test($("#total_weight").val())){
			$("#total_weight").tips({
				side:3,
	            msg:'总总量只能填写数字',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#total_svolume").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="purchaseOrder/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="id" id="id" value="${pd.id}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
		<c:if test="${pd.orderDate !=null}">
		<tr>
			<td style="width:70px;text-align: right;padding-top: 13px;">采购日期:</td>
			<td>
			<input type="text"  value="${pd.orderDate}" disabled="disabled"  maxlength="32"/></td>
			</tr>
		</c:if>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">送货时间:</td>
				<td>
				<input class="span10 date-picker"  name="deliver_date" id="deliver_date"  value="${pd.deliverDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px;" placeholder="送货时间" title="送货时间"/>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">送货方式:</td>
				<td>
				<select name="deliver_style" id="DELIVERSTYLE">
				<option value="0">送货上门</option>
				<option value="1">上门自去</option>
				</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">供应商:</td>
				<td>
				<select name="supplier_id" id="supplierid">
				<c:forEach items="${supplerList}" var="list">
				<option value="${list.id}">${list.supplier_name}</option>
				</c:forEach>
				</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">送货地址:</td>
				<td><input type="text" name="deliver_address" id="deliver_address" value="${pd.deliverAddress}" maxlength="32" placeholder="这里输入送货地址" title="送货地址"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人:</td>
				<td><input type="text" name="manager_name" id="manager_name" value="${pd.managerName}" maxlength="32" placeholder="这里输入联系人" title="联系人"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人电话:</td>
				<td><input type="text" name="manager_tel" id="manager_tel" value="${pd.managerTel}" maxlength="32" placeholder="这里输入联系人电话" title="联系人电话"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">订单备注:</td>
				<td><input type="text" name="comment" id="comment" value="${pd.comment}" maxlength="32" placeholder="这里输入订单备注" title="订单备注"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">订单总金额:</td>
				<td><input type="text" name="order_amount" id="order_amount" value="${pd.orderAmount}" maxlength="32" placeholder="这里输入订单总金额" title="订单总金额"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">总体积:</td>
				<td><input type="text" name="total_svolume" id="total_svolume" value="${pd.totalSvolume}" maxlength="32" placeholder="这里输入总体积" title="总体积"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">总重量:</td>
				<td><input type="text" name="total_weight" id="total_weight" value="${pd.totalWeight}" maxlength="32" placeholder="这里输入总重量" title="总重量"/></td>
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
			$("#DELIVERSTYLE").val("${pd.deliverStyle}");
			$("#supplierid").val("${pd.supplier.id}");
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>