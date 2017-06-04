package info.tawsoft.osmium;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener {

	CheckBox chSettingsAlarmSound;
	CheckBox chSettingsAlarmVibration;

	ToggleButton togglesmsemail;
	ToggleButton toggleenablegps;

	CheckBox chSettingsSMS;
	CheckBox chSettingsEmail;



	boolean gpsStatus = true;

	public static String filename = "MySharedString";
	SharedPreferences someData;

	int phNo = 0;
	String email = "";

	private ActionBar actionBar;

	public SettingsFragment() {
	}
	
	String emailadd;
	String Password;
	String message;
	
	String phoneNo;
	String smsMesssage;
	
	String To;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_settings, container,
				false);

		try {
			actionBar = getActivity().getActionBar();
		} catch (Exception e) {

		}
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		chSettingsAlarmSound = (CheckBox) rootView
				.findViewById(R.id.chSettingsAlarmSound);
		chSettingsAlarmVibration = (CheckBox) rootView
				.findViewById(R.id.chSettingsAlarmVibration);

		togglesmsemail = (ToggleButton) rootView.findViewById(R.id.togglesmsemail);
		toggleenablegps = (ToggleButton) rootView
				.findViewById(R.id.toggleenablegps);

		chSettingsSMS = (CheckBox) rootView.findViewById(R.id.chSettingsSMS);
		chSettingsEmail = (CheckBox) rootView
				.findViewById(R.id.chSettingsEmail);

		chSettingsSMS.setOnClickListener(this);
		chSettingsEmail.setOnClickListener(this);

		chSettingsSMS
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							// etSettingsSMS.setEnabled(true);
						} else {
							// etSettingsSMS.setEnabled(false);
						}
					}
				});

		chSettingsEmail
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							// etSettingsEmail.setEnabled(true);
						} else {
							// etSettingsEmail.setEnabled(false);
						}
					}
				});

		// etSettingsSMS=(EditText)rootView.findViewById(R.id.etSettingsSMS);
		// etSettingsEmail=(EditText)rootView.findViewById(R.id.etSettingsEmail);

		togglesmsemail
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						atFirst();
					}
				});

		someData = getActivity().getSharedPreferences(filename, 0);

		setHasOptionsMenu(true);

		setDefaults();
		atFirst();

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);

		MenuInflater blowUp = inflater;
		blowUp.inflate(R.menu.settingsmenu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.settingSave:
			Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG).show();

			setPref();

			break;
		case R.id.settingCancel:
			// Toast.makeText(getActivity(), "not saved",
			// Toast.LENGTH_LONG).show();
			setDefaults();
			break;
		}
		return false;

	}

	public void setPref() {

		try {
			SharedPreferences.Editor editor = someData.edit();

			editor.putBoolean("AlarmSound", chSettingsAlarmSound.isChecked());
			editor.putBoolean("AlarmVibration",
					chSettingsAlarmVibration.isChecked());
			editor.putBoolean("togglesmsemail", togglesmsemail.isChecked());
			editor.putBoolean("ASMS", chSettingsSMS.isChecked());
			editor.putBoolean("AEmail", chSettingsEmail.isChecked());
			editor.putString("phNo", phoneNo);
			 editor.putString("email",emailadd);
			editor.putBoolean("toggleenablegps", toggleenablegps.isChecked());
			
			//new adds
			editor.putString("password",Password);
			editor.putString("message",message);
			editor.putString("smsMesssage",smsMesssage);
			editor.putString("To",To);
			
			editor.commit();
			
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
					.show();
		}

	}

	public void setDefaults() {
		chSettingsAlarmSound.setChecked((someData
				.getBoolean("AlarmSound", true)));
		chSettingsAlarmVibration.setChecked((someData.getBoolean(
				"AlarmVibration", true)));

		togglesmsemail
				.setChecked((someData.getBoolean("togglesmsemail", false)));

		chSettingsSMS.setChecked((someData.getBoolean("ASMS", false)));
		chSettingsEmail.setChecked((someData.getBoolean("AEmail", false)));

		// etSettingsSMS.setText(someData.getString("phNo", ""));
		// etSettingsEmail.setText(someData.getString("email", ""));

		toggleenablegps.setChecked((someData.getBoolean("toggleenablegps",true)));
		
		 emailadd=someData.getString("email", "");
		 Password=someData.getString("password", "");
		 message=someData.getString("message", "");
		
		 phoneNo=someData.getString("phNo", "");
		 smsMesssage=someData.getString("smsMesssage", "");
		 To=someData.getString("To", "");
	}

	public void atFirst() {
		try {
			if (togglesmsemail.isChecked()) {
				chSettingsSMS.setEnabled(true);
				chSettingsEmail.setEnabled(true);

				if (chSettingsSMS.isChecked()) {
					// etSettingsSMS.setEnabled(true);
				} else {
					// etSettingsSMS.setEnabled(false);
				}

				if (chSettingsEmail.isChecked()) {
					// etSettingsEmail.setEnabled(true);
				} else {
					// etSettingsEmail.setEnabled(false);
				}
			} else {
				chSettingsSMS.setEnabled(false);
				chSettingsEmail.setEnabled(false);
				// etSettingsSMS.setEnabled(false);
				// etSettingsEmail.setEnabled(false);
			}

		} catch (Exception e) {
			Log.d("allmytests", e.toString());
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.chSettingsSMS:

			if(chSettingsSMS.isChecked()){
				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.settingssmsint);
				dialog.setTitle("SMS info");
				
				final EditText etSettingssms=(EditText)dialog.findViewById(R.id.etSettingssms);
				final EditText etSettingssmsmessage=(EditText)dialog.findViewById(R.id.etSettingssmsmessage);
				final Button btnSettingssmsOk=(Button)dialog.findViewById(R.id.btnSettingssmsOk);
				final Button btnSettingssmsCancel=(Button)dialog.findViewById(R.id.btnSettingssmsCancel);
				
				etSettingssms.setText(someData.getString("phNo", ""));
				etSettingssmsmessage.setText(someData.getString("smsMesssage", ""));
				
				btnSettingssmsOk.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(etSettingssms.getText().toString().isEmpty()&&etSettingssmsmessage.getText().toString().isEmpty()){
							Toast.makeText(getActivity(), "Empty", Toast.LENGTH_LONG).show();
							
						}
						else if(etSettingssms.getText().toString().isEmpty()){
							
							Toast.makeText(getActivity(), "Mobile No is empty", Toast.LENGTH_LONG).show();
						
						}
						else if(etSettingssmsmessage.getText().toString().isEmpty()){
							
							Toast.makeText(getActivity(), "Message is empty", Toast.LENGTH_LONG).show();
						
						}
						else{
							phoneNo=etSettingssms.getText().toString();
							smsMesssage=etSettingssmsmessage.getText().toString();
							dialog.cancel();
						}
					}
				});
				
				btnSettingssmsCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						etSettingssms.setText(someData.getString("phNo", ""));
						etSettingssmsmessage.setText(someData.getString("smsMesssage", ""));
					}
				});
				
				dialog.show();
				
			}
			
			
			
			break;

		case R.id.chSettingsEmail:

			if(chSettingsEmail.isChecked()){
			final Dialog dialog = new Dialog(getActivity());
			dialog.setContentView(R.layout.settingsemailint);
			dialog.setTitle("Email address info");
		
			final EditText txtSettingsEmailaddress=(EditText)dialog.findViewById(R.id.txtSettingsEmailaddress);
			final EditText txtSettingspassword=(EditText)dialog.findViewById(R.id.txtSettingspassword);
			final EditText txtSettingspasswordConfirm=(EditText)dialog.findViewById(R.id.txtSettingspasswordConfirm);
			final Button btnSettingsEmailOK=(Button)dialog.findViewById(R.id.btnSettingsEmailOK);
			final Button btnSettingsEmailCancel=(Button)dialog.findViewById(R.id.btnSettingsEmailCancel);
			final EditText etSettingsemailMessage=(EditText)dialog.findViewById(R.id.etSettingsemailMessage);
			
			
			final EditText txtSettingsEmailaddressTo=(EditText)dialog.findViewById(R.id.txtSettingsEmailaddressTo);
			
			txtSettingsEmailaddress.setText(someData.getString("email", ""));
			txtSettingspassword.setText(someData.getString("password", ""));
			txtSettingspasswordConfirm.setText(someData.getString("password", ""));
			etSettingsemailMessage.setText(someData.getString("message", ""));
			txtSettingsEmailaddressTo.setText(someData.getString("To", ""));
			
			btnSettingsEmailOK.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String pass=txtSettingspassword.getText().toString().trim();
					String passconfirm=txtSettingspasswordConfirm.getText().toString().trim();
					
					if(pass.contentEquals(passconfirm)){
						
						emailadd=txtSettingsEmailaddress.getText().toString().trim();
						Password=txtSettingspassword.getText().toString().trim();
						message=etSettingsemailMessage.getText().toString().trim();
						To=txtSettingsEmailaddressTo.getText().toString().trim();
						
						//Toast.makeText(getActivity(), "Cool", Toast.LENGTH_LONG).show();
						boolean ok=false;
						
						
						ok=android.util.Patterns.EMAIL_ADDRESS.matcher(emailadd).matches();
						ok=android.util.Patterns.EMAIL_ADDRESS.matcher(To).matches();
				
						if(Password.isEmpty())ok=false;
						if(message.isEmpty())ok=false;
						
						if(ok){
							Toast.makeText(getActivity(), "Matches", Toast.LENGTH_LONG).show();
							dialog.cancel();
						}else{
							Toast.makeText(getActivity(), "Incorrect", Toast.LENGTH_LONG).show();
						}
						
					}else{
						Toast.makeText(getActivity(), "Passwords does not match", Toast.LENGTH_LONG).show();
						
					}
					
					
				}
			});
			
			btnSettingsEmailCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txtSettingsEmailaddress.setText(someData.getString("email", ""));
					txtSettingspassword.setText(someData.getString("password", ""));
					txtSettingspasswordConfirm.setText(someData.getString("password", ""));
					etSettingsemailMessage.setText(someData.getString("message", ""));
					
				}
			});
			dialog.show();
			}
			break;
		}
	}

}
