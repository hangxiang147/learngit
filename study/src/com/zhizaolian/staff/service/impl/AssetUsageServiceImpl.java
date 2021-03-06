package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AssetDao;
import com.zhizaolian.staff.dao.AssetUsageDao;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.entity.AssetUsageEntity;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.AssetService;
import com.zhizaolian.staff.service.AssetUsageService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.AssetUsageVOTransFormer;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetUsageVO;
import com.zhizaolian.staff.vo.AssetVO;

public class AssetUsageServiceImpl implements AssetUsageService {
	@Autowired
	private AssetUsageDao assetUsageDao;
	@Autowired
	private AssetDao assetDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private AssetService assetService;
	@Override
	public void SaveAssetUsage(AssetUsageVO assetUsageVO) {
		for(int i=0;i<assetUsageVO.getAssetIDs().length;i++){
			AssetUsageEntity assetUsageEntity=new AssetUsageEntity();
			Date date=new Date();
			assetUsageEntity.setUsageID(assetUsageVO.getUsageID());
			assetUsageEntity.setRecipientID(assetUsageVO.getRecipientID());
			assetUsageEntity.setCompanyID(assetUsageVO.getCompanyID());
			assetUsageEntity.setDepartmentID(assetUsageVO.getDepartmentID());
			assetUsageEntity.setReceiveLocation(assetUsageVO.getReceiveLocation());
			assetUsageEntity.setReceiveTime(DateUtil.getSimpleDate(assetUsageVO.getReceiveTime()));
			assetUsageEntity.setReceiveOperatorID(assetUsageVO.getReceiveOperatorID());
			assetUsageEntity.setReceiveNote(assetUsageVO.getReceiveNote());
			assetUsageEntity.setReturnLocation(assetUsageVO.getReturnLocation());
			assetUsageEntity.setReturnTime(DateUtil.getSimpleDate(assetUsageVO.getReturnTime()));
			assetUsageEntity.setReturnOperatorID(assetUsageVO.getReturnOperatorID());
			assetUsageEntity.setReturnNote(assetUsageVO.getReturnNote());
			assetUsageEntity.setStatus(0);
			assetUsageEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			assetUsageEntity.setAddTime(date);
			assetUsageEntity.setUpdateTime(date);
			assetUsageEntity.setAssetID(assetUsageVO.getAssetIDs()[i]);
		    assetUsageDao.saveAssetUsage(assetUsageEntity);
		    assetDao.updateAsset(1,assetUsageVO.getAssetIDs()[i], assetUsageVO.getReceiveLocation());
		    }
			}

	@Override
	public void updateAssetUsage(AssetUsageVO assetUsageVO) {
		for(int i=0;i<assetUsageVO.getUsageIDs().length;i++){
			AssetUsageEntity assetUsageEntity=assetUsageDao.getAssetUsageByID(assetUsageVO.getUsageIDs()[i]);
			assetUsageEntity.setReturnLocation(assetUsageVO.getReturnLocation());
			assetUsageEntity.setReturnNote(assetUsageVO.getReturnNote());
			assetUsageEntity.setReturnOperatorID(assetUsageVO.getReturnOperatorID());
			assetUsageEntity.setReturnTime(DateUtil.getSimpleDate(assetUsageVO.getReturnTime()));                         
			assetUsageEntity.setStatus(1);
			assetUsageDao.saveAssetUsage(assetUsageEntity);
			assetDao.updateAsset(0,assetUsageEntity.getAssetID(),assetUsageVO.getReturnLocation());
		}
	}

