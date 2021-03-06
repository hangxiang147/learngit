<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
</script>
<style type="text/css">
		a:link {
		 text-decoration: none;
		}
		a:visited {
		 text-decoration: none;
		}
		a:hover {
		 text-decoration: none;
		}
		a:active {
		 text-decoration: none;
		}
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	
.clear{clear:both;height:0;font-size:0px;_line-height:0px;}
#namecy{position:absolute;top:10px;left:10px;}
#namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
#namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
	
.clear3{clear:both;height:0;font-size:0px;_line-height:0px;}
#namecy3{position:absolute;top:10px;left:10px;}
#namecy3 span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
#namecy3 span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'meeting'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="administration/meeting/saveMeeting" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">查看会议</h3>
 
        	   <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">会议主题:</label>
			    <div class="col-sm-3">
			    	<div class="col-sm-10"><span class="detail-control">${meetingVO.theme}</span></div>
			    </div>
			  </div> 
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">发起人:</label>
			  	<div class="col-sm-10"><span class="detail-control">${meetingVO.sponsorName}</span></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">会议地点: </label>
			    	<div class="col-sm-2"><span class="detail-control">${meetingVO.place}</span></div>
			    <label for="executor" class="col-sm-1 control-label">会议类型:</label>
			    <div class="col-sm-2"><span class="detail-control"><c:if test="${meetingVO.meetingType==0}">其他</c:if><c:if test="${meetingVO.meetingType==1}">例会</c:if>
			    <c:if test="${meetingVO.meetingType==2}">工作简会</c:if><c:if test="${meetingVO.meetingType==3}">专题会议</c:if><c:if test="${meetingVO.meetingType==4}">总结会议</c:if>
			    <c:if test="${meetingVO.meetingType==5}">部门会议</c:if></span></div>
			  </div>       	
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">开始时间:</label>
	    		<div class="col-sm-2"><span class="detail-control">${meetingVO.beginTime}</span></div>
	    		<label for="endDate" class="col-sm-1 control-label">结束时间:</label>
	    		<div class="col-sm-2"><span class="detail-control">${meetingVO.endTime}</span></div>
	    	  </div>
	    	  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">内容概述：</label>
			  	<div class="col-sm-5"><span class="detail-control">${meetingVO.content}</span></div>
			  </div> 
			 <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">参加个人和部门:</label>
			    <div class="col-sm-5">
			    <span class="detail-control">
			    <c:forEach items="${meetingActorVO1}" var="meetingActorVO">
			    ${meetingActorVO.userName}( <c:forEach items="${meetingActorVO.groupList}" var="group">${group},</c:forEach>)<br>
			    </c:forEach>
			    <c:forEach items="${groups1}" var="group">
			      ${group}<br>
			    </c:forEach>
			    </span>
			    </div>
			  </div>
			 <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">抄送个人和部门：</label>
			    <div class="col-sm-5">
			    <span class="detail-control">
			    <c:forEach items="${meetingActorVO2}" var="meetingActorVO">
			    ${meetingActorVO.userName}( <c:forEach items="${meetingActorVO.groupList}" var="group">${group}</c:forEach>)<br>
			    </c:forEach>
			    <c:forEach items="${groups}" var="group">
			      ${group}<br>
			    </c:forEach>
			    </span>
			    </div>
			  </div>
			  <c:if test="${ not empty meetingMinutesVOs}">
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">会议纪要：</label>
			  	<div class="col-sm-5">
			  	<table>
			  	<thead><tr><td class="col-sm-4 detail-control">纪要内容</td><td class="detail-control" style="word-break:keep-all">负责人</td></tr></thead>
			  	<tbody>
			  	<c:forEach items="${meetingMinutesVOs}" var="meetingMinutesVO">
			  	<tr><td class="col-sm-3 detail-control">${meetingMinutesVO.content }</td><td class="detail-control" style="word-break:keep-all">${meetingMinutesVO.ownerName}</td></tr>
			  	</c:forEach>
			  	</tbody>
			  	</table>
			  	</div>
			  </div>
			  </c:if>
			  
			  <c:if test="${not empty pictureNames}">
			  <div class="form-group">
			  <label for="reason" class="col-sm-1 control-label">图片：</label>
			  <c:forEach var="pictureName" items="${pictureNames}">
			    	<span><input type="hidden"  name="pictures" value="${pictureName}"/><img style="height:100px;" alt="" src="http://www.zhizaolian.com:9000/mt/${meetingVO.meetingID}_${pictureName}">
			    	<%-- <a href="javasript:(0)" onclick='deleteTag(this,"${staffVO.staffID}_${pictureName}")' class="namecolse">×</a> --%></span>
			    </c:forEach>
			    </div>
			    </c:if>
			  <c:if test="${not empty downloadNames}">
			  <div class="form-group">
			  <label for="reason" class="col-sm-1 control-label">文件：</label>
			  <table>
			  <c:forEach var="downloadName" items="${downloadNames}">
			    	<tr><td><span class="detail-control"><a href="<c:url value='/'/>administration/meeting/download?dLDName=${meetingVO.meetingID}_${downloadName}" >${downloadName}</a></span></td></tr>
			    </c:forEach>
			    </table>
			    </div>
			    </c:if>
			  <div class="form-group">
			  <div class="col-sm-6"></div>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>