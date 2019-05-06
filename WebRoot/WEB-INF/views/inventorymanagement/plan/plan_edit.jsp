<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

</head>
	<!-- jsp文件头和头部 -->

  <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
	<style type="text/css">
  		.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		.flsize{font-weight:normal;}
  	</style>
		
		<script type="text/javascript">
		function print(order_num){
			window.open("<%=basePath%>planOrder/planPrint.do?plan_order=${planorder.plan_order}", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
		}
		</script>
<body>

		<div class="layui-form" >
			<div style="padding-right: 20px	">
			<button style="float: right;" onclick="print()" class=" btn-app btn-light btn-mini"> <i class="icon-print"></i> 打印 </button>
  			</div>
  			<table class="layui-table">
				<tr>
					<th style="width:100px;text-align: right">基本信息</th>
					<th colspan="3"></th>
					</tr>
						<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">计划单号:</th>
								<th class="flsize">${planorder.plan_order}</th>
								<th style="width:90px;text-align: right;padding-top: 13px;">总件数:</th>
								<th class="flsize">${planorder.total_number}</th>
							</tr>
							<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">总重量:</th>
								<th class="flsize">${planorder.total_weight}</td>
								<th style="width:90px;text-align: right;padding-top: 13px;">总体积:</th>
								<th class="flsize">${planorder.total_volume}</th>
							</tr>
							<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">始发地:</th>
								<th  class="flsize">			
								   <%-- <div class="layui-input-inline">
								      <input type="text" class="layui-input" <c:if test="${!empty planorder.originating  }"> disabled="disabled"</c:if> value="太原" name="originating" id="originating" value="${planorder.originating }">
								    </div> --%>
								  <c:if test="${!empty planorder.originating  }"> 
								   <div class="layui-input-inline">
								 		<input type="text" class="layui-input"  disabled="disabled" value="${planorder.originating }"  name="originating" id="originating" >
									</div>
								 </c:if>
								 <c:if test="${empty planorder.originating  }"> 
								 <div class="layui-input-inline">
								 		<input type="text" class="layui-input" disabled="disabled" value="太原" name="originating" id="originating" >
								 		</div>
								 </c:if>
													</th>
								<th style="width:90px;text-align: right;padding-top: 13px;">目的地:</th>
								<th  class="flsize">
								 <div class="layui-inline">
							      <div class="layui-input-inline">
							        <select  name="kilometreid" id="kilometreid" lay-verify="required" lay-search="">
							           	<option value="">选择目的地</option>
										<c:forEach items="${kilometre}" var="kilometre">
										
											<option value="${kilometre.id }" <c:if test="${planorder.kilometre_id==kilometre.id }">selected="selected"</c:if>>${kilometre.sitename}</option>
										</c:forEach>
							        </select>
							      </div>
							    </div>
							</tr>
							<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">选择司机:</th>
								<th   class="flsize">
								<div class="layui-input-inline" style="float: left">
									<c:if test="${!empty planorder.driver_name }">${planorder.driver_name}</c:if>
										<c:if test="${empty planorder.driver_name}">
											<input type="hidden" name="phone" id="phone">
											<input type="hidden" name="cph" id="cph">
											<input type="hidden" name="have" id="havetype">
											<input class="layui-input" onclick="seachProduct()" name="productName" id="productName" value="" type="text" placeholder="请选择司机" /> 
										</c:if>
								</div>
								 <div class="layui-inline" style="float:left;margin:10px 0 0 10px;">
							      <a  onclick="seachProduct()" title="选择司机"> <i class="layui-icon layui-icon-search" style="font-size: 20px; color: #1E9FFF;"></i>   </a> 
							    </div>
							     <div class="layui-inline" style="float:left;margin-top:10px;">
							     <a href="javascript:revokedrsj('${planorder.plan_order}')" style="padding-left: 20px;font-size: 10px">撤销</a>
							    </div>
								</th>
							<th style="width:90px;text-align: right;padding-top: 13px;">预计金额:</th>
								<th  class="flsize">
								
									${planorder.final_amount}
									 <div class="layui-inline">
									<c:if test="${planorder.type==0}">
									<input type="text" name="amount" id="amount" placeholder="修改金额" class="layui-input"></c:if>
									</div>
								</th>
							</tr>
							<tr>
							<th style="width:90px;text-align: right;padding-top: 13px;">选择车型:</th>
								<th  class="flsize">
								 <div class="layui-inline" id="cartype">
							      <div class="layui-input-inline">
							        <select name="models" id="models" lay-verify="required" lay-search="">
							           	<option value="">选择车型</option>
										<c:forEach items="${models}" var="models">
											<option value="${models.id }" <c:if test="${planorder.models_id==models.id }">selected="selected"</c:if>>${models.models}</option>
										</c:forEach>
							        </select>
							        
							      </div>
							    </div>
							    <input type="text" name="models_id" id="models_id" style="display:none" disabled="disabled">
							    </th>
								<th style="width:90px;text-align: right;padding-top: 13px;">创建日期:</th>
								<th class="flsize">${planorder.create_time.substring(0,10)}</th>
							</tr>
							<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">司机到达时间:</th>
								<th > 
								<div class="layui-input-inline">
							        <input type="text" class="layui-input"  name="lastLoginStart" value="${planorder.siji_time.substring(0,19)}" id="lastLoginStart"  placeholder="司机到达时间">
							      </div>
								</th>
								<th style="width:90px;text-align: right;padding-top: 13px;">备注:</th>
								<th class="flsize"> <div class="layui-inline"><input type="text" id="comment"  class="layui-input"value="${planorder.comment }"></div></th>
							</tr>
							<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">到店时间:</th>
								<th colspan="3"> 
								<div class="layui-input-inline">
							        <input type="text" class="layui-input"  name="delivery_time" value="${planorder.delivery_time.substring(0,19)}" id="delivery_time"  placeholder="到店时间">
							      </div>
								</th>
							</tr>
							<tr>
								<th style="width:90px;text-align: right;padding-top: 13px;">关联订单号:</th>
								<th colspan="3">
									<c:forEach items="${planOrderItem}" var="item" varStatus="vs">
										<div style="padding:10px 0 10px 0;font-weight:normal;color: #08c">
										<a onclick="editProductNumber('${item.order_num}',1)">${item.order_num}</a>
										<span style="padding-left:10px">${item.merchant_id}</span>
										<span style="padding-left:10px">${item.merchant}</span>
										 <button style="" onclick="print('${item.order_num}')" class=" btn-app btn-light btn-mini">
											<i class="icon-print"></i>打印 </button>
											<c:if test="${planorder.type==0||planorder.type==1}">
												<a href="javascript:revokeNum('${planorder.plan_order}','${item.order_num}')" style="padding-left: 20px;font-size: 10px">撤销</a>
											</c:if>
										</div>
										
									</c:forEach>
								</th>
							</tr>
							
							<c:if test="${planorder.type==0}">
							<tr>
								<th colspan="4" >
									<div style="margin-left:49%"><button  onclick="editting();" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="修改">修改</button></div>
								</th>
							</tr>
							</c:if>
						</table>
						<c:if test="${planorder.type==0}">
							<div style="padding:10px 20px;float:right">
							<button  onclick="plan();" type="button" class="layui-btn layui-btn-radius layui-btn-small" title="发布">发布</button>
							</div>
						</c:if>
						<table class="layui-table">
							<thead>
								<tr>
									<th>商品名称</th>
									<th>商品条码</th>
									<th>数量</th>
									<th>总重量</th>
									<th>总体积</th>
								</tr>
							</thead>
							<tbody>
								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty planlist}">
										<c:forEach items="${planlist}" var="o" varStatus="vs">
											<tr>
												<td>${o.product_name}</td>
												<Td name="barCode">${o.bar_code}</Td>
												<Td>${o.final_quantity}</Td>
											    <Td><fmt:formatNumber  type="number" value="${o.sku_weight*o.final_quantity}" minFractionDigits="4"  maxFractionDigits="4"  /></Td>
											    <Td><fmt:formatNumber  type="number" value="${o.sku_volume*o.final_quantity}" minFractionDigits="4"  maxFractionDigits="4"  /></Td>
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
					</form>
				</div>
				<!-- PAGE CONTENT ENDS HERE -->
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
	<script src="static/layui/layui.all.js" charset="utf-8"></script>
	<!--提示框-->
	<script type="text/javascript">
		layui.use(['form','laydate'], function(){
			 var layer = layui.layer
  				,form = layui.form,
			  laydate = layui.laydate;
			  laydate.render({
			    elem: '#lastLoginStart' //指定元素
			     ,type: 'datetime'
			  });
			  laydate.render({
			    elem: '#delivery_time' //指定元素
			     ,type: 'datetime'
			  });
		});
		  
		
		//订单状态为0 可以删除司机信息
		function revokedrsj(orderNum){
			var url="planOrder/updateplanOrderNum?plan_order="+orderNum+"&type="+0;
			$.post(url,function(data){
				if(data){
		  			layer.msg("删除司机信息成功！",{icon:1,time: 2000},function(){window.location.href=window.location.href;}); 
		  		}else{
		  			layer.msg(data,{icon:1,time: 2000},function(){window.location.href=window.location.href;}); 
		  		}
			});
		}
		function revokeNum(planNum,orderNum){
			var url="planOrder/updateplanOrderNum?plan_order="+planNum+"&order_num="+orderNum+"&type=${planorder.type}";
			$.post(url,function(data){
				if(data){
		  			layer.msg("删除订单信息成功！",{icon:1,time: 2000},function(){window.location.href=window.location.href;}); 
		  		}else{
		  			layer.msg(data,{icon:1,time: 2000},function(){window.location.href=window.location.href;}); 
		  		}
			});
		}
		
		//获取页面的input的值
		  function plan(){
		  	var index = layer.load(1, {
			  shade: [0.3,'#fff'] //0.1透明度的白色背景
			});
		    if(!isnull(index)){
		    	return ;
		    }
		  	var url="planOrder/releasePlan?plan_order=${planorder.plan_order}";
		  	$.post(url,function(data){
		  		layer.close(index);
		  		if(data){
		  			layer.msg("计划发布成功！",{icon:1,time: 3000},function(){window.location.href=window.location.href;}); 
		  		}else{
		  			layer.msg(data,{icon:1,time: 3000},function(){window.location.href=window.location.href;}); 
		  		}
		  	})
		  }
			 //查看出库单
		  function editProductNumber(id1,type){
			var url='<%=basePath%>exwarehouseorder/goExwareorderProductEdit.do?orderId='+id1+'&type='+type;
				top.mainFrame.tabAddHandler("3920608325","查看出库单信息",url);
		 }
		 function editting(){
		  	var index = layer.load(1, {
			  shade: [0.3,'#fff'] //0.1透明度的白色背景
			});
		 	if(!isnull(index)){
		    	return ;
		    }
		 	var url="planOrder/updateplan?plan_order=${planorder.plan_order}&originating="+$("#originating").val()+"&have="+$("#havetype").val()+"&models_id="+$("#models_id").val()
		 	+"&amount="+$("#amount").val()+"&kilometre="+$("#kilometreid").val()+"&models="+$("#models").val()+"&comment="+$("#comment").val()
		 	+"&driver_platenumber="+$("#cph").val()+"&driver_phone="+$("#phone").val()+"&driver_name="+$("#productName").val()+"&type=${planorder.type}&siji_time="+$("#lastLoginStart").val()+"&delivery_time="+$("#delivery_time").val();
		 	$.post(url,function(data){
		 		layer.close(index);
		 		if(data){
		  			layer.msg("修改成功！",{icon:1,time: 1000},function(){window.location.href=window.location.href;}); 
		  		}else{
		  			layer.msg("修改失败");
		  		}
		  	})
		 }
		 function isnull(index){
			var originating=$("#originating").val();
		 	var kilometre=$("#kilometreid").val();
		 	var models=$("#models").val();
		 	var delivery_time=$("#delivery_time").val();
		 	var lastLoginStart=$("#lastLoginStart").val();
		 	if(originating==""){
		 		layer.close(index);
		 		layer.msg("始发地不能为空！");
		 		return false;
		 	}
		 	if(kilometre==""){
		 		layer.close(index);
		 		layer.msg("目的地不能为空！");
		 		return false;
		 	}
		 	if(models==""&&$("models_id").val()==""){
		 		layer.close(index);
		 		layer.msg("车辆类型不能为空！");
		 		return false;
		 	}
		 	if(delivery_time==""){
		 		layer.close(index);
		 		layer.msg("到店时间不能为空！");
		 		return false;
		 	}
		 	if(lastLoginStart==""){
		 		layer.close(index);
		 		layer.msg("司机到达不能为空！");
		 		return false;
		 	}
		 	
		 	return true;
		 }
		 
		 	 var _product;
