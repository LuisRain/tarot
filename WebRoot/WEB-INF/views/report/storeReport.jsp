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
			<form action="<%=basePath%>reportController/storeReport.do" method="post" id="merchantForm">
			<c:if test="${QX.cha == 1 }">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" >  
			  <option value ="1">1-按批次号</option>
              <option value ="2">2-按门店</option>  
              <option value="3">3-按门店编码</option>  
               </select>  
				</td>
					<td>
						<span class="input-icon">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						</span>
                     <select id="type" name="type" >  
					  <option value ="0" <c:if test="${pd.type==0 }">selected="selected"</c:if>>1-全部</option>
		              <option value ="2" <c:if test="${pd.type==2 }">selected="selected"</c:if>>2-仓配</option>  
		              <option value="1"  <c:if test="${pd.type==1 }">selected="selected"</c:if>>3-直配</option>  
	               </select>  
                     <button class="btn btn-mini btn-light" style="margin-bottom:10px" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
						<td>
					<a class="btn btn-mini btn-light" style="margin-bottom:10px"  onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
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
						<th>门店</th>
						<th>门店编码</th>
						<th>门店简称</th>
						<th>地址</th>
						<th>联系人</th>
						<th>联系人手机</th>
						<th>订单总额</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty listpd}">
						<c:forEach items="${listpd}" var="merchant" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					            <label><input type='checkbox' name='ids'  value="${merchant.id}" id="${merchant.id}" /><span class="lbl"></span></label>
					            </td>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${merchant.merchant_name}</td>
								<td>${merchant.merchant_num}</td>
								 <td>${merchant.short_name}</td>
								<td>${merchant.address}</td>
								<td>${merchant.contact_person}</td>
								<td>${merchant.mobile}</td>
								<td>${merchant.sumprice}</td>
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
					<a class="btn btn-small btn-success" onclick="add();">新增</a></c:if> --%>							
				<!-- 	<a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a> -->
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
	    /* $("#city").val("${pd.city}"); */
		$(top.hangge());
		
		//检索
		function search(){
			top.jzts();
			$("#merchantForm").submit();
		}
		
		     $("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
		
		//修改
		function showMerchantInfo(Id){
		 location.href=	'<%=basePath%>merchant/goInfo.do?id='+Id;
		}
		
			//导出excel
		function toExcel(){
			var keyword = $("#nav-search-input").val();
			var searchcriteria = $("#searchcriteria").val();
            var keyworda=encodeURI(encodeURI(keyword));
            var type=$("#type").val();
			window.location.href='<%=basePath%>reportController/exportexcel.do?keyword='+keyword+'&searchcriteria='+searchcriteria+'&type='+type;
		}
		</script>
 
	</body>
</html>

