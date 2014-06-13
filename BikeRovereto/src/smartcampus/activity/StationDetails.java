package smartcampus.activity;

import smartcampus.model.Station;
import smartcampus.util.ReportsAdapter;
import smartcampus.util.Tools;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class StationDetails extends ActionBarActivity
{

	// the station with its details
	Station station;
	
	ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_details);

		LayoutInflater inflater = getLayoutInflater();
		View header = inflater.inflate(R.layout.stations_model, null);
		
		// get the station from the parcels
		station = getIntent().getExtras().getParcelable("station");
		mList=(ListView)findViewById(R.id.details);
		mList.addHeaderView(header);
		mList.setAdapter(new ReportsAdapter(this, 0, station.getReports()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.station_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
