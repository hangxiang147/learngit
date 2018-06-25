package com.zhizaolian.staff.vo;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StaffAuditVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
	
	private String auditUserID;  //调查人ID
	
	private String auditUserName;  //调查人姓名
	
	private String educationID;  //学历证书编号
	
	private String degreeID;  //学位证书编号
	
	private String criminalRecord;  //案底说明
	
	private String[] company;  //企业名称
	
	private Double[] years;  //工作时间
	
	private String[] beginDate;
	
	private String[] endDate;
	
	private String[] description;  //工作经历描述
	
	private String[] referee;  //证明人
	
	private String[] telephone;  //证明人联系电话

	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("auditUserName", "调查人", auditUserName));
	} 
}
