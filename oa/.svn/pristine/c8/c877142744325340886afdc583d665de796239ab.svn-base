<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<style type="text/css">
	.inputout{position:relative;}
	.text{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
	.left{
		padding-left:0 !important;
		text-align:left !important;
	}
	 table{width:100%;border-collapse:collapse;}
	 table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	 table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	 table tr .black {background:#efefef;text-align:center;color:#000;}
	 table tbody tr td p{padding:4px 0px;}
	.blue{color:#428bca}
	.hand{cursor:hand}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'showTask'"></s:set>
      		<%@include file="/pages/performance/soft/subject/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">修改任务</h3> 
      			<form id="saveRequirementInfo" class="form-horizontal" enctype="multipart/form-data"
      			action="/performance/soft/save_saveSingleTask" method="post">
      			<div class="form-group">
      			<s:token></s:token>
      			<label for="project" class="col-sm-1 control-label">所属项目</label>
      			<div id="project" class="col-sm-2">
      				<select class="form-control" required id="selectProject" name="softPerformanceVo.projectId" onchange="changeProject(),changeName(this)">
      					  <c:forEach items="${projects}" var="project">
      					  	<option value="${project.id}" <c:if test="${softPerformanceVo.projectId==project.id}">selected="selected"</c:if>>${project.name}</option>
      					  </c:forEach>
					</select>
					<input type="hidden" name="softPerformanceVo.projectName" value="${projects[0].name}">
      			</div>
      			<label for="version" class="col-sm-1 control-label">所属版本</label>
      				<div id="version" class="col-sm-2">
      				<select class="form-control" required id="selectVersion" name='softPerformanceVo.projectVersionId' onchange="changeProjectVersionOrModule(),changeName(this)">
      					<c:forEach items="${versions}" var="version">
      						<option value="${version.id}" <c:if test="${softPerformanceVo.projectVersionId==version.id}">selected="selected"</c:if>>${version.version}</option>
      					</c:forEach>
					</select>
					<input type="hidden" name="softPerformanceVo.versionName" value="${versions[0].version}">
					</div>
      			<label for="module" class="col-sm-1 control-label">所属模块</label>
      				<div id="module" class="col-sm-2">
      				<select class="form-control" required id="selectModule" onchange="changeProjectVersionOrModule(),changeName(this)" name='softPerformanceVo.moduleId'>
      					<c:forEach items="${modules}" var="module">
      						<option value="${module.id}" <c:if test="${softPerformanceVo.moduleId==module.id}">selected="selected"</c:if>>${module.module}</option>
      					</c:forEach>
					</select>
					<input type="hidden" name="softPerformanceVo.moduleName" value="${modules[0].module}">
					</div>
      			</div>
      			<div class="form-group">
      			<label for="taskType" class="col-sm-1 control-label">任务类型</label>
      			<div id="taskType" class="col-sm-2">
      				<select class="form-control" required id="selectTaskType" name="softPerformanceVo.taskType">
      					  <option value="设计">设计</option>
      					  <option value="开发">开发</option>
      					  <option value="测试">测试</option>
      					  <option value="研究">研究</option>
      					  <option value="讨论">讨论</option>
      					  <option value="界面">界面</option>
      					  <option value="事务">事务</option>
      					  <option value="其他">其他</option>
					</select>
      			</div>
      			<label for="assigner" class="col-sm-1 control-label">指派</label>
					<div id="assigner" class="col-sm-2 inputout">
						<span class="input_text" onclick="f('assigner')">
						<input id="assignerName" class="form-control" required name="softPerformanceVo.assignerName"
							value="${softPerformanceVo.assignerName}" oninput="findStaffByName('assigner')" onblur="clears('assigner')"/>
				    	<input type="hidden" id="assignerId" value="${softPerformanceVo.assignerId}" name="softPerformanceVo.assignerId" />
				    	<input type="hidden" id="assignerFlag" value="${softPerformanceVo.assignerName}"/>
				    	</span>
				    	<div class="assigner_text_down text">
							<ul></ul>
						</div>
					</div>
				<label for="assigner" class="col-sm-1 control-label">截止时间</label>
					<div class="col-sm-2">
					<input type="text" class="form-control" name="softPerformanceVo.deadline"  required
			    		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })" placeholder="截止时间" value="${softPerformanceVo.deadline}">
			    	</div>
				</div>
				<div class="form-group">
				<label for="associatedRequire" class="col-sm-1 control-label">关联需求</label>
					<div id="associatedRequire" class="col-sm-8">
      					<select class="form-control" required id="selectAssociatedRequire" name='softPerformanceVo.requirementId' onchange="changeName(this)">
	      					<c:forEach items="${associatedRequires}" var="require">
	      						<option value="${require.id}">${require.name}</option>
	      					</c:forEach>
						</select>
						<input type="hidden" name="softPerformanceVo.requirementName" value="${associatedRequires[0].name}">
					</div>
      			</div>
      			<div class="form-group">
      				<label for="name" class="col-sm-1 control-label">任务名称</label>
      				<div id="name" class="col-sm-8">
						<input name="softPerformanceVo.name" required="required" value="${softPerformanceVo.name}" class="form-control"/>
					</div>
      			</div>
      			<div class="form-group">
      				<label for="priority" class="col-sm-1 control-label">优先级</label>
      				<div id="priority" class="col-sm-2">
						<select class="form-control" required id="selectPriority" name='softPerformanceVo.priority'>
      					  	<option value="1">低</option>
      					  	<option value="2">中</option>
      					  	<option value="3">高</option>
      					  	<option value="4">加急</option>
						</select>
					</div>
					<label for="estimatedTime" class="col-sm-1 control-label">工时</label>
      				<div id="estimatedTime" class="col-sm-2">
						<input required="required" type="number" min=0 name="softPerformanceVo.estimatedTime" value="${softPerformanceVo.estimatedTime}" placeholder="小时" class="form-control"/>
					</div>
					<label for="score" class="col-sm-1 control-label">分值</label>
      				<div id="score" class="col-sm-2">
						<input required="required" type="number" min=0 max=100 name="softPerformanceVo.score" value="${softPerformanceVo.score}" class="form-control"/>
					</div>
      			</div>
      			<div class="form-group">
					<label for="description" class="col-sm-1 control-label">任务描述</label>
					<div id="description" class="col-sm-8">
						<textarea required="required" class="form-control" name="softPerformanceVo.description" data-provide="markdown" rows="7"></textarea>
					</div>
				</div>
				<div class="form-group">
				  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 附件</label>
 						<c:if test="${fn:length(attachmentLst)>0}">
						<div class="left control-label" id="_attachment">
							<c:forEach items="${attachmentLst}" var="attachment">
							<span style="margin-left:2%">${attachment}</span>&nbsp;<span onclick="deleteAttachment(this)" class="glyphicon glyphicon-remove hand blue"></span>
							</c:forEach>
						</div>
						<div class="col-sm-1"></div>
						<div class="col-sm-5">
						<input type="file" id="attachment" name="files"  style="padding:6px 0px;" multiple />
						<input name="fileDetail" id="fileDetail" style="display:none"/>
						</div>
						</c:if>
						<c:if test="${fn:length(attachmentLst)<1}">
						<div class="col-sm-5">
						<input type="file" id="attachment" name="files"  style="padding:6px 0px;" multiple />
						<input name="fileDetail" id="fileDetail" style="display:none"/>
						</div>
						</c:if>
			  	</div>
      			<div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:2%">提交</button>
			    <button type="button" class="btn btn-default" onclick="javascript:location.href='javascript:history.go(-1);'" style="margin-left:2%">返回</button>
			  	</div>
				  	<input type="hidden" name="softPerformanceVo.isDelete"  value="${softPerformanceVo.isDelete}"/>
					<input type="hidden" name="softPerformanceVo.addTime"  value="${softPerformanceVo.addTime}"/>
					<input type="hidden" name="softPerformanceVo.id"  value="${softPerformanceVo.id}"/>
					<input type="hidden" name="softPerformanceVo.creatorId" value="${softPerformanceVo.creatorId}"/>
					<input type="hidden" name="loginUserId" value="<%=((User) request.getSession().getAttribute("user")).getId() %>" />
					<input type="hidden" name="instanceId" value="${softPerformanceVo.instanceId}">
      			</form>
      		</div>
      </div>
    </div>
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
	<script src="/assets/js/myjs/tableCreator.js"></script>
	<script src="/assets/js/util.js"></script>
	<script type="text/javascript">
    	$(function(){
    		var description = '${jf:format(softPerformanceVo.description)}';
    		description = description.replace(/<br>/g, "\r\n");
    		$("#description textarea").val(description);
    		
    		var $textareaItem=$('textarea');
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
    	function changeProject(){
    		var project = $("#selectProject option:selected").val();
    		$.ajax({
    			type:'post',
    			data:{'project':project},
    			url:'/performance/soft/changeProjectForTask',
    			success:function(data){
    				var modules = data.modules;
    				var html = '';
    				for(var i=0; i<modules.length; i++){
    					html += '<option value="'+modules[i].id+'">'+modules[i].module+'</option>';
    				}
    				$("#selectModule").html(html);
    				if(modules.length>0){
    					$("input[name='softPerformanceVo.moduleName']").val(modules[0].module);
    				}else{
    					$("input[name='softPerformanceVo.moduleName']").val('');
    				}
    				html = '';
    				var versions = data.versions;
    				for(var i=0; i<versions.length; i++){
    					html += '<option value="'+versions[i].id+'">'+versions[i].version+'</option>';
    				}
    				$("#selectVersion").html(html);
    				if(versions.length>0){
    					$("input[name='softPerformanceVo.versionName']").val(versions[0].version);
    				}else{
    					$("input[name='softPerformanceVo.versionName']").val('');
    				}
    				html = '';
    				var associatedRequires = data.associatedRequires;
    				for(var i=0; i<associatedRequires.length; i++){
    					html += '<option value="'+associatedRequires[i].id+'">'+associatedRequires[i].name+'</option>';
    				}
    				$("#selectAssociatedRequire").html(html);
    				if(associatedRequires.length>0){
    					$("input[name='softPerformanceVo.requirementName']").val(associatedRequires[0].name);
    				}else{
    					$("input[name='softPerformanceVo.requirementName']").val('');
    				}
    			}
    		});
    	}
    	function changeProjectVersionOrModule(){
    		var versionId = $("#selectVersion option:selected").val();
    		var moduleId = $("#selectModule option:selected").val();
    		$.ajax({
    			type:'post',
    			data:{'versionId':versionId,'moduleId':moduleId},
    			url:'/performance/soft/changeProjectVersionOrModule',
    			success:function(data){
    				var associatedRequires = data.associatedRequires;
    				var html = '';
    				for(var i=0; i<associatedRequires.length; i++){
    					html += '<option value="'+associatedRequires[i].id+'">'+associatedRequires[i].name+'</option>';
    				}
    				$("#selectAssociatedRequire").html(html);
    				if(associatedRequires.length>0){
    					$("input[name='softPerformanceVo.requirementName']").val(associatedRequires[0].name);
    				}else{
    					$("input[name='softPerformanceVo.requirementName']").val('');
    				}
    			}
    		});
    	}
    	function clears(index){
      		 if ($("#"+index+"Name").val()!=$("#"+index+"Flag").val()){
      			 $("#"+index+"Name").val("");
      			 $("#"+index+"Id").val("");
      		  }   
      	    }
       	function findStaffByName(index) {
       		var name = $("#"+index+"Name").val();
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
       			$("#"+index+"Name").val(shtml.split("（")[0]);
       			$("#"+index+"Flag").val(shtml.split("（")[0]);
       			var agent = $(this).find("input").val();
       			$("#"+index+"Id").val(agent.split("@")[0]);
       			});
       			$("."+index+"_text_down ul").empty();
       	}
       $("#saveRequirementInfo").submit(function(){
		  
    	   var checkFileResult_boolean=(function ($item,maxSize_u32,eachMaxSize){
   			var isPass_boolean=true;
      			var files_arr=$item[0].files;
      			var sumSize_u32=0;
      			var result_arr=_.reduce(files_arr,function (result_arr,value_obj){
      				var fileName_string=value_obj.name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
      				var suffix_string=fileName_string.replace(/.*\.(.*)/,"$1");
      				suffix_string=(suffix_string+"").toLowerCase();
      				result_arr.push([fileName_string,suffix_string]);
      				sumSize_u32+=value_obj.size;
      				if(value_obj.size>eachMaxSize){
      					layer.alert(commonUtils.replace_fn("上传单个文件大小不能超过%sM",eachMaxSize>>>20),function (){
       					layer.closeAll();
       				});
      					return isPass_boolean=false;
      				}
      				if(sumSize_u32>maxSize_u32){
      					layer.alert(commonUtils.replace_fn("上传文件大小不能超过%sM",maxSize_u32>>>20),function (){
       					layer.closeAll();
       				});
      					return isPass_boolean=false;
      				}
      				return  result_arr;
      			},[]);
      			if(isPass_boolean)
      				$('#fileDetail').val(JSON.stringify(result_arr));
      			return isPass_boolean;
      		})($('#attachment'),20<<20,5<<20)
    	   if(!checkFileResult_boolean)return checkFileResult_boolean;
       });
   	function deleteAttachment(target){
		var attachmentName = $(target).prev().text();
		var instanceId = $("input[name='instanceId']").val();
		$.ajax({
			type:'post',
			data:{'attachmentName':attachmentName,'instanceId':instanceId},
			url:'/performance/soft/deleteTaskAttachment',
			success:function(data){
				if(data.flag=="true"){
					$(target).prev().remove();
					$(target).remove();
				}
				//判断div里面附件是否为空，空的话，就清除掉这个div，以便调整布局
				if($("#_attachment").html().split(" ").join("").length<40){
					$("#_attachment").next().remove();
					$("#_attachment").remove();
				}
			}
		});
	}
    function changeName(target){
 	   var item = $(target).find("option:selected").text();
 	   $(target).next().val(item);
    }
  </script>
</body>
</html>