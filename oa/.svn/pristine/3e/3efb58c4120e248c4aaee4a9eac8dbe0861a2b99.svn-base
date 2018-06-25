<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	
	 function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function upload() {
			
		if ($("#category").val().trim()  == '') {
			showAlert("请选择分类！");
			return;
		}
		if ($("#softName").val().trim()  == '') {
			showAlert("请输入软件名称！");
			return;
		}
		if ($("#uploadFile").val().trim()  == '') {
			showAlert("请选择问上传文件！");
			return;
		}
		$.ajax({
			url:'attendance/uploadDetail',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					if (data.errorMessage == "文件已存在！") {
						
					}
					else {
						showAlert(data.errorMessage);
					}
					return;
				}
				layer.alert("上传成功！", {offset:'100px'});
			}
		});
		$("#uploadForm").attr("method","post").attr("action","<c:url value='/' />downloadcenter/upload").get(0).submit();
		Load.Base.LoadingPic.FullScreenShow(null);
	};
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'systemManagement'"></s:set>
      	<s:set name="selectedPanel" value="'softUpload'"></s:set>
        <%@include file="/pages/systemmanagement/softupload/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="uploadForm" class="form-horizontal" enctype="multipart/form-data" >
        	  <h3 class="sub-header" style="margin-top:0px;">软件上传<span style="font-size:20px;color:#ccc;float:right;"></span></h3>
			  
			  <div class="form-group">
			  	<label for="category" class="col-sm-1 control-label">分类&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="category" name="softUpAndDownloadVO.category" required="required">
				      <option value="">请选择</option>
					  <option value="1">办公软件</option>
					  <option value="2">开发软件</option>
					  <option value="3">驱动</option>
					  <option value="4">其它</option>
					</select>
			    </div>
			  </div>
			  
			  <div class="form-group">
			    <label for="softName" class="col-sm-1 control-label">软件名称&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="softName" name="softUpAndDownloadVO.softName" required="required"  maxlength="60">
			    </div>
			  </div>
			    
			  <div class="form-group">
			    <label for="softDetail" class="col-sm-1 control-label">软件说明</label>
			    <div class="col-sm-4">
			    	<textarea  class="form-control span5" rows="3" id="softDetail" name="softUpAndDownloadVO.softDetail" required="required"  maxlength="100" placeholder="请您输入对软件的描述，比如功能、版本，让下载者更好的了解和使用软件。"></textarea>
			    </div>
			  </div>
			  
			  <div class="form-group">
			    <label for="uploadFile" class="col-sm-1 control-label">选择文件&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="uploadFile"  style="padding:6px 0px;" >
			    </div>
			  </div>
			  
			   <div class="form-group">
			    <label for="formButton" class="col-sm-1 control-label"></label>
			    <div class="col-sm-3 col-md-2 col-sm-offset-1  col-md-offset-1">
			    	<input type="button" id="formButton" name="uploadFile" value="提交" class="btn btn-primary" onclick="upload()"/>
			    </div>
			   </div>
			</form>
        </div>
      </div>
    </div>
  </body>
</html>
