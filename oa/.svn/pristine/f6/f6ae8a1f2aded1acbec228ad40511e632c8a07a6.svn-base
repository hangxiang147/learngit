package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.SpecialDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.SpecialEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.SpecialService;
import com.zhizaolian.staff.vo.SpecialVO;

public class SpecialServiceImp implements SpecialService {
	@Autowired	
	private SpecialDao specialDao;
	@Autowired	
	private BaseDao baseDao;
	@Autowired	
	private StaffDao staffDao;

	
	public void saveSpecial(SpecialVO specialVO) {
		SpecialEntity specialEntitie= specialDao.getSpecialByTypeAndID(specialVO.getUserID(), specialVO.getType());		
		Date date=new Date();		
		if(specialEntitie!=null){
			specialEntitie.setIsDeleted(0);
			specialEntitie.setAddTime(date);
			specialDao.save(specialEntitie);
		}else{
		SpecialEntity specialEntity=new SpecialEntity();
		specialEntity.setSpecialID(specialVO.getSpecialID());
		specialEntity.setType(specialVO.getType());
		specialEntity.setUserID(specialVO.getUserID());
		specialEntity.setAddTime(date);
		specialEntity.setUpdateTime(date);
		specialEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		specialDao.save(specialEntity);
		}
		
	}

	@Override
	public Integer getSpecialByTypeUserID(Integer type, String userID) {
		List<SpecialEntity> specialEntities=specialDao.findSpecialListByTypeAndID(userID, type);
		if(specialEntities.size()>0){
			return 1;
		}
		return 0;
	}

	@Override
	public List<SpecialVO> findBySql(Integer positionCategory, Integer type) {
		String sql="select "
				+ " special.UserID,special.SpecialID from OA_Special special "
				+ "LEFT JOIN OA_Staff staff on staff.UserID = special.UserID "
				+ "where staff.IsDeleted = 0 and special.IsDeleted = 0 and staff.PositionCategory ='"+positionCategory+"' "
						+ " and special.Type= '"+type+"' ";
		List<Object> result = baseDao.findBySql(sql);
		List<SpecialVO> specialVOs=new ArrayList<>();
		for(Object obj:result){
			SpecialVO specialVO=new SpecialVO();
			Object[] objs = (Object[]) obj;
			specialVO.setUserID((String)objs[0]);
			specialVO.setSpecialID((Integer)objs[1]);
			specialVO.setName(staffDao.getStaffByUserID(specialVO.getUserID()).getStaffName());
			specialVOs.add(specialVO);
		}
		return specialVOs;
	}

	@Override
	public void deleteSpecial(Integer specialID) {
		
		specialDao.deleteSpecial(specialID);
		
	}
	
	

}
