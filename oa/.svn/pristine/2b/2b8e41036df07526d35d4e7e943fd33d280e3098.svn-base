package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractSignVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String contractId;
	private String contractName;
	private String otherCompanyName;
	private String description;
	private String money;
	private String userID;
	private Integer isPay;
	private Integer exceedSeason;
	private Integer exceedGroup;
	private String exceedSeasonRate;
	private String exceedGroupRate;
	private Integer applyResult;
	private String processInstanceID;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("contractId", "合同编号", contractId));
		fields.add(getFormField("contractName", "合同名称", contractName));
		fields.add(getFormField("otherCompanyName", "对方公司名称", otherCompanyName));
		fields.add(getFormField("description", "合同详情", description));
		fields.add(getFormField("money", "合同金额", money));
		fields.add(getFormField("isPay", "支出类合同", isPay==1 ? "是":"否"));
		fields.add(getFormField("exceedSeason", "超出本季度预算", (exceedSeason==null? "":(exceedSeason==1 ? "是":"否"))));
		fields.add(getFormField("exceedGroup", "超出集团审定预算", (exceedSeason==null? "":(exceedGroup==1 ? "是":"否"))));
		fields.add(getFormField("exceedSeasonRate", "超出本季度预算比例", exceedSeasonRate));
		fields.add(getFormField("exceedGroupRate", "超出集团审定预算比例", exceedGroupRate));
	}
}
