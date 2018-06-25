<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<style type="text/css">
	
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'problemOrderList'"></s:set>
      	<c:if test="${!fromPersonal}">
        <%@include file="/pages/performance/soft/manage/panel.jsp" %>	
       	</c:if>
       	<c:if test="${fromPersonal}">
        <%@include file="/pages/personal/panel.jsp" %>	
       	</c:if>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">提问题单</h3>
			<form class="form-horizontal" action="performance/soft/save_startProblemOrder" enctype="multipart/form-data" method="post" onsubmit="return checkAtta()">
				<s:token></s:token>
				<input type="hidden" name="fromPersonal" value="${fromPersonal}">
				<div class="form-group">
					<c:if test="${!fromPersonal}">
					<label class="col-sm-1 control-label" style="width:10%">项目<span style="color:red"> *</span></label>
					<div class="col-sm-2">
						<select name="problemOrderVo.projectId" id="selectProject" class="form-control" required onchange="changeProject()">
							<option value="">请选择</option>
							<c:forEach items="${projects}" var="project">
								<option value="${project.id}">${project.name}</option>
							</c:forEach>
						</select>
					</div>
					</c:if>
					<c:if test="${fromPersonal}"><input type="hidden" name="problemOrderVo.projectId" value="${projectId}"></c:if>
					<label class="col-sm-1 control-label" <c:if test="${fromPersonal}">style="width:10%"</c:if>>属主<span style="color:red"> *</span></label>
					<div class="col-sm-2">
						<input id="questionerId" type="text" autocomplete="off" class="form-control" required onkeyup="checkEmpty()" value="${questionerName}">
						<input type="hidden" name="problemOrderVo.questionerId" value="${questionerId}">
					</div>
				</div>
				<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%">问题名称<span style="color:red"> *</span></label>
					<div class="col-sm-5">
						<input autocomplete="off" class="form-control" required name="problemOrderVo.orderName">
					</div>
				</div>
				<div class="form-group">
	    		<label class="col-sm-1 control-label" style="width:10%">问题描述</label>
	    		<div class="col-sm-7">
	    			<textarea class="form-control" style="width:100%" name="problemOrderVo.description"></textarea>
	    		</div>
	    	  	</div> 
	    	  	<div class="form-group">
	    	  		<label class="col-sm-1 control-label" style="width:10%">附件</label>
	    			<div class="col-sm-8">
	    			<input id="files" type="file" name="problemOrderVo.attachment" multiple>
	    			</div>
	    	  	</div>
	    	  	<div class="col-sm-1" style="width:10%"></div>
	    	  	<button type="submit" class="btn btn-primary">提交</button>
				<button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
			</form>
        </div>
      </div>
    </div>
	<script src="/assets/js/layer/layer.js"></script>
	<script src="/assets/js/require/require2.js"></script>
	<script src="/assets/editor/kindeditor.js"></script>
	<script src="/assets/editor/lang/zh_CN.js"></script>
	<script type="text/javascript">
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
		kedit('textarea[name="problemOrderVo.description"]');
	});
	function changeProject(){
		var project = $("#selectProject option:selected").val();
		$.ajax({
			data:{'project':project},
			url:'/performance/soft/changeProjectForShowTask',
			success:function(data){
				var modules = data.modules;
				var html = '<option value="">请选择</option>';
				for(var i=0; i<modules.length; i++){
					html += '<option value="'+modules[i].id+'">'+modules[i].module+'</option>';
				}
				$("#selectModule").html(html);
				html = '<option value="">请选择</option>';
				var versions = data.versions;
				for(var i=0; i<versions.length; i++){
					html += '<option value="'+versions[i].id+'">'+versions[i].version+'</option>';
				}
				$("#selectVersion").html(html);
			}
		});
	}
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#questionerId'),function ($item){
			$("input[name='problemOrderVo.questionerId']").val($item.data("userId"));
		});
	});
	function checkEmpty(){
		if($("#questionerId").val()==''){
			$("input[name='problemOrderVo.questionerId']").val('');
		}
	}
	$(document).click(function (event) {
		if ($("input[name='problemOrderVo.questionerId']").val()=='')
		{
			$("#questionerId").val("");
		}
	});
	function kedit(kedit){
		var	editor = KindEditor.create(kedit, {
				resizeType : 1,
				allowPreviewEmoticons : false,
				height:200,
				items : ['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
							'insertunorderedlist', '|', 'emoticons', 'image', 'link', '|','preview']
			});
	}
	function checkAtta(){
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
