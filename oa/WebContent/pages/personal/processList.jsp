<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
</style>
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function () {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		var type = $("#type").val();
		if (type == 1) {
			$("#myTab li:eq(0) a").tab("show");
		} else if (type == 2) {
			$("#myTab li:eq(1) a").tab("show");
		} else if (type == 3) {
			$("#myTab li:eq(3) a").tab("show");
		} else if (type == 4) {
			$("#myTab li:eq(4) a").tab("show");
		} else if (type == 5) {
			$("#myTab li:eq(2) a").tab("show");
		}else if(type==15){
			$("#myTab li:eq(5) a").tab("show");
		}else if(type==30){
			$("#myTab li:eq(6) a").tab("show");
		}
		else if(type==35){
			$("#myTab li:eq(7) a").tab("show");
		}
		else if(type==37){
			$("#myTab li:eq(8) a").tab("show");
		}
		$("#myTab li:eq(0) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=1";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(1) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=2";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(3) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=3";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(4) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=4";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(2) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=5";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(5) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=15";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(6) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=30";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(7) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=35";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(8) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/findProcessList?type=37";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	});
	function showRemark(remark){
		layer.alert(remark, {title:'情况说明', offset:'100px'});
	}
	function showOvertimeUsers(users){
		users = users.substring(1, users.length-1);
		layer.alert(users, {title:'加班人员', offset:'100px'});
	}
	function showVacationUsers(users){
		users = users.substring(1, users.length-1);
		layer.alert(users, {title:'请假人员', offset:'100px'});
	}
</script>
<script>
	var stopTask=function (instanceId,bussinessKey){
		layer.confirm("是否确认取消流程？",{offset:'100px'},function (index){
			layer.close(index);
			Load.Base.LoadingPic.FullScreenShow(null);
			$.ajax({
				url:'/personal/stopInstance',
				data:{instanceId:instanceId,bussinessKey:bussinessKey},
				type:"post",
				dataType:'json',
				success:function (returnVal){
					if(returnVal.isSuccess===true){
						layer.alert("终止成功！",{offset:'100px'},function (index){
							layer.close(index);
							window.location.reload();
							Load.Base.LoadingPic.FullScreenShow(null);
						});
						
					}else{
						layer.alert("流程已经经过人员审批，无法终止!",{offset:'100px'});
					}
				},
				complete:function(){
					Load.Base.LoadingPic.FullScreenHide();
				}
			})
		},function (){
			layer.closeAll();
		})
		
	
	}
