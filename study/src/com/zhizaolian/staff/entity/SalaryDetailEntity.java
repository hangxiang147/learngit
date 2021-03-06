package com.zhizaolian.staff.entity;

import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_SalaryDetail")
public class SalaryDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private Integer year;
	private Integer month;
	@Column(length=100)
	private String name;
	@Column(length=100)
	private String departmentName;
	private String userId;
	@Column(length=4000)
	private String content;
	private Integer status;
	private byte[] detail;
	@Transient
	private List<String> detailList;
	private Integer  emailSend;
	private Integer  mobileSend;
	private String confirm;
}
