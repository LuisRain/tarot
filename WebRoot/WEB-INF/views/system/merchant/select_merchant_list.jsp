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
			<form action="<%=basePath%>merchant/selectlist.do" method="post" id="merchantForm">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" >  
              <option value ="1">1-按商户名称</option>  
              <option value ="2">2-按商户地区</option>    
              <option value="3">3-按联系人</option>  
              <option value="4">4-按编号</option>  
               </select>  
               
            
				</td>
					<td>
						<span class="input-icon">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
				
							
						</span>
						  <select id="city" name="city"  placeholder="这里选择地区" >  
					 <option value="0">请选择地区</option>
                <c:forEach items="${area}" var="a">
                 <option value="${a.id}">${a.area_name}</option>
                </c:forEach>
                     </select> 
                     <button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
				 	   
              
                
          
				</tr>
			</table>
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						
						</th>
						<th>序号</th>
						<th>商户编号</th>
						<th>名称</th>
						<th>地区</th>
						<th>地址</th>
						<th>联系人</th>
						<th>联系人手机</th>
						<th>联系电话</th>
						
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty merchantList}">
						
						<c:forEach items="${merchantList}" var="merchant" varStatus="vs">
									
							<tr>
					            <td class='center' style="width: 30px;">      
					            <label><input type="radio" name="merchantid"   value="${merchant.id}" /><span class="lbl"></span></label>
					            </td>
								<td class='center' style="width: 30px;">${merchant.id}</td>
								<td id="merchant_num${merchant.id}" >${merchant.merchant_num}</td>
								<td id="merchant_name${merchant.id}">${merchant.merchant_name}</td>
								
								 <td>${merchant.cityName}</td>
								<td id="address${merchant.id}">${merchant.address}</td>
								<td id="contact_person${merchant.id}">${merchant.contact_person}</td>
								<td id="mobile${merchant.id}">${merchant.mobile}</td>	
								<td>${merchant.phone}</td>
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
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<!-- 引入 -->
		
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		 
		
		  $("#searchcriteria").val("${pd.searchcriteria}");
	    $("#city").val("${pd.city}");
		$(top.hangge());
		
		//检索
		function search(){
			top.jzts();
			$("#merchantForm").submit();
		}

		</script>
 
	</body>
</html>

