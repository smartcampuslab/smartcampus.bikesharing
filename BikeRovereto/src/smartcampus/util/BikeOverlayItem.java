package smartcampus.util;

import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.util.GeoPoint;

import smartcampus.model.Bike;

public class BikeOverlayItem extends ExtendedOverlayItem
{

	Bike bike;

	public BikeOverlayItem(String aTitle, String aDescription, GeoPoint aGeoPoint, Bike bike)
	{
		super("bici anarchica", "gialla", bike.getPosition());
		this.bike = bike;
		
	}

	public Bike getBike()
	{
		return bike;
	}

}
