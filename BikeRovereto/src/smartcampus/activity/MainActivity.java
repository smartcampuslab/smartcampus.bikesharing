package smartcampus.activity;

import java.util.ArrayList;

import smartcampus.activity.StationsActivity.OnStationSelectListener;
import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.NavigationDrawerAdapter;
import smartcampus.util.Tools;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class MainActivity extends ActionBarActivity implements
		OnStationSelectListener
{


	private String[] navTitles;
	private int[] navIcons;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<Station> stations;
	private ArrayList<Bike> bikes;
	private LocationManager mLocationManager;
	private GeoPoint myLocation;
	private OnPositionAquiredListener mCallback;

	public interface OnPositionAquiredListener
	{
		public void onPositionAquired();
	}

	public void setOnPositionAquiredListener
	(
			OnPositionAquiredListener onPositionAquiredListener)
	{
		this.mCallback = onPositionAquiredListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		stations = new ArrayList<Station>();
		bikes = new ArrayList<Bike>();

		stations.add(new Station(new GeoPoint(45.890189, 11.034275),
				"STAZIONE FF.SS. - Piazzale Orsi", 12));
		stations.add(new Station(new GeoPoint(45.882221, 11.040483),
				"OSPEDALE - Corso Verona", 12));
		stations.add(new Station(new GeoPoint(45.886525, 11.044749),
				"MUNICIPIO - Piazzetta Sichardt", 6));
		stations.add(new Station(new GeoPoint(45.893571, 11.043891),
				"MART - Corso Bettini", 6));
		stations.add(new Station(new GeoPoint(45.866352, 11.019310),
				"ZONA INDUSTRIALE - Viale Caproni", 6));
		stations.add(new Station(new GeoPoint(45.892256, 11.039370),
				"VIA PAOLI - Via Manzoni/Via Paoli", 12));
		stations.add(new Station(new GeoPoint(45.840603, 11.009298),
				"SACCO - Viale della Vittoria/Via Udine", 6));
		stations.add(new Station(new GeoPoint(45.893120, 11.038846),
				"SACCO - Viale della Vittoria/Via Udine", 12));
		stations.add(new Station(new GeoPoint(45.883409, 11.072827),
				"NORIGLIO - Via Chiesa San Martino", 6));
		stations.add(new Station(new GeoPoint(45.904255, 11.044859),
				"BRIONE - Piazza della Pace", 6));
		stations.add(new Station(new GeoPoint(45.891021, 11.038729),
				"PIAZZA ROSMINI - via boh", 6));

		bikes.add(new Bike(new GeoPoint(45.924255, 11.064859), "0"));
		bikes.get(0).addReport("test");
		stations.get(0).setUsedSlots(11);
		stations.get(0).addReport("segnalazione 1");
		stations.get(0).addReport("segnalazione 2");
		stations.get(0).addReport("segnalazione 3");
		stations.get(1).setUsedSlots(12);
		stations.get(2).setUsedSlots(6);
		stations.get(3).setUsedSlots(5);

		OsmMap mainFragment = OsmMap.newInstance(stations, bikes);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.content_frame, mainFragment);
		transaction.commit();

		navTitles = getResources().getStringArray(R.array.navTitles);

		navTitles = getResources().getStringArray(R.array.navTitles);
		navIcons = new int[]
		{ R.drawable.ic_map, R.drawable.ic_station };

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description */
			R.string.drawer_close /* "close drawer" description */
		);
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		// Set the adapter for the list view
		mDrawerList.setAdapter(new NavigationDrawerAdapter(this, navTitles,
				navIcons));
		// Set the list's click listener
		mDrawerList
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3)
					{
						switch (position)
						{
						case 0:
							OsmMap mapFragment = OsmMap.newInstance(stations,
									bikes);
							FragmentTransaction transaction = getSupportFragmentManager()
									.beginTransaction();
							transaction.setCustomAnimations(
									android.R.anim.fade_in,
									android.R.anim.fade_out,
									android.R.anim.fade_in,
									android.R.anim.fade_out);
							transaction
									.replace(R.id.content_frame, mapFragment);
							transaction.commit();
							break;
						default:
							StationsActivity stationsFragment = StationsActivity
									.newInstance(stations);
							FragmentTransaction transaction1 = getSupportFragmentManager()
									.beginTransaction();
							transaction1.setCustomAnimations(
									android.R.anim.fade_in,
									android.R.anim.fade_out,
									android.R.anim.fade_in,
									android.R.anim.fade_out);
							transaction1.replace(R.id.content_frame,
									stationsFragment);
							transaction1.commit();
							break;
						}
						mDrawerLayout.closeDrawers();
					}
				});
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStationSelected(Station station)
	{
		Log.d("station selected", station.getName());
		StationDetails detailsFragment = StationDetails.newInstance(station,
				myLocation);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_left, 0, 0,
				R.anim.slide_right);
		transaction.replace(R.id.content_frame, detailsFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				Tools.LOCATION_REFRESH_TIME, Tools.LOCATION_REFRESH_DISTANCE,
				mLocationListener);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}

	private void updateDistances()
	{
		for (Station station : stations)
		{
			station.setDistance(myLocation.distanceTo(station.getPosition()));
		}
		for (Bike bike : bikes)
		{
			bike.setDistance(myLocation.distanceTo(bike.getPosition()));
		}
	}

	private final LocationListener mLocationListener = new LocationListener()
	{

		public void onLocationChanged(final Location location)
		{
			myLocation = new GeoPoint(location);
			updateDistances();
			if (mCallback != null)
				mCallback.onPositionAquired();
		}

		@Override
		public void onProviderDisabled(String arg0)
		{
		}

		@Override
		public void onProviderEnabled(String arg0)
		{
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{
		}
	};
	
	public GeoPoint getCurrentLocation()
	{
		return myLocation;
	}
}
