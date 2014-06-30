package smartcampus.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import smartcampus.activity.MainActivity;
import smartcampus.model.NotificationBlock;
import smartcampus.notifications.NotificationReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class RemindersAdapter extends ArrayAdapter<NotificationBlock>
{

	private ArrayList<NotificationBlock> reminders;
	private ArrayList<NotificationBlock> allReminders;
	private String stationID;

	public RemindersAdapter(Context context, ArrayList<NotificationBlock> reminders, ArrayList<NotificationBlock> allReminders, String stationID)
	{
		super(context, 0, reminders);
		this.reminders = reminders;
		this.allReminders = allReminders;
		this.stationID = stationID;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			convertView = inflater.inflate(R.layout.reminder_model,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.reminder = (TextView) convertView
					.findViewById(R.id.hour);
			viewHolder.clearBtn = (ImageView) convertView
					.findViewById(R.id.clear);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String format = "HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		viewHolder.reminder.setText(simpleDateFormat.format(getItem(position).getCalendar().getTime()));
		viewHolder.clearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("adapter",reminders.size()+"");
				NotificationBlock currentNB = reminders.get(position);
				reminders.remove(currentNB);
				allReminders.remove(currentNB);
				NotificationReceiver nr = new NotificationReceiver();
				nr.cancelAlarm(getContext(), currentNB.getUniqueID(), stationID);
				notifyDataSetChanged();
				NotificationBlock.saveArrayListToFile(allReminders, MainActivity.FILENOTIFICATIONDB, getContext());
			}
		});
		return convertView;

	}

	private static class ViewHolder
	{
		TextView reminder;
		ImageView clearBtn;
	}
}
