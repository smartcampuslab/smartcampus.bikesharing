package smartcampus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class About extends ActionBarActivity
{
	private static final String PARAM_LAYOUT = "layout";
	ImageButton close;
	Fragment fragment;
	
	public static Intent createAbout(Context ctx, int layout) {
		Intent intent = new Intent(ctx, About.class);
		intent.putExtra(PARAM_LAYOUT, layout);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(getIntent().getIntExtra(PARAM_LAYOUT, 0));
		getSupportActionBar().hide();
		close = (ImageButton) findViewById(R.id.about_close);
		setBtClose();
		TextView tv	= (TextView) findViewById(android.R.id.summary);
		if (tv != null) {
			try {
				PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
				tv.setText(info.versionName);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void setBtClose()
	{
		close.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.alpha_in,
				R.anim.alpha_out);

	}
	
	public void onClick(View v) {
		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse((String)v.getTag()));
			startActivity(i);
		} catch (Exception e) {
			Log.e("ABOUT",""+e.getMessage());
		}
	}
}
