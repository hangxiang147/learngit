package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="OA_SoftPerformanceVersion")
public class ProjectVersionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private String version;
	private Integer projectId;
	private Integer isDelete;
	private Date addTime;
	private Date updateTime;
	private String creatorId;
	private String status;
	private String beginDate;
	private String endDate;
	private Integer developerNum;
	private String workHour;
	private String developers;
	private String pms;
	private String fenXis;
	private String testers;
	private String shiShis;
	private Integer pmNum;
	private Integer fenXiNum;
	private Integer testerNum;
	private Integer shiShiNum;
	@Transient
	private String[] pm;
	@Transient
	private String[] fenXi;
	@Transient
	private String[] developer;
	@Transient
	private String[] tester;
	@Transient
	private String[] shiShi;
}
