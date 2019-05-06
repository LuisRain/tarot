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
	
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="product/findInventoryOfProduct.do" method="post" id="ProductForm">
			<input type="hidden" name="warehouse_id" value="${pd.warehouse_id }">
			<input type="hidden" name="ck_id" value="${pd.ck_id }">
			
			<table>
				<tr>
				<td>
			<select id="searchcriteria" name="searchcriteria"> 
			 <option value="4" >1-按商品条形码</option> 
			 <option value ="2"  >2-按商品编码</option> 
              <option value ="1"  >3-按商品名称</option>  
              <option value="3" >4-按商品类型</option>  
              <option value="5" >5-按商品负责人</option> 
              <option value="6" >6-按商品单位</option> 
               </select>  
				</td>
				<td  style="vertical-align:top;">
				<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
				</td>
					<td style="vertical-align:top;">
					<c:if test="${QX.cha == 1 }">
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
						</c:if>
					</td>
					<td style="vertical-align:top;">
					<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出商品库存到EXCEL">
					<i id="nav-search-icon" class="icon-download-alt"></i></a>
					</td>
				</tr>
			</table>
		
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>商品条形码</th>
						<th>商品名称</th>
						<th>货位</th>
						<th>商品类别</th>
						<th>总库存</th>
						<th>单位</th>
						<th class="center">操作</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty productList}">
						<c:forEach items="${productList}" var="product" varStatus="vs">
							<tr>
								<td class='center' style="width: 30px;">${product.id}
								   <input type='text'   style="display: none;"  value="${product.cargo_space_id}" id="csid${product.id}" />
								</td>
								<td>${product.bar_code }</td>
							
								<td><a>${product.product_name}</a></td>
								<td>${product.zone}区${product.storey}层${product.storey_num}号</td>
								<td>${product.classify_name}</td>
								<td>${product.product_quantity+product.product_quantity2}</td>
								<td>${product.unit_name}</td>
							
								<td>
								<c:if test="${pd.warehouse_id==1 }">
									 <a class="btn btn-mini btn-info" onclick="siMenu('product10000','proudct10000','周转库存操作记录','<%=basePath%>productinventroy/getProductInventroyinfo.do?productid=${product.id}&ckzt=5&warehouse_id=1')" title="周转库存操作记录"><i  class="icon-search"></i></a>
								</c:if>
								<c:if test="${pd.warehouse_id==2 }">
									<a class="btn btn-mini btn-info" onclick="siMenu('product100001','proudct100001','次品仓库存操作记录','<%=basePath%>productinventroy/getProductInventroyinfo.do?productid=${product.id}&ckzt=2&warehouse_id=2')" title="次品仓库存操作记录"><i  class="icon-search"></i></a>
								</c:if>
								<c:if test="${pd.warehouse_id==3 }">
									<a class="btn btn-mini btn-info" onclick="siMenu('product100002','proudct100002','储存库存操作记录','<%=basePath%>productinventroy/getProductInventroyinfo.do?productid=${product.id}&ckzt=2&warehouse_id=3')" title="储存库存操作记录"><i  class="icon-search"></i></a>
								</c:if>
								<c:if test="${pd.warehouse_id==4 }">
									<a class="btn btn-mini btn-info" onclick="siMenu('product100002','proudct100002','赠品库存操作记录','<%=basePath%>productinventroy/getProductInventroyinfo.do?productid=${product.id}&ckzt=2&warehouse_id=4')" title="赠品库存操作记录"><i  class="icon-search"></i></a>
								</c:if>
								 <%--  						
								   	 --%>											
								</td>
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
		<script type="text/javascript" src="static/js/myjs/head.js" ></script>
		<script type="text/javascript">
		  $("#searchcriteria").val("${pd.searchcriteria}");
		$(top.hangge());
		
		$("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
//删除
		function del(Id,csid){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>product/deleteProduct.do?id="+Id+"&csid="+csid+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		
			function getParameter(ids){
			   var list=new Array();
					var str = '';
				    var csid ='';
					var emstr = '';
					var phones = '';
					for(var i=0;i < document.getElementsByName(ids).length;i++)
					{
						  if(document.getElementsByName(ids)[i].checked){
						  	if(str==''){
						  	str += document.getElementsByName(ids)[i].value;
						  	csid+=$("#csid"+document.getElementsByName(ids)[i].value).val();
						  	}
						  	else {
						  	str += ',' + document.getElementsByName(ids)[i].value;
						  	csid+= ','+$("#csid"+document.getElementsByName(ids)[i].value).val();
						  	}
						  	
						  	if(emstr=='') emstr += document.getElementsByName(ids)[i].id;
						  	else emstr += ';' + document.getElementsByName(ids)[i].id;
						  	
						  	if(phones=='') phones += document.getElementsByName(ids)[i].alt;
						  	else phones += ';' + document.getElementsByName(ids)[i].alt;
						  }
					}
			list.splice(0,0,str);	
			list.splice(1,0,csid);
			return list;
		}
		
		
		
			//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
				var result=getParameter("ids");
				var str=result[0];
				var csid=result[1];
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
								url: '<%=basePath%>product/deleteAllProduct.do?tm='+new Date().getTime(),
						    	data: {SUPPLE_IDS:str,csid:csid},
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
		
		
	
		
		
		
		//检索
		function search(){
			top.jzts();
			$("#ProductForm").submit();
		}
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>product/goAddProduct.do';
			 diag.Width = 550;
			 diag.Height = 415;
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
		
		//导出excel
		function toExcel(){
			var keyword = $("#nav-search-input").val();
			var searchcriteria = $("#searchcriteria").val();
			var ckid=$("input[name='ck_id']").val();
			var warehouseid=$("input[name='warehouse_id']").val();
            var keyworda=encodeURI(encodeURI(keyword));
			window.location.href='<%=basePath%>product/excel.do?ck_id='+ckid+'&warehouse_id='+warehouseid+'&keyword='+keyworda+'&searchcriteria='+searchcriteria;
		}
		
		</script>
	</body>
</html>

