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
			<form action="<%=basePath%>orderGroup/orderGroupList.do" method="post" id="orderGroupForm">
			<c:if test="${QX.cha == 1 }">
			<table><tr><td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-批次编号</option>  
              <option value ="2"  >2-创建时间</option>
               <option value ="3"  >3-创建人</option>  
               </select>
				</td>
					<td>
						<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="startDate" id="startDate"  value="${pd.startDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="批次创建时间"/>
							<input class="span10 date-picker" id="endDate" name="endDate"  value="${pd.endDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="批次创建时间"/>
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
						</span>
					</td>
				</tr>
			</table></c:if>
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr><th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th>序号</th>
						<th>批次号</th>
						<th>入库单</th>
						<th>出库单</th>
						<th>状态</th>
						<th>创建人</th>
						<th>批次创建时间</th>
					</tr> 
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="orderGroup" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					           <%--  <label><input type='checkbox' name='ids'  value="${orderGroup.orderGroupId}" id="${orderGroup.orderGroupId}" /><span class="lbl"></span></label> --%>
					            </td>
								<td class='center' style="width: 30px;"></td>
								<td>${orderGroup.groupNum }</td>
								<td>
									<c:forEach items="${orderGroup.enos}" var="eno" varStatus="vsEN">
										<a onclick="getTeno('${eno.orderNum}');" id="tenNum">
										${vsEN.count}.${eno.orderNum}
										</a><br/>
									</c:forEach>
								</td>
								<td>
									<c:forEach items="${orderGroup.exos}" var="exo" varStatus="vsEX">
										<a onclick="getTexo('${exo.orderNum}');" id="texNum">
										${vsEX.count}.${exo.orderNum }
										</a><br/>
									</c:forEach>
								<td>
									<c:if test="${orderGroup.state==1}">
	   									新建批次
									</c:if>
									<c:if test="${orderGroup.state==2}">
	   									已完成批次
									</c:if>
								</td>
								<td>${orderGroup.user.NAME}</td>	
								<td>${orderGroup.createTime}</td>
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
				<td style="vertical-align:top;"><!-- <a class="btn btn-small btn-success" onclick="add();">新增</a>	 -->						
					<c:if test="${QX.del == 1 }"><a title="批量删除" class="btn btn-small btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='icon-trash'></i></a></c:if>
				</td>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
		</div>
		</form>
	</div>
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
		$(top.hangge());
		//设置时间加载插件
		$(function(){
			if(${pd.startDate !=null}){
			$("#searchcriteria").val("${pd.searchcriteria}");
			loaddate();
			}
			//日期框
			$('.date-picker').datepicker();
			//复选框
			$('table th input:checkbox').on('click' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
			});
		});
		function loaddate(){
			if($("#searchcriteria").val()=="2"){
				$("input[name='keyword']").hide();
				$("input[name='keyword']").val("");
				$("#startDate").show();
				$("#endDate").show();
			}else{
				$("input[name='keyword']").show();
				$("#startDate").hide();
				$("#startDate").val("");
				$("#endDate").hide();
				$("#endDate").val("");
			}
		}
		function goImportExcelPage(){
			top.mainFrame.tabAddHandler("999999","导入批次订单","<%=basePath%>sellingorder/goImportExcelPage.do");
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		function getTso(tsoNumVal){
			top.mainFrame.tabAddHandler("35","销售订单","<%=basePath%>sellingorder/sellingorderlist.do?searchcriteria=3&keyword="+tsoNumVal);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		function getTpo(tpoNumVal){
			top.mainFrame.tabAddHandler("31","采购订单","<%=basePath%>purchaseOrder/purchaseOrdersList.do?searchcriteria=3&keyword="+tpoNumVal);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		function getTeno(tenoNumVal){
			top.mainFrame.tabAddHandler("32","入库单","<%=basePath%>enwarehouseorder/list.do?searchcriteria=3&keyword="+tenoNumVal);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		function getTexo(texoNumVal){
			top.mainFrame.tabAddHandler("33","出库单","<%=basePath%>exwarehouseorder/list.do?searchcriteria=3&keyword="+texoNumVal);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>orderGroup/delete.do?id="+Id+"&tm="+new Date().getTime();
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
								url: '<%=basePath%>orderGroup/deleteAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
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
			$("#OrderGroupForm").submit();
		}
		
		//打开上传excel页面
		function fromExcel(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>sellingorder/goUploadExcel.do';
			 diag.Width = 300;
			 diag.Height = 150;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location.reload()",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		</script>
	</body>
</html>

