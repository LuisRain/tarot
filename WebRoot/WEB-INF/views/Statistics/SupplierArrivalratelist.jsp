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
	<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
	
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="supplierarrivalrate/findlistPage.do" method="post" id="supplierForm">
		
			<table>
				<tr>
				<td>
				<input class="span10 date-picker"  name="startdate" id="startdate"  value="${pd.startdate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px;" placeholder="开始日期" />
				<input class="span10 date-picker" id="enddate" name="enddate"  value="${pd.enddate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; " placeholder="结束日期" />
				</td>
					<td style="vertical-align:top;">
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
					<td style="vertical-align:top;">
						<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
					</td>
				</tr>
			</table>
			
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>供货商编码</th>
						<th>供货商名称</th>
						<th>订购数</th>
						<th>订购价</th>
						<th>实际到货数</th>
						<th>实际价格</th>
						<th>数量到货率</th>
					    <th>价格到货率</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${arrivaList }" var="arrivaList" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${arrivaList.supplier_num }</td>
							<td>${arrivaList.supplier_name }</td>
							<td>${arrivaList.sumquantity }</td>
							<td>${arrivaList.quantity_price }</td>
							<td>${arrivaList.sumfinal_quantity }</td>
							<td>${arrivaList.final_quantity_price }</td>
							<td>${arrivaList.quantity_dhl }%</td>
							<td>${arrivaList.price_dhl }%</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
		</div>
		</form>
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
		
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript" src="static/js/myjs/head.js" ></script>
		<script type="text/javascript">
		$(top.hangge());
		$(function(){
			$('.date-picker').datepicker();
		});
		//检索
		function search(){
			top.jzts();
			$("#supplierForm").submit();
		}
		//导出excel
		function toExcel(){
			var startdate = $("#startdate").val();
			var enddate = $("#enddate").val();
			window.location.href='<%=basePath%>supplierarrivalrate/excel.do?startdate='+startdate+'&enddate='+enddate;
		}
		</script>
	</body>
</html>

