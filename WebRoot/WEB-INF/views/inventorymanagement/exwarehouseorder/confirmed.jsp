<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../../system/admin/top.jsp"%>
  <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
<style>
.td-label label {
	display: block;
	margin: 0 10px;
	float: left;
}

.lbl {
	font-size: 12px;
	vertical-align: middle;
}
</style>

</head>
<body>
	<div class="container-fluid" id="main-container">
		<div id="page-content" class="clearfix">
			<div class="row-fluid">
				<div class="row-fluid">
				
						<table id="table_report"
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th class="center"><label><input type="checkbox"
											id="zcheckbox" /><span class="lbl"></span></label></th>
									<th class="center">序号</th>
									<th class="center">批次号</th>
									<th class="center">出库单编号</th>
									    <th class="center">门店全称</th>
									<th class="center">门店简称</th>
									<th class="center">门店地址</th>
									<th class="center">总体积</th>
									<th class="center">总重量</th>
								 	<th class="center">总金额</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty varList}">
										<c:forEach items="${varList}" var="var" varStatus="vs">
											<tr>
												<td class='center' style="width: 50px;">
													<label>
															<input type='checkbox' name='ids' id="cks${var.order_num}" 
																onclick="ckek('cks${var.order_num}',${var.total_weight},${var.total_svolume},'${var.total_count}','${var.type}')" 
															value="${var.id}" />
													 	
														<span class="lbl"></span></label> 
														<label style="display: none"><input name='states' value="${var.ivt_state}" /><span class="lbl"></span>
													</label>
												</td>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td class="center">${var.group_num} <input type="hidden" id="group_num" value="${var.group_num}"></td>
												<td class="center"> ${var.order_num} </td>
												<td class="center">${var.merchant_name}</td>
												<td class="center">${var.short_name}</td>
												<td class="center">${var.address}</td>
												 <td class="center">${var.total_svolume}</td>
												<td class="center">${var.total_weight}</td> 
													<td class="center">${var.final_amount}</td>
											</tr>

										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<c:if test="${pd.ivt_state eq  1}">
												<td colspan="100" class="center">没有新出库单</td>
											</c:if>
											<c:if test="${pd.ivt_state eq  2}">
												<td colspan="100" class="center">没有待分拣的出库单</td>
											</c:if>
											<c:if test="${pd.ivt_state eq  3}">
												<td colspan="100" class="center">请等待分拣系统分拣完成再进行操作</td>
											</c:if>
											<!-- <td colspan="100" class="center" >没有相关数据</td> -->
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;text-align: center;">
									<a class="btn btn-mini btn-success" onclick="javascript:qrfh();">
									确认符合</a>
									</td>
								</tr>
							</table>
						</div>
		
				</div>
			</div>
			<!--/row-->
		</div>
		<!--/#page-content-->
	</div>
	<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse"> <i class="icon-double-angle-up icon-only"></i></a>
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>
	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script src="static/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript">
	layui.use(['layer', 'form'], function(){
			  var layer = layui.layer ,form = layui.form; });
		function qrfh(){
			var Mask=layer.load(1, {shade: [0.3,'#fff'] });
			var group_num=$("#group_num").val();
			if(group_num!=null&&group_num!="undefined"){
				layer.close(Mask); 
				var url="exwarehouseorder/qrfh.do?group_num="+group_num;
				$.post(url,function(data){
					alert(data);
					location.reload();
				}) 
			}else{
				alert("暂无单子需要确认！");
				layer.close(Mask); 
			}
			
			
		}
	</script>
</body>
</html>

