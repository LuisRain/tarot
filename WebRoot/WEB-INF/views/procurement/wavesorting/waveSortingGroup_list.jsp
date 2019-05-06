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
	<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
	</head> 
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
  <div class="row-fluid">
	<div class="row-fluid">
			<!-- 检索  -->
			<form action="<%=basePath%>waveSortingGroup/waveSortingGroupList.do" method="post" id="waveSortingGroupForm">
			<table><tr><td>
			<select id="searchcriteria" name="searchcriteria" onchange="loaddate()">  
              <option value ="1"  >1-波次编号</option>  
              <option value ="2"  >2-创建时间</option>
               <option value ="3"  >3-创建人</option>  
               </select>
             
				</td>
					<td>
						<span class="input-icon" id="searchinput">
						 	<input autocomplete="off" id="nav-search-input" type="text" name="keyword" value="${pd.keyword}" placeholder="这里输入关键词" />
						 	<input class="span10 date-picker"  name="startDate" id="startDate"  value="${pd.startDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="开始日期" title="波次生成时间"/>
							<input class="span10 date-picker" id="endDate" name="endDate"  value="${pd.endDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px; display: none;" placeholder="结束日期" title="波次生成时间"/>
							<button class="btn btn-mini btn-light" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>
						</span>
					</td>
				</tr>
			</table>
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr><th class="center">
						<!--  选择全部按钮-->
						<label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
						</th>
						<th class='center'>序号</th>
						<th class='center'>波次号</th>
						<th class='center'>导出门店信息</th>
						<!--<th class='center'>导出出库单详情信息</th>  
						<th class='center'>查看本波次出库单分拣详情</th>-->
						<th class='center'>标签系统分拣详情及操作</th>
						<th class='center'>LED分拣详情</th>
						<th class='center'>状态</th>
						<th class='center'>创建人</th>
						<th class='center'>波次创建时间</th>
					</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${not empty varList}">
						<c:forEach items="${varList}" var="waveSortingGroup" varStatus="vs">
							<tr>
					            <td class='center' style="width: 30px;">
					            <label><input type='checkbox' name='ids'  value="${waveSortingGroup.waveSortingGroupId}" id="${waveSortingGroup.waveSortingGroupId}" />
					            <span class="lbl"></span></label>
					            </td>
								<td class='center' style="width: 30px;">${waveSortingGroup.waveSortingGroupId}</td>
								<td class='center' style="width: 80px;">${waveSortingGroup.waveSortingGroupNum }</td>
								<td class='center' style="width: 230px;">
									<a class="btn btn-mini btn-light" onclick="toExcel('${waveSortingGroup.waveSortingGroupNum}');" title="导出本波次门店信息">
										[全部<i id="nav-search-icon" class="icon-download-alt"></i>]
									</a>&nbsp;&nbsp;
									<a class="btn btn-mini btn-light" onclick="toExcelForPer('${waveSortingGroup.waveSortingGroupNum}');" title="导出本波次门店信息">
										[散货<i id="nav-search-icon" class="icon-download-alt"></i>]
									</a>
									<a class="btn btn-mini btn-light" onclick="toExcel1('${waveSortingGroup.waveSortingGroupNum}');" title="导出本波次站点的整箱总数">
										[整箱总数<i id="nav-search-icon" class="icon-download-alt"></i>]
									</a>&nbsp;&nbsp;
									<a class="btn btn-mini btn-light" onclick="toExcelForPer1('${waveSortingGroup.waveSortingGroupNum}');" title="导出本波次站点散货的总数">
										[散货总数<i id="nav-search-icon" class="icon-download-alt"></i>]
									</a>
								</td>
								<!--<td class='center' style="width: 80px;">
									<a class="btn btn-mini btn-light" onclick="toWord('${waveSortingGroup.waveSortingGroupNum}');" title="导出本波次出库详单">
										<i id="nav-search-icon" class="icon-download-alt"></i>
									</a>
								</td>-->
								<td class='center' style="width: 100px;">
										<input type="hidden" name="DATA_IDS" value="${waveSortingGroup.waveSortingGroupNum }">
										<a onclick="getTexoi('${waveSortingGroup.waveSortingGroupNum}');" id="texNum">
										查看详情</a>&nbsp;&nbsp;
										<%-- <a onclick="insertPer('${waveSortingGroup.waveSortingGroupNum}');" id="texNum">
										导入标签系统
										</a> --%>
										</td>
								<td class='center' style="width: 220px;">
										<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',0);" id="texNum">
										查看全部</a>
										[<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',1);" id="texNum">
										(1)</a>
										<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',2);" id="texNum">
										(2)</a>
										<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',3);" id="texNum">
										(3)</a>
										<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',4);" id="texNum">
										(4)</a>
										<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',5);" id="texNum">
										(5)</a>
										<a onclick="getTexoi0('${waveSortingGroup.waveSortingGroupNum}',6);" id="texNum">
										(6)</a>]
										</td>
								<td class='center' style="width: 40px;">
									<c:if test="${waveSortingGroup.waveSortingGroupState==1}">
	   									新建波次
									</c:if>
									<c:if test="${waveSortingGroup.waveSortingGroupState==2}">
	   									已完成波次
									</c:if>
								</td>
								<td class='center' style="width: 60px;">${waveSortingGroup.user.NAME}</td>	
								<td class='center' style="width: 160px;">${waveSortingGroup.createTime}</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
					
				</c:choose>
				<c:if test="${pd.flag==1}">
				<tr class="main_info">
					<td colspan="10" class="center">波次号为${pd.waveNum}的出库单导出成功</td>
					</tr>
				</c:if>
				<c:if test="${pd.flag==-1}">
				<tr class="main_info">
					<td colspan="10" class="center">操作有误，请重新操作。</td>
					</tr>
				</c:if>
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
		//导入到散货分拣系统
		function insertPer(waveOrder){
			bootbox.confirm("确定要把本波次订单散货数据导入到标签系统吗？", function(result) {
				if(result) {
					top.jzts();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>exwarehouseorder/importToPerSystem.do?tm='+new Date().getTime(),
				    	data: {DATA_IDS:waveOrder},
						dataType:'json',
						cache: false,
						success: function(data){
							 $.each(data.list, function(i, list){
								 if(data.list[0].msg!=''&&data.list[0].msg!=null&&data.list[0].msg!=undefined){
									 alert(data.list[0].msg);
								 }else{
									 alert("请重新操作。");
								 }
								 nextPage(${page.currentPage});
							 });
						}
					});
				}
			});
		}
				
		function getTexoi0(DATA_IDS,waveOrder){
			top.mainFrame.tabAddHandler("999","波次LED出库商品清单","<%=basePath%>exwarehouseorder/listOfWaveSortingGroup.do?DATA_IDS="+DATA_IDS+"&waveOrder="+waveOrder);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		function getTexoi(DATA_IDS){
			top.mainFrame.tabAddHandler("777777","波次标签系统出库商品清单","<%=basePath%>exwarehouseorder/listOfWaveSortingGroupBq.do?DATA_IDS="+DATA_IDS);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
		}
		//检索
		function search(){
			top.jzts();
			$("#waveSortingGroupForm").submit();
		}
		//导出excel 全部门店
		function toExcel(waveNum){
			window.location.href="<%=basePath%>exwarehouseorder/excelOfMerchants.do?waveNum="+waveNum;
		}
		//导出excel 全部门店
		function toExcel1(waveNum){
			window.location.href="<%=basePath%>exwarehouseorder/excelOfMerchants.do?state=1&waveNum="+waveNum;
		}
		//导出excel 仅散货分拣不为零的门店
		function toExcelForPer(waveNum){
			window.location.href="<%=basePath%>exwarehouseorder/excelOfMerchantsForPer.do?waveNum="+waveNum;
		}
		//导出excel
		function toWord(waveNum){
			window.location.href="<%=basePath%>exwarehouseorder/toWordFromExWarehouseInfos.do?waveNum="+waveNum;
		}
		//导出excel 仅散货分拣不为零的门店
		function toExcelForPer1(waveNum){
			window.location.href="<%=basePath%>exwarehouseorder/excelOfMerchantsForPer.do?state=1&waveNum="+waveNum;
		}
		
		</script>
	</body>
</html>

