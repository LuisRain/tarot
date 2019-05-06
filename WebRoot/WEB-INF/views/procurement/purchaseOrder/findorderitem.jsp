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
	<!-- jsp文件头和头部 -->
<%@ include file="../../system/admin/top.jsp"%>	
<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-skins.min.css" />
	<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/js/jQuery.print.js"></script>
		<link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
	
		<script type="text/javascript">
	 	function print(){
			window.open("<%=basePath%>purchaseOrder/print.do?order_num=${pd.order_num}", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
		}
		</script>
		<style type="text/css">
  		.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		#searchcriteria{width:202px;height:38px}

  	</style>
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">

	<div class="row-fluid">
	 
		 <div  style="float: right;">
		<!--<c:if test="${pd.checked_state==1}">
		<button style="" onclick="selectSupplier()" class=" btn-app btn-light btn-mini">
		调拨
		</button></c:if>-->
		 <button onclick="print()" class=" btn-app btn-light btn-mini">
		<i class="icon-print"></i>
		打印
		</button>
		  </div> 
	    
	<form action="exwarehouseorder/goExwareorderProductEdit.do?orderId=${pd.order_num}	" method="post" name="Form" id="Form">
		<table id="table_report" class="table table-striped table-bordered table-hover">
		  <tr>
		  		<td style="width:100px;text-align: center;"  colspan="4"><span style="font-size: 18px;">基本信息</span></td>
		  </tr>
		
		<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">订单编号:</td>
				<td>
			         ${obj.order_num}  |  ${obj.supplier_num }
				</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">下单日期:</td>
				<td>
			          ${obj.create_time}
				</td>
			</tr>
			<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">配送地址:</td>
				<td>
				<%-- <fmt:parseDate value=" ${pd.create_time}" var="orderDate" pattern="yyyy-MM-dd"></fmt:parseDate>
					<fmt:formatDate value="${orderDate}" pattern="yyyy-MM-dd"/> --%>
					
			         ${obj.deliver_address}
				</td>
			 
				 <td style="width:70px;text-align: right;padding-top: 13px;">联系人/电话:</td>
				 <td>
					${obj.manager_name}	/${obj.contact_person_mobile }
				 </Td>
				 
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>
				<td colspan="3">
			          ${obj.comment}
				</td>
				 
			</tr>
	 </table>
			<!-- 检索  -->
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				
				<thead>
					<tr>
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
						<th class="center">赠品数量</th>
						<th class="center">供货商编码</th>
						<th class="center">供货商</th>
						<th class="center">备注</th>
					</tr>
				</thead>
				<tbody>
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
							    <td class="center">${o.gift_quantity}</td>
							    <td class="center">${o.supplier_num}</td>
							     <td class="center">${o.supplier_name}</td>
								
								 <td class="center">${o.comment }</td>
								
							</tr>
						</c:forEach>
						<tr>

							<td colspan="7" id="finalAmount" class='center'><font style="color: red;font-weight:bold;">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计：
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</font>
							<td colspan="6" class="center" >
								<span style="color: red;font-weight:bold;"> 采购单总数：
								<fmt:formatNumber type="number" value="${obj.quantity}" maxFractionDigits="0"/>
								&emsp;&emsp;总价：
								<fmt:formatNumber type="number" value="${obj.order_amount}" minFractionDigits="2"
													 maxFractionDigits="2"/>
								</span>
							</td>
							<td  class="center"></td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				
				</tbody>
			</table>
			</form>
		<div class="page-header position-relative">
		<%-- <table style="width:100%;">
			<tr>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table> --%>
		</div>
	</div>
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		<script src="static/layui/layui.js" charset="utf-8"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		$(top.hangge());
		</script>
	</body>
</html>

