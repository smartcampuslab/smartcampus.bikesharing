package smartcampus.activity.gesture;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IOverlayMenuProvider;

import smartcampus.activity.MainActivity.OnPositionAquiredListener;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class RotationGestureOverlay extends SafeDrawOverlay implements RotationGestureDetector.RotationListener, IOverlayMenuProvider
{
	private final static boolean SHOW_ROTATE_MENU_ITEMS = false;

	private final static int MENU_ENABLED = getSafeMenuId();
	private final static int MENU_ROTATE_CCW = getSafeMenuId();
	private final static int MENU_ROTATE_CW = getSafeMenuId();

	private final RotationGestureDetector mRotationDetector;
	private MapView mMapView;
	private boolean mOptionsMenuEnabled = true;

	private final static float maxDegreesChange = 5;
	private OnRotateListener mCallback;
	
	public RotationGestureOverlay(Context context, MapView mapView)
	{
		super(context);
		mMapView = mapView;
		mRotationDetector = new RotationGestureDetector(this);
	}

	@Override
	protected void drawSafe(ISafeCanvas canvas, MapView mapView, boolean shadow)
	{
		// No drawing necessary
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		if (this.isEnabled())
		{
			mRotationDetector.onTouch(event);
		}
		return super.onTouchEvent(event, mapView);
	}

	@Override
	public void onRotate(float deltaAngle)
	{
		// divide deltaAngle by 1.2F to remove rotationLag
		deltaAngle /= 1.5F;
		deltaAngle =  Math.abs(deltaAngle) > maxDegreesChange ?  deltaAngle > 0 ? maxDegreesChange : - maxDegreesChange : deltaAngle;
		
		Log.d("Debug", Float.toString(deltaAngle));
		mMapView.setMapOrientation(mMapView.getMapOrientation() + deltaAngle);
		//mCallback.onRotateListener();
	}

	@Override
	public boolean isOptionsMenuEnabled()
	{
		return mOptionsMenuEnabled;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu pMenu, int pMenuIdOffset, MapView pMapView)
	{
		pMenu.add(0, MENU_ENABLED + pMenuIdOffset, Menu.NONE, "Enable rotation").setIcon(android.R.drawable.ic_menu_info_details);
		if (SHOW_ROTATE_MENU_ITEMS)
		{
			pMenu.add(0, MENU_ROTATE_CCW + pMenuIdOffset, Menu.NONE, "Rotate maps counter clockwise").setIcon(android.R.drawable.ic_menu_rotate);
			pMenu.add(0, MENU_ROTATE_CW + pMenuIdOffset, Menu.NONE, "Rotate maps clockwise").setIcon(android.R.drawable.ic_menu_rotate);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem pItem, int pMenuIdOffset, MapView pMapView)
	{
		if (pItem.getItemId() == MENU_ENABLED + pMenuIdOffset)
		{
			if (this.isEnabled())
			{
				mMapView.setMapOrientation(0);
				this.setEnabled(false);
			}
			else
			{
				this.setEnabled(true);
				return true;
			}
		}
		else if (pItem.getItemId() == MENU_ROTATE_CCW + pMenuIdOffset)
		{
			mMapView.setMapOrientation(mMapView.getMapOrientation() - 10);
		}
		else if (pItem.getItemId() == MENU_ROTATE_CW + pMenuIdOffset)
		{
			mMapView.setMapOrientation(mMapView.getMapOrientation() + 10);
		}

		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu pMenu, final int pMenuIdOffset, final MapView pMapView)
	{
		pMenu.findItem(MENU_ENABLED + pMenuIdOffset).setTitle(this.isEnabled() ? "Disable rotation" : "Enable rotation");
		return false;
	}

	@Override
	public void setOptionsMenuEnabled(boolean enabled)
	{
		mOptionsMenuEnabled = enabled;
	}
	
	public interface OnRotateListener
	{
		public void onRotateListener();
	}

	public void setOnRotateListener(
			OnRotateListener onRotateListener)
	{
		this.mCallback = onRotateListener;
	}
}