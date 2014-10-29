package smartcampus.util;

import java.util.ArrayList;
import java.util.Iterator;

import android.R.bool;
import android.content.Context;
import smartcampus.asynctask.GetStationsTask;
import smartcampus.asynctask.GetStationsTask.AsyncStationResponse;
import smartcampus.model.Station;

public class StationsHelper {

	public static ArrayList<Station> sStations;

	public static boolean isNotInitialized() {
		return sStations == null || sStations.size() < 1;
	}

	public static void initialize(Context ctx, AsyncStationResponse resp) {
		sStations = new ArrayList<Station>();
		GetStationsTask gst = new GetStationsTask(ctx.getApplicationContext());
		gst.delegate = resp;
		gst.execute("");
	}

	public static ArrayList<Station> getFavourites() {
		ArrayList<Station> out = new ArrayList<Station>(sStations.size());
		for (Station s : sStations) {
			if (s.getFavourite()) {
				out.add(s);
			}
		}
		return out;
	}

	public static synchronized void updateStation(Station s) {
		Iterator<Station> is = StationsHelper.sStations.iterator();
		Station tmp = null;
		while (is.hasNext()) {
			tmp = is.next();
			if (tmp.equals(s)) {
				tmp.setFavourite(s.getFavourite());
			}
		}
	}

}
