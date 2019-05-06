<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="utf-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
		<%-- <input type="text" id="numlen" value="${numlen }" style="display: none"> --%>
		<button  onclick="javascript:window.print()" class="noprint" id="showPrintButton" style="cursor:pointer">打印</button>
	</div>
	<% 
		Calendar cal = Calendar.getInstance();  
		try{
			String create_time=request.getAttribute("create_time").toString();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(create_time));  
		}catch (Exception e) {
			e.printStackTrace();
		}
        int d = 0;  
        if(cal.get(Calendar.DAY_OF_WEEK)==1){  
            d = -6;  
        }else{  
            d = 2-cal.get(Calendar.DAY_OF_WEEK);  
        }  
        cal.add(Calendar.DAY_OF_WEEK, d);  
        int month=cal.get(Calendar.MONTH)+1;  //当前月
        int weekOfMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);   //当前月的第几个星期天
 %> 
	<div class="print-title" id="headerInfo">
		<!-- <h4>山西中国石油中央仓商品收货单</h4> -->
		<%-- <h2>中国石油山西公司中央仓商品收货单<%=month %>月（<%=weekOfMonth %>）批次</h2> --%>
		<h2>中国石油山西公司中央仓商品收货单${batchNum }批次</h2>
		<span class="floatLeft"> 供应商：${enwarhouse.supplier_name}|${enwarhouse.supplier_num }<br/>配送地址：太原市清徐县赵家堡工业园（联坤仓储物流园）</span>
	 	<span class="floatRight">
	 		联系人：${enwarhouse.contact_person}<br/>
	 		电话：${enwarhouse.contact_person_mobile}
			<%-- <%java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
   			  java.util.Date currentTime = new java.util.Date();    
   			  String time = simpleDateFormat.format(currentTime).toString();  
   			  out.println(time);  
    		 %> --%>
	 	</span>
	</div>
	<table  class="tableTopBorder_3" id="tabContent">
	<thead>
		<tr>
			 			<th class="center">商品编码</th>
						<th class="center">商品名称</th>
						<th class="center">商品条形码</th>
						<th class="center">规格</th>
						<th class="center">商品单价</th>
						<th class="center">订货金额</th>
						<th class="center">订货数量</th>
						<th class="center">实收数量</th>
						<th class="center">实收金额</th>
						<th class="center">赠品数量</th>
						<th class="center">生产日期</th>
						<th class="center">保质期</th>
						
		</tr>
		</thead>
		<tbody>
		<c:choose>
					<c:when test="${not empty orderItemList}">
						<c:forEach items="${orderItemList}" var="o">
							<tr>
							 <td>${o.product_num}</td>
							 <td>${o.product_name}</td>
							 <td>${o.bar_code}</td>
							  <td>${o.specification}</td>
							 <td>${o.product_price}</td>
							  <td>${o.price1}</td>
							 <td><fmt:formatNumber type="number" value="${o.quantity}" maxFractionDigits="0" /></td>
							 <td><fmt:formatNumber type="number" value="${o.final_quantity}" minFractionDigits="2" maxFractionDigits="4" /></td>
							 <td>${o.price}</td>
							  <td><fmt:formatNumber type="number" value="${o.gift_quantity}" minFractionDigits="2" maxFractionDigits="4" /></td>
							  <%--<td>${o.product_group_num}</td>--%>
							  <td> ${fn:substring(o.product_time, 0, 10)}</td>
							   <td>${o.expire_days}</td>
							</tr>
						</c:forEach>
						<tr>

							<td colspan="5" id="finalAmount" class='center'><span style="color: red;font-weight:bold;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计：
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</span>
							</td>
							<td class="center"><span style="color: red;font-weight:bold;"> <%--订货金额合计--%>
								<fmt:formatNumber type="number" value="${enwarhouse.price1}" minFractionDigits="2"  maxFractionDigits="2"/>
							</span></td>
							<td class="center"><span style="color: red;font-weight:bold;"> <%--订货数量合计--%>
								<fmt:formatNumber type="number" value="${enwarhouse.quantity}" maxFractionDigits="0"/>
							</span></td>
							<td class="center"><span style="color: red;font-weight:bold;"> <%--实收数量合计--%>
							<fmt:formatNumber type="number" value="${enwarhouse.final_quantity}" maxFractionDigits="0"/>
							</span></td>
							<td class="center"><span style="color: red;font-weight:bold;"> <%--实收金额合计--%>
								<fmt:formatNumber type="number" value="${enwarhouse.price}" minFractionDigits="2"  maxFractionDigits="2"/>
							</span></td>
							<td class="center"><span style="color: red;font-weight:bold;"> <%--赠品数量合计--%>
								<fmt:formatNumber type="number" value="${enwarhouse.gift_quantity}" maxFractionDigits="0"/>
							</span></td>
							<td colspan="2" class="center" ></td>
						</tr>
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
				<td colspan="3" style="text-align: left;">仓管： </td>
				<td colspan="4"style="text-align: left;">运输： </td>
				<td colspan="4" style="text-align: left;">门店收货： </td>
				</tr>
				</tfoot>
	</table>
	</table>
		<div class="print-title" id="headerInfo">
		<span class="floatLeft">送货人：<br/>电话：<br/>车号：</span>
	 	<span class="floatRight" style="margin-right:150px">
	 		收货人：<br/>
	 		<br/>
	 		日期：
	 	</span>
	</div>
	 <div style="clear: left" >
			<span>注意事项：1.供应商送货日期要求：商品在6个月以内的效期，日期超过四份之一的日期拒收；2.在物流系统订货系统中没有及时变更信息资料的商品，中央仓直接拒收 ； 3.供应商送货超出规定时间的商品拒收。     <br/>
	  </div>
	<dir class="pageBreak"></dir>

</body>
</html>