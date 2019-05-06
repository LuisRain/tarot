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
  	<style type="text/css">
  	.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		#searchcriteria{width:202px;height:38px}
  		.layui-table th{text-align:center}
</style>
</head>
<body>
<form action="<%=basePath%>Sitestatistics/findSiteMerchantlitPage.do" method="post" id="SellingOrderForm" >
				 <div class="layui-form layui-form-pane"  style="width:100%">
				 <div class="layui-form-item"  style="float: left;" > 
					    <%-- <div class="fl">
					    	<select id="searchcriteria" name="searchcriteria" style="height:40px">   
				               <option <c:if test="${pd.searchcriteria==1 }">selected="selected"</c:if> value ="1">1-条码</option>  
				               <option <c:if test="${pd.searchcriteria==2 }">selected="selected"</c:if> value="2">2-站点名称</option>
				            </select>
					    </div> --%>
					    <div class="fl">
					     <input type="text"   id="short_name" name="short_name" value="${pd.short_name}" lay-verify="required" placeholder="请输入站点名称" class="layui-input">
					     <input type="text"   id="bar_code" name="bar_code" value="${pd.bar_code}" lay-verify="required" placeholder="请输入条码" class="layui-input">
					    </div>
				      <div class="fl">
				      
						  <button  onclick="search();" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="搜索">搜索</button>
						<%-- 
						<c:if test="${QX.QX1 == 1 }">
							<button  type="button"  onclick="xsdExcelAll();" class="layui-btn layui-btn-radius layui-btn-small" title="导出">
							<i class="layui-icon" style="font-size: 20px;">&#xe601;</i> </button>
						</c:if> --%>
				      </div>
				    </div>
				  </div>
						<div class="layui-form" >
  						<table class="layui-table"  id="diyige">
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">商品名称</th>
									<th class="center">商品编码</th>
								    <th class="center">站点</th>
									<th class="center">库存</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty sitelist}">
										<c:forEach items="${sitelist}" var="var" varStatus="vs">
											<tr>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td class="left">${var.product_name}</td>
												<td class="left">
													${var.bar_code}
												</td>
												<td class="center">${var.short_name}</td>
												<td class="center">${var.inventory}</td>
												<td>
													<a class="btn btn-mini btn-info" onclick="editProductNumber(${var.product_id},${var.merchant_id })" ><i  class="icon-search"></i></a>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<c:if test="${pd.checked_state eq  1}">
												<td colspan="100" class="center">没有新出库单</td>
											</c:if>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>

	</div>
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
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/layui/layui.js" charset="utf-8"></script>
		<!-- 引入 -->
		<script src="static/js/bootstrap.min.js"></script>
	
		
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		 $(top.hangge());
		layui.use('form', function(){
				  var $ = layui.jquery, form = layui.form();
				});
		//检索
		function search(){
			 top.jzts();
			$("#SellingOrderForm").submit(); 
		}
		function editProductNumber(product_id,merchant_id){
		//siMenu('product10001','product10001','站点库存操作记录','')
			var url='<%=basePath%>Sitestatistics/getProductInventroyinfo.do?product_id='+product_id+'&merchant_id='+merchant_id;
			top.mainFrame.tabAddHandler("product10001","站点库存操作记录",url);
		}
		</script>

</body>
</html>

