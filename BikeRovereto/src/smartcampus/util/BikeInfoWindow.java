package smartcampus.util;


//import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
//import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;

import smartcampus.activity.SignalActivity;
import smartcampus.model.Bike;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import eu.trentorise.smartcampus.bikerovereto.R;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.DefaultInfoWindow;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.ExtendedOverlayItem;
import eu.trentorise.smartcampus.osm.android.views.MapView;

public class BikeInfoWindow extends DefaultInfoWindow
{
	Context mContext;
	MapView myMapView;
	Bike bike;

	public BikeInfoWindow(MapView mapView, final FragmentManager fragmentManager)
	{
		super(R.layout.bike_info_bubble, mapView);
		myMapView = mapView;
		
		Button btn = (Button) (mView.findViewById(R.id.btToSignal));
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
				
				SignalActivity detailsFragment = SignalActivity.newInstance(bike);
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
		super.open(item, offsetX + 15, offsetY + 80);
	}

	@Override
	public void onOpen(ExtendedOverlayItem item)
	{
		super.onOpen(item);
		BikeOverlayItem sItem = (BikeOverlayItem) item;
		bike = sItem.getBike();

	}

	@Override
	public void close()
	{
		super.close();
	}
}