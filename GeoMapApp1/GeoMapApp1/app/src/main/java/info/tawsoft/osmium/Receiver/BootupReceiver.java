package info.tawsoft.osmium.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootupReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		 Intent i = new Intent("info.tawsoft.osmium.Map"); 
	     i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     context.startActivity(i);
 
	}

}
