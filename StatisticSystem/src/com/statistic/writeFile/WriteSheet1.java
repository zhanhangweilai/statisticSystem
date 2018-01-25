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

import com.statistic.time.MathUtil;

import bean.TeamBean;

public class WriteSheet1 {
	private XSSFWorkbook mXSSFWorkbook;
	private XSSFSheet mXSSFSheet1;
	private int mRowNum = 0;
	private XSSFCellStyle style, style1, style2;
	public WriteSheet1 (XSSFWorkbook xSSFWorkbook, XSSFSheet xSSFSheet) {
		mXSSFWorkbook = xSSFWorkbook;
		mXSSFSheet1 = xSSFSheet;
	}
	public void writeSheet(){
		XSSFDataFormat df = mXSSFWorkbook.createDataFormat();
		style = getStyle(mXSSFWorkbook);
		style1 = getStyle(mXSSFWorkbook);
		style1.setDataFormat(df.getFormat("##0.00"));
		style2 = getStyle(mXSSFWorkbook);
		style2.setDataFormat(df.getFormat("##0.0"));
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("出勤数据分析");
		arrayList.add("offshore人数");
		arrayList.add("应出勤人天");
		arrayList.add("出勤人天");
		arrayList.add("平日出勤");
		arrayList.add("周末出勤");
		arrayList.add("考勤异常人天");
		arrayList.add("平日出勤+考勤异常-应出勤人天");
		arrayList.add("平日+周末-出勤");
		arrayList.add("check");
		TotalWorkUtil twu = new TotalWorkUtil();
		for (int i = 0; i < 10; i++) {
			if (i ==7) {
				mXSSFSheet1.setColumnWidth(i, 28 * 256);
				continue;
			}
			mXSSFSheet1.setColumnWidth(i, 18 * 256);
		}
		int num = twu.getPlaceNum();
		ArrayList<String> placeList = new ArrayList<String>();
		placeList = twu.getPlace();
		
		XSSFRow row = mXSSFSheet1.createRow(0);  
		drawRowTitle(row, 10, style, arrayList);
		for(int i = 1; i <= num + 1; i++) {
			if(i != num + 1) {
				arrayList = twu.getWorkDay(placeList.get(i-1));
			} else {
				arrayList = twu.getTotalDay();
			}
			row = mXSSFSheet1.createRow(i);
			drawRow(row, 10, arrayList);
		}
		mRowNum = num + 1;
		
		//空6行
		for (int i = 1; i <= 6; i++) {
			mXSSFSheet1.createRow(i + mRowNum);
		}
		mRowNum += 6;
		
		//加班数据
		row = mXSSFSheet1.createRow(mRowNum + 1);  
		mRowNum++;
		arrayList = twu.getOverTimeTitle();
		drawRowTitle(row, 8, style, arrayList);
		for (int i = 1; i <= num + 1; i++) {
			if(i != num + 1) {
				arrayList = twu.getOverTime(placeList.get(i-1));
			} else {
				arrayList = twu.getTotalOverTime();
			}
			row = mXSSFSheet1.createRow(i + mRowNum);
			drawRow(row, 8, arrayList);
		}
		mRowNum += (num+1);
		
		//空6行
		for (int i = 1; i <= 6; i++) {
			mXSSFSheet1.createRow(i + mRowNum);
		}
		mRowNum += 6;
		
		//考勤异常
		row = mXSSFSheet1.createRow(mRowNum + 1);  
		mRowNum++;
		arrayList = twu.getAbsenceTitle();
		drawRowTitle(row, 10, style, arrayList);
		for (int i = 1; i <= num + 1; i++) {
			if(i != num + 1) {
				arrayList = twu.getAbsenceData(placeList.get(i-1));
			} else {
				arrayList = twu.getTotalAbsence();
			}
			row = mXSSFSheet1.createRow(i + mRowNum);
			drawRow(row, 10, arrayList);
		}
		mRowNum += (num+1);
		
		//空6行
		for (int i = 1; i <= 6; i++) {
			mXSSFSheet1.createRow(i + mRowNum);
		}
		mRowNum += 6;
		
		//加班
		row = mXSSFSheet1.createRow(mRowNum + 1); 
		arrayList = twu.getTitleOverTimeOfTeam();
		drawRowTitle(row, 8, style, arrayList);
		mRowNum++;
		num = twu.getTeamNum();
		TeamBean teamBean;
		ArrayList<TeamBean> teamList;
		teamList = twu.getOverTimeOfTeamData();
		for (int i = 1; i <= num + 1; i++) {
			if(i != num + 1) {
				System.out.println("excute 2");
				teamBean = teamList.get(i - 1);
			} else {
				System.out.println("excute 1");
				teamBean = twu.getTotalOverTimeOfTeamData();
			}
			row = mXSSFSheet1.createRow(i + mRowNum);
			drawRow1(row, 8, teamBean);
		}
		
	}
	
