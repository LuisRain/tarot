﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			<form action="<%=basePath%>dataCenter/exDataCenterlistPage.do" method="post" id="merchantForm">
			<c:if test="${QX.cha == 1 }">
			<table>
				<tr>
				<td>
			<select id="ivt_state" name="ivt_state" >  
              <option value ="0">全部</option>  
              <option value="1">将要出库</option>  
              <option value="5">已经出库</option>  
               </select>  
				</td>
					<td>
					   商品条形码:
						<span class="input-icon">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="bar_code" value="${pd.bar_code}" placeholder="这里输入商品条形码" />
						</span>
                    
					</td>
					
			   <td>
			   订单时间:<input class="span10 date-picker" 
			    id="StartDate" name="StartDate" value="${pd.StartDate}"
			     type="text" data-date-format="yyyy-mm-dd"
			      readonly="readonly" style="width: 110px;"
			       placeholder="开始时间" title="开始时间">
			     
			       <input class="span10 date-picker" 
			    id="EndDate" name="EndDate" value="${pd.EndDate}"
			     type="text" data-date-format="yyyy-mm-dd"
			      readonly="readonly" style="width: 110px;"
			       placeholder="结束时间" title="结束时间">
			   </td>	
			   <td>	
				 商户名称: <input autocomplete="off" id="nav-search-input" type="text" name="merchant_name" value="${pd.merchant_name}" placeholder="这里输入商户名称" />
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
						<th>订单日期</th>
						<th>出库日期</th>
						<th>出库单编号</th>
						<th>供应商</th>
						<th>条形码</th>
						<th>商品名称</th>
						<th>类别</th>
						<th>成本价</th>
						<th>出售价</th>
						<th>业务员</th>
						<th>订购数</th>
						<th>实际数</th>
						<th>单位</th>
						 <th>金额(元)</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty enDataList}">
						<c:forEach items="${enDataList}" var="n" varStatus="vs">
							<tr>
					            
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${n.create_time}</td>
								<td>${n.order_date}</td>
								 <td>${n.order_num}</td>
								 <Td>${n.merchant_name}</Td>
								<td>${n.bar_code}</td>
								<td>${n.product_name}</td>
								<td>${n.classify_name}</td>	
							 	<td>${n.purchase_price}</td>	
							 		<td>${n.sale_price}</td>	
							 			<td>${n.creator}</td>	
							 				 
							 				<td><fmt:formatNumber type="number" value="${n.quantity}" maxFractionDigits="0"/></td>	
							 					<td><fmt:formatNumber type="number" value="${n.final_quantity}" maxFractionDigits="0"/></td>	
							 						<td>${n.unit_name}</td>	
 								<td>
								 ${n.sumSalePrice}
								</td>
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
	 
		  $(function(){
			  $(top.hangge());
				$('.date-picker').datepicker();
				if(${pd.ivt_state !=null}){
					$("#ivt_state").val("${pd.ivt_state}");
				}
		  });

		
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

