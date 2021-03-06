package com.zhizaolian.staff.action.HR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;


import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.enums.AuditStatusEnum;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.FormalTypeEnum;
import com.zhizaolian.staff.enums.StaticResource;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.FormalService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.SocialSecurityService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.FormalVO;
import com.zhizaolian.staff.vo.GradeVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.HousingFundVO;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;
import com.zhizaolian.staff.vo.SocialSecurityVO;
import com.zhizaolian.staff.vo.StaffAuditVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.VacationTaskVO;
import com.zhizaolian.staff.vo.VacationVO;
import com.zhizaolian.staff.vo.ViewWorkReportVo;
import com.zhizaolian.staff.vo.WorkOvertimeTaskVo;

public class ProcessAction extends BaseAction {
	
	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	@Setter
	private String panel;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer type;
	@Getter
	private Integer totalPage;
	@Getter
	private String errorMessage;
	@Setter
	private Integer result;  //任务处理结果
	@Setter
	@Getter
	private FormalVO formalVO;
	@Setter
	@Getter
	private SocialSecurityProcessVO socialSecurityProcessVO;
	@Getter
	@Setter
	private SocialSecurityVO socialSecurityVO;
	@Setter
	@Getter
	private HousingFundVO housingFundVO;
	@Setter
	@Getter
	private String taskID;
	
	@Autowired
	private IdentityService identityService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormalService formalService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private SocialSecurityService socialSecurityService;
	@Autowired
	private WorkOvertimeService workOvertimeService;
	@Autowired
	private ViewReportService viewReportService;
	
	private static final long serialVersionUID = 1L;
	
	public String findVacationList() {
		try{
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				errorMessage = "您尚未登录，请先登录！";
				return "error";
			}
			
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			if (groups.size() <= 0) {
				selectedPanel = "vacationList";
				return "vacationList";
			}
			
			ListResult<VacationTaskVO> taskListResult = vacationService.findVacationTasksByGroups(groups, page, limit);
			count = taskListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			
			request.setAttribute("taskVOs", taskListResult.getList());
			selectedPanel = "vacationList";
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		
		return "vacationList";
	}
	
