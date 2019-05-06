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
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		<link rel="stylesheet" href="static/assets/css/font-awesome.css" />
		<!-- ace styles -->
		<link rel="stylesheet" href="static/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
			<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>
		<script type="text/javascript">
			
		
		$(function(){
			
			var t= ${type};
			if(t==1){
				 
				  $("#1").attr("checked","checked");
				  $("#0").removeAttr("checked");
			}else{
				
				  $("#0").attr("checked","checked");
				  $("#1").removeAttr("checked");
				
			}
			
		})
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
			var   inventoryType=	$("input[name='type']:checked").val();
				$.ajaxFileUpload({
					url : "importentempwarehouseorder/readTempExcel.do",            
					fileElementId:'excel',
					async : false,
					dataType:"text",
				 	success : function(data) {
				 		if(data="true"){
				 			openExcelTable(inventoryType);
				 		}
				 	}
				}); 
				
			
				
			}
			//@ sourceURL=cccccccccccccccccc.js
			function openExcelTable(inventoryType){
				 
				 var diagc = new top.Dialog();
				 diagc.Drag=true;
				 diagc.Title ="新增";
				 diagc.URL = '<%=basePath%>importentempwarehouseorder/openExcel.do';
				 diagc.Width = 800;
				 diagc.Height = 800;
				
				 diagc.CancelEvent = function(){ //关闭事件
					 diagc.close();
				 };
				 diagc.OKEvent = function(){
				$.post("<%=basePath%>importentempwarehouseorder/importTempOrder.do",{inventoryType:inventoryType},function(data){
				       if(inventoryType==0){
				    	     if(data=="true0"){
				    	    	   alert("临时入库成功");
				    	    	   
				    	    	   
				    	     }else{
				    	    	 
				    	    	 alert("临时入库失败");				    	     }
				       }else{
				    	   
				    	   if(data=="true1"){
			    	    	   alert("临时出库成功");
			    	     }else{
			    	    	 
			    	    	 alert("临时出库失败");	
				    	    
				       }
				       }
				       diagc.close();
				});
				  
				 };
				 diagc.show();
				 diagc.okButton.value="入库";
				 diagc.cancelButton.value="取消";
			}
			
			function fileType(obj){
				var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
			    if(fileType != '.xls'){
			    	$("#excel").tips({
						side:3,
			            msg:'请上传xls格式的文件',
			            bg:'#AE81FF',
			            time:3
			        });
			    	$("#excel").val('');
			    	document.getElementById("excel").files[0] = '请选择xls格式的文件';
			    }
			}
		</script>
	</head>
<body>
	<form action="importentempwarehouseorder/readTempExcel.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
		<div id="zhongxin1" >
		<table style="width:95%;">
	    <tr>
			<td style="text-align: center;">
			<label>
			入库
		    <input type="radio" name="type"  id="0"  value="0"   checked="checked"/>
			<span class="lbl"></span>
			</label>
			</td>
			<td style="text-align: center;">
			<label>
			出库
		    <input type="radio" name="type"  id="1" value="1" />
			<span class="lbl"></span>
			</label>
			</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-top: 20px;"><input type="file" id="excel" name="excel" style="width:50px;" onchange="fileType(this)" /></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
					<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				<!-- <a class="btn btn-mini btn-success" onclick="window.location.href='<%=basePath%>/user/downExcel.do'">下载模版</a>
				</td> -->	
			</tr>
		</table>
		</div>
		
	<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
		
	</form>
	
	
		<!-- 引入 -->
		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='static/assets/js/jquery.js'>"+"<"+"/script>");
		</script>
		<!-- <![endif]-->
		<!--[if IE]>
		<script type="text/javascript">
		 	window.jQuery || document.write("<script src='static/assets/js/jquery1x.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script src="static/js/bootstrap.min.js"></script>
		<!-- ace scripts -->
		<script src="static/assets/js/ace/elements.fileinput.js"></script>
		<script src="static/assets/js/ace/ace.js"></script>
		<!--提示框-->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		$(function() {
			//上传
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
			
		});
		
		</script>
	
</body>
</html>