package smartcampus.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.osmdroid.util.GeoPoint;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.NotificationBlock;
import smartcampus.model.Report;
import smartcampus.model.Station;
import smartcampus.util.ReportsAdapter;
import smartcampus.util.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import eu.trentorise.smartcampus.bikerovereto.R;

public class StationDetails extends Fragment
{

	// the station with its details
	private Station station;

	private ListView mList;
	private TextView name;
	private TextView street;
	private TextView availableBike, availableSlots;
	private TextView distance;
	private View addReminder;

	private ImageView editReminder;

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	
	private Report report;
	private Bitmap imageBitmap;
	public static StationDetails newInstance(Station station)
	{
		StationDetails fragment = new StationDetails();
		Bundle bundle = new Bundle();
		bundle.putParcelable("station", station);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_station_details, container, false);
		View header = inflater.inflate(R.layout.station_details_header, null);

		name = (TextView) header.findViewById(R.id.name);
		street = (TextView) header.findViewById(R.id.street);
		availableBike = (TextView) header.findViewById(R.id.available_bikes);
		availableSlots = (TextView) header.findViewById(R.id.available_slots);
		distance = (TextView) header.findViewById(R.id.distance);
		addReminder = header.findViewById(R.id.add_reminder);
		editReminder = (ImageView) header.findViewById(R.id.edit);

		// get the station from the parcels
		station = getArguments().getParcelable("station");

		mList = (ListView) rootView.findViewById(R.id.details);
		mList.addHeaderView(header, null, false);

		name.setText(station.getName());
		street.setText(station.getStreet());
		availableBike.setText(station.getNBikesPresent() + "");
		availableSlots.setText(station.getNSlotsEmpty() + "");
		distance.setText(Tools.formatDistance(station.getDistance()));
		
		ArrayList<String> sReports = new ArrayList<String>();
		
		for(Report r : station.getReports())
		{
			sReports.add(r.getDetails());
			Log.d("prova", r.toString());
		}
		mList.setAdapter(new ReportsAdapter(getActivity(), 0, sReports));

		distance.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				GeoPoint startPoint = ((MainActivity) getActivity()).getCurrentLocation();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Tools.getPathString(startPoint, station.getPosition())));
				startActivity(i);
			}
		});
		addReminder.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				addReminder();
			}
		});

		editReminder.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ReminderEdit reminderEdit = ReminderEdit.newInstance(station);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.setCustomAnimations(R.anim.slide_left, R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_right);
				transaction.replace(R.id.content_frame, reminderEdit);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});

		setHasOptionsMenu(true);

		((MainActivity) getActivity()).setOnPositionAquiredListener(new OnPositionAquiredListener()
		{

			@Override
			public void onPositionAquired()
			{
				distance.setText(Tools.formatDistance(station.getDistance()));
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
			addReport();
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

	private void dispatchTakePictureIntent()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
		{
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	private void addReport()
	{
		// TODO: add imageview to display the image captured
		report = new Report();
		View dialogContent = getActivity().getLayoutInflater().inflate(R.layout.report_dialog, null);
		final CheckBox choose1;
		final CheckBox choose2;
		final CheckBox choose3;
		final EditText descriptionEditText;
		final Button addPhoto;
		choose1 = (CheckBox) dialogContent.findViewById(R.id.choose1);
		choose2 = (CheckBox) dialogContent.findViewById(R.id.choose2);
		choose3 = (CheckBox) dialogContent.findViewById(R.id.choose3);
		descriptionEditText = (EditText) dialogContent.findViewById(R.id.description);
		addPhoto = (Button) dialogContent.findViewById(R.id.add_photo);
		addPhoto.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				dispatchTakePictureIntent();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.report_in) + " " + station.getName());
		builder.setPositiveButton(R.string.report, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogI, int id)
			{
				if (choose1.isChecked())
					report.setType(Report.Type.ADVICE);
				if (choose2.isChecked())
					report = new Report(Report.Type.COMPLAINT, descriptionEditText.getText().toString());
				if (choose3.isChecked())
					report.setType(Report.Type.WARNING);
				report.setDetails(descriptionEditText.getText().toString());
				report.setPhoto(imageBitmap);
				
				//TODO send report to web service
				station.addReport(report);
				Log.d("provaFotoz", station.getReport(0).toString());
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
			}
		});
		builder.setView(dialogContent);
		builder.show();
	}

	private void addReminder()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final TimePicker picker = new TimePicker(getActivity());
		final Calendar c = Calendar.getInstance(Locale.ITALIAN);
		Log.d("dai", c.getTime().getHours()+" "+c.getTime().getMinutes());
		builder.setTitle(getString(R.string.add_reminder));
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogI, int id)
			{
				((MainActivity) getActivity()).addReminderForStation(new NotificationBlock(new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), picker
						.getCurrentHour(), picker.getCurrentMinute(),0), station.getId(), getActivity())

				);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
			}
		});
		picker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
		builder.setView(picker);

		builder.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("station details", "onActivityResult");
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
		{
			Bundle extras = data.getExtras();
			imageBitmap = (Bitmap) extras.get("data");
			report.setPhoto(imageBitmap);
			
			// mImageView.setImageBitmap(imageBitmap);
		}
	}

}
