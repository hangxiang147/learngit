package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class StaffQueryVO {

	private String name;  //员工姓名
	private Integer status;  //在职状态
	private Integer companyID;  //公司ID
	private Integer departmentID;  //部门ID
	private String skills;
	private boolean isFuzzyQuery_Name = true;  //是否允许按姓名模糊查询
	private String personalPost;//岗位名称
}
