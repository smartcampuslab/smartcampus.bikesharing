package smartcampus.activity;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.Station;
import smartcampus.util.ReportsAdapter;
import smartcampus.util.Tools;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class StationDetails extends Fragment
{

	// the station with its details
	private Station station;

	private ListView mList;
	private LocationManager mLocationManager;
	private TextView name;
	private TextView street;
	private TextView availableBike, availableSlots;
	private TextView distance;

	public static StationDetails newInstance(Station station)
	{
		StationDetails fragment = new StationDetails();
		Bundle bundle = new Bundle();
		bundle.putParcelable("station", station);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_station_details,
				container, false);
		View header = inflater.inflate(R.layout.station_details_header, null);

		name = (TextView) header.findViewById(R.id.name);
		street = (TextView) header.findViewById(R.id.street);
		availableBike = (TextView) header.findViewById(R.id.available_bikes);
		availableSlots = (TextView) header.findViewById(R.id.available_slots);
		distance = (TextView) header.findViewById(R.id.distance);

		// get the station from the parcels
		station = getArguments().getParcelable("station");
		
		mList = (ListView) rootView.findViewById(R.id.details);
		mList.addHeaderView(header, null, false);

		name.setText(station.getName());
		street.setText(station.getStreet());
		availableBike.setText(station.getNSlotsUsed() + "");
		availableSlots.setText(station.getNSlotsEmpty() + "");
		distance.setText(Tools.formatDistance(station.getDistance()));

		mList.setAdapter(new ReportsAdapter(getActivity(), 0, station
				.getReports()));

		distance.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				GeoPoint startPoint = ((MainActivity)getActivity()).getCurrentLocation();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse(Tools.getPathString(startPoint, station.getPosition())));
				startActivity(i);
			}
		});
		mLocationManager = (LocationManager) getActivity().getSystemService(
				getActivity().LOCATION_SERVICE);
		setHasOptionsMenu(true);
		
		((MainActivity) getActivity()).setOnPositionAquiredListener(new OnPositionAquiredListener()
		{

			@Override
			public void onPositionAquired()
			{
				distance.setText(Tools.formatDistance(station.getDistance()));
			}
		});
		((MainActivity)getActivity()).mDrawerToggle.setDrawerIndicatorEnabled(false);
		((MainActivity)getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.station_details, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id)
		{
		case android.R.id.home:
			getFragmentManager().popBackStack();
			break;
		case R.id.action_add_report:
			addReport();
			break;
		case R.id.action_settings:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDetach() {
		((MainActivity)getActivity()).mDrawerToggle.setDrawerIndicatorEnabled(true);
		((MainActivity)getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		super.onDetach();
	}
	
	private void addReport()
	{
		View dialogContent = getActivity().getLayoutInflater().inflate(
				R.layout.report_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getString(R.string.report_in) + " "
				+ station.getName());
		builder.setPositiveButton(R.string.report,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						// TODO: implement report
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
					}
				});
		builder.setView(dialogContent);
		AlertDialog dialog = builder.create();

		dialog.show();

	}

}
