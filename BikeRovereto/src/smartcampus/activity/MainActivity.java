package smartcampus.activity;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import smartcampus.activity.StationsActivity.OnStationSelectListener;
import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.NavigationDrawerAdapter;
import smartcampus.util.Tools;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class MainActivity extends ActionBarActivity implements OnStationSelectListener
{

	private String[] navTitles;
	private int[] navIcons;
	private String[] navExtraTitles;
	private int[] navExtraIcons;
	public DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	public ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<Station> stations;
	private ArrayList<Bike> bikes;
	private LocationManager mLocationManager;
	private GeoPoint myLocation;
	private OnPositionAquiredListener mCallback;
	private NavigationDrawerAdapter navAdapter;

	private static final String FRAGMENT_MAP = "map";
	private static final String FRAGMENT_STATIONS = "stations";

	public interface OnPositionAquiredListener
	{
		public void onPositionAquired();
	}

	public void setOnPositionAquiredListener(OnPositionAquiredListener onPositionAquiredListener)
	{
		this.mCallback = onPositionAquiredListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getStation();
		getBikes();
		
		
		OsmMap mainFragment = OsmMap.newInstance(stations, bikes);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, mainFragment, FRAGMENT_MAP);
		transaction.commit();

		navTitles = getResources().getStringArray(R.array.navTitles);
		navIcons = new int[]
		{ R.drawable.ic_map, R.drawable.ic_station };
		navExtraTitles = getResources().getStringArray(R.array.navExtraTitles);
		navExtraIcons = new int[]
		{ R.drawable.nav_settings };

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		)
		{
			public void onDrawerClosed(View view)
			{
				super.onDrawerClosed(view);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				supportInvalidateOptionsMenu();
			}
		};
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		// Set the adapter for the list view
		navAdapter = new NavigationDrawerAdapter(this, navTitles, navIcons, navExtraTitles, navExtraIcons);
		mDrawerList.setAdapter(navAdapter);
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				Fragment currentFragment;
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
				switch (position)
				{
				case 0:
					currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_MAP);
					if (currentFragment == null || !currentFragment.isVisible())
					{
						OsmMap mapFragment = OsmMap.newInstance(stations, bikes);
						transaction.replace(R.id.content_frame, mapFragment, FRAGMENT_MAP);
						transaction.commit();
					}
					// Highlight the selected item, update the title, and close
					// the drawer
					navAdapter.setItemChecked(position);
					navAdapter.notifyDataSetChanged();
					break;
				case 1:
					currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_STATIONS);
					if (currentFragment == null || !currentFragment.isVisible())
					{
						StationsActivity stationsFragment = StationsActivity.newInstance(stations);
						transaction.replace(R.id.content_frame, stationsFragment, FRAGMENT_STATIONS);
						transaction.commit();
					}
					// Highlight the selected item, update the title, and close
					// the drawer
					navAdapter.setItemChecked(position);
					navAdapter.notifyDataSetChanged();
					break;
				case 2:
					Intent i = new Intent(getBaseContext(), SettingsActivity.class);
					i.putParcelableArrayListExtra("stations", stations);
					startActivity(i);
					break;
				}
				mDrawerLayout.closeDrawers();
			}
		});
		navAdapter.setItemChecked(0);
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// createNotification();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		hideMenuItems(menu, !drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private void hideMenuItems(Menu menu, boolean visible)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			menu.getItem(i).setVisible(visible);
		}
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
		StationDetails detailsFragment = StationDetails.newInstance(station);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_left, R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_right);
		transaction.replace(R.id.content_frame, detailsFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Tools.LOCATION_REFRESH_TIME, Tools.LOCATION_REFRESH_DISTANCE, mLocationListener);

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
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

	public void setCurrentLocation(GeoPoint newPositon)
	{
		myLocation = newPositon;
		updateDistances();
		if (mCallback != null)
			mCallback.onPositionAquired();
	}

	private void createNotification()
	{
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.bike_available).setContentTitle("Bike Rovereto").setContentText("Hello World!").setSound(
				RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(BIND_AUTO_CREATE, mBuilder.build());
	}

	private void getStation()
	{

		stations = new ArrayList<Station>();

		stations.add(new Station(new GeoPoint(45.890189, 11.034275), "STAZIONE FF.SS.", "Piazzale Orsi", 12));
		stations.add(new Station(new GeoPoint(45.882221, 11.040483), "OSPEDALE", "Corso Verona", 12));
		stations.add(new Station(new GeoPoint(45.886525, 11.044749), "MUNICIPIO", "Piazzetta Sichardt", 6));
		stations.add(new Station(new GeoPoint(45.893571, 11.043891), "MART", "Corso Bettini", 6));
		stations.add(new Station(new GeoPoint(45.866352, 11.019310), "ZONA INDUSTRIALE", "Viale Caproni", 6));
		stations.add(new Station(new GeoPoint(45.892256, 11.039370), "VIA PAOLI", "Via Manzoni/Via Paoli", 12));
		stations.add(new Station(new GeoPoint(45.840603, 11.009298), "SACCO", "Viale della Vittoria/Via Udine", 6));
		stations.add(new Station(new GeoPoint(45.893120, 11.038846), "SACCO", "Viale della Vittoria/Via Udine", 12));
		stations.add(new Station(new GeoPoint(45.883409, 11.072827), "NORIGLIO", "Via Chiesa San Martino", 6));
		stations.add(new Station(new GeoPoint(45.904255, 11.044859), "BRIONE", "Piazza della Pace", 6));
		stations.add(new Station(new GeoPoint(45.891021, 11.038729), "PIAZZA ROSMINI", "via boh", 6));

		stations.get(0).setUsedSlots(11);
		stations.get(0).addReport("segnalazione 1");
		stations.get(0).addReport("segnalazione 2");
		stations.get(0).addReport("segnalazione 3");
		stations.get(1).setUsedSlots(12);
		stations.get(2).setUsedSlots(6);
		stations.get(3).setUsedSlots(5);
	}

	private void getBikes()
	{
		bikes = new ArrayList<Bike>();

		bikes.add(new Bike(new GeoPoint(45.924255, 11.064859), "0"));
		bikes.get(0).addReport("test");

	}
}
