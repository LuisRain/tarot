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
	
	//保存
	function save(){
	if($("#SUPPLIER_NUM").val()==""){
			$("#SUPPLIER_NUM").tips({
				side:3,
	            msg:'请输入供应商编码',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#SUPPLIER_NUM").focus();
			return false;
		}
	
			if($("#SUPPLIER_NAME").val()==""){
			$("#SUPPLIER_NAME").tips({
				side:3,
	            msg:'请输入供货商名称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#SUPPLIER_NAME").focus();
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
		if($("#CONTACT_PERSON_MOBILE").val()==""){
			$("#CONTACT_PERSON_MOBILE").tips({
				side:3,
	            msg:'请输入联系人手机',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CONTACT_PERSON_MOBILE").focus();
			return false;
		}
		else if($("#CONTACT_PERSON_MOBILE").val().length != 11 && !shouji.test($("#CONTACT_PERSON_MOBILE").val())){
			$("#CONTACT_PERSON_MOBILE").tips({
				side:3,
	            msg:'手机号格式不正确',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#CONTACT_PERSON_MOBILE").focus();
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
		if($("#REMARKS").val()==""){
			$("#REMARKS").tips({
				side:3,
	            msg:'请输入备注',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#REMARKS").focus();
			return false;
		}
		/* if($("#user_name").val()==""){
			$("#user_name").tips({
				side:3,
				msg:'请输入用户名',
				bg:'#AE81FF',
				time:2
			});
			$("#user_name").focus();
			return false;
		} */
		
		if($("#SUPPLIER_TEL").val()==""){
			$("#SUPPLIER_TEL").tips({
				side:3,
	            msg:'请输入供应商联系电话',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#SUPPLIER_TEL").focus();
			return false;
		}else if($("#SUPPLIER_TEL").val().length != 11 && !dianhua.test($("#SUPPLIER_TEL").val())){
			$("#SUPPLIER_TEL").tips({
				side:3,
	            msg:'手机号格式不正确',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#SUPPLIER_TEL").focus();
			return false;
		}
		
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="supplier/${msg }.do" name="Form" id="Form" method="post">
	<input type="hidden" name="id" id="id" value="${pd.id}"/>
		<div id="zhongxin">
		<table id="table_report" class="table table-striped table-bordered table-hover">
		<%-- <tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">供货商ID:</td>
				<td>
				<c:if test="${pd.id ==null }">
			<!-- 	 <input type="text"  id="SUPPLIER_ID" name="SUPPLIER_ID" placeholder="这里输入供货商ID" title="供货商ID" maxlength="32"  /> -->
				</c:if>
				<c:if test="${pd.id !=null}">
				<!--  <input type="text"  value="${pd.SUPPLIER_NUM}" maxlength="32"  disabled="disabled" title="供货商标号"/>-->
				
				</c:if>
				</td>
			</tr> --%>
		<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">供货商编码:</td>
				<td>
				<c:if test="${pd.SUPPLIER_NUM ==null }">
				<input type="text"   maxlength="32" name="SUPPLIER_NUM" id="SUPPLIER_NUM"  title="供货商编号"/>
				</c:if>
				<c:if test="${pd.SUPPLIER_NUM !=null}">
				<input type="hidden"  id="SUPPLIER_ID" name="SUPPLIER_ID" value="${pd.id}"  disabled="disabled" title="供货商ID" maxlength="32"  />
				<input type="hidden"  value="${pd.SUPPLIER_NUM}" id="SUPPLIER_NUM" name="SUPPLIER_NUM" maxlength="32"/>
				<input type="text"  value="${pd.SUPPLIER_NUM}" maxlength="32"  disabled="disabled" title="供货商编号"/>
				</c:if>
				</td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">供货商名称:</td>
				<td><input type="text" name="SUPPLIER_NAME" id="SUPPLIER_NAME" value="${pd.SUPPLIER_NAME}" maxlength="32" placeholder="这里输入供货商名称" title="供货商名称"/></td>
			</tr>
			<%-- <tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">用户名:</td>
				<td><input type="text" name="user_name" id="user_name" value="${pdd.USERNAME}" maxlength="32" placeholder="这里输入用户名" title="用户名"/></td>
			</tr> --%>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人:</td>
				<td><input type="text" name="CONTACT_PERSON" id="CONTACT_PERSON" value="${pd.CONTACT_PERSON}" maxlength="32" placeholder="这里输入联系人" title="联系人"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系人手机:</td>
				<td>
				<input type="hidden" name="oldphone" value="${pd.CONTACT_PERSON_MOBILE}">
				<input type="text" name="CONTACT_PERSON_MOBILE" id="CONTACT_PERSON_MOBILE" value="${pd.CONTACT_PERSON_MOBILE}" maxlength="32" placeholder="这里输入联系人电话" title="联系人电话"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">联系地址:</td>
				<td><input type="text" name="ADDRESS" id="ADDRESS" value="${pd.ADDRESS}" maxlength="32" placeholder="这里输入供应商地址" title="供应商地址"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
				<td><input type="text" name="REMARKS" id="REMARKS" value="${pd.REMARKS}" maxlength="32" placeholder="这里输入备注" title="备注"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">供应商联系电话:</td>
				<td><input type="text" name="SUPPLIER_TEL" id="SUPPLIER_TEL" value="${pd.SUPPLIER_TEL}" maxlength="32" placeholder="这里输入供应商联系电话" title="供应商联系电话"/></td>
			</tr>
			<tr>
				<td style="width:70px;text-align: right;padding-top: 13px;">是否压期到货:</td>
				<td>
					<select name="timearrival">
						<option <c:if test="${pd.timearrival==0}">selected="selected"</c:if> value="0">否</option>
						<option <c:if test="${pd.timearrival==1}">selected="selected"</c:if> value="1">是</option>
					</select>
				</td>
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