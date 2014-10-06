package smartcampus.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.asynctask.GetStationsTask;
import smartcampus.asynctask.GetStationsTask.AsyncStationResponse;
import smartcampus.model.Station;
import smartcampus.util.StationsAdapter;
import smartcampus.util.Tools;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikesharing.R;

public class StationsListFragment extends ListFragment {

	private ArrayList<Station> mStations;
	private ArrayList<Station> mFav;
	private View emptyView;
	private StationsAdapter stationsAdapter;
	private int sortedBy;
	private static final int SORTED_BY_FAVOURITES = 0;
	private static final int SORTED_BY_DISTANCE = 1;
	private static final int SORTED_BY_NAME = 2;
	private static final int SORTED_BY_AVAILABLE_BIKES = 3;
	private static final int SORTED_BY_AVAILABLE_SLOTS = 4;

	private ActionMode mActionMode;

	private OnStationSelectListener mCallback;

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.station_details, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.action_add_report) {
				if (Integer.parseInt(mode.getTag().toString()) > -1)
					sendReport();
				mode.finish();
				return true;
			}
			return false;
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			getListView()
					.getChildAt(Integer.parseInt(mode.getTag().toString()))
					.setSelected(false);
		}
	};

	// Container Activity must implement this interface
	public interface OnStationSelectListener {
		public void onStationSelected(Station station, boolean animation);
	}

	public static StationsListFragment newInstance(ArrayList<Station> stations,
			ArrayList<Station> fav) {
		StationsListFragment fragment = new StationsListFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("stations", stations);
		bundle.putParcelableArrayList("fav", fav);
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
		mStations = getArguments().getParcelableArrayList("stations");
		mFav = getArguments().getParcelableArrayList("fav");
		((MainActivity) getActivity())
				.setOnPositionAquiredListener(new OnPositionAquiredListener() {

					@Override
					public void onPositionAquired() {
						stationsAdapter.notifyDataSetChanged();
					}
				});
		// If the app still waiting the server response, initiliaze the
		// arraylist to prevent crash for nullpointer

		if (mStations == null) {
			mStations = new ArrayList<Station>();
			return;
		}

		// If the distance is already defined the list is sorted by distance,
		// otherwise
		// is sorted by available bikes
		if (mStations.size() >= 1) {
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
		stationsAdapter = new StationsAdapter(getActivity(), 0, mStations,
				((MainActivity) getActivity()).getCurrentLocation());
		setListAdapter(stationsAdapter);

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mActionMode != null)
			mActionMode.finish();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		getListView()
				.setDividerHeight(Tools.convertDpToPixel(getActivity(), 5));
		getListView().setEmptyView(emptyView);
		getListView().setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View v, int position, long id) {
						if (mActionMode != null) {
							return false;
						}

						// Start the CAB using the ActionMode.Callback defined
						// above
						mActionMode = ((ActionBarActivity) getActivity())
								.startSupportActionMode(mActionModeCallback);
						v.setSelected(true);
						mActionMode.setTag(position);
						stationsAdapter.setSelectionPos(position);
						return true;
					}
				});
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (mActionMode != null) {
			v.setSelected(true);
			mActionMode.setTag(position);
			stationsAdapter.setSelectionPos(position);
		}
	}

	private void refreshDatas() {
		getActivity().setProgressBarIndeterminateVisibility(true);
		GetStationsTask getStationsTask = new GetStationsTask(getActivity());
		mStations.clear();
		getStationsTask.delegate = new AsyncStationResponse() {

			@Override
			public void processFinish(ArrayList<Station> stations,
					ArrayList<Station> favStations, int status) {
				mStations.addAll(stations);
				mFav.addAll(favStations);
				((MainActivity) getActivity()).setStations(stations);
				((MainActivity) getActivity()).setFavStations(favStations);
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
		mStations.removeAll(mFav);
		Collections.sort(mStations, new FavouriteComparator());
		Collections.sort(mFav, new FavouriteComparator());
		mStations.addAll(0, mFav);
		sortedBy = SORTED_BY_FAVOURITES;
		if (updateList && stationsAdapter != null)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sendReport() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		// TODO mettere le cose giuste.
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{"asdasd@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "bike sharing report");
		i.putExtra(Intent.EXTRA_TEXT, "Asda\n asd\n");
		startActivity(Intent.createChooser(i,
				getString(R.string.action_add_report)));

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
			if (mFav.contains(station0) && mFav.contains(station1))
				return station0.getDistance() - station1.getDistance();
			if (mFav.contains(station0))
				return 1;
			if (mFav.contains(station1))
				return 1;
			return station0.getDistance() - station1.getDistance();
		}

	}

	private class NameComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getName().compareToIgnoreCase(station1.getName());
		}

	}

}
