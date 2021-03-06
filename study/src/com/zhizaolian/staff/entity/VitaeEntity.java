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
@Table(name = "OA_Vitae")
public class VitaeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true)
	private Integer id;
	@Column(name = "PostCompanyId")
	private String postCompanyId;
	@Column(name = "PostCompanyName")
	private String postCompanyName;
	@Column(name = "PostDepartementId")
	private String postDepartementId;
	@Column(name = "PostDepartmentName")
	private String postDepartmentName;
	@Column(name = "PostPositionId")
	private String postPositionId;
	@Column(name = "PostPositionIname")
	private String postPositionName;
	@Column(name = "RequestUserId")
	private String requestUserId;
	@Column(name = "RequestUserName")
	private String requestUserName;
	@Column(name = "Reason")
	private String reason;
	@Column(name = "PostName")
	private String postName;
	@Column(name = "RealPostName")
	private String realPostName;
	@Column(name = "NeedPersonDescription")
	private String needPersonDescription;
	@Column(name = "RealNeedPersonDescription")
	private String realNeedPersonDescription;
	@Column(name = "NeddPersonNumber")
	private Integer neddPersonNumber;
	@Column(name = "InstanceId")
	private String instanceId;
	@Column(name = "Result")
	private Integer result;
	@Column(name = "FormatDepartMentId")
	private String formatDepartMentId;
	@Column(name = "VitaeDetailId")
	private Integer vitaeDetailId;
	@Column(name = "RealSubjectPersonIds")
	private String realSubjectPersonIds;
	@Column(name = "RealTechnologySubjectPersonIds")
	private String realTechnologySubjectPersonIds;
	@Column(name = "EffectivePersonNumber")
	private Integer effectivePersonNumber;
	@Column(name = "AddTime")
	private Date addTime;
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	@Column(name = "UpdateTime")
	private Date updateTime;
}
