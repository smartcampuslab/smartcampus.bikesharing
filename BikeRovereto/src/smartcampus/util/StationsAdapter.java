package smartcampus.util;

import java.security.acl.LastOwnerException;
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
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class StationsAdapter extends ArrayAdapter<Station> {

	private ArrayList<Station> mStations;
	private GeoPoint currentLocation;
	private SharedPreferences pref;
	private boolean isFavouriteAdapter = false;
	private int mLastPosition = -1;
	private int mSelection = -1;

	public StationsAdapter(Context context, int resource,
			ArrayList<Station> stations) {
		super(context, resource, stations);
		mStations = stations;
		pref = context
				.getSharedPreferences("favStations", Context.MODE_PRIVATE);
	}

	public void setSelectionPos(int mSelection) {
		this.mSelection = mSelection;
	}
	public void cancelSelection(){
		this.mSelection=-1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
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
		} else {
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
		viewHolder.favouriteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				thisStation.setFavourite(!thisStation.getFavourite());
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean(Tools.STATION_PREFIX + thisStation.getId(),
						thisStation.getFavourite());
				editor.apply();
				if (thisStation.getFavourite())
					((MainActivity) getContext())
							.addFavouriteStation(thisStation);
				else
					((MainActivity) getContext())
							.removeFavouriteStation(thisStation);
				if (isFavouriteAdapter)
					notifyDataSetChanged();
			}
		});
		
		//ATTENTION this is a cheat to avoid an Android bug.
		if (position == mSelection) {
			final View row = convertView;
			convertView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					row.setSelected(true);
				}
			}, 50);
			
		}
		if (position > mLastPosition) {
			convertView.startAnimation(AnimationUtils.loadAnimation(
					getContext(), R.anim.slide_up));
			mLastPosition = position;
		}
		return convertView;

	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mLastPosition = -1;
	}

	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
		mLastPosition = -1;
	}

	private static class ViewHolder {
		TextView name;
		TextView street;
		TextView availableBike, availableSlots;
		TextView distance;
		CheckBox favouriteBtn;

	}

	public void setIsFavouriteAdapter(boolean isFavAdapter) {
		isFavouriteAdapter = isFavAdapter;
	}

}
