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
	
<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
	<link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
	
      <link href="static/css/bootstrap.min.css" rel="stylesheet" />
	<!-- page specific plugin styles -->
	<!-- 下拉框-->

	<!-- ace styles -->
	<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-skins.min.css" />
		<script type="text/javascript">
		
		function print(){
			window.open("<%=basePath%>enwarehouseorder/print.do?orderId=${enwarhouse.order_num}&type=1", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
		}
		</script>
		<style type="text/css">
  		.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:30px}
  		#searchcriteria{width:202px;height:38px}
  		
  	</style>
<body>

	<div class="container-fluid" id="main-container">


		<div id="page-content" class="clearfix">

			<div class="row-fluid">


				<div class="row-fluid">
					<button style="float: right;" onclick="print()"
						class=" btn-app btn-light btn-mini">
						<i class="icon-print"></i> 打印预览
					</button>
					<form
						action="sellingorder/goEnwareorderProductEdit.do?orderId=${enwarhouse.order_num}	"
						method="post" name="Form" id="Form">
						<table id="table_report"
							class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:100px;text-align: right">基本信息:</td>
							</tr>

							<tr>
								<td style="width:90px;text-align: right;padding-top: 13px;">入库单编号:</td>
								<td>${enwarhouse.order_num}</td>
								<td style="width:90px;text-align: right;padding-top: 13px;">供应商:</td>
								<td>${enwarhouse.supplier_num}| ${enwarhouse.supplier_name}
								</td>
							</tr>
							<tr>
								<td style="width:90px;text-align: right;padding-top: 13px;">订购日期:</td>
								<td>${enwarhouse.create_time}</td>
								<td style="width:90px;text-align: right;padding-top: 13px;">入库日期:</td>
								 <Td>
								 <c:if test="${enwarhouse.ivt_state  eq 2}">
								 
								 ${enwarhouse.order_date}
								  </c:if>
								 </Td>
								<td id="supplierId"  style="display: none">${enwarhouse.supplier_id}</td>
							</tr>
							<tr>
								<td style="width:90px;text-align: right;padding-top: 13px;">供应商联系人:</td>
								<td>${enwarhouse.contact_person}</td>
								<td style="width:90px;text-align: right;padding-top: 13px;">联系电话:</td>
								<td>${enwarhouse.contact_person_mobile}</td>
							</tr>
							<Tr>
								<td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>
								<td>${enwarhouse.comment}</td>
								<td style="width:90px;text-align: right;padding-top: 13px;">状态:</td>
								<td><c:if test="${enwarhouse.checked_state eq 1}">
			      
			                待入库
			      
			      </c:if> <c:if test="${enwarhouse.checked_state eq 2}">

			                 已入库
			      
			      </c:if></td>


							</Tr>

                 
			
						</table>
						<!-- 检索  -->


						<!-- 检索  -->


						<table id="table_report"
							class="table table-striped table-bordered table-hover">

							<thead>
					
								<tr	 >
       <th 	class='center'  >序号</th>
									<th class='center'>商品条形码</th>
									<th class='center'>商品名称</th>
									<th class='center'>规格</th>
									<th class='center'>采购单价</th>
									<th class='center'>采购数量</th>
									<th class='center'>入库数</th>
									<th class='center'>赠品数</th>
									<th class='center'>生产日期</th>
									<th	class='center'>采购总价</th>
									<th class='center'>备注</th>
									<th class='center'>商品批次号</th>
								</tr>
							</thead>

							<tbody>

 	
								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty orderItemList}">

										<c:forEach items="${orderItemList}" var="o" varStatus="vs">
											<tr>
												<tr> <td class='center' >${vs.index+1}</td>
												<Td class='center' >${o.bar_code}</Td>
												<Td class='center' >${o.product_name}</Td>
												<Td class='center' >${o.specification}</Td>
												<td class='center' >${o.purchase_price}</td>
												<td class='center' ><fmt:formatNumber type="number" value="${o.quantity}" maxFractionDigits="0"/></td>
											
												<c:if test="${type eq  1}">
													<td 	class='center' ><c:if test="${enwarhouse.checked_state eq 1}">
															<input name="productNumber" style="width:50px;"  type="text"
																value="<fmt:formatNumber type="number" value="${o.final_quantity}" groupingUsed="false" maxFractionDigits="0"/>" id="input${o.id}">
														</c:if> 
														
														<c:if test="${enwarhouse.checked_state eq 2}">
														 <p id="p${o.id}"><fmt:formatNumber type="number" value="${o.final_quantity}" maxFractionDigits="0"/></p>
														</c:if>
														
														 

													</td>
													<td class='center'>
														<c:if test="${enwarhouse.checked_state eq 1}">
															<input name="gift_quantity" style="width:50px;"
																type="text"
																value="<fmt:formatNumber type="number" value="${o.gift_quantity}" maxFractionDigits="0"/>"
																id="input${o.id}">
														</c:if> 
														<c:if test="${enwarhouse.checked_state eq 2}">
															<p id="p${o.id}">
																<fmt:formatNumber type="number"
																	value="${o.gift_quantity}" maxFractionDigits="0" />
															</p>
														</c:if> <input style="display: none" type="text"
														name="orderItemId" VALUE="${o.id}"></td>
													<td class='center'>
													<input type="text" class="layui-input test-item" name="product_time" id="product_time${o.id}"
													value="${o.product_time}" readonly="readonly" placeholder="生产日期" >
													<!-- <input type="text" class="layui-input test-item" placeholder="yyyy-MM-dd"> -->
												</td>
														 <td class='center'>${o.price1}</td>
												</c:if>
												<td class='center'>${o.comment}</Td>
												<td class='center'><input  type="text" name="product_group_num" id="product_group_num" VALUE="${o.product_group_num}"></td>
											</tr>
										</c:forEach>

									</c:when>
									<c:otherwise>
										<tr class="main_info">
											<td colspan="10" class="center">没有相关数据</td>
										</tr>
									</c:otherwise>
								</c:choose>

                     <c:if test="${enwarhouse.checked_state eq 1}">
								<tr id="noprint">
									<td colspan="8"   >   
									  选择商品: <input type="text" name="productId" id="enProductId"
										style="display: none"> <input name="productName"
										id="productName" value="" type="text" readonly="readonly"
										placeholder="请选择商品" /> <a class="btn btn-mini btn-light"
										onclick="seachProduct()" title="选择商品"><i
											id="nav-search-icon" class="icon-search"> </i> </a> 数量:<input
										type="text" id="quantity"  value=""> <a
										onclick="addProduct()" class="btn btn-mini btn-primary" >增加 </a></td>
									
									<td  class='center'  ><a class="btn btn-mini btn-primary"  style="text-align: center;"
										onclick="editProductNum()">确定</a></td>
										<td ></td><td ></td>
										<td></td>
								</tr>
								</c:if>
							</tbody>
							 <tr>
			        <td colspan="8"></td>
			     
			        <td colspan="4" id="finalAmount">
			        <font style="color: red;">实际入库单总额:${enwarhouse.final_amount}元</font>
			        
			          <font style="color: red;">总数: <fmt:formatNumber type="number" value="${enwarhouse.final_quantity}" maxFractionDigits="0"/> </font>
			          </td>
			        </tr>  
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
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>

	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<script src="static/layui/layui.js" charset="utf-8"></script>
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->


	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script>
		
		layui.use(['form', 'layedit', 'laydate'], function(){
		  var form = layui.form
		  ,layer = layui.layer
		  ,layedit = layui.layedit
		  ,laydate = layui.laydate;
		  //日期
		 lay('.test-item').each(function(){
		    laydate.render({
		      elem: this
		      ,trigger: 'click'
		    });
		  });
		  });

