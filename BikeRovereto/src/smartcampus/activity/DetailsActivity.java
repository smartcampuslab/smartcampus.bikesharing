package smartcampus.activity;

import org.osmdroid.ResourceProxy.bitmap;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import smartcampus.model.Station;
import smartcampus.util.StationMarker;
import smartcampus.util.StationsHelper;
import smartcampus.util.Tools;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class DetailsActivity extends ActionBarActivity {

	public static final String EXTRA_STATION = "station";
	public static final String EXTRA_POSITION = "position";

	private Station mStation;
	private GeoPoint mPosition;

	private TextView mNameTV;
	private TextView mStreetTV;
	private TextView mDistanceTV;
	private TextView mAvailTV;
	private TextView mNAvailTV;

	private MapView mMap;
	private ImageView mImageMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(getString(R.string.to_details));

		getData();
		instantiateViews();
		fillViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.station_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (mStation != null) {
			if (mStation.getFavourite()
					|| StationsHelper.sFavouriteStations.contains(mStation)) {
				menu.getItem(0).setIcon(
						getResources().getDrawable(
								R.drawable.ic_action_favourite));
			} else {
				menu.getItem(0).setIcon(
						getResources().getDrawable(
								R.drawable.ic_action_non_favourite));
			}
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			return true;
		} else if (item.getItemId() == R.id.action_favourites) {
			toggleFavourite();
		} else if (item.getItemId() == R.id.action_add_report) {
			sendReport();
		} else if (item.getItemId() == R.id.action_navigate) {
			navigate();
		}
		return super.onOptionsItemSelected(item);
	}

	private void instantiateViews() {
		mNameTV = (TextView) findViewById(R.id.details_name);
		mStreetTV = (TextView) findViewById(R.id.details_street);
		mDistanceTV = (TextView) findViewById(R.id.details_km);
		mAvailTV = (TextView) findViewById(R.id.available_bikes);
		mNAvailTV = (TextView) findViewById(R.id.available_slots);
		mMap = (MapView) findViewById(R.id.map_view);
		mImageMap = (ImageView) findViewById(R.id.image_map);
	}

	private void getData() {
		Intent caller = getIntent();
		mStation = caller.getExtras().getParcelable(EXTRA_STATION);
		double[] l = caller.getDoubleArrayExtra(EXTRA_POSITION);
		if (l != null)
			mPosition = new GeoPoint(l[0], l[1]);
		invalidateOptionsMenu();
	}

	private void fillViews() {
		if (mStation == null)
			return;
		mNameTV.setText(mStation.getName());
		mStreetTV.setText(mStation.getStreet());
		mAvailTV.setText(mStation.getNBikesPresent() + "");
		mNAvailTV.setText(mStation.getNSlotsEmpty() + "");
		if (mStation.getDistance() > 0) {
			mDistanceTV.setText(String.format(
					getString(R.string.distancedetails),
					Tools.formatDistance(mStation.getDistance())));
		}

		mMap.getController().setZoom(18);
		mMap.setDrawingCacheEnabled(true);
		mMap.getOverlays().add(createMarker());
		mMap.setClickable(false);
		mMap.setOnTouchListener(null);
		mMap.postDelayed(new Runnable() {

			@Override
			public void run() {
				mMap.getController().animateTo(mStation.getPosition());
				mImageMap.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mImageMap.setImageBitmap(mMap.getDrawingCache().copy(Bitmap.Config.RGB_565, false));
						mMap.setVisibility(View.GONE);
						mImageMap.setVisibility(View.VISIBLE);
					}
				}, 1500);
									
				
			}
		}, 500);
		mImageMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(DetailsActivity.this, MainActivity.class);
				i.putExtra(EXTRA_STATION, mStation); 
				startActivity(i);
				DetailsActivity.this.finish();
				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			}
		});

	}

	private Overlay createMarker() {
		StationMarker marker = new StationMarker(mMap, mStation);
		Drawable markerImage = null;
		if (mStation.getNBikesPresent() == 0 && mStation.getNSlotsEmpty() == 0) {
			markerImage = getResources().getDrawable(R.drawable.marker_grey);
		} else {
			markerImage = selectImage(getResources(), mStation);
		}
		marker.setPosition(mStation.getPosition());
		marker.setIcon(markerImage);
		marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0, MapView arg1) {
				arg1.getController().zoomIn();
				return true;
			}
		});
		return marker;
	}

	private Drawable selectImage(Resources res, Station s) {
		Drawable markerImage;
		switch ((int) Math.round(s.getBikesPresentPercentage() * 10)) {
		case 0:
			markerImage = res.getDrawable(R.drawable.marker_0);
			break;
		case 1:
			markerImage = res.getDrawable(R.drawable.marker_10);
			break;
		case 2:
			markerImage = res.getDrawable(R.drawable.marker_20);
			break;
		case 3:
			markerImage = res.getDrawable(R.drawable.marker_30);
			break;
		case 4:
			markerImage = res.getDrawable(R.drawable.marker_40);
			break;
		case 5:
			markerImage = res.getDrawable(R.drawable.marker_50);
			break;
		case 6:
			markerImage = res.getDrawable(R.drawable.marker_60);
			break;
		case 7:
			markerImage = res.getDrawable(R.drawable.marker_70);
			break;
		case 8:
			markerImage = res.getDrawable(R.drawable.marker_80);
			break;
		case 9:
			markerImage = res.getDrawable(R.drawable.marker_90);
			break;
		case 10:
			markerImage = res.getDrawable(R.drawable.marker_100);
			break;
		default:
			markerImage = res.getDrawable(R.drawable.marker_grey);
			break;
		}
		return markerImage;
	}

	private void sendReport() {
		// Intent i = new Intent(Intent.ACTION_SEND);
		// i.setType("message/rfc822");
		// // TODO mettere le cose giuste.
		// i.putExtra(Intent.EXTRA_EMAIL, new String[] { "asdasd@gmail.com" });
		// i.putExtra(Intent.EXTRA_SUBJECT, "bike sharing report");
		// i.putExtra(Intent.EXTRA_TEXT, "Asda\n asd\n");
		// startActivity(Intent.createChooser(i,
		// getString(R.string.action_add_report)));

		Intent i = new Intent(this, MailActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}

	private void navigate() {

		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Tools
				.getPathString(mPosition, mStation.getPosition())));
		startActivity(i);
	}

	private void toggleFavourite() {
		mStation.setFavourite(!mStation.getFavourite());
		SharedPreferences.Editor editor = getSharedPreferences("favStations",
				Context.MODE_PRIVATE).edit();
		editor.putBoolean(Tools.STATION_PREFIX + mStation.getId(),
				mStation.getFavourite());
		editor.apply();

		if (mStation.getFavourite()) {
			StationsHelper.sStations.add(mStation);
		} else {
			StationsHelper.sFavouriteStations.remove(mStation);
		}
		invalidateOptionsMenu();
	}
}
