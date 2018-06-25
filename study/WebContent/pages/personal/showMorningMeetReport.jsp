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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h2 class="sub-header" style="margin-top:0px;">周早会汇报内容 <button class="btn btn-default" style="float:right" onclick="history.go(-1)">返回</button></h2> 
	       	  <label style="font-size:16px">会议内容：</label><br>
	       	  <div id="description">${morningMeeting.description}</div>
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
        </div>
      </div>
    </div>
</body>
</html>