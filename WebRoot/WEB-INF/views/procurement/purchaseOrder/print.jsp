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
  </head>
 <body>
	<div class="btn">
		<a href="javascript:window.print()" class="printBtn"><b></b>打印</a>
	</div>
	<table cellpadding="0" cellspacing="0" class="table-no-bd">
		<tr>
			<td colspan="4">基本信息：</td>
		</tr>
		<tr>
			<td>订单编号：${obj.order_num} | ${obj.supplier_num }</td>
			<td>下单日期: ${obj.create_time}</td>
		</tr>
		<tr>
			<td>配送地址: ${obj.deliver_address}</td>
			<td >联系人/电话:
			 ${obj.manager_name}/${obj.contact_person_mobile }	
			</td>
		</tr>
		<tr>
			<td colspan="4">备注: ${obj.comment}</td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" class="table">
		<tr class="trbg">
					 <th class="center">序号</th>
						 <th class="center">商品编码</th>
						<th class="center">商品名称</th>
						<th class="center">条形码</th>
						<th class="center">计量单位</th>
						<th class="center">包装数量</th>
						<th  class="center">供货单价</th>
						<th class="center">大类</th>
						<th class="center">保质期天数</th>
						<th class="center">规格</th>
						<th class="center">采购数量</th>
						<th class="center">实收数量</th>
						<th class="center">赠品数量</th>
						<th class="center">供货商编码</th>
						<th class="center">供货商</th>
						<th class="center">备注</th>
		</tr>
		
								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty list}">
						<c:forEach items="${list}" var="o" varStatus="vs">
							<tr>
							  <td class='center' >${vs.index+1}</td>
							 <td class="center">${o.product_num}</td>
							  <td class="center">${o.product_name}</td>
							  <td class="center">${o.bar_code}</td>
							  <td class="center">${o.unit_name}</td>
							  <td class="center">${o.box_number}</td>
							  <td class="center"><fmt:formatNumber  type="number" value="${o.purchase_price}" minFractionDigits="4"  maxFractionDigits="4"  /></td>
							  <td class="center">${o.classify_name}</td>
							  <td class="center">${o.expire_days}</td>
							   <td class="center">${o.specification}</td>
							    <td class="center">${o.quantity}</td>
							    <td class="center"></td>
							     <td class="center">${o.gift_quantity}</td>
							    <td class="center">${o.supplier_num}</td>
							     <td class="center">${o.supplier_name}</td>
								
								 <td class="center">${o.comment }</td>
								
							</tr>
						</c:forEach>
										<tr>

											<td colspan="7" id="finalAmount" class='center'><span style="color: red;font-weight:bold;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计：
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</span>
											<td colspan="9" class="center" >
								<span style="color: red;font-weight:bold;"> 采购单总数：
								<fmt:formatNumber type="number" value="${obj.quantity}" maxFractionDigits="0"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总价：
								<fmt:formatNumber type="number" value="${obj.order_amount}" minFractionDigits="2"
												  maxFractionDigits="2"/>
								</span>
								</td>
										</tr>
					</c:when>
									<c:otherwise>
										<tr>
											<td colspan="10" class="center">没有相关数据</td>
										</tr>
									</c:otherwise>
								</c:choose>
	</table>
	<div class="txt">
		<div class="left">送货人签字：</div>
		<div class="right">收货人签字：</div>
		<div class="clear"></div>
	</div>
	<div class="txt">
		问题反馈电话：18035121386，15234081322<b></b> <b></b>
		<span>中央仓问题反馈电话：18035121386，15234081322（8:30—20:00）</span>
		<br/>
	</div>
</body>
</html>
