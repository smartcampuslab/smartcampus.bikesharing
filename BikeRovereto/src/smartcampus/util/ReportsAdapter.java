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
import eu.trentorise.smartcampus.bikerovereto.R;

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
			convertView = inflater.inflate(R.layout.report_model,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.type = (TextView) convertView
					.findViewById(R.id.type);
			viewHolder.summary = (TextView) convertView
					.findViewById(R.id.summary);
			viewHolder.problems = (TextView) convertView
					.findViewById(R.id.problems);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.type.setText(mReports.get(position).getType().toHumanString(getContext()));
		viewHolder.summary.setText(mReports.get(position).getDetails());
		if (mReports.get(position).getType() == Report.Type.WARNING)
		{
			viewHolder.problems.setVisibility(View.VISIBLE);
			viewHolder.problems.setText(getContext().getString(R.string.problems) + " " + mReports.get(position).getWarningsHumanReadable(getContext()));
		}
		else
		{
			viewHolder.problems.setVisibility(View.GONE);
		}
		
		
		return convertView;

	}
	
	@Override
	public int getCount() {
		return mReports.size();
	}

	private static class ViewHolder
	{
		TextView type;
		TextView summary;
		TextView problems;
	}

}
