<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
			<c:if test="${QX.cha== 1 }"> 
			<form action="<%=basePath%>thproduct/commodityProduct.do" method="post" id="SellingOrderForm">
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-按门店名称</option>  
              <option value ="2"  >2-按创建日期</option>  
              <option value="3" >3-按订单编号</option>  
               </select>  
				</td>
					<td>
						<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
							<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
							<select name="checked_state" id="checked_state">
												<option value="">状态过滤----请选择</option>
												<option value="1" <c:if test="${pd.checked_state == 1 }">selected="selected"</c:if>>1-新订单</option>
												<option value="3" <c:if test="${pd.checked_state == 3 }">selected="selected"</c:if>>2-已完成</option> 
							</select>
							<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();" title="检索">
							<i id="nav-search-icon" class="icon-search"></i></button>
							<c:if test="${QX.FX_QX==1  }">
								<c:if test="${pd.checked_state eq  1}">
									<a class="btn btn-warning btn-mini" style="margin-bottom: 10px" onclick="makeAll1('确定要审核选中的数据吗?');" title="批量审核"> 
									<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i></a>
								</c:if>
							</c:if>
							<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="toexcel();" title="导出到EXCEL">
							<i id="nav-search-icon" class="icon-download-alt"></i></button>
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
						<th>创建日期</th>
						<th>联系人</th>
						<th>联系电话</th>
						<th>门店</th>
						<th>订单状态</th>
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
					            </td>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td><a onclick="editProductNumber('${purchaseOrder.order_num}')">${purchaseOrder.order_num }</a></td>
								<td>
									${fn:substring(purchaseOrder.create_time,0,10)}
								</td>
								<td>${purchaseOrder.manager_name}</td>
								<td>${purchaseOrder.manager_tel}</td>
								<td>${purchaseOrder.merchant_name}</td>
								<td>
									<c:if test="${purchaseOrder.checked_state==1}">新订单</c:if>
									<c:if test="${purchaseOrder.checked_state==2}">已审核未入库</c:if>
									<c:if test="${purchaseOrder.checked_state==3}">已审核已入库</c:if>
								</td>
								<td>${purchaseOrder.comment}</td>	
							<%-- 	<td>
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
  </div>
</div>

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
		function makeAll1(){
			bootbox.confirm("确定要审核吗？", function(result) {
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
								url: '<%=basePath%>thproduct/reviewedAll.do?tm='+new Date().getTime(),
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
			 //修改商品数量
		  function editProductNumber(id1){
			var url='<%=basePath%>thproduct/commodityProductItem.do?order_num='+id1;
				top.mainFrame.tabAddHandler("392060853227","查看订单信息",url);
			 
		 } 
		 //导出到Excel
		 function toexcel(){
		 	var keyword = $("#nav-search-input").val();
			var searchcriteria = $("#searchcriteria").val();
			var checked_state=$("#checked_state").val();
			var lastLoginStart = $("#lastLoginStart").val();
			var lastLoginEnd=$("#lastLoginEnd").val();
           	var keyworda=encodeURI(encodeURI(keyword));
			window.location.href='<%=basePath%>thproduct/exorderlistexcel.do?keyword='+keyword+'&searchcriteria='+searchcriteria+
			'&lastLoginStart='+lastLoginStart+'&lastLoginEnd='+lastLoginEnd+'&checked_state='+checked_state;
		 }
		</script>
	</body>
</html>

