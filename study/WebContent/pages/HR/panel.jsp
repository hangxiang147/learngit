<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'dangan'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <%-- <li <c:if test="${selectedPanel == 'newStaff' }">class="active"</c:if>><a onclick="goPath('HR/staff/newStaff')" href="javascript:void(0)">人事档案录入</a></li> --%>
	  <li <c:if test="${selectedPanel == 'staffManage' }">class="active"</c:if>><a onclick="goPath('HR/staff/findStaffList')" href="javascript:void(0)">人事档案管理</a></li>
	  <li <c:if test="${selectedPanel == 'staffAudit' }">class="active"</c:if>><a onclick="goPath('HR/staff/findStaffAuditList')" href="javascript:void(0)">背景调查</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'postCredentialApply' }">class="active"</c:if>><a onclick="goPath('HR/staff/showPostCredentialApply')" href="javascript:void(0)">证书审核</a></li> --%>
	  <li <c:if test="${selectedPanel == 'postCredentialApplyList' }">class="active"</c:if>><a onclick="goPath('HR/staff/postCredentialApplyRecord')" href="javascript:void(0)">证书审核列表</a></li>
	 <%--  <li <c:if test="${param.selectedPanel == 'countStaff' }">class="active"</c:if>><a href="pages/HR/blank.jsp?selectedPanel=countStaff&panel=dangan">人事统计</a></li> --%>
	  <%-- <li <c:if test="${selectedPanel == 'staffWarn' }">class="active"</c:if>><a onclick="goPath('HR/staff/staffWarnList')" href="javascript:void(0)">转正提醒</a></li> --%>
	  <%-- <li <c:if test="${selectedPanel == 'staffRegularRecord' }">class="active"</c:if>><a onclick="goPath('HR/staff/findStaffRegularRecord')" href="javascript:void(0)">转正记录查询</a></li> --%>
	  <li <c:if test="${selectedPanel == 'formalStaffApplicationList' }">class="active"</c:if>><a onclick="goPath('HR/staff/findFormalStaffApplicationList')" href="javascript:void(0)">转正管理</a></li>
	  <li <c:if test="${selectedPanel == 'resignationList' }">class="active"</c:if>><a onclick="goPath('HR/staff/findResignationList')" href="javascript:void(0)">离职管理</a></li>
	  <li <c:if test="${selectedPanel == 'staffSpecial' }">class="active"</c:if>><a onclick="goPath('HR/staff/findSpecialList')" href="javascript:void(0)">蓝白岗位管理</a></li>
	  <li <c:if test="${selectedPanel == 'staffSkill' }">class="active"</c:if>><a onclick="goPath('HR/staff/findSkillList')" href="javascript:void(0)">员工技能管理</a></li>
	  <li <c:if test="${selectedPanel == 'getLastName' }">class="active"</c:if>><a onclick="goPath('HR/staff/getLastName')" href="javascript:void(0)">花名查询</a></li>
	  <li <c:if test="${selectedPanel == 'getNameByTelephone' }">class="active"</c:if>><a onclick="goPath('HR/staff/getNameByTelephone')" href="javascript:void(0)">手机尾号查询</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'insuranceList' }">class="active"</c:if>><a onclick="goPath('HR/staff/getInsuranceList')" href="javascript:void(0)">员工保险名单</a></li>	 --%>
	  
	  <!--账户登录密码加密功能  -->
	  <%-- <auth:hasPermission name="encryption">
	  <li <c:if test="${selectedPanel == 'encryption' }">class="active"</c:if>><a onclick="goPath('/encryption')" href="javascript:void(0)" style="color:red;">更新密码</a></li>
	  </auth:hasPermission> --%>
	
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'position'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'findPositionList' }">class="active"</c:if>><a onclick="goPath('HR/position/findPositionList')" href="javascript:void(0)">编辑岗位</a></li>
	  <li <c:if test="${selectedPanel == 'findStaffList' }">class="active"</c:if>><a onclick="goPath('HR/position/findStaffList')" href="javascript:void(0)">员工调动</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'positionHistory' }">class="active"</c:if>><a onclick="goPath('HR/position/positionHistoryStaffList')" href="javascript:void(0)">员工调动历史查询</a></li> --%>
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'infoAlteration'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'gradeAlteration' }">class="active"</c:if>><a onclick="goPath('HR/staffInfoAlteration/gradeAlteration')" href="javascript:void(0)">职级调整</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'salaryAlteration' }">class="active"</c:if>><a onclick="goPath('HR/staffInfoAlteration/salaryAlteration')" href="javascript:void(0)">薪资调整</a></li> --%>
	  <li <c:if test="${selectedPanel == 'salaryAlteration' }">class="active"</c:if>><a onclick="goPath('HR/staff/findAllInJobStaffs')" href="javascript:void(0)">薪资调整</a></li>
	  <li <c:if test="${selectedPanel == 'socialSecurityList' or param.selectedPanel == 'socialSecurityList' }">class="active"</c:if>><a onclick="goPath('HR/process/findSocialSecurityList')" href="javascript:void(0)">社保缴纳</a></li>
	  <li <c:if test="${selectedPanel == 'socialSecurityInfo'}">class="active"</c:if>><a onclick="goPath('HR/staffSalary/findSocialSecurityInfos')" href="javascript:void(0)">标准社保维护</a></li>
	  <li <c:if test="${selectedPanel == 'batchImportSalary'}">class="active"</c:if>><a onclick="goPath('HR/staffSalary/toBatchImportSalary')" href="javascript:void(0)">批量导入人员薪资</a></li>
	</ul>
