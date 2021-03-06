<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js?version=<%=Math.random()%>"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
});
function checkName(){
	var appName = $("#addApp input[name='appInfo.appName']").val();
	var flag = true;
	$.ajax({
		url:'userCenter/checkAppName',
		data:{'appName':appName},
		async:false,
		success:function(data){
			if(data.exist){
				layer.alert("应用名称已存在",{offset:'100px'});
				flag = false;
			}
		}
	});
	return flag;
}
function checkNameForUpdate(){
	var appName = $("#updateApp input[name='appInfo.appName']").val();
	var id = $("input[name='appInfo.id']").val();
	var flag = true;
	$.ajax({
		url:'userCenter/checkAppName',
		data:{'appName':appName, 'id':id},
		async:false,
		success:function(data){
			if(data.exist){
				layer.alert("应用名称已存在",{offset:'100px'});
				flag = false;
			}
		}
	});
	return flag;
}
function showAppDescription(description){
	layer.alert(description, {offset:'100px',title:'应用简介'});
}
function showAppSecret(appSecret){
	layer.alert("<span style='color:red'>提示：密钥很重要，不可泄露</span><br>"+appSecret,{offset:'100px'});
}
function addLoding(){
	if(!checkName()){
		return false;
	}
	$("#addApp").modal('hide');
	Load.Base.LoadingPic.FullScreenShow(null);
}
function addLodingForUpdate(){
	if(!checkNameForUpdate()){
		return false;
	}
	$("#updateApp").modal('hide');
	Load.Base.LoadingPic.FullScreenShow(null);
}
function modifyApp(id, appName, goHomeUrl, description, iconId){
	$("#updateApp input[name='appInfo.appName']").val(appName);
	$("#updateApp input[name='appInfo.goHomeUrl']").val(goHomeUrl);
	$("#updateApp textarea[name='appInfo.description']").val(description);
	$("#updateApp input[name='appInfo.id']").val(id);
	$("#iconImg").attr("src", "/administration/notice/downloadPic?id="+iconId);
	$("#updateApp").modal('show');
}
function deleteApp(appId){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		location.href = "userCenter/deleteApp?appId="+appId;
		Load.Base.LoadingPic.FullScreenShow(null);
	});
}
</script>
<style type="text/css">
.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'userCenter'"></s:set>
        <s:set name="selectedPanel" value="'appManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">应用列表</h3>
        <a class="btn btn-primary" href="javascript:void(0)" data-toggle="modal" data-target="#addApp"><span class="glyphicon glyphicon-plus"></span> 应用</a>
			<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">图标</th>
		                  <th>注册应用名</th>
		                  <th>appId</th>
		                  <th>注册人员</th>
		                  <th>注册时间</th>
		                  <th style="width:15%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:forEach items="${appInfos}" var="appInfo" varStatus="index">
		  					<tr>
		  						<td><img src="/administration/notice/downloadPic?id=${appInfo.iconId}" style="width:25px;height:25px;border-radius:2px"></td>
		  						<td>${appInfo.appName}</td>
		  						<td>${appInfo.appId}</td>
		  						<td>${appInfo.creatorName}</td>
		  						<td><fmt:formatDate value="${appInfo.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		  						<td>
		  							<a href="javascript:showAppSecret('${appInfo.appSecret}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="查看密钥" data-toggle="tooltip">
    									<use xlink:href="#icon-key"></use>
									</svg>
									</a>
									&nbsp;
		  							<a href="javascript:showAppDescription('${hf:format(appInfo.description)}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="查看应用简介" data-toggle="tooltip">
    									<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
									</a>
									&nbsp;
		  							<a href="javascript:modifyApp('${appInfo.id}', '${appInfo.appName}', '${appInfo.goHomeUrl}', '${hf:format(appInfo.description)}', '${appInfo.iconId}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
    									<use xlink:href="#icon-modify"></use>
									</svg>
									</a>
									&nbsp;
		  							<a href="javascript:deleteApp('${appInfo.id}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
    									<use xlink:href="#icon-delete"></use>
									</svg>
									</a>
		  						</td>
		  					</tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
          		</div> 
        </div>
      </div>
    </div>
   	<div id="addApp" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form method="post" action="userCenter/saveApp" enctype="multipart/form-data"
		class="form-horizontal" onsubmit="return addLoding()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">注册应用</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
	    	  		<label class="col-sm-3 control-label">注册名称<span style="color:red"> *</span></label>
		    		<div class="col-sm-8">
		    		<input autoComplete="off" class="form-control" required name="appInfo.appName">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">主页地址</label>
		    		<div class="col-sm-8">
		    		<input autoComplete="off" class="form-control" required name="appInfo.goHomeUrl">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">应用简介<span style="color:red"> *</span><span style="font-size:12px;font-weight:bold">（不超过200字）</span></label>
		    		<div class="col-sm-8">
		    		<textarea class="form-control" rows="3" required name="appInfo.description"></textarea>
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">应用图标<span style="color:red"> *</span></label>
	    	  		<div class="col-sm-8">
	    	  			<input type="file" name="icon" accept="image/gif,image/jpeg,image/jpg,image/png" required>
	    	  		</div>
	    	  	</div>
	    	  	<div style="color:red;font-size:12px">注：应用图标，尺寸：512*512PX，圆角半径弧度：70PX，图片格式：PNG。</div>
				<div style="color:red;font-size:12px;margin-top:5px">示例：<img style="width:50%" src="/assets/images/rightlogo.png"></div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<div id="updateApp" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form method="post" action="userCenter/saveApp" enctype="multipart/form-data"
		class="form-horizontal" onsubmit="return addLodingForUpdate()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">修改应用信息</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
	    	  		<label class="col-sm-3 control-label">注册名称<span style="color:red"> *</span></label>
		    		<div class="col-sm-8">
		    		<input autoComplete="off" class="form-control" required name="appInfo.appName">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">主页地址</label>
		    		<div class="col-sm-8">
		    		<input autoComplete="off" class="form-control" required name="appInfo.goHomeUrl">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">应用简介<span style="color:red"> *</span><span style="font-size:12px;font-weight:bold">（不超过200字）</span></label>
		    		<div class="col-sm-8">
		    		<textarea class="form-control" rows="3" required name="appInfo.description"></textarea>
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">应用图标<span style="color:red"> *</span></label>
	    	  		<div class="col-sm-1">
	    	  			<img id="iconImg" src="" style="width:25px;height:25px;border-radius:2px">
	    	  		</div>
	    	  		<div class="col-sm-7">
	    	  			<input type="file" name="icon" accept="image/gif,image/jpeg,image/jpg,image/png">
	    	  		</div>
	    	  	</div>
	    	  	<div style="color:red;font-size:12px">注：应用图标，尺寸：512*512PX，圆角半径弧度：70PX，图片格式：PNG。</div>
				<div style="color:red;font-size:12px;margin-top:5px">示例：<img style="width:50%" src="/assets/images/rightlogo.png"></div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			<input name="appInfo.id" type="hidden">
		</div>
		</form>
	</div>
	</div>
  </body>
</html>