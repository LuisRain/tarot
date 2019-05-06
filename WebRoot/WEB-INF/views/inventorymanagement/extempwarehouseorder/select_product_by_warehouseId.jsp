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
			<form action="<%=basePath%>/product/findProductByWarhouseAndQuantitylistPage?warehouseId=1" method="post" id="productForm">
			<table>
				<tr>
				<td>
			
		<select id="searchcriteria" name="searchcriteria">
			 <option value="3">1-条形码</option>    
              <option value="1">2-商品名称</option>  
              <option value="2">3-商品编码</option>  
               </select>
				</td>
					<td>
						<span class="input-icon">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
						</span>
					</td>
					
				</tr>
			</table>
			<!-- 检索  -->
		
		
			<table id="table_report" class="table table-striped table-bordered table-hover">
				
				<thead>
					<tr>
						<th class="center">
						<!--  选择全部按钮-->
						</th>
						<th>商品名称</th>
						<th>商品编号</th>
						<th>商品条形码</th>
						<th>库存数量</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty productList}">
						
						<c:forEach items="${productList}" var="product" varStatus="vs">
							<tr>
					            <td>
					            <label>
					            <input type="radio" name="productId"   value="${product.id}" />
					            <span class="lbl"></span></label>
					            </td>
								<td>${product.product_name}</td>
							     <td>${product.product_num}</td>
								 <td >${product.bar_code}</td>
								  <td>${product.product_quantity}</td>
							 
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
		<script type="text/javascript">
		$(top.hangge());
		
		//检索
		function search(){
			top.jzts();
			$("#productForm").submit();
		}
		
		
		
		
		
		
	
		</script>
		
	
		
	</body>
</html>

