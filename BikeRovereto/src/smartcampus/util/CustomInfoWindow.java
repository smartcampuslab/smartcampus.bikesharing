package smartcampus.util;

//import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
//import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;

import smartcampus.activity.StationDetails;
import smartcampus.model.Station;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.DefaultInfoWindow;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.ExtendedOverlayItem;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;
import eu.trentorise.smartcampus.osm.android.views.MapView;

public class CustomInfoWindow extends DefaultInfoWindow
{
	//Context mContext;
	MapView myMapView;
	Station station;
	//GeoPoint currentLocation;

	public CustomInfoWindow(MapView mapView, final FragmentManager fragmentManager)
	{
		super(R.layout.info_bubble, mapView);
		//mContext = context;
		myMapView = mapView;
		Button btn = (Button) (mView.findViewById(R.id.btToDetails));
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
//				Intent detailsIntent = new Intent(mContext,
//						StationDetails.class);
//				detailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				detailsIntent.putExtra("station", station);
//
//				mContext.startActivity(detailsIntent);
				
				StationDetails detailsFragment = StationDetails.newInstance(station);
				FragmentTransaction transaction1 = fragmentManager.beginTransaction();
				transaction1.replace(R.id.content_frame, detailsFragment);
				transaction1.addToBackStack(null);
				transaction1.commit();
			}
		});
	}

	@Override
	public void open(ExtendedOverlayItem item, int offsetX, int offsetY)
	{
		// TODO Auto-generated method stub
		Log.d("debug2", "opened");
		super.open(item, offsetX + 15, offsetY + 80);
	}

	@Override
	public void onOpen(ExtendedOverlayItem item)
	{
		super.onOpen(item);

		StationOverlayItem sItem = (StationOverlayItem) item;

		station = sItem.getStation();
		mView.findViewById(R.id.green_bike).setVisibility(View.VISIBLE);
		mView.findViewById(R.id.black_bike).setVisibility(View.VISIBLE);
		TextView tAvailable = (TextView) mView.findViewById(R.id.txt_available);
		TextView tEmpty = (TextView) mView.findViewById(R.id.txt_empty);
		tAvailable
				.setText(Integer.toString(sItem.getStation().getNSlotsUsed()));
		tEmpty.setText(Integer.toString(sItem.getStation().getNSlotsEmpty()));
		tAvailable.setVisibility(View.VISIBLE);
		tEmpty.setVisibility(View.VISIBLE);

	}

	@Override
	public void close()
	{
		Log.d("debug2", "removed");
		super.close();
	}
}