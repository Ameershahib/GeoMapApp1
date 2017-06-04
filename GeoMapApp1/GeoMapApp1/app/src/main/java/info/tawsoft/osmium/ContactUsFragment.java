package info.tawsoft.osmium;


import java.net.URLEncoder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ContactUsFragment extends Fragment implements OnClickListener {

	EditText txtContactName;
	EditText txtContactSubject;
	EditText txtContactMessage;
	Button btnContactSend;
	Button btnContactCall;
	ImageView imgContatFB;
	ImageView imgContactTwitter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
		
		txtContactName=(EditText)rootView.findViewById(R.id.txtContactName);
		txtContactSubject=(EditText)rootView.findViewById(R.id.txtContactSubject);
		txtContactMessage=(EditText)rootView.findViewById(R.id.txtContactMessage);
		
		btnContactSend=(Button)rootView.findViewById(R.id.btnContactSend);
		btnContactCall=(Button)rootView.findViewById(R.id.btnContactCall);
		
		btnContactSend.setOnClickListener(this);
		btnContactCall.setOnClickListener(this);
		
		imgContatFB=(ImageView)rootView.findViewById(R.id.imgContatFB);
		imgContactTwitter=(ImageView)rootView.findViewById(R.id.imgContactTwitter);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnContactSend:
			String uriText =
		    "mailto:info@osmium.lk" + 
		    "?subject=" + URLEncoder.encode(txtContactSubject.getText().toString().trim()) + 
		    "&body=" + txtContactName.getText().toString()+" Said: \n"+txtContactMessage.getText().toString();

		Uri uri = Uri.parse(uriText);

		Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
		sendIntent.setData(uri);
		startActivity(Intent.createChooser(sendIntent, "Send email")); 
			
			break;
		case R.id.btnContactCall:
			
			Intent call = new Intent(Intent.ACTION_DIAL);
			call.setData(Uri.parse("tel:" + "+94776386222"));
			startActivity(call);
			
			break;
		}
	}

	

}
