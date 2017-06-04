package info.tawsoft.osmium.models;

public class ProximityTable {
	
	int eventID;
	int requestcode;
	String status;
	
	public ProximityTable(int evID,int req,String sta){
		eventID=evID;
		requestcode=req;
		status=sta;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public int getRequestcode() {
		return requestcode;
	}

	public void setRequestcode(int requestcode) {
		this.requestcode = requestcode;
	}

}
