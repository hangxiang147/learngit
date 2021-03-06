package com.zhizaolian.staff.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

public class PoiUtil {
	@SuppressWarnings("deprecation")
	public static String[] getCellValue(Cell cell){
		String value = "";
		String[] typeAndVal = new String[2];
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: // 数字
			//如果为时间格式的内容
			if (HSSFDateUtil.isCellDateFormatted(cell)) {      
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				value=sdf.format(HSSFDateUtil.getJavaDate(cell.
						getNumericCellValue())).toString();
				typeAndVal[0] = "时间";
				typeAndVal[1] = value; 
				break;
			} else {
				value = new DecimalFormat("0").format(cell.getNumericCellValue());
				typeAndVal[0] = "数字";
				typeAndVal[1] = value;
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // 字符串
			value = cell.getStringCellValue();
			typeAndVal[0] = "字符串";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
			value = cell.getBooleanCellValue() + "";
			typeAndVal[0] = "布尔";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 公式
			value = cell.getCellFormula() + "";
			typeAndVal[0] = "公式";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_BLANK: // 空值
			value = "";
			typeAndVal[0] = "空值";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 故障
			value = "非法字符";
			typeAndVal[0] = "故障";
			typeAndVal[1] = value;
			break;
		default:
			value = "未知类型";
			break;
		}
		return typeAndVal;
	}
	
	
	@SuppressWarnings("deprecation")
	public static String[] getCellValue_(Cell cell){
		String value = "";
		String[] typeAndVal = new String[2];
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: // 数字
			//如果为时间格式的内容
			if (HSSFDateUtil.isCellDateFormatted(cell)) {      
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
				value=sdf.format(HSSFDateUtil.getJavaDate(cell.
						getNumericCellValue())).toString();
				typeAndVal[0] = "时间";
				typeAndVal[1] = value;
				break;
			} else {
				value = new DecimalFormat("0.00").format(cell.getNumericCellValue());
				typeAndVal[0] = "数字";
				typeAndVal[1] = value;
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // 字符串
			value = cell.getStringCellValue();
			typeAndVal[0] = "字符串";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
			value = cell.getBooleanCellValue() + "";
			typeAndVal[0] = "布尔";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 公式
			value = cell.getCellFormula() + "";
			typeAndVal[0] = "公式";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_BLANK: // 空值
			value = "";
			typeAndVal[0] = "空值";
			typeAndVal[1] = value;
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 故障
			value = "非法字符";
			typeAndVal[0] = "故障";
			typeAndVal[1] = value;
			break;
		default:
			value = "未知类型";
			break;
		}
		return typeAndVal;
	}
}
