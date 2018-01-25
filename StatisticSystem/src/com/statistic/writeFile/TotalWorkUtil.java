package com.statistic.writeFile;

import java.util.ArrayList;
import java.util.HashMap;

import com.statistic.readFile.DatabaseOperate;
import com.statistic.time.MathUtil;

import bean.TeamBean;

public class TotalWorkUtil {
	private String place;
	private int personNum;
	private int shouldWorkDayNum;
	private int workDayNum;
	private int normalWorkDayNum;
	private int weekendWorkDayNum;
	private int exceptionDayNum;
	private int totalNum1 = 0;
	private int totalNum2 = 0;
	private int check = 0;
	private int num;
	
	
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	
	public ArrayList<String> getWorkDay(String place) {
		ArrayList<String> arrayList = new ArrayList<>();
		this.place = place;
		String sql1 = "select count(distinct name) as num from person where place = '"+place+"'and onsite = 0";
		personNum = dbo.queryInt(sql1);
		String sql2 = "select count(distinct dateDay) from dayperson where category = 1";
		shouldWorkDayNum = dbo.queryInt(sql2) * personNum;
		
		String sql3 = "SELECT sum(month_person.actually_day_num) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		workDayNum = (int) dbo.queryFloat(sql3);
		
		String sql4 = "SELECT sum(month_person.workday_work_num) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		
		normalWorkDayNum = (int) dbo.queryFloat(sql4);
		
		String sql5 = "SELECT sum(month_person.weekend_work_num) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		weekendWorkDayNum = (int) dbo.queryFloat(sql5);
		
		String sql6 = "SELECT sum(month_person.absence_day) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		exceptionDayNum = (int) dbo.queryFloat(sql6);
		arrayList.add(place);
		arrayList.add(String.valueOf(personNum));
		arrayList.add(String.valueOf(shouldWorkDayNum));
		arrayList.add(String.valueOf(workDayNum));
		arrayList.add(String.valueOf(normalWorkDayNum));
		arrayList.add(String.valueOf(weekendWorkDayNum));
		arrayList.add(String.valueOf(exceptionDayNum));
		arrayList.add(String.valueOf(totalNum1));
		arrayList.add(String.valueOf(totalNum2));
		arrayList.add(String.valueOf(check));
		return arrayList;
	}
	
	public ArrayList<String> getTotalDay()  {
		ArrayList<String> arrayList = new ArrayList<>();
		String sql1 = "select count(distinct name) as num from person where onsite = 0";
		personNum = dbo.queryInt(sql1);
		String sql2 = "select count(distinct dateDay) from dayperson where category = 1";
		shouldWorkDayNum = dbo.queryInt(sql2) * personNum;
		
		String sql3 = "SELECT sum(month_person.actually_day_num) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		workDayNum = (int) dbo.queryFloat(sql3);
		
		String sql4 = "SELECT sum(month_person.workday_work_num) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		
		normalWorkDayNum = (int) dbo.queryFloat(sql4);
		
		String sql5 = "SELECT sum(month_person.weekend_work_num) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		weekendWorkDayNum = (int) dbo.queryFloat(sql5);
		
		String sql6 = "SELECT sum(month_person.absence_day) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		exceptionDayNum = (int) dbo.queryFloat(sql6);
		arrayList.add("Total");
		arrayList.add(String.valueOf(personNum));
		arrayList.add(String.valueOf(shouldWorkDayNum));
		arrayList.add(String.valueOf(workDayNum));
		arrayList.add(String.valueOf(normalWorkDayNum));
		arrayList.add(String.valueOf(weekendWorkDayNum));
		arrayList.add(String.valueOf(exceptionDayNum));
		arrayList.add(String.valueOf(totalNum1));
		arrayList.add(String.valueOf(totalNum2));
		arrayList.add(String.valueOf(check));
		return arrayList;
	}
	

	public ArrayList<String> getPlace() {
		ArrayList<String> arrayList = new ArrayList<String>();
		String sql = "select distinct place from person";
		arrayList = dbo.queryString(sql);
		return arrayList;
		
	}
	
