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
		<%@ include file="../../system/admin/top.jsp"%> 
	
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="<%=basePath%>sellingorder/sellingorderlist.do" method="post" id="SellingOrderForm">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-按供应商名称</option>  
              <option value ="2"  >2-按采购日期</option>  
              <option value="3" >3-按订单编号</option>  
               </select>  
				</td>
					<td>
						<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
					<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
							<button class="btn btn-mini btn-light" onclick="search();" title="检索">
							<i id="nav-search-icon" class="icon-search"></i></button>
							 <a class="btn btn-mini btn-light" onclick="fromExcel();" title="导入销售订单">
							<i id="nav-search-icon" class="icon-cloud-upload"></i></a> 
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
						<th>订单备注</th>
						<th>订购日期</th>
						<th>送货日期</th>
						<th>联系人</th>
						<th>联系电话</th>
						<th>订单状态</th>
						<th class="center">操作</th>
					</tr>
				</thead>
										
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="purchaseOrder" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					            <label><input type='checkbox' name='ids'  value="${purchaseOrder.id}" id="${purchaseOrder.id}" /><span class="lbl"></span></label>
					            </td>
								<td class='center' style="width: 30px;">${purchaseOrder.id}</td>
								<td><a>${purchaseOrder.order_num }</a></td>
								<td>${purchaseOrder.comment}</td>
								<td>${purchaseOrder.order_date}</td>
								<td>${purchaseOrder.deliver_date}</td>
								<td>${purchaseOrder.manager_name}</td>
								<td>${purchaseOrder.manager_tel}</td>
								<td>
									<c:if test="${purchaseOrder.checked_state eq '12'}">未审核</c:if>
									<c:if test="${purchaseOrder.checked_state eq '2'}">已审核</c:if>
								</td>
								<td>
								<a class='btn btn-mini btn-info' title="编辑" onclick="edit('${purchaseOrder.id}');"><i class='icon-edit'></i></a>
								 <a class='btn btn-mini btn-danger' title="删除" onclick="del('${purchaseOrder.id}');"><i class='icon-trash'></i></a>
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
	<%-- 	<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					<a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a>
				</td>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table> --%>
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
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>sellingorder/delete.do?id="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
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
								url: '<%=basePath%>sellingorder/deleteAll.do?tm='+new Date().getTime(),
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
		
		
		
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>sellingorder/goEdit.do?id='+Id;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		
		
		//检索
		function search(){
			top.jzts();
			$("#SellingOrderForm").submit();
		}
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>sellingorder/goAdd.do';
			 diag.Width = 550;
			 diag.Height = 415;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		//打开上传excel页面
		function fromExcel(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>sellingorder/goUploadExcel.do';
			 diag.Width = 300;
			 diag.Height = 150;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location.reload()",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		</script>
	</body>
</html>

