package com.zhizaolian.staff.vo;


import java.util.List;

import lombok.Data;

@Data
public class SkillVO {
	private Integer skillID;
	private String skill;
	private String master;
	private String userID;
	private String userName;
	private List<String> skills;
	private List<String> masters;
	
}
