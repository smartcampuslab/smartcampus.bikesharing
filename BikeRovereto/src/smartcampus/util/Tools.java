package smartcampus.util;

import smartcampus.model.Station;
import android.content.Context;
import android.util.TypedValue;

public class Tools
{

	public static final long LOCATION_REFRESH_TIME = 60000;
	public static final float LOCATION_REFRESH_DISTANCE = 100;

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

}
