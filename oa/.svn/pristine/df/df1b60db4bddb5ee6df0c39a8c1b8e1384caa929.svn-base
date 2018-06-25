package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractBorrowVo extends BaseVO {
	private static final long serialVersionUID = 1L;
	private Integer contractBorrowId;
	private Integer contractId;
	private String contractName;
	private String userId;
	private Integer isBorrow;
	private String reason;
	private Date startTime;
	private Date endTime;
	private Date realStartTime;
	private Date realEndTime;
	private String processInstanceID;
	private Integer applyResult;
	private Integer processStatus;
	private Date addTime;
	private String fileName;
	@Override
	public void createFormFields(List<FormField> fields) {	
		fields.add(getFormField("contractName", "证件名称", contractName));
		fields.add(getFormField("reason", "原因", reason));
		fields.add(getFormField("isBorrow", "是否外借", isBorrow==1?"是":"否"));
		if(1==isBorrow){			
			fields.add(getFormField("startTime", "申请开始时间",DateUtil.getMinStr(startTime)));
			fields.add(getFormField("endTime", "申请结束时间",DateUtil.getMinStr(endTime)));
			if(realStartTime!=null){
				fields.add(getFormField("realStartTime", "实际使用开始时间",DateUtil.getMinStr(realStartTime)));
			}
			if(realEndTime!=null){
				fields.add(getFormField("realEndTime", "实际使用归还时间",DateUtil.getMinStr(realEndTime)));
			}
		}
	}
}
