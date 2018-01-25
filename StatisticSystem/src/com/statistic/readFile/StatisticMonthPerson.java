package com.statistic.readFile;

import java.util.ArrayList;

import bean.PersonMonthBean;
import bean.StructModify;

public class StatisticMonthPerson {
  private String name;
  private float work_time;
  private float over_time;
  private float workday_num;
  private float actually_day_num;
  private float workday_work_num;
  private float absence_day;
  private float weekend_work_num;
  private float total_overtime;
  private float workday_overtime;
  private float weekend_overtime;
  private float dayNum;
  private DatabaseOperate dbo = DatabaseOperate.getInstance();
  
  public void processMonthPerson() {
	  dbo.connectDatabase();
	  getModifyData();
	  getData();
	  dbo.closeDatabase();
  }
  
  public void getModifyData() {
	  ArrayList<StructModify> arrayList = new ArrayList<>();
	  String sql = "SELECT dayperson.name, dayperson.work_time,dayperson.dateDay, "
		  		+ "leaves_day.total_day as leave_day"
		  		+" from dayperson , leaves_day  where dayperson.name = leaves_day.name "
		  		+"and leaves_day.dateDay = dayperson.dateDay";
	  System.out.println(sql);
	  arrayList = dbo.queryModifyTable(sql);
	  for (StructModify structModify:arrayList) {
		  modifyTable(structModify);
	  }
  }
  public void modifyTable(StructModify structModify) {
	  float leaveDay = structModify.leaveDay;
	  float workTime = structModify.workTime;
	  float overTime = 0;
	  float absenceTime = 0;
	  String name = structModify.name;
	  String strDateDay = structModify.strDateDay;
	  
	  if (leaveDay == 0.5 && workTime < 9) {
		  if (workTime >= 4) {
			  overTime =  workTime - 4;
		  } else {
			  absenceTime = 4 - workTime;
		  }
		  if (name != null && strDateDay != null) {
			  String updateSql = "update dayperson set over_time = "+overTime+ ", absence_time ="+absenceTime+
					  " where name = '"+name+"' and dateDay ='"+strDateDay+"'";
			  dbo.update(updateSql);
		  }
	  } else if (workTime >= 9) {
		  String deleteSql = "delete from leaves_day where name = '"+name+"' and dateDay = '"+strDateDay +"'";
		  dbo.delete(deleteSql);
		  
	  }
	  
  }
  
  public void getData () {
	  ArrayList<PersonMonthBean> arrayList = new ArrayList<>();
	  String queryNameListandDateNum = "select  name, count(dateDay) as num_day, sum(work_time) as work_time,"
	  		+ " sum(over_time) as over_time from dayperson group by name";
	  arrayList = dbo.queryNameListandDateNum(queryNameListandDateNum);
	  for (PersonMonthBean pmb : arrayList) {
		  name = pmb.name;
		  dayNum = pmb.dayNum;
		  work_time = pmb.work_time;
		  over_time = pmb.over_time;
		  
		  String queryWorkDayNum = "select count(dateDay) as workday_num  from dayPerson "
		  		+ "where name = '"+pmb.name+"' and category = 1";
		  workday_num = dbo.queryDateNum(queryWorkDayNum);
		  
		  String queryActuallyDayNum = "select count(dateDay) as actuallyday_num  from dayPerson "
		  		+ "where name = '"+pmb.name+"' and work_time > 0";
		  actually_day_num = dbo.queryDateNum(queryActuallyDayNum);
		  
		  String queryWorkDayWorkNum = "select count(dateDay) as workdayWork_num"
		  		+ " from dayPerson where name = '"+pmb.name+"' and category = 1 and work_time > 0";
		  workday_work_num = dbo.queryDateNum(queryWorkDayWorkNum);
		  
		  String queryWorkDayOverTime = "select sum(over_time) as workday_overtime"
			  	+ " from dayPerson where name = '"+pmb.name+"' and category = 1 and work_time > 0";
		  workday_overtime = dbo.querySumTime(queryWorkDayOverTime);
		  
		  String queryAbsenceDayNum = "select sum(total_day) as absence_day  from leaves_day "
			  	+ "where name = '"+pmb.name+"' and daycategory = 1";
		  absence_day = dbo.queryDateNum(queryAbsenceDayNum);
		  
		  String queryWeekendWorkNum = "select count(dateDay) as weekdayWork_num"
		  		+ " from dayPerson where name = '"+pmb.name+"' and category = 0 and work_time > 0";
		  weekend_work_num = dbo.queryDateNum(queryWeekendWorkNum);
		  
		  String queryWeekendOverTime = "select sum(over_time) as weekday_overtime  "
			  	+ "from dayPerson where name = '"+pmb.name+"' and category = 0 and work_time > 0";
		  weekend_overtime = dbo.querySumTime(queryWeekendOverTime);
		  
		  workday_work_num = workday_num - absence_day;
		  actually_day_num = workday_work_num + weekend_work_num;
		  total_overtime = weekend_overtime + workday_overtime;
		  
		  String insertSql = "insert into month_person (name, work_time, over_time, workday_num, actually_day_num, workday_work_num,"
		  		+ " absence_day, weekend_work_num, total_overtime, workday_overtime, weekend_overtime) values ('"
		  		+name+"',"+work_time+","+over_time+","+workday_num+","+actually_day_num+","+workday_work_num+","
		  		+ absence_day+","+weekend_work_num+","+total_overtime+","+workday_overtime+","+weekend_overtime+")";
		  dbo.insert(insertSql);
	  }
  }
}
