
import java.text.SimpleDateFormat;


public class Event {
	String name;
	String description;
	SimpleDateFormat date;
	String address;
	int time_start;
	int time_end;
	int vol_needed;
	int vol_filled;
	public Event(String name, String description, String date, String address, int time_S, int time_E, int vol_needed ) {
		this.name = name;
		this.description = description;
		//this.date = new Date();
		this.address = address;
		this.time_start = time_S;
		this.time_end   = time_E;
		this.vol_needed = vol_needed;
		this.vol_filled = 0;
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
		//this.date = date;
	}
	String getAddress() {
		return this.address;
	}
	void setAddress(String address) {
		this.address = address;
	}
	int getTimeStart() {
		return this.time_start;
	}
	void setTimeStart(int time) {
		this.time_start = time;
	}
	int getTimeEnd() {
		return this.time_start;
	}
	void setTimeEnd(int time) {
		this.time_start = time;
	}
	int getVolNeeded() {
		return this.vol_needed;
	}
	void addVol() {
		this.vol_filled++;
	}
	
}
