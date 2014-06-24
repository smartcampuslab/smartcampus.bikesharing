package smartcampus.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.Station;
import smartcampus.util.GetStationsTask;
import smartcampus.util.StationsAdapter;
import smartcampus.util.Tools;
import smartcampus.util.GetStationsTask.AsyncResponse;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class StationsActivity extends Fragment implements AsyncResponse
{

	private ArrayList<Station> mStations;
	private ListView mList;
	private StationsAdapter stationsAdapter;
	private int sortedBy;
	private static final int SORTED_BY_DISTANCE = 1;
	private static final int SORTED_BY_NAME = 2;
	private static final int SORTED_BY_AVAILABLE_BIKES = 3;
	private static final int SORTED_BY_AVAILABLE_SLOTS = 4;
	
	OnStationSelectListener mCallback;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	// Container Activity must implement this interface
	public interface OnStationSelectListener
	{
		public void onStationSelected(Station station);
	}

	public static StationsActivity newInstance(ArrayList<Station> stations)
	{
		StationsActivity fragment = new StationsActivity();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("stations", stations);
		fragment.setArguments(bundle);
		return fragment;
	}

	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try
		{
			mCallback = (OnStationSelectListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement OnStationSelectListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		mStations = getArguments().getParcelableArrayList("stations");
		((MainActivity) getActivity())
				.setOnPositionAquiredListener(new OnPositionAquiredListener()
				{

					@Override
					public void onPositionAquired()
					{
						stationsAdapter.notifyDataSetChanged();
					}
				});
		
		//If the distance is already defined the list is sorted by distance, otherwise
		//is sorted by available bikes
		if (mStations.get(0).getDistance()==Station.DISTANCE_NOT_VALID)
			sortByAvailableBikes(false);
		else
			sortByDistance(false);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.stations_main, container,
				false);
		// Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
 
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
		
		
		stationsAdapter = new StationsAdapter(getActivity(), 0, mStations, ((MainActivity)getActivity()).getCurrentLocation());

		mList = (ListView) rootView.findViewById(R.id.stations_list);
		mList.setDivider(new ColorDrawable(Color.TRANSPARENT));
		mList.setDividerHeight(Tools.convertDpToPixel(getActivity(), 5));
		mList.setAdapter(stationsAdapter);
		mList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{					
				mCallback.onStationSelected(mStations.get(position));
			}
		});
		stationsAdapter.notifyDataSetChanged();
		
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("STR", "onRefresh called from SwipeRefreshLayout");
                new GetStationsTask(getActivity()).execute("");
            }
        });
		
		setHasOptionsMenu(true);
		return rootView;
	}
	 
	 private void onRefreshComplete(ArrayList<Station> result) {
        Log.i("STR", "onRefreshComplete");
 
        stationsAdapter.notifyDataSetChanged();
        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }
	 
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.stations, menu);
		//Set the sorting order at the start of the list
		switch (sortedBy) {
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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		item.setChecked(!item.isChecked());
		switch (item.getItemId())
		{
		case R.id.sort_distance:
			sortByDistance(true);
			break;
		case R.id.sort_available_bikes:
			sortByAvailableBikes(true);
			break;
		case R.id.sort_available_slots:
			sortByAvailableSlots(true);
			break;
		case R.id.sort_name:
			sortByName(true);
			break;
		case R.id.refresh:
			if (!mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
                //TODO: sss
            }
            Toast.makeText(getActivity(), getString(R.string.refresh_hint), Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}
	
	/**
	 * Sorter and comparator
	 */

	private void sortByDistance(boolean updateList)
	{
		Collections.sort(mStations, new DistanceComparator());
		sortedBy = SORTED_BY_DISTANCE;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByName(boolean updateList)
	{
		Collections.sort(mStations, new NameComparator());
		sortedBy = SORTED_BY_NAME;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByAvailableSlots(boolean updateList)
	{
		Collections.sort(mStations, new AvailableSlotsComparator());
		sortedBy = SORTED_BY_AVAILABLE_SLOTS;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private void sortByAvailableBikes(boolean updateList)
	{
		Collections.sort(mStations, new AvailableBikesComparator());
		sortedBy = SORTED_BY_AVAILABLE_BIKES;
		if (updateList)
			stationsAdapter.notifyDataSetChanged();
	}

	private class AvailableSlotsComparator implements Comparator<Station>
	{

		@Override
		public int compare(Station station0, Station station1)
		{
			return station1.getNSlotsEmpty() - station0.getNSlotsEmpty();
		}

	}

	private class AvailableBikesComparator implements Comparator<Station>
	{

		@Override
		public int compare(Station station0, Station station1)
		{
			return station1.getNSlotsUsed() - station0.getNSlotsUsed();
		}

	}

	private class DistanceComparator implements Comparator<Station>
	{

		@Override
		public int compare(Station station0, Station station1)
		{
			return station0.getDistance() - station1.getDistance();
		}
		
	}

	private class NameComparator implements Comparator<Station>
	{

		@Override
		public int compare(Station station0, Station station1)
		{
			return station0.getName().compareToIgnoreCase(station1.getName());
		}
		
	}

	@Override
	public void processFinish(ArrayList<Station> stations) {
		this.mStations=stations;
		onRefreshComplete(stations);
	}
		

}
