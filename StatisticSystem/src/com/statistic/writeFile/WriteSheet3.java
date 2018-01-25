package com.statistic.writeFile;

import java.util.ArrayList;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.statistic.readFile.DatabaseOperate;
import com.statistic.readFile.DayTableBeen;
import com.statistic.time.TimeUtil;

public class WriteSheet3 {
	
	private XSSFWorkbook mXSSFWorkbook;
	private XSSFSheet mXSSFSheet;
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	private XSSFCellStyle style,style1,style2;
	
	public WriteSheet3(XSSFWorkbook xSSFWorkbook, XSSFSheet xSSFSheet) {
		mXSSFWorkbook = xSSFWorkbook;
		mXSSFSheet = xSSFSheet;
	}
	
	
	public void writeSheet() {
		XSSFDataFormat df = mXSSFWorkbook.createDataFormat();
		style = getStyle(mXSSFWorkbook);
		style1 = getStyle(mXSSFWorkbook);
		style1.setDataFormat(df.getFormat("##0.00"));
		style2 = getStyle(mXSSFWorkbook);
		style2.setDataFormat(df.getFormat("##0.0"));
		writeTitle();
		writeData();
	}
	
	private void writeTitle() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("ts");
		arrayList.add("Site");
		arrayList.add("日期");
		arrayList.add("上班时间");
		arrayList.add("下班时间");
		arrayList.add("工作日/周末");
		arrayList.add("工作时长");
		arrayList.add("加班时间");
		arrayList.add("出勤日");
		XSSFRow row = mXSSFSheet.createRow(0); 
		XSSFCellStyle style = getStyle(mXSSFWorkbook);
		for (int i = 0; i < arrayList.size(); i++) {
			if (i > 1 && i <= 5) {
				mXSSFSheet.setColumnWidth(i, 23 * 256);
			}
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(); 
			cell.setCellStyle(style);
			text.setString(arrayList.get(i));
			cell.setCellValue(text.toString());
		}
	 }
	 
	 private void writeData() {
		 ArrayList<DayTableBeen> arrayList = new ArrayList<DayTableBeen>();
		 String sql = "select dayperson.name, person.place, dayperson.dateDay, dayperson.startTime, dayperson.endtime, dayperson.category,"
		 		+ " dayperson.work_time, dayperson.over_time from dayperson, person where dayperson.name = person.name order by person.place";
		 arrayList = dbo.queryPersonOfDay(sql);
		 
		 for (int i = 0; i < arrayList.size(); i++) {
			 DayTableBeen dtb = new DayTableBeen();
			 dtb = arrayList.get(i);
			 XSSFRow row = mXSSFSheet.createRow(i+1);
			 drawRow(row, dtb);
		 }
	 }
 
	 private void drawRow(XSSFRow row, DayTableBeen dtb) {
		 XSSFRichTextString text = new XSSFRichTextString();
		 for (int i = 0; i < 9; i++) {
			XSSFCell cell = row.createCell(i);
			switch (i) {
				case 0:
					cell.setCellStyle(style);
					text.setString(dtb.name);
					cell.setCellValue(text);
					break;
				case 1:
					cell.setCellStyle(style);
					text.setString(dtb.place);
					cell.setCellValue(text);
					break;
				case 2:
					cell.setCellStyle(style);
					text.setString(TimeUtil.DateToStringWithoutTime(dtb.date));
					cell.setCellValue(text);
					break;
				case 3:
					cell.setCellStyle(style);
					text.setString(TimeUtil.formatStringToString(dtb.strStartTime));
					cell.setCellValue(text);
					break;
				case 4:
					cell.setCellStyle(style);
					text.setString(TimeUtil.formatStringToString(dtb.strEndTime));
					cell.setCellValue(text);
					break;
				case 5:
					cell.setCellStyle(style);
					if (dtb.category == 1) {
						text.setString("工作日");
						cell.setCellValue(text);
					} else {
						text.setString("周末");
						cell.setCellValue(text);
					}
					break;
				case 6:
					cell.setCellStyle(style1);
					cell.setCellValue(dtb.work_time);
					break;
				case 7:
					cell.setCellStyle(style1);
					cell.setCellValue(dtb.over_time);
					break;
				case 8:
					float workDay;
					if (dtb.category == 1 && dtb.work_time < 9 && dtb.work_time >= 4) {
						cell.setCellStyle(style2);
						cell.setCellValue(0.5f);
					} else if (dtb.category == 1 && dtb.work_time < 4) {
						cell.setCellStyle(style);
						cell.setCellValue("FALSE");
					} else {
						cell.setCellStyle(style);
						cell.setCellValue(1);
					}
					break;
				
				default:
					break;
			}
		 }
		 
	 } 
	  
	 private XSSFCellStyle getStyle(XSSFWorkbook workBook) {
		XSSFCellStyle style = workBook.createCellStyle();
		style.setFillForegroundColor(HSSFColorPredefined.WHITE.getIndex());  
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
		style.setBorderBottom(BorderStyle.THIN);  
		style.setBorderLeft(BorderStyle.THIN);  
		style.setBorderRight(BorderStyle.THIN);  
		style.setBorderTop(BorderStyle.THIN);  
		style.setAlignment(HorizontalAlignment.CENTER );
		
		// 生成一个字体  
		XSSFFont font = workBook.createFont();  
		font.setColor(HSSFColorPredefined.BLACK.getIndex()); 
		//设置字体所在的行高度  
		font.setFontHeightInPoints((short) 11);  
		font.setFontName("微软雅黑");
		// 把字体应用到当前的样式  
		style.setFont(font);  
		// 指定当单元格内容显示不下时自动换行  
		style.setWrapText(true);  
		return style;
	}
}
