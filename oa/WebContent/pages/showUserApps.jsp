<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="/assets/images/favicon.ico">
<title>个人应用中心</title>
<script src="/assets/icon/iconfont.js?verson=<%=Math.random()%>"></script>
<script type="text/javascript" src="/assets/js/jquery-2.1.1.min.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
<script src="/assets/js/loading.js"></script>
<link href="/assets/css/mask.css" rel="stylesheet"> 
<style type="text/css">
.app td {
	text-align: center
}

.app {
	display: inline-block;
	margin: 20px 40px 20px 0;
}
.img-thumbnail {
  display: inline-block;
  max-width: 100%;
  height: auto;
  padding: 0;
  line-height: 1.42857143;
  background-color: #fff;
  border: #fff !important;
  border-radius: 50%;
  -webkit-transition: all .2s ease-in-out;
       -o-transition: all .2s ease-in-out;
          transition: all .2s ease-in-out;
}
.homepage {
	background: url(/assets/images/homepage.png) no-repeat center top;
	background-size: cover;
}
.icon {
   width: 1.5em; height: 1.5em;
   vertical-align: -0.15em;
   fill: currentColor;
   overflow: hidden;
}
</style>

</head>
<body class="homepage">
	<div id="codeContainer" style="position: absolute; top: 20%; left: 38%; display: none"></div>
	<div id="showApps">
	<div style="display:inline-block">
	<span style="float: left"><img style="margin-top:7px" alt="" src="/assets/images/logo.png?verson=<%=Math.random()%>>" /></span>
	<span style="font-size:27px;color:#fff;font-weight:bold;margin-top:3%;display:inline-block">智 造 链</span>
	</div>
	<ul style="list-style:none;float:right">
		<li style="display:inline-block">
		<span
			style="color:#fff; padding-right:10px;display: inline-block;font-size:14px">
				<c:if test="${headImgId!=null}">
					<img onclick="showHeadPic(${headImgId})"
						style="cursor: pointer; width:25px; height: 25px; border-radius: 2px;margin-bottom:-11%"
						src="/administration/notice/downloadPic?id=${headImgId}">
				</c:if> <c:if test="${headImgId==null}">
					<span class="glyphicon glyphicon-user"></span>
				</c:if> ${user.lastName}
		</span>
		</li>
		<li style="display:inline-block">
		<a href="/logout" style="color:#fff">
    				<svg class="icon" aria-hidden="true" title="退出" data-toggle="tooltip" style="margin-bottom: -18%;">
  						<use xlink:href="#icon-tuichu"></use>
  					</svg>
		</a></li>
	</ul>
<%-- 	<div style="position:absolute;top:45%;left:45%;width:6%">
		<img class="img-thumbnail" alt="个人照片 " src="/HR/staff/getPicture?userID=${user.id}">
	</div> --%>
	<div style="position:absolute;top:15%;left:15%;right:15%">
		<table class="app">
			<tr>
				<td><a target="_blank" href="/goHome"><img style="width:72px"
					src="/assets/images/oa.png?verson=<%=Math.random()%>"></a></td>
			</tr>
			<tr>
				<td style="font-weight: bold; color: #fff;font-size:14px">OA办公系统</td>
			</tr>
		</table>
		<c:forEach items="${appInfos}" var="appInfo">
			<table class="app">
				<tr>
					<td><a target="_blank" href="/goAppHome?appId=${appInfo[2]}"><img style="width:72px"
						src="/administration/notice/downloadPic?id=${appInfo[0]}"></a></td>
				</tr>
				<tr>
					<td style="font-weight: bold; color: #fff;font-size:14px">${appInfo[1]}</td>
				</tr>
			</table>
		</c:forEach>
	</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var msg = '${message}';
			var goHomeUrl = '${goHomeUrl}';
			if(msg){
				layer.alert(msg, {offset:'100px'});
			}
			if('${bind}'=='false'){
				var appID = "wx9c2800cfc46895f0";
				var uri = "http://www.zhizaolian.com:9090";
				var obj = new WxLogin({
				  id: "codeContainer",
				  appid: appID,
				  scope: "snsapi_login", 
				  redirect_uri: encodeURI(uri),
				  state: guid()
				});
				$("#codeContainer").css("display","block");
				$("#showApps").css("display", "none");
			}
		});
		function guid() {
		    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		        return v.toString(16);
		    });
		} 
		function showHeadPic(id) {
			var picData = {
				start : 0,
				data : []
			}
			picData.data.push({
				alt : name,
				src : "/administration/notice/downloadPic?id="+id,
			})
			layer.photos({
				offset : '10%',
				photos : picData,
				anim : 5,
			});
		}
		function goPath(url){
			if(url){
				window.location.href = url;
			}
			Load.Base.LoadingPic.FullScreenShow(null);
		}
	</script>
</body>
</html>
