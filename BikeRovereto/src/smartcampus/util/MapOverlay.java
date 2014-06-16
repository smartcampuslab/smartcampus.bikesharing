package smartcampus.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import eu.trentorise.smartcampus.osm.android.api.IGeoPoint;
import eu.trentorise.smartcampus.osm.android.util.BoundingBoxE6;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;
import eu.trentorise.smartcampus.osm.android.views.MapView;
import eu.trentorise.smartcampus.osm.android.views.overlay.Overlay;

/**
 * A class to handling the press gesture on the map(you can set the pressing
 * time). You only have to Override the OnLongPress(MotionEvent event) method.
 * 
 * @author Dylan Stenico
 * 
 */
public class MapOverlay extends Overlay
{
	private Context context;
	private BoundingBoxE6 bb;

	public MapOverlay(Context context, BoundingBoxE6 bb)
	{
		super(context);

		this.context = context;
		this.bb = bb;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		// ---when user lifts his finger---

		if (event.getAction() == event.ACTION_MOVE
				&& event.getPointerCount() == 1)
		{
			BoundingBoxE6 mapBb = mapView.getProjection().getBoundingBox();

			GeoPoint northWest, northEast, southWest, southEast;
			northWest = new GeoPoint(mapBb.getLatNorthE6() + 150,
					mapBb.getLonWestE6() - 150);
			northEast = new GeoPoint(mapBb.getLatNorthE6() + 150,
					mapBb.getLonEastE6() + 150);
			southWest = new GeoPoint(mapBb.getLatSouthE6() - 150,
					mapBb.getLonWestE6() - 150);
			southEast = new GeoPoint(mapBb.getLatSouthE6() - 150,
					mapBb.getLonEastE6() + 150);
			Log.d("scroll", Boolean.toString(bb.contains(northWest) && bb.contains(northEast) && bb.contains(southWest) && bb.contains(southEast)))
;			return !(bb.contains(northWest) && bb.contains(northEast) && bb.contains(southWest) && bb.contains(southEast)) ;
		}
		return false;
	}

	/**
	 * What to do when you make a long press on the map.
	 * 
	 * @param mapView
	 * @param event
	 */
	public void onLongPressGesture(MotionEvent event)
	{
	}

	@Override
	protected void draw(Canvas arg0, MapView arg1, boolean arg2)
	{
		// TODO Auto-generated method stub

	}
}