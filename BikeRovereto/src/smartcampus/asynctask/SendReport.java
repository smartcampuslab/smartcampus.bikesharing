package smartcampus.asynctask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.model.Report;
import smartcampus.util.Tools;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import eu.trentorise.smartcampus.bikerovereto.R;
//TODO: not tested and fix keys!

public class SendReport extends AsyncTask<Report, Void, String>{
	Context context;
	int status;

	public static final int NO_ERROR = 0;
	public static final int ERROR_SERVER = 1;
	public static final int ERROR_CLIENT = 2;
	
	public SendReport(Context context)
	{
		this.context = context;
	}
	
	@Override
	protected String doInBackground(Report... reports) {
		InputStream inputStream = null;
        String result = "";
        String url = Tools.SERVICE_URL + Tools.REPORT_REQUEST;
            try
			{
				// 1. create HttpClient
				HttpClient httpclient = new DefaultHttpClient();
 
				// 2. make POST request to the given URL
				HttpPost httpPost = new HttpPost(url);
 
				String json = "";
	            Charset charsEncoding = Charset.forName("UTF-8");
				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("id", null);
				jsonObject.accumulate("reportType", reports[0].getType().toString());
				jsonObject.accumulate("report", reports[0].getDetails());
				JSONArray jsonArray = new JSONArray(reports[0].getWarnings());
				jsonObject.accumulate("warnings", jsonArray);
				jsonObject.accumulate("objectType", reports[0].getReportOfType());
				jsonObject.accumulate("objectId", reports[0].getID());
				jsonObject.accumulate("cityId", "5061"); //Code of the city
				jsonObject.accumulate("date", reports[0].getDate());
				jsonObject.accumulate("fieldId", null);
				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();
 
            
				// 5. set json
				MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				multipartEntity.addPart("body", new StringBody(json, charsEncoding));
				//create a file to write bitmap data
				            
				if (reports[0].getPhoto() != null)
				{
				    File f = new File(context.getCacheDir(), "imageReport");
				    f.createNewFile();           
				    ByteArrayOutputStream bos = new ByteArrayOutputStream();
				    reports[0].getPhoto().compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
				    byte[] bitmapdata = bos.toByteArray();
				    //write the bytes in file
				    FileOutputStream fos = new FileOutputStream(f);
				    fos.write(bitmapdata);
				    multipartEntity.addPart("file", new FileBody(f));
				}
				Log.d("send", json);

				httpPost.setEntity(multipartEntity);
				
				// 8. Execute POST request to the given URL
				HttpResponse httpResponse = httpclient.execute(httpPost);
 
				// 9. receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();
 
				// 10. convert inputstream to string
				if(inputStream != null)
				{
					result = convertInputStreamToString(inputStream);
					JSONObject jsonResult = new JSONObject(result);
					int httpStatus = jsonResult.getInt("httpStatus");
					if (httpStatus == 200)
						status = NO_ERROR;
					else
						status = ERROR_SERVER;
				}                
				else
				{
				    result = "Did not work!";
					status = ERROR_CLIENT;
				}
			}
			catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = ERROR_CLIENT;
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = ERROR_CLIENT;
			}
			catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = ERROR_CLIENT;
			}
			catch (IllegalStateException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = ERROR_CLIENT;
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = ERROR_CLIENT;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = ERROR_CLIENT;
			}
 
        // 11. return result
        return result;
	}	
	
	@Override
	protected void onPostExecute(String result) {
		Log.d("sendReport", result);
		if (status == NO_ERROR)
		{
			Toast.makeText(context, context.getString(R.string.send_report_succes), Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context, context.getString(R.string.send_report_error), Toast.LENGTH_SHORT).show();			
		}
	}
	
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }   

}
