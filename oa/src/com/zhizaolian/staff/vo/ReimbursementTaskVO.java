package com.zhizaolian.staff.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReimbursementTaskVO extends TaskVO {

	private String reimbursementNo;  //报销单号
	
	private Double totalAmount;  //合计报销金额
}
