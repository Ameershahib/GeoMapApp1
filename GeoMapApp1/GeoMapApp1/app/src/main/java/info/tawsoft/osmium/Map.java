package info.tawsoft.osmium;


import info.tawsoft.osmium.models.Event;
import info.tawsoft.osmium.models.ProximityTable;
import info.tawsoft.osmium.models.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Map extends FragmentActivity implements OnMapClickListener,
		OnClickListener, OnMapLongClickListener,LocationListener  {

	
	 double longitude;
	 double latitude;
     
     
	GoogleMap googleMap;
	AutoCompleteTextView etAddress;
	Button btnSearch;

	PlacesTask placesTask;
	ParserTask parserTask;
	
	Circle cl;

	Settings set;

	boolean mapView = false;
	boolean setEvent = false;
	int type = 0;// map type

	LatLng selectedPoint;

	ArrayList<Event> ev;

	LocationManager locationManager;
	PendingIntent pendingIntent;

	public static String filename = "MySharedString";
	SharedPreferences someData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		
		
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);

		someData = getSharedPreferences(filename, 0);

		setDataFromSettingsPref();

		etAddress = (AutoCompleteTextView) findViewById(R.id.etAddress);
		btnSearch = (Button) findViewById(R.id.btnSearch);

		btnSearch.setOnClickListener(this);
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker point) {

							
							Intent i = new Intent(
									"info.tawsoft.osmium.EditInfo");
							i.putExtra("lat", point.getPosition().latitude);
							i.putExtra("longi", point.getPosition().longitude);

							startActivity(i);
					

						}
					});

		}

		googleMap.setOnMapClickListener(this);
		googleMap.setOnMapLongClickListener(this);

		// Setting a custom info window adapter for the google map
		googleMap.setInfoWindowAdapter(adapt);

		
		initializeMap();
		
	
		checkLocationUpdate();
	
	etAddress.setThreshold(1);		
	
	etAddress.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {				
			placesTask = new PlacesTask();				
			placesTask.execute(s.toString());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub				
		}
	});	
	}
	
	
	private void checkLocationUpdate() {
		try{
			Location l;
			if(set.isToggleenablegps()){
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,Map.this);
				l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			//	Toast.makeText(getApplicationContext(), "GPS used", Toast.LENGTH_LONG).show();
			}
			else{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,Map.this);
				l=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			//	Toast.makeText(getApplicationContext(), "Network provider used", Toast.LENGTH_LONG).show();
			}
				// camera
				CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(l.getLatitude(),l.getLongitude())).zoom(17).build();
		
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		catch(Exception e){
			
			//Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mapmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.more:
			Intent i = new Intent("info.tawsoft.osmium.MainActivity");
			startActivity(i);
			
			//Toast.makeText(Map.this,"Hi", Toast.LENGTH_LONG).show();
			break;
		case R.id.exit:
			finish();
			try{
				SharedPreferences.Editor editor = someData.edit();
				editor.putBoolean("isdead", true);
			}
			catch(Exception e){
				Toast.makeText(Map.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
			break;
		}
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setDataFromSettingsPref();
		googleMap.clear();
		initializeMap();
		
		checkLocationUpdate();
		
	
	
		if(set.isIsdead()){
			
		//	Toast.makeText(Map.this, "Dead", Toast.LENGTH_LONG).show();
			try{
				SharedPreferences.Editor editor = someData.edit();
				editor.putBoolean("isdead", false);
				editor.commit();
			}
			catch(Exception e){
				Toast.makeText(Map.this, e.toString(), Toast.LENGTH_LONG).show();
			}
			
			finish();
		}else{
		//Toast.makeText(Map.this, "Alive", Toast.LENGTH_LONG).show();
		}
		
	}

	public Marker addMarker(LatLng point, String title) {

		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		Marker m = googleMap.addMarker(markerOptions);
		m.setTitle(title);

		return m;
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
	public void onMapClick(LatLng point) {

		Log.d("noticeN", point.latitude+"");
		Log.d("noticeN", point.longitude+"");
		
	//	etAddress.setText(point.latitude+"     "+point.longitude+"");
	}

	public LatLng geocode(String add)   {

		Geocoder gc = new Geocoder(getBaseContext());
		List<Address> list = null;
		try {
			list = gc.getFromLocationName(add, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_LONG).show();
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

			}
			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getAddressLine(1);
			String country = addresses.get(0).getAddressLine(2);
			add = address + " " + city + "" + country;
		}

		return add;
	}

	@Override
	public void onClick(View v) {
		try {
			String add = etAddress.getText().toString();

			LatLng point = geocode(add);
			// addMarker(point);
			// drawCircle(point);

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(point).zoom(17).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onMapLongClick(LatLng point) {

		selectedPoint = point;

		String[] items = { "change Map view", "Set a event" };
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Map.this);
		alertDialogBuilder.setTitle("What do you Need");

		alertDialogBuilder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						switch (item) {
						case 0:
							// change a map view clicked
							mapView = true;
							setEvent = false;
							break;
						case 1:
							// set a event clicked
							mapView = false;
							setEvent = true;
							break;

						}

					}
				});
		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						if (mapView) {
							popupMapType();
						} else {

							if(isNetworkAvailable()){
							Intent i = new Intent(
									"info.tawsoft.osmium.SetEvent");
							i.putExtra("lat", selectedPoint.latitude);
							i.putExtra("long", selectedPoint.longitude);

							startActivity(i);
							}
							else{
								Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
							}

						}
					}
				}).setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		alertDialogBuilder.show();

	}

	public void popupMapType() {
		String[] items = { "Normal", "Hybrid", "Satellite", "Terrain", "None" };
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Select a option");

		alert.setSingleChoiceItems(items, -1, new

		DialogInterface.OnClickListener()

		{

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					// displayView("normal");
					type = 0;
					break;
				case 1:
					// displayView("hybrid");
					type = 1;
					break;
				case 2:
					// displayView("satellite");
					type = 2;
					break;
				case 3:
					// displayView("terrain");
					type = 3;
					break;
				case 4:
					// displayView("none");
					type = 4;
					break;
				}
			}

		});

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				switch (type) {

				case 0:
					displayView("normal");

					break;
				case 1:
					displayView("hybrid");

					break;
				case 2:
					displayView("satellite");

					break;
				case 3:
					displayView("terrain");

					break;
				case 4:
					displayView("none");

					break;
				}

			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				dialog.cancel();
			}
		});

		alert.show();
	}

	public void displayView(String type) {
		switch (type) {
		case "normal":
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case "hybrid":
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case "satellite":
			googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case "terrain":
			googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case "none":
			googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		}

	}

	public void initializeMap() {

		removeAllProximityBroadcast();

		initmapCompleted();

		DataContent entry = new DataContent(Map.this);

		try {
			entry.open();

			ev = entry.initializeMap();

			int size = ev.size() - 1;
			// camera
			LatLng sp = new LatLng(Double.parseDouble(ev.get(size).getLat()),
					Double.parseDouble(ev.get(size).getLng()));
			/*
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(sp).zoom(17).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
					*/
			String data = "";
			for (int i = 0; i < ev.size(); i++) {

				data += "Latitude :" + ev.get(i).getLat() + "   Longitude  :"
						+ ev.get(i).getLng();
				Double lat, lgn;
				String title, EventName, EventID;
				int Radius;

				lat = Double.parseDouble(ev.get(i).getLat());
				lgn = Double.parseDouble(ev.get(i).getLng());
				title = ev.get(i).getPlaceDesc();

				LatLng point = new LatLng(lat, lgn);
				Radius = Integer.parseInt(ev.get(i).getRadius());
				Marker m = addMarker(point, title);
				drawCircle(point);
				m.setIcon(BitmapDescriptorFactory
						.fromResource(R.drawable.markerpending));
				try {
					entry.open();

					ProximityTable pt = entry.getProximityTableOnID(ev.get(i)
							.getEventID(), "pending");

					addProximityBroadcast(point, Radius, pt.getRequestcode(),
							ev.get(i).getEventID());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					entry.close();
				}

			}

			try {
				entry.open();

				ArrayList<ProximityTable> pt = entry
						.getProximityTableOnCompleted("completed");
				for (int i = 0; i < pt.size(); i++) {

					removeProximityBroadcast(pt.get(i).getRequestcode());
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				entry.close();
			}

		} catch (Exception e) {

		} finally {
			entry.close();
		}
	}

	// Adapter
		public InfoWindowAdapter adapt = new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			@Override
			public View getInfoContents(Marker point) {

				// Getting view from the layout file info_window_layout
				View v = getLayoutInflater().inflate(R.layout.custommarker, null);

				
				String eID = "", EPlace = "", EDes = "";

				DataContent entry=new DataContent(Map.this);
				try{
					entry.open();
					ev = entry.getAllEventNamesModel();
					
				}
				catch(Exception e){
					
				}
				finally{entry.close();}
				
				for (int i = 0; i < ev.size(); i++) {

					LatLng p = point.getPosition();
					Event e =ev.get(i); 

					if (Double.parseDouble(e.getLat()) == p.latitude
							&& Double.parseDouble(e.getLng()) == p.longitude) {
						eID = e.getEventID() + "";
						EPlace = e.getEventDesc();
						EDes = e.getEventname();
					}

				}
				TextView txtCustomEventID = (TextView) v
						.findViewById(R.id.txtCustomEventID);
				TextView txtEventDescCustom = (TextView) v
						.findViewById(R.id.txtEventDescCustom);
				TextView txtPlaceDescCustom = (TextView) v
						.findViewById(R.id.txtPlaceDescCustom);

				
				
				txtCustomEventID.setText(eID);
				txtEventDescCustom.setText(EDes);
				txtPlaceDescCustom.setText(EPlace);

				return v;

			}
		};
	public void removeProximityIntent(int RequestCode) {
		Intent proximityIntent = new Intent(
				"info.tawsoft.osmium.proximity.ProximityActivity");

		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
				RequestCode, proximityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
		locationManager.removeProximityAlert(pendingIntent);
		}
		catch(Exception e){}
		

	}

	public void ControlleremoveAllProx() {
		for (int i = 1; i <= 20; i++) {
			removeProximityIntent(i);
		}

		//Toast.makeText(getApplicationContext(), "All Alarms Removed",
			//	Toast.LENGTH_LONG).show();

	}

	// broadcasting Addition Removal

	public void addProximityBroadcast(LatLng point, int radius,
			int RequestCode, int eventID) {
		Intent i = new Intent(
				"info.tawsoft.osmium.proximity.BroadcastReceiverProx"); // Custom
		// Action

		i.putExtra("EventID", eventID);

		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
				RequestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
		// RequestCode, i, PendingIntent.FLAG_ONE_SHOT);

		locationManager.addProximityAlert(point.latitude, point.longitude,
				radius, -1, pi);
	}

	public void removeAllProximityBroadcast() {

		for (int i = 0; i < 20; i++) {
			String context = Context.LOCATION_SERVICE;
			LocationManager locationManager = (LocationManager) getSystemService(context);

			Intent anIntent = new Intent(
					"info.tawsoft.osmium.proximity.BroadcastReceiverProx");
			PendingIntent operation = PendingIntent.getBroadcast(
					getApplicationContext(), i, anIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			try{
			locationManager.removeProximityAlert(operation);
			}
			catch(Exception e){}

		}
	}

	public void removeProximityBroadcast(int RequestCode) {

		String context = Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager) getSystemService(context);

		Intent anIntent = new Intent("com.example.maponolder.proximityalert");
		PendingIntent operation = PendingIntent.getBroadcast(
				getApplicationContext(), RequestCode, anIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		locationManager.removeProximityAlert(operation);

	}

	public void initmapCompleted() {

		DataContent entry = new DataContent(Map.this);
		try {
			entry.open();

			ev = entry.initializeMapComplete();
			String data = "";
			for (int i = 0; i < ev.size(); i++) {

				data += "Latitude :" + ev.get(i).getLat() + "   Longitude  :"
						+ ev.get(i).getLng();
				Double lat, lgn;
				String title, EventName, EventID;
				int Radius;

				lat = Double.parseDouble(ev.get(i).getLat());
				lgn = Double.parseDouble(ev.get(i).getLng());
				title = ev.get(i).getPlaceDesc();

				LatLng point = new LatLng(lat, lgn);
				Radius = Integer.parseInt(ev.get(i).getRadius());
				Marker m = addMarker(point, title);
				drawCircle(point);

				m.setIcon(BitmapDescriptorFactory
						.fromResource(R.drawable.markercomplete));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void setDataFromSettingsPref() {
		set = new Settings(someData.getBoolean("AlarmSound", true),
				someData.getBoolean("AlarmVibration", true),
				someData.getString("phNo", ""),			
				someData.getString("email", ""),
				someData.getBoolean("togglesmsemail", false),
				someData.getBoolean("ASMS", false),
				someData.getBoolean("AEmail", false),
				someData.getBoolean("toggleenablegps", true),
				someData.getBoolean("isdead", false),
				someData.getString("password", ""),
				someData.getString("message", ""),
				someData.getString("smsMesssage", ""),
				someData.getString("To", "")
				);

	}
	
	
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }	
	
	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";
			
			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyB2k-nI-gK9fsrveYr5w75hGoKyUJD8Oto";
			
			String input="";
			
			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}		
			
			
			// place type to be searched
			String types = "types=geocode";
			
			// Sensor enabled
			String sensor = "sensor=false";			
			
			// Building the parameters to the web service
			String parameters = input+"&"+types+"&"+sensor+"&"+key;
			
			// Output format
			String output = "json";
			
			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
	
			try{
				// Fetching the data from web service in background
				data = downloadUrl(url);
			}catch(Exception e){
                Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			
			// Creating ParserTask
			parserTask = new ParserTask();
			
			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}		
	}
	
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

    	JSONObject jObject;
    	
		@Override
		protected List<HashMap<String, String>> doInBackground(String... jsonData) {			
			
			List<HashMap<String, String>> places = null;
			
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	
            	// Getting the parsed data as a List construct
            	places = placeJsonParser.parse(jObject);

            }catch(Exception e){
            	Log.d("Exception",e.toString());
            }
            return places;
		}
		
		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {			
			
				String[] from = new String[] { "description"};
				int[] to = new int[] { android.R.id.text1 };
				
				// Creating a SimpleAdapter for the AutoCompleteTextView			
				SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);				
				
				// Setting the adapter
				etAddress.setAdapter(adapter);
		}			
    }


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}    

	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager=null ;
		 NetworkInfo activeNetworkInfo = null;
		
		try{
	     connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	     activeNetworkInfo= connectivityManager.getActiveNetworkInfo();
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		finally{
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}
	}
}
