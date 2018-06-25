<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>

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
// 		if ($("#company").val().trim()  == '') {
// 			showAlert("请选择所在分部！");
// 			return;
// 		}
		if ($("#uploadFile").val().trim()  == '') {
			showAlert("请选择上传文件！");
			return;
		}
		if(!$('input[name="year"]').val()){
			showAlert("请选择对应年份！");
			return;
		}
		if(!$('input[name="month"]').val()){
			showAlert("请选择对应年份！");
			return;
		}
		//文件大小校验,不超过1M
		var maxSize = 1*1024*1024;
	    var files = $("#uploadFile")[0].files;
	    for(var i=0; i<files.length; i++){
	 	   var file = files[i];
	 	   if(file.size>maxSize){
	 		   showAlert("文件"+file.name+"超过1M，限制上传！");
	 		   return;
	 	   }
	    }
		$("#formButton").addClass("disabled");
		var formData = new FormData($("#uploadForm")[0]);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'attendance/save_salaryContentUpload',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					showAlert(data.errorMessage);
					return;
				}
				layer.alert("上传成功！",{offset:'100px'},function (){
					location.reload();
				});
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
		});
	}
	
	function enableSubmitButton() {
		$("#formButton").removeClass("disabled");
	}
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
        <%@include file="/pages/attendance/salayPanel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="uploadForm" class="form-horizontal" enctype="multipart/form-data" >
              <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">工资单数据上传</h3>
			  
			  <div class="form-group">
			<label for="category" class="col-sm-1 control-label">年份&nbsp;</label>
			    
			     <div class="col-sm-2">
			    	<input name="year"  class="form-control"  onclick="WdatePicker({dateFmt:'yyyy'})" value="${year}"/>
			    </div>
			    			<label for="category" class="col-sm-1 control-label">月份&nbsp;</label>
			    
			     <div class="col-sm-2">
			    	<input name="month"  class="form-control"  onclick="WdatePicker({dateFmt:'MM'})" value="${month}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="uploadFile" class="col-sm-1 control-label">选择文件&nbsp;</label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="uploadFile" style="padding:6px 0px;" onchange="enableSubmitButton();" >
			    </div>
			  </div>
			  
		   	  <div class="form-group">
		    	<label for="formButton" class="col-sm-1 control-label"></label>
		    	<div class="col-sm-3 col-md-2 col-sm-offset-1  col-md-offset-1">
		    		<input type="button" id="formButton" value="提交" class="btn btn-primary" onclick="upload();"/>
		    	</div>
		      </div>
		      
			</form>
        </div>
      </div>
    </div>
  </body>
</html>
