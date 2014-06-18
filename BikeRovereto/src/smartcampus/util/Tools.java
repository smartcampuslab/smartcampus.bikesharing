package smartcampus.util;

import org.osmdroid.util.GeoPoint;

import smartcampus.model.Station;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class Tools
{

	public static final long LOCATION_REFRESH_TIME = 60000;
	public static final float LOCATION_REFRESH_DISTANCE = 100;
	public static final String ITEM_SELECTED = "selected";

	public static int convertDpToPixel(Context context, int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	public static String formatDistance(int distance)
	{
		if (distance == Station.DISTANCE_NOT_VALID)
			return "...";
		else if (distance < 1000)
			return distance + " m";
		else
			return Math.round(distance / 100) / 10.0 + " KM";
	}
	
	public static String getPathString(GeoPoint start, GeoPoint end)
	{
		String toRtn = "http://maps.google.com/maps?";

		if(start != null)
		{
			toRtn += "saddr=" + start.getLatitudeE6() / 1E6 + "," + start.getLongitudeE6() / 1E6 ;  
			toRtn += "&";
		}
		if(end != null)
		{
			toRtn += "daddr=" + end.getLatitudeE6() / 1E6 + "," + end.getLongitudeE6() / 1E6 ;  
		}
		toRtn += "&dirflg=w";
		return toRtn;
	}	
	
	public static void setNavDrawerItemNormal(ListView mDrawerListView, Resources resources)
	{
	    for (int i=0; i< mDrawerListView.getChildCount(); i++)
	    {
	        View v = mDrawerListView.getChildAt(i);
	        v.setBackgroundColor(resources.getColor(R.color.nav_backcolor_normal));
	    }
	}
	
	public static void setNavDrawerItemSelected(ListView mDrawerListView, int position, Resources resources)
	{	    
        View v = mDrawerListView.getChildAt(position);
        v.setBackgroundColor(resources.getColor(R.color.nav_backcolor_selected));
	}

}
