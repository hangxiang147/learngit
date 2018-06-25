<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>

<div class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">子需求信息</h4>
			</div>
			<div class="modal-body" style="max-height:400px;overflow:auto;">
				<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-2">需求名称</th>
	                  <th class="col-sm-4">需求描述</th>
	                  <th class="col-sm-2">附件</th>
	                </tr>
	              </thead>
	              <tbody id="content">
	              </tbody>
	            </table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
</div>

