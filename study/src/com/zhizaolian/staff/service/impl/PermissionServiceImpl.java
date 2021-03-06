package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.GroupDetailDao;
import com.zhizaolian.staff.dao.PermissionDao;
import com.zhizaolian.staff.dao.PermissionMembershipDao;
import com.zhizaolian.staff.entity.GroupDetailEntity;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.entity.PermissionMembershipEntity;
import com.zhizaolian.staff.enums.UserGroupTypeEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;

public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private GroupDetailDao groupDetailDao;
	@Autowired
	private PermissionMembershipDao permissionMembershipDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private BaseDao baseDao;
	@Override
	public List<String> findPermissionsByUserID(String userID) {
		List<Integer> userPermissionIDs = permissionMembershipDao.findPermissionIDsByUserGroupIDType(userID, UserGroupTypeEnum.USER.getValue());
		List<Integer> groupPermissionIDs = findGroupPermissionsByUserID(userID);
		groupPermissionIDs.removeAll(userPermissionIDs);
		groupPermissionIDs.addAll(userPermissionIDs);
		
		if (CollectionUtils.isEmpty(groupPermissionIDs)) {
			return Collections.emptyList();
		}
		List<PermissionEntity> permissionEntities = permissionDao.findPermissionsByIDs(groupPermissionIDs);
		return Lists2.transform(permissionEntities, new SafeFunction<PermissionEntity, String>() {
			@Override
			protected String safeApply(PermissionEntity input) {
				return input.getPermissionCode();
			}
		});
	}
	
	@Override
	public List<String> findGroupsByPermissionCodeCompany(String code, int companyID) {
		List<String> groupIDs = findGroupsByPermissionCode(code);
		return filterGroupIDs(groupIDs, companyID);
	}
	
	@Override
	public List<String> findGroupsByPermissionCode(String code) {
		PermissionEntity permissionEntity = permissionDao.getPermissionByCode(code);
		if (permissionEntity == null) {
			return Collections.emptyList();
		}
		
		return permissionMembershipDao.findUserGroupIDsByPermissionIDType(permissionEntity.getPermissionID(), UserGroupTypeEnum.GROUP.getValue());
	}
	
	@Override
	public List<String> findUsersByPermissionCodeCompany(String code, int companyID) {
		List<String> userIDs = findUsersByPermissionCode(code);
		return filterUserIDs(userIDs, companyID);
	}
	
	@Override
	public List<String> findUsersByPermissionCode(String code) {
		PermissionEntity permissionEntity = permissionDao.getPermissionByCode(code);
		if (permissionEntity == null) {
			return Collections.emptyList();
		}
		
		return permissionMembershipDao.findUserGroupIDsByPermissionIDType(permissionEntity.getPermissionID(), UserGroupTypeEnum.USER.getValue());
	}
	
	private List<String> filterGroupIDs(List<String> groupIDs, int companyID) {
		List<String> resultList = new ArrayList<String>();
		for (String groupID : groupIDs) {
			GroupDetailEntity groupDetail = groupDetailDao.getGroupDetailByGroupID(groupID);
			if (groupDetail.getCompanyID() == companyID) {
				resultList.add(groupID);
			}
		}
		return resultList;
	}
	
	private List<String> filterUserIDs(List<String> userIDs, int companyID) {
		List<String> resultList = new ArrayList<String>();
		for (String userID : userIDs) {
			List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
			for (Group group : groups) {
				if (group.getType().startsWith(String.valueOf(companyID))) {
					resultList.add(userID);
					break;
				}
			}
		}
		return resultList;
	}
	
	private List<Integer> findGroupPermissionsByUserID(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		List<String> groupIDs = Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group group) {
				return group.getId();
			}
		});
		return permissionMembershipDao.findPermissionIDsByUserGroupIDsType(groupIDs, UserGroupTypeEnum.GROUP.getValue());
	}
	@Override
	public void deleteRight(String userId) {
		String sql = "update OA_PermissionMembership set IsDeleted=1 where UserGroupID='"+userId+"'";
		baseDao.excuteSql(sql);
	}

	@Override
	public Map<String, String> findUserPermissionsByUserID(String userID) {
		return permissionMembershipDao.
				findPermissionIDAndCodeMapByUserGroupIDType(userID, UserGroupTypeEnum.USER.getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PermissionEntity> findPermissionListByUserID(String resignationUserID) {
		String hql = "SELECT\n" +
				"	p\n" +
				"FROM\n" +
				"	PermissionEntity p,\n" +
				"	PermissionMembershipEntity ship\n" +
				"WHERE\n" +
				"	p.permissionID = ship.permissionID\n" +
				"AND ship.userGroupID = '"+resignationUserID+"'\n" +
				"AND ship.isDeleted = 0\n" +
				"AND p.isDeleted = 0\n" +
				"AND p.process = 1";
		return (List<PermissionEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public PermissionMembershipEntity findPermissionmembership(Integer permissionID) {
		String hql = "from PermissionMembershipEntity where permissionID = "+permissionID;
		return (PermissionMembershipEntity) baseDao.hqlfindUniqueResult(hql);
	}
}
