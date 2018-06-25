package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 店铺注册信息
 * @author yxl
 *
 */
@Data
@Entity
@Table(name="OA_ShopInfo")
public class ShopInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String shopName;//店铺品牌
	
	private String registerTelephone;//注册手机号
	
	private String regPhoneOwner;//注册的手机号的属主
	
	private String reserveTelephone;//预留手机号
	
	private String reservePhoneOwner;//预留的手机号的属主
	
	private String account;//账号
	
	private String pwd;
	
	private String registerCompany;//注册公司
	
	private String registerNum;//营业执照注册号
	
	private String registerAlipayAccount;//注册支付宝账户
	
	private String registerBankAccount;//注册银行卡账号
	
	private String openDate;//开店时间
	
	private Integer shopStatus;//0代表开着，1代表关闭
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	
	private String reservePhoneStatus;
}
