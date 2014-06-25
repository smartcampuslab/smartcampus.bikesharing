package smartcampus.activity;

import java.util.ArrayList;

import eu.trentorise.smartcampus.bikerovereto.R;
import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.Station;
import smartcampus.util.GetStationsTask;
import smartcampus.util.StationsAdapter;
import smartcampus.util.Tools;
import smartcampus.util.GetStationsTask.AsyncStationResponse;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FavouriteFragment extends ListFragment{
	private ArrayList<Station> favStations;
	private StationsAdapter adapter;
	private TextView empty;
	
	private OnStationSelectListener mCallback;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	// Container Activity must implement this interface
	public interface OnStationSelectListener
	{
		public void onStationSelected(Station station);
	}

	
	public static FavouriteFragment newInstance(ArrayList<Station> favStations)
	{
		FavouriteFragment fragment = new FavouriteFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("stations", favStations);
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
					+ " must implement FavouriteFragment.OnStationSelectListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		adapter = new StationsAdapter(getActivity(), 0, favStations, ((MainActivity)getActivity()).getCurrentLocation());
		adapter.setIsFavouriteAdapter(true);
		setListAdapter(adapter);		
		View rootView = inflater.inflate(R.layout.fav_stations, null);
		empty = (TextView)rootView.findViewById(android.R.id.empty);
		// Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
 
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	refreshDatas();
            }
        });
		return rootView;
	}
	
	private void refreshDatas() {
		GetStationsTask getStationsTask = new GetStationsTask(getActivity());
        getStationsTask.delegate=new AsyncStationResponse() {
			
			@Override
			public void processFinish(ArrayList<Station> stations, ArrayList<Station> fav, int status) {
				((MainActivity)getActivity()).setStations(stations);
				((MainActivity)getActivity()).setFavStations(fav);
				favStations.clear();
				favStations.addAll(fav);
				adapter.notifyDataSetChanged();
				if (((MainActivity)getActivity()).getCurrentLocation() != null)
					((MainActivity)getActivity()).updateDistances();
				onRefreshComplete();	
				if (status != GetStationsTask.NO_ERROR)
				{
					Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
				}
				Log.d("Server call finished", "status code: " + status);
			}
		};
		getStationsTask.execute("");
	}
	
	private void onRefreshComplete() {
        Log.i("STR", "onRefreshComplete");
        adapter.notifyDataSetChanged();
        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		favStations = getArguments().getParcelableArrayList("stations");
		((MainActivity) getActivity())
				.setOnPositionAquiredListener(new OnPositionAquiredListener()
				{

					@Override
					public void onPositionAquired()
					{
						adapter.notifyDataSetChanged();
					}
				});
		/*
		//If the distance is already defined the list is sorted by distance, otherwise
		//is sorted by available bikes
		if (mStations.get(0).getDistance()==Station.DISTANCE_NOT_VALID)
			sortByAvailableBikes(false);
		else
			sortByDistance(false);*/
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		getListView().setDividerHeight(Tools.convertDpToPixel(getActivity(), 5));
		getListView().setEmptyView(empty);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onStationSelected(favStations.get(position));				
			}
		});
	}
	
}
