package eu.trentorise.smartcampus.bikerovereto;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends Activity
{
	Button close;
	Fragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		getActionBar().hide();
		close = (Button) findViewById(R.id.bt_close);
		setBtClose();
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
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
		overridePendingTransition(R.anim.abc_slide_in_top,R.anim.abc_slide_out_bottom);
	}
}
