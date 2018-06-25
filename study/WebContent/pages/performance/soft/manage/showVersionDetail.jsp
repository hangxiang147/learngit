<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>

<div id="showVersion" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加版本</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">版本名称：</label>
					<div class="col-sm-6 control-label" id="versionName" style="text-align:center">
					</div>
				</div>
				<div class="form-group">
	    		<label for="beginDate" class="col-sm-3 control-label">开始时间：</label>
	    		<div class="col-sm-6 control-label" id="beginDate_" style="text-align:center">
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
	    		<label for="endDate" class="col-sm-3 control-label">结束时间：</label>
	    		<div class="col-sm-6 control-label" id="endDate_" style="text-align:center">
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">项目经理：</label>
	    		<div class="col-sm-6 control-label" id="pm1" style="text-align:center">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">需求分析：</label>
	    		<div class="col-sm-6 control-label" id="fenXi1" style="text-align:center">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">编码人员：</label>
	    		<div class="col-sm-6 control-label" id="developer1" style="text-align:center">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">测试人员：</label>
	    		<div class="col-sm-6 control-label" id="tester1" style="text-align:center">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">实施人员：</label>
	    		<div class="col-sm-6 control-label" id="shiShi1" style="text-align:center">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label for="workHour" class="col-sm-3 control-label">每日工时：</label>
	    		<div class="col-sm-6 control-label" id="workHour_" style="text-align:center">
	    		</div>
	    	  </div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
</div>

