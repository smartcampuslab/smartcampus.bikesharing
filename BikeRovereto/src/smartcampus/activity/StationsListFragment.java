package smartcampus.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.osmdroid.util.GeoPoint;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.activity.MainActivity.onBackListener;
import smartcampus.asynctask.GetStationsTask;
import smartcampus.asynctask.GetStationsTask.AsyncStationResponse;
import smartcampus.model.Station;
import smartcampus.util.StationsAdapter;
import smartcampus.util.StationsHelper;
import smartcampus.util.Tools;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikesharing.R;

public class StationsListFragment extends ListFragment implements
		onBackListener {

	private String mStationId;
	private View emptyView;
	private StationsAdapter stationsAdapter;
	private int sortedBy;
	private static final int SORTED_BY_FAVOURITES = 0;
	private static final int SORTED_BY_DISTANCE = 1;
	private static final int SORTED_BY_NAME = 2;
	private static final int SORTED_BY_AVAILABLE_BIKES = 3;
	private static final int SORTED_BY_AVAILABLE_SLOTS = 4;

	private OnStationSelectListener mCallback;
	private ArrayList<Station> mStations;

	// Container Activity must implement this interface
	public interface OnStationSelectListener {
		public void onStationSelected(Station station, boolean animation);
	}

	public static StationsListFragment newInstance(Station station) {
		StationsListFragment fragment = new StationsListFragment();
		Bundle bundle = new Bundle();
		bundle.putString("stationid", station.getId());
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnStationSelectListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnStationSelectListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mStations = new ArrayList<Station>(StationsHelper.sStations);
		if (getArguments() != null && getArguments().containsKey("stationid")) {
			mStationId = getArguments().getString("stationid");
		}
		((MainActivity) getActivity())
				.setOnPositionAquiredListener(new OnPositionAquiredListener() {

					@Override
					public void onPositionAquired() {
						stationsAdapter.notifyDataSetChanged();
					}
				});
		// If the app still waiting the server response, initiliaze the
		// arraylist to prevent crash for nullpointer

		if (StationsHelper.isNotInitialized()) {
			StationsHelper.initialize(getActivity(),null);
		}

		// If the distance is already defined the list is sorted by distance,
		// otherwise
		// is sorted by available bikes
		if (StationsHelper.sStations.size() > 1) {
			// if (mStations.get(0).getDistance() == Station.DISTANCE_NOT_VALID)
			// sortByAvailableBikes(false);
			// else
			// sortByDistance(false);
			sortByFavourites(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.stations_main, container,
				false);

		emptyView = rootView.findViewById(android.R.id.empty);
		stationsAdapter = new StationsAdapter(getActivity(), 0,
				mStations);
		setListAdapter(stationsAdapter);

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setTitle(R.string.stations);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		getListView()
				.setDividerHeight(Tools.convertDpToPixel(getActivity(), 5));
		getListView().setEmptyView(emptyView);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Station s = (Station) l.getItemAtPosition(position);
		GeoPoint p = ((MainActivity) getActivity()).getCurrentLocation();
		Intent i = new Intent(getActivity(), DetailsActivity.class);
		i.putExtra(DetailsActivity.EXTRA_STATION, s);
		if (p != null) {
			i.putExtra(DetailsActivity.EXTRA_POSITION,
					new double[] { p.getLatitude(), p.getLongitude() });
		}
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.alpha_in,
				R.anim.alpha_out);
	}

	private void refreshDatas() {
		getActivity().setProgressBarIndeterminateVisibility(true);
		GetStationsTask getStationsTask = new GetStationsTask(getActivity());

		StationsHelper.sStations.clear();
		StationsHelper.sFavouriteStations.clear();

		getStationsTask.delegate = new AsyncStationResponse() {

			@Override
			public void processFinish(int status) {
				if (((MainActivity) getActivity()).getCurrentLocation() != null)
					((MainActivity) getActivity()).updateDistances();
				onRefreshComplete();
				if (status != GetStationsTask.NO_ERROR) {
					Toast.makeText(getActivity(), getString(R.string.error),
							Toast.LENGTH_SHORT).show();
				}
				Log.d("Server call finished", "status code: " + status);
			}
		};
		getStationsTask.execute("");
	}

	private void onRefreshComplete() {
		mStations = new ArrayList<Station>(StationsHelper.sStations);
		Log.i("STR", "onRefreshComplete");
		getActivity().setProgressBarIndeterminateVisibility(false);
		// Reorder the arraylist in the previous order
		switch (sortedBy) {
		case SORTED_BY_DISTANCE:
			sortByDistance(true);
			break;
		case SORTED_BY_NAME:
			sortByName(true);
			break;
		case SORTED_BY_AVAILABLE_BIKES:
			sortByAvailableBikes(true);
			break;
		case SORTED_BY_AVAILABLE_SLOTS:
			sortByAvailableSlots(true);
			break;
		default:
			sortByFavourites(true);
			break;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.stations, menu);
		// Set the sorting order at the start of the list
		switch (sortedBy) {
		case SORTED_BY_FAVOURITES:
			menu.findItem(R.id.sort_fav).setChecked(true);
			break;
		case SORTED_BY_DISTANCE:
			menu.findItem(R.id.sort_distance).setChecked(true);
			break;
		case SORTED_BY_NAME:
			menu.findItem(R.id.sort_name).setChecked(true);
			break;
		case SORTED_BY_AVAILABLE_BIKES:
			menu.findItem(R.id.sort_available_bikes).setChecked(true);
			break;
		case SORTED_BY_AVAILABLE_SLOTS:
			menu.findItem(R.id.sort_available_slots).setChecked(true);
			break;
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		item.setChecked(!item.isChecked());
		if (item.getItemId() == R.id.sort_fav) {
			sortByFavourites(true);
		} else if (item.getItemId() == R.id.sort_distance) {
			sortByDistance(true);
		} else if (item.getItemId() == R.id.sort_available_bikes) {
			sortByAvailableBikes(true);
		} else if (item.getItemId() == R.id.sort_available_slots) {
			sortByAvailableSlots(true);
		} else if (item.getItemId() == R.id.sort_name) {
			sortByName(true);
		} else if (item.getItemId() == R.id.action_map) {
			((MainActivity) getActivity()).insertMap();
		} else if (item.getItemId() == R.id.refresh) {
			refreshDatas();
		}
		return true;
	}
	
	/**
	 * Sorter and comparator
	 */

	private void sortByDistance(boolean updateList) {
		Collections.sort(mStations, new DistanceComparator());
		sortedBy = SORTED_BY_DISTANCE;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByName(boolean updateList) {
		Collections.sort(mStations, new NameComparator());
		sortedBy = SORTED_BY_NAME;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByAvailableSlots(boolean updateList) {
		Collections.sort(mStations, new AvailableSlotsComparator());
		sortedBy = SORTED_BY_AVAILABLE_SLOTS;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByAvailableBikes(boolean updateList) {
		Collections.sort(mStations, new AvailableBikesComparator());
		sortedBy = SORTED_BY_AVAILABLE_BIKES;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByFavourites(boolean updateList) {
		mStations.removeAll(StationsHelper.sFavouriteStations);
		Collections.sort(mStations, new FavouriteComparator());
		Collections.sort(StationsHelper.sFavouriteStations,
				new FavouriteComparator());
		mStations.addAll(0, StationsHelper.sFavouriteStations);
		sortedBy = SORTED_BY_FAVOURITES;
		if (updateList && stationsAdapter != null)
			stationsAdapter.notifyDataSetChanged();
	}

	private class AvailableSlotsComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station1.getNSlotsEmpty() - station0.getNSlotsEmpty();
		}

	}

	private class AvailableBikesComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station1.getNBikesPresent() - station0.getNBikesPresent();
		}

	}

	private class DistanceComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getDistance() - station1.getDistance();
		}

	}

	private class FavouriteComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			if (StationsHelper.sFavouriteStations.contains(station0)
					&& StationsHelper.sFavouriteStations.contains(station1)) {
				if (station0.getDistance() > -1) {
					return station0.getDistance() - station1.getDistance();
				} else {
					return new NameComparator().compare(station0, station1);
				}
			}
			if (StationsHelper.sFavouriteStations.contains(station0)) {
				return 1;
			}
			if (StationsHelper.sFavouriteStations.contains(station1)) {
				return 1;
			}
			if (station0.getDistance() > -1) {
				return station0.getDistance() - station1.getDistance();
			} else {
				return new NameComparator().compare(station0, station1);
			}

		}

	}

	private class NameComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getName().compareToIgnoreCase(station1.getName());
		}

	}

	@Override
	public void onBackPressed() {
		getFragmentManager().popBackStack();
	}

}
