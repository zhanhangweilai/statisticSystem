package com.statistic.writeFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
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

public class WriteFile {
	
	private String directorFile = "E:/StatisticsSystem/ProcessedFile/a.xlsx";
	private XSSFWorkbook mXSSFWorkbook;
	private XSSFSheet mXSSFSheet1;
	private XSSFSheet mXSSFSheet2;
	private XSSFSheet mXSSFSheet3;
	private XSSFSheet mXSSFSheet4;
	private XSSFSheet mXSSFSheet5;
	private XSSFSheet mXSSFSheet6;
	
	public void mainWrite() {
		mXSSFWorkbook = new XSSFWorkbook();
		mXSSFSheet1 = mXSSFWorkbook.createSheet();
		mXSSFWorkbook.setSheetName(0, "考勤数据分析");
		mXSSFSheet2 = mXSSFWorkbook.createSheet();
		mXSSFWorkbook.setSheetName(1, "考勤数据表");
		mXSSFSheet3 = mXSSFWorkbook.createSheet();
		mXSSFWorkbook.setSheetName(2, "基础考勤");
		mXSSFSheet4 = mXSSFWorkbook.createSheet();
		mXSSFWorkbook.setSheetName(3, "考勤异常-5");
		mXSSFSheet5 = mXSSFWorkbook.createSheet();
		mXSSFWorkbook.setSheetName(4, "Onsite_考勤");
		mXSSFSheet6 = mXSSFWorkbook.createSheet();
		mXSSFWorkbook.setSheetName(5, "异常数据校对-6");
		
		WriteSheet1 writeSheet1 = new WriteSheet1(mXSSFWorkbook, mXSSFSheet1);
		writeSheet1.writeSheet();
		
		WriteSheet2 writeSheet2 = new WriteSheet2(mXSSFWorkbook, mXSSFSheet2);
		writeSheet2.writeSheet();
		
		WriteSheet3 writeSheet3 = new WriteSheet3(mXSSFWorkbook, mXSSFSheet3);
		writeSheet3.writeSheet();
		
		WriteSheet4 writeSheet4 = new WriteSheet4(mXSSFWorkbook, mXSSFSheet4);
		writeSheet4.writeSheet();
		
		try {
			FileOutputStream fileOut = new FileOutputStream(directorFile); 
			mXSSFWorkbook.write(fileOut);
			fileOut.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 
		System.out.println("WriteComplete");
	}
}
