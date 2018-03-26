package com.statistic.writeFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.statistic.readFile.DatabaseOperate;
import com.statistic.readFile.RecordsUtil;
import com.statistic.time.TimeUtil;

public class WriteCVS {
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	private String path = "E:/StatisticsSystem/ProcessedFile/org"; 
	private String insertPath ="E:/StatisticsSystem/ProcessedFile";
	public void writeData() {
		String queryRecords = "select distinct(file_name) from records";
		ArrayList<String> fileList = dbo.queryString(queryRecords);
		for (String fileName: fileList) {
			if (null != fileName && !"".equals(fileName) && !"null".equals(fileName))
			createCSV(fileName);
		}
		getInsertRecords();
		System.out.println("writeComplete");
	}
	
	private void getInsertRecords() {
		String sql =  "select * from records where records.insert = 1";
		BufferedWriter writer = null;
		ArrayList<RecordsUtil> rowList = new ArrayList<RecordsUtil>();
		rowList = dbo.queryRecords(sql);
		File csvFile = null;
		csvFile = new File(insertPath,"insert.csv");
		File parent = csvFile.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		try {
			csvFile.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),"utf-8"), 1024);
			if (rowList.size() > 0) {
				for (RecordsUtil ru: rowList) {
					writer.write(writeRow(ru)+", " + ru.file_name);
					writer.newLine();
					writer.flush();
				}
			}
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createCSV(String fileName) {
		BufferedWriter writer = null;
		ArrayList<RecordsUtil> rowList = new ArrayList<RecordsUtil>();
		ArrayList<String> headList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		String stringRow = "";
		if (fileName.contains("BeiJing")) {
			System.out.println("BeiJing");
			headList = getHeadList();
			System.out.println("headList.size ="+headList.size());
		}
		
		String sql =  "select * from records where file_name = '"+fileName+"' and records.insert = 0 ";
		rowList = dbo.queryRecords(sql);
		File csvFile = null;
		csvFile = new File(path,fileName);
		File parent = csvFile.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		try {
			csvFile.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),"utf-8"), 1024);
			if (headList.size() > 0) {
				for (String str: headList) {
					stringRow += str;
				}
				writer.write(stringRow);
				writer.newLine();
				writer.flush();
			}
			System.out.println("rowList.size() ="+rowList.size());
			if (rowList.size() > 0) {
				for (RecordsUtil ru: rowList) {
					writer.write(writeRow(ru));
					writer.newLine();
					writer.flush();
				}
			}
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String writeRow(RecordsUtil ru) {
		String stringRow = "";
		if (null != ru.serial && !"".equals(ru.serial) && !"null".equals(ru.serial)) {
			stringRow += ru.serial;
		}
		
		if (null != ru.id_card && !"".equals(ru.id_card) && !"null".equals(ru.id_card)) {
			stringRow += ", " + ru.id_card;
		}
		if (null != ru.name && !"".equals(ru.name) && !"null".equals(ru.name)) {
			stringRow += ", " + ru.name;
		}
		if (null != ru.team_name && !"".equals(ru.team_name) && !"null".equals(ru.team_name)) {
			stringRow += ", " + ru.team_name;
		}
		if (null != ru.datetime && !"".equals(ru.datetime) && !"null".equals(ru.datetime)) {
			stringRow += ", " + TimeUtil.DateToString1(TimeUtil.StringToDate(ru.datetime));
		}
		if (null != ru.site && !"".equals(ru.site) && !"null".equals(ru.site)) {
			stringRow += ", " + ru.site;
		}
		if (null != ru.pass && !"".equals(ru.pass) && !"null".equals(ru.pass)) {
			stringRow += ", " + ru.pass;
		}
		if (null != ru.description && !"".equals(ru.description) && !"null".equals(ru.description)) {
			stringRow += ", " + ru.description;
		}
		return stringRow;
	}
	
	private ArrayList<String> getHeadList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("序号, ");
		arrayList.add("卡号, ");
		arrayList.add("姓名, ");
		arrayList.add("部门班组, ");
		arrayList.add("时间, ");
		arrayList.add("地点, ");
		arrayList.add("通过, ");
		arrayList.add("描述");
		return arrayList;
	}

}
