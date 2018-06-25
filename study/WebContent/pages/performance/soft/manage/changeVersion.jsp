<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="changeVersion" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">版本转换</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">版本</label>
					<div id="name" class="col-sm-6">
						<select id="selectVersion" class="form-control" name="versionId">
						</select>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="saveVersion()" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			<input type="hidden" id="changeVersionRequireId" name="requireId"> 
		</div>
		</form>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
		function saveVersion(){
			var $form = $("#changeVersion form");
	    		  $.ajax({  
	                  type: "post",  
	                  data: $form.serialize(),
	                  async: false,  
	                  url: "/performance/soft/saveVersion", 
	                  success:function (data){
	                	  if(data.success="ok"){
	                		  $("#changeVersion").modal("hide");
	                		  window.location.href='/performance/soft/divideRequireManage'; 
	                	  }
	                  }
	              }); 
		}
	</script>
</div>

