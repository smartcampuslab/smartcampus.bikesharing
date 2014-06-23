package smartcampus.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import smartcampus.model.Station;
import smartcampus.util.RemindersAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class ReminderEdit extends ActionBarActivity {

	private Station station;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder_edit);
		if (getIntent().getExtras().containsKey("station"))
			this.station = (Station) getIntent().getExtras().getParcelable("station");
		listView = (ListView) findViewById(R.id.list);
		ArrayList<Calendar> reminders = new ArrayList<Calendar>();
		reminders.add(new GregorianCalendar(2014,6,23,12,20));
		reminders.add(new GregorianCalendar(2014,6,23,18,0));
		reminders.add(new GregorianCalendar(2014,6,23,8,0));
		listView.setAdapter(new RemindersAdapter(this, reminders));
		
		TextView textView = new TextView(this);
		textView.setText("Modifica i promemoria");

		listView.addHeaderView(textView);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		getSupportActionBar().setTitle(station.getName());
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
