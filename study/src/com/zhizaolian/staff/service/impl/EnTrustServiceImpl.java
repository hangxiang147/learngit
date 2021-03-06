package com.zhizaolian.staff.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
//import com.zhizaolian.staff.dao.PermissionMembershipDao;
import com.zhizaolian.staff.dao.RightDao;
//import com.zhizaolian.staff.entity.PermissionMembershipEntity;
import com.zhizaolian.staff.service.EnTrustService;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;

public class EnTrustServiceImpl  implements EnTrustService{
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private RightDao rightDao;
/*	@Autowired
	private PermissionMembershipDao permissionMembershipDao;*/
	@Override
	public List<Object> findTaskIdsByUserGroupIDs(String taskName,
			List<String> groups, List<String> users) {
		String groupIDs = Arrays.toString(Lists2.transform(groups, new SafeFunction<String, String>() {
			@Override
			protected String safeApply(String input) {
				return "'"+input+"'";
			}
		}).toArray());
		String userIDs = Arrays.toString(Lists2.transform(users, new SafeFunction<String, String>() {
			@Override
			protected String safeApply(String input) {
				return "'"+input+"'";
			}
		}).toArray());
		String  sql= "select DISTINCT task.ID_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ ='"+taskName+"' "
				+ "and (identityLink.GROUP_ID_ in ("+groupIDs.substring(1, groupIDs.length()-1)+") "
				+ "or identityLink.USER_ID_ in ("+userIDs.substring(1, userIDs.length()-1)+"))";
		return  baseDao.findBySql(sql);
	}
	@Override
	public List<Object> findAssigneeTaskIdsByUserIdAndTaskName(String taskName,
			String assigneeId) {
		String sql="SELECT u.ID_ FROM ACT_RU_TASK u where u.ASSIGNEE_='"+assigneeId+"' and u.TASK_DEF_KEY_='"+taskName+"' ";
		return  baseDao.findBySql(sql);
	}
	@Override
	public Object getDetailByRightID(String rightId) {
		String sql="select * from OA_EnTrust where rightId='"+rightId+"'";
		return baseDao.getUniqueResult(sql);
	}
	@Override
	public void transferAssigneer(String receiverUserId, String resignationUserID) {
		String sql = "update ACT_RU_TASK set ASSIGNEE_='"+receiverUserId+"' where ASSIGNEE_='"+resignationUserID+"'";
		baseDao.excuteSql(sql);
	}
/*	@Override
	public void addRight(String receiverUserId, int rightId) {
		PermissionMembershipEntity permission = permissionMembershipDao.findPermissionMembershipById(rightId);
		//判断交接人有无该权限，若没有，加上
		if(!rightDao.checkHasThisRight(receiverUserId, permission.getPermissionID())){
			rightDao.createRightMemberShip(receiverUserId, "1", String.valueOf(permission.getPermissionID()));
		}
	}*/
	@Override
	public void addRight(String receiverUserId, int rightId) {
		//PermissionMembershipEntity permission = permissionMembershipDao.findPermissionMembershipById(rightId);
		//判断交接人有无该权限，若没有，加上
		if(!rightDao.checkHasThisRight(receiverUserId, rightId)){
			rightDao.createRightMemberShip(receiverUserId, "1", String.valueOf(rightId));
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void transferTasks(String receiverUserId, String resignationUserID) {
		String hql = "select id from ToBeDoneTaskEntity where userId='"+resignationUserID+"' and isDeleted=0 and status=0";
		List<Integer> ids = (List<Integer>) baseDao.hqlfind(hql);
		for(Integer id: ids){
			hql = "update ToBeDoneTaskEntity set userId='"+receiverUserId+" where id="+id;
			baseDao.excuteHql(hql);
		}
	}
}
