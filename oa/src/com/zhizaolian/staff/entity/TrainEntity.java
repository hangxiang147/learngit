package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/*
 * 培训记录表
 */

@Data

@Entity
@Table(name = "OA_Train")
public class TrainEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TrainID", unique = true)
	private Integer trainID;
	
	@Column(name = "StartTime")
	private Date startTime;     //开始时间
	
	@Column(name = "EndTime")   
	private Date endTime;		//结束时间
	
	@Column(name = "Place")
	private String place;		//地点
	
	@Column(name = "Lector")   //讲师
	private String lector;
	
	@Column(name = "Topic")
	private String topic;		//主题
	
	@Column(name = "Content")
	private String content;		//内容
	
	@Column(name = "ParticipantNum")
	private Integer participantNum;//参与人员数
	
	@Column(name = "PID")
	private Integer PID;		//跟进id
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