	@Override
	public ListResult<AssetUsageVO> findAssetUsageList(AssetUsageVO assetUsageVO, int limit, int page) {
		ListResult<Object> assetUsageEntities=assetUsageDao.findAssetUsageList(getQuerySqlByAssetUsageVO(assetUsageVO), getQueryCountSqlByAssetUsageVO(assetUsageVO), limit, page);
		List<AssetUsageVO> list=new ArrayList<AssetUsageVO>();
		for(Object obj:assetUsageEntities.getList()){
			AssetUsageVO assetUsageVO1=new AssetUsageVO();
			Object[] objs = (Object[]) obj;
			assetUsageVO1.setUsageID((Integer)objs[0]);
			assetUsageVO1.setAssetID((Integer)objs[1]);
			assetUsageVO1.setRecipientID((String)objs[2]);
			assetUsageVO1.setCompanyID((Integer)objs[3]);
			assetUsageVO1.setDepartmentID((Integer)objs[4]);
			assetUsageVO1.setReceiveLocation((String)objs[5]);
			if(objs[6]!=null){
				assetUsageVO1.setReceiveTime(DateUtil.formateDate((Date)objs[6]));
			}
			assetUsageVO1.setReceiveOperatorID((String)objs[7]);
			assetUsageVO1.setReceiveNote((String)objs[8]);
			assetUsageVO1.setReturnLocation((String)objs[9]);
			if(objs[10]!=null){
			  assetUsageVO1.setReturnTime(DateUtil.formateDate((Date)objs[10]));
			}
			assetUsageVO1.setReturnOperatorID((String)objs[11]);
			assetUsageVO1.setReturnNote((String)objs[12]);
			assetUsageVO1.setStatus(Integer.parseInt(objs[13].toString()));
			if(assetUsageVO1.getRecipientID()!=null){
				assetUsageVO1.setRecipientName(staffService.getStaffByUserID(assetUsageVO1.getRecipientID()).getLastName());
			}
			if(assetUsageVO1.getCompanyID()!=null){
				assetUsageVO1.setCompanyName(companyDao.getCompanyByCompanyID(assetUsageVO1.getCompanyID()).getCompanyName());
			}
			if(assetUsageVO1.getDepartmentID()!=null){
				assetUsageVO1.setDepartmentName(departmentDao.getDepartmentByDepartmentID_(assetUsageVO1.getDepartmentID()).getDepartmentName());
			}
			if(assetUsageVO1.getReceiveOperatorID()!=null){
				assetUsageVO1.setReceiveOperatorName(staffService.getStaffByUserID(assetUsageVO1.getReceiveOperatorID()).getLastName());
			}
			if(assetUsageVO1.getReturnOperatorID()!=null){
				assetUsageVO1.setReturnOperatorName(staffService.getStaffByUserID(assetUsageVO1.getReturnOperatorID()).getLastName());
			}
			if(assetUsageVO1.getAssetID()!=null){
				assetUsageVO1.setAssetName(assetDao.getAssetByID(assetUsageVO1.getAssetID()).getAssetName());
				
			}
			AssetVO assetVO = assetService.getAssetByID((Integer)objs[1]);
			assetUsageVO1.setAssetVO(assetVO);
			list.add(assetUsageVO1);
		}
		
		return new ListResult<AssetUsageVO>(list,assetUsageEntities.getTotalCount());
		}
	


	private String getQuerySqlByAssetUsageVO(AssetUsageVO assetUsageVO){
		StringBuffer hql=new StringBuffer("select assetUsage.* from OA_AssetUsage assetUsage,OA_Asset asset,OA_Staff staff where "
				+ "assetUsage.assetID=asset.assetID and assetUsage.recipientID=staff.userID and assetUsage.isDeleted = 0 ");
		hql.append(getWhereByAssetUsageVO(assetUsageVO));
		hql.append(" order by assetUsage.status,assetUsage.addTime desc");		return hql.toString();
		
	}
	
	private String getQueryCountSqlByAssetUsageVO(AssetUsageVO assetUsageVO){
		StringBuffer hql=new StringBuffer("select count(*) from OA_AssetUsage assetUsage,OA_Asset asset,OA_Staff staff where "
				+ "assetUsage.assetID=asset.assetID and assetUsage.recipientID=staff.userID and assetUsage.isDeleted = 0  ");
		hql.append(getWhereByAssetUsageVO(assetUsageVO));
		return hql.toString();
		
	}

		
	
	
	private String getWhereByAssetUsageVO(AssetUsageVO assetUsageVO){
		StringBuffer whereHql=new StringBuffer("");
		if(!StringUtils.isBlank(assetUsageVO.getRecipientName())){
			whereHql.append(" and staff.staffName like '%"+assetUsageVO.getRecipientName()+"%'");
			
		}
		if(assetUsageVO.getStatus()!=null){
			whereHql.append(" and assetUsage.status='"+assetUsageVO.getStatus()+"'");
			
		}
		if(assetUsageVO.getAssetVO()!=null){
			if(!StringUtils.isBlank(assetUsageVO.getAssetVO().getAssetName())){
				whereHql.append(" and asset.assetName like'%"+assetUsageVO.getAssetVO().getAssetName()+"%'");
				
			}
			if(!StringUtils.isBlank(assetUsageVO.getAssetVO().getSerialNumber())){
				whereHql.append(" and asset.serialNumber like '%"+assetUsageVO.getAssetVO().getSerialNumber()+"%'");
				
			}
		
			if(assetUsageVO.getAssetVO().getType()!=null){
				whereHql.append(" and asset.type='"+assetUsageVO.getAssetVO().getType()+"'");
				
			}
		}
		return whereHql.toString();
		
	}

