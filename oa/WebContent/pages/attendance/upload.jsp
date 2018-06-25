<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        	<form id="uploadForm" class="form-horizontal" enctype="multipart/form-data" >
              <s:token></s:token>
			  <div class="form-group">
			  	<label for="category" class="col-sm-1 control-label">公司&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="companyforUpload" name="companyID" required="required" onchange="enableSubmitButton();">
				      <option value="">请选择</option>
					  <c:if test="${not empty companyVOs }">
				      	<s:iterator id="cp" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#cp.companyID" />"><s:property value="#cp.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="uploadFile" class="col-sm-1 control-label">选择文件&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="uploadFile" style="padding:6px 0px;" onchange="enableSubmitButton();" >
			    </div>
			  </div>
			  
		   	  <div class="form-group">
		    	<label for="formButton" class="col-sm-1 control-label"></label>
		    	<div class="col-sm-3">
		    		<button type="button" id="formButton" class="btn btn-primary" onclick="upload()">提交</button>
		    	</div>
		      </div>
		      
			</form>
    <script type="text/javascript">
	
	function upload() {
		if ($("#companyforUpload").val().trim()  == '') {
			layer.alert("请选择所在分部！",{offset:'100px'});
			return;
		}
		if ($("#uploadFile").val().trim()  == '') {
			layer.alert("请选择上传文件！",{offset:'100px'});
			return;
		}
		
		$("#formButton").addClass("disabled");
		var formData = new FormData($("#uploadForm")[0]);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'attendance/save_uploadDetail',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				Load.Base.LoadingPic.FullScreenHide();
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					showAlert(data.errorMessage);
					return;
				}
				layer.alert("上传成功！",{offset:'100px'});
			}
		});
	}
	
	function enableSubmitButton() {
		$("#formButton").removeClass("disabled");
	}
	
</script>
