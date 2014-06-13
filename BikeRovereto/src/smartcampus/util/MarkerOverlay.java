package smartcampus.util;

import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import eu.trentorise.smartcampus.osm.android.bonuspack.overlays.ItemizedOverlayWithBubble;
import eu.trentorise.smartcampus.osm.android.views.MapView;
import eu.trentorise.smartcampus.osm.android.views.overlay.OverlayItem;

public class MarkerOverlay<Item extends OverlayItem> extends ItemizedOverlayWithBubble<Item>

{
	
	float x, y;

	public MarkerOverlay(Context applicationContext,
			List<Item> markers, MapView mapView,
			CustomInfoWindow customInfoWindow)
	{
		super(applicationContext, markers, mapView, customInfoWindow);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		boolean toRtn = false;
		if (event.getAction() == event.ACTION_DOWN)
		{
			x = event.getX();
			y = event.getY();
		}
		else if (event.getAction() == event.ACTION_UP)
		{
			if ((Math.abs(event.getX() - x) <= 10)
					&& (Math.abs(event.getY() - y) <= 10))
			{
				if(getBubbledItemId() != -1)
				{
					closeOpenBubble();
				}
				else
				{
					toRtn = super.onTouchEvent(event, mapView);
				}
				
				return toRtn;
			}
		}
		return toRtn;
	}
	
	private void closeOpenBubble()
	{
		hideBubble();
	}
}
