package smartcampus.asynctask;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import smartcampus.model.Bike;
import smartcampus.model.Station;
import android.os.AsyncTask;
import android.util.Log;

public class GetAnarchicBikesTask extends AsyncTask<Void, Void, ArrayList<Bike>>
{

	private static final String BIKE_ID = "id";
	private static final String BIKE_LATITUDE = "latitude";
	private static final String BIKE_LONGITUDE = "longitude";

	public static final int NO_ERROR = 0;
	public static final int ERROR_SERVER = 1;
	public static final int ERROR_CLIENT = 2;
	
	private int currentStatus;
	
	public interface AsyncBikesResponse
	{
		void processFinish(ArrayList<Bike> result, int status);
	}

	public AsyncBikesResponse delegate = null;

	@Override
	protected ArrayList<Bike> doInBackground(Void... data)
	{
		HttpGet httpg = new HttpGet("http://192.168.41.154:8080/bikesharing-web/bikes/5061/");
		Log.d("prova", httpg.getURI().toString());
		String responseJSON;
		ArrayList<Bike> bikes = new ArrayList<Bike>();
		try
		{
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			
			HttpResponse response = httpClient.execute(httpg);
			responseJSON = EntityUtils.toString(response.getEntity());
		}
		catch (ClientProtocolException e)
		{
			currentStatus = ERROR_CLIENT;
			return bikes;
		}
		catch (IOException e)
		{
			currentStatus = ERROR_CLIENT;
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
			delegate.processFinish(result, currentStatus);
	}
}
