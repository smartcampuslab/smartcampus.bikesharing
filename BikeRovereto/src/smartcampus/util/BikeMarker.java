package smartcampus.util;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import android.graphics.drawable.Drawable;
import smartcampus.model.Bike;

public class BikeMarker extends Marker
{

	Bike bike;

	public BikeMarker(MapView mapView, Bike bike)
	{
		super(mapView);
		this.bike = bike;
		
	}

	public Bike getBike()
	{
		return bike;
	}

	public Drawable getIcon() {
		return this.mIcon;
	}
}
