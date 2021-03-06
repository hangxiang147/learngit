package com.zhizaolian.staff.service.impl;




import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SigninDao;
import com.zhizaolian.staff.entity.SigninEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.SigninService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.SigninVO;

public class SigninServiceImpl implements SigninService {
	@Autowired	
	private SigninDao signinDao;

	@Override
	public void saveSignin(SigninVO signinVO) {
		Date date=new Date();
		List<SigninEntity> signinEntitys=(List<SigninEntity>) signinDao.findSigninList(signinVO.getUserID(), DateUtil.getSimpleDate(signinVO.getSigninDate()));
		if (signinEntitys.size() != 0) {
			//throw new RuntimeException("今日已签到！");
			return;
		}
		
		SigninEntity signinEntity=new SigninEntity();
		signinEntity.setUserID(signinVO.getUserID());
		signinEntity.setSigninID(signinVO.getSigninID());
		signinEntity.setSigninDate(DateUtil.getSimpleDate(signinVO.getSigninDate()));
		signinEntity.setAddTime(date);
		signinEntity.setUpdateTime(date);
		signinEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		signinDao.save(signinEntity);
	}

	@Override
	public Integer findSingleByDateUserID(Date date, String userID) {
		List<SigninEntity> signinEntities = signinDao.findSigninList(userID, date);
		if(signinEntities.size()!=0){
			return 1;
		}
		return 0;
	}
	


}
