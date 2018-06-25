package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
@Data
@Entity
@Table(name="OA_PublicRelationEvent")
public class PublicRelationEventEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true)
	private Integer id;
	
	private String eventDescription;
	
	private String userId;
	
	private String phone;//申请人电话
	
	private String deadlineDate;//截止时间
	
	private Integer publicRelationId;
	
	private Integer applyResult;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
	@Transient
	private String userName;
}
