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
			window.open("<%=basePath%>sellingorder/print.do?id=${pd.id}", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
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
	 
		<!-- <div  style="float: right;">
		<c:if test="${pd.checked_state==1}">
		<button style="" onclick="selectSupplier()" class=" btn-app btn-light btn-mini">
		调拨
		</button></c:if>
		 <button onclick="print()" class=" btn-app btn-light btn-mini">
		<i class="icon-print"></i>
		打印
		</button>
		  </div> -->
	    
	
		<table id="table_report" class="table table-striped table-bordered table-hover">
		  <tr>
		  		<td style="width:100px;text-align: center;"  colspan="4"><span style="font-size: 18px;">基本信息</span></td>
		  </tr>
		
		<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">订单编号:</td>
				<td>
			         ${obj.order_num}
				</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">下单日期:</td>
				<td>
			          ${obj.order_date}
				</td>
			</tr>
			<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">配送地址:</td>
				<td>
				<%-- <fmt:parseDate value=" ${pd.create_time}" var="orderDate" pattern="yyyy-MM-dd"></fmt:parseDate>
					<fmt:formatDate value="${orderDate}" pattern="yyyy-MM-dd"/> --%>
					
			         ${obj.deliver_address}
				</td>
			 
				 <td style="width:70px;text-align: right;padding-top: 13px;">联系人:</td>
				 <td>
					${obj.manager_name}	
				 </Td>
				 
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>
				<td colspan="3">
			          ${obj.comment}
				</td>
				 
			</tr>
	 </table>
	 <input type ="hidden" value="${pd.order_num }" name="order_num" id = "order_num">
			<!-- 检索  -->
			<!-- 检索  -->
		<form action="" method="post" name="Form" id="Form">
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						 <th class="center">序号</th>
						<th class="center">商品条形码</th>
						<th class="center">商品名称</th>
						<th class="center">规格</th>
						<th  class="center">订购单价</th>
						<th class="center">订购数量</th>
						<th class="center">赠品数量</th>
						<th class="center">备注</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="o" varStatus="vs">
							<tr>
							 <td class='center' >${vs.index+1}<input type = "hidden" class="ids" value="${o.id}" name="id"></td>
							  <td class="center">${o.bar_code}</td>
							  <td class="center">${o.product_name}</td>
							   <td class="center">${o.specification}</td>
							     <td class="center">
							     	<c:if test="${o.gift_quantity!=0.0}">
								 		0
								 	</c:if>
								 	<c:if test="${o.gift_quantity==0.0}">
								 		<fmt:formatNumber  type="number" value="${o.sale_price}" minFractionDigits="4"  maxFractionDigits="4"  />
								 	</c:if>
							     </td>
							     <td class="center">
							     	<c:if test="${o.gift_quantity!=0.0}">
								 		0
								 	</c:if>
								 	<c:if test="${o.gift_quantity==0.0}">
								 		<input type="text" name="quantity" id="quantity" value="${o.quantity}" maxlength="10" placeholder="请输入数量" title="数量"/>
								 	</c:if>
								 	
								 </td>
								 <td class="center">
								 		${o.gift_quantity}
								 </td>
								 <td class="center">${o.comment}</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				<tr>
				<c:if test="${pd.state eq 1 }">
				
					<td style="text-align: center;" colspan="10">
						<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
						<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				</c:if>
			</tr>
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
		function save(){
			var obj = $('#Form').serializeArray();
			var url = '<%=basePath%>sellingorder/editorderitem.do';
			var order_num = $("#order_num").val();
			$.ajax({
				url:url,
				data:{data:JSON.stringify(obj)},
				success:function(data){
					if(data == "true"){
						alert("保存成功");
						location.href='<%=basePath%>sellingorder/findorderitem.do?order_num='+order_num;
					}
				}
			});
		//	$("#Form").submit();
		}
		</script>
	</body>
</html>

