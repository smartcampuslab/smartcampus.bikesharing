package smartcampus.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import smartcampus.model.Report;
import android.os.AsyncTask;
import android.util.Log;
//TODO: not tested and fix keys!
public class SendReport extends AsyncTask<Report, Void, String>{

	@Override
	protected String doInBackground(Report... reports) {
		InputStream inputStream = null;
        String result = "";
        String url = "192.168.41.154:8080/bikesharing-web/reports";
        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
 
            String json = "";
 
            // 3. build jsonObject
            JSONObject container = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("type", reports[0].getType().toString());
            jsonObject.accumulate("details", reports[0].getDetails());
            jsonObject.accumulate("objectType", reports[0].getReportOfType());            
            jsonObject.accumulate("objectID", reports[0].getID());
            jsonObject.accumulate("date", reports[0].getDate());
 
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            container.accumulate("data", json);
 
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(container.toString());
 
            // 6. set httpPost Entity
            httpPost.setEntity(se);
 
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
 
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        // 11. return result
        return result;
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
