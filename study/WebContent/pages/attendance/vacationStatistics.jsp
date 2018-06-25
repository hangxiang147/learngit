<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form id="queryForm"
	action="attendance/vacationStatistics">
	<input name="userName" id="userName" value="${userName}"
		style="display: none" /> <input type="hidden" id="flag" name="flag"
		value="${flag }" /> <input type="hidden" id="companyId"
		name="companyID" />
</form>
		<div class="tab-content">
		<c:if test="${not empty companyVOs }">
			<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
				<div role="tabpanel" class="tab-pane" id="${companyVO.companyID }">
					<div class="table-responsive" style="margin-top: 30px;">
						<table class="tableuser">
							<tbody>
								<tr>
									<td width="18%" align="center">今日请假人数：</td>
									<td width="70%">${count}人</td>
								</tr>
								<tr>
									<td align="center">人员明细：</td>
									<td colspan="2"><c:forEach items="${vacationVOs }"
											var="vacationVO" varStatus="st">
								    	${vacationVO.requestUserName }（<c:forEach
												items="${vacationVO.groupList }" var="group" varStatus="st">
												<c:if test="${st.index != 0 }">；</c:if>${group }</c:forEach>）：${vacationVO.beginDate }至${vacationVO.endDate }<br>
										</c:forEach></td>
								</tr>
							</tbody>
						</table>
					</div>
					<h3 class="sub-header">本月员工请假统计</h3>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">请假人</label>
						<div class="col-sm-2 inputout">
							<input type="text" id="agent" class="form-control"
								name="userName" value="${userName}"/>

						</div>
					</div>
					<button type="button" class="btn btn-primary"
						onclick="searchForVacationStatistic()">查询</button>


					<br>
					<br>
					<div class="table-responsive">
						<table id="vacat" class="table table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>姓名</th>
									<th>请假明细</th>
									<th>累计请假天数</th>
									<th>审批详情</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${not empty statisticList}">
									<s:set name="staff_id"
										value="(#request.page-1)*(#request.limit)"></s:set>
									<s:iterator id="statistic" value="#request.statisticList"
										status="count">
										<tr>
											<td class="col-sm-1"><s:property value="#staff_id+1" /></td>
											<td class="col-sm-1"><s:property
													value="#statistic.requestUserName" /></td>
											<td class="col-sm-3"><s:iterator id="detail"
													value="#statistic.dateDetail" status="count">
													<s:property value="#detail" />
													<br />
												</s:iterator></td>
											<td class="col-sm-2"><s:property value="#statistic.days" />天<s:property
													value="#statistic.showHours" />小时</td>
											<td class="col-sm-1">
											<a href="javascript:showMore('<s:property value="#statistic.showVacationIds" />')">审批详情</a>
											</td>
										</tr>
										<s:set name="staff_id" value="#staff_id+1"></s:set>
									</s:iterator>
								</c:if>
							</tbody>
						</table>
					</div>
		<div class="dropdown" style="margin-top: 25px;">
			<label>每页显示数量：</label>
			<button class="btn btn-default dropdown-toggle" type="button"
				id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="true">
				${limit} <span class="caret"></span>
			</button>
			<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
				style="left: 104px; min-width: 120px;">
				<li><a class="dropdown-item-20" href="#">20</a></li>
				<li><a class="dropdown-item-50" href="#">50</a></li>
				<li><a class="dropdown-item-100" href="#">100</a></li>
			</ul>
			&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span> <input
				type="hidden" id="page" value="${page}" /> <input type="hidden"
				id="limit" value="${limit}" /> <input type="hidden" id="totalPage"
				value="${totalPage }" />
		</div>
		<%@include file="/includes/pager.jsp"%>
		</div>
		</c:forEach>
		</c:if>
		</div>
<div class="modal fade bs-example-modal-lg" id="cheakModal"
	tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	<div class="modal-dialog modal-md" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">审批详情</h4>
				<div class="container-fluid1"></div>
			</div>
			<div class="modal-body">
				<input type="hidden" id="groupDetailID" />
				<p id="ntcContent"></p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>
<script>
    	 function showMore (vacationId){
    	 $.ajax({
    		 url:'personal/getVacationHistoryDetail',
    		 data:{vacationId:vacationId},
    		 success:function(data){
    			 var subjectList=Array.prototype.slice.call(data,1);
    			 if(subjectList.length==0){
    				 layer.alert("暂无审批信息", {offset:'100px'});
    			 }else{
    				 var resultHtml=_.reduce(subjectList,function (str,value){
    					 return str+="【"+value.taskName+"】 审批人："+value.assigneeName+"</br>审批时间："+value.endTime+"</br></br>";
    				 },"")
    				$('#ntcContent').empty().html(resultHtml);
			    	 $("#cheakModal").modal('show');
    				 
    			 }
    		 }
    	 });
     }
    	var searchForVacationStatistic=function (){
    		var flag = parseInt($("#flag").val())-1;
    		$("#userName").val($('input[name="userName"]:gt(0)').eq(flag).val());
    		var selectCompanyId=($('#myTab li[class="active"]').find("a").attr("href")+"").substring(1);
    		$('#companyId').val(selectCompanyId);
    		$('#queryForm').submit();
    		Load.Base.LoadingPic.FullScreenShow(null);
    	}
    </script>
</html>