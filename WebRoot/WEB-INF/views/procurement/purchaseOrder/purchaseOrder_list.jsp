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
	<script type="text/javascript">
		function fromExcel(){
			var searchcriteria=$("#searchcriteria").val();
			var keyword = $("#nav-search-input").val();
			var lastLoginStart = $("#lastLoginStart").val();
			var lastLoginEnd = $("#lastLoginEnd").val();
			var state = $("#state").val();
			window.location.href='<%=basePath%>purchaseOrder/purexcel.do?searchcriteria='+searchcriteria+'&lastLoginStart='+lastLoginStart+'&lastLoginEnd='+lastLoginEnd+'&keyword='+keyword+'&state='+state;
		}
	</script>
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="<%=basePath%>purchaseOrder/purchaseOrdersList.do" method="post" id="PurchaseOrderForm">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-按供应商名称</option>  
              <option value ="2"  >2-按采购日期</option>  
              <option value="3" >3-按订单编号</option>  
              <option value="4">4-按批次号</option> 
               </select>  
				</td>
					<td>
					<!-- 	<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
					<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
					<select name="state">
									<option  value="" ${pd.state eq ''? "selected='selected'":""}>状态过滤----请选择</option>
									<option value="1" ${pd.state eq 1 ? "selected='selected'":""}>1-未审核</option>
									<option value="2" ${pd.state eq 2 ? "selected='selected'":""}>2-已审核</option>
							</select>
					<c:if test="${pd.checked_state eq 1}">
									<a class="btn btn-warning btn-mini" style="margin-bottom: 10px" onclick="examine('确定要审核选中的数据吗?');" title="审核"> 
									<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
									</a>
								</c:if>
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
						</span> -->
						
							<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
					<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
							<select name="state" id="state">
									<option  value="" ${pd.state eq ''? "selected='selected'":""}>状态过滤----请选择</option>
									<option value="1" ${pd.state eq 1 ? "selected='selected'":""}>1-未审核</option>
									<option value="2" ${pd.state eq 2 ? "selected='selected'":""}>2-已审核</option>
							</select>
								<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();"
											title="检索">
										<i id="nav-search-icon" class="icon-search"></i>
								</button>
								<!-- 审核权限 -->
								<c:if test="${QX.FX_QX==1}">
								<c:if test="${pd.checked_state eq 1}">
									<a class="btn btn-warning btn-mini" style="margin-bottom: 10px" onclick="examine('确定要审核选中的数据吗?');" title="审核"> 
									<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
									</a>
								</c:if>
								</c:if>
							<!-- <button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="fromExcel();"
											 title="导入销售订单">
										<i id="nav-search-icon" class="icon-cloud-upload"></i>
							</button> -->
							<c:if test="${pd.USERNAME=='admin'|| pd.role_id==32}">
								<a class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="fromExcel();" title="导出采购单"><i id="nav-search-icon" class="icon-download-alt"></i></a>
							</c:if>
							
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
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th>序号</th>
						<th>订单编号</th>
						<th>供应商</th>
						<th>采购金额</th>
						<th>采购日期</th>
						<th>送货日期</th>
						<th>联系人</th>
						<th>联系电话</th>
						<th>订单状态</th>
						<!-- <th class="center">操作</th> -->
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="purchaseOrder" varStatus="vs">
							<tr>
							<%-- <input type="hidden" name="checked_state" value="${purchaseOrder.checked_state}">  --%>
							<td class='center' style="width: 30px;"><label><input
														type='checkbox' name='ids' value="${purchaseOrder.id}" /><span
														class="lbl"></span></label> <label style="display: none"><input
														name='checked_state' value="${purchaseOrder.checked_state}" /><span
														class="lbl"></span></label></td>
					            <%-- <td class='center' style="width: 30px;">
					            <label><input type='checkbox' name='ids'  value="${purchaseOrder.id}" id="${purchaseOrder.id}" /><span class="lbl"></span></label>
					            </td> --%>
								<td class='center' style="width: 30px;">${purchaseOrder.id}</td>
								<td><a id="ordernum" onclick="findorderitem('${purchaseOrder.order_num }');">${purchaseOrder.order_num }</a></td>
								<td>${purchaseOrder.supplier_name}</td>
								<td>${purchaseOrder.order_amount}</td>
								<td>${purchaseOrder.order_date}</td>
								<td>${purchaseOrder.deliver_date}</td>
								<td>${purchaseOrder.manager_name}</td>
								<td>${purchaseOrder.contact_person_mobile}</td>
								<td>
									<c:if test="${purchaseOrder.checked_state eq '1'}">未审核</c:if>
									<c:if test="${purchaseOrder.checked_state eq '2'}">已审核</c:if>
								</td>	
								<!--
								<td>
								 <a class='btn btn-mini btn-info' title="编辑" onclick="edit('${purchaseOrder.id}');"><i class='icon-edit'></i></a> 
								 <a class='btn btn-mini btn-danger' title="删除" onclick="del('${purchaseOrder.id}');"><i class='icon-trash'></i></a>
								 <a class="btn btn-mini btn-light" onclick="toExcel('0','${purchaseOrder.id}');" title="供应商导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
								 <a class="btn btn-mini btn-light" onclick="toExcel('1','${purchaseOrder.id}');" title="客户商导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a> 
								</td>
								-->
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
				<!--<td style="vertical-align:top;">
					<a class="btn btn-small btn-success" onclick="add();">新增</a>							
					<a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a>
				
				</td>-->
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
		//设置时间加载插件
		$(function(){
			if(${pd.lastLoginStart !=null}){
			$("#searchcriteria").val("${pd.searchcriteria}");
			loaddate();
			}
			//日期框
			$('.date-picker').datepicker();
			//复选框
			$('table th input:checkbox').on('click' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
					
			});
		});
		function loaddate(){
			if($("#searchcriteria").val()=="2"){
				$("input[name='keyword']").hide();
				$("input[name='keyword']").val("");
				$("#lastLoginStart").show();
				$("#lastLoginEnd").show();
			}else{
				$("input[name='keyword']").show();
				$("#lastLoginStart").hide();
				$("#lastLoginStart").val("");
				$("#lastLoginEnd").hide();
				$("#lastLoginEnd").val("");
			}
		}
 
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					var emstr = '';
					var phones = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
						  	else emstr += ';' + document.getElementsByName('ids')[i].id;
						  	
						  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
						  	else phones += ';' + document.getElementsByName('ids')[i].alt;
						  }
					}
					if(str==''){
						bootbox.dialog("您没有选择任何内容!", 
							[
							  {
								"label" : "关闭",
								"class" : "btn-small btn-success",
								"callback": function() {
									//Example.show("great success");
									}
								}
							 ]
						);
						
						$("#checkAll").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>purchaseOrder/deleteAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
						
						
					}
				}
			});
		}
	 
		//检索
		function search(){
			top.jzts();
			$("#PurchaseOrderForm").submit();
		}
		/**
		*	查询详情
		*/
		function findorderitem(id){
			location.href = "<%=basePath%>purchaseOrder/findorderitem.do?order_num="+id;
		}
		//审核操作
		function examine(msg){
			bootbox.confirm(msg, function(result) {  
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	  if(document.getElementsByName("checked_state")[i].value!=1){
						  		bootbox.dialog("请选择未审核的采购订单!",
										[
										  {
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}
											}
										 ]
									);
									
						  		  
						    	  return ;
						  		  
						  	  }
						  }
					}
					if(str==''){
						bootbox.dialog("您没有选择任何内容!", 
							[
							  {
								"label" : "关闭",
								"class" : "btn-small btn-success",
								"callback": function() {
									//Example.show("great success");
									}
								}
							 ]
						);
						
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						
						return;
						
					}
					else{	
						if(msg == '确定要审核选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>purchaseOrder/examine.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'text',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									if(data=="true"){
										   alert("审核成功");
									}else if(data=="false"){
										alert("审核失败");
									}else{
										alert(data);
									}
								 nextPage(${page.currentPage});
								}
							});
						}
					}
				}
			});
		};
		</script>
	</body>
</html>

