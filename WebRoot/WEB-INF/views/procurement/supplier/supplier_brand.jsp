<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	  <link rel="stylesheet" href="static/layui/css/layui.css"  media="all">
	  <style type="text/css">
  	.layui-table{width:99%;margin: 0 auto;margin-top: 10px}
  		.layui-table td, .layui-table th{word-break: keep-all;padding: 9px;white-space: nowrap;text-overflow: ellipsis;}
  		.fl{float:left;margin: 15px 10px 0;}
  		input.layui-input{text-align:center;height:40px}
  		#searchcriteria{width:202px;height:38px}
  		.layui-table th{text-align:center}
</style>
	</head> 
<body>
		
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
  <legend>企业基本情况</legend>
</fieldset>

<form class="layui-form layui-form-pane" action="supplier/savearchives.do">
<div class="layui-form">
  <table class="layui-table">
    <colgroup>
      <col width="150">
      <col width="150">
      <col width="200">
      <col>
    </colgroup>
    <tbody>
      <tr>
        <td>供应商名称：</td>
        <td style="width:25%">${supp.SUPPLIER_NAME }</td>
        <td>供应商编码：</td>
        <td>${supp.SUPPLIER_NUM }</td>
      </tr>
      <tr>
        <td>地址：</td>
        <td>${supp.ADDRESS }</td>
        <td>统一社会信息用代码：</td>
        <td>
        	<input type="hidden" name="supplier_id" value="${supp.id }">
        	<input type="hidden" name="id" value="${findarchives.id }">
        	<div class="layui-input-inline">
    	  		<input type="text" name="codes" value="${findarchives.codes }" lay-verify="required" placeholder="统一社会信息用代码" autocomplete="off" class="layui-input">
 	   		</div>
 	   </td>
      </tr>
     <tr>
        <td>注册资金（万元）：</td>
        <td>
        	<div class="layui-input-inline">
    	  		<input type="text" name="registered_capital" value="${findarchives.registered_capital }"  lay-verify="required" placeholder="注册资金（万元）" autocomplete="off" class="layui-input">
 	   		</div>
        </td>
        <td>员工人数：</td>
        <td>
        	<div class="layui-input-inline">
    	  		<input type="text" name="people" value="${findarchives.people }"  lay-verify="required" placeholder="员工人数" autocomplete="off" class="layui-input">
 	   		</div>
        </td>
      </tr>
      <tr>
        <td>法人代表：</td>
        <td>
        	<div class="layui-input-inline">
    	  		<input type="text" name="representative" value="${findarchives.representative }"  lay-verify="required" placeholder="法人代表" autocomplete="off" class="layui-input">
 	   		</div>
        </td>
        <td>法人身份证号码：</td>
        <td>
        	<div class="layui-input-inline">
    	  		<input type="text" name="id_number"  value="${findarchives.id_number }" lay-verify="required" placeholder="法人身份证号码" autocomplete="off" class="layui-input">
 	   		</div>
        </td>
      </tr>
      <tr>
        <td>经营范围：</td>
        <td colspan="3">
    	  		<input type="text" name="operation" value="${findarchives.operation }"  lay-verify="required" placeholder="经营范围" autocomplete="off" class="layui-input">
        </td>
      </tr>
      <tr>
        <td>联系人（常用联系人）：</td>
        <td>
        	<div class="layui-input-inline">
    	  		<input type="text" name="contacts" value="${findarchives.contacts }"  lay-verify="required" placeholder="联系人" autocomplete="off" class="layui-input">
 	   		</div>
        </td>
        <td>联系电话：</td>
        <td>
        	<div class="layui-input-inline">
    	  		<input type="text" name="phone" value="${findarchives.phone }"  lay-verify="required" placeholder="联系电话" autocomplete="off" class="layui-input">
 	   		</div>
        </td>
      </tr>
      <tr>
      	<td colspan="4"  style="text-align: center;">
      
		    <button class="layui-btn" lay-submit="" type="submit" lay-filter="demo2">添加</button>
		
      	</td>
      </tr>
      
    </tbody>
  </table>
</div>
</form>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
  <legend>合同基本情况</legend>
</fieldset>

<div class="layui-form">
  <table class="layui-table">
    <colgroup>
      <col width="150">
      <col width="150">
      <col width="200">
      <col>
    </colgroup>
    <tbody>
    	<c:if test="${not empty findcontract}">
	    	<c:forEach items="${findcontract}" var="cont" varStatus="vs">
	    	  <tr>
		        <td>第${vs.index+1 }次签订合同日期：</td>
		        <td>${fn:substring(cont.start_time,0,10)}</td>
		        <td>合同到期日期：</td>
		        <td>${fn:substring(cont.end_time,0,10)}</td>
		      </tr>
		      <tr>
		        <td>目前合作情况(正常，停止)：</td>
		        <td>
		        	<c:if test="${cont.state==1 }">停止</c:if>
		        	<c:if test="${cont.state==0}">正常</c:if>
		        </td>
		        <td>合作预期：</td>
		        <td>${fn:substring(cont.end_time,0,10)}</td>
		      </tr>
	    	</c:forEach>
     	</c:if>
     <tr>
     	<td colspan="4"  style="text-align: center;">
		    <button class="layui-btn" lay-submit="" lay-filter="hetong">添加合同</button>
      	</td>
     </tr>
     </tbody>
  </table>
