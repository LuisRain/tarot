<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
  <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
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
</head>
<body>
	<div class="container-fluid" id="main-container">
		<div id="page-content" class="clearfix">
			<div class="row-fluid">
				<div class="row-fluid">
				<c:if test="${QX.cha == 1 }">
					<form action="thproduct/CgOrderList.do" method="post" name="Form"
						id="Form">
						<input type="hidden" name="order_type" value="${pd.order_type}">
						<input type="hidden" name="ivt_state" value="${pd.ivt_state}">
						<c:if test="${QX.cha == 1 }">
						<table>
							<tr>
								<td><select id="searchcriteria" name="searchcriteria"
									onchange="loaddate()">
										<option value="1" <c:if test="${pd.searchcriteria == 1 }">selected="selected"</c:if> >1-按供应商名称</option>
										<option value="2" <c:if test="${pd.searchcriteria == 2 }">selected="selected"</c:if> >2-按订购日期</option>
										<option value="3" <c:if test="${pd.searchcriteria == 3 }">selected="selected"</c:if>  >3-按订单编号</option>
										<option value="4" <c:if test="${pd.searchcriteria == 4 }">selected="selected"</c:if> >4-按批次号</option>
								</select></td>
								<td><span class="input-icon"> <input
										class="span10 date-picker" id="orderdate" name="order_date"
										value="${pd.order_date}" type="text"
										data-date-format="yyyy-mm-dd" readonly="readonly"
										style="width:110px; display: none;" placeholder="按订购日期"
										title="按订购日期" /> <input autocomplete="off"
										id="nav-search-input" type="text" name="keyword"
										value="${pd.keyword}" placeholder="这里输入关键词" /> 
											<select name="checked_state" id="checked_state">
												<option value="">状态过滤----请选择</option>
												<option value="1" <c:if test="${pd.checked_state == 1 }">selected="selected"</c:if>>1-新出库单</option>
												<option value="2" <c:if test="${pd.checked_state == 2 }">selected="selected"</c:if>>2-完成</option> 
											</select>
									<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();"
											title="检索">
										<i id="nav-search-icon" class="icon-search"></i>
								    </button>
								    <c:if test="${QX.FX_QX==1  }">
										<c:if test="${pd.checked_state eq  1}">
											<a class="btn btn-warning btn-mini"
												onclick="makeAll('确定要审核选中的数据吗?');" title="批量审核"> 
												<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
											</a>
										</c:if> 
									</c:if>
								</span>
							
								</td>
							</tr>
						</table></c:if>
						<table id="table_report"
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th class="center"><label><input type="checkbox"
											id="zcheckbox" /><span class="lbl"></span></label></th>
									<th class="center">序号</th>
									<th class="center">批次号</th>
									<th class="center">订单编号</th>
									    <th class="center">供应商名称</th>
									<th class="center">取货地址</th>
									<!-- <th class="center">联系人</th>
									<th class="center">联系方式</th> -->
									<th class="center">订单状态</th>
									<!--<th class="center">备注</th>-->
									
									<th class="center">总体积</th>
									<th class="center">总重量</th>
								 	<th class="center">总金额</th>
								 <!-- 	<th class="center">打印</th> -->
									<!-- <th class="center">创建人</th>
									<th class="center">订单生成时间</th> -->
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty varList}">
										<c:forEach items="${varList}" var="var" varStatus="vs">
											<tr>
												<td class='center' style="width: 50px;">
													<label>
															<input type='checkbox' name='ids'  value="${var.order_num}" />
														<span class="lbl"></span></label> 
														<label style="display: none"><input name='states' value="${var.ivt_state}" /><span class="lbl"></span>
													</label>
												</td>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td class="center">${var.group_num}</td>
												<td class="center">
												<a onclick="editProductNumber('${var.order_num}',1)">${var.order_num}</a>
												<%-- &nbsp;&nbsp;&nbsp;&nbsp;
												<a onclick="editProductNumberPQ('${var.order_num}',1)">分拣详单</a> --%>
												</td>
													<td class="center">${var.supplier_name}</td>
												<td class="center">${var.deliver_address}</td>
												<%-- <td class="center">${var.manager_name}</td>
												<td class="center">${var.manager_tel}</td> --%>
												<c:if test="${var.checked_state eq  1}">
													<td class="center">新出库单</td>
												</c:if>
												  <c:if test="${var.checked_state eq  2}">
													<td>订单已完成</td>
												</c:if>   
										 		
												 <td class="center">${var.total_svolume}</td>
												<td class="center">${var.total_weight}</td> 
													<td class="center">${var.final_amount}</td>
												<%-- <td>
												<button type="button" style="float: right;" onclick="print('${var.order_num}',1)" class=" btn-app btn-light btn-mini">
													<i class="icon-print"></i>
													打印
												</button>
												</td> --%>
											</tr>

										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<c:if test="${pd.ivt_state eq  1}">
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
					</c:if>
						<c:if test="${QX.cha != 1 }">
						<div style="text-align: center;color:red;margin-top: 20px">
							<span>暂无权限</span>
						</div>
						</c:if>
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
		<script src="static/layui/layui.js" charset="utf-8"></script>
		
	<!--提示框-->
	<script type="text/javascript">
		var _qj=0;
		layui.use(['layer', 'form'], function(){
			  var layer = layui.layer ,form = layui.form; });
		$(top.hangge());
		//加载日期搜索框
		if(${pd.order_date !=null}){
			$("#searchcriteria").val("${pd.searchcriteria}");
			loaddate();
			if(${pd.ivt_state !=null}){
				$("#ivt_state").val("${pd.ivt_state}");
			}
			}
			
		function a(type){
			var str=document.getElementsByName("city");
            var objarray=str.length;
            var chestr="";
            for ( var i=0;i<objarray;i++){
              if(str[i].checked == true){
              chestr+=str[i].value+",";
              }
            }  
          var ordernum = chestr.substring(0,chestr.length-1);
            if(chestr==""){
            	layer.alert("请选择城市！",{icon: 6000});
            }else{
            	window.open("<%=basePath%>exwarehouseorder/printlist.do?city="+chestr+"&type="+type, "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');
            }
		}	
			
		function print(ordernum,type){
			
			window.open("<%=basePath%>exwarehouseorder/print.do?orderId="+ordernum+"&type="+type, "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
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
			var url='<%=basePath%>thproduct/goExwareorderProductEdit.do?orderId='+id1+'&type=1';
				top.mainFrame.tabAddHandler("392060853225","查看出库单信息",url);
			 
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

		function printa(msg){
		bootbox.confirm(msg, function(result) {
		if(result) {
		var orderNum="";
			for(var i=0;i < document.getElementsByName('ids').length;i++){
				if(document.getElementsByName('ids')[i].checked){
					if(orderNum=='') orderNum += document.getElementsByName('ids')[i].value;
					else orderNum += ',' + document.getElementsByName('ids')[i].value;
				}
			}
			if(orderNum==''){
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
						if(msg == '确定要打印该订单吗?'){
							window.open("<%=basePath%>exwarehouseorder/print.do?orderId="+orderNum+"&type=1", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');
							
						}
					}
				}
			});
		}
		
		//导出出库单 
		function toSplitEXToExcel(){
			window.location.href='<%=basePath%>exwarehouseorder/toSplitEXToExcel.do';
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
								url: '<%=basePath%>thproduct/cgExamine.do?tm='+new Date().getTime(),
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

