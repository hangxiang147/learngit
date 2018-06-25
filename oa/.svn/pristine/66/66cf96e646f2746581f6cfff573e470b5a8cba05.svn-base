package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractDetailVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer contract_Id;
	private String picId;
	private String no;
	private String requestUserId;
	private String requestUserName;
	private String responsiblePersonId;
	private String responsiblePersonName;
	private String detail;
	private String purpose;
	private String subjectPersonId;
	private String subjectPersonName;
	private String signPersoId;
	private String signPersonName;
	private String storePersonId;
	private String storePersonName;
	private String processInstanceID;
	private Date subjectTime;
	private Date signTime;
	private String storePlace;
	private Integer processStatus;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("no", "合同编号", no));
		fields.add(getFormField("detail", "合同细节", detail));
		fields.add(getFormField("purpose", "合同目的", purpose));
		fields.add(getFormField("responsiblePersonName", "负责人", responsiblePersonName));
		fields.add(getFormField("subjectPersonName", "审核人", subjectPersonName));
		fields.add(getFormField("signPersonName", "签字人", signPersonName));
	}
}
