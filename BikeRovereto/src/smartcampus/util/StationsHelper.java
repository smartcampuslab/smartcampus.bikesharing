package smartcampus.util;

import java.util.ArrayList;

import android.R.bool;
import android.content.Context;
import smartcampus.asynctask.GetStationsTask;
import smartcampus.asynctask.GetStationsTask.AsyncStationResponse;
import smartcampus.model.Station;

public class StationsHelper {

	public static ArrayList<Station> sStations;
	public static ArrayList<Station> sFavouriteStations;
	
	public static boolean isNotInitialized(){
		return sStations==null || sStations.size()<1 || sFavouriteStations==null;
	}
	public static void initialize(Context ctx,AsyncStationResponse resp){
		sStations = new ArrayList<Station>();
		sFavouriteStations = new ArrayList<Station>();
		GetStationsTask gst = new GetStationsTask(ctx.getApplicationContext());
		gst.delegate = resp;
		gst.execute("");
	}


}
