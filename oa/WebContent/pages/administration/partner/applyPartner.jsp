 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/editor/kindeditor-min.js"></script>
<script type="text/javascript">
$(function(){
	//富文本
	kedit('#applyContent');
});
var	editor;
function kedit(kedit){
	editor =KindEditor.create(kedit, {
			resizeType : 1,
			allowPreviewEmoticons : false,
			height:'300px',
			items : [
				'fontname', 'fontsize', '|', 'bold', 'italic', 'underline',
				'|', 'justifyleft', 'justifycenter', 'justifyright', '|', 'preview']
		});
}
function checkInfo(){
	editor.sync(); 
	if(!$("#applyContent").val()){
		layer.alert("申请书不能为空",{offset:'100px'});
		return false;
	}
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">合伙人申请</h3> 
<%--        	  <form action="/administration/partner/save_startPartner" method="post" class="form-horizontal" onsubmit="return checkInfo()">
  			  <input type="hidden" name="partner.userId"  value="${staff.userID}"/>
  			  <input type="hidden" name="partner.applyDate" value="<fmt:formatDate value='${applyerDate}'   pattern='yyyy-MM-dd'/>">
       	  	  <s:token></s:token>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-2 control-label" style="text-align:left">申请人：${staff.staffName}</label>
       	  	  	<label class="col-sm-3 control-label" style="text-align:left">申请时间：<fmt:formatDate value="${applyerDate}"   pattern="yyyy年MM月dd日" /></label>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="text-align:left">申请书<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-5" style="margin-left:-3%">
       	  	  	<textarea id="applyContent" class="form-control" rows="3" name="partner.applyContent"></textarea>
       	  	  	</div>
       	  	  </div>
      	  	  <div class="form-group">
      	  	  	<div class="col-sm-1"></div>
      	  	  	<div class="col-sm-3" style="margin-left:-3%">
      	  	  	<button type="submit" class="btn btn-primary">提交</button>
      	  	  	<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:5%">返回</button>
      	  	  	</div>
      	  	  </div>
       	  </form> --%>
       	  <h2 style="color:red">暂不开放</h2>
      	</div>
      </div>
    </div>
</body>
</html>