</div>  

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'process'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'vacationList' or param.selectedPanel == 'vacationList'}">class="active"</c:if>><a onclick="goPath('HR/process/findVacationList')" href="javascript:void(0)">请假申请   <span class="badge" style="background-color:red;<c:if test='${vacationCount == 0 }'>display:none;</c:if>">${vacationCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'resignationList' }">class="active"</c:if>><a onclick="goPath('HR/process/findResignationList')" href="javascript:void(0)">离职申请   <span class="badge" style="background-color:red;<c:if test='${resignationHRCount == 0 }'>display:none;</c:if>">${resignationHRCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'formalList' }">class="active"</c:if>><a onclick="goPath('HR/process/findFormalList')" href="javascript:void(0)">转正申请   <span class="badge" style="background-color:red;<c:if test='${formalHRCount == 0 }'>display:none;</c:if>">${formalHRCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'workOvertimeList' }">class="active"</c:if>><a onclick="goPath('HR/process/findWorkOvertimeList')" href="javascript:void(0)">加班申请   <span class="badge" style="background-color:red;<c:if test='${workOvertimeCount == 0 }'>display:none;</c:if>">${workOvertimeCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'auditList' }">class="active"</c:if>><a onclick="goPath('HR/process/findAuditList')" href="javascript:void(0)">背景调查   <span class="badge" style="background-color:red;<c:if test='${auditHRCount == 0 }'>display:none;</c:if>">${auditHRCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'viewReportList' }">class="active"</c:if>><a onclick="goPath('HR/process/findViewReportList')" href="javascript:void(0)">查看日报申请 <span class="badge" style="background-color:red;<c:if test='${viewReportCount == 0 }'>display:none;</c:if>">${viewReportCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'socialSecurityList' or param.selectedPanel == 'socialSecurityList' }">class="active"</c:if>><a onclick="goPath('HR/process/findSocialSecurityAuditList')" href="javascript:void(0)">社保缴纳审核   <span class="badge" style="background-color:red;<c:if test='${socialSecurityHRCount == 0 }'>display:none;</c:if>">${socialSecurityHRCount }</span></a></li>
	</ul>
</div> 

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'contract'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'contractList' or param.selectedPanel == 'contractList' }">class="active"</c:if>><a onclick="goPath('HR/contract/findContractList')" href="javascript:void(0)">合同管理</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'remindCtrList' }">class="active"</c:if>><a onclick="goPath('HR/contract/findRemindCtrList')" href="javascript:void(0)">合同到期提醒</a></li> --%>
	</ul>
</div> 

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'querys'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'telephoneQuery' }">class="active"</c:if>><a onclick="goPath('HR/staff/getNameByTelephone?isSelf=1')" href="javascript:void(0)">尾号查询</a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'HRCenter'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
		<li <c:if test="${selectedPanel == 'HRCenter' }">class="active"</c:if>><a onclick="goPath('/HRCenter/goHRCenter')" href="javascript:void(0)">智能人事</a></li>
		<li <c:if test="${selectedPanel == 'sendShortMessages' }">class="active"</c:if>>
			<a onclick="goPath('/HRCenter/massTexting')" href="javascript:void(0)">
				短信群发功能
			</a>
		</li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'PmManage'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'workReport' }">class="active"</c:if>><a onclick="goPath('workReport/findWorkReportList')" href="javascript:void(0)">工作汇报管理</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'rewardAndPunishmentList' }">class="active"</c:if>><a onclick="goPath('PM/process/findRewardAndPunishmentList')" href="javascript:void(0)">行政奖惩</a></li> --%>
	</ul>
</div> 
