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
		body,td,th{font: 12px/1.5 "Microsoft yahei",SimHei normal;font-family: "宋体"}
		.table{border-top: 1px solid #000000!important;border-left: 1px solid #000000!important;width: 100%;}
		.table tr.trbg{background: #f9f9f9;}
		.table th{border-bottom: 1px solid #000000!important;border-right: 1px solid #000000!important;height: 30px;line-height:30px;}
		.table td{border-bottom: 1px solid #000000!important;border-right: 1px solid #000000!important;height: 30px;line-height:30px;}
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
		.print-title h2{font-size:25px!important;font-family: "宋体"}
		.print-title h4{font-size:20px!important;font-family: "宋体"}
		.signatureArea {height: 25px;line-height: 25px;}
		.signatureArea .firstSpan{margin-right:200px;}
		.floatRight {float: right;}
		.tprint span{ display:inline-block; width:24%;}
		.tab-style,.tab-style td{border:none;}
		.tab-style td{text-align:center;width: 33%;}
		.tab-bd-none,.tab-bd-none td{border:none;}
		.new-tab-w{width:40%;float:right;}
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
		<h4>对账函</h4><br/>
	 	<table border="0" class="tab-bd-none">
	 		<tr>
	 			<td colspan="5" style="text-align: left;">收货方：中国石油天然气股份有限公司山西销售分公司</td>
	 			<td colspan="5" style="text-align: left;">配送方式：统采统配  统采直配</td>
	 		</tr>
	 		<tr>
	 			<td colspan="5" style="text-align: left;">供应商名称：${supinfo.supplier_name }</td>
	 			<td colspan="5" style="text-align: left;">供应商联系方式：${supinfo.contact_person_mobile }</td>
	 		</tr>
	 		<tr>
	 			<td colspan="5" style="text-align: left;">供应商性质：统采供应商</td>
	 			<td colspan="5" style="text-align: left;">供应商对帐邮箱：${supinfo.supplier_email}</td>
	 		</tr>
	 		<tr>
	 			<td colspan="5" style="text-align: left;">供应商编码：${supinfo.supplier_num}</td>
	 			<td colspan="5" style="text-align: left;">供应商联系人姓名：${supinfo.contact_person }</td>
	 		</tr>
	 		<tr>
	 			<td colspan="5" style="text-align: left;">1、截至${year}年${month}月,双方发生业务如下：</td>
	 			<td colspan="5" style="text-align: left;">单位（元）</td>
	 		</tr>
	 	</table>
	</div>
	<table  class="tableTopBorder_3" id="tabContent">
	<thead>
		<tr>
			 			<th class="center" colspan="2">已送货未结算商品总额（含税）</th>
			 			<th class="center" colspan="2">已送货未结算商品总额（不含税）</th>
						<th class="center" colspan="2">其中：已开发票（含税）</th>
						<th class="center" colspan="1">其中：未开发票 （含税）</th>
						<th class="center" colspan="1">未销售商品的金额（含税）</th>
						<th class="center" colspan="1">不含税库存</th>
						<th class="center" colspan="1">可结算金额</th>
		</tr>
		<tr>
			 			<th class="center" colspan="2">${supinfo.TotalPrice }</th>
			 			<th class="center" colspan="2"></th>
						<th class="center" colspan="2">${supinfo.TotalPrice }</th>
						<th class="center" colspan="1"></th>
						<th class="center" colspan="1"></th>
						<th class="center" colspan="1"></th>
						<th class="center" colspan="1"></th>
		</tr>
		<tr>
			 			<th class="center" colspan="10" style="text-align: left;">2、按期次汇总的对帐明细：单位（元）</th>
		</tr>
		<tr>
			 			<th class="center">送货批次</th>
			 			<th class="center">送货时间</th>
						<th class="center">送货地点</th>
						<th class="center">送货数量</th>
						<th class="center">送货金额(含税)</th>
						<th class="center">送货金额(不含税)</th>
						<th class="center">已开票金额(不含税)</th>
						<th class="center">未开票金额(不含税)</th>
						<th class="center"></th>
						<th class="center">${month}月付款</th>
		</tr>
		</thead>
		<tbody>
				<c:forEach items="${varList}" var="list" varStatus="vs">
					<c:if test="${list.TotalPrice != 0.0 }">
						<tr>
							<td class="center">易货嘀${list.group_num}期</td>
				 			<td class="center">${list.create_time }</td>
							<td class="center">易货嘀仓库</td>
							<td class="center">${list.final_quantity }</td>
							<td class="center">${list.TotalPrice }</td>
							<td class="center"></td>
							<td class="center"></td>
							<td class="center"></td>
							<td class="center"></td>
							<td class="center">${list.paid_amount }</td>
						</tr>
					</c:if>
				</c:forEach>
				<tr>
						<td class="center">合计</td>
			 			<td class="center"></td>
						<td class="center"></td>
						<td class="center">${supinfo.final_quantity }</td>
						<td class="center">${supinfo.TotalPrice }</td>
						<td class="center"></td>
						<td class="center"></td>
						<td class="center"></td>
						<td class="center"></td>
						<td class="center">${supinfo.paid_amount }</td>
					</tr>
		</tbody>
				
	</table>
			
	</table>
	<table cellspace="0" cellpadding="0" border="0" class="tab-style new-tab-w">
		<tr>
		 <td>财务专用章：</td>
		</tr>
		<tr>
			 <td>日期:</td>
		</tr>
	</table>
	
	<dir class="pageBreak"></dir>
	
</body>
</html>
