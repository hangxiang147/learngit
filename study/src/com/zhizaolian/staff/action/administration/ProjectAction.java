package com.zhizaolian.staff.action.administration;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.ProjectInfoEntity;
import com.zhizaolian.staff.entity.ProjectReportInfoEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.ProjectProcessEnum;
import com.zhizaolian.staff.enums.ProjectStatusEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ProjectService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.ProjectInfoVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Getter;
import lombok.Setter;

public class ProjectAction extends BaseAction{
	
	private static final long serialVersionUID = 5101906352450627928L;
	@Setter
	@Getter
	private ProjectInfoVo projectInfoVo;
	@Getter
	private String selectedPanel;
	@Getter
	private String panel;
	@Setter
	private File[] attachment;
	@Setter
	private String[] attachmentFileName; 
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProjectService projectService;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	@Setter
	private ProjectReportInfoEntity projectReport;
	@Autowired
	private ProcessService processService;
	@Autowired
	private RuntimeService runtimeService;
	
	public String findMyProjectList(){
		User user = (User) request.getSession().getAttribute("user");
		ListResult<ProjectInfoVo> projectInfoVos = projectService.findMyProjectList(page, limit, user.getId());
		request.setAttribute("projectInfoVos", projectInfoVos.getList());
		count = projectInfoVos.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0){
			totalPage = 1;
		}
		selectedPanel = "myProjectList";
		return "myProjectList";
	}
	public String findMyaddProjectList(){
		User user = (User)request.getSession().getAttribute("user");
		ListResult<ProjectInfoVo> projectInfoVos = projectService.findMyaddProjectList(page, limit, user.getId());
		request.setAttribute("projectInfoVos", projectInfoVos.getList());
		count = projectInfoVos.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0){
			totalPage = 1;
		}
		selectedPanel = "myAddProject";
		return "findMyAddProjectList";
	}
	public String findAllProjectList(){
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		ListResult<ProjectInfoVo> projectInfoVos = projectService.findAllProjectList(page, limit, beginDate, endDate);
		request.setAttribute("projectInfoVos", projectInfoVos.getList());
		count = projectInfoVos.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0){
			totalPage = 1;
		}
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		selectedPanel = "allProjectList";
		return "allProjectList";
	}
	public String addProject(){
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		selectedPanel = "addProject";
		return "addProject";
	}
	public String startProject(){
		try {
			projectService.startProject(projectInfoVo, attachment, attachmentFileName);
		} catch (Exception e) {
			errorMessage = "项目发起失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			panel = "projectManagement";
			selectedPanel = "addProject";
			return "error";
		}
		selectedPanel = "myAddProject";
		return "render_findMyAddProjectList";
	}
	public String processHistory() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("selectedPanel", request.getParameter("selectedPanel"));
		return "processHistory";
	}
	public String showProjectDetail(){
		String processInstanceID = request.getParameter("processInstanceID");
		ProjectInfoVo projectInfo = projectService.getProjectInfoByInstanceId(processInstanceID);
		request.setAttribute("projectInfo", projectInfo);
		request.setAttribute("selectedPanel", request.getParameter("selectedPanel"));
		return "showProjectDetail";
	}
	public String showTask(){
		String taskName = request.getParameter("taskName");
		User user = (User) request.getSession().getAttribute("user");
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		String processInstanceID = request.getParameter("processInstanceID");
		ProjectInfoVo projectInfo = projectService.getProjectInfoByInstanceId(processInstanceID);
		request.setAttribute("projectInfo", projectInfo);
		request.setAttribute("taskName", taskName);
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("staff", staff);
		selectedPanel = "findTaskList";
		if(ProjectProcessEnum.进度汇报.name().equals(taskName)){
			List<ProjectReportInfoEntity> projectReportInfos = projectService.getProjectReportInfos(projectInfo.getId(), user.getId());
			request.setAttribute("projectReportInfos", projectReportInfos);
			return "showTask";
		}else{
			Map<String, List<ProjectReportInfoEntity>> projectReportInfosMap = projectService.getProjectReportInfosMap(projectInfo.getId(), projectInfo);
			request.setAttribute("projectReportInfosMap", projectReportInfosMap);
			return "showTaskForCheck";
		}
	}
	@Getter
	private int type;
	public String saveProjectReport(){
		String taskId = request.getParameter("taskId");
		try {
			projectService.saveProjectReport(taskId, projectReport, attachment, attachmentFileName);
		} catch (Exception e) {
			errorMessage = "项目进度汇报失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "findTaskList";
			return "render_error";
		}
		selectedPanel = "findTaskList";
		type = BusinessTypeEnum.PROJECT.getValue();
		return "toProjectList";
	}
	public String error(){
		request.setAttribute("errorMessage", request.getParameter("errorMessage"));
		request.setAttribute("selectedPanel", request.getParameter("selectedPanel"));
		return "_error";
	}
	public String taskComplete(){
		String taskId = request.getParameter("taskId");
		String taskName = request.getParameter("taskName");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		String processInstanceID = request.getParameter("processInstanceID");
		try {
			ProjectInfoEntity projectInfo = projectService.getProjectByInstanceId(processInstanceID);
			String finalAuditor = projectInfo.getFinalAuditor();
			User user = (User) request.getSession().getAttribute("user");
			processService.completeTask(taskId, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
			if((StringUtils.isBlank(finalAuditor) && ProjectProcessEnum.项目验收.name().equals(taskName) && TaskResultEnum.AGREE.getValue()==Integer.parseInt(result)) ||
					(StringUtils.isNotBlank(finalAuditor) && ProjectProcessEnum.项目审批.name().equals(taskName)) && TaskResultEnum.AGREE.getValue()==Integer.parseInt(result)){
				projectService.updateProjectStatus(processInstanceID, ProjectStatusEnum.COMPLETE);
			}else if(StringUtils.isNotBlank(finalAuditor) && ProjectProcessEnum.项目验收.name().equals(taskName) && TaskResultEnum.AGREE.getValue()==Integer.parseInt(result)){
				projectService.updateProjectStatus(processInstanceID, ProjectStatusEnum.AUDIT);
			}else if(TaskResultEnum.DISAGREE.getValue()==Integer.parseInt(result)){
				projectService.updateProjectStatus(processInstanceID, ProjectStatusEnum.PROGRESS);
				//修改汇报人之前的完成状态为进行中
				projectService.modifyProjectReportStatus(projectInfo.getId());
			}
		} catch (Exception e) {
			errorMessage = "处理失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			selectedPanel = "findTaskList";
			return "render_error";
		}
		selectedPanel = "findTaskList";
		type = BusinessTypeEnum.PROJECT.getValue();
		return "toProjectList";
	}
	public String showProjectProcess(){
		String processInstanceID = request.getParameter("processInstanceID");
		ProjectInfoVo projectInfo = projectService.getProjectInfoByInstanceId(processInstanceID);
		Map<String, List<ProjectReportInfoEntity>> projectReportInfosMap = projectService.getProjectReportInfosMap(projectInfo.getId(), projectInfo);
		request.setAttribute("projectReportInfosMap", projectReportInfosMap);
		request.setAttribute("selectedPanel", request.getParameter("selectedPanel"));
		return "showProjectProcess";
	}
	public void stopProject(){
		Map<String, Object> resultMap = new HashMap<>();
		String processInstanceID = request.getParameter("processInstanceID");
		if (!projectService.checkProjectHasProcess(processInstanceID)) {
			runtimeService.deleteProcessInstance(processInstanceID, "complete");
			projectService.updateProjectStatus(processInstanceID, ProjectStatusEnum.END);
			resultMap.put("success", true);
		} else {
			resultMap.put("success", false);
		}
		printByJson(resultMap);
	}
	public String toAddProject(){
		selectedPanel = "addProject";
		return "toAddProject";
	}
}