</script>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <input type="hidden" id="type" value="${type }" />
       	  <ul class="nav nav-tabs" role="tablist" id="myTab">
			  <li role="presentation"><a href="#vacation" role="tab" data-toggle="tab">请假申请</a></li>
			  <li role="presentation"><a href="#assignment" role="tab" data-toggle="tab">任务分配</a></li>
			  <li role="presentation"><a href="#reimbursement" role="tab" data-toggle="tab">报销申请</a></li>
			  <li role="presentation"><a href="#resignation" role="tab" data-toggle="tab">离职申请</a></li>
			  <li role="presentation"><a href="#formal" role="tab" data-toggle="tab">转正申请</a></li>
			  <li role="presentation"><a href="#advance" role="tab" data-toggle="tab">预付申请</a></li>
			  <li role="presentation"><a href="#workOvertime" role="tab" data-toggle="tab">加班申请</a></li>
			  <li role="presentation"><a href="#payment" role="tab" data-toggle="tab">付款申请</a></li>
			  <auth:hasPermission name="morningMeetingReport">
			  <li role="presentation"><a href="#morningMeetingReport" role="tab" data-toggle="tab">周早会汇报</a></li>
			  </auth:hasPermission>
		  </ul>
			
		  <div class="tab-content">
			  <div role="tabpanel" class="tab-pane" id="vacation">
			  	<div class="table-responsive" style="margin-top:30px;">
            		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>申请时间</th>
		                  <th>请假开始日期</th>
		                  <th>请假结束日期</th>
		                  <th>天数</th>
		                  <th>请假类型</th>
		                  <th>当前处理人</th>
		                  <th>流程状态</th>
		                  <th>操作</th>
		                </tr>
		                 
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty vacationVOs}">
		              	<s:set name="vacation_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="vacationVO" value="#request.vacationVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#vacation_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('personal/processHistory?processInstanceID=<s:property value='#vacationVO.processInstanceID'/>&selectedPanel=findProcessList')" href="javascript:void(0)"><s:property value="#vacationVO.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#vacationVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#vacationVO.beginDate"/></td>
		              			<td class="col-sm-1"><s:property value="#vacationVO.endDate"/></td>
		              			<td class="col-sm-1"><s:property value="#vacationVO.days"/>天<s:property value="#vacationVO.showHours" />小时</td>
		              			<td class="col-sm-1"><s:if test="#vacationVO.vacationType == 1">公假</s:if><s:if test="#vacationVO.vacationType == 2">事假</s:if><s:if test="#vacationVO.vacationType == 3">年休假</s:if><s:if test="#vacationVO.vacationType == 4">婚假</s:if></td>
		              			<td class="col-sm-1"><s:property value="#vacationVO.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#vacationVO.status"/></td>
		              			<td class="col-sm-1">
		              				<c:if test="${(vacationVO.status ne '已审批') and (vacationVO.status ne '未通过' ) and (vacationVO.status ne '流程意外中断' ) && canStopInstance}">
		              					<a href="javascript:stopTask('${vacationVO.processInstanceID}','请假申请')">
		              						<svg class="icon" aria-hidden="true" title="终止流程" data-toggle="tooltip">
												<use xlink:href="#icon-zhongzhi"></use>
											</svg>
		              					</a>
		              					&nbsp;
		              				</c:if>
		              				<c:if test="${vacationVO.type=='部门'}">
			              				<a href="javascript:void(0)" onclick="showVacationUsers('${vacationVO.vacationUsers}')">
	           		             			<svg class="icon" aria-hidden="true" title="查看请假人员" data-toggle="tooltip">
												<use xlink:href="#icon-Detailedinquiry"></use>
											</svg>
		           		             	</a>
			              			</c:if>
		              			</td>
		              		</tr>
		              		<s:set name="vacation_id" value="#vacation_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           		    </table>
          		</div> 
			  </div>
			  <div role="tabpanel" class="tab-pane" id="assignment">
			  	<div class="table-responsive" style="margin-top:30px;">
            		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起时间</th>
		                  <th>任务类型</th>
		                  <th>执行人</th>
		                  <th>优先级</th>
		                  <th>截止日期</th>
		                  <th>开始时间</th>
		                  <th>得分</th>
		                  <th>流程状态</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty assignmentVOs}">
		              	<s:set name="assignment_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="assignmentVO" value="#request.assignmentVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#assignment_id+1"/></td>
		              			<td class="col-sm-1">
		              			<s:if test="#assignmentVO.status == '待对方确认' || #assignmentVO.status == '修改完成'">
		              			<a onclick="goPath('personal/updateAssignment?processInstanceID=<s:property value='#assignmentVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#assignmentVO.title"/></a>
		              			</s:if>
		              			<s:else>
		              			<a onclick="goPath('personal/processDetail?processInstanceID=<s:property value='#assignmentVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#assignmentVO.title"/></a>
		              			</s:else>
		              			</td>
		              			<td class="col-sm-1"><s:property value="#assignmentVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:if test="#assignmentVO.type == 1">其他</s:if><s:if test="#assignmentVO.type == 2">IT需求</s:if><s:if test="#assignmentVO.type == 3">培训需求</s:if></td>
		              			<td class="col-sm-1"><s:property value="#assignmentVO.executorName"/></td>
		              			<td class="col-sm-1"><s:if test="#assignmentVO.priority == 1">高</s:if><s:if test="#assignmentVO.priority == 2">中</s:if><s:if test="#assignmentVO.priority == 3">低</s:if></td>
		              			<td class="col-sm-1"><s:property value="#assignmentVO.deadline"/></td>
		              			<td class="col-sm-1"><s:property value="#assignmentVO.beginDate"/></td>
		              			<td class="col-sm-1"><s:property value="#assignmentVO.score"/></td>
		              			<td class="col-sm-1"><s:property value="#assignmentVO.status"/></td>
		              		</tr>
		              		<s:set name="assignment_id" value="#assignment_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           		    </table>
          		</div> 
			  </div>
			  <div role="tabpanel" class="tab-pane" id="reimbursement">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>申请时间</th>
		                  <th>报销单号</th>
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
		              			<td class="col-sm-1">
		              			<a onclick="goPath('personal/processDetail?processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)">
		              			<s:property value="#reimbursementVO.title"/>
		              			</a>
		              			</td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.reimbursementNo"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.payeeName"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.totalAmount"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.status"/></td>
 		              			<%-- <td class="col-sm-1"><a href="personal/getPrintDetail?instanceId=<s:property value='#reimbursementVO.processInstanceID'/>">打印单据</a></td> --%>
		           		              <td class="col-sm-1">				
		           		              	<c:if test="${reimbursementVO.status eq '处理中' && canStopInstance}">
		           		             		<a href="javascript:stopTask('${reimbursementVO.processInstanceID}','报销申请')">
		           		             			<svg class="icon" aria-hidden="true" title="终止流程" data-toggle="tooltip">
													<use xlink:href="#icon-zhongzhi"></use>
												</svg>
		           		             		</a>
		           		             		&nbsp;
							           	</c:if>
							           	<a onclick="goPath('finance/reimbursement/getReimbursementDetail?type=4&processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)">
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
			  </div>
			  <div role="tabpanel" class="tab-pane" id="resignation">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>申请时间</th>
		                  <th>离职日期</th>
		                  <th>离职原因</th>
		                  <th>备注</th>
		                  <th>当前处理人</th>
		                  <th>流程状态</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty resignationVOs}">
		              	<s:set name="resignation_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="resignationVO" value="#request.resignationVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#resignation_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('personal/processHistory?processInstanceID=<s:property value='#resignationVO.processInstanceID'/>&selectedPanel=findProcessList')" href="javascript:void(0)"><s:property value="#resignationVO.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#resignationVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#resignationVO.leaveDate"/></td>
		              			<td class="col-sm-1"><s:property value="#resignationVO.reasons"/></td>
		              			<td class="col-sm-1"><s:property value="#resignationVO.note"/></td>
		              			<td class="col-sm-1"><s:property value="#resignationVO.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#resignationVO.status"/></td>
		              		</tr>
		              		<s:set name="resignation_id" value="#resignation_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	
			  </div>
			  <div role="tabpanel" class="tab-pane" id="formal">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>申请时间</th>
		                  <th>申请转正日期</th>
		                  <th>当前处理人</th>
		                  <th>流程状态</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty formalVOs}">
		              	<s:set name="formal_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="formalVO" value="#request.formalVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#formal_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('personal/processHistory?processInstanceID=<s:property value='#formalVO.processInstanceID'/>&selectedPanel=findProcessList')" href="javascript:void(0)"><s:property value="#formalVO.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#formalVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#formalVO.requestFormalDate"/></td>
		              			<td class="col-sm-1"><s:property value="#formalVO.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#formalVO.status"/></td>
		              		</tr>
		              		<s:set name="formal_id" value="#formal_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="advance">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>申请时间</th>
		                  <th>预付单号</th>
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
		              			<td class="col-sm-1"><a onclick="goPath('personal/processDetail?processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#reimbursementVO.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.reimbursementNo"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.payeeName"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.totalAmount"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.status"/></td>
