package com.zhizaolian.staff.action.HR;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.JobEntity;
import com.zhizaolian.staff.entity.VitaeEntity;
import com.zhizaolian.staff.entity.VitaeResultEntity;
import com.zhizaolian.staff.entity.VitaeSignEduEntity;
import com.zhizaolian.staff.entity.VitaeSignEntity;
import com.zhizaolian.staff.entity.VitaeSignFamilyEntity;
import com.zhizaolian.staff.entity.VitaeSignJobHistoryEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VitaeService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.VitaeTaskVo;
import com.zhizaolian.staff.vo.VitaeVo;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VitaeAction extends BaseAction {
	private static final long serialVersionUID = 1L;
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
	private JobEntity jobEntity;
	@Autowired
	private VitaeService vitaeService;
	@Getter
	@Setter
	private VitaeVo vitaeVo;
	@Getter
	@Setter
	private String type;
	@Autowired
	private ProcessService processService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Getter
	@Setter
	private File[] files;
	@Getter
	@Setter
	private String fileDetail;
	@Getter
	@Setter
	private Integer result;
	@Getter
	@Setter
	private String comment;
	@Autowired
	private IdentityService identityService;
	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	@Setter
	private VitaeSignEntity vitaeSignEntity;
	
	@Autowired
	private StaffService staffService;
	@Getter
	private String errorMessage;

	public String toJobList() {
		String name = request.getParameter("name");
		ListResult<JobEntity> jobs = vitaeService.getJobEntityListMore(name, page,
				limit);
		request.setAttribute("name", name);
		count = jobs.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("resultList", jobs.getList());
		User user = (User)request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int confirmMsgTaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_COMPLETEBASICMSG.getName()).count();
		request.getSession().setAttribute("confirmMsgTaskCount", confirmMsgTaskCount);
		int startVitaeTaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_START_INVITE.getName()).count();
		request.getSession().setAttribute("startVitaeTaskCount", startVitaeTaskCount); 
		int step1TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP1.getName()).count();
		request.getSession().setAttribute("step1TaskCount", step1TaskCount); 
		int step2TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP2.getName()).count();
		request.getSession().setAttribute("step2TaskCount", step2TaskCount); 
		int step3TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP3.getName()).count();
		request.getSession().setAttribute("step3TaskCount", step3TaskCount); 
		int step4TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP4.getName()).count();
		request.getSession().setAttribute("step4TaskCount", step4TaskCount); 
		return "toJobList";
	}
	
	

	public String toJobAddList() {
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id))
			jobEntity = vitaeService.getJobEntityById(id);
		return "toJobAddList";
	}
	
	public String toJobViewList(){
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id))
			jobEntity = vitaeService.getJobEntityById(id);
		return "toJobViewList";
	}
	public void getAllJob() {
		ListResult<JobEntity> jobs = vitaeService.getJobEntityList(null, 1,
				Integer.MAX_VALUE);
		printByJson(jobs);
	}
	public String saveJob() {
		if (jobEntity.getId() != null) {
			vitaeService.updateJob(jobEntity);
		} else {
			vitaeService.addJob(jobEntity);
		}
		return "render_toJobList";
	}

	public String deleteJob(){
		String id=request.getParameter("id");
		vitaeService.deleteJob(id);
		return "render_toJobList";
	}
	public String startVitae() {
		try {
			vitaeService.startVitae(vitaeVo, files, fileDetail);
		} catch (Exception e) {
			e.printStackTrace(); 
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("招聘申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.VITAE.getValue() + "";
		return "render_showMyVitae";
	}

	public String toConfirmVitae() {
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<FormField> formFields = processService.getFormFields(taskId);
		vitaeVo = (VitaeVo) runtimeService.getVariable(pInstance.getId(),
				"arg");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pInstance.getProcessDefinitionId())
				.singleResult();
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(pInstance.getId());
		List<Attachment> attas = taskService
				.getProcessInstanceAttachments(pInstance.getId());
		List<CommentVO> comments = processService.getComments(taskId);
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskId", taskId);
		return "confirmVitae";
	}

	public String confirmVitae() {
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskID");
		//如果失败 那么 记录到业务表中
		if(result==2){
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			vitaeService.updateVitaeStatusByInstanceId(pInstance.getId(), 2);
		}
		try {
			processService.completeTask(taskID, user.getId(),
					TaskResultEnum.valueOf(result), comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		type = BusinessTypeEnum.VITAE.getValue() + "";
		
		return "render_showSubjectVitae";
	}

	public String toFixVitaeList() {
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "toFixVitaeList";
			return "toFixVitaeList";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_COMPLETEBASICMSG);
		ListResult<VitaeTaskVo> taskListResult = vitaeService
				.findVitaeVoByUserGroupIDs(tasks, groups,
						Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int confirmMsgTaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_COMPLETEBASICMSG.getName()).count();
		request.getSession().setAttribute("confirmMsgTaskCount", confirmMsgTaskCount);
		selectedPanel = "toFixVitaeList";
		return "toFixVitaeList";
	}

	public String toFixVitae() {
		selectedPanel = "toFixVitaeList";
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<FormField> formFields = processService.getFormFields(taskId);
		vitaeVo = (VitaeVo) runtimeService.getVariable(pInstance.getId(),
				"arg");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pInstance.getProcessDefinitionId())
				.singleResult();
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(pInstance.getId());
		List<Attachment> attas = taskService
				.getProcessInstanceAttachments(pInstance.getId());
		List<CommentVO> comments = processService.getComments(taskId);
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskId", taskId);
		return "toFixVitae";
	}

	public String completeVitae() {
		User user = (User) request.getSession().getAttribute("user");
		String postKey = request.getParameter("postKey");
		String taskId = request.getParameter("taskId");
		String ids = request.getParameter("ids");
		String names = request.getParameter("names");
		String tIds = request.getParameter("tIds");
		String tNames = request.getParameter("tNames");
		String name = request.getParameter("name");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		vitaeService.completeVitae(pInstance.getId(), name, postKey, ids,
				names,tIds,tNames);
		try {
			processService.completeTask(taskId, user.getId(), null, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		VitaeVo arg = (VitaeVo) runtimeService.getVariable(pInstance.getId(), "arg");
		arg.setRealPostName(name);
		runtimeService.setVariable(pInstance.getId(), "arg", arg);
		return "render_toFixVitaeList";
	}

	/**
	 * 开始人员邀请list
	 * 
	 * @return
	 */
	public String toStartVitaeList() {
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "toStartVitae";
			return "toStartVitae";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_START_INVITE);
		ListResult<VitaeTaskVo> taskListResult = vitaeService
				.findVitaeVoByUserGroupIDs(tasks, groups,
						Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int startVitaeTaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_START_INVITE.getName()).count();
		request.getSession().setAttribute("startVitaeTaskCount", startVitaeTaskCount);
		selectedPanel = "toStartVitaeList";
		return "toStartVitaeList";
	}

	public String toStartVitaePage() {
		selectedPanel = "toStartVitaeList";
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<FormField> formFields = processService.getFormFields(taskId);
		String itemNumber= null;
		String effectiveNumber =null;
		for (int i = 0; i < formFields.size(); i++) {
			if("所需人数".equals(formFields.get(i).getFieldText())){
				itemNumber=formFields.get(i).getFieldValue();
			}
			if("已录用人数".equals(formFields.get(i).getFieldText())){
				effectiveNumber=formFields.get(i).getFieldValue();
			}
		}
		if(StringUtils.isNotBlank(itemNumber)){
			if(itemNumber.equals(effectiveNumber)){
				request.setAttribute("isMax", "true");
			}
		}
		
		vitaeVo = (VitaeVo) runtimeService.getVariable(pInstance.getId(),
				"arg");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pInstance.getProcessDefinitionId())
				.singleResult();
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(pInstance.getId());
		List<Attachment> attas = taskService
				.getProcessInstanceAttachments(pInstance.getId());
		List<CommentVO> comments = processService.getComments(taskId);
		Integer id = vitaeService.getVitaeEntityByIntstanceId(pInstance.getId())
				.getId();
		
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("vitaeId", id + "");
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskId", taskId);
		return "toStartVitaePage";
	}

	public String statrInvite() {
		return "render_toStartVitaeList";
	}

	public String editSignTable() {
		request.setAttribute("vitaeId", request.getParameter("vitaeId"));
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("selectedPanel",
				request.getParameter("selectPanel"));
		VitaeSignEntity vitaeSignEntity = null;
		vitaeSignEntity = new VitaeSignEntity();
		vitaeSignEntity.setBh(vitaeService.getLastBh() + 1);
		request.setAttribute("vitaeSignEntity", vitaeSignEntity);
		return "editSignTable";
	}

	public String editSignTableByEmployee() {
		String vitaeSignId = request.getParameter("vitaeSignId");
		request.setAttribute("selectedPanel",
				request.getParameter("selectPanel"));
		vitaeSignEntity = vitaeService.VitaeSignEntity(vitaeSignId);
		request.setAttribute("resultId", request.getParameter("resultId"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		List<VitaeSignEduEntity> vitaeSignEduEntities = vitaeService
				.getVitaeSignEduEntityByVitaeId(vitaeSignId);
		List<VitaeSignJobHistoryEntity> vitaeSignJobHistoryEntities = vitaeService
				.getVitaeSignJobHistoryEntityByVitaeId(vitaeSignId);
		List<VitaeSignFamilyEntity> vitaeSignFamilyEntities = vitaeService
				.getVitaeSignFamilyEntityByVitaeId(vitaeSignId);
		request.setAttribute("vitaeSignEduEntities", vitaeSignEduEntities);
		request.setAttribute("vitaeSignJobHistoryEntities",
				vitaeSignJobHistoryEntities);
		request.setAttribute("vitaeSignFamilyEntities",
				vitaeSignFamilyEntities);
		return "editSignTableByEmployee";

	}
	public String chooseDetail() {
		// if("tiSkill".equals(type))
		// return "ChooseDetailTiSkill";
		// if("wysp".equals(type))
		// return "chooseDetailWY";
		if ("zy".equals(type))
			return "chooseDetailZY";
		return "chooseDetail";
	}
	public String saveVitaeSign() {
		vitaeSignEntity.setIsDeleted(0);
		vitaeSignEntity.setAddTime(new Date());
		int id=vitaeService.commonSave(vitaeSignEntity);
		String jobDetail = request.getParameter("jobDetail");
		String eduDetail = request.getParameter("eduDetail");
		String familyDetail = request.getParameter("familyDetail");
		if (StringUtils.isNotBlank(jobDetail)) {
			JSONArray jajobDetail = JSONArray.fromObject(jobDetail);
			if (CollectionUtils.isNotEmpty(jajobDetail)) {
				for (int i = 0, n = jajobDetail.size(); i < n; i++) {
					VitaeSignJobHistoryEntity entity = (VitaeSignJobHistoryEntity) JSONObject
							.toBean(JSONObject.fromObject(jajobDetail.get(i)),
									VitaeSignJobHistoryEntity.class);
					if(StringUtils.isBlank(entity.getStartTime())){
						entity.setStartTime(null);
					}
					if(StringUtils.isBlank(entity.getEndTime())){
						entity.setEndTime(null);
					}
					entity.setAddTime(new Date());
					entity.setIsDeleted(0);
					entity.setVitaeSignId(id);
					vitaeService.commonSave(entity);
				}
			}
		}
		if (StringUtils.isNotBlank(eduDetail)) {
			JSONArray jaeduDetail = JSONArray.fromObject(eduDetail);
			if (CollectionUtils.isNotEmpty(jaeduDetail)) {
				for (int i = 0, n = jaeduDetail.size(); i < n; i++) {
					VitaeSignEduEntity entity = (VitaeSignEduEntity) JSONObject
							.toBean(JSONObject.fromObject(jaeduDetail.get(i)),
									VitaeSignEduEntity.class);
					if(StringUtils.isBlank(entity.getStartTime())){
						entity.setStartTime(null);
					}
					if(StringUtils.isBlank(entity.getEndTime())){
						entity.setEndTime(null);
					}
					entity.setAddTime(new Date());
					entity.setIsDeleted(0);
					entity.setVitaeSignId(id);
					entity.setSortIndex(i);
					vitaeService.commonSave(entity);
				}
			}
		}
		if (StringUtils.isNotBlank(familyDetail)) {
			JSONArray jafamilyDetail = JSONArray.fromObject(familyDetail);
			if (CollectionUtils.isNotEmpty(jafamilyDetail)) {
				for (int i = 0, n = jafamilyDetail.size(); i < n; i++) {
					VitaeSignFamilyEntity entity = (VitaeSignFamilyEntity) JSONObject
							.toBean(JSONObject
									.fromObject(jafamilyDetail.get(i)),
									VitaeSignFamilyEntity.class);
					entity.setAddTime(new Date());
					entity.setIsDeleted(0);
					entity.setVitaeSignId(id);
					entity.setSort(i);
					vitaeService.commonSave(entity);
				}
			}
		}
		// 假如继续 邀请 那么 isFinish 的状态为 2
		User user = (User) request.getSession().getAttribute("user");
		try {
			processService.completeTask(request.getParameter("taskId"),
					user.getId(), TaskResultEnum.VITAE_CONTINUE, null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 我们将下一个节点 的 taskId 以及其对应的vitaeSignId 存在放 vitaeResult 表中
		// 1.方便下次 查询的列表 初始化
		// 2.方便历史页面 vitae 对应 多个 人员招聘详情
		// 每个人员招聘详情 对应 多个 招聘结果的列表

		// 这个步骤是哪个hrDepartment 下个步骤任然是
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP1);
		int taskId = vitaeService.getEffectiveTaskId(tasks, groups,
				Arrays.asList(user.getId()), 1);
		// 新增一条 vitaeResult记录
		VitaeResultEntity vitaeResultEntity = new VitaeResultEntity();
		vitaeResultEntity.setAddTime(new Date());
		// 关联到vitaeSign
		vitaeResultEntity.setVitaeSignId(id);
		String guessTime=request.getParameter("guessTime");
		Date time=null;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(guessTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		vitaeResultEntity.setGuessVitaeTime(time);
		vitaeResultEntity.setNextTaskId(taskId);
		vitaeResultEntity.setIsDeleted(0);
		vitaeService.commonSave(vitaeResultEntity);
		return "render_toStartVitaeList";
	}

	public String updateVitaeSign() {
		vitaeService.commonUpdate(vitaeSignEntity);
		Integer id = vitaeSignEntity.getId();
		String jobDetail = request.getParameter("jobDetail");
		String eduDetail = request.getParameter("eduDetail");
		String familyDetail = request.getParameter("familyDetail");
		vitaeService.clearSignLinkTable(id + "");
		if (StringUtils.isNotBlank(jobDetail)) {
			JSONArray jajobDetail = JSONArray.fromObject(jobDetail);
			if (CollectionUtils.isNotEmpty(jajobDetail)) {
				for (int i = 0, n = jajobDetail.size(); i < n; i++) {
					VitaeSignJobHistoryEntity entity = (VitaeSignJobHistoryEntity) JSONObject
							.toBean(JSONObject.fromObject(jajobDetail.get(i)),
									VitaeSignJobHistoryEntity.class);
					if(StringUtils.isBlank(entity.getStartTime())){
						entity.setStartTime(null);
					}
					if(StringUtils.isBlank(entity.getEndTime())){
						entity.setEndTime(null);
					}
					entity.setAddTime(new Date());
					entity.setIsDeleted(0);
					entity.setVitaeSignId(id);
					vitaeService.commonSave(entity);
				}
			}
		}
		if (StringUtils.isNotBlank(eduDetail)) {
			JSONArray jaeduDetail = JSONArray.fromObject(eduDetail);
			if (CollectionUtils.isNotEmpty(jaeduDetail)) {
				for (int i = 0, n = jaeduDetail.size(); i < n; i++) {
					VitaeSignEduEntity entity = (VitaeSignEduEntity) JSONObject
							.toBean(JSONObject.fromObject(jaeduDetail.get(i)),
									VitaeSignEduEntity.class);
					if(StringUtils.isBlank(entity.getStartTime())){
						entity.setStartTime(null);
					}
					if(StringUtils.isBlank(entity.getEndTime())){
						entity.setEndTime(null);
					}
					entity.setAddTime(new Date());
					entity.setIsDeleted(0);
					entity.setVitaeSignId(id);
					entity.setSortIndex(i);
					vitaeService.commonSave(entity);
				}
			}
		}
		if (StringUtils.isNotBlank(familyDetail)) {
			JSONArray jafamilyDetail = JSONArray.fromObject(familyDetail);
			if (CollectionUtils.isNotEmpty(jafamilyDetail)) {
				for (int i = 0, n = jafamilyDetail.size(); i < n; i++) {
					VitaeSignFamilyEntity entity = (VitaeSignFamilyEntity) JSONObject
							.toBean(JSONObject
									.fromObject(jafamilyDetail.get(i)),
									VitaeSignFamilyEntity.class);
					entity.setAddTime(new Date());
					entity.setIsDeleted(0);
					entity.setVitaeSignId(id);
					entity.setSort(i);
					vitaeService.commonSave(entity);
				}
			}
		}
		
		String taskId=request.getParameter("taskId");
		User user = (User) request.getSession().getAttribute("user");
		//走流程
		try {
			processService.completeTask(taskId, user.getId(), null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//更新业务表状态
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP3);
		int nextTaskId = vitaeService.getEffectiveTaskId(tasks, groups,
				Arrays.asList(user.getId()), 1);
		// 新增一条 vitaeResult记录
		VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
		vitaeResultEntity.setNextTaskId(nextTaskId);
		vitaeResultEntity.setStep2UserId(user.getId());
		vitaeResultEntity.setStep2Time(new Date());
		vitaeService.commonUpdate(vitaeResultEntity);
		return "render_toVitaeStep2";
	}

	public String toVitaeStep1() {
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "toVitaeStep1";
			return "toVitaeStep1";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP1);
		ListResult<VitaeTaskVo> taskListResult = vitaeService
				.findVitaeVoByUserGroupIDsByStepForNew(tasks, groups,
						Arrays.asList(user.getId()), page, limit, 1);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int step1TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP1.getName()).count();
		request.getSession().setAttribute("step1TaskCount", step1TaskCount); 
		selectedPanel = "toVitaeStep1";
		return "toVitaeStep1";
	}

	public String outOfTime() {
		String taskId = request.getParameter("taskID");
		String vitaeResultId = request.getParameter("resultId");
		// 更新业务表 状态
		User user = (User) request.getSession().getAttribute("user");
		vitaeService.outOfTime(vitaeResultId, user.getId());
		// 更新流程 状态
		try {
			processService.completeTask(taskId, user.getId(), TaskResultEnum.VITAE_OUTOFTIME, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "render_toVitaeStep1";
	}
	
	public String toFixVitaeEmployee() {
		selectedPanel = "toFixVitaeList";
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<FormField> formFields = processService.getFormFields(taskId);
		vitaeVo = (VitaeVo) runtimeService.getVariable(pInstance.getId(),
				"arg");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pInstance.getProcessDefinitionId())
				.singleResult();
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(pInstance.getId());
		List<Attachment> attas = taskService
				.getProcessInstanceAttachments(pInstance.getId());
		List<CommentVO> comments = processService.getComments(taskId);
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskId", taskId);
		return "toFixVitaeEmployee";
	}
	
	public String hrConfirm(){
		selectedPanel = "toVitaeStep1";
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<FormField> formFields = processService.getFormFields(taskId);
		vitaeVo = (VitaeVo) runtimeService.getVariable(pInstance.getId(),
				"arg");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pInstance.getProcessDefinitionId())
				.singleResult();
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(pInstance.getId());
		List<Attachment> attas = taskService
				.getProcessInstanceAttachments(pInstance.getId());
		List<CommentVO> comments = processService.getComments(taskId);
		VitaeEntity vitaeEntity=vitaeService.getVitaeEntityByIntstanceId(pInstance.getProcessInstanceId());
		
		VitaeResultEntity vitaeResultEntity=null;
		try{
			vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);			
		}catch(Exception ignore){}
		String subjectIds=null;
		String t_subjectIds=null;;
		if(vitaeResultEntity.getPrevId()!=null){
			//是复试
			subjectIds=vitaeResultEntity.getIds();
			t_subjectIds=vitaeResultEntity.getTIds();
		}else{
			subjectIds=vitaeEntity.getRealSubjectPersonIds();
			t_subjectIds=vitaeEntity.getRealTechnologySubjectPersonIds();
		}
	
		String names="";
		String t_names="";
		if(StringUtils.isNotBlank(subjectIds))
			for(String str:subjectIds.split(",")){
				names+=staffService.getRealNameByUserId(str)+",";
			}
		if(StringUtils.isNotBlank(t_subjectIds))
			for(String str:t_subjectIds.split(",")){
				t_names+=staffService.getRealNameByUserId(str)+",";
			}
		request.setAttribute("ids", subjectIds);
		request.setAttribute("names", names);
		request.setAttribute("n", request.getParameter("n"));
		request.setAttribute("t",  request.getParameter("t"));
		request.setAttribute("t_ids", t_subjectIds);
		request.setAttribute("t_names", t_names);
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskId", taskId);
		return "hrConfirm";
	}
	
	public String completeHrConfirm(){
		String taskId=request.getParameter("taskId");
		User user = (User) request.getSession().getAttribute("user");
		String users=request.getParameter("ids");
		String t_users=request.getParameter("tIds");
		String time=request.getParameter("time");
		if (StringUtils.isNotBlank(users)) {
			String[] userArray=users.split(",");
			for(String str:userArray){
				try{
					ShortMsgSender.getInstance().send(staffService.getTelephoneByUserId(str),"您将参与一场于:"+time+"的面试，请与人事确认做好准备。");				
				}catch(Exception e){
					continue;
				}
			}
		}
		if (StringUtils.isNotBlank(t_users)) {
			String[] userArray=t_users.split(",");
			for(String str:userArray){
				try{
					ShortMsgSender.getInstance().send(staffService.getTelephoneByUserId(str),"您将参与一场于:"+time+"的面试，请与人事确认做好准备。");				
				}catch(Exception e){
					continue;
				}
			}
		}
		//走流程
		try {
			processService.completeTask(taskId, user.getId(), TaskResultEnum.VITAE_INTIME, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//更新业务表状态
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP2);
		int nextTaskId = vitaeService.getEffectiveTaskId(tasks, groups,
				Arrays.asList(user.getId()), 1);
		// 新增一条 vitaeResult记录
		VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
		vitaeResultEntity.setNextTaskId(nextTaskId);
		vitaeResultEntity.setStep1UserId(user.getId());
		vitaeResultEntity.setStep1Time(new Date());
		vitaeResultEntity.setSubjectPersonsId(users);
		vitaeResultEntity.setTechnologySubjectPersonId(t_users);
		vitaeResultEntity.setType(1);
		vitaeService.commonUpdate(vitaeResultEntity);
		return "render_toVitaeStep1";
	}
	
	public String toVitaeStep2(){
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "toVitaeStep2";
			return "toVitaeStep2";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP2);
		ListResult<VitaeTaskVo> taskListResult = vitaeService
				.findVitaeVoByUserGroupIDsByStep(tasks, groups,
						Arrays.asList(user.getId()), page, limit, 1);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int step2TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP2.getName()).count();
		request.getSession().setAttribute("step2TaskCount", step2TaskCount); 
		selectedPanel = "toVitaeStep2";
		return "toVitaeStep2";
	}
	
	public String toVitaeStep3(){
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "toVitaeStep3";
			return "toVitaeStep3";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP3);
		ListResult<VitaeTaskVo> taskListResult = vitaeService
				.findVitaeVoByUserGroupIDsByStep(tasks, groups,
						Arrays.asList(user.getId()), page, limit, 1);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int step3TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP3.getName()).count();
		request.getSession().setAttribute("step3TaskCount", step3TaskCount); 
		selectedPanel = "toVitaeStep3";
		return "toVitaeStep3";
	}
	
	public String recordVitaeResult(){
		selectedPanel = "toVitaeStep1";
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<FormField> formFields = processService.getFormFields(taskId);
		vitaeVo = (VitaeVo) runtimeService.getVariable(pInstance.getId(),
				"arg");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(pInstance.getProcessDefinitionId())
				.singleResult();
		List<TaskVO> finishedTaskVOs = processService
				.findFinishedTasksByProcessInstanceID(pInstance.getId());
		List<Attachment> attas = taskService
				.getProcessInstanceAttachments(pInstance.getId());
		List<CommentVO> comments = processService.getComments(taskId);
		VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
		String subjectIds=vitaeResultEntity.getSubjectPersonsId();
		String t_subjectIds=vitaeResultEntity.getTechnologySubjectPersonId();
		if(StringUtils.isNotBlank(subjectIds)){
			String names="";
			for(String str:subjectIds.split(",")){
				names+=staffService.getRealNameByUserId(str)+",";
			}
			request.setAttribute("names", names.substring(0, names.length()-1).split(","));
		}
		if(StringUtils.isNotBlank(t_subjectIds)){
			String names="";
			for(String str:t_subjectIds.split(",")){
				names+=staffService.getRealNameByUserId(str)+",";
			}
			request.setAttribute("tnames", names.substring(0, names.length()-1).split(","));
		}
		request.setAttribute("ids", subjectIds);
		request.setAttribute("attas", attas);
		request.setAttribute("n", request.getParameter("n"));
		request.setAttribute("t", request.getParameter("t"));
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskId", taskId);
		return "recordVitaeResult";
	}
	
	@Getter
	@Setter
	private String[] isPass;
	@Getter
	@Setter
	private String[] score;
	public String saveVitaeResult(){
		String notice=request.getParameter("notice");
		String taskId=request.getParameter("taskID");
		User user = (User) request.getSession().getAttribute("user");
		boolean passResult=true;
		String isPassStr="";
		String scoreStr="";
		for(int i=0,n=isPass.length; i<n;i++){
			if("0".equals(isPass[i])){
				passResult=false;
			}
			isPassStr+=isPass[i]+",";
			scoreStr+=score[i]+",";
		}
		if(passResult){
			ProcessInstance pInstance = processService.getProcessInstance(taskId);
			VitaeVo arg = (VitaeVo) runtimeService.getVariable(pInstance.getId(), "arg");	
			Integer number=arg.getPassPersonNumber();
			if(number==null)number=0;
			arg.setPassPersonNumber(++number);
			runtimeService.setVariable(pInstance.getId(), "arg", arg);
			
			try {
				processService.completeTask(taskId, user.getId(),TaskResultEnum.VITAE_PASS, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Group> groups = identityService.createGroupQuery()
					.groupMember(user.getId()).list();
			List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
			tasks.add(TaskDefKeyEnum.VITAE_STEP4);
			int nextTaskId = vitaeService.getEffectiveTaskId(tasks, groups,
					Arrays.asList(user.getId()), 1);
			
			VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
			vitaeResultEntity.setNextTaskId(nextTaskId);
			vitaeResultEntity.setScoreResult(scoreStr);
			vitaeResultEntity.setSubjectResult(isPassStr);
			vitaeResultEntity.setReason(notice);
			vitaeResultEntity.setStep3Time(new Date());
			vitaeResultEntity.setStep3UserId(user.getId());
			vitaeResultEntity.setType(2);
			vitaeService.commonUpdate(vitaeResultEntity);
			
			
		}else{
			VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
			vitaeResultEntity.setScoreResult(scoreStr);
			vitaeResultEntity.setSubjectResult(isPassStr);
			vitaeResultEntity.setReason(notice);
			vitaeResultEntity.setType(3);
			vitaeResultEntity.setStep3Time(new Date());
			vitaeResultEntity.setStep3UserId(user.getId());
			vitaeService.commonUpdate(vitaeResultEntity);
			try {
				processService.completeTask(taskId, user.getId(), TaskResultEnum.VITAE_UNPASS, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "render_toVitaeStep3";
	}

	
	public String toVitaeStep4(){
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "toVitaeStep4";
			return "toVitaeStep4";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP4);
		ListResult<VitaeTaskVo> taskListResult = vitaeService
				.findVitaeVoByUserGroupIDsByStep(tasks, groups,
						Arrays.asList(user.getId()), page, limit, 1);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		List<String> groupIds = new ArrayList<>();
		for(Group group: groups){
			groupIds.add(group.getId());
		}
		int step4TaskCount = (int) taskService.createTaskQuery().taskCandidateGroupIn(groupIds)
				.taskDefinitionKey(TaskDefKeyEnum.VITAE_STEP4.getName()).count();
		request.getSession().setAttribute("step4TaskCount", step4TaskCount); 
		selectedPanel = "toVitaeStep4";
		return "toVitaeStep4";
	}
	
	public String toSignList(){
		String name=request.getParameter("name");
		ListResult<VitaeSignEntity> jobs = vitaeService.getPagedVitaeByName(name, page,
				limit);
		request.setAttribute("name", name);
		count = jobs.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("resultList", jobs.getList());
		return "toSignList";
	}
	
	public String toViewSignDetail(){
		String vitaeSignId=request.getParameter("vitaeSignId");
		vitaeSignEntity = vitaeService.VitaeSignEntity(vitaeSignId);
		request.setAttribute("resultId", request.getParameter("resultId"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		List<VitaeSignEduEntity> vitaeSignEduEntities = vitaeService
				.getVitaeSignEduEntityByVitaeId(vitaeSignId);
		List<VitaeSignJobHistoryEntity> vitaeSignJobHistoryEntities = vitaeService
				.getVitaeSignJobHistoryEntityByVitaeId(vitaeSignId);
		List<VitaeSignFamilyEntity> vitaeSignFamilyEntities = vitaeService
				.getVitaeSignFamilyEntityByVitaeId(vitaeSignId);
		request.setAttribute("vitaeSignEduEntities", vitaeSignEduEntities);
		request.setAttribute("vitaeSignJobHistoryEntities",
				vitaeSignJobHistoryEntities);
		request.setAttribute("vitaeSignFamilyEntities",
				vitaeSignFamilyEntities);
		return "toViewSignDetail";
	}
	
	public String confirmRZ(){
		User user = (User) request.getSession().getAttribute("user");
		String taskId=request.getParameter("taskID");
		VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
		vitaeResultEntity.setStep4UserId(user.getId());
		vitaeResultEntity.setStep4Time(new Date());
		vitaeResultEntity.setType(5);
		VitaeSignEntity vitaeSignEntity=vitaeService.VitaeSignEntity(vitaeResultEntity.getVitaeSignId()+"");
		VitaeEntity vitaeEntity=vitaeService.getVitaeEntityById(vitaeSignEntity.getVitaeId()+"");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		VitaeVo arg = (VitaeVo) runtimeService.getVariable(pInstance.getId(), "arg");	
		Integer number=arg.getEffectivePersonNumber();
		if(number==null)number=0;
		vitaeEntity.setEffectivePersonNumber(number+1);
		arg.setEffectivePersonNumber(number+1);
		vitaeService.commonUpdate(vitaeEntity);
		vitaeService.commonUpdate(vitaeResultEntity);
		runtimeService.setVariable(pInstance.getId(), "arg", arg);
		try {
			processService.completeTask(taskId, user.getId(),null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "render_toVitaeStep4";
	}
	public String confirmRZ_(){
		User user = (User) request.getSession().getAttribute("user");
		String taskId=request.getParameter("taskID");
		VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
		vitaeResultEntity.setStep4UserId(user.getId());
		vitaeResultEntity.setStep4Time(new Date());
		vitaeResultEntity.setType(4);
		VitaeSignEntity vitaeSignEntity=vitaeService.VitaeSignEntity(vitaeResultEntity.getVitaeSignId()+"");
		VitaeEntity vitaeEntity=vitaeService.getVitaeEntityById(vitaeSignEntity.getVitaeId()+"");		
		vitaeService.commonUpdate(vitaeEntity);
		vitaeService.commonUpdate(vitaeResultEntity);
		try {
			processService.completeTask(taskId, user.getId(),null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "render_toVitaeStep4";
	}
	
	public String finishVitae(){
		User user = (User) request.getSession().getAttribute("user");
		String taskId=request.getParameter("taskId");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		try {
			processService.completeTask(taskId, user.getId(),TaskResultEnum.VITAE_FINISH, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VitaeEntity vitaeEntity=vitaeService.getVitaeEntityByIntstanceId(pInstance.getProcessInstanceId());
		vitaeEntity.setResult(TaskResultEnum.VITAE_FINISH.getValue());
		vitaeService.commonUpdate(vitaeEntity);
		return "render_toStartVitaeList";
	}
	/**
	 * 查询 是否 任然有 子流程 未完结
	 * @return
	 */
	public void stillHaveUnCompleteTask(){
		User user = (User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		String taskId=request.getParameter("taskId");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP1);
		tasks.add(TaskDefKeyEnum.VITAE_STEP2);
		tasks.add(TaskDefKeyEnum.VITAE_STEP3);
		tasks.add(TaskDefKeyEnum.VITAE_STEP4);
		int count = vitaeService.findVitaeVoByUserGroupIDsCount(tasks, groups, Arrays.asList(user.getId()), pInstance.getId());
		Map<String,Integer> queryMap= new HashMap<>();
		queryMap.put("number", count);
		printByJson(queryMap);
	}
	
	public String getVitaeDetailByInstanceId(){
		String instanceId=request.getParameter("instanceId");
		VitaeEntity vitaeEntity=vitaeService.getVitaeEntityByIntstanceId(instanceId);
		List<VitaeSignEntity> vitaeSignEntity=vitaeService.VitaeSignEntityByVitaeId(vitaeEntity.getId()+"");
		List<VitaeResultEntity> vitaeResultEntities=new ArrayList<>();
		for (int i = 0; i < vitaeSignEntity.size(); i++) {
			List<VitaeResultEntity> list=vitaeService.getVitaeResultListBySignId(vitaeSignEntity.get(i).getId()+"");
			for (VitaeResultEntity vitaeResultEntity : list) {
				vitaeResultEntity.setXm(vitaeSignEntity.get(i).getXm());
			}
			vitaeResultEntities.addAll(list);

		}
		for (VitaeResultEntity vitaeResultEntity : vitaeResultEntities) {
			String id1=vitaeResultEntity.getStep1UserId();
			if(StringUtils.isNotBlank(id1)){
				vitaeResultEntity.setStep1Name(staffService.getRealNameByUserId(id1));
			}
			String id3=vitaeResultEntity.getStep3UserId();
			if(StringUtils.isNotBlank(id3)){
				vitaeResultEntity.setStep3Name(staffService.getRealNameByUserId(id3));
			}
			String id4=vitaeResultEntity.getStep4UserId();
			if(StringUtils.isNotBlank(id4)){
				vitaeResultEntity.setStep4Name(staffService.getRealNameByUserId(id4));
			}
		}
		request.setAttribute("result2", vitaeResultEntities);
		return "getVitaeDetailByInstanceId";
	}
	
	public String toCollectRetrailMsg(){
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("signId", request.getParameter("signId"));
		request.setAttribute("resultId", request.getParameter("resultId"));
		return "toCollectRetrailMsg";
	}
	
	public void retrailSave(){
		User user = (User) request.getSession().getAttribute("user");
		String taskId=request.getParameter("taskId");
		VitaeResultEntity vitaeResultEntity=vitaeService.VitaeResultEntityByTaskId(taskId);
		vitaeResultEntity.setStep4UserId(user.getId());
		vitaeResultEntity.setStep4Time(new Date());
		//复试
		vitaeResultEntity.setType(6);
		vitaeService.commonUpdate(vitaeResultEntity);
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		VitaeVo arg = (VitaeVo) runtimeService.getVariable(pInstance.getId(), "arg");
		Integer number=arg.getPassPersonNumber();
		if(number==null)number=0;
		if(number==0)number=1;
		arg.setPassPersonNumber(number-1);
		runtimeService.setVariable(pInstance.getId(), "arg", arg);
		try {
			processService.completeTask(taskId, user.getId(),null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reStartTrail(pInstance.getProcessInstanceId());
	}
	private void reStartTrail(String instanceId){
		Task task=taskService.createTaskQuery().processInstanceId(instanceId).taskDefinitionKeyLike("startInvite").singleResult();
		User user = (User) request.getSession().getAttribute("user");
		try {
			processService.completeTask(task.getId(),user.getId(), TaskResultEnum.VITAE_CONTINUE, null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.VITAE_STEP1);
		int taskId = vitaeService.getEffectiveTaskId(tasks, groups,
				Arrays.asList(user.getId()), 1);
		// 新增一条 vitaeResult记录
		
		request.getParameter("signId");
		VitaeResultEntity vitaeResultEntity = new VitaeResultEntity();
		vitaeResultEntity.setAddTime(new Date());
		String guessTime=request.getParameter("time");
		Date time=null;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(guessTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		vitaeResultEntity.setGuessVitaeTime(time);
		vitaeResultEntity.setNextTaskId(taskId);
		vitaeResultEntity.setVitaeSignId(Integer.parseInt(request.getParameter("signId")));
		vitaeResultEntity.setIds(request.getParameter("ids"));
		vitaeResultEntity.setNames(request.getParameter("names"));
		vitaeResultEntity.setTIds(request.getParameter("tIds"));
		vitaeResultEntity.setTNames(request.getParameter("tNames"));
		vitaeResultEntity.setPrevId(Integer.parseInt(request.getParameter("resultId")));
		vitaeResultEntity.setIsDeleted(0);
		vitaeService.commonSave(vitaeResultEntity);
	}
}
