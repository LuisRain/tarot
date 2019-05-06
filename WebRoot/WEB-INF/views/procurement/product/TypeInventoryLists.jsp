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
		<meta charset="utf-8" />
		<title></title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<!-- jsp文件头和头部 -->
<%@ include file="../../system/admin/top.jsp"%>	
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/font-awesome.min.css" />
		<!-- 下拉框 -->
		<link rel="stylesheet" href="static/css/chosen.css" />
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	</head>
<body>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="width: 100px">商品名称</td>
			<!-- 	<td style="width: 100px">类型</td> -->
				<td style="width: 100px">可用库存数</td>
				<td style="width: 100px">生产天数</td>
				<td style="width: 100px">距离过期天数</td>
				<!-- <td style="width: 100px">类型保质期(天)</td> -->
				<td style="width: 100px">保质期(天)</td>
				<td style="width: 100px">生产日期</td>
				<!-- <td style="width: 100px">移库到次品库(移出到正品库)数量</td>
			    <td style="width:150px;">操作</td> -->
			</tr>
			<c:forEach items="${list}" var="typeinventroy">
			<c:if test="${typeinventroy.tpq!=0.0}">
			<tr>
			<td>${typeinventroy.product_name }</td>
			<%-- <td>${typeinventroy.tmtname }</td> --%>
			<td>${typeinventroy.tpq }${typeinventroy.tun}</td>
			<td>${typeinventroy.kulunut }</td>
			<td>${typeinventroy.kysj }</td>	
		<%-- 	<td>${typeinventroy.daysOverdue }</td>	 --%>
			<td>${typeinventroy.expire_days}</td>	
			<td>${typeinventroy.product_date}</td>
			<%-- <td>
			<input type="text" id="${typeinventroy.tpiid}"  value="">
			<input type="text" style="display: none" id="quantity${typeinventroy.tpiid}"  value="${typeinventroy.tpq}">
			</td>
			<td>
				<a style="text-align: center;" onclick="Movement(${typeinventroy.tpiid})" class="btn btn-mini btn-primary">确定移库</a>
			</td> --%>
			</tr></c:if>
			</c:forEach>
		</table>
		<div>
			<span style="padding-left:15px;color:red;">统计：1/2:<c:if test="${staleDated.a eq null}">0</c:if>${staleDated.a}${staleDated.tun}</span>
			<span style="padding-left:15px;color:blue;">1/3:<c:if test="${staleDated.b eq null}">0</c:if>${staleDated.b}${staleDated.tun}</span>
			<span style="padding-left:15px;color:green;">1/4:<c:if test="${staleDated.c eq null}">0</c:if>${staleDated.c}${staleDated.tun}</span>
				<span style="padding-left:15px;color:green;">其他:${staleDated.d-staleDated.a}${staleDated.tun}</span>
		</div>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		//@ sourceURL=msgprompt.js
		var Number=/^[0-9]*$/;//只能输入任意数字
		$(top.hangge());
		function Movement(id){
		var quantity=$("#quantity"+id).val();
			number=$("#"+id).val();
			if($("#"+id).val()==""){
				$("#"+id).tips({
					side:3,
		            msg:'请输数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#"+id).focus();
				return false;
			}
			else if(number.length != 11 && !Number.test(number)){
				$("#"+id).tips({
					side:3,
		            msg:'只能输入数字！',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#"+id).focus();
				return false;
		}
			if(number*1>quantity*1){
				$("#"+id).tips({
					side:3,
		            msg:'不能大于现有库存！',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#"+id).focus();
				return false;
			}
			
			if(number*1==0){
				$("#"+id).tips({
					side:3,
		            msg:'0不能入库',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#"+id).focus();
				return false;
			}
				
				
			var url="<%=basePath%>product/Movement.do";
			 $.post(url,{id:id,number:number},function(data){
				     if(data=="true"){
				    	 alert("已生成次品库入库单。");
				    	 $("#"+id).val("");
				     }
				     else{
				    	 alert("出现故障");
				     }
				          
			 });
		}
		
	
		</script>
</body>
</html>