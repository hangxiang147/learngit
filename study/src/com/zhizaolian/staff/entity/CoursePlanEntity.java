package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhizaolian.staff.vo.CommentVO;

import lombok.Data;
@Data
@Entity
@Table(name="OA_CoursePlan")
public class CoursePlanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer courseId;
	
	private String beginTime;
	/**
	 * 培训时长
	 */
	private String trainHours;
	/**
	 * 培训地点
	 */
	private String place;
	/**
	 * 讲师
	 */
	private String lecturer;
	/**
	 * 是否发过短信提醒 默认为0，1表示已发
	 */
	private Integer sendMsg;
	
	private String processStatus;
	
	private String processInstanceID;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private String[] _id;
	@Transient
	private String[] _beginTime;
	@Transient
	private String[] _trainHours;
	@Transient
	private String[] _place;
	@Transient
	private String[] _lecturer;
	@Transient
	private String reason;//请假原因
	@Transient
	private String auditStatus;//请假的审批状态
	@Transient
	private CommentVO comment;
}
