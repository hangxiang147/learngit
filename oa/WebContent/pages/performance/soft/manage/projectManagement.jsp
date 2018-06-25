<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
	.hand{
		cursor:hand;
	}
	.glyphicon-remove:hover{
		color:red;
	}
	.glyphicon:hover{text-decoration:none}
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
      		<s:set name="selectedPanel" value="'showProject'"></s:set>
      		<%@include file="/pages/performance/soft/manage/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">项目管理</h3> 
      			<a class="btn btn-primary"  onclick="goPath('/performance/soft/toEditProject')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span>项目</a>
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-2">项目名称</th>
	                  <th class="col-sm-1">项目代号</th>
	                  <th class="col-sm-1">项目负责人</th>
	                  <th class="col-sm-1">测试负责人</th>
	                  <th class="col-sm-1">最新版本</th>
	                  <th class="col-sm-2">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach items="${projectLst}" var="project" varStatus="index">
	              		<tr>
	              		<td>${project.name}</td>
	              		<td>${project.code}</td>
	              		<td>${project.projectHeaderName}</td>
	              		<td>${project.testHeaderName}</td>
	              		<td>${project.updatestVersion=='null'?'':project.updatestVersion}</td>
	              		<td>
	              			<a class="hand " onclick="showVersionModal('${project.name}',${project.id})">
	              				<svg class="icon" aria-hidden="true" title="添加版本" data-toggle="tooltip">
             						<use xlink:href="#icon-banben"></use>
             					</svg>
	              			</a>
	              			&nbsp;
	              			<a class="hand" onclick="showModuleModal(${project.id})">
	              				<svg class="icon" aria-hidden="true" title="添加模块" data-toggle="tooltip">
             						<use xlink:href="#icon-mokuai"></use>
             					</svg>
	              			</a>
	              			&nbsp;
	              			<a onclick="goPath('/performance/soft/showProjectDetail?projectId=${project.id}')" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
             						<use xlink:href="#icon-Detailedinquiry"></use>
             					</svg>
	              			</a>
	              			&nbsp;
	              			<a onclick="goPath('/performance/soft/toEditProject?projectId=${project.id}')" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="修改项目" data-toggle="tooltip">
             						<use xlink:href="#icon-modify"></use>
             					</svg>
	              			</a>
	              			&nbsp;
	              			<a onclick="goPath('/performance/soft/showVersion?projectId=${project.id}')" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="修改版本" data-toggle="tooltip">
             						<use xlink:href="#icon-banbenxiugai"></use>
             					</svg>
							 </a>
							&nbsp;
							<a onclick="goPath('/performance/soft/showModule?projectId=${project.id}')" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="修改模块" data-toggle="tooltip">
             						<use xlink:href="#icon-b2"></use>
             					</svg>
							</a>
	              			<%-- <a style="cursor:hand" onclick="deleteProject(this)" data-preifx="${project.id}">| 删除</a>	 --%>
	              		</td>
	              		</tr>
	              	</c:forEach>
	              </tbody>
	            </table>
	          </div>
			  </div>
			  <%@include file="/pages/performance/soft/manage/addVersion.jsp" %>
			  <%@include file="/pages/performance/soft/manage/addModule.jsp" %>
      </div>
    </div>
  <script src="/assets/js/textarea/marked.js"></script>
  <script type="text/javascript">
  $(function(){
	  $("[data-toggle='tooltip']").tooltip();
  });
    	function deleteProject(target){
    		layer.open({  
	            content: '确定删除吗？',  
	            btn: ['确认', '取消'],  
	            yes: function() {  
	            	var projectId = $(target).attr("data-preifx");
					window.location.href='/performance/soft/deleteProject?projectId='+projectId;
	            }
	        }); 
    	}
  		function showVersionModal(projectName, projectId){
  			$("#pm").html("");
  			$("#fenXi").html("");
  			$("#developer").html("");
  			$("#tester").html("");
  			$("#shiShi").html("");
  			$.ajax({
  				url:'performance/soft/getPersonsByProjectName',
  				data:{'projectName': projectName},
  				type:'post',
  				success:function(data){
  					var pms = data.pms;
  					var developers = data.developers;
  					var testers = data.testers;
  					var shiShis = data.shiShis;
  					var fenXis = data.fenXis;
  					pms.forEach(function(value, index){
  						$("#pm").append('<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  		    			'<input type="hidden" name="projectVersion.pm" value="'+value.userId+'">'+
  		    			'</span>');
  					});
  					fenXis.forEach(function(value, index){
  						$("#fenXi").append('<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  		    			'<input type="hidden" name="projectVersion.fenXi" value="'+value.userId+'">'+
  		    			'</span>');
  					});
  					developers.forEach(function(value, index){
  						$("#developer").append('<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  		    			'<input type="hidden" name="projectVersion.developer" value="'+value.userId+'">'+
  		    			'</span>');
  					});
  					testers.forEach(function(value, index){
  						$("#tester").append('<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  		    			'<input type="hidden" name="projectVersion.tester" value="'+value.userId+'">'+
  		    			'</span>');
  					});
  					shiShis.forEach(function(value, index){
  						$("#shiShi").append('<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  		    			'<input type="hidden" name="projectVersion.shiShi" value="'+value.userId+'">'+
  		    			'</span>');
  					});
  				}
  			});
  			$("input[name='projectId']").val(projectId);
  			$("#version").modal('show');
  		}
  		function showModuleModal(projectId){
  			$("input[name='projectId']").val(projectId);
  			$("#module").modal('show');
  		}
  </script>
</body>
</html>