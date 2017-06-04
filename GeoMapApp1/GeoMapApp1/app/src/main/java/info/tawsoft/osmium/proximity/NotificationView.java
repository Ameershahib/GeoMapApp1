package info.tawsoft.osmium.proximity;

import com.google.android.gms.internal.en;

import info.tawsoft.osmium.DataContent;
import info.tawsoft.osmium.R;
import info.tawsoft.osmium.models.Event;
import info.tawsoft.osmium.models.EventDates;
import info.tawsoft.osmium.models.OneTime;
import info.tawsoft.osmium.models.Repeating;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationView extends Activity{
	
	DataContent entry;

	TextView txtEventName;
	TextView txtEventDesc;
	TextView txtStatus;
	TextView txtPlaceDesc;
	
	TextView txtNextDate;
	
	Button btnNotification;
	
	
	Event ev;
	
	Repeating rep;
	OneTime one;
	EventDates dates;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.notification);
		
		txtEventName=(TextView)findViewById(R.id.txtEventName);
		txtEventDesc=(TextView)findViewById(R.id.txtEventDesc);
		txtStatus=(TextView)findViewById(R.id.txtStatus);
		txtPlaceDesc=(TextView)findViewById(R.id.txtPlaceDesc);
		btnNotification=(Button)findViewById(R.id.btnNotification);
		txtNextDate=(TextView)findViewById(R.id.txtNextDate);
		
		
		btnNotification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	
		int EventID=getIntent().getExtras().getInt("EventID");
		
		entry=new DataContent(NotificationView.this);
		
		try{
			entry.open();
			
			ev=entry.getEventInfoByID(EventID);
		}
		catch(Exception e){
			
		}
		finally{
			entry.close();
		}
		
		txtEventName.setText(ev.getEventname());
		txtEventDesc.setText(ev.getEventDesc());
		txtStatus.setText(ev.getRepStatus());
		txtPlaceDesc.setText(ev.getPlaceDesc());
		
		if(ev.getRepStatus().contentEquals("Repeating")){
			//txtNextDate
			
			try{
				entry.open();
				
				rep=entry.getRepeatingModel(EventID).get(0);
				txtNextDate.setText(rep.getDate());
			}
			catch(Exception e){
				
			}
			finally{
				entry.close();
			}
		}
		else if(ev.getRepStatus().contentEquals("OneTime")){
			try{
				entry.open();
				
				one=entry.getOneTimeModel(EventID).get(0);
				txtNextDate.setText("No more repeatings");
			}
			catch(Exception e){
				
			}
			finally{
				entry.close();
			}
		}
		else if(ev.getRepStatus().contentEquals("Custom")){
			try{
				entry.open();
				
				dates=entry.getAllDatesModel(EventID).get(0);
				txtNextDate.setText(dates.getEdate());
			}
			catch(Exception e){
				
			}
			finally{
				entry.close();
			}
		}
		

		else{
			txtNextDate.setText("When ever");
		}
	}
}
