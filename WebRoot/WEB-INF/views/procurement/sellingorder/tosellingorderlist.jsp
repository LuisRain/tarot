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
	<style>
.td-label label {
	display: block;
	margin: 0 10px;
	float: left;
}

.lbl {
	font-size: 12px;
	vertical-align: middle;
}
</style>
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="<%=basePath%>sellingorder/findsellingorderlist.do" method="post" id="SellingOrderForm">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-按供应商名称/编码</option>  
              <option value ="2"  >2-按采购日期</option>  
              <option value="3" >3-按订单编号</option>  
              <option value="4" >4-按联系人</option>  
               </select>  
				</td>
					<td>
						<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
					<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
							<!-- <select name="state">
									<option  value="" >状态过滤----请选择</option>
									<option value="1" <c:if test="${pd.state==1 }">selected='selected'</c:if>>1-未审核</option>
									<option value="2" <c:if test="${pd.state==2 }">selected='selected'</c:if>>2-已审核</option>
							</select> -->
							<select name="type">
									<option  value="0" ${pd.type eq ''? "selected='selected'":""}>配送方式----请选择</option>
									<option value="1" ${pd.type eq 1 ? "selected='selected'":""}>1-加急</option>
									<option value="2" ${pd.type eq 2 ? "selected='selected'":""}>2-仓配</option>
							</select>
								<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();"
											title="检索">
										<i id="nav-search-icon" class="icon-search"></i>
								</button>
								<!-- <c:if test="${pd.checked_state eq 1}">
									<a class="btn btn-warning btn-mini" style="margin-bottom: 10px" onclick="examine('确定要审核选中的数据吗?');" title="审核"> 
									<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
									</a>
								</c:if> -->
							<!-- <button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="fromExcel();"
											 title="导入销售订单">
										<i id="nav-search-icon" class="icon-cloud-upload"></i>
							</button> -->
						</span>
					</td>
				</tr>
			</table>
			<input type="hidden" name="cityid" id="city" value="${pd.cityid}">
			<table style="width:100%;margin: 10px 0 20px;">
							<tr>
							<td style="width:5%;">按城市选择: <label><input name="cityqx" type="checkbox" 
										  onclick="SelectAll();"/><span class="lbl">全选</span> </label> 
								   <label><input name="cityqbx" type="checkbox" 
										  onclick="SelectNo();"/><span class="lbl">全不选</span> </label> </td>
							 
								<td style="width:90%;" class="td-label">
							<c:forEach items="${area}" var="are" varStatus="vn">
						         <label><input name="city" type="checkbox" value="${are.id}"  onclick="search();"/><span class="lbl">${are.area_name}</span> </label> 
						  </c:forEach>
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
						<th>门店名称</th>
						<th>城市</th>
						<th>联系人</th>
						<th>联系电话</th>
						
						<th>订购日期</th>
						<th>送货日期</th>
						<!-- 
						<th>订单状态</th> -->
						<th>配送</th>
						<th>订单备注</th>
						<!-- <th class="center">操作</th> -->
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
					            	<input type="hidden" name="checked_state" value="${purchaseOrder.checked_state}"> 
					            </td>
								<td class='center' style="width: 30px;">${purchaseOrder.id}</td>
								<td><a id="ordernum" onclick="findorderitem('${purchaseOrder.order_num }','${purchaseOrder.checked_state }');">${purchaseOrder.order_num }</a></td>
								<td>${purchaseOrder.merchant_name}</td>
								<td>${purchaseOrder.area_name}</td>
								<td>${purchaseOrder.contact_person}</td>
								<td>${purchaseOrder.manager_tel}</td>
								
								<td>${purchaseOrder.order_date}</td>
								<td>${purchaseOrder.deliver_date}</td>
								
								<%-- <td>
									<c:if test="${purchaseOrder.checked_state eq '1'}">未审核</c:if>
									<c:if test="${purchaseOrder.checked_state eq '2'}">已审核</c:if>
								</td> --%>
								<td>
									<c:if test="${purchaseOrder.type eq '1'}">加急</c:if>
									<c:if test="${purchaseOrder.type eq '2'}">仓配</c:if>
								</td>
								<td>${purchaseOrder.comment}</td>	
								<%-- <td>
								<a class='btn btn-mini btn-info' title="编辑" onclick="edit('${purchaseOrder.id}');"><i class='icon-edit'></i></a>
								 <a class='btn btn-mini btn-danger' title="删除" onclick="del('${purchaseOrder.id}');"><i class='icon-trash'></i></a>
								</td> --%>
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
				<!-- <td style="vertical-align:top;">
					<a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a>
				</td> -->
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
						
						$("#zcheckbox").tips({
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
			$("#SellingOrderForm").submit();
		}
		function SelectAll() {
			 var chestr="";
			 var checkboxs=document.getElementsByName("city");
			 for (var i=0;i<checkboxs.length;i++) {
			 var e=checkboxs[i];
			  e.checked=!e.checked;
			  chestr+=checkboxs[i].value+",";
			 }
			   $("#city").val(chestr.substring(0,chestr.length-1));
			   top.jzts();
				$("#SellingOrderForm").submit(); 
			}
		
		function SelectNo() {
			 var checkboxs=document.getElementsByName("city");
			 for (var i=0;i<checkboxs.length;i++) {
			 var e=checkboxs[i];
			  e.checked=e.checked;
			 }
			   $("#city").val('');
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
		/**
		*	查询详情
		*/
		function findorderitem(id,state){
			location.href = "<%=basePath%>sellingorder/findorderitem.do?order_num="+id+"&state="+state;
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
						  		bootbox.dialog("请选择未审核的销售订单!", 
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
								url: '<%=basePath%>sellingorder/examine.do?tm='+new Date().getTime(),
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

