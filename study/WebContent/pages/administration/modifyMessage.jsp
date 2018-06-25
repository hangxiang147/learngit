<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/editor/themes/default/default.css" />
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/editor/kindeditor-min.js"></script>
<script src="/assets/editor/lang/zh_CN.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">

	$(function() {
		//富文本
    	kedit('textarea[name="noticeVO.ntcContent"]');
		
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "noticeVO.departmentIDs");
		
    	$('#addVacationForm').submit(function(){
    		var fileList=document.getElementById('files').files;
    		var sumSize=0;
    		var result_arr=_.reduce(fileList,function(arr,input){
    			var name=input.name;
    			var fileName= name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
   	            var suffix=fileName.replace(/.*\.(.*)/,"$1");
   	            if(suffix)suffix=suffix.toLowerCase();
   	            arr.push([fileName,suffix]);
   	         	sumSize+=input.size;
   	            return arr;
    		},[]);
    		if(sumSize>1024*1024*20){
    			layer.alert("文件总大小不能超过20M", {offset:'100px'});
    			return false;
    		}
     	    $('input[name="fileDetail"]').val(JSON.stringify(result_arr));
     	    $('#submitButton').attr('disabled','disabled');
     	   	Load.Base.LoadingPic.FullScreenShow(null);
    	})
	});
	function kedit(kedit){
		var	editor =KindEditor.create(kedit, {
				resizeType : 1,
				allowPreviewEmoticons : false,
				items : [
					'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
					'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
					'insertunorderedlist', 'table', '|', 'emoticons', 'image', 'link', '|','preview']
			});
	}
	function selectUploadMode(){
		$("#uploadMode").modal("show");
	}
	function selectFile(){
		$("input[name='uploadMode']").val($("#selectMode").find("option:selected").val());
		$("#files").click();
		$("#fileBtn").css("display","none");
		$("#fileBtn").next().css("display","none");
		$("#files").css("display","block");
	}
</script>
<style type="text/css">
	.ke-container{
		width:100% !important;
	}
	.icon {
	width: 1.5em; height: 1.5em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'notice'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="addVacationForm"  action="administration/notice/saveMessage"  method="post" class="form-horizontal" enctype="multipart/form-data">
        	  <input type="hidden" name="noticeVO.ntcID" value="${noticeVO.ntcID}"> 
        	  <!-- 附件上传模式（新增或覆盖） -->
   			  <input type="hidden" name="uploadMode">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">修改通知</h3>
        	   <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label" style="width:10%">标题</label>
			   <div class="col-sm-4">
			    	<input type="text" class="form-control" id="expressNumber" name="noticeVO.ntcTitle" value="${noticeVO.ntcTitle}" required="required"/>
			    </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label" style="width:10%">内容</label>
			     <div class="col-sm-6">
			   		<textarea class="form-control" rows="8" id="content" name="noticeVO.ntcContent">${noticeVO.ntcContent}</textarea>
			    </div>
			  </div>
	    	  <div class="form-group flag1">
				<label for="company" class="col-sm-1 control-label" style="width:10%">通知部门：</label>
				<div class="col-sm-5 control-label" style="text-align:left">
				<c:if test="${not empty groups}">
				<c:forEach items="${groups}" var="group">${group}<br></c:forEach>
				</c:if>
				<c:if test="${empty groups}">
				——
				</c:if>
				</div>
			</div>
			<div id="demo"></div>
	    	  <div class="form-group" >
				<label for="attachment" class="col-sm-1 control-label" style="width:10%"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
				<div class="col-sm-5">
					<c:if test="${not empty attachments}">
					<c:forEach items="${attachments}" var="attachment">
						<a href="administration/notice/download?attachmentPath=${attachment.softURL}&attachmentName=${attachment.softName}">${attachment.softName}</a><br>
					</c:forEach>
					<input type="button" id="fileBtn" onclick="selectUploadMode()" value="选择文件"> <span>未选择任何文件</span>
	    			<input multiple name="files"  id="files" type="file" style="display:none"/>
	    			<input name="fileDetail" style="display:none"/>
					</c:if>
					<c:if test="${empty attachments}">
					<input multiple name="files"  id="files" type="file"/>
	    			<input name="fileDetail" style="display:none"/>
					</c:if>
	    		</div>
	    	  </div>
			  <div class="form-group">
			  	<div class="col-sm-1 control-label" style="width:10%"></div>
			  	<div class="col-sm-3">
			    <button type="submit" id="submitButton" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			 	</div>
			  </div>
			</form>
        </div>
      </div>
    </div>
    <div id="uploadMode" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">上传模式</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label">模式</label>
					<div class="col-sm-6">
						<select id="selectMode" class="form-control">
							<option value="新增">新增</option>
							<option value="覆盖">覆盖</option>
						</select>
					</div>
				</div>
				</form>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="selectFile()" data-dismiss="modal" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
</body>
</html>