<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	function checkInfo(){
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		beginDate = new Date(beginDate.replace(/-/g, "/"));
		endDate = new Date(endDate.replace(/-/g, "/"));
		var diff = endDate.getTime() - beginDate.getTime();
		if(diff<0){
			layer.alert("结束时间不能小于开始时间",{offset:'100px'});
			return false;
		}
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'brandAuthManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">品牌授权申请</h3> 
       	  <form action="/administration/process/save_startBrandAuth" method="post" class="form-horizontal" onsubmit="return checkInfo()">
       	  	  <input type="hidden" name="brandAuthVo.userName"  value="${staff.lastName}"/>
  			  <input type="hidden" name="brandAuthVo.userId"  value="${staff.userID}"/>
  			  <input type="hidden" name="brandAuthVo.userID"  value="${staff.userID}"/>  
       	  	  <s:token></s:token>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">公司名称<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-3">
       	  	  		<input autoComplete="off" name="brandAuthVo.companyName" class="form-control" required>
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">平台<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-3">
       	  	  		<input autoComplete="off" name="brandAuthVo.platform" class="form-control" required>
       	  	  	</div>
       	  	  	<div class="col-sm-3 control-label" style="text-align:left">比如：淘宝、亚马孙</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">店铺名称<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-3">
       	  	  		<input autoComplete="off" name="brandAuthVo.shopName" class="form-control" required>
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">店铺网址<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-3">
       	  	  		<input autoComplete="off" name="brandAuthVo.shopAddress" class="form-control" required>
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">授权品牌<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-3">
       	  	  		<input autoComplete="off" name="brandAuthVo.brand" class="form-control" required>
       	  	  	</div>
       	  	  	<div class="col-sm-3 control-label" style="text-align:left">比如：haoduoyi、haoyihui</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">授权时间<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-2" style="width:13%">
       	  	  		<input id="beginDate" autoComplete="off" onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}', minDate:'%y-%M-%d' })" name="brandAuthVo.authBeginDate" class="form-control" required placeholder="开始时间">
       	  	  	</div>
       	  	  	<div class="col-sm-2" style="width:13%;margin-left:-1%">
       	  	  		<input id="endDate" autoComplete="off" onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}' })" name="brandAuthVo.authEndDate" class="form-control" required placeholder="结束时间">
       	  	  	</div>
       	  	  </div>
      	  	 <div class="form-group">
      	  	  	<label class="col-sm-1 control-label" style="width:9%">联系人<span style="color:red"> *</span></label>
      	  	  	<div class="col-sm-3">
      	  	  		<input autoComplete="off" name="brandAuthVo.contact" class="form-control" required>
      	  	  	</div>
      	  	  </div>
      	  	  <div class="form-group">
      	  	  	<label class="col-sm-1 control-label" style="width:9%">电话<span style="color:red"> *</span></label>
      	  	  	<div class="col-sm-3">
      	  	  		<input autoComplete="off" name="brandAuthVo.telephone" class="form-control" required
      	  	  		oninput="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="this.v();">
      	  	  	</div>
      	  	  </div>
      	  	  <div class="form-group">
      	  	  	<div class="col-sm-1" style="width:9%"></div>
      	  	  	<div class="col-sm-3">
      	  	  	<button type="submit" class="btn btn-primary">提交</button>
      	  	  	<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:5%">返回</button>
      	  	  	</div>
      	  	  </div>
       	  </form>
      	</div>
      </div>
    </div>
</body>
</html>