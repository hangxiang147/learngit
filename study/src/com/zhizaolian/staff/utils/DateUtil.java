package com.zhizaolian.staff.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.apache.commons.lang3.StringUtils;


public class DateUtil {

	public static Date getSimpleDate(String dateStr)
	{
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateStr);
		}
		catch (ParseException e)
		{
			throw new RuntimeException("时间字符串解析失败，请联系系统管理员！");
		}
	}

	public static Date getFullDate(String dateStr)
	{
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(dateStr);
		}
		catch (ParseException e)
		{
			throw new RuntimeException("时间字符串解析失败，请联系系统管理员！");
		}
	}

	public static String formateDate(Date date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		}
		catch (Exception e)
		{
			throw new RuntimeException("时间格式化失败，请联系系统管理员");
		}
	}
	public static String formateMonth(Date date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			return sdf.format(date);
		}
		catch (Exception e)
		{
			throw new RuntimeException("时间格式化失败，请联系系统管理员");
		}
	}
	public static String formateFullDate(Date date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		}
		catch (Exception e)
		{
			throw new RuntimeException("时间格式化失败，请联系系统管理员");
		}
	}

	public static String getTodayString() {
		Date d = new Date();
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	public static String getNowString() {
		Date d = new Date();
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
	}

	public static String getSpecifiedDayAfter(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String getFirstDayofMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String getFirstDayofMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String getLastDayofMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String getLastDayofMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String getFirstDayofPreviousMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String getLastDayofPreviousMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	private final static SimpleDateFormat SIMPLE_DATE_FORMAT_DAY=new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat SIMPLE_DATE_FORMAT_MIN=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date parseDay(String dateStr){
		Date date=null;
		try{
			date=SIMPLE_DATE_FORMAT_DAY.parse(dateStr);
		}catch(Exception ignore){};
		return date;
	} 

	public static String getDayStr(Date date){
		String resultStr=null;
		try{
			resultStr=SIMPLE_DATE_FORMAT_DAY.format(date);
		}catch(Exception ignore){};
		return resultStr;
	}
	public static String getMinStr(Date date){
		String resultStr=null;
		try{
			resultStr=SIMPLE_DATE_FORMAT_MIN.format(date);
		}catch(Exception ignore){};
		return resultStr;
	}
	public static Date[] getJobBeginTimeAndEndTime(Date date){
		Date[] dates=null;
		try{
			dates=new Date[2];
			String dayStr=SIMPLE_DATE_FORMAT_DAY.format(date);
			dates[0]=SIMPLE_DATE_FORMAT_MIN.parse(dayStr+" 08:30");
			dates[1]=SIMPLE_DATE_FORMAT_MIN.parse(dayStr+" 18:00");
		}catch(Exception ignore){}
		return dates;
	}
	/**
	 * 比较date1是否在date2之后（包括相等）
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean after(Date date1, Date date2){
		if(date1.after(date2) || date1.equals(date2)){
			return true;
		}
		return false;
	}
	/**
	 * 比较date1是否在date2之前（包括相等）
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean before(Date date1, Date date2){
		if(date1.before(date2) || date1.equals(date2)){
			return true;
		}
		return false;
	}
	public static String formateTime(Date date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			return sdf.format(date);
		}
		catch (Exception e)
		{
			throw new RuntimeException("时间格式化失败，请联系系统管理员");
		}
	}
	/**  
	 * 计算两个日期之间相差的天数  
	 * @param smdate 较小的时间 
	 * @param bdate  较大的时间 
	 * @return 相差天数 
	 * @throws ParseException  
	 */    
	public static int daysBetween(Date smDate,Date bDate) throws ParseException    
	{    
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		smDate=sdf.parse(sdf.format(smDate));  
		bDate=sdf.parse(sdf.format(bDate));  
		Calendar cal = Calendar.getInstance();    
		cal.setTime(smDate);    
		long time1 = cal.getTimeInMillis();                 
		cal.setTime(bDate);    
		long time2 = cal.getTimeInMillis();         
		long between_days=(time2-time1)/(1000*3600*24);  
		return Integer.parseInt(String.valueOf(between_days));           
	}  
	public static double hoursBetween(Date smDate,Date bDate) throws Exception{
		long differTimes = bDate.getTime() - smDate.getTime();
		return Math.floor(differTimes/(30*60*1000))/2;
	}
	public static String daysAndHoursBetween(Date smDate,Date bDate) throws Exception{
		long differTimes = bDate.getTime() - smDate.getTime();
		double hours = Math.floor(differTimes/(30*60*1000))/2;
		int days = (int) (hours/24);
		hours = hours - 24*days;
		if(days>0){
			if(hours>0){
				return days+"天"+hours+"小时";
			}else{
				return days+"天";
			}
		}else{
			return hours+"小时";
		}
	}
	/** 
	 * 根据当前日期获得所在周的日期区间（周一和周日日期） 
	 * @return 
	 */  
	public static String[] getTimeInterval() throws Exception{  
		Calendar cal = Calendar.getInstance();  
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
		if (1 == dayWeek) {  
			cal.add(Calendar.DAY_OF_MONTH, -1);  
		}  
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
		cal.setFirstDayOfWeek(Calendar.MONDAY);  
		// 获得当前日期是一个星期的第几天  
		int day = cal.get(Calendar.DAY_OF_WEEK);  
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
		String weekBegin = formateDate(cal.getTime()); 
		cal.add(Calendar.DATE, 6);  
		String weekEnd = formateDate(cal.getTime());   
		return new String[]{weekBegin, weekEnd};  
	}  
	/** 
	 * 根据当前日期获得上周的日期区间（上周周一和周日日期） 
	 */  
	public static String[] getLastTimeInterval() throws Exception{  
		Calendar calendar1 = Calendar.getInstance();  
		Calendar calendar2 = Calendar.getInstance();  
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;  
		int offset1 = 1 - dayOfWeek;  
		int offset2 = 7 - dayOfWeek;  
		calendar1.add(Calendar.DATE, offset1 - 7);  
		calendar2.add(Calendar.DATE, offset2 - 7);  
		String weekBegin = formateDate(calendar1.getTime());  
		String weekEnd = formateDate(calendar2.getTime());  
		return new String[]{weekBegin, weekEnd};  
	} 
	/**
	 * date2比date1多的天数
	 * @param date1    
	 * @param date2
	 * @return    
	 */
	public static int differentDays(Date date1,Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if(year1 != year2)   //同一年
		{
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++)
			{
				if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
				{
					timeDistance += 366;
				}
				else    //不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2-day1) ;
		}
		else    //不同年
		{
			return day2-day1;
		}
	}
	public static int getYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	public static String getWeekDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		String[] weekDays = {"日","一","二","三","四","五","六"};
		return weekDays[weekDay-1];
	}
	public static int getMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}
	/** 
	 * 得到指定月的天数 
	 * */  
	public static int getMonthLastDay(int year, int month)  
	{  
	    Calendar a = Calendar.getInstance();  
	    a.set(Calendar.YEAR, year);  
	    a.set(Calendar.MONTH, month - 1);  
	    a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
	    a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	    int maxDate = a.get(Calendar.DATE);  
	    return maxDate;  
	}  
}
