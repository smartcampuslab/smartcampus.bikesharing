package smartcampus.util;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import smartcampus.model.Station;
import android.os.AsyncTask;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> THIS IS NOT TESTED!! <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//TODO: edit JSON key, edit Station class to introduce all the information passed by server

public class GetStationsTask extends AsyncTask<String, Void, ArrayList<Station>> {

	private static final String STATION_NAME = "name";
	private static final String STATION_STREET = "street";
	private static final String STATION_LATITUDE = "latitude";	
	private static final String STATION_LONGITUDE = "longitude";	
	private static final String AVAILABLE_BIKES = "bikes";
	private static final String MAX_SLOTS = "slots";
	private static final String BROKEN_SLOTS = "brokenslots";
	
	@Override
	protected ArrayList<Station> doInBackground(String... data) {
		HttpClient httpclient = new DefaultHttpClient(); 
		HttpGet httpg = new HttpGet("http://www.yoursite.com/"+data); 
		String responseJSON;
		try { 
			HttpResponse response = httpclient.execute(httpg); 
			responseJSON = EntityUtils.toString(response.getEntity()); 
		} 
		catch (ClientProtocolException e) {
			return null;
		} 
		catch (IOException e) {
			return null;
		} 
		ArrayList<Station> stations = new ArrayList<Station>();
		try {
			JSONArray stationsArrayJSON = new JSONArray(responseJSON);
			for (int i = 0; i < stationsArrayJSON.length(); i++) { 
				 JSONObject stationJSON = stationsArrayJSON.getJSONObject(i); 
				 String name = stationJSON.getString(STATION_NAME);
				 String street = stationJSON.getString(STATION_STREET);
				 Double latitude = stationJSON.getDouble(STATION_LATITUDE);
				 Double longitude = stationJSON.getDouble(STATION_LONGITUDE);
				 int availableBikes = stationJSON.getInt(AVAILABLE_BIKES);
				 int maxSlots = stationJSON.getInt(MAX_SLOTS);
				 int brokenSlots = stationJSON.getInt(BROKEN_SLOTS);
				 Station station = new Station(new GeoPoint(latitude, longitude), name+"-"+street, maxSlots);
				 station.setUsedSlots(availableBikes);
				 stations.add(station);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}			
		return stations;
	}

}
