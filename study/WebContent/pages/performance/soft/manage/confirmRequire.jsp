<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>

<div id="require" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">确认版本已完成</h4>
			</div>
			<div id="requireContent" class="modal-body" style="max-height:400px;overflow:auto;">

			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="confirmCompleteVersion()" class="btn btn-primary">完成</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
	function confirmCompleteVersion(){ 
		layer.open({  
			  offset:'100px',
	          content: '确定版本已开发完成？',  
	          btn: ['确认', '取消'],  
	          yes: function() { 
	        	if(!checkComplete()){
	        		layer.alert("有需求未开发或任务未完成，无法确认完成版本",{offset:'100px'});
	        		return;
	        	}
	          	window.location.href='/performance/soft/confirmCompleteVersion?versionId='+versionId; 
	          	$("#task").modal('hide');
	          }
	    }); 
	}
	//检查版本下面的需求是否已开发/任务是否已完成
	function checkComplete(){
		var flag = true;
		$(".requireStatus").each(function(){
			if($(this).text()=='待开发'){
				flag = false;
			}
		});
		$(".taskStatus").each(function(){
			if($(this).text()!='任务结束' && $(this).text()!='分值无效' &&
					$(this).text()!='分值有效' && $(this).text()!='任务终止'){
				flag = false;
			}
		});
		return flag;
	}
	</script>
</div>

