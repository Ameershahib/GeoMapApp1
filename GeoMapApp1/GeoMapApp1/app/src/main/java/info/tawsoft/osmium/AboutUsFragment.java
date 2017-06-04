package info.tawsoft.osmium;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutUsFragment extends Fragment implements OnClickListener{

	ImageView aboutUs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
		
		aboutUs=(ImageView)rootView.findViewById(R.id.aboutUs);
		aboutUs.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
	
		Intent intent = new Intent(Intent.ACTION_VIEW, 
	    Uri.parse("http://osmium.lk"));
	    startActivity(intent);
	    
	}
}
