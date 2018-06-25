package com.zhizaolian.staff.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_MeetingMinutes")
public class MeetingMinutesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MMID", unique = true)
	private Integer mMID;
	
	@Column(name="MeetingID")
	private Integer meetingID;//会议ID
	
	@Column(name="Content")
	private String content;//纪要内容
	
	@Column(name="OwnerID")
	private String ownerID;//负责人
	
	@Column(name = "AttachementNames")
	private String attachementNames;  //上传附件文件名
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	
	

}
