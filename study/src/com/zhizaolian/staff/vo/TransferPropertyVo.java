package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class TransferPropertyVo extends BaseVO{
	
	private static final long serialVersionUID = 4201400114318791355L;

	private Integer id;

	private String assetId;
	
	private String assetName;

	private String assetNum;

	private String assetType;
	/**
	 * 规格型号
	 */
	private String model;

	private String number;

	private String unitPrice;

	private String money;
	/**
	 * 调拨原因
	 */
	private String transferReason;
	/**
	 * 调出单位
	 */
	private String oldCompany;
	/**
	 * 调入单位
	 */
	private String newCompany;

	private String userID;
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("assetName", "资产名称", assetName));
		fields.add(getFormField("assetNum", "资产编号", assetNum));
		fields.add(getFormField("assetType", "类型", assetType));
		fields.add(getFormField("model", "规格型号", model));
		fields.add(getFormField("number", "数量", number));
		fields.add(getFormField("unitPrice", "单价", unitPrice));
		fields.add(getFormField("money", "金额", money));
		fields.add(getFormField("transferReason", "调拨原因", transferReason));
		fields.add(getFormField("oldCompany", "调出单位", oldCompany));
		fields.add(getFormField("newCompany", "调入单位", newCompany));
		fields.add(getFormField("assetId", "资产ID", assetId));
	}
}
