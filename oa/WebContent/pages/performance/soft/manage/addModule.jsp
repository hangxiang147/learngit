<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>

<div id="module" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加模块</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">模块名称</label>
					<div id="name" class="col-sm-6">
						<input class="form-control" autocomplete="off" required="required" name="projectModule.module" value="${projectModule.module}"/>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="saveModule(this)" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		<input type="hidden" name="projectModule.id" value="${projectModule.id}"/>
		<input type="hidden" name="projectModule.projectId" value="${projectModule.projectId}"/>
		<input type="hidden" name="projectId" value="${project.id}">
		</form>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
		function saveModule(){
			var $form = $("#module form");
			var module = $("input[name='projectModule.module']").val();
			if(module==""){
				layer.alert("模块名称不能为空",{offset:'100px'});
				return;
			}
	    		  $.ajax({  
	                  type: "post",  
	                  data: $form.serialize(),
	                  async: false,  
	                  url: "/performance/soft/saveProjectModule",  
	                  success: function (data) {  
	                	var error = data.error;
	                	if(error!=null && error.length>0){
	                		layer.alert("保存失败："+error,{offset:'100px'});
	                		return;
		               	}
	                	if(data.exist == "false"){
	                		$form.parent().parent().modal('hide');
	                		if(data.edit == "true"){
	                			window.location.href="/performance/soft/showModule?projectId="+$("input[name='projectModule.projectId']").val();
	                			Load.Base.LoadingPic.FullScreenShow(null);
	                		}else{
	                			window.location.href="/performance/soft/showProject";
	                			Load.Base.LoadingPic.FullScreenShow(null);
	                		}
	                	}else{
	                		layer.alert("模块已存在",{offset:'100px'});
	                	}
	                  }  
	              }); 
		}
		
	    $("input[name='projectModule.module']").bind('keypress', function(event) {  
	        if (event.keyCode == "13") {              
	            event.preventDefault();   
	            saveModule();
	        }  
	    }); 
	</script>
</div>

