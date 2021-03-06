package com.zhizaolian.staff.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResignationVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	private String requestUserID;  //离职人ID
	
	private String requestUserName;  //离职人姓名
	
	private String leaveDate;  //申请离职日期  离职日期
	
	private String[] reason;  //离职原因
	
	private String note;  //备注
	
	private String reasons;  //页面展示离职原因
	
	private String supervisorConfirmDate;  //主管确认离职日期
	
	private String managerConfirmDate;  //总经理确认离职日期
	
	private String beginDate;//开始日期
	
	private String endDate;//结束日期
	
	private Integer companyID;
	
	private Integer departmentID;
	
	private String departmentName;//部门名称
	
	private String requestLeaveDate;  //申请离职日期
	
	private String telephone;  //手机号码
	
	private String entryDate;  //入职日期
	
	private Byte staffEntityStatus;
	
	private Byte processStatus;
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("requestUserName", "离职人", requestUserName));
		fields.add(super.getFormField("leaveDate", "申请离职日期", leaveDate));
		fields.add(super.getFormField("reason", "离职原因", StringUtils.join(reason, "；")));
		fields.add(super.getFormField("note", "备注", note));
		fields.add(super.getFormField("supervisorConfirmDate", "主管确认离职日期", supervisorConfirmDate));
		fields.add(super.getFormField("managerConfirmDate", "总经理确认离职日期", managerConfirmDate));
	}
}
