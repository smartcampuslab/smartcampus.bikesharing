package smartcampus.activity;

import smartcampus.model.Station;
import smartcampus.util.ReportsAdapter;
import smartcampus.util.Tools;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class StationDetails extends Fragment
{

	// the station with its details
	Station station;
	
	ListView mList;
	GeoPoint myLocation;
	private LocationManager mLocationManager;
	TextView name;
	TextView street;
	TextView availableBike, availableSlots;
	TextView distance;	
	
	public StationDetails(){}
	
	
	public static StationDetails newInstance(Station station){
		StationDetails fragment = new StationDetails();
	    Bundle bundle = new Bundle();
	    bundle.putParcelable("station", station);
	    fragment.setArguments(bundle);

	    return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_station_details, container,false);
		View header = inflater.inflate(R.layout.station_details_header, null);
		
		name=(TextView)header.findViewById(R.id.name);
		street=(TextView)header.findViewById(R.id.street);
		availableBike=(TextView)header.findViewById(R.id.available_bikes);
		availableSlots=(TextView)header.findViewById(R.id.available_slots);
		distance=(TextView)header.findViewById(R.id.distance);
				
		// get the station from the parcels
		station = getArguments().getParcelable("station");
		mList=(ListView)rootView.findViewById(R.id.details);
		mList.addHeaderView(header, null, false);
		
		name.setText(station.getName());
		street.setText(station.getStreet());
		availableBike.setText(station.getNSlotsUsed()+"");
		availableSlots.setText(station.getNSlotsEmpty()+"");
		distance.setText(Tools.formatDistance(station.getDistance()));
		
		mList.setAdapter(new ReportsAdapter(getActivity() , 0, station.getReports()));
		
		distance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+ station.getLatitudeDegree() + "," + station.getLongitudeDegree()));
				startActivity(i);
			}
		});		
		mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
		setHasOptionsMenu(true);
		return rootView;
	}		
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.station_details, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id){
			case R.id.action_add_report:
				addReport();
				break;
			case R.id.action_settings:
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	private void addReport() {
		View dialogContent = getActivity().getLayoutInflater().inflate(R.layout.report_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getString(R.string.report_in)+ " "+station.getName());
		builder.setPositiveButton(R.string.report, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   //TODO: implement report
	           }
	       });
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {	               
	           }
	       });
		builder.setView(dialogContent);
		AlertDialog dialog = builder.create();
		
		dialog.show();
		
	}

}
