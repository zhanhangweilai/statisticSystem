package com.statistic.readFile;

import java.util.Date;

public class DayTableBeen {
	public String name;
	public float work_time;
	public float over_time;
	public Date date;
	public boolean exception;
	public float absence_time;
	public int category = 0;
	public Date startTime;
	public Date endTime;
	public String place;
	public String strStartTime;
	public String strEndTime;
	
	public void clear() {
		name = null;
		work_time = 0.0f;
		over_time = 0.0f;
		date = null;
		exception = false;
		absence_time = 0.0f;
		category = 0;
		startTime = null;
		endTime  = null;
		place = "";
	}
}
