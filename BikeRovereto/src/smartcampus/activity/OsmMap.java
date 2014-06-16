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
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.BoundingBoxE6;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;
import eu.trentorise.smartcampus.osm.android.views.MapController;
import eu.trentorise.smartcampus.osm.android.views.MapView;
import eu.trentorise.smartcampus.osm.android.views.overlay.MyLocationOverlay;

public class OsmMap extends Fragment
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
	ToggleButton mToggle;
	
	
	public OsmMap(){}
	
	public static OsmMap newInstance(ArrayList<Station> stations){		
		OsmMap fragment = new OsmMap();
	    Bundle bundle = new Bundle();
	    bundle.putParcelableArrayList("stations", stations);
	    fragment.setArguments(bundle);
	    return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		stations = getArguments().getParcelableArrayList("stations");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_osm_map, container,false);
		// get the mapView and the controller
		mapView = (MapView) rootView.findViewById(R.id.map_view);
		//mToggle = (ToggleButton) findViewById(R.id.togglebutton); TODO: implement
		
		mapController = mapView.getController();

		bikes=new ArrayList<Bike>();
		// mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		// to keep the display on
		// mapView.setKeepScreenOn(true);

		// stuff for my Location
		myLoc = new MyLocationOverlay(getActivity(), mapView);
		myLoc.enableCompass();

		// add the markers on the mapView
		addMarkers();

		//setActionBar();

		//setSwitch();

		mapView.getOverlays().add(myLoc);
		

		mapView.setScrollableAreaLimit(getBoundingBox());
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("wtf!","ineedzoom!");
		mapView.zoomToBoundingBox(getBoundingBox());
		mapView.setMinZoomLevel(mapView.getZoomLevel());
	}
	
/* TODO: what?!?
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		mapView.zoomToBoundingBox(getBoundingBox());
		mapView.setMinZoomLevel(mapView.getZoomLevel());
	}
*/
		
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);		
		return true;
	}

	private void addMarkers()
	{
		addBikesMarkers();
		addStationsMarkers();
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
				getActivity(), markers, mapView, new BikeInfoWindow(
						mapView, getActivity()));

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
				getActivity(), markers, mapView,
				new CustomInfoWindow(mapView, getActivity()));

		mapView.getOverlays().add(stationsMarkersOverlay);
	}
/* TODO: implement this!
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
*/
	private void setOnClickSwitch()
	{
		
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
		

		mToggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					// close the bubble relative to the bikesMarkers if opened
					bikesMarkersOverlay.hideBubble();

					// remove the anarchic bikes
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

	private BoundingBoxE6 getBoundingBox()
	{
		BoundingBoxE6 toRtn;
		BoundingBoxE6 stationsBoundingBox = Station.getBoundingBox(stations);
		BoundingBoxE6 bikesBoundingBox = Bike.getBoundingBox(bikes);
		toRtn = new BoundingBoxE6(
				stationsBoundingBox.getLatNorthE6() > bikesBoundingBox.getLatNorthE6() ? stationsBoundingBox.getLatNorthE6()
						: bikesBoundingBox.getLatNorthE6(),
						
				stationsBoundingBox.getLonEastE6() > bikesBoundingBox
						.getLonEastE6() ? stationsBoundingBox.getLonEastE6()
						: bikesBoundingBox.getLonEastE6(),
						
				stationsBoundingBox.getLatSouthE6() < bikesBoundingBox
						.getLatSouthE6() ? stationsBoundingBox.getLatSouthE6()
						: bikesBoundingBox.getLatSouthE6(),
						
				stationsBoundingBox.getLonWestE6() < bikesBoundingBox
						.getLonWestE6() ? stationsBoundingBox.getLonWestE6()
						: bikesBoundingBox.getLonWestE6());
		return toRtn;
	}
}