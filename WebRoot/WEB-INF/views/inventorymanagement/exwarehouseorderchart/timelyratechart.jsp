<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="../../system/admin/top.jsp"%>
</head>
<body>
<div class="container-fluid" id="main-container">
<div id="page-content" class="clearfix">
	<div>
	<input autocomplete="off" type="text" id="groupnum" name="groupnum" value="${pd.groupnum}" placeholder="请输入批次" onkeydown="entersearch()">
	
	<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();" title="检索"><i id="nav-search-icon" class="icon-search"></i></button>

	<button type="button" class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="excel();" title="导出到EXCEL">
									<i id="nav-search-icon" class="icon-download-alt"></i> </button></div>
		<div id="main" style="width: 100%; height: 600px;"></div>
		<div style="text-align: center;">
  <span style="color: red">注：最高分值为55</span>
  </div>
	</div>
	</div>
	<!-- 引入 -->
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="static/echarts/js/echarts.js"></script>
	<script src="static/layer/layer.js"></script>
</body>
<script type="text/javascript">
		
		$(top.hangge());
		
		function entersearch(){  
			var event = window.event || arguments.callee.caller.arguments[0];  
			if (event.keyCode == 13)  
			{  
				search();
			}  
		}
		function search(){ 
			var groupnum=$("#groupnum").val();
			if(groupnum!=null && groupnum!=''){
				jishilv();
			}else{
				layer.alert('请输入批次', {icon: 2});   
			}
			
		}
		
		var myChart = echarts.init(document.getElementById('main')); 
 		function jishilv(){
 		var groupnum=$("#groupnum").val();
 			$.ajax({
				type: "POST",
				url: '<%=basePath%>exwarehouseorder/timelyDeliveryRate.do',
				data:{'groupnum':groupnum},
				dataType:'json',
				cache: false,
				success: function(data){
				console.log(data);
					option = {
						title : {
							    text: '配送及时率'
						 },
						 tooltip: {
						        trigger: 'axis',
						        axisPointer: {
						            type: 'cross',
						            crossStyle: {
						                color: '#999'
						            }
						        }
						    },
						    toolbox: {
						        feature: {
						            dataView: {show: true, readOnly: false},
						            magicType: {show: true, type: ['line', 'bar']},
						            restore: {show: true},
						            saveAsImage: {show: true}
						        }
						    },
 						    /* legend: {
						        data:['配送率']
						    }, */
						    grid: {
						        left: '3%',
						        right: '4%',
						        bottom: '3%',
						        containLabel: true
						    },
						    xAxis : [
						        {
						            type : 'category',
						            data :data.listgroup,
						            axisLabel: {
						            	interval:0,  
						            	rotate:40 
						            }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						            
						        }
						    ],
						    series : [
						        {
						            name:'配送率',
						           
						            type:'bar',
						            
						            label: {
				                        show: true,
				                        position: 'top'
				                    },
				                    data:data.fenshu
						        }
						    ]
						};
					myChart.setOption(option);
				}
			});
 };
 
 
 	function excel(){
 		var groupnum=$("#groupnum").val();
 		
 		window.location.href='<%=basePath%>exwarehouseorder/timelyDeliveryexcel.do?groupnum='+groupnum;
 	} 
		
	</script>
</html>

