package com.zhizaolian.staff.timedTask;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.zhizaolian.staff.service.StaffSalaryService;
@Lazy(value=false)
public class StaffSalaryAction {
	
	private static Logger logger = Logger.getLogger(SynPerformanceTask.class);
	
	@Autowired
	private StaffSalaryService staffSalaryService;
	/**
	 * 根据已审批的调薪申请，每月初，刷新员工的薪资标准
	 */
	public void refreshStaffSalary(){
		try {
			staffSalaryService.refreshStaffSalary();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	/**
	 * 系统生成所有上月在职员工的基本的工资数据，侧重点在扣除项（工作日报未汇报、未打卡等等）、加班补贴、满勤等
	 */
	public void generateStaffMonthlySalary(){
		try {
			staffSalaryService.generateStaffMonthlySalary();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
}
