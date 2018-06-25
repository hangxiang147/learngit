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
@Table(name="OA_PartnerQuitSalaryDetails")
public class PartnerQuitSalaryDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String userId;
	
	private byte[] quitSalaryDetails;
	
}
