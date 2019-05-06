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
			<form action="product/listProducts.do" method="post" id="ProductForm">
			<c:if test="${QX.cha == 1 }">
			 <table>
				<tr>
					
					<td style="vertical-align:top;">
					<a class="btn btn-small btn-success"  onclick="toExcel();">导出到EXCEL</a>
					</td>
				</tr>
			</table> 
			</c:if> 
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="checkAll" /><span class="lbl"></span></label>
						</th>
						<th>序号</th>
						<th>商品类别</th>
						<th>HOS数量</th>
						<th>HOS金额</th>
						<th>实盘数量</th>
						<th>实盘金额</th>
						<th>盈亏数量</th>
					    <th>盈亏金额</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						
						<c:forEach items="${varList}" var="varList" varStatus="vs">
									
							<tr>
					            <td class='center' style="width: 30px;">
					           <%-- <label>
					             <input type='checkbox' name='ids'  value="${product.id}" id="${product.id}" /> 
					            <span class="lbl"></span>
					            </label>
					              <input type='text'   style="display: none;"  value="${product.cargo_space_id}" id="csid${product.id}" />
					            </td>  
					            --%>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${varList.classify_name}</td>
								<td>${varList.pro_num }</td>
								<td>${varList.hos_amount }</td>
								<td>${varList.product_quantity }</td>
								<td>${varList.price}</td>			
								<td>${varList.yksl}</td>
								<td>${varList.ykje}</td>	
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
				<c:if test="${QX.edit == 1 }">
					<a class="btn btn-small btn-success"  onclick="goImportExcelPage();">批量导入</a></c:if>					
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
		$(function(){
			$("#searchcriteria").val("${pd.searchcriteria}");
			$("#bursting").val("${pd.bursting}");
			$("#is_shelve").val("${pd.is_shelve}");
		})
	/* 	
		$("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
 */
		//检索
		function search(){
			top.jzts();
			$("#ProductForm").submit();
		}
		
		
		//导出excel
		function toExcel(){
			
			window.location.href='<%=basePath%>reportController/excel.do';
		}
		
		
	
		//批量导入商品信息
		function goImportExcelPage(){
			top.mainFrame.tabAddHandler("999777","批量导入商品信息","<%=basePath%>reportController/goImportExcelPage.do");
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		</script>
	</body>
</html>

