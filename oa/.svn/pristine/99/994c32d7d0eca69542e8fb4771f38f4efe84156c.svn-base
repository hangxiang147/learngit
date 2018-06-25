<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/layer/layer.js"></script>
<script type="text/javascript">
function showPic(id) {
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
function completeTask(result){
	var comment = $("#comment").val();
	if(result==2 && !comment.trim()){
		layer.alert("审批意见不可为空",{offset:'100px'});
		return;
	}
	location.href = "HR/staffSalary/auditRewardAndPunishment?taskId=${taskId}&result="+result+"&comment="+comment;
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	.tab tr th, .tab tr td{
		word-wrap:break-word;
		word-break:break-all;
		font-size:10px;
		padding:8px 7px;
		text-align:center;
		border:1px solid #ddd
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h3 class="sub-header" style="margin-top:0px;">行政奖惩审批</h3>
				<form class="form-horizontal">
				<div class="form-group">
				<label class="control-label col-sm-1" style="width:10%">奖惩人员：</label>
				<span class="col-sm-5 control-label" style="text-align:left">${rewardAndPunishmentVo.requestUserNames}</span>
				</div>
				<div class="form-group">
				<label class="control-label col-sm-1" style="width:10%">奖惩类型：</label>
				<span class="col-sm-1 control-label" style="text-align:left">${rewardAndPunishmentVo.type==0 ? '奖励':'惩罚'}</span>
				<label class="control-label col-sm-1">额度：</label>
				<span class="col-sm-2 control-label" style="text-align:left">${rewardAndPunishmentVo.money}元</span>
				</div>
				<div class="form-group">
				<label class="control-label col-sm-1" style="width:10%">生效时间：</label>
				<span class="col-sm-2 control-label" style="text-align:left">${rewardAndPunishmentVo.effectiveDate}</span>
				</div>
				<div class="form-group">
				<label class="control-label col-sm-1" style="width:10%">奖惩原因：</label>
				<span class="col-sm-2 control-label" style="text-align:left">${rewardAndPunishmentVo.reason}</span>
				</div>
				<c:if test="${not empty rewardAndPunishmentVo.attachments}">
				<div class="form-group">
					<label class="control-label col-sm-1" style="width:10%">附件：</label>
					<div class="col-sm-10">
						<c:forEach items="${rewardAndPunishmentVo.attachments}" var="attachment">
						<c:if test="${attachment.imageType=='png'}">
						<img onclick="showPic(${attachment.attachment_ID})"
						style="cursor: pointer; width:100px;"
						src="/administration/notice/downloadPic?id=${attachment.attachment_ID}">
						</c:if>
						<c:if test="${attachment.imageType!='png'}">
						&nbsp;
						<a href="/administration/notice/downloadAtta?id=${attachment.attachment_ID}">${attachment.softName}</a>
						</c:if>
						</c:forEach>
					</div>
				</div>
				</c:if>
				<div class="form-group">
					<label class="col-sm-1 control-label" style="width:10%">审批意见：</label>
					<div class="col-sm-5">
						<textarea class="form-control" rows="4" id="comment"></textarea>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-1" style="width:10%"></div>
					<div class="col-sm-5">
					<button type="button" onclick="completeTask(1)" class="btn btn-primary">同意</button>
					<button type="button" style="margin-left:2%" onclick="completeTask(2)" class="btn btn-primary">不同意</button>
					<button type="button" style="margin-left:2%" onclick="history.go(-1)" class="btn btn-default">返回</button>
					</div>
				</div>
				</form>
		</div>
      </div>
    </div>
</body>
</html>