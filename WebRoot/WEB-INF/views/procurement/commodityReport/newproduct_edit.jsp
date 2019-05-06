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
	<%@ include file="/WEB-INF/views/system/admin/top.jsp"%> 
	
	</head> 
		
		<title></title>
		<script type="text/javascript" src="static/1.9.1/jquery.min.js"></script><!-- 
		<script type="text/javascript" src="static/upload/jQuery.upload.min.js"></script> -->
		<script type="text/javascript" src="static/upload/ajaxfileupload.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<link rel="stylesheet" href="plugins/zTree/3.5/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="plugins/zTree/3.5/jquery.ztree.core-3.5.js"></script>
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
		
		
		 var tptId='${product.tptId}';
		      if(tptId!=null&&tptId!=""){
		    	  setZtree(tptId);
		      }
		      
		      var isshelve='${product.is_shelve}'; 
		      if(isshelve!=null&&isshelve!=""){
		     $("input[type=radio][name='IS_SHELVE'][value='"+isshelve+"']").attr("checked",'checked');
		      }
	});
	
	
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
            $("#supplier"+id).val(suppliernum+"|"+suppliername);
            $("#supplierid"+id).val(supplierid);
            diag.close();
		 };
		 diag.show();
	 }
	
	function upzxs(){
	   var boNumber=$("#box_number").val();
	   var fclWeight=$("#FCL_WEIGHT").val();
	    var fclVolume=$("#FCL_VOLUME").val();
	   $("#sku_weight").val(fclWeight/boNumber).toFixed(2);
	    $("#sku_volume").val(fclVolume/boNumber).toFixed(2);
   }
    function upzxt(){
	   var boNumber=$("#box_number").val();
	   var fclVolume=$("#FCL_VOLUME").val();
	   $("#sku_volume").val(fclVolume/boNumber).toFixed(2);
   }
    function upzxz(){
	   var boNumber=$("#box_number").val();
	   var fclWeight=$("#FCL_WEIGHT").val();
	   $("#sku_weight").val(fclWeight/boNumber).toFixed(2);
   }
   
	
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
		$("#PRODUCT_TYPE_ID").val(treeNode.id);
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
	
	

	
	//保存
	
	function save() {
		var zm = /^[A-Z]+$/; //只能输入大写字母
		var number = /^[0-9]*$/;//只能输入任意数字
		var qian = /^([1-9][\d]{0,7}|0)(\.[\d]{1,4})?$/;
		var xs = /^\d+(\.\d+)?$/;
		if ($("#PRODUCT_TYPE_ID").val() == "") {
			$("#producttype").tips({
				side : 3,
				msg : '请选择商品分类',
				bg : '#AE81FF',
				time : 2
			});
			$("#producttype").focus();
			return false;
		}
		if ($("#PRODUCT_NAME").val() == "") {
			$("#PRODUCT_NAME").tips({
				side : 3,
				msg : '请输入商品名称',
				bg : '#AE81FF',
				time : 2
			});
			$("#PRODUCT_NAME").focus();
			return false;
		}

		/* if($("#PRODUCT_NAME").val().indexOf(" ")>=0){
			$("#PRODUCT_NAME").tips({
				side:3,
		        msg:'商品名称不能有空格',
		        bg:'#AE81FF',
		        time:2
		   });
			$("#PRODUCT_NAME").focus();
			return false;
		} */
		if ($("#BAR_CODE").val() == "") {
			$("#BAR_CODE").tips({
				side : 3,
				msg : '请输入商品条码',
				bg : '#AE81FF',
				time : 2
			});
			$("#BAR_CODE").focus();
			return false;
		}
		if ($("#UNIT").val() == "") {
			$("#UNIT").tips({
				side : 3,
				msg : '请输入单位',
				bg : '#AE81FF',
				time : 2
			});
			$("#UNIT").focus();
			return false;
		}
		if ($("#EXPIRE_DAYS").val() == "") {
			$("#EXPIRE_DAYS").tips({
				side : 3,
				msg : '请输入保质期',
				bg : '#AE81FF',
				time : 2
			});
			$("#EXPIRE_DAYS").focus();
			return false;
		} else if ($("#EXPIRE_DAYS").val().length != 11
				&& !number.test($("#EXPIRE_DAYS").val())) {
			$("#EXPIRE_DAYS").tips({
				side : 3,
				msg : '保质期只能填写数字',
				bg : '#AE81FF',
				time : 3
			});
			$("#EXPIRE_DAYS").focus();
			return false;
		}

		/* if($("#MIN_STOCK_NUM").val()==""){
			$("#MIN_STOCK_NUM").tips({
				side:3,
		        msg:'请输入最小库存数',
		        bg:'#AE81FF',
		        time:2
		    });
			$("#MIN_STOCK_NUM").focus();
			return false;
		}
		if($("#MAX_STOCK_NUM").val()==""){
			$("#MAX_STOCK_NUM").tips({
				side:3,
		        msg:'请输入最大库存数',
		        bg:'#AE81FF',
		        time:2
		    });
			$("#MAX_STOCK_NUM").focus();
			return false;
		} */
		if ($("#product_num").val() == "") {
			$("#product_num").tips({
				side : 3,
				msg : '请输入商品编码',
				bg : '#AE81FF',
				time : 2
			});
			$("#product_num").focus();
			return false;
		}
		if ($("#FCL_VOLUME").val() == "") {
			$("#FCL_VOLUME").tips({
				side : 3,
				msg : '请输入整体体积',
				bg : '#AE81FF',
				time : 2
			});
			$("#FCL_VOLUME").focus();
			return false;
		} else if ($("#FCL_VOLUME").val().length != 11
				&& !xs.test($("#FCL_VOLUME").val())) {
			$("#FCL_VOLUME").tips({
				side : 3,
				msg : '整体重量只能填写数字',
				bg : '#AE81FF',
				time : 3
			});
			$("#FCL_VOLUME").focus();
			return false;
		}

		if ($("#FCL_WEIGHT").val() == "") {
			$("#FCL_WEIGHT").tips({
				side : 3,
				msg : '请输入整体重量',
				bg : '#AE81FF',
				time : 2
			});
			$("#FCL_WEIGHT").focus();
			return false;
		} else if ($("#FCL_WEIGHT").val().length != 11
				&& !xs.test($("#FCL_WEIGHT").val())) {
			$("#FCL_WEIGHT").tips({
				side : 3,
				msg : '整体重量只能填写数字',
				bg : '#AE81FF',
				time : 3
			});
			$("#FCL_WEIGHT").focus();
			return false;
		}

		if ($("#box_number").val() == "") {
			$("#box_number").tips({
				side : 3,
				msg : '请输入箱装数',
				bg : '#AE81FF',
				time : 2
			});
			$("#box_number").focus();
			return false;
		} else if ($("#box_number").val().length != 11
				&& !number.test($("#box_number").val())) {
			$("#box_number").tips({
				side : 3,
				msg : '箱装数只能填写数字',
				bg : '#AE81FF',
				time : 3
			});
			$("#box_number").focus();
			return false;
		}
		/* if($("#REMARKS").val()==""){
			$("#REMARKS").tips({
				side:3,
		        msg:'请输入备注',
		        bg:'#AE81FF',
		        time:2
		    });
			$("#REMARKS").focus();
			return false;
		} */

		if ($("#WAREHOUSE_ID").val() == "") {
			$("#WAREHOUSE_ID").tips({
				side : 3,
				msg : '请选择商品仓库',
				bg : '#AE81FF',
				time : 2
			});
			$("#WAREHOUSE_ID").focus();
			return false;
		}

		if ($("#zone").val() == "") {
			$("#zone").tips({
				side : 3,
				msg : '请输入商品存放区',
				bg : '#AE81FF',
				time : 2
			});
			$("#zone").focus();
			return false;
		} else if ($("#zone").val().length != 11 && !zm.test($("#zone").val())) {
			$("#zone").tips({
				side : 3,
				msg : '商品存放区只能填写大写字母',
				bg : '#AE81FF',
				time : 3
			});
			$("#zone").focus();
			return false;
		}
		if ($("#storey").val() == "") {
			$("#storey").tips({
				side : 3,
				msg : '请输入具体层数',
				bg : '#AE81FF',
				time : 2
			});
			$("#storey").focus();
			return false;
		}
		if ($("#storey_num").val() == "") {
			$("#storey_num").tips({
				side : 3,
				msg : '请输入具体编号',
				bg : '#AE81FF',
				time : 2
			});
			$("#storey_num").focus();
			return false;
		}
		if ($("#supplier1").val() == "") {
			$("#supplier1").tips({
				side : 3,
				msg : '请选择供应商',
				bg : '#AE81FF',
				time : 2
			});
			$("#supplierprice1").focus();
			return false;
		}
		
		if($("#newproduct_time").val()==""){
			$("#newproduct_time").tips({
				side : 3,
				msg : '请选择日期',
				bg : '#AE81FF',
				time : 2
			});
			$("#newproduct_time").focus();
			return false;
		}

		if ($("#specification").val() == "") {
			$("#specification").tips({
				side : 3,
				msg : '请选输入规格',
				bg : '#AE81FF',
				time : 2
			});
			$("#specification").focus();
			return false;
		}

		if ($("#supplierid1").val() != "" && $("#supplierid1").val() != null) {
			if ($("#supplierprice1").val() == "") {
				$("#supplierprice1").tips({
					side : 3,
					msg : '请输入价钱',
					bg : '#AE81FF',
					time : 2
				});
				$("#supplierprice1").focus();
				return false;
			} else if ($("#supplierprice1").val().length != 11
					&& !qian.test($("#supplierprice1").val())) {
				$("#supplierprice1").tips({
					side : 3,
					msg : '金额输入格式不正确！',
					bg : '#AE81FF',
					time : 3
				});
				$("#supplierprice1").focus();
				return false;
			}

		}

		if (!qian.test($("#price1").val())) {
			$("#price1").tips({
				side : 3,
				msg : '金额输入格式不正确！',
				bg : '#AE81FF',
				time : 3
			});
			$("#price1").focus();
			return false;

		}

		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();

	}
