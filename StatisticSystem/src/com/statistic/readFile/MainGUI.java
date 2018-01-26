package com.statistic.readFile;

import java.io.File;
import java.util.ArrayList;

import com.statistic.writeFile.WriteCVS;
import com.statistic.writeFile.WriteFile;

import jxl.write.biff.WritableFormattingRecords;



public class MainGUI {
	public static void main (String[] arg0) {
		String origin_file_path2 = "E:/StatisticsSystem/OriginFile";
		String origin_file_path1 = "E:/StatisticsSystem/OriginFile/日期修改.txt";
		ReadOriginFile readOriginFile = new ReadOriginFile(); 
		System.out.println("kaishi");
		StatisticOneDay oneDay = new StatisticOneDay();
		DatabaseOperate dbo = DatabaseOperate.getInstance();
//		dbo.connectDatabase();
//		dbo.createTable();
		WriteFile writeFile = new WriteFile();
		StatisticMonthPerson smp = new StatisticMonthPerson();
		
		TeamUtils tu = new TeamUtils();
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(origin_file_path1);
				//readOriginFile.readTXT(file);
				ArrayList<String> fileList = new ArrayList<String>();
				fileList = (ArrayList<String>) readOriginFile.getFilePath(origin_file_path2, fileList);
				for (String filePath : fileList) {
					readOriginFile.readFile(filePath);
				}
				System.out.println("readAllComplete");
				oneDay.statisticPersonOfDay();
				smp.processMonthPerson();
				tu.statisticTeam("北京"); 
				System.out.println("ProcessComplete");
				writeFile.mainWrite();
				ModifyRecords mr = new ModifyRecords();
				mr.getExceptionData();
				WriteCVS  writeCVS = new WriteCVS();
				writeCVS.writeData();
				dbo.closeDatabase();
				
			}
		};
		new Thread(runnable).start();
	}
}
