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
import com.statistic.time.MathUtil;
import com.statistic.time.TimeUtil;

import bean.PersonOfMonth;

public class WriteSheet2 {
	
	private XSSFWorkbook mXSSFWorkbook;
	private XSSFSheet mXSSFSheet;
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	private XSSFCellStyle style, style1;
	
	public WriteSheet2(XSSFWorkbook xSSFWorkbook, XSSFSheet xSSFSheet) {
		mXSSFWorkbook = xSSFWorkbook;
		mXSSFSheet = xSSFSheet;
	}
	
	public void writeSheet() {
		XSSFDataFormat df = mXSSFWorkbook.createDataFormat();
		style = getStyle(mXSSFWorkbook);
		style1 = getStyle(mXSSFWorkbook);
		style1.setDataFormat(df.getFormat("##0.0"));
		writeTitle();
		getData();
	}
	private void writeTitle() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("No.");
		arrayList.add("��Ա����");
		arrayList.add("Site");
		arrayList.add("����");
		arrayList.add("����ģ��");
		arrayList.add("��Ա�ӿ�");
		arrayList.add("��ʼ��");
		arrayList.add("��ֹ��");
		arrayList.add("�Ƿ�onsite");
		arrayList.add("�Ƿ��п���");
		arrayList.add("��������");
		arrayList.add("���������ճ���");
		arrayList.add("Ӧ��������");
		arrayList.add("�����쳣");
		arrayList.add("��ĩ����");
		arrayList.add("�ܼӰ�");
		arrayList.add("ƽ�ռӰ�");
		XSSFRow row = mXSSFSheet.createRow(0); 
		XSSFCellStyle style = getStyle(mXSSFWorkbook);
		style.setFillForegroundColor(HSSFColorPredefined.GREEN.getIndex());
		// ����һ������  
		XSSFFont font = mXSSFWorkbook.createFont();  
		font.setColor(HSSFColorPredefined.BLACK.getIndex()); 
		font.setBold(true);
		//�����������ڵ��и߶�  
		font.setFontHeightInPoints((short) 11);  
		font.setFontName("΢���ź�");
		// ������Ӧ�õ���ǰ����ʽ  
		style.setFont(font);  
		for (int i = 0; i < arrayList.size(); i++) {
			if (i == 3 || i == 4 || i == 6 || i == 7  || i == 10 || i == 11 ) {
				mXSSFSheet.setColumnWidth(i, 23 * 256);
			} else if (i == 5 || i == 8 || i == 9) {
				mXSSFSheet.setColumnWidth(i, 18 * 256);
			}
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(); 
			cell.setCellStyle(style);
			text.setString(arrayList.get(i));
			cell.setCellValue(text.toString());
		}
	}
	
	private void getData () {
		ArrayList<PersonOfMonth> arrayList = new ArrayList< >();
		String sql = "select person.name, person.place, person.team, person.start_time, person.end_time, person.onsite, month_person.actually_day_num,"
				+ " month_person.workday_work_num, month_person.workday_num, month_person.absence_day, month_person.weekend_work_num, "
				+ "month_person.total_overtime, month_person.workday_overtime, month_person.weekend_overtime from month_person, person "
				+ "where month_person.name = person.name order by person.place desc";
		arrayList =  dbo.queryPersonOfMonth(sql);
		for (int i = 0; i < arrayList.size(); i++) {
			PersonOfMonth pom = arrayList.get(i);
			XSSFRow row  = mXSSFSheet.createRow(i+1);
			drawRow(row, pom);
		}
	}
	
	public void drawRow(XSSFRow row, PersonOfMonth pom) {
		XSSFRichTextString text = new XSSFRichTextString(); 
		for (int i = 0;i < 17; i++) {
			XSSFCell cell = row.createCell(i);
			switch (i) {
				case 0:
					cell.setCellStyle(style);
					break;
				
				case 1:
					cell.setCellStyle(style);
					text.setString(pom.personName);
					cell.setCellValue(text.toString());
					break;
					
				case 2:
					cell.setCellStyle(style);
					text.setString(pom.place);
					cell.setCellValue(text.toString());
					break;
					
				case 3:
					cell.setCellStyle(style);
					text.setString(pom.team);
					cell.setCellValue(text.toString());
					break;
					
				case 4:
					cell.setCellStyle(style);
					break;
					
				case 5:
					cell.setCellStyle(style);
					break;
					
				case 6:
					cell.setCellStyle(style);
					text.setString(TimeUtil.formatStringToStringWithoutTime(pom.startTime));
					cell.setCellValue(text.toString());
					break;
					
				case 7:
					cell.setCellStyle(style);
					text.setString(TimeUtil.formatStringToStringWithoutTime(pom.endTime));
					cell.setCellValue(text.toString());
					break;
					
				case 8:
					String isOnsite;
					if (pom.onsite == 1) {
						isOnsite = "��";
					} else {
						isOnsite = "��";
					}
					cell.setCellStyle(style);
					text.setString(isOnsite);
					cell.setCellValue(text.toString());
					break;
					
				case 9:
					cell.setCellStyle(style);
					text.setString(pom.personName);
					cell.setCellValue(text.toString());
					break;
					
				case 10:
					cell.setCellStyle(style1);
					float actuallyWorkdayNum = MathUtil.floatParseOne(pom.actuallyWorkdayNum);
					cell.setCellValue(actuallyWorkdayNum);
					break;
					
				case 11:
					cell.setCellStyle(style1);
					float workdayWorkNum = MathUtil.floatParseOne(pom.workdayWorkNum);
					cell.setCellValue(workdayWorkNum);
					break;
					
				case 12:
					cell.setCellStyle(style1);
					cell.setCellValue(pom.shouldWorkNum);
					break;
					
					
				case 13:
					cell.setCellStyle(style1);
					float absenceDayNum = MathUtil.floatParseOne(pom.absenceDayNum);
					cell.setCellValue(absenceDayNum);
					break;
					
				case 14:
					cell.setCellStyle(style1);
					float weekendWorkNum = MathUtil.floatParseOne(pom.weekendWorkNum);
					cell.setCellValue(weekendWorkNum);
					break;
					
				case 15:
					cell.setCellStyle(style);
					float totalOverTime = MathUtil.floatParseOne(pom.totalOverTime);
					cell.setCellValue(totalOverTime);
					break;
				case 16:
					cell.setCellStyle(style1);
					float normalOverTime = MathUtil.floatParseOne(pom.normalOverTime);
					cell.setCellValue(normalOverTime);
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
		
		// ����һ������  
		XSSFFont font = workBook.createFont();  
		font.setColor(HSSFColorPredefined.BLACK.getIndex()); 
		//�����������ڵ��и߶�  
		font.setFontHeightInPoints((short) 11);  
		font.setFontName("΢���ź�");
		// ������Ӧ�õ���ǰ����ʽ  
		style.setFont(font);  
		// ָ������Ԫ��������ʾ����ʱ�Զ�����  
		style.setWrapText(true);  
		return style;
	}
}
