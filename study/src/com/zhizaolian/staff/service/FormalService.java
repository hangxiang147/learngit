package com.zhizaolian.staff.service;

import java.util.Date;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;

import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.FormalVO;
import com.zhizaolian.staff.vo.TaskVO;

public interface FormalService {

	void startFormal(FormalVO formalVO, int type);
	
	/**
	 * 判断指定员工是否已经提交过转正申请，且流程未结束
	 * @param userID
	 * @return
	 */
	boolean hasSubmitted(String userID);
	
	/**
	 * 分页查询指定用户的转正申请
	 * @param userID
	 * @return
	 */
	ListResult<FormalVO> findFormalListByUserID(String userID, int page, int limit);
	
	/**
	 * 根据用户组列表，查找有权限处理的转正申请
	 * @param groups
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<TaskVO> findFormalTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit);
	
	/**
	 * 更新转正记录
	 * @param formalVO
	 * @param processInstanceID
	 * @param result
	 */
	void updateFormal(FormalVO formalVO, String processInstanceID, TaskResultEnum result);
	
	/**
	 * 更新转正申请流程节点的处理结果
	 * @param processInstanceID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	/**
	 * 确认员工的转正日期
	 * @param processInstanceID
	 * @param beginDate
	 */
	void confirmFormalDate(String processInstanceID, Date formalDate);
	
	/**
	 * 查询转正流程未完结的所有转正申请
	 * @return
	 */
	List<FormalVO> findNotEndFormals();
	
	/**
	 * 查询指定流程的转正申请记录
	 * @param processInstanceID
	 * @return
	 */
	FormalVO getFormalByProcessInstanceID(String processInstanceID);
	
	List<TaskVO> createTaskVOListByTaskList(List<Task> tasks);

	ListResult<FormalVO> findFormalStaffApplicationList(String staffName, int page, int limit, String beginDate,
			String endDate);
}
