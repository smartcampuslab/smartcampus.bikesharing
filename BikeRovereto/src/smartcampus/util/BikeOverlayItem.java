package smartcampus.util;

import smartcampus.model.Bike;
import smartcampus.model.Station;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.ExtendedOverlayItem;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

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
