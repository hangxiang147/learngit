<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'notice'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form  id="form_" action="administration/notice/saveNews"  method="post" class="form-horizontal"  enctype="multipart/form-data" >
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">新增大事记</h3>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label">标题</label>
			   <div class="col-sm-4">
			    	<input type="text" class="form-control" id="expressNumber" name="noticeVO.ntcTitle" required="required"/>
			    </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-1 control-label">内容</label>
			     <div class="col-sm-5">
			    	<textarea class="form-control" rows="8" id="content" name="noticeVO.ntcContent"  required="required"></textarea>
			    </div>
			  </div>
			  <div class="form-group" >
<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label><div class="col-sm-2">
	    			<input  multiple name="files"  id="files" type="file" />
	    			<input name="fileDetail" style="display:none"/>
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
    <script>
    $(function(){
    	$('#form_').submit(function(){
    		var fileList=document.getElementById('files').files;
    		var result_arr=_.reduce(fileList,function(arr,input){
    			var name=input.name;
    			var fileName= name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
   	            var suffix=fileName.replace(/.*\.(.*)/,"$1");
   	            if(suffix)suffix=suffix.toLowerCase();
   	            arr.push([fileName,suffix]);
   	            return arr;
    		},[]);
     	    $('input[name="fileDetail"]').val(JSON.stringify(result_arr));
     	   Load.Base.LoadingPic.FullScreenShow(null);
    		$('#submitButton').attr('disabled','disabled')
    	})
    })
    </script>
</body>
</html>