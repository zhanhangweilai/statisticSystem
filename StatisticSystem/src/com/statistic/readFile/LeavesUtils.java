package com.statistic.readFile;

import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.statistic.time.TimeUtil;

import bean.DateBean;

public class LeavesUtils {
	
	public String name;
	public String leave_start_time;
	public String leave_end_time;
	public String category;
	public String team;
	public float total;
	
	public void clear() {
		name = null;
		leave_end_time = null;
		leave_start_time = null;
		category = null;
		team = null;
		total = 0.0f;
	}
	
	private String getPlace(String fileName) {
		String place = "北京";
		if (fileName.contains("北京")){
			place = "北京";
		} else if (fileName.contains("南京")) {
			place = "南京";
		} else if (fileName.contains("上海")) {
			place = "上海";
		} else if (fileName.contains("深圳")) {
			place = "深圳";
		} 
		
		return place;
	}
	public void InsertSql(XSSFWorkbook workBook, String fileName) {
		String sql = null;
		String mSql = null;
		float totalHour = 0.00f;
		float totalDay = 0;
		int daycategory = 0;
		String place = getPlace(fileName);
		DatabaseOperate dbo = DatabaseOperate.getInstance();
		//dbo.connectDatabase();
		ArrayList <Date> arrayList = new ArrayList<>();
		ArrayList <DateBean>modifyDateList = new ArrayList<>();
		String dateSql = "select * from date_category";
		modifyDateList = dbo.queryModityDateCategory(dateSql);
		for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = workBook.getSheetAt(numSheet);
			for (int i = 0; i< xssfSheet.getLastRowNum(); i++) {
				XSSFRow xssfRow = xssfSheet.getRow(i);
				clear();
				for (int j =0; j < xssfRow.getLastCellNum(); j++) {
					String result = FormatValue.getXLSXValue(xssfRow.getCell(j));
					if ("姓名".equals(result)) {
						break;
					}
					switch (j) {
						case 0: 
							name = result;
							break;
						case 1: 
							team = result;
							break;
						case 2: 
							category = result;
							break;
						case 3: 
							leave_start_time = result;
							System.out.println("result="+result);
							System.out.println("leave_start_time="+leave_start_time);
							break;
						case 4: 
							leave_end_time = result;
							break;
						case 5: 
							total = Float.parseFloat(result);
							break;
					}
				}
				if (null == name) {
					continue;
				}
				System.out.println(name+","+leave_start_time+","+leave_end_time);
				sql = "insert into leaves (`name`, `team`, `leave_start_time`, `leave_end_time`, `category`, `place`, `total`)"
						+"values('"+name+"','"+team+"','"+leave_start_time+"','"+leave_end_time+"','"
						+category+"','"+place+"',"+total+")";
				dbo.insert(sql);
				arrayList = TimeUtil.getAllDateBetween(TimeUtil.StringToDate(leave_start_time),
						TimeUtil.StringToDate(leave_end_time));
				
				
				if (arrayList.size() == 1) {
					mSql = null;
					totalDay = total;
					daycategory = TimeUtil.getCategoryOfDay(arrayList.get(0));
					for(DateBean dateBean:modifyDateList) {
						if (TimeUtil.isSameDay(arrayList.get(0), TimeUtil.StringToDate(dateBean.date))) {
							daycategory = dateBean.category;
							break;
						}
					}
					totalHour = TimeUtil.getHour(TimeUtil.StringToDate(leave_start_time), TimeUtil.StringToDate(leave_end_time));
					if (totalHour <= 0) {
						totalHour = 0.0f;
						totalDay = 0.0f;
					}
					mSql = "insert into leaves_day (`name`, `team`, `leave_start_time`, `leave_end_time`, `dateDay`,`category`, `total_hour`, `total_day`, `place`, `daycategory`)"
							+"values('"+name+"','"+team+"','"+leave_start_time+"','"+leave_end_time+"','"+TimeUtil.DateToString(arrayList.get(0))+"','"
							+category+"',"+totalHour+","+totalDay+",'"+place+"',"+daycategory+")";
					dbo.insert(mSql);
				} else {
					for (int z = 0; z < arrayList.size(); z++) {
						mSql = null;
						daycategory = TimeUtil.getCategoryOfDay(arrayList.get(z));
						for(DateBean dateBean:modifyDateList) {
							if (TimeUtil.isSameDay(arrayList.get(0), TimeUtil.StringToDate(dateBean.date))) {
								daycategory = dateBean.category;
								break;
							}
						}
						if (TimeUtil.getDateWithoutTime(leave_start_time).equals(arrayList.get(z)) ) {
							totalHour = TimeUtil.getHour(TimeUtil.StringToDate(leave_start_time), 
									TimeUtil.StringToDate(TimeUtil.getEndWorkTime(arrayList.get(z))));
							if (totalHour <= 0) {
								totalHour = 0.0f;
								totalDay = 0.0f;
							} else if (TimeUtil.getDay(totalHour) > 0.5 && TimeUtil.StringToDate(leave_start_time).getHours() < 12) {
								totalDay = 1.0f;
							} else {
								totalDay = 0.5f;
							}
							mSql = "insert into leaves_day (`name`, `team`, `leave_start_time`, `leave_end_time`, `dateDay`,`category`, `total_hour`, `total_day`, `place`, `daycategory`)"
									+"values('"+name+"','"+team+"','"+leave_start_time+"','"+TimeUtil.getEndWorkTime(arrayList.get(z))+"','"+TimeUtil.DateToString(arrayList.get(z))+"','"
									+category+"',"+totalHour+","+totalDay+",'"+place+"',"+daycategory+")";
						} else if (TimeUtil.getDateWithoutTime(leave_end_time).equals(arrayList.get(z))) {
							totalHour = TimeUtil.getHour(TimeUtil.StringToDate(TimeUtil.getStartWorkTime(arrayList.get(z))), 
									TimeUtil.StringToDate(leave_end_time));
							if (totalHour <= 0) {
								totalHour = 0.0f;
								totalDay = 0.0f;
							} else if (TimeUtil.getDay(totalHour) > 0.5 && TimeUtil.StringToDate(leave_end_time).getHours() > 16) {
								totalDay = 1.0f;
							} else {
								totalDay = 0.5f;
							}
							mSql = "insert into leaves_day (`name`, `team`, `leave_start_time`, `leave_end_time`, `dateDay`,`category`, `total_hour`, `total_day`, `place`, `daycategory`)"
									+"values('"+name+"','"+team+"','"+TimeUtil.getStartWorkTime(arrayList.get(z))+"','"+leave_end_time+"','"+TimeUtil.DateToString(arrayList.get(z))+"','"
									+category+"',"+totalHour+","+totalDay+",'"+place+"',"+daycategory+")";
						} else {
							totalHour = 8.0f;
							totalDay = 1.0f;
							mSql = "insert into leaves_day (`name`, `team`, `leave_start_time`, `leave_end_time`, `dateDay`,`category`, `total_hour`, `total_day`, `place`, `daycategory`)"
									+"values('"+name+"','"+team+"','"+TimeUtil.getStartWorkTime(arrayList.get(z))+"','"+TimeUtil.getEndWorkTime(arrayList.get(z))+"','"+TimeUtil.DateToString(arrayList.get(z))+"','"
									+category+"',"+totalHour+","+totalDay+",'"+place+"',"+daycategory+")";
						}
						
						dbo.insert(mSql);
					}
				}
			}
		} 
		
		//dbo.closeDatabase();
	}
}
