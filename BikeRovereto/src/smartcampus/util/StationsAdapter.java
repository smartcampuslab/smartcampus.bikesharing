package smartcampus.util;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import smartcampus.activity.MainActivity;
import smartcampus.model.Station;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class StationsAdapter extends ArrayAdapter<Station>
{

	private ArrayList<Station> mStations;
	private GeoPoint currentLocation;
	private SharedPreferences pref;
	private boolean isFavouriteAdapter = false;
	
	public StationsAdapter(Context context, int resource,
			ArrayList<Station> stations, GeoPoint currentLocation)
	{
		super(context, resource, stations);
		mStations = stations;
		this.currentLocation = currentLocation;
		pref = context.getSharedPreferences("favStations", Context.MODE_PRIVATE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;

		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			convertView = inflater.inflate(R.layout.stations_model, parent,
					false);

			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.street = (TextView) convertView
					.findViewById(R.id.street);
			viewHolder.availableBike = (TextView) convertView
					.findViewById(R.id.available_bikes);
			viewHolder.availableSlots = (TextView) convertView
					.findViewById(R.id.available_slots);
			viewHolder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			viewHolder.favouriteBtn = (CheckBox) convertView
					.findViewById(R.id.favourites_btn);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Station thisStation = mStations.get(position);

		viewHolder.name.setText(thisStation.getName());
		viewHolder.street.setText(thisStation.getStreet());
		viewHolder.availableBike.setText(thisStation.getNBikesPresent() + "");
		viewHolder.availableSlots.setText(thisStation.getNSlotsEmpty() + "");
		viewHolder.distance.setText(Tools.formatDistance(thisStation
				.getDistance()));
		viewHolder.favouriteBtn.setChecked(thisStation.getFavourite());
		viewHolder.distance.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse(Tools.getPathString(currentLocation, thisStation.getPosition())));
				getContext().startActivity(i);
			}
		});
		viewHolder.favouriteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				thisStation.setFavourite(!thisStation.getFavourite());
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean(Tools.STATION_PREFIX+thisStation.getId(), thisStation.getFavourite());
				editor.apply();
				if (thisStation.getFavourite())
					((MainActivity)getContext()).addFavouriteStation(thisStation);
				else
					((MainActivity)getContext()).removeFavouriteStation(thisStation);
				if (isFavouriteAdapter)
					notifyDataSetChanged();
			}
		});
		return convertView;

	}

	private static class ViewHolder
	{
		TextView name;
		TextView street;
		TextView availableBike, availableSlots;
		TextView distance;
		CheckBox favouriteBtn;

	}
	
	public void setIsFavouriteAdapter(boolean isFavAdapter)
	{
		isFavouriteAdapter = isFavAdapter;
	}

}
