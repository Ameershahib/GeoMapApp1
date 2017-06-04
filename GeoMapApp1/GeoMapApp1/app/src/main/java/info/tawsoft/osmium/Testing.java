package info.tawsoft.osmium;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class Testing extends Activity   {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testing);
		
		if(isNetworkAvailable()){
			
			
			Intent i=new Intent("info.tawsoft.osmium.Map");
			//Intent i=new Intent("info.tawsoft.osmium.MainActivity");
			startActivity(i);//}
		
		}
		else{
			final AlertDialog.Builder alert = new AlertDialog.Builder(Testing.this);

			alert.setTitle("This application needs internet to run it seems you are not connected to a network");
			alert.setIcon(getResources().getDrawable(R.drawable.alert));
			alert.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					finish();
				}
			});
			
			alert.show();
		
		}

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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
