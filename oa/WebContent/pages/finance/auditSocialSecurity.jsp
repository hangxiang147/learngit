<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">

	function taskComplete(result) {
		var businessType = "社保名单审核";
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "finance/process/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType;
	}
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'audit'"></s:set>
        <%@include file="/pages/finance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <input type="hidden" id="taskID" value="${taskID}"/>
        	<div class="fz20 mt20">社会保险费申报表（参保）</div>
		    <div class="filter mt10 clearfix">
			  <div class="fr clearfix">
			  	<span class="fl">缴纳时间：<font class="fcr">${socialSecurityProcessVO.ssYear }年${socialSecurityProcessVO.ssMonth }月</font></span>
			  	<span class="fl ml20">合计：<font class="fcr">${socialSecurityProcessVO.ssTotalCount }元</font></span>
			  </div>
		    </div>
		    <div class="mt20">
				<table class="fundtab">
					<thead>
						<tr>
							<td>序号</td>
							<td>姓名</td>
							<td>入职时间</td>
							<td>转正时间</td>
							<td>性别</td>
							<td>身份证明号码</td>
							<td>证件名称</td>
							<td>是否参保过</td>
							<td>单位合计</td>
							<td>个人合计</td>
							<td>应缴金额</td>
							<td>备注</td>
						</tr>
					</thead>
					<tbody>
					<c:if test="${not empty housingFundVOs}">
					<c:set var="hf_id" value="0"></c:set>
					<c:forEach items="${housingFundVOs}" var="housingFundVO">
						<tr>
	              			<td>${hf_id+1}</td>
	              			<td>${housingFundVO.userName}</td>
	              			<td>${housingFundVO.entryDate }</td>
	              			<td>${housingFundVO.formalDate }</td>
							<td><c:if test="${housingFundVO.gender==1 }">男</c:if><c:if test="${housingFundVO.gender==2 }">女</c:if></td>
							<td>${housingFundVO.idNumber}</td>
							<td><c:if test="${housingFundVO.idType==1 }">身份证</c:if></td>
							<td><c:if test="${housingFundVO.hasPaid==1 }">是</c:if><c:if test="${housingFundVO.hasPaid==0 }">否</c:if></td>
							<td>${housingFundVO.companyCount}</td>
							<td>${housingFundVO.personalCount}</td>
							<td>${housingFundVO.totalCount}</td>
							<td>${housingFundVO.note}</td>
	              		</tr>
	              		<c:set var="hf_id" value="${hf_id+1}"></c:set>
					</c:forEach>
					</c:if>
				</tbody>
				</table>
			  </div>
			  
			  <div class="fz20 mt20">住房公积金汇缴明细表</div>
			  <div class="filter mt10 clearfix">
				<div class="fr clearfix">
				  <span class="fl">缴纳时间：<font class="fcr">${socialSecurityProcessVO.year }年${socialSecurityProcessVO.month }月</font></span>
				  <span class="fl ml20">个人部分：<font class="fcr">${socialSecurityProcessVO.personalCount }</font>&nbsp;&nbsp;&nbsp;单位部分：<font class="fcr">${socialSecurityProcessVO.companyCount }</font>&nbsp;&nbsp;&nbsp;合计：<font class="fcr">${socialSecurityProcessVO.totalCount }</font></span>
				</div>
			  </div>
			  <div class="mt20">
			  	<table class="fundtab">
				<thead> 
					<tr>
						<td rowspan="2" width="50">序号</td>
						<td rowspan="2" width="80">职工姓名</td>
						<td rowspan="2" width="100">证件类型</td>
						<td rowspan="2" width="12%">证件号码</td>
						<td rowspan="2" width="80">缴存基数</td>
						<td rowspan="2" width="80">个人缴存比例(%)	</td>
						<td rowspan="2">增加原因</td>
						<td colspan="3">住房公积金月缴存额（元）</td>
					</tr>
			        <tr>
						<td width="100">个人部分</td>
						<td width="100">单位部分</td>
						<td width="100">合 计</td>
					</tr>
				</thead>
				<tbody>
				<c:if test="${not empty socialSecurityVOs}">
				<c:set var="ss_id" value="0"></c:set>
				<c:forEach items="${socialSecurityVOs}" var="socialSecurityVO">
					<tr>
              			<td>${ss_id+1}</td>
              			<td>${socialSecurityVO.userName}</td>
						<td><c:if test="${socialSecurityVO.idType==1 }">身份证</c:if></td>
						<td>${socialSecurityVO.idNumber}</td>
						<td>${socialSecurityVO.basePay}</td>
						<td>${socialSecurityVO.selfPaidRatio}%</td>
						<td>${socialSecurityVO.reason}</td>
						<td>${socialSecurityVO.personalProvidentFund}</td>
						<td>${socialSecurityVO.companyProvidentFund}</td>
						<td>${socialSecurityVO.totalProvidentFund}</td>
              		</tr>
              		<c:set var="ss_id" value="${ss_id+1}"></c:set>
				</c:forEach>
				</c:if>
				</tbody>
				</table>
			  
				<div class="mt10 fz18">审批意见</div>
				<div class="mt10">
				<textarea id="comment" class="form-control"></textarea>
				</div>
			  </div>
			  
			  <div class="mt20 tc">
				<button type="button" class="btn btn-primary" onclick="taskComplete(15)" style="margin-left:15px;">成功缴纳</button>
		  		<button type="button" class="btn btn-primary" onclick="taskComplete(16)" style="margin-left:20px;">部分失败</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  
			  <h3 class="fz18 mt20 bdt_gray pt20">流程状态</h3>
	       	  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <div class="form-group clearfix">
			  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
			  	<div class="col-sm-10">
			  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
			  	</div>
			  	<c:if test="${not empty comments }">
	       	  	<c:forEach items="${comments }" var="comment" varStatus="st">
	       	  	<c:if test="${comment.taskID == task.taskID }">
	       	  		<div class="col-sm-2"></div>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${comment.content }</span>
				  	</div>
	       	  	</c:if>
	       	  	</c:forEach>
	       	  	</c:if>
			  </div>
			  </c:forEach>
			  </c:if>
		  
        </div>
      </div>
    </div>
</body>
</html>