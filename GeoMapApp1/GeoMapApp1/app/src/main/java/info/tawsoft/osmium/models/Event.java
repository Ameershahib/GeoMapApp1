package info.tawsoft.osmium.models;

public class Event {
	

	int eventID;
	String Eventname;
	String EventDesc;
	String PlaceDesc;
	String repStatus;
	String CurrentStatus;
	String Radius;
	String lat;
	String lng;
	String InOut;
	
	public Event(int eID,String Ename,String EDesc,String pDesc,String repSt,String curr,String Rad,String la,String lo,String IO)
	{
		eventID=eID;
		Eventname=Ename;
		EventDesc=EDesc;
		PlaceDesc=pDesc;
		repStatus=repSt;
		CurrentStatus=curr;
		Radius=Rad;
		lat=la;
		lng=lo;
		InOut=IO;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getEventname() {
		return Eventname;
	}

	public void setEventname(String eventname) {
		Eventname = eventname;
	}

	public String getEventDesc() {
		return EventDesc;
	}

	public void setEventDesc(String eventDesc) {
		EventDesc = eventDesc;
	}

	public String getPlaceDesc() {
		return PlaceDesc;
	}

	public void setPlaceDesc(String placeDesc) {
		PlaceDesc = placeDesc;
	}

	public String getRepStatus() {
		return repStatus;
	}

	public void setRepStatus(String repStatus) {
		this.repStatus = repStatus;
	}

	public String getCurrentStatus() {
		return CurrentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		CurrentStatus = currentStatus;
	}

	public String getRadius() {
		return Radius;
	}

	public void setRadius(String radius) {
		Radius = radius;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getInOut() {
		return InOut;
	}

	public void setInOut(String inOut) {
		InOut = inOut;
	}

	

}
