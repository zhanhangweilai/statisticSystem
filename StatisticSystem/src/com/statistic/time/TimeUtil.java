package com.statistic.time;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
	private static DateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static DateFormat formatGetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat formatDateWithoutTime = new SimpleDateFormat("yyyy/MM/dd");
	private static DateFormat formatGetDateWithoutTime = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat formatGetDateWithoutSeconds = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private static String mStartWorkTime ;
	private static String mEndWorkTime ;
	private static final float STANDARDTIEM = 8.0f;

	/**
	 * 适配不标准的时间为标准的时间格式
	 * @param string 不标准的日期
	 * @return 标准格式的日期
	 */
	public static String formatStandardTime(String string) {
		String strDate = null ;
		try {
		 strDate = DateToString(formatGetDateWithoutSeconds.parse(string));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Original file is error");
			e.printStackTrace();
		}
		System.out.println("TimeUtil,"+strDate);
		return strDate;
	}
	public static String getStartWorkTime(Date date) {
		getStandardWorkTime(date);
		return mStartWorkTime;
	}
	/**
	 * 传入当天的日期得到当天标准下班时间，日期如：yy-MM-dd 00:00:00
	 * @param date
	 * @return
	 */
	public static String getEndWorkTime(Date date) {
		getStandardWorkTime(date);
		return mEndWorkTime;
	}
	private static void getStandardWorkTime(Date date) {
		long time  = date.getTime();
		long nh = 1000 * 60 * 60;
		Date startWorkTime = new Date();
		startWorkTime.setTime( (long) ((9.5 * nh) + time));
		Date endWorkTime = new Date();
		endWorkTime.setTime((long)(18.5 * nh + time));
		mStartWorkTime = formatGetDate.format(startWorkTime);
		mEndWorkTime = formatGetDate.format(endWorkTime);
	}
	
	private static float getRandomNum () {
		float randomNum = 0;
		for (int i = 0; i < 1000; i++) {
			float random1 = new Random().nextFloat();
			float random2 = new Random().nextFloat();
			randomNum = random1 - random2;
			if (randomNum < 0.5 && randomNum > 0.1) {
				break;
			} else {
				randomNum = 0.4f;
			}
		}
		return randomNum;
	}
	
	public static String getRandomWorkStartTime(Date date) {
		long time  = date.getTime();
		float startTime = 9.0f;
		long nh = 1000 * 60 * 60;
		startTime += getRandomNum();
		Date startWorkTime = new Date();
		startWorkTime.setTime( (long) ((startTime * nh) + time));
		return formatGetDate.format(startWorkTime);
	}
	
	public static String getRandomWorkEndTime(Date date) {
		long time  = date.getTime();
		float endTime = 18.5f;
		long nh = 1000 * 60 * 60;
		endTime += getRandomNum();
		Date endWorkTime = new Date();
		endWorkTime.setTime((long)(endTime * nh + time));
		return formatGetDate.format(endWorkTime);
	}
	
	public static String DateToString(Date date) {
		String strDate = formatGetDate.format(date);
		return strDate;
	}
	
	public static String DateToString1(Date date) {
		String strDate = formatDate.format(date);
		return strDate;
	}
	
	public static Date StringToDateWithoutTime(String strDate) {
		Date date = null;
		try {
			if (strDate.contains("-")) {
				date = formatGetDateWithoutTime.parse(strDate);
			} else {
				date = formatDateWithoutTime.parse(strDate);
			} 
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
		}
		return date;
	}
	public static String DateToStringWithoutTime(Date date) {
		String strDate = formatDateWithoutTime.format(date);
		return strDate;
	}
	
	public static String DateToStringWithoutTime1(Date date) {
		String strDate = formatGetDateWithoutTime.format(date);
		return strDate;
	}
	
	public static Date StringToDate(String strDate) {
		Date date = null;
		try {
			if (strDate.contains("-")) {
				date = formatGetDate.parse(strDate);
			} else {
				date = formatDate.parse(strDate);
			} 
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
		}
		return date;
	}
	public static Date getDateWithoutTime(String dateTime) {
		String[] temp = dateTime.split("\\s+");
		String dateDay = temp[0];
		Date date;
		try {
			if (dateDay.contains("-")) {
				date = formatGetDateWithoutTime.parse(dateDay);
			} else {
				date = formatDateWithoutTime.parse(dateDay);
			} 
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
		}
		return date;
	}
	public static ArrayList<Date> getAllDateBetween (Date firstDate, Date endDate) {
		ArrayList <Date> arrayList = new ArrayList();
		float hour = 0.00f;
		float tempHour = 0.00f;
		long dayTime = 1000 * 60 * 60 * 24;
		long time = 0;
		int num;
		Date firstDay = getDateWithoutTime(formatGetDate.format(firstDate));
		Date endDay = getDateWithoutTime(formatGetDate.format(endDate));
		if (firstDay.equals(endDay)) {
			arrayList.add(firstDay);
		} else if (endDay.after(firstDay)) {
			hour = getHour (firstDate, endDate);
			tempHour = getHour(endDay,endDate);
			float a = hour - tempHour;
			System.out.println("hour = "+hour);
			System.out.println("temphour = "+tempHour);
			System.out.println("a = "+a);
			num =(int) (hour - tempHour) / 24;
			for (int i = 0; i <= num+1; i++) {
				Date date = new Date();
				time = firstDay.getTime();
				time += (i * dayTime);
				date.setTime(time);
				arrayList.add(date);
			}
		}
		for (int i = 0;i<arrayList.size();i++ ) {
			Date date1 = arrayList.get(i);
			System.out.println("TimeUtil.date1="+date1);
		}
		return arrayList;
	}
	public static float getHour(Date firstDate, Date endDate) {
		float hour = 0.00f;
		double diff = endDate.getTime() - firstDate.getTime();
		long nh = 1000 * 60 * 60;
		hour = (float) diff/nh;
		hour = MathUtil.floatParseTwo(hour);
		return hour;
	}
	
	public static float getDay(float hour) {
		float day = hour / STANDARDTIEM;
		day = MathUtil.floatParseOne(day);
		return day;
	}
	public static int getCategoryOfDay(Date date) {
		int category;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
		  category = 0;
		 } else  {
		  category = 1;
		 }
		return category;
	}
	
	public static boolean isSameDay(Date date1, Date date2){
		String strDate1 = formatGetDateWithoutTime.format(date1);
		String strDate2 = formatGetDateWithoutTime.format(date2);
		if (strDate1.equals(strDate2)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static int grepDateTime (String strDate) {
		int count  = 0;
		Pattern pattern = Pattern.compile(":");
		Matcher matcher = pattern.matcher(strDate);
		while (matcher.find()) {
			count++;
		}
		return count;
	}
	
	/**
	 * 得到某月所有天数
	 * @param date 某月的某个日期
	 * @return
	 */
	public static ArrayList<String> getAllDayOfMonth (Date date) {
		float hour = 0.00f;
		float tempHour = 0.00f;
		long dayTime = 1000 * 60 * 60 * 24;
		long time = 0;
		int num;
		ArrayList<Date> arrayList1 = new ArrayList<Date>();
		ArrayList<String> arrayList = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDayOfMonth = calendar.getTime();  
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDayOfMonth = calendar.getTime();
		System.out.println("firstday = "+firstDayOfMonth);
		System.out.println("lastday = "+lastDayOfMonth);
		hour = getHour(firstDayOfMonth, lastDayOfMonth);
		num = (int)(hour / 24);
		System.out.println("num = "+num);
		System.out.println("firstday = "+firstDayOfMonth.getDay());
		for (int i = 0; i <= num; i++) {
			Date date1 = new Date();
			time = firstDayOfMonth.getTime();
			time += (i * dayTime);
			date1.setTime(time);
			arrayList.add(DateToString(date1));
		}
		return arrayList;
	}
	/**
	 * getAllDateWithoutTimeBetween different with getAllDateBetween()
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static ArrayList<String> getAllDayBetween (Date date1, Date date2) {
		float hour = 0.00f;
		float tempHour = 0.00f;
		long dayTime = 1000 * 60 * 60 * 24;
		long time = 0;
		int num;
		ArrayList<Date> arrayList1 = new ArrayList<Date>();
		ArrayList<String> arrayList = new ArrayList<String>();
		hour = getHour(date1, date2);
		num = (int)(hour / 24);
		for (int i = 0; i <= num; i++) {
			Date date3 = new Date();
			time = date1.getTime();
			time += (i * dayTime);
			date3.setTime(time);
			arrayList.add(DateToString(date3));
		}
		return arrayList;
	}
	
	public static Date getModifyDateTime(Date date, float hour) {
		long dayTime = (long) (1000 * 60 * 60 * hour);
		long time = 0;
		Date endDataTime = new Date();
		time = date.getTime() + dayTime;
		endDataTime.setTime(time);
		return endDataTime;
	}
	
	public static String formatStringToString(String strDate) {
		Date date = StringToDate(strDate);
		strDate = formatGetDateWithoutSeconds.format(date);
		return strDate;
	}
	
	public static String formatStringToStringWithoutTime(String strDate) {
		Date date = null;
		try {
			if (strDate.contains("-")) {
				date = formatGetDateWithoutTime.parse(strDate);
			} else {
				date = formatDateWithoutTime.parse(strDate);
			} 
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
		}
		strDate = formatDateWithoutTime.format(date);
		return strDate;
	}
}
