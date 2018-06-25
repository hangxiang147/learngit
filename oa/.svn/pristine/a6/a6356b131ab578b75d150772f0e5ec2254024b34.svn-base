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
@Table(name="OA_SoftPerformanceProject")
public class ProjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private String name;
	private String description;
	private Integer isDelete;
	private Date addTime;
	private Date updateTime;
	private String code;
	private String projectHeader;
	private String testHeader;
	private String creatorId;
	private String updatestVersion;
	
	private Double xq;
	private Double ss;
	private Double jl;
	private Double kf;
	private Double cs;
	private Double zz;
}
