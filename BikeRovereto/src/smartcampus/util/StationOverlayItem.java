package smartcampus.util;

import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.util.GeoPoint;

import smartcampus.model.Station;

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
