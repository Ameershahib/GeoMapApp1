package info.tawsoft.osmium;


import info.tawsoft.osmium.R;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class AboutFragment extends Fragment implements
ActionBar.TabListener {
	
	public AboutFragment(){}
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "About Us", "Our Services", "Contact Us" };
	//private String[] tabs = { "Top Rated", "Games", "Movies" };
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
       View rootView = inflater.inflate(R.layout.fragment_about, container, false);
       setRetainInstance(true);
				viewPager = (ViewPager)rootView.findViewById(R.id.pager);
				actionBar = getActivity().getActionBar();
			
				actionBar.removeAllTabs();
				
				mAdapter = new TabsPagerAdapter(getFragmentManager());
				
				viewPager.setAdapter(mAdapter);
				actionBar.setHomeButtonEnabled(true);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	

			            
			
				// Adding Tabs
				for (String tab_name : tabs) {
					actionBar.addTab(actionBar.newTab().setText(tab_name)
							.setTabListener(this));
				}

				viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// on changing the page
						// make respected tab selected
						actionBar.setSelectedNavigationItem(position);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
				
				
		
        return rootView;
    }
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(1);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}
}
