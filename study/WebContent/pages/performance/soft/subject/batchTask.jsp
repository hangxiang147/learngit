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
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<style type="text/css">
	.inputout{position:relative;}
	.text{position:absolute;top:34px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
	.left{
		padding-left:0 !important;
		text-align:left !important;
	}
	.table th{
		text-align:center;
	}
	.center{
		text-align:center;
	}
	.table td{
		vertical-align:middle !important;
	}
	.form-control{
		padding:0 0 !important;
	}
	.hand{
		cursor:hand;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'showTask'"></s:set>
      		<%@include file="/pages/performance/soft/subject/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      		<h3 class="sub-header" style="margin-top:0px;">批量分配任务</h3> 
      		<form id="saveRequirementInfo" class="form-horizontal" enctype="multipart/form-data"
      		 action="/performance/soft/save_saveBatchTask" method="post">
      		<s:token></s:token>
      		<div class="form-group">
      			<label for="project" class="col-sm-1 control-label">所属项目</label>
      			<div id="project" class="col-sm-2">
      				<select class="form-control" required id="selectProject" onchange="changeProject(),changeName(this)">
      					 <c:forEach items="${projects}" var="project">
     					  	<option value="${project.id}">${project.name}</option>
     					  </c:forEach>
					</select>
					<input type="hidden" name="batchTaskVo.projectName" value="${projects[0].name}">
      			</div>
      			<label for="version" class="col-sm-1 control-label">所属版本</label>
      			<div id="version" class="col-sm-2">
      				<select class="form-control" required id="selectVersion" onchange="changeProjectVersion(),changeName(this)">
      					<c:forEach items="${versions}" var="version">
	      					<option value="${version.id}">${version.version}</option>
	      				</c:forEach>
					</select>
					<input type="hidden" name="batchTaskVo.versionName" value="${versions[0].version}">
      			</div>
      		</div>
      		<button type="button" onclick="add()" style="float: right;" class="btn btn-primary">增加</button>
      		<br>
      		<br>
            <table class="table">
              <thead>
                <tr>
                  <th style="width:3%">ID</th>
                  <th style="width:10%">所属模块</th>
                  <th style="width:10%">相关需求</th>
                  <th style="width:10%">任务名称</th>
                  <th style="width:7%">类型</th>
                  <th style="width:10%">指派给</th>
                  <th style="width:7%">工时</th>
                  <th style="width:7%">分值</th>
                  <th style="width:10%">截止时间</th>
                  <th >任务描述</th>
                  <th style="width:7%">优先级</th>
                  <th style="width:5%">操作</th>
                </tr>
                </thead>
              <tbody id="batchTask">
               	  <tr id="task1">
                  	<td class="center">1</td>
                  	<td>
                  		<select class="form-control _module" onchange="changeModule(this),changeName(this)">
                  			<c:forEach items="${modules}" var="module">
      							<option value="${module.id}">${module.module}</option>
      						</c:forEach>
                  		</select>
                  		<input type="hidden" name="batchTaskVo.moduleName" value="${modules[0].module}">
                  	</td>
                  	<td>
                  		<select class="form-control _require" onchange="changeName(this)">
                  			<c:forEach items="${associatedRequires}" var="require">
      							<option value="${require.id}">${require.name}</option>
      						</c:forEach>
                  		</select>
                  		<input type="hidden" name="batchTaskVo.requirementName" value="${associatedRequires[0].name}">
                  	</td>
                  	<td>
                  		<input name="batchTaskVo.name" autocomplete="off" required class="form-control"/>
                  	</td>
                  	<td>
                  		<select class="form-control _taskType">
                  		  <option value="开发">开发</option>
      					  <option value="设计">设计</option>
      					  <option value="测试">测试</option>
      					  <option value="研究">研究</option>
      					  <option value="讨论">讨论</option>
      					  <option value="界面">界面</option>
      					  <option value="事务">事务</option>
      					  <option value="其他">其他</option>
                  		</select>
                  	</td>
                  	<td>
                  		<div class="inputout">
                  		<span class="input_text" onclick="f('1')">
						<input id="1Name" class="form-control" autocomplete="off" required name="batchTaskVo.assignerName"
							value="${batchTaskVo.assignerName}" oninput="findStaffByName('1')" onblur="clears('1')"/>
				    	<input type="hidden" id="1Id" value="${batchTaskVo.assignerId}" name="batchTaskVo.assignerId" />
				    	<input type="hidden" id="1Flag" value="${batchTaskVo.assignerName}"/>
				    	</span>
				    	<div class="1_text_down text">
							<ul></ul>
						</div>
						</div>
                  	</td>
                  	<td>
                  		<input name="batchTaskVo.estimatedTime" autocomplete="off" min=0 type="number" placeholder="小时" class="form-control"/>
                  	</td>
                  	<td>
                  		<input name="batchTaskVo.score" autocomplete="off" required min=0 max=100 type="number" class="form-control"/>
                  	</td>
                  	<td style="width:10%">
                  		<input type="text" autocomplete="off" class="form-control" name="batchTaskVo.deadLine"  
			    		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })" placeholder="截止时间">
                  	</td>
                  	<td>
                  		<textarea name="batchTaskVo.description" class="form-control"></textarea>
                  	</td>
                  	<td>
                  		<select class="form-control priority" required>
      					  	<option value="低">低</option>
      					  	<option value="中">中</option>
      					  	<option value="高">高</option>
      					  	<option value="加急">加急</option>
						</select>
                  	</td>
                  	<td class="center hand"></td>
                  	<td style="display:none">
                  		<input name="batchTaskVo.moduleId"/>
                  		<input name="batchTaskVo.requirementId"/>
                  		<input name="batchTaskVo.taskType"/>
                  		<input name="batchTaskVo.priority"/>
                  	</td>
                  </tr>
              </tbody>
            </table>
            <input type="hidden" name="batchTaskVo.projectId"/>
            <input type="hidden" name="batchTaskVo.versionId"/>
            <br>
           	<div class="center">
			    <button type="submit" class="btn btn-primary" style="margin-left:2%">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:2%">返回</button>
			</div>
			</form>
			</div>
      </div>
    </div>
	<script type="text/javascript">
		$(function(){
        	$(document).click(function(){
            	$(".text ul").empty();
            });  
		});
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
    	function changeProject(){
    		var project = $("#selectProject option:selected").val();
    		$.ajax({
    			type:'post',
    			data:{'project':project},
    			url:'/performance/soft/changeProjectForTask',
    			success:function(data){
    				var html = '';
    				var versions = data.versions;
    				var modules = data.modules;
    				var associatedRequires = data.associatedRequires;
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
    				for(var i=0; i<modules.length; i++){
    					html += '<option value="'+modules[i].id+'">'+modules[i].module+'</option>';
    				}
    				$("._module").html(html);
    				if(modules.length>0){
    					$("._module").next().val(modules[0].module);
    				}else{
    					$("._module").next().val('');
    				}
    				html = '<option value="sameUp">同上</option>'+html;
    				$(".module").each(function(){
    					$(this).html(html);
        				if(modules.length>0){
        					$(this).next().val(modules[0].module);
        				}else{
        					$(this).next().val('');
        				}
    				});
    				html = '';
    				if(associatedRequires==undefined){
    					$("._require").html(html);
        				$(".require").each(function(){
        					html = '<option value="sameUp">同上</option>'+html;
        					$(this).html(html);
            				$(this).next().val('');
        				});
    					return;
    				}
    				for(var i=0; i<associatedRequires.length; i++){
    					html += '<option value="'+associatedRequires[i].id+'">'+associatedRequires[i].name+'</option>';
    				}
    				$("._require").html(html);
    				if(associatedRequires.length>0){
    					$("._require").next().val(associatedRequires[0].name);
    				}else{
    					$("._require").next().val('');
    				}
    				html = '<option value="sameUp">同上</option>'+html;
    				$(".require").each(function(){
    					$(this).html(html);
    					if(associatedRequires.length>0){
        					$(this).next().val(associatedRequires[0].name);
        				}else{
        					$(this).next().val('');
        				}
    				});
    				
    				
    			}
    		});
    	}
    	var index = 1;
    	function add(){
    		index++;
    		var projectId = $("#selectProject option:selected").val();
    		var versionId = $("#selectVersion option:selected").val();
    		var modules;
    		var associatedRequires;
    		$.ajax({
    			type:'post',
    			data:{'projectId':projectId,'versionId':versionId},
    			async: false,
    			url:'/performance/soft/getModulesAndRequires',
    			success:function(data){
    				modules = data.modules;
    				associatedRequires = data.associatedRequires;
    			}
    		});
    		var moduleOptions = '<option value="sameUp">同上</option>';
    		var requireOptions = '<option value="sameUp">同上</option>';
    		for(var i=0; i<modules.length; i++){
    			moduleOptions += '<option value="'+modules[i].id+'">'+modules[i].module+'</option>';
    		}
    		for(var i=0; i<associatedRequires.length; i++){
    			requireOptions += '<option value="'+associatedRequires[i].id+'">'+associatedRequires[i].name+'</option>';
    		}
    		var initModule = modules.length>0 ? modules[0].module:"";
    		var initRequire = associatedRequires.length>0 ? associatedRequires[0].name:"";
    		$("#batchTask").append('<tr id=task'+index+'>'+
                  				   '<td class="center">'+index+'</td><td>'+
                  				   '<select class="form-control module" onchange="changeModule(this),changeName(this)">'+moduleOptions+'</select>'+
                  				   '<input type="hidden" name="batchTaskVo.moduleName" value="'+initModule+'"></td><td>'+
                  				   '<select class="form-control require" onchange="changeName(this)">'+requireOptions+'</select>'+
                  				   '<input type="hidden" name="batchTaskVo.requirementName" value="'+initRequire+'"></td><td>'+
                  				   '<input name="batchTaskVo.name" autocomplete="off" class="form-control"/></td><td>'+
                  				   '<select class="form-control taskType">'+
                  				 	'<option value="sameUp">同上</option>'+
                  		  		   	'<option value="设计">设计</option>'+
                  		  			'<option value="开发">开发</option>'+
                  		  			'<option value="测试">测试</option>'+
                  		  			'<option value="研究">研究</option>'+
			                  		'<option value="讨论">讨论</option>'+
			                  		'<option value="界面">界面</option>'+
			                  		'<option value="事务">事务</option>'+
			                  		'<option value="其他">其他</option>'+
                  					'</select></td><td>'+
                  					'<div class="inputout">'+
                  					'<span class="input_text" onclick="f(\''+index+'\')">'+
									'<input id="'+index+'Name" class="form-control" autocomplete="off" required name="batchTaskVo.assignerName"'+
									'value="${batchTaskVo.assignerName}" oninput="findStaffByName(\''+index+'\')" onblur="clears(\''+index+'\')"/>'+
				    				'<input type="hidden" id="'+index+'Id" value="${batchTaskVo.assignerId}" name="batchTaskVo.assignerId" />'+
				    				'<input type="hidden" id="'+index+'Flag" value="${batchTaskVo.assignerName}"/>'+
				    				'</span><div class="'+index+'_text_down text"><ul></ul></div></div></td><td>'+
                  					'<input name="batchTaskVo.estimatedTime" autocomplete="off" type="number" min=0 placeholder="小时" class="form-control"/>'+
                  					'</td><td>'+
                              		'<input name="batchTaskVo.score" autocomplete="off" required min=0 max=100 type="number" class="form-control"/>'+
                                    '</td>'+
                                  	'<td style="width:10%">'+
                              		'<input type="text" autocomplete="off" class="form-control" name="batchTaskVo.deadLine"'+  
            			    		'onclick="WdatePicker({ dateFmt: \'yyyy-MM-dd\' })" placeholder="截止时间"></td>'+
                  					'<td><textarea name="batchTaskVo.description" class="form-control">'+
									'</textarea></td><td>'+
                  					'<select class="form-control priority" required">'+
      					  			'<option value="低">低</option>'+
      					  			'<option value="中">中</option>'+
      					  			'<option value="高">高</option>'+
      					  			'<option value="加急">加急</option>'+
									'</select></td>'+
                  					'<td class="center hand"><a onclick="delRow('+index+')">删除</a></td>'+
                  					'<td style="display:none">'+
                  					'<input name="batchTaskVo.moduleId"/>'+
                  					'<input name="batchTaskVo.requirementId"/>'+
                  					'<input name="batchTaskVo.taskType"/>'+
                  					'<input name="batchTaskVo.priority"/></td></tr>');
    	}
    	function delRow(i){
    		$("#task"+i).remove();
    	}
    	function changeProjectVersion(){
    		var versionId = $("#selectVersion option:selected").val();
    		var moduleId = $("._module option:selected").val();
    		$.ajax({
    			type:'post',
    			data:{'versionId':versionId,'moduleId':moduleId},
    			url:'/performance/soft/changeProjectVersion',
    			success:function(data){
    				var associatedRequires = data.associatedRequires;
    				var associatedOptions = '';
    				for(var i=0; i<associatedRequires.length; i++){
    					associatedOptions += '<option value="'+associatedRequires[i].id+'">'+associatedRequires[i].name+'</option>';
    				}
    				$("._require").html(associatedOptions);
    				if(associatedRequires.length>0){
    					$("._require").next().val(associatedRequires[0].name);
    				}else{
    					$("._require").next().val('');
    				}
    				associatedOptions = '<option value="sameUp">同上</option>'+associatedOptions;
    				$(".require").each(function(){
    					$(this).html(associatedOptions);
    					if(associatedRequires.length>0){
        					$(this).next().val(associatedRequires[0].name);
        				}else{
        					$(this).next().val('');
        				}
    				});
    			}
    		});
    	}
    	function changeModule(target){
    		var moduleId = $(target).find("option:selected").val();
    		if("sameUp"==moduleId){
    			return;
    		}
    		var versionId = $("#selectVersion option:selected").val();
    		$.ajax({
    			type:'post',
    			data:{'versionId':versionId,'moduleId':moduleId},
    			url:'/performance/soft/changeProjectVersion',
    			success:function(data){
    				var associatedRequires = data.associatedRequires;
    				var associatedOptions = '';
    				for(var i=0; i<associatedRequires.length; i++){
    					associatedOptions += '<option value="'+associatedRequires[i].id+'">'+associatedRequires[i].name+'</option>';
    				}
    				var cs = $(target).attr("class");
    				if(cs.indexOf("_module")!=-1){
    					$(target).parent().next().find("select").html(associatedOptions);
        				if(associatedRequires.length>0){
        					$("._module").parent().next().find("select").next().val(associatedRequires[0].name);
        				}else{
        					$("._module").parent().next().find("select").next().val('');
        				}
    				}else{
    					associatedOptions = '<option value="sameUp">同上</option>'+associatedOptions;
    					$(target).parent().next().find("select").html(associatedOptions);
    					if(associatedRequires.length>0){
    						$(target).parent().next().find("select").next().val(associatedRequires[0].name);
        				}else{
        					$(target).parent().next().find("select").next().val('');
        				}
    				}
    			}
    		});
    	}
      	$("#saveRequirementInfo").submit(function(){
      		var moduleIds = [];
      		var requirementIds = [];
      		var taskTypes = [];
      		var prioritys = [];
    		//第一行的数据
    		var moduleId = $("._module").find("option:selected").val();
    		moduleIds.push(moduleId);
    		var requirementId = $("._require").find("option:selected").val();
    		requirementIds.push(requirementId);
    		var taskType = $("._taskType").find("option:selected").val();
    		taskTypes.push(taskType);
    		var upModuleId = moduleId;
    		$(".module").each(function(){
    			var _moduleId = $(this).find("option:selected").val();
    			//判断是否同上
    			if("sameUp"==_moduleId){
    				moduleIds.push(upModuleId);
    			}else{
    				moduleIds.push(_moduleId);
    				upModuleId = _moduleId;
    			}
    		});
    		var upRequireId = requirementId;
    		$(".require").each(function(){
    			var _upRequireId = $(this).find("option:selected").val();
    			//判断是否同上
    			if("sameUp"==_upRequireId){
    				requirementIds.push(upRequireId);
    			}else{
    				requirementIds.push(_upRequireId);
    				upRequireId = _upRequireId;
    			}
    		});
    		var upTaskType = taskType;
    		$(".taskType").each(function(){
    			var _taskType = $(this).find("option:selected").val();
    			//判断是否同上
    			if("sameUp"==_taskType){
    				taskTypes.push(upTaskType);
    			}else{
    				taskTypes.push(_taskType);
    				upTaskType = _taskType;
    			}
    		});
    		$(".priority").each(function(){
    			prioritys.push($(this).find("option:selected").val());
    		});
    		setValueToInput("batchTaskVo.moduleId", moduleIds);
    		setValueToInput("batchTaskVo.requirementId", requirementIds);
    		setValueToInput("batchTaskVo.taskType", taskTypes);
    		setValueToInput("batchTaskVo.priority", prioritys);
    		
    		$("input[name='batchTaskVo.projectId']").val($("#selectProject option:selected").val());
    		$("input[name='batchTaskVo.versionId']").val($("#selectVersion option:selected").val());
    	});
      	function setValueToInput(name, values){
      		var index = 0;
    		$("input[name='"+name+"']").each(function(){
    			$(this).val(values[index]);
    			index++;
    		});
      	}
      	function changeName(target){
     	   var item = $(target).find("option:selected").text();
     	   $(target).next().val(item);
        }
  </script>
</body>
</html>