package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=true)
public class WorkOvertimeVo extends BaseVO{
	private static final long serialVersionUID = 8182101147419816450L;
	
	private Integer id;
	/**
	 * 加班开始时间
	 */
	private String beginDate;
	/**
	 * 加班结束时间
	 */
	private String endDate;
	/**
	 * 加班时长
	 */
	private String workHours;
	/**
	 * 加班原因
	 */
	private String reason;
	private String department;
	private String status;
	
	private String userID;
	
	private String requestUserName;
	private String requestUserID;
	private String type;//个人、部门
	private String companyId;//加班部门所在公司
	private String departmentId;//加班的部门
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	private List<String> overTimeUsers;
	
	private String thecurrenLink;//当前环节
	private String candidateUsers;//所有待处理人
	@Override
	public void createFormFields(List<FormField> fields) {
		// TODO Auto-generated method stub
		
	}

}
