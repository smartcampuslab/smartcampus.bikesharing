package smartcampus.util;


//import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
//import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import smartcampus.activity.SignalView;
import smartcampus.model.Bike;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class BikeInfoWindow extends MarkerInfoWindow
{
	Context mContext;
	MapView myMapView;
	Bike bike;

	public BikeInfoWindow(MapView mapView, final FragmentManager fragmentManager)
	{
		super(R.layout.info_bubble, mapView);
		myMapView = mapView;
		
		TextView btn = (TextView) (mView.findViewById(R.id.btToDetails));
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
//				Intent detailsIntent = new Intent(mContext,
//						SignalActivity.class);
//				detailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//				detailsIntent.putExtra("bike", bike);
//
//				mContext.startActivity(detailsIntent);
				
				SignalView detailsFragment = SignalView.newInstance(bike);
				FragmentTransaction transaction1 = fragmentManager.beginTransaction();
				transaction1.setCustomAnimations(R.anim.slide_left, R.anim.alpha_out,
						R.anim.alpha_in, R.anim.slide_right);
				transaction1.replace(R.id.content_frame, detailsFragment);
				transaction1.addToBackStack(null);
				transaction1.commit();
			}
		});
	}


	@Override
	public void open(Object object, GeoPoint position, int offsetX, int offsetY)
	{
		super.open(object, position, offsetX, offsetY);
	}
	
	@Override
	public void onOpen(Object item)
	{
		super.onOpen(item);
		BikeMarker sItem = (BikeMarker) item;
		bike = sItem.getBike();
		if(bike.getReports().size() > 0)
		{
			mView.findViewById(R.id.image_warnings).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void close()
	{
		super.close();
	}
}