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
<style>
.td-label label{display:block;margin: 0 10px;float:left;}
.lbl{font-size:12px;vertical-align:middle;}
</style>
</head>
<body>
	<div class="container-fluid" id="main-container">
		<div id="page-content" class="clearfix">
			<div class="row-fluid">
				<div class="row-fluid">
					<form action="exwarehouseorder/listOfGift.do" method="post" name="Form"
						id="Form">
						<input type="hidden" name="order_type" value="${pd.order_type}">
						<input type="hidden" name="ivt_state" value="${pd.ivt_state}">
								<input type="hidden" name="cityid" id="city" value="${pd.cityid}">
						<table>
							<tr>
								<td><select id="searchcriteria" name="searchcriteria"
									onchange="loaddate()">
										<option value="1">1-按供应商/客户名称</option>
										<option value="2">2-按订购日期</option>
										<option value="3">3-按出库编号</option>
										<option value="4">4-按批次号</option>
								</select></td>
								<td><span class="input-icon"> <input
										class="span10 date-picker" id="orderdate" name="order_date"
										value="${pd.order_date}" type="text"
										data-date-format="yyyy-mm-dd" readonly="readonly"
										style="width:110px; display: none;" placeholder="按订购日期"
										title="按订购日期" /> <input autocomplete="off"
										id="nav-search-input" type="text" name="keyword"
										value="${pd.keyword}" placeholder="这里输入关键词" /> <c:if
											test="${empty pd.ivt_state}">
											<select name="ivt_state" id="ivt_state">
												<option value="">状态过滤----请选择</option>
												<option value="11">1-新出库单</option>
												<option value="4">4-订单已完成</option>
												<!-- <option value="6">6-完成</option> -->
											</select>
										</c:if>
									 <c:if test="${pd.ivt_state eq 11}">
											<a  class="btn btn-warning btn-mini"
												onclick="makeAll1('确定要审核您所选的出库单吗?');"
												title="出库单审核"><i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
											</a>
									</c:if>
											<!-- <a class="btn btn-mini btn-light" onclick="toSplitEXToExcel();" title="导出商品拆分出库单">
									<i id="nav-search-icon" class="icon-download-alt"></i>
								</a> -->
										 <%-- <c:if test="${pd.ivt_state eq 2}">
											<a class="btn btn-mini btn-light"
												onclick="showAndEditWaveForExOrder('确定要波次操作您所选的出库单吗?');"
												title="管理波次出库单"> <i id="nav-search-icon"
												class="icon-cloud-upload"> </i>
											</a>
										</c:if> <c:if test="${pd.ivt_state eq 300}">
											<a class="btn btn-warning btn-mini"
												onclick="makeAll('确定要波次操作您所选的出库单吗?');" title="批量审核"> <i
												id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
											</a>
										</c:if> <c:if test="${pd.ivt_state eq 3}">
											<a class="btn btn-mini btn-light"
												onclick="toCombineEXTotoExcel('确定要波次操作您所选的出库单吗?');" title="合并出库单"> <i
												id="nav-search-icon" class="icon-exchange"></i>
											</a>
										</c:if> --%>
								</span>			<button class="btn btn-mini btn-light" onclick="search();"
											title="检索">
									<i id="nav-search-icon" class="icon-search"></i>
							</button>	
								</td>
							</tr>
							
						</table>
						<table style="width:100%;margin: 10px 0 20px;">
							<tr>
							<td style="width:5%;">按城市选择:</td>
								<td style="width:90%;" class="td-label">
							<c:forEach items="${area}" var="are" varStatus="vn">
						         <label><input name="city" type="checkbox" 
										value="${are.id}"  onclick="search();"/><span class="lbl">${are.area_name}</span> </label> 
						  </c:forEach>
									</td>
							</tr>
								
						</table>
						<table id="table_report"
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th class="center"><label><input type="checkbox"
											id="zcheckbox" /><span class="lbl"></span></label></th>
									<th class="center">序号</th>
									<th class="center">批次号</th>
									<th class="center">出库订单编号</th>
									<th class="center">门店</th>
									<th class="center">联系人</th>
									<th class="center">联系方式</th>
									<th class="center">出库状态</th>
									<th class="center">备注</th>
									<th class="center">总体积</th>
									<th class="center">总重量</th>
									<th class="center">创建人</th>
									<th class="center">订单生成时间</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty varList}">
										<c:forEach items="${varList}" var="var" varStatus="vs">
											<tr>
												<td class='center' style="width: 30px;"><label><input
														type='checkbox' name='ids' value="${var.id}" /><span
														class="lbl"></span></label> <label style="display: none"><input
														name='states' value="${var.ivt_state}" /><span
														class="lbl"></span></label></td>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td>${var.group_num}</td>
												<td><a
													onclick="editProductNumber('${var.order_num}',1)">${var.order_num}</a></td>
												<td>${var.merchant_name}</td>
												<td>${var.manager_name}</td>
												<td>${var.manager_tel}</td>
												<c:if test="${var.ivt_state eq  11}">
													<td>新出库单</td>
												</c:if>
												<c:if test="${var.ivt_state eq  2}">
													<td>LED系统正在分拣</td>
												</c:if>
												<c:if test="${var.ivt_state eq  3}">
													<td>标签系统正在分拣</td>
												</c:if>
												<c:if test="${var.ivt_state eq  5}">
													<td>订单完成</td>
												</c:if>
												<%-- <c:if test="${var.ivt_state eq  5}">
													<td>订单已完成</td>
												</c:if> --%>
												<%-- <c:if test="${var.ivt_state eq  6}">
									   <td>
									 完成
									 </td>
									 </c:if> --%>
												<td>${var.comment}</td>
												<td>${var.total_svolume}</td>
												<td>${var.total_weight}</td>
												<td>${var.userName}</td>
												<td>${var.create_time}</td>
											</tr>

										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<c:if test="${pd.ivt_state eq  11}">
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
									<td style="vertical-align:top;"></td>
									<td style="vertical-align:top;"><div class="pagination"
											style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</div>
			<!--/row-->
		</div>
		<!--/#page-content-->
	</div>
	<!--/.fluid-container#main-container-->
	<!-- 返回顶部  -->
	<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse"> <i
		class="icon-double-angle-up icon-only"></i>
	</a>
	<!-- 引入 -->
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>

	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<!-- 日期框 -->
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!--提示框-->
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
	        var str=document.getElementsByName("city");
            var objarray=str.length;
            var chestr="";
            for ( var i=0;i<objarray;i++){
              if(str[i].checked == true){
              chestr+=str[i].value+",";
              }
            }  
            $("#city").val(chestr.substring(0,chestr.length-1));
			 top.jzts();
			$("#Form").submit(); 
		}
		
		

 
			 //修改商品数量
		  function editProductNumber(id1,type){
			var url='<%=basePath%>exwarehouseorder/goExwareorderProductEdit.do?orderId='+id1+'&type='+type;
				top.mainFrame.tabAddHandler("392060853225","查看出库单信息",url);
			 
		 } 
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>exwarehouseorder/goAdd.do';
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
					var url = "<%=basePath%>exwarehouseorder/delete.do?id="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>exwarehouseorder/goEdit.do?id='+Id;
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
			
			
						var inputs1 = document.getElementsByName("city");
						for (var j = 0; j < inputs1.length; j++) {
			                        inputs1[j].checked = "";
			            }	
						var arr1=$("#city").val();
						if(arr1!=null){
				            arr1 = arr1.split(",");
				            for (var i = 0; i < arr1.length; i++) {
				                for (var j = 0; j < inputs1.length; j++) {
				                    if (arr1[i] == inputs1[j].value) {
				                        inputs1[j].checked = true;
				                       break;
				                    }
				                }
				            }
			            }
		});
		//打开上传excel页面
		function fromExcel(){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>enwarehouseorder/goUploadExcel.do?type=0';
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
		//第三步
		function showAndEditWaveForExOrder(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=2){
						  		bootbox.dialog("只能选择LED分拣结束的出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}}]);
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
						if(msg == '确定要波次操作您所选的出库单吗?'){
							top.mainFrame.tabAddHandler("999","波次出库商品分量清单","<%=basePath%>exwarehouseorder/doWaveExwareorderOfPerForGift.do?DATA_IDS="+str);
							if(MENU_URL != "druid/index.html"){
								jzts();
							}
						}
					}
				}
			});
		}
		//第四步 toCombineEXTotoExcel
		function toCombineEXTotoExcel(msg){//'确定要波次操作选中的出库单吗?'
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=3){
						  		bootbox.dialog("只能选择标签系统分拣结束的出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}}]);
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
						if(msg == '确定要波次操作您所选的出库单吗?'){
							<%-- top.mainFrame.tabAddHandler("999","出库单管理","<%=basePath%>exwarehouseorder/doSumEXOItem.do?ivt_state=4");
							if(MENU_URL != "druid/index.html"){
								jzts();
							} --%>
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>exwarehouseorder/doSumEXOItemForGift.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									nextPage(${page.currentPage});
									//window.location.href='<%=basePath%>exwarehouseorder/listOfGift.do?ivt_state=3&order_type=1';
								}
							}); 
						}
					}
				}
			});
		}
		//LED波次操作
		function toWaveForExOrder(msg){//'确定要波次操作选中的出库单吗?'
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=1){
						  		bootbox.dialog("只能选择新出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}}]);
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
						if(msg == '确定要波次操作您所选的出库单吗?'){
							top.mainFrame.tabAddHandler("999","波次出库商品清单","<%=basePath%>exwarehouseorder/doWaveExwareorderForGift.do?DATA_IDS="+str);
							if(MENU_URL != "druid/index.html"){
								jzts();
							}
						}
					}
				}
			});
		}
		
		
		//导出出库单 
		function toSplitEXToExcel(){
			window.location.href='<%=basePath%>exwarehouseorder/toSplitEXToExcel.do';
		}
		
		//合并出库单toWaveForExOrder;
		function toCombineEXTotoExcel1(){
			window.location.href='<%=basePath%>exwarehouseorder/toCombineEXTotoExcel.do';
		}
		function waveEx(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=2){
						  		bootbox.dialog("请选择审核过的出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}
										}]);
						    	  return ;
						  	  }
						  }
					}
					if(str==''){
						bootbox.dialog("您没有选择任何内容!", 
							[{
								"label" : "关闭",
								"class" : "btn-small btn-success",
								"callback": function() {
									//Example.show("great success");
									}
							}]);
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要波次操作您所选的出库单吗?'){
							top.mainFrame.tabAddHandler("999","波次出库商品清单","<%=basePath%>exwarehouseorder/doWaveExwareorderForGift.do?DATA_IDS=" +str);
												if (MENU_URL != "druid/index.html") {
													jzts();
												}
											}
										}
									}
								});
			}
			
			//批量操作
		function makeAll1(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
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
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>exwarehouseorder/giftReviewedAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'text',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									if(data=="true"){
										   alert("审核出库成功");
									}else if(data=="false"){
										alert("审核出库失败");
									}else{
										alert(data);
									}
								 nextPage(${page.currentPage});
									 
								}
							});
					}
				}
			});
		}
	
		</script>

</body>
</html>

