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
				// Intent detailsIntent = new Intent(mContext,
				// StationDetails.class);
				// detailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// detailsIntent.putExtra("station", station);
				//
				// mContext.startActivity(detailsIntent);

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
	public void open(Object object, GeoPoint position, int offsetX, int offsetY)
	{
		// super.open(object, position, offsetX + 15, offsetY + 80);
		super.open(object, position, offsetX, offsetY);
	}

	@Override
	public void onOpen(Object item)
	{
		super.onOpen(item);

		StationMarker sItem = (StationMarker) item;

		station = sItem.getStation();
		mView.findViewById(R.id.green_bike).setVisibility(View.VISIBLE);
		mView.findViewById(R.id.black_bike).setVisibility(View.VISIBLE);

		TextView tAvailable = (TextView) mView.findViewById(R.id.txt_available);
		TextView tEmpty = (TextView) mView.findViewById(R.id.txt_empty);
		tAvailable.setText(Integer.toString(sItem.getStation().getNBikesPresent()));
		tEmpty.setText(Integer.toString(sItem.getStation().getNSlotsEmpty()));
		tAvailable.setVisibility(View.VISIBLE);
		tEmpty.setVisibility(View.VISIBLE);

		mView.findViewById(R.id.bubble_title).setVisibility(View.VISIBLE);
		mView.findViewById(R.id.bubble_description).setVisibility(View.VISIBLE);

		mView.findViewById(R.id.numbers_layout).setVisibility(View.VISIBLE);
		mView.findViewById(R.id.images_layout).setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mView.findViewById(R.id.main_layout).getLayoutParams();
		params.setMargins(0, 0, 0, 5);
		mView.findViewById(R.id.main_layout).setLayoutParams(params);

		if (station.areThereReports())
		{
			mView.findViewById(R.id.image_warnings).setVisibility(View.VISIBLE);
		}
		else
		{
			mView.findViewById(R.id.image_warnings).setVisibility(View.GONE);
		}
	}

	@Override
	public void close()
	{
		super.close();
	}
}