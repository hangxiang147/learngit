package com.zhizaolian.staff.timedTask;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.zhizaolian.staff.entity.VehicleInfoEntity;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VehicleService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.InsuranceRecord;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.YearlyInspectionVo;

/**
 * 定时同步保险/年检离过期还有最多一个月的车辆
 * @author yxl
 *
 */
@Lazy(value=false)
public class SynoSoonOverdueVehicle {
	@Autowired
	private VehicleService vehicleService;
	@Autowired
	private StaffService staffService;
	
	public void findSoonOverdueVehicle(){
		try {
			ShortMsgSender shortMsgSender = ShortMsgSender.getInstance();
			List<VehicleInfoEntity> vehicles = vehicleService.findAllVehicleList();
			//当前日期
			Date currentDate = DateUtil.getSimpleDate(DateUtil.formateDate(new Date()));
			for(VehicleInfoEntity vehicle: vehicles){
				//负责人
				String leaderId = vehicle.getLeaderId();
				StaffVO staff = staffService.getStaffByUserID(leaderId);
				String name = "";
				String telephone = "";
				if(null!=staff){
					telephone = staff.getTelephone();
					if("男".equals(staff.getGender())){
						name = staff.getLastName()+"先生";
					}else{
						name = staff.getLastName()+"女士";
					}
				}
				InsuranceRecord insuranceRecord = vehicle.getInsuranceRecordVo();
				if(null!=insuranceRecord){
					String[] nextInsuranceTimes = insuranceRecord.getNextInsuranceTime();
					String nextInsuranceTime = getLatestTime(nextInsuranceTimes);
					//下次保险日期
					Date nextInsuranceDate = DateUtil.getSimpleDate(nextInsuranceTime);
					long diffTime = nextInsuranceDate.getTime()-currentDate.getTime();
					//距离到期两个月内/过期
					if(diffTime<(30*24*60*60*1000L)){
						//0 表示未发短信
						if(StringUtils.isNotBlank(telephone) && 0==vehicle.getInsuranceSendMsg()){
							String content = "【智造链】 尊敬的"+name+"，您所负责的车牌号为"+vehicle.getLicenseNumber()+"的车辆，保险于"
											 +nextInsuranceTime+"到期，请及时办理";
							shortMsgSender.send(telephone, content);
							//短信已发
							vehicle.setInsuranceSendMsg(1);
						}
						//未办理
						vehicle.setInsuranceHandle(0);
						vehicleService.saveVehicle(vehicle);
					}
				}
				YearlyInspectionVo inspection = vehicle.getYearlyInspectionVo();
				if(null!=inspection){
					String[] nextYearlyInspectionTimes = inspection.getNextYearlyInspectionTime();
					String nextYearlyInspectionTime = getLatestTime(nextYearlyInspectionTimes);
					//下次年检日期
					Date nextYearlyInspectionDate = DateUtil.getSimpleDate(nextYearlyInspectionTime);
					//距离到期两个月内
					if((nextYearlyInspectionDate.getTime()-currentDate.getTime())<(30*24*60*60*1000L)){
						//0 表示未发短信
						if(StringUtils.isNotBlank(telephone) && 0==vehicle.getInspectionSendMsg()){
							String content = "【智造链】 尊敬的"+name+"，您所负责的车牌号为"+vehicle.getLicenseNumber()+"的车辆，年检于"
											 +nextYearlyInspectionTime+"到期，请及时办理";
							shortMsgSender.send(telephone, content);
							//短信已发
							vehicle.setInspectionSendMsg(1);
						}
						vehicle.setInspectionHandle(0);
						vehicleService.saveVehicle(vehicle);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private String getLatestTime(String[] times){
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
}
