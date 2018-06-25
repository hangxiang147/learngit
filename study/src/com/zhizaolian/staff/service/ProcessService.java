package com.zhizaolian.staff.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.CredentialEntity;
import com.zhizaolian.staff.entity.CredentialUploadEntity;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CommonSubjectTaskVo;
import com.zhizaolian.staff.vo.ContractSignVo;
import com.zhizaolian.staff.vo.CoursePlanTaskVo;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.SoftPerformanceTaskVO;
import com.zhizaolian.staff.vo.TaskVO;

public interface ProcessService {

	/**
	 * 将Task集合转为TaskVO集合
	 * @param tasks
	 * @return
	 */
	List<TaskVO> createTaskVOList(List<Task> tasks);
	
	List<CommonSubjectTaskVo> createCommonSubjectVoList(List<Task> tasks);
	List<SoftPerformanceTaskVO> createSoftPerformanceTaskVoList(List<Task> tasks);
	
	/**
	 * 根据任务ID，查询用户填写的表单数据
	 * @param taskId
	 * @return
	 */
	List<FormField> getFormFields(String taskID);
	
	/**
	 * 根据流程ID，查询用户填写的表单数据
	 * @param processInstanceID
	 * @return
	 */
	List<FormField> getFormFieldsByProcessInstanceID(String processInstanceID);
	
	/**
	 * 根据任务ID，查询一个任务所在流程的全部评论
	 * @param taskID
	 * @return
	 */
	List<CommentVO> getComments(String taskID);
	
	/**
	 * 根据流程ID，查询该流程的全部评论
	 * @param processInstanceID
	 * @return
	 */
	List<CommentVO> getCommentsByProcessInstanceID(String processInstanceID);
	
	/**
	 * 完成任务
	 * @throws Exception 
	 */
	void completeTask(String taskID, String userID, TaskResultEnum result, String comment);
	
	
	/**
		完成任务   在特殊情况 下 一个节点需要更新多个状态
	 */
	void completeTask(String taskID, String userID, TaskResultEnum result, String comment,Map<String,Object> keys);

	/**
	 * 查询指定任务所在流程实例
	 * @param taskID
	 * @return
	 */
	ProcessInstance getProcessInstance(String taskID);
	
	/**
	 * 获取指定流程当前任务节点的指派人
	 * @param processInstanceID
	 * @return
	 */
	String getProcessTaskAssignee(String processInstanceID);
	
	/**
	 * 根据流程实例ID获取该流程已完成的任务列表
	 * @param processInstanceID
	 * @return
	 */
	List<TaskVO> findFinishedTasksByProcessInstanceID(String processInstanceID);
	
	/**
	 * 更新业务表中流程节点状态
	 * @param processInstanceID
	 * @param result
	 * @param businessType
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum result, String businessType);
	
	/**
	 * 根据用户组和用户列表，分页查找有权限处理的给定任务的任务列表
	 * @param groups
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<TaskVO> findTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit);

	/**
	 * 跟据用户id 查询 这个人所有 指定 类型的 流程
	 * @param userId
	 * @param typePrefix
	 * @return 
	 */
	ListResult<Object> getAllInstanceIdByUserAndTypePrefix(String userId,String typePrefix,int page,int limit,String filter);

	void contractSignFinancialConfirm(String taskID, String id, TaskResultEnum result, String comment,
			ContractSignVo contractSignVo);

	List<CoursePlanTaskVo> createCoursePlanTaskVoList(List<Task> classHours);
	
		
		//添加岗位资格证书-------------------------
		void addCredentialEntity(String applyUserId,String applyExplain,String offerUserId);
		//查询状态为几的岗位资格证书
		ListResult<CredentialEntity> findCredentialBy(Integer page,Integer limit);
		
		List<CredentialEntity> findCredentialVOTasks(List<Task> credentialVOTasks);
		
		void addCredentialUploadEntity(CredentialUploadEntity credentialUploadEntity) throws Exception;
		
		void updateCredentialEntity(Integer id);
		
		List<CredentialEntity> getPersonalCredentialById(Integer id) throws Exception;
		
		CredentialEntity getCredentialEntityBy(Integer Id);
		
		void checkCredential(String taskId, String result, String applyResult,Integer id);
		
		void updateCredential(Integer id, String applyResult,Integer status);
		
		void updateCredential(Integer id, Integer status);
		
		CredentialUploadEntity getCredentialUploadEntityById(Integer id);
		
		List<CredentialUploadEntity> getCredentialUploadEntityBy(Integer credentialEntityId);
		
		void updateCredentialUploadEntity(CredentialUploadEntity credentialUploadEntity,Integer id);
		
		void deleteCredentialUpload(Integer id);
}
