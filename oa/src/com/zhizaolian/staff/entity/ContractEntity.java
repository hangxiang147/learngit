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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OA_Contract")
public class ContractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ContractID", unique = true)
	private Integer contractID;
	
	@Column(name="PartyA")
	private Integer partyA;//甲方
	
	@Column(name="PartyB")
	private String partyB;//乙方
	
	@Column(name="BeginDate")
	private Date beginDate; //开始日期
	
	@Column(name="EndDate")
	private Date endDate; //结束日期
	
	@Column(name="ContractBackups")
	private String contractBackups; //合同备份
	
	@Column(name="Signature")
	private String signature; //签名
	
	@Column(name = "Status")
	private Integer status;  //合同是否有效

	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
}
