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
			<form action="bulk/listtwo.do" method="post" id="bulkForm">
			<c:if test="${QX.cha == 1 }"><table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loadSecond()">  
              <option value ="1"  >1-分拣未完成商品信息</option>  
              <option value ="2"  >2-分拣已完成商品信息</option>  
              <option value="3" >3-与商品关联的便利店信息</option>  
              <option value="4" >4-与便利店关联的商品信息</option> 
              <option value="5" >5-便利店对应储位</option> 
              <option value="6" >6-储位对应便利店</option>
               </select>
               <select id="laneway" name="laneway">
               <option value ="0"  >请选择巷道</option>  
              <option value ="1"  >第一巷道</option>  
              <option value ="2"  >第二巷道</option>  
              <option value="3" >第三巷道</option>  
              <option value="4" >第四巷道</option> 
              <option value="5" >第五巷道</option> 
              <option value="6" >第六巷道</option>
              <option value="7" >第七巷道</option> 
              <option value="8" >第八巷道</option>
               </select>  
				</td>
				<td  id="kid" style="vertical-align:top;">
				<input autocomplete="off" id="nav-search-input" type="text" id="keyword" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
				</td>
					<td style="vertical-align:top;">
							<input class="span10 date-picker"  name="startDate" id="startDate"  value="${pd.startDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; " placeholder="开始日期" title="批次创建时间"/>
							<input class="span10 date-picker" id="endDate" name="endDate"  value="${pd.endDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; " placeholder="结束日期" title="批次创建时间"/>
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
					<!-- <td style="vertical-align:top;">
					<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
					</td> -->
					<td style="vertical-align:top;">
									<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出分拣系统门店信息">
										<i id="nav-search-icon" class="icon-download-alt"></i>
									</a>
								</td>
				</tr>
			</table>
			</c:if>
			</form>
			<!-- 检索 结果显示 -->
			<table id="table_report1" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<!-- <th class="center">
						 选择全部按钮
						<label><input type="checkbox" id="checkAll" /><span class="lbl"></span></label>
						</th> -->
						<th>序号</th>
						<th>商品条形码</th>
						<th>商品名称</th>
						<th>商品单位</th>
						<th>商品数量</th>
						<th>分拣状态</th>
						<th>所属便利店</th>
						<th>对应储位</th>
						<th>扫描时间</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty pList}">
						<c:forEach items="${pList}" var="product" varStatus="vs">
							<tr>
					           <%--  <td class='center' style="width: 30px;">
					            <label>
					            <input type='checkbox' name='ids'  value="${product.id}" id="${product.id}" />
					            <span class="lbl"></span>
					            </label> 
					              <input type='text'   style="display: none;"  value="${product.cargo_space_id}" id="csid${product.id}" />
					            </td>  --%>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${product.barcode}</a></td>
								<td>${product.goodsname }</td>
								<td>${product.goodssize }</td>
								<td>${product.quantity }</td>
								<td>
								<c:if test="${product.state eq  0}">
									未分拣		
								</c:if>
								<c:if test="${product.state eq  3}">
									已分拣		
								</c:if>
								</td>			
								<td>${product.shopname}</td>
								<td>${product.location}</td>
								<td>${product.oprationtime}</td>		
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
			<table id="table_report2" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="checkAll" /><span class="lbl"></span></label>
						</th>
						<th>序号</th>
						<!-- <th>商品条形码</th>
						<th>商品名称</th>
						<th>商品单位</th>
						<th>商品数量</th>
						<th>分拣状态</th> -->
						<th>便利店名称</th>
						<th>对应储位</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty mList}">
						<c:forEach items="${mList}" var="mer" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					            </td>  
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${mer.shopname}</td>
								<td>${mer.location}</td>	
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
									<td style="vertical-align:top;"></td>
									<td style="vertical-align:top;"><div class="pagination"
											style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
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
		
			//日期框
			$('.date-picker').datepicker();
		
		});
		$("#table_report2").hide();
		//$("#kid").hide();
		$("#laneway").hide();
		$("#table_report1").show();
			//加载第二搜索条件
			if (${pd.searchcriteria !=null}) {
				$("#searchcriteria").val("${pd.searchcriteria}");
				loadSecond();
				if (${pd.laneway !=null}) {
					$("#laneway").val("${pd.laneway}");
				}
			}
			function loadSecond() {
			
				if ($("#searchcriteria").val() == "1"||$("#searchcriteria").val() == "2"||
						$("#searchcriteria").val() == "3"||$("#searchcriteria").val() == "4") {
					$("#table_report2").hide();
					$("#table_report1").show();
					if($("#searchcriteria").val() == "1"||$("#searchcriteria").val() == "2"){
						//$("#kid").hide();
					}
					if($("#searchcriteria").val() == "3"||$("#searchcriteria").val() == "4"){
						$("#kid").show();
					}
				}else{
					if($("#searchcriteria").val() == "5"||$("#searchcriteria").val() == "6"){
						$("#kid").show();
					}
					$("#table_report1").hide();
					$("#table_report2").show();
				}
				if ($("#searchcriteria").val() == "3") {
					//$("input[name='keyword']").hide();
					$("input[name='keyword']").val("");
					$("#laneway").val("${pd.laneway}");
					$("#laneway").show();
				} else {
					$("input[name='keyword']").val("${pd.keyword}");
					//$("input[name='keyword']").show();
					$("#laneway").hide();
					$("#laneway").val("");
				}
			}
		//检索
		function search(){
			top.jzts();
			$("#bulkForm").submit();
		}
		//导出excel
		function toExcel(){
		var keyword=$("#keyword").val();
		var searchcriteria=$("#searchcriteria").val();
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
			window.location.href='<%=basePath%>bulk/excelOfMerchant.do?searchcriteria='+searchcriteria+'&keyword='+keyword+'&startDate='+startDate+'&endDate='+endDate;
		}
		</script>
	</body>
</html>

