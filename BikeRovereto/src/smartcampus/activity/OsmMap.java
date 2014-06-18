package smartcampus.activity;

import java.util.ArrayList;

import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.BikeInfoWindow;
import smartcampus.util.BikeOverlayItem;
import smartcampus.util.CustomInfoWindow;
import smartcampus.util.MarkerOverlay;
import smartcampus.util.StationOverlayItem;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.BoundingBoxE6;
import eu.trentorise.smartcampus.osm.android.views.MapController;
import eu.trentorise.smartcampus.osm.android.views.MapView;
import eu.trentorise.smartcampus.osm.android.views.overlay.compass.CompassOverlay;
import eu.trentorise.smartcampus.osm.android.views.overlay.compass.InternalCompassOrientationProvider;
import eu.trentorise.smartcampus.osm.android.views.overlay.mylocation.GpsMyLocationProvider;
import eu.trentorise.smartcampus.osm.android.views.overlay.mylocation.MyLocationNewOverlay;

public class OsmMap extends Fragment
{

	// menu
	private static final int MENU_ON_STATION_ID = 0;

	// the view where the map is showed
	private MapView mapView;

	// the tools to control the map
	private MapController mapController;

	private MyLocationNewOverlay myLoc;
	// the stations to show in the map
	private ArrayList<Station> stations;

	private ArrayList<Bike> bikes;

	// marker for the stations
	private MarkerOverlay<StationOverlayItem> stationsMarkersOverlay;

	// marker for the bikes
	private MarkerOverlay<BikeOverlayItem> bikesMarkersOverlay;

	public static OsmMap newInstance(ArrayList<Station> stations,
			ArrayList<Bike> bikes)
	{
		OsmMap fragment = new OsmMap();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("stations", stations);
		bundle.putParcelableArrayList("bikes", bikes);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		stations = getArguments().getParcelableArrayList("stations");
		bikes = getArguments().getParcelableArrayList("bikes");

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_osm_map, container,
				false);
		// get the mapView and the controller
		mapView = (MapView) rootView.findViewById(R.id.map_view);

		mapController = mapView.getController();

		// mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		// to keep the display on
		// mapView.setKeepScreenOn(true);

		// stuff for my Location
		GpsMyLocationProvider gpsMLC = new GpsMyLocationProvider(getActivity()
				.getApplicationContext());
		myLoc = new MyLocationNewOverlay(getActivity().getApplicationContext(),
				gpsMLC, mapView);
		InternalCompassOrientationProvider iCOP = new InternalCompassOrientationProvider(
				getActivity().getApplicationContext());
		CompassOverlay compassOverlay = new CompassOverlay(getActivity()
				.getApplicationContext(), iCOP, mapView);
		compassOverlay.enableCompass(iCOP);

		// add the markers on the mapView
		addMarkers();

		mapView.getOverlays().add(myLoc);

		mapView.getOverlays().add(compassOverlay);

		// mapView.setScrollableAreaLimit(getBoundingBox(true));

		setHasOptionsMenu(true);

		Button toMyLoc = (Button) rootView.findViewById(R.id.bt_to_my_loc);
		toMyLoc.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// myLoc.enableFollowLocation();
				if (myLoc.getMyLocation() != null)
				{
					mapController.setZoom(18);
					mapController.animateTo(myLoc.getMyLocation());
				}
			}
		});
		return rootView;
	}

	@Override
	public void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		mapView.post(new Runnable()
		{

			@Override
			public void run()
			{
				mapView.zoomToBoundingBox(getBoundingBox(false));
				// mapView.setMinZoomLevel(mapView.getZoomLevel());

			}
		});
	}

	@Override
	public void onPause()
	{
		super.onPause();
		myLoc.disableMyLocation();
		myLoc.disableFollowLocation();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
		case R.id.myswitch:
			item.setChecked(!item.isChecked());
			if (item.isChecked())
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
			break;
		default:
			break;
		}

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

		bikesMarkersOverlay = new MarkerOverlay<BikeOverlayItem>(getActivity(),
				markers, mapView, new BikeInfoWindow(mapView,
						getFragmentManager()));

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
				getActivity(), markers, mapView, new CustomInfoWindow(mapView,
						getFragmentManager()));

		mapView.getOverlays().add(stationsMarkersOverlay);
	}

	private BoundingBoxE6 getBoundingBox(boolean addFrame)
	{

		final int frame = addFrame ? 40000 : 0;
		BoundingBoxE6 toRtn;
		BoundingBoxE6 stationsBoundingBox = Station.getBoundingBox(stations);
		BoundingBoxE6 bikesBoundingBox = Bike.getBoundingBox(bikes);
		toRtn = new BoundingBoxE6(
				stationsBoundingBox.getLatNorthE6() > bikesBoundingBox.getLatNorthE6() ? stationsBoundingBox.getLatNorthE6()
						+ frame
						: bikesBoundingBox.getLatNorthE6() + frame,

				stationsBoundingBox.getLonEastE6() > bikesBoundingBox
						.getLonEastE6() ? stationsBoundingBox.getLonEastE6()
						+ frame : bikesBoundingBox.getLonEastE6() + frame,

				stationsBoundingBox.getLatSouthE6() < bikesBoundingBox
						.getLatSouthE6() ? stationsBoundingBox.getLatSouthE6()
						- frame : bikesBoundingBox.getLatSouthE6() - frame,

				stationsBoundingBox.getLonWestE6() < bikesBoundingBox
						.getLonWestE6() ? stationsBoundingBox.getLonWestE6()
						- frame : bikesBoundingBox.getLonWestE6() - frame);
		return toRtn;
	}
}