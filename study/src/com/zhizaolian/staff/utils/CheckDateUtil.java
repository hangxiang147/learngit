package com.zhizaolian.staff.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

/**
*@author Zhouk
*@date 2017年3月12日 下午4:04:18
*@describtion 在报表统计 和签到统计中 遇到一个问题
*一个人 2017-01-01 08:00:00 到 2017-01-01 08:10:00 
*又从 2017-01-01 08:10:00  到  2017-01-01 18:10:00  请假  
*我们不能仅仅通过判断某一组 时间区间 去 判断这个人是否在这天请了假
*所以写了 这个工具类   判断 当天的 08:30:00-11:20:00   12:10:00-18:00:00 是否在请假
**/
public class CheckDateUtil { 
	private final static String AM_START_TIME="08:30:00";
	private final static String AM_END_TIME="11:20:00";
	private final static String PM_START_TIME="12:30:00";
	private final static String PM_END_TIME="18:00:00";
	private final static SimpleDateFormat SIMPLE_DATE_FORMAT_DAY=new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat SIMPLE_DATE_FORMAT_SECOND=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static boolean isVacation(Date time,Set<Date[]> vacationTimes, Integer companyId, String[] arr_times){
		//String[] arr_times=CompanyIDEnum.valueOf(companyId).getTimeLimitByDate(null).split(" ");
		try {
			//String[] arr_times = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyId+"", "");
			String date = DateUtil.formateDate(time);
			Date beginDate = DateUtil.getFullDate(date+" "+arr_times[0]+":00");
			Date amEndDate = DateUtil.getFullDate(date+" "+arr_times[1]+":00");
			Date pmBeginDate = DateUtil.getFullDate(date+" "+arr_times[2]+":00");
			Date endDate = DateUtil.getFullDate(date+" "+ arr_times[3]+":00");
			Date[] times = new Date[]{beginDate, amEndDate, pmBeginDate, endDate};
			//Date[] times=getJobBeginTimeAndEndTime(time);
			Set<Date[]> effectiveTime = effectiveTimeFilter(times,vacationTimes);
			if(CollectionUtils.isEmpty(effectiveTime))return false;
			return isItemTimeVacation(times, vacationTimes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private  static Date[] getJobBeginTimeAndEndTime(Date date){
		Date[] dates=null;
		try{
			dates=new Date[4];
			String dayStr=SIMPLE_DATE_FORMAT_DAY.format(date);
			dates[0]=SIMPLE_DATE_FORMAT_SECOND.parse(dayStr+" "+AM_START_TIME);
			dates[1]=SIMPLE_DATE_FORMAT_SECOND.parse(dayStr+" "+AM_END_TIME);
			dates[2]=SIMPLE_DATE_FORMAT_SECOND.parse(dayStr+" "+PM_START_TIME);
			dates[3]=SIMPLE_DATE_FORMAT_SECOND.parse(dayStr+" "+PM_END_TIME);
		}catch(Exception ignore){}
		return dates;
	}
	/**
	 * 根据日期 筛选出有用 日期 数据   目标日期开始时间 小于等于 结束时间 并且  目标日期 结束 时间 大于等于开始时间
	 * @param time
	 * @param vacationTimes
	 * @return
	 */
	private static Set<Date[]> effectiveTimeFilter(Date[] times,Set<Date[]> vacationTimes){
		Iterator<Date[]> it=vacationTimes.iterator();
		while(it.hasNext()){
			Date[] vdates=it.next();
			boolean isEffective=false;
			if(times[0].compareTo(vdates[1])<=0){
				if(times[3].compareTo(vdates[0])>=0){
					isEffective=true;
				}
			}
			if(!isEffective){
				it.remove();
			}
		}
		return vacationTimes;
	}
	
	/**
	 * @param times
	 * @param vacationTimes
	 * @return
	 * 按照开始时间由低到高排序
	 */
	private static boolean isItemTimeVacation(Date[] times,Set<Date[]> vacationTimes){
		List<Date[]> vacationTimesList=new ArrayList<Date[]>(vacationTimes);
		if(vacationTimesList.size()>1)
			java.util.Collections.sort(vacationTimesList,new Comparator <Date[]>() {
				@Override
				public int compare(Date[] o1, Date[] o2) {
					return o1[0].compareTo(o2[0]);
				}
			
			});
		return ValidateByStep(0,vacationTimesList,times,times[0],0);
	}
	/**
	 * @param index
	 * @param vacationTimes
	 * @param times
	 * @param nextStartTime 下一个日期的需要的开始时间
	 * @param nextTimeType  nextStartTime所在的区间    (减少重复判断)   0:[8:30-11.20)  1:[12:10-18:00)
	 * @return
	 */
	private static boolean ValidateByStep(int index,List<Date[]> vacationTimes,Date[] times,Date nextStartTime,int nextTimeType){
		if(index>=vacationTimes.size())return false;
		Date[] currentTime=vacationTimes.get(index);
		//当前时间区间开始时间必须 小于等于 nextStartTime
		if(currentTime[0].compareTo(nextStartTime)>0)return false;
		//如果结束时间大于等于 pm_end_time return true;
		if(currentTime[1].compareTo(times[3])>=0)return true;
		//如果还是上午
		if(nextTimeType==0){
			//如果结束时间大于等于 am_endTime 那么 下个开始时间取  times[2]和 currentTime[1]更大的值
			if(currentTime[1].compareTo(times[1])>=0){
				return ValidateByStep(++index, vacationTimes, times, times[2].compareTo(currentTime[1])>0?times[2]:currentTime[1], 1);
			}
			//如果 结束时间 小于am_endTime
			else{
				return ValidateByStep(++index, vacationTimes, times, currentTime[1], 0);
			}
			
		}else{
			//下午 还未结束的情况
			return ValidateByStep(++index, vacationTimes, times, currentTime[1], 1);
		}	
	}
	
	/**
	 * 测试
	 * @param args
	 */
//	public static void main(String[] args) {
//		Function<String[], Date[]> createTestDate=new Function<String[], Date[]>() {
//			@Override
//			public Date[] apply(String[] arg0) {
//				Date[] date=new Date[2];
//				try{
//
//					date[0]=SIMPLE_DATE_FORMAT_SECOND.parse(arg0[0]);
//					date[1]=SIMPLE_DATE_FORMAT_SECOND.parse(arg0[1]);
//				}catch(Exception ignore){}
//				return date;
//			}
//		};
//		Set<Date[]> testTimes=new HashSet<Date[]>();
//		Date checkTime=DateUtil.parseDay("2017-01-01");
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 18:30:00"}));
//		assert isVacation(checkTime, testTimes)==true;
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 18:30:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 18:30:00"}));
//		assert isVacation(checkTime, testTimes)==true;
//		testTimes.clear();
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 17:59:00"}));
//		assert isVacation(checkTime, testTimes)==false;
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 17:59:59"}));
//		assert isVacation(checkTime, testTimes)==false;
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 17:59:59","2017-01-01 19:59:59"}));
//		assert isVacation(checkTime, testTimes)==true;
//		testTimes.clear();
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:31:00","2017-01-01 17:59:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 17:59:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 18:59:00"}));
//		assert isVacation(checkTime, testTimes)==true;
//		testTimes.clear();
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 09:30:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 09:31:00","2017-01-01 17:59:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 17:31:00","2017-01-01 18:59:00"}));
//		assert isVacation(checkTime, testTimes)==false;
//		testTimes.clear();
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 08:30:00","2017-01-01 11:30:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 12:11:00","2017-01-01 17:59:00"}));
//		testTimes.add(createTestDate.apply(new String[]{"2017-01-01 17:31:00","2017-01-01 18:59:00"}));
//		assert isVacation(checkTime, testTimes)==false;
//	}
}
