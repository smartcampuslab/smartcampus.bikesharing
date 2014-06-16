package smartcampus.util;

import java.util.ArrayList;

import smartcampus.model.Station;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class StationsAdapter extends ArrayAdapter<Station>{

	ArrayList<Station> mStations;
	
	
	public StationsAdapter(Context context, int resource, ArrayList<Station> stations) {
		super(context, resource, stations);
		mStations=stations;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (convertView == null){
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			convertView = inflater.inflate(R.layout.stations_model, parent, false);
			
			viewHolder=new ViewHolder();
			viewHolder.name=(TextView)convertView.findViewById(R.id.name);
			viewHolder.street=(TextView)convertView.findViewById(R.id.street);
			viewHolder.availableBike=(TextView)convertView.findViewById(R.id.available_bikes);
			viewHolder.availableSlots=(TextView)convertView.findViewById(R.id.available_slots);
			viewHolder.distance=(TextView)convertView.findViewById(R.id.distance);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder)convertView.getTag();			
		}
		final Station thisStation = mStations.get(position);
		
		viewHolder.name.setText(thisStation.getName());
		viewHolder.street.setText(thisStation.getStreet());
		viewHolder.availableBike.setText(thisStation.getNSlotsUsed()+"");
		viewHolder.availableSlots.setText(thisStation.getNSlotsEmpty()+"");
		viewHolder.distance.setText(Tools.formatDistance(thisStation.getDistance()));

		viewHolder.distance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+ thisStation.getLatitudeDegree() + "," + thisStation.getLongitudeDegree()));
				getContext().startActivity(i);
			}
		});		
		
		return convertView;
		
	}
	
	private static class ViewHolder {
		TextView name;
		TextView street;
		TextView availableBike, availableSlots;
		TextView distance;	
		
		
	}

}


