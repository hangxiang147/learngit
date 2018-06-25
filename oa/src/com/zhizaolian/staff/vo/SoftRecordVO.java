package com.zhizaolian.staff.vo;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author wjp
 *
 */
@Data
public class SoftRecordVO {
	
	private Integer softRecordID;// 主键
	private String userID;  //对应员工身份数据表主键
	private Integer softID;	//对应软件上传下载表的主键
	private Integer type;	//状态（上传/下载）
	private Date time;	//上传/下载时间
}
