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
@Table(name = "OA_SoftPersonDetail")
public class SoftPersonDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "userId")
	private String userId;
	@Column(name = "type0")
	private String type0;
	@Column(name = "isLeadeer")
	private Integer isLeadeer;
	@Column(name = "groupPersons")
	private String groupPersons;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
}