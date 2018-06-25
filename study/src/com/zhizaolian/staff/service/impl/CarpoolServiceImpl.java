package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.CarpoolDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CarpoolEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.service.CarpoolService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CarpoolVo;
import com.zhizaolian.staff.vo.GroupDetailVO;


public class CarpoolServiceImpl implements CarpoolService {
	@Autowired
	private CarpoolDao carpoolDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private StaffService staffService;
	@Override
	public  ListResult<CarpoolVo> getCarpoolList(Map<String,String> params,int page,int limit) {
		List<CarpoolEntity> carpoolEntities=carpoolDao.getCarpoolList(params,page,limit);
		if(CollectionUtils.isNotEmpty(carpoolEntities)){
			Map<String,String> cacheMap= new HashMap<String,String>();
			List<CarpoolVo> carpoolVos=new ArrayList<CarpoolVo>();
			for(CarpoolEntity carpoolEntity:carpoolEntities){
				CarpoolVo carpoolVo=fillCarpoolEntity(carpoolEntity,cacheMap);
				carpoolVos.add(carpoolVo);
			}
			return new ListResult<CarpoolVo>(carpoolVos, carpoolDao.getCarpoolCount(params));
		}
		return new ListResult<CarpoolVo>(new ArrayList<CarpoolVo>(),0);
	}
	
	
	private CarpoolVo fillCarpoolEntity(CarpoolEntity carpoolEntity,Map<String, String> cacheMap){
		String personDetail=carpoolEntity.getPersonDetail();
		if(StringUtils.isBlank(personDetail))
			throw new RuntimeException("数据有误,人员详情不能为空!");
		String[] personIds=personDetail.split(",");
		Object[] personDetails=new Object[personIds.length];
		int i=0;
		for (String personId : personIds) {
			String name=cacheMap.get(personId);
			if (name==null) {
				name=staffDao.getEmployeeNameByUsrId(personId);
				cacheMap.put(personId, name);
			}
			if(StringUtils.isNotBlank(name))
			personDetails[i++]=new String[]{personId,name};
		}
		CarpoolVo carpoolVo=null;
		try {
			carpoolVo = (CarpoolVo) CopyUtil.DeclaredFieldCopy(CarpoolVo.class, carpoolEntity, 2,null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		carpoolVo.setPersonDetail_(personDetails);
		DepartmentEntity departmentEntity=departmentDao.getDepartmentByDepartmentID(carpoolVo.getDept_Id());
		carpoolVo.setDept_Name(departmentEntity==null?"":departmentEntity.getDepartmentName());;
		return carpoolVo;
	}
	@Override
	public void saveCarpool(CarpoolEntity carpoolEntity) {
		carpoolDao.save(carpoolEntity);
	}
	@Override
	public void delete(String id) {
		carpoolDao.delete(id);
	}


	@SuppressWarnings("serial")
	@Override
	public CarpoolVo getCarpoolById(String id) {
		final String id_=id;
		List<CarpoolEntity> carpoolEntities=carpoolDao.getCarpoolList(new HashMap<String,String>(){{this.put("id", id_);}}, 1, 1);
		if(CollectionUtils.isEmpty(carpoolEntities)){
			return null;
		}else{
			CarpoolEntity carpoolEntity=carpoolEntities.get(0);
			//初始化数据 获取
			CarpoolVo carpoolVo=null;
			try {
				carpoolVo = (CarpoolVo) CopyUtil.DeclaredFieldCopy(CarpoolVo.class, carpoolEntity, 2,null);
				//为每条数据塞入 姓名 公司 部门 职务 是否是司机字段
				String personDetails=carpoolVo.getPersonDetail();
				String[] peoples=personDetails.split(",");
				Object[] resultAll=new Object[peoples.length];
				int i=0;
				for(String peopleId :peoples){
					Object[] resultData=new Object[5];
					StaffEntity staffEntity=staffDao.getStaffByUserID(peopleId);
					if(staffEntity==null)continue;
					StaffStatusEnum staffStatus = StaffStatusEnum.valueOf(staffEntity.getStatus());
					if (staffStatus==null || staffStatus==StaffStatusEnum.LEAVE ) {
						continue;
					}
					List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(staffEntity.getUserID());
					if (groups.size() <= 0) {
						continue;
					}
					resultData[0]=peopleId;
					resultData[1]=staffEntity.getStaffName();
					resultData[2]=groups.get(0).getCompanyName();
					resultData[3]=groups.get(0).getDepartmentName();
					resultData[4]=groups.get(0).getPositionName();
					resultAll[i++]=resultData;
				}
				carpoolVo.setPersonDetail_(resultAll);;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return carpoolVo;
		}
	}


	@Override
	public void updateCarpool(CarpoolEntity carpoolEntity) {
		carpoolDao.updateCarpool(carpoolEntity);
		
	}

}
