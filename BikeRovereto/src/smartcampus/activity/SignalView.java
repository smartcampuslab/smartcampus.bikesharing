package smartcampus.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.osmdroid.util.GeoPoint;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import smartcampus.model.Bike;
import smartcampus.model.Report;
import smartcampus.util.ReportsAdapter;
import smartcampus.util.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class SignalView extends Fragment
{
	private Bike bike;
	private TextView txtID;
	private TextView distance;
	private ListView mList;

	private Report report;
	private Bitmap imageBitmap;
	// private LocationManager mLocationManager;
	private static final int REQUEST_IMAGE_CAPTURE = 1;

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
		
		ArrayList<String> sReports = new ArrayList<String>();
		Log.d("sas", (bike.getReports() == null)+"");
		for (Report r : bike.getReports())
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

	private void addReport()
	{
		final long date = Calendar.getInstance().getTimeInMillis();
		report = new Report(Report.BIKE, bike.getId(), date);
		View dialogContent = getActivity().getLayoutInflater().inflate(R.layout.report_dialog, null);
		final RadioGroup radioGroup;
		final RadioButton chooseAdvice;
		final RadioButton chooseComplaint;
		final RadioButton chooseWarning;
		final EditText descriptionEditText;
		final Button addPhoto;
		final LinearLayout[] signalLayouts = new LinearLayout[2];
		final CheckBox[] checkBoxes = new CheckBox[4];
		radioGroup = (RadioGroup) dialogContent.findViewById(R.id.chooses_group);
		chooseAdvice = (RadioButton) dialogContent.findViewById(R.id.choose1);
		chooseComplaint = (RadioButton) dialogContent.findViewById(R.id.choose2);
		chooseWarning = (RadioButton) dialogContent.findViewById(R.id.choose3);

		checkBoxes[0] = (CheckBox) dialogContent.findViewById(R.id.checkbox_chain);
		checkBoxes[1] = (CheckBox) dialogContent.findViewById(R.id.checkbox_brakes);
		checkBoxes[2] = (CheckBox) dialogContent.findViewById(R.id.checkbox_gears);
		checkBoxes[3] = (CheckBox) dialogContent.findViewById(R.id.checkbox_tire);

		signalLayouts[0] = (LinearLayout) dialogContent.findViewById(R.id.lr1);
		signalLayouts[1] = (LinearLayout) dialogContent.findViewById(R.id.lr2);

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

		builder.setTitle(getString(R.string.report_in) + " " + bike.getId());
		builder.setPositiveButton(R.string.report, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogI, int id)
			{
				if (chooseAdvice.isChecked())
				{
					report.setType(Report.Type.ADVICE);
					report.setDetails(descriptionEditText.getText().toString());
				}
				else if (chooseComplaint.isChecked())
				{
					report = new Report(Report.Type.COMPLAINT, descriptionEditText.getText().toString(), Report.BIKE, bike.getId(), date);
					report.setDetails(descriptionEditText.getText().toString());
				}
				else if (chooseWarning.isChecked())
				{
					report.setType(Report.Type.WARNING);
					String details = "";
					for (int i = 0; i < 4; i++)
					{
						if (checkBoxes[i].isChecked())
						{
							details += checkBoxes[i].getText().toString() + " ";
						}
						report.setDetails(details);
					}
					details += descriptionEditText.getText().toString();
				}

				report.setPhoto(imageBitmap);

				// TODO send report to web service
				bike.addReport(report);
				Log.d("provaFotoz", bike.getReport(0).toString());
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
			}
		});

		builder.setView(dialogContent);
		final AlertDialog dialog = builder.create();
		dialog.show();
		// Initially disable the button
		((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (chooseWarning.isChecked())
				{
					for (int i = 0; i < 2; i++)
					{
						signalLayouts[i].setVisibility(View.VISIBLE);
					}
				}
				else
				{
					for (int i = 0; i < 2; i++)
					{
						signalLayouts[i].setVisibility(View.GONE);
					}
				}
				Log.d("provassh", descriptionEditText.getText().toString() + "d");
				if ((chooseAdvice.isChecked() || chooseComplaint.isChecked()) && (!descriptionEditText.getText().toString().equals("")))
				{
					((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				}
				else if ((chooseWarning.isChecked() && (!descriptionEditText.getText().toString().equals("") || checkBoxes[0].isChecked()
						|| checkBoxes[1].isChecked() || checkBoxes[2].isChecked() || checkBoxes[3].isChecked())))
				{
					((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				}
			}
		});
		descriptionEditText.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (!descriptionEditText.getText().toString().equals(""))
				{
					if (chooseAdvice.isChecked() || chooseComplaint.isChecked() || chooseWarning.isChecked())
					{
						((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
					}
				}
				else
				{
					((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});

		for (int i = 0; i < 4; i++)
		{
			checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (checkBoxes[0].isChecked() || checkBoxes[1].isChecked() || checkBoxes[2].isChecked() || checkBoxes[3].isChecked())
					{
						((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
					}
					else
					{
						((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
					}
				}
			});
		}
	}

	private void dispatchTakePictureIntent()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
		{
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
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
