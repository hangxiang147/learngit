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
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	
	function search() {
		var params = $("#queryReimbursementForm").serialize();
		window.location.href = "finance/process/findAdvanceList?" + params;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'audit'"></s:set>
        <%@include file="/pages/finance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form id="queryReimbursementForm" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">申请列表</h3>
       	  <div class="form-group">
				<label for="reimbursementNo" class="col-sm-1 control-label">预付单号</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="reimbursementNo" name="reimbursementNo" value="${reimbursementNo }" >
			    </div>
			    <label for="beginDate" class="col-sm-1 control-label">发起时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="beginDate" value="${beginDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endDate" value="${endDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			    <div class="col-sm-2" style="padding-top:2px;">
			    	<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
			    </div>
		  </div>
		 </form>
			
		  <h3 class="sub-header"></h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>标题</th>
                  <th>预付单号</th>
                  <th>合计金额</th>
                  <th>发起人</th>
                  <th>发起时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty taskVOs}">
              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.reimbursementNo"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.totalAmount" /></td>
              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
              			<td class="col-sm-1">
              			<a onclick="goPath('finance/process/auditAdvance?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=reimbursementList&taskName=<s:property value="#taskVO.taskName"/>')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
    						<use xlink:href="#icon-shenpi"></use>
						</svg>
              			</a>
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
  </body>
</html>
