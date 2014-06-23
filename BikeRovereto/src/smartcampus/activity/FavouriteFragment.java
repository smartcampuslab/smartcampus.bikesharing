package smartcampus.activity;

import java.util.ArrayList;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.Station;
import smartcampus.util.StationsAdapter;
import smartcampus.util.Tools;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FavouriteFragment extends ListFragment{
	private ArrayList<Station> favStations;
	private StationsAdapter adapter;
	private OnStationSelectListener mCallback;
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
		return super.onCreateView(inflater, container, savedInstanceState);
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
		int padding = Tools.convertDpToPixel(getActivity(), 10);
		getListView().setPadding(padding, padding, padding, padding);
		getListView().setFadingEdgeLength(0);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setClipToPadding(false);
		getListView().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		getListView().setDividerHeight(Tools.convertDpToPixel(getActivity(), 5));
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onStationSelected(favStations.get(position));				
			}
		});
	}
	
}