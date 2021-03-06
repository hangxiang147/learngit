<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
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
        <s:set name="panel" value="'projectManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">发起项目</h3>
          <form class="form-horizontal" action="administration/project/save_startProject" enctype="multipart/form-data"
          		method="post" onsubmit="return checkInfo()" >
          		<s:token></s:token>
          		<input style="display:none" name="projectInfoVo.userName"  value="${staff.lastName}"/>
  			  	<input style="display:none" name="projectInfoVo.userID"  value="${staff.userID}"/>  
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">项目名称<span style="color:red"> *</span></label>
  			  		<div class="col-sm-3">
  			  			<input required name="projectInfoVo.projectName" class="form-control" autoComplete="off">
  			  		</div> 
  			  	</div>
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">负责人<span style="color:red"> *</span></label>
  			  		<div class="col-sm-3">
  			  			<input id="projectLeader" required class="form-control" type="text" autocomplete="off" onkeyup="checkEmpty()">
  			  			<input type="hidden" name="projectInfoVo.projectLeaderId">
  			  		</div> 
  			  		<label class="col-sm-1 control-label" style="width:12%">最终审批人</label>
  			  		<div class="col-sm-3" style="width:21.35%">
  			  			<input id="finalAuditor" class="form-control" type="text" autocomplete="off" onkeyup="checkEmptyForAuditor()">
  			  			<input type="hidden" name="projectInfoVo.finalAuditor">
  			  		</div> 
  			  	</div>
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">项目描述<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
		    			<textarea id="description" class="form-control" style="width:100%" name="projectInfoVo.projectDescription"></textarea>
		    		</div>
  			  	</div>
  			  	<div class="form-group">
		    		<label class="col-sm-1 control-label" style="width:10%">参与人员<span style="font-size:10px;font-weight:bold">（不包括负责人）</span></label>
		    		<div class="col-sm-7">
			    		<div style="position:relative;">
				    	<span class="input_text">
				    	<textarea id="joinUser" class="form-control" rows="3" onblur="$(this).val('')" onfocus="resetMouse(this)"></textarea>
				    	</span>
				    	<div id="namecy" class="namecy" style="width:590px"></div>
				    	</div>
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-1 control-label" style="width:10%">附件</label>
	    			<div class="col-sm-8">
	    			<input id="files" type="file" name="attachment" multiple>
	    			</div>
	    	  	</div> 
	    	  	<div class="form-group">
	    	  	 	<label class="col-sm-1" style="width:10%"></label>
	    	  	 	<div class="col-sm-2">
				    <button id="submitBtn" type="submit" class="btn btn-primary" >提交</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
			  		</div>
			  	</div>
			  	<!-- 参会人员 -->
			  	<input  type="hidden" name="projectInfoVo.projectParticipants">
          </form>
        </div>
      </div>
    </div>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/js/myjs/staffInput.js"></script>
<script src="/assets/js/myjs/textareaForProject.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script src="/assets/editor/kindeditor.js"></script>
<script src="/assets/editor/lang/zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		kedit('textarea[name="projectInfoVo.projectDescription"]');
		new staffInputBind().render('#joinUser',textAfterChoose,{textarea:$('#joinUser'),namecy:$('#namecy')});
		$('#namecy').click(function (){
			$('#joinUser').focus();
		});	
	});
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#projectLeader'),function ($item){
			$("input[name='projectInfoVo.projectLeaderId']").val($item.data("userId"));
		});
		new staffComplete().render($('#finalAuditor'),function ($item){
			$("input[name='projectInfoVo.finalAuditor']").val($item.data("userId"));
		});
	});
	function checkEmpty(){
		if($("#projectLeader").val()==''){
			$("input[name='projectInfoVo.projectLeaderId']").val('');
		}
	}
	function checkEmptyForAuditor(){
		if($("#finalAuditor").val()==''){
			$("input[name='projectInfoVo.finalAuditor']").val('');
		}
	}
	$(document).click(function (event) {
		if ($("input[name='projectInfoVo.projectLeaderId']").val()=='')
		{
			$("#projectLeader").val("");
		}
		if ($("input[name='projectInfoVo.finalAuditor']").val()=='')
		{
			$("#finalAuditor").val("");
		}
	});
	var	editor;
	function kedit(kedit){
		editor = KindEditor.create(kedit, {
				resizeType : 1,
				allowPreviewEmoticons : false,
				height:250,
				items : ['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist', '|', 'emoticons', 'image', 'link', '|','preview']
			});
	}
	function checkInfo(){
		var projectLeaderId = $("input[name='projectInfoVo.projectLeaderId']").val();
		var projectParticipants = $("input[name='projectInfoVo.projectParticipants']").val();
		//检查参与人员是否和责任人重复
		if(projectParticipants.indexOf(projectLeaderId)!=-1){
			layer.alert("参与人员不包含负责人", {offset:'100px'});
			return false;
		}
		editor.sync(); 
		//检查会议内容
		var projectDescription =  $("#description").val();
		if($.trim(projectDescription)==''){
			layer.alert("项目描述不能为空", {offset:'100px'});
			return false;
		}
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
	function showPic(name, path) {
		var picData = {
			start : 0,
			data : []
		}
		picData.data.push({
			alt : name,
			src : "performance/soft/showImage?attachmentPath=" + path
		})
		layer.photos({
			offset : '50px',
			photos : picData,
			anim : 5
		});
	}
</script>
  </body>
</html>