<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
function showPicture(obj) {
	var src = window.URL.createObjectURL(document.getElementById($(obj).attr("id")).files[0]); 
	$(obj).parent().children().eq(0).attr("src", src);
	var ext = $(obj).val().substring($(obj).val().indexOf(".")+1);
	$(obj).next().val(ext);
}
var flag = true;
function checkUrl(obj){
	var url=$(obj).val();
	/* var reg = /^(?:http(?:s|):\/\/|)(?:(?:\w*?)\.|)(?:\w*?)\.(?:\w{2,4})(?:\?.*|\/.*|)$/ig; */   
    regExp = /^((https?|ftp|news):\/\/)?([a-z]([a-z0-9\-]*[\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\/[a-z0-9_\-\.~]+)*(\/([a-z0-9_\-\.]*)(\?[a-z0-9+_\-\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$/;
    if (url!= "") {
        if(!(regExp.test(url))){
            $(obj).parent().children().eq(3).text('网址格式不正确');
            flag = false;
        }else{
            $(obj).parent().children().eq(3).text('');
            flag = true;
        }
    }
}
function deleteThis(obj){
	var credentialUploadId = $(obj).attr("data-credentialUploadId");
	var taskId = $("#taskIdAja").val();
	var id = $("#idAja").val();
	$.ajax({
		url:'HR/staff/deleteCredentialUpload',
		data:{'credentialUploadId':credentialUploadId},
		type:'post',
		success:function(data){
			/* window.location.href='HR/staff/postCredentialAmend?taskId='+taskId+'&id='+id;
			Load.Base.LoadingPic.FullScreenShow(null); */
			$(obj).parent().parent().remove();
		}
	})
}
var index = 10;
function addCredential(){
	index++;
	var tr = document.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	if(tr.length<=5){
		$("#credential").append(
				'<tr>'+
				'<td>'+
					'<div class="col-sm-12">'+
					'<input maxlength="10" type="text" class="form-control" id="credentialName'+index+'" name="credentialUploadEntityList['+index+'].credentialName" required="required">'+
					'</div>'+
				'</td>'+
				'<td>'+
					'<div class="col-sm-12">'+
					'<img src="" alt="证书照片 " id="photo'+index+'" class="img-thumbnail" style="width: 50px; height: 35px;" onclick="showHeadPic(this)">'+
	    			'<input type="file" id="picture'+index+'" name="credentialUploadEntityList['+index+'].credentialPicture" required="required" accept="image/gif,image/jpeg,image/jpg,image/png" style="padding:6px 0px;float:right;" onchange="showPicture(this)">'+
	    			'<input type="hidden" id="ext'+index+'" name="credentialUploadEntityList['+index+'].credentialPictureExt" required="required"/>'+
					'</div>'+
				'</td>'+
				'<td>'+
					'<div class="col-sm-12">'+
					'<input type="text" class="form-control" onblur="checkUrl(this)" id="credentialUrl'+index+'" name="credentialUploadEntityList['+index+'].credentialUrl" required="required">'+
			    	'<input type="hidden" class="form-control" name="credentialUploadEntityList['+index+'].credentialEntityId" value="${id }">'+
			    	'<input type="hidden" class="form-control" name="credentialUploadEntityList['+index+'].offerUserId" value="${item.offerUserId }">'+
			    	'<b style="color:red;" class="urlInfo"></b>'+
			    	'</div>'+
				'</td>'+
				'<td>'+
					'<a class="delete" href="javacript:void(0)" onclick="$(this).parent().parent().remove()">'+
						'<svg class="icon" aria-hidden="true">'+
							'<use xlink:href="#icon-delete"></use>'+
						'</svg>'+
					'</a>'+
				'</td>'+
			'</tr>'
		);
	}else{
		layer.alert('最多只能上传6个证书', {offset:'100px',title:'提示'});
	}
	
}
function check_form(){
	var flag = true;
	var tr = document.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
	if(tr.length==0){
		layer.alert('最少需要上传1个证书',{offset:'100px',title:'提示'});
		flag = false;
	}
	$(".urlInfo").each(function(){
		$(this).text();
		if($(this).text()!=''){
			layer.alert('网址格式不正确',{offset:'100px',title:'提示'});
			flag = false;
		}
	})
	return flag;
}
</script>
<style>
.icon {
	width: 1.5em;
	height: 1.5em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/personal/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<form action="HR/staff/save_amendCredential?taskId=${taskId }&id=${id }"
					enctype="multipart/form-data" method="post" class="form-horizontal" onsubmit="return check_form()">
					<s:token></s:token>
					<h3 class="sub-header" style="margin-top: 0px;">
						更改后上传<span style="font-size:14px;">(最多上传6个证书)</span>
						<div style="float:right;">
					    	<div class="btn btn-primary" onclick="addCredential()">
					    		<span class="glyphicon glyphicon-plus"></span> 证书
					    	</div>
					    </div>
					</h3>
					<input id="taskIdAja" type="hidden" value="${taskId }" name="taskId">
					<input id="idAja" type="hidden" value="${id }" name="id">
					<c:if test="${not empty applyResult }">
					<h5 style="color: black; margin-top: 2%">
						审核意见：<span style="color: #337ab7;">${applyResult }</span>
						
					</h5>
					</c:if>
					
					<table class="table" style="margin-top: 2%">
						<thead>
							<tr>
								<th style="width:22%;text-align:center;">证书名(限制10个字)</th>
			        			<th style="width:18%;text-align:center;">证书照片</th>
			        			<th style="width:22%;text-align:center;">证书查询地址</th>
			        			<th style="width:3%;text-align:center;">操作</th>
							</tr>
						</thead>
						<tbody id="credential">
							<c:if test="${not empty credentialEntitys }">
								<c:forEach items="${credentialEntitys }" var="item"
									varStatus="number">
									<h3 id="size${item.credentialUploadId }" style="display:none;" data-credentialEntitys="${fn:length(credentialEntitys)-1 }">${fn:length(credentialEntitys)-1 }</h3>
									<tr>
										<td>
											<div class="col-sm-12">
												<input maxlength="10" type="text" value="${item.credentialName }"
													class="form-control"
													id="credentialName${item.credentialUploadId }"
													name="credentialUploadEntityList[${number.index }].credentialName"
													required="required">
												<input type="hidden" value="${item.credentialUploadAddTime }"
													name="credentialUploadEntityList[${number.index }].addTime">
											</div>
										</td>
										<td>
											<div class="col-sm-12">
												<img
													src="HR/staff/checkPicture?credentialUploadId=${item.credentialUploadId }"
													alt="证书照片 " id="photo${item.credentialUploadId }"
													class="img-thumbnail"
													style="width: 50px; height: 35px;"
													required="required"
													onclick="showHeadPic(this)">
												<input type="file" id="picture${item.credentialUploadId }"
													name="credentialUploadEntityList[${number.index }].credentialPicture"
													accept="image/gif,image/jpeg,image/jpg,image/png"
													style="padding: 6px 0px;float:right;" onchange="showPicture(this)">
												<input type="hidden" id="ext${item.credentialUploadId }"
													name="credentialUploadEntityList[${number.index }].credentialPictureExt"/>
												
											</div>
										</td>
										<script type="text/javascript">
										function showHeadPic(obj) {
											var imgsrc = $(obj).attr("src");
											var picData = {
												start : 0,
												data : []
											}
											picData.data.push({
												alt : name,
												src : imgsrc,
											})
											layer.photos({
												offset : '10%',
												photos : picData,
												anim : 5,
											});
										}
										var ind = $("#size${item.credentialUploadId }").attr("data-credentialEntitys");
										
									</script>
										<td>
											<div class="col-sm-12">
												<input type="text" onblur="checkUrl(this)"
													value="${item.credentialUrl }" class="form-control"
													id="credentialUrl${item.id }"
													name="credentialUploadEntityList[${number.index }].credentialUrl"
													required="required">
												<input type="hidden" class="form-control"
													name="credentialUploadEntityList[${number.index }].credentialEntityId"
													value="${id }">
												<input type="hidden" class="form-control"
													name="credentialUploadEntityList[${number.index }].id"
													value="${item.credentialUploadId }"> 
												<b style="color: red;" class="urlInfo"></b>
												<input type="hidden" class="form-control"
													name="credentialUploadEntityList[${number.index }].offerUserId"
													value="${item.offerUserId }">
											</div>
										</td>
										<td>
											<a class="delete" href="javacript:void(0)" onclick="deleteThis(this)" data-credentialUploadId="${item.credentialUploadId }"> 
												<svg class="icon" aria-hidden="true">
					 								<use xlink:href="#icon-delete"></use>
					 							</svg>
											</a>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					<div class="form-group">
						<label for="formButton" class="col-sm-1 control-label"></label>
						<div class="col-sm-2">
							<button type="submit" id="formButton" class="btn btn-primary">提交</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>