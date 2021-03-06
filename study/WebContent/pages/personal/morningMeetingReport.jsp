<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/editor/kindeditor.js"></script>
<script src="/assets/editor/lang/zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		kedit('textarea[name="morningMeetingVo.description"]');
	});
	var	editor;
	function kedit(kedit){
		editor = KindEditor.create(kedit, {
				resizeType : 1,
				allowPreviewEmoticons : false,
				height:400,
				items : ['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist', '|', 'emoticons', 'image', 'link', '|','preview']
			});
	}
	function checkInfo(){
		if($("select[name='morningMeetingVo.hasMeeting'] option:selected").val()=="是"){
			editor.sync(); 
			//检查会议内容
			var meetingContent =  $("#description").val();
			if($.trim(meetingContent)==''){
				layer.alert("会议内容不能为空", {offset:'100px'});
				return false;
			}
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
	function selectHas(){
		var hasMeeting = $("select[name='morningMeetingVo.hasMeeting'] option:selected").val();
		if(hasMeeting == '是'){
			$("#meeting").css("display","block");
			$("#noMeeting").css("display","none");
			$("textarea[name='morningMeetingVo.description']").attr("required","required");
			$("select[name='morningMeetingVo.noMeetingReason']").removeAttr("required");
			$("textarea[name='morningMeetingVo.remark']").removeAttr("required");
		}else{
			$("#meeting").css("display","none");
			$("#noMeeting").css("display","block");
			$("textarea[name='morningMeetingVo.description']").removeAttr("required");
			$("select[name='morningMeetingVo.noMeetingReason']").attr("required","required");
			$("textarea[name='morningMeetingVo.remark']").attr("required","required");
		}
	}
</script>
<style type="text/css">
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">周早会汇报</h3>
          <form class="form-horizontal" action="personal/save_startWeekMorningMeetingReport" enctype="multipart/form-data"
          		method="post" onsubmit="return checkInfo()" >
          		<s:token></s:token>
          		<input style="display:none" name="morningMeetingVo.userName"  value="${staff.lastName}"/>
  			  	<input style="display:none" name="morningMeetingVo.userID"  value="${staff.userID}"/>  
  			  	<input style="display:none" name="morningMeetingVo.weekday"  value="星期${weekDay}"/>
  			  	<input style="display:none" name="morningMeetingVo.taskId" value="${taskId}"> 
  			  	<input style="display:none" name="morningMeetingVo.processInstanceID" value="${processInstanceId}">
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">汇报人</label>
  			  		<div class="col-sm-2 control-label" style="text-align:left">${staff.lastName}</div>
  			  	</div>
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">汇报时间</label>
  			  		<div class="col-sm-4 control-label" style="text-align:left">${currentDate}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;星期${weekDay}</div>
  			  	</div>
  			  	<c:if test="${taskId==null}">
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">开早会<span style="color:red"> *</span></label>
  			  		<div class="col-sm-2">
  			  			<select class="form-control" required name="morningMeetingVo.hasMeeting"  onchange="selectHas()">
  			  				<option value="是">是</option>
  			  				<option value="否">否</option>
  			  			</select>
  			  		</div>
  			  	</div>
  			  	</c:if>
  			  	<div id="meeting">
  			  	<div class="form-group">
		    		<label class="col-sm-1 control-label" style="width:10%">会议内容<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
		    			<textarea id="description" class="form-control" style="width:100%" name="morningMeetingVo.description"></textarea>
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-1 control-label" style="width:10%">附件</label>
	    			<div class="col-sm-8">
	    			<input id="files" type="file" name="attachment" multiple>
	    			</div>
	    	  	</div> 
	    	  	</div>
  			  	<div id="noMeeting" style="display:none">
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">未开原因<span style="color:red"> *</span></label>
  			  		<div class="col-sm-2">
  			  			<select class="form-control" name="morningMeetingVo.noMeetingReason">
  			  				<option value="">请选择</option>
  			  				<option value="延期召开">延期召开</option>
  			  				<option value="本周不开">本周不开</option>
  			  			</select>
  			  		</div>
  			  	</div>
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:10%">情况说明<span style="color:red"> *</span></label>
  			  		<div class="col-sm-7">
  			  			<textarea class="form-control" rows="3" name="morningMeetingVo.remark"></textarea>
  			  		</div>
  			  	</div>
  			  	</div>
	    	  	<div class="form-group">
	    	  	 	<label class="col-sm-1" style="width:10%"></label>
	    	  	 	<div class="col-sm-2">
				    <button id="submitBtn" type="submit" class="btn btn-primary" >提交</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
			  		</div>
			  	</div>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>