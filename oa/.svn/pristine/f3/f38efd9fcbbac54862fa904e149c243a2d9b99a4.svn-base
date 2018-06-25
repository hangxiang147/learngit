package com.zhizaolian.staff.utils;

import java.math.BigDecimal;

public class NumberUtil {
	/**
	 * 四舍五入
	 * @param number
	 * @param index 保留小数的位数
	 * @return
	 */
	public static double Rounding(double number, int index){
		BigDecimal b = new BigDecimal(number);
		number = b.setScale(index, BigDecimal.ROUND_HALF_UP).doubleValue();
		return number;
	}
	public static boolean isNum(String str){
		String reg = "^([0-9]+\\.[0-9]+)|([0-9]+)$";
		return str.matches(reg);
	}
}
