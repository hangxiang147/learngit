package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;

@Data
public class TaskVO {

	//标题
	private String title;
	//请求的用户名称
	private String requestUserName;
	
	
	//请求日期
	private String requestDate;
	
	//流程实例ID
	private String processInstanceID;
	
	//任务ID
	private String taskID;
	
	//任务名称
	private String taskName;
	
	private String taskDefKey;
	/**
	 * 受理人Id
	 */
	private String assigneeId;
	
	/**
	 * 受理人照片
	 */
	private byte[] assigneePic;
	//受理人姓名
	private String assigneeName;
	
	//任务完成时间
	private String endTime;
	
	//任务审批结果
	private String result;
	
	//转正申请人入职日期
	private String entryDate;
	
	//岗位列表
	private List<String> groupList;
	
	//业务类型名称，app待办列表用
	private String businessKey;
	
	//待办创建时间
	private String createTime;
	
	//调查人姓名
	private String auditUserName;
}
