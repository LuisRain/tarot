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
 <script type="text/javascript">
 
 </script>
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="<%=basePath%>dataCenter/dataCenterPIlistPage.do" method="post" id="merchantForm">
			<c:if test="${QX.cha == 1 }">
			<table>
				<tr>
					<Td>
			   商品条形码: <input autocomplete="off" id="nav-search-input" type="text" name="bar_code" value="${pd.bar_code}" placeholder="这里输入商品条形码" />
			 </Td>
			 	<Td>
			    最近一次的批次号:${pd.group_num}
			 </Td>
			  <td>
			   <button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
			  </td>
				</tr>
			</table>
			</c:if>
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>商品名称</th>
						<th>商品条形码</th>
						<th>商品规格</th>
						<th>商品单位</th>
						 <th>商品单价</th>
						 <th>入库</th>
						 <th>出库</th>
						 <th>本期结算</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty enDataList}">
						<c:forEach items="${enDataList}" var="n" varStatus="vs">
							<tr>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${n.product_name}</td>
								<td>${n.bar_code}</td>
								 <td>${n.specification}</td>
								 <Td>${n.unit_name}</Td>
 								<td>
								 ${n.product_price}
								</td>
								<td><fmt:formatNumber type="number" value="${n.xquantity}" maxFractionDigits="0"/></td>
								 <td><fmt:formatNumber type="number" value="${n.xquantity}" maxFractionDigits="0"/></td>
								  <Td><fmt:formatNumber type="number" value="${n.y}" maxFractionDigits="0"/></Td>
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
			$("#merchantForm").submit();
		}
		
		     $("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
		
 
		</script>
 
	</body>
</html>

