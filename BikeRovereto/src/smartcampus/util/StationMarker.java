package smartcampus.util;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import smartcampus.model.Station;

public class StationMarker extends Marker
{

	Station station;
	public StationMarker(MapView mapView, Station station)
	{
		super(mapView);
		this.station = station;
	}
	
	public Station getStation ()
	{
		return station;
	}
	
	

}
