package smartcampus.activity;

import java.util.ArrayList;

import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import smartcampus.activity.cluster.GridMarkerClustererCustom;
import smartcampus.activity.gesture.RotationGestureOverlay;
import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.BikeInfoWindow;
import smartcampus.util.BikeMarker;
import smartcampus.util.StationInfoWindow;
import smartcampus.util.StationMarker;
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
	// the view where the map is showed
	private MapView mapView;

	private MyLocationNewOverlay mLocationOverlay;
	// the stations to show in the map
	private ArrayList<Station> stations;

	private ArrayList<Bike> bikes;

	// marker for the stations

	// private MarkerOverlay<StationOverlayItem> stationsMarkersOverlay;
	private GridMarkerClustererCustom stationsMarkersOverlay;
	// marker for the bikes
	private GridMarkerClusterer bikesMarkersOverlay;

	//private RotationGestureOverlay rotationGestureOverlay;
	private Button toMyLoc;

	private BoundingBoxE6 currentBoundingBox;
	private float currentMapOrientation;

	public static OsmMap newInstance(ArrayList<Station> stations, ArrayList<Bike> bikes)
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_osm_map, container, false);
		// get the mapView and the controller
		mapView = (MapView) rootView.findViewById(R.id.map_view);

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

		mLocationOverlay = new MyLocationNewOverlay(getActivity(), new CustomLocationProvider(getActivity()), mapView);
		InternalCompassOrientationProvider iCOP = new InternalCompassOrientationProvider(getActivity().getApplicationContext());
		CompassOverlay compassOverlay = new CompassOverlay(getActivity().getApplicationContext(), iCOP, mapView);
		compassOverlay.enableCompass(iCOP);

		// add the markers on the mapView
		addMarkers();

		mapView.getOverlays().add(mLocationOverlay);

		mapView.getOverlays().add(compassOverlay);
		// mapView.setScrollableAreaLimit(getBoundingBox(true));

		setHasOptionsMenu(true);
		toMyLoc = (Button) rootView.findViewById(R.id.bt_to_my_loc);
		setBtToMyLoc();

		// rotation gesture
