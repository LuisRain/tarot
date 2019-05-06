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
<meta charset="utf-8" />
<title></title>
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="static/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<!-- 下拉框 -->
<link rel="stylesheet" href="static/css/chosen.css" />

<link rel="stylesheet" href="static/css/ace.min.css" />
<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
<link rel="stylesheet" href="static/css/ace-skins.min.css" />

<link rel="stylesheet" href="static/css/datepicker.css" />
<!-- 日期框 -->
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="static/js/jquery.tips.js"></script>

<script type="text/javascript">
	
	
	//保存
	function save(){
			if($("#checked_state").val()==""){
			$("#checked_state").tips({
				side:3,
	            msg:'请输入入库状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#checked_state").focus();
			return false;
		}
		if($("#order_date").val()==""){
			$("#order_date").tips({
				side:3,
	            msg:'请输入入库时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#order_date").focus();
			return false;
		}
		if($("#manager_name").val()==""){
			$("#manager_name").tips({
				side:3,
	            msg:'请输入联系人',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#manager_name").focus();
			return false;
		}
		if($("#manager_tel").val()==""){
			$("#manager_tel").tips({
				side:3,
	            msg:'请输入联系方式',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#manager_tel").focus();
			return false;
		}
		if($("#comment").val()==""){
			$("#comment").tips({
				side:3,
	            msg:'请输入备注',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#comment").focus();
			return false;
		}
		if($("#final_amount").val()==""){
			$("#final_amount").tips({
				side:3,
	            msg:'请输入入库总额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#final_amount").focus();
			return false;
		}
		if($("#total_svolume").val()==""){
			$("#total_svolume").tips({
				side:3,
	            msg:'请输入 总体积',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#total_svolume").focus();
			return false;
		}
		if($("#total_weight").val()==""){
			$("#total_weight").tips({
				side:3,
	            msg:'请输入总重量',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#total_weight").focus();
			return false;
		}
		if($("#paid_amount").val()==""){
			$("#PAID_AMOUNT").tips({
				side:3,
	            msg:'请输入已付款额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#PAID_AMOUNT").focus();
			return false;
		}
		if($("#is_temporary").val()==""){
			$("#is_temporary").tips({
				side:3,
	            msg:'请输入是否是临时入库单',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#is_temporary").focus();
			return false;
		}
		if($("#is_order_print").val()==""){
			$("#is_order_print").tips({
				side:3,
	            msg:'请输入订单打印状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#is_order_print").focus();
			return false;
		}
		if($("#ivt_state").val()==""){
			$("#ivt_state").tips({
				side:3,
	            msg:'请输入状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ivt_state").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
	 function selectSupplier(id){
		 top.jzts();
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
	        var comment=$("#comment").val();
           $("#supplier"+id).val(suppliernum+"|"+suppliername);
           $("#supplierid"+id).val(supplierid);
           $("#supplierName").val();
             $.post("<%=basePath%>/enwarehouseorder/saveInferior.do",{supplierId:supplierid,comment:comment},function(data){
            	       var datas=data.split(",");
            	       
            	       $("#orderNum").val(datas[0]);
            	       $("#groupNum").val(datas[1]);
            	       $("#enwarsehouseId").val(datas[2]);
            	        
             })
           diag.close();  
           
          
		 };
		 diag.show();
	 }
	
</script>
</head>
<body>
	<form action="enwarehouseorder/${msg}.do" name="Form" id="Form"
		method="post">
		<input type="hidden" name="id" id="id" value="${pd.id}" />
		<div id="zhongxin">
			<table id="table_report"
				class="table table-striped table-bordered table-hover">
				<tr>
					<td>基本信息</td>
				</tr>
				<tr>
					<Td>订单编号:<input type="text" value="系统自动生成"></Td>
				</tr>
				<tr>
					<td >供应商: <input name='supplier1'
						id="supplier1" disabled="disabled"
						value="${product.supplier_name}"
						placeholder="===编号===请选择===公司名称===" /> <input name='supplierid1'
						id="supplierid1" value="${product.tsId}" style="display: none" />
						<a class="btn btn-mini btn-light" onclick="selectSupplier('1')"
						title="选择供应商"><i id="nav-search-icon" class="icon-search"></i></a>
					</td>
				</tr>
				<tr>
					<td>供应商联系人:<input type="text" value="" id="supplierName" readonly="readonly"></Td>
					<td>联系电话:<input type="text" value="" id="mobel" readonly="readonly"></Td>
				</tr>
				<tr>
					<td>备注:<input type="text" value=""></Td>
				</tr>
			</table>
			<input id="orderNum" value="" style="display: none"> 
			<input
				id="groupNum" value="" style="display: none;">
				 <input id="enwarsehouseId" value="" style="display: none">
			<table id="table_report1"
				class="table table-striped table-bordered table-hover">
				<tr >
					<Td colspan="8">次品库内容</Td>
				</tr>
				<tr>
					<td>商品条形码</td>
					<td>商品名称</td>
					<td>仓库</td>
					<td>规格</td>
					<td>库存</td>
					<td>采购价</td>
					<Td>订购数</Td>
					<td>单位</td>
				</tr>
				 

	</table>

         <table>
				<tr id="noprint">
					<td colspan="6"><select id="productTypea"
						onchange="setproductType()">
							<option value="">请选择</option>
							<c:forEach items="${productType}" var="classname">
								<option value="${classname.id}">${classname.classifyName}</option>
							</c:forEach>
					</select> <select id="productTypeb" class="productTypeb">
					</select> 选择商品: <input type="text" name="productId" id="enProductId"
						style="display: none"> <input name="productName"
						id="productName" value="" type="text" readonly="readonly"
						placeholder="请选择商品" /> <a class="btn btn-mini btn-light"
						onclick="seachProduct()" title="选择商品"><i id="nav-search-icon"
							class="icon-search"> </i> </a> 数量:<input type="text" id="quantity"
						value=""> <a onclick="addProduct()"
						class="btn btn-mini btn-primary">增加 </a></td>
					<td colspan="2"><a class="btn btn-mini btn-primary"
						style="text-align: center;" onclick="editProductNum()">确定</a></td>
				</tr>
				</table>
		
		</div>
		<div id="zhongxin2" class="center" style="display:none">
			<br />
			<br />
			<br />
			<br />
			<br />
			<img src="static/images/jiazai.gif" /><br />
			<h4 class="lighter block green">提交中...</h4>
		</div>
	</form>
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
	<script type="text/javascript">
		$(top.hangge());
		$(function() {
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			//日期框
			$('.date-picker').datepicker();
		});
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
		  
		  var   productData;
		  
		//@ sourceURL=msgprompt.js
			 //搜索商品
			  function seachProduct(){
			 	var typeId= $("#productTypeb").val();
			 	 if(typeId==null){
			 		  typeId=0;
			 	 }
			 	 var supplierid =$("#supplierid1").val();
			 	 var diag = new top.Dialog();
			 	 diag.Drag=true;
			 	 diag.Title ="选择商品";
			  diag.URL = '<%=basePath%>product/getProductByTypeId.do?typeId='+typeId+'&supplierid='+supplierid;
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
				 
				//@ sourceURL=msgprompt.js
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
						var sku_weight=productData.product.sku_weight;//单个商品的重量
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
				bkType : 2
			}, function(data) {
				 
				var datas = data.split(",");
			 
            var text='次品库';
				var tr = "<tr><td>" + datas[1] + "</td><td>" + datas[0]
						+ "</td><td>" + text + "</td><td>" + datas[2]
						+ "</td><td>" + datas[3] + "</td><td>" + datas[4]
						+ "</td><td>" + datas[5] + "</td><td>" + datas[6]
						+ "</td></tr>";
				$("#table_report1").append(tr);
			});
		}
	</script>
</body>
</html>