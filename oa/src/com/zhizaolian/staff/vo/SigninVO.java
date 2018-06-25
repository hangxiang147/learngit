package com.zhizaolian.staff.vo;


import java.util.List;

import lombok.Data;

@Data
public class SigninVO {
	private Integer signinID;
	private String signinDate;
	private String userID;
	private String userName;//签到人姓名
	
	private List<String> groupList;  //签到人岗位列表
	
	private Integer count = 0;//每月未签到次数；
	
	private String[] signDates;//未签到日期数组
	
	private String departmentName;//部门名
}
