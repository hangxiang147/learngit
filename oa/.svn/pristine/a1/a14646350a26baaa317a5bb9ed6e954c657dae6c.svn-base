package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.FormField;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripVo extends BaseVO  {

	private static final long serialVersionUID = -2860665280903014597L;
	private String requestUserID;
	private String requestUserName;
	private String reason;
	private Date startTime;
	private Date endTime;
	private Integer isNeedTicket;
	private String ticketDetail;
	private Integer applyResult;
	private String processInstanceID;
	private String itemPlace;
	private String vehicle;
	@Override
	public void createFormFields(List<FormField> fields) {	
		fields.add(getFormField("reason", "出差原因", reason));
		fields.add(getFormField("startTime", "出差开始时间", DateUtil.formateFullDate(startTime)));
		fields.add(getFormField("itemPlace", "出差地点", itemPlace));
		fields.add(getFormField("vehicle", "交通工具", vehicle));
		fields.add(getFormField("endTime", "出差结束时间", endTime==null?"":DateUtil.formateFullDate(endTime)));
		fields.add(getFormField("isNeedTicket", "是否需要协助购买车票", 1==isNeedTicket?"是":"否"));
		if(1==isNeedTicket){			
			fields.add(getFormField("ticketDetail", "车票详情",ticketDetail));
		}
	}
}
