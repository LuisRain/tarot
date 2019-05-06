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
		
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
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
 
 
 
 
    
//搜索商品
 function seachProduct(){
	   
	var typeId= $("#productTypeb").val();
	 if(typeId==null){
		  typeId=0;
	 }
	
	
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="选择商品";
	 diag.URL = '<%=basePath%>product/getProductByTypeId.do?typeId='+typeId;
	 diag.Width = 700;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
	
		diag.close();
	 };
	 diag.OKEvent = function(){
		 var pid=diag.innerFrame.contentWindow.$('input[name="productId"]:checked').val();
	                   $("#enProductId").val(pid);
		    $.post("<%=basePath%>/product/findProductById?productId="+pid,function(data){
		    	        $("#productName").val(data.product.product_name);  
		    	        $("#bar_code").val(data.product.bar_code);
		    	        $("#unit").val(data.product.unit);
		    	        $("#specification").val(data.product.specification);
		    	        $("#expire_days").val(data.product.expire_days);
		    	        $("#warhouse").val(data.product.warehouse_name);
		    	        $("#cargoSpace").val(data.product.cargoSpace);
		    	        $("#PurchasePrice").val(data.product.PurchasePrice);
		    	        $("#SalePrice").val(data.product.SalePrice);
		    	        $("#inventory").val(data.product.inventory);
		    	        $("#inventory2").val(data.product.inventory2);
		    	        $("#productDate").val(data.product.create_time);
		    });
        diag.close();
	 };
	 diag.show();
	
	 
 }

function save(){
	var numberTest=/^[0-9]*$/;
      if($("#newQuantity").val()==""){
    	  
    	  $("#newQuantity").tips({
  			side:3,
              msg:'入库数量不能为空',
              bg:'#AE81FF',
              time:2
          });
  		$("#newQuantity").focus();
    	  return ; 
     }
     if(!numberTest.test($("#newQuantity").val())){
    	 
    		  $("#newQuantity").tips({
    	  			side:3,
    	              msg:'入库数量必须是数字',
    	              bg:'#AE81FF',
    	              time:2
    	          });
    	  		$("#newQuantity").focus();
    	    	  return ; 
     }
     
     if($("#productName").val()==""){
    	    $("#productName").tips({
	  			side:3,
	              msg:'请选择商品',
	              bg:'#AE81FF',
	              time:2
	          });
	  		$("#productName").focus();
	  		return ;
     }
     
       if($("#reason").val()=="-1"){
    	  
    	   $("#reason").tips({
	  			side:3,
	              msg:'请选择原因',
	              bg:'#AE81FF',
	              time:2
	          });
	  		$("#reason").focus();
	  		return ;
       }
 	  $("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();  
		
	 
	
	
	
}
</script>
	</head>
<body>
	<form action="<%=basePath%>/enwarehouseorder/addTempOrder.do?type=1" name="Form" id="Form" method="post">
		 
		<div id="zhongxin" >
		<table id="table_report" class="table table-striped table-bordered table-hover">
		  	  <tr>
		  	   <td>商品类别</td>
		  		<td>
					<select id="productTypea"  onchange="setproductType()">
					 <option value="">请选择</option>
					<c:forEach items="${productType}" var="classname">
					 <option value="${classname.id}">${classname.classifyName}</option>
					</c:forEach>
					</select>
			       <select id="productTypeb" class="productTypeb">
					</select>
					</td>
				 </tr>
				 <tr>
				<td>选择商品:</td>
				<td>
				  <input type="text" name="productId"    id="enProductId" style="display: none">
				<input  
				name="productName" id="productName" 
				value="" type="text"  
				 readonly="readonly"  placeholder="请选择商品"/>
				 <a class="btn btn-mini btn-light" onclick="seachProduct()" title="选择商品"><i id="nav-search-icon" class="icon-search"></i></a>
			</tr>
			
			
			<tr>
			 <td>条形码：</td>
			  <Td><input type="text" readonly="readonly"  id="bar_code"  name="bar_code" value=""></Td>
			   <Td>计量单位：</Td>
			 <Td><input type="text" readonly="readonly"  id="unit"  name="unit" value=""></Td> 
			</tr>
			
				 
				<tr>
			 <td>规格：</td>
			  <Td><input type="text" readonly="readonly"  id="specification"  name="specification"></Td>
			   <Td>保质期：</Td>
			 <Td><input type="text" readonly="readonly"  id="expire_days"  name="expire_days">天</Td> 
			</tr>
	 
	 	<tr>
			 <td>仓库：</td>
			  <Td><input type="text" readonly="readonly"  id="warhouse" ></Td>
			   <Td>货位：</Td>
			 <Td><input type="text" readonly="readonly"  id="cargoSpace"  >天</Td> 
			</tr>
				<tr>
			 <td>采购价格：</td>
			  <Td><input type="text" readonly="readonly"  id="PurchasePrice"   ></Td>
			   <Td>零售价格：</Td>
			 <Td><input type="text" readonly="readonly"  id="SalePrice"  ></Td> 
			</tr>
			<tr>
			 <td>正常库存:</td>
			  <Td><input type="text" readonly="readonly"  id="inventory"   ></Td>
			   <Td>退货仓库库存：</Td>
			 <Td><input type="text" readonly="readonly"  id="inventory2"  ></Td> 
			</tr>
			<tr>
			 <td>生产日期:</td>
			  <Td><input type="text" readonly="readonly"  id="productDate"></Td>
			  
			</tr>
		 	<tr>
			 <td>入库数量:</td>
			  <Td><input type="text"    id="newQuantity"  name="newQuantity" ></Td>
			</tr>
			 	<tr>
			 <td>入库原因:</td>
			  <Td>
			  <select name="reason" id="reason">
	 
			    <option  value="-1">请选择</option>
			       <option value="01-供应商临时赠品">01-供应商临时赠品</option>
			          <option value="02-仓库盘点报溢">02-仓库盘点报溢</option>
			             <option value="03-客户临时退货">03-客户临时退货</option>
			                <option value="04-临时自采购">04-临时自采购</option>
			                <option value="99-其他">99-其他</option>
			  
			  </select></Td>
			</tr>
			
			 <tr>
			 <td>仓库:</td>
			  <Td>
			  <select name="warehouseId" >
	             <c:forEach items="${wlist}" var="w">
			  <option value="${w.id}">${w.warehouse_name}</option>
			      </c:forEach>
			     
			  
			  </select></Td>
			</tr>
			 <tr>
			 <td>备注:</td>
			  <Td>
			  
			   <input type="text"    name="comment" >请言简意赅填写
			 </Td>
			</tr>
		 
			 	<tr>
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
		 
	</form>
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		$(top.hangge());
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>