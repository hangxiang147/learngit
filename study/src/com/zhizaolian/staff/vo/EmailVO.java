package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	private String requestUserID;  //邮箱申请人ID
	
	private String requestUserName;  //邮件申请人姓名
	
	private String address;  //申请邮箱地址
	
	private String reason;  //申请原因
	
	private String confirmAddress;  //开通邮箱地址
	
	private String originalPassword;  //初始密码
	
	private String loginUrl;  //登录网址
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("requestUserName", "邮箱申请人", requestUserName));
		fields.add(super.getFormField("address", "邮箱地址", address));
		fields.add(super.getFormField("reason", "申请原因", reason));
	}
}
