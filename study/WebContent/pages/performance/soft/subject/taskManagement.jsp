<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	.hand{
		cursor:hand;
	}
	table{table-layout:fixed;word-break:break-all;}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover{text-decoration:none}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'showTask'"></s:set>
      		<%@include file="/pages/performance/soft/subject/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">分配任务</h3> 
      			<!-- <a class="btn btn-primary"  href="/performance/soft/singleTask">单个分配</a>&nbsp;&nbsp;
      			<a class="btn btn-primary"  href="/performance/soft/batchTask">批量分配</a>
      			<br>
      			<br> -->
      			<form id="searchTask" action="/performance/soft/showTaskByConditions" class="form-horizontal">
      			<div class="form-group">
      			<label for="project" class="col-sm-1 control-label">所属项目</label>
      			<div id="project" class="col-sm-2">
      				<select class="form-control" required id="selectProject" name="functionVo.project" onchange="changeProject()">
      					  <c:forEach items="${projects}" var="project">
      					  	<option value="${project.id}" <c:if test="${functionVo.project==project.id}">selected="selected"</c:if>>${project.name}</option>
      					  </c:forEach>
					</select>
      			</div>
				<label for="version" class="col-sm-1 control-label">所属版本</label>
      				<div id="version" class="col-sm-2">
      				<select class="form-control" required id="selectVersion" name="functionVo.version">
      					<option value="">请选择</option>
      					<c:forEach items="${versions}" var="version">
      						<option value="${version.id}" <c:if test="${functionVo.version==version.id}">selected="selected"</c:if>>${version.version}</option>
      					</c:forEach>
					</select>
					</div>
				 <label for="module" class="col-sm-1 control-label">所属模块</label>
      				<div id="module" class="col-sm-2">
      				<select class="form-control" required id="selectModule" name="functionVo.module">
      					<option value="">请选择</option>
      					<c:forEach items="${modules}" var="module">
      						<option value="${module.id}" <c:if test="${functionVo.module==module.id}">selected="selected"</c:if>>${module.module}</option>
      					</c:forEach>
					</select>
					</div>
      			</div>
      			<div class="form-group">
					<label for="beginDate" class="col-sm-1 control-label">开始时间</label>
		    		<div class="col-sm-2">
		    			<input type="text" autocomplete="off" class="form-control" id="beginDate" name="functionVo.beginDate" value="${functionVo.beginDate}" 
		    			onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}' })" placeholder="开始时间">
		    		</div>
					<label for="endDate" class="col-sm-1 control-label">结束时间</label>				
		    		<div class="col-sm-2">
		    			<input type="text" autocomplete="off" class="form-control" id="endDate" name="functionVo.endDate" value="${functionVo.endDate}" 
		    			onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}' })" placeholder="结束时间">
		    		</div>
					<label for="isComplete" class="col-sm-1 control-label">是否完成</label>
      				<div id="isComplete" class="col-sm-2">
      				<select class="form-control" required name="functionVo.isComplete">
      						<option value="false" <c:if test="${functionVo.isComplete=='false'}">selected="selected"</c:if>>未完成</option>
      						<option value="true" <c:if test="${functionVo.isComplete=='true'}">selected="selected"</c:if>>已完成</option>
					</select>
					</div>
					<div class="col-sm-1">
					<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
					</div>
      			</div>
      			</form>
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-2">任务名称</th>
	                  <th class="col-sm-1">责任人</th>
	                  <th class="col-sm-2">关联需求</th>
	                  <th class="col-sm-1">分值</th>
	                  <th class="col-sm-2">分配时间</th>
	                  <th class="col-sm-1">当前状态</th>
	                  <th class="col-sm-1">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach items="${taskLst}" var="task" varStatus="index">
	              		<tr>
	              		<td>${index.index+startIndex}</td>
	              		<td>${task.name}</td>
	              		<td>${task.assigner!='null'?task.assigner:'——'}</td>
	              		<td>${task.associatedRequirement}</td>
	              		<td><c:if test="${task.score!='null'}">${task.score}</c:if></td>
	              		<td>${task.addTime!='null'?task.addTime:'——'}</td>
	              		<td>
	              			<c:choose>
	              			<c:when test="${task.result == '6' }">
	              				任务结束   
	              			</c:when>
	              			<c:when test="${task.result == '29' }">
	              				已办结:分值无效
	              			</c:when>
	              			<c:when test="${task.result == '30' }">
	              				已办结:分值有效
	              			</c:when>
	              			<c:when test="${task.result == '31' }">
	              				强制终止
	              			</c:when>
	              			<c:otherwise>
	              				进行中
	              			</c:otherwise>
	              			</c:choose>
	              		</td>
	              		<td>
	              			<c:if test="${task.instanceId != 'null'}">
	              			<a href="javascript:location.href='/performance/soft/showTaskDetail?taskId=${task.id}&instanceId=${task.instanceId}'">
	              			<svg class="icon" aria-hidden="true"  title="查看" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              			</a>
	              			&nbsp;
	              			<c:if test="${task.result ne '6' and task.result ne '29' and task.result ne '30' and task.result ne '31'}">
	              				<a href="javascript:endTask('${task.instanceId }')">
	              				<svg class="icon" aria-hidden="true" title="终止任务" data-toggle="tooltip">
             						<use xlink:href="#icon-zhongzhi"></use>
             					</svg>
	              				</a>
	              			</c:if>
	              			</c:if>
	              			<c:if test="${task.instanceId == 'null'}">——</c:if>
	              		</td>
	              		</tr>
	              	</c:forEach>
	              </tbody>
	            </table>
	          </div>
	        <div class="dropdown">
           	<label>每页显示数量：</label>
           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
           		${limit}
           		<span class="caret"></span>
           	</button>
           	<ul class="dropdown-menu limit" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
			    <li><a class="dropdown-item-20" href="#">20</a></li>
			    <li><a class="dropdown-item-50" href="#">50</a></li>
			    <li><a class="dropdown-item-100" href="#">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
		</div>
      </div>
    </div>
  <script src="/assets/js/textarea/marked.js"></script>
  <script src="/assets/js/layer/layer.js"></script>
  <script type="text/javascript">
    	$(function(){
    		set_href();
    		$("[data-toggle='tooltip']").tooltip();
/*     		$(".pager li a,.limit li a").each(function(){
    			$(this).click(function(){
    				var href = $(this).attr("href");
    				if(href.indexOf("showTaskByConditions")!=-1){
    					href += "&"+$("#searchTask").serialize();
    				}
    				window.location.href = href;
    				return false;
    			});
    		}); */
    	});
  	    function deleteTask(target){
  			layer.open({  
  	            content: '确定删除吗？',  
  	            btn: ['确认', '取消'],  
  	            yes: function() {  
  	            	var taskId = $(target).attr("data-preifx");
  	            	window.location.href='/performance/soft/deleteTask?taskId='+taskId; 
  	            }
  	        }); 
  	    }
    	function changeProject(){
    		var project = $("#selectProject option:selected").val();
    		$.ajax({
    			type:'post',
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
    	function search() {
    		$("#searchTask").submit();
    		Load.Base.LoadingPic.FullScreenShow(null);
    	}
    	function endTask(instanceId){
    		$.post('/performance/soft/endSoftTask?instanceId='+instanceId,function(){
    			location.reload();
    		})
    	}
  </script>
</body>
</html>