<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>

<div id="task" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">任务列表</h4>
			</div>
			<div class="modal-body" style="max-height:400px;overflow:auto;">
				<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-3">任务名称</th>
	                  <th class="col-sm-2">责任人</th>
	                  <th class="col-sm-2">当前状态</th>
	                </tr>
	              </thead>
	              <tbody id="content">
	              </tbody>
	            </table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<!-- <button type="button" onclick="confirmCompleteRequire()" class="btn btn-primary">完成</button> -->
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
	function confirmCompleteRequire(){ 
		layer.open({  
	          content: '确定需求已开发完成？',  
	          btn: ['确认', '取消'],  
	          yes: function() {  
	          	window.location.href='/performance/soft/confirmCompleteRequire?requireId='+requireId; 
	          	$("#task").modal('hide');
	          }
	    }); 
	}
	</script>
</div>

