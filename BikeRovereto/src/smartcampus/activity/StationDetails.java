package smartcampus.activity;

import smartcampus.model.Station;
import smartcampus.util.ReportsAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
		
		TextView name;
		TextView street;
		TextView availableBike, availableSlots;
		TextView distance;	

		name=(TextView)header.findViewById(R.id.name);
		street=(TextView)header.findViewById(R.id.street);
		availableBike=(TextView)header.findViewById(R.id.available_bikes);
		availableSlots=(TextView)header.findViewById(R.id.available_slots);
		distance=(TextView)header.findViewById(R.id.distance);
				
		// get the station from the parcels
		station = getIntent().getExtras().getParcelable("station");
		mList=(ListView)findViewById(R.id.details);
		mList.addHeaderView(header);
		name.setText(station.getName());
		street.setText(station.getStreet());
		availableBike.setText(station.getNSlotsUsed()+"");
		availableSlots.setText(station.getNSlotsEmpty()+"");
		distance.setText("5 Km");
		mList.setAdapter(new ReportsAdapter(this, 0, station.getReports()));
		
		distance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=padova"));
				startActivity(i);
			}
		});
		
		
		
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
