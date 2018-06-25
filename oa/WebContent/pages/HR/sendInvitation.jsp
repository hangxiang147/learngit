<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function check() {
		$("#submitButton").addClass("disabled");
		return true;
	}
	
</script>
<style type="text/css">
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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<s:set name="panel" value="'HRCenter'"></s:set>
		<s:set name="selectedPanel" value="'HRCenter'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="HR/process/save_formalInvitation" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">转正邀请</h3> 
        	  <input type="hidden" id="taskID" value="${taskID}" name="taskID"/>
        	  <input type="hidden" id="result" value="10" name="result" />
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">转正人</label>
			  	<div class="col-sm-11"><span class="detail-control">${userName }</span></div>
			  </div>
			  <div class="form-group">
			  	<label for="Grade" class="col-sm-1 control-label">正式职级</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="Grade" name="formalVO.gradeName" required="required">
				      <option value="">请选择</option>
					  <c:if test="${not empty gradeVOs }">
				      	<s:iterator id="grade" value="#request.gradeVOs" status="st">
				      		<option value="<s:property value="#grade.gradeID" />_<s:property value="#grade.gradeName"/>"><s:property value="#grade.gradeName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="salary" class="col-sm-1 control-label">薪资标准</label>
			    <div class="col-sm-5">
			    	<textarea id="salary" class="form-control" name="formalVO.salary" required="required"></textarea>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="socialSecurity" class="col-sm-1 control-label">社保缴纳标准</label>
			    <div class="col-sm-5">
			    	<textarea id="socialSecurity" class="form-control" name="formalVO.socialSecurity" required="required"></textarea>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">备注</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" class="form-control" name="comment"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>