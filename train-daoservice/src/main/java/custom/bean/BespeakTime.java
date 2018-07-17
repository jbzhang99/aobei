package custom.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BespeakTime implements Comparable<BespeakTime>{
	String before_hour;
	
	String after_minutes;

	String time_format;

	public String getBefore_hour() {
		return before_hour;
	}

	public void setBefore_hour(String before_hour) {
		this.before_hour = before_hour;
	}

	public String getAfter_minutes() {
		return after_minutes;
	}

	public void setAfter_minutes(String after_minutes) {
		this.after_minutes = after_minutes;
	}
	
	@Override
	public String toString() {
		return "BespeakTime [before_hour=" + before_hour + ", after_minutes=" + after_minutes + ", time_format="
				+ time_format + "]";
	}

	public String getTime_format() {
		return time_format;
	}

	public void setTime_format(String time_format) {
		this.time_format = time_format;
	}

	@Override
	public int compareTo(BespeakTime o) {
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
		int i = 0;
		try {
			Date date = sd.parse(this.getBefore_hour());
			i=date.compareTo(sd.parse(o.getBefore_hour()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	

}
