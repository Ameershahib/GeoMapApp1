package info.tawsoft.osmium.models;

public class Repeating {
	private int eventID;
	private String Type;
	private String Date;
	
	public Repeating(int evID,String ty,String da){
		
		eventID=evID;
		Type=ty;
		Date=da;
		
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

}