	public int getPlaceNum() {
		int num;
		String sql = "select count(distinct place) as num from person";
		num = dbo.queryInt(sql);
		this.num = num;
		return num;
	}
	
	
	public ArrayList<String> getOverTime(String place) {
		float totalOverTime;
		float normalDayOverTime;
		float weekendOVerTime;
		float averDayOverTime;
		float averPersonOverTime;
		float actuallyWorkDayNum;
		ArrayList<String> arrayList = new ArrayList<>();
		this.place = place;
		String sql1 = "select count(distinct name) as num from person where place = '"+place+"'and onsite = 0";
		personNum = dbo.queryInt(sql1);
		String sql2 = "select sum(month_person.actually_day_num) from month_person, person where "
				+ "month_person.name = person.name and person.place = '"+place+"'";
		actuallyWorkDayNum = dbo.queryFloat(sql2);
		
		String sql3 = "SELECT sum(month_person.over_time) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		totalOverTime =  dbo.queryFloat(sql3);
		
		String sql4 = "SELECT sum(month_person.workday_overtime) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		
		normalDayOverTime = (int) dbo.queryFloat(sql4);
		
		String sql5 = "SELECT sum(month_person.weekend_overtime) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.place = '"+place+"' and person.onsite = 0";
		weekendOVerTime = (int) dbo.queryFloat(sql5);
		
		if (actuallyWorkDayNum < 1 ) {
			averDayOverTime = 0;
		} else {
			averDayOverTime = totalOverTime / actuallyWorkDayNum;
			averDayOverTime = MathUtil.floatParseTwo(averDayOverTime);
		}
		if (personNum < 1 ) {
			averPersonOverTime = 0;
		} else {
			averPersonOverTime = totalOverTime / personNum;
			averPersonOverTime = MathUtil.floatParseTwo(averPersonOverTime);
		}
		
		
		int totalOverTime1 = MathUtil.floatParseInt(totalOverTime);
		int normalDayOverTime1 = MathUtil.floatParseInt(normalDayOverTime);
		int weekendOVerTime1 = MathUtil.floatParseInt(weekendOVerTime);
		int actuallyWorkDayNum1 = MathUtil.floatParseInt(actuallyWorkDayNum);
		arrayList.add(place);
		arrayList.add(String.valueOf(personNum));
		arrayList.add(String.valueOf(totalOverTime1));
		arrayList.add(String.valueOf(normalDayOverTime1));
		arrayList.add(String.valueOf(weekendOVerTime1));
		arrayList.add(String.valueOf(actuallyWorkDayNum1));
		arrayList.add(String.valueOf(averDayOverTime));
		arrayList.add(String.valueOf(averPersonOverTime));
		return arrayList;
	}
	
	public ArrayList<String> getTotalOverTime() {
		float totalOverTime;
		float normalDayOverTime;
		float weekendOVerTime;
		float averDayOverTime;
		float averPersonOverTime;
		float actuallyWorkDayNum;
		ArrayList<String> arrayList = new ArrayList<>();
		String sql1 = "select count(distinct name) as num from person where onsite = 0";
		personNum = dbo.queryInt(sql1);
		String sql2 = "select sum(month_person.actually_day_num) from month_person, person where "
				+ "month_person.name = person.name";
		actuallyWorkDayNum = dbo.queryFloat(sql2);
		
		String sql3 = "SELECT sum(month_person.over_time) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		totalOverTime =  dbo.queryFloat(sql3);
		
		String sql4 = "SELECT sum(month_person.workday_overtime) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		
		normalDayOverTime = (int) dbo.queryFloat(sql4);
		
		String sql5 = "SELECT sum(month_person.weekend_overtime) as num FROM month_person , person "
				+ "where month_person.name = person.name and person.onsite = 0";
		weekendOVerTime = (int) dbo.queryFloat(sql5);
		
		if (actuallyWorkDayNum < 1 ) {
			averDayOverTime = 0;
		} else {
			averDayOverTime = totalOverTime / actuallyWorkDayNum;
			averDayOverTime = MathUtil.floatParseTwo(averDayOverTime);
		}
		if (personNum < 1 ) {
			averPersonOverTime = 0;
		} else {
			averPersonOverTime = totalOverTime / personNum;
			averPersonOverTime = MathUtil.floatParseTwo(averPersonOverTime);
		}
		
		int totalOverTime1 = MathUtil.floatParseInt(totalOverTime);
		int normalDayOverTime1 = MathUtil.floatParseInt(normalDayOverTime);
		int weekendOVerTime1 = MathUtil.floatParseInt(weekendOVerTime);
		int actuallyWorkDayNum1 = MathUtil.floatParseInt(actuallyWorkDayNum);
		String averDayOverTime1 = String.valueOf(averDayOverTime);
		String averPersonOverTime1 = String.valueOf(averPersonOverTime);
		arrayList.add("Total");
		arrayList.add(String.valueOf(personNum));
		arrayList.add(String.valueOf(totalOverTime1));
		arrayList.add(String.valueOf(normalDayOverTime1));
		arrayList.add(String.valueOf(weekendOVerTime1));
		arrayList.add(String.valueOf(actuallyWorkDayNum1));
		arrayList.add(averDayOverTime1);
		arrayList.add(averPersonOverTime1);
		return arrayList;
	}

