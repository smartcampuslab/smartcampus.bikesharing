package smartcampus.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.Station;
import smartcampus.util.StationsAdapter;
import smartcampus.util.Tools;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class StationsActivity extends Fragment{
	
	ArrayList<Station> mStations;
	ListView mList;
	StationsAdapter stationsAdapter;
	String[] navTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	OnStationSelectListener mCallback;
	
    // Container Activity must implement this interface
    public interface OnStationSelectListener {
        public void onStationSelected(Station station);
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

	public StationsActivity(){}
	
	
	public static StationsActivity newInstance(ArrayList<Station> stations){
		StationsActivity fragment = new StationsActivity();
	    Bundle bundle = new Bundle();
	    bundle.putParcelableArrayList("stations", stations);
	    fragment.setArguments(bundle);
	    return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mStations= getArguments().getParcelableArrayList("stations");
		((MainActivity)getActivity()).setOnPositionAquiredListener(new OnPositionAquiredListener() {
			
			@Override
			public void onPositionAquired() {
				stationsAdapter.notifyDataSetChanged();				
			}
		});
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.stations_main, container,false);
		
		stationsAdapter = new StationsAdapter(getActivity(), 0, mStations);
		
		mList = (ListView)rootView.findViewById(R.id.stations_list);
		mList.setDivider(new ColorDrawable(Color.TRANSPARENT));
		mList.setDividerHeight(Tools.convertDpToPixel(getActivity(), 5));
		mList.setAdapter(stationsAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mCallback.onStationSelected(mStations.get(position));
			}
		});
		stationsAdapter.notifyDataSetChanged();
		return rootView;
	}
	
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.stations, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.sort_distance:
			sortByDistance();
			break;
		case R.id.sort_available_bikes:
			sortByAvailableBikes();
			break;
		case R.id.sort_available_slots:
			sortByAvailableSlots();
			break;
		case R.id.sort_name:
			sortByName();
			break;
		}
		return true;
	}
	
	
	private void sortByDistance(){
		Collections.sort(mStations, new DistanceComparator());	
		stationsAdapter.notifyDataSetChanged();
	}
	private void sortByName(){
		Collections.sort(mStations, new NameComparator());
		stationsAdapter.notifyDataSetChanged();
	}
	private void sortByAvailableSlots(){
		Collections.sort(mStations, new AvailableSlotsComparator());
		stationsAdapter.notifyDataSetChanged();
	}
	private void sortByAvailableBikes(){
		Collections.sort(mStations, new AvailableBikesComparator());
		stationsAdapter.notifyDataSetChanged();
	}
	private class AvailableSlotsComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station1.getNSlotsEmpty()-station0.getNSlotsEmpty();
		}
		
	}
	private class AvailableBikesComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station1.getNSlotsUsed()-station0.getNSlotsUsed();
		}
		
	}
	private class DistanceComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getDistance()-station1.getDistance();
		}
		
	}
	private class NameComparator implements Comparator<Station> {

		@Override
		public int compare(Station station0, Station station1) {
			return station0.getName().compareToIgnoreCase(station1.getName());
		}
		
	}
	

}
