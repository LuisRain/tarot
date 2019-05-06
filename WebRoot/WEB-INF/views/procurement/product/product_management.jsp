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
			<form action="product/getManagementproduct.do.do" method="post" id="ProductForm">
			<c:if test="${QX.cha == 1 }">
			<table>
				<tr><td>
			<select id="searchcriteria" name="searchcriteria">  
			<option value="1" >1-按商品条形码</option>
              <option value ="2"  >2-按商品名称</option>  
              <option value ="3"  >3-按商品编码</option>  
               </select>  
				</td>
				<td  style="vertical-align:top;">
				<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
				</td>
					<td style="vertical-align:top;">
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
					<td style="vertical-align:top;">
										
					</td>
				</tr>
			</table>
			</c:if>
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>商品条形码</th>
						<th>商品名称</th>
						<th>销售数量</th>
						<th>销售日期</th>
						<th>商品售价</th>
						<th>应收金额</th>
					    <th>实收金额</th>
					    <th>收银机号</th>
					    <th>小票号码</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty productList}">
						<c:forEach items="${productList}" var="product" varStatus="vs">
							<tr>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${product.bar_code}</td>
								<td>${product.product_name }</td>
								<td>${product.quantity }</td>
								<td>${product.sell_time }</td>
								<td>${product.sale_price}</td>			
								<td>${product.ammount}</td>
								<td>${product.final_ammount}</td>
								<td>${product.cash_register}</td>
								<td>${product.small_ticket}</td>
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
				<!--<td style="vertical-align:top;">
					<a class="btn btn-small btn-success" onclick="siMenu('product9999','proudct999','新增商品','<%=basePath%>product/goAddProduct.do')">新增</a>						
				 <a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a> 
				</td>-->	
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
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript" src="static/js/myjs/head.js" ></script>
		<script type="text/javascript">
		$(top.hangge());
		
		function search(){
			top.jzts();
			$("#ProductForm").submit();
		}
		
	
		</script>
	</body>
</html>

