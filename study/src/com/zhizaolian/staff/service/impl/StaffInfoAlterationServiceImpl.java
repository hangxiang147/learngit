package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.zhizaolian.staff.dao.GradeDao;
import com.zhizaolian.staff.dao.StaffInfoAlterationDao;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffInfoAlterationEntity;
import com.zhizaolian.staff.service.StaffInfoAlterationService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.StaffInfoAlterationVOTransformer;
import com.zhizaolian.staff.transformer.StaffVOTransformer;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.StaffInfoAlterationVO;
import com.zhizaolian.staff.vo.StaffVO;
/**
 * 
 * @author wjp
 *
 */
@Transactional
public class StaffInfoAlterationServiceImpl implements StaffInfoAlterationService {
	
	@Autowired
	private StaffService staffService;
	
	@Autowired 
	private StaffInfoAlterationDao staffInfoAlterationDao;
	
	@Autowired
	private GradeDao gradeDao;
	
	
	/*
	 * 将员工信息变动记录保存到记录表，并 更新员工信息表
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(StaffInfoAlterationVO staffInfoAlterationVO) {
		
		StaffInfoAlterationEntity staffInfoAlterationEntity = new StaffInfoAlterationEntity();
		staffInfoAlterationEntity = StaffInfoAlterationVOTransformer.VOToEntity(staffInfoAlterationVO);
		Date date = new Date();
		staffInfoAlterationEntity.setIsDeleted(0);
		staffInfoAlterationEntity.setAddTime(date);
		staffInfoAlterationEntity.setUpdateTime(date);
		//拿到userID，查询员工信息表，得到薪资和职级;把薪资和职级放到staffInfoAlterationEntity里面，调用相应方法保存到数据库
		StaffVO staffVO = staffService.getStaffByUserID(staffInfoAlterationVO.getUserID());
		staffInfoAlterationEntity.setGradeBefore(staffVO.getGradeID());
		String beforeSalary = staffVO.getSalary();
		staffInfoAlterationEntity.setSalaryBefore(StringUtils.isBlank(beforeSalary)?"0":beforeSalary);
		if(staffInfoAlterationEntity.getGradeAfter()==null) //如果没有当前职级这个数据，那么当前职级设置为以前的职级
			staffInfoAlterationEntity.setGradeAfter(staffInfoAlterationEntity.getGradeBefore());
		if(staffInfoAlterationEntity.getSalaryAfter()==null) //如果没有当前薪资这个数据，那么当前薪资设置为以前薪资
			staffInfoAlterationEntity.setSalaryAfter(staffInfoAlterationEntity.getSalaryBefore());
		staffInfoAlterationDao.save(staffInfoAlterationEntity);
		//调用staffService的update方法，将修改的字段（目前是薪资和职级）set到staffVOQuery里面，staffVOQuery作为参数传进去，保存到员工信息表里面
		staffService.updateStaff(staffInfoAlterationVO); 
	
	}
	
	/*
	 * 查询职级历史记录
	 */
	public ListResult<StaffInfoAlterationVO> gradeHistory(String userID,Integer page, Integer limit) {
		
		ListResult<StaffInfoAlterationEntity> staffInfoAlterationEntityList = staffInfoAlterationDao.gradeHistory(userID,page,limit);
		if(staffInfoAlterationEntityList==null)
			return null;
		List<StaffInfoAlterationVO> staffInfoAlterationVOList = new ArrayList<StaffInfoAlterationVO>();
		for(StaffInfoAlterationEntity staffInfoAlterationEntity :staffInfoAlterationEntityList.getList()) {
			StaffInfoAlterationVO staffInfoAlterationVO = new StaffInfoAlterationVO();
			staffInfoAlterationVO = StaffInfoAlterationVOTransformer.entityToVO(staffInfoAlterationEntity);
			staffInfoAlterationVO.setGradeNameBefore(gradeDao.getGradeByGradeID(staffInfoAlterationVO.getGradeIDBefore()).getGradeName());
			staffInfoAlterationVO.setGradeNameAfter(gradeDao.getGradeByGradeID(staffInfoAlterationVO.getGradeIDAfter()).getGradeName());
			String id = ((User)ServletActionContext.getContext().getSession().get("user")).getId();
			staffInfoAlterationVO.setOperatorName(staffService.getStaffByUserID(id).getLastName()); //得到操作人姓名
			staffInfoAlterationVO.setUserName(staffService.getStaffByUserID(staffInfoAlterationVO.getUserID()).getLastName()); //得到被操作员工的姓名
			staffInfoAlterationVOList.add(staffInfoAlterationVO);
		}
		return (new ListResult<StaffInfoAlterationVO>(staffInfoAlterationVOList,staffInfoAlterationEntityList.getTotalCount())) ;
	}
	
	/*
	 * 查询薪资历史记录
	 */
	public ListResult<StaffInfoAlterationVO> salaryHistory(String userID,Integer page, Integer limit) {
		
		ListResult<StaffInfoAlterationEntity> staffInfoAlterationEntityList = staffInfoAlterationDao.salaryHistory(userID,page,limit);
		if(staffInfoAlterationEntityList==null)
			return null;
		List<StaffInfoAlterationVO> staffInfoAlterationVOList = new ArrayList<StaffInfoAlterationVO>();
		for(StaffInfoAlterationEntity staffInfoAlterationEntity :staffInfoAlterationEntityList.getList()) {
			StaffInfoAlterationVO staffInfoAlterationVO = new StaffInfoAlterationVO();
			staffInfoAlterationVO = StaffInfoAlterationVOTransformer.entityToVO(staffInfoAlterationEntity);
			staffInfoAlterationVO.setGradeNameBefore(gradeDao.getGradeByGradeID(staffInfoAlterationVO.getGradeIDBefore()).getGradeName());
			staffInfoAlterationVO.setGradeNameAfter(gradeDao.getGradeByGradeID(staffInfoAlterationVO.getGradeIDAfter()).getGradeName());
			staffInfoAlterationVO.setUserName(staffService.getStaffByUserID(staffInfoAlterationVO.getUserID()).getLastName()); //设置员工姓名
			staffInfoAlterationVO.setOperatorName(staffService.getStaffByUserID(staffInfoAlterationVO.getOperatorID()).getLastName()); //得到操作人姓名
			staffInfoAlterationVO.setUserName(staffService.getStaffByUserID(staffInfoAlterationVO.getUserID()).getLastName()); //得到被操作员工的姓名
			staffInfoAlterationVOList.add(staffInfoAlterationVO);
		}
		return (new ListResult<StaffInfoAlterationVO>(staffInfoAlterationVOList,staffInfoAlterationEntityList.getTotalCount())) ;
	}

	@Override
	public ListResult<StaffVO> findStaffByGradeAndName(StaffVO staffVO,Integer page, Integer limit) {
		
		ListResult<StaffEntity> staffEntityList = staffInfoAlterationDao.findStaffByGradeAndName(staffVO,page,limit);
		if(staffEntityList==null)
			return null;
		List<StaffVO> staffVOList = new ArrayList<StaffVO>();
		for(StaffEntity staffEntity :staffEntityList.getList()) {
			staffVO = StaffVOTransformer.INSTANCE.apply(staffEntity);
			staffVO.setGradeName(gradeDao.getGradeByGradeID(staffVO.getGradeID()).getGradeName());
			staffVOList.add(staffVO);
		}
		return (new ListResult<StaffVO>(staffVOList,staffEntityList.getTotalCount())) ;
	}

	
	
}
