package smartcampus.util;

import java.io.File;
import java.util.Calendar;

import smartcampus.asynctask.SendReport;
import smartcampus.model.Bike;
import smartcampus.model.Report;
import smartcampus.model.Reportable;
import smartcampus.model.Station;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import eu.trentorise.smartcampus.bikerovereto.R;

public class ReportTools {

	private static Uri mImageUri;
	public static ImageView photoView;
	public static Bitmap image;
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	
	public static Report addReport(final Reportable reportable, final Activity activity, final Fragment fragment)
	{
		final Report report;
		final long date = Calendar.getInstance().getTimeInMillis();
		report = new Report(reportable.getType(), reportable.getId(), date);
		View dialogContent = activity.getLayoutInflater().inflate(R.layout.report_dialog, null);
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
		photoView = (ImageView) dialogContent.findViewById(R.id.photo);
		addPhoto.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				dispatchTakePictureIntent(fragment, activity);
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setInverseBackgroundForced(true);
		builder.setTitle(activity.getString(R.string.report_in) + " " + reportable.getName());
		builder.setPositiveButton(R.string.report, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogI, int id)
			{
				if (chooseAdvice.isChecked())
				{
					report.setType(Report.Type.ADVICE);
				}
				else if (chooseComplaint.isChecked())
				{
					report.setType(Report.Type.COMPLAINT);
				}
				else if (chooseWarning.isChecked())
				{
					report.setType(Report.Type.WARNING);
					String details = "";
					for (int i = 0; i < checkBoxes.length; i++)
					{
						if (checkBoxes[i].isChecked())
						{
							report.addWarning(Report.WARNINGS[i]);
						}
					}
					report.setDetails(details);
				}
				report.setDetails(descriptionEditText.getText().toString());

				report.setPhoto(image);

				reportable.addReport(report);
				new SendReport(activity).execute(report);
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
				else
				{
					((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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
		return report;
	}

	private static void dispatchTakePictureIntent(Fragment fragment, Activity activity)
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photo;
		try
		{
			// place where to store camera taken picture
			photo = createTemporaryFile("picture", ".png", activity);
			photo.delete();
		}
		catch (Exception e)
		{
			Log.v("REPORT", "Can't create file to take picture!");
			return;
		}
		mImageUri = Uri.fromFile(photo);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		Log.d("test", mImageUri.toString());
		// start camera intent
		if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null)
		{
			fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	private static File createTemporaryFile(String part, String ext, Activity activity) throws Exception
	{
		File tempDir = activity.getExternalCacheDir();
		tempDir = new File(tempDir.getAbsolutePath());
		if (!tempDir.exists())
		{
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}
	public static Bitmap grabImage(Context context)
	{
		Log.d("asasdasda",mImageUri.toString());
	    context.getContentResolver().notifyChange(mImageUri, null);
	    ContentResolver cr = context.getContentResolver();
	    try
	    {
	        return android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
	    }
	    catch (Exception e)
	    {
	        Log.d("REPORT", "Failed to load", e);
	        return null;
	    }
	}
}
