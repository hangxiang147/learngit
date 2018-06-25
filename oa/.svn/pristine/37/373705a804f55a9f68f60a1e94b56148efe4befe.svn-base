package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="OA_MorningMeeting")
public class MorningMeetingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String weekday;
	
	private String hasMeeting;//是否开早会
	
	private String noMeetingReason;//未开早会的原因
	
	private String remark;//备注说明
	
	private String description;//早会具体内容
	
	private String attachmentIds;
	
	private String userID;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private Date reportTime;;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
}
