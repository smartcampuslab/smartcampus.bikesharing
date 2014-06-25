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

import smartcampus.activity.MainActivity;
import smartcampus.model.Station;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> THIS IS NOT TESTED!! <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public class GetStationsTask extends AsyncTask<String, Void, ArrayList<Station>>
{
	

	private static final String STATION_NAME = "name";
	private static final String STATION_STREET = "street";
	private static final String STATION_LATITUDE = "latitude";
	private static final String STATION_LONGITUDE = "longitude";
	private static final String AVAILABLE_BIKES = "nBikes";
	private static final String MAX_SLOTS = "maxSlots";
	private static final String BROKEN_SLOTS = "nBrokenBikes";
	private static final String STATION_ID = "id";	
	

	private Context context;

	public GetStationsTask(Context context)
	{
		this.context = context;
	}
	
	public interface AsyncStationResponse {
	    void processFinish(ArrayList<Station> stations);
	}
	public AsyncStationResponse delegate=null;


	@Override
	protected ArrayList<Station> doInBackground(String... data)
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpg = new HttpGet("http://192.168.41.157:8080/bikesharing-web/stations/5061/"+data[0]);
		Log.d("prova", httpg.getURI().toString());
		String responseJSON;
		ArrayList<Station> stations = new ArrayList<Station>();
		try
		{
			HttpResponse response = httpclient.execute(httpg);
			responseJSON = EntityUtils.toString(response.getEntity());
		}
		catch (ClientProtocolException e)
		{
			return stations;
		}
		catch (IOException e)
		{
			return stations;
		}
		try
		{
			SharedPreferences pref = context.getSharedPreferences("favStations", Context.MODE_PRIVATE);
			JSONObject container = new JSONObject(responseJSON);
			
			if(data[0] == "")
			{
				JSONArray stationsArrayJSON = container.getJSONArray("data");
				
				for (int i = 0; i < stationsArrayJSON.length(); i++)
				{
					JSONObject stationJSON = stationsArrayJSON.getJSONObject(i);
					String name = stationJSON.getString(STATION_NAME);
					String street = stationJSON.getString(STATION_STREET);
					Double latitude = stationJSON.getDouble(STATION_LATITUDE);
					Double longitude = stationJSON.getDouble(STATION_LONGITUDE);
					int availableBikes = stationJSON.getInt(AVAILABLE_BIKES);
					int maxSlots = stationJSON.getInt(MAX_SLOTS);
					int brokenSlots = stationJSON.getInt(BROKEN_SLOTS);
					String id = stationJSON.getString(STATION_ID);
					Station station = new Station(new GeoPoint(latitude, longitude), name, street, maxSlots, availableBikes, brokenSlots, id);
					boolean fav = pref.getBoolean(Tools.STATION_PREFIX + id, false);
					station.setFavourite(fav);
					station.setUsedSlots(availableBikes);
					stations.add(station);
					if (fav)
						((MainActivity)context).addFavouriteStation(station);
				}
			}
			else
			{
				JSONObject stationJSON = container.getJSONObject("data");
				String name = stationJSON.getString(STATION_NAME);
				String street = stationJSON.getString(STATION_STREET);
				Double latitude = stationJSON.getDouble(STATION_LATITUDE);
				Double longitude = stationJSON.getDouble(STATION_LONGITUDE);
				int availableBikes = stationJSON.getInt(AVAILABLE_BIKES);
				int maxSlots = stationJSON.getInt(MAX_SLOTS);
				int brokenSlots = stationJSON.getInt(BROKEN_SLOTS);
				String id = stationJSON.getString(STATION_ID);
				Station station = new Station(new GeoPoint(latitude, longitude), name, street, maxSlots, availableBikes, brokenSlots, id);
				boolean fav = pref.getBoolean(Tools.STATION_PREFIX + id, false);
				station.setFavourite(fav);
				station.setUsedSlots(availableBikes);
				stations.add(station);
			}
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return stations;
		}
		return stations;
	}

	@Override
	protected void onPostExecute(ArrayList<Station> result) {
		if (delegate!=null)
			delegate.processFinish(result);
	}
	
}
