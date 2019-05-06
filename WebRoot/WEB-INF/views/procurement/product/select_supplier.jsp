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
			<form action="<%=basePath%>supplier/supplierlist.do" method="post" id="SupplierForm">
			<table>
				<tr>
				<td>
			
			<select id="searchcriteria" name="searchcriteria">  
              <option value ="1"  >1-按供应商名称</option>  
              <option value ="2"  >2-按联系地址</option>  
              <option value="3" >3-按联系人</option>  
              <option value="4" >4-按编号</option>  
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
						<th>供应商编号</th>
						<th>供应商名称</th>
						<td>供应商联系人</td>
						<td>联系人电话</td>
						<td>地址</td>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty supplerList}">
						
						<c:forEach items="${supplerList}" var="suppler" varStatus="vs">
									
							<tr>
					            <td>
					            <label><input type="radio" name="supplierid"   value="${suppler.id}" /><span class="lbl"></span></label>
					            </td>
								<td id="suppliernum${suppler.id}">${suppler.supplier_num}</td>
								<td id="suppliername${suppler.id}">${suppler.supplier_name}</td>
								 <td id="contact_person${suppler.id}">${suppler.contact_person}</td>
								 <td id="contact_person_mobile${suppler.id}">${suppler.contact_person_mobile}</td>
								<td id="address${suppler.id}">${suppler.address}</td>
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
			$("#SupplierForm").submit();
		}
		
		
		
		
		
		
	
		</script>
		
	
		
	</body>
</html>

