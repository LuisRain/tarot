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
  <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
  	<style type="text/css">
  	.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		#searchcriteria{width:202px;height:38px}
  		.layui-table th{text-align:center}
</style>
</head>
<body>
<form action="<%=basePath%>planOrder/findplanOrderlistPage.do" method="post" id="SellingOrderForm">
				 <div class="layui-form layui-form-pane"  style="width:100%">
				 <div class="layui-form-item"  style="float: left;" > 
					    <div class="fl">
					    	<select id="searchcriteria" name="searchcriteria" style="height:40px">   
				               <option <c:if test="${pd.searchcriteria==1 }">selected="selected"</c:if> value ="1">1-批次号</option>  
				               <option <c:if test="${pd.searchcriteria==2 }">selected="selected"</c:if> value="2">2-按创建日期</option>
				               <option <c:if test="${pd.searchcriteria==3 }">selected="selected"</c:if> value="3">3-按计划编号</option>
				            </select>
					    </div>
					    <div class="fl">
					     <input type="text"   id="keyword" name="keyword" value="${pd.keyword}" lay-verify="required" placeholder="请输入" class="layui-input">
						 	<input class="span10 date-picker"  name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="最近登录开始"/>
					<input class="span10 date-picker" id="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="最近登录结束"/>
					    </div>
					    <div class="fl">
					    	<select name="type" id="type">
								<option value="">状态过滤----请选择</option>
								<option  <c:if test="${pd.type==0&&!empty pd.type}">selected="selected"</c:if> value ="0" >0-未发布</option>  
		                        <option  <c:if test="${pd.type==1}">selected="selected"</c:if> value ="1">1-未装车</option>  
		                        <option  <c:if test="${pd.type==2 }">selected="selected"</c:if> value ="2">2-运输中</option>  
		                        <option  <c:if test="${pd.type==3 }">selected="selected"</c:if> value ="3">3-已签收</option>  
							</select>
					    </div>
					     <div class="fl">
					    	 <input type="text"   id="short_name" name="short_name" value="${pd.short_name}" lay-verify="required" placeholder="门店简称" class="layui-input">
					    </div>
				      <div class="fl">
						  <button  onclick="search();" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="搜索">搜索</button>
						
						<c:if test="${QX.QX1 == 1 }">
							<button  type="button"  onclick="xsdExcelAll();" class="layui-btn layui-btn-radius layui-btn-small" title="导出">
							<i class="layui-icon" style="font-size: 20px;">&#xe601;</i> </button>
						</c:if>
				      </div>
				    </div>
				  </div>
						<div class="layui-form" >
  						<table class="layui-table"  id="diyige">
							<thead>
								<tr>
									<th class="center">序号</th>
								    <th class="center">批次号</th>
									<th class="center">计划单号</th>
									<th class="center">总重量</th>
									<th class="center">总体积</th>
									<th class="center">总件数</th>
									<th class="center">目的地</th>
									<th class="center">门店总数</th>
									<th class="center">状态</th>
									<th class="center">司机</th>
									<th class="center">电话</th>
									<th class="center">创建时间</th>
									<th class="center">备注</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty varList}">
										<c:forEach items="${varList}" var="var" varStatus="vs">
											<tr>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td class="left">${var.plan_group}</td>
												<td class="left">
													<a onclick="editProductNumber('${var.plan_order}',1)">${var.plan_order}</a>
												</td>
												<td class="center">${var.total_weight}</td>
												<td class="center">${var.total_volume}</td>
												<td class="center">${var.total_number}</td>
												<td class="center">${var.sitename}</td>
												<td class="center">${var.zs}</td>
												<td class="center">
													<c:if test="${var.type==0}">未发布</c:if>
													<c:if test="${var.type==1}">未装车</c:if>
													<c:if test="${var.type==2}">运输中</c:if>
													<c:if test="${var.type==3}">已签收</c:if>
												</td>
												<td>${var.driver_name}</td>
												<td>${var.driver_phone}</td>
												<td class="center">${var.create_time.substring(0,10)}</td>
												<td class="center">${var.comment}</td>
												<td class="center">									
												<button  onclick="deleteplan('${var.plan_order}',${var.type });" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="撤销">撤销</button></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<c:if test="${pd.checked_state eq  1}">
												<td colspan="100" class="center">没有新出库单</td>
											</c:if>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>

	</div>
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
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/layui/layui.js" charset="utf-8"></script>
	<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		//导出excel
		function xsdExcelAll(){
			var keyword = $("#keyword").val();
			var searchcriteria = $("#searchcriteria").val();
			var lastLoginStart = $("#lastLoginStart").val();
			var lastLoginEnd = $("#lastLoginEnd").val();
			var short_name= $("#short_name").val();
			var type= $("#type").val();
            var keyworda=keyword;
			window.location.href='<%=basePath%>planOrder/findAllplanOrderexcel.do?keyword='+keyworda+'&searchcriteria='+searchcriteria+'&lastLoginEnd='
			+lastLoginEnd+'&lastLoginStart='+lastLoginStart+'&short_name='+short_name+'&type='+type;
		}
		
		 $(top.hangge());
		$(function(){
			$('.date-picker').datepicker();
			 if(${pd.lastLoginStart !=null}){
				$("#searchcriteria").val("${pd.searchcriteria}");
				if(${pd.checked_state !=null}){
					$("#checked_state").val("${pd.checked_state}");
				}
				if($("#searchcriteria").val()==2){
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
		}); 
		layui.use('form', function(){
				  var $ = layui.jquery, form = layui.form;
				  form.on('select', function(data){
				  	if(data.elem.id=="searchcriteria"){
				  		if(data.value==2){
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
				  })
				});
		//检索
		function search(){
			 top.jzts();
			$("#SellingOrderForm").submit(); 
		}
		layui.use(['layer', 'form'], function(){ var layer = layui.layer ,form = layui.form;});
			
		function editProductNumber(orderNum){
			var url='<%=basePath%>planOrder/planEdit.do?plan_order='+orderNum;
			top.mainFrame.tabAddHandler("3920608327","查看计划详情信息",url);
		}
		
		function deleteplan(orderNum,type){
			var opl=layer.confirm('确认删除吗？', {
			  btn: ['确定','取消'] //按钮
			}, function(){
				layer.close(opl);
				var index = layer.load(1, {
				  shade: [0.3,'#fff'] //0.1透明度的白色背景
				});
			 var url='<%=basePath%>planOrder/deleteplan.do?plan_order='+orderNum+'&type='+type;
				$.post(url,function(data){
					layer.close(index);
					layer.msg(data,{icon:1,time: 2000},function(){window.location.href=window.location.href;}); 
				})
			});
		}
		</script>

</body>
</html>

