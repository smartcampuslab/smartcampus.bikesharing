package smartcampus.activity;

import java.io.File;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import eu.trentorise.smartcampus.bikesharing.R;

public class MailActivity extends ActionBarActivity {

	private static final String MAIL_SUBJ = "[BIKESHARING] ";
	private static final int PICTURE_CODE = 1234;
	private static final String METADATA_FEEDBACK_EMAIL = "eu.trentorise.smartcampus.bikerovereto.FEEDBACK_MAIL";
	private static final int IMG_HEIGHT = 150;

	private EditText mBody;
	private RadioGroup mKind;
	private LinearLayout mTakePicture;
	private Uri mPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(getString(R.string.app_name));

		mBody = (EditText) findViewById(R.id.Mail_text);
		mKind = (RadioGroup) findViewById(R.id.mail_kind_rdg);
		mTakePicture = (LinearLayout) findViewById(R.id.take_picture_ll);

		mTakePicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startGalleryIntent();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			return true;
		} else if (item.getItemId() == R.id.action_mail) {
			startMailIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startGalleryIntent() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,
				getString(R.string.select_image_app)), PICTURE_CODE);
	}

	private void startMailIntent() {

		String kind = ((RadioButton) findViewById(mKind
				.getCheckedRadioButtonId())).getText().toString();

		// MORE general, attachment works
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("application/image");

		String email = null;
		try {
			ApplicationInfo app = getPackageManager().getApplicationInfo(
					this.getPackageName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_META_DATA);
			Bundle metaData = app.metaData;
			email = metaData.getString(METADATA_FEEDBACK_EMAIL);
		} catch (NameNotFoundException e) {
			Log.e(getClass().getName(), ""+e.getMessage());
		}
		if (email != null) {
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { email });
		}
		
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, MAIL_SUBJ
				+ kind);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mBody.getText()
				.toString());
		if (mPath != null) {
			emailIntent.putExtra(Intent.EXTRA_STREAM,
					Uri.parse(mPath.toString()));
		}
		startActivity(Intent.createChooser(emailIntent,
				getString(R.string.send_with)));

		// MORE STRICT, it triggers only mail apps but the attachment function
		// doesn't work
		// String kind = ((RadioButton) findViewById(mKind
		// .getCheckedRadioButtonId())).getText().toString();
		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// String mailto = "mailto:" + MAIL_ADDR + "?subject=" + MAIL_SUBJ +
		// kind
		// + "&body=" + mBody.getText().toString();
		// if (mPath != null) {
		// mailto += "&attachment=" + getRealPathFromURI(mPath);
		// }
		// Uri data = Uri.parse(mailto);
		// intent.setData(data);
		// startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, requestCode, data);
		if (requestCode == PICTURE_CODE && resultCode == RESULT_OK) {
			mPath = data.getData();
			ImageView myImage = (ImageView) findViewById(R.id.feedback_img_result);
			myImage.setVisibility(View.VISIBLE);
			myImage.setImageURI(mPath);
		}
	}

}
