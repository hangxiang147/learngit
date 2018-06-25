package com.zhizaolian.staff.vo;


import java.util.List;

import lombok.Data;

@Data
public class TrainVO {
	private Integer trainID;//主键
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String place;//地点
	private String lector;//讲师
	private String topic;//主题
	private String content;//内容
	private List<String> participantIDs;//参与人IDs
	private Integer PID;//跟进的id
	private List<StaffVO> staffs;//参与人
	

}
