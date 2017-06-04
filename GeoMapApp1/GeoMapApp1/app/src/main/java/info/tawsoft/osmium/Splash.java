package info.tawsoft.osmium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
		
		Thread t=new Thread(){
			@Override
			public void run(){
				try{
					sleep(4000);
				
				}catch(Exception e){
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
				}
				finally{
					
					//if(isNetworkAvailable()){
					
					//Intent i=new Intent("info.tawsoft.osmium.Map");
					Intent i=new Intent("info.tawsoft.osmium.Testing");
					//Intent i=new Intent("info.tawsoft.osmium.MainActivity");
					startActivity(i);//}
					//Toast.makeText(getApplicationContext(), "Internext not available", Toast.LENGTH_LONG).show();
				}
			}
		};
		t.start();
		
		
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}
