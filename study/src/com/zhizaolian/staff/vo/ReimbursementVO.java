package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReimbursementVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	private String reimbursementNo;  //报销单号
	
	private String requestUserID;  //报销人ID
	
	private String requestUserName;  //报销人姓名
	
	private String payeeID;  //领款人ID
	
	private String payeeName;  //领款人姓名
	
	private String cardName;  //户主姓名
	
	private String bank;  //开户行
	
	private String cardNumber;  //银行卡卡号
	
	private Integer invoiceTitleID;  //发票抬头
	
	private String invoiceTitle;  //发票抬头
	
	private Integer invoiceNum;  //发票张数
	
	private Integer detailNum;  //明细单张数
	
	private Double totalAmount;  //合计报销金额
	
	private String[] usage;  //用途
	
	private Double[] amount;  //金额
	
	private Date addTime;
	
	private String moneyType; //币种  (1000元以上由林总审批 js中select的value存放了大致的汇率)
	
	private String beginDate;
	
	private String endDate;
	
	/**
	 * 证明人Id
	 */
	private String reternenceId;
	
	/**
	 * 证明人姓名
	 */
	private String reternenceName;
	
	/**
	 * 证明人手机号
	 */
	private String reternenceMobile;
	/**
	 * 是否固定资产
	 */
	private Integer isFixedAsset;
	
	/**
	 *固定资产编号 
	 */
	private String fixedAssetNo;
	
	private String showPerson1;
	
	private String showPerson2;
	
	private String showPerson3;
	
	private Integer reimbursementID;  //报销记录表ID
	
	private List<byte[]> picList;
	
	private boolean canStopInstance;
	
	private String workingState;//在职状态
	
	private String taskId;
	
	private String thecurrenLink;//当前环节
	
	private String candidateUsers;//所有待处理人
	
	//是否重新发起   1表示已发
	private String reStart;
	
	public void createFormFields(List<FormField> fields) {
		
		fields.add(super.getFormField("requestUserName", "报销人", requestUserName));
		fields.add(super.getFormField("payeeName", "领款人", payeeName));
		fields.add(super.getFormField("reternenceName", "证明人", reternenceName));
		fields.add(super.getFormField("reternenceMobile", "证明人手机号", reternenceMobile));
		//老数据或者否的情况
		if(isFixedAsset==null||isFixedAsset==0){
			fields.add(super.getFormField("isFixedAsset", "是否固定资产","否"));
		}else{
			fields.add(super.getFormField("isFixedAsset", "是否固定资产", 1==isFixedAsset?"是":"否"));
			fields.add(super.getFormField("fixedAssetNo", "固定资产编号", fixedAssetNo));	
		}
		fields.add(super.getFormField("reimbursementNo", "报销单号", reimbursementNo));
		fields.add(super.getFormField("invoiceTitle", "发票抬头", invoiceTitle));
		fields.add(super.getFormField("invoiceNum", "发票张数", String.valueOf(invoiceNum)));
		fields.add(super.getFormField("detailNum", "明细单张数", String.valueOf(detailNum)));
		fields.add(super.getFormField("totalAmount", "合计报销金额", totalAmount.toString()));
		fields.add(super.getFormField("moneyType", "币种", StringUtils.isBlank(moneyType)?"人民币":moneyType.split(":")[0]));
		int usageLength = usage==null ? 0 : usage.length;
		for (int i=0; i<usageLength; ++i) {
			fields.add(super.getFormField("use"+(i+1), "用途"+(i+1), usage[i]+"；"+amount[i]+"元"));
		}
		fields.add(super.getFormField("cardName", "户主姓名", cardName));
		fields.add(super.getFormField("bank", "开户行", bank));
		fields.add(super.getFormField("cardNumber", "银行卡卡号", cardNumber));
	}
}
 