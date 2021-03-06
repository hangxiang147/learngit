package com.zhizaolian.staff.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.AssetDao;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.AssetService;
import com.zhizaolian.staff.service.AssetUsageService;
import com.zhizaolian.staff.transformer.AssetVOTransFormer;
import com.zhizaolian.staff.utils.DateUtil;import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetUsageVO;
import com.zhizaolian.staff.vo.AssetVO;



public class AssetServiceImpl implements AssetService {
	@Autowired	
	private AssetDao assetDao;
	@Autowired
	private AssetUsageService assetUsageService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private CompanyDao companyDao;
	@Override
	public Integer addAsset(AssetVO assetVO) {
		AssetEntity assetEntity=new AssetEntity();
		Date date=new Date();
		
		assetEntity.setAssetName(assetVO.getAssetName());
		assetEntity.setSerialNumber(assetVO.getSerialNumber());
		assetEntity.setType(assetVO.getType());
		assetEntity.setModel(assetVO.getModel());
		assetEntity.setAmount(assetVO.getAmount());
		assetEntity.setCompanyID(assetVO.getCompanyID());
		assetEntity.setPurchaseTime(DateUtil.getSimpleDate(assetVO.getPurchaseTime()));
		assetEntity.setStorageLocation(assetVO.getStorageLocation());
		assetEntity.setStatus(assetVO.getStatus());
		assetEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		assetEntity.setAddTime(date);
		assetEntity.setUpdateTime(date);
		assetEntity.setDeviceID(assetVO.getDeviceID());
		assetDao.save(assetEntity);
		return assetEntity.getAssetID();
		
	}

	@Override
	public ListResult<AssetVO> findAssetList(AssetVO assetVO1, int limit, int page) {
		ListResult<AssetEntity> assetList=assetDao.findAssetList(getQuerySqlByAssetVO(assetVO1),getQueryCountSqlByAssetVO(assetVO1), page, limit);
		List<AssetVO> list=new ArrayList<AssetVO>();
		for(AssetEntity assetEntity:assetList.getList()){ 
			AssetVO assetVO=AssetVOTransFormer.entityToVO(assetEntity);
			AssetUsageVO assetUsageVO=assetUsageService.getAssstUsageByAssetID1(assetVO.getAssetID());
			assetVO.setAssetUsageVO(assetUsageVO);
			
			list.add(assetVO);
			
		}
		return  new ListResult<AssetVO>(list,assetList.getTotalCount());
	}
	
	
	private String getQuerySqlByAssetVO(AssetVO assetVO){
		StringBuffer hql=new StringBuffer("FROM AssetEntity asset WHERE asset.isDeleted = 0");
		hql.append(getWhereByAssetVO(assetVO));
		hql.append(" order by asset.addTime desc");
		return hql.toString();
	}
	
	private String getWhereByAssetVO(AssetVO assetVO){
		StringBuffer whereSql=new StringBuffer("");
		if(assetVO.getCompanyID()!=null || null !=assetVO.getCompanyID()){
			whereSql.append(" and asset.companyID='"+assetVO.getCompanyID()+"'");
			
		}
		if(StringUtils.isNotBlank(assetVO.getAssetName())){
			whereSql.append(" and asset.assetName like'%"+assetVO.getAssetName()+"%'");
			
		}
		if(StringUtils.isNotBlank(assetVO.getSerialNumber())){
			whereSql.append(" and asset.serialNumber like '%"+assetVO.getSerialNumber()+"%'");
			
		}
		if(assetVO.getStatus()!=null || null!=assetVO.getStatus()){
			whereSql.append(" and asset.status='"+assetVO.getStatus()+"'");
			
		}
		if(assetVO.getType()!=null || null!= assetVO.getType()){
			whereSql.append(" and asset.type='"+assetVO.getType()+"'");
			
		}
		if(StringUtils.isNotBlank(assetVO.getDeviceID())){
			whereSql.append(" and asset.deviceID like '%"+assetVO.getDeviceID()+"%'");
		}
		return whereSql.toString();
		
	}

	
	private String getQueryCountSqlByAssetVO(AssetVO assetVO){
		StringBuffer hql=new StringBuffer("SELECT COUNT(*)FROM AssetEntity asset WHERE asset.isDeleted = 0");
		hql.append(getWhereByAssetVO(assetVO));
		return hql.toString();
		
		
	}
	@Override
	public AssetVO getAssetByID(Integer assetID) {
		AssetEntity assetEntity=assetDao.getAssetByID(assetID);
		AssetVO assetVO=AssetVOTransFormer.entityToVO(assetEntity);
		return assetVO;
	}

	@Override
	public void updateAssetCompany(String assetId, String companyName) {
		CompanyEntity company = companyDao.getCompanyByCompanyName(companyName);
		if(null != company){
			String sql = "update OA_Asset set CompanyID="+company.getCompanyID()+"  where AssetID="+assetId;
			baseDao.excuteSql(sql);
		}
	}

	@Override
	public List<Object> getStaffAssets(String userId) throws Exception{
		String sql = "SELECT\n" +
					"	asset.AssetName,\n" +
					"	asset.SerialNumber,\n" +
					"	asset.Model,\n" +
					"	asset.StorageLocation\n" +
					"FROM\n" +
					"	OA_AssetUsage usr\n" +
					"LEFT JOIN OA_Asset asset ON usr.AssetID = asset.AssetID\n" +
					"WHERE\n" +
					"	usr.IsDeleted = 0\n" +
					"AND asset.IsDeleted = 0\n" +
					"AND usr.`Status` = 0\n" +
					"and usr.RecipientID = '"+userId+"'";
		return baseDao.findBySql(sql);
	}

