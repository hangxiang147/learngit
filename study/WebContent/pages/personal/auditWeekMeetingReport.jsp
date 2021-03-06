<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
var showPic = function(name, path) {
	var picData = {
		start : 0,
		data : []
	}
	picData.data.push({
		alt : name,
		src : "performance/soft/showImage?attachmentPath=" + path
	})
	layer.photos({
		offset : '50px',
		photos : picData,
		anim : 5
	});
}
$(function(){
	$("#description img").each(function(){
		var url = $(this).attr("src");
		$(this).css("cursor","hand");
		$(this).attr("onclick","showPic('','"+url+"')");
	})
});
function auditWeekMeetReport(result, taskId, processInstanceId){
	var comment = $("#comment").val();
	location.href = "personal/auditMeetReport?comment="+comment+"&result="+result+"&taskId="+taskId+"&processInstanceId="+processInstanceId;
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;width:15%;}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
	.tab table tr .title {text-align:center;color:#000;width:15%;background:#efefef}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	 <s:set name="selectedPanel" value="'newWeekMeetingReport'"></s:set>
         <%@include file="/pages/personal/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h2 class="sub-header" style="margin-top:0px;">周早会审批 <button class="btn btn-default" style="float:right" onclick="history.go(-1)">返回</button></h2> 
		   	 <div class="tab">
       	  		<table>
       	  			<tr>
       	  				<td class="title">汇报人</td>
       	  				<td>${morningMeetingVo.userName}</td>
       	  				<td class="title">所属部门</td>
       	  				<td>${morningMeetingVo.department}</td>
       	  				<td class="title">汇报时间</td>
       	  				<td><fmt:formatDate value="${morningMeetingVo.reportTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
       	  			</tr>
       	  			<c:if test="${morningMeetingVo.hasMeeting=='否'}">
       	  			<tr>
       	  				<td class="title">未开早会的原因</td>
       	  				<td colspan="5">${morningMeetingVo.noMeetingReason}</td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">情况说明</td>
       	  				<td colspan="5" style="height:100px">${morningMeetingVo.remark}</td>
       	  			</tr>
       	  			</c:if>
       	  			<c:if test="${morningMeetingVo.hasMeeting=='是'}">
       	  			<tr>
       	  				<td class="title">会议内容</td>
       	  				<td colspan="5" style="height:100px" id="description">${morningMeetingVo.description}</td>
       	  			</tr>
       	  			</c:if>
       	  		</table>
       	  	  </div>
	       	  <c:if test="${!empty attaList}">
	       	  		<label style="font-size:16px">附件：</label><br>
	       	  		<c:forEach items="${attaList}" var="atta">
	       	  				<c:if test="${atta.suffix=='png'}">
								<a href="javascript:showPic('${atta.softName}','${atta.softURL}')"
									style="text-decoration: none"> <img
									src="performance/soft/showImage?attachmentPath=${atta.softURL}"
									style="width: 200px; height: 200px">&nbsp;&nbsp;
								</a>
							</c:if>
							<c:if test="${atta.suffix!='png'}">
								<a href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
							</c:if>
	       	  		</c:forEach>
	       	  </c:if>
	       	  <form class="form-horizontal" style="margin-top:20px">
	       	  	<div class="form-group">
		       	  	<label class="label-control col-sm-1">审批意见</label>
		       	  	<div class="col-sm-5">
		       	  		<textarea rows="3" id="comment" class="form-control"></textarea>
		       	  	</div>
	       	  	</div>
	       	  	<div class="form-group">
	       	  		<div class="col-sm-1"></div>
	       	  		<div class="col-sm-5">
		       	  	<button type="button" class="btn btn-primary" onclick="auditWeekMeetReport(${morningMeetingVo.noMeetingReason=='延期召开'&&morningMeetingVo.hasMeeting=='否'?41:1 },
		       	  	 	${morningMeetingVo.taskId}, ${morningMeetingVo.processInstanceID})">通过</button>
		       	  	<button type="button" class="btn btn-primary" style="margin-left:8px" onclick="auditWeekMeetReport(2, ${morningMeetingVo.taskId},
		       	  		${morningMeetingVo.processInstanceID})">不通过</button>
		       	  	</div>
	       	  	</div>
       	  	  </form>
       	  	  <form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:5px;">流程状态</h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <c:if test="${not empty task }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
			  	<div class="col-sm-10">
			  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
			  	</div>
			  	<c:if test="${not empty comments }">
        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
        	  	<c:if test="${comment.taskID == task.taskID }">
        	  		<div class="col-sm-2"></div>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${comment.content }</span>
				  	</div>
        	  	</c:if>
        	  	</c:forEach>
        	  	</c:if>
			  </div>
			  </c:if>
			  </c:forEach>
			  </c:if>
			  </form>
        </div>
      </div>
    </div>
</body>
</html>