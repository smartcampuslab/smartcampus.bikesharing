package smartcampus.activity;

import java.util.ArrayList;

import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.BikeInfoWindow;
import smartcampus.util.BikeOverlayItem;
import smartcampus.util.CustomInfoWindow;
import smartcampus.util.MarkerOverlay;
import smartcampus.util.StationOverlayItem;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;
import eu.trentorise.smartcampus.osm.android.views.MapController;
import eu.trentorise.smartcampus.osm.android.views.MapView;
import eu.trentorise.smartcampus.osm.android.views.overlay.MyLocationOverlay;

public class OsmMap extends ActionBarActivity
{

	// menu
	private static final int MENU_ON_STATION_ID = 0;

	// the view where the map is showed
	private MapView mapView;

	// the tools to control the map
	private MapController mapController;

	MyLocationOverlay myLoc;

	// the stations to show in the map
	ArrayList<Station> stations;
	ArrayList<Bike> bikes;

	// marker for the stations
	MarkerOverlay<StationOverlayItem> stationsMarkersOverlay;

	// marker for the bikes
	MarkerOverlay<BikeOverlayItem> bikesMarkersOverlay;

	// switch
	ToggleButton btSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_osm_map);

		// get the mapView and the controller
		mapView = (MapView) findViewById(R.id.map_view);
		mapController = mapView.getController();

		// mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		// to keep the display on
		mapView.setKeepScreenOn(true);

		setUpPointers();

		// stuff for my Location
		myLoc = new MyLocationOverlay(getApplicationContext(), mapView);
		myLoc.enableMyLocation();
		myLoc.enableCompass();
		mapView.getOverlays().add(myLoc);

		// add the markers on the mapView
		addMarkers();

		setActionBar();

		setSwitch();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		mapView.zoomToBoundingBox(Station.getBoundingBox(stations));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, MENU_ON_STATION_ID, Menu.NONE,
				R.string.title_activity_stations);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		super.onOptionsItemSelected(item);
		
		
		switch (item.getItemId())
		{
		case MENU_ON_STATION_ID:
			Intent listIntent = new Intent(getApplicationContext(),
					StationsActivity.class);

			listIntent.putParcelableArrayListExtra("stations", stations);

			startActivity(listIntent);
			
			break;
		default:
			break;
		}
		return true;
	}

	private void addMarkers()
	{
		addStationsMarkers();
		addBikesMarkers();
	}

	private void addBikesMarkers()
	{
		Resources res = getResources();

		ArrayList<BikeOverlayItem> markers = new ArrayList<BikeOverlayItem>();

		for (int i = 0; i < bikes.size(); i++)
		{
			markers.add(new BikeOverlayItem(bikes.get(i).getId(), "bike", bikes
					.get(i).getPosition(), bikes.get(i)));

			Drawable markerImage = null;
			markerImage = res.getDrawable(R.drawable.anarchich_bike);

			markers.get(i).setMarker(markerImage);
		}

		bikesMarkersOverlay = new MarkerOverlay<BikeOverlayItem>(
				getApplicationContext(), markers, mapView, new BikeInfoWindow(
						mapView, getApplicationContext()));

		mapView.getOverlays().add(bikesMarkersOverlay);
	}

	private void addStationsMarkers()
	{
		// markers at:
		// http://openclipart.org/detail/184847/map-marker-vector-by-rfvectors.com-184847
		Resources res = getResources();

		ArrayList<StationOverlayItem> markers = new ArrayList<StationOverlayItem>();

		for (int i = 0; i < stations.size(); i++)
		{
			markers.add(new StationOverlayItem(stations.get(i).getName(),
					"station", stations.get(i).getPosition(), stations.get(i)));

			Drawable markerImage = null;
			if (stations.get(i).getBikesPresentPercentage() > 0.5)
			{
				markerImage = res.getDrawable(R.drawable.marker_green);
			}
			else if (stations.get(i).getBikesPresentPercentage() > 0.2)
			{
				markerImage = res.getDrawable(R.drawable.marker_yellow);
			}
			else
			{
				markerImage = res.getDrawable(R.drawable.marker_red);
			}

			markers.get(i).setMarker(markerImage);
		}

		stationsMarkersOverlay = new MarkerOverlay<StationOverlayItem>(
				getApplicationContext(), markers, mapView,
				new CustomInfoWindow(mapView, getApplicationContext()));

		mapView.getOverlays().add(stationsMarkersOverlay);
	}

	private void setActionBar()
	{
		LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
				.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View customActionBarView = inflater.inflate(R.layout.actionbar_custom,
				null);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setCustomView(customActionBarView,
				new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
	}

	private void setOnClickSwitch()
	{
		ToggleButton mToggle = (ToggleButton) findViewById(R.id.togglebutton);
		mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					Log.d("debug", "pressed");
				}
				else
				{
					Log.d("debug", "Notpressed");
				}
			}
		});
	}

	private void setSwitch()
	{
		btSwitch = (ToggleButton) findViewById(R.id.togglebutton);

		btSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					mapView.getOverlays().remove(bikesMarkersOverlay);
					mapView.invalidate();
				}
				else
				{
					mapView.getOverlays().add(bikesMarkersOverlay);
					mapView.invalidate();
				}
			}
		});

	}
	
	private void setUpPointers()
	{
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
		stations.add(new Station(new GeoPoint(45.891021, 11.038729), "PIAZZA ROSMINI - via boh",
				6));

		bikes.add(new Bike(new GeoPoint(45.924255, 11.064859), "0"));
		stations.get(0).setUsedSlots(11);
		stations.get(0).addReport("segnalazione 1");
		stations.get(0).addReport("segnalazione 2");
		stations.get(0).addReport("segnalazione 3");
		stations.get(1).setUsedSlots(12);
		stations.get(2).setUsedSlots(6);
		stations.get(3).setUsedSlots(5);
	}
}
