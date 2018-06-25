package com.zhizaolian.staff.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentTaskVO extends TaskVO {

	private String reimbursementNo; 
	
	private Double totalAmount;  
}