function seachProduct(){
	_product=layer.open({
		 title:'司机信息',
		 type: 1,
		 skin: 'layui-layer-rim', //加上边框
	     area: ['80%', '80%'], //宽高
		 content: '<div class="demoTable" style="float:left">'+
		 '<div class="layui-inline"><input type="text" name="title" placeholder="请输入司机名称" id="driver_name" class="layui-input" autocomplete="off"></div>'
		 +'</div><button class="layui-btn" data-type="reload" onclick="productName()">搜索</button><table class="layui-hide" id="demo" lay-filter="demo"></table></div>',
		 success: function(layero){
			  $(document).keydown(function (event) {
				    if (event.keyCode == 13) {
				    	productName();
				    	return false;
				    };
			  });
		 }
	});
	productName();
}
function productName(){
  var url='planOrder/selectDriver.do?driver_name='+$("#driver_name").val();
  $.post(url,function(data){
		  layui.use('table', function(){
		  var table = layui.table;
		  table.render({
		    elem: '#demo'
		    ,page:'false'
		    ,cols: [[
		      {checkbox: true, fixed: true },
		       {field:'REALNAME', title: '司机名称'}
		      ,{field:'PHONE', width:80, title: '联系方式', sort: true}
		      ,{field:'CAR_NUMBER', title: '车牌号'}
		      ,{field:'CARSTATE', title: '车型'}
		      ,{field:'type',title: '类型', sort: true, templet: function(d){
			      if(d==1){return "自有"}else{ return "挂靠"} }}
		    ]],
		    data:data
		  });
		    table.on('checkbox(demo)', function(obj){
		    	console.log(obj);
		    	prodsell(obj.data.REALNAME,obj.data.PHONE,obj.data.CAR_NUMBER,obj.data.CARSTATE,obj.data.type);
		    	//prodsell(\'"+item.REALNAME+"\',\'"+item.PHONE+"\',\'"+item.CAR_NUMBER+"\',\'"+item.CARSTATE+"\',\'"+item.type+"\')\">"
			    layer.close(_product);
			});
		});	
		
			 
 },"json")

}

	/*  function productName(){
			 layui.use(['form'], function(){
				  var form = layui.form;
				  var url='planOrder/selectDriver.do?driver_name='+$("#driver_name").val();
					 $.post(url,function(data){
						 layui.use(['laypage', 'layer'], function(){
						  var laypage = layui.laypage,layer = layui.layer;
						  var nums = 5; //每页出现的数据量
						  var json=eval(data.list);
						  console.log(json);
						  var render = function(json, curr){
						    var arr = []
						    ,thisData = json.concat().splice(curr*nums-nums, nums);
						     layui.each(thisData, function(index, item){
						     	var type="";
						     	if(item.type==1){
						    		type="自有车辆"; 	
						    	}else{
						    		type="挂靠车辆";
						    	}
						    	 arr.push("<tr><td><div class=\"layui-form-item\" "
						    			 +"onclick=\"prodsell(\'"+item.REALNAME+"\',\'"+item.PHONE+"\',\'"+item.CAR_NUMBER+"\',\'"+item.CARSTATE+"\',\'"+item.type+"\')\">"
						    			 +"<input type='radio'  title=' ' value='"+item.id+"' ></div></td>"
						    			 +"<td>"+item.REALNAME+"</td><td>"+item.PHONE+"</td><td>"+item.CAR_NUMBER+"</td><td>"+item.CARSTATE+"</td><td>"+type+"</td>");
						        arr.push("</tr>");
						    });
						    return arr.join('');
						  };	 
						 laypage({
							    cont: 'product_list'
							    ,pages: Math.ceil(json.length/nums) //得到总页数
							    ,jump: function(obj){
							    	$("#product_id").find("tbody").html(render(json,obj.curr));
							    	form.render("radio"); 
							    }
							  }); 
					});	
					 });
				});
		 } */
		 function prodsell(name,phone,cph,cartype,type){
		 $("#phone").val(phone);
	     $("#productName").val(name);
	     $("#cph").val(cph);
	     $("#havetype").val(type);
	     $("#cartype").css("display","none");
	     $("#models_id").css("display","block");
	     $("#models_id").val(cartype);
	 	 layer.close(_product);
	}	 
		 
		</script>
</body>
</html>

