package smartcampus.util;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

import smartcampus.model.Bike;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class BikeInfoWindow extends MarkerInfoWindow {
	private MapView myMapView;
	private BikeMarker mItem;
	private Bike mBike;

	public BikeInfoWindow(MapView mapView, final FragmentManager fragmentManager) {
		super(R.layout.bonuspack_bubble, mapView);
		myMapView = mapView;
		TextView btn = (TextView) (mView.findViewById(R.id.btToDetails));
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO implement anarchic bikes
			}
		});
	}

	@Override
	public void onOpen(Object item) {
		super.onOpen(item);
		mItem = (BikeMarker) item;
		mBike = mItem.getBike();

		// if(bike.areThereReports())
		// {
		// mView.findViewById(R.id.image_warnings).setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// mView.findViewById(R.id.image_warnings).setVisibility(View.GONE);
		// }
	}

}