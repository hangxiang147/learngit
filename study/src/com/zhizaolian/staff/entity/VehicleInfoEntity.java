package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhizaolian.staff.vo.InsuranceRecord;
import com.zhizaolian.staff.vo.MaintainRecord;
import com.zhizaolian.staff.vo.RepairRecordVo;
import com.zhizaolian.staff.vo.VehicleUseRecord;
import com.zhizaolian.staff.vo.YearlyInspectionVo;

import lombok.Data;
@Data
@Entity
@Table(name="OA_VehicleInfo")
public class VehicleInfoEntity implements Cloneable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 车牌
	 */
	private String licenseNumber;
	/**
	 * 品牌
	 */
	private String brand;
	/**
	 * 车辆所有人
	 */
	private String ownerId;
	
	private String ownerName;
	/**
	 * 车架号 
	 */
	private String frameNumber;
	/**
	 * 使用性质
	 */
	private String useProperty;
	/**
	 * 发动机号
	 */
	private String engineNumber;
	/**
	 * 核定载重量
	 */
	private String deadWeight;
	/**
	 * 车辆负责人
	 */
	private String leaderId;
	private String leaderName;
	/**
	 * 保险记录
	 */
	private byte[] insuranceRecord;
	/**
	 * 年检记录
	 */
	private byte[] yearlyInspectionRecord;
	/**
	 * 保养记录
	 */
	private byte[] maintainRecord;
	/**
	 * 维修记录
	 */
	private byte[] repairRecord;
	/**
	 * 车辆使用记录
	 */
	private byte[] vehicleUseRecord;
	/**
	 * 保险办理（0、未办理；1、已办理）
	 */
	private Integer insuranceHandle;
	/**
	 * 年检办理（0、未办理；1、已办理）
	 */
	private Integer inspectionHandle;
	//0、未发；1、已发
	private Integer insuranceSendMsg;
	//0、未发；1、已发
	private Integer inspectionSendMsg;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private InsuranceRecord insuranceRecordVo;
	@Transient
	private YearlyInspectionVo yearlyInspectionVo;
	@Transient
	private MaintainRecord maintainRecordVo;
	@Transient
	private RepairRecordVo repairRecordVo;
	@Transient
	private VehicleUseRecord vehicleUseRecordVo;
	//车辆过期的类型（保险、年检）
	@Transient
	private String type;
	
    public VehicleInfoEntity clone() throws CloneNotSupportedException {  
        return (VehicleInfoEntity)super.clone();  
    }  
}
