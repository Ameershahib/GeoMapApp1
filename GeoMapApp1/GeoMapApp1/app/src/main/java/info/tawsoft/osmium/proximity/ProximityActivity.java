package info.tawsoft.osmium.proximity;

import java.util.ArrayList;
import java.util.Calendar;

import info.tawsoft.osmium.DataContent;
import info.tawsoft.osmium.R;
import info.tawsoft.osmium.models.Event;
import info.tawsoft.osmium.models.EventDates;
import info.tawsoft.osmium.models.OneTime;
import info.tawsoft.osmium.models.Repeating;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class ProximityActivity extends Activity {

	String notificationTitle;
	String notificationContent;
	String tickerMessage;

	DataContent entry;

	String result = "";
	Event event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.proximityactivity);

		entry = new DataContent(ProximityActivity.this);

		boolean proximity_entering = getIntent().getBooleanExtra(
				LocationManager.KEY_PROXIMITY_ENTERING, true);

		String eventID = getIntent().getExtras().getString("EventID");

		setNotification(proximity_entering, eventID);
		
		finish();
	}

	public void setNotification(boolean proximity_entering, String evID) {

	//	Toast.makeText(getApplicationContext(), "Event ID is: " + evID,
	//			Toast.LENGTH_LONG).show();

		try {
			entry.open();
			Event ev = entry.getEventNamesModel(Integer.parseInt(evID),"completed").get(0);

			if (ev.getRepStatus().contains("whenever")) {
				
				//alert shown
				showNotification(proximity_entering,ev.getInOut());
				
			} else if (ev.getRepStatus().contains("Custom")) {

				//custom 
				
				if(DatesCheck(Integer.parseInt(evID))){
					//play sound
					setContentView(R.layout.proximityactivity);
					//Toast.makeText(getApplicationContext(), "matched",Toast.LENGTH_LONG).show();
					showNotification(proximity_entering,ev.getInOut());
				}
				else{
					finish();
				}
			} else if (ev.getRepStatus().contains("OneTime")) {

				
				//one time
				if(oneTimeCheck(Integer.parseInt(evID))){
					
					try{
						entry.open();
						
						entry.updateEventrepStatus(Integer.parseInt(evID), "completed");
						//Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();
					}
					catch(Exception e){
						e.printStackTrace();
					}
					finally{
						entry.close();	
					}
					setContentView(R.layout.proximityactivity);
					//play sound
					
					showNotification(proximity_entering,ev.getInOut());
				}
				else{
					finish();
				}
				
			} else if (ev.getRepStatus().contains("Repeating")) {

				//repeating
				if(RepeatingCheck()){
					setContentView(R.layout.proximityactivity);
					//play sound
					
					showNotification(proximity_entering,ev.getInOut());
				}
				else{
					finish();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entry.close();
		}

		
	}

	// one Time check
	public boolean oneTimeCheck(int eventID) {
		// Check one Time
		boolean isToday=false;
		
		Calendar c = Calendar.getInstance();

		int date = c.get(Calendar.DATE);

		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);

		String Fulldate = date + "/" + month + "/" + year;

		//Toast.makeText(getApplicationContext(), "Today is :" + Fulldate,
			//	Toast.LENGTH_LONG).show();

		try {
			entry.open();
			ArrayList<OneTime> oneAList = entry.getOneTimeModel(eventID);
			String dateFromDB = oneAList.get(0).getDate();

		//	Toast.makeText(
		//			getApplicationContext(),
			//		"Today is:" + Fulldate + "\n" + "Alarm should pop up on : "
			//				+ dateFromDB, Toast.LENGTH_LONG).show();

			if (dateFromDB.contentEquals(Fulldate.trim())) {
				Toast.makeText(getApplicationContext(), "Dates equal",
						Toast.LENGTH_LONG).show();
				
				isToday=true;

			} else {
			//	Toast.makeText(getApplicationContext(), "Dates not equal",
				//		Toast.LENGTH_LONG).show();

				isToday=false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			entry.close();
		}
		
		return isToday;
	}

	// Dates check
	public boolean DatesCheck(int EventID) {

		boolean isToday=false;
		
		String dateName = "";
		String datesFromDB = "";
		Calendar c = Calendar.getInstance();
		int i = c.get(Calendar.DAY_OF_WEEK);
		switch (i) {

		case 1:
			dateName = "SUNDAY";
			break;
		case 2:
			dateName = "MONDAY";
			break;
		case 3:
			dateName = "TUESDAY";
			break;
		case 4:
			dateName = "WEDNESDAY";
			break;
		case 5:
			dateName = "THURSDAY";
			break;
		case 6:
			dateName = "FRIDAY";
			break;
		case 7:
			dateName = "SATURDAY";
			break;

		}

		try {
			entry.open();
			ArrayList<EventDates> ed = entry.getDatesModel(EventID);
			datesFromDB = ed.get(0).getEdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entry.close();
		}
/*
		Toast.makeText(getApplicationContext(), dateName + "",
				Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), datesFromDB + "",
				Toast.LENGTH_LONG).show();*/
		if (datesFromDB.contains(dateName)) {
		//	Toast.makeText(getApplicationContext(), "Alarm......k",
			//		Toast.LENGTH_LONG).show();
			
			isToday=true;
		}
		else{
			isToday=false;
		}
		
		return isToday;
	}

	// Repeating check
	public boolean RepeatingCheck() {
		boolean isToday = false;
		String status;

		try {
			entry.open();
			ArrayList<Repeating> rep = entry.getRepeatingModel(1);

			status = rep.get(0).getType();
			int EventID = rep.get(0).getEventID();

	//		Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG)
					//.show();
//
			if (status.contains("daily")) {
			//	Toast.makeText(getApplicationContext(), "today is the day",
				//		Toast.LENGTH_LONG).show();
				isToday=true;

			} else if (status.contains("weekly")) {
				//Toast.makeText(getApplicationContext(), "Weekly we will check",
				//		Toast.LENGTH_LONG).show();

				String dateFromDB = rep.get(0).getDate();
			//	Toast.makeText(getApplicationContext(),
					//	"Alarm should popup on :" + dateFromDB,
					//	Toast.LENGTH_LONG).show();

				String[] dat = dateFromDB.split("/");

				Calendar cToday = Calendar.getInstance();
				cToday.set(cToday.get(Calendar.YEAR),
						(cToday.get(Calendar.MONTH) + 1),
						cToday.get(Calendar.DATE));

				Calendar cDatabase = Calendar.getInstance();
				cDatabase.set(Integer.parseInt(dat[2]),
						Integer.parseInt(dat[1]), Integer.parseInt(dat[0]));

				// cDatabase.set(2014,7,21);

				// What is needed
				if ((cToday.get(Calendar.DATE) == cDatabase.get(Calendar.DATE))
						&& (cToday.get(Calendar.YEAR) == cDatabase
								.get(Calendar.YEAR))
						&& ((cToday.get(Calendar.MONTH) + 1) == cDatabase
								.get(Calendar.MONTH))) {
					//Toast.makeText(getApplicationContext(), "yep",
					//		Toast.LENGTH_LONG).show();
					
					isToday=true;
				}

				Toast.makeText(getApplicationContext(),
						"Today is :" + cToday.getTime().toString() + "",
						Toast.LENGTH_LONG).show();
				// If time has passed
				if (cToday.after(cDatabase)) {

				
					Calendar newdate = cDatabase;

					while (newdate.before(cToday)) {
						newdate.add(Calendar.DATE, 7);
					}

					// Final Date
					// Toast.makeText(getApplicationContext(),
					// newdate.getTime().toString(), Toast.LENGTH_LONG).show();
					

					try {

						entry.open();
						float f = entry.updateRepeatingWeeklyAndMonthly(
								EventID, newdate.get(Calendar.DATE) + "/"
										+ newdate.get(Calendar.MONTH) + "/"
										+ newdate.get(Calendar.YEAR));
						Toast.makeText(getApplicationContext(), f + "",
								Toast.LENGTH_LONG).show();

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						entry.close();
					}
				}

			} else {
				
				String dateFromDB = rep.get(0).getDate();
				
				String[] dat = dateFromDB.split("/");

				Calendar cToday = Calendar.getInstance();
				cToday.set(cToday.get(Calendar.YEAR),
						(cToday.get(Calendar.MONTH) + 1),
						cToday.get(Calendar.DATE));

				Calendar cDatabase = Calendar.getInstance();
				cDatabase.set(Integer.parseInt(dat[2]),
						Integer.parseInt(dat[1]), Integer.parseInt(dat[0]));

				// cDatabase.set(2014,7,31);

				// What is needed
				if ((cToday.get(Calendar.DATE) == cDatabase.get(Calendar.DATE))
						&& (cToday.get(Calendar.YEAR) == cDatabase
								.get(Calendar.YEAR))
						&& ((cToday.get(Calendar.MONTH) + 1) == cDatabase
								.get(Calendar.MONTH))) {
					Toast.makeText(getApplicationContext(), "yep",
							Toast.LENGTH_LONG).show();

					isToday=true;
				}

				// If time has passed
				if (cToday.after(cDatabase)) {
					// New date adjustment
					Calendar newdate = cDatabase;

					while (newdate.before(cToday)) {
						newdate.add(Calendar.DATE, 30);
					}

				


					try {

						entry.open();
						float f = entry.updateRepeatingWeeklyAndMonthly(
								EventID, newdate.get(Calendar.DATE) + "/"
										+ newdate.get(Calendar.MONTH) + "/"
										+ newdate.get(Calendar.YEAR));
					
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						entry.close();
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entry.close();
		}

		return isToday;
	}
	
	public void showNotification(boolean proximity_entering,String inOut){
		if(inOut.contains("IO")){
			if (proximity_entering) {
				
				notificationTitle = "Proximity - Entry";
				notificationContent = "Entered the region";
				tickerMessage = "Entered the region";
			} else {
				
				notificationTitle = "Proximity - Exit";
				notificationContent = "Exited the region";
				tickerMessage = "Exited the region";
			}
		}
		else if(inOut.contains("I")){
			if (proximity_entering) {
				
				notificationTitle = "Proximity - Entry";
				notificationContent = "Entered the region";
				tickerMessage = "Entered the region";
			}
		}
		else if(inOut.contains("O")){
				
				notificationTitle = "Proximity - Exit";
				notificationContent = "Exited the region";
				tickerMessage = "Exited the region";
		}else{
			notificationTitle = "Proximity - Entry";
			notificationContent = "Entered the region";
			tickerMessage = "Entered the region";
		}
		

		Intent notificationIntent = new Intent(getApplicationContext(),
				NotificationView.class);
		notificationIntent.putExtra("content", notificationContent);

		/**
		 * This is needed to make this intent different from its previous
		 * intents
		 */

		notificationIntent.setData(Uri.parse("tel:/"
				+ (int) System.currentTimeMillis()));

		/**
		 * Creating different tasks for each notification. See the flag
		 * Intent.FLAG_ACTIVITY_NEW_TASK
		 */
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		/** Getting the System service NotificationManager */
		NotificationManager nManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		/** Configuring notification builder to create a notification */
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				getApplicationContext()).setWhen(System.currentTimeMillis())
				.setContentText(notificationContent)
				.setContentTitle(notificationTitle)
				.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
				.setTicker(tickerMessage).setContentIntent(pendingIntent);

		/** Creating a notification from the notification builder */
		Notification notification = notificationBuilder.build();

		/**
		 * Sending the notification to system. The first argument ensures that
		 * each notification is having a unique id If two notifications share
		 * same notification id, then the last notification replaces the first
		 * notification
		 * */
		nManager.notify((int) System.currentTimeMillis(), notification);
	}
}
