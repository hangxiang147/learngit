package com.zhizaolian.staff.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.VehicleInfoEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.VehicleService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.vo.InsuranceRecord;
import com.zhizaolian.staff.vo.MaintainRecord;
import com.zhizaolian.staff.vo.RepairRecordVo;
import com.zhizaolian.staff.vo.VehicleUseRecord;
import com.zhizaolian.staff.vo.YearlyInspectionVo;

public class VehicleServiceImpl implements VehicleService {
	@Autowired
	private BaseDao baseDao;
	@Override
	public void saveVehicle(VehicleInfoEntity vehicleVo) throws Exception {
		vehicleVo.setIsDeleted(0);
		if(null != vehicleVo.getId()){
			baseDao.hqlUpdate(vehicleVo);
		}else{
			vehicleVo.setInspectionHandle(1);
			vehicleVo.setInsuranceHandle(1);
			vehicleVo.setInspectionSendMsg(0);
			vehicleVo.setInsuranceSendMsg(0);
			vehicleVo.setAddTime(new Date());
			baseDao.hqlSave(vehicleVo);
		}
	}
	@Override
	public ListResult<VehicleInfoEntity> findVehicleList(String licenseNumber, Integer limit, Integer page) throws Exception {
		String hql = "from VehicleInfoEntity where isDeleted=0 ";
		if(StringUtils.isNotBlank(licenseNumber)){
			hql += " and licenseNumber like '%"+EscapeUtil.decodeSpecialChars(licenseNumber)+"%'";
		}
		hql += " order by addTime";
		@SuppressWarnings("unchecked")
		List<VehicleInfoEntity> vehicleInfos = (List<VehicleInfoEntity>) baseDao.hqlPagedFind(hql, page, limit);
		String sqlCount = "select count(*) from OA_VehicleInfo where isDeleted=0";
		if(StringUtils.isNotBlank(licenseNumber)){
			sqlCount += " and licenseNumber like '%"+EscapeUtil.decodeSpecialChars(licenseNumber)+"%'";
		}
		int totalCount = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(vehicleInfos, totalCount);
	}
	@Override
	public VehicleInfoEntity getVehicleDetails(String id) throws Exception {
		String hql = "from VehicleInfoEntity where id="+id;
		VehicleInfoEntity  vehicleInfo = (VehicleInfoEntity) baseDao.hqlfindUniqueResult(hql);
		if(null != vehicleInfo){
			byte[] insuranceRecord = vehicleInfo.getInsuranceRecord();
			if(null != insuranceRecord){
				InsuranceRecord insuranceRecordVo = (InsuranceRecord) ObjectByteArrTransformer.toObject(insuranceRecord);
				vehicleInfo.setInsuranceRecordVo(insuranceRecordVo);
			}
			byte[] yearlyInspection = vehicleInfo.getYearlyInspectionRecord();
			if(null != yearlyInspection){
				YearlyInspectionVo yearlyInspectionVo = (YearlyInspectionVo) ObjectByteArrTransformer.toObject(yearlyInspection);
				vehicleInfo.setYearlyInspectionVo(yearlyInspectionVo);
			}
			byte[] maintainRecord = vehicleInfo.getMaintainRecord();
			if(null != maintainRecord){
				MaintainRecord maintainRecordVo = (MaintainRecord) ObjectByteArrTransformer.toObject(maintainRecord);
				vehicleInfo.setMaintainRecordVo(maintainRecordVo);
			}
			byte[] repairRecord = vehicleInfo.getRepairRecord();
			if(null != repairRecord){
				RepairRecordVo repairRecordVo = (RepairRecordVo) ObjectByteArrTransformer.toObject(repairRecord);
				vehicleInfo.setRepairRecordVo(repairRecordVo);
			}
			byte[] vehicleUseRecord = vehicleInfo.getVehicleUseRecord();
			if(null != vehicleUseRecord){
				VehicleUseRecord vehicleUseRecordVo = (VehicleUseRecord) ObjectByteArrTransformer.toObject(vehicleUseRecord);
				vehicleInfo.setVehicleUseRecordVo(vehicleUseRecordVo);
			}
		}
		return vehicleInfo;
	}
	@Override
	public void deleteVehicle(String id) {
		String sql = "update OA_VehicleInfo set isDeleted=1 where id="+id;
		baseDao.excuteSql(sql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleInfoEntity> findAllVehicleList() throws Exception {
		String hql = "from VehicleInfoEntity where isDeleted=0";
		List<VehicleInfoEntity> vehicles = (List<VehicleInfoEntity>) baseDao.hqlfind(hql);
		for(VehicleInfoEntity vehicleInfo: vehicles){
			byte[] insuranceRecord = vehicleInfo.getInsuranceRecord();
			if(null != insuranceRecord){
				InsuranceRecord insuranceRecordVo = (InsuranceRecord) ObjectByteArrTransformer.toObject(insuranceRecord);
				vehicleInfo.setInsuranceRecordVo(insuranceRecordVo);
			}
			byte[] yearlyInspection = vehicleInfo.getYearlyInspectionRecord();
			if(null != yearlyInspection){
				YearlyInspectionVo yearlyInspectionVo = (YearlyInspectionVo) ObjectByteArrTransformer.toObject(yearlyInspection);
				vehicleInfo.setYearlyInspectionVo(yearlyInspectionVo);
			}
		}
		return vehicles;
	}
	@Override
	public int getSoonOverDueVehicleCount() {
		String sql = "select count(*) from OA_VehicleInfo where isDeleted=0 and inspectionHandle=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		sql = "select count(*) from OA_VehicleInfo where isDeleted=0 and insuranceHandle=0";
		count += Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		return count;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleInfoEntity> getSoonOverDueVehicles() throws Exception {
		String hql = "from VehicleInfoEntity where isDeleted=0 and inspectionHandle=0";
		List<VehicleInfoEntity> soonOverDueVehicles = new ArrayList<>();
		List<VehicleInfoEntity> vehicleInfos = (List<VehicleInfoEntity>) baseDao.hqlfind(hql);
		for(VehicleInfoEntity vehicleInfo: vehicleInfos){
			//考虑到hibernate的缓存机制，防止数据间发生串动
			VehicleInfoEntity inspectionVehicleInfo = vehicleInfo.clone();
			inspectionVehicleInfo.setType("年检");
			soonOverDueVehicles.add(inspectionVehicleInfo);
		}
		hql = "from VehicleInfoEntity where isDeleted=0 and insuranceHandle=0";
		List<VehicleInfoEntity> _vehicleInfos = (List<VehicleInfoEntity>) baseDao.hqlfind(hql);
		for(VehicleInfoEntity vehicleInfo: _vehicleInfos){
			vehicleInfo.setType("保险");
		}
		soonOverDueVehicles.addAll(_vehicleInfos);
		for(VehicleInfoEntity vehicleInfo: soonOverDueVehicles){
			byte[] insuranceRecord = vehicleInfo.getInsuranceRecord();
			if(null != insuranceRecord){
				InsuranceRecord insuranceRecordVo = (InsuranceRecord) ObjectByteArrTransformer.toObject(insuranceRecord);
				vehicleInfo.setInsuranceRecordVo(insuranceRecordVo);
			}
			byte[] yearlyInspection = vehicleInfo.getYearlyInspectionRecord();
			if(null != yearlyInspection){
				YearlyInspectionVo yearlyInspectionVo = (YearlyInspectionVo) ObjectByteArrTransformer.toObject(yearlyInspection);
				vehicleInfo.setYearlyInspectionVo(yearlyInspectionVo);
			}
		}
		return soonOverDueVehicles;
	}
	@Override
	public void completeHandle(String vehicleId, String handleType) throws ClassNotFoundException, IOException {
		String sql = "";
		if(Constants.INSURANCE.equals(handleType)){
			sql = "update OA_VehicleInfo set insuranceHandle=1 where id="+vehicleId;
		}else if(Constants.YEARLY_INSPECTION.equals(handleType)){
			sql = "update OA_VehicleInfo set inspectionHandle=1 where id="+vehicleId;
		}else{
			return;
		}
		baseDao.excuteSql(sql);
		//自动新增记录
		String hql = "from VehicleInfoEntity where id="+vehicleId;
		VehicleInfoEntity vehicle = (VehicleInfoEntity) baseDao.hqlfindUniqueResult(hql);
		if(Constants.INSURANCE.equals(handleType)){
			byte[] insuranceRecord = vehicle.getInsuranceRecord();
			if(null != insuranceRecord){
				InsuranceRecord insuranceRecordVo = (InsuranceRecord) ObjectByteArrTransformer.toObject(insuranceRecord);
				List<String> times = new ArrayList<>();
				times.addAll(Arrays.asList(insuranceRecordVo.getTime()));
				List<String> moneys = new ArrayList<>();
				moneys.addAll(Arrays.asList(insuranceRecordVo.getMoney()));
				List<String> nextInsuranceTimes = new ArrayList<>();
				nextInsuranceTimes.addAll(Arrays.asList(insuranceRecordVo.getNextInsuranceTime()));
				String leastInsuranceTime = getLatestTime(nextInsuranceTimes);
				Date leastInsuranceDate = DateUtil.getSimpleDate(leastInsuranceTime);
				Date currentDate = new Date();
				//判断是否已人工新增记录
				if((leastInsuranceDate.getTime()-currentDate.getTime())/(24*60*60*1000) < 30){
					Calendar cal = Calendar.getInstance();
					cal.setTime(leastInsuranceDate);
					cal.add(Calendar.YEAR, 1);
					String nextInsuranceTime = DateUtil.formateDate(cal.getTime());
					times.add(leastInsuranceTime);
					moneys.add(moneys.get(0));
					nextInsuranceTimes.add(nextInsuranceTime);
					insuranceRecordVo.setTime(times.toArray(new String[times.size()]));
					insuranceRecordVo.setMoney(moneys.toArray(new String[moneys.size()]));
					insuranceRecordVo.setNextInsuranceTime(nextInsuranceTimes.toArray(new String[nextInsuranceTimes.size()]));
					vehicle.setInsuranceRecord(ObjectByteArrTransformer.toByteArray(insuranceRecordVo));
					baseDao.hqlUpdate(vehicle);
				}
			}
			//由于年检时间间隔不确定，所以不自动新增记录
		}else if(Constants.YEARLY_INSPECTION.equals(handleType)){
/*			byte[] yearlyInspection = vehicle.getYearlyInspectionRecord();
			if(null != yearlyInspection){
				YearlyInspectionVo yearlyInspectionVo = (YearlyInspectionVo) ObjectByteArrTransformer.toObject(yearlyInspection);
				List<String> times = new ArrayList<>();
				times.addAll(Arrays.asList(yearlyInspectionVo.getTime()));
				List<String> nextYearlyInspectionTimes = new ArrayList<>();
				nextYearlyInspectionTimes.addAll(Arrays.asList
						(yearlyInspectionVo.getNextYearlyInspectionTime()));
				String leastYearlyInspectionTime = getLatestTime(nextYearlyInspectionTimes);
				Date leastYearlyInspectionDate = DateUtil.getSimpleDate(leastYearlyInspectionTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(leastYearlyInspectionDate);
				cal.add(Calendar.YEAR, 1);
				String nextYearlyInspectionTime = DateUtil.formateDate(cal.getTime());
				times.add(leastYearlyInspectionTime);
				nextYearlyInspectionTimes.add(nextYearlyInspectionTime);
				yearlyInspectionVo.setTime(times.toArray(new String[times.size()]));
				yearlyInspectionVo.setNextYearlyInspectionTime(nextYearlyInspectionTimes.toArray(
						new String[nextYearlyInspectionTimes.size()]));
				vehicle.setYearlyInspectionRecord(ObjectByteArrTransformer.toByteArray(yearlyInspectionVo));
				baseDao.hqlUpdate(vehicle);
			}*/
		}
	}
	private String getLatestTime(List<String> times){
		String latestTime = "";
		for(String time: times){
			if(StringUtils.isBlank(latestTime)){
				latestTime = time;
			}else{
				Date latestDate = DateUtil.getSimpleDate(latestTime);
				Date comparedDate = DateUtil.getSimpleDate(time);
				if(DateUtil.after(comparedDate, latestDate)){
					latestTime = time;
				}
			}
		}
		return latestTime;
	}
	@Override
	public void saveVehicleYearInspection(String thisTime, String nextTime, String vehicleId) throws Exception {
		String sql = "update OA_VehicleInfo set inspectionHandle=1 where id="+vehicleId;
		baseDao.excuteSql(sql);
		String hql = "from VehicleInfoEntity where id="+vehicleId;
		VehicleInfoEntity vehicle = (VehicleInfoEntity) baseDao.hqlfindUniqueResult(hql);
		byte[] yearlyInspection = vehicle.getYearlyInspectionRecord();
		YearlyInspectionVo yearlyInspectionVo = (YearlyInspectionVo) ObjectByteArrTransformer.toObject(yearlyInspection);
		List<String> times = new ArrayList<>();
		times.addAll(Arrays.asList(yearlyInspectionVo.getTime()));
		List<String> nextYearlyInspectionTimes = new ArrayList<>();
		nextYearlyInspectionTimes.addAll(Arrays.asList
				(yearlyInspectionVo.getNextYearlyInspectionTime()));
		times.add(thisTime);
		nextYearlyInspectionTimes.add(nextTime);
		yearlyInspectionVo.setTime(times.toArray(new String[times.size()]));
		yearlyInspectionVo.setNextYearlyInspectionTime(nextYearlyInspectionTimes.toArray(
				new String[nextYearlyInspectionTimes.size()]));
		vehicle.setYearlyInspectionRecord(ObjectByteArrTransformer.toByteArray(yearlyInspectionVo));
		baseDao.hqlUpdate(vehicle);
	}
}
