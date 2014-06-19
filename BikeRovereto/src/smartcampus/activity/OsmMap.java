package smartcampus.activity;

import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import smartcampus.activity.gesture.RotationGestureOverlay;
import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.BikeInfoWindow;
import smartcampus.util.BikeOverlayItem;
import smartcampus.util.CustomInfoWindow;
import smartcampus.util.MarkerOverlay;
import smartcampus.util.StationOverlayItem;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import eu.trentorise.smartcampus.bikerovereto.R;

public class OsmMap extends Fragment
{

	// menu
	private static final int MENU_ON_STATION_ID = 0;

	// the view where the map is showed
	private MapView mapView;

	// the tools to control the map
	private IMapController mapController;

	private MyLocationNewOverlay mLocationOverlay;
	// the stations to show in the map
	private ArrayList<Station> stations;

	private ArrayList<Bike> bikes;

	// marker for the stations
	private MarkerOverlay<StationOverlayItem> stationsMarkersOverlay;

	// marker for the bikes
	private MarkerOverlay<BikeOverlayItem> bikesMarkersOverlay;

	private Button toMyLoc;

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

		// stuff for my
		// Location********************************************************************************
		// GpsMyLocationProvider gpsMLC = new
		// GpsMyLocationProvider(getActivity()
		// .getApplicationContext());
		// mLocationOverlay = new
		// MyLocationNewOverlay(getActivity().getApplicationContext(),
		// gpsMLC, mapView);
		// InternalCompassOrientationProvider iCOP = new
		// InternalCompassOrientationProvider(
		// getActivity().getApplicationContext());
		// CompassOverlay compassOverlay = new CompassOverlay(getActivity()
		// .getApplicationContext(), iCOP, mapView);
		// compassOverlay.enableCompass(iCOP);
		// END stuff for my
		// Location********************************************************************************

		mLocationOverlay = new MyLocationNewOverlay(getActivity(),
				new CustomLocationProvider(getActivity()), mapView);
		InternalCompassOrientationProvider iCOP = new InternalCompassOrientationProvider(
				getActivity().getApplicationContext());
		CompassOverlay compassOverlay = new CompassOverlay(getActivity()
				.getApplicationContext(), iCOP, mapView);
		compassOverlay.enableCompass(iCOP);

		//rotation gesture
		mapView.getOverlays().add(new RotationGestureOverlay(getActivity(), mapView));
		
		// add the markers on the mapView
		addMarkers();

		mapView.getOverlays().add(mLocationOverlay);

		mapView.getOverlays().add(compassOverlay);
		// mapView.setScrollableAreaLimit(getBoundingBox(true));

		setHasOptionsMenu(true);
		toMyLoc = (Button) rootView.findViewById(R.id.bt_to_my_loc);
		setBtToMyLoc();

		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mLocationOverlay.enableMyLocation();
		mapView.post(new Runnable()
		{

			@Override
			public void run()
			{
				for (int i = 0; i < 3; i++)
				{
					mapView.zoomToBoundingBox(getBoundingBox(true));
				}

			}
		});
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mLocationOverlay.disableMyLocation();
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

	private BoundingBoxE6 getBoundingBox(boolean addCurrentPosition)
	{
		BoundingBoxE6 toRtn;
		BoundingBoxE6 stationsBoundingBox = Station.getBoundingBox(stations);
		BoundingBoxE6 bikesBoundingBox = Bike.getBoundingBox(bikes);

		int north = Integer.MIN_VALUE;
		int south = Integer.MAX_VALUE;
		int west = Integer.MAX_VALUE;
		int east = Integer.MIN_VALUE;

		north = stationsBoundingBox.getLatNorthE6() > bikesBoundingBox
				.getLatNorthE6() ? stationsBoundingBox.getLatNorthE6()
				: bikesBoundingBox.getLatNorthE6();

		east = stationsBoundingBox.getLonEastE6() > bikesBoundingBox
				.getLonEastE6() ? stationsBoundingBox.getLonEastE6()
				: bikesBoundingBox.getLonEastE6();

		south = stationsBoundingBox.getLatSouthE6() < bikesBoundingBox
				.getLatSouthE6() ? stationsBoundingBox.getLatSouthE6()
				: bikesBoundingBox.getLatSouthE6();

		west = stationsBoundingBox.getLonWestE6() < bikesBoundingBox
				.getLonWestE6() ? stationsBoundingBox.getLonWestE6()
				: bikesBoundingBox.getLonWestE6();

		if (addCurrentPosition && mLocationOverlay.getMyLocation() != null)
		{
			GeoPoint mPosition = new GeoPoint(mLocationOverlay.getMyLocation()
					.getLatitudeE6(), mLocationOverlay.getMyLocation()
					.getLongitudeE6());
			if (mPosition.getLatitudeE6() > north)
			{
				north = mPosition.getLatitudeE6();
			}
			if (mPosition.getLatitudeE6() > east)
			{
				east = mPosition.getLongitudeE6();
			}
			if (mPosition.getLatitudeE6() < west)
			{
				west = mPosition.getLongitudeE6();
			}
			if (mPosition.getLatitudeE6() < south)
			{
				south = mPosition.getLatitudeE6();
			}
		}

		toRtn = new BoundingBoxE6(north, east, south, west);
		return toRtn;
	}

	private class CustomLocationProvider extends GpsMyLocationProvider
	{

		public CustomLocationProvider(Context context)
		{
			super(context);
		}

		@Override
		public void onLocationChanged(Location location)
		{
			super.onLocationChanged(location);
			((MainActivity) getActivity()).setCurrentLocation(new GeoPoint(
					location));
		}
	}

	private void setBtToMyLoc()
	{
		toMyLoc.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// myLoc.enableFollowLocation();
				if (mLocationOverlay.getMyLocation() != null)
				{
					// mapController.animateTo(getBoundingBox(true).getCenter());
					mapView.zoomToBoundingBox(getBoundingBox(true));
				}
			}
		});
		toMyLoc.setOnTouchListener(new OnTouchListener()
		{
			
//			float startX, startY;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					toMyLoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.to_my_loc_clicked));
//					startX = event.getX();
//					startY = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					toMyLoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.to_my_loc));
				}
//				if(event.getAction() == MotionEvent.ACTION_MOVE)
//				{
//					Log.d("debugXDIFF", Float.toString(startX - toMyLoc.getX()));
//					Log.d("debugYDIFF", Float.toString(startY - toMyLoc.getY()));
//					if((Math.abs(startX - toMyLoc.getX()) > 200) || Math.abs(startY - toMyLoc.getY()) > 200)
//					{
//						toMyLoc.setBackgroundDrawable(getResources().getDrawable(R.drawable.to_my_loc));
//					}
//				}
				return false;
			}
		});
	}
}