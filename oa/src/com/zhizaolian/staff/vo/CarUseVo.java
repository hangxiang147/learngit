package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class CarUseVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer carUse_Id;
	private String requestUserID;
	private String requsetUserName;
	private String handlePersonId;
	private String useTime;
	private Integer processStatus;
	private String processInstanceID;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("requsetUserName", "车辆申请人",requsetUserName));
		fields.add(super.getFormField("useTime", "预约时间", useTime));
		
	}
}
