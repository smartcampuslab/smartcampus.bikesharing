package smartcampus.util;

import android.content.Context;
import android.util.TypedValue;

public class Tools
{

	public static int convertDpToPixel(Context context, int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

}
