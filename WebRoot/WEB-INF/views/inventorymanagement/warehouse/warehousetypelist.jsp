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
	<base href="<%=basePath%>"><!-- jsp文件头和头部 -->
	<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
	<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
	</head>
<body>
<form action="" style="width:100%">
	<div style="width:20%;height:100%;float: left;">
		<ul id="demo" ></ul>
	</div>
	<div style="padding-top: 20px;width:80%;padding-left:20%">
		上级仓库：
		<input type="text" id="parent_name" disabled="disabled"><br/>
		仓库名称：
		<input type="text" id="cang" disabled="disabled">
		
		<input type="hidden"  name="cid" id="cid" >
		仓库级别：
		<input type="text" id="lever" disabled="disabled"><br/>
		调整上级仓库：
		 <select name="quiz" id="quiz">
        </select>
        <br/>
        <div style="padding-left:130px;padding-top:10px" >
         <button  onclick="add();" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="新增">新增</button>
		 <button  onclick="update();" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="修改">修改</button>
	</div></div>	
</form>
<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
<script src="static/layui/layui.js" charset="utf-8"></script>
		<script type="text/javascript">
		$(top.hangge());
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>warehouse/gotypeAdd.do';
			 diag.Width = 450;
			 diag.Height = 355;
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
		
		function update(){
			var id=$("#cid").val();
			var parent_id=$("#quiz").val();
			$.ajax({
				type: "POST",
				url: '<%=basePath%>warehouse/updatewarehousetypelist.do?id='+id+'&parent_id='+parent_id,
				dataType:'json',
				cache: false,
				success: function(data){
					if(data==false){
						layer.msg("修改错误！");
					}else{
						window.location.reload();
					}
				}
			});
			
		}
		
		
		var _html=new Array();
			$(function() {
			$.ajax({
				type: "POST",
				url: '<%=basePath%>warehouse/warehousetypelist.do',
				dataType:'json',
				cache: false,
				success: function(data){
					layui.use(['tree', 'layer'], function(){
					    var layer = layui.layer
					    ,$ = layui.jquery; 
						layui.tree({
						  elem: '#demo' //传入元素选择器
						  ,nodes:[data.data]
						  ,click: function(node){
						  	$("#parent_name").val(node.parent_name);
						  	$("#cang").val(node.name);
						  	$("#cid").val(node.id);
						  	$("#lever").val(node.level_id+"级仓库"); //node即为当前点击的节点数据
						    sel(data.li,parseInt(node.level_id)-2);
						  }  
						});
					});
					
				}
			});
		})
		function sel(li,lever){
			var _html="";
			
			if(lever<0){
				lever=0;
			}
			$("#quiz option").remove();
			$("#quiz optgroup").remove();
			for(var i=0;i<li.length;i++){
				var va=li[i];
				if(va.level_id==lever){
					var _lev="";
					for(var j=0;j<li.length;j++){
						if(li[j].parent_id==va.id){
							_lev+="<option value='"+li[j].id+"'>"+li[j].name+"</option>";
						}
					}
					if(_lev!=""){
						_html+="<optgroup label='"+va.name+"'>";
						_html+=_lev;
					}
				}
				_html+="</optgroup>";
			}
			$("#quiz").append(_html);
		}
		</script>
		
	</body>
</html>

