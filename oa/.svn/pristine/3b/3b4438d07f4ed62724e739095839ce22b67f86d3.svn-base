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
<script src="/assets/js/myjs/textareaForProject.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
$(function(){
	if('${success}'=='true'){
		layer.alert("提交成功",{offset:'100px'});
	}
	new staffInputBind().render('#names',textAfterChoose,{textarea:$('#names'),namecy:$('#namecy')});
	$('#namecy').click(function (){
		$('#names').focus();
	});	
});
require(['staffComplete'],function (staffComplete){
	new staffComplete().render($('#staffName'),function ($item){
		$("input[name='partnerDetail.userIds']").val($item.data("userId"));
	});
});
function checkInfo(){
	var object = $("#object option:selected").val();
	if(object=='部分'){
		if(!$("input[name='partnerDetail.userIds']").val()){
			layer.alert("姓名不能为空",{offset:'100px'});
			return false;
		}
	}
	Load.Base.LoadingPic.FullScreenShow(null);
}
function changeDetailType(){
	var detailType = $("#detailType option:selected").val();
	if(detailType == '奖励'){
		$("#rewardType").css("display","block");
		$("select[name='partnerDetail.rewardType']").attr("required","required");
		$("input[name='partnerDetail.money']").attr("required","required");
	}else{
		$("#rewardType").css("display","none");
		$("select[name='partnerDetail.rewardType']").removeAttr("required");
		$("input[name='partnerDetail.money']").removeAttr("required");
	}
}
function checkEmpty(){
	if($("#staffName").val()==''){
		$("input[name='partnerDetail.userIds']").val('');
	}
}
$(document).click(function (event) {
	if ($("input[name='partnerDetail.userIds']").val()=='')
	{
		$("#staffName").val("");
	}
});
function selectObject(){
	$("input[name='partnerDetail.userIds']").val('');
	var object = $("#object option:selected").val();
	if(object=='个人'){
		$("#single").css("display","block");
		$("#part").css("display","none");
		$("#staffName").attr("required","required");
	}else if(object=='部分'){
		$("#part").css("display","block");
		$("#single").css("display","none");
		$("#staffName").removeAttr("required");
	}else{
		$("#part").css("display","none");
		$("#single").css("display","none");
		$("#staffName").removeAttr("required");
	}
}
function checkIsPartner(){
	var userIds = $("input[name='partnerDetail.userIds']").val();
	if(userIds){
		$.ajax({
			url:'/administration/partner/checkIsPartner',
			data:{'userIds':userIds},
			type:'post',
			success:function(data){
				var staffNames = data.staffNames;
				if(staffNames){
					layer.alert(staffNames+"还不是合伙人，请核对", {offset:'100px'});
					$("input[name='partnerDetail.userIds']").val('');
					$("#staffName").val('');
				}
			}
		});
	}
}
function sumitForm(){
	layer.confirm("确认提交？",{offset:'100px'},function(index){
		layer.close(index);
		var userIds = $("input[name='partnerDetail.userIds']").val();
		if(userIds){
			$.ajax({
				url:'/administration/partner/checkIsPartner',
				data:{'userIds':userIds},
				type:'post',
				success:function(data){
					var staffNames = data.staffNames;
					if(staffNames){
						layer.alert(staffNames+"还不是合伙人，请核对", {offset:'100px'});
					}else{
						$("#submitBtn").click();
					}
				}
			});
		}else{
			$("#submitBtn").click();
		}
	});
}
</script>
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
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">新增合伙人内容明细</h3> 
       	  <form action="/administration/partner/save_savePartnerDetail" method="post" class="form-horizontal" onsubmit="return checkInfo()">
       	  	  <input type="hidden" name="partnerDetail.userIds">
       	  	  <s:token></s:token>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">类型<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-2">
       	  	  	<select id="detailType" class="form-control" name="partnerDetail.detailType" required onchange="changeDetailType()">
       	  	  		<option value="">请选择</option>
       	  	  		<option value="已享受福利&待遇">已享受福利&待遇</option>
       	  	  		<option value="已进行的培训和拓展">已进行的培训和拓展</option>
       	  	  		<option value="合伙人章程&规定">合伙人章程&规定</option>
       	  	  		<option value="奖励">奖励</option>
       	  	  	</select>
       	  	  	</div>
       	  	  	<div id="rewardType" style="display:none">
       	  	  		<label class="col-sm-1 control-label" style="width:9%">奖励类型<span style="color:red"> *</span></label>
       	  	  		<div class="col-sm-2" style="width:16%">
       	  	  		<select class="form-control" name="partnerDetail.rewardType">
       	  	  			<option value="">请选择</option>
       	  	  			<option value="现金">现金</option>
       	  	  			<option value="期权">期权</option>
       	  	  		</select>
       	  	  		</div>
       	  	  		<label class="col-sm-1 control-label" style="width:9%">额度(元)<span style="color:red"> *</span></label>
       	  	  		<div class="col-sm-2">
       	  	  			<input class="form-control" autoComplete="off" name="partnerDetail.money"
       	  	  			oninput="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
       	  	  		</div>
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">应用对象</label>
       	  	  	<div class="col-sm-2">
       	  	  		<select id="object" class="form-control" name="partnerDetail.object" onchange="selectObject()">
       	  	  			<option value="个人">个人</option>
       	  	  			<option value="全体">全体</option>
       	  	  			<option value="部分">部分</option>
       	  	  		</select>
       	  	  	</div>
       	  	  	<div id="single">
	       	  	  	<label class="col-sm-1 control-label" style="width:9%">姓名<span style="color:red"> *</span></label>
	       	  	  	<div class="col-sm-2" style="width:16%">
	       	  	  	<input type="text" id="staffName" class="form-control" autoComplete="off" required onblur="checkEmpty(),checkIsPartner()">
	       	  	  	</div>
       	  	  	</div>
       	  	  </div>
       	  	  <div id="part" style="display:none" class="form-group">
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
       	  	  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-0.5%">主题<span style="color:red"> *</span><span style="font-size:12px;font-weight:bold">（不超过20字）</span></label>
       	  	  	<div class="col-sm-5">
       	  	  		<input class="form-control" autoComplete="off" name="partnerDetail.theme" required maxLength="20">
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:9%">内容明细<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-5">
       	  	  		<textarea class="form-control" name="partnerDetail.content" required rows="4"></textarea>
       	  	  	</div>
       	  	  </div>
      	  	  <div class="form-group">
      	  	  	<div class="col-sm-1"></div>
      	  	  	<div class="col-sm-3">
      	  	  	<button id="submitBtn" type="submit" style="display:none"></button>
      	  	  	<button type="button" class="btn btn-primary" onclick="sumitForm()">提交</button>
      	  	  	<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:5%">返回</button>
      	  	  	</div>
      	  	  </div>
       	  </form>
      	</div>
      </div>
    </div>
</body>
</html>