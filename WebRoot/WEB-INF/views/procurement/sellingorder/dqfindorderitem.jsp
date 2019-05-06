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
<%@ include file="../../system/admin/top.jsp"%>	
<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
	<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-responsive.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/css/ace-skins.min.css" />
	<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.7.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/js/jQuery.print.js"></script>
	<link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
		<script type="text/javascript">
	 	function print(){
			window.open("<%=basePath%>sellingorder/print.do?id=${pd.id}", "", 'left=250,top=150,width=1400,height=500,toolbar=no,menubar=no,status=no,scrollbars=yes,resizable=yes');	
		}
		</script>
		<style type="text/css">
  		.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		#searchcriteria{width:202px;height:38px}
  	</style>
<body>
		
<div class="container-fluid" id="main-container">


<div id="page-content" class="clearfix">
						
  <div class="row-fluid">


	<div class="row-fluid">
	 
		<!-- <div  style="float: right;">
		<c:if test="${pd.checked_state==1}">
		<button style="" onclick="selectSupplier()" class=" btn-app btn-light btn-mini">
		调拨
		</button></c:if>
		 <button onclick="print()" class=" btn-app btn-light btn-mini">
		<i class="icon-print"></i>
		打印
		</button>
		  </div> -->
	    
	
		<table id="table_report" class="table table-striped table-bordered table-hover">
		  <tr>
		  		<td style="width:100px;text-align: center;"  colspan="4"><span style="font-size: 18px;">基本信息</span></td>
		  </tr>
		
		<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">订单编号:</td>
				<td>
			         ${obj.order_num}
				</td>
				 <td style="width:70px;text-align: right;padding-top: 13px;">下单日期:</td>
				<td>
			          ${obj.order_date}
				</td>
			</tr>
			<tr>
			 <td style="width:70px;text-align: right;padding-top: 13px;">配送地址:</td>
				<td>
				<%-- <fmt:parseDate value=" ${pd.create_time}" var="orderDate" pattern="yyyy-MM-dd"></fmt:parseDate>
					<fmt:formatDate value="${orderDate}" pattern="yyyy-MM-dd"/> --%>
					
			         ${obj.deliver_address}
				</td>
			 
				 <td style="width:70px;text-align: right;padding-top: 13px;">联系人:</td>
				 <td>
					${obj.manager_name}	
				 </Td>
				 
			</tr>
			<tr>
				<td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>
				<td colspan="3">
			          ${obj.comment}
				</td>
				 
			</tr>
	 </table>
	 <input type ="hidden" value="${pd.order_num }" name="order_num" id = "order_num">
			<!-- 检索  -->
			<!-- 检索  -->
		<form action="" method="post" name="Form" id="Form">
			<table id="table_report" class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						 <th class="center">序号</th>
						<th class="center">商品条形码</th>
						<th class="center">商品名称</th>
						<th class="center">规格</th>
						<th  class="center">订购单价</th>
						<th class="center">订购数量</th>
						<th class="center">实际数量</th>
						<th class="center">赠品数量</th>
						<th class="center">备注</th>
					</tr>
				</thead>
				<tbody>
				<!-- 开始循环 -->	
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="o" varStatus="vs">
							<tr>
							 <td class='center' >${vs.index+1}<input type = "hidden" class="ids" value="${o.id}" name="id"></td>
							  <td class="center">${o.bar_code}</td>
							  <td class="center">${o.product_name}</td>
							   <td class="center">${o.specification}</td>
							     <td class="center">
							     	<c:if test="${o.gift_quantity!=0.0}">
								 		0
								 	</c:if>
								 	<c:if test="${o.gift_quantity ==0.0}">
								 		<fmt:formatNumber  type="number" value="${o.sale_price}" minFractionDigits="4"  maxFractionDigits="4"  />
								 	</c:if>
							     </td>
							     <td class="center">
							     	${o.quantity}
								 </td>
								 <td class="center">
									 <c:if test="${role_id==32 }">
									 <c:choose>
										 <c:when test="${obj.checked_state !=  12}">
											 <fmt:formatNumber  type="number" value="${o.quantity}"  />
										 </c:when>
										 <c:otherwise>
											 <input style="display: none" type="text" name="orderItemId" VALUE="${o.id}">
											 <input type="number"  name="itemnum" id="input${o.id}"
													value="<fmt:formatNumber type="number" value="${o.final_quantity}" groupingUsed="false" maxFractionDigits="0"/>" >
										 </c:otherwise>
									 </c:choose>
								 	</c:if>
									<c:if test="${role_id!=32}">
								 		${o.final_quantity}
								 	</c:if>
								 </td>
								 <td class="center">
								 		${o.gift_quantity}
								 </td>
								 <td class="center">${o.comment}</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="main_info">
							<td colspan="10" class="center">没有相关数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				<tr>
				
			</tr>
				<c:if test="${obj.checked_state == 12}">
					<tr id="noprint">
						<td colspan="8"   >
							选择商品:
							<input type="text" name="productId" id="enProductId"  style="display: none">
							<input name="productName" id="productName" value="" type="text" readonly="readonly" placeholder="请选择商品" />
							<a class="btn btn-mini btn-light" onclick="seachProduct()" title="选择商品"><i id="nav-search-icon" class="icon-search"> </i> </a>
							数量:
							<input type="text" id="quantity"  value="" placeholder="">
							<a onclick="addProduct()" class="btn btn-mini btn-primary" >增加 </a>
						</td>

						<td  class='center'  >
							<%--<a class="btn btn-mini btn-primary"  style="text-align: center;" onclick="editProductNum()">确定</a>--%>
						</td>
						<%--<td ></td><td ></td>
						<td></td>--%>
					</tr>
				</c:if>
				</tbody>
			</table>
			</form>
			<c:if test="${role_id==32 }">
				<div class="page-header position-relative">
				<table style="width:100%;">
					<tr style="text-align: center;">
						<td><a class="btn btn-small btn-success" onclick="editProductNum();">修改</a></td>
					</tr>
				</table>
				</div>
		</c:if>
	</div>
	<!-- PAGE CONTENT ENDS HERE -->
  </div><!--/row-->
	
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
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
		<script src="static/layui/layui.js" charset="utf-8"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script>
            layui.use(['form', 'layedit', 'laydate'], function () {
                var form = layui.form
                    , layer = layui.layer
                    , layedit = layui.layedit
                    , laydate = layui.laydate;
                //日期
                lay('.test-item').each(function () {
                    laydate.render({
                        elem: this
                        , trigger: 'click'
                    });
                });
            });
		</script>
		<script type="text/javascript">
            $(top.hangge());
			layui.use(['form'], function(){
		  var form = layui.form
		  ,layer = layui.layer;
		  });
		/* var numberTest=/^[0-9]*$/;*/
            //存放data 信息 用于添加data 信息
            var productData;
            var numberTest=/^[0-9]*$/;
			function editProductNum(){
		 	var index = layer.load(1, {
						  shade: [0.3,'#fff'] });//0.1透明度的白色背景
		   var orderNum='${enwarhouse.order_num}';
		   var ids='';
		   var numbers='';
		   var  number=$('input[name="itemnum"]');
		   var  itemid=$('input[name="orderItemId"]');
			  for(var i =0;i<itemid.length;i++){
				      var id=itemid[i].value;
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
				       ids+=id+",";
				       numbers+=number[i].value+",";
			   }
			   if(ids==''){

				   return ;
			   }
 			var url="<%=basePath%>sellingorder/updatesellOrderProduct.do";
			 $.post(url,{ids:ids,numbers:numbers},function(data){
				       layer.close(index);
				     location.reload();
			 });
		}

            //搜索商品
            function seachProduct(){
                top.jzts();
                var diag = new top.Dialog();
                diag.Drag=true;
                diag.Title ="选择商品";
                diag.URL = '<%=basePath%>product/getProductByIsShelve.do';
                diag.Width = 800;
                diag.Height = 600;
                diag.CancelEvent = function(){ //关闭事件
                    diag.close();
                };
                diag.OKEvent = function(){
                    var pid=diag.innerFrame.contentWindow.$('input[name="productId"]:checked').val();
                    $("#enProductId").val(pid);
                    $.post("<%=basePath%>/product/findProductById?productId="+pid,function(data){
                        productData=data;
                        $("#productName").val(data.product.product_name);
                        $("#quantity").attr('placeholder','请输入'+ data.product.min_order_num +'的整数倍!')
                    });
                    diag.close();
                };
                diag.show();
            }
            function checkType(productTypeId) {
                return (productTypeId == 609 || productTypeId == 8205 || productTypeId == 8109) ? true : false;
            }
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
                        msg:'采购数量必须是数字',
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
                var val = $("#quantity").val()
                //当前产品最小起订量
                var minOrderNum = productData.product.min_order_num
                //当前商品类别id
                var productTypeId = productData.product.product_type_id
                if(isNaN(val)||val<=0||!(/^\d+$/.test(val))) {
                    $("#quantity").tips({
                        side:3,
                        msg:'商品数量只能为整数',
                        bg:'#AE81FF',
                        time:2
                    });
                    $("#quantity").val(minOrderNum)
                    return false
                }
                if (val == 0) {
                    $("#quantity").tips({
                        side:3,
                        msg:'商品数量不能为0',
                        bg:'#AE81FF',
                        time:2
                    });
                    $("#quantity").val(minOrderNum)
                    return false
                }

                if (checkType(productTypeId)) {
                    if (val % minOrderNum != 0) {
                        $("#quantity").tips({
                            side:3,
                            msg:'该商品仅能整箱订购',
                            bg:'#AE81FF',
                            time:2
                        });
                        $("#quantity").val(minOrderNum*(Math.ceil(val/minOrderNum)))
                        return false
                    }
                }
                if (Number(val) < Number(minOrderNum) ) {
                    $("#quantity").tips({
                        side:3,
                        msg:'商品不能低于最小起订量',
                        bg:'#AE81FF',
                        time:2
                    });
                    $("#quantity").val(minOrderNum)
                    return false
                }
                var orderId=${obj.id};
                var productId =productData.product.id;//选择的商品Id
                var orderNum='${obj.order_num}';//选择的订单号
                var groupNum='${obj.group_num}';//选择的批次号
                var purchasePrice= productData.product.PurchasePrice;//商品的采购价格 信息
                var salePrice= productData.product.salePrice;//商品的销售价 信息
                var sku_volume=productData.product.sku_volume;//单个商品的体积
                var sku_weight=productData.product.sku_weight;//单个商品的重量
                var quantity=$("#quantity").val();
                var url="<%=basePath%>sellingorder/saveSellingOrderItem.do";
                $.post(url,{order_num:orderNum,
                    orderId:orderId,
                    group_num:groupNum,
                    sku_volume:sku_volume,
                    sku_weight:sku_weight,
                    bkType:1,//设置入库仓库地址
                    product_id:productId,
                    purchase_price:purchasePrice,
                    sale_price:salePrice,
                    quantity:quantity,
                    final_quantity:quantity,},function(data){
                    console.info(data);
                    alert(data);
                    findorderitem('${obj.order_num}')
                });
            }
            function findorderitem(ordernum){
                top.mainFrame.tabAddHandler("392060","销售订单详情","<%=basePath%>sellingorder/sellingorderitemdq.do?order_num="+ordernum);
                if(MENU_URL != "druid/index.html"){
                    jzts();
                }
                location.href = '<%=basePath%>sellingorder/sellingorderitemdq.do?order_num='+ordernum;
            }
		</script>
	</body>
</html>

