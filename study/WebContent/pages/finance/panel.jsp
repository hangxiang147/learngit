<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'reimbursement'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <auth:hasPermission name="reimbursement">
	  <li <c:if test="${selectedPanel == 'newReimbursement' }">class="active"</c:if>><a onclick="goPath('finance/process/showReimbursementDiagram')" href="javascript:void(0)">费用报销申请</a></li>
	  <li <c:if test="${selectedPanel == 'myReimbursementList' }">class="active"</c:if>><a onclick="goPath('finance/process/myReimbursementList')" href="javascript:void(0)">我的报销</a></li>
  	  </auth:hasPermission>
  	  <li <c:if test="${selectedPanel == 'newAdvance' }">class="active"</c:if>><a onclick="goPath('finance/process/showAdvanceDiagram')" href="javascript:void(0)">预约付款申请</a></li>
	  <li <c:if test="${selectedPanel == 'myAdvanceList' }">class="active"</c:if>><a onclick="goPath('finance/process/myAdvanceList')" href="javascript:void(0)">我的预约付款</a></li>
	  <li <c:if test="${selectedPanel == 'newPayment' }">class="active"</c:if>><a onclick="goPath('finance/process/showPaymentDiagram')" href="javascript:void(0)">付款申请</a></li>
	  <li <c:if test="${selectedPanel == 'myPaymentList' }">class="active"</c:if>><a onclick="goPath('finance/process/myPaymentList')" href="javascript:void(0)">我的付款</a></li>
	</ul>
</div> 

<%-- <div class="col-sm-3 col-md-2 sidebar"  <c:if test="${panel ne 'reimbursementManagement' }">style="display:none;"</c:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'findReimbursementList' }">class="active"</c:if>><a onclick="goPath('finance/reimbursement/findReimbursementList')" href="javascript:void(0)">报销管理</a></li>
	  <li <c:if test="${selectedPanel == 'findAdvanceList' }">class="active"</c:if>><a onclick="goPath('finance/reimbursement/findAdvanceList')" href="javascript:void(0)">预付管理</a></li>	
	  <li <c:if test="${selectedPanel == 'findPaymentList' }">class="active"</c:if>><a onclick="goPath('finance/reimbursement/findPaymentList')" href="javascript:void(0)">付款管理</a></li>	
	</ul>
</div> 

<div class="col-sm-3 col-md-2 sidebar"  <c:if test="${panel ne 'all' }">style="display:none;"</c:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'findReimbursementList' }">class="active"</c:if>><a onclick="goPath('/finance/process/findReimbursementListAll')" href="javascript:void(0)">报销管理</a></li>
	  <li <c:if test="${selectedPanel == 'findAdvanceList' }">class="active"</c:if>><a onclick="goPath('/finance/process/findAdvanceListAll')" href="javascript:void(0)">预付管理</a></li>	
	  <li <c:if test="${selectedPanel == 'findPaymentList' }">class="active"</c:if>><a onclick="goPath('/finance/process/findPaymentListAll')" href="javascript:void(0)">付款管理</a></li>	
	</ul>
</div> --%>

<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  -->
<div class="col-sm-3 col-md-2 sidebar"  <c:if test="${panel ne 'paymentOfRefund' }">style="display:none;"</c:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'paymentOfRefund' }">class="active"</c:if>><a onclick="goPath('/finance/reimbursement/paymentOfRefund')" href="javascript:void(0)">报销单管理</a></li>
	  <li <c:if test="${selectedPanel == 'prepaidManagement' }">class="active"</c:if>><a onclick="goPath('/finance/reimbursement/prepaidManagement')" href="javascript:void(0)">预付单管理</a></li>	
	  <li <c:if test="${selectedPanel == 'paymentOrderManagement' }">class="active"</c:if>><a onclick="goPath('/finance/reimbursement/paymentOrderManagement')" href="javascript:void(0)">付款单管理</a></li>	
	</ul>
</div>
<!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  -->

<div class="col-sm-3 col-md-2 sidebar" <c:if test="${panel ne 'myHistoryProcess' }">style="display:none;"</c:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'ReimbursementList' }">class="active"</c:if>><a onclick="goPath('administration/process/findUserReimbursementList')" href="javascript:void(0)">我的报销审批历史</a></li>
	  <li <c:if test="${selectedPanel == 'AdvanceList' }">class="active"</c:if>><a onclick="goPath('administration/process/findUserAdvanceList')" href="javascript:void(0)">我的预付审批历史</a></li>
	  <li <c:if test="${selectedPanel == 'paymentList' }">class="active"</c:if>><a onclick="goPath('administration/process/findUserPaymentList')" href="javascript:void(0)">我的付款审批历史</a></li>
	</ul>
</div> 

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'audit'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'reimbursementList' }">class="active"</c:if>><a onclick="goPath('finance/process/findReimbursementList')" href="javascript:void(0)">报销申请   <span class="badge" style="background-color:red;<c:if test='${reimbursementCount == 0 }'>display:none;</c:if>">${reimbursementCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'advanceList' }">class="active"</c:if>><a onclick="goPath('finance/process/findAdvanceList')" href="javascript:void(0)">预付申请   <span class="badge" style="background-color:red;<c:if test='${advanceCount == 0 }'>display:none;</c:if>">${advanceCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'paymentList' }">class="active"</c:if>><a onclick="goPath('finance/process/findPaymentList')" href="javascript:void(0)">付款申请   <span class="badge" style="background-color:red;<c:if test='${paymentCount == 0 }'>display:none;</c:if>">${paymentCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'socialSecurityList' }">class="active"</c:if>><a onclick="goPath('finance/process/findSocialSecurityList')" href="javascript:void(0)">社保缴纳申请   <span class="badge" style="background-color:red;<c:if test='${socialSecurityCount == 0 }'>display:none;</c:if>">${socialSecurityCount }</span></a></li>
	</ul>
</div> 