<%-- 		              			<td class="col-sm-1"><a href="personal/getPrintDetail?instanceId=<s:property value='#reimbursementVO.processInstanceID'/>">打印单据</a></td> --%>
		           					 <td class="col-sm-1">				
		           					 <c:if test="${reimbursementVO.status eq '处理中' && canStopInstance }">
		           		             <a href="javascript:stopTask('${reimbursementVO.processInstanceID}','预约付款')">
           		             			<svg class="icon" aria-hidden="true" title="终止流程" data-toggle="tooltip">
											<use xlink:href="#icon-zhongzhi"></use>
										</svg>
		           		             	&nbsp;
		           		             </a>
							         </c:if>
							         <a onclick="goPath('finance/reimbursement/getAdvanceDetail?type=4&processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)">
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
			  </div>
			  <div role="tabpanel" class="tab-pane" id="payment">
			  	<div class="table-responsive" style="margin-top:30px;">
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
		              			<td class="col-sm-1"><a onclick="goPath('personal/processDetail?processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#reimbursementVO.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.reimbursementNo"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.payeeName"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.totalAmount"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#reimbursementVO.status"/></td>
<%-- 		              			<td class="col-sm-1"><a href="personal/getPrintDetail?instanceId=<s:property value='#reimbursementVO.processInstanceID'/>">打印单据</a></td> --%>
		           					 <td class="col-sm-1">				
		           					 <c:if test="${reimbursementVO.status eq '处理中' && canStopInstance }">
		           		             <a href="javascript:stopTask('${reimbursementVO.processInstanceID}','付款申请')">
		           		             	<svg class="icon" aria-hidden="true" title="终止流程" data-toggle="tooltip">
											<use xlink:href="#icon-zhongzhi"></use>
										</svg>
		           		             	&nbsp;
		           		             </a>
							         </c:if>
							         <a onclick="goPath('finance/reimbursement/getPaymentDetail?type=4&processInstanceID=<s:property value='#reimbursementVO.processInstanceID'/>')" href="javascript:void(0)">
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
			  </div>
			  <div role="tabpanel" class="tab-pane" id="morningMeetingReport">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:15%">标题</th>
		                  <th style="width:15%">发起时间</th>
		                  <th style="width:10%">是否开早会</th>
		                  <th style="width:10%">未开原因</th>
		                  <th style="width:10%">当前处理人</th>
		                  <th style="width:10%">流程状态</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty morningMeetingVos}">
						<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${morningMeetingVos}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td><a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findProcessList')" href="javascript:void(0)">${item.userName}的早会汇报</a></td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			              		<td>${item.hasMeeting}</td>
			              		<td>${item.hasMeeting=='否'?item.noMeetingReason:'——'}</td>
			              		<td>${item.assigneeUserName==null?'——':item.assigneeUserName}</td>
			              		<td>${item.status}</td>
			              		<td>
			              			<c:if test="${item.hasMeeting=='是'}">
			              			<a onclick="goPath('personal/showMorningMeetReport?processInstanceId=${item.processInstanceID}')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看汇报内容" data-toggle="tooltip">
             							<use xlink:href="#icon-Detailedinquiry"></use>
             						</svg>
             						</a>
             						</c:if>
             						<c:if test="${item.hasMeeting=='否'}">
			              			<a href="javascript:void(0)" onclick="showRemark('${item.remark}')">
			              			<svg class="icon" aria-hidden="true" title="查看情况说明" data-toggle="tooltip">
             							<use xlink:href="#icon-shuoming-copy-copy"></use>
             						</svg>
             						</a>
             						</c:if>
			              		</td>
         					 </tr>
		              	</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  </div>
          	  <div role="tabpanel" class="tab-pane" id="workOvertime">
			  	<div class="table-responsive" style="margin-top:30px;">
            		<table class="table table-striped">
		              <thead>
		                <tr>
	                 		<th style="width: 5%">序号</th>
							<th style="width: 10%">标题</th>
							<th style="width: 15%">申请时间</th>
							<th style="width: 10%">开始时间</th>
							<th style="width: 10%">结束时间</th>
							<th style="width: 10%">预计加班工时</th>
							<th style="width: 15%">加班原因</th>
							<th style="width: 10%">当前处理人</th>
							<th style="width: 10%">流程状态</th>
							<th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${workOvertimeVos}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td><a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findProcessList')" href="javascript:void(0)">${item.title}</a></td>
			              		<td>${item.requestDate}</td>
			              		<td>${item.beginDate}</td>
			              		<td>${item.endDate}</td>
			              		<td>${item.workHours}</td>
			              		<td>${item.reason}</td>
			              		<td>${item.assigneeUserName}</td>
			              		<td>${item.status}</td>
			              		<td>
			              			<c:if test="${item.type=='部门'}">
			              				<a href="javascript:void(0)" onclick="showOvertimeUsers('${item.overTimeUsers}')">
	           		             			<svg class="icon" aria-hidden="true" title="查看加班人员" data-toggle="tooltip">
												<use xlink:href="#icon-Detailedinquiry"></use>
											</svg>
		           		             	</a>
			              			</c:if>
			              			<c:if test="${item.type!='部门'}">
			              			——
			              			</c:if>
			              		</td>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
          		</div> 
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
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		    <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
    </div>
  </body>
</html>
