package com.statistic.readFile;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.statistic.time.TimeUtil;

import bean.DateBean;

public class StatisticOneDay {
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	private ArrayList<String> dateList = new ArrayList<String>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private DayTableBeen dtb = new DayTableBeen();
	private DateFormat formatGetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final float STANDARDTIME = 9.00f; 
	String sql = null;
	public void statisticPersonOfDay(){
		dbo.connectDatabase();
		getPerson();
		ArrayList <DateBean>modifyDateList = new ArrayList<>();
		String dateSql = "select * from date_category";
		modifyDateList = dbo.queryModityDateCategory(dateSql);
		for (int i = 0; i< nameList.size(); i++) {
			String name  = nameList.get(i);
			dateList = getDate(name);
			if (dateList.size() < 1 || nameList.size() < 1) {
				continue;
			}
			for (int j = 0; j< dateList.size(); j++) {
				String strDate = dateList.get(j);
				String sql ="select date, category, name, dateDay from records where name = '"+name+"' and dateDay = '"+strDate+"'order by date asc";
				System.out.println("sql = "+sql);
				dtb = dbo.queryTimeRecords(sql);
				
				//若没有打卡记录则判断当天是否为周末
				if (dtb.category == 0) {
					dtb.category = TimeUtil.getCategoryOfDay(TimeUtil.StringToDate(strDate));
				}
				
				for(DateBean dateBean:modifyDateList) {
					if (TimeUtil.isSameDay(TimeUtil.StringToDate(strDate), TimeUtil.StringToDate(dateBean.date))) {
						dtb.category = dateBean.category;
						break;
					}
				}
				if (dtb.category == 0) {
					dtb.over_time = dtb.work_time;
					dtb.exception = false;
				} else if (dtb.work_time >= STANDARDTIME) {
					dtb.over_time = dtb.work_time - STANDARDTIME;
					dtb.exception = false;
				} else {
					dtb.absence_time = STANDARDTIME - dtb.work_time;
					dtb.exception = true;
				}
				String startTime;
				String endTime;
				if (dtb.startTime == null) {
					startTime = strDate;
				} else {
					startTime = TimeUtil.DateToString(dtb.startTime);
				}
				if (dtb.endTime == null) {
					endTime = strDate;
				} else {
					endTime = TimeUtil.DateToString(dtb.endTime);
				}
				
				sql = "insert into dayperson (name, work_time, over_time, dateDay, startTime, endTime, exception, absence_time, category) values('"+
				name+"',"+dtb.work_time+","+dtb.over_time+",'"+strDate+"','"+startTime+"','"+endTime+"',"+dtb.exception+","+dtb.absence_time+","+dtb.category+")";
				
				dbo.insert(sql);
			}
		}
		dbo.closeDatabase();
	}
	
	public ArrayList<String> getDate(String name) {
		String sql1 = "select start_time from person where name = '"+name+"'";
		String sql2 = "select end_time from person where name = '"+name+"'";
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<Date> dateList1 = new ArrayList<Date>();
		ArrayList<Date> dateList2 = new ArrayList<Date>();
		ArrayList<Date> dateListTemp = new ArrayList<Date>();
		dateList1 = dbo.queryDateDay(sql1);
		dateList2 = dbo.queryDateDay(sql2);
		System.out.println(dateList1.get(0));
		if (dateList1 != null && dateList1.size() > 0 && dateList2 != null && dateList2.size() > 0) {
			Date date1 = dateList1.get(0);
			Date date2 = dateList2.get(0);
			dateList = TimeUtil.getAllDayBetween(date1, date2);
		} else {
			String sql = "select `dateDay` FROM records group by dateDay order by dateDay asc ";
			dateListTemp =  dbo.queryDateDay(sql);
			for (Date date: dateListTemp){
				dateList.add(TimeUtil.DateToString(date));
			}
		}
		return dateList;
	}
	public void getPerson() {
		String sql = "select name FROM person";
		nameList = (ArrayList) dbo.queryPerson(sql);
	}
	
}
