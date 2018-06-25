<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

</script>
<style type="text/css">
	.col-sm-1 {
		padding-right:0px;
		padding-left:0px;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'notice'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="form_"  action="administration/notice/saveNotice"  method="post" class="form-horizontal" enctype="multipart/form-data">
        	  <!-- 附件上传模式（新增或覆盖） -->
   			  <input type="hidden" name="uploadMode">
   			  <input type="hidden" name="noticeVO.ntcID" value="${noticeVO.ntcID}"> 
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">修改公告</h3>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label">标题</label>
			   <div class="col-sm-4">
			    	<input type="text" class="form-control" id="expressNumber" name="noticeVO.ntcTitle" value="${noticeVO.ntcTitle}" required="required"/>
			    </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label">内容</label>
			     <div class="col-sm-5">
			    	<textarea class="form-control" rows="8" id="content" name="noticeVO.ntcContent" required="required">${noticeVO.ntcContent}</textarea>
			    </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label">是否置顶</label>
			     <div class="col-sm-2">						
			    <select class="form-control"  name="noticeVO.isTop" >
				      <option value="">请选择</option>
				      <option value="1" ${noticeVO.isTop=='1'?'selected':''}>是</option>
				      <option value="0" ${noticeVO.isTop=='0'?'selected':''}>否</option>
				     </select>
			     </div>
			  </div>
			  <div class="form-group" >
	    		<label for="beginDate" class="col-sm-1 control-label">置顶开始日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="noticeVO.topStartTime"  value="${noticeVO.topStartTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
			  <div class="form-group" >
	    		<label for="beginDate" class="col-sm-1 control-label">置顶结束日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="noticeVO.topEndTime"  value="${noticeVO.topEndTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
	    	 <div class="form-group" >
				<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
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
			    <button type="submit" id="submitButton" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
    <script src="/assets/js/underscore-min.js"></script>
    <script src="/assets/js/layer/layer.js"></script>
    <script>
    $(function(){
    	$('#form_').submit(function(){
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
    })
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