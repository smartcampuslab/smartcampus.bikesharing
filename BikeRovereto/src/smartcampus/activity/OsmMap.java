package smartcampus.activity;

import java.util.ArrayList;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import smartcampus.activity.MainActivity.OnBikesAquired;
import smartcampus.activity.MainActivity.OnBikesRefresh;
import smartcampus.activity.MainActivity.OnStationRefresh;
import smartcampus.activity.MainActivity.OnStationsAquired;
import smartcampus.activity.cluster.GridMarkerClustererBikes;
import smartcampus.activity.cluster.GridMarkerClustererStation;
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
	// overlya for current Location
	private MyLocationNewOverlay mLocationOverlay;

	// the stations
	private ArrayList<Station> stations;
	// the bikes
	private ArrayList<Bike> bikes;

	// marker for stations
	private GridMarkerClustererStation stationsMarkersOverlay;
	// marker for the bikes
	private GridMarkerClustererBikes bikesMarkersOverlay;

	// button for animating to my position
	private Button toMyLoc;

	// current BoundingBoxE6 shown
	private BoundingBoxE6 currentBoundingBox;
	// default bounding box
	private static final BoundingBoxE6 defaultBoundingBox = new BoundingBoxE6(45.911087, 11.065997, 45.86311, 11.00263);

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

		// default bounding box
		currentBoundingBox = defaultBoundingBox;

		setCallBackListeners();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_osm_map, container, false);
		// get the mapView
		mapView = (MapView) rootView.findViewById(R.id.map_view);

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

		// my LOCATION stuff
		mLocationOverlay = new MyLocationNewOverlay(getActivity(), new CustomLocationProvider(getActivity()), mapView);
		InternalCompassOrientationProvider iCOP = new InternalCompassOrientationProvider(getActivity().getApplicationContext());
		CompassOverlay compassOverlay = new CompassOverlay(getActivity().getApplicationContext(), iCOP, mapView);
		compassOverlay.enableCompass(iCOP);

		mapView.getOverlays().add(mLocationOverlay);

		mapView.getOverlays().add(compassOverlay);
		// mapView.setScrollableAreaLimit(getBoundingBox(true));

		toMyLoc = (Button) rootView.findViewById(R.id.bt_to_my_loc);
		setBtToMyLoc();

		setMarkers();
		setMapListener();

		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		((MainActivity) getActivity()).startTimer();
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
				// cycle because the zoomToBoundingBox must be called 3 times to
				// take effect (osm bug?)
				for (int i = 0; i < 3; i++)
				{
					mapView.zoomToBoundingBox(currentBoundingBox);
				}
			}
		});
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mLocationOverlay.disableMyLocation();
		currentBoundingBox = mapView.getBoundingBox();
		((MainActivity) getActivity()).stopTimer();
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
				for (Overlay o : bikesMarkersOverlay.getItems())
				{
					((BikeMarker) o).hideInfoWindow();
				}

				// remove the anarchic bikes
				mapView.getOverlays().remove(bikesMarkersOverlay);
			}
			else
			{
				mapView.getOverlays().add(bikesMarkersOverlay);
			}
			mapView.invalidate();
			break;
		}

		return true;
	}

	private void addBikesMarkers()
	{
		if (!this.isAdded())
			return;
		Resources res = getResources();

		bikesMarkersOverlay = new GridMarkerClustererBikes(getActivity());
		mapView.getOverlays().add(bikesMarkersOverlay);

		Drawable markerImage = res.getDrawable(R.drawable.marker_bike);

		BikeInfoWindow customInfoWindow = new BikeInfoWindow(mapView, getFragmentManager());
		for (Bike b : bikes)
		{
			BikeMarker marker = new BikeMarker(mapView, b);

			marker.setPosition(b.getPosition());

			marker.setIcon(markerImage);

			marker.setInfoWindow(customInfoWindow);

			bikesMarkersOverlay.add(marker);
		}
		bikesMarkersOverlay.setGridSize(100);
	}

	private void addStationsMarkers()
	{
		if (!this.isAdded())
			return;

		Resources res = getResources();
		stationsMarkersOverlay = new GridMarkerClustererStation(getActivity());
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
		stationsMarkersOverlay.setGridSize(100);
	}

	//

	private class CustomLocationProvider extends GpsMyLocationProvider
	{
		private boolean firstTime = true;

		public CustomLocationProvider(Context context)
		{
			super(context);
		}

		@Override
		public void onLocationChanged(Location location)
		{
			super.onLocationChanged(location);
			((MainActivity) getActivity()).setCurrentLocation(new GeoPoint(location));
			if (firstTime)
			{
				if (stations == null || stations.size() == 0)
					mapView.getController().animateTo(new GeoPoint(location));
				firstTime = false;
			}
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
					mapView.getController().animateTo(mLocationOverlay.getMyLocation());
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
					x = event.getX();// InternalCompassOrientationProvider iCOP
										// = new

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

	private void refreshBikesMarkers()
	{
		if(bikesMarkersOverlay == null)
		{
			bikesMarkersOverlay = new GridMarkerClustererBikes(getActivity());
		}
		if(bikesMarkersOverlay.getItems() != null)
		bikesMarkersOverlay.getItems().clear();

		Resources res = getResources();

		Drawable markerImage = res.getDrawable(R.drawable.marker_bike);

		BikeInfoWindow customInfoWindow = new BikeInfoWindow(mapView, getFragmentManager());
		for (Bike b : bikes)
		{
			BikeMarker marker = new BikeMarker(mapView, b);

			marker.setPosition(b.getPosition());

			marker.setIcon(markerImage);

			marker.setInfoWindow(customInfoWindow);

			bikesMarkersOverlay.add(marker);
		}
		bikesMarkersOverlay.invalidate();
	}

	private void refreshStationsMarkers()
	{
		if(stationsMarkersOverlay == null)
		{
			stationsMarkersOverlay = new GridMarkerClustererStation(getActivity());
		}
		if (stationsMarkersOverlay.getItems() != null)
			stationsMarkersOverlay.getItems().clear();
		Resources res = getResources();

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
		stationsMarkersOverlay.invalidate();
	}

	private void setMarkers()
	{
		if (stations != null)
			addStationsMarkers();
		if (bikes != null)
			addBikesMarkers();

	}

	private void setCallBackListeners()
	{
		((MainActivity) getActivity()).setOnStationsAquiredListener(new OnStationsAquired()
		{

			@Override
			public void stationsAquired(ArrayList<Station> sta)
			{
				stations = sta;
				addStationsMarkers();
				if (stations.size() > 0)
				{
					for (int i = 0; i < 3; i++)
					{
						mapView.zoomToBoundingBox(Station.getBoundingBox(stations));
					}
				}
			}
		});

		((MainActivity) getActivity()).setOnBikesAquiredListener(new OnBikesAquired()
		{
			@Override
			public void bikesAquired(ArrayList<Bike> b)
			{
				bikes = b;
				addBikesMarkers();
			}
		});

		((MainActivity) getActivity()).setOnStationRefresh(new OnStationRefresh()
		{

			@Override
			public void stationsRefreshed(ArrayList<Station> sta)
			{
				if (sta.size() > 0)
				{
					stations = sta;
					refreshStationsMarkers();
				}
			}
		});

		((MainActivity) getActivity()).setOnBikesRefresh(new OnBikesRefresh()
		{

			@Override
			public void bikesRefreshed(ArrayList<Bike> b)
			{
				if (b.size() > 0)
				{
					bikes = b;
					refreshBikesMarkers();
				}
			}
		});
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		((MainActivity) getActivity()).setOnStationRefresh(null);
		((MainActivity) getActivity()).setOnBikesRefresh(null);

	}

	// private BoundingBoxE6 getBoundingBox(boolean addCurrentPosition)
	// {
	// BoundingBoxE6 toRtn;
	// BoundingBoxE6 stationsBoundingBox = null;
	// int north, south, west, east;
	// if (stations != null && stations.size() > 0)
	// {
	// stationsBoundingBox = Station.getBoundingBox(stations);
	// north = stationsBoundingBox.getLatNorthE6();
	// south = stationsBoundingBox.getLatSouthE6();
	// west = stationsBoundingBox.getLonWestE6();
	// east = stationsBoundingBox.getLonEastE6();
	// }
	// else
	// {
	// north = (int) (45.911087 * 1E6);
	// south = (int) (45.86311 * 1E6);
	// east = (int) (11.065997 * 1E6);
	// west = (int) (11.002632 * 1E6);
	// }
	//
	// // if (stationsBoundingBox != null && bikesBoundingBox != null)
	// // {
	// // north = stationsBoundingBox.getLatNorthE6() >
	// // bikesBoundingBox.getLatNorthE6() ?
	// // stationsBoundingBox.getLatNorthE6() :
	// // bikesBoundingBox.getLatNorthE6();
	// //
	// // east = stationsBoundingBox.getLonEastE6() >
	// // bikesBoundingBox.getLonEastE6() ? stationsBoundingBox.getLonEastE6()
	// // : bikesBoundingBox.getLonEastE6();
	// //
	// // south = stationsBoundingBox.getLatSouthE6() <
	// // bikesBoundingBox.getLatSouthE6() ?
	// // stationsBoundingBox.getLatSouthE6() :
	// // bikesBoundingBox.getLatSouthE6();
	// //
	// // west = stationsBoundingBox.getLonWestE6() <
	// // bikesBoundingBox.getLonWestE6() ? stationsBoundingBox.getLonWestE6()
	// // : bikesBoundingBox.getLonWestE6();
	// // }
	// //
	// // if (addCurrentPosition && mLocationOverlay.getMyLocation() != null)
	// // {
	// // GeoPoint mPosition = new
	// // GeoPoint(mLocationOverlay.getMyLocation().getLatitudeE6(),
	// // mLocationOverlay.getMyLocation().getLongitudeE6());
	// // if (mPosition.getLatitudeE6() > north)
	// // {
	// // north = mPosition.getLatitudeE6();
	// // }
	// // if (mPosition.getLatitudeE6() > east)
	// // {
	// // east = mPosition.getLongitudeE6();
	// // }
	// // if (mPosition.getLatitudeE6() < west)
	// // {
	// // west = mPosition.getLongitudeE6();
	// // }
	// // if (mPosition.getLatitudeE6() < south)
	// // {
	// // south = mPosition.getLatitudeE6();
	// // }
	// // }
	//
	// toRtn = new BoundingBoxE6(north, east, south, west);
	// return toRtn;
	// }
}
