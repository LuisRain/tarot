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
			<form action="weixin/getSmsListPage.do" method="post" name="wxSmsForm" id="wxSmsForm">
			<!-- 检索  -->
				<table>
				<tr>
					<td>
			  <select id="searchcriteria" name="searchcriteria">  
              <option value="1">1-手机号</option>  
              <option value="2">2-回复内容</option>  
               </select>
				</td>
				<td>
						<span class="input-icon">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}"  placeholder="这里输入关键词">
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
						</span>
					</td>
				</tr>
			</table>
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
					    <th>序号</th>
						<th>手机号</th>
						<th>回复内容</th>
						<th>回复时间</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="a" varStatus="vs">
							<tr>
						<td class='center' style="width: 30px;">${vs.index+1}</td>
						<td>${a.phone}</td>
						<td>${a.content}</td>	
						<td>${a.create_time}</td>								
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
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<script type="text/javascript">
		 $(function(){	
				
			  $("#searchcriteria").val("${pd.searchcriteria}");
			
			 });
		$(top.hangge());
		</script>
	</body>
</html>

