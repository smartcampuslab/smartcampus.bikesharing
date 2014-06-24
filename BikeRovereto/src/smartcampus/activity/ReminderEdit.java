package smartcampus.activity;

import java.util.ArrayList;
import java.util.Calendar;

import smartcampus.model.NotificationBlock;
import smartcampus.model.Station;
import smartcampus.util.RemindersAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		ArrayList<Calendar> reminders = NotificationBlock.getReminderForID(station.getId(), this);
		listView.setAdapter(new RemindersAdapter(this, reminders));
		
		TextView textView = new TextView(this);
		textView.setText("Modifica i promemoria");
		textView.setTextSize(15);

		listView.addHeaderView(textView, null, false);
		listView.setEmptyView(findViewById(R.id.no_reminders));
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		getSupportActionBar().setTitle(station.getName());
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
		
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
