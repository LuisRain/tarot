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
			<form action="product/productActivity.do" method="post" id="ProductForm">
			<c:if test="${QX.cha == 1 }">
			<table>
				<tr><td>
			<select id="searchcriteria" name="searchcriteria">  
			<option value="2" >1-按商品条形码</option>
              <option value ="1"  >2-按商品名称</option>
              <!-- <option value ="3"  >3-按期数</option> -->
               </select>  
				</td>
				<td  style="vertical-align:top;">
				<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
				</td>
				<td>
					<select id="state" name="state">
						<option value="">请选择状态</option>
						<option value="1">已生效</option>
						<option value="2">未生效</option>
					</select>
					</td>
					<td style="vertical-align:top;">
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
					</td>
					
					<td style="vertical-align:top;">
					<c:if test="${QX.FX_QX==1}">
					
					<c:if test="${pd.state eq 2}">
							<a class="btn btn-warning btn-mini"
							onclick="makeAll('确定要审核选中的数据吗?');" title="批量审核"> <i
							id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
							</a>
							
					</c:if>
					</c:if>
					</td>
					
					<%-- <td style="vertical-align:top;">
						<c:if test="${QX.edit == 1 }">
							<a class="btn btn-small btn-success"  onclick="add();">新增</a></c:if>					
					</td> --%>
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
						<!-- <th>期数</th> -->
						<th>商品条形码</th>
						<th>商品名称</th>
						<th>达标数量</th>
						<th>赠品条形码</th>
						<th>赠品名称</th>
						<th>赠品数量</th>
						<th>开始日期</th>
						<th>结束日期</th>
						<th>是否生效</th>
					    <th>状态</th>
					    <!-- <th>操作</th> -->
					</tr>
				</thead>
										
				<tbody>
					
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty varlist}">
						<c:forEach items="${varlist}" var="product" varStatus="vs">
							<tr>
								<td class='center' style="width: 30px;">
									<label><input type='checkbox' name='ids' value="${product.id}" /><span class="lbl"></span></label>
								</td>
								<td class='center' style="width: 30px;">${vs.index+1}</td>
								<%-- <td>${product.stagenumber}</td> --%>
								<td>${product.bar_code}</td>
								<td>${product.product_name }</td>
								<td>${product.quantity }</td>
								<td>${product.abar_code }</td>
								<td>${product.aproduct_name}</td>			
								<td>${product.final_quantity}</td>
								<fmt:parseDate value="${product.begintime}" var="begintime" pattern="yyyy-MM-dd"/>
								<fmt:parseDate value="${product.endtime}" var="endtime" pattern="yyyy-MM-dd"/>
								<td><fmt:formatDate value="${begintime}" pattern="yyyy-MM-dd"/></td>
								<td><fmt:formatDate value="${endtime}"  pattern="yyyy-MM-dd" /></td>
								<%-- <td>${product.begintime}</td>
								<td>${product.endtime}</td> --%>
								<td><c:if test="${product.state==1}">已生效</c:if>
								<c:if test="${product.state==2}">未生效</c:if>
								</td>
								<td><c:if test="${product.type==1}">正在活动</c:if>
								<c:if test="${product.type==2}">已结束</c:if>
								</td>	
								<%-- <td><a class='btn btn-mini btn-info' title="编辑" onclick="editUser('${product.id }');"><i class='icon-edit'></i></a></td> --%>
							</tr>
						
						</c:forEach>
				
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="13" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
					
				
				</tbody>
			</table>
			
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td>
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
			$("#searchcriteria").val(${pd.searchcriteria});
			$("#state").val(${pd.state});
		})
		
		$("#checkAll").click(function () {
		     
                $("input[name='ids']:checkbox").prop("checked", this.checked);
            });
		$('table th input:checkbox').on('click' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
					
			}); 

		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>product/productActivityedit.do';
			 diag.Width = 350;
			 diag.Height = 380;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
				}
				diag.close();
			 };
			 diag.show();
		}
		//修改
		function editUser(user_id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="修改";
			 diag.URL = '<%=basePath%>product/productActivityedit.do?id='+user_id;
			 diag.Width = 300;
			 diag.Height = 380;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 top.jzts();
						 setTimeout("self.location=self.location",100);
				}
				diag.close();
			 };
			 diag.show();
		}
		
		
		
			//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') {
						  		str += document.getElementsByName('ids')[i].value;
						  	}
						  	else {
						  	str += ',' + document.getElementsByName('ids')[i].value;
						  	}
						  	
						  }
					}
					console.log(str)
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
						if(msg == '确定要审核选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>product/productexamine.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'text',
								cache: false,
								success: function(data){
									if(data=="true"){
										   alert("审核成功");
									}else if(data=="false"){
										alert("审核失败");
									}
								 nextPage(${page.currentPage});
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
		</script>
	</body>
</html>

