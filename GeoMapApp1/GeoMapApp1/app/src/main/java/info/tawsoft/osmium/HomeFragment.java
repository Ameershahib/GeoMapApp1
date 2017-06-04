package info.tawsoft.osmium;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment{

	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		  
		    
		View rootView = null;

		rootView= inflater.inflate(R.layout.fragment_home, container, false);
	
 
        return rootView;
    }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

}
