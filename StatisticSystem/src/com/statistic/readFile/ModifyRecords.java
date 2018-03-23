package com.statistic.readFile;

import java.util.ArrayList;
import java.util.Date;

import com.statistic.time.TimeUtil;

class ModifyRecordsBean{
	public String name;
	public float work_time;
	public Date dateDay;
}
public class ModifyRecords {
	private DatabaseOperate dbo = DatabaseOperate.getInstance();
	
	public void getExceptionData() {
		dbo.connectDatabase();
		ArrayList<ModifyRecordsBean> arrayList = new ArrayList<>();
		String sql = "select dayperson.name, dayperson.work_time, dayperson.dateDay from dayperson, person where dayperson.name = person.name "
				+ " and dayperson.exception = 1 and dayperson.category = 1 and dayperson.work_time < 9 and person.onsite = 0";
				
		String sqlModify;
		String queryRecords;
		String insertRecords;
		boolean isInsert = true;
		RecordsUtil ru = new RecordsUtil();
		arrayList = dbo.queryModityRecords(sql);
		ArrayList<RecordsUtil> ruList = new ArrayList();
		ArrayList<String> dateList = new ArrayList<>(); 
		float leavesDay;
		for (ModifyRecordsBean mdr: arrayList) {
			String sql1 = "select total_day from leaves_day where name = '"+mdr.name+"' and dateDay = '"+
					TimeUtil.DateToString(mdr.dateDay)+"'";
			leavesDay = dbo.queryFloat(sql1);
			if (mdr.work_time >= 4) {
				if (leavesDay >= 0.5) {
					continue;
				} else {
					queryRecords = "select * from Records where name = '"+mdr.name+"' and dateDay = '"+mdr.dateDay+"'"; 
					ru = dbo.queryRecords(queryRecords).get(0);
					sqlModify = "select date from records where name = '"+mdr.name+"' and dateDay = '"+mdr.dateDay+"'order by date asc";
					System.out.println("sqlModify ="+sqlModify);
					String firstTime =   dbo.queryString(sqlModify).get(0);
					String endTime = TimeUtil.DateToString(TimeUtil.getModifyDateTime(TimeUtil.StringToDate(firstTime), 9));
					insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
							+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+endTime+"','"
							+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
							+isInsert+","+ru.category+")";
					dbo.insert(insertRecords);
					System.out.println("insertRecords1="+insertRecords);
				}
			} else {
				if (leavesDay >= 1) {
					continue;
				} else if (leavesDay == 0.5) {
					queryRecords = "select * from Records where name = '"+mdr.name+"' and dateDay = '"+mdr.dateDay+"'"; 
					ruList = dbo.queryRecords(queryRecords);
					if (ruList.size() > 0) {
						ru = ruList.get(0);
						sqlModify = "select date from records where name = '"+mdr.name+"' and dateDay = '"+mdr.dateDay+"'order by date asc";
						dateList =  dbo.queryString(sqlModify);
						String firstTime =  dateList.get(0);
						System.out.println("firstTime ="+firstTime);
						String endTime = TimeUtil.DateToString(TimeUtil.getModifyDateTime(TimeUtil.StringToDate(firstTime), 4));
						insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
								+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+endTime+"','"
								+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
								+isInsert+","+ru.category+")";
						dbo.insert(insertRecords);
					} else {
						queryRecords = "select distinct(id_card) from records where name = '"+mdr.name+"'";
						ru.id_card = dbo.queryString(queryRecords).get(0);
						queryRecords = "select distinct(site) from records where name = '"+mdr.name+"'";
						ru.site = dbo.queryString(queryRecords).get(0);
						queryRecords = "select distinct(team_name) from records where name = '"+mdr.name+"'";
						ru.team_name = dbo.queryString(queryRecords).get(0);
						queryRecords = "select distinct(pass) from records where name = '"+mdr.name+"'";
						ru.pass = dbo.queryString(queryRecords).get(0);
						queryRecords = "select distinct(description) from records where name = '"+mdr.name+"'";
						ru.site = dbo.queryString(queryRecords).get(0);
						queryRecords = "select serial from records where dateDay = '"+mdr.dateDay+"'";
						ru.serial = dbo.queryString(queryRecords).get(0);
						ru.dateDay = TimeUtil.DateToString(mdr.dateDay);
						ru.name = mdr.name;
						queryRecords = "select distinct(file_name) from records where dateDay = '"+TimeUtil.DateToString(mdr.dateDay)+"' and file_name like '%BeiJing%'";
						ArrayList<String> fileList = dbo.queryString(queryRecords);
						String strDate = TimeUtil.DateToStringWithoutTime1(mdr.dateDay);
						for (String fileName: fileList) {
							if (fileName.contains(strDate)) {
								ru.file_name = fileName;
								break;
							}
						}
						String startTime = TimeUtil.getRandomWorkStartTime(mdr.dateDay);
						String endTime = TimeUtil.DateToString(TimeUtil.getModifyDateTime(TimeUtil.StringToDate(startTime), 4));
						insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
								+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+startTime+"','"
								+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
								+isInsert+","+ru.category+")";
						dbo.insert(insertRecords);
						
						insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
								+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+endTime+"','"
								+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
								+isInsert+","+ru.category+")";
						dbo.insert(insertRecords);
					}
				} else if (leavesDay < 0.5) {
					queryRecords = "select * from records where name = '"+mdr.name+"' and dateDay = '"+mdr.dateDay+"'";
					System.out.println("queryRecords="+queryRecords);
					ruList = dbo.queryRecords(queryRecords);
					if (ruList.size() > 0) {
						ru.clear();
						ru = ruList.get(0);
						System.out.println("ru,file_name ="+ru.file_name);
						sqlModify = "select date from records where name = '"+mdr.name+"' and dateDay = '"+mdr.dateDay+"'order by date asc";
						dateList =  dbo.queryString(sqlModify);
						String firstTime =  dateList.get(0);
						System.out.println("firstTime ="+firstTime);
						String endTime = TimeUtil.DateToString(TimeUtil.getModifyDateTime(TimeUtil.StringToDate(firstTime), 9));
						insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
								+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+endTime+"','"
								+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
								+isInsert+","+ru.category+")";
						System.out.println("insertRecords3="+insertRecords);
						dbo.insert(insertRecords);
					} else {
						ArrayList<String> recordsList = new ArrayList<String>();
						String place ;
						queryRecords = "select * from records where name = '"+mdr.name+"'";
						recordsList = dbo.queryString(queryRecords);
						if (recordsList.size() > 0) {
							queryRecords = "select distinct(id_card) from records where name = '"+mdr.name+"'";
							ru.id_card = dbo.queryString(queryRecords).get(0);
							queryRecords = "select distinct(site) from records where name = '"+mdr.name+"'";
							ru.site = dbo.queryString(queryRecords).get(0);
							queryRecords = "select distinct(team_name) from records where name = '"+mdr.name+"'";
							ru.team_name = dbo.queryString(queryRecords).get(0);
							queryRecords = "select distinct(pass) from records where name = '"+mdr.name+"'";
							ru.pass = dbo.queryString(queryRecords).get(0);
							queryRecords = "select distinct(description) from records where name = '"+mdr.name+"'";
							ru.description = dbo.queryString(queryRecords).get(0);
						} else {
							RecordsUtil recordsUtil = noRecords(mdr.name);
							if (recordsUtil != null) {
								ru.id_card = recordsUtil.id_card ;
								ru.site = recordsUtil.site;
								ru.team_name = recordsUtil.team_name;
								ru.pass = recordsUtil.pass;
								ru.site = recordsUtil.site;
								ru.description = recordsUtil.description;
							}
						}
						place = getPlace(mdr.name);
						if (place != null && !"other".equals(place)) {
							queryRecords = "select serial from records where dateDay = '"+mdr.dateDay+"'"
									+ " and file_name like '%"+place+"%'";
							recordsList =  dbo.queryString(queryRecords);
							if (recordsList != null && recordsList.size() > 0) {
								ru.serial = dbo.queryString(queryRecords).get(0);
							} else {
								queryRecords = "select serial from records where file_name like '%"+place+"%'";
								recordsList =  dbo.queryString(queryRecords);
								if (recordsList != null && recordsList.size() > 0) {
									ru.serial = dbo.queryString(queryRecords).get(0);
								} 
							}
							queryRecords = "select category from records where dateDay = '"+mdr.dateDay+"'";
							ru.category = dbo.queryInt(queryRecords);
						
							ru.dateDay = TimeUtil.DateToString(mdr.dateDay);
							ru.name = mdr.name;
							queryRecords = "select distinct(file_name) from records where dateDay = '"+TimeUtil.DateToString(mdr.dateDay)+"' and file_name like '%"+place+"%'";
							ArrayList<String> fileList = dbo.queryString(queryRecords);
							String strDate = TimeUtil.DateToStringWithoutTime1(mdr.dateDay);
							for (String fileName: fileList) {
								if (fileName.contains(strDate)) {
									ru.file_name = fileName;
									System.out.println("fileName = "+fileName);
									break;
								}
							}
							String startTime = TimeUtil.getRandomWorkStartTime(mdr.dateDay);
							String endTime = TimeUtil.getRandomWorkEndTime(mdr.dateDay);
							insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
									+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+startTime+"','"
									+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
									+isInsert+","+ru.category+")";
							System.out.println("insertRecords2……="+insertRecords);
							dbo.insert(insertRecords);
							
							insertRecords = "insert into records (`serial`, `id_card`, `name`, `team_name`, `date`, `dateDay`, `site`, `pass`, `description`, `file_name`, `insert`, `category`)"
									+"values('"+ru.serial+"','"+ru.id_card+"','"+ru.name+"','"+ru.team_name+"','"+endTime+"','"
									+ru.dateDay+"','"+ru.site+"','"+ru.pass+"','"+ru.description+"','"+ru.file_name+"',"
									+isInsert+","+ru.category+")";
							dbo.insert(insertRecords);
						} else {
							System.out.println(mdr.name+" "+mdr.dateDay +"的数据无法插入" );
						}
					}
				}
			}
		}
	}

