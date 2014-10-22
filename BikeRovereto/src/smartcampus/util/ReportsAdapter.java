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
import eu.trentorise.smartcampus.bikesharing.R;

public class ReportsAdapter extends ArrayAdapter<Report>
{

	ArrayList<Report> mReports;

	public ReportsAdapter(Context context, int resource, ArrayList<Report> reports)
	{
		super(context, resource, reports);
		mReports = reports;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		int layoutCode = getItemViewType(position);
		
		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			viewHolder = new ViewHolder();
			if (layoutCode == 0)
			{
				convertView = inflater.inflate(R.layout.report_model_warning_without_summary, parent, false);
				viewHolder.type = (TextView) convertView.findViewById(R.id.type);
				viewHolder.problems = (TextView) convertView.findViewById(R.id.problems);
				
			}
			else if (layoutCode == 1)
			{
				convertView = inflater.inflate(R.layout.report_model_warning, parent, false);
				viewHolder.type = (TextView) convertView.findViewById(R.id.type);
				viewHolder.problems = (TextView) convertView.findViewById(R.id.problems);
				viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
			}
			else
			{
				convertView = inflater.inflate(R.layout.report_model, parent, false);
				viewHolder.type = (TextView) convertView.findViewById(R.id.type);
				viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
			}			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.type.setText(mReports.get(position).getType().toHumanString(getContext()));
		
//		switch (layoutCode) {
//		case 0:
//			viewHolder.problems.setText(getContext().getString(R.string.problems) + " "
//					+ mReports.get(position).getWarningsHumanReadable(getContext()));
//			break;
//		case 1:
//			viewHolder.problems.setText(getContext().getString(R.string.problems) + " "
//					+ mReports.get(position).getWarningsHumanReadable(getContext()));
//			viewHolder.summary.setText(mReports.get(position).getDetails());
//			break;
//		default:
//			viewHolder.summary.setText(mReports.get(position).getDetails());			
//			break;
//		}
		
		return convertView;
	}

	@Override
	public int getCount()
	{
		return mReports.size();
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (getItem(position).getDetails().equals(""))
		{
			return 0;
		}
		else
		{
			if ( getItem(position).getType() == Report.Type.WARNING)
			{
				return 1;
			}
			else
			{
				return 2;
			}
		}
	}
	

	private static class ViewHolder
	{
		TextView type;
		TextView summary;
		TextView problems;
	}

}
