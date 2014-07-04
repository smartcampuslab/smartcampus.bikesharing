package smartcampus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import eu.trentorise.smartcampus.bikesharing.R;

public class About extends ActionBarActivity
{
	Button close;
	Fragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		getSupportActionBar().hide();
		close = (Button) findViewById(R.id.bt_close);
		setBtClose();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
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
		overridePendingTransition(R.anim.slide_down_slower, R.anim.slide_down);
	}
}
