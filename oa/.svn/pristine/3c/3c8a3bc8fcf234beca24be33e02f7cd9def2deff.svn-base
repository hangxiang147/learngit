<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">

	function deleteHousingFund(hfID) {
		if (confirm("确认删除该员工汇缴明细？")) {
			var taskID = $("#taskID").val();
			$.ajax({
				url:'HR/process/deleteHousingFund',
				type:'post',
				data:{hfID: hfID,
					  taskID: taskID},
				dataType:'json',
				success:function (data) {
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "HR/staff/error?panel=process&selectedPanel=socialSecurityList&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						return false;
					}
					
					if (confirm("删除成功！")) {
						window.location.reload();
						Load.Base.LoadingPic.FullScreenShow(null);
					}
				}
			});
		}
	}

	function deleteSocialSecurity(ssID) {
		if (confirm("确认删除该员工汇缴明细？")) {
			var taskID = $("#taskID").val();
			$.ajax({
				url:'HR/process/deleteSocialSecurity',
				type:'post',
				data:{ssID: ssID,
					  taskID: taskID},
				dataType:'json',
				success:function (data){
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "HR/staff/error?panel=process&selectedPanel=socialSecurityList&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						return false;
					}
					
					if (confirm("删除成功！")) {
						window.location.reload();
						Load.Base.LoadingPic.FullScreenShow(null);
					}
				}
			});
		}
	}
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<s:set name="panel" value="'HRCenter'"></s:set>
		<s:set name="selectedPanel" value="'HRCenter'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="HR/process/save_completeUpdateSocialSecurity" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <input type="hidden" id="taskID" name="taskID" value="${taskID }" />
			  <div class="fz20 mt20">社会保险费申报表（参保）</div>
			  <div class="filter mt10 clearfix">
				<div class="fl"><a href="HR/process/addHousingFund?taskID=${taskID }&panel=process" class="oa_btn btn_s bg_red plr20">添加人员名单</a></div>
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
							<td width="100">操作</td>
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
								<td><a href="HR/staffInfoAlteration/updateHousingFund?hfID=${housingFundVO.hfID}&panel=process&taskID=${taskID}" class="fcr">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="fcr" onclick="return deleteHousingFund(${housingFundVO.hfID});">删除</a></td>
		              		</tr>
		              		<c:set var="hf_id" value="${hf_id+1}"></c:set>
						</c:forEach>
						</c:if>
					</tbody>
				</table>
			  </div>
			  
			  <div class="fz20 mt20">住房公积金汇缴明细表</div>
			  <div class="filter mt10 clearfix">
				<div class="fl"><a href="HR/process/addSocialSecurity?taskID=${taskID }&panel=process" class="oa_btn btn_s bg_red plr20">添加人员名单</a></div>
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
						<td rowspan="2" width="100">操作</td>
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
						<td><a href="HR/staffInfoAlteration/updateSocialSecurity?ssID=${socialSecurityVO.ssID}&panel=process&taskID=${taskID}" class="fcr">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="fcr" onclick="return deleteSocialSecurity(${socialSecurityVO.ssID});">删除</a></td>
              		</tr>
              		<c:set var="ss_id" value="${ss_id+1}"></c:set>
				</c:forEach>
				</c:if>
				</tbody>
				</table>
				
				<div class="mt10 fz18">备注</div>
				<div class="mt10">
				<textarea id="comment" name="comment" class="form-control"></textarea>
				</div>
			  </div>
			  
			  <h4 class="sub-header mt20">流程状态</h4>
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
			  
			  <div class="mt20 tc"><button type="submit" class="oa_btn btn_s bg_blue plr20" >提交</button>
			  <a href="javascript:void(0);" onclick="location.href='javascript:history.go(-1);'" class="oa_btn btn_s bg_999 plr20">返回</a>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>