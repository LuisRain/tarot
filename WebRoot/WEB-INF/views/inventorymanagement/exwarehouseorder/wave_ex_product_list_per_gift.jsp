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
	<link href="static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="/static/css/font-awesome.min.css" />
	<link rel="stylesheet" href="/static/css/ace.min.css" />
	<link rel="stylesheet" href="/static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="/static/css/ace-skins.min.css" />
	
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
	 <button style="float: right;" onclick="dy()" class=" btn-app btn-light btn-mini">
		<i class="icon-print"></i>
		打印
		</button>
			<form action="exwarehouseorder/doWaveExwareorder.do?DATA_IDS=${str} " method="post" name="Form" id="Form">
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr><th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th>商品条码</th>
						<th>商品名称</th>
						<th>商品货位</th>
						<th>分量合计</th>
						<th>单位</th>
						<!-- <th>商户订购总量详单</th> 
						<th>拆分后整箱数详单</th>-->
						<th>分拣系统分量数详单</th>						
					</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="product" varStatus="vs">
							<tr>
								<td class='center' style="width: 30px;">${vs.count}</td>
								<td>${product.barCode }</td>
								<td>${product.productName }</td>
								<td>${product.cargoSpace.zone }区${product.cargoSpace.storey}层${product.cargoSpace.storeyNum}</td>
								<td>${product.totalQuantity }</td>
								<td>${product.unit}</td>
								<%-- <td>
									<c:forEach items="${product.merchants}" var="merchant" varStatus="vsM">
										${vsM.count}.${merchant.merchantName }(${merchant.productCount })；<br/>
									</c:forEach>
								</td> --%>
								<%-- <td>
									<c:forEach items="${product.merchants}" var="merchant" varStatus="vsM">
										<c:if test="${merchant.splitTotalCount !=0 }"></c:if>
											${vsM.count}.${merchant.merchantName }(${merchant.splitTotalCount })；
												<input size="6" id="splitTotalCount${merchant.texoiId}" type="text"
														value="<fmt:formatNumber type="number" value="${merchant.splitTotalCount}" 
														maxFractionDigits="0"/>" id="input${merchant.id}">
											<input style="display: none" type="text" id="texoiId${merchant.texoiId}" VALUE="${merchant.texoiId}">
											<input style="display: none" type="text" name="mtexoiId" VALUE="${merchant.texoiId}">
											<br/>
									</c:forEach>
								</td> --%>
								<td>
									<c:forEach items="${product.merchants}" var="merchant" varStatus="vsM">
										${vsM.count}.${merchant.merchantName }(${merchant.splitPerCount })；<br/>
									</c:forEach>
								</td>
									
								<%-- <c:when test="${not empty product.merchants}"></c:when> --%>
							</tr>
						</c:forEach>
						<!-- <tr id="noprint">
									<td colspan="4"><a class="btn btn-mini btn-primary"  style="text-align: center;" onclick="editProductNum()">确定</a></td>
								</tr> -->
					</c:when>
					
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				</tbody>
				
			</table>		
			</form>
	</div>
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
	    <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
	    <script type="text/javascript" src="static/js/jQuery.print.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		$(top.hangge());
		function dy(){
			  $("#Form").print({
				  globalStyles: true,
				  noPrintSelector: "#noprint"
			  });			
		}
		//修改商品数量
		  function editProductNumber(id1){
			var url='<%=basePath%>enwarehouseorder/goEnwareorderProductEdit.do?type=1&orderId='+id1;
				top.mainFrame.tabAddHandler("392060852","查看入库单信息",url);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		 } 
		</script>
	</body>
</html>

