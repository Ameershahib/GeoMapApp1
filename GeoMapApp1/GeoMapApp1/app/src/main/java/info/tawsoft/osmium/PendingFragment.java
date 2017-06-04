package info.tawsoft.osmium;


import java.util.ArrayList;

import info.tawsoft.osmium.R;
import info.tawsoft.osmium.models.Event;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PendingFragment extends Fragment {
	
	TableLayout tl;
	public PendingFragment(){}
	
	DataContent entry;
	
	boolean des=false;
	int i=0;
	private ActionBar actionBar;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_pending, container, false);
        
        tl=(TableLayout)rootView.findViewById(R.id.tablelayout1);   
        try{
        actionBar = getActivity().getActionBar();
        }
        catch(Exception e){
        	
        }
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);	
        
        entry=new DataContent(getActivity());
        try{
        	entry.open();
        	
        	ArrayList<Event> se=entry.getAllFromEvent("pending");
        	
        	for(int i=0;i<se.size();i++){
        		String name=se.get(i).getEventname();
        		int eventID=se.get(i).getEventID();
        		String rep=se.get(i).getRepStatus();
        		
        		addNewRow(name, eventID, rep);
        		
        	}
        }
        catch(Exception e){
        	Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
        finally{
        	entry.close();
        }
        
       
        
        return rootView;
    }
	
	public void addNewRow(String sheduleName,int sid,String status ) {

		TableRow tr = new TableRow(getActivity().getApplicationContext());

		tr.setPadding(10, 10, 10, 10);

		// Image
		ImageView imgView = new ImageView(getActivity().getApplicationContext());
		imgView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.taskmanager2));

		tr.addView(imgView);

		// Content
		TextView tv = new TextView(getActivity().getApplicationContext());
		tv.setText("    Event Name : "+sheduleName+"\n"+"" +
				   "    Event Type   : "+status);
		tv.setTextColor(Color.RED);
		tv.setTextSize(14);
		tr.addView(tv);

		TextView id = new TextView(getActivity().getApplicationContext());
		id.setText(sid+"");
		id.setVisibility(id.INVISIBLE);
		tr.addView(id);

		ImageView imgViewD = new ImageView(getActivity()
				.getApplicationContext());
		imgViewD.setImageBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.crossnew));

		tr.addView(imgViewD);

		// events
		tr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final TableRow tr1 = (TableRow) v;
				final TextView tv = (TextView) tr1.getChildAt(1);
				final TextView id = (TextView) tr1.getChildAt(2);

				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

				alert.setTitle("Do you really want to delete this?");
				alert.setIcon(getResources().getDrawable(R.drawable.alert));

				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int ids) {

						Toast.makeText(getActivity(), tv.getText().toString()+"  Deleted", Toast.LENGTH_LONG).show();
					
						tl.removeView(tr1);
						
						try{
							int eveID=Integer.parseInt(id.getText().toString());
							entry.open();
							entry.dropEvent(eveID);
							
							if(entry.checkAvailOneTime(eveID))entry.dropOneTime(eveID);
							if(entry.checkAvailRepeating(eveID))entry.dropRepeating(eveID);
							if(entry.checkDates(eveID))entry.dropDates(eveID);
							
						}
						catch(Exception e){
							
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
						}
						finally{
							entry.close();
						}
						
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					
					}
				});

				alert.show();

			}
		});

		tl.addView(tr);

	}
	

}
