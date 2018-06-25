package com.zhizaolian.staff.entity;

import java.util.Date;
import java.util.List;

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
@Table(name="OA_TrainCourse")
public class TrainCourseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 课程名字
	 */
	private String courseName;
	/**
	 * 培训类别
	 */
	private String trainClass;
	/**
	 * 课程纲要
	 */
	private String description;
	/**
	 * 课时
	 */
	private String classHour;
	/**
	 * 讲师id
	 */
	private String lecturerIds;
	/**
	 * 讲师名字
	 */
	private String lecturerNames;
	/**
	 * 截止时间
	 */
	private String deadline;
	/**
	 * 已报名人数
	 */
	private String joinerNum;
	/**
	 * 截止人数
	 */
	private String maxPersonNum;
	
	private String attachmentIds;
	/**
	 * 首开培训时间
	 */
	private String beginTime;
	/**
	 * 是否公开报名（是/否）
	 */
	private String publicReport;
	/**
	 * 是否发送短信提醒  默认为0，1表示已发
	 */
	private Integer sendMsg;
	/**
	 * 培训取消原因
	 */
	private String cancelReason;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private String[] companyId;
	@Transient
	private String[] departmentId; 
	@Transient
	private String joinUsers;
	@Transient
	private String status;
	@Transient
	private List<String> companyAndDepNames;
	@Transient
	private List<CommonAttachment> attaList;
	@Transient
	private String role;
}
