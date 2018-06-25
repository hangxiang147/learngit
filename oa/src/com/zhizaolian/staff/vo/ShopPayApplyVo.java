package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=true)
public class ShopPayApplyVo extends BaseVO implements Comparable<ShopPayApplyVo>{
	private static final long serialVersionUID = 7667459839896473974L;
	private Integer id;
	/**
	 * 表OA_SpreadShopApply的id
	 */
	private String spreadPayApplyIds;
	/**
	 * 表OA_ShopPayPlugin的id
	 */
	private String pluginPayApplyIds;
	/**
	 * 表OA_ShopOtherPay的id
	 */
	private String otherPayApplyIds;
	/**
	 * 是不是流程   0代表不是
	 */
	private String isProcess;
	
	private String beginDate;
	private String endDate;
	private String userID;
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	@Override
	public void createFormFields(List<FormField> fields) {
	}
	@Override
	public int compareTo(ShopPayApplyVo arg0) {
		Date addTime1 = DateUtil.getFullDate(super.getRequestDate());
		Date addTime2 = DateUtil.getFullDate(arg0.getRequestDate());
		if(addTime1.before(addTime2)){ 
			return 0;
		}
		return -1;
	}
}
