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

	<style type="text/css">
		.new-row-fluid{margin:15px 0;border:1px solid #f5f5f5;}
		.fl{width:15%;float:left;background:#f5f5f5;text-align:center;padding: 50px 0;}
		.fl1{width:15%;float:left;background:#f5f5f5;text-align:center;padding: 134px 0;}
		.fl2{width:15%;float:left;background:#f5f5f5;text-align:center;padding: 172px 0;}
		.fr{width:84%;float:right;padding-left:1%;margin-top:27px;}
		.fr1{width:84%;float:right;padding-left:1%;}
		.fr2{width:84%;float:right;padding-left:1%;}
		.new-fl{float:left;width:100%;}
		.new-fl1{float:left;width:100%;}
		.new-fl2{float:left;width:100%;}
		.btn-style{vertical-align:top;}
		.div1{margin-left:20px}
		.div2{margin-left: 20px;margin-top: 20px}
	</style>
	<script type="text/javascript">
			//保存
			function save(){
				if($("#excel").val()=="" || document.getElementById("excel").files[0] =='请选择xls格式的文件'){
					$("#excel").tips({
						side:3,
			            msg:'请选择文件',
			            bg:'#AE81FF',
			            time:3
			        });
					return false; 
				}
				$("#sellingOrderForm").submit();
				$("#zhongxin").hide();
				$("#msgdiv").hide();
				$("#zhongxin2").show();
			}
			//出库单导入
			function save2(){
				if($("#excel2").val()=="" || document.getElementById("excel2").files[0] =='请选择xls格式的文件'){
					$("#excel2").tips({
						side:3,
			            msg:'请选择文件',
			            bg:'#AE81FF',
			            time:3
			        });
					return false; 
				}
				$("#exOrderForm").submit();
				$("#zhongxin3").hide();
				$("#msgdiv2").hide();
				$("#zhongxin4").show();
			}
		
		</script>
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<div class="row-fluid new-row-fluid">
				<div class="fl1">
					1-销售排行导入
				</div>
				<div class="fr1">
					<div class="new-fl1">
						<form action="sellingorder/importExcelOfSellingOrder.do" name="sellingOrderForm" id="sellingOrderForm" method="post" enctype="multipart/form-data">
							<div id="zhongxin">
							<table style="width:95%;" >
								<tr>
									<td style="padding-top: 20px;">
									
									<input type="file" id="excel" name="excel" style="width:50px;" />
									
									</td>
								</tr>
								<tr>
									<td style="text-align: center;">
										<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
										<a class="btn btn-mini btn-success" href="uploadFiles/file/scxl.xlsx">下载销售排行模版</a>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="text-align: left;">
										<span style="font-size: 16px;">&nbsp;<b>提示：Excel文档信息必须符合如下格式规范:<input type="text" style="width:16px;height:2px;background-color:red">必须有<input type="text" style="width:16px;height:2px;background-color:yellow">可有可无</b></span></br>
										<img src="static/images/scxl.png"/>
									</td>
								</tr>
							</table>
							</div>
							
							<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
							
						</form>
						<div id="msgdiv" class="step-content row-fluid position-relative">
							<ul class="unstyled spaced2">
								<c:choose>
									<c:when test="${not empty errorMsg2}">
										<li class="text-warning orange"><i class="icon-warning-sign"></i>
											数据导入返回信息： ${errorMsg2}
										</li>
								</c:when>
								</c:choose>				
							</ul>
							<ul class="unstyled spaced2">
							<c:choose>
								<c:when test="${not empty varList}">
									<c:forEach items="${varList}" var="pdE" varStatus="vs">
										<li class="text-warning red"><i class="icon-warning-sign"></i>
											${vs.count}.错误行：${pdE.line}；列：${pdE.row}错误原因：${pdE.reason} 
										</li>
									</c:forEach>
								</c:when>
								</c:choose>				
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid new-row-fluid">
				<div class="fl1">
					2-配发出库单导入
				</div>
				<div class="fr1">
					<div class="new-fl1">
						<form action="pfackdcontroller/importExcelOfEXOrder.do" name="exOrderForm" id="exOrderForm" method="post" enctype="multipart/form-data">
							<div id="zhongxin3">
							<table style="width:95%;" >
								<tr>
									<td style="padding-top: 20px;">
									
									<input type="file" id="excel2" name="excel2" style="width:50px;" />
									
									</td>
								</tr>
								<tr>
									<td style="text-align: center;">
										<a class="btn btn-mini btn-primary" onclick="save2();">导入</a>
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
										<a class="btn btn-mini btn-success" href="uploadFiles/file/ckd.xlsx">下载配发出库单模版</a>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="text-align: left;">
										<span style="font-size: 16px;">&nbsp;<b>提示：Excel文档信息必须符合如下格式规范:<input type="text" style="width:16px;height:2px;background-color:red">必须有<input type="text" style="width:16px;height:2px;background-color:yellow">可有可无</b></span></br>
										<img src="static/images/psckd.png"/>
									</td>
								</tr>
							</table>
							</div>
							
							<div id="zhongxin4" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
							
						</form>
						<div id="msgdiv2" class="step-content row-fluid position-relative">
							<ul class="unstyled spaced2">
								<c:choose>
									<c:when test="${not empty Msg}">
										<li class="text-warning orange"><i class="icon-warning-sign"></i>
											数据导入返回信息： ${Msg}
										</li>
								</c:when>
								</c:choose>				
							</ul>
							<ul class="unstyled spaced2">
							<c:choose>
								<c:when test="${not empty varList2}">
									<c:forEach items="${varList2}" var="pdE" varStatus="vs">
										<li class="text-warning red"><i class="icon-warning-sign"></i>
											${vs.count}.错误行：${pdE.line}；列：${pdE.row0}错误原因：${pdE.reason0} 
										</li>
									</c:forEach>
								</c:when>
								</c:choose>				
							</ul>
						</div>
					</div>
				</div>
			</div>
	</div>
	
  </div>
	
</div><!--/#page-content-->

		
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
		<script type="text/javascript" src="static/js/myjs/head.js" ></script>
		<script type="text/javascript">
		
	
		$(top.hangge());
			$(function() {
			$('#excel').ace_file_input({
				no_file:'请选择EXCEL ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'xls|xls',
				blacklist:'gif|png|jpg|jpeg'
				//onchange:''
				//
			});
			$('#excel2').ace_file_input({
				no_file:'请选择EXCEL ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'xls|xls',
				blacklist:'gif|png|jpg|jpeg'
				//onchange:''
				//
			});
		});
		</script>
	</body>
</html>

