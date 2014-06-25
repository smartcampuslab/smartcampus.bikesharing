package smartcampus.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.osmdroid.util.GeoPoint;

import smartcampus.model.Bike;
import smartcampus.model.NotificationBlock;
import smartcampus.model.Station;
import smartcampus.util.GetAnarchicBikesTask;
import smartcampus.util.GetAnarchicBikesTask.AsyncBikesResponse;
import smartcampus.util.GetStationsTask;
import smartcampus.util.GetStationsTask.AsyncStationResponse;
import smartcampus.util.NavigationDrawerAdapter;
import smartcampus.util.Tools;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikerovereto.R;

public class MainActivity extends ActionBarActivity implements StationsListFragment.OnStationSelectListener, FavouriteFragment.OnStationSelectListener
{

	private String[] navTitles;
	private int[] navIcons;
	private String[] navExtraTitles;
	private int[] navExtraIcons;
	public DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	public ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<Station> stations;
	private ArrayList<Station> favStations;
	private ArrayList<Bike> bikes;
	private ArrayList<NotificationBlock> notificationBlock;
	private LocationManager mLocationManager;
	private GeoPoint myLocation;
	private OnPositionAquiredListener mCallback;
	private OnStationsAquired mCallbackStationsAquired;
	private OnBikesAquired mCallbackBikesAquired;
	private NavigationDrawerAdapter navAdapter;

	private static final String FRAGMENT_MAP = "map";
	private static final String FRAGMENT_STATIONS = "stations";
	private static final String FRAGMENT_FAVOURITE = "favourite";
	public static final String FILENOTIFICATIONDB = "notificationBlockDB";

	public interface OnPositionAquiredListener
	{
		public void onPositionAquired();
	}

	public interface OnStationsAquired
	{
		public void stationsAquired(ArrayList<Station> stations);
	}

	public interface OnBikesAquired
	{
		public void bikesAquired(ArrayList<Bike> bikes);
	}

	public void setOnPositionAquiredListener(OnPositionAquiredListener onPositionAquiredListener)
	{
		this.mCallback = onPositionAquiredListener;
	}

	public void setOnStationsAquiredListener(OnStationsAquired onStationsAquired)
	{
		this.mCallbackStationsAquired = onStationsAquired;
	}