	private String getPlace(String name) {
		String  place = null;
		String sql =  "select place from person where name = '"+name+"'";
		ArrayList<String> list = dbo.queryString(sql);
		if (list.size() > 0) {
		 place = list.get(0);
		}
		
		if (place == null) {
			System.out.println("此同事无工作地址");
			return null;
		} 
		switch (place) {
			case "北京":
				return "BeiJing";
			case "南京":
				return "NJ";
			case "上海":
				return "SH";
			case "深圳":
				return "Shenzhen";
			default:
				System.out.println("非正常工作地址");
				return "other";
		}
	}
	private RecordsUtil noRecords(String name) {
		String  place = null;
		String sql =  "select place from person where name = '"+name+"'";
		ArrayList<RecordsUtil> recordsList = null;
		RecordsUtil recordsUtil = new RecordsUtil();
		ArrayList<String> list = dbo.queryString(sql);
		String queryRecords;
		if (list.size() > 0) {
		 place = list.get(0);
		}
		
		if (place == null) {
			System.out.println(name+"同学工作地址不正确无法插入数据");
			return null;
		} 
		switch (place) {
			case "北京":
				queryRecords = "select * from records where file_name like '%BeiJing%'";
				recordsList = dbo.queryRecords(queryRecords);
				break;
			case "南京":
				queryRecords = "select * from records where file_name like '%NJ%'";
				recordsList = dbo.queryRecords(queryRecords);
				break;
			case "上海":
				queryRecords = "select * from records where file_name like '%SH%'";
				recordsList = dbo.queryRecords(queryRecords);
				break;
			case "深圳":
				queryRecords = "select * from records where file_name like '%Shenzhen%'";
				recordsList = dbo.queryRecords(queryRecords);
				break;
			default:
				break;
		}
		if (recordsList != null && recordsList.size() > 0) {
			recordsUtil = recordsList.get(0);
			//若打卡记录中有team，则从person表中查询此人的team
			if (recordsUtil.team_name != null && !"".equals(recordsUtil.team_name) && !"null".equals(recordsUtil.team_name)) {
				String queryTeam = "select team from person where name = '"+name+"'";
				list = dbo.queryString(queryTeam);
				if (list.size() > 0) {
					recordsUtil.team_name = list.get(0);
				}
			}
			return recordsUtil;
		} else {
			return null;
		}
	}
}
