package info.tawsoft.osmium.proximity;


import info.tawsoft.osmium.DataContent;
import info.tawsoft.osmium.R;
import info.tawsoft.osmium.models.Event;
import info.tawsoft.osmium.models.EventDates;
import info.tawsoft.osmium.models.OneTime;
import info.tawsoft.osmium.models.Repeating;
import info.tawsoft.osmium.models.Settings;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class BroadcastReceiverProx extends BroadcastReceiver{
	
	
	String notificationTitle;
	String notificationContent;
	String tickerMessage;

	DataContent entry;

	String result = "";
	Event event;
	
	public static String filename = "MySharedString";
	SharedPreferences someData;
	

	int id;
	
	Settings set;
	
	Context con;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		someData = context.getSharedPreferences(filename, 0);

		con=context;
		setDataFromSettingsPref();
		
		
		id=intent.getExtras().getInt("EventID");
		
		entry = new DataContent(context.getApplicationContext());
		
		String k = LocationManager.KEY_PROXIMITY_ENTERING;
		// Key for determining whether user is leaving or entering
		boolean state = intent.getBooleanExtra(k, false);
		// Gives whether the user is entering or leaving in boolean form
		if (state) {
			
			
			//Toast.makeText(context, "Welcome to my Area ",Toast.LENGTH_LONG).show();
			//Toast.makeText(context,id+"", Toast.LENGTH_LONG).show();
			
			setNotification(state,id+"",context);
			
			
		} else {
			setNotification(state,id+"",context);
			//Toast.makeText(context,"Thank you for visiting my Area,come back again !!", Toast.LENGTH_LONG).show();

		}
	}
	
	
	public void setNotification(boolean proximity_entering, String evID,Context context) {

			
		try {
			entry.open();
			Event ev = entry.getEventNamesModel(Integer.parseInt(evID),"pending").get(0);

			
			//Toast.makeText(context, ev.getRepStatus(), Toast.LENGTH_LONG).show();
			
			if (ev.getRepStatus().contains("whenever")) {
				
				//alert shown
				showNotification(proximity_entering,ev.getInOut(),context);
				
			} else if (ev.getRepStatus().contains("Custom")) {

				//custom 
				
				if(DatesCheck(Integer.parseInt(evID))){
					
					showNotification(proximity_entering,ev.getInOut(),context);
				}
				else{

					
				}
			} else if (ev.getRepStatus().contains("OneTime")) {

				
				//one time
				if(oneTimeCheck(Integer.parseInt(evID))){
					
					try{
						entry.open();
						
						entry.updateEventrepStatus(Integer.parseInt(evID), "completed");
						
					}
					catch(Exception e){
						e.printStackTrace();
					}
					finally{
						entry.close();	
					}
					
					//play sound
					
					showNotification(proximity_entering,ev.getInOut(),context);
				}
				else{
				
				}
				
			} else if (ev.getRepStatus().contains("Repeating")) {

				//Toast.makeText(context, "playing success", Toast.LENGTH_LONG).show();
				//repeating
				if(RepeatingCheck(id)){
					
					//play sound
					
					showNotification(proximity_entering,ev.getInOut(),context);
				}
				else{
				
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


			try {
				entry.open();
				ArrayList<OneTime> oneAList = entry.getOneTimeModel(eventID);
				String dateFromDB = oneAList.get(0).getDate();

				if (dateFromDB.contentEquals(Fulldate.trim())) {
					
					
					isToday=true;

				} else {
			

					isToday=false;
				}

			} catch (Exception e) {
				
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
	
			if (datesFromDB.contains(dateName)) {
			
				isToday=true;
			}
			else{
				isToday=false;
			}
			
			return isToday;
		}

		// Repeating check
		public boolean RepeatingCheck(int id) {
			boolean isToday = false;
			String status;

			try {
				entry.open();
				ArrayList<Repeating> rep = entry.getRepeatingModel(id);

				status = rep.get(0).getType();
				int EventID = rep.get(0).getEventID();

				
				if (status.contains("daily")) {
				
					isToday=true;

				} else if (status.contains("weekly")) {
				
					String dateFromDB = rep.get(0).getDate();
				
					String[] dat = dateFromDB.split("/");

					Calendar cToday = Calendar.getInstance();
					cToday.set(cToday.get(Calendar.YEAR),
							(cToday.get(Calendar.MONTH) + 1),
							cToday.get(Calendar.DATE));

					Calendar cDatabase = Calendar.getInstance();
					cDatabase.set(Integer.parseInt(dat[2]),
							Integer.parseInt(dat[1]), Integer.parseInt(dat[0]));

					if ((cToday.get(Calendar.DATE) == cDatabase.get(Calendar.DATE))
							&& (cToday.get(Calendar.YEAR) == cDatabase
									.get(Calendar.YEAR))
							&& ((cToday.get(Calendar.MONTH) + 1) == cDatabase
									.get(Calendar.MONTH))) {
						
						
						isToday=true;
					}

				
					if (cToday.after(cDatabase)) {

					
						Calendar newdate = cDatabase;

						while (newdate.before(cToday)) {
							newdate.add(Calendar.DATE, 7);
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

				
					if ((cToday.get(Calendar.DATE) == cDatabase.get(Calendar.DATE))
							&& (cToday.get(Calendar.YEAR) == cDatabase
									.get(Calendar.YEAR))
							&& ((cToday.get(Calendar.MONTH) + 1) == cDatabase
									.get(Calendar.MONTH))) {

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
		
		public void showNotification(boolean proximity_entering,String inOut,Context context){
			if(inOut.contains("IO")){
				if (proximity_entering) {
					
					notificationTitle = "Proximity - Entry";
					notificationContent = "You have Entered to the region";
					tickerMessage = "Entered the region";
				} else {
					
					notificationTitle = "Proximity - Exit";
					notificationContent = "Exited from the region";
					tickerMessage = "Exited the region";
				}
			}
			else if(inOut.contains("I")){
				if (proximity_entering) {
					
					notificationTitle = "Proximity - Entry";
					notificationContent = "You have Entered to the region";
					tickerMessage = "Entered the region";
				}
			}
			else if(inOut.contains("O")){
					
					notificationTitle = "Proximity - Exit";
					notificationContent = "Exited from the region";
					tickerMessage = "Exited the region";
			}
			else{
				notificationTitle = "Proximity - Entry";
				notificationContent = "You have Entered to the region";
				tickerMessage = "Entered the region";
			}
			

			Intent notificationIntent = new Intent(context.getApplicationContext(),
					NotificationView.class);
			notificationIntent.putExtra("content", notificationContent);

			notificationIntent.putExtra("EventID", id);
		

			notificationIntent.setData(Uri.parse("tel:/"
					+ (int) System.currentTimeMillis()));

		
			PendingIntent pendingIntent = PendingIntent.getActivity(
					context.getApplicationContext(), 0, notificationIntent,
					Intent.FLAG_ACTIVITY_NEW_TASK);

			/** Getting the System service NotificationManager */
			NotificationManager nManager = (NotificationManager) context.getApplicationContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);

			/** Configuring notification builder to create a notification */
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					context.getApplicationContext()).setWhen(System.currentTimeMillis())
					.setContentText(notificationContent)
					.setContentTitle(notificationTitle)
					.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
					.setTicker(tickerMessage).setContentIntent(pendingIntent);
					
			// If alarm sound
			if (set.isChSettingsAlarmSound()) {
			//RingtoneManager.ge
				Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(context.getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
				notificationBuilder.setSound(defaultRintoneUri);
				
				//notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
				//notificationBuilder.setSound(Uri.parse("android.resource://info.tawsoft.osmium.proximity/"+ R.raw.back));
				//Toast.makeText(context, "Alarm is on", Toast.LENGTH_LONG).show();
			}
			else{
				//Toast.makeText(context, "Alarm is off", Toast.LENGTH_LONG).show();
				notificationBuilder.setSound(null);
			}

			
			Notification notification = notificationBuilder.build();
			

			nManager.notify((int) System.currentTimeMillis(), notification);
			
			if (set.isTogglesmsemail()) {
				
				// if sms selected
				if (set.isASMS()) {
				
					String phoneNo=set.getEtSettingsSMS();
					String smsMessage=set.getSmsMesssage();
					
					boolean ok=false;
					if(phoneNo.isEmpty()){
						ok=false;
					}
					else{
						ok=true;
					}
					
					if(smsMessage.isEmpty()){
						ok=false;
					}
					else{
						ok=true;
					}
					
					if(ok){
					SmsManager smsManager = SmsManager.getDefault();
					
					smsManager.sendTextMessage(phoneNo, null,smsMessage, null, null);
					}
					else
					{
						Toast.makeText(context, "SMS send failed", Toast.LENGTH_LONG).show();	
					}
				}

				// if email
				if (set.isAEmail()) {
					String emailadd=set.getTo();
					String message=set.getMessage();
					
					boolean ok=false;
					if(emailadd.isEmpty()){
						ok=false;
					}
					else{
						ok=true;
					}
					
					if(message.isEmpty()){
						ok=false;
					}
					else{
						ok=true;
					}
					
				
					if(ok){
					sendMail(emailadd, "Geo Map Alert", message);
					}else
					{
						Toast.makeText(context, "Email send failed", Toast.LENGTH_LONG).show();	
					}
				}
			}

		
			// if vibration
			if (set.isChSettingsAlarmVibration()) {
				
				Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				 
				v.vibrate(2000);
			}

			// if gps
			if (set.isToggleenablegps()) {
				
			}//network provider
			else{
				//Toast.makeText(context, "Network provider is used", Toast.LENGTH_LONG).show();
			}

					//AudioManager audiomanage = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				   // audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			
					//AudioManager audiomanage = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				    //audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

		
		}

		
		
		public void setDataFromSettingsPref() {
			set = new Settings(someData.getBoolean("AlarmSound", true),
					someData.getBoolean("AlarmVibration", true),
					someData.getString("phNo", ""),			
					someData.getString("email", ""),
					someData.getBoolean("togglesmsemail", false),
					someData.getBoolean("ASMS", false),
					someData.getBoolean("AEmail", false),
					someData.getBoolean("toggleenablegps", false),
					someData.getBoolean("isdead", false),
					someData.getString("password", ""),
					someData.getString("message", ""),
					someData.getString("smsMesssage", ""),
					someData.getString("To", ""));

		}
		
		
		private void sendMail(String email, String subject, String messageBody) {
	        Session session = createSessionObject();

	        try {
	            Message message = createMessage(email, subject, messageBody, session);
	            new SendMailTask().execute(message);
	        } catch (AddressException e) {
	            e.printStackTrace();
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	    }

	    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(email,"Powered by Osmium"));
	       
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
	        message.setSubject(subject);
	        message.setText(messageBody);
	        return message;
	    }

	    private Session createSessionObject() {
	        Properties properties = new Properties();
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.port", "587");

	        return Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(set.getEtSettingsEmail(), set.getPassword());
	               
	            }
	        });
	    }

	    private class SendMailTask extends AsyncTask<Message, Void, Void> {
	        //private ProgressDialog progressDialog;

	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	          //  progressDialog = ProgressDialog.show(con, "Please wait", "Sending mail", true, false);
	       Log.d("wait", "sending...........");
	        }

	        @Override
	        protected void onPostExecute(Void aVoid) {
	            super.onPostExecute(aVoid);
	          //  progressDialog.dismiss();
	        }

	        @Override
	        protected Void doInBackground(Message... messages) {
	            try {
	                Transport.send(messages[0]);
	            } catch (MessagingException e) {
	                e.printStackTrace();
	             
	            }
	            return null;
	        }
	    }
	    
		
}
