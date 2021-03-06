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
      			<h3 class="sub-header" style="margin-top:0px;">模块信息
      						<button type="button" class="btn btn-default"
							onclick="history.go(-1)"
							style="margin-left: 80%">返回</button></h3> 
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-2">模块名称</th>
	                  <th class="col-sm-2">创建人</th>
	                  <th class="col-sm-2">创建时间</th>
	                  <th class="col-sm-2">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach items="${moduleLst}" var="projectModule" varStatus="index">
	              		<tr>
	              		<td>${index.index+startIndex}</td>
	              		<td>${projectModule.module}</td>
	              		<td>${projectModule.creator}</td>
	              		<td>${projectModule.createTime}</td>
	              		<td>
	              			<a class="hand" onclick="showModal('${projectModule.id}','${projectModule.projectId}','${projectModule.module}')">
	              			<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
             					<use xlink:href="#icon-modify"></use>
             				</svg>
	              			</a>
	              			&nbsp;
	              			<a class="hand delete" onclick="deleteModule(this)" data-preifx="${projectModule.id}">
	              			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
             					<use xlink:href="#icon-delete"></use>
             				</svg>
	              			</a>
	              			<input type="hidden" value="${projectModule.projectId}">
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
          <%@include file="/pages/performance/soft/manage/addModule.jsp" %>
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
  	    function deleteModule(target){
  			layer.open({  
  	            content: '确定删除吗？',  
  	            btn: ['确认', '取消'],  
  	            offset:'100px',
  	            yes: function() {  
  	            	var moduleId = $(target).attr("data-preifx");
  	            	var projectId = $(target).next().val();
  	            	window.location.href='/performance/soft/deleteModule?moduleId='+moduleId+"&projectId="+projectId; 
  	            	Load.Base.LoadingPic.FullScreenShow(null);
  	            }
  	        }); 
  	    }
  		function showModal(id, projectId, module){
  			$("input[name='projectModule.module']").val(module);
  			$("input[name='projectModule.projectId']").val(projectId);
  			$("input[name='projectModule.id']").val(id);
  			$(".modal").modal('show');
  		}
  </script>
</body>
</html>