	public String findResignationList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "resignationList";
			return "resignationList";
		}
		
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.HR_AUDIT);
		tasks.add(TaskDefKeyEnum.RESIGNATION_TRANSFER);
		ListResult<TaskVO> taskListResult = resignationService.findResignationTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		
		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "resignationList";
		return "resignationList";
	}
	
	public String findSocialSecurityAuditList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "socialSecurityList";
			return "socialSecurityAuditList";
		}
		
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.SS_HR_UPDATE);
		tasks.add(TaskDefKeyEnum.SS_FOLLOW_UP);
		ListResult<TaskVO> taskListResult = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "socialSecurityList";
		return "socialSecurityAuditList";
	}
	
	public String findAuditList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "auditList";
			return "auditList";
		}
		
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.AUDIT_HR_AUDIT);
		ListResult<TaskVO> taskListResult = staffService.findAuditTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		
		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "auditList";
		return "auditList";
	}
	
	public String findFormalList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "formalList";
			return "formalList";
		}
		
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.FORMAL_INVITATION);
		tasks.add(TaskDefKeyEnum.FORMAL_HR_AUDIT);
		ListResult<TaskVO> taskListResult = formalService.findFormalTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		
		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "formalList";
		return "formalList";
	}
	public String findWorkOvertimeList() {
		try{
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				errorMessage = "您尚未登录，请先登录！";
				return "error";
			}
			
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			if (groups.size() <= 0) {
				selectedPanel = "workOvertimeList";
				return "workOvertimeList";
			}
			
			ListResult<WorkOvertimeTaskVo> taskListResult = workOvertimeService.findWorkOvertimeTasksByGroups(groups, page, limit);
			count = taskListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			
			request.setAttribute("taskVOs", taskListResult.getList());
			selectedPanel = "workOvertimeList";
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		return "workOvertimeList";
	}
	public String findViewReportList() {
		try{
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				errorMessage = "您尚未登录，请先登录！";
				return "error";
			}
			
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			if (groups.size() <= 0) {
				selectedPanel = "viewReportList";
				return "viewReportList";
			}
			
			ListResult<ViewWorkReportVo> taskListResult = viewReportService.findViewReportTasksByGroups(groups, page, limit);
			count = taskListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			request.setAttribute("taskVOs", taskListResult.getList());
			selectedPanel = "viewReportList";
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "viewReportList";
	}
	public String auditBackground() {
		String taskID = request.getParameter("taskID");
		List<CommentVO> comments = processService.getComments(taskID);
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		StaffAuditVO staffAuditVO = (StaffAuditVO) runtimeService.getVariable(processInstance.getId(), "arg");
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("comments", comments);
		request.setAttribute("staffAuditVO", staffAuditVO);
		request.setAttribute("taskID", taskID);
		selectedPanel = "auditList";
		return "auditBackground";
	}
	
	public String saveAuditResult() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
			
		String taskID = request.getParameter("taskID");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			StaffAuditVO staffAuditVO = (StaffAuditVO) runtimeService.getVariable(pInstance.getId(), "arg");
			//完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), null);
			StaffVO staffVO = staffService.getStaffByUserID(staffAuditVO.getAuditUserID());
			AuditStatusEnum auditStatusEnum = result==TaskResultEnum.AGREE.getValue()? AuditStatusEnum.APPROVED : AuditStatusEnum.NOT_APPROVED;
			staffService.updateAuditResult(String.valueOf(staffVO.getStaffID()), auditStatusEnum);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.AUDIT.getValue();
		return "auditComplete";
	}
	
	public String auditTask() {
		String taskID = request.getParameter("taskID");
		List<FormField> formFields = processService.getFormFields(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		return "auditTask";
	}
	
	public String sendInvitation() {
		String taskID = request.getParameter("taskID");
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		FormalVO arg = (FormalVO) runtimeService.getVariable(processInstance.getId(), "arg");
		List<GradeVO> gradeVOs = positionService.findAllGrades();
		request.setAttribute("gradeVOs", gradeVOs);
		request.setAttribute("userName", arg.getRequestUserName());
		request.setAttribute("taskID", taskID);
		selectedPanel = "formalList";
		return "sendInvitation";
	}
	
	public String sendFormalInvitation() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		Integer staffID = Integer.parseInt(request.getParameter("staffID"));
		StaffVO staffVO = staffService.getStaffByStaffID(staffID);
		List<GradeVO> gradeVOs = positionService.findAllGrades();
		request.setAttribute("gradeVOs", gradeVOs);
		request.setAttribute("staffVO", staffVO);
		request.setAttribute("staffID", staffID);
		request.setAttribute("userName", staffService.getStaffByUserID(user.getId()).getLastName());
		selectedPanel = "staffWarn";
		return "sendFormalInvitation";
	}
	
	public String startFormal() {
		try {
			String[] gradeList = formalVO.getGradeName().split("_");
			formalVO.setGradeID(Integer.valueOf(gradeList[0]));
			formalVO.setGradeName(gradeList[1]);
			formalService.startFormal(formalVO, FormalTypeEnum.INVITATION.getValue());
			String staffID = request.getParameter("staffID");
			//去除已邀请的
			Iterator<Entry<Integer, List<StaffVO>>> ite = StaticResource.companyIdAndformalStaffVosMap.entrySet().iterator();
			User user = (User) request.getSession().getAttribute("user");
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(user.getId());
			//操作人所属的公司
			Set<Integer> companyIds = new HashSet<>();
			for(GroupDetailVO groupDetail: groupDetails){
				companyIds.add(groupDetail.getCompanyID());
			}
			boolean flag = false;
			while(ite.hasNext()){
				Entry<Integer, List<StaffVO>> entry = ite.next();
				if(!companyIds.contains(entry.getKey())){
					continue;
				}
				List<StaffVO> staffVos = entry.getValue();
				Iterator<StaffVO> it = staffVos.iterator();
				while(it.hasNext()){
					if(Integer.parseInt(staffID) == it.next().getStaffID()){
						it.remove();
						flag = true;
						break;
					}
				} 
				if(flag){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "转正邀请发送失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "startFormal";
	}
	
	public String taskComplete() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String businessType = request.getParameter("businessType");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			//完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			//更新业务表的流程节点状态processStatus
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			if (BusinessTypeEnum.VACATION.getName().equals(businessType) && 
					TaskResultEnum.valueOf(result)!=null && result==TaskResultEnum.AGREE.getValue()) {
				//请假申请审批通过时，再次检查考勤数据统计
				VacationVO vacationVO = vacationService.getVacationByProcessInstanceID(pInstance.getId());
				attendanceService.checkAttendanceDetailsByVacationVO(vacationVO);
			}
			if(BusinessTypeEnum.VIEW_REPORT.getName().equals(businessType) &&
					TaskResultEnum.valueOf(result)!=null && result==TaskResultEnum.AGREE.getValue()){
				viewReportService.saveViewReportRight(pInstance.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.valueOfName(businessType).getValue();
		return "HRCenter";
		//return getReturnString(businessType);
	}
	
	public String formalInvitation() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		try {
			ProcessInstance pinInstance = processService.getProcessInstance(taskID);
			FormalVO arg = (FormalVO) runtimeService.getVariable(pinInstance.getId(), "arg");
			String[] gradeList = formalVO.getGradeName().split("_");
			arg.setGradeID(Integer.valueOf(gradeList[0]));
			arg.setGradeName(gradeList[1]);
			formalVO.setGradeID(arg.getGradeID());
			formalVO.setGradeName(arg.getGradeName());
			arg.setSalary(formalVO.getSalary());
			arg.setSocialSecurity(formalVO.getSocialSecurity());
			runtimeService.setVariable(pinInstance.getId(), "arg", arg);
			//完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			//更新业务表的数据
			formalService.updateFormal(formalVO, pinInstance.getId(), TaskResultEnum.valueOf(result));
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "发送邀请失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.FORMAL.getValue();
		return "formalComplete";
	}
	
/*	private String getReturnString(String businessType) {
		if (BusinessTypeEnum.VACATION.getName().equals(businessType)) {
			return "vacationComplete";
		} else if (BusinessTypeEnum.RESIGNATION.getName().equals(businessType)) {
			return "resignationComplete";
		} else if (BusinessTypeEnum.FORMAL.getName().equals(businessType)) {
			return "formalComplete";
		} else if (BusinessTypeEnum.WORK_OVERTIME.getName().equals(businessType)) {
			return "workOvertimeComplete";
		} else if (BusinessTypeEnum.VIEW_REPORT.getName().equals(businessType)) {
			return "viewWorkReportComplete";
		}
		return "error";
	}*/

	public String processHistory() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("fromDangAn", true);
		request.setAttribute("selectedPanel", request.getParameter("selectedPanel"));
		return "processHistory";
	}
	
	public String showSocialSecurityDiagram() {
		selectedPanel = "socialSecurityList";
		return "socialSecurityDiagram";
	}
	
	public String newSocialSecurity() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "newSocialSecurity";
			return "newSocialSecurity";
		} 
		
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		request.setAttribute("companyID", companyID);
		request.setAttribute("ssYear", request.getParameter("ssYear"));
		request.setAttribute("ssMonth", request.getParameter("ssMonth"));
		request.setAttribute("hfYear", request.getParameter("hfYear"));
		request.setAttribute("hfMonth", request.getParameter("hfMonth"));
		
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		selectedPanel = "socialSecurityList";
		return "newSocialSecurity";
	}
	
	public String startSocialSecurity() {
		try {
			socialSecurityService.startSocialSecurity(socialSecurityProcessVO);
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "社保审核名单提交失败："+e.getMessage();
			selectedPanel = "newSocialSecurity";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		return "startSocialSecurity";
	}
	
	public String findSocialSecurityList() {
		try {
//			int count = 0;
			ListResult<SocialSecurityProcessVO> sspListResult = socialSecurityService.findSocialSecurityProcessListByPage(page, limit);
			request.setAttribute("socialSecurityProcessVOs", sspListResult.getList());
			count = sspListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		selectedPanel = "socialSecurityList";
		return "socialSecurityList";
	}
	
	public String getSocialSecurityHistory() {
		String processInstanceID = request.getParameter("processInstanceID");
		SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
		List<SocialSecurityVO> socialSecurityVOs = socialSecurityService.findSocialSecurityListByProcessID(socialSecurityProcessVO.getSspID());
		for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
			socialSecurityVO.setUserName(staffService.getStaffByUserID(socialSecurityVO.getUserID()).getLastName());
		}
		request.setAttribute("socialSecurityVOs", socialSecurityVOs);
		List<HousingFundVO> housingFundVOs = socialSecurityService.findHousingFundListByProcessID(socialSecurityProcessVO.getSspID());
		for (HousingFundVO housingFundVO : housingFundVOs) {
			housingFundVO.setUserName(staffService.getStaffByUserID(housingFundVO.getUserID()).getLastName());
		}
		request.setAttribute("housingFundVOs", housingFundVOs);
		
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		
		request.setAttribute("socialSecurityProcessVO", socialSecurityProcessVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel = "socialSecurityList";
		return "socialSecurityHistory";
	}
	
	public String updateSocialSecurity() {
		String taskID = request.getParameter("taskID");
		try {
			String processInstanceID = processService.getProcessInstance(taskID).getId();
			SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
			List<SocialSecurityVO> socialSecurityVOs = socialSecurityService.findSocialSecurityListByProcessID(socialSecurityProcessVO.getSspID());
			for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
				socialSecurityVO.setUserName(staffService.getStaffByUserID(socialSecurityVO.getUserID()).getLastName());
			}
			request.setAttribute("socialSecurityVOs", socialSecurityVOs);
			List<HousingFundVO> housingFundVOs = socialSecurityService.findHousingFundListByProcessID(socialSecurityProcessVO.getSspID());
			for (HousingFundVO housingFundVO : housingFundVOs) {
				housingFundVO.setUserName(staffService.getStaffByUserID(housingFundVO.getUserID()).getLastName());
			}
			request.setAttribute("housingFundVOs", housingFundVOs);
			
			List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
			List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
			
			request.setAttribute("socialSecurityProcessVO", socialSecurityProcessVO);
			request.setAttribute("comments", comments);
			request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取名单信息失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		request.setAttribute("taskID", taskID);
		selectedPanel = "socialSecurityList";
		return "updateSocialSecurity";
	}
	
	public String addSocialSecurity() {
		request.setAttribute("year", request.getParameter("year"));
		request.setAttribute("month", request.getParameter("month"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		request.setAttribute("companyID", request.getParameter("companyID"));
		request.setAttribute("ssYear", request.getParameter("ssYear"));
		request.setAttribute("ssMonth", request.getParameter("ssMonth"));
		selectedPanel = "socialSecurityList";
		return "addSocialSecurity";
	}
	
	public String addHousingFund() {
		request.setAttribute("year", request.getParameter("year"));
		request.setAttribute("month", request.getParameter("month"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		request.setAttribute("companyID", request.getParameter("companyID"));
		request.setAttribute("hfYear", request.getParameter("hfYear"));
		request.setAttribute("hfMonth", request.getParameter("hfMonth"));
		selectedPanel = "socialSecurityList";
		return "addHousingFund";
	}
	
	/**
	 * 保存社保明细
	 * @return
	 */
	public String saveHousingFund() {
		String processInstanceID = processService.getProcessInstance(taskID).getId();
		SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
		if (housingFundVO.getCompanyID() == null) {
			housingFundVO.setProcessID(socialSecurityProcessVO.getSspID());
			housingFundVO.setPaymentYear(socialSecurityProcessVO.getSsYear());
			housingFundVO.setPaymentMonth(socialSecurityProcessVO.getSsMonth());
			housingFundVO.setCompanyID(socialSecurityProcessVO.getCompanyID());
		}
		
		try {
			//保存员工社保缴纳明细
			socialSecurityService.save(housingFundVO);
			//修改社保审核流程合计参数
			updateProcessHFCount(processInstanceID);
		} catch (Exception e) {
			e.printStackTrace();
			panel = "process";
			errorMessage = "保存失败："+e.getMessage();
			selectedPanel = "socialSecurityList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "saveHousingFund";
	}
	
	public String saveSocialSecurity() {
		String processInstanceID = processService.getProcessInstance(taskID).getId();
		SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
		if (socialSecurityVO.getCompanyID() == null) {
			socialSecurityVO.setProcessID(socialSecurityProcessVO.getSspID());
			socialSecurityVO.setPaymentYear(socialSecurityProcessVO.getYear());
			socialSecurityVO.setPaymentMonth(socialSecurityProcessVO.getMonth());
			socialSecurityVO.setCompanyID(socialSecurityProcessVO.getCompanyID());
		}
		
		try {
			//保存员工公积金缴纳明细
			socialSecurityService.save(socialSecurityVO);
			//修改社保审核流程合计参数
			updateSocialSecurityProcessCount(processInstanceID);
		} catch (Exception e) {
			e.printStackTrace();
			panel = "process";
			errorMessage = "保存失败："+e.getMessage();
			selectedPanel = "socialSecurityList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		
		return "saveSocialSecurity";
	}
	
	private void updateProcessHFCount(String processInstanceID) {
		SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
		List<HousingFundVO> housingFundVOs = socialSecurityService.findHousingFundListByProcessID(socialSecurityProcessVO.getSspID());
		
		double totalCount = 0;
		for (HousingFundVO housingFundVO : housingFundVOs ) {
			totalCount += housingFundVO.getTotalCount();
		}
		socialSecurityService.updateProcessHFCount(processInstanceID, totalCount);
	}
	
	private void updateSocialSecurityProcessCount(String processInstanceID) {
		SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
		List<SocialSecurityVO> socialSecurityVOs = socialSecurityService.findSocialSecurityListByProcessID(socialSecurityProcessVO.getSspID());
		
		double personalCount=0, companyCount=0;
		for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
			personalCount += socialSecurityVO.getPersonalProvidentFund();
			companyCount += socialSecurityVO.getCompanyProvidentFund();
		}
		socialSecurityService.updateProcessCount(processInstanceID, personalCount, companyCount);
	}
	
	public String deleteHousingFund() {
		int hfID = Integer.parseInt(request.getParameter("hfID"));
		String taskID = request.getParameter("taskID");
		try {
			socialSecurityService.deleteHousingFundByID(hfID);
			//修改审核流程社保合计参数
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			updateProcessHFCount(pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "deleteHousingFund";
	}
	
	public String deleteSocialSecurity() {
		int ssID = Integer.parseInt(request.getParameter("ssID"));
		String taskID = request.getParameter("taskID");
		try {
			socialSecurityService.deleteSocialSecurityByID(ssID);
			//修改审核流程公积金合计参数
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			updateSocialSecurityProcessCount(pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		
		return "deleteSocialSecurity";
	}
	
	public String completeUpdateSocialSecurity() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		try {
			//完成任务
			processService.completeTask(taskID, user.getId(), null, comment);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.SOCIAL_SECURITY.getValue();
		return "completeUpdateSocialSecurity";
	}
	
}
