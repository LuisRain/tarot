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
		<form action="weixin/updateorderlist.do" method="post" name="wxOrderForm" id="wxOrderForm">
			<!-- 检索  -->
				<table>
				<tr>
					<td>
			  <select id="searchcriteria" name="searchcriteria">  
              <option value="1">1-姓名</option>  
              <option value="2">2-车牌</option>  
              <option value="3">3-手机号</option>  
               </select>
				</td>
				<td style="vertical-align:top;">
				<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词">
				</td>
				<td style="vertical-align:top;">
				<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
				</td>
				<td style="vertical-align:top;">
					<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
					</td>
				</tr>
			</table>
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>id</th>
						<th>姓名</th>
						<th>车牌</th>
						<th>手机号</th>
						<th>坐标</th>
						<th>坐标</th>
						<th>日期</th>
						<th>门店编码</th>
					
						<th class="center">操作</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="a" varStatus="vs">
						<tr>
						<td class='center' style="width: 30px;">${vs.index+1}</td>
						<td>${a.id}</td>
						<td>${a.name}</td>			
						<td>${a.number_plate}</td>			
						<td>${a.phone}</td>				
						<td>${a.latitude}</td>
						<td>${a.longitude}</td>	
						<td>${a.create_time}</td>
						<td>${a.site}</td>
							
								<td>
								 <a class="btn btn-mini btn-info" 
								 onclick="siMenu('wxorder4561','wxorder4561','查看返单','<%=basePath%>weixin/getupdateorder.do?id=${a.id}')" 
								  ><i  class="icon-search"></i>查看</a>
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
				<td style="vertical-align:top;">

				</td>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
		</div>
		</form>
	</div>
  </div>
</div>
</div>
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/myjs/head.js" ></script>
		<script type="text/javascript">
		 $(function(){	
				
			  $("#searchcriteria").val("${pd.searchcriteria}");
			
			 });
		$(top.hangge());
		
		
		
		//检索
		function search(){
			top.jzts();
			$("#wxOrderForm").submit();
		}
		
		//导出excel
		function toExcel(){
			var keyword = $("#nav-search-input").val();
			var searchcriteria = $("#searchcriteria").val();
            var keyworda=encodeURI(encodeURI(keyword));
			window.location.href='<%=basePath%>weixin/orderExcel.do';
		}
		
		</script>
	</body>
</html>

