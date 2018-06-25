<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">请假审批流程</h3> 
           	<div class="vacation_img" style="margin-bottom:-80px;">
               	<img alt="请假流程图" src="assets/images/vacation.png"></img>  
            </div>
            <form style="margin-top:5%" class="form-horizontal" action="personal/newVacation" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
            	<div class="form-group">
            		<label class="col-sm-1 control-label" style="width:9%">请假对象<span style="color:red"> *</span></label>
            		<div class="col-sm-2">
            			<select class="form-control" name="objectType" required>
            				<option value="">请选择</option>
            				<option value="个人">个人</option>
            				<option value="部门">部门</option>
            			</select>
            		</div>
            	</div>
            	<div class="form-group">
            	<div class="col-sm-1" style="width:9%"></div>
            	<div class="col-sm-2">
            	<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 请假</button>
            	</div>
            	</div>
            </form>
      	</div>
      </div>
    </div>
</body>
</html>