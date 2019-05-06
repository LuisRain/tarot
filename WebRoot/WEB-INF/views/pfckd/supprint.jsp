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
		.print-title h2,.print-title h4,.print-title h5{margin:0;padding:0;text-align:center;}
		.print-title h2,.print-title h4{font-size:25px!important;font-family: "宋体"}
		.print-title h5{font-size:16px!important;font-family: "宋体"}
		.signatureArea {
			height: 25px;
			line-height: 25px;
		}
		.signatureArea .firstSpan{
			margin-right:200px;
		}
		.floatRight {
			float: right;
		}
		.tprint span{ display:inline-block; width:24%;}
		.tab-style,.tab-style td{border:none;}
		.tab-style td{text-align:center;width: 33%;}
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
		<h4>中国石油山西销售分公司便利店付款审批单</h4><br/><br/>
		<h5>${pd.year}年${pd.month}月${pd.day}日</<h5>
	 	
	</div>
	<table  class="tableTopBorder_3" id="tabContent">
		<tbody>
			<tr>
			 <td style="width: 20%">收款单位名称</td>
			 <td style="width: 30%">${varList.supplier_name}</td>
			 <td style="width: 20%">付款方式</td>
			 <td style="width: 30%"></td>
			</tr>
			<tr>
			 <td style="width: 20%">开户银行</td>
			 <td style="width: 30%"></td>
			 <td style="width: 20%">  账    号</td>
			 <td style="width: 30%"></td>
			</tr>
			<tr>
			 <td style="width: 20% ; height: 150px;" rowspan="4"> 付款事由</td>
			 <td style="width: 80% ; height: 150px; " rowspan="4" colspan="3" ></td>
			</tr>
			<tr></tr><tr></tr><tr></tr>
			<tr>
			 <td style="width: 20%">付款金额（元、大写）</td>
			 <td style="width: 30%"></td>
			 <td style="width: 20%"> 付款金额（元、小写）</td>
			 <td style="width: 30%"></td>
			</tr>
			<tr>
			 <td style="width: 20%; height: 100px;">备     注</td>
			 <td style="width: 30%; height: 100px;"></td>
			 <td style="width: 20%; height: 100px;">备     注</td>
			 <td style="width: 30%; height: 100px;"></td>
			</tr>
		</tbody>
		
	</table>
			
	</table>
	<table cellspace="0" cellpadding="0" border="0" class="tab-style">
		<tr>
		 <td>审核：</td>
		 <td>复核:</td>
		 <td>制表：</td>
		</tr>
	</table>
	
	<dir class="pageBreak"></dir>
	
</body>
</html>