	public void drawRowTitle(XSSFRow row ,int n, XSSFCellStyle style, ArrayList<String> arrayList) {
		for (int i = 0; i < n; i++) {  
			XSSFCell cell = row.createCell((short) i);
			cell.setCellStyle(style);  
			XSSFRichTextString text = new XSSFRichTextString();  
			text.setString(arrayList.get(i));
			cell.setCellValue(text.toString());
		}
	}
	public void drawRow(XSSFRow row ,int n, ArrayList<String> arrayList) {
		for (int j = 0; j < n; j++) {  
			XSSFCell cell = row.createCell((short) j);
			XSSFRichTextString text = new XSSFRichTextString();  
			if (j == 0) {
				cell.setCellStyle(style);
				text.setString(arrayList.get(j));
				cell.setCellValue(text.toString());
			}else {
				if (pointNum(arrayList.get(j)) >= 1) {
					cell.setCellStyle(style2);
					text.setString(arrayList.get(j));
					cell.setCellValue(Float.parseFloat(text.toString()));
					System.out.println(Float.parseFloat(text.toString()));
				}else {
					cell.setCellStyle(style);
					text.setString(arrayList.get(j));
					cell.setCellValue(Integer.parseInt(text.toString()));
				}
			}
		} 
	}
	
	public void drawRow1(XSSFRow row ,int n, TeamBean teamBean) {
		XSSFRichTextString text = new XSSFRichTextString();  
		for (int j = 0; j < n; j++) {  
			XSSFCell cell = row.createCell((short) j);
			switch (j) {
				case 0:
					cell.setCellStyle(style);
					text.setString(teamBean.teamName);
					cell.setCellValue(text.toString());
					break;
				
				case 1:
					cell.setCellStyle(style);
					cell.setCellValue(teamBean.personNum);
					break;
				
				case 2:
					cell.setCellStyle(style);
					int totalOverTime = MathUtil.floatParseInt(teamBean.totalOverTime);
					cell.setCellValue(totalOverTime);
					break;
				
				case 3:
					cell.setCellStyle(style);
					int normalDayOverTime = MathUtil.floatParseInt(teamBean.normalDayOverTime);
					cell.setCellValue(normalDayOverTime);
					break;
				
				case 4:
					cell.setCellStyle(style);
					int weekendOVerTime = MathUtil.floatParseInt(teamBean.weekendOVerTime);
					cell.setCellValue(weekendOVerTime);
					break;
				
				case 5:
					cell.setCellStyle(style);
					int actuallyWorkdayNum = MathUtil.floatParseInt(teamBean.actuallyWorkdayNum);
					cell.setCellValue(actuallyWorkdayNum);
					break;
				
				case 6:
					cell.setCellStyle(style1);
					float averDayOverTime = MathUtil.floatParseTwo(teamBean.averDayOverTime);
					System.out.println("averDayOverTime = "+teamBean.averDayOverTime);
					cell.setCellValue(averDayOverTime);
					break;
				
				case 7:
					cell.setCellStyle(style1);
					float averPersonOverTime = MathUtil.floatParseTwo(teamBean.averPersonOverTime);
					cell.setCellValue(averPersonOverTime);
					break;
				
				default:
					break;
			}
		} 
	}
	
	public int pointNum (String string) {
		int num = 0;
		int start = string.indexOf(".");
		if (start != -1) {
			String subString = string.substring(start+1, string.length());
			num = subString.length();
		}
		
		return num;
	}

	public XSSFCellStyle getStyle(XSSFWorkbook workBook) {
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
