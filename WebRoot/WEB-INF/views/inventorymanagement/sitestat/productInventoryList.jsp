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
	<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
	<!-- page specific plugin styles -->
	<!-- 下拉框-->

	<!-- ace styles -->
	<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-skins.min.css" />
	<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/js/jQuery.print.js"></script>
	
	</head> 
		<script type="text/javascript">
		function dy(){
			  $("#table_report").print({
				  globalStyles: true
			  });
		}
		</script>
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="productinventroy/getProductInventroyinfo.do?productid=${pd.productid}&ckzt=${pd.ckzt}&warehouse_id=${pd.warehouse_id}" method="post" id="ProductForm">
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
					
						<th style="width: 35px">序号</th>
						<th style="width: 70px">入出库日期</th>
	                    <th style="width: 35px">批次号</th>
	                    <th style="width: 200px;font-size: 1px">商品名称</th>
	                    <th style="width: 200px">站点</th>
	                    <th style="width: 35px">类型</th>
	                    <th style="width: 35px">数量</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="product" varStatus="vs">
							<tr>
					     <td>${vs.index+1}</td>
						<td>${product.create_time }</td>
	                    <td>${product.group_num }</td>
	                    <td>${product.product_name }</td>
	                    <td>${product.short_name }</td>
	                    <td>
	                    <c:if test="${product.type==1}">
	                    <img alt="入库"   height="10px" width="10px" src="<%=basePath%>static/images/rk.gif">
	                    </c:if>
	                      <c:if test="${product.type==2}">
	                    <img alt="出库"  height="10px" width="10px" src="<%=basePath%>static/images/chuku.gif">
	                    </c:if>
	                    </td>
	                     <td>${product.quantity }</td>
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
			</table>
			
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					
				
				</td>
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
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		var type="${pd.type}";
		if(type!=null){
			$("#type").val(type);
		}
		var warehouseid="${pd.warehouseid}";
		if(warehouseid!=null){
			$("#warehouseid").val(warehouseid);
		}
		
		  $("#searchcriteria").val("${pd.searchcriteria}");
		$(top.hangge());
		//检索
		function search(){
			top.jzts();
			$("#ProductForm").submit();
		}

		//导出excel
		function toExcel(){
			$("#ProductForm").attr("action", "productinventroy/excel.do");
			$("#ProductForm").submit();
			$("#ProductForm").attr("action", "productinventroy/getProductInventroyinfo.do");
		}
		
		</script>
	</body>
</html>

