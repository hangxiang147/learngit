package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeBankAccountVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String accountCompanyName;
	private String accountType;
	private String bankName;
	private String accountNumber;
	private String applyType;
	private String newAccountReason;
	private String accountUse;
	private String changeItem;
	private String afterChangeInfo;
	private String changeReason;
	private String deleteAccountReason;
	private String moneyWhere;
	private String userID;
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("accountCompanyName", "单位名称", accountCompanyName));
		fields.add(getFormField("accountType", "账户类别", accountType));
		fields.add(getFormField("bankName", "开户行全称", bankName));
		fields.add(getFormField("accountNumber", "账号", accountNumber));
		fields.add(getFormField("newAccountReason", "开户依据", newAccountReason));
		fields.add(getFormField("accountUse", "账户用途", accountUse));
		fields.add(getFormField("changeItem", "变更事项", changeItem));
		fields.add(getFormField("afterChangeInfo", "变更后信息", afterChangeInfo));
		fields.add(getFormField("changeReason", "变更原因", changeReason));
		fields.add(getFormField("deleteAccountReason", "销户原因", deleteAccountReason));
		fields.add(getFormField("moneyWhere", "资金去向", moneyWhere));
		fields.add(getFormField("applyType", "类型", applyType));
	}
}
