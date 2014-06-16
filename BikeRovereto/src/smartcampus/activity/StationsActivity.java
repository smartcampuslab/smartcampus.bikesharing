package smartcampus.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import smartcampus.model.Station;
import smartcampus.util.StationsAdapter;
import smartcampus.util.Tools;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class StationsActivity extends ActionBarActivity{
	
	ArrayList<Station> mStations;
	ListView mList;
	StationsAdapter stationsAdapter;
	GeoPoint myLocation;
	private LocationManager mLocationManager;
	String[] navTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stations_main);		
		mStations=getIntent().getExtras().getParcelableArrayList("stations");
		
		stationsAdapter = new StationsAdapter(this, 0, mStations);
		
		mList = (ListView)findViewById(R.id.stations_list);
		mList.setDivider(new ColorDrawable(Color.TRANSPARENT));
		mList.setDividerHeight(Tools.convertDpToPixel(getBaseContext(), 5));
		mList.setAdapter(stationsAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent detailsIntent = new Intent(getApplicationContext(), StationDetails.class);
				detailsIntent.putExtra("station", mStations.get(position));
				startActivity(detailsIntent);
			}
		});
		stationsAdapter.notifyDataSetChanged();
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		navTitles= getResources().getStringArray(R.array.navTitles);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, navTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch (position) {
				case 0:					
					startActivity(new Intent(getApplicationContext(), OsmMap.class));
					break;
				default:					
					startActivity(new Intent(getApplicationContext(), StationsActivity.class));
					break;
				}
			}
		});
		
	}
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	
	@Override
	protected void onStart() {
		boolean allDistancesValid=true;
		for (int i=0; i<mStations.size() && allDistancesValid; i++)
		{
			if (mStations.get(i).getDistance()==Station.DISTANCE_NOT_VALID)
				allDistancesValid=false;
		}
		if (!allDistancesValid)
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.stations, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.sort_distance:
			sortByDistance();
			break;
		case R.id.sort_available_bikes:
			sortByAvailableBikes();
			break;
		case R.id.sort_available_slots:
			sortByAvailableSlots();
			break;
		case R.id.sort_name:
			sortByName();
			break;
		}
		return true;
	}
	
	
	private void sortByDistance(){
		Collections.sort(mStations, new DistanceComparator());	
		stationsAdapter.notifyDataSetChanged();
	}
	private void sortByName(){
		Collections.sort(mStations, new NameComparator());
		stationsAdapter.notifyDataSetChanged();
	}
	private void sortByAvailableSlots(){
		Collections.sort(mStations, new AvailableSlotsComparator());
		stationsAdapter.notifyDataSetChanged();
	}
	private void sortByAvailableBikes(){
		Collections.sort(mStations, new AvailableBikesComparator());
		stationsAdapter.notifyDataSetChanged();
	}
	private class AvailableSlotsComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station1.getNSlotsEmpty()-station0.getNSlotsEmpty();
		}
		
	}
	private class AvailableBikesComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station1.getNSlotsUsed()-station0.getNSlotsUsed();
		}
		
	}
	private class DistanceComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getDistance()-station1.getDistance();
		}
		
	}
	private class NameComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getName().compareToIgnoreCase(station1.getName());
		}
		
	}
	
	private void updateDistances()
	{
		for (Station station : mStations){
			station.setDistance(myLocation.distanceTo(station.getPosition()));			
		}
	}
	
	private final LocationListener mLocationListener = new LocationListener() {
	    @Override
	    public void onLocationChanged(final Location location) {
	        myLocation=new GeoPoint(location);
	        updateDistances();
	        stationsAdapter.notifyDataSetChanged();
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
