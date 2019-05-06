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
		
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	var shouji = /^(((13[0-9]{1})|159)+\d{8})$/; //手机号验证
	var dianhua =/^0\d{2,3}-?\d{7,8}$/;//电话号码验证
	var number=/^[0-9]*$/;//只能输入任意数字
	 
	

	//保存
	function save(){
		if($("#merchant_num").val()==""){
			$("#merchant_num").tips({
				side:3,
	            msg:'请输入商户编码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#merchant_num").focus();
			return false;
		}
		/* else if($("#id").val().length != 11 && !number.test($("#id").val())){
			$("#id").tips({
				side:3,
	            msg:'商户编码不正确只能为数字！',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#id").focus();
			return false;
		} */
		
		
		
			if($("#MERCHANT_NAME").val()==""){
			$("#MERCHANT_NAME").tips({
				side:3,
	            msg:'请输入供货商名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#MERCHANT_NAME").focus();
			return false;
		}
		if($("#short_name").val()==""){
			$("#short_name").tips({
				side:3,
	            msg:'请输入商户简称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#short_name").focus();
			return false;
		}
		
		if($("#CONTACT_PERSON").val()==""){
			$("#CONTACT_PERSON").tips({
				side:3,
	            msg:'请输入联系人',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CONTACT_PERSON").focus();
			return false;
		}
		if($("#MOBILE").val()==""){
			$("#MOBILE").tips({
				side:3,
	            msg:'请输入联系人手机',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#MOBILE").focus();
			return false;
		}
		else if($("#MOBILE").val().length != 11 && !shouji.test($("#MOBILE").val())){
			$("#MOBILE").tips({
				side:3,
	            msg:'手机号格式不正确',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#MOBILE").focus();
			return false;
		}
		
		
		if($("#ADDRESS").val()==""){
			$("#ADDRESS").tips({
				side:3,
	            msg:'请输入供应商地址',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ADDRESS").focus();
			return false;
		}
		
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="merchant/${msg}.do" name="Form" id="Form" method="post">
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
		<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">商户编号:</td>
				<td>
				<input type="hidden"  value="${pd.id}" id="oldId" name="id" />
				<input type="text"  value="${pd.merchant_num}" maxlength="32"  id="merchant_num" name="merchant_num" <c:if test="${not empty pd.merchant_num }">disabled="disabled"</c:if>  placeholder="这里输入商户编号" title="商户编号"/>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">商户名称:</td>
				<td><input type="text" name="merchant_name" id="MERCHANT_NAME" value="${pd.merchant_name}" maxlength="32" placeholder="这里输入商户名称" title="商户名称"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">商户简称:</td>
				<td><input type="text" name="short_name" id="short_name" value="${pd.short_name}" maxlength="32" placeholder="这里输入商户简称" title="商户简称"/></td>
			</tr>
			
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人:</td>
				<td><input type="text" name="contact_person" id="CONTACT_PERSON" value="${pd.contact_person}" maxlength="32" placeholder="这里输入联系人" title="联系人"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人手机:</td>
				<td><input type="text" name="mobile" id="MOBILE" value="${pd.mobile}" maxlength="32" placeholder="这里输入联系人手机" title="联系人手机"/></td>
			</tr>
			
			 <Tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">联系人电话:</td>
				<td><input type="text" name="phone" id="phone" value="${pd.phone}" maxlength="32" placeholder="这里输入联系人电话" title="联系人电话"/></td>
			 
			 </Tr>
			 <tr>
			<td style="width:70px;text-align: right;padding-top: 13px;">地区:</td>
			 <td>
			 
			   
			   <select   name="city" id="city"> 
			        
			         <c:forEach items="${area}" var="a">
			         
			          <c:if test="${pd.city== a.id}">
			          
			            <option value="${a.id}" selected="selected">${a.area_name}</option>
			          
			          </c:if>
			          <c:if test="${pd.city != a.id}">
			          
			            <option value="${a.id}">${a.area_name}</option>
			          
			          </c:if>
			         
			        
			          </c:forEach>
			   
			   </select>
			  </td>
			 </tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系地址:</td>
				<td><input type="text" name="address" id="ADDRESS" value="${pd.address}" maxlength="32" placeholder="这里输入客户地址" title="客户地址"/></td>
			</tr>
				<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">网址:</td>
				<td><input type="text" name="website"   value="${pd.website}" maxlength="32" placeholder="这里输入客户网址" title="客户网址"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
				<td><input type="text" name="remarks" id="REMARKS" value="${pd.remarks}" maxlength="32" placeholder="这里输入备注" title="备注"/></td>
			</tr>
			<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
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
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>