	public ArrayList<String> getOverTimeTitle() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("加班数据分析");
		arrayList.add("offshre人数");
		arrayList.add("总加班(小时)");
		arrayList.add("平日加班(小时)");
		arrayList.add("周末加班(小时)");
		arrayList.add("出勤(天)");
		arrayList.add("日均加班(小时)");
		arrayList.add("人均加班(小时)");
		return arrayList;
	}
	
	public ArrayList<String> getAbsenceTitle() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("考勤分析");
		arrayList.add("年假");
		arrayList.add("调休假");
		arrayList.add("司龄假");
		arrayList.add("婚假");
		arrayList.add("产假\\哺乳假\\产检假\\陪产假");
		arrayList.add("出差");
		arrayList.add("临时onsite");
		arrayList.add("病假");
		arrayList.add("异常人天");
		return arrayList;
	}
	
	
	public ArrayList<String> getAbsenceData(String place) {
		float annualVacation;
		float exchangeVacation;
		float silingVacation;
		float marryVacation;
		float othersVacation;
		float businessTrip;
		float onsite;
		float sickVacation;
		float exception;
		ArrayList arrayList = new ArrayList<String>();
		String sql1 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and person.place = '"+place+"' and leaves_day.category = '年假' and leaves_day.daycategory = 1";
		annualVacation = dbo.queryFloat(sql1);
		
		String sql2 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and person.place = '"+place+"' and leaves_day.category = '加班转调休' and leaves_day.daycategory = 1";
		exchangeVacation = dbo.queryFloat(sql2);
		
		String sql3 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and person.place = '"+place+"' and leaves_day.category = '司龄假' and leaves_day.daycategory = 1";
		silingVacation = dbo.queryFloat(sql3);
		
		String sql4 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and person.place = '"+place+"' and leaves_day.category = '婚假' and leaves_day.daycategory = 1";
		marryVacation = dbo.queryFloat(sql4);
		
		String sql5 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and person.place = '"+place+"' and (leaves_day.category = '产假' or leaves_day.category = '产检假' or leaves_day.category = '哺乳假' or "
						+ "leaves_day.category = '陪产假') and leaves_day.daycategory = 1";
		othersVacation = dbo.queryFloat(sql5);
		System.out.println("othersVacation ="+othersVacation);
		businessTrip =0.0f;
		onsite = 0.0f;
		
		String sql6 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and person.place = '"+place+"' and leaves_day.category = '病假'and leaves_day.daycategory = 1";
		sickVacation = dbo.queryFloat(sql6);
		
		String sql7 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name and "
				+ "person.place = '"+place+"' and leaves_day.daycategory = 1";
		exception = dbo.queryFloat(sql7);
		arrayList.add(place);
		arrayList.add(String.valueOf(annualVacation));
		arrayList.add(String.valueOf(exchangeVacation));
		arrayList.add(String.valueOf(silingVacation));
		arrayList.add(String.valueOf(marryVacation));
		arrayList.add(String.valueOf(othersVacation));
		arrayList.add(String.valueOf(businessTrip));
		arrayList.add(String.valueOf(onsite));
		arrayList.add(String.valueOf(sickVacation));
		arrayList.add(String.valueOf(exception));
		return arrayList;
	}
	
	public ArrayList<String> getTotalAbsence() {
		float annualVacation;
		float exchangeVacation;
		float silingVacation;
		float marryVacation;
		float othersVacation;
		float businessTrip;
		float onsite;
		float sickVacation;
		float exception;
		ArrayList arrayList = new ArrayList<String>();
		String sql1 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and leaves_day.category = '年假' and leaves_day.daycategory = 1";
		annualVacation = dbo.queryFloat(sql1);
		
		String sql2 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and leaves_day.category = '加班转调休' and leaves_day.daycategory = 1";
		exchangeVacation = dbo.queryFloat(sql2);
		
		String sql3 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and leaves_day.category = '司龄假'and leaves_day.daycategory = 1";
		silingVacation = dbo.queryFloat(sql3);
		
		String sql4 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and leaves_day.category = '婚假'and leaves_day.daycategory = 1";
		marryVacation = dbo.queryFloat(sql4);
		
		String sql5 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and (leaves_day.category = '产假' or leaves_day.category = '产检假' or leaves_day.category = '哺乳假' "
				+ "or leaves_day.category = '陪产假') and leaves_day.daycategory = 1";
		othersVacation = dbo.queryFloat(sql5);
		
		businessTrip =0.0f;
		onsite = 0.0f;
		
		String sql6 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name "
				+ "and leaves_day.category = '病假' and leaves_day.daycategory = 1";
		sickVacation = dbo.queryFloat(sql6);
		
		String sql7 = "select sum(leaves_day.total_day) from leaves_day, person where leaves_day.name = person.name and "
				+ "leaves_day.daycategory = 1";
		exception = dbo.queryFloat(sql7);
		
		arrayList.add("Total");
		arrayList.add(String.valueOf(annualVacation));
		arrayList.add(String.valueOf(exchangeVacation));
		arrayList.add(String.valueOf(silingVacation));
		arrayList.add(String.valueOf(marryVacation));
		arrayList.add(String.valueOf(othersVacation));
		arrayList.add(String.valueOf(businessTrip));
		arrayList.add(String.valueOf(onsite));
		arrayList.add(String.valueOf(sickVacation));
		arrayList.add(String.valueOf(exception));
		return arrayList;
	}
	
	public ArrayList<String> getTitleOverTimeOfTeam() {
		ArrayList<String> arrayList = new ArrayList();
		arrayList.add("加班数据分析");
		arrayList.add("offshore人数");
		arrayList.add("总加班(小时)");
		arrayList.add("平日加班(小时)");
		arrayList.add("周末加班(小时)");
		arrayList.add("出勤(天)");
		arrayList.add("日均加班(小时)");
		arrayList.add("人均加班(小时)");
		return arrayList;
	}	
	
	public int getTeamNum() {
		int num = 0;
		String sql = "select count(distinct team) from month_team";
		num = dbo.queryInt(sql);
		return num;
	}
	
	public ArrayList<TeamBean> getOverTimeOfTeamData() {
		ArrayList<TeamBean> arrayList;
		String sql = "select * from month_team";
		arrayList = dbo.queryTeam(sql);
		return arrayList;
	}
	
	public TeamBean getTotalOverTimeOfTeamData(){
		TeamBean teamBean = new TeamBean();
		teamBean.teamName = "北京";
		String sql1 = "select sum(person_num) as num from month_team";
		teamBean.personNum = dbo.queryInt(sql1);
		
		String sql2 = "select sum(total_overtime) as num from month_team";
		teamBean.totalOverTime = dbo.queryFloat(sql2);
		
		String sql3 = "select sum(workday_overtime) as num from month_team";
		teamBean.normalDayOverTime = dbo.queryFloat(sql3);
		
		String sql4 = "select sum(weekend_overtime) as num from month_team";
		teamBean.weekendOVerTime = dbo.queryFloat(sql4);
		
		String sql5 = "select sum(workday_num) as num from month_team";
		teamBean.actuallyWorkdayNum = dbo.queryFloat(sql5);
		
		if (teamBean.actuallyWorkdayNum < 1 ) {
			teamBean.averDayOverTime = 0;
		} else {
			teamBean.averDayOverTime = teamBean.totalOverTime / teamBean.actuallyWorkdayNum;
			teamBean.averDayOverTime = MathUtil.floatParseTwo(teamBean.averDayOverTime);
		}
		if (teamBean.personNum < 1 ) {
			teamBean.averPersonOverTime = 0;
		} else {
			teamBean.averPersonOverTime = teamBean.totalOverTime / teamBean.personNum;
			teamBean.averPersonOverTime = MathUtil.floatParseTwo(teamBean.averPersonOverTime);
		}
		return teamBean;
	}
}
