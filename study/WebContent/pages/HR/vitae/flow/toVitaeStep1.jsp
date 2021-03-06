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
<script type="text/javascript">
	$(function() {
		set_href();
	});
	
	function search() {
		var params = $("#queryReimbursementForm").serialize();
		window.location.href = "finance/process/findReimbursementList?" + params;
	}
</script>
<style type="text/css">
	
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'job'"></s:set>
        <%@include file="/pages/HR/vitae/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form id="queryReimbursementForm" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">面试开始待办列表</h3>
    
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
                  <th width="10%">应聘人员</th>
                  <th width="10%">联系方式</th>
                  <th width="10%">预期面试时间</th>
                  <th width="15%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty taskVOs}">
              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
              			<td class="col-sm-1"><s:property value="#taskVO.postName"/></td>
              			<td class="col-sm-1"><s:property value="#taskVO.requestDate"/></td>
              			<td class="col-sm-1"><s:property value="#taskVO.itemPersonName"/></td>
              			<td class="col-sm-1"><s:property value="#taskVO.itemPersonTelephone"/></td>
              			<td class="col-sm-1"><s:property value="#taskVO.guessTime"/></td>
              			<td class="col-sm-1">
              			<a href="/HR/vitae/hrConfirm?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=reimbursementList&taskName=<s:property value="#taskVO.taskName"/>&t=<s:property value="#taskVO.itemPersonTelephone"/>&n=<s:property value="#taskVO.itemPersonName"/>">确认信息</a>
              			<a href="/HR/vitae/outOfTime?taskID=<s:property value='#taskVO.taskID'/>&resultId=<s:property value="#taskVO.vitaeResultId"/>&vitaeSignId=<s:property value="#taskVO.vitaeSignId"/>">逾期未到</a>
              			
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
          <script>
          $('table').find("tr:gt(0)").find("td:eq(6)").each(function (){
        	  var value;
        	  if(value=$(this).html()){
        		  $(this).html(value.substring(0,value.length-2))
        	  }
          })
          </script>
          <%@include file="/includes/pager.jsp" %>
          
        </div>
      </div>
    </div>
  </body>
</html>
