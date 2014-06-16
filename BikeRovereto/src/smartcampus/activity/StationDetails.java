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
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class StationDetails extends ActionBarActivity
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_details);

		LayoutInflater inflater = getLayoutInflater();
		View header = inflater.inflate(R.layout.station_details_header, null);
		
		

		name=(TextView)header.findViewById(R.id.name);
		street=(TextView)header.findViewById(R.id.street);
		availableBike=(TextView)header.findViewById(R.id.available_bikes);
		availableSlots=(TextView)header.findViewById(R.id.available_slots);
		distance=(TextView)header.findViewById(R.id.distance);
				
		// get the station from the parcels
		station = getIntent().getExtras().getParcelable("station");
		mList=(ListView)findViewById(R.id.details);
		mList.addHeaderView(header, null, false);
		
		name.setText(station.getName());
		street.setText(station.getStreet());
		availableBike.setText(station.getNSlotsUsed()+"");
		availableSlots.setText(station.getNSlotsEmpty()+"");
		distance.setText(Tools.formatDistance(station.getDistance()));
		
		mList.setAdapter(new ReportsAdapter(this, 0, station.getReports()));
		
		distance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:title=todo&ll="+ station.getPosition().getLatitudeE6() + "," + station.getPosition().getLongitudeE6() + "&mode=w"));
				startActivity(i);
			}
		});		
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	protected void onStart() {
		if (station.getDistance()==Station.DISTANCE_NOT_VALID)
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Tools.LOCATION_REFRESH_TIME,
					Tools.LOCATION_REFRESH_DISTANCE, mLocationListener);
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		mLocationManager.removeUpdates(mLocationListener);
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.station_details, menu);
		return true;
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
		View dialogContent = getLayoutInflater().inflate(R.layout.report_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

	private void updateDistance()
	{
		station.setDistance(myLocation.distanceTo(station.getPosition()));
		distance.setText(Tools.formatDistance(station.getDistance()));		
	}

	private final LocationListener mLocationListener = new LocationListener() {
	    @Override
	    public void onLocationChanged(final Location location) {
	        myLocation=new GeoPoint(location);
	        updateDistance();
	    }

		@Override
		public void onProviderDisabled(String arg0) {			
		}

		@Override
		public void onProviderEnabled(String arg0) {			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}
	};

}
