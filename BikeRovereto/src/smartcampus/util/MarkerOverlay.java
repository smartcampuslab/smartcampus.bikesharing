package smartcampus.util;

import java.util.List;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.ItemizedOverlayWithBubble;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.view.MotionEvent;

public class MarkerOverlay<Item extends OverlayItem> extends
		ItemizedOverlayWithBubble<Item>

{

	float x, y;

	public MarkerOverlay(Context applicationContext, List<Item> markers,
			MapView mapView, InfoWindow infoWindow)
	{
		super(applicationContext, markers, mapView, infoWindow);
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
				if (getBubbledItemId() != -1)
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