</script>
	<!--提示框-->
	<script type="text/javascript">
		$(top.hangge());
		  
		  //获取页面的input的值
		  
		 
		 
		 
		 
		 
		 
		function editProductNum(){
		 	var index = layer.load(1, {
						  shade: [0.3,'#fff'] //0.1透明度的白色背景
						});
			 
			var orderId=${enwarhouse.id};
		   var orderNum='${enwarhouse.order_num}';
		   var ids='';
		   var numbers='';
			 var product_date='';
			  var productDate='';
			  var productGroup_num='';
			  var strss='';
			  var zengpin='';
				var protime=$("#product_time").val();  
			 var orderItemIds=$('input[name="orderItemId"]');
			  var  number=$('input[name="productNumber"]');
			  var productDates=$('input[name="product_time"]');
			  var zp_quantity=$('input[name="gift_quantity"]');
			   var productGroup_nums=$('input[name="product_group_num"]');
			  for(var i =0;i<orderItemIds.length;i++){
				      var id=orderItemIds[i].value;
					    	   if(!numberTest.test($("#input"+id).val())){
					    	     	layer.close(index);
						    		  $("#input"+id).tips({
						    	  			side:3,
						    	              msg:'必须是数字或者不能含有空格',
						    	              bg:'#AE81FF',
						    	              time:2
						    	          });
						    	  		$("#input"+id).focus();
						    	    	  return ; 
						     }
						      if($("#product_time"+id).val()==""){
						        layer.close(index);
					    		   $("#product_time"+id).tips({
					    	  			side:3,
					    	              msg:'生产日期必须填写',
					    	              bg:'#AE81FF',
					    	              time:2
					    	          });
					    	  		$("#product_time"+id).focus();
					    	    	  return ; 
					    	   } 
				       ids+=id+",";
				       numbers+=number[i].value+",";
				       productDate+=productDates[i].value+",";
				       productGroup_num+=productGroup_nums[i].value+",";
				       zengpin+=zp_quantity[i].value+",";
			   }
			   if(ids==''){
				    
				   return ;
			   }
 			var url="<%=basePath%>enwarehouseorder/updateEnOrderProduct.do";
			 $.post(url,{ids:ids,numbers:numbers,orderId:orderId,product_time:productDate,productgroupnum:productGroup_num,gift_quantity:zengpin},function(data){
				       layer.close(index);
				     editProductNumber(orderNum);
				          
			 });
			
		
		}

	 
		
		 //修改商品数量
		 
		  function editProductNumber(id1){
			var url='<%=basePath%>sellingorder/goEnwareorderProductEdit.do?type=1&orderId='+id1;
				top.mainFrame.tabAddHandler("3920608252","查看入库单信息",url);
			if(MENU_URL != "druid/index.html"){
				jzts();
			}
			 
		 } 
		
		
		 //存放data 信息 用于添加data 信息
		 var productData;
		 
		 
		 
		 
		 //搜索商品
		  function seachProduct(){
		 	   
		 	var typeId= $("#productTypeb").val();
		 	 if(typeId==null){
		 		  typeId=0;
		 	 }
		 	 var supplierId=$("#supplierId").html();
		 	 top.jzts();
		 	 var diag = new top.Dialog();
		 	 diag.Drag=true;
		 	 diag.Title ="选择商品";
		  
		 	 diag.URL = '<%=basePath%>product/getStorageProductByTypeId.do?typeId='+typeId+'&supplierid='+supplierId;
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

			 var numberTest=/^[0-9]*$/;
			//添加商品信息  
		 function addProduct(){
		
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
			  
			
			  
			   var orderId=${enwarhouse.id};
				
				var productId =productData.product.id;//选择的商品Id
				var orderNum='${enwarhouse.order_num}';//选择的订单号
				var groupNum='${enwarhouse.group_num}';//选择的批次号
				var purchasePrice= productData.product.PurchasePrice;//商品的价格 信息
				var sku_volume=productData.product.sku_volume;//单个商品的体积
				var sku_weight=productData.product.sku_weight;//单个商品的重量
				var quantity=$("#quantity").val();
				var url="<%=basePath%>enwarehouseorder/saveEnOrderItem.do";
				 $.post(url,{order_num:orderNum,
					 orderId:orderId,
					 group_num:groupNum,
					 sku_volume:sku_volume,
					 sku_weight:sku_weight,
					 bkType:1,//设置入库仓库地址
					 product_id:productId,purchase_price:purchasePrice,quantity:quantity,final_quantity:quantity,},function(data){
					    
						   alert("添加成功");
						   editProductNumber(orderNum);
					  
				 });
		 } 
		//产品分类
		  function setproductType(){
		        var parentid= $("#productTypea  option:selected").val();  
 
		     $.ajax({  
		       type : "post",  
		        url : "<%=basePath%>product/getProductType.do",  
		        data : {"parent_id":parentid},  
		        async : false,  
		        success : function(data){  
		          $("#productTypeb option").remove();
		        var producttype=data.productType;
		          var options = [];   
		        options.push("<option value=''>请选择</option>");
		       for(var i=0;i<producttype.length;i++){
		       options.push("<option value='" + producttype[i].id + "'> " + producttype[i].classifyName + " </option>");
		         } 
		         $("#productTypeb").append(options.join(''));  
		  	    }
		   }); 
		}
 
	 
	
		</script>



</body>
</html>

