package smartcampus.asynctask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import smartcampus.model.Report;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
//TODO: not tested and fix keys!

public class SendReport extends AsyncTask<Report, Void, String>{
	Context context;
	
	public SendReport(Context context)
	{
		this.context = context;
	}
	
	@Override
	protected String doInBackground(Report... reports) {
		InputStream inputStream = null;
        String result = "";
        String url = "http://192.168.41.154:8080/bikesharing-web/report";
        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
 
            String json = "";
 
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", null);
            jsonObject.accumulate("reportType", reports[0].getType().toString());
            jsonObject.accumulate("report", reports[0].getDetails());
            jsonObject.accumulate("objectType", reports[0].getReportOfType());            
            jsonObject.accumulate("objectId", reports[0].getID());
            jsonObject.accumulate("date", reports[0].getDate());
            jsonObject.accumulate("fieldId", null);
           // jsonObject.accumulate("file", reports[0].getPhotoAsByteArray());
 
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
 
            // 5. set json to StringEntity
            /*
            StringEntity se = new StringEntity(container.toString());            
            BasicNameValuePair param = new BasicNameValuePair("body", container.toString());
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(param);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));*/  
            
            /*ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("body", json));
            postParameters.add(new BasicNameValuePair("name", "value"));*/
            // 6. set httpPost Entity
            //httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("body", new StringBody(json));
            //create a file to write bitmap data
                        
            File f = new File(context.getCacheDir(), "imageReport");
            f.createNewFile();           
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            reports[0].getPhoto().compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            multipartEntity.addPart("file", new FileBody(f));
            Log.d("send", json);

            httpPost.setEntity(multipartEntity);
            
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
	
	@Override
	protected void onPostExecute(String result) {
		Log.d("sendReport", result);
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
