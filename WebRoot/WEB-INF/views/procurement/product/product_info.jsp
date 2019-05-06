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
<%-- function goEnwarouseOrder(id){
	
	top.mainFrame.tabAddHandler("392061","出入库","<%=basePath%>enwarehouseorder/list.do?id="+id);
	if(MENU_URL != "druid/index.html"){
		jzts();
	}
	
} --%>
 
 
	
</script>
	</head>
<body>

		<table id="table_report" class="table table-striped table-bordered table-hover">
		  <tr><td colspan="4" style="width:100%;text-align: left;" >商品的基本信息:</td></tr>
		
		<tr>
			 <td style="width:15%;text-align: right;padding-top: 13px;">商品编号|名称</td>
				<td style="width:35%;padding-top: 13px;">
			       ${product.host_code}|${product.product_name}
				</td>
				<td style="width:15%;text-align: right;padding-top: 13px;">商品类别</td>
				<td style="width:35%;padding-top: 13px;">
			       ${product.classify_name}
				</td>
				
			</tr>
				
		<tr>
			 <td style="width:50px;text-align: right;padding-top: 13px;">条形码:</td>
				<td>
			       ${product.bar_code}
				</td>
				<td style="width:50px;text-align: right;padding-top: 13px;">商品编码:</td>
				<td>
			       ${product.product_num}
				</td>
				
			</tr>
				<tr>
			 <td style="width:50px;text-align: right;padding-top: 13px;">规格:</td>
				<td>
			       ${product.specification}
				</td>
				<td style="width:50px;text-align: right;padding-top: 13px;">计价单位:</td>
				<td>
			       ${product.unit_name}
				</td>
				
			</tr>
				<tr>
			 <td style="width:50px;text-align: right;padding-top: 13px;">最小起订量:</td>
				<td>
			       ${product.min_order_num}
				</td>
				<td style="width:50px;text-align: right;padding-top: 13px;">箱装数:</td>
				<td>
			       ${product.box_number}
				</td>

			</tr>
			<tr>
			 <td style="width:50px;text-align: right;padding-top: 13px;">订购单价:</td>
				<td>
			      ${product.price2}(RMB：元)
				</td>
				 <td style="width:50px;text-align: right;padding-top: 13px;">保质期:</td>
				<td>
			        ${product.expire_days}天
				</td>
			</tr>
			   
		<%-- 	   <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">储藏方式：</td>
				<td>
			       
			      ${product.store_method}  
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">服务电话：</td>
				<td>
			          ${product.store_method_consulting_telephone}  
				</td>
				
				
			</tr> --%>  
				   
			   <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">使用方式：</td>
				<td>
			       ${product.usage_mode}  
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">产地：</td>
				<td>
			       
			        ${product.origin_place}  
				</td>
				
				
			</tr>  
			   
			   <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">正常库：</td>
				<td>
			        ${product.warehouse_name}
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">货位：</td>
				<td>
			         ${product.zone}
				</td>
				
				
			</tr>  
	<%-- 		  <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">最小库存量(↓)：</td>
				<td>
			       ${product.min_stock_num}
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">最大库存量(↑)：</td>
				<td>
			       ${product.max_stock_num}
				</td>
				
				
			</tr>  --%>
			
	<%-- 		<tr>
			
	 <td style="width:50px;text-align: right;padding-top: 13px;">商品是否上架:</td>		
 
    <c:if test="${product.is_shelve eq 1}">
    
        <td> 
        
        <input type="radio"  name="identity" value="上架" checked="checked" />上架
             <input type="radio"  name="identity"  value="下架"  />下架（商品下架是该商品停止出售或采购，上架是可以出售或采购该商品）
        
        </td>    
    
    </c:if>
        <c:if test="${product.is_shelve eq 2}">
    
        <td> 
        
         <input type="radio" name="identity"  value="上架" />上架
         <input type="radio"  name="identity"  value="下架" checked="checked" />下架
         
         （商品下架是该商品停止出售或采购，上架是可以出售或采购该商品）
        
        </td>    
    
    </c:if>
          
			</tr>
 --%>
			 <%--  <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">业务员：</td>
				<td>
			        ${product.creator}
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">装箱数：</td>
				<td>
			       
				</td>
				
				
			</tr>   --%>
			 <%--  <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">单品体积：</td>
				<td>
			      ${product.sku_volume}
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">整箱体积：</td>
				<td>
			          ${product.fcl_volume}
				</td>
				
				
			</tr>   --%>
			<%--   <tr>
			
			 <td style="width:50px;text-align: right;padding-top: 13px;">重量：</td>
				<td>
			     
			        ${product.weight}
				</td>
					
			 <td style="width:50px;text-align: right;padding-top: 13px;">税率：</td>
				<td>
			        ${product.taxes}
				</td>
			</tr>   --%>
			
			  <tr><td colspan="4" style="width:100%;text-align: left;" >供应商与价格:</td></tr>
 
 	        
 	     <Tr>
		  
		         <td style="width:50px;text-align: right;padding-top: 13px;">供应商编号|名称：</td>
				<td>
			        
			         ${product.supplier_id}|${product.supplier_name}
				</td>
		    
		    
		    
		         <td style="width:50px;text-align: right;padding-top: 13px;">采购单价:</td>
				<td>
			         ${product.price1}(RMB：元)
				</td>
		 
		      
		      </Tr>
 	    
 	  
 	      	  <tr><td colspan="4" style="width:100%;text-align: left;" >统计信息:</td></tr>
 
		    <tr>
		     <td style="width:50px;text-align: right;padding-top: 13px;">库存：</td>
				<td>
			      ${product.product_quantity}  （${product.unit_name}）
				</td>
		    
		    
		    </tr>
		 <!--      <tr>
		      
		      <td style="width:100px;text-align: right" >最后修改:</td>
		      </tr>
		      <Tr>
		  
		         <td style="width:50px;text-align: right;padding-top: 13px;">修改人：</td>
				<td>
			        
				</td>
		    
		    
		    
		         <td style="width:50px;text-align: right;padding-top: 13px;">修改时间：</td>
				<td>
			        
				</td>
		 
		      
		      </Tr>
		       -->
			 
		</table>
	 
		
		 
 
	
	
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