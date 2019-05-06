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
			<form action="enwarehouseorder/list.do" method="post" name="Form" id="Form">
			<c:if test="${QX.cha == 1 }"><table>
				<tr>
						<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-按供应商/客户名称</option>  
              <option value ="2"  >2-按订购日期</option>  
              <option value="3" >3-按入库单编号</option>  
              <option value="4" >4-按批次号</option>    
               </select>  
				</td>
					<td style="vertical-align:top;">
						<span class="input-icon">
						    <input class="span10 date-picker" id="orderdate" name="order_date"  value="${pd.order_date}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="按订购日期" title="按订购日期"/>
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
							
						</span>
				   
				 
						<td style="vertical-align:top;">
						<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					   <a class="btn btn-warning btn-mini" onclick="makeAll('确定要审核选中的数据吗?');" title="批量审核" ><i  id="nav-search-icon"  class='icon-wrench icon-2x icon-only'></i></a>
					    <c:if test="${QX.edit == 1 }"><a class="btn btn-mini btn-light" onclick="fromExcel();" title="上传EXCEL">
					    <i id="nav-search-icon" class="icon-cloud-upload"></i></a></c:if>
					</td>
				
				</tr>
			
			   
			</table></c:if>
			<!-- 检索  -->
		
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th class="center">
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th class="center">序号</th>
						<th class="center">批次号</th>
						<th class="center">入库订单编号</th>
						<th class="center">入库状态</th>
						<th class="center">供应商</th>
					 <!--<th class="center">入库时间</th> -->
						<th class="center">联系人</th>
						<th class="center">联系方式</th>
						<th class="center">备注</th>
						<th class="center"> 总体积</th>
						<th class="center">总重量</th>
					<!-- 	<th class="center">已付款额</th>
						<th class="center">打印状态</th>
						<th class="center">是否是临时入库单</th>  -->
						<th class="center">创建人</th>
						<th class="center">订单生成时间</th>
					<!--  <th class="center">订单打印状态</th>-->	
						<th class="center">操作</th>
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
										<td>${var.group_num}</td>
										<td><a onclick="editProductNumber('${var.order_num}',1)">${var.order_num}</a></td>
										
										
										
										 <c:if test="${var.ivt_state eq  1}">
										
										   
										<td>
										
										 待入库
										</td>
										</c:if>
										 <c:if test="${var.ivt_state eq  2}">
										
										   
										<td>
										  已入库
										 
										</td>
										
										</c:if>
										<td>${var.supplier_name}</td>
								<!--  	  <td>${var.order_date}</td>-->
										<td>${var.manager_name}</td>
										<td>${var.manager_tel}</td>
										<td>${var.comment}</td>
								 
										<td>${var.total_svolume}</td>
										<td>${var.total_weight}</td>
									<%-- 	<td>${var.paid_amount}</td>
										<td>${var.is_ivt_order_print}</td>
										<td>${var.is_temporary}</td>   --> --%>
										<td>${var.userName}</td>
										<td>${var.create_time}</td>
									<!--  	<td>${var.is_order_print}</td>-->
										 
								<td>
							     <c:if test="${QX.cha == 1 }">
								  <a class="btn btn-mini btn-light" onclick="toExcel('${var.id}');" title="导出到EXCEL">
								   
								  
								  <i id="nav-search-icon" class="icon-download-alt"></i></a>
								
								</c:if>
								</td>
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
				  <a class="btn btn-small btn-success" onclick="add();">新增</a> 
					</c:if>
				 
					
				</td>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
		</div>
			<div class="page-header position-relative">
	 
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
		//加载日期搜索框
		if(${pd.order_date !=null}){
			$("#searchcriteria").val("${pd.searchcriteria}");
			loaddate();
			if(${pd.ivt_state !=null}){
				$("#ivt_state").val("${pd.ivt_state}");
			}
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
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>enwarehouseorder/goAdd.do';
			 diag.Width = 450;
			 diag.Height = 355;
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

		
		
	 
		
		
		
		 //修改商品数量
		  function editProductNumber(id1,type){
			var url='<%=basePath%>enwarehouseorder/goEnwareorderProductEdit.do?orderId='+id1+'&type='+type;
				top.mainFrame.tabAddHandler("392060852","查看入库单信息",url);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
			 
		 } 
		 
		 
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>enwarehouseorder/goEdit.do?id='+Id;
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
		</script>
		
		<script type="text/javascript">
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
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>enwarehouseorder/reviewedAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
								     alert("审核入库成功");
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
		
		//打开上传excel页面
		function fromExcel(){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>enwarehouseorder/goUploadExcel.do?type=1';
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
		//导出excel
		function toExcel(id){
			window.location.href='<%=basePath%>enwarehouseorder/excel.do?id='+id;
		}
		
		//添加次品库入库单
	
		function  add(){
				var url='<%=basePath%>enwarehouseorder/goInferio.do';
					top.mainFrame.tabAddHandler("392062852","添加入库单",url);
				if(MENU_URL != "druid/index.html"){
					jzts();
		 }
				 
			
		}
		
		</script>
		
	</body>
</html>

