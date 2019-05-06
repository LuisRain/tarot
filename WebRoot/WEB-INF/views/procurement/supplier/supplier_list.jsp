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
	<script type="text/javascript">
		function toExcelgg(){
            	var keyword = $("#nav-search-input").val();
				var searchcriteria = $("#searchcriteria").val();
           		var keyworda=encodeURI(encodeURI(keyword));
				window.location.href='<%=basePath%>supplier/excel.do?keyword='+keyword+'&searchcriteria='+searchcriteria;
		}
	
	</script>
	</head> 
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">


	<div class="row-fluid">
			<c:if test="${QX.cha == 1 }">
			<!-- 检索  -->
			<form action="<%=basePath%>supplier/list.do" method="post" id="SupplierForm">
			<table>
				<tr>
				<td>
			
			<select id="searchcriteria" name="searchcriteria">  
              <option value ="1"  >1-按供应商名称</option>  
              <option value ="2"  >2-按联系地址</option>  
              <option value="3" >3-按联系人</option>  
              <option value="4" >4-按供应商编号</option>
				<option value="5" >5-按联系人电话</option>
               </select>  
            
				</td>
					<td>
						<span class="input-icon">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
							<button  type="button"class="btn btn-mini btn-light"style="margin-bottom: 10px" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
							<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="toExcelgg();" title="导出供应商信息">
									<i id="nav-search-icon" class="icon-download-alt"></i> </button>
						</span>
					</td>
				</tr>
			</table>
			</c:if>
			<!-- 检索  -->
			
		
			<table id="table_report" class="table table-striped table-bordered table-hover">
				
				<thead>
					<tr>
						<th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="checkAll" /><span class="lbl"></span></label>
						</th>
						<th>序号</th>
						<th>供应商ID</th>
						<th>供应商编号</th>
						<th>名称</th>
						<th>联系地址</th>
						<th>联系人</th>
						<th>联系人手机</th>
						<th>供应商联系电话</th>
						<th class="center">操作</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty supplerList}">
						<c:forEach items="${supplerList}" var="suppler" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					            <label><input type='checkbox' name='ids'  value="${suppler.id}" id="${suppler.id}" /><span class="lbl"></span></label>
					            </td>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td>${suppler.id}</td>
								<td><a onclick="getProductInfoById('${suppler.id}')">${suppler.supplier_num}</a></td>
								<td>${suppler.supplier_name} </td>
								<td>${suppler.address}</td>
								<td>${suppler.contact_person}</td>
								<td>${suppler.contact_person_mobile}</td>	
								<td>${suppler.supplier_tel }</td>
								<td>
								<c:if test="${QX.edit == 1 }">
								<a class='btn btn-mini btn-info' title="编辑" onclick="editSupplier('${suppler.id }');"><i class='icon-edit'></i></a>
							<!--  	 <a class='btn btn-mini btn-danger' title="删除" onclick="delSupplier('${suppler.id}','${suppler.supplier_name}');"><i class='icon-trash'></i></a>-->
								</c:if></td>
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
				<td style="vertical-align:top;">
					<c:if test="${QX.add == 1 }">
					<a class="btn btn-small btn-success" onclick="add();">新增</a>	</c:if>						
				<!--  	<a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a>-->
				
				</td>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
		</div>
		</form>
	</div>
 
 
 
 
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
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
		<script type="text/javascript">
		 $(function(){	
		
		  $("#searchcriteria").val("${pd.searchcriteria}");
		
		 });
		$(top.hangge());
		
		
		function getProductInfoById(id){
			top.mainFrame.tabAddHandler("392060","供应商详情","<%=basePath%>supplier/getProductInfoById.do?supplier_id="+id);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		
		//检索
		function search(){
			top.jzts();
			$("#SupplierForm").submit();
		}
		
		     $("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
		
		
		//单个删除
		function delSupplier(id,msg){
			bootbox.confirm("确定要删除["+msg+"]吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>supplier/deleteSupplier.do?id="+id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					var emstr = '';
					var phones = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
						  	else emstr += ';' + document.getElementsByName('ids')[i].id;
						  	
						  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
						  	else phones += ';' + document.getElementsByName('ids')[i].alt;
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
						
						$("#checkAll").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>supplier/deleteAllSupller.do?tm='+new Date().getTime(),
						    	data: {SUPPLE_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
						
						
					}
				}
			});
		}
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>supplier/goAdd.do';
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
		
		
		//修改
		function editSupplier(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>supplier/goEdit.do?id='+Id;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		</script>
		
	
		
	</body>
</html>

