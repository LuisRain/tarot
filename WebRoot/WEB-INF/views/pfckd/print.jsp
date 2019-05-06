<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>打印</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="static/css/printtab.css"> 
<script type="text/javascript" src="static/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="static/js/msswms_print.js"></script>
	  <style type="text/css">
		  .clear{clear: both;}
		  body,td{font: 12px/1.5 "Microsoft yahei",SimHei normal;}
		  .table{border-top: 2px solid #eee!important;border-left: 2px solid #eee!important;width: 100%;}
		  .table tr.trbg{background: #f9f9f9;}
		  .table td{border-bottom: 2px solid #eee!important;border-right: 2px solid #eee!important;height: 30px;line-height:30px;}
		  .txtLeft{text-align: left;width: 35%;padding-left: 15px;}
		  .txtRight{text-align: right;width: 15%;padding-right: 15px;}
		  .table{margin-top: 10px;margin-bottom: 10px;}
		  .table td{text-align: center;}
		  .txt{width:80%;font-size: 12px;margin: 0 auto;padding: 10px 0;}
		  .left,.right{width: 40%;float: left;text-align: left;}
		  .right{float: right;}
		  .txt b{display: inline-block;margin: 0 10px;}
		  .btn{float: right;margin: 10px 0;}
		  .btn a{color: #333;text-decoration: none;display: block;border: 1px solid #eee;padding: 6px 10px;}
		  .printBtn b{background: url(static/img/print.png) no-repeat -2px -4px;width: 20px;height: 20px;display: block;float: left;margin-right: 3px;}
		  .btn a:hover{background: #f9f9f9;}
		  .print-title{margin: 25px auto;}
		  .print-title h2,.print-title h4{margin:0;padding:0;text-align:center;}
		  .print-title h2{font-size:20px!important;}
		  .print-title h4{font-size:16px!important;}
	  </style>
	<style type="text/css" media="print">
		.noprint { display:none;}
</style>
  </head>
 <body>
 	<table>
	<div class="btn">
		<input type="text" id="numlen" value="${numlen }" style="display: none">
		<button  onclick="javascript:window.print()" class="noprint" id="showPrintButton" style="cursor:pointer">打印</button>
	</div>
	
	<div class="print-title" id="headerInfo">
		<h4>${pd.month}统采供应商结算表</h4>
		<span class="floatLeft"> 单位名称：中国石油山西销售公司</span>
	 	
	</div>
	<table  class="tableTopBorder_3" id="tabContent">
	<thead>
		<tr>
			 			<th class="center" rowspan="3">序号</th>
			 			<th class="center" rowspan="3">单位名称</th>
						<th class="center" colspan="2">应付账款</th>
						<th class="center" colspan="2">销售及库存情况</th>
						<th class="center" rowspan="2">${pd.month}申请付款金额</th>
						<th class="center" rowspan="3">备注</th>
		</tr>
		<tr>
			 			<th class="center">含税金额</th>
			 			<th class="center">不含税金额</th>
						<th class="center">期末库存商品金额</th>
						<th class="center">已销售商品金额</th>
		</tr>
		<tr>
			 			<th class="center"><span style="color: red">(1)</span></th>
			 			<th class="center"><span style="color: red">(2)=(1)/税率</span></th>
						<th class="center"><span style="color: red">(3)</span></th>
						<th class="center"><span style="color: red">(4)=(1)-(3)</span></th>
						<th class="center"><span style="color: red">(5)≦(4)</span></th>
		</tr>
		</thead>
		<tbody>
						<c:forEach items="${varList}" var="list" varStatus="vs">
							
							<tr>
							 <td>${vs.index+1}</td>
							 <td>${list.supplier_name}</td>
							 <td>${list.TotalPrice}</td>
							 <td></td>
							 <td></td>
							 <td></td>
							 <td></Td>
							<td>${list.comment}</td>
							</tr>
						</c:forEach>
				</tbody>
				
	</table>
			
	</table>
	<table cellspace="0" cellpadding="0" border="0" class="tab-style">
		<tr>
		 <td>非油处：</td>
		 <td>复核:</td>
		 <td>制表：</td>
		</tr>
	</table>
	
	<dir class="pageBreak"></dir>
	
</body>
</html>
