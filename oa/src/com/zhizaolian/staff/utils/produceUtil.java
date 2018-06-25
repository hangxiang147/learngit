package com.zhizaolian.staff.utils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.GroupDetailVO;

public class produceUtil {
	
	public final static void initBaseVo(BaseVO baseVO,String userId,StaffService staffService,BusinessTypeEnum businessTypeEnum){
		//塞入baseVo的信息
		baseVO.setUserID(userId);
		baseVO.setUserName(staffService.getRealNameByUserId(userId));
		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(baseVO.getUserID());
		if (CollectionUtils.isEmpty(groups)) {
			throw new RuntimeException("无法查询到当前人员所在部门位置");
		}
		GroupDetailVO groupVo =groups.get(0);
		baseVO.setBusinessType(businessTypeEnum.getName());
		baseVO.setTitle(baseVO.getUserName()+"的"+businessTypeEnum.getName());
		baseVO.setUserName(baseVO.getUserName()+"("+groupVo.getCompanyName()+"-"+groupVo.getDepartmentName()+"-"+groupVo.getPositionName()+")");		
		baseVO.setRequestDate(DateUtil.formateFullDate(new Date()));
	}
	/**
	 * 因为每个流程的实体类都需要塞入 isDeleted instanceId 和 addTime 3个字段
	 * @param object
	 * @throws  
	 * @throws NoSuchMethodException 
	 */
	public final static void initEntity(Object obj,String instanceId) throws Exception{
		Method isDeletedMethod=obj.getClass().getMethod("setIsDeleted", Integer.class);
		isDeletedMethod.invoke(obj,0);
		Method dateMethod=obj.getClass().getMethod("setAddTime", Date.class);
		dateMethod.invoke(obj, new Date());
		Method instanceIdMethod=obj.getClass().getMethod("setInstanceId", String.class);
		instanceIdMethod.invoke(obj, instanceId);
	} 
}
