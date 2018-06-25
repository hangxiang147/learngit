<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style>
.float_div{
	position:fixed;
	width:380px;
	right:0px;
	top:70px;
	z-index:100; 
}
.form-group {
    margin-bottom: 30px;
    margin-top: 30px;
}
.col-sm-1 {
	padding-right: 0px;
	padding-left: 0px;
}

.detail-control {
	display: block;
    width: 100%;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555; 
}
.inputout1{position:relative;}
.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
.text_down1 ul{padding:2px 10px;}
.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
.text_down1 ul li span{color:#cc0000;}
</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startContractSign" method="post"  id="form_"class="form-horizontal"
        	 			enctype="multipart/form-data" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起合同签署</h3> 			 
  			  	  <input style="display:none" name="contractSignVo.userID"  value="${staff.userID}"/>
  			  	  <input style="display:none" name="contractSignVo.userName"  value="${staff.lastName}"/>  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label" style="width:9%">合同编号<span style="color:red"> *</span></label>
			  	<div class="col-sm-2" id="find_div"><input required="required" autoComplete="off" type="text" class="form-control" name="contractSignVo.contractId"  ></div>
			  	<label for="reason" class="col-sm-1 control-label">合同名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-2" id="find_div"><input required="required" autoComplete="off" type="text" class="form-control" name="contractSignVo.contractName"  ></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label" style="width:9%">对方公司名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-2" id="find_div"><input required="required" autoComplete="off" type="text" class="form-control" name="contractSignVo.otherCompanyName"  ></div>
			  	<label for="reason" class="col-sm-1 control-label">合同金额</label>
			  	<div class="col-sm-2" id="find_div"><input autoComplete="off" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)"
			  	onblur="this.v();"  type="text" class="form-control" name="contractSignVo.money"  ></div>
			  </div>
			   <div class="form-group">
			   	<label for="reason" class="col-sm-1 control-label" style="width:9%">合同详情</label>
			   	<div class="col-sm-5">
			  	<textarea rows="4" class="form-control" name="contractSignVo.description"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label" style="width:9%">支出类合同<span style="color:red"> *</span></label>
 			  	<div class="col-sm-2">
 			  		<select class="form-control"  name="contractSignVo.isPay" required="required">
 			  				<option value="">请选择</option>
				      		<option value="0">否</option>
				      		<option value="1">是</option>
					</select>
 			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%"><span class="glyphicon glyphicon-paperclip"></span> 法务的审批或签名</label>
			    <div class="col-sm-5">
			    	<input type="file" accept="image/gif,image/jpeg,image/jpg,image/png" name="attachment2" style="padding:6px 0px;">
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="attachment" class="col-sm-1 control-label" style="width:9%"><span class="glyphicon glyphicon-paperclip"></span> 添加合同封面</label>
			    <div class="col-sm-5">
			    	<input type="file" id="attachment" accept="image/gif,image/jpeg,image/jpg,image/png" name="attachment" style="padding:6px 0px;">
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/underscore-min.js"></script>
      </body>
</html>