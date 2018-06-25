package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ViewWorkReportVo extends BaseVO{
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String requestUserId;//申请人id
	
	private String requestUserName;
	
	private String companyIds;
	
	private String depIds;
	
	private String userIds;//页面选择的人员Id
	
	private String allUserIds;//被查看所有人员Id
	
	private Integer applyResult;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private String[] companyId;
	
	private String[] departmentId;
	
	private String taskID;
	
	private String taskName;
	
	private List<String> companyAndDepList;
	
	private String userNames;
	
	private String department;
	
	private String viewType;
	
	/********日报查询条件*********/
	private String userName;
	private Integer _companyID;
	private Integer _departmentID;
	private String beginDate;
	private String endDate;
	private String reportDate;
	/*************************/
	private boolean oneTime;
	private int page;
	@Override
	public void createFormFields(List<FormField> fields) {
		
	}
}
