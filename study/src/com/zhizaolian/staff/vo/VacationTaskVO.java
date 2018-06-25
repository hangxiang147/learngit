package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VacationTaskVO extends TaskVO {

	private String vacationUserName;  //请假人
	private String vacationUserId; //请假人Id
	private String beginDate;  //开始时间
	
	private String endDate;  //结束时间
	
	private String vacationTime;  //请假时长
	
	private String agentName;  //工作代理人姓名
	
	private String vacationType;  //休假类型
	
	private String reason;  //休假原因
	
	private int attachmentSize;  //附件个数
	
	private List<byte[]> picLst;
	private String defKey; //请假较为特殊    有特殊步骤需要判断 所以 要通过这个值 去判断 当前是那步
	
	private List<String> staffNames;
	
	private String type;
}
