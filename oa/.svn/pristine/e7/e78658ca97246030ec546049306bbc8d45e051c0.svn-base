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
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'projectManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<h3 class="sub-header" style="margin-top: 0px;">
					项目详情
					<button style="float: right"
						onclick="location.href='javascript:history.go(-1);'"
						class="btn btn-default">返回</button>
				</h3>
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
							<td colspan="5" id="description" style="text-align: left">${projectInfo.projectDescription}</td>
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
										<a
											href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
						</c:if>
								</c:forEach>
								</td>
								</c:if>
								<c:if test="${empty projectInfo.attaList }"><td colspan="5" style="text-align: left">——</td></c:if>
						</tr>
					</table>
				</div>
			
        </div>
      </div>
    </div>
  </body>
</html>