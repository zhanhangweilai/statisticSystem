package com.statistic.readFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecordsUtil {
	public String serial;
	public String id_card;
	public String name;
	public String team_name;
	public Date date;
	public String datetime;//具体到秒
	public String site;
	public String pass;
	public String description;
	public String file_name;
	public int category =-1;
	public String dateDay;//具体到天
	public boolean isInsert = false;

	public void clear() {
		serial = null;
		id_card = null;
		name = null;
		team_name = null;
		date = null;
		datetime = null;
		site = null;
		pass =null;
		description = null;
		file_name = null;
		category = -1;
		isInsert = false;
	}
	public void getDataAndTime() {
		DateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat formatGetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] temp;
		temp = datetime.split("\\s+");
		dateDay = temp[0];
		try {
			if (datetime.contains("-")) {
				date = formatGetDate.parse(datetime);
			} else {
				date = formatDate.parse(datetime);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
			  category = 0;
			 } else  {
			  category = 1;
			 }
		} catch (ParseException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