</script>
	<script>
	function upload_cover(obj) {
       	console.log(obj);
        $.ajaxFileUpload({
                fileElementId: 'picture_upload',    //需要上传的文件域的ID，即<input type="file">的ID。
                url: 'product/uploadimg.do', //后台方法的路径
                type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                secureuri: false,   //是否启用安全提交，默认为false。
                success: function(data,status) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                   $("#proimage").val(data.responseText);
                   $("#image1").attr("src", data.responseText);
                }
            });
    }
</script>
	</head>
<body>
<div id="page-content" class="clearfix">
	<form action="product/${msg }.do" name="Form" id="Form" method="post">
	   <input type="hidden" name="CARGO_SPACE_ID"  value="${product.tcsid}"/>
		<input type="hidden" name="id" id="id" value="${product.id}"/>
		<input type="hidden" name="packing_measurement_id"  value="${product.packing_measurement_id}"/>
		<div id="zhongxin">
	
		<table id="table_report" class="table table-striped table-bordered table-hover">
		
		<tr>
					<td class='center' class='center'>
					请选择商品类别
					</td>
					<td>
						<ul class="list">
						<li class="title">
						<input id="producttype" type="text" readonly value="" style="float:left; "/>&nbsp;
						<input name="PRODUCT_TYPE_ID" id="PRODUCT_TYPE_ID" type="text" style="display: none" />
						<c:if test="${pd.ROLE_ID ne 24}">
						<a id="menuBtn" onclick="showMenu();return false" >选择</a>
						</c:if>
						</li>
					    </ul>
			</td>
			<td rowspan="4" width="230px">
					 <input type="hidden" id="proimage" name="proimage">
					 <c:if test="${empty product.proimage }"><img id="image1" class="cover-radius"   width="100%" style="cursor: pointer;width:100%;height:230px" /></c:if>
				     <c:if test="${!empty product.proimage }"><img id="image1" class="cover-radius" src="${product.proimage }"  width="100%" style="cursor: pointer;width:100%;height:230px" /></c:if>
	                  <input id="picture_upload" name="file"  type="file" onchange="upload_cover(this)" 
	                	style="position: absolute; right: 0px; top: 0px; width: 230px; height: 260px; opacity: 0; cursor: pointer;"/>
			</td>
		</tr>
			
			<tr>
				<td class='center' >商品名称:</td>
				<td ><input type="text" name="PRODUCT_NAME" id="PRODUCT_NAME" value="${product.product_name}" maxlength="32" placeholder="这里输入商品名称" title="商品名称"/></td>
			</tr>
				<tr>
				<td class='center' >商品条码:</td>
				<td ><input type="number" name="BAR_CODE" id="BAR_CODE" value="${product.bar_code}" maxlength="32" placeholder="这里输入商品条码" title="商品条码"/></td>
			</tr>
		
			
			<tr>
				<td class='center' >箱装数:</td>
				<td ><input type="number" name="box_number" id="box_number" value="${product.box_number}" onkeyup="upzxs()"   maxlength="32" placeholder="箱装数" title="箱装数"/></td>
			</tr>
			<tr>
				<td class='center' >规格:</td>
				<td colspan="2"><input type="text" name="SPECIFICATION" id="specification" value="${product.specification}" maxlength="32"   placeholder="规格" title="规格"/></td>
			</tr>
			
			<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<tr>
				<td class='center' >配送:</td>
				<td colspan="2">
					<select name="type">
						<option value="2" <c:if test="${product.type==2}">selected="selected"</c:if>>仓库配送</option>
						<option value="1"<c:if test="${product.type==1}">selected="selected"</c:if>>加急商品</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class='center' >是否爆款:</td>
				<td colspan="2">
					<select name="bursting">
						<option value="2" <c:if test="${product.bursting==2}">selected="selected"</c:if>>否</option>
						<option value="1" <c:if test="${product.bursting==1}">selected="selected"</c:if>>是</option>
					</select>
				</td>
			</tr>
				<tr>
				<td class='center' >供货价:</td>
				<td  colspan="2"><input type="text" name="price1" id="price1" value="${product.tppaproduct_price}" maxlength="32"   placeholder="零售价" title="零售价"/></td>
				
				</c:when>
				<c:otherwise>
				<tr style="display: none;">
				<td  colspan="2"><input type="text"  name="price1" id="price1" value="${product.tppaproduct_price}" maxlength="32"   placeholder="零售价" title="零售价"/></td>
				</tr>
				</c:otherwise>
				</c:choose>
			
				<tr>
				<td class='center' >供应商:</td>
				<td  colspan="2" >
				<input  name='supplier1' id="supplier1" disabled="disabled" value="${product.supplier_name}"  placeholder="===编号===请选择===公司名称===" />
				<input  name='supplierid1' id="supplierid1" value="${product.tsId}" style="display: none" />
				<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<a class="btn btn-mini btn-light" onclick="selectSupplier('1')" title="选择供应商"><i id="nav-search-icon" class="icon-search"></i></a>
				 采购价：<input type="text" name="supplierprice1"  value="${product.product_price}" id="supplierprice1">
				</c:when>
				<c:otherwise>
				 <span style="display: none"> 采购价：<input type="text" name="supplierprice1"  value="${product.product_price}" id="supplierprice1"></span>
				  </c:otherwise>
				 </c:choose>
				</td>
			    </tr>
			<!-- <tr>
				<td class='center' >商品编码:</td>
				<td ><input type="text" name="PRODUCT_NUM" id="PRODUCT_NUM" value="${product.product_num}" maxlength="32"  disabled placeholder="自动生成" title="商品编码"/></td>
			</tr> -->
			<tr>
				<td class='center' >单位:</td>
				<td colspan="2" >
				<select name="UNIT">
				<c:if test="${empty  unitList}">
				<option value="${product.tmuId}">${product.unit_name}</option>
				</c:if>
					<c:if test="${ not empty unitList}">
				<c:forEach items="${unitList}" var="list">
				<option value="${list.id}">${list.name}</option>
				</c:forEach>
				</c:if>
				
				</select>
				</td>
			</tr>
			<tr>
				<td class='center' >保质期:</td>
				<td  colspan="2"><input type="number" name="EXPIRE_DAYS" id="EXPIRE_DAYS" value="${product.expire_days}" maxlength="32" placeholder="这里输入保质期" title="保质期"/></td>
			</tr>
				
					<tr>
					<td class='center'>仓库</td>
					<c:choose>
					<c:when  test="${pd.ROLE_ID ne 24}">
					<td colspan="2" >
					<select name="WAREHOUSE_ID" id="warehouse">
					<c:forEach items="${warehouseList}" var="list">
					<option value="${list.id}">${list.warehouse_number}|${list.warehouse_name}</option>
					</c:forEach>
					</select>
					</c:when>
					<c:otherwise>
					<td colspan="2" >
					<select disabled="disabled" name="WAREHOUSE_ID" id="warehouse">
					<c:forEach items="${warehouseList}" var="list">
					<option value="${list.id}">${list.warehouse_number}|${list.warehouse_name}</option>
					</c:forEach>
					</select>
					</c:otherwise>
					</c:choose>
				</tr>	
			<tr>
				<td class='center' >商品是否上架：</td>
				<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<td  colspan="2">
				<label>上架<input type="radio" checked="checked" name="IS_SHELVE" value="1"/>
				<span class="lbl"></span>
				</label>
				<label>下架<input type="radio" name="IS_SHELVE" value="2"/>
				<span class="lbl"></span>
				</label>
				</td>
				</c:when>
				<c:otherwise>
				<td   colspan="2">
				<label>上架<input disabled="disabled" type="radio" checked="checked" name="IS_SHELVE" value="1"/>
				<span class="lbl"></span>
				</label>
				<label>下架<input disabled="disabled" type="radio" name="IS_SHELVE" value="2"/>
				<span class="lbl"></span>
				</label>
				</td>
				</c:otherwise>
				</c:choose>
				
			</tr>
			<tr>
				<td class='center' >最小库存数:</td>
				<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<td  colspan="2"><input type="number" name="MIN_STOCK_NUM" id="MIN_STOCK_NUM" value="${product.min_stock_num}" maxlength="32" placeholder="这里输入最小库存数" title="最小库存数"/></td>
				</c:when>
				<c:otherwise>
				<td  colspan="2"><input type="number" disabled="disabled"  name="MIN_STOCK_NUM" id="MIN_STOCK_NUM" value="${product.min_stock_num}" maxlength="32" placeholder="这里输入最小库存数" title="最小库存数"/></td>
				</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td class='center' >最大库存数:</td>
				<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<td  colspan="2"><input type="number" name="MAX_STOCK_NUM" id="MAX_STOCK_NUM" value="${product.max_stock_num}" maxlength="32" placeholder="这里输入最大库存数" title="最大库存数"/></td>
				</c:when>
				<c:otherwise>
				<td  colspan="2"><input type="number" disabled="disabled" name="MAX_STOCK_NUM" id="MAX_STOCK_NUM" value="${product.max_stock_num}" maxlength="32" placeholder="这里输入最大库存数" title="最大库存数"/></td>
				</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				
				<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<td class='center' >商品编码:</td>
				<td  colspan="2"><input type="text" name="product_num" id="product_num" value="${product.product_num}" maxlength="32" placeholder="这里输入商品编码" title="商品编码"/></td>
				</c:when>
				<c:otherwise>
				<input type="hidden"  name="product_num" id="product_num" value="${product.product_num}" maxlength="32" placeholder="这里输入商品编码" title="商品编码"/>
				</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td class='center'>商品日期:</td>
				<td colspan="2"><input class="span10 date-picker" name="newproduct_time" id="newproduct_time" value="${product.newproduct_time}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:205px;" placeholder="商品日期"/></td>
			</tr>
			<tr>
				<td class='center' >整箱重量:</td>
				<td  colspan="2"><input type="text" name="FCL_WEIGHT" id="FCL_WEIGHT" value="${product.fcl_weight}" onkeyup="upzxz()" maxlength="32" placeholder="这里输入重量" title="整体重量"/></td>
			</tr>
			  <tr>
				<td class='center' >单品重量:</td>
				<td colspan="2" ><input type="text" readonly="readonly" id="sku_weight" name="sku_weight" value="${product.sku_weight}" maxlength="32" placeholder="单品重量" title="单品重量"/></td>
			</tr>
			
			
			<tr>
				<td class='center' >单品体积:</td>
				<td colspan="2" ><input type="text" readonly="readonly"  name="sku_volume" id="sku_volume" value="${product.sku_volume}" maxlength="32" placeholder="单品体积" title="单品体积"/></td>
			</tr>
			<tr>
				<td class='center' >整箱体积:</td>
				<td colspan="2" ><input type="text" name="FCL_VOLUME" id="FCL_VOLUME" value="${product.fcl_volume}" onkeyup="upzxt()" maxlength="32" placeholder="这里输入整箱体积" title="整箱体积"/></td>
			</tr>			
				<c:choose>
				<c:when  test="${pd.ROLE_ID  ne 24}">
				<tr> 
				<td class='center' >备注:</td>
				<td  colspan="2"><input type="text" name="REMARKS" id="REMARKS" value="${product.remarks}" maxlength="32" placeholder="这里输入备注" title="备注"/></td>
				</tr>
				</c:when>
				<c:otherwise>
				<tr style="display: none"> 
				<td class='center' >备注:</td>
				<td  colspan="2"><input type="text" name="REMARKS" id="REMARKS" value="${product.remarks}" maxlength="32" placeholder="这里输入备注" title="备注"/></td>
				</tr>
				</c:otherwise>
				</c:choose>
			
			
				<c:choose>
				<c:when  test="${pd.ROLE_ID ne 24}">
				<tr>
				<td class='center' >商品存放位置:</td>
				<td  colspan="2"><input type="text" name="zone" value="${product.zone}" id="zone" />区<input type="number" value="${product.storey}"  name="storey" id="storey" />层<input type="number" value="${product.storey_num}"  name="storey_num" id="storey_num" />编号</td>
				</tr>
				</c:when>
				<c:otherwise>
				<tr style="display: none;">
				<td class='center' >商品存放位置:</td>
				<td  colspan="2"><input type="text" name="zone" value="${product.zone}" id="zone" />区<input type="number" value="${product.storey}"  name="storey" id="storey" />层<input type="number" value="${product.storey_num}"  name="storey_num" id="storey_num" />编号</td>
				</tr>
				</c:otherwise>
				</c:choose>
			
			
			
			<tr>
				<td class='center' style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
	</form>
	
	
	<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul>
</div>
</div>
<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
<script type="text/javascript">
	$(top.hangge());
	$(function(){
		$('.date-picker').datepicker();
	});
</script>

</body>
</html>