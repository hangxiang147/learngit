package com.zhizaolian.staff.vo;

import lombok.Data;

/**
 * 岗位信息
 * @author zpp
 *
 */
@Data
public class GroupDetailVO {

	private Integer groupDetailID;    
	private String groupID;  //员工组ID
	private Integer companyID;  //公司ID
	private String companyName;  //公司名称
	private Integer departmentID;  //部门ID
	private String departmentName;  //部门名称
	private Integer positionID;  //职务ID
	private String positionName;  //ְ职务名称
	private String responsibility;  //岗位职责描述
	private String positionType;//蓝领、白领
	
}
