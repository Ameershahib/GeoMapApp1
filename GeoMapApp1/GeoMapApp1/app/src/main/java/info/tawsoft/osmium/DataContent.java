package info.tawsoft.osmium;

import java.util.ArrayList;

import info.tawsoft.osmium.models.Event;
import info.tawsoft.osmium.models.EventDates;
import info.tawsoft.osmium.models.OneTime;
import info.tawsoft.osmium.models.ProximityTable;
import info.tawsoft.osmium.models.Repeating;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataContent {

	private static String DATABASE_NAME = "geoMapApp";
	private static int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("create table Event "
					+ "(eventID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "Eventname TEXT NOT NULL," + "EventDesc TEXT NOT NULL,"
					+ "PlaceDesc TEXT NOT NULL," + "repStatus TEXT NOT NULL,"
					+ "CurrentStatus TEXT NOT NULL,"
					+ "radius INTEGER NOT NULL," + "lat TEXT NOT NULL,"
					+ "long TEXT NOT NULL,InOut TEXT NOT NULL);");

			db.execSQL("create table Repeating" + "(eventID INTEGER NOT NULL,"
					+ "Type TEXT NOT NULL,Date TEXT NOT NULL);");

			db.execSQL("create table OneTime (eventID INTEGER NOT NULL,"
					+ "Date TEXT NOT NULL); ");

			db.execSQL("create table EventDates (eventID INTEGER NOT NULL,"
					+ "edate TEXT NOT NULL); ");
			
			db.execSQL("create table ProximityTable (eventID INTEGER NOT NULL,"
					+ "requestcode INTEGER PRIMARY KEY AUTOINCREMENT,status TEXT NOT NULL); ");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS Event");
			db.execSQL("DROP TABLE IF EXISTS Repeating");
			db.execSQL("DROP TABLE IF EXISTS OneTime");
			db.execSQL("DROP TABLE IF EXISTS EventDates");
			db.execSQL("DROP TABLE IF EXISTS ProximityTable");
			
			onCreate(db);
		}
	}

	public DataContent(Context c) {
		ourContext = c;
	}

	public DataContent open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public float insertNewEvent(String eventname, String eventDesc,
			String placeDesc, String repStatus, String currentStatus,
			String radius, Double lat, Double lon, String inout) {

		ContentValues cv = new ContentValues();

		cv.put("Eventname", eventname);
		cv.put("EventDesc", eventDesc);
		cv.put("PlaceDesc", placeDesc);
		cv.put("repStatus", repStatus);
		cv.put("CurrentStatus", currentStatus);
		cv.put("radius", radius);
		cv.put("lat", lat.toString());
		cv.put("long", lon.toString());
		cv.put("InOut", inout);

		return ourDatabase.insert("Event", null, cv);

	}

	public float insertNewRepeating(int eventID, String type, String date) {

		ContentValues cv = new ContentValues();
		cv.put("eventID", eventID);
		cv.put("Type", type);
		cv.put("Date", date);

		return ourDatabase.insert("Repeating", null, cv);
	}

	public float insertNewOneTime(int eventID, String Date) {
		ContentValues cv = new ContentValues();
		cv.put("eventID", eventID);
		cv.put("Date", Date);

		return ourDatabase.insert("OneTime", null, cv);
	}

	public float insertNewEventDates(int eventID, String edate) {
		ContentValues cv = new ContentValues();
		cv.put("eventID", eventID);
		cv.put("edate", edate);

		return ourDatabase.insert("EventDates", null, cv);
	}

	// get Last Event
	public String getLastEvent() {
		String[] columns = new String[] { "eventID", "Eventname", "PlaceDesc",
				"repStatus", "CurrentStatus", "radius", "lat", "long" };
		// Cursor c = ourDatabase.query("Event", columns, null, null, null,
		// null, null);
		Cursor c = ourDatabase.rawQuery(
				"SELECT eventID FROM Event ORDER BY eventID DESC LIMIT 1;",
				null);

		String id = "";
		if (c != null) {
			c.moveToFirst();
			id = c.getString(0);

		}
		return id;
	}

	public String allEventNames() {
		String data = "";
		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			data += c.getString(0) + " " + c.getString(1) + " "
					+ c.getString(2) + " " + c.getString(3) + " "
					+ c.getString(4) + " " + c.getString(5) + " "
					+ c.getString(6) + " " + c.getString(7) + ""+ c.getString(8)+ c.getString(9)+"\n\n";
		}

		return data;
	}

	public String getAllRepeating() {
		String data = "";
		Cursor c = ourDatabase.rawQuery("SELECT * FROM Repeating;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			data += c.getString(0) + " " + c.getString(1) + " "
					+ c.getString(2) + "\n";
		}

		return data;
	}

	public String getAllOneTime() {
		String data = "";
		Cursor c = ourDatabase.rawQuery("SELECT * FROM OneTime;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			data += c.getString(0) + " " + c.getString(1) + "\n";
		}

		return data;
	}

	public String getAllEventDates() {
		String data = "";
		Cursor c = ourDatabase.rawQuery("SELECT * FROM EventDates;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			data += c.getString(0) + " " + c.getString(1) + "\n";
		}

		return data;
	}

	public ArrayList<Event> initializeMapAll() {

		ArrayList<Event> ev = new ArrayList<Event>();

		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Event e = new Event(Integer.parseInt(c.getString(0)),
					c.getString(1), c.getString(2), c.getString(3),
					c.getString(4), c.getString(5), c.getString(6),
					c.getString(7), c.getString(8), c.getString(9));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<Event> initializeMap() {

		ArrayList<Event> ev = new ArrayList<Event>();

		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where CurrentStatus='pending';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Event e = new Event(Integer.parseInt(c.getString(0)),
					c.getString(1), c.getString(2), c.getString(3),
					c.getString(4), c.getString(5), c.getString(6),
					c.getString(7), c.getString(8), c.getString(9));
			ev.add(e);
		}

		return ev;
	}

	
	public ArrayList<Event> initializeMapComplete() {

		ArrayList<Event> ev = new ArrayList<Event>();

		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where CurrentStatus='completed';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Event e = new Event(Integer.parseInt(c.getString(0)),
					c.getString(1), c.getString(2), c.getString(3),
					c.getString(4), c.getString(5), c.getString(6),
					c.getString(7), c.getString(8), c.getString(9));
			ev.add(e);
		}

		return ev;
	}
	
	
	public Event getEventInfo(Double lat, Double longi) {

		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where lat='"
				+ lat.toString().trim() + "' and long='"
				+ longi.toString().trim() + "';", null);
		c.moveToNext();
		Event e = new Event(Integer.parseInt(c.getString(0)), c.getString(1),
				c.getString(2), c.getString(3), c.getString(4), c.getString(5),
				c.getString(6), c.getString(7), c.getString(8), c.getString(9));

		return e;

	}

	public Event getEventInfoByID(int eventID) {

		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where eventID='"+eventID+"';", null);
		c.moveToNext();
		Event e = new Event(Integer.parseInt(c.getString(0)), c.getString(1),
				c.getString(2), c.getString(3), c.getString(4), c.getString(5),
				c.getString(6), c.getString(7), c.getString(8), c.getString(9));

		return e;

	}

	
	public float UpdateEventInfo(String EID, String EventName,
			String Eventmessage, String radus, String INOut, String repStatus) {
		ContentValues cv = new ContentValues();
		cv.put("Eventname", EventName);
		cv.put("EventDesc", Eventmessage);
		cv.put("radius", radus);
		cv.put("InOut", INOut);
		cv.put("repStatus", repStatus);
		cv.put("CurrentStatus", "pending");

		return ourDatabase.update("Event", cv, "eventID =" + EID, null);

	}

	public float UpdateEventDates(int EID, String dates) {
		ContentValues cv = new ContentValues();
		cv.put("eventID", EID);
		cv.put("edate", dates);

		return ourDatabase.update("EventDates", cv, "eventID =" + EID, null);

	}

	public float UpdateOneTime(int EID,String date){
		ContentValues cv = new ContentValues();
		cv.put("eventID", EID);
		cv.put("Date", date);

		return ourDatabase.update("OneTime", cv, "eventID =" + EID, null);

	}
	

	public float UpdateRepeating(int EID,String Type,String Date){
		ContentValues cv = new ContentValues();
		cv.put("eventID", EID);
		cv.put("Type", Type);
		cv.put("Date", Date);

		return ourDatabase.update("Repeating", cv, "eventID =" + EID, null);

	}
	
	public boolean getEventDates(int eventID) {

		Cursor c = ourDatabase.rawQuery("SELECT eventID FROM EventDates where eventID='" + eventID+ "';", null);
		int co=0;
		try{
		co=c.getCount();
		}
		catch(Exception e){}
		if (co==0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean checkAvailRepeating(int eventID) {
		Cursor c = ourDatabase.rawQuery("SELECT eventID FROM Repeating where eventID='" + eventID+ "';", null);
		int co=0;
		try{co=c.getCount();}
		catch(Exception e){}
		if (co==0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checkAvailOneTime(int eventID) {
		Cursor c = ourDatabase.rawQuery("SELECT eventID FROM OneTime where eventID='" + eventID+ "';", null);
		
		int co=0;
		try{co=c.getCount();}
		catch(Exception e){}
		if (co==0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean checkDates(int eventID) {
		Cursor c = ourDatabase.rawQuery("SELECT eventID FROM EventDates where eventID='" + eventID+ "';", null);
		
		int co=0;
		try{
		co=c.getCount();
		}
		catch(Exception e){}
		if (co==0) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public void dropRepeating(int eventID){
		ourDatabase.delete("Repeating","eventID=" + eventID, null);
	}

	public void dropOneTime(int eventID){
		ourDatabase.delete("OneTime", "eventID=" + eventID, null);
	}
	public void dropDates(int eventID){
		ourDatabase.delete("EventDates", "eventID=" + eventID, null);
	}
	
	public void dropEvent(int eventID){
		ourDatabase.delete("Event", "eventID=" + eventID, null);
	}
	
	public ArrayList<Event> getAllFromEvent(String status) {

		ArrayList<Event> ev = new ArrayList<Event>();

		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where CurrentStatus='"+status+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Event e = new Event(Integer.parseInt(c.getString(0)),
					c.getString(1), c.getString(2), c.getString(3),
					c.getString(4), c.getString(5), c.getString(6),
					c.getString(7), c.getString(8), c.getString(9));
			ev.add(e);
		}

		return ev;
	}
	
	
	public ArrayList<Event> getAllEventNamesModel(){
		ArrayList<Event> ev = new ArrayList<Event>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Event e = new Event(Integer.parseInt(c.getString(0)),
					c.getString(1), c.getString(2), c.getString(3),
					c.getString(4), c.getString(5), c.getString(6),
					c.getString(7), c.getString(8), c.getString(9));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<Repeating> getAllRepeatingModel(){
		ArrayList<Repeating> ev = new ArrayList<Repeating>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM Repeating;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Repeating e = new Repeating(Integer.parseInt(c.getString(0)),c.getString(1), c.getString(2));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<OneTime> getAllOneTimeModel(){
		ArrayList<OneTime> ev = new ArrayList<OneTime>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM OneTime;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			OneTime e = new OneTime(Integer.parseInt(c.getString(0)),c.getString(1));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<EventDates> getAllDatesModel(){
		ArrayList<EventDates> ev = new ArrayList<EventDates>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM EventDates;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			EventDates e = new EventDates(Integer.parseInt(c.getString(0)),c.getString(1));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<EventDates> getAllDatesModel(int eid){
		ArrayList<EventDates> ev = new ArrayList<EventDates>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM EventDates where eventID='"+eid+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			EventDates e = new EventDates(Integer.parseInt(c.getString(0)),c.getString(1));
			ev.add(e);
		}

		return ev;
	}
	
	//One record
	public ArrayList<Event> getEventNamesModel(int eventID){
		ArrayList<Event> ev = new ArrayList<Event>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where eventID='"+eventID+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Event e = new Event(Integer.parseInt(c.getString(0)),
					c.getString(1), c.getString(2), c.getString(3),
					c.getString(4), c.getString(5), c.getString(6),
					c.getString(7), c.getString(8), c.getString(9));
			ev.add(e);
		}

		return ev;
	}
	
	//One record overloading
		public ArrayList<Event> getEventNamesModel(int eventID,String status){
			ArrayList<Event> ev = new ArrayList<Event>();
			Cursor c = ourDatabase.rawQuery("SELECT * FROM Event where eventID='"+eventID+"' and CurrentStatus='"+status+"';", null);

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Event e = new Event(Integer.parseInt(c.getString(0)),
						c.getString(1), c.getString(2), c.getString(3),
						c.getString(4), c.getString(5), c.getString(6),
						c.getString(7), c.getString(8), c.getString(9));
				ev.add(e);
			}

			return ev;
		}
		
	public ArrayList<Repeating> getRepeatingModel(int eventID){
		ArrayList<Repeating> ev = new ArrayList<Repeating>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM Repeating where eventID='"+eventID+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Repeating e = new Repeating(Integer.parseInt(c.getString(0)),c.getString(1), c.getString(2));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<OneTime> getOneTimeModel(int eventID){
		ArrayList<OneTime> ev = new ArrayList<OneTime>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM OneTime where eventID='"+eventID+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			OneTime e = new OneTime(Integer.parseInt(c.getString(0)),c.getString(1));
			ev.add(e);
		}

		return ev;
	}
	
	public ArrayList<EventDates> getDatesModel(int eventID){
		ArrayList<EventDates> ev = new ArrayList<EventDates>();
		Cursor c = ourDatabase.rawQuery("SELECT * FROM EventDates where eventID='"+eventID+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			EventDates e = new EventDates(Integer.parseInt(c.getString(0)),c.getString(1));
			ev.add(e);
		}

		return ev;
	}

	public float updateRepeatingWeeklyAndMonthly(int eventID, String date) {
		ContentValues cv = new ContentValues();
		cv.put("eventID", eventID);
		cv.put("Date", date);

		return ourDatabase.update("Repeating", cv, "eventID =" + eventID, null);

	}
	
	
	//ProximityTable related stuff
	public ArrayList<ProximityTable> getAllProximityTable(){
		
		ArrayList<ProximityTable> pt=new ArrayList<ProximityTable>();
		
		Cursor c = ourDatabase.rawQuery("SELECT * FROM ProximityTable;", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ProximityTable e = new ProximityTable(Integer.parseInt(c.getString(0)),Integer.parseInt(c.getString(1)),c.getString(2));
			pt.add(e);
		}

		
		return pt;
	}
	
	public ProximityTable getProximityTableOnID(int EventID,String status){
		
		
		Cursor c = ourDatabase.rawQuery("SELECT * FROM ProximityTable where eventID='"+EventID+"' and status='"+status+"';", null);
		ProximityTable e = null;
		if (c != null) {
			c.moveToNext();
			e = new ProximityTable(Integer.parseInt(c.getString(0)),Integer.parseInt(c.getString(1)),c.getString(2));
			
		}
	

		return e;
	}
	
	public int getLastRequestCodeFromProximityTable(){
		Cursor c = ourDatabase.rawQuery("SELECT requestcode FROM ProximityTable ORDER BY requestcode DESC LIMIT 1;",null);

		String id = "1";
		if (c != null) {
			c.moveToFirst();
			id = c.getString(0);

		}else{
			id = "1";
		}
		
		
		return Integer.parseInt(id);
		
	}
	
public ArrayList<ProximityTable> getProximityTableOnCompleted(String status){
		
		ArrayList<ProximityTable> pt=new ArrayList<ProximityTable>();
		
		Cursor c = ourDatabase.rawQuery("SELECT * FROM ProximityTable where status='"+status+"';", null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ProximityTable e = new ProximityTable(Integer.parseInt(c.getString(0)),Integer.parseInt(c.getString(1)),c.getString(2));
			pt.add(e);
		}

		
		return pt;
	}

public float insertNewProximityTable(int eventID,String status){
	
	ContentValues cv = new ContentValues();
	cv.put("eventID", eventID);
	cv.put("status", status);
	
	return ourDatabase.insert("ProximityTable", null, cv);
	
}

public float updateProximityTable(int eventID,String status){
	ContentValues cv = new ContentValues();
	cv.put("eventID", eventID);
	cv.put("status", status);

	return ourDatabase.update("ProximityTable", cv, "eventID =" + eventID, null);

}

public void dropProximityTable(int eventID){
	
	ourDatabase.delete("ProximityTable", "eventID=" + eventID, null);
	
}



public float updateEventrepStatus(int eventID,String status){
	ContentValues cv = new ContentValues();
	cv.put("CurrentStatus", status);

	return ourDatabase.update("Event", cv, "eventID =" + eventID, null);

}

}
