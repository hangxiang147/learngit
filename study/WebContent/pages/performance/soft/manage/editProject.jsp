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
<link href="/assets/css/dark.css" rel='stylesheet'/>
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
			<s:set name="selectedPanel" value="'showProject'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">编辑项目信息</h3>
				<form id="saveProjectInfo" action="/performance/soft/save_saveProject"
					method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					<s:token></s:token>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">项目名称</label>
						<div id="name" class="col-sm-2">
							<select class="form-control" required name="projectVO.name" id="selectProject">
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">项目代号</label>
						<div id="name" class="col-sm-2">
							<input class="form-control" autocomplete="off" required="required" name="projectVO.code"
								value="${projectVO.code}" />
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">项目负责人</label>
						<div id="name" class="col-sm-2 inputout">
							<span class="input_text" onclick="f('project')">
							<input id="projectHeaderName" autocomplete="off" class="form-control" required="required" name="projectVO.projectHeaderName"
								value="${projectVO.projectHeaderName}" oninput="findStaffByName('project')" onblur="clears('project')"/>
					    	<input type="hidden" id="projectHeaderId" value="${projectVO.projectHeaderId}" name="projectVO.projectHeaderId" />
					    	<input type="hidden" id="projectHeaderFlag" value="${projectVO.projectHeaderName}"/>
					    	</span>
					    	<div class="project_text_down text">
								<ul></ul>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">测试负责人</label>
						<div id="name" class="col-sm-2 inputout">
							<span class="input_text" onclick="f('test')">
							<input id="testHeaderName" autocomplete="off" class="form-control" required="required" name="projectVO.testHeaderName"
								value="${projectVO.testHeaderName}" oninput="findStaffByName('test')" onblur="clears('test')"/>
							<input type="hidden" id="testHeaderId" value="${projectVO.testHeaderId}" name="projectVO.testHeaderId" />
					    	<input type="hidden" id="testHeaderFlag" value="${projectVO.testHeaderName}"/>
							</span>
							<div class="test_text_down text">
								<ul></ul>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="description" class="col-sm-1 control-label">项目描述</label>
						<div id="description" class="col-sm-6">
							<textarea required="required" class="form-control" name="projectVO.description" data-provide="markdown" rows="7"></textarea>
						</div>
					</div>
					<div class="form-group" id="percentDiv1">
						 <label  class="col-sm-1 control-label">需求</label>
						 <div class="col-sm-1">
						 	<input  name="projectVO.xq" class="form-control" required type="number" max=100 min =0 value="${projectVO.xq}"/>
						 </div>
						<label  class="col-sm-1 control-label"> 组长</label>
						 <div class="col-sm-1">
						 	<input  name="projectVO.zz" class="form-control" required type="number" max=100 min =0 value="${projectVO.zz}" />
						 </div>
						 <label  class="col-sm-1 control-label">经理</label>
						 <div class="col-sm-1">
						 	<input   name="projectVO.jl" class="form-control" required type="number" max=100 min =0  value="${projectVO.jl}"/>
						 </div>
					</div>
						<div class="form-group" id="percentDiv2">
						 <label  class="col-sm-1 control-label">开发</label>
						 <div class="col-sm-1">
						 	<input  name="projectVO.kf" class="form-control" required type="number" max=100 min =0    value="${projectVO.kf}" />
						 </div>
						 <label  class="col-sm-1 control-label">测试</label>
						 <div class="col-sm-1">
						 	<input  name="projectVO.cs" class="form-control" required type="number" max=100 min =0   value="${projectVO.cs}"/>
						 </div>
						 <label  class="col-sm-1 control-label">实施</label>
						 <div class="col-sm-1">
						 	<input  name="projectVO.ss" class="form-control" required type="number" max=100 min =0    value="${projectVO.ss}" />
						 </div>
							<label  class="col-sm-1 control-label">总计</label>
						 
						  <div class="col-sm-1" id="resultdiv">
						  <label class="col-sm-1 control-label">
						  	 <font color="red"></font></label>
						  </div>
					</div>
					<div class="form-group">
						<button type="submit" class="btn btn-primary"
							style="margin-left: 2%">提交</button>
						<button type="button" class="btn btn-default"
							onclick="location.href='/performance/soft/showProject'"
							style="margin-left: 2%">返回</button>
					</div>
					<input style="display: none" name="projectVO.isDelete" value="${projectVO.isDelete}" />
					<input style="display: none" name="projectVO.addTime" value="${projectVO.addTime}" />
					<input style="display: none" name="projectVO.id" value="${projectVO.id}" />
					<input style="display: none" name="projectVO.creatorId" value="${projectVO.creatorId}" />
					<input style="display: none" name="projectVO.updatestVersion" value="${projectVO.updatestVersion}" />
					<input style="display: none" name="loginUserId" value="<%=((User) request.getSession().getAttribute("user")).getId() %>" />
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
	<script src="/assets/js/textareaHelper.js"></script>
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
	<script src="/assets/js/dic.js"></script>
	<script type="text/javascript">
    	$(function(){
    		var description = '${jf:format(projectVO.description)}';
    		description = description.replace(/<br>/g, "\r\n");
    		$("#description textarea").val(description);
    		
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
        	
           		$(document).click(function(){
	            	$(".text ul").empty();
	            }); 
           		var project = '${projectVO.name}';
           		var source = dicContent['softPersonProject']['data'];
            	var sourceOptions = '';
            	_.each(source, function(value){
            		if(value[0]==project){
            			sourceOptions += '<option value="'+value[0]+'" selected >'+value[1]+'</option>';
            		}else{
            			sourceOptions += '<option value="'+value[0]+'">'+value[1]+'</option>';
            		}
            		
            	});
            	$("#selectProject").append(sourceOptions);
    	});
    	function clears(index){
   		 if ($("#"+index+"HeaderName").val()!=$("#"+index+"HeaderFlag").val()){
   			 $("#"+index+"HeaderName").val("");
   			 $("#"+index+"HeaderId").val("");
   		  }   
   	    }
    	function findStaffByName(index) {
    		var name = $("#"+index+"HeaderName").val();
    		if (name.length == 0) {
    			return;
    		}
    		$("."+index+"_text_down ul").empty();
    		$.ajax({
    			url:'personal/findStaffByName',
    			type:'post',
    			data:{name:name},
    			dataType:'json',
    			success:function (data){
    				$.each(data.staffVOs, function(i, staff) {
    					var groupDetail = staff.groupDetailVOs[0];
    					$("."+index+"_text_down ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
    				});
    				$("."+index+"_text_down").show();
    			}
    		});
    	}
    	function f(index){
    		if($("."+index+"_text_down ul").html() != ""){
    				$("."+index+"_text_down").show();
    				event.stopPropagation();
    			}
    			$('body').on('click','.'+index+'_text_down ul li',function () {
    			var shtml=$(this).html();
    			$("."+index+"_text_down").hide();
    			$("#"+index+"HeaderName").val(shtml.split("（")[0]);
    			$("#"+index+"HeaderFlag").val(shtml.split("（")[0]);
    			var agent = $(this).find("input").val();
    			$("#"+index+"HeaderId").val(agent.split("@")[0]);
    			});
    			$("."+index+"_text_down ul").empty();
    	}
    	
    	
    	var caculate=(function(){
    		var allInputs=[];
    		$('#percentDiv1,#percentDiv2').find("input").each(function (){
    			$(this).bind("blur",function (){
    				var number=0;
    				$('#percentDiv1,#percentDiv2').find("input").each(function (){
    					number+=+$(this).val();
    				})
    				$('#resultdiv').find("font").html(number+"%");
    			});
    		})
    		$('#percentDiv1').find('input:eq(0)').trigger("blur");
    	})();
    	
    	$('#saveProjectInfo').submit(function (){
    		var number=0;
			$('#percentDiv1,#percentDiv2').find("input").each(function (){
				number+=+$(this).val();
			})
			if(number!==100){
				layer.confirm("总计并非100%,请重新填写！",{offset:'100px'});
			}else{
				return true;
			}
			return false;
    	})
    	       
//       以下是textarea 支持 图偏上传的代码
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