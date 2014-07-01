package smartcampus.util;

import java.util.ArrayList;

import smartcampus.model.Report;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReportsAdapter extends ArrayAdapter<Report>
{

	ArrayList<Report> mReports;

	public ReportsAdapter(Context context, int resource,
			ArrayList<Report> reports)
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
					.findViewById(android.R.id.text1); //TODO: custom layout!
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.report.setText(mReports.get(position).getDetails());
		return convertView;

	}

	private static class ViewHolder
	{
		TextView report;
	}

}
