package smartcampus.util;

import java.util.ArrayList;

import smartcampus.model.Station;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		
		viewHolder.name.setText(mStations.get(position).getName());
		viewHolder.street.setText(mStations.get(position).getStreet());
		viewHolder.availableBike.setText(mStations.get(position).getNSlotsUsed()+"");
		viewHolder.availableSlots.setText(mStations.get(position).getNSlotsEmpty()+"");
		viewHolder.distance.setText(Tools.formatDistance(mStations.get(position).getDistance()));
		
		return convertView;
		
	}
	
	private static class ViewHolder {
		TextView name;
		TextView street;
		TextView availableBike, availableSlots;
		TextView distance;	
		
		
	}

}


