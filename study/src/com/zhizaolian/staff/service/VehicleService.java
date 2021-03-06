package com.zhizaolian.staff.service;

import java.io.IOException;
import java.util.List;

import com.zhizaolian.staff.entity.VehicleInfoEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface VehicleService {

	void saveVehicle(VehicleInfoEntity vehicleVo) throws Exception;

	ListResult<VehicleInfoEntity> findVehicleList(String licenseNumber, Integer limit, Integer page) throws Exception;

	VehicleInfoEntity getVehicleDetails(String id) throws Exception;

	void deleteVehicle(String id);
	
	List<VehicleInfoEntity> findAllVehicleList() throws Exception;

	int getSoonOverDueVehicleCount();

	List<VehicleInfoEntity> getSoonOverDueVehicles() throws Exception;

	void completeHandle(String vehicleId, String handleType) throws ClassNotFoundException, IOException;

	void saveVehicleYearInspection(String thisTime, String nextTime, String vehicleId) throws Exception;
}
