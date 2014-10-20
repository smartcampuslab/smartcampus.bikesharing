package smartcampus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import eu.trentorise.smartcampus.bikesharing.R;

public class About extends ActionBarActivity
{
	ImageButton close;
	Fragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		getSupportActionBar().hide();
		close = (ImageButton) findViewById(R.id.close_credits);
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
