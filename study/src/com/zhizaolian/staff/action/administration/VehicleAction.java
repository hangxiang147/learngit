package com.zhizaolian.staff.action.administration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.VehicleInfoEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.service.VehicleService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.vo.InsuranceRecord;
import com.zhizaolian.staff.vo.MaintainRecord;
import com.zhizaolian.staff.vo.RepairRecordVo;
import com.zhizaolian.staff.vo.VehicleUseRecord;
import com.zhizaolian.staff.vo.YearlyInspectionVo;

import lombok.Getter;
import lombok.Setter;

public class VehicleAction extends BaseAction{
	
	private static final long serialVersionUID = 3130439647130619264L;
	@Setter
	@Getter
	private VehicleInfoEntity vehicleVo;
	@Setter
	@Getter
	private InsuranceRecord insuranceRecord;
	@Setter
	@Getter
	private YearlyInspectionVo yearlyInspectionVo;
	@Setter
	@Getter
	private MaintainRecord maintainRecord;
	@Setter
	@Getter
	private RepairRecordVo repairRecord;
	@Setter
	@Getter
	private VehicleUseRecord vehicleUseRecord;
	@Autowired
	private VehicleService vehicleService;
	@Getter
	private String errorMessage;
	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer page = 1;
	@Getter
	private Integer totalPage = 1;
	@Getter
	private Integer type;
	
	public String findVehicleList(){
		String licenseNumber = request.getParameter("licenseNumber");
		try {
			ListResult<VehicleInfoEntity> VehicleInfoList = vehicleService.findVehicleList(licenseNumber, limit, page);
			count = VehicleInfoList.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			request.setAttribute("VehicleInfoList", VehicleInfoList.getList());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		request.setAttribute("licenseNumber", licenseNumber);
		selectedPanel = "vehicleManage";
		return "findVehicleList";
	}
	public String addVehicle(){
		selectedPanel = "vehicleManage";
		return "addVehicle";
	}
	public String saveVehicle(){
		try {
			//更新车辆信息
			if(null != vehicleVo.getId()){
				VehicleInfoEntity vehicleInfo = vehicleService.getVehicleDetails(vehicleVo.getId()+"");
				//修改前的保险记录
				InsuranceRecord oldInsuranceRecord = vehicleInfo.getInsuranceRecordVo();
				if(null==oldInsuranceRecord){
					//新增了保险记录
					if(null!=insuranceRecord){
						vehicleVo.setInsuranceHandle(1);
						vehicleVo.setInsuranceSendMsg(0);
					}
				}else if(null!=insuranceRecord){
					//新增了保险记录
					if(insuranceRecord.getTime().length>oldInsuranceRecord.getTime().length){
						vehicleVo.setInsuranceHandle(1);
						vehicleVo.setInsuranceSendMsg(0);
					}
				}else{
					//do nothing
				}
				//修改前的车检记录
				YearlyInspectionVo oldInspection = vehicleInfo.getYearlyInspectionVo();
				if(null==oldInspection){
					//新增了年检记录
					if(null!=yearlyInspectionVo){
						vehicleVo.setInspectionHandle(1);
						vehicleVo.setInspectionSendMsg(0);
					}
				}else if(null!=yearlyInspectionVo){
					//新增了年检记录
					if(yearlyInspectionVo.getTime().length>oldInspection.getTime().length){
						vehicleVo.setInspectionHandle(1);
						vehicleVo.setInspectionSendMsg(0);
					}
				}else{
					//do nothing
				}
			}
			
			if(null!=insuranceRecord){
				vehicleVo.setInsuranceRecord(ObjectByteArrTransformer.toByteArray(insuranceRecord));
			}
			if(null!=yearlyInspectionVo){
				vehicleVo.setYearlyInspectionRecord(ObjectByteArrTransformer.toByteArray(yearlyInspectionVo));
			}
			if(null!=maintainRecord){
				vehicleVo.setMaintainRecord(ObjectByteArrTransformer.toByteArray(maintainRecord));
			}
			if(null!=repairRecord){
				vehicleVo.setRepairRecord(ObjectByteArrTransformer.toByteArray(repairRecord));
			}
			if(null!=vehicleUseRecord){
				vehicleVo.setVehicleUseRecord(ObjectByteArrTransformer.toByteArray(vehicleUseRecord));
			}
			vehicleService.saveVehicle(vehicleVo);
			selectedPanel = "vehicleManage";
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "render_findVehicleList";
	}
	public String showVehicleDetails(){
		String id = request.getParameter("id");
		try {
			vehicleVo = vehicleService.getVehicleDetails(id);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "vehicleManage";
		return "showVehicleDetails";
	}
	public String deleteVehicle(){
		String id = request.getParameter("id");
		vehicleService.deleteVehicle(id);
		selectedPanel = "vehicleManage";
		return "render_findVehicleList";
	}
	public String updateVehicleInfo(){
		String id = request.getParameter("id");
		try {
			vehicleVo = vehicleService.getVehicleDetails(id);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "vehicleManage";
		return "updateVehicleInfo";
	}
	public String completeHandle(){
		String handleType = request.getParameter("handleType");
		String vehicleId = request.getParameter("vehicleId");
		try {
			vehicleService.completeHandle(vehicleId, handleType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		} catch (IOException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.VEHICLE_OVERDUE.getValue();
		return "toTaskList";
	}
	public String saveVehicleYearInspection(){
		String thisTime = request.getParameter("thisTime");
		String nextTime = request.getParameter("nextTime");
		String vehicleId = request.getParameter("vehicleId");
		try {
			vehicleService.saveVehicleYearInspection(thisTime, nextTime, vehicleId);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.VEHICLE_OVERDUE.getValue();
		return "toTaskList";
	}
}
