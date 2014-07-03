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

import smartcampus.model.Report;
import smartcampus.util.Tools;
import android.os.AsyncTask;
import android.util.Log;

public class GetReportsTask extends AsyncTask<String, Void, ArrayList<Report>>
{
	public static final String BIKE = "bikes";
	public static final String STATION = "stations";
	
	
	public static final int NO_ERROR = 0;
	public static final int ERROR_SERVER = 1;
	public static final int ERROR_CLIENT = 2;
	
	private int currentStatus;	
	
	public interface AsyncReportsResponse
	{
	    void processFinish(ArrayList<Report> reports, int status);
	}
	public AsyncReportsResponse delegate = null;

	//data[0] is BIKE or STATION
	//data[1] is the stationID or the bikeID
	@Override
	protected ArrayList<Report> doInBackground(String... data)
	{
		HttpGet httpg = new HttpGet(Tools.SERVICE_URL + data[0] + "/" + Tools.CAP_ROVERETO + data[1] + "/" + Tools.REPORTS_REQUEST);
		Log.d("getReportsTask", httpg.getURI().toString());
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
			JSONArray reportsArrayJSON = container.getJSONArray("data");
			Log.d("getReportsTask", reportsArrayJSON.length()+"");
			for (int i = 0; i < reportsArrayJSON.length(); i++)
			{
				JSONObject reportJSON = reportsArrayJSON.getJSONObject(i);
				 
				Report.Type reportType = Report.Type.stringToType(reportJSON.getString("reportType"));				
				String details = reportJSON.getString("report");
				JSONArray jsonArray = reportJSON.getJSONArray("warnings");
				ArrayList<String> warnings = new ArrayList<String>();
				for (int j=0; j<jsonArray.length(); j++) {
					warnings.add(jsonArray.getString(j));
				}
				Log.d("getReportsTask",warnings.toString());
				String objectType = reportJSON.getString("objectType");
				String objectId = reportJSON.getString("objectId");
				long date = reportJSON.getLong("date");

				Report report = new Report(reportType, details, objectType, objectId, date);
				report.addAllWarnings(warnings);
				reports.add(report);
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
		Log.d("getReportsTask", "finished with "+result.size()+" reports");
		if (delegate!=null)
			delegate.processFinish(result, currentStatus);
	}
	
}
