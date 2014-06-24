package smartcampus.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RemindersAdapter extends ArrayAdapter<GregorianCalendar>
{


	public RemindersAdapter(Context context, ArrayList<GregorianCalendar> reminders)
	{
		super(context, 0, reminders);
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
		String format = "hh:mm aa";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		viewHolder.reminder.setText(simpleDateFormat.format(getItem(position).getTime()));
		return convertView;

	}

	private static class ViewHolder
	{
		TextView reminder;
	}

}
