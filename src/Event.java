import java.sql.Date;
import java.sql.Time;


public class Event {
	String name;
	String description;
	Date date;
	String address;
	Time time_start;
	Time time_end;
	int vol_needed;
	int vol_filled;
	@SuppressWarnings("deprecation")
	public Event(String name, String description, String date, String address, int time_S, int time_E, int vol_needed, int vol_filled ) {
		this.name = name;
		this.description = description;
		//this.date = new Date();
		this.address = address;
		this.time_start = new Time(time_S,0,0);
		this.time_end   = new Time(time_E,0,0);
		this.vol_needed = vol_needed;
		this.vol_filled = vol_filled;
	}
	String getName() {
		return this.name;
	}
	void setName(String name) {
		this.name = name;
	}
	String getDescription() {
		return this.description;
	}
	void setDescription(String description) {
		this.description = description;
	}
	String getDate() {
		return this.date.toString();
	}
	void setDate(String date) {
		//this.date = new Date();
	}
	String getAddress() {
		return this.address;
	}
	void setAddress(String address) {
		this.address = address;
	}
	String getTimeStart() {
		return this.time_start.toString();
	}
	void setStartTime(int time) {
		
	}
	
}
