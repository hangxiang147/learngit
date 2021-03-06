package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.service.StaffAnalysisService;
import com.zhizaolian.staff.utils.DateUtil;

@Service(value="staffAnalysisService")
public class StaffAnalysisServiceImpl implements StaffAnalysisService {
	@Autowired
	private BaseDao baseDao;
	@Override
	public Map<String, Object> getStaffNumByMonthAndDep(List<String> depNames, List<String> depIdStrs) {
		Map<String, Object> dataMap = new HashMap<>();
		List<Map<String, Object>> seriesList = new ArrayList<>();
		List<String> monthList = new ArrayList<>();
		Set<String> allStaffs = new HashSet<>();
		Set<String> allLeaveStaffs = new HashSet<>();
		Set<String> allComeStaffs = new HashSet<>();
		int index = 0;
		List<String> departmentNames = new ArrayList<>();
		List<List<Integer>> staffNumByDepList =  new ArrayList<>();
		for(String depIdStr: depIdStrs){
			//同一个部门的最近7个月的每个月的人员数量
			List<Integer> staffNumList =  new ArrayList<>();
			//大部门下面所有的小部门id
			List<String> smallDepIds = new ArrayList<>();
			String[] depIds = depIdStr.split(",");
			//获取小部门
			for(String depId: depIds){
				List<String> childDepIds = new ArrayList<>();
				getChildDepIds(depId, childDepIds);
				smallDepIds.addAll(childDepIds);
			}
			smallDepIds.addAll(Arrays.asList(depIds));
			//获取小部门下面的员工
			List<String> staffs = getStaffsByDepId(smallDepIds);
			int staffNum = staffs.size();
			for(String staff: allStaffs){
				//重复了
				if(staffs.contains(staff)){
					staffNum--;
				}
			}
			if(staffNum<1){
				index++;
				continue;
			}
			departmentNames.add(depNames.get(index));
			allStaffs.addAll(staffs);
			staffNumList.add(staffNum);
			int currentMonth = DateUtil.getMonth(new Date());
			int monthNum = 1;
			Calendar cal = Calendar.getInstance();
			//依次获取前6个月的人员数量
			while(monthNum<7){
				int year = 0;
				int month = 0;
				if(monthNum!=1){
					//往前推1个月
					cal.add(Calendar.MONTH, -1);
					year = cal.get(Calendar.YEAR);
					month = cal.get(Calendar.MONTH)+1;
				}else{
					year = DateUtil.getMonth(new Date());
					month = currentMonth+1;
				}
				//该月离职人数
				List<String> leaveStaffs = getLeaveNumByMonth(year, month, smallDepIds);
				int leaveStaffNum = leaveStaffs.size();
				for(String leaveStaff: allLeaveStaffs){
					if(leaveStaffs.contains(leaveStaff)){
						leaveStaffNum--;
					}
				}
				allLeaveStaffs.addAll(leaveStaffs);
				//加上离职的人员
				staffNum += leaveStaffNum;
				//该月入职人数
				List<String> comeStaffs = getComeNumByMonth(year, month, smallDepIds);
				int comeStaffNum = comeStaffs.size();
				for(String comeStaff: allComeStaffs){
					if(comeStaffs.contains(comeStaff)){
						comeStaffNum--;
					}
				}
				allComeStaffs.addAll(comeStaffs);
				//减去入职的人员
				staffNum -= comeStaffNum;
				staffNumList.add(staffNum);
				monthNum++;
			}
			Map<String, Object> series = new HashMap<>();
			series.put("name", depNames.get(index));
			series.put("type", "bar");
			series.put("stack", "总量");
			//Map<String, Object> normal = new HashMap<>();
			//normal.put("show", true);
			//normal.put("position", "insideRight");
			//series.put("label", normal);
			series.put("data", staffNumList);
			staffNumByDepList.add(staffNumList);
			seriesList.add(series);
			index++;
		}
		List<Integer> totalStaffNumByMonth = new ArrayList<>();
		int monthNum = 7;
		for(int i=0; i<monthNum; i++){
			totalStaffNumByMonth.add(0);
		}
		for(List<Integer> staffNumByDep: staffNumByDepList){
			for(int i=0; i<monthNum; i++){
				int totalStaffNum = totalStaffNumByMonth.get(i);
				totalStaffNum += staffNumByDep.get(i);
				totalStaffNumByMonth.set(i, totalStaffNum);
			}
		}
		int maxStaffNum = 0;
		for(int num: totalStaffNumByMonth){
			if(num>maxStaffNum){
				maxStaffNum = num;
			}
		}
		dataMap.put("maxStaffNum", maxStaffNum);
		Map<String, Object> series = new HashMap<>();
		series.put("name", "总人数");
		series.put("type", "bar");
		series.put("stack", "总量");
		series.put("data", totalStaffNumByMonth);
		Map<String, String> styleMap = new HashMap<>();
		styleMap.put("color", "#555");
		Map<String, Object> normalMap = new HashMap<>();
		normalMap.put("show", true);
		normalMap.put("position", "insideLeft");
		normalMap.put("textStyle", styleMap);
		series.put("label", normalMap);
		series.put("color", "#fff");
		seriesList.add(series);
		dataMap.put("series", seriesList);
		Calendar calForMonth = Calendar.getInstance();
		monthList.add(getMonth(calForMonth.get(Calendar.MONTH))+"月份");
		for(int i=0; i<=5; i++){
			calForMonth.add(Calendar.MONTH, -1);
			monthList.add(getMonth(calForMonth.get(Calendar.MONTH))+"月份");
		}
		dataMap.put("data", monthList);
		dataMap.put("departmentNames", departmentNames);
		return dataMap;
	}

