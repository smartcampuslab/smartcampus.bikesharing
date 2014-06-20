package smartcampus.util;

import java.util.List;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.ItemizedOverlayWithBubble;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean onSingleTapUpHelper(int index, Item item, MapView mapView)
	{
		Log.d("debug", "tap");
		return super.onSingleTapUpHelper(index, item, mapView);
	}

	private void closeOpenBubble()
	{
		hideBubble();
	}

	
}
