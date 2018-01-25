package bean;

import java.util.Date;

public class LeavesOfPerson {
	public String name;
	public String place;
	public String category;
	public String startTime;
	public String endTime;
	public float leaveDay;
	
	public void clear() {
		name = null;
		place = null;
		category = null;
		startTime = null;
		endTime = null;
		leaveDay = 0.0f;
	}
}
