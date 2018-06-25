package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeContractVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String contractId;
	private String contractName;
	private String contractContent;
	private String signDate;
	private String contractDescription;
	private String isChange;
	private String changeReason;
	private String beforeChangeContent;
	private String afterChangeContent;
	private String relieveReason;
	private String userID;
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("contractId", "合同编号", contractId));
		fields.add(getFormField("contractName", "合同名称", contractName));
		fields.add(getFormField("contractContent", "合同内容", contractContent));
		fields.add(getFormField("signDate", "签订时间", signDate));
		fields.add(getFormField("contractDescription", "合同已履行情况介绍", contractDescription));
		fields.add(getFormField("changeReason", "变更合同原因", changeReason));
		fields.add(getFormField("beforeChangeContent", "变更前内容", beforeChangeContent));
		fields.add(getFormField("afterChangeContent", "变更后内容", afterChangeContent));
		fields.add(getFormField("relieveReason", "解除合同原因", relieveReason));
	}
}