	public void setOnBikesAquiredListener(OnBikesAquired onBikesAquired)
	{
		this.mCallbackBikesAquired = onBikesAquired;
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
		{ R.drawable.nav_map, R.drawable.nav_station, R.drawable.nav_favourite };
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
						StationsListFragment stationsFragment = StationsListFragment.newInstance(stations);
						transaction.replace(R.id.content_frame, stationsFragment, FRAGMENT_STATIONS);
						transaction.commit();
					}
					// Highlight the selected item, update the title, and close
					// the drawer
					navAdapter.setItemChecked(position);
					navAdapter.notifyDataSetChanged();
					break;
				case 2:
					currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_FAVOURITE);
					if (currentFragment == null || !currentFragment.isVisible())
					{
						FavouriteFragment stationsFragment = FavouriteFragment.newInstance(favStations);
						transaction.replace(R.id.content_frame, stationsFragment, FRAGMENT_FAVOURITE);
						transaction.commit();
					}
					// Highlight the selected item, update the title, and close
					// the drawer
					navAdapter.setItemChecked(position);
					navAdapter.notifyDataSetChanged();
					break;
				case 3:
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
		setUpdateBikesPositionTimer();
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
	protected void onPause()
	{
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}

	private void updateDistances()
	{
		if (stations != null)
			for (Station station : stations)
			{
				station.setDistance(myLocation.distanceTo(station.getPosition()));
			}
		if (bikes != null)
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

	private void getStation()
	{
		/*
		 * stations = new ArrayList<Station>();
		 * 
		 * stations.add(new Station(new GeoPoint(45.890189, 11.034275),
		 * "STAZIONE FF.SS.", "Piazzale Orsi", 12, 5, 1, "01"));
		 * stations.add(new Station(new GeoPoint(45.882221, 11.040483),
		 * "OSPEDALE", "Corso Verona", 12, 5, 1, "02")); stations.add(new
		 * Station(new GeoPoint(45.886525, 11.044749), "MUNICIPIO",
		 * "Piazzetta Sichardt", 6, 5, 1, "03")); stations.add(new Station(new
		 * GeoPoint(45.893571, 11.043891), "MART", "Corso Bettini", 6, 5, 1,
		 * "04")); stations.add(new Station(new GeoPoint(45.866352, 11.019310),
		 * "ZONA INDUSTRIALE", "Viale Caproni", 6, 5, 1, "05"));
		 * stations.add(new Station(new GeoPoint(45.892256, 11.039370),
		 * "VIA PAOLI", "Via Manzoni/Via Paoli", 12, 5, 1, "06"));
		 * stations.add(new Station(new GeoPoint(45.840603, 11.009298), "SACCO",
		 * "Viale della Vittoria/Via Udine", 6, 5, 1, "07")); stations.add(new
		 * Station(new GeoPoint(45.893120, 11.038846), "SACCO",
		 * "Viale della Vittoria/Via Udine", 12, 5, 1, "08")); stations.add(new
		 * Station(new GeoPoint(45.883409, 11.072827), "NORIGLIO",
		 * "Via Chiesa San Martino", 6, 5, 1, "09")); stations.add(new
		 * Station(new GeoPoint(45.904255, 11.044859), "BRIONE",
		 * "Piazza della Pace", 6, 5, 1, "10")); stations.add(new Station(new
		 * GeoPoint(45.891021, 11.038729), "PIAZZA ROSMINI", "via boh", 6, 5, 1,
		 * "11"));
		 * 
		 * for (Station station : stations) { if (station.getFavourite())
		 * favStations.add(station); }
		 */
		favStations = new ArrayList<Station>();
		GetStationsTask getStationsTask = new GetStationsTask(this);
		getStationsTask.delegate = new AsyncStationResponse()
		{

			@Override
			public void processFinish(ArrayList<Station> result, int status)
			{
				stations = result;
				if (status != GetStationsTask.NO_ERROR)
				{
					Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
				}
				Log.d("Server call to stations finished", "status code: " + status);
				mCallbackStationsAquired.stationsAquired(stations);
			}
		};
		getStationsTask.execute("");

	}

	private void getBikes()
	{
		GetAnarchicBikesTask getBikesTask = new GetAnarchicBikesTask();
		getBikesTask.delegate = new AsyncBikesResponse()
		{

			@Override
			public void processFinish(ArrayList<Bike> result, int status)
			{
				bikes = result;
				if (status != GetAnarchicBikesTask.NO_ERROR)
				{
					Toast.makeText(getApplicationContext(), getString(R.string.error_bikes), Toast.LENGTH_SHORT).show();
				}
				mCallbackBikesAquired.bikesAquired(bikes);
			}
		};
		getBikesTask.execute();
	}

	public void addFavouriteStation(Station station)
	{
		favStations.add(station);
	}

	public void removeFavouriteStation(Station station)
	{
		favStations.remove(station);
	}

	public void addReminderForStation(NotificationBlock nb)
	{
		notificationBlock = NotificationBlock.readArrayListFromFile(FILENOTIFICATIONDB, this);
		notificationBlock.add(nb);
		NotificationBlock.saveArrayListToFile(notificationBlock, FILENOTIFICATIONDB, getApplicationContext());
	}

	public void updateRemindersListFile()
	{
		if (notificationBlock == null)
			notificationBlock = NotificationBlock.readArrayListFromFile(FILENOTIFICATIONDB, this);
		NotificationBlock.saveArrayListToFile(notificationBlock, FILENOTIFICATIONDB, getApplicationContext());
	}

	private void setUpdateBikesPositionTimer()
	{
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask()
		{
			@Override
			public void run()
			{
				handler.post(new Runnable()
				{
					@SuppressWarnings("unchecked")
					public void run()
					{
						try
						{
							GetAnarchicBikesTask getBikesTask = new GetAnarchicBikesTask();
							getBikesTask.delegate = new AsyncBikesResponse()
							{

								@Override
								public void processFinish(ArrayList<Bike> result, int status)
								{
									bikes = result;
									Log.d("prova", "executed");
									mCallbackBikesAquired.bikesAquired(bikes);
								}
							};
							getBikesTask.execute();
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 4000);
	}

}
