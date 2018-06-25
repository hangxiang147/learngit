<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<meta http-equiv="expires" content="0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'job'"></s:set>
        <%@include file="/pages/HR/vitae/panel.jsp" %>	
              	      	<s:set name="selectedPanel" value="'toVitaeStep4'"></s:set>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form id="queryReimbursementForm" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">确认入职待办列表</h3>
    
		 </form>
			
		  <h3 class="sub-header"></h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th width="10%">序号</th>
                  <th width="15%">标题</th>
                  <th width="10%">岗位名称</th>
                  <th width="10%">发起时间</th>
                  <th width="10%">招聘人员</th>
                  <th width="10%">联系方式</th>
                  <th width="25%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty taskVOs}">
              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.postName"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.itemPersonName"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.itemPersonTelephone"/></td>
              			<td class="col-sm-1">
              			<a href="javascript:confirmrz('/HR/vitae/confirmRZ?taskID=<s:property value='#taskVO.taskID'/>&resultId=<s:property value="#taskVO.vitaeResultId"/>&vitaeSignId=<s:property value="#taskVO.vitaeSignId"/>')">确认入职</a></br>
              			<a href="javascript:confirmwrz('/HR/vitae/confirmRZ_?taskID=<s:property value='#taskVO.taskID'/>&resultId=<s:property value="#taskVO.vitaeResultId"/>&vitaeSignId=<s:property value="#taskVO.vitaeSignId"/>')">未入职</a></br>
              			<a href="javascript:collectMsg('<s:property value='#taskVO.taskID'/>','<s:property value="#taskVO.vitaeResultId"/>','<s:property value="#taskVO.vitaeSignId"/>')">发起复试</a>
              			</td>
              		</tr>
              		<s:set name="task_id" value="#task_id+1"></s:set>
              	</s:iterator>
              	</c:if>
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
        </div>
      </div>
    </div>
	<script src="/assets/js/underscore-min.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
    <script>
var collectMsg=function (taskId,resultId,signId){
	layer.open({
		  type: 2,
		  title: '复试信息确认',
		  shadeClose: true,
		  shade: 0.8,
		  area: ['550px', '68%'],
		  content: "/HR/vitae/toCollectRetrailMsg?taskId="+taskId+"&signId="+signId+"&resultId="+resultId,
		  btn: ['确认', '取消'],
			yes:function(){
				var iframeWin = window['layui-layer-iframe1'];
				iframeWin.sub();
			}
		});
}
    </script>
    
    <script type="text/javascript">
	$(function() {
		set_href();
	});
	
	var confirmrz=function(url){
		layer.confirm('是否确认已入职？', {
  		  btn: ['确认','取消']
  		},function(){
  			location.href=url;
  		});
	}
	var confirmwrz=function (url){
		layer.confirm('是否确认未入职？', {
	  		  btn: ['确认','取消']
	  		},function(){
	  			location.href=url;
	  		});
	}
	function search() {
		var params = $("#queryReimbursementForm").serialize();
		window.location.href = "finance/process/findReimbursementList?" + params;
	}
</script>
  </body>
</html>
