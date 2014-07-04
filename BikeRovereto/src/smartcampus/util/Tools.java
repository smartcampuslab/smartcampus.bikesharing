package smartcampus.util;

import org.osmdroid.util.GeoPoint;

import smartcampus.model.Station;
import android.content.Context;
import android.preference.Preference;
import android.util.TypedValue;

public class Tools
{

	public static final long LOCATION_REFRESH_TIME = 60000;
	public static final float LOCATION_REFRESH_DISTANCE = 100;
	public static final String STATION_PREFIX = "sta";
	
	public static final String SERVICE_URL = "http://192.168.41.154:8080/bikesharing-web/";
	public static final String STATIONS_REQUEST = "stations/";
	public static final String BIKES_REQUEST = "bikes/";
	public static final String REPORTS_REQUEST = "reports/";
	public static final String REPORT_REQUEST = "report/";
	public static String CITY_CODE;

	
	public static int convertDpToPixel(Context context, int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
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

		if (start != null)
		{
			toRtn += "saddr=" + start.getLatitudeE6() / 1E6 + "," + start.getLongitudeE6() / 1E6;
			toRtn += "&";
		}
		if (end != null)
		{
			toRtn += "daddr=" + end.getLatitudeE6() / 1E6 + "," + end.getLongitudeE6() / 1E6;
		}
		toRtn += "&dirflg=w";
		return toRtn;
	}

	public static boolean isRuntimeAfterHoneycomb()
	{
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	}
}
