<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'certificateList'"></s:set>
      	<s:set name="selectedPanel" value="'certificateList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <button type="button" class="btn btn-default"
		onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:10px;margin-right:20px">返回</button>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <c:forEach items="${attachmentNames}" var="attachment" varStatus="index">
				<img src="administration/certificate/showImage?certificateImage=${attachment}"/>
			</c:forEach>
      	</div>
      </div>
    </div>
</body>
</html>