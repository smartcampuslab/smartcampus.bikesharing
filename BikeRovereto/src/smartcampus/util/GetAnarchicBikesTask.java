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

import smartcampus.model.Bike;
import android.os.AsyncTask;
import android.util.Log;

public class GetAnarchicBikesTask extends AsyncTask<Void, Void, ArrayList<Bike>>
{

	private static final String BIKE_ID = "id";
	private static final String BIKE_LATITUDE = "latitude";
	private static final String BIKE_LONGITUDE = "longitude";

	public interface AsyncBikesResponse
	{
		void processFinish(ArrayList<Bike> result);
	}

	public AsyncBikesResponse delegate = null;

	@Override
	protected ArrayList<Bike> doInBackground(Void... arg0)
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpg = new HttpGet("http://192.168.41.157:8080/bikesharing-web/stations/5061/");
		Log.d("prova", httpg.getURI().toString());
		String responseJSON;
		ArrayList<Bike> bikes = new ArrayList<Bike>();
		try
		{
			HttpResponse response = httpclient.execute(httpg);
			responseJSON = EntityUtils.toString(response.getEntity());
		}
		catch (ClientProtocolException e)
		{
			return bikes;
		}
		catch (IOException e)
		{
			return bikes;
		}
		try
		{
			JSONObject container = new JSONObject(responseJSON);

			JSONArray bikesArrayJSON = container.getJSONArray("data");

			for (int i = 0; i < bikesArrayJSON.length(); i++)
			{
				JSONObject bikesJSON = bikesArrayJSON.getJSONObject(i);
				String id = bikesJSON.getString(BIKE_ID);
				Double latitude = bikesJSON.getDouble(BIKE_LATITUDE);
				Double longitude = bikesJSON.getDouble(BIKE_LONGITUDE);
				Bike bike = new Bike(new GeoPoint(latitude, longitude), id);
		
				bikes.add(bike);
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return bikes;
		}
		return bikes;
	}

	@Override
	protected void onPostExecute(ArrayList<Bike> result)
	{
		if (delegate != null)
			delegate.processFinish(result);
	}
}
