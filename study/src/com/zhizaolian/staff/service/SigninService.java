package com.zhizaolian.staff.service;
import java.util.Date;

import com.zhizaolian.staff.vo.SigninVO;

public interface SigninService {
	
	void saveSignin(SigninVO signinVO);
	
	Integer findSingleByDateUserID(Date date,String userID);

}
