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
              <s:set name="panel" value="'application'"></s:set>
      
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">店铺申请流程</h3> 
           	<div class="formal_img" style="margin-bottom:-20px;">
               	<img alt="店铺申请" src="assets/images/shopApply.png"></img>  
            </div>
            <br>
            <form action="personal/toBeginShopApplyPage" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
            	<div class="form-group">
				  	<label class="col-sm-1 control-label" style="width:10%">申请类型<span style="color:red"> *</span></label>
					<div class="col-sm-2">
			  		<select class="form-control" required name="applyType">
			  			<option value="">请选择</option>
			  			<option value="开店">开店</option>
			  			<option value="店铺信息维护">店铺信息维护</option>
			  			<option value="关店">关店</option>
			  		</select>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  		<div class="col-sm-1 control-label">
			  			<button type="submit"class="btn btn-primary">我要申请</button>
			  		</div>
			  	</div>
            </form>
      	</div>
      </div>
    </div>
</body>
</html>