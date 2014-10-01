package smartcampus.util;


import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import smartcampus.activity.SignalView;
import smartcampus.model.Bike;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class BikeInfoWindow extends MarkerInfoWindow
{
	private MapView myMapView;
	private BikeMarker mItem;
	private Bike mBike;

	public BikeInfoWindow(MapView mapView, final FragmentManager fragmentManager)
	{
		super(R.layout.bonuspack_bubble, mapView);
		myMapView = mapView;
		TextView btn = (TextView) (mView.findViewById(R.id.btToDetails));
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				
				SignalView detailsFragment = SignalView.newInstance(mBike);
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
	public void onOpen(Object item)
	{
		super.onOpen(item);
		mItem = (BikeMarker) item;
		mBike = mItem.getBike();
		
		
//		if(bike.areThereReports())
//		{
//			mView.findViewById(R.id.image_warnings).setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			mView.findViewById(R.id.image_warnings).setVisibility(View.GONE);
//		}
	}
	

}