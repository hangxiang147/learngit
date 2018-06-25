package com.zhizaolian.staff.vo;

import java.util.Date;

import lombok.Data;
@Data
public class CarpoolVo {
	private Integer Carpool_Id;
	private Integer  Dept_Id;
	private String Dept_Name;
	private Integer Company_Id;
	private Date StartTime;
	private Date EndTime;
	private String StartPlace;
	private String EndPlace;
	private String PersonDetail;
	private Object[] PersonDetail_;
	private String Remarks;
	private Date AddTime;
	private Date UpdateTime;
	private Integer IsDeleted;
}
