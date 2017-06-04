package info.tawsoft.osmium.models;

public class EventDates {

	private int eventID;
	private String edate;
	
	public EventDates(int evID,String eD){
		eventID=evID;
		edate=eD;
			
	}
	
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	
	
}
