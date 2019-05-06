<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>  
<%@ page import="java.text.*"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>计划单</title>
    <base href="<%=basePath%>">
    <link href="static/css/print.css" rel="stylesheet" />
    <style media="print">
    @page {
      size: auto;  /* auto is the initial value */
      margin: 0mm; /* this affects the margin in the printer settings */
    }
   
</style>
	<style type="text/css">
    	.lefttd{text-align: left!important;padding-left:20px}
	
	</style>
  </head>
 <body>
	<div class="print-title">
		<h2 class="print-title-h2"><img alt="" src="<%=basePath%>/${enwarhouse.url}">计划单<a href="javascript:isprint()" class="printBtn"><b></b>打印</a></h2>
	</div>
	<table cellpadding="0" cellspacing="0" width="100%" class="table">
	<tr>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right"  >计划单号:</td>
			<td class="lefttd" colspan="5">${planorder.plan_order}</td>
		</tr>
		<tr>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right">总件数:</td>
			<td class="lefttd" >${planorder.total_number}</td>
			<td width="12%" align="right">总重量:</td>
			<td class="lefttd" >${planorder.total_weight}</td>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right">总体积:</td>
			<td class="lefttd" >${planorder.total_volume}</td>
		</tr>
		<tr>
			<td width="12%" align="right">始发地:</td><td class="lefttd" > ${planorder.originating }</td>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right">目的地:</td>
			<td class="lefttd" >${planorder.sitename}</td>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right">价格:</td>
			<td  class="lefttd" >${planorder.final_amount}</td>
		</tr>
		<tr>
			<td width="12%" align="right">司机姓名：</td>
			<td class="lefttd" >${planorder.driver_name}</td>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right">车型:</td>
			 <td class="lefttd" >${planorder.models}</td>
			 <td width="12%" style="font-size:8px;font-weight:normal;" align="right">车牌号:</td>
			 <td class="lefttd" >${planorder.driver_platenumber}</td>
		</tr>
		<tr>
			<td width="12%" align="right">创建日期：</td><td class="lefttd"  colspan="5">${planorder.create_time.substring(0,10)}</td>
		</tr>
		<tr>
			<td width="12%" style="font-size:8px;font-weight:normal;" align="right">关联订单号:</td>
			 <td class="lefttd"  colspan="5">
			 	<c:forEach items="${planOrderItem}" var="item" varStatus="vs">
					<div style="padding:10px 0 10px 0;font-weight:normal;color: #08c;">
						<a onclick="editProductNumber('${item.order_num}',1)">${item.order_num}</a>
					</div>
				</c:forEach></td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="70%">送货人签字：</td>
			<td width="30%">收货人签字：</td>
		</tr>
		<tr>
			<td colspan="2">
			<%--ck_id==4  中心门店 --%>
			<c:if test="${enwarhouse.ck_id==4}">中心门店问题反馈电话：18035121386，15234081322<b></b> <b></b></c:if>
			<c:if test="${enwarhouse.ck_id!=4}">中央仓问题反馈电话：18035121386，15234081322<b></b> <b></b></c:if>
			</td>
		</tr>
	</table>
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script type="text/javascript">
		//@ sourceURL=msgprompt.js
		  function isprint(){
			window.print();
		}

	</script>
</body>
</html>
