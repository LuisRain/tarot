<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
<script type="text/javascript" src="static/js/twomsswms_print.js"></script>
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
		
	</style>
	<style type="text/css" media="print">
		.noprint { display:none;}
</style>
  </head>
 <body>
 	<table>
 	<div class="btn">
		<%-- <input type="text" id="numlen" value="${numlen }" style="display: none"> --%>
		<button  onclick="javascript:window.print()" class="noprint" id="showPrintButton" style="cursor:pointer">打印</button>
	</div>
	
	<div class="print-title" id="headerInfo">
		<h4>中石油山西昆仑好客便利店</h4>
		<h2>中央仓配送商品退货单</h2>
		<span class="floatLeft"> 门店：  ${sell.merchant_id}| ${sell.merchant_name}<br/>门店地址：${sell.address}</span>
	 	<span class="floatRight">
	 		联系电话：${sell.manager_name}|${sell.manager_tel}<br/>
	 		打印时间：
			<%java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
   			  java.util.Date currentTime = new java.util.Date();    
   			  String time = simpleDateFormat.format(currentTime).toString();  
   			  out.println(time);  
    		 %>
	 	</span>
	</div>
	<table  class="tableTopBorder_3" id="tabContent">
	<thead>
		<tr>
			 			<th class="center">商品编码</th>
						<th class="center">商品名称</th>
						<th class="center">商品条形码</th>
						<th class="center">单位</th>
						<th class="center">退货数量</th>
						<th class="center">退货金额</th>
						<th class="center">实退数量</th>
						
		</tr>
		</thead>
		<tbody>
		<c:choose>
					<c:when test="${not empty sellitem}">
						<c:forEach items="${sellitem}" var="o">
							<tr>
							 <td>${o.product_num}</td>
							 <td>${o.product_name}</td>
							 <td>${o.bar_code}</td>
							 <td>${o.unit_name}</td>
							 <td><fmt:formatNumber type="number" value="${o.quantity}" maxFractionDigits="0" /></td>
							 <td><fmt:formatNumber type="number" value="${o.quantity*o.sale_price}" minFractionDigits="2" maxFractionDigits="4" /></Td>
							<td></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				</tbody>
				<tfoot>
				<tr  class="tprint" style="clear: right;border-style: none">
				<td colspan="2" style="text-align: left;">打印人：${pdd.name}</td>
				<td colspan="1" style="text-align: left;">仓管： </td>
				<td colspan="2"style="text-align: left;">运输： </td>
				<td colspan="3" style="text-align: left;">门店收货： </td>
				</tr>
				</tfoot>
	</table>
	</table>
	  <div class="signatureArea">
			<span class="firstSpan">单据说明：<br/></span>
			<span>1、第一联一中央仓、第二联一门店、第三一非油结算、第四联一供货商；<br/>
			2、便利店在收到此配送验收单确认无误后需在加管系统及时入库；若发现商品短缺，按零售价格以现金方式现场赔付，如未现场赔付，要记录清楚并双方签字确认。中央仓问题反馈电话：18035121386，15234081322（8:30—20:00）</span>
			<br/>
			<span class="floatRight">门店实际到货时间：<br/>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日 </span>
		
		<div class="tprint">
			<span>送货人签字：</span>
			<span>收货人签字：</span>
		</div>
	  </div>
	<dir class="pageBreak"></dir>

</body>
</html>