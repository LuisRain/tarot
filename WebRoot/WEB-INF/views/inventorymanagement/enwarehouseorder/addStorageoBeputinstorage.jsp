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

<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
			<link rel="stylesheet" href="<%=basePath%>plugins/zTree/3.5/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<%=basePath%>plugins/zTree/3.5/jquery.ztree.core-3.5.js"></script>
</head>		
		<script type="text/javascript">
		function dy(){
			  $("#Form").print({
				  globalStyles: true,
				  noPrintSelector: "#noprint"
			  });
			
		}
		
		
		 //选中供应商
		 function selectSupplier(){
			 var orderdate= $("#order_date").val();
			 if(orderdate==""){
				 alert("请选择日期");
				 return false;
			 }
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="选择供应商";
			 diag.URL = '<%=basePath%>supplier/supplierlist.do';
			 diag.Width = 700;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
			
				diag.close();
			 };
			 diag.OKEvent = function(){
				var supplierid=diag.innerFrame.contentWindow.$('input[name="supplierid"]:checked').val();
				var suppliername=diag.innerFrame.contentWindow.$("#suppliernum"+supplierid).text();
				var suppliernum=diag.innerFrame.contentWindow.$("#suppliername"+supplierid).text();
				var sj =diag.innerFrame.contentWindow.$("#contact_person_mobile"+supplierid).text();
				var lxr=diag.innerFrame.contentWindow.$("#contact_person"+supplierid).text();
				var comment=$("#comment").val();
	            $("#suppliertd").val(suppliernum+"|"+suppliername);
	            $("#supplier").val(supplierid);
	            $("#sj").text(sj);
	            $("#lxr").text(lxr);
	            var orderdate= $("#order_date").val();
	            var ordernum=$("#orderNum").val();
	            $.post("<%=basePath%>/enwarehouseorder/Generatethestorereceipt.do",{supplierId:supplierid,comment:comment,ordernum:ordernum,orderdate:orderdate},function(data){
         	       var datas=data.split(",");
         	       $("#orderNum").val(datas[0]);
         	       $("#order_num").val(datas[0]);
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
					
					<form
						action="enwarehouseorder/goEnwareorderProductEdit.do?orderId=${enwarhouse.order_num}	"
						method="post" name="Form" id="Form">
						<table id="table_report"
							class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:100px;text-align: right">基本信息:</td>
							</tr>

							<tr>
								<td style="width:90px;text-align: right;padding-top: 13px;">入库单编号:</td>
								<td><input type="text" name="order_num" id="order_num" value="自动生成" disabled="disabled"></td>
								<td style="width:90px;text-align: right;padding-top: 13px;">供应商:</td>
								<td>
								<input type="text" disabled="disabled" style="width: 400px" name="suppliertd"   id="suppliertd" />
								<input type="text" name="supplier" id="supplier" style="display: none"  />
								<input type="text" id="enwarsehouseId" style="display: none" />
								<input type="text" id="groupNum" style="display: none" />
								<input type="text" id="orderNum" style="display: none" />
								
								<a class="btn btn-mini btn-light" onclick="selectSupplier()" title="选择供应商"><i id="nav-search-icon" class="icon-search"></i></a>
								</td>

							</tr>
							<tr>
								<td style="width:90px;text-align: right;padding-top: 13px;">订购日期:</td>
								<td>
								<input class="span10 date-picker" name="order_date" id="order_date" value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="订购日期" title="订购日期">
								</td>
								<td style="width:90px;text-align: right;padding-top: 13px;">供应商联系人:</td>
								<td id="lxr"></td>

							</tr>
							<tr>
								
								<td style="width:90px;text-align: right;padding-top: 13px;">联系电话:</td>
								<td id="sj"></td>
                                <td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>

								<td><input type="text" id="comment" name="comment"></td>
							</tr>
                
						</table>
						<!-- 检索  -->


						<!-- 检索  -->


						<table id="table_report1"
							class="table table-striped table-bordered table-hover">

							<thead>
					
								<tr>

					 <td>商品编码</td>
					<td>商品名称</td>
					<td>仓库</td>
					<td>规格</td>
					<td>库存</td>
					<td>采购价</td>
					<Td>订购数</Td>
					<Td>实际采购数</Td>
									



								</tr>
							</thead>

							<tbody>

</table>
								

                         <table>
								<tr id="noprint">
									
									
									
				<td>					 选择商品: <input type="text" name="productId" id="enProductId"
										style="display: none"> <input name="productName"
										id="productName" value="" type="text" readonly="readonly"
										placeholder="请选择商品" /> <a class="btn btn-mini btn-light"
										onclick="seachProduct()" title="选择商品"><i id="nav-search-icon" class="icon-search"> </i> </a> 
										</td>
										
										
										<td>
											数量:<input type="text" id="quantity"  value=""> 
										<a onclick="addProduct()" class="btn btn-mini btn-primary" >增加 </a>
										</td>
										
										

									<td colspan="4"><a class="btn btn-mini btn-primary"  style="text-align: center;"
										onclick="editProductNum()">确定</a></td>
										<td></td><td></td>
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
<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul>
</div>
		<!-- 引入 -->
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
		
		 
		 
		 
		 
	
	 
		
		 //修改商品数量
		 
		  function editProductNumber(id1){
			var url='<%=basePath%>enwarehouseorder/goEnwareorderProductEdit.do?type=1&orderId='+id1;
				top.mainFrame.tabAddHandler("392060852","查看入库单信息",url);
		
			 
		 } 
		
		
		 //存放data 信息 用于添加data 信息
		 var productData;
		 
		 
		 
		 
		 //搜索商品
		  function seachProduct(){
			 var supplierid=$("#supplier").val();
		 	 if(supplierid==""){
		 		  alert("请选择供应商!");
		 		  return false;
		 	 }
		 	 top.jzts();
		 	 var diag = new top.Dialog();
		 	 diag.Drag=true;
		 	 diag.Title ="选择商品";
		 	 diag.URL = '<%=basePath%>product/getStorageProductByTypeId.do?supplierid='+supplierid;
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
					  
					   var orderId=$("#enwarsehouseId").val();
						var productId =productData.product.id;//选择的商品Id
						var orderNum=$("#orderNum").val();//选择的订单号
						var groupNum=$("#groupNum").val();//选择的批次号
					
						var purchasePrice= productData.product.PurchasePrice;//商品的价格 信息
					 
						var sku_volume=productData.product.sku_volume;//单个商品的体积
						console.log("sku_volume"+sku_volume);
						var sku_weight=productData.product.sku_weight;//单个商品的重量
						console.log("sku_weight"+sku_weight);
						var quantity=$("#quantity").val();
						var url="<%=basePath%>enwarehouseorder/saveEnOrderItem.do";
		
			$.post(url, {
				order_num : orderNum,
				orderId : orderId,
				group_num : groupNum,
				sku_volume : sku_volume,
				sku_weight : sku_weight,
				product_id : productId,
				purchase_price : purchasePrice,
				quantity : quantity,
				final_quantity : quantity,
				bkType : 3
			}, function(data) {
				var datas = data.split(",");
				var text='存储库';
				var tr = "<tr><td>" + datas[1] + "</td><td>" + datas[0]
						+ "</td><td>" + text + "</td><td>" + datas[2]
						+ "</td><td>" + datas[3] + "</td><td>" + datas[4]
						+ "</td><td>" + datas[5] + "</td><td>" + datas[5] + "</td></tr>";
				$("#table_report1").append(tr);
			});
		}
	
    
			
	
		</script>



</body>
</html>

