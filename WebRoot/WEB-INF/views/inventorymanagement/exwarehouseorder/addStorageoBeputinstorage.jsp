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

	
	</head> 


<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-skins.min.css" />
	<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/js/jQuery.print.js"></script>
		<link rel="stylesheet" href="<%=basePath%>plugins/zTree/3.5/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<%=basePath%>plugins/zTree/3.5/jquery.ztree.core-3.5.js"></script>
</head>
	<style type="text/css">
ul.ztree {/* margin-top: 10px; *//* border: 1px solid #167169; */background: #f0f6e4;/* width:220px; */height:360px;overflow-y:scroll;overflow-x:auto;}
ul.log {border: 1px solid #617775;background: #f0f6e4;width:300px;height:170px;overflow: hidden;}
ul.log.small {height:45px;}
ul.log li {color: #666666;list-style: none;padding-left: 10px;}
ul.log li.dark {background-color: #E3E3E3;}
li {list-style: circle;font-size: 12px;}
li.title {list-style: none;}
ul.list {margin-left: 17px;}
</style>		
		<script type="text/javascript">
		var typeId="";
		$(function(){
			var setting = {
					view: {
						dblClickExpand: false,
						selectedMulti: false
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						beforeClick: beforeClick,
						onClick: onClick
					}
					
				};
			
			var zNodes = [];//createProductTypeTree
			$.ajax({
				type: "POST",
				url: '<%=basePath%>productTypeTree/getProudctTypeList.do',
				dataType:'json',
				cache: false,
				async:false,
				success: function(data){
					if(data!=null&&data!=''){ 
						zNodes = new Array(eval(data.menuTree));
					}
				}
			});
			
			
			
			$.fn.zTree.init($("#treeDemo"), setting, zNodes[0]);
			
			
			
		});
		
		
		
		function setZtree(productType){
			   var treeObj = $.fn.zTree.getZTreeObj("treeDemo"); 
			   var node = treeObj.getNodeByParam("id", productType, null);
			   treeObj.selectNode(node);
			   var cityObj = $("#producttype");
				cityObj.attr("value", node.name );
				$("#PRODUCT_TYPE_ID").val(productType);
		   }

			function showMenu() {
				var cityObj = $("#producttype");
				var cityOffset = $("#producttype").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
			}
			
			

			function beforeClick(treeId, treeNode) {
				var check = (treeNode && !treeNode.isParent);
				if (!check) alert("只能选择最后一个...");
				return check;
			}
			
			
			function onClick(e, treeId, treeNode) {
				 typeId=treeNode.id;
				var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
				nodes = zTree.getSelectedNodes(),
				v = "";
				nodes.sort(function compare(a,b){return a.id-b.id;});
				for (var i=0, l=nodes.length; i<l; i++) {
					v += nodes[i].name + ",";
				}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				var cityObj = $("#producttype");
				cityObj.attr("value", v);
			}

			
			function hideMenu() {
				$("#menuContent").fadeOut("fast");
				$("body").unbind("mousedown", onBodyDown);
			}
			function onBodyDown(event) {
				if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
					hideMenu();
				}
			}
			
			
		
		
		
		
		
		function dy(){
			  $("#Form").print({
				  globalStyles: true,
				  noPrintSelector: "#noprint"
			  });
			
		}
		
		 //选中便利店
		 function selectSupplier(){
			 var orderdate= $("#order_date").val();
			 if(orderdate==""){
				 alert("请选择日期");
				 return false;
			 }
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="选择门店";
			 diag.URL = '<%=basePath%>merchant/selectlist.do';
			 diag.Width = 1000;
			 diag.Height = 700;
			 diag.CancelEvent = function(){ //关闭事件
			
				diag.close();
			 };
			 diag.OKEvent = function(){
				var merchantid=diag.innerFrame.contentWindow.$('input[name="merchantid"]:checked').val();
				var merchant_name=diag.innerFrame.contentWindow.$("#merchant_name"+merchantid).text();
				var dz =diag.innerFrame.contentWindow.$("#address"+merchantid).text();
				var lxr=diag.innerFrame.contentWindow.$("#contact_person"+merchantid).text();
				var sj=diag.innerFrame.contentWindow.$("#mobile"+merchantid).text();
	            $("#merchantname").val(merchant_name);
	            $("#merchantid").val(merchantid);
	            $("#sj").text(sj);
	            $("#dz").text(dz);
	            $("#lxr").text(lxr);
	            var comment=$("#comment").val();
			     var ordernum=$("#orderNum").val();
				 var orderdate=$("#order_date").val();
				 $.post("<%=basePath%>exwarehouseorder/Generatethestorereceipt.do",{supplierId:merchantid,comment:comment,ordernum:ordernum,orderdate:orderdate},function(data){
	         	       var datas=data.split(",");
	         	       $("#orderNum").val(datas[0]);
	         	       $("#order_num").text(datas[0]);
	         	      $("#groupNum").val(datas[1]);
	         	       $("#enwarsehouseId").val(datas[2]);
	         	      $("#nav-search-icon").hide();
	         	        
	          });
				 
	            diag.close();
			 };
			 diag.show();
		 }
		</script>
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">


	<div class="row-fluid">
	     <button style="float: right;" onclick="dy()" class=" btn-app btn-light btn-mini">
		<i class="icon-print"></i>
		打印
		</button>
			<form action="exwarehouseorder/goExwareorderProductEdit.do?orderId=${enwarhouse.order_num}	" method="post" name="Form" id="Form">
		<table id="table_report" class="table table-striped table-bordered table-hover">
		  <tr><td style="width:100px;text-align: right" >基本信息:</td></tr>
		
		<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">出库单编号:</td>
				<td id="order_num">
			         ${enwarhouse.order_num}
				</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">便利店:</td>
				<td >
				<input type="text" id="merchantid" style="display: none" />
				<input type="text" id="enwarsehouseId" value="${enwarhouse.id}" style="display: none" />
				<input type="text" id="groupNum" value="${enwarhouse.order_num}" style="display: none" />
				<input type="text" id="orderNum" value="${enwarhouse.order_num}" style="display: none" />
				<input type="text" disabled="disabled" id="merchantname" value=" ${enwarhouse.merchant_num}| ${enwarhouse.merchant_name}" />
			           	<a class="btn btn-mini btn-light" onclick="selectSupplier()" title="选择便利店"><i id="nav-search-icon" class="icon-search"></i></a>
				</td>
				 
			</tr>
			<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">订购日期:</td>
				<td>
				<input class="span10 date-picker" name="order_date" id="order_date" value="${enwarhouse.create_time}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="订购日期" title="订购日期">
			          
				</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">出库日期:</td>
				<td>
				 
				</td>
				 
			</tr>
			<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">配送方式:</td>
				<td>
			         送货上门
				</td>
				 <td style="width:90px;text-align: right;padding-top: 13px;">配送地址:</td>
				<td id="dz">
			          ${enwarhouse.address}
				</td>
				 
			</tr>
			<tr>
			  <td style="width:120px;text-align: right;padding-top: 13px;">便利店联系人:</td>
				<td id="lxr">
			          ${enwarhouse.manager_name}
				</td>
				 <td style="width:90px;text-align: right;padding-top: 13px;">联系电话:</td>
				<td id="sj">
			          ${enwarhouse.manager_tel}
				</td>
			</tr>
			    <tr>  
			     <td>总体积: </td><Td id="amount">${enwarhouse.total_svolume}</Td> 
			    <td>总重量:<Td id="finalAmount">${enwarhouse.total_weight}</td>
			     </tr> 
			
	
		 	 
			
	 </table>
			<!-- 检索  -->
			<!-- 检索  -->
			<table id="table_report" class="table table-striped table-bordered table-hover">
				
				<thead>
					<tr>
						 
						<th>商品条形码</th>
						<th>商品名称</th>
					
						<th>规格</th>
						<th>销售单价</th>
						 <th>销售总价</th>
						 <th>数量</th>
						  <th>实际销售总价</th>
						  <th>实际数量</th>
						  <th colspan="2" >备注</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty orderItemList}">
						<c:forEach items="${orderItemList}" var="o" varStatus="vs">
							<tr>
							  <Td>${o.bar_code}</Td>
							  <Td>${o.product_name}</Td>
							
							   <Td>${o.specification}</Td>
							     <td>${o.sale_price}</td>
							      <Td>${o.price1}</Td>
							      <td><fmt:formatNumber type="number" value="${o.quantity}" maxFractionDigits="0"/> </td>
							       <Td>${o.price}</Td>
							     
								<c:if test="${type eq  1}">
													<td><c:if test="${enwarhouse.ivt_state eq 1}">
															<input name="productNumber" type="text"
															 value='<fmt:formatNumber type="number" value="${o.final_quantity}" maxFractionDigits="0"/> '  id="input${o.id}">
														</c:if> 
														<c:if test="${enwarhouse.ivt_state eq 2}">
														 <p id="p${o.id}">
														 
														 <fmt:formatNumber type="number" value="${o.final_quantity}" maxFractionDigits="0"/> 
														 </p>
														</c:if>
														 <input style="display: none" type="text" name="orderItemId" VALUE="${o.id}">
													</td>
												</c:if>
										 <td colspan="2" >${o.comment}</td>		
							</tr>
						</c:forEach>
				
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
					
				 <c:if test="${enwarhouse.ivt_state eq 1}">
										<tr id="noprint">

									<td colspan="2">
									
										<ul class="list">
			<li class="title">
			<input id="producttype" type="text" readonly value="" style="float:left; "/>&nbsp;
			<input name="PRODUCT_TYPE_ID" id="PRODUCT_TYPE_ID" type="text" style="display: none" />
			<a id="menuBtn" onclick="showMenu();return false" >选择</a>
			</li>
		    </ul>
		    </td>
									
									
									
				<td colspan="4">					 选择商品: <input type="text" name="productId" id="enProductId"
										style="display: none"> <input name="productName"
										id="productName" value="" type="text" readonly="readonly"
										placeholder="请选择商品" /> <a class="btn btn-mini btn-light"
										onclick="seachProduct()" title="选择商品"><i id="nav-search-icon" class="icon-search"> </i> </a> 
										</td>
										<td colspan="3">
									   数量:<input type="text" id="quantity"  value=""> 
										<a onclick="addProduct()" class="btn btn-mini btn-primary" >增加 </a>
										<a class="btn btn-mini btn-primary"  style="text-align: center;" onclick="editProductNum()">确定</a>
										</td>
									<td colspan="1"></td>
								</tr>
								</c:if>
								<tr>
								<td colspan="8"></td>
								<td>
							   <font style="color: red;">出库总额:${enwarhouse.amount}</font>
								</td>
								<td>
								<font style="color: red;">实际出库总额:${enwarhouse.final_amount}</font>
								</td>
								</tr>
				</tbody>
			</table>
			</form>
		<div class="page-header position-relative">
		<table style="width:100%;">
			<tr>
				<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
			</tr>
		</table>
	
		</div>
 
	</div>
 
 
 
 
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
</div><!--/#page-content-->
</div><!--/.fluid-container#main-container-->
		
		<!-- 返回顶部  -->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul>
</div>
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
		
		  
		//日期框
		$('.date-picker').datepicker({
		});
		//检索
		function search(){
			top.jzts();
			$("#productForm").submit();
		}
		
	 
		 
			 //修改商品数量
			 
		  function editProductNumber(id1){
			var url='<%=basePath%>exwarehouseorder/goStorageExwareorderProductEdit.do?type=1&orderId='+id1;
				top.mainFrame.tabAddHandler("392060853","查看出库单信息",url);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
			 
		 } 
	 
		 
		
		
	 
		 

		
			//添加商品信息  
			 function addProduct(){
				 var numberTest=/^[0-9]*$/;
				 if( $("#quantity").val()==""){
					 
					 $("#quantity").tips({
				  			side:3,
				              msg:'数量不能为空',
				              bg:'#AE81FF',
				              time:2
				          });
				  		$("#quantity").focus();
				    	  return ; 
				 } 
				  if(!numberTest.test($("#quantity").val())){
				    	 
		    		  $("#quantity").tips({
		    	  			side:3,
		    	              msg:'入库数量必须是数字',
		    	              bg:'#AE81FF',
		    	              time:2
		    	          });
		    	  		$("#quantity").focus();
		    	    	  return ; 
		     }
					 if( $("#productName").val()==""){
						 
						 $("#productName").tips({
			    	  			side:3,
			    	              msg:'请选择商品',
			    	              bg:'#AE81FF',
			    	              time:2
			    	          });
			    	  		$("#productName").focus();
			    	    	  return ; 
					 }
					  
					
					
				     
					
				
				
					var salePrice= productData.product.salePrice;//商品的价格 信息
					var purcahsePrice=productData.product.PurchasePrice;
					var sku_volume=productData.product.sku_volume;//单个商品的体积
					var sku_weight=productData.product.sku_weight;//单个商品的重量
					var productId =productData.product.id;//选择的商品Id
					var orderId=$("#enwarsehouseId").val();
					var orderNum=$("#orderNum").val();//选择的订单号
					var groupNum=$("#groupNum").val();//选择的批次号
					var quantity=$("#quantity").val();
					
					var url="<%=basePath%>exwarehouseorder/saveEnOrderItem.do";
					 $.post(url,{order_num:orderNum,orderId:orderId,group_num:groupNum,product_id:productId,
						 sale_price:salePrice,
						 purchase_price:purcahsePrice,
						 sku_volume:sku_volume,
						 sku_weight:sku_weight,
						 bkType : 3,
						 quantity:quantity,final_quantity:quantity},function(data){
						   if(data!="false"){
							   alert("添加成功");
							   editProductNumber(orderNum);
							   
						   }
					 });
			 } 
		
				
				
			 //存放data 信息 用于添加data 信息
			 var productData;
			 
			 
			 //搜索商品
			  function seachProduct(){
			 	   
			

			 	
			 	 top.jzts();
			 	 var diag = new top.Dialog();
			 	 diag.Drag=true;
			 	 diag.Title ="选择商品";
			 	 diag.URL = '<%=basePath%>product/findProudctBytypeId.do?typeid='+typeId;
			 	 diag.Width = 700;
			 	 diag.Height = 500;
			 	 diag.CancelEvent = function(){ //关闭事件
			 	
			 		diag.close();
			 	 };
			 	 diag.OKEvent = function(){
			 		 
			 		
			 		 var pid=diag.innerFrame.contentWindow.$('input[name="productId"]:checked').val();
			 	                   $("#enProductId").val(pid);
			 		    $.post("<%=basePath%>/product/findProductById?productId="+pid,function(data){
			 		    	   
			 		    	        productData=data;
			 		    	        $("#productName").val(data.product.product_name);  
			 		    	       
			 		    	        
			 		    });
			         diag.close();
			 	 };
			 	 diag.show();
			 	
			 	 
			  }
		
				 
				function editProductNum(id){
					var numberTest=/^[0-9]*$/;
					var orderId='${enwarhouse.id}';
				   var orderNum='${enwarhouse.order_num}';
				   var ids='';
				   var numbers='';
					var newJson=getOrderItem();
					  
						  
					 var orderItemIds=$('input[name="orderItemId"]');
					  for(var i =0;i<orderItemIds.length;i++){
						      var id=orderItemIds[i].value;
						     if(oldJson[id]!=newJson[id]){
						    	   if(!numberTest.test($("#input"+id).val())){
								      	 
							    		  $("#input"+id).tips({
							    	  			side:3,
							    	              msg:'必须是数字或者不能含有空格',
							    	              bg:'#AE81FF',
							    	              time:2
							    	          });
							    	  		$("#input"+id).focus();
							    	    	  return ; 
							     }
						       ids+=id+",";
						       numbers+=newJson[id]+",";
				          }
					   }	
					 
					   if(ids==''){
						    
						   return ;
					   }
					
					var url="<%=basePath%>exwarehouseorder/updateEnOrderProduct.do";
					 $.post(url,{ids:ids,numbers:numbers,orderId:orderId},function(data){
						          if(data=="true"){
						        alert("修改成功");
						     editProductNumber(orderNum);
						          }else{
						        	    alert("修改失败");
						          }
					 });
					
				
				}

				  
				  //获取页面的input的值
				  
				  function getOrderItem(){
					  var json={};
						 var orderItemIds=$('input[name="orderItemId"]');
						  var productNumbers=$('input[name="productNumber"]');
						   for(var i =0;i<orderItemIds.length;i++){
							   var id=orderItemIds[i].value;
							   var productNumber=productNumbers[i].value;
							      json[id]=productNumber;
						   }
						   
						   return json;
					  
				  }
				  
				  
				  //页面加载获取数量以及id的值
				  var oldJson;
				 $(function(){
				    oldJson=	 getOrderItem();
				 });
				 
				 
	
		</script>
		
	
		
	</body>
</html>

