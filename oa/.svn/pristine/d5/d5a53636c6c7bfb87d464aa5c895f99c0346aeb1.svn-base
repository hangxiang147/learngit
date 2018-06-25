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
	$(".description img").each(function(){
		var url = $(this).attr("src");
		$(this).css("cursor","hand");
		$(this).attr("onclick","showPic('','"+url+"')");
	})
});
function completeCheck(result, taskId){
	location.href = "administration/project/taskComplete?result="+result+"&taskId="+taskId+"&comment="+$("#comment").val()+"&taskName=${taskName}&processInstanceID=${projectInfo.processInstanceID}";
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
.tab {
	color: #555555;
	box-shadow: 0px 1px 5px #dddddd;
}

.tab table {
	width: 100%;
	border-collapse: collapse;
}

.tab table tr td {
	border: 1px solid #ddd;
	word-wrap: break-word;
	font-size: 14px
}

.tab table tbody tr td {
	height: auto;
	line-height: 20px;
	padding: 10px 10px;
	text-align: center;
}

.tab table tr .black {
	background: #efefef;
	text-align: center;
	color: #000;
	width: 15%
}
a:hover,a:focus,a:visited{text-decoration:none}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<h3 class="sub-header" style="margin-top: 0px;">
					${taskName}
					<button style="float: right"
						onclick="location.href='javascript:history.go(-1);'"
						class="btn btn-default">返回</button>
				</h3>
				<label style="font-size:14px;color:#428BCA">项目详情>></label>
				<div class="tab">
					<table>
						<tr>
							<td class="black">项目名称</td>
							<td colspan="3">${projectInfo.projectName}</td>
							<td class="black">发起时间</td>
							<td style="width:17%"><fmt:formatDate value="${projectInfo.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="black">负责人</td>
							<td style="width:15%">${projectInfo.projectLeaderName}</td>
							<td class="black">参与人</td>
							<td>${projectInfo.projectParticipantNames==null?'——':projectInfo.projectParticipantNames}</td>
							<td class="black">最终审批人</td>
							<td>${projectInfo.finalAuditorName==null?'——':projectInfo.finalAuditorName}</td>
						</tr>
						<tr>
							<td class="black">项目描述</td>
							<td colspan="5" class="description" style="text-align: left">${projectInfo.projectDescription}</td>
						</tr>
						<tr>
							<td class="black">附件</td>
							<c:if test="${!empty projectInfo.attaList}">
							<td colspan="5" style="text-align: left"><c:forEach
									items="${projectInfo.attaList}" var="atta">
									<c:if test="${atta.suffix=='png'}">
										<a
											href="javascript:showPic('${atta.softName}','${atta.softURL}')"
											style="text-decoration: none"> <img
											src="performance/soft/showImage?attachmentPath=${atta.softURL}"
											style="width: 100px; height: 100px">&nbsp;&nbsp;
										</a>
									</c:if>
									<c:if test="${atta.suffix!='png'}">
										<a href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
						</c:if>
								</c:forEach>
								</td>
								</c:if>
								<c:if test="${empty projectInfo.attaList }"><td colspan="5" style="text-align: left">——</td></c:if>
						</tr>
					</table>
				</div>
				<label style="font-size:14px;margin-top:10px;color:#428BCA">项目进展明细>></label>
					<c:forEach items="${projectReportInfosMap}" var="projectMap">
						<div><span style="font-size:15px;font-weight:bold;">${projectMap.key}</span> 负责的项目进展：</div>
						<c:forEach items="${projectMap.value}" var="projectReportInfo" varStatus="status">
						<div class="tab">
						<table>
							<tr>
								<td class="black">阶段</td>
								<td style="width:35%">第${status.index+1}阶段</td>
								<td class="black">进度</td>
								<td>${projectReportInfo.progress}</td>
							</tr>
							<tr>
								<td class="black">汇报内容</td>
								<td colspan="3" style="text-align: left" class="description">${projectReportInfo.reportContent}</td>
							</tr>
							<tr>
							<td class="black">附件</td>
							<c:if test="${!empty projectReportInfo.attaList}">
							<td colspan="5" style="text-align: left"><c:forEach
									items="${projectReportInfo.attaList}" var="atta">
									<c:if test="${atta.suffix=='png'}">
										<a
											href="javascript:showPic('${atta.softName}','${atta.softURL}')"
											style="text-decoration: none"> <img
											src="performance/soft/showImage?attachmentPath=${atta.softURL}"
											style="width: 100px; height: 100px">&nbsp;&nbsp;
										</a>
									</c:if>
									<c:if test="${atta.suffix!='png'}">
										<a href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
								</c:if>
								</c:forEach>
								</td>
								</c:if>
								<c:if test="${empty projectReportInfo.attaList }"><td colspan="5" style="text-align: left">——</td></c:if>
							</tr>
						</table>
						</div>
						<br>
					</c:forEach>
					</c:forEach>
				<form class="form-horizontal" >
						<div class="form-group">
							<label class="control-label col-sm-1">${taskName=='进度汇报'?'验收':'审批'}意见</label>
							<div class="col-sm-5">
								<textarea  class="form-control" rows="3" id="comment"></textarea>
							</div>
						</div>
			    	  	<div class="form-group">
			    	  	 	<label class="col-sm-1"></label>
			    	  	 	<div class="col-sm-4">
						    <button type="button" class="btn btn-primary" onclick="completeCheck(1,${taskId})">通过</button>
						    <button type="button" class="btn btn-primary" onclick="completeCheck(2,${taskId})" style="margin-left:20px;">不通过</button>
						    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
					  		</div>
					  	</div>
			</form>
        </div>
      </div>
    </div>
  </body>
</html>