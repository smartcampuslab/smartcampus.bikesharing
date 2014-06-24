package smartcampus.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RemindersAdapter extends ArrayAdapter<GregorianCalendar>
{

	ArrayList<GregorianCalendar> mReminders;

	public RemindersAdapter(Context context, ArrayList<GregorianCalendar> reminders)
	{
		super(context, 0, reminders);
		mReminders = reminders;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			convertView = inflater.inflate(android.R.layout.simple_list_item_1,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.reminder = (TextView) convertView
					.findViewById(android.R.id.text1);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.reminder.setText(mReminders.get(position).getTime().getHours() + ":" + mReminders.get(position).getTime().getMinutes());
		return convertView;

	}

	private static class ViewHolder
	{
		TextView reminder;
	}

}
