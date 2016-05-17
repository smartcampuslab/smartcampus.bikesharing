package smartcampus.activity.cluster;

import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import eu.trentorise.smartcampus.bikesharing.R;

public class GridMarkerClustererBikes extends GridMarkerClusterer
{
	Context ctx;

	public GridMarkerClustererBikes(Context ctx)
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

		Resources res = ctx.getResources();
		Drawable drawableImage = res.getDrawable(R.drawable.marker_bike);

		mClusterIcon = ((BitmapDrawable) drawableImage).getBitmap();
		Bitmap finalIcon = Bitmap.createBitmap(mClusterIcon.getWidth(), mClusterIcon.getHeight(), mClusterIcon.getConfig());

		Canvas iconCanvas = new Canvas(finalIcon);
		iconCanvas.drawBitmap(mClusterIcon, 0, 0, null);
		mTextPaint.setColor(Color.rgb(255, 192, 64));
		String text = "" + cluster.getSize();
		int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
		iconCanvas.drawText(text, mTextAnchorU * mClusterIcon.getWidth(), mTextAnchorV * mClusterIcon.getHeight() - textHeight / 2, mTextPaint);
		m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));
		return m;
	}

}
