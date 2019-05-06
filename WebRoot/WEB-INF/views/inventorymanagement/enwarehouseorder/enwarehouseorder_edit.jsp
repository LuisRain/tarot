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
			if($("#checked_state").val()==""){
			$("#checked_state").tips({
				side:3,
	            msg:'请输入入库状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#checked_state").focus();
			return false;
		}
		if($("#order_date").val()==""){
			$("#order_date").tips({
				side:3,
	            msg:'请输入入库时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#order_date").focus();
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
	            msg:'请输入联系方式',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#manager_tel").focus();
			return false;
		}
		if($("#comment").val()==""){
			$("#comment").tips({
				side:3,
	            msg:'请输入备注',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#comment").focus();
			return false;
		}
		if($("#final_amount").val()==""){
			$("#final_amount").tips({
				side:3,
	            msg:'请输入入库总额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#final_amount").focus();
			return false;
		}
		if($("#total_svolume").val()==""){
			$("#total_svolume").tips({
				side:3,
	            msg:'请输入 总体积',
	            bg:'#AE81FF',
	            time:2
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
		if($("#paid_amount").val()==""){
			$("#PAID_AMOUNT").tips({
				side:3,
	            msg:'请输入已付款额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#PAID_AMOUNT").focus();
			return false;
		}
		if($("#is_temporary").val()==""){
			$("#is_temporary").tips({
				side:3,
	            msg:'请输入是否是临时入库单',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#is_temporary").focus();
			return false;
		}
		if($("#is_order_print").val()==""){
			$("#is_order_print").tips({
				side:3,
	            msg:'请输入订单打印状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#is_order_print").focus();
			return false;
		}
		if($("#ivt_state").val()==""){
			$("#ivt_state").tips({
				side:3,
	            msg:'请输入状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ivt_state").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="enwarehouseorder/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="id" id="id" value="${pd.id}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">入库状态:</td>
				<td>
				<select name="checked_state" >
				<option value="1" <c:if test="${pd.checked_state ==1}">selected="selected"</c:if> >入库-采购订单</option>
				<option <c:if test="${pd.checked_state ==2}">selected="selected"</c:if>  value="2">2-新入库-销售退货</option>
				<option <c:if test="${pd.checked_state ==3}">selected="selected"</c:if> value="3">3-已入库-采购订单</option>
				<option <c:if test="${pd.checked_state ==4}">selected="selected"</c:if> value="4">4-已入库-销售退货</option>
				</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">入库时间:</td>
				<td>
				<input class="span10 date-picker" name="order_date" id="order_date" value="${pd.order_date}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="入库时间"/>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人:</td>
				<td><input type="text" name="manager_name" id="manager_name" value="${pd.manager_name}" maxlength="32" placeholder="这里输入联系人" title="联系人"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系方式:</td>
				<td><input type="text" name="manager_tel" id="manager_tel" value="${pd.manager_tel}" maxlength="32" placeholder="这里输入联系方式" title="联系方式"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
				<td><input type="text" name="comment" id="comment" value="${pd.comment}" maxlength="32" placeholder="这里输入备注" title="备注"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">入库总额:</td>
				<td><input type="text" name="final_amount" id="final_amount" value="${pd.final_amount}" maxlength="32" placeholder="这里输入入库总额" title="入库总额"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;"> 总体积:</td>
				<td><input type="text" name="total_svolume" id="total_svolume" value="${pd.total_svolume}" maxlength="32" placeholder="这里输入 总体积" title=" 总体积"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">总重量:</td>
				<td><input type="text" name="total_weight" id="total_weight" value="${pd.total_weight}" maxlength="32" placeholder="这里输入总重量" title="总重量"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">已付款额:</td>
				<td><input type="text" name="paid_amount" id="paid_amount" value="${pd.paid_amount}" maxlength="32" placeholder="这里输入已付款额" title="已付款额"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">是否是临时入库单:</td>
				<td>
				<select name="is_temporary" id="is_temporary">
				<option  <c:if test="${pd.is_temporary ==1}">selected="selected"</c:if> value="1">是</option>
				<option <c:if test="${pd.is_temporary ==2}">selected="selected"</c:if>  value="2">否</option>
				</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">订单打印状态:</td>
				<td>
				<select name="is_order_print" id="is_order_print">
				<option  <c:if test="${pd.is_order_print ==1}">selected="selected"</c:if> value="1">是</option>
				<option <c:if test="${pd.is_order_print ==2}">selected="selected"</c:if>  value="2">否</option>
				</select>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">状态:</td>
				<td>
				<select name="ivt_state" id="ivt_state">
				<option  <c:if test="${pd.ivt_state ==1}">selected="selected"</c:if> value="1">未入库</option>
				<option <c:if test="${pd.ivt_state ==2}">selected="selected"</c:if>  value="2">已入库</option>
				<option <c:if test="${pd.ivt_state ==3}">selected="selected"</c:if>  value="3">取消入库</option>
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
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>