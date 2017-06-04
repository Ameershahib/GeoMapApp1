package info.tawsoft.osmium.models;

public class Settings {

	boolean chSettingsAlarmSound;
	boolean chSettingsAlarmVibration;
	
	String etSettingsSMS;
	String etSettingsEmail;
	
	boolean togglesmsemail;
	boolean ASMS;
	boolean AEmail;
	boolean toggleenablegps;
	
	boolean isdead;
	
	String password;
	String message;
	String smsMesssage;
	
	String To;
	

	public Settings(boolean a, boolean b,String c,String d,boolean e,boolean f,boolean g,boolean h,boolean i,String j,String k,String l,String m) {
		
		chSettingsAlarmSound = a;
		chSettingsAlarmVibration = b;
		etSettingsSMS=c;
		etSettingsEmail=d;
		togglesmsemail=e;
		ASMS=f;
		AEmail=g;
		toggleenablegps=h;
		isdead=i;
		
		password=j;
		message=k;
		smsMesssage=l;
		To=m;
	}


	public boolean isIsdead() {
		return isdead;
	}


	public void setIsdead(boolean isdead) {
		this.isdead = isdead;
	}


	public boolean isChSettingsAlarmSound() {
		return chSettingsAlarmSound;
	}


	public void setChSettingsAlarmSound(boolean chSettingsAlarmSound) {
		this.chSettingsAlarmSound = chSettingsAlarmSound;
	}


	public boolean isChSettingsAlarmVibration() {
		return chSettingsAlarmVibration;
	}


	public void setChSettingsAlarmVibration(boolean chSettingsAlarmVibration) {
		this.chSettingsAlarmVibration = chSettingsAlarmVibration;
	}


	public String getEtSettingsSMS() {
		return etSettingsSMS;
	}


	public void setEtSettingsSMS(String etSettingsSMS) {
		this.etSettingsSMS = etSettingsSMS;
	}


	public String getEtSettingsEmail() {
		return etSettingsEmail;
	}


	public void setEtSettingsEmail(String etSettingsEmail) {
		this.etSettingsEmail = etSettingsEmail;
	}


	public boolean isTogglesmsemail() {
		return togglesmsemail;
	}


	public void setTogglesmsemail(boolean togglesmsemail) {
		this.togglesmsemail = togglesmsemail;
	}


	public boolean isASMS() {
		return ASMS;
	}


	public void setASMS(boolean aSMS) {
		ASMS = aSMS;
	}


	public boolean isAEmail() {
		return AEmail;
	}


	public void setAEmail(boolean aEmail) {
		AEmail = aEmail;
	}


	public boolean isToggleenablegps() {
		return toggleenablegps;
	}


	public void setToggleenablegps(boolean toggleenablegps) {
		this.toggleenablegps = toggleenablegps;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getSmsMesssage() {
		return smsMesssage;
	}


	public void setSmsMesssage(String smsMesssage) {
		this.smsMesssage = smsMesssage;
	}


	public String getTo() {
		return To;
	}


	public void setTo(String to) {
		To = to;
	}

}
