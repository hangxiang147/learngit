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
        <s:set name="panel" value="'projectManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<h3 class="sub-header" style="margin-top: 0px;">
					项目进展明细
					<button style="float: right"
						onclick="location.href='javascript:history.go(-1);'"
						class="btn btn-default">返回</button>
				</h3>
					<c:forEach items="${projectReportInfosMap}" var="projectMap">
						<div style="margin-top:10px"><span style="font-size:15px;font-weight:bold;">${projectMap.key}</span> 负责的项目进展：</div>
						<div style="color:red"><c:if test="${empty projectMap.value}">暂无进展</c:if></div>
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
        </div>
      </div>
    </div>
  </body>
</html>