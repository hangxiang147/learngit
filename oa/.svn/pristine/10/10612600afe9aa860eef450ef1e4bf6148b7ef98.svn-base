<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
</head>
  <body>
    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="salaryForm" class="form-horizontal" enctype="multipart/form-data" >
          	<h3 class="sub-header" style="margin-top:0px;">批量导入人员薪资</h3> 
				<div class="form-group">
					<label class="col-sm-1 control-lable" style="width:10%">上传文件<span style="color:red"> *</span></label>
					<div class="col-sm-2"><input name="salaryFile" required type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"></div>
				</div>
				<div class="form-group">
					<div class="col-sm-2">
					<button id="submitBtn" type="button" class="btn btn-primary" onclick="importSalary()">导入</button>
					</div>
				</div>
          </form>
        </div>
      </div>
    </div>
    <script src="/assets/js/layer/layer.js"></script>
    <script type="text/javascript">
    function importSalary(){
		if (!$("input[name='salaryFile']").val().trim()) {
			layer.alert("文件不能为空",{offset:'100px'});
			return;
		}
		//文件大小校验,不超过1M
		var maxSize = 1*1024*1024;
	    var files = $("input[name='salaryFile']")[0].files;
	    for(var i=0; i<files.length; i++){
	 	   var file = files[i];
	 	   if(file.size>maxSize){
	 		   showAlert("文件"+file.name+"超过1M，限制上传！");
	 		   return;
	 	   }
	    }
	    var formData = new FormData($("#salaryForm")[0]);
	    Load.Base.LoadingPic.FullScreenShow(null);
	    $.ajax({
	    	url:'HR/staffSalary/batchImportSalary',
	    	data:formData,
	    	type:'post',
			processData:false,
			contentType:false,
	    	success:function(data){
	    		if(data.success){
	    			layer.alert("导入成功",{offset:'100px'},function(index){
	    				layer.close(index);
	    				location.reload();
	    			});
	    		}else{
	    			layer.alert("导入失败",{offset:'100px'});
	    		}
	    	},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
	    });
    }
    </script>
  </body>
</html>