	private String getMonth(int i) {
		String[] month = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};
		return month[i];
	}

	private List<String> getComeNumByMonth(int year, int month, List<String> smallDepIds) {
		String sql = "SELECT DISTINCT\n" +
				"	(staff.UserID)\n" +
				"FROM\n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail\n" +
				"WHERE\n" +
				"	staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND MONTH(staff.EntryDate) = "+month+"\n" +
				"AND YEAR(staff.EntryDate) = "+year+"\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		List<Object> objList = baseDao.findBySql(sql);
		List<String> staffs = new ArrayList<>();
		for(Object obj: objList){
			staffs.add(String.valueOf(obj));
		}
		return staffs;
	}

	private List<String> getLeaveNumByMonth(int year, int month, List<String> smallDepIds) {
		String sql = "SELECT DISTINCT\n" +
				"	(staff.UserID)\n" +
				"FROM\n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail\n" +
				"WHERE\n" +
				"	staff.IsDeleted = 0\n" +
				"AND staff.`Status` = 4\n" +
				"AND MONTH(staff.LeaveDate) = "+month+"\n" +
				"AND YEAR(staff.LeaveDate) = "+year+"\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		List<Object> objList = baseDao.findBySql(sql);
		List<String> staffs = new ArrayList<>();
		for(Object obj: objList){
			staffs.add(String.valueOf(obj));
		}
		return staffs;
	}

	private List<String> getStaffsByDepId(List<String> smallDepIds) {
		String sql = "SELECT DISTINCT\n" +
				"	(staff.UserID)\n" +
				"FROM\n" +
				"	OA_Staff staff,\n" +
				"	ACT_ID_MEMBERSHIP ship,\n" +
				"	OA_GroupDetail detail\n" +
				"WHERE\n" +
				"	staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		List<Object> objList = baseDao.findBySql(sql);
		List<String> staffs = new ArrayList<>();
		for(Object obj: objList){
			staffs.add(String.valueOf(obj));
		}
		return staffs;
	}

	private void getChildDepIds(String depId, List<String> childDepIds) {
		String sql = "select DepartmentID from OA_Department where ParentID="+depId+" and IsDeleted=0";
		List<Object> objList = baseDao.findBySql(sql);
		for(Object obj: objList){
			childDepIds.add(String.valueOf(obj));
			getChildDepIds(String.valueOf(obj), childDepIds);
		}
	}

	@Override
	public Map<String, Object> getStaffGenderByDep(List<String> departmentNames, List<String> depIdStrs, StaffStatusEnum status) {
		Map<String, Object> dateMap = new HashMap<>();
		int males = 0;
		int females = 0;
		List<Map<String, Object>> dataMapList = new ArrayList<>();
		List<Map<String, Object>> femaleMapList = new ArrayList<>();
		Set<String> allStaffs = new HashSet<>();
		List<String> _departmentNames = new ArrayList<>();
		int index = 0;
		for(String depIdStr: depIdStrs){
			String[] depIds = depIdStr.split(",");
			//大部门下面所有的小部门id
			List<String> smallDepIds = new ArrayList<>();
			//获取小部门
			for(String depId: depIds){
				List<String> childDepIds = new ArrayList<>();
				getChildDepIds(depId, childDepIds);
				smallDepIds.addAll(childDepIds);
			}
			smallDepIds.addAll(Arrays.asList(depIds));
			Map<String, List<String>> staffsMap = getStaffsByGender(smallDepIds, status);
			List<String> maleStaffs = staffsMap.get("男");
			int maleNum = maleStaffs.size();
			for(String male: maleStaffs){
				if(allStaffs.contains(male)){
					maleNum--;
				}
			}
			if(maleNum>0){
				Map<String, Object> maleDataMap = new HashMap<>();
				maleDataMap.put("value", maleNum);
				maleDataMap.put("name", departmentNames.get(index));
				dataMapList.add(maleDataMap);
				_departmentNames.add(departmentNames.get(index));
				males += maleNum;
			}
			allStaffs.addAll(maleStaffs);
			List<String> femaleStaffs = staffsMap.get("女");
			int femaleNum = femaleStaffs.size();
			for(String female: femaleStaffs){
				if(allStaffs.contains(female)){
					femaleNum--;
				}
			}
			if(femaleNum>0){
				Map<String, Object> femaleDataMap = new HashMap<>();
				femaleDataMap.put("value", femaleNum);
				femaleDataMap.put("name", departmentNames.get(index));
				femaleMapList.add(femaleDataMap);
				_departmentNames.add(departmentNames.get(index));
				females += femaleNum;
			}
			allStaffs.addAll(femaleStaffs);
			index++;
		}
		dataMapList.addAll(femaleMapList);
		dateMap.put("maleNum", males);
		dateMap.put("femaleNum", females);
		dateMap.put("departmentNamesForGender", _departmentNames);
		dateMap.put("genderData", dataMapList);
		return dateMap;
	}

	private Map<String, List<String>> getStaffsByGender(List<String> smallDepIds, StaffStatusEnum status) {
		Map<String, List<String>> staffsMap = new HashMap<>();
		String sql = "";
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND staff.Gender = '男' \n" +
					"AND detail.DepartmentID in ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND staff.Gender = '男' \n" +
					"AND detail.DepartmentID in ("+StringUtils.join(smallDepIds, ",")+")";
		}
		List<Object> objList = baseDao.findBySql(sql);
		List<String> males = new ArrayList<>();
		for(Object obj: objList){
			males.add(String.valueOf(obj));
		}
		staffsMap.put("男", males);
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND staff.Gender = '女' \n" +
					"AND detail.DepartmentID in ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND staff.Gender = '女' \n" +
					"AND detail.DepartmentID in ("+StringUtils.join(smallDepIds, ",")+")";
		}

		List<Object> femaleObjList = baseDao.findBySql(sql);
		List<String> feMales = new ArrayList<>();
		for(Object obj: femaleObjList){
			feMales.add(String.valueOf(obj));
		}
		staffsMap.put("女", feMales);
		return staffsMap;
	}
	@Override
	public Map<String, Object> getStaffInfoByDep(List<String> departmentNames, List<String> depIdStrs, StaffStatusEnum status) {
		Map<String, Object> dataMap = new HashMap<>();
		List<Map<String, Object>> seriesList = new ArrayList<>();
		List<String> departmentNamesForStaffInfo = new ArrayList<>();
		Set<String> marryStaffs = new HashSet<>();
		List<Integer> unMarriedStaffNum = new ArrayList<>();
		List<Integer> marriedStaffNum = new ArrayList<>();
		List<Integer> unknownStaffNum = new ArrayList<>();

		Set<String> genderStaffs = new HashSet<>();
		List<Integer> maleStaffNum = new ArrayList<>();
		List<Integer> femaleStaffNum = new ArrayList<>();

		Set<String> ageStaffs = new HashSet<>();
		List<Integer> age18_24StaffNum = new ArrayList<>();
		List<Integer> age25_35StaffNum = new ArrayList<>();
		List<Integer> age36_45StaffNum = new ArrayList<>();
		List<Integer> age46_55StaffNum = new ArrayList<>();
		List<Integer> age55UpStaffNum = new ArrayList<>();

		Set<String> educationStaffs = new HashSet<>();
		List<Integer> juniorBelowStaffNum = new ArrayList<>();
		List<Integer> middleStaffNum = new ArrayList<>();
		List<Integer> highStaffNum = new ArrayList<>();
		List<Integer> doctorStaffNum = new ArrayList<>();
		int index = 0;
		for(String depIdStr: depIdStrs){
			String departmentName = departmentNames.get(index);
			String[] depIds = depIdStr.split(",");
			//大部门下面所有的小部门id
			List<String> smallDepIds = new ArrayList<>();
			//获取小部门
			for(String depId: depIds){
				List<String> childDepIds = new ArrayList<>();
				getChildDepIds(depId, childDepIds);
				smallDepIds.addAll(childDepIds);
			}
			smallDepIds.addAll(Arrays.asList(depIds));
			//婚姻
			Map<String, List<String>> marryStaffsMap = getMarriedStaffs(smallDepIds, status);
			List<String> marriedStaffs = marryStaffsMap.get("已婚");
			int marriedNum = marriedStaffs.size();
			for(String marriedStaff: marriedStaffs){
				if(marryStaffs.contains(marriedStaff)){
					marriedNum--;
				}
			}
			marryStaffs.addAll(marriedStaffs);
			List<String> unMarriedStaffs = marryStaffsMap.get("未婚");
			int unMarriedNum = unMarriedStaffs.size();
			for(String unMarriedStaff: unMarriedStaffs){
				if(marryStaffs.contains(unMarriedStaff)){
					unMarriedNum--;
				}
			}
			marryStaffs.addAll(unMarriedStaffs);
			List<String> unknownStaffs = marryStaffsMap.get("未知");
			int unknownNum = unknownStaffs.size();
			for(String unknownStaff: unknownStaffs){
				if(marryStaffs.contains(unknownStaff)){
					unknownNum--;
				}
			}
			marryStaffs.addAll(unknownStaffs);
			Map<String,List<String>> ageStaffMap = getStaffsByAge(smallDepIds, status);
			List<String> age18_24Staffs = ageStaffMap.get("18-24");
			int age18_24Num = age18_24Staffs.size();
			for(String age18_24Staff: age18_24Staffs){
				if(ageStaffs.contains(age18_24Staff)){
					age18_24Num--;
				}
			}
			ageStaffs.addAll(age18_24Staffs);
			List<String> age25_35Staffs = ageStaffMap.get("25-35");
			int age25_35Num = age25_35Staffs.size();
			for(String age25_35Staff: age25_35Staffs){
				if(ageStaffs.contains(age25_35Staff)){
					age25_35Num--;
				}
			}
			ageStaffs.addAll(age25_35Staffs);
			List<String> age36_45Staffs = ageStaffMap.get("36-45");
			int age36_45Num = age36_45Staffs.size();
			for(String age36_45Staff: age36_45Staffs){
				if(ageStaffs.contains(age36_45Staff)){
					age36_45Num--;
				}
			}
			ageStaffs.addAll(age36_45Staffs);
			List<String> age46_55Staffs = ageStaffMap.get("46-55");
			int age46_55Num = age46_55Staffs.size();
			for(String age46_55Staff: age46_55Staffs){
				if(ageStaffs.contains(age46_55Staff)){
					age46_55Num--;
				}
			}
			ageStaffs.addAll(age46_55Staffs);
			List<String> age55UpStaffs = ageStaffMap.get("55以上");
			int age55UpNum = age55UpStaffs.size();
			for(String age55UpStaff: age55UpStaffs){
				if(ageStaffs.contains(age55UpStaff)){
					age55UpNum--;
				}
			}
			ageStaffs.addAll(age55UpStaffs);
			Map<String, List<String>> educationStaffMap = getStaffByEducation(smallDepIds, status);
			List<String> juniorStaffs = educationStaffMap.get("大专及以下");
			int juniorNum = juniorStaffs.size();
			for(String juniorStaff: juniorStaffs){
				if(educationStaffs.contains(juniorStaff)){
					juniorNum--;
				}
			}
			educationStaffs.addAll(juniorStaffs);
			List<String> middleStaffs = educationStaffMap.get("本科");
			int middleNum = middleStaffs.size();
			for(String middleStaff: middleStaffs){
				if(educationStaffs.contains(middleStaff)){
					middleNum--;
				}
			}
			educationStaffs.addAll(middleStaffs);
			List<String> highStaffs = educationStaffMap.get("研究生");
			int highNum = highStaffs.size();
			for(String highStaff: highStaffs){
				if(educationStaffs.contains(highStaff)){
					highNum--;
				}
			}
			educationStaffs.addAll(highStaffs);
			List<String> doctorStaffs = educationStaffMap.get("博士");
			int doctorNum = doctorStaffs.size();
			for(String doctorStaff: doctorStaffs){
				if(educationStaffs.contains(doctorStaff)){
					doctorNum--;
				}
			}
			educationStaffs.addAll(highStaffs);

			Map<String, List<String>> genderStaffMap = getStaffsByGender(smallDepIds, status);
			List<String> maleStaffs = genderStaffMap.get("男");
			int maleNum = maleStaffs.size();
			for(String male: maleStaffs){
				if(genderStaffs.contains(male)){
					maleNum--;
				}
			}
			genderStaffs.addAll(maleStaffs);
			List<String> femaleStaffs = genderStaffMap.get("女");
			int femaleNum = femaleStaffs.size();
			for(String female: femaleStaffs){
				if(genderStaffs.contains(female)){
					femaleNum--;
				}
			}
			genderStaffs.addAll(femaleStaffs);
			if(marriedNum>0 || unMarriedNum>0 || maleNum>0 || femaleNum>0
					|| age18_24Num>0 || age25_35Num>0 || age36_45Num>0 || age46_55Num>0
					|| age55UpNum>0 || juniorNum>0 || middleNum>0 || highNum>0 || doctorNum>0 || unknownNum>0){
				marriedStaffNum.add(marriedNum);
				unMarriedStaffNum.add(unMarriedNum);
				unknownStaffNum.add(unknownNum);
				maleStaffNum.add(maleNum);
				femaleStaffNum.add(femaleNum);
				age18_24StaffNum.add(age18_24Num);
				age25_35StaffNum.add(age25_35Num);
				age36_45StaffNum.add(age36_45Num);
				age46_55StaffNum.add(age46_55Num);
				age55UpStaffNum.add(age55UpNum);
				juniorBelowStaffNum.add(juniorNum);
				middleStaffNum.add(middleNum);
				highStaffNum.add(highNum);
				doctorStaffNum.add(doctorNum);
				departmentNamesForStaffInfo.add(departmentName);
			}
			index++;
		}
		dataMap.put("departmentsForStaffInfo", departmentNamesForStaffInfo);
		//婚姻
		Map<String, Object> unMarrySeries = new HashMap<>();
		unMarrySeries.put("name", "未婚");
		unMarrySeries.put("type", "bar");
		unMarrySeries.put("stack", "婚姻");
		unMarrySeries.put("data", unMarriedStaffNum);
		seriesList.add(unMarrySeries);
		Map<String, Object> marrySeries = new HashMap<>();
		marrySeries.put("name", "已婚");
		marrySeries.put("type", "bar");
		marrySeries.put("stack", "婚姻");
		marrySeries.put("data", marriedStaffNum);
		seriesList.add(marrySeries);
		Map<String, Object> unknownSeries = new HashMap<>();
		unknownSeries.put("name", "未知");
		unknownSeries.put("type", "bar");
		unknownSeries.put("stack", "婚姻");
		unknownSeries.put("data", unknownStaffNum);
		seriesList.add(unknownSeries);
		//性别
		Map<String, Object> maleSeries = new HashMap<>();
		maleSeries.put("name", "男性");
		maleSeries.put("type", "bar");
		maleSeries.put("stack", "性别");
		maleSeries.put("data", maleStaffNum);
		seriesList.add(maleSeries);
		Map<String, Object> femaleSeries = new HashMap<>();
		femaleSeries.put("name", "女性");
		femaleSeries.put("type", "bar");
		femaleSeries.put("stack", "性别");
		femaleSeries.put("data", femaleStaffNum);
		seriesList.add(femaleSeries);
		//年龄
		Map<String, Object> age18_24Series = new HashMap<>();
		age18_24Series.put("name", "18-24");
		age18_24Series.put("type", "bar");
		age18_24Series.put("stack", "年龄");
		age18_24Series.put("data", age18_24StaffNum);
		seriesList.add(age18_24Series);
		Map<String, Object> age25_35Series = new HashMap<>();
		age25_35Series.put("name", "25-35");
		age25_35Series.put("type", "bar");
		age25_35Series.put("stack", "年龄");
		age25_35Series.put("data", age25_35StaffNum);
		seriesList.add(age25_35Series);
		Map<String, Object> age36_45Series = new HashMap<>();
		age36_45Series.put("name", "36-45");
		age36_45Series.put("type", "bar");
		age36_45Series.put("stack", "年龄");
		age36_45Series.put("data", age36_45StaffNum);
		seriesList.add(age36_45Series);
		Map<String, Object> age46_55Series = new HashMap<>();
		age46_55Series.put("name", "46-55");
		age46_55Series.put("type", "bar");
		age46_55Series.put("stack", "年龄");
		age46_55Series.put("data", age46_55StaffNum);
		seriesList.add(age46_55Series);
		Map<String, Object> age55UpSeries = new HashMap<>();
		age55UpSeries.put("name", "55以上");
		age55UpSeries.put("type", "bar");
		age55UpSeries.put("stack", "年龄");
		age55UpSeries.put("data", age55UpStaffNum);
		seriesList.add(age55UpSeries);
		Map<String, Object> juniorSeries = new HashMap<>();
		juniorSeries.put("name", "大专及以下");
		juniorSeries.put("type", "bar");
		juniorSeries.put("stack", "学历");
		juniorSeries.put("data", juniorBelowStaffNum);
		seriesList.add(juniorSeries);
		Map<String, Object> middleSeries = new HashMap<>();
		middleSeries.put("name", "本科");
		middleSeries.put("type", "bar");
		middleSeries.put("stack", "学历");
		middleSeries.put("data", middleStaffNum);
		seriesList.add(middleSeries);
		Map<String, Object> highSeries = new HashMap<>();
		highSeries.put("name", "研究生");
		highSeries.put("type", "bar");
		highSeries.put("stack", "学历");
		highSeries.put("data", highStaffNum);
		seriesList.add(highSeries);
		Map<String, Object> doctorSeries = new HashMap<>();
		doctorSeries.put("name", "博士");
		doctorSeries.put("type", "bar");
		doctorSeries.put("stack", "学历");
		doctorSeries.put("data", doctorStaffNum);
		seriesList.add(doctorSeries);
		dataMap.put("series", seriesList);
		return dataMap;
	}

	private Map<String, List<String>> getStaffByEducation(List<String> smallDepIds, StaffStatusEnum status) {
		Map<String, List<String>> staffsMap = new HashMap<>();
		String sql = "";
		//大专及以下
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.Education not in('研究生','本科','博士','硕士')\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.Education not in('研究生','本科','博士','硕士')\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		List<Object> objList = baseDao.findBySql(sql);
		List<String> juniorStaffs = new ArrayList<>();
		for(Object obj: objList){
			juniorStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("大专及以下", juniorStaffs);
		//本科
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.Education='本科'\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.Education='本科'\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> middleStaffs = new ArrayList<>();
		for(Object obj: objList){
			middleStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("本科", middleStaffs);
		//研究生
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND (staff.Education='研究生' OR staff.Education='硕士')\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND (staff.Education='研究生' OR staff.Education='硕士')\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> highStaffs = new ArrayList<>();
		for(Object obj: objList){
			highStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("研究生", highStaffs);
		//博士
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.Education='博士'\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.Education='博士'\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> doctorStaffs = new ArrayList<>();
		for(Object obj: objList){
			doctorStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("博士", doctorStaffs);
		return staffsMap;
	}

	private Map<String, List<String>> getStaffsByAge(List<String> smallDepIds, StaffStatusEnum status) {
		Map<String, List<String>> staffsMap = new HashMap<>();
		Date currentDate = new Date();
		int year = DateUtil.getYear(currentDate);
		String sql = "";
		//18-24
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-24)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-18)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-24)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-18)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		List<Object> objList = baseDao.findBySql(sql);
		List<String> age18_24Staffs = new ArrayList<>();
		for(Object obj: objList){
			age18_24Staffs.add(String.valueOf(obj));
		}
		staffsMap.put("18-24", age18_24Staffs);
		//25-35
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-35)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-25)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-35)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-25)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> age25_35Staffs = new ArrayList<>();
		for(Object obj: objList){
			age25_35Staffs.add(String.valueOf(obj));
		}
		staffsMap.put("25-35", age25_35Staffs);
		//36-45
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-45)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-36)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-45)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-36)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> age36_45Staffs = new ArrayList<>();
		for(Object obj: objList){
			age36_45Staffs.add(String.valueOf(obj));
		}
		staffsMap.put("36-45", age36_45Staffs);
		//46-55
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-55)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-46)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND YEAR(staff.Birthday)>="+(year-55)+"\n" +
					"AND YEAR(staff.Birthday)<="+(year-46)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> age46_55Staffs = new ArrayList<>();
		for(Object obj: objList){
			age46_55Staffs.add(String.valueOf(obj));
		}
		staffsMap.put("46-55", age46_55Staffs);
		//55以上
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND YEAR(staff.Birthday)<"+(year-55)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND YEAR(staff.Birthday)<"+(year-55)+"\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> age55UpStaffs = new ArrayList<>();
		for(Object obj: objList){
			age55UpStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("55以上", age55UpStaffs);
		return staffsMap;
	}

	private Map<String, List<String>> getMarriedStaffs(List<String> smallDepIds, StaffStatusEnum status) {
		Map<String, List<String>> staffsMap = new HashMap<>();
		String sql = "";
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.MaritalStatus = 0\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.MaritalStatus = 0\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		List<Object> objList = baseDao.findBySql(sql);
		List<String> unMarriedStaffs = new ArrayList<>();
		for(Object obj: objList){
			unMarriedStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("未婚", unMarriedStaffs);
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.MaritalStatus = 1\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.MaritalStatus = 1\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> marriedStaffs = new ArrayList<>();
		for(Object obj: objList){
			marriedStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("已婚", marriedStaffs);
		if(status.getValue() == StaffStatusEnum.JOB.getValue()){
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` != 4\n" +
					"AND staff.MaritalStatus is null\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}else{
			sql = "SELECT DISTINCT\n" +
					"	(staff.UserID)\n" +
					"FROM\n" +
					"	OA_Staff staff,\n" +
					"	ACT_ID_MEMBERSHIP ship,\n" +
					"	OA_GroupDetail detail\n" +
					"WHERE\n" +
					"	staff.IsDeleted = 0\n" +
					"AND staff.`Status` = 4\n" +
					"AND staff.MaritalStatus is null\n" +
					"AND staff.UserID = ship.USER_ID_\n" +
					"AND ship.GROUP_ID_ = detail.GroupID\n" +
					"AND detail.DepartmentID IN ("+StringUtils.join(smallDepIds, ",")+")";
		}
		objList = baseDao.findBySql(sql);
		List<String> unknownStaffs = new ArrayList<>();
		for(Object obj: objList){
			unknownStaffs.add(String.valueOf(obj));
		}
		staffsMap.put("未知", unknownStaffs);
		return staffsMap;
	}

	@Override
	public Map<String, Object> getLeaveStaffNumByMonthAndDep(List<String> depNames,
			List<String> depIdStrs) {
		Map<String, Object> dataMap = new HashMap<>();
		List<Map<String, Object>> seriesList = new ArrayList<>();
		List<String> monthList = new ArrayList<>();
		Set<String> allLeaveStaffs = new HashSet<>();
		int index = 0;
		List<String> departmentNames = new ArrayList<>();
		List<List<Integer>> staffNumByDepList =  new ArrayList<>();
		for(String depIdStr: depIdStrs){
			//同一个部门的最近7个月的每个月的人员数量
			List<Integer> staffNumList =  new ArrayList<>();
			//大部门下面所有的小部门id
			List<String> smallDepIds = new ArrayList<>();
			String[] depIds = depIdStr.split(",");
			//获取小部门
			for(String depId: depIds){
				List<String> childDepIds = new ArrayList<>();
				getChildDepIds(depId, childDepIds);
				smallDepIds.addAll(childDepIds);
			}
			smallDepIds.addAll(Arrays.asList(depIds));
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			//获取小部门该月的离职人数
			List<String> staffs = getLeaveNumByMonth(year, month, smallDepIds);
			int staffNum = staffs.size();
			for(String staff: allLeaveStaffs){
				//重复了
				if(staffs.contains(staff)){
					staffNum--;
				}
			}
			allLeaveStaffs.addAll(staffs);
			staffNumList.add(staffNum);
			int monthNum = 1;
			//依次获取前6个月的离职人员数量
			while(monthNum<7){
				//往前推1个月
				cal.add(Calendar.MONTH, -1);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH)+1;
				//该月离职人数
				List<String> leaveStaffs = getLeaveNumByMonth(year, month, smallDepIds);
				int leaveStaffNum = leaveStaffs.size();
				for(String leaveStaff: allLeaveStaffs){
					if(leaveStaffs.contains(leaveStaff)){
						leaveStaffNum--;
					}
				}
				allLeaveStaffs.addAll(leaveStaffs);
				staffNumList.add(leaveStaffNum);
				monthNum++;
			}
			for(int num: staffNumList){
				if(num>0){
					Map<String, Object> series = new HashMap<>();
					series.put("name", depNames.get(index));
					series.put("type", "bar");
					series.put("stack", "总量");
					//Map<String, Object> normal = new HashMap<>();
					//normal.put("show", true);
					//normal.put("position", "insideRight");
					//series.put("label", normal);
					series.put("data", staffNumList);
					staffNumByDepList.add(staffNumList);
					seriesList.add(series);
					departmentNames.add(depNames.get(index));
					break;
				}
			}
			index++;
		}
		List<Integer> totalLeaveStaffNumByMonth = new ArrayList<>();
		int monthNum = 7;
		for(int i=0; i<monthNum; i++){
			totalLeaveStaffNumByMonth.add(0);
		}
		for(List<Integer> staffNumByDep: staffNumByDepList){
			for(int i=0; i<monthNum; i++){
				int totalStaffNum = totalLeaveStaffNumByMonth.get(i);
				totalStaffNum += staffNumByDep.get(i);
				totalLeaveStaffNumByMonth.set(i, totalStaffNum);
			}
		}
		int maxStaffNum = 0;
		for(int num: totalLeaveStaffNumByMonth){
			if(num>maxStaffNum){
				maxStaffNum = num;
			}
		}
		dataMap.put("maxStaffNum", maxStaffNum);
		Map<String, Object> series = new HashMap<>();
		series.put("name", "总人数");
		series.put("type", "bar");
		series.put("stack", "总量");
		series.put("data", totalLeaveStaffNumByMonth);
		Map<String, String> styleMap = new HashMap<>();
		styleMap.put("color", "#555");
		Map<String, Object> normalMap = new HashMap<>();
		normalMap.put("show", true);
		normalMap.put("position", "insideLeft");
		normalMap.put("textStyle", styleMap);
		series.put("label", normalMap);
		series.put("color", "#fff");
		seriesList.add(series);
		dataMap.put("series", seriesList);
		Calendar calForMonth = Calendar.getInstance();
		monthList.add(getMonth(calForMonth.get(Calendar.MONTH))+"月份");
		for(int i=0; i<=5; i++){
			calForMonth.add(Calendar.MONTH, -1);
			monthList.add(getMonth(calForMonth.get(Calendar.MONTH))+"月份");
		}
		dataMap.put("data", monthList);
		dataMap.put("departmentNames", departmentNames);
		return dataMap;
	}
}
