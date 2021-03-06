package com.zhizaolian.staff.vo;

import lombok.Data;

/**
 * 员工信息变动记录实体类对应的VO类
 * @author wjp
 *
 */
@Data
public class StaffInfoAlterationVO {
	
	private Integer staffInfoAlterationID; //主键
	private String operatorID; //操作人，对应员工身份数据表的主键
	private String operatorName; //操作人姓名
	private String userID; //员工，对应员工身份数据表的主键
	private String userName; //员工姓名
	private Integer gradeIDBefore; //员工之前的职级，对应职级表的主键
	private Integer gradeIDAfter; //员工现在的职级，对应职级表的主键
	private String gradeNameBefore; //员工之前的职级，对应职级表的主键
	private String gradeNameAfter; //员工现在的职级，对应职级表的主键
	private String salaryBefore; //员工之前的薪资
	private String salaryAfter; //员工现在的薪资
	private Integer type; //修改的是员工的哪一项信息
	private String operateTime; //信息变更时间
	private String effectDate;
	private String attachmentIds;
}
