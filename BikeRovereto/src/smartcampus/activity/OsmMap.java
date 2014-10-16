package smartcampus.activity;

import java.io.InputStream;
import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
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
import smartcampus.activity.MainActivity.onBackListener;
import smartcampus.activity.cluster.GridMarkerClustererBikes;
import smartcampus.activity.cluster.GridMarkerClustererStation;
import smartcampus.model.Bike;
import smartcampus.model.Station;
import smartcampus.util.BikeInfoWindow;
import smartcampus.util.BikeMarker;
import smartcampus.util.StationInfoWindow;
import smartcampus.util.StationMarker;
import smartcampus.util.StationsHelper;
import smartcampus.util.Tools;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.ImageView;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikesharing.R;

public class OsmMap extends Fragment implements onBackListener {
	// the view where the map is showed
	private MapView mapView;
	// overlya for current Location
	private MyLocationNewOverlay mLocationOverlay;

	// the stations
	// the bikes
	private ArrayList<Bike> bikes;

	// marker for stations
	private ArrayList<StationMarker> stationsMarkersOverlay;
	// marker for the bikes
	private ArrayList<BikeMarker> bikesMarkersOverlay;

	// button for animating to my position
	private ImageView toMyLoc;

	private ImageView mZoomIn;
	private ImageView mZoomOut;

	// current BoundingBoxE6 shown
	private BoundingBoxE6 currentBoundingBox;
	private ArrayList<Station> stations;
	// default bounding box
	private static final BoundingBoxE6 defaultBoundingBox = new BoundingBoxE6(
			45.911087, 11.065997, 45.86311, 11.00263);

	public static OsmMap newInstance(ArrayList<Bike> bikes) {
		OsmMap fragment = new OsmMap();
		Bundle bundle = new Bundle();

		bundle.putParcelableArrayList("bikes", bikes);

		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		bikes = getArguments().getParcelableArrayList("bikes");

		// default bounding box
		currentBoundingBox = defaultBoundingBox;

		setCallBackListeners();
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_osm_map, container,
				false);
		// get the mapView
		mapView = (MapView) rootView.findViewById(R.id.map_view);

		mapView.setMultiTouchControls(true);

		// my LOCATION stuff
		mLocationOverlay = new MyLocationNewOverlay(getActivity(),
				new CustomLocationProvider(getActivity()), mapView);
		mapView.setTileSource(TileSourceFactory.getTileSource("MapquestOSM"));
		mapView.getOverlays().add(mLocationOverlay);

		toMyLoc = (ImageView) rootView.findViewById(R.id.bt_to_my_loc);
		setBtToMyLoc();

		mZoomIn = (ImageView) rootView.findViewById(R.id.btn_zoom_in);
		mZoomOut = (ImageView) rootView.findViewById(R.id.btn_zoom_out);
		setZoomBtns();

		stations = new ArrayList<Station>(StationsHelper.sStations);
		refresh();
		setMapListener();

