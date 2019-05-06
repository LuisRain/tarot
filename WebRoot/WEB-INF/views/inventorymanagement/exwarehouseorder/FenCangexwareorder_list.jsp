<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../../system/admin/top.jsp"%>
  <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
<style>
.td-label label {
	display: block;
	margin: 0 10px;
	float: left;
}

.lbl {
	font-size: 12px;
	vertical-align: middle;
}
</style>
</head>
<body>
	<div class="container-fluid" id="main-container">
		<div id="page-content" class="clearfix">
			<div class="row-fluid">
				<div class="row-fluid">
					<form action="exwarehouseorder/fencanglist.do" method="post" name="Form"
						id="Form">
						<input type="hidden" name="order_type" value="${pd.order_type}">
						<input type="hidden" name="ivt_state" value="${pd.ivt_state}">
						<input type="hidden" name="cityid" id="city" value="${pd.cityid}">
						<c:if test="${QX.cha == 1 }">
						<table>
							<tr>
								<td><select id="searchcriteria" name="searchcriteria"
									onchange="loaddate()">
										<option value="1">1-按门店名称/门店编码</option>
										<option value="2">2-按订购日期</option>
										<option value="3">3-按出库编号</option>
										<option value="4">4-按批次号</option>
								</select></td>
								<td><span class="input-icon"> <input
										class="span10 date-picker" id="orderdate" name="order_date"
										value="${pd.order_date}" type="text"
										data-date-format="yyyy-mm-dd" readonly="readonly"
										style="width:110px; display: none;" placeholder="按订购日期"
										title="按订购日期" /> <input autocomplete="off" style="margin-left: 3px"
										id="nav-search-input" type="text" name="keyword"
										value="${pd.keyword}" placeholder="这里输入关键词" />
										<%-- <c:if test="${pd.ivt_state eq 4}">
											<a class="btn btn-warning btn-mini" style="margin-bottom: 10px"
												onclick="makeAll1('确定要审核选中的数据吗?');" title="批量审核"> <i
												id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
											</a>
										</c:if> --%>
								</span>
								</td>
								<td style="padding-left: 5px">
								
											<select name="ivt_state">
												<option  value="" ${pd.ivt_state eq ''? "selected='selected'":""}>状态过滤----请选择</option>
												<option value="1" ${pd.ivt_state eq 1 ? "selected='selected'":""}>1-新出库单</option>
												<option value="5" ${pd.ivt_state eq 5 ? "selected='selected'":""}>2-订单已出库</option>
											</select>
								
								<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();"
											title="检索">
										<i id="nav-search-icon" class="icon-search"></i>
								</button>
								<c:if test="${pd.ivt_state eq 1}">
									<a class="btn btn-warning btn-mini" style="margin-bottom: 10px" onclick="examine('确定要审核选中的数据吗?');" title="审核"> 
									<i id="nav-search-icon" class='icon-wrench icon-2x icon-only'></i>
									</a>
								</c:if>
								<a class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="fromExcel();" title="上传EXCEL"><i id="nav-search-icon" class="icon-cloud-upload"></i></a>
								<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="a()" title="打印">打印</button>	
								</td>
							</tr>
						</table></c:if>
						<table style="width:100%;margin: 10px 0 20px;">
							<tr>
							<td style="width:5%;">按城市选择: <label><input name="cityqx" type="checkbox" 
										  onclick="SelectAll();"/><span class="lbl">全选</span> </label> 
								   <label><input name="cityqbx" type="checkbox" 
										  onclick="SelectNo();"/><span class="lbl">全不选</span> </label> </td>
							 
								<td style="width:90%;" class="td-label">
							<c:forEach items="${area}" var="are" varStatus="vn">
						         <label><input name="city" type="checkbox" 
										value="${are.id}"  onclick="search();"/><span class="lbl">${are.area_name}</span> </label> 
						  </c:forEach>
									</td>
							</tr>
								
						</table>
						<div style="border-top:1px solid #eee;">
							<div style="padding:10px 0;float:left"><a class="btn btn-mini btn-light" onclick="plan();" title="做计划">做计划</a></div>
							<div style="float: right;padding: 10px 0;">您共选<b style="color: red;font-size:13px;font-family:'Microsoft yahei';margin: 0 3px;">${page.totalResult}</b>条出库单</div>
						</div>
						<div style="border-top:1px solid #eee;">
							<div style="float: right;padding: 10px 0;">您共选<b style="color: red;font-size:13px;font-family:'Microsoft yahei';margin: 0 3px;">${page.totalResult}</b>条出库单</div>
						</div>
						<table id="table_report"
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th class="center"><label><input type="checkbox"
											id="zcheckbox" /><span class="lbl"></span></label></th>
									<th class="center">序号</th>
									<th class="center">批次号</th>
									<th class="center">出库单编号</th>
									 <!-- <th class="center">波次操作编号</th> --> 
									    <th class="center">门店全称</th>
									<th class="center">门店简称</th>
									<td class="center">所属仓库</td>
									<th class="center">联系人</th>
									<th class="center">联系方式</th>
									<th class="center">出库状态</th>
									<!--<th class="center">备注</th>-->
									<th class="center">整箱数合计</th>
									<th class="center">总体积</th>
									<th class="center">总重量</th>
								 	<th class="center">总金额</th>
								 	<th class="center">配送</th>
									<!-- <th class="center">创建人</th>
									<th class="center">订单生成时间</th> -->
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty varList}">
										<c:forEach items="${varList}" var="var" varStatus="vs">
											<tr>
												<td class='center' style="width: 50px;">
													<label>
															<input type='checkbox' name='ids' id="cks${var.order_num}" 
																onclick="ckek('cks${var.order_num}',${var.total_weight},${var.total_svolume},${var.squantity},'${var.type}')" 
															value="${var.id}" />
													<span class="lbl"></span></label> 
													 	<%-- 
													 		<c:if test="${var.ivt_state ne  4}">	 </c:if>  
													 	<c:if test="${var.ivt_state eq  4}">
															<input type='checkbox' name='nums' id="cks${var.order_num}"
															onclick="ckek('cks${var.order_num}',${var.total_weight},${var.total_svolume},${var.squantity},'${var.type}')" 
															 value="${var.order_num}" />
														</c:if>   --%>
														
														<label style="display: none"><input name='states' value="${var.ivt_state}" /><span class="lbl"></span>
													</label>
												</td>
												<td class='center' style="width: 30px;">${vs.index+1}</td>
												<td class="center">${var.group_num}</td>
												<td class="center">
												<a onclick="editProductNumber('${var.order_num}',1)" style="color:#2283c5">${var.order_num}</a>
												<%-- &nbsp;&nbsp;&nbsp;&nbsp;
												<a onclick="editProductNumberPQ('${var.order_num}',1)" style="color:#2283c5">分拣详单</a> --%>
												</td>
													<%-- <td class="center">
													<c:if test="${not empty var.wave_sorting_num}">
													${var.wave_sorting_num}
													</c:if>
													<c:if test="${empty var.wave_sorting_num}">
														--
													</c:if>
													</td> --%>
													<td class="center">${var.merchant_name}</td>
													
												<td class="center">${var.short_name}</td>
												<c:choose>
												<c:when test="${var.ck_id eq 1}"><td class="center">中央仓</td></c:when>
												<c:when test="${var.ck_id eq 2}"><td class="center">洛阳分仓</td></c:when>
												<c:when test="${var.ck_id eq 4}"><td class="center">洛阳分仓</td></c:when>
												<c:otherwise><td class="center">--</td></c:otherwise>
												</c:choose>
												<td class="center">${var.manager_name}</td>
												<td class="center">${var.manager_tel}</td>
												<c:if test="${var.ivt_state eq  1}">
													<td class="center">新出库单</td>
												</c:if>
												<c:if test="${var.ivt_state eq  2}">
													<td class="center">正在分拣</td>
												</c:if>
												<c:if test="${var.ivt_state eq  3}">
													<td class="center">分拣完成</td>
												</c:if>
												<c:if test="${var.ivt_state eq  4}">
													<td class="center">订单已合并</td>
												</c:if>
												  <c:if test="${var.ivt_state eq  5}">
													<td>订单已完成</td>
												</c:if>   
												 
												<!-- <c:if test="${var.ivt_state eq  6}">
									   <td>
									 完成
									 </td>
									 </c:if> 
												-<td class="center">
												<a title=" ${var.comment}"><p style="width: 90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                                                         ${var.comment}
                                                       </p>
                                                       </a>
												      
												
										</td>-->
										 		<td class="center"><fmt:formatNumber type="number" value="${var.total_count}"  maxFractionDigits="0"/> </td>
												 <td class="center">${var.total_svolume}</td>
												<td class="center">${var.total_weight}</td> 
													<td class="center">${var.final_amount}</td>
													<td>
													<c:if test="${var.type==0}">未发布</c:if>
													<c:if test="${var.type==1}">未装车</c:if>
													<c:if test="${var.type==2}">运输中</c:if>
													<c:if test="${var.type==3}">已签收</c:if>
												</td>
											<%-- 	<td class="center">${var.userName}</td>
												<td class="center">${var.create_time}</td> --%>
											</tr>

										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<c:if test="${pd.ivt_state eq  1}">
												<td colspan="100" class="center">没有新出库单</td>
											</c:if>
											<c:if test="${pd.ivt_state eq  2}">
												<td colspan="100" class="center">没有待分拣的出库单</td>
											</c:if>
											<c:if test="${pd.ivt_state eq  3}">
												<td colspan="100" class="center">请等待分拣系统分拣完成再进行操作</td>
											</c:if>
											<!-- <td colspan="100" class="center" >没有相关数据</td> -->
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
							<table style="width:100%;">
								<tr>
									<td style="vertical-align:top;"></td>
									<td style="vertical-align:top;"><div class="pagination"
											style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</div>
			<!--/row-->
		</div>
		<!--/#page-content-->
	</div>
	<!--/.fluid-container#main-container-->
	<!-- 返回顶部  -->
	<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse"> <i
		class="icon-double-angle-up icon-only"></i>
	</a>
	<!-- 引入 -->
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>

	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<!-- 日期框 -->
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script src="static/layui/layui.js" charset="utf-8"></script>
		
	<!--提示框-->
	<script type="text/javascript">
		var _qj=0;
		layui.use(['layer', 'form'], function(){
			  var layer = layui.layer ,form = layui.form; });
		$(top.hangge());
		//加载日期搜索框
		if(${pd.order_date !=null}){
			$("#searchcriteria").val("${pd.searchcriteria}");
			loaddate();
			if(${pd.ivt_state !=null}){
				$("#ivt_state").val("${pd.ivt_state}");
			}
			}
		function loaddate(){
			if($("#searchcriteria").val()=="2"){
				$("input[name='keyword']").hide();
				$("input[name='keyword']").val("");
				$("#orderdate").val("${pd.order_date}");
				$("#orderdate").show();
			}else{
				$("input[name='keyword']").val("${pd.keyword}");
				$("input[name='keyword']").show();
				$("#orderdate").hide();
				$("#orderdate").val("");
			}
		}
		
		function a(){
			var str=document.getElementsByName("city");
            var objarray=str.length;
            var chestr="";
            for ( var i=0;i<objarray;i++){
              if(str[i].checked == true){
              chestr+=str[i].value+",";
              }
            }  
          var ordernum = chestr.substring(0,chestr.length-1);
            if(chestr==""){
            	layer.alert("请选择城市！",{icon: 6000});
            }else{
            	window.open("<%=basePath%>exwarehouseorder/fcPrint.do?city="+chestr, "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');
            }
		}	
		
		//检索
		function search(){
	        var str=document.getElementsByName("city");
            var objarray=str.length;
            var chestr="";
            for ( var i=0;i<objarray;i++){
              if(str[i].checked == true){
              chestr+=str[i].value+",";
              }
            }  
            $("#city").val(chestr.substring(0,chestr.length-1));
			 top.jzts();
			$("#Form").submit(); 
		}
		function plan(){
			_qj=1;
			$("input[name='ids']").each(function(){
                $(this).prop('checked',false);
            })
			var _html='<div style="margin:0 auto;width:80%;position:relative;padding:10px 150px 10px 0;">'
						+'<div style="font-size:16px;line-height:40px;"><span style="width:30%; display:inline-block" >总重量：<span id="zhongliang"></span></span>'
						+'<span  style="width:30%;display:inline-block; margin:0 5%;">总体积：<span id="tiji"></span></span><span style="width:30%;">总件数：<span id="squantity"></span></span>'
						+'</div><span class="layer-tool" style="position:absolute; top:50%; margin-top:-20px; right:10px;line-height:40px">'
						+'<a href="javascript:addjh()" class="layui-btn"  >添加计划</a></span></div>'
			var planx=layer.open({
			  type: 1,
			  title:'计划',
			  offset: 'b',
			  shade:0,
			  skin: 'layui-layer-rim', //加上边框
			  area: ['80%', '15%'], //宽高
			  content: _html
			});
		}
		function addjh(){
			var Mask=layer.load(1, {shade: [0.3,'#fff'] });
			var orderNum="";
			for(var i=0;i < document.getElementsByName('ids').length;i++){
				if(document.getElementsByName('ids')[i].checked){
					if(orderNum=='') orderNum += document.getElementsByName('ids')[i].value;
					else orderNum += ',' + document.getElementsByName('ids')[i].value;
				}
			}
			var url="planOrder/saveplan.do?total_number="+$("#squantity").html()+"&total_weight="+$("#zhongliang").html()+"&total_volume="+$("#tiji").html()+"&orderNum="+orderNum;
			$.post(url,function(data){
				layer.close(Mask); 
				if(data){
					layer.msg("计划已生成！",{icon:1,time: 3000},function(){nextPage(${page.currentPage});}); 
				}else{
					layer.msg(data,{icon:1,time: 3000},function(){nextPage(${page.currentPage});}); 
				}
			})
		}
			function ckek(id,zl,tj,sq,type){
			if(_qj==1){
				if(type!=''){
               		$("#"+id).prop('checked',false);
					layer.msg("该订单已添加计划");
					return ;
				}
			}
			var _zl=parseFloat(zl.toFixed(2));
			var _tj=parseFloat(tj.toFixed(2));
			var _sq=parseFloat(sq.toFixed(2));
			if($("#"+id).is(":checked")){
				if($("#zhongliang").html()!=""&&$("#zhongliang").html()!="undefined"&&$("#zhongliang").html()!=null){
					_zl=(_zl+parseFloat($("#zhongliang").html())).toFixed(2);
				}else{
					_zl+=parseFloat(0);
				}
				if($("#tiji").html()!=""&&$("#tiji").html()!="undefined"&&$("#tiji").html()!=null){
					_tj=(_tj+parseFloat($("#tiji").html())).toFixed(2);
				}else{
					_tj+=parseFloat(0);
				}
				if($("#squantity").html()!=""&&$("#squantity").html()!="undefined"&&$("#squantity").html()!=null){
					_sq+=parseFloat($("#squantity").html());
				}else{
					_sq+=parseFloat(0);
				}
			}else{
				if($("#zhongliang").html()!=""&&$("#zhongliang").html()!="undefined"&&$("#zhongliang").html()!=null){
					_zl=(parseFloat($("#zhongliang").html())-_zl).toFixed(2);
				}
				if($("#tiji").html()!=""&&$("#tiji").html()!="undefined"&&$("#tiji").html()!=null){
					_tj=(parseFloat($("#tiji").html())-_tj).toFixed(2);
				}
				if($("#squantity").html()!=""&&$("#squantity").html()!="undefined"&&$("#squantity").html()!=null){
					_sq=(parseFloat($("#squantity").html())-_sq).toFixed(2);
				}
			}
			$("#zhongliang").html(_zl);
			$("#tiji").html(_tj);
			$("#squantity").html(_sq);
		}
		function SelectAll() {
			 var chestr="";
			 var checkboxs=document.getElementsByName("city");
			 for (var i=0;i<checkboxs.length;i++) {
			 var e=checkboxs[i];
			  e.checked=!e.checked;
			  chestr+=checkboxs[i].value+",";
			 }
			   $("#city").val(chestr.substring(0,chestr.length-1));
			   top.jzts();
				$("#Form").submit(); 
			}
		
		function SelectNo() {
			 var checkboxs=document.getElementsByName("city");
			 for (var i=0;i<checkboxs.length;i++) {
			 var e=checkboxs[i];
			  e.checked=e.checked;
			 }
			   $("#city").val('');
			   top.jzts();
				$("#Form").submit(); 
			}
 
			 //修改商品数量
		  function editProductNumber(id1,type){
			var url='<%=basePath%>exwarehouseorder/FengoExwareorderProductEdit.do?orderId='+id1+'&type='+type;
				top.mainFrame.tabAddHandler("392060853225","查看出库单信息",url);
			 
		 } 
			 //查看出库单标签系统分类详情-分量详情
		  function editProductNumberPQ(id1,type){
			var url='<%=basePath%>exwarehouseorder/goExwareorderProductEditPQ.do?orderId='+id1+'&type='+type;
				top.mainFrame.tabAddHandler("97976679","查看出库单分量详情",url);
		 } 
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>exwarehouseorder/goAdd.do';
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
		
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>exwarehouseorder/delete.do?id="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>exwarehouseorder/goEdit.do?id='+Id;
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

	<script type="text/javascript">
			$(function() {
			
			//下拉框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
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
			var inputs1 = document.getElementsByName("city");
						for (var j = 0; j < inputs1.length; j++) {
			                        inputs1[j].checked = "";
			            }	
						var arr1=$("#city").val();
						if(arr1!=null){
				            arr1 = arr1.split(",");
				            for (var i = 0; i < arr1.length; i++) {
				                for (var j = 0; j < inputs1.length; j++) {
				                    if (arr1[i] == inputs1[j].value) {
				                        inputs1[j].checked = true;
				                       break;
				                    }
				                }
				            }
			            }
		});
			
			function fromExcel(){
				top.mainFrame.tabAddHandler("999999","导入出库订单","<%=basePath%>exwarehouseorder/goExUploadExcel.do");
				if(MENU_URL != "druid/index.html"){
					jzts();
				}
			}
	<%-- 	//打开上传excel页面
		function fromExcel(){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="EXCEL 导入到数据库";
			 diag.URL = '<%=basePath%>enwarehouseorder/goUploadExcel.do?type=0';
			 diag.Width = 500;
			 diag.Height = 300;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin1').style.display == 'none'){
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
		} --%>
		function tomergeEx(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					//top.jzts();
					var url = "<%=basePath%>exwarehouseorder/tomergeEx.do";
					$.get(url,function(data){
						if(data=="success"){
							alert("合并订单成功！");
						}
						
					});
				}
			})
	}	
		//第二步 标签波次出库 先选定小于或等于351家门店,便于LED波次分拣 以46家门店为单位进行分拣操作
		function showAndEditWaveForExOrder(msg){
			
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=1){
						  		bootbox.dialog("只能选新出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}}]);
						    	  return ;
						  	  }
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
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要波次操作您所选的出库单吗?'){
							top.mainFrame.tabAddHandler("999","波次出库商品分量清单","<%=basePath%>exwarehouseorder/doWaveExwareorderOfPer.do?DATA_IDS="+str);
							if(MENU_URL != "druid/index.html"){
								top.jzts();
							}
							
						}
					}
				}
			});
		}
		//第三步 LED波次操作
		function toWaveForExOrder(msg){//'确定要波次操作选中的出库单吗?'
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=2){
						  		bootbox.dialog("只能选择LED分拣结束的出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}}]);
						    	  return ;
						  	  }
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
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要波次操作您所选的出库单吗?'){
							top.mainFrame.tabAddHandler("999","波次出库商品清单","<%=basePath%>exwarehouseorder/doWaveExwareorderOfLED.do?DATA_IDS="+str);
							if(MENU_URL != "druid/index.html"){
								top.jzts();
							}
						}
					}
				}
			});
		}
		//第四步 toCombineEXTotoExcel
		function toCombineEXTotoExcel(msg){//'确定要波次操作选中的出库单吗?'
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=3){
						  		bootbox.dialog("只能选择标签系统分拣结束的出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}}]);
						    	  return ;
						  	  }
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
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要波次操作您所选的出库单吗?'){
				<%-- top.mainFrame.tabAddHandler("999","出库单管理","<%=basePath%>exwarehouseorder/doSumEXOItem.do?ivt_state=4");
							if(MENU_URL != "druid/index.html"){
								jzts();
							} --%>
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>exwarehouseorder/doSumEXOItem.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									if(data.msg=='1'){
										alert("您所选的出库单条目,标签系统还未分拣结束,请耐心等待!");
									}
									if(data.msg=='2'){
										alert("您所选的出库单条目,请重新操作!");
									}
									if(data.msg=='ok'){
										alert("合并完成!");
									}
									nextPage(${page.currentPage});
									//window.location.href='<%=basePath%>exwarehouseorder/list.do?ivt_state=3&order_type=1';
								}
							}); 
						}
					}
				}
			});
		}
		
		//导出出库单 
		function toSplitEXToExcel(){
			window.location.href='<%=basePath%>exwarehouseorder/toSplitEXToExcel.do';
		}
		
		//合并出库单toWaveForExOrder;
		function toCombineEXTotoExcel1(){
			window.location.href='<%=basePath%>exwarehouseorder/toCombineEXTotoExcel.do';
		}
		function waveEx(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	  if(document.getElementsByName("states")[i].value!=2){
						  		bootbox.dialog("请选择审核过的出库单!", 
										[{
											"label" : "关闭",
											"class" : "btn-small btn-success",
											"callback": function() {
												//Example.show("great success");
												}
										}]);
						    	  return ;
						  	  }
						  }
					}
					if(str==''){
						bootbox.dialog("您没有选择任何内容!", 
							[{
								"label" : "关闭",
								"class" : "btn-small btn-success",
								"callback": function() {
									//Example.show("great success");
									}
							}]);
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要波次操作您所选的出库单吗?'){
							top.mainFrame.tabAddHandler("999","波次出库商品清单","<%=basePath%>exwarehouseorder/doWaveExwareorder.do?DATA_IDS=" +str);
												if (MENU_URL != "druid/index.html") {
													jzts();
												}
											}
										}
									}
								});
			}
		//批量操作
		function makeAll1(msg){
			bootbox.confirm(msg, function(result) {
				
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	
						  	  if(document.getElementsByName("states")[i].value!=4){
						  		bootbox.dialog("请选择以导出合并的订单!", 
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
									
						  		  
						    	  return ;
						  		  
						  	  }
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
						
						$("#zcheckbox").tips({
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
								url: '<%=basePath%>exwarehouseorder/reviewedAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'text',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									if(data=="true"){
										   alert("审核出库成功");
									}else if(data=="false"){
										alert("审核出库失败");
									}else{
										alert(data);
									}
								 nextPage(${page.currentPage});
									 
								}
							});
						}
					}
				}
			});
		};
		
		
		//审核操作
		function examine(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					
					var str = '';
					
					for(var i=0;i < document.getElementsByName('ids').length;i++)
					{
						
						  if(document.getElementsByName('ids')[i].checked){
						  	if(str=='') str += document.getElementsByName('ids')[i].value;
						  	else str += ',' + document.getElementsByName('ids')[i].value;
						  	
						  	  if(document.getElementsByName("states")[i].value!=1){
						  		bootbox.dialog("请选择等待出库的订单!", 
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
									
						  		  
						    	  return ;
						  		  
						  	  }
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
						
						$("#zcheckbox").tips({
							side:3,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						
						return;
						
					}
					else{	
						if(msg == '确定要审核选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>exwarehouseorder/examine.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'text',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									
									  
									if(data=="true"){
										   alert("审核出库成功");
									}else if(data=="false"){
										alert("审核出库失败");
									}else{
										alert(data);
									}
								 nextPage(${page.currentPage});
									 
								}
							});
						}
					}
				}
			});
		};
		
		</script>

</body>
</html>

