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
@Table(name = "OA_Meeting")
public class MeetingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MeetingID", unique = true)
	private Integer meetingID;
	
	@Column(name="SponsorID")
	private String sponsorID;   //会议发起人
	
	@Column(name="BeginTime")
	private Date beginTime;     //会议开始时间
	
	@Column(name="EndTime")     //会议结束时间
	private Date endTime;
	
	@Column(name="Place")       //会议地点
	private String place;
	
	@Column(name="MeetingType")
	private Integer meetingType;//会议类型
	
	@Column(name="Theme")
	private String theme;       //会议主题
	
	@Column(name="Content")
	private String content;     //内容概要
	

	
	@Column(name = "UploadNames")
	private String uploadNames;  //上传文件名
	
	@Column(name="PpCompanyIDs")
	private String ppCompanyIDs;  //参加人公司列表
	
	@Column(name="PpDepartmentIDs")
	private String ppDepartmentIDs;//参加人部门列表
	
	@Column(name="CcCompanyIDs")
	private String ccCompanyIDs;  //抄送人公司列表
	
	@Column(name="CcDepartmentIDs")
	private String ccDepartmentIDs;//抄送人部门列表
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;

}
