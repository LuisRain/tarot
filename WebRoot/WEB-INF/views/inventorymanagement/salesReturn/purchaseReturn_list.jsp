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
	<base href="<%=basePath%>"><!-- jsp文件头和头部 -->
	<%@ include file="../../system/admin/top.jsp"%> 
	</head>
	
	
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">

	<div class="row-fluid">
			<!-- 检索  -->
			<form action="<%=basePath%>enwarehouseorder/purchaseReturnlistpage.do" method="post" name="Form" id="Form">
			<c:if test="${QX.cha == 1 }"><table>
				<tr>
			 <td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1" >1-订单编号</option>  
              <option value ="2">2-退货日期</option> 
              </select>
				</td>
					<td>
						<span class="input-icon">
						   <input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						    <input class="span10 date-picker" id="orderdate" name="order_date"  value="${pd.order_date}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="按入库日期" title="按入库日期"/>
					  <select name="ivt_state" id="ivt_state">
					<option value="">状态过滤----请选择</option>
					<option value="1">1-待入库</option>
					<option value="2">2-已入库</option>
					     </select> 
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
							<a class="btn btn-warning btn-mini"
											onclick="makeAll('确定要审核选中的数据吗?');" title="批量审核"> <i
											id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
										</a>
						</span>
					</td>
					<Td> 
					</Td>
				</tr>
			</table>
			</c:if>
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th class="center">序号</th>
					    <th class="center">退货单编号</th>
					     <th class="center">供应商</th>
					      <th class="center">退货日期</th>
					       <th class="center">联系人</th>
					        <th class="center">联系电话</th>
					         <th class="center">状态</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						
						<c:forEach items="${varList}" var="var" varStatus="vs">
							<tr>
								<td class='center' style="width: 30px;">
									<label><input type='checkbox' name='ids' value="${var.id}" /><span class="lbl"></span></label>
								<label style="display: none"><input   name='states' value="${var.ivt_state}" /><span class="lbl"></span></label>
								</td>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
									<td><a onclick="editProductNumber('${var.order_num}',1)">${var.order_num}</a></td>
										<td>${var.supplier_name}</td>
										<td>${var.order_date}</td>
										<td>${var.manager_name}</td>
										<td>${var.manager_tel}</td>
									    <td>
									       <c:if test="${var.ivt_state==1}">
									                 待审核
									       </c:if>
									         <c:if test="${var.ivt_state==2}">
									                 已审核
									       </c:if>
									    </td>
									 
									 
							<%-- 	<td>
								<a class='btn btn-mini btn-info' title="编辑" onclick="edit('${var.id}');"><i class='icon-edit'></i></a>
								 <a class='btn btn-mini btn-danger' title="删除" onclick="del('${var.id}');"><i class='icon-trash'></i></a>
								</td> --%>
							</tr>
						
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="100" class="center" >没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
					
				
				</tbody>
			</table>
			
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					<c:if test="${QX.add == 1 }">
				  <a class="btn btn-small btn-success" onclick="add();">新增</a> 	</c:if>		
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
		
		$(top.hangge());
	    $("#reason").val("${pd.reason}");
		//加载日期搜索框
		if(${pd.order_date !=null}){
			$("#searchcriteria").val("${pd.searchcriteria}");
			loaddate();
		
			}
		if(${pd.ivt_state !=null}){
			$("#ivt_state").val("${pd.ivt_state}");
		}
		function loaddate(){
			if($("#searchcriteria").val()=="2"){
				$("input[name='keyword']").hide();
				$("input[name='keyword']").val("");
				$("#orderdate").val("${pd.order_date}");
				$("#orderdate").show();
			}else{
				$("input[name='keyword']").val("${pd.keyword}");
				$("input[name='keyword']").show();
				$("#orderdate").hide();
				$("#orderdate").val("");
			}
		}
		//检索
		function search(){
			top.jzts();
			$("#Form").submit();
		}
			//修改
			function add(){
		 
				var url='<%=basePath%>enwarehouseorder/addPurchaseOrder.do';
				top.mainFrame.tabAddHandler("3920638356","添加采购退货单",url);
			}
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>enwarehouseorder/delete.do?id="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		$(function() {
			//下拉框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
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
		//@ sourceURL=ccccc.js
		//打开上传excel页面
		function fromExcel(){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>importentempwarehouseorder/goImportTempExcel.do?type=0';
			 diag.Width = 500;
			 diag.Height = 300;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin1').style.display == 'none'){
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
		 //修改商品数量
	  function editProductNumber(id1,type){
		var url='<%=basePath%>enwarehouseorder/goEnwareorderPurchaseProductEdit.do?orderId='+id1+'&type='+type;
			top.mainFrame.tabAddHandler("392060853235","查看采购退货信息",url);
	 } 
	//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	  if(document.getElementsByName("states")[i].value!=1){
						  		  
						  		bootbox.dialog("审核不能审核!", 
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
					}else{
						if(msg == '确定要审核选中的数据吗?'){
							 
							$.ajax({
								type: "POST",
								url: '<%=basePath%>enwarehouseorder/purchaseReviewedAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'text',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									if(data=="false"){
										alert("审核失败,库存商品不足");
									}else{
										alert("审核成功");
									}
											nextPage(${page.currentPage});
									 
									
								}
							});
						}
					}
				}
			});
		}
		</script>
	</body>
</html>

