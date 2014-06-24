package smartcampus.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import smartcampus.model.NotificationBlock;
import smartcampus.model.Station;
import smartcampus.util.RemindersAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import eu.trentorise.smartcampus.bikerovereto.R;

public class ReminderEdit extends Fragment {

	private Station station;
	private ListView listView;
	private ArrayList<NotificationBlock> allReminders;
	private RemindersAdapter adapter;

	public static ReminderEdit newInstance(Station station) {
		ReminderEdit fragment = new ReminderEdit();
		Bundle bundle = new Bundle();
		bundle.putParcelable("station", station);
		fragment.setArguments(bundle);

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_reminder_edit, container, false);
		
		listView = (ListView) rootView.findViewById(R.id.list);
		station = getArguments().getParcelable("station");
		allReminders = NotificationBlock.readArrayListFromFile(MainActivity.FILENOTIFICATIONDB, getActivity());
		final ArrayList<NotificationBlock> reminders = new ArrayList<NotificationBlock>();
		for (NotificationBlock nb : allReminders)
		{
			if (nb.getID().equals(station.getId()))
			{
				reminders.add(nb);
			}
		}
		Collections.sort(reminders, new Comparator<NotificationBlock>() {
			@Override
			public int compare(NotificationBlock lhs, NotificationBlock rhs) {				
				return (int) (rhs.getCalendar().getTimeInMillis()-lhs.getCalendar().getTimeInMillis());
			}
		});
		
		TextView textView = new TextView(getActivity());
		textView.setText("Modifica i promemoria");
		textView.setTextSize(15);

		listView.addHeaderView(textView, null, false);
		listView.setEmptyView(rootView.findViewById(R.id.no_reminders));
		adapter = new RemindersAdapter(getActivity(), reminders, allReminders);
		listView.setAdapter(adapter);

		((MainActivity) getActivity()).getSupportActionBar().setTitle(station.getName());
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				editReminder(reminders.get(position-1)); //Minus one because 0 is the header!
			}

			
		});
		
		setHasOptionsMenu(true);
		return rootView;
	}
	

	@Override
	public void onDetach()
	{
		((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));		
		super.onDetach();
	}

	
	private void editReminder(final NotificationBlock noti) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final TimePicker picker = new TimePicker(getActivity());
		picker.setCurrentHour(noti.getCalendar().getTime().getHours());
		picker.setCurrentMinute(noti.getCalendar().getTime().getMinutes());
		builder.setTitle(getString(R.string.add_reminder));
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogI, int id)
			{
				Calendar c = Calendar.getInstance();
				noti.setCalendar(new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), picker
						.getCurrentHour(), picker.getCurrentMinute()));
				NotificationBlock.saveArrayListToFile(allReminders, MainActivity.FILENOTIFICATIONDB, getActivity());
				adapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
			}
		});
		picker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
		builder.setView(picker);

		builder.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case android.R.id.home:
			getFragmentManager().popBackStack();
			break;
		}
		return super.onOptionsItemSelected(item);
	}


}
