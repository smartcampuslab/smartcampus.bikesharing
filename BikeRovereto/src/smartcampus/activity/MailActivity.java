package smartcampus.activity;

import eu.trentorise.smartcampus.bikesharing.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MailActivity extends ActionBarActivity {

	private static final String MAIL_ADDR = "bikesharing@asd.it";
	private static final String MAIL_SUBJ = "[BIKESHARING] ";
	private static final int PICTURE_CODE = 1234;

	private EditText mBody;
	private RadioGroup mKind;
	private Button mTakePicture;
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
		mTakePicture = (Button) findViewById(R.id.take_picture_btn);

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
		
		//MORE general, attachment works
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("application/image");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { MAIL_ADDR });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, MAIL_SUBJ+kind);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mBody.getText()
				.toString());
		if (mPath != null) {
			emailIntent.putExtra(Intent.EXTRA_STREAM,
					Uri.parse(mPath.toString()));
		}
		startActivity(Intent.createChooser(emailIntent,
				getString(R.string.send_with)));

		
		// MORE STRICT, it triggers only mail apps but the attachment function doesn't work
//		String kind = ((RadioButton) findViewById(mKind
//				.getCheckedRadioButtonId())).getText().toString();
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		String mailto = "mailto:" + MAIL_ADDR + "?subject=" + MAIL_SUBJ + kind
//				+ "&body=" + mBody.getText().toString();
//		if (mPath != null) {
//			mailto += "&attachment=" + getRealPathFromURI(mPath);
//		}
//		Uri data = Uri.parse(mailto);
//		intent.setData(data);
//		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, requestCode, data);
		if (requestCode == PICTURE_CODE && resultCode == RESULT_OK) {
			mPath = data.getData();
		}
	}
	
	// And to convert the image URI to the direct file system path of the image file
	public String getRealPathFromURI(Uri contentUri) {

	        // can post image
	        String [] proj={MediaStore.Images.Media.DATA};
	        Cursor cursor = getContentResolver().query( contentUri,
	                        proj, // Which columns to return
	                        null,       // WHERE clause; which rows to return (all rows)
	                        null,       // WHERE clause selection arguments (none)
	                        null); // Order-by clause (ascending by name)
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();

	        return cursor.getString(column_index);
	}

}
