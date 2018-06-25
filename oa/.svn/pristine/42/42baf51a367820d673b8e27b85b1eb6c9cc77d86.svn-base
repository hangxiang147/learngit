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
@Table(name="OA_PerformancePositionTemplate")
public class PerformancePositionTemplateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 模板名称
	 */
	private String templateName;
	/**
	 * 岗位id
	 */
	private String positionId;
	
	private Integer status;//0：已申请；1：通过；2：不通过
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private List<PerformanceProjectEntity> projects;
	
}
