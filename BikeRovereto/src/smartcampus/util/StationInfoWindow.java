package smartcampus.util;

//import org.osmdroid.bonuspack.overlays.DefaultInfoWindow;
//import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import smartcampus.activity.DetailsActivity;
import smartcampus.activity.MainActivity;
import smartcampus.activity.StationsListFragment;
import smartcampus.model.Station;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class StationInfoWindow extends MarkerInfoWindow {
	private MapView mMapView;
	private Station station;
	private StationMarker mItem;
	private Paint mPaint;

	// GeoPoint currentLocation;

	public StationInfoWindow(MapView mapView,
			final FragmentManager fragmentManager) {
		super(R.layout.bonuspack_bubble, mapView);

		mMapView = mapView;

		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);

	}

	@Override
	public void onOpen(Object item) {
		super.onOpen(item);

		mItem = (StationMarker) item;

		station = mItem.getStation();
		mMapView.getController().animateTo(mItem.getPosition());
		if (mMapView.getZoomLevel() < 20)
			mMapView.getController().setZoom(20);
		TextView tAvailable = (TextView) mView.findViewById(R.id.txt_available);
		TextView tEmpty = (TextView) mView.findViewById(R.id.txt_empty);
		tAvailable.setText(Integer.toString(mItem.getStation()
				.getNBikesPresent()));
		tEmpty.setText(Integer.toString(mItem.getStation().getNSlotsEmpty()));
		tAvailable.setVisibility(View.VISIBLE);
		tEmpty.setVisibility(View.VISIBLE);

		TextView title = (TextView) mView.findViewById(R.id.bubble_title);
		title.setVisibility(View.VISIBLE);
		title.setText(mItem.getTitle().split("-")[0]);
		// mView.findViewById(R.id.bubble_description).setVisibility(View.VISIBLE);

		mView.findViewById(R.id.images_layout).setVisibility(View.VISIBLE);

		// USELESS untile reprts must be displayed
		// if (station.areThereReports())
		// {
		// mView.findViewById(R.id.image_warnings).setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// mView.findViewById(R.id.image_warnings).setVisibility(View.GONE);
		// }

		TextView btn = (TextView) (mView.findViewById(R.id.btToDetails));
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(mMapView.getContext(),
						DetailsActivity.class);
				i.putExtra(DetailsActivity.EXTRA_STATION, mItem.getStation());
				GeoPoint p = ((MainActivity) mMapView.getContext())
						.getCurrentLocation();
				if (p != null) {
					i.putExtra(DetailsActivity.EXTRA_POSITION,
							new double[] { p.getLatitude(), p.getLongitude() });
				}
				mMapView.getContext().startActivity(i);
			}
		});
	}

}