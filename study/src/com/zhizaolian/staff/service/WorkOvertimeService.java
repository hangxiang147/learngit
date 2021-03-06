package com.zhizaolian.staff.service;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.WorkOvertimeTaskVo;
import com.zhizaolian.staff.vo.WorkOvertimeVo;

public interface WorkOvertimeService {

	void startWorkOvertime(WorkOvertimeVo workOvertimeVo);

	void saveWorkOvertime(WorkOvertimeVo workOvertimeVo);

	List<WorkOvertimeTaskVo> createTaskVOListByTaskList(List<Task> workOvertimeTasks);

	void updateWorkOvertimeProcessStatus(TaskResultEnum result, String processInstanceID);

	ListResult<WorkOvertimeTaskVo> findWorkOvertimeTasksByGroups(List<Group> groups, Integer page, Integer limit);

	int findWorkOvertimeTasksCountByGroups(List<Group> groups);

	ListResult<WorkOvertimeVo> findWorkOvertimeListByUserID(String id, Integer page, Integer limit);

	ListResult<WorkOvertimeVo> findWorkOvertimeListByCondition(String[] conditions, Integer page, Integer limit);
	
	String[] getNightWorkTimesAndhours(String userId, String beginDate, String endDate) throws Exception;
}
