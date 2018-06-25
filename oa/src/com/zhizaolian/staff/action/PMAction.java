package com.zhizaolian.staff.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Getter;
import lombok.Setter;

public class PMAction extends BaseAction{

	@Getter
	@Setter
	private String selectedPanel;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	private String errorMessage;
	@Setter
	private Integer result;  //任务处理结果
	
	@Autowired
	private ProcessService processService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private IdentityService identityService;
	
	private static final long serialVersionUID = 1L;
	
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
		tasks.add(TaskDefKeyEnum.SALARY_SETTLEMENT);
		ListResult<TaskVO> taskListResult = resignationService.findResignationTasksByGroups(groups, tasks, page, limit);
		int count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		
		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "resignationList";
		return "resignationList";
	}
	
	public String auditTask() {
		String taskID = request.getParameter("taskID");
		List<FormField> formFields = processService.getFormFields(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		return "auditTask";
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
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	
		return "resignationComplete";
	}
}
