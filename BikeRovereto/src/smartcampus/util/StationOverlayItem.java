package smartcampus.util;

import smartcampus.model.Station;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.ExtendedOverlayItem;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class StationOverlayItem extends ExtendedOverlayItem
{

	Station station;
	public StationOverlayItem(String aTitle, String aDescription, GeoPoint aGeoPoint, Station station)
	{
		super(station.getName(), station.getStreet(), station.getPosition());
		this.station = station;
	}
	
	public Station getStation ()
	{
		return station;
	}

}
