package smartcampus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
}
