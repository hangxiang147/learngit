<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
})
function completeTask(result){
	var applyResult = $("#applyResult").val();
	var taskId = $("#taskId").val();
	var id = $("#id").val();
	location.href = "HR/staff/checkCredential?result="+result+"&applyResult="+applyResult+"&taskId="+taskId+"&id="+id;
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style>
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
			<%@include file="/pages/personal/panel.jsp" %>	
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">审核</h3>
				<input type="hidden" id="taskId" value="${taskId }">
				<input type="hidden" id="id" value="${id }">
				<table class="table table-striped">
	  				<thead>
	  					<tr>
	  						<th style="width:15%">序号</th>
	  						<th style="width:20%">证书名</th>
	  						<th style="width:25%">证书查询网址</th>
	  						<th style="width:15%">证书照片</th>
	  					</tr>
	  				</thead>
	  				<tbody>
	  					<c:if test="${not empty credentialEntitys }">
		  					<c:forEach items="${credentialEntitys }" var="item" varStatus="number">
			  					<tr>
			  						<td>${number.index+1}</td>
			  						<td>${item.credentialName }</td>
			  						<td>${item.credentialUrl }</td>
			  						<td>
			  							<a onclick="showHeadPic(${item.credentialUploadId })" href="javascript:void(0)">
											<svg class="icon" aria-hidden="true" title="查看照片" data-toggle="tooltip">
												<use xlink:href="#icon-Detailedinquiry"></use>
											</svg>
										</a>
			  						</td>
			  						<script type="text/javascript">
										function showHeadPic(id) {
											var picData = {
												start : 0,
												data : []
											}
											picData.data.push({
												alt : name,
												src : "HR/staff/checkPicture?credentialUploadId="+id,
											})
											layer.photos({
												offset : '10%',
												photos : picData,
												anim : 5,
											});
										}
									</script>
			  						
			  					</tr>
			  				</c:forEach>
	  					</c:if>
	  				</tbody>
			  	</table>
			  	<div class="col-sm-12" style="margin-top:2%">
					<label class="control-label col-sm-1" style="width:10%;text-align:left;margin-left:2%">
						审批意见：
						<p style="color:red;"><span id="text_count">50</span>/50字</p>
					</label>
					<div class="col-sm-4">
						<textarea rows="3" class="form-control" id="applyResult"></textarea>
					</div>
				</div>
				<script type="text/javascript">  
					/*字数限制*/  
					$("#applyResult").on("input propertychange", function() {  
					    var $this = $(this),  
					        _val = $this.val(),
					        count = "";  
					    if (_val.length > 50) {  
					        $this.val(_val.substring(0, 50));  
					    }  
					    count = 50 - $this.val().length;
					    $("#text_count").text(count);
					}); 
				</script>
				<div class="col-sm-12" style="margin-top:2%">
					<label class="control-label col-sm-1" style="width:10%;text-align:left;margin-left:2%">
						
					</label>
					<div class="col-sm-6">
						<button class="btn btn-primary" onclick="completeTask(1)">通过</button>
						<button class="btn btn-primary" onclick="completeTask(2)" style="margin-left:5%">打回</button>
						<button class="btn btn-default" onclick="history.go(-1)" style="margin-left:5%">返回</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>