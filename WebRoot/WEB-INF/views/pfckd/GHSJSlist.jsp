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
			<%-- <c:if test="${QX.cha == 1 }"> --%>
			<!-- 检索  -->
			<form action="<%=basePath%>ghsjscontroller/findGHSJSlistpage.do" method="post" id="SupplierForm">
			<table>
				<tr>
				<td>
					供货商名称：<input autocomplete="off" id="suppname" type="text" name="suppname" value="${pd.suppname}" placeholder="输入供货商名称" />
				</td>
					<td>
						<span class="input-icon">
						 	月份：<input autocomplete="off" id="month" type="text" name="month" value="${pd.month}" placeholder="格式：201806" />
						 	<!-- <span style="color: red">格式：年份+月份 如：201806</span> -->
							<button class="btn btn-mini btn-light" onclick="search();" title="检索" style="margin-bottom: 10px"><i id="nav-search-icon" class="icon-search"></i></button>
							<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="print()" title="打印">打印</button>	
							<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="expexcel()" title="导出Excel">导出Excel</button>	
						</span>
					</td>
					
				</tr>
			</table>
			<%-- </c:if> --%>
			<!-- 检索  -->
			
		
			<table id="table_report" class="table table-striped table-bordered table-hover">
				
				<thead>
					<tr>
						<th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="checkAll" /><span class="lbl"></span></label>
						</th>
						<th>序号</th>
						<th>供应商名称 </th>
						<th>含税金额</th>
						<th>不含税金额</th>
						<th>期末库存商品余额</th>
						<th>已销售商品金额</th>
						<th>申请付款金额</th>
						<th>备注</th>
						<th class="center">操作</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="list" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					            <label><input type='checkbox' name='ids'  value="${list.supplier_id}" id="${list.supplier_id}" /><span class="lbl"></span></label>
					            </td>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${list.supplier_name}</td>
								<td>${list.TotalPrice}</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>	
								<td>${list.comment}</td>
								<td>
								<%-- <c:if test="${QX.edit == 1 }"> --%>
								<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="supprint('${list.supplier_id}')" title="打印">打印</button>	
								<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="supexp('${list.supplier_id}')" title="导出">导出</button>
								<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="dzhprint('${list.supplier_id}')" title="打印对账函">打印对账函</button>		
							<!--  	 <a class='btn btn-mini btn-danger' title="删除" onclick="delSupplier('${suppler.id}','${suppler.supplier_name}');"><i class='icon-trash'></i></a>-->
								<%-- </c:if> --%></td>
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
					<%-- <c:if test="${QX.add == 1 }">
					<a class="btn btn-small btn-success" onclick="add();">新增</a>	</c:if>				 --%>		
				<!--  	<a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a>-->
				
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
		 $(function(){	
		
		  $("#searchcriteria").val("${pd.searchcriteria}");
		
		 });
		$(top.hangge());
		
		//检索
		function search(){
			top.jzts();
			$("#SupplierForm").submit();
		}
		
		     $("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
		
		/**
		*	打印所有供应商信息
		*/
		function print(){
			var suppname = $("#suppname").val();
			var month = $("#month").val();
			window.open("<%=basePath%>ghsjscontroller/printlist.do?suppname="+suppname+"&month="+month, "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');
		}
		
		/**
		*	打印单个供应商信息
		*/
		function supprint(id){
			window.open("<%=basePath%>ghsjscontroller/supprint.do?supplier_id="+id, "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');
		}
		//打印对账函
		function dzhprint(id){
			var month = $("#month").val();
			if(month==null || month == ''){
				alert("请输入年月");
				return false;
			}
			window.open("<%=basePath%>ghsjscontroller/dzhprint.do?supplier_id="+id+"&month="+month, "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');
		}		
		//导出所有供应商信息到excel
		function expexcel(){
			var suppname = $("#suppname").val();
			var month = $("#month").val();
			window.location.href = "<%=basePath%>ghsjscontroller/expsupexcel.do?suppname=" +suppname + "&month=" +month;
		}
		function supexp(id){
			window.location.href = "<%=basePath%>ghsjscontroller/expexcel.do?supplier_id="+id;
		}
		</script>
		
	
		
	</body>
</html>