	@Override
	public AssetUsageVO getAssetUsageByUsageID(Integer usageID) {
		AssetUsageEntity assetUsageEntity = assetUsageDao.getAssetUsageByID(usageID);
		AssetUsageVO assetUsageVO = AssetUsageVOTransFormer.entityToVO(assetUsageEntity);
		assetUsageVO.setRecipientName(staffService.getStaffByUserID(assetUsageEntity.getRecipientID()).getLastName());
		return assetUsageVO;
	}
	
	@Override
	public List<AssetUsageVO> getAssstUsageByAssetID(Integer assetID) {
		List<AssetUsageEntity> assetUsageEntities=assetUsageDao.getAssetUsageByAssetID(assetID);
		List<AssetUsageVO> list=new ArrayList<>();
		for(AssetUsageEntity assetUsageEntity:assetUsageEntities){
			AssetUsageVO assetUsageVO=new AssetUsageVO();
			assetUsageVO=AssetUsageVOTransFormer.entityToVO(assetUsageEntity);
			if(assetUsageVO.getRecipientID()!=null){
				assetUsageVO.setRecipientName(staffService.getStaffByUserID(assetUsageVO.getRecipientID()).getLastName());
			}
			if(assetUsageVO.getCompanyID()!=null){
				assetUsageVO.setCompanyName(companyDao.getCompanyByCompanyID(assetUsageVO.getCompanyID()).getCompanyName());
			}
			if(assetUsageVO.getDepartmentID()!=null){
				assetUsageVO.setDepartmentName(departmentDao.getDepartmentByDepartmentID_(assetUsageVO.getDepartmentID()).getDepartmentName());
			}
			if(assetUsageVO.getReceiveOperatorID()!=null){
				assetUsageVO.setReceiveOperatorName(staffService.getStaffByUserID(assetUsageVO.getReceiveOperatorID()).getLastName());
			}
			if(assetUsageVO.getReturnOperatorID()!=null){
				assetUsageVO.setReturnOperatorName(staffService.getStaffByUserID(assetUsageVO.getReturnOperatorID()).getLastName());
			}
			if(assetUsageVO.getAssetID()!=null){
				assetUsageVO.setAssetName(assetDao.getAssetByID(assetUsageVO.getAssetID()).getAssetName());
			}
			list.add(assetUsageVO);
		}
		return list;
	}

	@Override
	public AssetUsageVO getAssstUsageByAssetID1(Integer assetID) {
		AssetUsageEntity assetUsageEntity=assetUsageDao.getAssetUsageByID1(assetID);
		AssetUsageVO assetUsageVO=new AssetUsageVO();
		if(assetUsageEntity!=null){
			assetUsageVO=AssetUsageVOTransFormer.entityToVO(assetUsageEntity);
			if(assetUsageVO.getRecipientID()!=null){
				assetUsageVO.setRecipientName(staffService.getStaffByUserID(assetUsageVO.getRecipientID()).getLastName());
			}
			if(assetUsageVO.getCompanyID()!=null){
				CompanyEntity company = companyDao.getCompanyByCompanyID(assetUsageVO.getCompanyID());
				if(null != company){
					assetUsageVO.setCompanyName(company.getCompanyName());
				}
			}
			if(assetUsageVO.getDepartmentID()!=null){
				DepartmentEntity dep = departmentDao.getDepartmentByDepartmentID(assetUsageVO.getDepartmentID());
				if(null != dep){
					assetUsageVO.setDepartmentName(dep.getDepartmentName());
				}
			}
			if(assetUsageVO.getReceiveOperatorID()!=null){
				assetUsageVO.setReceiveOperatorName(staffService.getStaffByUserID(assetUsageVO.getReceiveOperatorID()).getLastName());
			}
			if(assetUsageVO.getReturnOperatorID()!=null){
				assetUsageVO.setReturnOperatorName(staffService.getStaffByUserID(assetUsageVO.getReturnOperatorID()).getLastName());
			}
			if(assetUsageVO.getAssetID()!=null){
				assetUsageVO.setAssetName(assetDao.getAssetByID(assetUsageVO.getAssetID()).getAssetName());
			}
		}
	
		return assetUsageVO;
	}

	@Override
	public void saveAssetUsage(AssetUsageEntity assetUsage) {
		assetUsage.setAddTime(new Date());
		assetUsage.setIsDeleted(0);
		assetUsage.setStatus(0);
		baseDao.hqlSave(assetUsage);
	}
}
