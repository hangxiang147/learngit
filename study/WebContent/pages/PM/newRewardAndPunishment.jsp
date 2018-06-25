<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/js/myjs/staffInput.js"></script>
<script src="/assets/js/myjs/textarea.js"></script>
<style type="text/css">
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
	.namecy{position:absolute;top:10px;left:10px;}
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#75b9f3;float:left;margin-right:20px;margin-top:4px;border-radius: 10px}
	.namecy span a{position:absolute;color:#ff0000;font-size:25px;top:-12px;right:0px;cursor:pointer;text-decoration:none}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/PM/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">奖惩申请</h3> 
       	  <form action="/PM/process/save_saveRewardAndPunishment" method="post" enctype="multipart/form-data"
       	      class="form-horizontal" onsubmit="return checkInfo()">
       	  	  <input type="hidden" name="rewardAndPunishmentVo.requestUserIds">
       	  	  <input type="hidden" name="rewardAndPunishmentVo.userId" value="${userId}">
       	  	  <s:token></s:token>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">申请类型<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-2">
       	  	  	<select id="detailType" class="form-control" name="rewardAndPunishmentVo.type" required>
       	  	  		<option value="0">奖励</option>
       	  	  		<option value="1">惩罚</option>
       	  	  	</select>
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	 <div>
       	  	  		<label class="col-sm-1 control-label" style="width:9%">额度(元)<span style="color:red"> *</span></label>
       	  	  		<div class="col-sm-2">
       	  	  			<input class="form-control" autoComplete="off" name="rewardAndPunishmentVo.money" required
       	  	  			oninput="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
       	  	  		</div>
       	  	  	 </div>
       	  	  	 <div>
       	  	  		<label class="col-sm-1 control-label" style="width:9%">生效月份<span style="color:red"> *</span></label>
       	  	  		<div class="col-sm-2" style="width:16%">
       	  	  			<input class="form-control" autoComplete="off" name="rewardAndPunishmentVo.effectiveDate" required
       	  	  			onclick="WdatePicker({dateFmt:'yyyy-MM', minDate:'%y-%M'})">
       	  	  		</div>
       	  	  	 </div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  		<label class="col-sm-1 control-label" style="width:9%">姓名<span style="color:red"> *</span></label>
       	  	  		<div class="col-sm-5">
			    		<div style="position:relative;">
				    	<span class="input_text">
				    	<textarea id="names" class="form-control" rows="3" onblur="$(this).val('')" onfocus="resetMouse(this)"></textarea>
				    	</span>
				    	<div id="namecy" class="namecy" style="width:90%"></div>
				    	</div>
		    		</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">奖惩原因<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-5">
       	  	  		<textarea class="form-control" name="rewardAndPunishmentVo.reason" required rows="4"></textarea>
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group" >
				<label class="col-sm-1 control-label" style="width:9%"><span class="glyphicon glyphicon-paperclip"></span> 附件</label>
				<div class="col-sm-2">
	    			<input multiple name="attachment" id="files" type="file" />
	    		</div>
	    	  	</div>
      	  	  <div class="form-group">
      	  	  	<div class="col-sm-1"></div>
      	  	  	<div class="col-sm-3">
      	  	  	<button type="submit" class="btn btn-primary">提交</button>
      	  	  	<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:5%">返回</button>
      	  	  	</div>
      	  	  </div>
       	  </form>
      	</div>
      </div>
    </div>
    <script type="text/javascript">
	$(function(){
	new staffInputBind().render('#names',textAfterChoose,{textarea:$('#names'),namecy:$('#namecy')});
		$('#namecy').click(function (){
			$('#names').focus();
		});	
	});
	function checkInfo(){
		var data = $('#names').data("resultData");
		if(!data || data.length<1){
			layer.alert("姓名不能为空",{offset:'100px'});
			return false;
		}
		var requestUserIds = [];
		data.forEach(function(value, index){
			requestUserIds.push(value[0]);
		});
		$("input[name='rewardAndPunishmentVo.requestUserIds']").val(requestUserIds.join(","));
		//验证文件大小，不能超过2M(2*1024*1024)
		var maxSize = 2*1024*1024;
	    var files = $("#files")[0].files;
	    for(var i=0; i<files.length; i++){
	 	   var file = files[i];
	 	   if(file.size>maxSize){
	 		   layer.alert("文件"+file.name+"超过2M，限制上传",{offset:'100px'});
	 		   return false;
	 	   }
	    }
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
</body>
</html>