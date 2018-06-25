<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
function auditBrandAuth(result, processInstanceId, taskId){
	var comment = $("#comment").val();
	location.href = "administration/process/auditBrandAuth?comment="+comment+"&result="+result+"&taskId="+taskId+"&processInstanceId="+processInstanceId;
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;width:15%;}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
	.tab table tr .title {text-align:center;color:#000;width:10%;background:#efefef}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	 <s:set name="selectedPanel" value="'findTaskList'"></s:set>
         <%@include file="/pages/personal/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">品牌授权审批 <button class="btn btn-default" style="float:right" onclick="history.go(-1)">返回</button></h3> 
		   	 <div class="tab">
       	  		<table>
       	  			<tr>
       	  				<td class="title">申请人</td>
       	  				<td>${brandAuth.userName}</td>
       	  				<td class="title">所属部门</td>
       	  				<td>${department}</td>
       	  				<td class="title">申请时间</td>
       	  				<td><fmt:formatDate value="${brandAuth.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">公司</td>
       	  				<td>${brandAuth.companyName}</td>
       	  				<td class="title">联系人</td>
       	  				<td>${brandAuth.contact}</td>
       	  				<td class="title">电话</td>
       	  				<td>${brandAuth.telephone}</td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">店铺名称</td>
       	  				<td>${brandAuth.shopName}</td>
       	  				<td class="title">店铺网址</td>
       	  				<td colspan="3">${brandAuth.shopAddress}</td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">平台</td>
       	  				<td>${brandAuth.platform}</td>
       	  				<td class="title">品牌</td>
       	  				<td>${brandAuth.brand}</td>
       	  				<td class="title">授权时间</td>
       	  				<td>${brandAuth.authBeginDate} 至 ${brandAuth.authEndDate}</td>
       	  			</tr>
       	  		</table>
       	  	  </div>
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
		       	  	<button type="button" class="btn btn-primary" onclick="auditBrandAuth(1, ${brandAuth.processInstanceID}, ${taskId})">通过</button>
		       	  	<button type="button" class="btn btn-primary" style="margin-left:8px" onclick="auditBrandAuth(2, ${brandAuth.processInstanceID}, ${taskId})">不通过</button>
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