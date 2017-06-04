package info.tawsoft.osmium.models;

public class OneTime {
	private int eventID;
	private String Date;

	public OneTime(int evID,String da){
		eventID=evID;
		Date=da;
	}
	
	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

}
