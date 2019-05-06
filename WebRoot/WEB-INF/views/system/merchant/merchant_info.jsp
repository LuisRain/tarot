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
	 
	var category=['郑州市','新乡市','洛阳市','安阳市','焦作市','许昌市','平顶山市','漯河市','开封市','濮阳市','鹤壁市','南阳市','三门峡市','驻马店市','商丘市','信阳市','周口市','其它','中联','中亚','亚立'];
	 
 
	function goSelling (id){
		   
		
		   location.href="<%=basePath%>sellingorder/sellingorderlist.do?searchcriteria= 1 &keyword="+id;
		
		
	}
 
	
</script>
	</head>
<body>

		<table id="table_report" class="table table-striped table-bordered table-hover">
		  <tr><td style="width:100px;text-align: right" >客户的基本信息:</td></tr>
		
		<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">编号|名称</td>
				<td>
			       ${pd.merchant_num}|${pd.merchant_name}
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系地址:</td>
				<td>${pd.city},${pd.address}</td>
				
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">销售价格:</td>
				<td> </td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">网址:</td>
				<td>${pd.url}</td>
			</tr>
			 <tr>
			<td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
			   
			   <td>${pd.remarks}</td>
			 
			 
			 </tr>
			  <Tr><Td style="width:100px;text-align: right">联系人信息:</Td></Tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人名称:</td>
				<td>${pd.contact_person}</td>
		 
	 
				<td style="width:70px;text-align: right;padding-top: 13px;">电话:</td>
				<td>${pd.moblie}</td>
			</tr>
			  <Tr><Td>统计信息:</Td></Tr>
		  	<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">累计订单:</td>
				<td><a onclick="goSelling(${pd.id}) ">${pd.orderCount}(个)</a></td>
			</tr>
				<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">订单交易额:</td>
				<td>${pd.orderSum}元(按已审核销售订单计算)</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">实际交易额:</td>
				<td>${pd.finalAmount}(按实际出库计算)</td>
			</tr>
				<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">已付款:</td>
				<td>${pd.amount}(元)</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">未付款:</td>
				<td>${pd.shiji}(元)</td>
			</tr>
			 
			 <tr>
			 
			  <Td style="width:100px;text-align: right">最后修改:</Td>
 
			   </tr>
			   
			   <tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">修改人:</td>
				<td>${pd.USERNAME}</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">最后一次修改时间:</td>
				<td>${pd.update_time}</td>
			</tr>
			 
		</table>
	 
		
		 
 
	
	
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