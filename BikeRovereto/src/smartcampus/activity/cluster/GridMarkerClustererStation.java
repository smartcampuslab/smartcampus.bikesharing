package smartcampus.activity.cluster;

import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import smartcampus.util.StationMarker;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import eu.trentorise.smartcampus.bikerovereto.R;

public class GridMarkerClustererStation extends GridMarkerClusterer
{
	Context ctx;
	public GridMarkerClustererStation(Context ctx)
	{
		super(ctx);
		this.ctx = ctx;
	}

	/**
	 * Build the marker for a cluster. Uses the cluster icon, and displays
	 * inside the number of markers it contains. <br/>
	 * In the standard Google coordinate system for Marker icons: <br/>
	 * - The cluster icon is anchored at mAnchorU, mAnchorV. <br/>
	 * - The text showing the number of markers is anchored at mTextAnchorU,
	 * mTextAnchorV. This text is centered horizontally and vertically.
	 */
	@Override
	public Marker buildClusterMarker(StaticCluster cluster, MapView mapView)
	{
		Marker m = new Marker(mapView);
		m.setPosition(cluster.getPosition());
		m.setInfoWindow(null);
		m.setAnchor(mAnchorU, mAnchorV);

		float totalPercentage = 0;
		for (int i = 0; i < cluster.getSize(); i++)
		{
			totalPercentage += ((StationMarker) cluster.getItem(i)).getStation().getBikesPresentPercentage();
		}
		totalPercentage /= cluster.getSize();
		Resources res = ctx.getResources();
		Drawable drawableImage = null;
		switch ((int) (totalPercentage * 10))
		{
		case 0:
			drawableImage = res.getDrawable(R.drawable.marker_0);
			break;
		case 1:
			drawableImage = res.getDrawable(R.drawable.marker_10);
			break;
		case 2:
			drawableImage = res.getDrawable(R.drawable.marker_20);
			break;
		case 3:
			drawableImage = res.getDrawable(R.drawable.marker_30);
			break;
		case 4:
			drawableImage = res.getDrawable(R.drawable.marker_40);
			break;
		case 5:
			drawableImage = res.getDrawable(R.drawable.marker_50);
			break;
		case 6:
			drawableImage = res.getDrawable(R.drawable.marker_60);
			break;
		case 7:
			drawableImage = res.getDrawable(R.drawable.marker_70);
			break;
		case 8:
			drawableImage = res.getDrawable(R.drawable.marker_80);
			break;
		case 9:
			drawableImage = res.getDrawable(R.drawable.marker_90);
			break;
		case 10:
			drawableImage = res.getDrawable(R.drawable.marker_100);
			break;
		default:
			break;
		}
		
		mClusterIcon = ((BitmapDrawable)drawableImage).getBitmap();
		Bitmap finalIcon = Bitmap.createBitmap(mClusterIcon.getWidth(), mClusterIcon.getHeight(), mClusterIcon.getConfig());

		Canvas iconCanvas = new Canvas(finalIcon);
		iconCanvas.drawBitmap(mClusterIcon, 0, 0, null);
		String text = "" + cluster.getSize();
		int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
		iconCanvas.drawText(text, mTextAnchorU * mClusterIcon.getWidth(), mTextAnchorV * mClusterIcon.getHeight() - textHeight / 2, mTextPaint);
		m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));
		return m;
	}

}
