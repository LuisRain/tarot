<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>微信</title>
	<style type="text/css">
		.clear{clear: both;}
		.new-mui-content-padded h5{margin: 30px auto 10px;}
		.new-mui-button{width: 120px;margin: 0 auto;}
		.img-list ul{list-style: none;padding: 0;margin: 0 1%;}
		.img-list li{display: block;float: left;margin: 1%;width: 23%;border: 1px solid #eee;}
		.img-list li img{display: block;width: 100%;height:20%;}
	}
	</style>
	
	<link rel="stylesheet" href="<%=basePath%>static/wx/css/mui.min.css" />
	<link rel="stylesheet" href="<%=basePath%>static/wx/layer/need/layer.css" />
	<script type="text/javascript" src="<%=basePath%>static/wx/js/mui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/wx/layer/layer.js"></script>
	 <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script> 
	 <script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&key=ZDVBZ-C4Y3V-DDZPS-UIDQC-HY3GO-5MFIK"></script>
	 <script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
    <script type="text/javascript">
    var latitude; // 纬度，浮点数，范围为90 ~ -90
     var longitude; // 经度，浮点数，范围为180 ~ -180。
     var speed; // 速度，以米/每秒计
     var accuracy; // 位置精度
     var localIds=new Array();//图片
     var serverId=new Array();//服务器图片
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: 'wx5c4dcfe568b9429c', // 必填，企业号的唯一标识，此处填写企业号corpid
        timestamp:${wx.timestamp}, // 必填，生成签名的时间戳
        nonceStr:'${wx.nonceStr}', // 必填，生成签名的随机串
        signature:'${wx.signature}',// 必填，签名，见附录1
        jsApiList: [

'chooseImage',
'previewImage',
'uploadImage',
'getLocation',
'closeWindow',

                    ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
    
    wx.ready(function(){
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    getGps();
    });

    
    wx.error(function(res){
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
      
    });
     //上传图片
    function uploadpictures(){
    //验证gps是否开启
    	 wx.getLocation({
      	    type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
      	    success: function (res) {
      	         latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
      	         longitude = res.longitude ; // 经度，浮点数，范围为180 ~ -180。
      	         speed = res.speed; // 速度，以米/每秒计
      	         accuracy = res.accuracy; // 位置精度
      	       wx.chooseImage({
       		    count: 9, // 默认9
       		 sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
       	    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
       		    success: function (res) {
       		    localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
       		    for(var i=0;i<localIds.length;i++){
    		    	$("#img").append('<li><img src="'+localIds[i]+'"></li>');
    		    }
               //上传图片开始
       		 syncUpload(localIds);
               //上传图片结束
       		    }
       		});
      	    },
      	  fail: function(res) { //gps没打开 传入参数不够
      	    if(res.errMsg=='getLocation:fail'){
      	            alert('请打开gps');
      	       
      	    }
      	  
      	    }
      	    
      	});
    	
    }
    //循环上传图片
    var syncUpload = function(localIds){
    		  var localId = localIds.pop();
    		  wx.uploadImage({
    		    localId: localId,
    		    isShowProgressTips: 1,
    		    success: function (res) {
    		    serverId.push(res.serverId); // 返回图片的服务器端ID
    		      //其他对serverId做处理的代码
    		      if(localIds.length > 0){
    		        syncUpload(localIds);
    		      }
    		    }
    		  });
    		};  
  //删除图片
  function deleteimage(){
	  if(serverId.length>0){  
		  layer.open({
			    content: '确认删除图片!',
			    btn: ['删除', '取消'],
			    shadeClose: false,
			    yes: function(){  
			    	 localIds.splice(0,localIds.length);  
				  	  serverId.splice(0,serverId.length);
				  	 $('#img').empty();
				  	 layer.closeAll();
			    }, no: function(){  
			    }
			});
          }else{
 	                 alert("已经没有图片了！"); 
           }
	 
	  
  }
     //获取gps
     function getGps(){
    	 wx.getLocation({
     	    type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
     	    success: function (res) {
     	         latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
     	         longitude = res.longitude ; // 经度，浮点数，范围为180 ~ -180。
     	         speed = res.speed; // 速度，以米/每秒计
     	         accuracy = res.accuracy; // 位置精度
     	        init();
     	    },
     	  fail: function(res) { //gps没打开 传入参数不够
     	    if(res.errMsg=='getLocation:fail'){
     	            alert('请打开gps');
     	       
     	    }
     	  
     	    }
     	    
     	});
    	
     }

     var geocoder,map, marker = null;
     var init = function() {
    	 var center = new qq.maps.LatLng(latitude, longitude);
         map = new qq.maps.Map(document.getElementById('container'),{
             center: center,
             zoom: 15
         });
         var info = new qq.maps.InfoWindow({map: map});
         geocoder = new qq.maps.Geocoder({
             complete : function(result){
                 map.setCenter(result.detail.location);
                 var marker = new qq.maps.Marker({
                     map:map,
                     position: result.detail.location
                 });
                 //添加监听事件 当标记被点击了  设置图层
                 qq.maps.event.addListener(marker, 'click', function() {
                     info.open();
                     info.setContent('<div style="width:280px;height:100px;">'+
                         result.detail.address+'</div>');
                     info.setPosition(result.detail.location);
                 });
             }
         });
       geocoder.getAddress(center);
     }

    </script>
</head>
<body>
	<div class="mui-content">
		<div class="mui-content-padded new-mui-content-padded">
			<h5>订单上传：</h5>
			<button type="button" onclick="uploadpictures()" class="mui-btn mui-btn-success">
				上传订单
			</button>
			
				<button type="button" onclick="deleteimage()" class="mui-btn mui-btn-danger">
				清空上传订单
			</button>
	      
		</div>
			<div class="img-list">
			<ul id="img">
			</ul>
			<div class="clear"></div>
		</div>
		<div class="mui-content-padded new-mui-content-padded">
		<h5>所属站点：<input type="text" name="site" id="site" /></h5>
		<h5>便利店手机号：<input type="text" name="phone"  id="phone" /></h5>
		</div>
		<div class="mui-content-padded new-mui-content-padded">
			<h5>当前位置：</h5>
			<button type="button" onclick="getGps()" class="mui-btn">
				定位
			</button>
			<div class="mui-input-row" id="container" style="margin: 10px 0;">
			<textarea id="textarea" rows="10" placeholder=""></textarea>
			</div>
		</div>
		<div class="mui-content-padded new-mui-button">
			<button type="submit" onclick="submitBtn()" class="mui-btn mui-btn-primary">
				提交
			</button>
			<button type="button" onclick="wx.closeWindow()" class="mui-btn mui-btn-danger">
				取消
			</button>
		</div>
	</div>
	<span class="mui-spinner" style="display: none"></span>
</body>
<script type="text/javascript" charset="utf-8">
	//mui初始化
	mui.init({
		swipeBack: true //启用右滑关闭功能
	});
	function submitBtn(){
		layer.open({type: 2,shadeClose:false});
		var site=$("#site").val();
		var number=/^[0-9]*$/;//只能输入任意数字
		if(site==null||site==""){
			layer.closeAll();
			alert("请填写站点编码！");
			return false;
			}
		
		if(!number.test(site)){
			layer.closeAll();
			alert("站点信息只能是数字！");
			return false;
		}
		var shouji = /^(((13[0-9]{1})|159)+\d{8})$/; //手机号验证
		var phone =$("#phone").val();
		if(phone==null||phone==""){
			layer.closeAll();
			alert("请填写手机号！");
			return false;
		}
		
		if(phone.length != 11 && !shouji.test(phone)){
			layer.closeAll();
			alert("手机号码格式不正确！");
			return false;
		}
		if(serverId.length==0){
			layer.closeAll();
			layer.open({
			    title: '错误',
			    content: '请上传订单图片。'
			});
			return false;
		}
		
		 $.ajax({  
		     type : "post",  
		      url : "<%=basePath%>weixin/saveorder.do",  
		      data : {"latitude":latitude,"longitude":longitude,"serverId":serverId.toString(),"site":site,"phone_sms":phone},  
		      async : true,  
		      success : function(data){ 
		    	  if(data.ok=='ok'){
		    		  layer.closeAll();
		    		  layer.open({
		  			    title: '提示',
		  			    content: '提交成功。',
		  			  end: function(index, layero){
		  				wx.closeWindow();
		  			  }
		  			
		  			});
		    	  }
		    	  else{
		    		  layer.closeAll();
		    		  layer.open({
		  			    title: '提示',
		  			    content: '提交失败,请检查网络无问题后继续提交！',
		    		  });
		    	  }
			    }
		 }); 
		
	}
	
		

</script>
</html>