<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	.hand {
		cursor: hand;
	}
	table{table-layout:fixed;word-break:break-all;}
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
			<s:set name="selectedPanel" value="'showPreparedRequirement'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">录入需求</h3>
				<div>
				<a class="btn btn-primary" onclick="goPath('/performance/soft/toEditPreparedRequirement')"  href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span>需求</a>
				</div>
				<br>
				<form class="form-horizontal" action="/performance/soft/showPreparedRequirement" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
						<div class="form-group">
						<label class="col-sm-1 control-label">优先级</label>
						<div class="col-sm-2">
							<select class="form-control" name="priority">
							<option <c:if test="${priority==''}">selected="selected"</c:if> value="">全部</option>
							<option <c:if test="${priority=='低'}">selected="selected"</c:if> value="低">低</option>
      					  	<option <c:if test="${priority=='中'}">selected="selected"</c:if> value="中">中</option>
      					  	<option <c:if test="${priority=='高'}">selected="selected"</c:if> value="高">高</option>
      					  	<option <c:if test="${priority=='加急'}">selected="selected"</c:if> value="加急">加急</option>
							</select>
						</div>
						<label class="col-sm-1 control-label">项目</label>
						<div class="col-sm-2">
							<select class="form-control" name="projectId">
								<option <c:if test="${projectId==''}">selected="selected"</c:if> value="">全部</option>
								<c:forEach items="${projects}" var="project">
	      					  	<option <c:if test="${projectId==project.id}">selected="selected"</c:if> value="${project.id}">${project.name}</option>
	      					 	</c:forEach>
							</select>
						</div>
						<label class="col-sm-1 control-label">需求名称</label>
						<div class="col-sm-2">
							<input class="form-control" name="requirementName" autoComplete="off" value="${requirementName}">
						</div>
						<button type="submit" class="btn btn-primary">查询</button>
						</div>
				</form>
				<div>
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-1">编号</th>
	                  <th class="col-sm-1">P</th>
	                  <th class="col-sm-1">项目</th>
	                  <th class="col-sm-2">需求名称</th>
	                  <th class="col-sm-1">版本</th>
	                  <th class="col-sm-1">模块</th>
	                  <th class="col-sm-1">创建人</th>
	                  <th class="col-sm-2">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	  <c:forEach items="${requirementLst}" var="require" varStatus="index">
	              		<tr>
	              		<td>
	              		${index.index+startIndex}
	              		<c:if test="${require.back==1}">
	              			<span class="glyphicon glyphicon-star" data-toggle="tooltip" title="${require.returnReason}"></span>
	              		</c:if>
	              		</td>
	              		<td>${require.id}</td>
	              		<td>${require.priority}</td>
	              		<td>${require.projectName}</td>
	              		<td>${hf:format(require.name)}</td>
	              		<td>
	              			<c:if test="${require.version!='null'}">
	              				${require.version}
	              			</c:if>
	              			<c:if test="${require.version=='null'}">
	              				<span class="glyphicon glyphicon-pencil hand" onclick="toSelectVersion(this,'${require.project}')"></span>
	              			</c:if>
	              		</td>
	              		<td>
	              			<c:if test="${require.module!='null'}">
	              				${require.module}
	              			</c:if>
	              			<c:if test="${require.module=='null'}">
	              				<span class="glyphicon glyphicon-pencil hand" onclick="toSelectModule(this,'${require.project}')"></span>
	              			</c:if>
	              		</td>
	              		<td>${require.creator}</td>
	              		<td>
	              			<a class="hand" onclick="saveVersionOrModule(this,'${require.id}')">
	              			<svg class="icon" aria-hidden="true" title="保存" data-toggle="tooltip">
             					<use xlink:href="#icon-unie602"></use>
             				</svg>
	              			</a>
	              			&nbsp;
	              			<a onclick="goPath('/performance/soft/showRequirementDetail?requirementId=${require.id}&fromPreparedRequire=1')" href="javascript:void(0)">
	              			<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              			</a>
	              			&nbsp;
		              		<a onclick="goPath('/performance/soft/toEditPreparedRequirement?requirementId=${require.id}')" href="javascript:void(0)">
		              		<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
             					<use xlink:href="#icon-modify"></use>
             				</svg>
		              		</a>
		              		&nbsp;
		              		<a style="cursor:hand" class="delete" onclick="deleteRequirement('${require.id}')">
		              		<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
             					<use xlink:href="#icon-delete"></use>
             				</svg>
		              		</a>
		              		&nbsp;
		              		<a style="cursor:hand" onclick="returnRequirement('${require.id}')">
		              		<svg class="icon" aria-hidden="true" data-toggle="tooltip" title="驳回">
             					<use xlink:href="#icon-bohui"></use>
             				</svg>
		              		</a>
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
			    <li><a class="dropdown-item-20 hand" onclick="changeLimit(this)">20</a></li>
			    <li><a class="dropdown-item-50 hand" onclick="changeLimit(this)">50</a></li>
			    <li><a class="dropdown-item-100 hand" onclick="changeLimit(this)">100</a></li>
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
	</div>
	<div id="return" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">驳回</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-2 control-label">原因</label>
					<div id="name" class="col-sm-9">
						<textarea class="form-control" required="required" name="returnReason" style="height:100px"></textarea>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="saveReturnReason()" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		<input type="hidden" name="requirementId"/>
		</form>
	</div>
