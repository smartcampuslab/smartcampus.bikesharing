package smartcampus.util;

//import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
//import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import smartcampus.activity.StationDetails;
import smartcampus.model.Station;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class StationInfoWindow extends MarkerInfoWindow
{
	// Context mContext;
	MapView myMapView;
	Station station;

	// GeoPoint currentLocation;

	public StationInfoWindow(MapView mapView, final FragmentManager fragmentManager)
	{
		super(R.layout.bonuspack_bubble, mapView);
		// mContext = context;
		myMapView = mapView;
		TextView btn = (TextView) (mView.findViewById(R.id.btToDetails));
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{

				StationDetails detailsFragment = StationDetails.newInstance(station);
				FragmentTransaction transaction1 = fragmentManager.beginTransaction();
				transaction1.setCustomAnimations(R.anim.slide_left, R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_right);
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

		StationMarker sItem = (StationMarker) item;

		station = sItem.getStation();

		TextView tAvailable = (TextView) mView.findViewById(R.id.txt_available);
		TextView tEmpty = (TextView) mView.findViewById(R.id.txt_empty);
		tAvailable.setText(Integer.toString(sItem.getStation().getNBikesPresent()));
		tEmpty.setText(Integer.toString(sItem.getStation().getNSlotsEmpty()));
		tAvailable.setVisibility(View.VISIBLE);
		tEmpty.setVisibility(View.VISIBLE);

		TextView title = (TextView) mView.findViewById(R.id.bubble_title);
		title.setVisibility(View.VISIBLE);
		title.setText(sItem.getTitle().split("-")[0]);
//		mView.findViewById(R.id.bubble_description).setVisibility(View.VISIBLE);

		mView.findViewById(R.id.images_layout).setVisibility(View.VISIBLE);


//		if (station.areThereReports())
//		{
//			mView.findViewById(R.id.image_warnings).setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			mView.findViewById(R.id.image_warnings).setVisibility(View.GONE);
//		}
	}
}