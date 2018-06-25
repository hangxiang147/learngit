<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
	<style type="text/css">
		.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
		.tab table{width:100%;border-collapse:collapse;}
		.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
		.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
		.tab table tr .title {text-align:center;color:#000;width:15%}
		.bold{font-weight:bold}
		table{word-break:break-all !important;}
		._title{text-align:center;background:#efefef;}
		.shopPayApply td{text-align:center;}
		.col_blue{color:#428bca}
		.col_blue span{color:#111;font-size:16px}
		.underline{text-decoration:underline}
	</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<c:if test="${from=='approvaRecord'}">
      		 	<s:set name="panel" value="'approvaRecord'"></s:set>
      	</c:if>
      	<c:if test="${from!='approvaRecord'}">
      		 	<s:set name="panel" value="'application'"></s:set>
        		<s:set name="selectedPanel" value="'myProcessList'"></s:set>
      	</c:if>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<c:if test="${businessType=='ChangeContract'}">
        			<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">合同变更或解除申请
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">所属部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${changeContractVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${changeContractVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">合同基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">合同名称</td>
        	  			<td colspan="5">${changeContractVo.contractName}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">合同编号</td>
						<td colspan="3">${changeContractVo.contractId}</td>
						<td class="title">签订时间</td>
						<td>${changeContractVo.signDate}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">合同内容</td>
        	  			<td colspan="5">${changeContractVo.contractContent}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">合同已履行情况介绍</td>
        	  			<td colspan="5">${changeContractVo.contractDescription}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="3">变更合同</td>
        	  			<td class="title">变更合同原因</td>
        	  			<td colspan="4">${changeContractVo.changeReason}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更前内容</td>
        	  			<td colspan="4">${changeContractVo.beforeChangeContent}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更后内容</td>
        	  			<td colspan="4">${changeContractVo.afterChangeContent}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">解除合同原因</td>
        	  			<td colspan="4">${changeContractVo.relieveReason}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${null != supervisor}">
        	  		<tr>
        	  			<td rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${null == supervisor}">
        	  		<tr>
        	  			<td rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='ContractSign'}">
        			<div id="printArea">
       	  	        	<h4 style="text-align:center" class="bold">合同审批表
       					<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div style="text-align:right;margin-right:20px">合同编号：<span class="underline">${contractSignVo.contractId}</span></div>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">主办部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">拟稿人</td>
        	  			<td>${contractSignVo.userName}</td>
        	  			<td class="title">提交日期</td>
        	  			<td style="width:16%">${contractSignVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">合同基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">合同名称</td>
        	  			<td colspan="5">${contractSignVo.contractName}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">对方公司名称</td>
						<td colspan="3">${contractSignVo.otherCompanyName}</td>
						<td class="title">合同金额</td>
						<td>${contractSignVo.money}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">合同详情</td>
        	  			<td colspan="5">${contractSignVo.description}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${null != supervisor}">
        	  		<tr>
        	  			<td rowspan="2">主办部门经理</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="4">财务部负责人</td>
        	  			<td class="title" rowspan="2">仅限支出类合同</td>
        	  			<c:if test="${contractSignVo.isPay=='1'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedSeason=='0'?'checked':''} style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedSeason=='1'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   ${contractSignVo.exceedSeasonRate}%）</td>
        	  			</c:if>
        	  			<c:if test="${contractSignVo.isPay=='0'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<c:if test="${contractSignVo.isPay=='1'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedGroup=='0'?'checked':''} style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedGroup=='1'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（    ${contractSignVo.exceedGroupRate}%）</td>
        	  			</c:if>
        	  			<c:if test="${contractSignVo.isPay=='0'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${null == supervisor}">
        	  		<tr>
        	  			<td rowspan="2">主办部门经理</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="4">财务部负责人</td>
        	  			<td class="title" rowspan="2">仅限支出类合同</td>
        	  			<c:if test="${contractSignVo.isPay=='1'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedSeason=='0'?'checked':''} style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedSeason=='1'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   ${contractSignVo.exceedSeasonRate}%）</td>
        	  			</c:if>
        	  			<c:if test="${contractSignVo.isPay=='0'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<c:if test="${contractSignVo.isPay=='1'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedGroup=='0'?'checked':''} style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" ${contractSignVo.exceedGroup=='1'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（    ${contractSignVo.exceedGroupRate}%）</td>
        	  			</c:if>
        	  			<c:if test="${contractSignVo.isPay=='0'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='bankAccount'}">
        				<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">开设、变更及撤销银行账户申请表
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">经办人</td>
        	  			<td>${changeBankAccountVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${changeBankAccountVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">账户基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">单位名称</td>
        	  			<td colspan="5">${changeBankAccountVo.accountCompanyName}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">开户行全称</td>
						<td colspan="5">${changeBankAccountVo.bankName}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">账户类别</td>
        	  			<td colspan="2">${changeBankAccountVo.accountType}</td>
        	  			<td class="title">账   号</td>
        	  			<td colspan="2">${changeBankAccountVo.accountNumber}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">申请信息</td></tr>
        	  		<c:if test="${changeBankAccountVo.applyType=='开立'}">
        	  		<tr>
        	  			<td class="title" rowspan="2">开立</td>
        	  			<td class="title">开户依据</td>
        	  			<td colspan="4">${changeBankAccountVo.newAccountReason}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">账户用途</td>
        	  			<td colspan="4">${changeBankAccountVo.accountUse}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${changeBankAccountVo.applyType=='变更'}">
        	  		<tr>
        	  			<td class="title" rowspan="3">变更</td>
        	  			<td class="title">变更事项</td>
        	  			<td colspan="4">${changeBankAccountVo.changeItem}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更后信息</td>
        	  			<td colspan="4">${changeBankAccountVo.afterChangeInfo}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更原因</td>
        	  			<td colspan="4">${changeBankAccountVo.changeReason}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${changeBankAccountVo.applyType=='撤销'}">
        	  		<tr>
        	  			<td class="title" rowspan="2">撤销</td>
        	  			<td class="title">销户原因</td>
        	  			<td colspan="4">${changeBankAccountVo.deleteAccountReason}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">资金去向</td>
        	  			<td colspan="4">${changeBankAccountVo.moneyWhere}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${null != supervisor}">
        	  		<tr>
        	  			<td rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${null == supervisor}">
        	  		<tr>
        	  			<td rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='DestroyChop'}">
        				<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">印章缴销申请表
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${chopDestroyVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${chopDestroyVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">印章基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">印章名称</td>
        	  			<td colspan="2">${chopDestroyVo.chopName}</td>
        	  			<td class="title">印章类型</td>
        	  			<td colspan="2">
        	  			<c:if test="${chopDestroyVo.type=='1'}">
        	  				公章
        	  			</c:if>
        	  			<c:if test="${chopDestroyVo.type=='2'}">
        	  				合同专用章
        	  			</c:if>
        	  			<c:if test="${chopDestroyVo.type=='3'}">
        	  				法人章
        	  			</c:if>
        	  			<c:if test="${chopDestroyVo.type=='4'}">
        	  				财务专用章
        	  			</c:if>
        	  			<c:if test="${chopDestroyVo.type=='5'}">
        	  				发票专用章
        	  			</c:if>
        	  			</td>
        	  		</tr>
        	  		<tr>
						<td class="title">印章描述</td>
						<td colspan="5">${chopDestroyVo.description}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">缴销原因</td>
        	  			<td colspan="5">${chopDestroyVo.destroyReason}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${null != supervisor}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理部门领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${null == supervisor}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理部门领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  	<br>
        	  	<c:if test="${not empty attas}">
        	  	<div>
        	  		<label style="color:red">公安等部门出具的相关缴销文件：</label>
        	  		<div>
        	  			<c:forEach items="${attas}" var="item" varStatus="index">
        	  				<div>
							<img src="administration/process/showImg?processInstanceID=${processInstanceID}&index=${index.index}"/>
							</div>
						</c:forEach>
        	  		</div>	
        	  	</div>
        	  	</c:if>
        		</c:if>
        		<c:if test="${businessType=='purchaseProperty'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">财产购置申请单
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
				<div style="font-weight:bold">
        	  	<div style="float:left;margin-left:20px">申购单编号：0000${id}</div>
        	  	<div style="float:right;margin-right:20px">申购日期：${fn:split(purchasePropertyVo.requestDate," ")[0]}</div>
        	  	</div>
        	  	<br>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">部门</td>
        	  			<td colspan="3">${department}</td>
        	  			<td class="title">申购人</td>
        	  			<td>${purchasePropertyVo.userName}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">固定资产和低值易耗品名称</td>
        	  			<td colspan="3">${purchasePropertyVo.propertyName}</td>
        	  			<td class="title">数量</td>
        	  			<td>${purchasePropertyVo.number}</td>
        	  		</tr>
        	  		<tr>
	        	  		<td class="title">预算总价</td>
        	  			<td>${purchasePropertyVo.budgetPrice}元</td>
        	  			<td class="title">预算</td>
        	  			<td>
        	  				<input type="checkbox" onclick="return false;" <c:if test='${purchasePropertyVo.exceedBudget=="0"}'>checked</c:if>  value="0" style="height:15px;width:15px">&nbsp;<label>内</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${purchasePropertyVo.exceedBudget=="1"}'>checked</c:if>  value="1" style="height:15px;width:15px">&nbsp;<label>外</label> 
        	  			</td>
        	  			<td class="title">型号规格</td>
        	  			<td colspan="5">${purchasePropertyVo.model}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">使用地点</td>
        	  			<td colspan="3">${purchasePropertyVo.place}</td>
        	  			<td class="title">使用(保管)人</td>
        	  			<td>${purchasePropertyVo.storageUserName}</td>
        	  		</tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td style="border-right-color:#fff;border-bottom-color:#fff" class="title">购置原因：</td>
        	  			<td style="border-bottom-color:#fff" colspan="5">${purchasePropertyVo.reason}</td>
        	  		</tr>
        	  		<tr>
        	  			<td style="border-right-color:#fff" colspan="4"></td>
        	  			<td style="border-right-color:#fff">申购部门主管签字：</td>
        	  			<td>${finishedTaskVOs[1 ].assigneeName}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title">购置原因</td>
        	  			<td colspan="5">${purchasePropertyVo.reason}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">采购人意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='purchaserAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">办公室意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">分管领导意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[5].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">总经理意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[6].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">预算小组意见：</td>
        	  			<td colspan="5">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='budgetAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">采购人意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='purchaserAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">办公室意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">分管领导意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">总经理意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[5].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">预算小组意见：</td>
        	  			<td colspan="5">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='budgetAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr>
        	  			<td colspan="6" class="bold" style="text-align:center">以下由采购人员填写</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2">品名:${purchasePropertyVo.productName}</td>
        	  			<td colspan="2">规格型号:${purchasePropertyVo._model}</td>
        	  			<td>数量:${purchasePropertyVo._number}</td>
        	  			<td>单价:<c:if test="${purchasePropertyVo.unitPrice!=''&&purchasePropertyVo.unitPrice!=null}">${purchasePropertyVo.unitPrice}元</c:if></td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2" class="title">使用或采购部门验收意见</td>
        	  			<td colspan="2">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${purchasePropertyVo.purchaserCheckResult=="0"}'>checked</c:if>  value="1" style="height:15px;width:15px">&nbsp;<label>合格</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${purchasePropertyVo.purchaserCheckResult=="1"}'>checked</c:if>  value="0" style="height:15px;width:15px">&nbsp;<label>不合格</label> 
        	  			</td>
        	  			<td class="title">使用或申购部门签收</td>
        	  			<td>
        	  				<c:forEach items="${finishedTaskVOs}" var="task">
	        	  				<c:if test="${task.taskDefKey=='propertySign'}">
					  				${task.assigneeName}
	        	  				</c:if>
	        	  			</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="6" class="bold" style="text-align:center">以下由财务部填写</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2" class="title">固定资产和低值易耗品分类</td>
        	  			<td colspan="2">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${purchasePropertyVo.propertyType=="0"}'>checked</c:if>  value="1" style="height:15px;width:15px">&nbsp;<label>固定资产</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${purchasePropertyVo.propertyType=="1"}'>checked</c:if>  value="0" style="height:15px;width:15px">&nbsp;<label>低值易耗品</label> 
        	  			</td>
        	  			<td colspan="2">固定资产和低值易耗品编号：${purchasePropertyVo.propertyNum}</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2" class="title">折旧年限</td>
        	  			<td colspan="2"><c:if test="${purchasePropertyVo.useTime!=''&&purchasePropertyVo.useTime!=null}">${purchasePropertyVo.useTime}年</c:if></td>
        	  			<td class="title">净残值率</td>
        	  			<td><c:if test="${purchasePropertyVo.netSalvageRate!=''&&purchasePropertyVo.netSalvageRate!=null}">${purchasePropertyVo.netSalvageRate}%</c:if></td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	<div style="color:red">注：本表一式二份，一份由办公室固定资产和低值易耗品管理员留存，一份由财务作为入账凭证。</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='CarveChop'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">印章刻制申请表
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${carveChopVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${carveChopVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">印章基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">申请刻制印章名称（全称）</td>
        	  			<td colspan="3">${carveChopVo.chopName}</td>
        	  			<td class="title">印章类型</td>
        	  			<td colspan="1">${carveChopVo.chopType}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">刻章理由</td>
						<td colspan="5">
							<input type="checkbox" onclick="return false;" ${carveChopVo.carveReason=='新制'?'checked':''} style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">新制</label>&nbsp;&nbsp;&nbsp;
	        	  			<input type="checkbox" onclick="return false;" ${carveChopVo.carveReason=='遗失'?'checked':''} style="height:15px;width:15px">&nbsp;<label>遗失</label>&nbsp;&nbsp;&nbsp;
							<input type="checkbox" onclick="return false;" ${carveChopVo.carveReason=='损坏'?'checked':''} style="height:15px;width:15px">&nbsp;<label>损坏</label>&nbsp;&nbsp;&nbsp;
							<input type="checkbox" onclick="return false;" ${carveChopVo.carveReason=='更名'?'checked':''} style="height:15px;width:15px">&nbsp;<label>更名</label>&nbsp;&nbsp;&nbsp;
							<input type="checkbox" onclick="return false;" ${carveChopVo.carveReason=='更换材质'?'checked':''} style="height:15px;width:15px">&nbsp;<label>更换材质</label>
						</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">备注</td>
        	  			<td colspan="5">${carveChopVo.remark}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">用章部门</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">行政人事部门</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">用章部门</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">行政人事部门</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='handleProperty'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">资产处置申请单
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  		<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请人</td>
        	  			<td colspan="3">${handlePropertyVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td colspan="2">${handlePropertyVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">资产基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">资产名称</td>
        	  			<td colspan="3">${handlePropertyVo.assetName}</td>
        	  			<td class="title">资产编号</td>
        	  			<td colspan="1">${handlePropertyVo.assetNum}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">规格型号</td>
        	  			<td colspan="3">${handlePropertyVo.model}</td>
        	  			<td class="title">使用部门</td>
        	  			<td colspan="1">${handlePropertyVo.useDepartment}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">处置原因</td>
						<td colspan="5">${handlePropertyVo.handleReason}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">处置方案</td>
        	  			<td colspan="5">${handlePropertyVo.handleCase}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物使用部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物管理部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='transferProperty'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">固定资产调拨申请单
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  		<div class="tab">
        	  		<table>
        	  		<tr>
        	  			<td class="title">申请人</td>
        	  			<td colspan="3">${transferPropertyVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td>${transferPropertyVo.requestDate}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">资产基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">资产名称</td>
        	  			<td colspan="3">${transferPropertyVo.assetName}</td>
        	  			<td class="title">资产编号</td>
        	  			<td colspan="1">${transferPropertyVo.assetNum}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">规格型号</td>
        	  			<td colspan="3">${transferPropertyVo.model}</td>
        	  			<td class="title">类型</td>
        	  			<td colspan="1">${transferPropertyVo.assetType}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">数量</td>
						<td>${transferPropertyVo.number}</td>
						<td class="title">单价</td>
						<td>${transferPropertyVo.unitPrice}元</td>
						<td class="title">金额</td>
						<td>${transferPropertyVo.money}元</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">调出单位</td>
        	  			<td colspan="3">${transferPropertyVo.oldCompany}</td>
        	  			<td class="title">调入单位</td>
        	  			<td colspan="1">${transferPropertyVo.newCompany}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">调拨原因</td>
        	  			<td colspan="5">${transferPropertyVo.transferReason}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物使用部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物管理部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='shopApply'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">${shopApplyVo.applyType}申请单
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  		<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${shopApplyVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${shopApplyVo.requestDate}</td>
        	  		</tr>
        	  		<c:if test="${shopApplyVo.applyType=='开店'}">
        	  			<tr>
	        	  			<td class="title">办理人</td>
	        	  			<td colspan="5">${shopApplyVo.handlerName}</td>
	        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='店铺信息维护'}">
	        	  		<tr>
	        	  			<td class="title">操作账号</td>
	        	  			<td>${shopApplyVo.operationAccount}</td>
	        	  			<td class="title">办理人</td>
	        	  			<td colspan="3">${shopApplyVo.handlerName}</td>
	        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='关店'}">
	        	  		<tr>
	        	  			<td class="title">操作账号</td>
	        	  			<td>${shopApplyVo.operationAccount}</td>
	        	  			<td class="title">财产接收人</td>
	        	  			<td>${shopApplyVo.propertyReceiver}</td>
	        	  			<td class="title">办理人</td>
	        	  			<td>${shopApplyVo.handlerName}</td>
	        	  		</tr>
        	  		</c:if>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">店铺基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">店铺名称</td>
        	  			<td colspan="1">${shopApplyVo.shopName}</td>
        	  			<td class="title">平台</td>
        	  			<td colspan="1">${shopApplyVo.platform}</td>
        	  			<td class="title">开店时间</td>
        	  			<td colspan="1">${shopApplyVo.shopStartTime}</td>
        	  		</tr>
        	  		<c:if test="${shopApplyVo.applyType=='开店'}">
        	  		<tr>
        	  			<td class="title" rowspan="2">开店类型（个人开店/企业开店）</td>
        	  			<td rowspan="2">${shopApplyVo.shopType}</td>
        	  			<td class="title" rowspan="2">主营</td>
        	  			<td class="title">一级类目</td>
        	  			<td colspan="2">${shopApplyVo.firstCategory}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">二级类目</td>
        	  			<td colspan="2">${shopApplyVo.secondCategory}</td>
        	  		</tr>
					<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">认证信息</td></tr>
					<c:if test="${shopApplyVo.shopType=='企业开店'}">
						<tr>
							<td class="title" rowspan="3">企业</td>
							<td class="title">法定代表人</td>
							<td>${shopApplyVo.legalPerson}</td>
							<td class="title">认证企业</td>
							<td colspan="2">${shopApplyVo.certificatedCompany}</td>
						</tr>
						<tr>
							<td class="title">支付宝验证手机</td>
							<td>${shopApplyVo.companyAliPayPhone}</td>
							<td class="title">企业认证支付宝</td>
							<td colspan="2">${shopApplyVo.companyAliPayAccount}</td>
						</tr>
						<tr>
							<td class="title">对公银行账号</td>
							<td colspan="5">${shopApplyVo.publicBankAccount}</td>
						</tr>
					</c:if>
					<c:if test="${shopApplyVo.shopType=='个人开店'}">
						<tr>
							<td class="title" rowspan="4">个人</td>
							<td class="title">店铺负责人</td>
							<td>${shopApplyVo.shopOwner}</td>
							<td class="title">认证支付宝</td>
							<td colspan="2">${shopApplyVo.privateAliPayAccount}</td>
						</tr>
						<tr>
							<td class="title">支付宝验证手机</td>
							<td>${shopApplyVo.privateAliPayPhone}</td>
							<td class="title">个人与企业是否签订协议</td>
							<td colspan="2">${shopApplyVo.signIn}</td>
						</tr>
						<tr>
							<td class="title">法定代表人</td>
							<td>${shopApplyVo.privateLegalPerson}</td>
							<td class="title">认证企业</td>
							<td colspan="2">${shopApplyVo.privateCertificatedCompany}</td>
						</tr>
						<tr>
							<td class="title">对公银行账号</td>
							<td colspan="4">${shopApplyVo.publicBankAccount}</td>
						</tr>
					</c:if>
					<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">开店费用</td></tr>
					<tr>
        	  			<td class="title">保证金</td>
        	  			<td colspan="1">${shopApplyVo.bond}元</td>
        	  			<td class="title">技术年费</td>
        	  			<td colspan="1">${shopApplyVo.technologyAnnualFee}元</td>
        	  			<td class="title">平台佣金比例</td>
        	  			<td colspan="1">${shopApplyVo.commissionRate}%</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='店铺信息维护'}">
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">申请修改信息</td></tr>	
        	  		<tr>
        	  			<td class="title" style="height:80px">原信息</td>
        	  			<td colspan="5">${shopApplyVo.oldInformation}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="height:80px">变更后信息</td>
        	  			<td colspan="5">${shopApplyVo.changeInformation}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='关店'}">
        	  		<tr>
        	  			<td class="title" style="height:80px">关店原因</td>
        	  			<td colspan="3">${shopApplyVo.closeShopReason}</td>
        	  			<td class="title" style="height:80px">关店时间</td>
        	  			<td>${shopApplyVo.closeShopTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法人代表）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法人代表）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='shopPayApply'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">运营部营销费用充值申请表
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div class="tab shopPayApply">
        	  	<table>
        	  		<tr>
        	  			<td class="_title">申请时间</td>
        	  			<td colspan="2">${fn:split(shopPayApplyVo.requestDate, " ")[0]}</td>
        	  			<td class="_title">申请人</td>
        	  			<td colspan="3">${shopPayApplyVo.userName}</td>
        	  			<td class="_title">使用部门</td>
        	  			<td colspan="2">${department}</td>
        	  			<td class="_title">充值周期</td>
        	  			<td colspan="3">${shopPayApplyVo.beginDate}<c:if test="${shopPayApplyVo.beginDate!='' && shopPayApplyVo.beginDate!=null}">至</c:if>${shopPayApplyVo.endDate}</td>
        	  			
        	  		</tr>
        	  		<tr>
        	  			<td class="_title" colspan="5">运营推广费用</td>
        	  			<td class="_title" colspan="5">付费插件费用</td>
        	  			<td class="_title" colspan="4">其他服务费</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="_title" style="width:7%">店铺名称</td>
        	  			<td class="_title" style="width:5%">直通车</td>
        	  			<td class="_title" style="width:5%">钻石展位</td>
        	  			<td class="_title" style="width:5%">品销宝</td>
        	  			<td class="_title" style="width:5%">小计</td>
        	  			<td class="_title" style="width:7%">店铺名称</td>
        	  			<td class="_title" style="width:7%">服务/应用名称</td>
        	  			<td class="_title" style="width:7%">插件/服务作用</td>
        	  			<td class="_title" style="width:7%">开通时长</td>
        	  			<td class="_title" style="width:7%">付费金额</td>
        	  			<td class="_title" style="width:7%">项目名称</td>
        	  			<td class="_title" style="width:7%">项目作用</td>
        	  			<td class="_title" style="width:17%">项目明细</td>
        	  			<td class="_title" style="width:5%">项目花费</td>
        	  		</tr>
        	  		<c:forEach items="${shopPayApplyListVos}" var="shopPayApplyListVo">
        	  			<tr>
        	  				<td>${shopPayApplyListVo.spreadShopName}</td>
        	  				<td>${shopPayApplyListVo.directPassMoney}</td>
        	  				<td>${shopPayApplyListVo.showMoney}</td>
        	  				<td>${shopPayApplyListVo.saleMoney}</td>
        	  				<td>${shopPayApplyListVo.total}</td>
        	  				<td>${shopPayApplyListVo.pluginShopName}</td>
        	  				<td>${shopPayApplyListVo.serviceName}</td>
        	  				<td>${shopPayApplyListVo.serviceUse}</td>
        	  				<td><c:if test="${shopPayApplyListVo.openTime!='' && shopPayApplyListVo.openTime!=null}">${shopPayApplyListVo.openTime}个月</c:if></td>
        	  				<td>${shopPayApplyListVo.payMoney}</td>
        	  				<td>${shopPayApplyListVo.projectName}</td>
        	  				<td>${shopPayApplyListVo.projectUse}</td>
        	  				<td style="font-size:12px">${shopPayApplyListVo.projectDescription}</td>
        	  				<td>${shopPayApplyListVo.projectPay}</td>
        	  			</tr>
        	  		</c:forEach>
        	  		<tr>
        	  			<td class="_title">运营推广费用合计</td>
        	  			<td colspan="2">${resultMap.运营推广费用合计==null?'/':resultMap.运营推广费用合计}</td>
        	  			<td class="_title">付费插件费用合计</td>
        	  			<td colspan="2">${resultMap.付费插件费用合计==null?'/':resultMap.付费插件费用合计}</td>
        	  			<td class="_title">其他服务费合计</td>
        	  			<td colspan="2">${resultMap.其他服务费合计==null?'/':resultMap.其他服务费合计}</td>
        	  			<td class="_title">总计（大写）</td>
        	  			<td colspan="4" id="totalShopPay" class="col_blue"></td>
        	  		</tr>
        	  		<tr>
        	  			<td class="_title">总经理：</td>
        	  			<td colspan="3">${finishedTaskVOs[2].assigneeName}</td>
        	  			<td class="_title">财务主管：</td>
        	  			<td colspan="3">${finishedTaskVOs[1].assigneeName}</td>
        	  			<td class="_title">部门主管：</td>
        	  			<td colspan="2">${finishedTaskVOs[0].assigneeName}</td>
        	  			<td class="_title">经办人：</td>
        	  			<td colspan="2">${finishedTaskVOs[4].assigneeName}</td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        		<c:if test="${businessType=='ChopBorrow'}">
        		<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">印章使用申请单
        	  	<button id="return" type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
						>返回</button>
						<div style="float:right;height:20px;width:10px"></div>
       					<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button></h4>
        	  	<div style="text-align:right;margin-right:20px">NO:年份&nbsp;<span class="underline">${year}</span>
        	  	&nbsp;编号&nbsp;<span class="underline">0000${chopBorrrowVo.chopBorrow_Id}</span></div>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">所属部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${chopBorrrowVo.requestDate}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">印章名称</td>
        	  			<td colspan="5">${chopBorrrowVo.chop_Name}</td>
        	  		</tr>
        	  		<tr>
	        	  		<c:if test="${chopBorrrowVo.isBorrow=='1'}">
	        	  			<td class="title">是否外借</td>
	        	  			<td>是</td>
	        	  			<td class="title">开始时间</td>
	        	  			<td>${chopBorrrowVo.startTime}</td>
	        	  			<td class="title">结束时间</td>
	        	  			<td>${chopBorrrowVo.endTime}</td>
	        	  		</c:if>
	        	  		<c:if test="${chopBorrrowVo.isBorrow=='0'}">
	        	  			<td class="title">是否外借</td>
	        	  			<td colspan="5">否</td>
	        	  		</c:if>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">文件基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">文件名称</td>
        	  			<td colspan="3">${chopBorrrowVo.fileName}</td>
        	  			<td class="title">文件类型</td>
        	  			<td>${chopBorrrowVo.fileType}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">文件用途</td>
        	  			<td colspan="3">${chopBorrrowVo.fileUse}</td>
        	  			<td style="text-align:left">是否涉及法律等重要事项</td>
        	  			<td>
        	  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrrowVo.relateLaw=="1"}'>checked</c:if> name="chopBorrrowVo.relateLaw" value="1" style="height:15px;width:15px">&nbsp;<label>是</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrrowVo.relateLaw=="0"}'>checked</c:if> name="chopBorrrowVo.relateLaw" value="0" style="height:15px;width:15px">&nbsp;<label>否</label> 
        	  			</td>
        	  		</tr>
        	  		<c:if test="${chopBorrrowVo.fileType=='合同'}">
        	  		<tr>
        	  			<td class="title">甲方</td>
        	  			<td>${chopBorrrowVo.partyA}</td>
        	  			<td class="title">乙方</td>
        	  			<td>${chopBorrrowVo.partyB}</td>
        	  			<td class="title">合同申请时间</td>
        	  			<td>${chopBorrrowVo.contractApplyDate}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr>
        	  			<td class="title">正版/复印件</td>
        	  			<td colspan="3">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrrowVo.isCopy=="0"}'>checked</c:if> name="chopBorrrowVo.isCopy" value="0" style="height:15px;width:15px">&nbsp;<label>正版</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrrowVo.isCopy=="1"}'>checked</c:if> name="chopBorrrowVo.isCopy" value="1" style="height:15px;width:15px">&nbsp;<label>复印件</label>
        	  			</td>
        	  			<td class="title">份数</td>
        	  			<td>${chopBorrrowVo.fileNum}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门经理</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门分管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章主管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理人员</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门经理</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门分管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章主管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理人员</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        		</c:if>
        <c:if test="${businessType=='shopApply' and fn:length(attas)> 0}">
        	<br>
		  	<div class="form-group">
		  	<label class="col-sm-1">附件：</label>
		  	<div>
		  		<c:forEach items="${attas}" var="atta" varStatus="status">
		  			<a href="personal/getVacationAttachmentAll_?processInstanceID=${processInstanceID}&index=${status.index}">${atta.name}</a>&nbsp;&nbsp;
		  		</c:forEach>
		  	</div>
		  	</div>
		</c:if>
		  <c:if test="${businessType=='ContractSign' && fn:length(lawAttas)>0}">
		  <br>
		  <div class="form-group">
		  	<label class="col-sm-2">法务的审批或签名：</label>
	  		<c:forEach items="${lawAttas}" var="lawAtta" varStatus="status">
	  		<a href="javascript:showPic('${lawAtta.name}',${lawAtta.id})" target="_self">
	  		<img src="personal/getAttachmentByAttachmentId?attachmentId=${lawAtta.id}" alt="附件图片" id="photo" class="img-thumbnail" style="width:100px;height:100px;">
	  		</a>
	  		</c:forEach>
		  </div>
		  </c:if>
	  	  <c:if test="${businessType=='ChangeContract' && fn:length(attas)>0}">
	  	  <br>
		  <div class="form-group">
		  	<label class="col-sm-2">法务的审批或签名：</label>
		  		<c:forEach items="${attas}" var="atta" varStatus="status">
		  		<a href="javascript:showPic('${atta.name}',${atta.id})" target="_self">
		  		<img src="personal/getAttachmentByAttachmentId?attachmentId=${atta.id}" alt="附件图片" id="photo" class="img-thumbnail" style="width:100px;height:100px;">
		  		</a>
		  		</c:forEach>
		  </div>
		  </c:if>
		  <c:if test="${businessType=='ContractSign' && fn:length(contractAttas)>0}">
		  <div class="form-group">
		  	<label class="col-sm-2">合同封面：</label>
	  		<c:forEach items="${contractAttas}" var="contractAtta">
	  		<a href="javascript:showPic('${contractAtta.name}', ${contractAtta.id})" target="_self">
	  		<img src="personal/getAttachmentByAttachmentId?attachmentId=${contractAtta.id}" alt="附件图片" id="photo" class="img-thumbnail" style="width:100px;height:100px;">
	  		</a>
	  		</c:forEach>
		  </div>
		  </c:if>
        </div>
      </div>
    </div>
    	<script src="/assets/js/layer/layer.js"></script>
        <script type="text/javascript">
    	function printOrder(){
    	$("#return").remove();
    	$("#print").remove();
	  	var content=$('#printArea').html();
		$('body').empty().html(content);
		window.print();
		var argSearch = location.search;
		argSearch = argSearch.substring(1, argSearch.length);
		var args = argSearch.split("&");
		args.forEach(function(value, index){
			if(index==1){
				$("input[name='businessType']").val(value.split("=")[1]);
			}else{
				$("input[name='processInstanceID']").val(value.split("=")[1]);
			}
		});
		window.location.reload();
	   	}
		var search=location.search;
		//假如参数为空  这种情况 不可能发生 除了 从 print 页跳转回来
		if(!search){
			location.href=localStorage.lastUrl;
		}
		localStorage.lastUrl=location.href;
		$(function(){
			var totalPay = '${resultMap.总计}';
			toUpperCase(totalPay);
		});
		//数字转大写
		function toUpperCase(number){
			var number_arr_=["零","壹","贰","叁","肆","伍","陆","柒","捌","玖"];
			var unit_arr1=["拾","佰","仟"];
			var unit_arr2=["万","亿","兆"];
			var index;
			if(~(index=number.indexOf("."))){
			    //只取到分
			    if(index+3<number.length){
			    	number=number.substring(0,index+3);
			    }else{
			        var num=2-(number.length-1-index);
			        //需要填充的 num的数量
			        for(var i=0;i<num;i++){
			        	number+="0";
			        }
			    }
			}else{
				number=number+".00";
			}
			var prev=number.substring(0,number.length-3);
			var tail=number.substring(number.length-2);
			var getTailHtml=function(){
				return "<span>"+number_arr_[tail[0]]+"</span>　角　" + "<span>"+number_arr_[tail[1]]+"</span>　分　";
			}
			var number_arr=[];
			var getPrevHtml=function(){
				for(var i=prev.length;i>=0;i-=4){
					var startIndex=(i-4>=0)?i-4:0;
					var endIndex=i;
					number_arr.push(prev.substring(startIndex,endIndex));
					if(startIndex==0)break;
				}
			}
			getPrevHtml();
			var resultHtml="";
			for(var i=0;i<number_arr.length;i++){
				if(i==0){
					var number_str=number_arr[i];
					for(var j=number_str.length-1,k=0;j>=0;j--,k++){
						var number_=number_str.charAt(j);
						if(k==0){
							resultHtml="<span>"+number_arr_[number_]+"</span>　元　"+resultHtml;				
						}else{
							resultHtml="<span>"+number_arr_[number_]+"</span>　"+unit_arr1[k-1]+"　"+resultHtml;				
						}
					}
				}else{
					var number_str=number_arr[i];
					for(var j=number_str.length-1,k=0;j>=0;j--,k++){
						var number_=number_str.charAt(j);
						if(k==0){
							resultHtml="<span>"+number_arr_[number_]+"</span>　"+unit_arr2[i-1]+"　"+resultHtml;						
						}else{
							resultHtml="<span>"+number_arr_[number_]+unit_arr1[k-1]+"</span>"+resultHtml;
							
						}
					}
				}
			}
			var upperCase = resultHtml+getTailHtml();
			$("#totalShopPay").html(upperCase);
		}
		function showPic(name, id){
			var photos = {"start": 0, "data":[]};
			photos.data.push({"alt":name, "src":"personal/getAttachmentByAttachmentId?attachmentId="+id});
			layer.photos({
				offset:'100px',
			    photos: photos,
			    anim: 5
			});
		}
	</script>
	<input name="processInstanceID" type="hidden"/>
	<input name="businessType" type="hidden"/>
</body>
</html>