package com.statistic.readFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.statistic.time.TimeUtil;

import bean.DateBean;
import bean.LeavesOfPerson;
import bean.PersonMonthBean;
import bean.PersonOfMonth;
import bean.StructModify;
import bean.TeamBean;

public class DatabaseOperate {
	private final String dirver = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/statistic?characterEncoding=utf8&COLLATE=utf8_unicode_ci&useSSL=true";
	private final String user = "root";
	private final String password = "lihang123";
	private Connection con;
	private PreparedStatement psmt;
	
	private static DatabaseOperate instance = new DatabaseOperate();
	private DatabaseOperate() {}
	
	public static DatabaseOperate getInstance() {
		return  instance;
	}
	
	DateFormat formatGetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 连接数据库
	 */
	public void connectDatabase() {
		
		try {
			Class.forName(dirver);
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connect to Database");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建数据库表
	 */
	public void createTable () {
		String dropPersonTable = "DROP TABLE  if exists person";
		String createPersonTable = "CREATE TABLE `person` ("
				  +" `name` VARCHAR(20) NOT NULL,"
				  +" `place` VARCHAR(20) NOT NULL,"
				  +" `team` VARCHAR(20) NOT NULL,"
				  +" `onsite` TINYINT NOT NULL,"
				  +" `module` VARCHAR(50) NOT NULL,"
				  +" `leader` VARCHAR(20) NOT NULL,"
				  +" `start_time` DATE NOT NULL,"
				  +" `end_time` DATE NOT NULL,"
				  +"PRIMARY KEY (`name`),"
				  +"UNIQUE INDEX `name_UNIQUE` (`name` ASC))CHARSET=utf8";
		
		String dropRecodsTable = "drop table if exists records";
		String createRecordsTable = "CREATE TABLE `records` ("
				  +"`serial` VARCHAR(8) NULL,"
				  +"`id_card` VARCHAR(10) NULL,"
				  +"`name` VARCHAR(20) NOT NULL,"
				  +"`team_name` VARCHAR(20) NULL,"
				  +"`date` DATETIME(1) NOT NULL,"
				  +"`dateDay` DATETIME(1) NOT NULL,"
				  +"`site` VARCHAR(50) NULL,"
				  +"`pass` VARCHAR(50) NULL,"
				  +"`description` VARCHAR(50) NULL,"
				  +"`file_name` VARCHAR(40) NULL,"
				  +"`insert` TINYINT NOT NULL,"
				  +"`category` INT NULL"
				  +")CHARSET=utf8";
		
		String dropLeaveTable = "DROP TABLE if exists leaves";
		String createLeaveTable = "CREATE TABLE if not exists `leaves` ("
				  +"`name` VARCHAR(20) NOT NULL,"
				  +"`team` VARCHAR(40) NOT NULL,"
				  +"`leave_start_time` DATETIME(1) NOT NULL,"
				  +"`leave_end_time` DATETIME(1) NOT NULL,"
				  +"`category` VARCHAR(30) NOT NULL,"
				  +"`total` FLOAT NOT NULL,"
				  +"`place` VARCHAR(20) NOT NULL"
				  +")CHARSET=utf8";
		String dropDayPersonTable = "DROP TABLE if exists dayperson";
		String createdaypersonTable = "CREATE TABLE if not exists `dayperson` ("
				  +"`name` VARCHAR(20) NOT NULL,"
				  +"`work_time` FLOAT NOT NULL,"
				  +"`over_time` FLOAT ,"
				  +"`dateDay` DATETIME(1) NOT NULL,"
				  +"`startTime` DATETIME(1) NOT NULL,"
				  +"`endTime` DATETIME(1) NOT NULL,"
				  +"`exception` TINYINT NOT NULL,"
				  +"`absence_time` FLOAT,"
				  +"`category` INT"
				  +")CHARSET=utf8";
		
		String dropLeaveDayTable = "DROP TABLE if exists leaves_day";
		String createLeaveDayTable = "CREATE TABLE if not exists `leaves_day` ("
				  +"`name` VARCHAR(20) NOT NULL,"
				  +"`team` VARCHAR(40) NOT NULL,"
				  +"`leave_start_time` DATETIME(1) NOT NULL,"
				  +"`leave_end_time` DATETIME(1) NOT NULL,"
				  +"`dateDay` DATETIME(1) NOT NULL,"
				  +"`category` VARCHAR(30) NOT NULL,"
				  +"`total_hour` FLOAT,"
				  +"`total_day` FLOAT,"
				  +"`place` VARCHAR(20) NOT NULL,"
				  +"`daycategory` INT NULL"
				  +")CHARSET=utf8";
		
		String dropMonthTeamTable = "DROP TABLE if exists month_team";
		String createMonthTeamTable = "CREATE TABLE if not exists `month_team` ("
				  +"`team` VARCHAR(40) NOT NULL,"
				  +"`person_num` INT,"
				  +"`total_overtime` FLOAT,"
				  +"`aver_overtime` FLOAT,"
				  +"`workday_overtime` FLOAT,"
				  +"`weekend_overtime` FLOAT,"
				  +"`workday_num` FLOAT,"
				  +"`aver_day_overtime` FLOAT"
				  +")CHARSET=utf8";
		
		String dropMonthPersonTable = "DROP TABLE if exists month_person";
		String createMonthPersonTable = "CREATE TABLE if not exists `month_person` ("
				  +"`name` VARCHAR(20) NOT NULL,"
				  +"`work_time` FLOAT,"
				  +"`over_time` FLOAT,"
				  +"`workday_num` FLOAT,"
				  +"`actually_day_num` FLOAT,"
				  +"`workday_work_num` FlOAT,"
				  +"`absence_day` FLOAT,"
				  +"`weekend_work_num` FLOAT,"
				  +"`total_overtime` FLOAT,"
				  +"`workday_overtime` FLOAT,"
				  +"`weekend_overtime` FLOAT"
				  +")CHARSET=utf8";
		
		String dropDateCategory = "DROP TABLE if exists date_category";
		String createDateCategory = "CREATE TABLE if not exists `date_category` ("
				  +"`date` DATETIME(1) NOT NULL,"
				  +"`category` INT"
				  +")CHARSET=utf8";


		try {
			Statement statement = con.createStatement();
			statement.execute(dropPersonTable);
			statement.execute(createPersonTable);
			statement.execute(dropRecodsTable);
			statement.execute(createRecordsTable);
			statement.execute(dropLeaveTable);
			statement.execute(createLeaveTable);
			statement.execute(dropDayPersonTable);
			statement.execute(createdaypersonTable);
			statement.execute(dropLeaveDayTable);
			statement.execute(createLeaveDayTable);
			statement.execute(dropMonthTeamTable);
			statement.execute(createMonthTeamTable);
			statement.execute(dropMonthPersonTable);
			statement.execute(createMonthPersonTable);
			statement.execute(dropDateCategory);
			statement.execute(createDateCategory);
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据添加到数据库
	 */
	public void insert(String sql) {
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询数据库
	 */
	
	public List queryPerson(String sql) {
		ArrayList<String> arrayList = new ArrayList<>(); 
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				arrayList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	public ArrayList<Date> queryDateDay(String sql) {
		ArrayList<Date> arrayList = new ArrayList<>(); 
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				arrayList.add(rs.getDate(1));
				System.out.println("DataBaseOperation="+rs.getDate(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public DayTableBeen queryTimeRecords(String sql) {
		ArrayList<Date> arrayList = new ArrayList<>(); 
		DayTableBeen dtb = new DayTableBeen();
		String name = "";
		float workHour = 0.00f, overTime = 0.00f,absence_time = 0.00f;
		boolean exception = false;
		int category = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				arrayList.add(formatGetDate.parse(rs.getString(1)));
				category = rs.getInt(2);
				name = rs.getString(3);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("size ="+arrayList.size());
		if (arrayList.size() > 1) {
			System.out.println("0="+arrayList.get(0));
			System.out.println("1="+arrayList.get(arrayList.size() - 1));
			dtb.startTime = arrayList.get(0);
			dtb.endTime = arrayList.get(arrayList.size() - 1);
			workHour = TimeUtil.getHour( arrayList.get(0), arrayList.get(arrayList.size() - 1));
		} else {
			workHour = 0.00f;
		}
		dtb.work_time = workHour;
		dtb.category = category;
		return dtb;
	}
	
	/**
	 * 删除数据库
	 */
	public void delete(String sql) {
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新数据库
	 */
	public void update(String sql) {
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		if (con != null) {
			try {
				con.close();
				System.out.println("close DataBase");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				con = null;
			}
		}
	}
	
	public ArrayList<TeamBean> queryTeam(String sql) {
		ArrayList<TeamBean> arrayList = new ArrayList<>(); 
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				TeamBean teamBean = new TeamBean();
				teamBean.teamName = rs.getString(1);
				teamBean.personNum = rs.getInt(2);
				teamBean.totalOverTime = rs.getFloat(3);
				teamBean.averPersonOverTime = rs.getFloat(4);
				teamBean.normalDayOverTime = rs.getFloat(5);
				teamBean.weekendOVerTime = rs.getFloat(6);
				teamBean.actuallyWorkdayNum = rs.getFloat(7);
				teamBean.averDayOverTime = rs.getFloat(8);
				arrayList.add(teamBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public int queryPersonNum(String sql) {
		int num = 0; 
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	
	public ArrayList<StructModify> queryModifyTable (String sql) {
		ArrayList <StructModify> arrayList = new ArrayList<>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				StructModify structModify = new StructModify();
				 structModify.name = rs.getString(1);
				 structModify.workTime = rs.getFloat(2);
				 structModify.strDateDay = rs.getString(3);
				 structModify.leaveDay = rs.getFloat(4);
				 arrayList.add(structModify);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return arrayList;
	}
	
	public ArrayList<PersonMonthBean> queryNameListandDateNum(String sql){
		ArrayList <PersonMonthBean> arrayList = new ArrayList<>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				PersonMonthBean pmb= new PersonMonthBean();
				pmb.name = rs.getString(1);
				pmb.dayNum = rs.getInt(2);
				pmb.work_time = rs.getFloat(3);
				pmb.over_time = rs.getFloat(4);
				arrayList.add(pmb);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return arrayList;
	}
	
	public float queryDateNum(String sql){
		float num = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getFloat(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return num;
	}
	
	public float querySumTime(String sql){
		float sum = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sum = rs.getFloat(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return sum;
	}
	
	public ArrayList<String> queryString(String sql) {
		ArrayList<String> arrayList = new ArrayList<>(); 
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				arrayList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public int queryInt(String sql) {
		int num = 0; 
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	
	public float queryFloat (String sql) {
		float num = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				num = rs.getFloat(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return num;
	}
	
	public  ArrayList<PersonOfMonth> queryPersonOfMonth(String sql) {
		ArrayList<PersonOfMonth> arrayList = new ArrayList<PersonOfMonth>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				PersonOfMonth pom = new PersonOfMonth();
				pom.personName = rs.getString(1);
				pom.place = rs.getString(2);
				pom.team = rs.getString(3);
				pom.module = rs.getString(4);
				pom.leader = rs.getString(5);
				pom.startTime = rs.getString(6);
				pom.endTime = rs.getString(7);
				pom.onsite = rs.getInt(8);
				pom.actuallyWorkdayNum = rs.getFloat(9);
				pom.workdayWorkNum = rs.getFloat(10);
				pom.shouldWorkNum = rs.getFloat(11);
				pom.absenceDayNum = rs.getFloat(12);
				pom.weekendWorkNum = rs.getFloat(13);
				pom.totalOverTime = rs.getFloat(14);
				pom.normalOverTime = rs.getFloat(15);
				pom.weekendOverTime = rs.getFloat(16);
				arrayList.add(pom);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arrayList;   
	}
	
	
	public ArrayList<DayTableBeen> queryPersonOfDay(String sql) {
		ArrayList<DayTableBeen> arrayList = new ArrayList<DayTableBeen>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				DayTableBeen dtb = new DayTableBeen();
				dtb.name =  rs.getString(1);
				dtb.place = rs.getString(2);
				dtb.date = rs.getDate(3);
				dtb.strStartTime = rs.getString(4);
				dtb.strEndTime = rs.getString(5);
				dtb.category = rs.getInt(6);
				dtb.work_time = rs.getFloat(7);
				dtb.over_time = rs.getFloat(8);
				arrayList.add(dtb);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<LeavesOfPerson>queryLeavesOfPerson(String sql) {
		ArrayList<LeavesOfPerson> arrayList = new ArrayList();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				LeavesOfPerson lop = new LeavesOfPerson();
				lop.name = rs.getString(1);
				lop.place= rs.getString(2);
				lop.category = rs.getString(3);
				lop.startTime = rs.getString(4);
				lop.endTime = rs.getString(5);
				lop.leaveDay = rs.getFloat(6);
				arrayList.add(lop);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<LeavesOfPerson>queryTotalAbsence(String sql) {
		ArrayList<LeavesOfPerson> arrayList = new ArrayList<>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				LeavesOfPerson lop = new LeavesOfPerson();
				lop.name = rs.getString(1);
				lop.leaveDay= rs.getFloat(2);
				arrayList.add(lop);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<DateBean> queryModityDateCategory(String sql) {
		ArrayList<DateBean> arrayList = new ArrayList<DateBean>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				DateBean dateBeen = new DateBean();
				dateBeen.date = rs.getString(1);
				dateBeen.category = rs.getInt(2);
				arrayList.add(dateBeen);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<ModifyRecordsBean> queryModityRecords(String sql) {
		ArrayList<ModifyRecordsBean> arrayList = new ArrayList<ModifyRecordsBean>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ModifyRecordsBean mrb = new ModifyRecordsBean();
				mrb.name = rs.getString(1);
				mrb.work_time = rs.getFloat(2);
				mrb.dateDay = rs.getDate(3);
				arrayList.add(mrb);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<RecordsUtil> queryRecords(String sql) {
		ArrayList<RecordsUtil> arrayList = new ArrayList<RecordsUtil>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				RecordsUtil ru = new RecordsUtil();
				ru.serial = rs.getString(1);
				ru.id_card =rs.getString(2);
				ru.name = rs.getString(3);
				ru.team_name = rs.getString(4);
				ru.datetime = rs.getString(5);
				ru.dateDay = rs.getString(6);
				ru.site = rs.getString(7);
				ru.pass = rs.getString(8);
				ru.description = rs.getString(9);
				ru.file_name = rs.getString(10);
				ru.category = rs.getInt(11);
				arrayList.add(ru);
				//break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
}
