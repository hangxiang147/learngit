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
<script type="text/javascript">
$(function(){
	new staffInputBind().render('#names',textAfterChoose,{textarea:$('#names'),namecy:$('#namecy')});
	$('#namecy').click(function (){
		$('#names').focus();
	});	
	var ourPersonNums = parseInt('${fn:length(publicRelation.ourPersonNames)}');
	if(ourPersonNums>0){
		var text="";
		for( var i=0;i<ourPersonNums;i++)
		{
			text+="                       ";
			if((i+1)%5==0){
				text+="\n"+"                       ".repeat(5)+"\n";
			}
		}	
		$("#names").text(text);
		var resultData = [];
		var ourPersonIds = '${publicRelation.ourPersonIds}'.split(',');
		var ourPersonNames = '${ourPersonNames}'.split(',');
		for(var i=0; i<ourPersonIds.length; i++){
			var personDetail = [];
			personDetail[0] = ourPersonIds[i];
			personDetail[1] = ourPersonNames[i];
			resultData.push(personDetail);
		}
		$("#names").data('resultData',resultData);
		$(".delete a").each(function(){
			$(this).click(function(){
				var personId = $(this).parent().attr("data-id");
				var resultData=$("#names").data('resultData');
				var index_ = 0;
				$.each(resultData, function(index, value){
					if(value[0] == personId){
						index_ = index;
						return;
					}
				});
				var deletedData = resultData.slice(0, index_).concat(resultData.slice(index_+1));
				$("#names").data('resultData',deletedData);
				$("#names").attr("rows",3*((Math.floor((deletedData.length)/5)+1)));
				var text="";
				for( var i=0;i<deletedData.length;i++)
				{
					text+="                       ";
					if((i+1)%5==0){
						text+="\n"+"                       ".repeat(5)+"\n";
					}
				}			
				$("#names").val(text);
				$("#names").focus();
				$(this).parent().remove();
			});
		});
	}
});
function checkInfo(){
	//检查讲师是否填写
	var data = $('#names').data("resultData");
	if(data && data.length>0){
		var result=data.reduce(function (result,value){
			result.push(value[0]);
			return result;
		},[]);
		$('input[name="publicRelation.ourPersonIds"]').val(result.join(","));
	}else{
		layer.alert("我方联系人 不能为空",{offset:'100px'});
		return false;
	}
	Load.Base.LoadingPic.FullScreenShow(null);
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
        <s:set name="panel" value="'public'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">新增社会公共关系</h3> 
       	  <form action="/administration/public/save_savePublicRelation" method="post"
       	   	    class="form-horizontal" onsubmit="return checkInfo()">
       	  	  <input type="hidden" name="publicRelation.ourPersonIds">
       	  	  <input type="hidden" name="publicRelation.id" value="${publicRelation.id}">
       	  	  <input type="hidden" name="publicRelation.addTime" value="${publicRelation.addTime}">
       	  	  <input type="hidden" name="publicRelation.status" value="${publicRelation.status}">
       	  	  <s:token></s:token>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:15%">对方姓名<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-2">
       	  	  		<input autoComplete="off" name="publicRelation.otherName" required class="form-control" value="${publicRelation.otherName}">
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:15%">电话<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-2">
       	  	  		<input autoComplete="off" name="publicRelation.otherPhone" required class="form-control" value="${publicRelation.otherPhone}"
       	  	  		oninput="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" maxLength="11">
       	  	  	</div>
       	  	  	<label class="col-sm-1 control-label">职位<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-2">
       	  	  		<input autoComplete="off" name="publicRelation.otherJob" required class="form-control" value="${publicRelation.otherJob}">
       	  	  	</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  		<label class="col-sm-1 control-label" style="width:15%">我方联系人<span style="color:red"> *</span></label>
       	  	  		<div class="col-sm-5">
			    		<div style="position:relative;">
				    	<span class="input_text">
				    	<textarea id="names" class="form-control" rows="3" onblur="$(this).val('')" onfocus="resetMouse(this)"></textarea>
				    	</span>
				    	<div id="namecy" class="namecy" style="width:90%">
				    		<c:forEach items="${publicRelation.ourPersonNames}" var="ourPersonName" varStatus="status">
				    			<span data-id="${ourPersonIds[status.index]}" class="delete" style="width:73px;padding:6px 9px">${ourPersonName}<a>×</a></span>
				    		</c:forEach>
				    	</div>
				    	</div>
		    		</div>
       	  	  </div>
       	  	  <div class="form-group">
       	  	  	<label class="col-sm-1 control-label" style="width:15%">社会公共关系范畴<span style="color:red"> *</span></label>
       	  	  	<div class="col-sm-5">
       	  	  		<textarea class="form-control" name="publicRelation.category" required rows="4">${publicRelation.category}</textarea>
       	  	  	</div>
       	  	  </div>
      	  	  <div class="form-group">
      	  	  	<div class="col-sm-1" style="width:15%"></div>
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