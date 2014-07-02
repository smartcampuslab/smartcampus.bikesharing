package smartcampus.activity;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.asynctask.GetReportsTask;
import smartcampus.asynctask.GetReportsTask.AsyncReportsResponse;
import smartcampus.model.Bike;
import smartcampus.model.Report;
import smartcampus.util.ReportTools;
import smartcampus.util.ReportsAdapter;
import smartcampus.util.Tools;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikerovereto.R;

public class SignalView extends Fragment implements AsyncReportsResponse
{
	private Bike bike;
	private TextView txtID;
	private TextView distance;
	private ListView mList;

	private ImageView photoView;
	// private LocationManager mLocationManager;
	private ArrayList<Report> mReports;
	private ReportsAdapter adapter;

	public static SignalView newInstance(Bike bike)
	{
		SignalView fragment = new SignalView();
		Bundle bundle = new Bundle();
		bundle.putParcelable("bike", bike);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_signal, container, false);
		View header = inflater.inflate(R.layout.activity_signal_header, null);

		txtID = (TextView) header.findViewById(R.id.txtId);
		distance = (TextView) header.findViewById(R.id.distance);

		bike = getArguments().getParcelable("bike");

		txtID.setText("ID: " + bike.getId());

		mList = (ListView) rootView.findViewById(R.id.signal);
		mList.addHeaderView(header, null, false);

		distance.setText(Tools.formatDistance(bike.getDistance()));


		mReports = new ArrayList<Report>();
		new GetReportsTask().execute(GetReportsTask.BIKE, bike.getId());
		adapter = new ReportsAdapter(getActivity(), 0, mReports);
		mList.setAdapter(adapter);

		distance.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				GeoPoint startPoint = ((MainActivity) getActivity()).getCurrentLocation();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Tools.getPathString(startPoint, bike.getPosition())));
				startActivity(i);
			}
		});
		// mLocationManager = (LocationManager) getActivity().getSystemService(
		// getActivity().LOCATION_SERVICE);

		setHasOptionsMenu(true);

		((MainActivity) getActivity()).setOnPositionAquiredListener(new OnPositionAquiredListener()
		{

			@Override
			public void onPositionAquired()
			{
				distance.setText(Tools.formatDistance(bike.getDistance()));
			}
		});
		((MainActivity) getActivity()).mDrawerToggle.setDrawerIndicatorEnabled(false);
		((MainActivity) getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
			ReportTools.addReport(bike, getActivity(), this, adapter);
			break;
		case R.id.action_settings:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDetach()
	{
		((MainActivity) getActivity()).mDrawerToggle.setDrawerIndicatorEnabled(true);
		((MainActivity) getActivity()).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		super.onDetach();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("station details", "onActivityResult");
		if (requestCode == ReportTools.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
		{
			Bitmap imageBitmap = ReportTools.grabImage(getActivity());
			ReportTools.image = imageBitmap;
			
			photoView = ReportTools.photoView;
			
			photoView.setVisibility(View.VISIBLE);
			photoView.setScaleType(ScaleType.CENTER_CROP);
			photoView.setImageBitmap(imageBitmap);
		}
	}

	@Override
	public void processFinish(ArrayList<Report> reports, int status) {
		mReports = reports;
		adapter.notifyDataSetChanged();
		if (status != GetReportsTask.NO_ERROR)
		{
			Toast.makeText(getActivity(), getString(R.string.error_reports), Toast.LENGTH_SHORT).show();
		}
	}
}
