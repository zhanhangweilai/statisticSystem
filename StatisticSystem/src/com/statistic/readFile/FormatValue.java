package com.statistic.readFile;

import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class FormatValue {
	/**
	 * 将读取的xlsx格式Excel表数据转换为适合的类型并最终转换为字符串型
	 * @param xssfCell得到的Excel表单元格的数据
	 * @return
	 */
	public static String getXLSXValue (XSSFCell xssfCell) {
		switch (xssfCell.getCellTypeEnum()) {
			case BOOLEAN :
				return String.valueOf(xssfCell.getBooleanCellValue());
			case NUMERIC :
				if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                return sdf.format(HSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue()));
				}
				return String.valueOf(xssfCell.getNumericCellValue());
			case STRING :
				return String.valueOf(xssfCell.getStringCellValue());
			case FORMULA :
				try {
					return String.valueOf(xssfCell.getNumericCellValue());
				} catch (IllegalArgumentException e) {
					System.out.print("a");
					return String.valueOf(xssfCell.getRichStringCellValue());
				}
			default:
				System.out.print(xssfCell.getCellTypeEnum());
				return "null";
		}
	}
	
	/**
	 * 将读取的xls格式Excel表数据转换为适合的类型并最终转换为字符串型
	 * @param xssfCell 得到的Excel表单元格的数据
	 * @return
	 */
	public static String getXLSValue (HSSFCell hssfCell) {
		switch (hssfCell.getCellTypeEnum()) {
			case BOOLEAN :
				return String.valueOf(hssfCell.getBooleanCellValue());
			case NUMERIC :
				if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                return sdf.format(HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()));
				}
				return String.valueOf(hssfCell.getNumericCellValue());
			case STRING :
				return String.valueOf(hssfCell.getStringCellValue());
			case FORMULA :
				try {
					return String.valueOf(hssfCell.getNumericCellValue());
				} catch (IllegalArgumentException e) {
					System.out.print("a");
					return String.valueOf(hssfCell.getRichStringCellValue());
				}
			default:
				System.out.print(hssfCell.getCellTypeEnum());
				return "null";
		}
	}
}
