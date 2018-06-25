package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SkillDao;
import com.zhizaolian.staff.entity.SkillEntity;

import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.SkillService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SkillVO;
import com.zhizaolian.staff.vo.StaffVO;

public class SkillServiceImpl implements SkillService {
	@Autowired
	private SkillDao skillDao;
	@Autowired
	private StaffService staffService;
	@Override
	public void addSkill(StaffVO staffVO) {
		if (staffVO == null) {
			throw new RuntimeException("获取员工信息失败！");
		}
		Date now = new Date();
		SkillEntity skillEntity=new SkillEntity();
		skillEntity.setUserID(staffVO.getUserID());
		skillEntity.setSkill(staffVO.getSkill());
		skillEntity.setMaster(staffVO.getMaster());
		skillEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		skillEntity.setAddTime(now);
		skillEntity.setUpdateTime(now);
		skillDao.save(skillEntity);
	}
	
	
	@Override
	public List<StaffVO> getSkillByUserID(String userID) {
		List<SkillEntity> skillEntities=skillDao.findSkillsByUserID(userID);
		List<StaffVO> list=new ArrayList<>();
		for(SkillEntity skillEntity:skillEntities){
			StaffVO staffVO=new StaffVO();
			staffVO.setSkill(skillEntity.getSkill());
			staffVO.setMaster(skillEntity.getMaster());
		}
		return list;
	}
	
	
	@Override
	public ListResult<SkillVO> findSkillListByUserID(SkillVO skillVO, int page, int limit) {
		List<Object> list=skillDao.findSkillListByUserID(getQuerySqlByStaffVO(skillVO), page, limit);
		List<SkillVO> skillVOs=new ArrayList<>();
		for(Object obj:list){
			Object[] objs = (Object[]) obj;
			SkillVO skillVO2=new SkillVO();
			skillVO2.setSkillID((Integer)(objs[0]));
			skillVO2.setUserID((String)objs[1]);
			String[] skills1=StringUtils.split((String) objs[2], ",");
			List<String> skills=new ArrayList<>();
			if(skills1!=null){
			for(int i=0;i<skills1.length;i++){
				String skill=String.valueOf(skills1[i]);
				skills.add(skill);
			}	
			
			}
			skillVO2.setSkills(skills);
			String[] masters=StringUtils.split((String) objs[3], ",");
			List<String> master1=new ArrayList<>();
			if(masters!=null){
			for(int i=0;i<masters.length;i++){
				String master=String.valueOf(masters[i]);
				master1.add(master);
			}				
			}
			skillVO2.setMasters(master1);
			skillVO2.setUserName((String) objs[4]);
			skillVOs.add(skillVO2);
		}
		Object countObj=skillDao.getUniqueResult(getQueryCountSql(skillVO));
		int count=countObj==null? 0: ((BigInteger)countObj).intValue();
		return new ListResult<SkillVO>(skillVOs,count);
	}
	
	String getQuerySqlByStaffVO(SkillVO skillVO){
		StringBuffer sql=new StringBuffer("select skill.SkillID,os.UserID,GROUP_CONCAT(skill.Skill),  "
				+ " GROUP_CONCAT(skill.Master),os.StaffName from  OA_Staff os LEFT JOIN "
				+ " OA_Skill skill ON skill.UserID=os.UserID where os.IsDeleted = 0 and (skill.IsDeleted = 0 or skill.IsDeleted is null) and os.Status != 4 ");				
		sql.append(getWhereByStaffVO(skillVO));
		sql.append(" GROUP BY os.UserID ");
		return sql.toString();
	}
	
	private String getQueryCountSql(SkillVO skillVO){
		StringBuffer sql=new StringBuffer("select count(*) from (select skill.SkillID,os.UserID,GROUP_CONCAT(skill.Skill), "
				+ " GROUP_CONCAT(skill.Master),os.StaffName from  OA_Staff os LEFT JOIN "
				+ " OA_Skill skill ON skill.UserID=os.UserID where os.IsDeleted = 0 and (skill.IsDeleted = 0 or skill.IsDeleted is null) and os.Status != 4 ");				
		sql.append(getWhereByStaffVO(skillVO));
		sql.append(" GROUP BY os.UserID) s ");
		return sql.toString();
	}
	
	private String getWhereByStaffVO(SkillVO skillVO){
		StringBuffer whereSql=new StringBuffer();
		if (!StringUtils.isBlank(skillVO.getUserName())) {
			whereSql.append(" and os.StaffName like '%"+skillVO.getUserName()+"%'");
		}
		if (skillVO.getSkill() != null && !skillVO.getSkill().equals("")) {
			whereSql.append(" and skill.Skill like '%"+skillVO.getSkill()+"%'");
		}
		if (skillVO.getMaster() != null && !skillVO.getMaster().equals("")) {
			whereSql.append(" and skill.Master like '%"+skillVO.getMaster()+"%'");
		}
		return whereSql.toString();
	}


	@Override
	public SkillVO getSkillBySkillID(String userID) {
		List<SkillEntity> skillEntities=skillDao.getSkillBySkillID(userID);
		SkillVO skillVO=new SkillVO();
		if(skillEntities.size()!=0){
		List<String> list=new ArrayList<>();
		List<String> list1=new ArrayList<>();		
		for(SkillEntity skillEntity:skillEntities){		
		skillVO.setUserID(userID);
		skillVO.setUserName(staffService.getStaffByUserID(skillEntity.getUserID()).getLastName());
		list.add(skillEntity.getSkill());
		list1.add(skillEntity.getMaster());		
		}
		skillVO.setSkills(list);
		skillVO.setMasters(list1);
		}else{
			skillVO.setUserID(userID);
			skillVO.setUserName(staffService.getStaffByUserID(userID).getLastName());
		}
		return skillVO;
	}


	@Override
	public void updateSkill(StaffVO staffVO,String userID) {
		skillDao.delete(userID);
		if (staffVO == null) {
			throw new RuntimeException("获取员工信息失败！");
		}
		String[] skills=staffVO.getSkills();
		String[] masters=staffVO.getMasters();                          
		if(skills!=null && masters!=null){
		for(int i=0;i<skills.length;i++){
		Date now = new Date();
		SkillEntity skillEntity=new SkillEntity();
		skillEntity.setUserID(userID);
		skillEntity.setSkill(skills[i]);
		skillEntity.setMaster(masters[i]);
		skillEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		skillEntity.setAddTime(now);
		skillEntity.setUpdateTime(now);
		skillDao.save(skillEntity);
		}
	}
	}
}