//		rotationGestureOverlay = new RotationGestureOverlay(getActivity().getApplicationContext(), mapView);
//		rotationGestureOverlay.setEnabled(false);
		
		//mapView.getOverlays().add(rotationGestureOverlay);

		setMapListener();
		return rootView;
	}

	// TODO: when the map orientation is not 0, bubbles are not clickable in the
	// right position!

	@Override
	public void onStart()
	{
		super.onStart();
		mapView.post(new Runnable()
		{

			@Override
			public void run()
			{
				// cycle because the zoomToBoundingBox must be called 3 times to
				// take effect (osm bug?)
				if (currentBoundingBox == null)
				{
					for (int i = 0; i < 3; i++)
					{
						mapView.zoomToBoundingBox(getBoundingBox(true));
					}
				}
				else
				{
					for (int i = 0; i < 3; i++)
					{
						mapView.zoomToBoundingBox(currentBoundingBox);
					}
				}
				mapView.setMapOrientation(currentMapOrientation);

			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mLocationOverlay.enableMyLocation();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mLocationOverlay.disableMyLocation();
		currentBoundingBox = mapView.getBoundingBox();
		currentMapOrientation = mapView.getMapOrientation();
		mapView.getMapOrientation();
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
		case R.id.switch_bikes_tipe:
			item.setChecked(!item.isChecked());
			if (item.isChecked())
			{
				// close the bubble relative to the bikesMarkers if opened
				// TODO bikesMarkersOverlay.hideBubble();
				for (Overlay o : bikesMarkersOverlay.getItems())
				{
					((BikeMarker) o).hideInfoWindow();
				}

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
//		case R.id.switch_rotation:
//			item.setChecked(!item.isChecked());
//			if (item.isChecked())
//			{
//				rotationGestureOverlay.setEnabled(true);
//			}
//			else
//			{
//				mapView.setMapOrientation(0);
//				rotationGestureOverlay.setEnabled(false);
//			}
//			break;
//		default:
//			break;
		}

		return true;
	}

	private void addMarkers()
	{
		addBikesMarkers();
		addStationsMarkers();

		stationsMarkersOverlay.setGridSize(100);
		bikesMarkersOverlay.setGridSize(100);
	}

	private void addBikesMarkers()
	{
		Resources res = getResources();
		bikesMarkersOverlay = new GridMarkerClusterer(getActivity());
		mapView.getOverlays().add(bikesMarkersOverlay);

		Drawable markerImage = res.getDrawable(R.drawable.anarchich_bike);

		BikeInfoWindow customInfoWindow = new BikeInfoWindow(mapView, getFragmentManager());
		for (Bike b : bikes)
		{
			BikeMarker marker = new BikeMarker(mapView, b);

			marker.setPosition(b.getPosition());

			marker.setIcon(markerImage);

			marker.setInfoWindow(customInfoWindow);

			bikesMarkersOverlay.add(marker);
		}
	}

	private void addStationsMarkers()
	{
		// markers at:
		// http://openclipart.org/detail/184847/map-marker-vector-by-rfvectors.com-184847
		Resources res = getResources();
		stationsMarkersOverlay = new GridMarkerClustererCustom(getActivity());
		mapView.getOverlays().add(stationsMarkersOverlay);

		Drawable markerImage = null;
		StationInfoWindow customInfoWindow = new StationInfoWindow(mapView, getFragmentManager());
		for (Station s : stations)
		{
			StationMarker marker = new StationMarker(mapView, s);
			marker.setTitle(s.getName());
			marker.setSnippet(s.getStreet());
			marker.setPosition(s.getPosition());

			switch ((int) Math.round(s.getBikesPresentPercentage() * 10))
			{
			case 0:
				markerImage = res.getDrawable(R.drawable.marker_0);
				break;
			case 1:
				markerImage = res.getDrawable(R.drawable.marker_10);
				break;
			case 2:
				markerImage = res.getDrawable(R.drawable.marker_20);
				break;
			case 3:
				markerImage = res.getDrawable(R.drawable.marker_30);
				break;
			case 4:
				markerImage = res.getDrawable(R.drawable.marker_40);
				break;
			case 5:
				markerImage = res.getDrawable(R.drawable.marker_50);
				break;
			case 6:
				markerImage = res.getDrawable(R.drawable.marker_60);
				break;
			case 7:
				markerImage = res.getDrawable(R.drawable.marker_70);
				break;
			case 8:
				markerImage = res.getDrawable(R.drawable.marker_80);
				break;
			case 9:
				markerImage = res.getDrawable(R.drawable.marker_90);
				break;
			case 10:
				markerImage = res.getDrawable(R.drawable.marker_100);
				break;
			default:
				break;
			}

			marker.setIcon(markerImage);
			marker.setInfoWindow(customInfoWindow);
			marker.setAnchor(0, 1);
			stationsMarkersOverlay.add(marker);
		}
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

		north = stationsBoundingBox.getLatNorthE6() > bikesBoundingBox.getLatNorthE6() ? stationsBoundingBox.getLatNorthE6() : bikesBoundingBox.getLatNorthE6();

		east = stationsBoundingBox.getLonEastE6() > bikesBoundingBox.getLonEastE6() ? stationsBoundingBox.getLonEastE6() : bikesBoundingBox.getLonEastE6();

		south = stationsBoundingBox.getLatSouthE6() < bikesBoundingBox.getLatSouthE6() ? stationsBoundingBox.getLatSouthE6() : bikesBoundingBox.getLatSouthE6();

		west = stationsBoundingBox.getLonWestE6() < bikesBoundingBox.getLonWestE6() ? stationsBoundingBox.getLonWestE6() : bikesBoundingBox.getLonWestE6();

		if (addCurrentPosition && mLocationOverlay.getMyLocation() != null)
		{
			GeoPoint mPosition = new GeoPoint(mLocationOverlay.getMyLocation().getLatitudeE6(), mLocationOverlay.getMyLocation().getLongitudeE6());
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
			((MainActivity) getActivity()).setCurrentLocation(new GeoPoint(location));
		}
	}

	private void setBtToMyLoc()
	{
		toMyLoc.setBackgroundResource(R.drawable.to_my_loc_image);
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
					mapView.setMapOrientation(0);
				}
			}
		});
	}

	private void setMapListener()
	{
		mapView.setOnTouchListener(new OnTouchListener()
		{
			float x, y;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == event.ACTION_DOWN)
				{
					x = event.getX();
					y = event.getY();
				}
				else if (event.getAction() == event.ACTION_UP)
				{
					if ((Math.abs(event.getX() - x) <= 10) && (Math.abs(event.getY() - y) <= 10))
					{
						boolean toRtn = false;
						for (InfoWindow i : InfoWindow.getOpenedInfoWindowsOn(mapView))
						{
							i.close();
							toRtn = true;
						}
						return toRtn;
					}
				}
				return false;
			}
		});
	}
}