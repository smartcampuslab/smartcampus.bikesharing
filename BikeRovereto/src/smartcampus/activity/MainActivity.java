package smartcampus.activity;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.osmdroid.util.GeoPoint;

import smartcampus.asynctask.GetStationsTask.AsyncStationResponse;
import smartcampus.model.Bike;
import smartcampus.model.NotificationBlock;
import smartcampus.model.Station;
import smartcampus.notifications.NotificationReceiver;
import smartcampus.util.NavigationDrawerAdapter;
import smartcampus.util.StationsHelper;
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

	private ArrayList<Fragment> frags;
	
	private Station mSelecteStation;

	private static final String FRAGMENT_MAP = "map";
	private static final String FRAGMENT_STATIONS = "stations";
	private static final String FRAGMENT_FAVOURITE = "favourite";
	public static final String FILENOTIFICATIONDB = "notificationBlockDB";
	public static final String FRAGMENT_ABOUT = "about";
	private static final int DISCONNECTED = 0;
	private static final int CONNECTED = 1;
	private static final int CONNECTING = 2;
	protected static final long CONNECTING_TIME = 120;

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

	public interface onBackListener {
		public void onBackPressed();
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

		setProgressBarIndeterminateVisibility(true);

		initialization();
		
		if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(DetailsActivity.EXTRA_STATION)){
			mSelecteStation = (Station) getIntent().getParcelableExtra(DetailsActivity.EXTRA_STATION);
		}

		frags = new ArrayList<Fragment>(3);
		frags.add(OsmMap.newInstance(bikes,mSelecteStation));
		frags.add(new StationsListFragment());
		frags.add(new FavouriteFragment());

	}

	private void initialization() {
		if (StationsHelper.isNotInitialized()) {
			StationsHelper.initialize(getApplicationContext(),
					new AsyncStationResponse() {

						@Override
						public void processFinish(int status) {
							mHandler.sendEmptyMessage(0);
						}
					});
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	private void showNoInternetDialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle(R.string.dialog_title_no_internet)
				.setMessage(R.string.dialog_msg_no_internet)
				.setPositiveButton(R.string.settings,
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
		navExtraIcons = new int[] { R.drawable.ic_action_about };

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
						switch (position) {
						case 0:
							insertMap();
							break;
						case 1:
							replaceFragment(frags.get(position),
									FRAGMENT_STATIONS, position);
							break;
						case 2:
							replaceFragment(frags.get(position),
									FRAGMENT_FAVOURITE, position);
							break;
						case 3:
							Intent i2 = new Intent(getBaseContext(),
									About.class);
							startActivity(i2);
							overridePendingTransition(R.anim.alpha_in,
									R.anim.alpha_out);
							break;
						}
						setDrawerIndicator(position);
						mDrawerLayout.closeDrawers();
					}
				});
		navAdapter.setItemChecked(0);
	}

	public void insertMap() {
		if(!replaceFragment(frags.get(0), FRAGMENT_MAP, 0)){
			OsmMap currentFragment = (OsmMap)getSupportFragmentManager()
					.findFragmentByTag(FRAGMENT_MAP);
			currentFragment.refresh();
		}
		if (getIntent().hasExtra(NotificationReceiver.INTENT_FROM_NOTIFICATION)
				&& getIntent().getBooleanExtra(
						NotificationReceiver.INTENT_FROM_NOTIFICATION, false)) {
			onStationSelected(
					(Station) getIntent().getParcelableExtra("station"), false);

		}

	}

	private boolean replaceFragment(Fragment f, String tag, int position) {
		Fragment currentFragment = getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (currentFragment == null || !currentFragment.isVisible()) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.content_frame, f, tag);
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transaction.addToBackStack(tag);
			transaction.commit();
			return true;
		}
		return false;
	}

	public void setDrawerIndicator(int position) {
		navAdapter.setItemChecked(position);
		navAdapter.notifyDataSetChanged();
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
		} else if (item.getItemId() == R.id.action_stations) {
			replaceFragment(frags.get(1), FRAGMENT_STATIONS, 1);
		}
		return super.onOptionsItemSelected(item);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			int status = checkConnection();
			if (checkConnection() == CONNECTED) {
				insertMap();
				updateDistances();
			} else if (status == CONNECTING) {
				setSupportProgressBarIndeterminateVisibility(true);
				mHandler.sendEmptyMessageDelayed(0, CONNECTING_TIME);
			} else {
				showNoInternetDialog();
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				Tools.LOCATION_REFRESH_TIME, Tools.LOCATION_REFRESH_DISTANCE,
				mLocationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}

	public void updateDistances() {
		if (stations != null && myLocation != null) {
			for (Station station : stations) {
				station.setDistance(myLocation.distanceTo(station.getPosition()));
			}
		}
		if (bikes != null && myLocation != null) {
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

	// private void getBikes() {
	// if (Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_ANARCHIC)) {
	// GetAnarchicBikesTask getBikesTask = new GetAnarchicBikesTask();
	//
	// getBikesTask.delegate = new AsyncBikesResponse() {
	// @Override
	// public void processFinish(ArrayList<Bike> result, int status) {
	//
	// bikes = result;
	// if (status != GetAnarchicBikesTask.NO_ERROR) {
	// Toast.makeText(getApplicationContext(),
	// getString(R.string.error_bikes),
	// Toast.LENGTH_SHORT).show();
	// } else {
	// mCallbackBikesAquired.bikesAquired(bikes);
	// }
	// }
	// };
	// getBikesTask.execute();
	// }
	// }

	public void addReminderForStation(NotificationBlock nb) {
		notificationBlock = NotificationBlock.readArrayListFromFile(
				FILENOTIFICATIONDB, this);
		notificationBlock.add(nb);
		NotificationBlock.saveArrayListToFile(notificationBlock,
				FILENOTIFICATIONDB, getApplicationContext());
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Fragment cf = getSupportFragmentManager().findFragmentById(
					R.id.content_frame);
			if (cf instanceof onBackListener) {
				((onBackListener) cf).onBackPressed();
			} else {
				super.onBackPressed();
			}
		}
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	@Override
	public void onStationSelected(Station station, boolean animation) {
		// TODO Auto-generated method stub

	}

	public void refresh() {
		StationsHelper.sStations.clear();
		setSupportProgressBarIndeterminateVisibility(true);
		initialization();
	}
}
