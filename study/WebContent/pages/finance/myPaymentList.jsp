<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function () {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
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
        <s:set name="panel" value="'reimbursement'"></s:set>
        <%@include file="/pages/finance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h3 class="sub-header" style="margin-top:0px;">付款申请列表</h3>
       	  
       	    <div class="table-responsive">
		  		<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th>序号</th>
	                  <th>标题</th>
	                  <th>申请时间</th>
	                  <th>付款单号</th>
	                  <th>领款人</th>
	                  <th>合计金额</th>
	                  <th>当前处理人</th>
	                  <th>流程状态</th>
	                  <th>操作</th>
	                </tr>
	              </thead>
             		  <tbody>
	              	<c:if test="${not empty reimbursementVOs}">
	              	<s:set name="reimbursement_id" value="(#request.page-1)*(#request.limit)"></s:set>
	              	<s:iterator id="reimbursementVO" value="#request.reimbursementVOs" status="count">
	              		<tr>
	              			<td class="col-sm-1"><s:property value="#reimbursement_id+1"/></td>
	              			<td class="col-sm-1"><a onclick="goPath('finance/process/processHistory?type=2&processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#reimbursementVO.title"/></a></td>
	              			<td class="col-sm-1"><s:property value="#reimbursementVO.requestDate"/></td>
	              			<td class="col-sm-1"><s:property value="#reimbursementVO.reimbursementNo"/></td>
	              			<td class="col-sm-1"><s:property value="#reimbursementVO.payeeName"/></td>
	              			<td class="col-sm-1"><s:property value="#reimbursementVO.totalAmount"/></td>
	              			<td class="col-sm-1"><s:property value="#reimbursementVO.assigneeUserName"/></td>
	              			<td class="col-sm-1"><s:property value="#reimbursementVO.status"/></td>
	              			<td class="col-sm-1">
	              				<a onclick="goPath('finance/reimbursement/getPaymentDetail?type=5&processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>&selectedPanel=myPaymentList')" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
	              				</a>
	              			</td>
	              		</tr>
	              		<s:set name="reimbursement_id" value="#reimbursement_id+1"></s:set>
	              	</s:iterator>
	              	</c:if>
	              </tbody>
          		</table>
          	</div>
			    <div class="dropdown" style="margin-top:25px;">
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
