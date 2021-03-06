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
@Table(name = "OA_VitaeSign")
public class VitaeSignEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "vitaeId")
	private Integer vitaeId;
	@Column(name = "bh")
	private Integer bh;
	@Column(name = "tbsj")
	private Date tbsj;
	@Column(name = "xm")
	private String xm;
	@Column(name = "xb")
	private String xb;
	@Column(name = "mz")
	private String mz;
	@Column(name = "nl")
	private String nl;
	@Column(name = "zgxl")
	private String zgxl;
	@Column(name = "zzmm")
	private String zzmm;
	@Column(name = "jg")
	private String jg;
	@Column(name = "jkzk")
	private String jkzk;
	@Column(name = "hyqk")
	private String hyqk;
	@Column(name = "ywsb")
	private String ywsb;
	@Column(name = "dgsj")
	private Date dgsj;
	@Column(name = "qwxz")
	private String qwxz;
	@Column(name = "sfz")
	private String sfz;
	@Column(name = "lxfs")
	private String lxfs;
	@Column(name = "dz0")
	private String dz0;
	@Column(name = "dz1")
	private String dz1;
	@Column(name = "dz2")
	private String dz2;
	@Column(name = "dzDetail")
	private String dzDetail;
	@Column(name = "wysp")
	private String wysp;
	@Column(name = "tiSkill")
	private String tiSkill;
	@Column(name = "sfsgxscf")
	private String sfsgxscf;
	@Column(name = "sfyblsh")
	private String sfyblsh;
	@Column(name = "ah")
	private String ah;
	@Column(name = "jl")
	private String jl;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
}
