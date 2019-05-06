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
	  <meta charset="utf-8">
	 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  	 <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
     <meta name="format-detection" content="telephone=no">
      <link href="static/css/bootstrap.min.css" rel="stylesheet" />
     <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
  	<style type="text/css">
  		.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		#searchcriteria{width:202px;height:38px}
  		
  	</style>
		<script type="text/javascript">
			function print(){
				window.open("<%=basePath%>enwarehouseorder/print.do?orderId=${enwarhouse.order_num}&type=1", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
			}
		</script>
	</head> 
<body>
<button style="float: right;" onclick="print()"
						class=" btn-app btn-light btn-mini">
						<i class="icon-print"></i> 打印
					</button>
	<form action="" method="post" id="PurchaseOrderForm">
		<div class="layui-form" >
  <table class="layui-table"  id="diyige">
		
		  <tr><td style="width:70px;text-align: right;padding-top: 13px;" >基本信息:</td>
		  <td colspan="3"></td>
		  </tr>
		<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">入库单编号:</td>
				<td>${pd.order_num }</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">供应商:</td>
				<td>
				<input type="hidden" id="supplierId">
				<span id="supplierName" name="supplierName"></span>
				<input type="hidden" id="supplier_id">
				<!-- <button  onclick="seachSupplier()">请选择供应商信息</button> -->
				<a  class="btn btn-mini btn-primary" onclick="seachSupplier()"><span>请选择供应商信息</span></a>
				</td>
			</tr>
			<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">创建日期:</td>
				<td>
			         
				</td>
			 
				 <td style="width:70px;text-align: right;padding-top: 13px;">入库日期:</td>
				 <Td></td>
			</tr>
			<tr>
		   <td style="width:90px;text-align: right;padding-top: 13px;">供应商联系人:</td>
				<td>
				<span id="supplierPerson"></span>
				</td>
				  <td style="width:120px;text-align: right;padding-top: 13px;">联系电话:</td>
				<td><span id="supplierPhone"></span></td>
			</tr>
			<tr>
			<td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" value="${enwarhouse.comment}" id="enwarhousecomment" >
								<a class="btn btn-mini btn-primary" style="text-align: center;left:20px"
											onclick="enwarhousecomment()">确定</a>
								</td>
								<td style="width:90px;text-align: right;padding-top: 13px;">状态:</td>
								<td><c:if test="${enwarhouse.checked_state eq 1}">
			                待入库
			      </c:if> <c:if test="${enwarhouse.checked_state eq 2}">
			                 已入库
			      </c:if></td>
			</tr>
	 </table>

  <table class="layui-table"  id="diyige"  >
		<thead>
					
								<tr>
									<th class='center'>序号</th>
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
																value="<fmt:formatNumber type="number" value="${o.final_quantity}" maxFractionDigits="0"/>" id="input${o.id}">
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
									<tr id="noprint">
										<td colspan="6">
										选择商品: <input type="text" name="productId" id="enProductId" style="display: none">
										<input type="text"  id="sale_price" style="display: none">
										 <input type="text"  id="sku_weight" style="display: none">
										 <input type="text"  id="sku_volume" style="display: none">
										 <input type="text"  id="purchase_price" style="display: none">			
										  <input type="text"  id="product_quantity" style="display: none">
										<input name="productName" id="productName" value="" type="text" readonly="readonly" 	placeholder="请选择商品" /> 
										<a class="btn btn-mini btn-light" onclick="seachProducts()" title="选择商品">
										<i id="nav-search-icon" class="icon-search"> </i> </a> 数量:<input type="text" id="quantity"  value=""> 
										<a onclick="addProduct()" class="btn btn-mini btn-primary" >增加 </a></td> 
										<td  colspan="6" style="text-align: center;"><a class="btn btn-mini btn-primary"  onclick="editProductNum()">确定</a></td>
									</tr>
							</tbody>
							<tr>
								<td colspan="8"></td>

								<td colspan="4" id="finalAmount">
									<font style="color: red;">实际入库单总额:<fmt:formatNumber type="number" value="${enwarhouse.final_amount}"  minFractionDigits="4" maxFractionDigits="4"/>元</font>
									<font style="color: red;">总数: <fmt:formatNumber type="number" value="${enwarhouse.final_quantity}"  /> </font>
								</td>
							</tr>
						</table>
	</div>
	</form>
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/layui/layui.all.js" charset="utf-8"></script>
		<script src="static/layui/lay/modules/laydate.js" charset="utf-8"></script>
		<script src="static/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		 $(top.hangge());
		 layui.use(['form'], function(){
					 var layer = layui.layer
  				,form = layui.form
				 });
		//添加商品信息  
		 function addProduct(){
				if( $("#productName").val()==""){
					 laytips("请选择商品","#productName");
		    	    	  return ; 
				 }
				
				if($("#quantity").val()==""){
					laytips("数量不能为空！","#quantity");
					return ;
				}
				var checked_state=1;
			    var supplier_id=$("#supplier_id").val();
				var productId =$("#enProductId").val();//选择的商品Id
				var salePrice=$("#sale_price").val();//商品的价格 信息
				var purchase_price=$("#purchase_price").val();
				var sku_volume=$("#sku_volume").val();//单个商品的体积
				var sku_weight=$("#sku_weight").val();//单个商品的重量
				var quantity=$("#quantity").val();
				var url="<%=basePath%>sellingorder/saveenorderProduct.do";
				 $.post(url,
				     {order_num:'${pd.order_num}',
					 checked_state:checked_state,
					 product_id:productId,
					 sale_price:salePrice,
					 sku_volume:sku_volume,
					 sku_weight:sku_weight,
					 purchase_price:purchase_price,
					 supplier_id:supplier_id,
					 comment:'',
					 quantity:quantity,
					 final_quantity:quantity,
					 manager_tel:$("#supplierPhone").html(),
					 manager_name:$("#supplierPerson").html(),
					 deliver_address:$("#deliver_address").val()
					 },function(data){
						  if(data=="false"){
							  layer.msg("保存错误!",{icon:2,time: 1000}); 
						  }else{
							  layer.msg("保存成功!",{icon:1,time: 1000},function(){
							  var url='<%=basePath%>sellingorder/goEnwareorderProductEdit.do?orderId=${pd.order_num}&type=1';
								top.mainFrame.tabAddHandler("3920608252","查看入库单信息",url);
								 
							  }); 
						  }
				 });
		 } 		 
				 
		 var _html="";
				 function seachSupplier(){
					 _html=layer.open({
						 title:'供应商信息',
						  type: 1,
						  skin: 'layui-layer-rim', //加上边框
						  area: ['80%', '80%'], //宽高	
						  content: '<div class="demoTable">'+
						 '<div class="layui-inline"><input type="text" name="supplierName" placeholder="请输入供应商信息" id="suppName" class="layui-input" autocomplete="off"></div>'
						 +'<button class="layui-btn" style="float:right" data-type="reload" onclick="searchSupp()">搜索</button></div><table class="layui-hide" id="demo" lay-filter="demo"></table></div>',
						  success: function(layero){
							  $(document).keydown(function (event) {
								    if (event.keyCode == 13) {
								    	searchSupp();
								    	return false;
								    };
								});
						 }
						});
					 searchSupp();
				 }
			function searchSupp(){
						  var url='<%=basePath%>supplier/findSupplierList.do?supplierName='+$("#suppName").val();
							 $.post(url,function(data){
							 //console.log(eval(data));
							 //console.log(data.list)
								 layui.use('table', function(){
								 var table = layui.table;
								  table.render({
								    elem: '#demo'
								    ,page:'true'
								    ,cols: [[
								      {checkbox: true, fixed: true }
								      ,{field:'id', title: '供应商ID'}
								      ,{field:'supplier_name', title: '供应商名称'}
								      ,{field:'contact_person',  title: '联系人', sort: true}
								      ,{field:'supplier_tel', title: '联系电话'}
								    ]],
								    data:data.list
								  });
										table.on('checkbox(demo)', function(item){
								    	prodsells(item.data.id,item.data.supplier_name,item.data.contact_person,item.data.supplier_tel);
								    	//prodsell(\'"+item.REALNAME+"\',\'"+item.PHONE+"\',\'"+item.CAR_NUMBER+"\',\'"+item.CARSTATE+"\',\'"+item.type+"\')\">"
									    layer.close(_html);
									});	
							});	
							 });
				}
				 function prodsells(id,name,contact_person,supplier_tel){
				 	 	$("#supplier_id").val(id);
						$("#supplierName").html(name);
						$("#supplierPerson").html(contact_person);
						$("#supplierPhone").html(supplier_tel);
						layer.close(_html);
				 	 }
				 
				 	 var _product;
function seachProducts(){
	var supplierId=$("#supplier_id").val();
	if(supplierId==""){
		layer.msg("请填写供应商信息信息!",{icon:2,time: 1000}); 
		return ;
	}
	_product=layer.open({
		 title:'商品信息',
		 type: 1,
		 skin: 'layui-layer-rim', //加上边框
	     area: ['80%', '80%'], //宽高
	     content: '<div class="demoTable" >'+
		'<div class="layui-inline"  ><input type="text" name="title" placeholder="请输入商品名称" id="product_name" class="layui-input" autocomplete="off"></div>'
		+'<button class="layui-btn" style="float:right" data-type="reload" onclick="productName()">搜索</button></div><table class="layui-hide" id="demo" lay-filter="demo"></table></div>',
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
			 layui.use(['form'], function(){
				  var url='product/saleReturnProductlist.do?product_name='+$("#product_name").val();
					 $.post(url,function(data){
						 layui.use('table', function(){
						 var table = layui.table; 
						 table.render({
								    elem: '#demo'
								    ,page:'true'
								    ,cols: [[
								      {checkbox: true, fixed: true }
								      ,{field:'product_id', title: '商品ID'}
								      ,{field:'product_name', title: '商品名称'}
								      ,{field:'bar_code', title: '商品条码'}
								      ,{field:'purchase_price',  title: '采购价', sort: true}
								    ]],
								    data:data.list
								  });
								  table.on('checkbox(demo)', function(item){
								    	console.log(item);
								    	console.log(2222222);
								    	prodsell(item.data.product_id,item.data.product_name,item.data.bar_code,item.data.purchase_price,item.data.sku_volume,item.data.sku_weight);
								    	//prodsell(\'"+item.REALNAME+"\',\'"+item.PHONE+"\',\'"+item.CAR_NUMBER+"\',\'"+item.CARSTATE+"\',\'"+item.type+"\')\">"
									    layer.close(_html);
									});	
							});	
					 });
				});
		 }
		 function prodsell(id,name,bar_code,purchase_price,sku_volume,sku_weight){
		 $("#enProductId").val(id);
	     $("#productName").val(name);
	 	 $("#sku_volume").val(sku_volume);
	 	 $("#sku_weight").val(sku_weight);
	 	 $("#purchase_price").val(purchase_price);
	 	 //$("#product_quantity").val(product_quantity);
	 	 layer.close(_product);
	}	 
		</script>
	</body>
</html>

