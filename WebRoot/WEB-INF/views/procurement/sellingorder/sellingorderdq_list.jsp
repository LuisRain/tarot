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
			<form action="<%=basePath%>sellingorder/sellingorderdq.do" method="post" id="SellingOrderForm">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-按门店名称</option>  
              <option value ="2"  >2-按采购日期</option>  
              <option value="3" >3-按订单编号</option>  
               </select>  
				</td>
					<td>
						<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
					<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
							<select name="state" id="state">
									<option  value="0" >状态过滤----请选择</option>
									<option value="12" <c:if test="${pd.state==12 }">selected='selected'</c:if>>新订单</option>
									<option value="1" <c:if test="${pd.state==1 }">selected='selected'</c:if>>地市已审核</option>
									<option value="2" <c:if test="${pd.state==2 }">selected='selected'</c:if>>省级已审核</option>
							</select>

							<select name="type" id="type">
									<option  value="0" ${pd.type eq ''? "selected='selected'":""}>配送方式----请选择</option>
									<option value="1" ${pd.type eq 1 ? "selected='selected'":""}>1-加急</option>
									<option value="2" ${pd.type eq 2 ? "selected='selected'":""}>2-仓配</option>
							</select>
							<button class="btn btn-mini btn-light" onclick="search();" title="检索">
							<i id="nav-search-icon" class="icon-search"></i></button>
							<!-- 审核权限  -->
								<c:if test="${QX.FX_QX==1}">
									<c:if test="${pd.state eq 12}">
									<a class="btn btn-warning btn-mini" style="margin-bottom: 10px" onclick="examine12('确定要审核选中的数据吗?');" title="审核12">
									<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
									</a>
									</c:if>
								</c:if>
							<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
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
					</tr>
				</thead>
										
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="purchaseOrder" varStatus="vs">
							<tr>

								<td class='center' style="width: 30px;">
									<label><input
										type='checkbox' name='ids' value="${purchaseOrder.id}" /><span
										class="lbl"></span></label> <label style="display: none"><input
										name='checked_state' value="${purchaseOrder.checked_state}" /><span
										class="lbl"></span></label></td>


								<td class='center' style="width: 30px;">${purchaseOrder.id}</td>
								<td><a onclick="findorderitem('${purchaseOrder.order_num }');">${purchaseOrder.order_num }</a></td>
								<td>${purchaseOrder.comment}</td>
								<td>${purchaseOrder.order_date}</td>
								<td>${purchaseOrder.deliver_date}</td>
								<td>${purchaseOrder.manager_name}</td>
								<td>${purchaseOrder.manager_tel}</td>
								<td>
									<c:if test="${purchaseOrder.checked_state==12}">未审核</c:if>
									<c:if test="${purchaseOrder.checked_state==1}">地市已审核</c:if>
									<c:if test="${purchaseOrder.checked_state==2}">省级已审核</c:if>
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
	 	<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;">
					
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
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		function findorderitem(ordernum){
		top.mainFrame.tabAddHandler("392060","销售订单详情","<%=basePath%>sellingorder/sellingorderitemdq.do?order_num="+ordernum);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
			location.href = '<%=basePath%>sellingorder/sellingorderitemdq.do?order_num='+ordernum;
		}
		//导出excel
		function toExcel(){
			var keyword = $("#nav-search-input").val();
			var searchcriteria = $("#searchcriteria").val();
			var lastLoginStart = $("#lastLoginStart").val();
			var lastLoginEnd = $("#lastLoginEnd").val();
			var state= $("#state").val();
			var type= $("#type").val();
            var keyworda=keyword;
			window.location.href='<%=basePath%>sellingorder/dqexcel.do?keyword='+keyworda+'&searchcriteria='+searchcriteria+'&lastLoginEnd='
			+lastLoginEnd+'&lastLoginStart='+lastLoginStart+'&state='+state+'&type='+type;
		}
		
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
		
		//检索
		function search(){
			top.jzts();
			$("#SellingOrderForm").submit();
		}

		//审核操作
		function examine12(msg){

			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{

						if(document.getElementsByName('ids')[i].checked){
							if(str=='') str += document.getElementsByName('ids')[i].value;
							else str += ',' + document.getElementsByName('ids')[i].value;

							if(document.getElementsByName("checked_state")[i].value!=12){
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
								url: '<%=basePath%>sellingorder/examine12.do?tm='+new Date().getTime(),
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
		}
		
		</script>
	</body>
</html>

