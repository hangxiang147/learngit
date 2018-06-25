<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	.inputout{position:relative;}
	.text{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
	.blue{color:#428bca}
	.left{text-align:left !important}
	.hand{cursor:hand}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="selectedPanel" value="'divideRequire'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">编辑任务</h3>
				<form id="saveSubRequirementInfo" action="/performance/soft/save_saveSubRequirement"
					method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					<s:token></s:token>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">任务名称</label>
						<div id="name" class="col-sm-2">
							<input class="form-control" autoComplete="off" required name="subRequirementVo.subRequirementName" value="${hf:format(subRequirementVo.subRequirementName)}"> 
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">分值</label>
						<div id="name" class="col-sm-2">
							<input class="form-control" autoComplete="off" type="number" required name="subRequirementVo.score" value="${subRequirementVo.score}"> 
						</div>
					</div>
					<div class="form-group">
						<label for="description" class="col-sm-1 control-label">任务描述</label>
						<div id="description" class="col-sm-6">
							<textarea class="form-control" name="subRequirementVo.description" data-provide="markdown" rows="7"></textarea>
						</div>
					</div>
				<div class="form-group">
				  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 附件</label>
				  		<c:if test="${!edit}">
						<div class="col-sm-5">
						<input type="file" id="attachment" name="files"  style="padding:6px 0px;" multiple />
						<input name="fileDetail" id="fileDetail" style="display:none"/>	
						</div>
						</c:if>
						<c:if test="${edit}">
						<div class="left control-label" id="_attachment"></div>
						</c:if>
			  	</div>
					<div class="form-group">
						<button type="submit" class="btn btn-primary"
							style="margin-left: 2%">提交</button>
						<button type="button" class="btn btn-default"
							onclick="location.href='javascript:history.go(-1);'"
							style="margin-left: 2%">返回</button>
					</div>
					<input style="display: none" name="subRequirementVo.isDeleted" value="${subRequirementVo.isDeleted}" />
					<input style="display: none" name="subRequirementVo.addTime" value="${subRequirementVo.addTime}" />
					<input style="display: none" name="subRequirementVo.id" value="${subRequirementVo.id}" />
					<input style="display: none" name="subRequirementVo.requirementId" value="${subRequirementVo.requirementId}" />
					<input style="display: none" name="requirementId" value="${requirementId}" />
					<input type="hidden" name="attachmentPathStr" value="${subRequirementVo.attachmentPath}"/>
					<input type="hidden" name="attachmentNameStr" value="${subRequirementVo.attachmentName}"/>
				</form>
			</div>
		</div>
	</div>
	<!-- 附件上传 -->
	<div class="modal fade bs-example-modal-lg" id="attachmentDiv" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">附件上传</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="file" name="fileAttachment" id="fileAttachment" />
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal" id="confirm">确定</button>
	      <button type="button" class="btn btn-primary" data-dismiss="modal" id="close">关闭</button>
	     
	      
	      </div>
	    </div>
	  </div>
	</div>
    <div id="tar_box" style="display:none"><img></img></div>
	<script src="/assets/js/underscore-min.js"></script>
	<script src="/assets/js/textarea/highlight.pack.js"></script>
	<script>hljs.initHighlightingOnLoad();</script>
	<script src="/assets/js/textarea/marked.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script>
     marked.setOptions({
         highlight: function (code) {
             return hljs.highlightAuto(code).value;
         }
     });
    </script>
	<script src="/assets/js/textarea/to-markdown.js"></script>
	<script src="/assets/js/textarea/bootstrap-markdown.js"></script>
	<script src="/assets/js/textarea/jquery.textcomplete.js"></script>
	<script src="/assets/js/textareaHelper.js"></script>
	<script type="text/javascript">
    	$(function(){
           		$(document).click(function(){
	            	$(".text ul").empty();
	            }); 
        		if('${edit}'=='true'){
        			//转义字符，如单引号
		        	var description = '${jf:format(subRequirementVo.description)}';
		    		description = description.replace(/<br>/g, "\r\n");
		    		$("#description textarea").val(description);
	        		var attachmentNameStr = $("input[name='attachmentNameStr']").val();
	        		var attachmentPathStr = $("input[name='attachmentPathStr']").val();
            		if(attachmentNameStr!='' && attachmentNameStr!='null' && attachmentPathStr!='' && attachmentPathStr!='null'){
            			var attachmentNames = attachmentNameStr.split("#@#&");
            			var attachmentPaths = attachmentPathStr.split("#@#&");
                		var html = '';
                		for(var i=0; i<attachmentNames.length; i++){
                			if(attachmentNames[i]==''){
                				continue;
                			}
                			html += '<span data-preifx="'+attachmentPaths[i]+' "style="margin-left:2%">'+attachmentNames[i]+'</span>&nbsp;<span onclick="deleteAttachment(this)" class="glyphicon glyphicon-remove hand blue"></span>';
                		}
                		$("#_attachment").html(html);
                		html = '';
                 			html += '<div class="col-sm-1"></div>'+
    								'<div class="col-sm-5">'+
    								'<input type="file" id="attachment" name="files"  style="padding:6px 0px;" multiple />'+
    								'<input name="fileDetail" id="fileDetail" style="display:none"/></div>';
    					$("#_attachment").after(html);
            		}else{
            			$("#_attachment").after('<div class="col-sm-5">'+
        									'<input type="file" id="attachment" name="files"  style="padding:6px 0px;" multiple />'+
        									'<input name="fileDetail" id="fileDetail" style="display:none"/>');
            			$("#_attachment").remove();
            		}
        		}
        		var $textareaItem=$('#description textarea');
            	var textareaInit=(function ($item){
            	  		  $('#comment-area').textcomplete([
            	                 {
            	                     mentions: ['admin','Devops','ly','root_root','ly','gonglexin','ly','EricGuo','ly','steve','ly','liuxey','ly','axlrose','unix','ly','ly','newbee','ly','gaicitadie','ly','gazeldx','ly','jthmath','ly','ly','yugo','ly','lxy254069025','ly','Arata','hades3264331136','itfanr','itfanr','ly','ly'],
            	                     match: /\B@(\w*)$/,
            	                     search: function (term, callback) {
            	                         callback($.map(this.mentions, function (mention) {
            	                             return mention.indexOf(term) === 0 ? mention : null;
            	                         }));
            	                     },
            	                     index: 1,
            	                     replace: function (mention) {
            	                         return '@' + mention + ' ';
            	                     }
            	                 }
            	             ]);
            	  		})($textareaItem);
    	});
        $("#saveSubRequirementInfo").submit(function(){
  		  //验证文件大小，不能超过5M(5*1024*1024)
  		   var maxSize = 5*1024*1024;
             var files = $("#attachment")[0].files;
             var fileNames = '';
             for(var i=0; i<files.length; i++){
          	   var file = files[i];
          	   if(file.size>maxSize){
          		   layer.alert("文件"+file.name+"超过5M，限制上传",{offset:'100px'});
          		   return false;
          	   }else{
          		   if(i==0){
          			   fileNames += file.name;
          		   }else{
          			   fileNames += "#@#&"+file.name;
          		   }
          	   }
             }
             $("input[name='fileDetail']").val(fileNames);
         });
       	function deleteAttachment(target){
    		var attachmentName = $(target).prev().text();
    		var attachmentPath = $(target).prev().attr("data-preifx");
    		var subRequireId = $("input[name='subRequirementVo.id']").val();
    		$.ajax({
    			type:'post',
    			data:{'attachmentName':attachmentName,'attachmentPath':attachmentPath,'subRequireId':subRequireId},
    			url:'/performance/soft/deleteAttachmentForSubRequire',
    			success:function(data){
    				if(data.flag=="true"){
    					$(target).prev().remove();
    					$(target).remove();
    				}
    				//判断div里面附件是否为空，空的话，就清除掉这个div，以便调整布局
    				if($("#_attachment").html().length<20){
    					$("#_attachment").next().remove();
    					$("#_attachment").remove();
    				}
    			}
    		});
    	}
//      以下是textarea 支持 图偏上传的代码
        $(function (){
     	   _.delay(function (){
     		  var $items= $('button[title="Image"]');
     		  $items.each(function (){
     			  $(this).bind("click",function (event){
     				  event.stopPropagation();
     				  $('#fileAttachment').val("");
     				  $('#attachmentDiv').modal('show');
     				  $('#attachmentDiv').data("item",$(this).parent().parent().next());
     			  });
     		  })
     	   },500)
        });
        $('#close').click(function (){
 			$('#attachmentDiv').modal('hide');
        })
        $('#confirm').click(function (){
    		    var img = document.getElementById("fileAttachment");
   		    var name=$(img).val();
   		    var replace="";
 			if(name!=null&&name!=''){
 				var index=name.lastIndexOf("\\");
 	            var fileName=name.substring(index+1,name.length)
 				var suffix=fileName.replace(/.*\.(.*)/,"$1");
 		        suffix=suffix.toLowerCase();
 		        if(!_.find(['png','jpg','jpeg'],function(s){
 		        	return s==suffix
 		        })){
 		        	layer.alert("附件类型只能是:png,jpg,jpeg!",{offset:'100px'})
 		        	$(img).val("");
 		        	return;
 		        }else{
 		        	replace=fileName;
 		        }
 			}else{
 	        	return ;
 			}

    		    
    		    var fm = new FormData();
    		    fm.append('imageName', fileName);
    		    fm.append('file', img.files[0]);
    		    $.ajax(
    		        {
    		            url: '/performance/soft/attachmentSave',
    		            type: 'POST',
    		            data: fm,
    		            async:false,
    		            contentType: false, 
    		            processData: false, 
    		            success: function (result) {
    		            
    	   		            var id=result.id;
    	   		            var url=location.href.substring(0,location.href.lastIndexOf("/"))+"/downloadPic?id="+id;
    	   					$('#attachmentDiv').modal('hide');
    	   					var $textarea=$('#attachmentDiv').data("item");
    	   					var textarea=$textarea[0];
    	   					pos = cursorPosition.get(textarea);  
    	   					var addStr="!["+replace+"]"+"("+url+")";
    	   					cursorPosition.add(textarea, pos, addStr);
    	   					var pos =cursorPosition.get(textarea);
    	   					var removeStr=addStr.length-1;
    	   					pos.start=pos.start-removeStr+1;
    	   					pos.end=replace.length+pos.start;
    	   					cursorPosition.set(textarea, pos);
    		            	
    		            	
    		            },fail:function (result){
    		            	layer.alert("图片上传失败",{offset:'100px'});
    		            }
    		        }
    		    );
        })
     
     	
     	
     $("textarea").on("paste", function(event) {
     	var item_textarea=$(this);
 	    var items = (event.clipboardData || event.originalEvent.clipboardData).items;
 	    for (index in items) {
 	        var item = items[index];
 	        var type = item.type;
 	        if(type && type.split('/')[0] == 'image') {
 	            var suffix = type.split('/')[1];
 	            var blob = item.getAsFile();
 	            var size = blob.size;
 	            if(size / (1024 * 1024) < 5) {
 	                var reader = new FileReader();
 	                reader.onload = function (event) {
 	                    var form = document.createElement("form").setAttribute("enctype", "multipart/form-data");
 	                    var formData = new FormData(form);
 	                    formData.append("file", blob, "image." + suffix);
 	                    formData.append("imageName","image")
 	                    var xhr = new XMLHttpRequest();
 	                    xhr.open('POST', '/performance/soft/attachmentSave', true);
 	                    xhr.onload = function (event) {
 	                        var responseText = event.currentTarget.responseText;
 	                        var json = JSON.parse(responseText);
 	   	   		            var url=location.href.substring(0,location.href.lastIndexOf("/"))+"/downloadPic?id="+json.id;
 	                        var replace="image";
 	                    	var textarea=item_textarea[0];
 	   	   					pos = cursorPosition.get(textarea);  
 	   	   					var addStr="!["+replace+"]"+"("+url+")";
 	   	   					cursorPosition.add(textarea, pos, addStr);
 	   	   					var pos =cursorPosition.get(textarea);
 	   	   					var removeStr=addStr.length-1;
 	   	   					pos.start=pos.start-removeStr+1;
 	   	   					pos.end=replace.length+pos.start;
 	   	   					cursorPosition.set(textarea, pos);
 	                    };
 	                    xhr.send(formData);
 	                };
 	                reader.readAsDataURL(blob);
 	            } else {
 	                layer.alert("上传图片大小不能超过5M",{offset:'100px'});
 	            }
 	        }
     }
 });
  </script>
</body>
</html>