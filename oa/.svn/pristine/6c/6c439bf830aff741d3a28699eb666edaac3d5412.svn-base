package com.zhizaolian.staff.service;

import java.util.Date;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssignmentVO;

public interface AssignmentService {

	/**
	 * 启动一个任务分配流程
	 * @param assignment
	 */
	void startAssignment(AssignmentVO assignment);
	
	/**
	 * 更新任务分配流程节点处理结果
	 * @param userID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	/**
	 * 更新分配任务的开始时间
	 * @param processInstanceID
	 * @param beginDate
	 */
	void updateBeginDate(String processInstanceID, Date beginDate);
	
	/**
	 * 更新任务完成得分
	 * @param processInstanceID
	 * @param score
	 */
	void updateScore(String processInstanceID, Float score);
	
	/**
	 * 修改已分配任务
	 * @param assignment
	 */
	void updateAssignment(String processInstanceID, AssignmentVO assignment);
	
	/**
	 * 分页查询指定用户分配的任务
	 * @param userID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<AssignmentVO> findAssignmentListByUserID(String userID, int page, int limit);
	
	/**
	 * 根据taskID，查询对应任务分配流程的参数
	 * @param taskID
	 * @return
	 */
	AssignmentVO getAssignmentVOByTaskID(String taskID);
}
