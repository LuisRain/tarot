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
			var type=	 $("input[name='type']:checked").val();
			$.ajaxFileUpload({
				url : "enwarehouseorder/readExcel.do?type="+type,            
				fileElementId:'excel',
				async : false,
				dataType:"text",
			 	success : function(data) {
			 		if(data="true"){
			 			openExcelTable(type);
			 		}
			 		
			 	   
			 	}
			}); 
				
			
				
			}
		
			function openExcelTable(type){
				 var diagc = new top.Dialog();
				 diagc.Drag=true;
				 diagc.Title ="新增";
				 diagc.URL = '<%=basePath%>enwarehouseorder/openExcel.do';
				 diagc.Width = 800;
				 diagc.Height = 800;
				 diagc.CancelEvent = function(){ //关闭事件
					 diagc.close();
				 };
				 diagc.OKEvent = function(){
				 		$.post("<%=basePath%>importentempwarehouseorder/importTempOrder.do",{inventoryType:type},function(data){
					   alert("保存");
					
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
	<form action="enwarehouseorder/readExcel.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
		<div id="zhongxin1">
		<table style="width:95%;" >
			<tr>
			<td style="text-align: center;">
			<label>
			入库
		    <input type="radio" name="type"  id="1"  value="1" />
			<span class="lbl"></span>
			</label>
			</td>
			<td style="text-align: center;">
			
			<label>
			出库
		    <input type="radio" name="type"  id="0" value="0" />
			<span class="lbl"></span>
			</label>
			</td>
			</tr>
			<tr>
				<td  colspan=2 style="padding-top: 20px;" style="text-align: center;"><input type="file" id="excel" name="excel" style="width:50px;" onchange="fileType(this)" /></td>
				<td></td>
			</tr>
			<tr>
				<td style="text-align: center;">
					<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
				</td>	
				<td style="text-align: center;">
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
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
			
			var type='${type}';
		      if(type=="1"){
		    	  $("#1").attr("checked","checked");
		      }else if(type=="0"){
		    	  $("#0").attr("checked","checked");
		      }
		});
		
		</script>
	
</body>
</html>