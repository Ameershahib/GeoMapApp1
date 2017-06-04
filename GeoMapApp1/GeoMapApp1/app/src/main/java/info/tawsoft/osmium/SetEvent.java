package info.tawsoft.osmium;

import info.tawsoft.osmium.Custom.MySwitch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.api.a.d;
import com.google.android.gms.internal.di;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SetEvent extends FragmentActivity implements OnClickListener,
		OnSeekBarChangeListener {

	boolean Rep = true;
	boolean one = false;

	boolean in = false;
	boolean out = false;
	String inOutString;

	String repeatingStatusOp = "daily";

	boolean toggleButton = false;

	// dates
	String data;

	Button btnDate;
	ArrayList seletedItems;

	// Selected dates
	boolean mo, tu, we, th, fr, sa, su;

	GoogleMap googleMap;

	LatLng selectedPoint;

	Circle cl;

	SeekBar setEventseekBar;
	TextView tvsetAEventRadius;

	// Edit Text
	EditText etEventName;
	EditText etMessage;

	RadioButton radiosetEventRepeating;
	RadioButton radiosetEventOnetime;

	RadioGroup radioGroup1;

	String repeatingStatus = "whenever";

	// selection paths

	boolean whe = true, cus = false, oneT = false, RepT = false;

	// one Time
	String oneTimeDate;

	// repeating
	String oneTimeDateRepeating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.setaevent);

		Double lat = getIntent().getExtras().getDouble("lat");
		Double longi = getIntent().getExtras().getDouble("long");

		selectedPoint = new LatLng(lat, longi);

		setEventseekBar = (SeekBar) findViewById(R.id.setEventseekBar);
		tvsetAEventRadius = (TextView) findViewById(R.id.tvsetAEventRadius);
		etEventName = (EditText) findViewById(R.id.etEventName);
		etMessage = (EditText) findViewById(R.id.etMessage);
		radiosetEventRepeating = (RadioButton) findViewById(R.id.radiosetEventRepeating);
		radiosetEventOnetime = (RadioButton) findViewById(R.id.radiosetEventOnetime);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		setEventseekBar.setOnSeekBarChangeListener(this);

		tvsetAEventRadius.setText("20");

		try {

			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map1);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// camera
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(selectedPoint).zoom(17).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			addMarker(selectedPoint);
			drawCircle(selectedPoint);

		} catch (Exception e) {
			//Toast.makeText(getApplicationContext(), e.toString(),
				//	Toast.LENGTH_LONG).show();
		}

		datesInit();

		btnDate = (Button) findViewById(R.id.btnDate);

		btnDate.setOnClickListener(this);
	}

	private void datesInit() {
		mo = false;
		tu = false;
		we = false;
		th = false;
		fr = false;
		sa = false;
		su = false;
	}

	@Override
	protected void onStop() {

		super.onStop();

	}

	@Override
	public void onClick(View v) {
		// setDateDialog();
		// datesInit();

		final Dialog dialog = new Dialog(SetEvent.this);
		dialog.setContentView(R.layout.optional);
		dialog.setTitle("Optional Features");

		final RadioButton radiosetEventRepeating = (RadioButton) dialog
				.findViewById(R.id.radiosetEventRepeating);

		// radio button click event
		radiosetEventRepeating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dailyWeeklyMonthly();
				Toast.makeText(getApplicationContext(), "Repeating clicked",
						Toast.LENGTH_LONG).show();

				repeatingStatus = "Repeating";

				whe = false;
				cus = false;
				oneT = false;
				RepT = true;
			}
		});

		final RadioButton radiosetEventOnetime = (RadioButton) dialog
				.findViewById(R.id.radiosetEventOnetime);
		radiosetEventOnetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Use the current time as the default values for the picker
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dp = new DatePickerDialog(SetEvent.this,
						datePickerListener, year, month, day);

				dp.show();

				repeatingStatus = "OneTime";

				whe = false;
				cus = false;
				oneT = true;
				RepT = false;

			}
		});

		final CheckBox optionalchIn = (CheckBox) dialog
				.findViewById(R.id.optionalchIn);
		final CheckBox optionalchOut = (CheckBox) dialog
				.findViewById(R.id.optionalchOut);

		final RadioButton radiosetEventCustom = (RadioButton) dialog
				.findViewById(R.id.radiosetEventCustom);
		radiosetEventCustom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDateDialog();
				datesInit();
				repeatingStatus = "Custom";

				whe = false;
				cus = true;
				oneT = false;
				RepT = false;
			}

		});

		final ToggleButton toggleButton1 = (ToggleButton) dialog
				.findViewById(R.id.toggleButton1);
		toggleButton1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					radiosetEventRepeating.setEnabled(true);
					radiosetEventCustom.setEnabled(true);
					radiosetEventOnetime.setEnabled(true);
				} else {
					radiosetEventRepeating.setEnabled(false);
					radiosetEventCustom.setEnabled(false);
					radiosetEventOnetime.setEnabled(false);
				}
			}
		});

		if (toggleButton1.isChecked()) {
			radiosetEventRepeating.setEnabled(true);
			radiosetEventCustom.setEnabled(true);
			radiosetEventOnetime.setEnabled(true);
		} else {
			radiosetEventRepeating.setEnabled(false);
			radiosetEventCustom.setEnabled(false);
			radiosetEventOnetime.setEnabled(false);
		}

		Button btnOptionalOK = (Button) dialog.findViewById(R.id.btnOptionalOK);
		Button btnOptionalCancel = (Button) dialog
				.findViewById(R.id.btnOptionalCancel);
		btnOptionalOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				in = false;
				out = false;
				// TODO Auto-generated method stub
				if (radiosetEventRepeating.isChecked()) {
					Rep = true;
					one = false;

					whe = false;
					cus = false;
					oneT = false;
					RepT = true;
				} else if (radiosetEventCustom.isChecked()) {

					whe = false;
					cus = true;
					oneT = false;
					RepT = false;
				} else {
					Rep = false;
					one = true;

					whe = false;
					cus = false;
					oneT = true;
					RepT = false;
				}
				if (optionalchIn.isChecked()) {
					in = true;

				}
				if (optionalchOut.isChecked()) {
					out = true;

				}

				if (in) {
					//Toast.makeText(getApplicationContext(), "In selected",
						//	Toast.LENGTH_LONG).show();
				}
				if (out) {
					//Toast.makeText(getApplicationContext(), "Out selected",
							//Toast.LENGTH_LONG).show();
				}

				toggleButton = toggleButton1.isChecked();

				dialog.cancel();

			}
		});

		btnOptionalCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.cancel();
			}
		});

		dialog.show();

	}

	public void setDateDialog() {
		
		

		final CharSequence[] items = { " MONDAY", " TUESDAY ", " WEDNESDAY ",
				"THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY" };
		seletedItems = new ArrayList();

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Select Dates");

		alert.setMultiChoiceItems(items, null,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int indexSelected, boolean isChecked) {
						if (isChecked) {

							seletedItems.add(indexSelected);
						} else if (seletedItems.contains(indexSelected)) {

							seletedItems.remove(Integer.valueOf(indexSelected));
						}
					}
				});

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				data = "";

				for (int i = 0; i < seletedItems.size(); i++) {
					int no = (int) seletedItems.get(i);
					if (no == 0)
						mo = true;
					if (no == 1)
						tu = true;
					if (no == 2)
						we = true;
					if (no == 3)
						th = true;
					if (no == 4)
						fr = true;
					if (no == 5)
						sa = true;
					if (no == 6)
						su = true;
				}

				if (mo)
					data += "MONDAY \n";
				if (tu)
					data += "TUESDAY \n";
				if (we)
					data += "WEDNESDAY \n";
				if (th)
					data += "THURSDAY \n";
				if (fr)
					data += "FRIDAY \n";
				if (sa)
					data += "SATURDAY \n";
				if (su)
					data += "SUNDAY \n";

			}
		});
		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				dialog.cancel();
			}
		});

		alert.show();
		
		
		/*
		final Dialog dialog = new Dialog(SetEvent.this);
		dialog.setContentView(R.layout.datesdialog);
		dialog.setTitle("Select Dates");
		
		Button btndatesdialogOK=(Button)dialog.findViewById(R.id.btndatesdialogOK);
		Button btndatesdialogCancel=(Button)dialog.findViewById(R.id.btndatesdialogCancel);
		
		final CheckBox checkBox1=(CheckBox)dialog.findViewById(R.id.checkBox1);
		final CheckBox checkBox2=(CheckBox)dialog.findViewById(R.id.checkBox2);
		final CheckBox checkBox3=(CheckBox)dialog.findViewById(R.id.checkBox3);
		final CheckBox checkBox4=(CheckBox)dialog.findViewById(R.id.checkBox4);
		final CheckBox checkBox5=(CheckBox)dialog.findViewById(R.id.checkBox5);
		final CheckBox checkBox6=(CheckBox)dialog.findViewById(R.id.checkBox6);
		final CheckBox checkBox7=(CheckBox)dialog.findViewById(R.id.checkBox7);
		
		btndatesdialogOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mo=false; tu=false; we=false; th=false; fr=false; sa=false; su=false;
				if(checkBox1.isChecked()){
					mo=true;	
				}
				if(checkBox2.isChecked()){
					tu=true;				
				}
				if(checkBox3.isChecked()){
					we=true;
				}
				if(checkBox4.isChecked()){
					th=true;
				}
				if(checkBox5.isChecked()){
					fr=true;
				}
				if(checkBox6.isChecked()){
					sa=true;
				}
				if(checkBox7.isChecked()){
					su=true;
				}
				if(mo==false&& tu==false&&  we==false&&  th==false&&  fr==false&&  sa==false&&  su==false){
					
					Toast.makeText(getApplicationContext(), "All empty", Toast.LENGTH_LONG).show();
				
				}
				else{
				dialog.cancel();
				}
				
				
			}
		});
		
		btndatesdialogCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mo=false; tu=false; we=false; th=false; fr=false; sa=false; su=false;
				dialog.cancel();
			}
		});
		
		
		dialog.show();
		*/
		
	}

	public void addMarker(LatLng point) {

		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		Marker m = googleMap.addMarker(markerOptions);

		m.setIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.markerpending));

		m.setTitle(reverseGeocode(point));

	}

	private void drawCircle(LatLng point) {

		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();

		// Specifying the center of the circle
		circleOptions.center(point);

		// Radius of the circle
		circleOptions.radius(20);

		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);

		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);

		// Border width of the circle
		circleOptions.strokeWidth(2);

		// Adding the circle to the GoogleMap
		cl = googleMap.addCircle(circleOptions);

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		tvsetAEventRadius.setText(progress * 10 + "");
		cl.setRadius(progress * 10);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.eventmenu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.settingSave:

			if(validation()){
			saveToDB();
			finish();
			}else{
			
			}
			

			break;
		case R.id.settingCancel:
			etMessage.setText("");
			etEventName.setText("");
			data = "";
			repeatingStatusOp = "daily";
			mo=false; tu=false; we=false; th=false; fr=false; sa=false; su=false;
			break;

		case R.id.settingsback:

			finish();
			break;

		

		}
		return false;
	}

	public void saveToDB() {

		String Eventname = etEventName.getText().toString();
		String EventDesc = etMessage.getText().toString();
		String PlaceDesc = reverseGeocode(selectedPoint);

		String repStatus = "";

		if (toggleButton) {
			if (whe) {
				repStatus = "whenever";
			} else if (cus) {
				repStatus = "Custom";
			} else if (oneT) {
				repStatus = "OneTime";
			} else if (RepT) {
				repStatus = "Repeating";
			}
		} else {
			repStatus = "whenever";
		}

		String CurrentStatus = "pending";

		String radius = tvsetAEventRadius.getText().toString();
		Double lat = selectedPoint.latitude;
		Double lon = selectedPoint.longitude;
		try {
			if (in && out)
				inOutString = "IO";
			else if (in)
				inOutString = "I";
			else if (out)
				inOutString = "O";
			else
				inOutString = "I";
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}

		// Insert New Record
		insertNewEvent(Eventname, EventDesc, PlaceDesc, repStatus,
				CurrentStatus, radius, lat, lon, inOutString);

		if (toggleButton) {

			if (whe) {
				// do nothing
			} else if (cus) {
				// EventDates section

				String data = "";
				if (mo)
					data += "MONDAY \n";
				if (tu)
					data += "TUESDAY \n";
				if (we)
					data += "WEDNESDAY \n";
				if (th)
					data += "THURSDAY \n";
				if (fr)
					data += "FRIDAY \n";
				if (sa)
					data += "SATURDAY \n";
				if (su)
					data += "SUNDAY \n";

				String eventID = getLastEventID();

				insertNewEventDates(Integer.parseInt(eventID), data.trim());

			} else if (oneT) {
				// OneTime section
			//	Toast.makeText(getApplicationContext(), oneTimeDate,
						//Toast.LENGTH_LONG).show();

				String eventID = getLastEventID();
				insertNewOneTime(Integer.parseInt(eventID), oneTimeDate);
			} else if (RepT) {

				// repeating section
				//Toast.makeText(getApplicationContext(), repeatingStatusOp,
						//Toast.LENGTH_LONG).show();

				String eventID = getLastEventID();

				Calendar c = Calendar.getInstance();
				int year = 0;
				int month = 0;
				int day = 0;

				String NewDate;

				//Toast.makeText(getApplicationContext(), oneTimeDateRepeating,
						//Toast.LENGTH_LONG).show();

				if (repeatingStatusOp.contains("daily")) {
					//Toast.makeText(getApplicationContext(), "Nothing to add",
							//Toast.LENGTH_LONG).show();

					try {

						year = c.get(Calendar.YEAR);
						month = c.get(Calendar.MONTH);
						day = c.get(Calendar.DATE);

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								e.toString() + "hi", Toast.LENGTH_LONG).show();
					}

					NewDate = day + "/" + (month + 1) + "/" + year;

				} else if (repeatingStatusOp.contains("weekly")) {

					try {
						String[] dates = oneTimeDateRepeating.split("/");

						c.set(Integer.parseInt(dates[2]),
								Integer.parseInt(dates[1]),
								Integer.parseInt(dates[0]));

						year = c.get(Calendar.YEAR);
						month = c.get(Calendar.MONTH);
						day = c.get(Calendar.DATE);
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								e.toString() + "hi", Toast.LENGTH_LONG).show();
					}

					NewDate = day + "/" + (month) + "/" + year;

				} else {

					String[] dates = oneTimeDateRepeating.split("/");

					c.set(Integer.parseInt(dates[2]),
							Integer.parseInt(dates[1]),
							Integer.parseInt(dates[0]));

					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DATE);

					NewDate = day + "/" + (month) + "/" + year;

				}

				insertNewRepeating(Integer.parseInt(eventID),
						repeatingStatusOp, NewDate);

			}

		}

		DataContent entry = null;
		String eventID = getLastEventID();
		try {
			entry = new DataContent(SetEvent.this);

			entry.open();
			entry.insertNewProximityTable(Integer.parseInt(eventID), "pending");
			//Toast.makeText(getApplicationContext(), "proximity added",
				//	Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString() + "hi",
					Toast.LENGTH_LONG).show();

		} finally {
			entry.close();
		}

	}

	// Geocoding and reverse Geocoding

	public LatLng geocode(String add) {

		Geocoder gc = new Geocoder(getBaseContext());
		List<Address> list = null;
		try {
			list = gc.getFromLocationName(add, 1);
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Check your internet connectivity", Toast.LENGTH_LONG).show();
		finish();
		}
		Address address = list.get(0);

		double lat = address.getLatitude();
		double lng = address.getLongitude();

		LatLng point = new LatLng(lat, lng);

		return point;
	}

	public String reverseGeocode(LatLng point) {
		String add = "";
		Geocoder geocoder;
		List<Address> addresses = null;
		geocoder = new Geocoder(getBaseContext());
		if (point.latitude != 0 || point.longitude != 0) {
			try {
				addresses = geocoder.getFromLocation(point.latitude,
						point.longitude, 1);
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Check your internet connectivity", Toast.LENGTH_LONG).show();
				finish();
			}
			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getAddressLine(1);
			String country = addresses.get(0).getAddressLine(2);
			add = address + " " + city + "" + country;
		}

		return add;
	}

	public void dailyWeeklyMonthly() {
		final CharSequence[] items = { "Daily", "Weekly", "Monthly" };

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Repeating");
		alert.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						int year, month, day;
						final Calendar c;
						DatePickerDialog dp;

						switch (item) {
						case 0:
							// Your code when first option seletced
							repeatingStatusOp = "daily";
							break;
						case 1:
							// Your code when 2nd option seletced
							repeatingStatusOp = "weekly";

							c = Calendar.getInstance();
							year = c.get(Calendar.YEAR);
							month = c.get(Calendar.MONTH);
							day = c.get(Calendar.DAY_OF_MONTH);

							dp = new DatePickerDialog(SetEvent.this,
									datePickerListenerForRepeating, year,
									month, day);

							dp.show();

							break;
						case 2:
							// Your code when 3rd option seletced
							repeatingStatusOp = "monthly";

							c = Calendar.getInstance();
							year = c.get(Calendar.YEAR);
							month = c.get(Calendar.MONTH);
							day = c.get(Calendar.DAY_OF_MONTH);

							dp = new DatePickerDialog(SetEvent.this,
									datePickerListenerForRepeating, year,
									month, day);

							dp.show();

							break;

						}

					}
				});
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				dialog.cancel();
			}
		});
		alert.show();
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			oneTimeDate = selectedDay + "/" + (selectedMonth + 1) + "/"
					+ selectedYear;

		}
	};

	private DatePickerDialog.OnDateSetListener datePickerListenerForRepeating = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			oneTimeDateRepeating = selectedDay + "/" + (selectedMonth) + "/"
					+ selectedYear;

		}
	};

	public void insertNewRepeating(int eventID, String type, String date) {

		DataContent entry = new DataContent(SetEvent.this);

		try {
			entry.open();

			float fl = entry.insertNewRepeating(eventID, type, date);

		} catch (Exception e) {

		} finally {
			entry.close();
		}
	}

	public void insertNewOneTime(int eventID, String Date) {

		DataContent entry = new DataContent(SetEvent.this);

		try {
			entry.open();

			float fl = entry.insertNewOneTime(eventID, Date);

		} catch (Exception e) {

		} finally {
			entry.close();
		}
	}

	public void insertNewEventDates(int eventID, String edate) {
		DataContent entry = new DataContent(SetEvent.this);

		try {
			entry.open();

			float fl = entry.insertNewEventDates(eventID, edate);

		} catch (Exception e) {

		} finally {
			entry.close();
		}

	}

	public void insertNewEvent(String eventname, String eventDesc,
			String placeDesc, String repStatus, String currentStatus,
			String radius, Double lat, Double lon, String inout) {

		DataContent entry = new DataContent(SetEvent.this);

		try {
			entry.open();

			float fl = entry.insertNewEvent(eventname, eventDesc, placeDesc,
					repStatus, currentStatus, radius, lat, lon, inout);

		} catch (Exception e) {

		} finally {
			entry.close();
		}

	}

	public String getLastEventID() {
		DataContent entry = new DataContent(SetEvent.this);
		String eventID = "";
		try {
			entry.open();

			eventID = entry.getLastEvent();
		} catch (Exception e) {

		} finally {
			entry.close();
		}
		return eventID;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	
	public boolean validation(){
		boolean correct=true;
		
		String eventName=etEventName.getText().toString().trim();
		String eventMessage=etMessage.getText().toString().trim();
		
		if(eventName.isEmpty()&&eventMessage.isEmpty()){
			correct=false;
			Toast.makeText(getApplicationContext(), "Please fill the content before you save ", Toast.LENGTH_LONG).show();
			
		}else if(eventName.isEmpty()){
			correct=false;
			Toast.makeText(getApplicationContext(), "Event Name cannot be null ", Toast.LENGTH_LONG).show();
			
		}
		else if(eventMessage.isEmpty()){
			correct=false;
			Toast.makeText(getApplicationContext(), "Description cannot be null ", Toast.LENGTH_LONG).show();
			
		}
		
		if(eventName.contains("+")){
			correct=false;
			
			Toast.makeText(getApplicationContext(), "Illegal keyword used", Toast.LENGTH_LONG).show();
			
		}
		if(eventMessage.contains("+")){
			correct=false;
			
			Toast.makeText(getApplicationContext(), "Illegal keyword used", Toast.LENGTH_LONG).show();
			
		}
		
		if(repeatingStatus.contentEquals("Custom")){
			if(mo==false&& tu==false&&  we==false&&  th==false&&  fr==false&&  sa==false&&  su==false){
				
				
				Toast.makeText(getApplicationContext(), "Please select atleast one day", Toast.LENGTH_LONG).show();
				correct=false;
			}
		}
		
		return correct;
	}
}
