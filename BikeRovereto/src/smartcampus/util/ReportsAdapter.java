package smartcampus.util;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReportsAdapter extends ArrayAdapter<String>
{

	ArrayList<String> mReports;

	public ReportsAdapter(Context context, int resource,
			ArrayList<String> reports)
	{
		super(context, resource, reports);
		mReports = reports;
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
			viewHolder.report = (TextView) convertView
					.findViewById(android.R.id.text1);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.report.setText(mReports.get(position));
		return convertView;

	}

	private static class ViewHolder
	{
		TextView report;
	}

}
