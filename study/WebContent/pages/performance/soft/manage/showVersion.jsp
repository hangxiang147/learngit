<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
	.hand{cursor:hand}
	.glyphicon:hover{text-decoration:none}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover{text-decoration:none}
	.delete:hover{color:red}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'showProject'"></s:set>
      		<%@include file="/pages/performance/soft/manage/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">版本信息
      						<button type="button" class="btn btn-default"
							onclick="history.go(-1)"
							style="margin-left: 80%">返回</button></h3> 
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-1">版本名称</th>
	                  <th class="col-sm-1">创建人</th>
	                  <th class="col-sm-2">创建时间</th>
	                  <th class="col-sm-1">开发人数</th>
	                  <th class="col-sm-1">每日工时</th>
	                  <th class="col-sm-1">开始时间</th>
	                  <th class="col-sm-1">结束时间</th>
	                  <th class="col-sm-1">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach items="${versionLst}" var="projectVersion" varStatus="index">
	              		<tr>
	              		<td>${index.index+startIndex}</td>
	              		<td>${projectVersion.version}</td>
	              		<td>${projectVersion.creator}</td>
	              		<td>${projectVersion.createTime}</td>
	              		<td>${projectVersion.developerNum}</td>
	              		<td>${projectVersion.workHour}</td>
	              		<td>${projectVersion.beginDate}</td>
	              		<td>${projectVersion.endDate}</td>
	              		<td>
	              			<c:if test="${projectVersion.status!='已完成'}">
		              			<a class="hand" onclick="showModal('${projectVersion.id}','${projectVersion.projectId}',
		              			'${projectVersion.version}','${projectVersion.developerNum}','${projectVersion.beginDate}',
		              			'${projectVersion.endDate}','${projectVersion.workHour}')">
		              			<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
	             					<use xlink:href="#icon-modify"></use>
	             				</svg>
		              			</a>
		              			&nbsp;
		              			<a class="hand delete" onclick="deleteTask(this)" data-preifx="${projectVersion.id}">
		              			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
	             					<use xlink:href="#icon-delete"></use>
	             				</svg>
		              			</a>
	              			</c:if>
	              			<c:if test="${projectVersion.status=='已完成'}">
	              				<a class="hand" onclick="viewDetail('${projectVersion.id}','${projectVersion.version}','${projectVersion.developerNum}',
	              				'${projectVersion.beginDate}','${projectVersion.endDate}','${projectVersion.workHour}')">
		              			<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
	             					<use xlink:href="#icon-Detailedinquiry"></use>
	             				</svg>
		              			</a>
	              			</c:if>
	              			<input type="hidden" value="${projectVersion.projectId}">
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
           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
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
          <%@include file="/pages/performance/soft/manage/addVersion.jsp" %>
          <%@include file="/pages/performance/soft/manage/showVersionDetail.jsp" %>
		</div>
      </div>
    </div>
  <script src="/assets/js/textarea/marked.js"></script>
  <script src="/assets/js/layer/layer.js"></script>
  <script type="text/javascript">
    	$(function(){
    		set_href();
    		$("[data-toggle='tooltip']").tooltip();
    	});
  	    function deleteTask(target){
  			layer.open({  
  	            content: '确定删除吗？',  
  	            btn: ['确认', '取消'],  
  	          	offset:'100px',
  	            yes: function() {  
  	            	var versionId = $(target).attr("data-preifx");
  	            	var projectId = $(target).next().val();
	  	      		$.ajax({
	  	    			url:'/performance/soft/checkIsCreateTask',
	  	    			type:'post',
	  	    			data:{'byType':'version','versionId':versionId},
	  	    			dataType:'json',
	  	    			success:function (data){
	  	    				if(data.exist=="true"){
	  	    					layer.alert("该版本已分配任务，不可删除！",{offset:'100px'});
	  	    				}else{
	  	    					window.location.href='/performance/soft/deleteVersion?versionId='+versionId+"&projectId="+projectId; 
	  	    					Load.Base.LoadingPic.FullScreenShow(null);
	  	    				}
	  	    			}
	  	    		});
  	            }
  	        }); 
  	    }
  		function showModal(id, projectId, version, developerNum, beginDate, endDate, workHour){
  			$("input[name='projectVersion.version']").val(version);
  			$("input[name='projectVersion.developerNum']").val(developerNum);
  			$("input[name='projectVersion.beginDate']").val(beginDate);
  			$("input[name='projectVersion.endDate']").val(endDate);
  			$("input[name='projectVersion.workHour']").val(workHour);
  			$("input[name='projectVersion.projectId']").val(projectId);
  			$("input[name='projectVersion.id']").val(id);
  			
  			$("#pm").html("");
  			$("#fenXi").html("");
  			$("#developer").html("");
  			$("#tester").html("");
  			$("#shiShi").html("");
  			$.ajax({
  				url:'performance/soft/getPartPersons',
  				data:{'versionId': id},
  				type:'post',
  				success:function(data){
  					var pms = data.pms;
  					var developers = data.developers;
  					var testers = data.testers;
  					var shiShis = data.shiShis;
  					var fenXis = data.fenXis;
  					pms.forEach(function(value, index){
  						if(value.flag){
  	  						$("#pm").append('&nbsp;<span style="display:inline-block;margin-top:3%;border-bottom:1px solid red">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.pm" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}else{
  	  						$("#pm").append('&nbsp;<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.pm" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}
  					});
  					fenXis.forEach(function(value, index){
  						if(value.flag){
  	  						$("#fenXi").append('&nbsp;<span style="display:inline-block;margin-top:3%;border-bottom:1px solid red">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.fenXi" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}else{
  	  						$("#fenXi").append('&nbsp;<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.fenXi" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}
  					});
  					developers.forEach(function(value, index){
  						if(value.flag){
  	  						$("#developer").append('&nbsp;<span style="display:inline-block;margin-top:3%;border-bottom:1px solid red">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.developer" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}else{
  	  						$("#developer").append('&nbsp;<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.developer" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}
  					});
  					testers.forEach(function(value, index){
  						if(value.flag){
  	  						$("#tester").append('&nbsp;<span style="display:inline-block;margin-top:3%;border-bottom:1px solid red">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.tester" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}else{
  	  						$("#tester").append('&nbsp;<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.tester" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}
  					});
  					shiShis.forEach(function(value, index){
  						if(value.flag){
  	  						$("#shiShi").append('&nbsp;<span style="display:inline-block;margin-top:3%;border-bottom:1px solid red">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.shiShi" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}else{
  	  						$("#shiShi").append('&nbsp;<span style="display:inline-block;margin-top:3%">'+value.userName+'&nbsp;<a onclick="deletePerson(this)" class="glyphicon glyphicon-remove hand"></a>'+
  	  	  		    			'<input type="hidden" name="projectVersion.shiShi" value="'+value.userId+'">'+
  	  	  		    			'</span>&nbsp;');
  						}
  					});
  					$("#version").modal('show');
  				}
  			});
  		}
  		function viewDetail(id, version, developerNum, beginDate, endDate, workHour){
  			$("#versionName").text(version);
  			$("#developerNum").text(developerNum);
  			$("#beginDate_").text(beginDate);
  			$("#endDate_").text(endDate);
  			$("#workHour_").text(workHour);
  			
  			$("#pm1").html("");
  			$("#fenXi1").html("");
  			$("#developer1").html("");
  			$("#tester1").html("");
  			$("#shiShi1").html("");
  			$.ajax({
  				url:'performance/soft/getPersonsByVersionId',
  				data:{'versionId': id},
  				type:'post',
  				success:function(data){
  					var pms = data.pms;
  					var developers = data.developers;
  					var testers = data.testers;
  					var shiShis = data.shiShis;
  					var fenXis = data.fenXis;
  					pms.forEach(function(value, index){
  						$("#pm1").append('<span>'+value.lastName+
  		    			'</span>&nbsp;');
  					});
  					fenXis.forEach(function(value, index){
  						$("#fenXi1").append('<span>'+value.lastName+
  		    			'</span>&nbsp;');
  					});
  					developers.forEach(function(value, index){
  						$("#developer1").append('<span>'+value.lastName+
  		    			'</span>&nbsp;');
  					});
  					testers.forEach(function(value, index){
  						$("#tester1").append('<span>'+value.lastName+
  		    			'</span>&nbsp;');
  					});
  					shiShis.forEach(function(value, index){
  						$("#shiShi1").append('<span>'+value.lastName+
  		    			'</span>&nbsp;');
  					});
  					$("#showVersion").modal('show');
  				}
  			});
  		}
  </script>
</body>
</html>