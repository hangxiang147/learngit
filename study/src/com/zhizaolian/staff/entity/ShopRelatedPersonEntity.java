package com.zhizaolian.staff.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="OA_ShopRelatedPerson")
public class ShopRelatedPersonEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private Integer shopDepId;//组id
	private String platform;//平台
	private String shopType;//业务类型：国内，B2B，跨境
	private String shopName;//店铺名称
	private String salesItem;//品类
	private String shopOwner;//店长
	private String saleBefore;//售前
	private String saleAfter;//售后
	private String operator;//运营
	private String service;//客服
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
}
