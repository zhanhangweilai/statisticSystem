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
import com.statistic.time.TimeUtil;

import bean.LeavesOfPerson;

public class WriteSheet4 {
	private XSSFWorkbook mXSSFWorkbook;
	private XSSFSheet mXSSFSheet;
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	private XSSFCellStyle style, style1;
	
	public WriteSheet4(XSSFWorkbook xSSFWorkbook, XSSFSheet xSSFSheet) {
		mXSSFWorkbook = xSSFWorkbook;
		mXSSFSheet = xSSFSheet;
	}
	
	
	public void writeSheet() {
		XSSFDataFormat df = mXSSFWorkbook.createDataFormat();
		style = getStyle(mXSSFWorkbook);
		style1 = getStyle(mXSSFWorkbook);
		style1.setDataFormat(df.getFormat("##0.0"));
		writeTitle();
		writeData();
	}
	private void writeTitle() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("姓名");
		arrayList.add("组织");
		arrayList.add("部门");
		arrayList.add("休假类别");
		arrayList.add("休假开始时间");
		arrayList.add("休假结束时间");
		arrayList.add("休假时长");
		XSSFRow row = mXSSFSheet.createRow(0); 
		XSSFCellStyle style = getStyle(mXSSFWorkbook);
		for (int i = 0; i < arrayList.size() + 3; i++) {
			if (i == 1 || i == 4 || i == 5) {
				mXSSFSheet.setColumnWidth(i, 23 * 256);
			}
			if (i == 3 || i == 9) {
				mXSSFSheet.setColumnWidth(i, 20 * 256);
			}
			if (i < arrayList.size()) {
				XSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				XSSFRichTextString text = new XSSFRichTextString(); 
				cell.setCellStyle(style);
				text.setString(arrayList.get(i));
				cell.setCellValue(text.toString());
			}
		}
	}
	
	private void writeData() {
		ArrayList<LeavesOfPerson> arrayList = new ArrayList<>();
		ArrayList<LeavesOfPerson> arrayListTotal = new ArrayList<>();
		String sql = "select leaves_day.name, person.place, leaves_day.category, leaves_day.leave_start_time, leaves_day.leave_end_time, "
				+ "leaves_day.total_day from leaves_day, person where leaves_day.name = person.name";
		arrayList = dbo.queryLeavesOfPerson(sql);
		arrayListTotal = getTotalAbsence();
		for (int i = 0; i < arrayList.size(); i++) {
			XSSFRow row = mXSSFSheet.createRow(i+1);
			LeavesOfPerson lop = new LeavesOfPerson();
			lop = arrayList.get(i);
			drawRow(row, lop);
			if (i == 2) {
				writeAbsenceTile(row);
			} else if (i > 2 && i < arrayListTotal.size() + 3) {
				lop.clear();
				lop = arrayListTotal.get(i - 3);
				writeAbsence(row, lop);
			} else if (i == arrayListTotal.size() + 3) {
				lop.clear();
				lop.name = "总计";
				for (int j = 0; j < arrayListTotal.size(); j++){
					lop.leaveDay += arrayListTotal.get(j).leaveDay;
				}
				writeAbsence(row, lop);
			}
		}
	}

	private void drawRow(XSSFRow row, LeavesOfPerson lop){
		String strDate;
		XSSFRichTextString text = new XSSFRichTextString(); 
		for (int i = 0; i < 7; i++) {
			XSSFCell cell = row.createCell(i);
			switch (i) {
				case 0:
					cell.setCellStyle(style);
					text.setString(lop.name);
					cell.setCellValue(text);
					break;
				case 1:
					cell.setCellStyle(style);
					text.setString("中科创达股份有限公司");
					cell.setCellValue(text);
					break;
					
				case 2:
					cell.setCellStyle(style);
					text.setString(lop.place);
					cell.setCellValue(text);
					break;
					
				case 3:
					cell.setCellStyle(style);
					text.setString(lop.category);
					cell.setCellValue(text);
					break;
					
				case 4:
					strDate = TimeUtil.formatStringToString(lop.startTime);
					cell.setCellStyle(style);
					text.setString(strDate);
					cell.setCellValue(text);
					break;
					
				case 5:
					strDate = TimeUtil.formatStringToString(lop.endTime);
					cell.setCellStyle(style);
					text.setString(strDate);
					cell.setCellValue(text);
					break;
					
				case 6:
					cell.setCellStyle(style1);
					cell.setCellValue(lop.leaveDay);
					break;
					
				default:
					break;
			}
		}
	}
	
	private void writeAbsence(XSSFRow row, LeavesOfPerson lop) {
		XSSFRichTextString text = new XSSFRichTextString(); 
		for (int i = 0; i < 2; i++) {
			XSSFCell cell = row.createCell(i+8);
			switch (i) {
				case 0:
					cell.setCellStyle(style);
					text.setString(lop.name);
					cell.setCellValue(text);
					break;
				case 1:
					cell.setCellStyle(style1);
					cell.setCellValue(lop.leaveDay);
					break;
			}
		}
	}
	
	private void writeAbsenceTile(XSSFRow row) {
		XSSFRichTextString text = new XSSFRichTextString(); 
		System.out.println("excue");
		for (int i = 0; i <2; i++) {
			XSSFCell cell = row.createCell(i+8);
			switch (i) {
				case 0:
					cell.setCellStyle(style);
					text.setString("姓名");
					cell.setCellValue(text);
					break;
					
				case 1:
					cell.setCellStyle(style);
					text.setString("休假时长");
					cell.setCellValue(text);
					break;
					
				default:
					break;
			}
		}
	}
	
	private ArrayList<LeavesOfPerson> getTotalAbsence() {
		ArrayList<LeavesOfPerson> arrayList = new ArrayList<LeavesOfPerson>();
		String sql = "select name, absence_day from month_person where absence_day > 0 ";
		arrayList = dbo.queryTotalAbsence(sql);
		return arrayList;
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
