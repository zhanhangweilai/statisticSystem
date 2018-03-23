package com.statistic.readFile;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PersonUtils {
	public String name;		//姓名
	public String place;		//工作地点
	public String team;		//所在组
	public String module; //负责的模块
	public String leader; //负责人
	public boolean onsite; 	//是否onsite
	public String start_time; 	//开始刷卡时间
	public String end_time;	//结束刷卡时间
	public void  clear() {
		name = null;
		place = null;
		team = null;
		onsite = false;
		start_time = null;
		end_time = null;
	}
	public void InsertSql(XSSFWorkbook workBook) {
		String sql = null;
		DatabaseOperate dbo = DatabaseOperate.getInstance();
		System.out.println(3);
		dbo.connectDatabase();
		for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
		XSSFSheet xssfSheet = workBook.getSheetAt(numSheet);
		for (int i = 0; i<= xssfSheet.getLastRowNum(); i++) {
			XSSFRow xssfRow = xssfSheet.getRow(i);
			clear();
			for (int j =0; j < xssfRow.getLastCellNum(); j++) {
				String result = FormatValue.getXLSXValue(xssfRow.getCell(j));
				if ("序号".equals(result) || "No.".equals(result)) {
					break;
				}
				switch (j) {
					case 1: 
						name = result;
						break;
					case 2: 
						place = result;
						break;
					case 3: 
						team = result;
						break;
					case 4: 
						module = result;
						break;
					case 5: 
						leader = result;
						break;
					case 6: 
						start_time = result;
						break;
					case 7: 
						end_time = result;
						break;
					case 8: 
						if ("否".equals(result)) {
							onsite = false;
						} else {
							onsite = true;
						}
						break;
				}
			}
			System.out.println("name ="+name);
			if (null == name || "".equals(name)||"null".equals(name)) {
				System.out.println("countinue");
				continue;
			}
			sql = "insert into person (`name`, `place`, `team`, `onsite`, `module`, `leader`, `start_time`, `end_time`)"
					+"values('"+name+"','"+place+"','"+team+"',"+onsite+",'"+module+"','"+leader+"','"+start_time+"','"
					+end_time+"')";
			System.out.println(sql);
			dbo.insert(sql);
			}
		}
		//dbo.closeDatabase();
	}
}