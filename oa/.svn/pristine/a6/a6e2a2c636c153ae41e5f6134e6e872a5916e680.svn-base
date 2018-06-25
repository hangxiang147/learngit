package com.zhizaolian.staff.action.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.PublicRelationEntity;
import com.zhizaolian.staff.entity.PublicRelationEventEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.PublicService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.PublicRelationEventVo;

import lombok.Getter;
import lombok.Setter;
@Controller(value="publicAction")
@Scope(value="prototype")
public class PublicAction extends BaseAction{
	private static final long serialVersionUID = 8845500677330557840L;
	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer page = 1;
	@Getter
	private Integer totalPage = 1;
	@Setter
	private String errorMessage;
	@Autowired
	private PublicService publicService;
	@Setter
	@Getter
	private PublicRelationEntity publicRelation;
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProcessService processService;
	@Getter
	private Integer type;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	public String findPublicRelations(){
		String category = request.getParameter("category");
		String person = request.getParameter("person");
		ListResult<PublicRelationEntity> publicRelations = publicService.findPublicRelations(page, limit, category, person);
		count = publicRelations.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("publicRelations", publicRelations.getList());
		request.setAttribute("category", category);
		request.setAttribute("person", person);
		selectedPanel = "publicRelations";
		return "publicRelations";
	}
	public String addPublicRelation(){
		selectedPanel = "publicRelations";
		return "addPublicRelation";
	}
	public String savePublicRelation(){
		publicService.savePublicRelation(publicRelation);
		return "render_findPublicRelations";
	}
	public String modifyPublicRelation(){
		String publicRelationId = request.getParameter("publicRelationId");
		PublicRelationEntity publicRelation = publicService.getPublicRelation(publicRelationId);
		String[] ourPersonIds = publicRelation.getOurPersonIds().split(",");
		List<String> ourPersonNames = new ArrayList<>();
		for(String personId: ourPersonIds){
			StaffEntity staff = staffService.getInJobStaffByUserId(personId);
			if(null != staff){
				ourPersonNames.add(staff.getStaffName());
			}
		}
		publicRelation.setOurPersonNames(ourPersonNames.toArray(new String[ourPersonNames.size()]));
		request.setAttribute("ourPersonNames", StringUtils.join(ourPersonNames, ","));
		request.setAttribute("ourPersonIds", ourPersonIds);
		request.setAttribute("publicRelation", publicRelation);
		selectedPanel = "publicRelations";
		return "modifyPublicRelation";
	}
	public String noMatchPublicEvent(){
		String processInstanceId = request.getParameter("processInstanceId");
		User user = (User)request.getSession().getAttribute("user");
		String taskId = request.getParameter("taskId");
		String comment = request.getParameter("comment");
		processService.completeTask(taskId, user.getId(), TaskResultEnum.DISAGREE, comment);
		publicService.updatePublicEventStatus(TaskResultEnum.DISAGREE, processInstanceId);
		type = BusinessTypeEnum.PUBLIC_EVENT.getValue();
		return "toTaskList";
	}
	public String toMatchPublicEvent(){
		String processInstanceId = request.getParameter("processInstanceID");
		String taskId = request.getParameter("taskId");
		PublicRelationEventEntity publicEvent = publicService.getPublicEventTaskByInstanceId(processInstanceId);
		publicEvent.setUserName(staffService.getStaffByUserId(publicEvent.getUserId()).getStaffName());
		List<PublicRelationEntity> publicRelations =  publicService.findPublicRelations();
		request.setAttribute("publicRelations", publicRelations);
		request.setAttribute("publicEvent", publicEvent);
		request.setAttribute("taskId", taskId);
		request.setAttribute("processInstanceId", processInstanceId);
		return "matchPublicEvent";
	}
	public void getOurPersonsByOtherPerson(){
		String publicRelationId = request.getParameter("publicRelationId");
		PublicRelationEntity publicRelation = publicService.getPublicRelation(publicRelationId);
		String[] ourPersonIds = publicRelation.getOurPersonIds().split(",");
		List<StaffEntity> ourPersons = new ArrayList<>();
		for(String ourPersonId: ourPersonIds){
			StaffEntity person = staffService.getInJobStaffByUserId(ourPersonId);
			if(null != person){
				ourPersons.add(person);
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("ourPersons", ourPersons);
		printByJson(resultMap);
	}
	public String matchPublicEvent(){
		String taskId = request.getParameter("taskId");
		String processInstanceId = request.getParameter("processInstanceId");
		String publicRelationId = request.getParameter("publicRelationId");
		String handlerId = request.getParameter("handlerId");
		User user = (User)request.getSession().getAttribute("user");
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put(TaskDefKeyEnum.PUBLIC_EVENT_MATCH.getResult(), TaskResultEnum.COMPLETE_MATCH.getValue());
		vars.put("handler", handlerId);
		taskService.setAssignee(taskId, user.getId());
		taskService.complete(taskId, vars);
		publicService.updatePublicRelationId(publicRelationId, processInstanceId);
		type = BusinessTypeEnum.PUBLIC_EVENT.getValue();
		return "toTaskList";
	}
	public String toHandlePublicEvent(){
		String processInstanceId = request.getParameter("processInstanceID");
		String taskId = request.getParameter("taskId");
		PublicRelationEventEntity publicEvent = publicService.getPublicEventTaskByInstanceId(processInstanceId);
		int publicRelationId = publicEvent.getPublicRelationId();
		PublicRelationEntity publicRelation = publicService.getPublicRelation(String.valueOf(publicRelationId));
		publicEvent.setUserName(staffService.getStaffByUserId(publicEvent.getUserId()).getStaffName());
		request.setAttribute("publicRelation", publicRelation);
		request.setAttribute("publicEvent", publicEvent);
		request.setAttribute("taskId", taskId);
		request.setAttribute("processInstanceId", processInstanceId);
		return "handlePublicEvent";
	}
	public String noHandlePublicEvent(){
		String processInstanceId = request.getParameter("processInstanceId");
		User user = (User)request.getSession().getAttribute("user");
		String taskId = request.getParameter("taskId");
		String comment = request.getParameter("comment");
		processService.completeTask(taskId, user.getId(), TaskResultEnum.NO_HANDLE, comment);
		publicService.updatePublicEventStatus(TaskResultEnum.NO_HANDLE, processInstanceId);
		type = BusinessTypeEnum.PUBLIC_EVENT.getValue();
		return "toTaskList";
	}
	public String handlePublicEvent(){
		String taskId = request.getParameter("taskId");
		String processInstanceId = request.getParameter("processInstanceId");
		User user = (User)request.getSession().getAttribute("user");
		String comment = request.getParameter("comment");
		processService.completeTask(taskId, user.getId(), TaskResultEnum.COMPLETE_HANDLE_, comment);
		publicService.updatePublicEventStatus(TaskResultEnum.COMPLETE_HANDLE_, processInstanceId);
		type = BusinessTypeEnum.PUBLIC_EVENT.getValue();
		return "toTaskList";
	}
	public String toAdvicePublicEvent(){
		String processInstanceId = request.getParameter("processInstanceID");
		String taskId = request.getParameter("taskId");
		PublicRelationEventEntity publicEvent = publicService.getPublicEventTaskByInstanceId(processInstanceId);
		publicEvent.setUserName(staffService.getStaffByUserId(publicEvent.getUserId()).getStaffName());
		HistoricTaskInstanceEntity handleTask = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).
				taskDefinitionKey(TaskDefKeyEnum.PUBLIC_EVENT_HANDLE.getName()).singleResult();
		String handlerId = handleTask.getAssignee();
		Date endTime = handleTask.getEndTime();
		StaffEntity handlerInfo = staffService.getStaffByUserId(handlerId);
		request.setAttribute("handlerInfo", handlerInfo);
		request.setAttribute("completeHandleTime", DateUtil.formateFullDate(endTime));
		request.setAttribute("publicEvent", publicEvent);
		request.setAttribute("taskId", taskId);
		return "advicePublicEvent";
	}
	public String advicePublicEvent(){
		String taskId = request.getParameter("taskId");
		User user = (User)request.getSession().getAttribute("user");
		String comment = request.getParameter("comment");
		processService.completeTask(taskId, user.getId(), TaskResultEnum.ADVICE, comment);
		type = BusinessTypeEnum.PUBLIC_EVENT.getValue();
		return "toTaskList";
	}
	public String closePublicRelation(){
		String publicRelationId = request.getParameter("publicRelationId");
		publicService.closePublicRelation(publicRelationId);
		return "render_findPublicRelations";
	}
	public String showHistoricalPublicEvents(){
		String publicRelationId = request.getParameter("publicRelationId");
		List<PublicRelationEventVo> historicalPublicEvents = publicService.showHistoricalPublicEventsByRelationId(publicRelationId);
		request.setAttribute("historicalPublicEvents", historicalPublicEvents);
		selectedPanel = "publicRelations";
		return "showHistoricalPublicEvents";
	}
}
