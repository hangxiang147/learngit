package com.zhizaolian.staff.service;

import java.util.Date;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ResignationVO;
import com.zhizaolian.staff.vo.TaskVO;

public interface ResignationService {

	/**
	 * 启动一个离职流程
	 * @param resignationVO
	 */
	void startResignation(ResignationVO resignationVO);
	
	/**
	 * 分页查询指定用户的离职申请
	 * @param userID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<ResignationVO> findResignationListByUserID(String userID, int page, int limit);
	
	/**
	 * 更新离职申请流程节点的处理结果
	 * @param processInstanceID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	/**
	 * 说明：离职交接和工资清算权限支持个人时，统一使用下面的接口，该接口可删
	 * 根据用户组列表，查找有权限审批的离职申请
	 * @param groups
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<TaskVO> findResignationTasksByGroups(List<Group> groups, List<TaskDefKeyEnum> tasks, int page, int limit);

	/**
	 * 根据用户组和用户列表，分页查找有权限处理的离职申请列表
	 * @param tasks
	 * @param groups
	 * @param users
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<TaskVO> findResignationTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit);
	
	List<TaskVO> createTaskVOListByTaskList(List<Task> tasks);
	
	/**
	 * 确认员工的离职日期
	 * @param processInstanceID
	 * @param leaveDate  离职日期
	 * @param taskDefKey 流程节点
	 */
	void confirmLeaveDate(String processInstanceID, Date leaveDate, String taskDefKey);
	
	/**
	 * 根据taskID，查询对应离职申请流程的参数
	 * @param taskID
	 * @return
	 */
	ResignationVO getResignationVOByTaskID(String taskID);
	
	ListResult<ResignationVO> findResignationByResignationVO(ResignationVO resignationVO, int page, int limit);
	
	XSSFWorkbook exportResignationVO(ResignationVO resignationVO);
	
}
