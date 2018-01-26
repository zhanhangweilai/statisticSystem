package com.statistic.readFile;

import java.util.ArrayList;

import com.statistic.time.MathUtil;


public class TeamUtils{
	private String teamName;
	private int personNum;
	private float totalOverTime;
	private float workdayOverTime;
	private float weekendOverTime;
	private float workdayNum;
	private float averDayOverTime;
	private float averOverTime;
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	private ArrayList<String> teamList = new ArrayList<>();
	public void statisticTeam(String place) {
		dbo.connectDatabase();
		getTeam(place);
		for (String team : teamList) {
			teamName =team;
			getPersonNum(team, place);
			getOverTime(team, place);
			getDayNum(team, place);
			caculateAve();
			insert();
		}
	}
	public void getTeam(String place) {
		String sql;
		if (place == null || "".equals(place)){
			sql = "SELECT distinct team FROM person";
		} else {
			sql = "SELECT distinct team FROM person where place = '"+place+"'";
		}
		teamList = dbo.queryString(sql);
	}
	
	public  void getPersonNum(String team, String place) {
		String sql;
		personNum = 0;
		if (place == null || "".equals(place)){
			sql = "SELECT count(name) FROM person where team = '"+team+"' and onsite = 0";
		} else {
			sql = "SELECT count(name) FROM person where team = '"+team+"' and place = '"+place+"' and onsite = 0";
		}
		personNum = dbo.queryPersonNum(sql);
		System.out.println("team="+team+"  place ="+place+" personNum ="+personNum);
	}
	public void getOverTime(String team,String place) {
		String queryTotalOverTime;
		String queryWorkdayOverTime;
		String queryWeekendOverTime;
		if (null == place || "".equals(place)) {
			queryTotalOverTime = "select sum(month_person.over_time) as total_overtime from month_person, person"
					+ " where month_person.name = person.name and person.team = '"+team+"' and person.onsite = 0";
			
			queryWorkdayOverTime = "select sum(month_person.workday_overtime ) as total_overtime from month_person, person"
					+ " where month_person.name = person.name and person.team = '"+team+"' and person.onsite = 0";
			
			queryWeekendOverTime = "select sum(month_person.weekend_overtime) as total_overtime from month_person, person"
					+ " where month_person.name = person.name and person.team = '"+team+"' and person.onsite = 0";
			
		} else {
			queryTotalOverTime = "select sum(month_person.over_time) as total_overtime from month_person, person"
					+ " where month_person.name = person.name and person.team = '"+team+"' and person.place = '"+place+"' and person.onsite = 0";
					
			queryWorkdayOverTime = "select sum(month_person.workday_overtime) as total_overtime from month_person, person"
					+ " where month_person.name = person.name and person.team = '"+team+"' and person.place = '"+place+"' and person.onsite = 0";
					
			queryWeekendOverTime = "select sum(month_person.weekend_overtime) as total_overtime from month_person, person"
					+ " where month_person.name = person.name and person.team = '"+team+"' and person.place = '"+place+"' and person.onsite = 0";
		}
		
		totalOverTime = dbo.querySumTime(queryTotalOverTime);
		workdayOverTime  = dbo.querySumTime(queryWorkdayOverTime);
		weekendOverTime = dbo.querySumTime(queryWeekendOverTime);
	}
	
	public void getDayNum(String team,String place) {
		String queryDayNum;
		if (null == place || "".equals(place)) {
			queryDayNum = "select sum(month_person.actually_day_num) from month_person, person "
					+ "where month_person.name = person.name and person.team = '"+team+"' and person.onsite = 0";
		} else {
			queryDayNum = "select sum(month_person.actually_day_num) from month_person, person "
					+ "where month_person.name = person.name and person.team = '"+team+"' and person.place = '"+place+"' and person.onsite = 0";
		}
		workdayNum = dbo.queryDateNum(queryDayNum);
	}
	public void caculateAve() {
		averDayOverTime = totalOverTime / workdayNum;
		averDayOverTime = MathUtil.floatParseTwo(averDayOverTime);
		averOverTime = totalOverTime / personNum;
		averOverTime = MathUtil.floatParseTwo(averOverTime);
	}
	
	public void insert () {
		String sql = "insert into month_team (team, person_num, total_overtime, aver_overtime, workday_overtime,"
				+ "weekend_overtime, workday_num, aver_day_overtime) values ('"+teamName+"',"+personNum+","+totalOverTime+","
				+averOverTime+","+workdayOverTime+","+weekendOverTime+","+workdayNum+","+averDayOverTime+")";
		dbo.insert(sql);
	}
	
}