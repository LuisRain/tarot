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
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/font-awesome.min.css" />
		<!-- 下拉框 -->
		<link rel="stylesheet" href="static/css/chosen.css" />
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<!--提示框-->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	$(top.hangge());
	
	//保存
	function save(){
		if($("#product_name").val()==""){
			$("#product_name").tips({
				side:3,
	            msg:'选择活动商品',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#product_name").focus();
			return false;
		}
		if($("#quantity").val()==""){
			$("#quantity").tips({
				side:3,
	            msg:'达标数量不能为空',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#quantity").focus();
			return false;
		}
		if($("#aproductName").val()==""){
			$("#aproductName").tips({
				side:3,
	            msg:'请选择赠送商品',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#aproductName").focus();
			return false;
		}
		if($("#final_quantity").val()==""){
			$("#final_quantity").tips({
				side:3,
	            msg:'赠品数量不能为空',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#final_quantity").focus();
			return false;
		}
		if($("#begintime").val()==""){
			$("#begintime").tips({
				side:3,
	            msg:'请选择开始日期',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#begintime").focus();
			return false;
		}
		if($("#endtime").val()==""){
			$("#endtime").tips({
				side:3,
	            msg:'请选择结束日期',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#endtime").focus();
			return false;
		}
		if($("#stagenumber").val()==""){
			$("#stagenumber").tips({
				side:3,
	            msg:'请选择期数',
	            bg:'#AE81FF',
	            time:2
	        });
			
			$("#stagenumber").focus();
			return false;
		}
		
		$("#userForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
	
</script>
	
	</head>
<body>
	<form action="<%=basePath%>product/saveActivityproduct.do" name="userForm" id="userForm" method="post">
		<div id="zhongxin">
			<input type="text" name="id" value="${pd.id}"  style="display: none"> 
			<input type="text" name="type" id="type" value="${pd.type}"  style="display: none"> 
		<table>
			<tr class="info">
			 <%-- <td>
			   <select  name="stagenumber" id="stagenumber" style="overflow:auto;"> 
			         <c:forEach items="${listpd}" var="a">
			          <c:if test="${pd.t_sId== a.id}">
			            <option value="${a.id}" selected="selected">${a.stagenumber}</option>
			          </c:if>
			          <c:if test="${pd.t_sId != a.id}">
			            <option value="${a.id}">${a.stagenumber}</option>
			          </c:if>
			          </c:forEach>
			   </select>
			  </td> --%>
			  </tr>
			<tr>
				<!-- <td class='center'>开始日期:</td> -->
				<td colspan="2"><input class="span10 date-picker" name="begintime" id="begintime" value="${pd.begintime}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:220px;" placeholder="开始日期"/></td>
			</tr>
			<tr>
				<!-- <td class='center'>结束日期:</td> -->
				<td colspan="2"><input class="span10 date-picker" name="endtime" id="endtime" value="${pd.endtime}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:220px;" placeholder="结束日期"/></td>
			</tr>
			<tr>
			<td>
		    	<input type="text" name="product_id" value="${pd.product_id}"  id="product_id" style="display: none"> 
		     	<input name="product_name" id="product_name"  value="${pd.product_name }" type="text" readonly="readonly" placeholder="请选择商品" />
		     	<a class="btn btn-mini btn-light" onclick="seachProduct()" title="选择商品"><i id="nav-search-icon" class="icon-search"> </i> </a>
			</td>
				</tr>
			<tr class="info">
			
				<td>
					<input type="text" name="quantity" value="${pd.quantity }" placeholder="请输入达标数量">
				</td>
			</tr>

			<tr>
				<td>
					<input type="text" name="product_activity" value="${pd.product_activity}" id="product_activity" style="display: none"> 
		     		<input name="aproductName" id="aproductName" value="${pd.aproduct_name }" type="text" readonly="readonly" placeholder="请选择商品" />
		     		<%--<a class="btn btn-mini btn-light" onclick="seachProduct2()" title="选择赠品"><i id="nav-search-icon" class="icon-search"> </i>
					</a>--%>
				</td>
			</tr>
			<%-- 原逻辑,赠品可以选择
			<tr>
				<td>
					<input type="text" name="product_activity" value="${pd.product_activity}" id="product_activity" style="display: none">
		     		<input name="aproductName" id="aproductName" value="${pd.aproduct_name }" type="text" readonly="readonly" placeholder="请选择商品" />
		     		<a class="btn btn-mini btn-light" onclick="seachProduct2()" title="选择赠品"><i id="nav-search-icon" class="icon-search"> </i> </a>
				</td>
			</tr>--%>
			<tr>
				<td>
					<input type="text" name="final_quantity" value="${pd.final_quantity }" placeholder="请输入赠品数量">
				</td>
			</tr>
			<c:if test="${!empty pd.id }">
			<tr>
				<td>
					<label style="float:left;padding-left: 50px;">
						<input name="type-1" id="form-field-radio1" onclick="setType('1');" <c:if test="${pd.type == '1' }">checked="checked"</c:if> type="radio" value="icon-edit">
						<span class="lbl">开启</span>
					</label>
					<label style="float:left;padding-left: 50px;padding-bottom: 20px">
						<input name="type-1" id="form-field-radio1" onclick="setType('2');" <c:if test="${pd.type == '2' }">checked="checked"</c:if> type="radio" value="icon-edit">
						<span class="lbl">关闭</span>
					</label>
				</td>
			</tr>
			</c:if>
			<tr>
				<td style="text-align: center;">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
		
	</form>
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		$(top.hangge());
		$(function(){
		$('.date-picker').datepicker();
		});
		function setType(value){
					$("#type").val(value);
				}
	 //搜索商品
		  function seachProduct(){
		 	 top.jzts();
		 	 var diag = new top.Dialog();
		 	 diag.Drag=true;
		 	 diag.Title ="选择商品";
		 	 diag.URL = '<%=basePath%>product/getActivityproduct.do';
		 	 diag.Width = 700;
		 	 diag.Height = 500;
		 	 diag.CancelEvent = function(){ //关闭事件
		 		diag.close();
		 	 };
		 	 diag.OKEvent = function(){
		 		 var pid=diag.innerFrame.contentWindow.$('input[name="productId"]:checked').val();
		 	                   $("#product_id").val(pid);
		 		    $.post("<%=basePath%>/product/findProductById?productId="+pid,function(data){
		 		    	        productData=data;
		 		    	        $("#product_name").val(data.product.product_name);
		 		    	        //仅能赠送自己
		 		    	        $("#product_activity").val(data.product.id);
		 		    	        $("#aproductName").val(data.product.product_name);
		 		    });
		         diag.close();
		 	 };
		 	 diag.show();
		  }
		    function seachProduct2(){
		 	 top.jzts();
		 	 var diag = new top.Dialog();
		 	 diag.Drag=true;
		 	 diag.Title ="选择赠品";
		 	 diag.URL = '<%=basePath%>product/getActivityproduct.do';
		 	 diag.Width = 700;
		 	 diag.Height = 500;
		 	 diag.CancelEvent = function(){ //关闭事件
		 		diag.close();
		 	 };
		 	 diag.OKEvent = function(){
		 		 var pid=diag.innerFrame.contentWindow.$('input[name="productId"]:checked').val();
		 	                   $("#product_activity").val(pid);
		 		    $.post("<%=basePath%>/product/findProductById?productId="+pid,function(data){
		 		    	        productData=data;
		 		    	        $("#aproductName").val(data.product.product_name);  
		 		    });
		         diag.close();
		 	 };
		 	 diag.show();
		  }
		
		</script>
	
</body>
</html>