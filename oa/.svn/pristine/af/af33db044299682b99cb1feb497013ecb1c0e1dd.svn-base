package com.zhizaolian.staff.service;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.EmailVO;

public interface EmailService {

	/**
	 * 启动一个公司邮箱申请流程
	 * @param emailVO
	 */
	void startEmail(EmailVO emailVO);
	
	/**
	 * 分页查询指定用户的公司邮箱申请
	 * @param userID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<EmailVO> findEmailListByUserID(String userID, int page, int limit);
	
	/**
	 * 更新公司邮箱申请流程节点的处理结果
	 * @param processInstanceID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	/**
	 * 根据taskID，查询对应邮箱申请流程的申请参数
	 * @param taskID
	 * @return
	 */
	EmailVO getEmailVOByTaskID(String taskID);
	
	/**
	 * 根据流程实例ID， 查询对应邮箱申请流程的申请参数
	 * @param processInstanceID
	 * @return
	 */
	EmailVO getEmailVOByProcessInstanceID(String processInstanceID);
	
	/**
	 * 确认邮箱开通信息
	 * @param processInstanceID
	 * @param address 邮箱地址
	 * @param password 初始密码
	 * @param loginUrl 登录网址
	 */
	void confirmEmailAccount(String processInstanceID, String address, String password, String loginUrl);
}
