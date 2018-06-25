package com.zhizaolian.staff.vo;

import java.io.File;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CardVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	private String requestUserID;  //工牌申请人ID
	
	private String requestUserName;  //工牌申请人姓名
	
	private String reason;  //申领原因
	
	private File[] attachment;  //附件
	
	private String[] attachmentFileName;  //附件名称
	
	private String staffNumber;  //员工工号
	
	private String nickName;  //花名
	
	private String companyName;  //地区名称
	
	private String departmentName;  //部门名称
	
	private String positionName;  //ְ职位名称
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("requestUserName", "工牌申请人", requestUserName));
		fields.add(super.getFormField("reason", "申领原因", reason));
	}

}
