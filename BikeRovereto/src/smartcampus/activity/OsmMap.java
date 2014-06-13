package smartcampus.activity;

import java.util.ArrayList;

import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.BikeOverlayItem;
import smartcampus.util.CustomInfoWindow;
import smartcampus.util.MarkerOverlay;
import smartcampus.util.StationOverlayItem;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.views.MapController;
import eu.trentorise.smartcampus.osm.android.views.MapView;
import eu.trentorise.smartcampus.osm.android.views.overlay.MyLocationOverlay;

public class OsmMap extends ActionBarActivity
{
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

		// get the station from the parcels
		stations = getIntent().getExtras().getParcelableArrayList("stations");
		
		// get the bikes from the parcels
		bikes = getIntent().getExtras().getParcelableArrayList("bikes");

		
		// stuff for my Location
		myLoc = new MyLocationOverlay(getApplicationContext(), mapView);
		myLoc.enableMyLocation();
		myLoc.enableCompass();
		mapView.getOverlays().add(myLoc);

		// add the markers on the mapView
		addMarkers();

		setActionBar();
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
				getApplicationContext(), markers, mapView,
				new CustomInfoWindow(mapView, getApplicationContext()));

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
}