</div>
	<script src="/assets/js/textarea/marked.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
		function toSelectVersion(target, projectId){
    		$.ajax({
    			type:'post',
    			data:{'projectId':projectId},
    			url:'/performance/soft/getVersionList',
    			success:function(data){
    				var select = '<select class="form-control">';
    				var versionLst = data.versionLst;
    				var options = '';
    				for(var i=0; i<versionLst.length; i++){
    					options += '<option value="'+versionLst[i].id+'">'+versionLst[i].version+'</option>';
    				}
    				select += options;
    				select += '</select>';
    				$(target).parent().html(select);
    			}
    		});
		}
		function toSelectModule(target, projectId){
    		$.ajax({
    			type:'post',
    			data:{'projectId':projectId},
    			url:'/performance/soft/getModuleList',
    			success:function(data){
    				var select = '<select class="form-control">';
    				var moduleLst = data.moduleLst;
    				var options = '';
    				for(var i=0; i<moduleLst.length; i++){
    					options += '<option value="'+moduleLst[i].id+'">'+moduleLst[i].module+'</option>';
    				}
    				select += options;
    				select += '</select>';
    				$(target).parent().html(select);
    			}
    		});
		}
		function saveVersionOrModule(target, id){
			var version = $(target).parent().prev().prev().prev().find("option:selected").val();
			var module = $(target).parent().prev().prev().find("option:selected").val();
			//未激活版本或模块
			if(version==undefined && module==undefined){
				return;
			}
			var _version = '';
			var _module = '';
			if(version!=undefined){
				_version = version;
			}
			if(module!=undefined){
				_module = module;
			}
    		$.ajax({
    			type:'post',
    			data:{'versionId':_version,'moduleId':_module,'requireId':id},
    			url:'/performance/soft/saveVersionOrModule',
    			success:function(data){
    				window.location.href="/performance/soft/showPreparedRequirement?priority=${priority}&projectId=${projectId}&requirementName=${requirementName}&limit=${limit}&page=${page}";
    				Load.Base.LoadingPic.FullScreenShow(null);
    			}
    		});
		}
	    function deleteRequirement(requirementId){
			layer.open({  
	            content: '确定删除吗？',  
	            btn: ['确认', '取消'],  
	            offset: '100px',
	            yes: function() {  
	  	    		window.location.href='/performance/soft/deletePreparedRequirement?requirementId='+requirementId; 
	  	    		Load.Base.LoadingPic.FullScreenShow(null);
	            }
	        }); 
	    }

	    $(function (){
	    	set_href();
	    	$("[data-toggle='tooltip']").tooltip();
	    })
	    function returnRequirement(requirementId){
	    	$("input[name='requirementId']").val(requirementId);
	    	$("#return").modal("show");
	    }
		function saveReturnReason(){
			var $form = $("#return form");
    		$.ajax({
    			type:'post',
    			data:$form.serialize(),
    			url:'/performance/soft/saveReturnReason',
    			success:function(data){
    				$("#return").modal("hide");
    				window.location.href="/performance/soft/showPreparedRequirement?priority=${priority}&projectId=${projectId}&requirementName=${requirementName}&limit=${limit}&page=${page}";
    				Load.Base.LoadingPic.FullScreenShow(null);
    			}
    		});
		}
  	</script>
</body>
</html>