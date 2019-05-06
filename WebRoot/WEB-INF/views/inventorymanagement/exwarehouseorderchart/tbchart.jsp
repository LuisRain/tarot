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
<div style="margin: 8px 0; padding-left: 8px;">
<input autocomplete="off" 	id="groupnum" type="text" name="groupnum" onkeydown="entersearch()"	value="${pd.groupnum}" placeholder="输入格式如：201806" />
		<button class="btn btn-mini btn-light" style="margin-bottom: 10px" onclick="search();"
											title="检索">
									<i id="nav-search-icon" class="icon-search"></i>
		</button>
</div>

		
		<div id="main" style="width: 800px; height: 600px;float:left;"></div>
		<div id="mains" style="width: 800px; height: 600px; float: right;"></div>
	</div>
	<!-- 引入 -->
	<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
	<script src="static/echarts/js/echarts.js"></script>
	<script src="static/layer/layer.js"></script>
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
				tb();
				hb();
			}else{
				layer.alert('请输入批次', {icon: 2});   
			}
			
		}
		
		var myChart = echarts.init(document.getElementById('main')); 
		var myCharts=echarts.init(document.getElementById('mains')); 
		function tb(){
			var groupnum=$("#groupnum").val();
			$.ajax({
				type: "POST",
				url: '<%=basePath%>exwarehouseorder/tbarrival.do',
				data:{'groupnum':groupnum},
				dataType:'json',
				cache: false,
				success: function(data){
					option = {
						title : {
							    text: '出库数和金额同比',
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
						    legend: {
						        data:['数量','金额（元）']
						    },
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

						            	//字体竖直两个显示
                                        /* interval: 0,
                                        formatter:function(value)
                                        {
                                            debugger
                                            var ret = "";//拼接加\n返回的类目项
                                            var maxLength = 2;//每项显示文字个数
                                            var valLength = value.length;//X轴类目项的文字个数
                                            var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                                            if (rowN > 1)//如果类目项的文字大于3,
                                            {
                                                for (var i = 0; i < rowN; i++) {
                                                    var temp = "";//每次截取的字符串
                                                    var start = i * maxLength;//开始截取的位置
                                                    var end = start + maxLength;//结束截取的位置
                                                    //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                                                    temp = value.substring(start, end) + "\n";
                                                    ret += temp; //凭借最终的字符串
                                                }
                                                return ret;
                                            }
                                            else {
                                                return value;
                                            }
                                        } */
						            }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						            name: '数量/金额（元）',
						        }
						    ],
						    /* dataZoom: [
					            {
					                show: true,
					                start: 94,
					                end: 100
					            },
					            {
					                type: 'inside',
					                start: 94,
					                end: 100
					            },
					            {
					                show: true,
					                yAxisIndex: 0,
					                filterMode: 'empty',
					                width: 30,
					                height: '80%',
					                showDataShadow: false,
					                left: '93%'
					            }
					        ], */
						    series : [
						        {
						            name:'数量',
						            type:'bar',
						            data:data.listquantity,
						            markPoint : {
						                data : [
						                    {type : 'max', name: '最大值'},
						                    {type : 'min', name: '最小值'}
						                ]
						            },
						            label: {
						                normal: {
						                    show: true,
						                    position: 'top'
						                }
						            },
						            //随机颜色
						            /* itemStyle: {
						                normal: {
						                    // 随机显示
						                    //color:function(d){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}
						                  
						                    // 定制显示（按顺序）
						                    color: function(params) { 
						                        var colorList = ['#C33531','#EFE42A','#64BD3D','#EE9201','#29AAE3', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ]; 
						                        return colorList[params.dataIndex] 
						                    }
						                },
						            }, */
						        },
						        {
						            name:'金额（元）',
						            type:'bar',
						            data:data.listprice,
						            markPoint : {
						                data : [
						                    {type : 'max', name: '最大值'},
						                    {type : 'min', name: '最小值'}
						                ]
						            },
						            markLine : {
						                lineStyle: {
						                    normal: {
						                        type: 'dashed'
						                    }
						                }
						            },
						            label: {
						                normal: {
						                    show: true,
						                    position: 'top',
						                    formatter: '{c} 元'
						                }
						            },
						            /* itemStyle: {
						                normal: {
						                    // 随机显示
						                    //color:function(d){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}
						                  
						                    // 定制显示（按顺序）
						                    color: function(params) { 
						                        var colorList = ['#C33531','#EFE42A','#64BD3D','#EE9201','#29AAE3', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ]; 
						                        return colorList[params.dataIndex] 
						                    }
						                },
						            }, */
						        }
						    ]
						};
					myChart.setOption(option);
				}
			});
			}
		function hb(){
			var groupnum=$("#groupnum").val();
			$.ajax({
				type: "POST",
				url: '<%=basePath%>exwarehouseorder/hbarrival.do',
				data:{'groupnum':groupnum},
				dataType:'json',
				cache: false,
				success: function(data){
					option = {
						title : {
							    text: '出库数和金额环比'
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
						    legend: {
						        data:['数量','金额（元）']
						    },
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

						            	//字体竖直两个显示
                                        /* interval: 0,
                                        formatter:function(value)
                                        {
                                            debugger
                                            var ret = "";//拼接加\n返回的类目项
                                            var maxLength = 2;//每项显示文字个数
                                            var valLength = value.length;//X轴类目项的文字个数
                                            var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                                            if (rowN > 1)//如果类目项的文字大于3,
                                            {
                                                for (var i = 0; i < rowN; i++) {
                                                    var temp = "";//每次截取的字符串
                                                    var start = i * maxLength;//开始截取的位置
                                                    var end = start + maxLength;//结束截取的位置
                                                    //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                                                    temp = value.substring(start, end) + "\n";
                                                    ret += temp; //凭借最终的字符串
                                                }
                                                return ret;
                                            }
                                            else {
                                                return value;
                                            }
                                        } */
						            }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						            name: '数量/金额（元）',
						        }
						    ],
						    /* dataZoom: [
					            {
					                show: true,
					                start: 94,
					                end: 100
					            },
					            {
					                type: 'inside',
					                start: 94,
					                end: 100
					            },
					            {
					                show: true,
					                yAxisIndex: 0,
					                filterMode: 'empty',
					                width: 30,
					                height: '80%',
					                showDataShadow: false,
					                left: '93%'
					            }
					        ], */
						    series : [
						        {
						            name:'数量',
						            type:'bar',
						            data:data.listquantity,
						            markPoint : {
						                data : [
						                    {type : 'max', name: '最大值'},
						                    {type : 'min', name: '最小值'}
						                ]
						            },
						            label: {
						                normal: {
						                    show: true,
						                    position: 'top'
						                }
						            },
						            //随机颜色
						            /* itemStyle: {
						                normal: {
						                    // 随机显示
						                    //color:function(d){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}
						                  
						                    // 定制显示（按顺序）
						                    color: function(params) { 
						                        var colorList = ['#C33531','#EFE42A','#64BD3D','#EE9201','#29AAE3', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ]; 
						                        return colorList[params.dataIndex] 
						                    }
						                },
						            }, */
						        },
						        {
						            name:'金额（元）',
						            type:'bar',
						            data:data.listprice,
						            markPoint : {
						                data : [
						                    {type : 'max', name: '最大值'},
						                    {type : 'min', name: '最小值'}
						                ]
						            },
						            markLine : {
						                lineStyle: {
						                    normal: {
						                        type: 'dashed'
						                    }
						                }
						            },
						            label: {
						                normal: {
						                    show: true,
						                    position: 'top',
						                    formatter: '{c} 元'
						                }
						            },
						            /* itemStyle: {
						                normal: {
						                    // 随机显示
						                    //color:function(d){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}
						                  
						                    // 定制显示（按顺序）
						                    color: function(params) { 
						                        var colorList = ['#C33531','#EFE42A','#64BD3D','#EE9201','#29AAE3', '#B74AE5','#0AAF9F','#E89589','#16A085','#4A235A','#C39BD3 ','#F9E79F','#BA4A00','#ECF0F1','#616A6B','#EAF2F8','#4A235A','#3498DB' ]; 
						                        return colorList[params.dataIndex] 
						                    }
						                },
						            }, */
						        }
						    ]
						};
					myCharts.setOption(option);
				}
			});
			}
		
	</script>

</body>
</html>

