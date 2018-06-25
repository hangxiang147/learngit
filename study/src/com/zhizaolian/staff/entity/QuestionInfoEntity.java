package com.zhizaolian.staff.entity;

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
@Table(name="OA_CourseQuestionInfo")
public class QuestionInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer coursePlanId;
	
	private String title;
	/**
	 * 试题选项
	 */
	private String choiceA;
	private String choiceB;
	private String choiceC;
	private String choiceD;
	private String choiceE;
	private String choiceF;
	/**
	 * 是否是多项选择，默认为0，1代表是
	 */
	private Integer multipleChoice;
	/**
	 * 试题答案
	 */
	private String answer;
	/**
	 * 测试时间（课时所有题目）
	 */
	private String timer;
	@Transient
	private String choice;
	@Transient
	private boolean correct;
}
