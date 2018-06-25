package com.zhizaolian.staff.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="OA_ViewReportValid")
public class ViewReportValidEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String viewerId;
	
	private String userId;
	
	private String companyId;
	
	private String viewType;
	
	private String depId;
	
	private Integer isDeleted = 0;
}
