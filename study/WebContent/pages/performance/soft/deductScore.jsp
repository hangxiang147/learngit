<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<style type="text/css">
	.inputout{position:relative;}
	.text{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="selectedPanel" value="'showScore'"></s:set>
			<%@include file="/pages/performance/soft/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">扣分</h3>
				<form id="saveDeductedScore" action="/performance/soft/save_saveDeductedScore"
					method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					<s:token></s:token>
					<div class="form-group">
					<label class="col-sm-1 control-label">项目</label>
					<div class="col-sm-2">
						<select id="project" required class="form-control" onchange="getProjectVersions()">
							<option>请选择</option>
							<c:forEach items="${projects}" var="project">
								<option value="${project.id}">${project.name}</option>
							</c:forEach>
						</select>
					</div>
					</div>
					<div class="form-group">
					<label class="col-sm-1 control-label">版本</label>
					<div class="col-sm-2">
						<select class="form-control" id="selectVersion" required name="scoreResultVo.versionId">
						</select>
					</div>
					</div>
					<div class="form-group">
						<label for="user" class="col-sm-1 control-label">被扣人员</label>
						<div id="user" class="col-sm-2 inputout">
							<span class="input_text" onclick="f()">
							<input id="userName" autoComplete="off" class="form-control" required 
								 oninput="findStaffByName()" onblur="clears()"/>
					    	<input type="hidden" id="userId" name="scoreResultVo.userId" />
					    	<input type="hidden" id="userFlag" />
					    	</span>
					    	<div class="text_down text">
								<ul></ul>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="resultScore" class="col-sm-1 control-label">扣几分</label>
						<div id="resultScore" class="col-sm-2">
							<input autoComplete="off" class="form-control" type="number" min="1" required name="scoreResultVo.resultScore"/>
						</div>
					</div>
					<div class="form-group">
						<label for="reason" class="col-sm-1 control-label">扣分原因</label>
						<div id="reason" class="col-sm-6">
							<textarea required class="form-control" name="scoreResultVo.reason" data-provide="markdown" rows="7"></textarea>
						</div>
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-primary"
							style="margin-left: 2%">提交</button>
						<button type="button" class="btn btn-default"
							onclick="location.href='javascript:history.go(-1);'"
							style="margin-left: 2%">返回</button>
					</div>
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
	<script src="/assets/js/underscore-min.js"></script>
	<script src="/assets/js/textareaHelper.js"></script>
	<script type="text/javascript">
		function getProjectVersions(){
			var project = $("#project").find("option:selected").val();
    		$.ajax({
    			url:'performance/soft/changeProject',
    			type:'post',
    			data:{'project':project},
    			dataType:'json',
    			success:function (data){
    				var html = "<option value=''>请选择</option>";
    				data.versions.forEach(function(value, index){
    					html += "<option value='"+value.id+"'>"+value.version+"</option>";
    				});
    				$("#selectVersion").html(html);
    			}
    		});
		}
    	$(function(){
    		var $textareaItem=$('#reason textarea');
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
        	
           		$(document).click(function(){
	            	$(".text ul").empty();
	            });   
    	});
    	function clears(){
   		 if ($("#userName").val()!=$("#userFlag").val()){
   			 $("#userName").val("");
   			 $("#userId").val("");
   		  }   
   	    }
    	function findStaffByName(index) {
    		var name = $("#userName").val();
    		if (name.length == 0) {
    			return;
    		}
    		$(".text_down ul").empty();
    		$.ajax({
    			url:'personal/findStaffByName',
    			type:'post',
    			data:{name:name},
    			dataType:'json',
    			success:function (data){
    				$.each(data.staffVOs, function(i, staff) {
    					var groupDetail = staff.groupDetailVOs[0];
    					$(".text_down ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
    				});
    				$(".text_down").show();
    			}
    		});
    	}
    	function f(index){
    		if($(".text_down ul").html() != ""){
    				$(".text_down").show();
    				event.stopPropagation();
    			}
    			$('body').on('click','.text_down ul li',function () {
    			var shtml=$(this).html();
    			$(".text_down").hide();
    			$("#userName").val(shtml.split("（")[0]);
    			$("#userFlag").val(shtml.split("（")[0]);
    			var agent = $(this).find("input").val();
    			$("#userId").val(agent.split("@")[0]);
    			});
    			$(".text_down ul").empty();
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
    		            	layer.alert("图片上传失败");
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