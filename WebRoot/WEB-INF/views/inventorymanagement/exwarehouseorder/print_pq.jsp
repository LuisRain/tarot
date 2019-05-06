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
<title>打印1</title>
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
		
		
	</style>
<style type="text/css" media="print">
		.noprint { display:none;}
</style>
</head>
<body >
	<div class="btn">
		<%-- <input type="text" id="numlen" value="${numlen }" style="display: none"> --%>
		<button onclick="javascript:window.print()" class="noprint"  style="cursor:pointer"><b></b>打印</button>
		<input type="hidden" id="numlen" value="1">
	</div>
	<div class="print-title" id="headerInfo1">
		<h2>中央仓配送商品验收单</h2>
		<span class="floatLeft"> 门店：  ${enwarhouse.merchant_id}| ${enwarhouse.merchant_name}<br/>门店地址：${enwarhouse.address}</span>
	 	<span class="floatRight">
	 		联系电话：${enwarhouse.manager_name}|${enwarhouse.manager_tel}<br/>
	 		打印时间：
			<%java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
   			  java.util.Date currentTime = new java.util.Date();    
   			  String time = simpleDateFormat.format(currentTime).toString();  
   			  out.println(time);  
    		 %>
	 	</span>
	</div>
	<%-- <table id="beforetable" cellpadding="0" cellspacing="0" width="100%">
		 <tr class="trbg">
			<td class="txtLeft" colspan="1">基本信息：</td>
			<td class="txtLeft" >${enwarhouse.merchant_id}</td>
		</tr> 
		<tr>
			<td class="txtRight">出库单编号：</td>
			<td class="txtLeft">${enwarhouse.order_num}</td>
			<td colspan="6" width="70%" style="font-size:12px;font-weight:normal;">便利店：
				${enwarhouse.merchant_id}| ${enwarhouse.merchant_name}</td>
			<!-- <td></td> -->
			<td colspan="4" width="30%">联系电话：${enwarhouse.manager_tel}</td>
			<!-- <td></td> -->
			<td class="txtRight">便利店联系人：</td>
			<td class="txtLeft">${enwarhouse.manager_name}</td>
		</tr>
		<tr class="trbg">
			<td class="txtRight">订购日期：</td>
			<td class="txtLeft">  ${enwarhouse.create_time}</td>
			<td class="txtRight">出库日期：</td>
			<td class="txtLeft">
			 <c:if test="${enwarhouse.ivt_state eq 5}">
				    ${enwarhouse.order_date}
				 </c:if>
			</td>
		</tr>
		<tr>
			<td colspan="6" width="70%" style="font-size:12px;font-weight:normal;">配送地址：${enwarhouse.address}</td>
			<!-- <td class="txtLeft"> </td> -->
			<td  colspan="4" width="30%">备注： ${enwarhouse.comment}</td>
			<!-- <td class="txtLeft">
		
			</td> -->
		</tr>
		<tr class="trbg">
			
			<td class="txtRight">备注：</td>
			<td class="txtLeft">
		 ${enwarhouse.comment}
			</td>
		</tr>
	</table> --%>
	<!-- <table id="tt" border="1" cellpadding="0" cellspacing="0" bordercolor="#000000" style="border-collapse:collapse;" width="100%"> -->
	<table  class="tableTopBorder_3" id="tabContent1">
		<thead>
		<tr>
			<th class="center">商品条形码</th>
			<th class="center">商品名称</th>
			<th class="center">规格</th>
			<th class="center">订购单价</th>
			<th class="center">订购数量</th>
			<th class="center">出库数量</th>
			<th class="center">出库总价</th>
			<th class="center">分拣整箱数</th>
			<th class="center">分拣散货数</th>
			<th class="center">备注</th>
		</tr>
		</thead>
		<tbody>
		<c:choose>
			<c:when test="${not empty orderItemList}">
				<c:forEach items="${orderItemList}" var="o" varStatus="vs">
					<tr id="tr${vs.count+1}">
						<td>${o.bar_code}</td>
						<td>${o.product_name}</td>
						<td>${o.specification}</td>
						<td ><fmt:formatNumber
							type="number" value="${o.sale_price}" minFractionDigits="2" maxFractionDigits="4" /></td>
						<td> <fmt:formatNumber
							type="number" value="${o.quantity}" maxFractionDigits="0" /></td>
						<c:if test="${type eq  1}">
							<td class="center"><c:if test="${enwarhouse.ivt_state eq 1}">
					   <fmt:formatNumber
							type="number" value=" ${o.final_quantity}" maxFractionDigits="0" />
						</c:if> <c:if test="${enwarhouse.ivt_state  ne    1 }">
						<fmt:formatNumber
							type="number" value=" ${o.final_quantity}" maxFractionDigits="0" />
						</c:if></td>
						</c:if>
						<Td><fmt:formatNumber
							type="number" value="${o.price}" minFractionDigits="2" maxFractionDigits="4" /></Td>
						<td class="center"><c:if test="${not empty o.total_count}">
								<fmt:formatNumber type="number" value="${o.total_count}"
									maxFractionDigits="0" />(箱)
											 </c:if> <c:if test="${empty o.total_count}">
											 	--
											 </c:if></td>
						<td class="center"><c:if test="${not empty o.per_count}">
								<fmt:formatNumber type="number" value="${o.per_count}"
									maxFractionDigits="0" />(${o.unit_name})
											 </c:if> <c:if test="${empty o.per_count}">
											 	--
											 </c:if></td>
						<td>${o.comment}</td>
					</tr>
				</c:forEach>
				<tr>
			<td colspan="4" id="finalAmount" class='center'><font
				style="color: red;font-weight:bold;">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计：
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</font></td>
			<td class="center"><font style="color: red;font-weight:bold;">
					<!-- 订购总数: --> <fmt:formatNumber type="number"
						value="${sumOrder.quantity}" maxFractionDigits="0" />
			</font></td>
			<td class="center"><font style="color: red;font-weight:bold;">
					<!-- 出库总数:  -->
					<fmt:formatNumber type="number" value="${sumOrder.final_quantity}"
						maxFractionDigits="0" />
			</font></td>
			<td class="center"><font style="color: red;font-weight:bold;">
					<!-- 出库单总额: -->
					<fmt:formatNumber type="number" value="${enwarhouse.final_amount}"
						minFractionDigits="2" maxFractionDigits="4" />
			</font></td>
			<td class="center"><font style="color: red;font-weight:bold;">
					<!-- 整量总数:  -->
					<fmt:formatNumber type="number" value="${sumOrder.total_count}"
						maxFractionDigits="0" />箱
			</font></td>
			<td class="center"><font style="color: red;font-weight:bold;">
					<!-- 散货总数:  -->
					<fmt:formatNumber type="number" value="${sumOrder.per_count}"
						maxFractionDigits="0" />
			</font></td>
			<td class="center"></td></tr>
			</c:when>
			<c:otherwise>
				<tr class="main_info">
					<td colspan="10" class="center">没有相关数据</td>
				</tr>
			</c:otherwise>
		</c:choose>
		</tbody>
		<tfoot>
				<%-- <tr  class="tprint" style="clear: right;border-style: none">
				<td colspan="2" style="text-align: left;">打印人：${pdd.name}</td>
				<td colspan="1" style="text-align: left;">仓管： </td>
				<td colspan="2"style="text-align: left;">运输： </td>
				<td colspan="3" style="text-align: left;">门店收货： </td>
				</tr> --%>
		</tfoot>
	</table>
	<div class="signatureArea">
		<div class="tprint">
			<div>
			<span>送货人签字：</span>
			<span style="margin-left: 60%">收货人签字：</span>
			</div>
		</div>
		<span>中央仓问题反馈电话：18035121386，15234081322<b></b> <b></b> </span>
	</div>
	<dir class="pageBreak"></dir>
	<!-- <table id="aftertable" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td colspan="6" width="70%">送货人签字：</td>
			<td colspan="4" width="30%">收货人签字：</td>
		</tr>
		<tr>
			
		</tr>
	</table> -->
	<!-- <div class="txt">
		<div class="left">送货人签字：</div>
		<div class="right">收货人签字：</div>
		<div class="clear"></div>
	</div>
	<div class="txt">
		
	</div> -->
</body>
<script type="text/javascript">
		init();
		function init(){
			var tt = document.getElementById("tt");
			var rows = tt.rows;
			var level = rows.length;
			/* alert("rows.length:"+rows.length); */
			var pageNum = 0;
			if(level>15){
				/* pageNum = level%15; */
				if(level>0){
					/* alert("-----:");
					alert("tr15:"+document.getElementById("tr15")); */
					document.getElementById("tr15").style.pageBreakAfter = "always";
					if(level>1){
						document.getElementById("tr30").style.pageBreakAfter = "always";
					}
				}
				
			}
		}
	</script>
</html>
