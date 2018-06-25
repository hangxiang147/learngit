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
@Table(name = "OA_MeetingActor")
public class MeetingActorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MtActorID", unique = true)
	private Integer mtActorID;
	
	@Column(name="MeetingID")
	private Integer meetingID;
	
	@Column(name="UserID")
	private String userID;
	
	@Column(name="ActorType")
	private Integer actorType;
	
	@Column(name="AddType")
	private Integer addType;
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;

}