	@Override
	public void updateAssetUsageStatus(String usageID) {
		String sql = "update OA_AssetUsage set Status=1 where UsageID="+usageID;
		baseDao.excuteSql(sql);
	}

	@Override
	public XSSFWorkbook exportAssetVO(AssetVO assetVO) {
		List<AssetVO> assetVOs = prepareExportAssetVO(assetVO);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("资产信息列表");
		XSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格
		row.createCell((short) 0).setCellValue("所属公司");
		row.createCell((short) 1).setCellValue("资产类别");
		row.createCell((short) 2).setCellValue("资产名称");
		row.createCell((short) 3).setCellValue("规格型号");
		row.createCell((short) 4).setCellValue("资产条码");
		row.createCell((short) 5).setCellValue("设备序列号");
		row.createCell((short) 6).setCellValue("金额");
		row.createCell((short) 7).setCellValue("购入时间");
		row.createCell((short) 8).setCellValue("状态");
		row.createCell((short) 9).setCellValue("使用公司");
		row.createCell((short) 10).setCellValue("使用部门");
		row.createCell((short) 11).setCellValue("使用人员");
		row.createCell((short) 12).setCellValue("存放地点");

		for (int i = 0, j = sheet.getLastRowNum() + 1, length = assetVOs.size(); i < length; ++i, ++j) {
			XSSFRow row_data = sheet.createRow(j);
			AssetVO asset = assetVOs.get(i);
			// 第四步，创建单元格，并设置值
			String companyName;
			if(asset.getCompanyID()!=null){
				switch(asset.getCompanyID()){
				case 1:
					companyName = "智造链骑岸总部";
					break;
				case 2:
					companyName = "智造链如东迷丝茉分部";
					break;
				case 3:
					companyName = "智造链南通分部";
					break;
				case 4:
					companyName = "智造链广州亦酷亦雅分部";
					break;
				case 5:
					companyName = "智造链南京分部";
					break;
				case 6:
					companyName = "智造链佛山迷丝茉分部";
					break;
				default:
					companyName = "未知";
				}
				row_data.createCell((short) 0).setCellValue(companyName);
			}
			
			String type;
			if(asset.getType()!=null){
				switch(asset.getType()){
				case 1:
					type="行政办公设备";
					break;
				case 2:
					type="电子产品";
					break;
				case 3:
					type="通信设备";
					break;
				case 4:
					type="交通工具";
					break;
				default:
					type="未知";
				}
				row_data.createCell((short) 1).setCellValue(type);
			}
			
			if(asset.getAssetName()!=null){
				row_data.createCell((short) 2).setCellValue(asset.getAssetName());
			}
			
			if(asset.getModel()!=null){
				row_data.createCell((short) 3).setCellValue(asset.getModel());
			}
			
			if(asset.getSerialNumber()!=null){
				row_data.createCell((short) 4).setCellValue(asset.getSerialNumber());
			}
			
			if(asset.getDeviceID()!=null){
				row_data.createCell((short) 5).setCellValue(asset.getDeviceID());
			}
			
			if(asset.getAmount()!=null){
				row_data.createCell((short) 6).setCellValue(asset.getAmount());
			}
			if(asset.getPurchaseTime()!=null){
				row_data.createCell((short) 7).setCellValue(asset.getPurchaseTime());
			}
			
			String status;
			if(asset.getStatus()!=null){
				switch(asset.getStatus()){
				case 0:
					status = "闲置";
					break;
				case 1:
					status = "在用";
					break;
				default:
					status = "未知";
				}
				row_data.createCell((short) 8).setCellValue(status);
			}
			
			if(asset.getAssetUsageVO().getCompanyName()!=null){
				row_data.createCell((short) 9).setCellValue(asset.getAssetUsageVO().getCompanyName());
			}
			if(asset.getAssetUsageVO().getDepartmentName()!=null){
				row_data.createCell((short) 10).setCellValue(asset.getAssetUsageVO().getDepartmentName());			
			}
			if(asset.getAssetUsageVO().getRecipientName()!=null){
				row_data.createCell((short) 11).setCellValue(asset.getAssetUsageVO().getRecipientName());
			}
			if(asset.getStorageLocation()!=null){
				row_data.createCell((short) 12).setCellValue(asset.getStorageLocation());
			}
		}
		return wb;
	}
	
	@SuppressWarnings("unchecked")
	private List<AssetVO> prepareExportAssetVO(AssetVO assetVO){
		String hql = getQuerySqlByAssetVO(assetVO);
		List<AssetEntity> assetEntitys = (List<AssetEntity>) baseDao.hqlfind(hql);
		List<AssetVO> list=new ArrayList<AssetVO>();
		for(AssetEntity assetEntity:assetEntitys){ 
			AssetVO assetVo=AssetVOTransFormer.entityToVO(assetEntity);
			AssetUsageVO assetUsageVO=assetUsageService.getAssstUsageByAssetID1(assetVo.getAssetID());
			assetVo.setAssetUsageVO(assetUsageVO);
			list.add(assetVo);
		}
		return list;
	}

	@Override
	public void updateAssetEntity(AssetEntity assetEntity) {
		AssetEntity asset = assetDao.getAssetByID(assetEntity.getAssetID());
		asset.setAssetName(assetEntity.getAssetName());
		asset.setSerialNumber(assetEntity.getSerialNumber());
		asset.setType(assetEntity.getType());
		asset.setModel(assetEntity.getModel());
		asset.setAmount(assetEntity.getAmount());
		asset.setCompanyID(assetEntity.getCompanyID());
		asset.setPurchaseTime(assetEntity.getPurchaseTime());
		asset.setStorageLocation(assetEntity.getStorageLocation());
		asset.setStatus(assetEntity.getStatus());
		asset.setDeviceID(assetEntity.getDeviceID());
		assetDao.updateAssetEntity(asset);
	}
}
