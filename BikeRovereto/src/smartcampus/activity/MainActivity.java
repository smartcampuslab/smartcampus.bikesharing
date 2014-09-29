package smartcampus.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.ArrayUtils;
import org.osmdroid.util.GeoPoint;

import smartcampus.asynctask.GetAnarchicBikesTask;
import smartcampus.asynctask.GetAnarchicBikesTask.AsyncBikesResponse;
import smartcampus.asynctask.GetStationsTask;
import smartcampus.asynctask.GetStationsTask.AsyncStationResponse;
import smartcampus.model.Bike;
import smartcampus.model.NotificationBlock;
import smartcampus.model.Station;
import smartcampus.notifications.NotificationReceiver;
import smartcampus.util.NavigationDrawerAdapter;
import smartcampus.util.Tools;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikesharing.R;

public class MainActivity extends ActionBarActivity implements
		StationsListFragment.OnStationSelectListener,
		FavouriteFragment.OnStationSelectListener {

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
	private OnStationRefresh mCallbackStationRefreshed;

	private OnBikesAquired mCallbackBikesAquired;
	private OnBikesRefresh mCallbackBikesRefreshed;
	private NavigationDrawerAdapter navAdapter;

	private Timer timer;
	private static final String FRAGMENT_MAP = "map";
	private static final String FRAGMENT_STATIONS = "stations";
	private static final String FRAGMENT_FAVOURITE = "favourite";
	public static final String FILENOTIFICATIONDB = "notificationBlockDB";
	public static final String FRAGMENT_ABOUT = "about";
	private static final int DISCONNECTED = 0;
	private static final int CONNECTED = 1;
	private static final int CONNECTING = 2;

	public interface OnPositionAquiredListener {
		public void onPositionAquired();
	}

	// stations
	public interface OnStationsAquired {
		public void stationsAquired(ArrayList<Station> stations);
	}

	public interface OnStationRefresh {
		public void stationsRefreshed(ArrayList<Station> stations);
	}

	// bikes
	public interface OnBikesAquired {
		public void bikesAquired(ArrayList<Bike> bikes);
	}

	public interface OnBikesRefresh {
		public void bikesRefreshed(ArrayList<Bike> bikes);
	}

	public void setOnPositionAquiredListener(
			OnPositionAquiredListener onPositionAquiredListener) {
		this.mCallback = onPositionAquiredListener;
	}

	public void setOnStationRefresh(OnStationRefresh onStationRefresh) {
		this.mCallbackStationRefreshed = onStationRefresh;
	}

	public void setOnStationsAquiredListener(OnStationsAquired onStationsAquired) {
		this.mCallbackStationsAquired = onStationsAquired;
	}

	public void setOnBikesAquiredListener(OnBikesAquired onBikesAquired) {
		this.mCallbackBikesAquired = onBikesAquired;
	}

	public void setOnBikesRefresh(OnBikesRefresh onBikesRefresh) {
		this.mCallbackBikesRefreshed = onBikesRefresh;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		checkManifestConfiguration();

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		prepareNavigationDrawer();

	}

	private void showNoInternetDialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle(R.string.dialog_title_no_internet)
				.setMessage(R.string.dialog_msg_no_internet)
				.setPositiveButton(R.string.dialog_msg_settings_button,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								startActivity(new Intent(
										Settings.ACTION_SETTINGS));
							}
						})
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								MainActivity.this.finish();
							}
						}).create().show();
	}

	private int checkConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null)
			return DISCONNECTED;
		if (activeNetwork.isConnected())
			return CONNECTED;
		if (activeNetwork.isConnectedOrConnecting())
			return CONNECTING;
		return DISCONNECTED;
	}

	private void prepareNavigationDrawer() {
		navTitles = getResources().getStringArray(R.array.navTitles);
		navIcons = new int[] { R.drawable.nav_map, R.drawable.nav_station,
				R.drawable.nav_favourite };

		if (Tools.BIKE_TYPES != null
				&& !Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_EMOTION)) {
			navTitles = ArrayUtils.remove(navTitles, 1);
			navIcons = ArrayUtils.remove(navIcons, 1);
		}

		navExtraTitles = getResources().getStringArray(R.array.navExtraTitles);
		navExtraIcons = new int[] { R.drawable.nav_settings,
				R.drawable.ic_action_about };

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				supportInvalidateOptionsMenu();
			}
		};
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Set the adapter for the list view
		navAdapter = new NavigationDrawerAdapter(this, navTitles, navIcons,
				navExtraTitles, navExtraIcons);
		mDrawerList.setAdapter(navAdapter);

		// Set the list's click listener
		mDrawerList
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Fragment currentFragment;
						FragmentTransaction transaction = getSupportFragmentManager()
								.beginTransaction();
						transaction
								.setCustomAnimations(android.R.anim.fade_in,
										android.R.anim.fade_out,
										android.R.anim.fade_in,
										android.R.anim.fade_out);
						switch (position) {
						case 0:
							currentFragment = getSupportFragmentManager()
									.findFragmentByTag(FRAGMENT_MAP);
							if (currentFragment == null
									|| !currentFragment.isVisible()) {
								OsmMap mapFragment = OsmMap.newInstance(
										stations, bikes);
								transaction.replace(R.id.content_frame,
										mapFragment, FRAGMENT_MAP);
								transaction.commit();
							}
							// Highlight the selected item, update the title,
							// and close
							// the drawer
							navAdapter.setItemChecked(position);
							navAdapter.notifyDataSetChanged();
							break;
						case 1:
							currentFragment = getSupportFragmentManager()
									.findFragmentByTag(FRAGMENT_STATIONS);
							if (currentFragment == null
									|| !currentFragment.isVisible()) {
								StationsListFragment stationsFragment = StationsListFragment
										.newInstance(stations);
								transaction.replace(R.id.content_frame,
										stationsFragment, FRAGMENT_STATIONS);
								transaction.commit();
							}
							// Highlight the selected item, update the title,
							// and close
							// the drawer
							navAdapter.setItemChecked(position);
							navAdapter.notifyDataSetChanged();
							break;
						case 2:
							currentFragment = getSupportFragmentManager()
									.findFragmentByTag(FRAGMENT_FAVOURITE);
							if (currentFragment == null
									|| !currentFragment.isVisible()) {
								FavouriteFragment stationsFragment = FavouriteFragment
										.newInstance(favStations);
								transaction.replace(R.id.content_frame,
										stationsFragment, FRAGMENT_FAVOURITE);
								transaction.commit();
							}
							// Highlight the selected item, update the title,
							// and close
							// the drawer
							navAdapter.setItemChecked(position);
							navAdapter.notifyDataSetChanged();
							break;
						case 3:
							Intent i = new Intent(getBaseContext(),
									SettingsActivity.class);
							startActivity(i);
							break;
						case 4:
							Intent i2 = new Intent(getBaseContext(),
									About.class);
							startActivity(i2);
							overridePendingTransition(R.anim.slide_up,
									R.anim.slide_up_slower);
							break;
						}
						mDrawerLayout.closeDrawers();
					}
				});
		navAdapter.setItemChecked(0);
	}

	private void insertMap() {
		OsmMap mainFragment = OsmMap.newInstance(stations, bikes);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.content_frame, mainFragment, FRAGMENT_MAP);
		transaction.commit();
		if (getIntent().getBooleanExtra(
				NotificationReceiver.INTENT_FROM_NOTIFICATION, false)) {
			onStationSelected(
					(Station) getIntent().getParcelableExtra("station"), false);
		}
	}

	private void checkManifestConfiguration() {
		try {
			ApplicationInfo app = getPackageManager().getApplicationInfo(
					this.getPackageName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_META_DATA);
			Bundle metaData = app.metaData;

			String errorString = null;

			if (metaData == null) {
				errorString = "Metadata not configured!";
			} else if (metaData.get(Tools.METADATA_SERVICE_URL) == null
					|| (metaData.get(Tools.METADATA_SERVICE_URL) + "")
							.equals("")) {
				errorString = "Metadata: service URL not configured!";
			} else if (metaData.get(Tools.METADATA_CITY_CODE) == null
					|| (metaData.get(Tools.METADATA_CITY_CODE) + "").equals("")) {
				errorString = "Metadata: city code not configured!";
			} else if (metaData.get(Tools.METADATA_BIKE_TYPES) == null
					|| (metaData.get(Tools.METADATA_BIKE_TYPES) + "")
							.equals("")) {
				errorString = "Metadata: bike types not configured!";
			}

			if (errorString != null) {
				Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
				Log.e("BIKESHARING", errorString);
				finish();
			} else {
				String serviceUrl = ""
						+ metaData.get(Tools.METADATA_SERVICE_URL);
				Tools.SERVICE_URL = serviceUrl;
				String cityCode = "" + metaData.get(Tools.METADATA_CITY_CODE);
				Tools.CITY_CODE = cityCode;
				String bikeTypesString = ""
						+ metaData.get(Tools.METADATA_BIKE_TYPES);
				Tools.BIKE_TYPES = bikeTypesString.split(";");

				Log.e("BIKESHARING", "EVERYTHING SEEMS TO BE RIGHT!\n"
						+ Tools.SERVICE_URL + "\n" + Tools.CITY_CODE + "\n"
						+ bikeTypesString);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		hideMenuItems(menu, !drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private void hideMenuItems(Menu menu, boolean visible) {
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(visible);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStationSelected(Station station, boolean animation) {
		Log.d("station selected", station.getName());
		StationDetails detailsFragment = StationDetails.newInstance(station);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (animation) {
			transaction.setCustomAnimations(R.anim.slide_left,
					R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_right);
		}
		transaction.replace(R.id.content_frame, detailsFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				Tools.LOCATION_REFRESH_TIME, Tools.LOCATION_REFRESH_DISTANCE,
				mLocationListener);

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			int status = checkConnection();
			if (checkConnection() == CONNECTED) {
				insertMap();
				getStation();
				getBikes();
				updateDistances();
			} else if (status == CONNECTING) {
				setSupportProgressBarIndeterminateVisibility(true);
				mHandler.sendEmptyMessageDelayed(0, 120);
			} else {
				showNoInternetDialog();
			}
		}
	};

	@Override
	protected void onPostResume() {

		super.onPostResume();
		setSupportProgressBarIndeterminateVisibility(true);
		mHandler.sendEmptyMessage(0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
		stopTimer();
	}

	public void updateDistances() {
		if (stations != null) {
			for (Station station : stations) {
				station.setDistance(myLocation.distanceTo(station.getPosition()));
			}
		}
		if (bikes != null) {
			for (Bike bike : bikes) {
				bike.setDistance(myLocation.distanceTo(bike.getPosition()));
			}
		}
	}

	private final LocationListener mLocationListener = new LocationListener() {
		public void onLocationChanged(final Location location) {
			myLocation = new GeoPoint(location);
			updateDistances();
			if (mCallback != null)
				mCallback.onPositionAquired();
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

	public GeoPoint getCurrentLocation() {
		return myLocation;
	}

	public void setCurrentLocation(GeoPoint newPositon) {
		myLocation = newPositon;
		updateDistances();
		if (mCallback != null) {
			mCallback.onPositionAquired();
		}
	}

	private void getStation() {
		favStations = new ArrayList<Station>();
		GetStationsTask getStationsTask = new GetStationsTask(this);
		getStationsTask.delegate = new AsyncStationResponse() {
			@Override
			public void processFinish(ArrayList<Station> result,
					ArrayList<Station> favs, int status) {
				stations = result;
				favStations = favs;
				if (status != GetStationsTask.NO_ERROR) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.error), Toast.LENGTH_SHORT)
							.show();
				} else {
					if (myLocation != null)
						updateDistances();
					mCallbackStationsAquired.stationsAquired(stations);
				}
			}
		};
		getStationsTask.execute("");
	}

	private void getBikes() {
		if (Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_ANARCHIC)) {
			GetAnarchicBikesTask getBikesTask = new GetAnarchicBikesTask();

			getBikesTask.delegate = new AsyncBikesResponse() {
				@Override
				public void processFinish(ArrayList<Bike> result, int status) {

					bikes = result;
					if (status != GetAnarchicBikesTask.NO_ERROR) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.error_bikes),
								Toast.LENGTH_SHORT).show();
					} else {
						mCallbackBikesAquired.bikesAquired(bikes);
					}
				}
			};
			getBikesTask.execute();
		}
	}

	public void setStations(ArrayList<Station> stations) {
		this.stations.clear();
		this.stations.addAll(stations);
	}

	public void setFavStations(ArrayList<Station> favStations) {
		this.favStations.clear();
		this.favStations.addAll(favStations);
	}

	public void addFavouriteStation(Station station) {
		favStations.add(station);
	}

	public void removeFavouriteStation(Station station) {
		favStations.remove(station);
	}

	public void addReminderForStation(NotificationBlock nb) {
		notificationBlock = NotificationBlock.readArrayListFromFile(
				FILENOTIFICATIONDB, this);
		notificationBlock.add(nb);
		NotificationBlock.saveArrayListToFile(notificationBlock,
				FILENOTIFICATIONDB, getApplicationContext());
	}

	private void setUpdateTimer() {
		final Handler handler = new Handler();
		timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							getBikes();
							getStation();
							updateDistances();

						} catch (Exception e) {
						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 40000);
	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	public void startTimer() {
		setUpdateTimer();
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			super.onBackPressed();
		}
	}

	public ArrayList<Station> getStations() {
		return stations;
	}
}
