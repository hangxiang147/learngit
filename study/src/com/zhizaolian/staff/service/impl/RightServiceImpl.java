package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.RightDao;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.service.RightService;
import com.zhizaolian.staff.utils.ListResult;

public class RightServiceImpl implements RightService {
	@Autowired
	private RightDao rightDao;
	@Autowired
	private BaseDao baseDao;
	@Override
	public List<Object[]> getAllRight() {
		return rightDao.getAllRight();
	}

	@Override
	public void insertRight(String rightName, String code) {
		rightDao.insertRight(rightName, code);
	}

	@Override
	public ListResult<Object[]> getRightMemberShip(String userId, int page,
			int limit) {
		return (ListResult<Object[]>) new ListResult<>(
				rightDao.getRightMemberShip(userId, page, limit),
				rightDao.getRightMemberShipCount(userId));
	}

	@Override
	public ListResult<Object[]> getGroupRightMemberShip(String groupId,
			int page, int limit) {
		return (ListResult<Object[]>) new ListResult<>(
				rightDao.getGroupRightMemberShip(groupId, page, limit),
				rightDao.getGroupRightMemberShip(groupId));
	}

	@Override
	public void createRightMemberShip(String keyId, String type,
			String rightId) {
		rightDao.createRightMemberShip(keyId, type, rightId);
	}

	@Override
	public void breakMemberShip(int id) {
		rightDao.breakMemberShip(id);
	}

	@Override
	public String getGroupIdByKeys(String companyId, String departMentId,
			String positionId) {
		return rightDao.getGroupIdByKeys(companyId, departMentId, positionId);
	}

	@Override
	public void saveRight(PermissionEntity permission) {
		permission.setIsDeleted(0);
		permission.setAddTime(new Date());
		baseDao.hqlSave(permission);
	}
}
