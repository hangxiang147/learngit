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
@Table(name = "OA_VitaeSignFamily")
public class VitaeSignFamilyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "vitaeSignId")
	private Integer vitaeSignId;
	@Column(name = "type")
	private Integer type;
	@Column(name = "name")
	private String name;
	@Column(name = "relationShip")
	private String relationShip;
	@Column(name = "telephone")
	private String telephone;
	@Column(name = "sort")
	private Integer sort;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
}