		setHasOptionsMenu(true);
		return rootView;
	}

	private void setZoomBtns() {
		mZoomIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mapView.getController().zoomIn();
			}
		});
		mZoomOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mapView.getController().zoomOut();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mLocationOverlay.enableMyLocation();
		mapView.post(new Runnable() {
			@Override
			public void run() {
				// cycle because the zoomToBoundingBox must be called 3 times to
				// take effect (osm bug?)
				for (int i = 0; i < 3; i++) {
					mapView.zoomToBoundingBox(currentBoundingBox);
				}
			}
		});
		getActivity().getActionBar().setTitle(getString(R.string.app_name));
	}

	@Override
	public void onPause() {
		super.onPause();
		mLocationOverlay.disableMyLocation();
		currentBoundingBox = mapView.getBoundingBox();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.osm_map, menu);

		// THIS IS USELESS, unless other bikes providers are added
		// if (Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_EMOTION)) {
		// menu.getItem(1).setVisible(true);
		// menu.getItem(1).getSubMenu().getItem(0).setVisible(true);
		// }
		//
		// if (Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_ANARCHIC)) {
		// menu.getItem(1).setVisible(true);
		// menu.getItem(1).getSubMenu().getItem(1).setVisible(true);
		// }

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == R.id.bike_type_emotion) {
			// item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				mapView.getOverlays().addAll(stationsMarkersOverlay);
			} else {
				for (Overlay o : stationsMarkersOverlay) {
					((StationMarker) o).hideInfoWindow();
				}
				// remove the e-motion bikes
				mapView.getOverlays().remove(stationsMarkersOverlay);
			}
			mapView.invalidate();
		} else if (item.getItemId() == R.id.bike_type_anarchic) {
			item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				mapView.getOverlays().addAll(bikesMarkersOverlay);
			} else {
				for (Overlay o : bikesMarkersOverlay) {
					((BikeMarker) o).hideInfoWindow();
				}
				// remove the anarchic bikes
				mapView.getOverlays().remove(bikesMarkersOverlay);
			}
			mapView.invalidate();
		} else if (item.getItemId() == R.id.action_refresh
				|| item.getItemId() == R.id.map_refresh) {
			((MainActivity) getActivity()).refresh();
		}
		return true;
	}

	private void addBikesMarkers() {
		if (!this.isAdded() || bikes == null
				|| !Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_ANARCHIC)) {
			return;
		}

		Resources res = getResources();

		if (bikesMarkersOverlay == null) {
			bikesMarkersOverlay = new ArrayList<BikeMarker>();
		} else {
			bikesMarkersOverlay.clear();
		}
		loadBikesMarkers();
	}

	private void addStationsMarkers() {
		if (!this.isAdded() || stations == null
				|| !Tools.bikeTypesContains(Tools.METADATA_BIKE_TYPE_EMOTION)) {
			return;
		}

		Resources res = getResources();
		if (stationsMarkersOverlay == null) {
			stationsMarkersOverlay = new ArrayList<StationMarker>();
		} else {
			stationsMarkersOverlay.clear();
		}
		refreshStationsMarkers();
	}

	private class CustomLocationProvider extends GpsMyLocationProvider {
		private boolean firstTime = true;

		public CustomLocationProvider(Context context) {
			super(context);
		}

		@Override
		public void onLocationChanged(Location location) {
			super.onLocationChanged(location);
			((MainActivity) getActivity()).setCurrentLocation(new GeoPoint(
					location));
//			if (firstTime) {
//				if (stations == null || stations.size() == 0)
//					mapView.getController().animateTo(new GeoPoint(location));
//				firstTime = false;
//			}
		}
	}

	private void setBtToMyLoc() {
		toMyLoc.setBackgroundResource(R.drawable.to_my_loc_image);
		toMyLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// myLoc.enableFollowLocation();
				if (mLocationOverlay.getMyLocation() != null) {
					mapView.getController().animateTo(
							mLocationOverlay.getMyLocation());
				}
				else{
					Toast.makeText(getActivity(), R.string.positionnotavail, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setMapListener() {
		mapView.setOnTouchListener(new OnTouchListener() {
			float x, y;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == event.ACTION_DOWN) {
					x = event.getX();
					// InternalCompassOrientationProvider iCOP = new
					y = event.getY();
				} else if (event.getAction() == event.ACTION_UP) {
					if ((Math.abs(event.getX() - x) <= 10)
							&& (Math.abs(event.getY() - y) <= 10)) {
						boolean toRtn = false;
						for (InfoWindow i : InfoWindow
								.getOpenedInfoWindowsOn(mapView)) {
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

	private void loadBikesMarkers() {
		if (bikesMarkersOverlay == null) {
			bikesMarkersOverlay = new ArrayList<BikeMarker>();
		} else {
			bikesMarkersOverlay.clear();
		}

		Resources res = getResources();

		Drawable markerImage = res.getDrawable(R.drawable.marker_bike);

		BikeInfoWindow customInfoWindow = new BikeInfoWindow(mapView,
				getFragmentManager());
		for (Bike b : bikes) {
			BikeMarker marker = new BikeMarker(mapView, b);
			marker.setPosition(b.getPosition());
			marker.setIcon(markerImage);
			marker.setInfoWindow(customInfoWindow);
			bikesMarkersOverlay.add(marker);
		}
		mapView.getOverlays().addAll(bikesMarkersOverlay);
	}

	private void refreshStationsMarkers() {
		if (stationsMarkersOverlay == null) {
			stationsMarkersOverlay = new ArrayList<StationMarker>();
		} else {
			stationsMarkersOverlay.clear();
		}
		Resources res = getResources();

		Drawable markerImage = null;
		StationInfoWindow customInfoWindow = new StationInfoWindow(mapView,
				getFragmentManager());
		for (Station s : stations) {
			StationMarker marker = new StationMarker(mapView, s);
			marker.setTitle(s.getName());
			marker.setSnippet(s.getStreet());
			marker.setPosition(s.getPosition());

			if (s.getNBikesPresent() == 0 && s.getNSlotsEmpty() == 0) {
				markerImage = res.getDrawable(R.drawable.marker_grey);
			} else {
				markerImage = selectImage(res, s);
			}

			marker.setIcon(markerImage);
			marker.setInfoWindow(customInfoWindow);
			stationsMarkersOverlay.add(marker);
		}
		mapView.getOverlays().addAll(stationsMarkersOverlay);
	}

	private Drawable selectImage(Resources res, Station s) {
		Drawable markerImage;
		switch ((int) Math.round(s.getBikesPresentPercentage() * 10)) {
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
			markerImage = res.getDrawable(R.drawable.marker_grey);
			break;
		}
		return markerImage;
	}

	public void refresh() {
		if (mapView != null && mapView.getOverlays() != null){
			mapView.getOverlays().clear();
			mapView.invalidate();
		}
		addStationsMarkers();
		addBikesMarkers();
		mapView.invalidate();
	}

	private void setCallBackListeners() {
		((MainActivity) getActivity())
				.setOnStationsAquiredListener(new OnStationsAquired() {
					@Override
					public void stationsAquired(ArrayList<Station> sta) {
						stations = sta;
						addStationsMarkers();
						if (stations.size() > 0) {
							for (int i = 0; i < 3; i++) {
								mapView.zoomToBoundingBox(Station
										.getBoundingBox(stations));
							}
						}
					}
				});

		((MainActivity) getActivity())
				.setOnBikesAquiredListener(new OnBikesAquired() {
					@Override
					public void bikesAquired(ArrayList<Bike> b) {
						bikes = b;
						addBikesMarkers();
					}
				});

		((MainActivity) getActivity())
				.setOnStationRefresh(new OnStationRefresh() {
					@Override
					public void stationsRefreshed(ArrayList<Station> sta) {
						if (sta.size() > 0) {
							stations = sta;
							refreshStationsMarkers();
						}
					}
				});

		((MainActivity) getActivity()).setOnBikesRefresh(new OnBikesRefresh() {

			@Override
			public void bikesRefreshed(ArrayList<Bike> b) {
				if (b.size() > 0) {
					bikes = b;
					loadBikesMarkers();
				}
			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
		((MainActivity) getActivity()).setOnStationRefresh(null);
		((MainActivity) getActivity()).setOnBikesRefresh(null);

	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() > 1)
			getFragmentManager().popBackStack();
		else
			getActivity().finish();
	}

}
