package smartcampus.asynctask;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

import smartcampus.model.Report;
import smartcampus.model.Station;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class GetReportsTask extends AsyncTask<String, Void, ArrayList<Report>>
{
	private static final String KEY = "name";
	private static final String KEY2 = "street";

	public static final String BIKE = "bikes";
	public static final String STATION = "stations";
	
	
	public static final int NO_ERROR = 0;
	public static final int ERROR_SERVER = 1;
	public static final int ERROR_CLIENT = 2;
	
	private int currentStatus;	
	
	public interface AsyncStationResponse
	{
	    void processFinish();
	}
	public AsyncStationResponse delegate = null;

	//data[0] is BIKE or STATION
	//data[1] is the stationID or the bikeID
	@Override
	protected ArrayList<Report> doInBackground(String... data)
	{
		HttpGet httpg = new HttpGet("http://192.168.41.154:8080/bikesharing-web/" + data[0] + "/5061/reports/"+data[1]);
		String responseJSON;
		
		ArrayList<Report> reports = new ArrayList<Report>();
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
			return reports;
		}
		catch (IOException e)
		{
			currentStatus = ERROR_CLIENT;
			return reports;
		}
		try
		{
			JSONObject container = new JSONObject(responseJSON);
			int httpStatus = container.getInt("httpStatus");
			if (httpStatus == 200)
				currentStatus = NO_ERROR;
			else
				currentStatus = ERROR_SERVER;
			String errorString = container.getString("errorString");
			JSONArray stationsArrayJSON = container.getJSONArray("data");
			for (int i = 0; i < stationsArrayJSON.length(); i++)
			{
				JSONObject stationJSON = stationsArrayJSON.getJSONObject(i);
				// TODO: creation of the object Report
			}			
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return reports;
		}
		return reports;
	}

	@Override
	protected void onPostExecute(ArrayList<Report> result) {
		if (delegate!=null)
			delegate.processFinish();
	}
	
}