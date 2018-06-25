package com.zhizaolian.staff.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhizaolian.staff.vo.TrainStudentCommentVo;

import lombok.Data;
@Data
@Entity
@Table(name="OA_CourseScore")
public class CourseScoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 课时表id
	 */
	private Integer coursePlanId; 
	/**
	 * 得分
	 */
	private String score;
	/**
	 * 职责(学员/讲师)
	 */
	private String duty;
	
	private String userId;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private String userName;
	
	private byte[] studentCommentVo;
	@Transient
	private TrainStudentCommentVo trainStudentCommentVo;
	
	private String commentUserId;//评价人
}	
