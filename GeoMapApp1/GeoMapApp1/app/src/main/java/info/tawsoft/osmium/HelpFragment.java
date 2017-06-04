package info.tawsoft.osmium;


import java.util.ArrayList;

import info.tawsoft.osmium.R;
import info.tawsoft.osmium.adapter.FullScreenImageAdapter;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HelpFragment extends Fragment {
	
	public HelpFragment(){}
	
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	ArrayList<String> path;
	private ActionBar actionBar;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_fullscreen_view, container, false);
         
       // setContentView(R.layout.activity_fullscreen_view);
        try{
            actionBar = getActivity().getActionBar();
            }
            catch(Exception e){
            	
            }
            actionBar.removeAllTabs();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            
		path=new ArrayList<String>();
		
		path.add(R.drawable.s1+"");
		path.add(R.drawable.s2+"");
		path.add(R.drawable.s3+"");
		path.add(R.drawable.s4+"");
		path.add(R.drawable.s5+"");
		path.add(R.drawable.s6+"");
		path.add(R.drawable.s7+"");
		path.add(R.drawable.s8+"");
		path.add(R.drawable.s9+"");
		path.add(R.drawable.s10+"");
		path.add(R.drawable.s11+"");
		path.add(R.drawable.s12+"");
		path.add(R.drawable.s13+"");
		path.add(R.drawable.s14+"");
		
		
		viewPager = (ViewPager) rootView.findViewById(R.id.pager);

		

		Intent i = getActivity().getIntent();
		int position = i.getIntExtra("position", 0);

		adapter = new FullScreenImageAdapter(getActivity(),path);

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
            

        
        return rootView;
        
    }
	

}