</div>
 <div class="layui-form">
    <table class="layui-table">
  <colgroup>
    <col width="150">
    <col width="150">
    <col width="200">
    <col>
  </colgroup>
  <thead>
    <tr>
      <th>合作品牌</th>
      <th>经营品牌名称</th>
      <th>生产厂家或经销商级别</th>
      <th>授权销售的区域或范围</th>
    </tr> 
  </thead>
  <tbody>
     <c:if test="${not empty findbrand}">
	    	<c:forEach items="${findbrand}" var="brand" varStatus="vs">
	    	  <tr>
		        <td>品牌${vs.index+1 }</td>
		        <td>${brand.brand }</td>
		        <td>${brand.levels}</td>
		        <td>${brand.ranges}</td>
		      </tr>
	    	</c:forEach>
     	</c:if>
     
      <tr>
     	<td colspan="4"  style="text-align: center;">
		    <button class="layui-btn" lay-submit="" lay-filter="pinpai">添加品牌</button>
      	</td>
     </tr>
    </tbody>
  </table>
  </div>


		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript" src="static/js/bootbox.min.js"></script><!-- 确认窗口 -->
		<script src="static/layui/layui.js" charset="utf-8"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script><!--提示框-->
		<script type="text/javascript">
		  $(top.hangge());
		layui.use(['form'], function(){
		  var form = layui.form
		  ,layer = layui.layer;
		   form.on('submit(pinpai)', function(data){
		     //示范一个公告层
		      layer.open({
		        type: 1
		        ,title: '添加品牌' //不显示标题栏
		        ,closeBtn: false
		        ,area: '300px;'
		        ,shade: 0.8
		        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
		        ,btn: ['确定', '取消']
		        ,btnAlign: 'c'
		        ,moveType: 1 //拖拽模式，0或者1
		        ,content: '<div class="layui-form"><table class="layui-table"><tbody>'
		        +'<tr><td> <input type="text" name="brand" id="brand" lay-verify="required" placeholder="经营品牌名称" autocomplete="off" class="layui-input"></td></tr>'
		        +'<tr><td> <input type="text" name="level" id="level" lay-verify="required" placeholder="生产厂家或经销商级别" autocomplete="off" class="layui-input"></td></tr>'
		        +'<tr><td> <input type="text" name="range" id="range" lay-verify="required" placeholder="授权销售的区域或范围" autocomplete="off" class="layui-input"></td></tr>'
		        +'</tbody></table>'
		        ,yes: function(){
		        	var brand=$("#brand").val();
		        	var level=$("#level").val();
		        	var range=$("#range").val();
		        	if(brand==""){
		        		layer.msg("经营品牌名称不能为空");
		        		return ;
		        	}
		        	if(level==""){
		        		layer.msg("生产厂家或经销商级别不能为空");
		        		return ;
		        	}
		        	if(range==""){
		        		layer.msg("授权销售的区域或范围不能为空");
		        		return ;
		        	}
		          	var url='<%=basePath%>supplier/savebrand.do?supplier_id=${supp.id}&brand='+brand+'&range='+range+'&level='+level;
		         	$.post(url,function(data){
		         		alert(data.error);
		         		window.location.href=window.location.href;
		         	},'json');
		        }
		      });
		    return false;
		  });
		  form.on('submit(hetong)', function(data){
		     //示范一个公告层
		      layer.open({
		        type: 1
		        ,title: '添加合同' //不显示标题栏
		        ,closeBtn: false
		        ,area: '300px;'
		        ,shade: 0.8
		        ,id: 'het' //设定一个id，防止重复弹出
		        ,btn: ['确定', '取消']
		        ,btnAlign: 'c'
		        ,moveType: 1 //拖拽模式，0或者1
		        ,content: '<div class="layui-form"><table class="layui-table"><tbody>'
		        +'<tr ><td style="line-height:30px"><input class="span10 date-picker"  name="start_time" id="start_time"   type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px;" placeholder="开始日期" title="合同开始时间"/></td></tr>'
		        +'<tr><td><input class="span10 date-picker"  name="end_time" id="end_time"   type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:110px;" placeholder="结束日期" title="合同结束时间"/></td></tr>'
		        +'</tbody></table>'
		        ,success:function(){
		        	$('.date-picker').datepicker();
		        }
		        ,yes: function(){
		        	var start_time=$("#start_time").val();
		        	var end_time=$("#end_time").val();
		        	
		        	if(start_time==""){
		        		layer.msg("开始时间不能为空");
		        		return ;
		        	}
		        	if(end_time==""){
		        		layer.msg("结束时间不能为空");
		        		return ;
		        	}
		        	if(start_time>end_time){
		        		layer.msg("开始时间不能大于结束时间");
		        		return ;
		        	}
		          	var url='<%=basePath%>supplier/savecontract.do?supplier_id=${supp.id}&start_time='+start_time+'&end_time='+end_time;
		         	$.post(url,function(data){
		         		alert(data.error);
		         		window.location.href=window.location.href;
		         	},'json');
		        }
		      });
		    return false;
		  });
		  
  		})
		</script>
	</body>
</html>