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
	<script src="static/echarts/js/echarts.js"></script>
	</head> 
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">
<form action="<%=basePath%>jyzshmydcontroller/findmyd.do" method="post" id="SupplierForm">
			<table>
				<tr>
					<td>
						<span class="input-icon">
						 	月份：<input autocomplete="off" id="month" type="text" name="month" value="${pd.month}" placeholder="格式：201806" />
						 	<!-- <span style="color: red">格式：年份+月份 如：201806</span> -->
							<button class="btn btn-mini btn-light" onclick="search();" title="检索" style="margin-bottom: 10px"><i id="nav-search-icon" class="icon-search"></i></button>
						</span>
					</td>
					
				</tr>
			</table>
	</form>
<div id="main" style="width: 100%; height: 600px;"></div>

	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
  <div style="text-align: center;">
  <span style="color: red">注：最高分值为20</span>
  </div>
	 
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
		//检索
		function search(){
			top.jzts();
			$("#SupplierForm").submit();
		}
		var myChart = echarts.init(document.getElementById('main')); 
		option = {
			     tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        },
			        formatter: function (params) {
			            var tar = params[1];
			            return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
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
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis: {
			        type: 'category',
			         splitLine: {show:false},
			        data: ${groupNumList}
			    },
			    yAxis: {
			        type: 'value'
			    },
			    series: [
			        {
			            name: '初始',
			            type: 'bar',
			            stack:  '得分',
			            itemStyle: {
			                normal: {
			                    barBorderColor: 'rgba(0,0,0,0)',
			                    color: 'rgba(0,0,0,0)'
			                },
			                emphasis: {
			                    barBorderColor: 'rgba(0,0,0,0)',
			                    color: 'rgba(0,0,0,0)'
			                }
			            },
			            data: [0, 0, 0, 0, 0, 0]
			        },
			        {
			            name: '得分',
			            type: 'bar',
			            stack: '得分',
			            label: {
			                normal: {
			                    show: true,
			                    position: 'inside'
			                }
			            },
			            data:${myd}
			        }
			    ]
			};
			myChart.setOption(option);
		</script>
		
	
		
	</body>
</html>

