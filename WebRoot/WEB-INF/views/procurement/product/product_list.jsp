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
			<form action="product/listProducts.do" method="post" id="ProductForm">
			<c:if test="${QX.cha == 1 }">
			<table>
				<tr><td>
			<select id="searchcriteria" name="searchcriteria">  
			<option value="4" >1-按商品条形码</option>
              <option value ="1"  >2-按商品名称</option>  
              <option value ="2"  >3-按商品编码</option>  
              <option value="3" >4-按商品类型</option>  
              <option value="5" >5-按商品负责人</option> 
              <option value="6" >6-按商品单位</option> 
               </select>  
				</td>
				<td  style="vertical-align:top;">
				<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
				</td>
				<td>
					<select id="bursting" name="bursting">  
						<option value="" >请选择是否爆款</option>
						<option value="1" >1-是</option>
              			<option value ="2"  >2-否</option>  
               		</select>  
				</td>
				<td>
					<select id="is_shelve" name="is_shelve">  
						<option value="" >请选择上架/下架</option>
						<option value="1" >1-上架</option>
              			<option value ="2"  >2-下架</option>  
               		</select>  
				</td>
					<td style="vertical-align:top;">
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
					<td style="vertical-align:top;">
					<a class="btn btn-mini btn-light" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="icon-download-alt"></i></a>
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
						<th>商品条形码</th>
						<th>商品名称</th>
						<th>商品编码</th>
						<th>规格</th>
						<th>最小起订量</th>
						<th>箱装数</th>
						<th>商品单位</th>
						<th>类别</th>
					    <th>采购单价</th>
					    <th>订购单价</th>
					    <th>货位</th>
					    <th>保质期</th>
					   <%-- <th>配送</th>
					     <th>爆款</th>--%>
					      <th>是否集采</th>
						<th class="center">操作</th>
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty productList}">
						
						<c:forEach items="${productList}" var="product" varStatus="vs">
									
							<tr>
					            <td class='center' style="width: 30px;">
					            <label>
					            <input type='checkbox' name='ids'  value="${product.id}" id="${product.id}" />
					            <span class="lbl"></span>
					            </label>
					              <input type='text'   style="display: none;"  value="${product.cargo_space_id}" id="csid${product.id}" />
					            </td>  
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<td><a  onclick="getProductInfoById('${product.id}')">${product.bar_code}</a></td>
								<td>${product.product_name }</td>
								<td>${product.product_num }</td>
								<td>${product.specification }</td>
								<td>${product.min_order_num }</td>
								<td>${product.box_number }</td>
								<td>${product.unit_name}</td>
								<td>${product.classify_name}</td>
								<td>${product.c}</td>
								<td>${product.s}</td>
								<c:choose>
									<c:when test="${empty product.zone}">
									<td>--</td>
									</c:when>
									<c:otherwise>
									<td>${product.zone}区${product.storey}层${product.storey_num}号</td>
									</c:otherwise> 
								</c:choose>
								
								<td>${product.expire_days }</td>	
								<%--<td>
									<c:if test="${product.type==1 }">加急商品</c:if>
									<c:if test="${product.type==2}">仓库配送</c:if>
								</td>
								<td>
									<c:if test="${product.bursting==1 }">是</c:if>
									<c:if test="${product.bursting==2}">否</c:if>
								</td>	--%>
								<td>
									<c:if test="${product.kl==0}">集采</c:if>
									<c:if test="${product.kl==1}">统采</c:if>
								</td>							
								<td>
								<c:if test="${QX.edit == 1 }">
								<a class='btn btn-mini btn-info' title="编辑" onclick="edit('${product.id}');"><i class='icon-edit'></i></a></c:if>
								<!--   <a class='btn btn-mini btn-danger' title="删除" onclick="del('${product.id}','${product.cargo_space_id}');"><i class='icon-trash'></i></a>-->
								
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
				<!--<td style="vertical-align:top;">
					<a class="btn btn-small btn-success" onclick="siMenu('product9999','proudct999','新增商品','<%=basePath%>product/goAddProduct.do')">新增</a>						
				 <a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a> 
				</td>-->	
				<td style="vertical-align:top;">
				<c:if test="${QX.edit == 1 }">
					<a class="btn btn-small btn-success"  onclick="goImportExcelPage();">批量导入</a></c:if>					
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
		$(top.hangge());
		$(function(){
			$("#searchcriteria").val("${pd.searchcriteria}");
			$("#bursting").val("${pd.bursting}");
			$("#is_shelve").val("${pd.is_shelve}");
		})
		
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
		
		
		
		//修改
		function edit(Id){
			
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>product/goEdit.do?productId='+Id;
			 diag.Width = 800;
			 diag.Height = 800;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
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
            var keyworda=encodeURI(encodeURI(keyword));
			window.location.href='<%=basePath%>importingproduct/excel.do?keyword='+keyworda+'&searchcriteria='+searchcriteria;
		}
		
		
		function getProductInfoById(id){
			
			top.mainFrame.tabAddHandler("392060","商品详情","<%=basePath%>product/getProductInfoById.do?id="+id);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
			
		}
		//批量导入商品信息
		function goImportExcelPage(){
			top.mainFrame.tabAddHandler("999777","批量导入商品信息","<%=basePath%>product/goImportExcelPage.do");
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		</script>
	</body>
</html>

