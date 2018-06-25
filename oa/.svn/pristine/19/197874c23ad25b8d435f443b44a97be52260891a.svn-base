package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AssignmentDao;
import com.zhizaolian.staff.entity.AssignmentEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AssignmentService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssignmentVO;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.StaffVO;
public class AssignmentServiceImpl implements AssignmentService {
	
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private AssignmentDao assignmentDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;

	@Override
	public void startAssignment(AssignmentVO assignment) {
		StaffVO staff = staffService.getStaffByUserID(assignment.getUserID());
		assignment.setBusinessType(BusinessTypeEnum.ASSIGNMENT.getName());
		assignment.setUserName(staff.getLastName());
		//初始化任务参数   
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", assignment);
		
		ProcessInstance pInstance = runtimeService.startProcessInstanceByKey("Assignment");
		// 查询第一个任务
		Task task = taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), assignment.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
		// 记录任务分配数据
		saveAssignment(assignment, pInstance.getId());
	}
	
	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		
		assignmentDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}
	
	@Override
	public void updateBeginDate(String processInstanceID, Date beginDate) {
		AssignmentVO assignment = (AssignmentVO) runtimeService.getVariable(processInstanceID, "arg");
		assignment.setBeginDate(DateUtil.formateFullDate(beginDate));
		runtimeService.setVariable(processInstanceID, "arg", assignment);
		
		assignmentDao.updateBeginDate(processInstanceID, beginDate);
	}
	
	@Override
	public void updateScore(String processInstanceID, Float score) {
		assignmentDao.updateScore(processInstanceID, score);
	}
	
	@Override
	public void updateAssignment(String processInstanceID, AssignmentVO assignment) {
		StaffVO staff = staffService.getStaffByUserID(assignment.getUserID());
		assignment.setBusinessType(BusinessTypeEnum.ASSIGNMENT.getName());
		assignment.setUserName(staff.getLastName());
		/*String[] executorVars = assignment.getExecutorID().split("_");
		assignment.setExecutorID(executorVars[0]);
		assignment.setExecutorName(executorVars[1]);*/
		//修改任务分配流程参数
		runtimeService.setVariable(processInstanceID,"arg", assignment);
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceID).singleResult();
		taskService.setAssignee(task.getId(), assignment.getExecutorID());
		//修改任务分配业务表数据
		updateAssignmentData(assignment, processInstanceID);
	}
	
	private void updateAssignmentData(AssignmentVO assignment, String processInstanceID) {
		AssignmentEntity assignmentEntity = assignmentDao.getAssignmentByProcessInstanceID(processInstanceID);
		assignmentEntity.setUserID(assignment.getUserID());
		assignmentEntity.setType(assignment.getType());
		assignmentEntity.setContent(assignment.getContent());
		assignmentEntity.setExecutorID(assignment.getExecutorID());
		assignmentEntity.setPriority(assignment.getPriority());
		assignmentEntity.setDeadline(DateUtil.getFullDate(assignment.getDeadline()));
		assignmentEntity.setGoal(assignment.getGoal());
		assignmentEntity.setUpdateTime(new Date());
		assignmentDao.save(assignmentEntity);
	}
	
	private void saveAssignment(AssignmentVO assignment, String processInstanceID) {
		Date now = new Date();
		AssignmentEntity assignmentEntity = AssignmentEntity.builder()
													.userID(assignment.getUserID())
													.type(assignment.getType())
													.content(assignment.getContent())
													.executorID(assignment.getExecutorID())
													.priority(assignment.getPriority())
													.deadline(DateUtil.getFullDate(assignment.getDeadline()))
													.goal(assignment.getGoal())
													.processInstanceID(processInstanceID)
													.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
													.addTime(now)
													.updateTime(now)
													.build();
		assignmentDao.save(assignmentEntity);
	}
	
	@Override
	public ListResult<AssignmentVO> findAssignmentListByUserID(String userID, int page, int limit) {
		//查询OA_Assignment表的数据
		List<AssignmentEntity> assignmentEntities = assignmentDao.findAssignmentsByUserID(userID, page, limit);
		List<AssignmentVO> result = new ArrayList<AssignmentVO>();
		for (AssignmentEntity assignment : assignmentEntities) {
			AssignmentVO assignmentVO = new AssignmentVO();
			assignmentVO.setProcessInstanceID(assignment.getProcessInstanceID());
			assignmentVO.setType(assignment.getType()); 
			assignmentVO.setContent(assignment.getContent());
			assignmentVO.setExecutorName(staffService.getStaffByUserID(assignment.getExecutorID()).getLastName());
			assignmentVO.setPriority(assignment.getPriority());
			assignmentVO.setDeadline(assignment.getDeadline()==null?"":DateUtil.formateFullDate(assignment.getDeadline()));
			assignmentVO.setBeginDate(assignment.getBeginDate()==null?"":DateUtil.formateFullDate(assignment.getBeginDate()));
			assignmentVO.setScore(assignment.getScore()==null?"":assignment.getScore().toString());
			assignmentVO.setGoal(assignment.getGoal());
			assignmentVO.setStatus((assignment.getProcessStatus()==null||TaskResultEnum.valueOf(assignment.getProcessStatus())==null)?"待对方确认":
									TaskResultEnum.valueOf(assignment.getProcessStatus()).getName());
			
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery().processInstanceId(assignment.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					BaseVO arg = (BaseVO) variable.getValue();
					assignmentVO.setRequestDate(arg.getRequestDate());
					assignmentVO.setUserName(arg.getUserName());
					assignmentVO.setTitle(arg.getTitle());
				}
			}
			
			result.add(assignmentVO);
		}
		
		int count = assignmentDao.countAssignmentsByUserID(userID);
		return new ListResult<AssignmentVO>(result, count);
	}
	
	@Override
	public AssignmentVO getAssignmentVOByTaskID(String taskID) {
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		return (AssignmentVO) runtimeService.getVariable(pInstance.getId(), "arg");
	}
}
