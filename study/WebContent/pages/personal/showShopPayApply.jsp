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
       	  <h3 class="sub-header" style="margin-top:0px;">店铺付费申请流程</h3> 
           	<div class="formal_img" style="margin-bottom:-20px;">
               	<img alt="店铺付费申请" src="assets/images/ShopPayApply.png"></img>  
            </div>
            <br>
            <form action="personal/toBeginShopPayApplyPage" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
            	<div class="form-group">
				  	<label class="col-sm-1 control-label" style="width:10%">申请类型<span style="color:red"> *</span></label>
					<div class="col-sm-2">
			  		<select class="form-control" required name="applyType">
			  			<option value="">请选择</option>
			  			<option value="付费推广充值">付费推广充值</option>
			  			<option value="付费服务/插件开通">付费服务/插件开通</option>
			  			<option value="其他服务费">其他服务费</option>
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