package com.tvs.module;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.tvs.activity.TVSEService;


/**
 * Class to handle all network Connection
 * 
 * @author XRG
 * @version 1.0
 */
public class NetworkHelper {

	private static final int TIMEOUTCONNECTION = 2000;
	private static final int TIMEOUTSOCKET = 500000;

	static NetworkHelper instance = null;
	Context context = null;

	/**
	 * Constructor method
	 */
	public NetworkHelper(Context context) {
		this.context = context;
	}

	/**
	 * Get Network Helper instance
	 * 
	 * @return
	 */
	public static NetworkHelper getInstance(Context context) {
		if (null == instance) {
			instance = new NetworkHelper(context);
		}
		return instance;
	}
	
	
	public boolean uploadImage(String resources)throws CustomException{
		 String fileNameWithPath = null;
		 boolean success = false;		 
		 File sdImageMainDirPath = new File(Environment.getExternalStorageDirectory(), resources);
		 
		 if (sdImageMainDirPath.isDirectory()) {// make dir if not present
			 fileNameWithPath = sdImageMainDirPath.toString() + "/" + TVSEService.filesign + ".png";
		 }
		 Bitmap bitmapOrg = BitmapFactory.decodeFile(fileNameWithPath);
		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
		 bitmapOrg.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
         byte [] byte_arr = stream.toByteArray();
         String image_str = Base64.encodeBytes(byte_arr);
         ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

         nameValuePairs.add(new BasicNameValuePair("filename",image_str));

         try{
             HttpClient httpclient = new DefaultHttpClient();
      
             HttpPost httppost = new HttpPost(Constants.SIGNATURE_UPLOAD+"filename="+TVSEService.filesign+".png");//use the post url,but i have used get url
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             HttpResponse response = httpclient.execute(httppost);
             String the_string_response = convertResponseToString(response);
             if(null != the_string_response){
            	 success = true;
             }
         }catch(Exception e){
        	 throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
         }
         
         return success;
	}
	
	
	
	
/*	public void uploadImage(String resources) throws CustomException{
		File sdImageMainDirPath = new File(Environment.getExternalStorageDirectory(), resources);
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String boundary =  "*****";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		int maxBufferSize = 1*1024*1024;
		byte[] buffer;
		
		if(!isOnline()){
			throw new CustomException(Constants.ERR_CONNECTION_UNAVAIL);
		}
		
		if (sdImageMainDirPath.isDirectory()) {// make dir if not present
			String testImg = sdImageMainDirPath.toString() + "/" + TVSEService.filesign + ".png";
			try{
			FileInputStream fileInputStream = new FileInputStream(testImg);
			URL url = new URL(Constants.SIGNATURE_LOCATION+TVSEService.filesign+".png");
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			conn.connect();
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + TVSEService.filesign + ".png\"" + lineEnd);
			dos.writeBytes(lineEnd);
			int bytesAvailable = fileInputStream.available();
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0)
			{
			 dos.write(buffer, 0, bufferSize);
			 bytesAvailable = fileInputStream.available();
			 bufferSize = Math.min(bytesAvailable, maxBufferSize);
			 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			fileInputStream.close();
			dos.flush();
			dos.close();
			}catch (Exception e) {
				throw new CustomException(Constants.ERR_CONNECTION);
			}
		} 
	}
	*/
	
	 public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
		 
         String res = "";
         StringBuffer buffer = new StringBuffer();
         InputStream inputStream = response.getEntity().getContent();
         int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
       
         if (contentLength < 0){
         }
         else{
                byte[] data = new byte[contentLength];
                int len = 0;
                try
                {
                    while (-1 != (len = inputStream.read(data)) )
                    {
                        buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    inputStream.close(); // closing the stream…..
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                res = buffer.toString();     // converting stringbuffer to string…..

         }
         return res;
    }
	/**
	 * Method to do get
	 * 
	 * @param url
	 * @return String Response String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doGet(String url) throws CustomException{
		
		String result = null;
		HttpResponse response = null;
		
		Log.d("Url Request", url);
		
		url = url.replace("\n", "%20");
		url = url.replace(" ", "%20");
		
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 7000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		HttpGet httpget = new HttpGet(url);

		try {
			if (isOnline()) {
				response = httpclient.execute(httpget);
				
				// Examine the response status
	            //Log.i("Praeda",response.getStatusLine().toString());
				
	        	/* Checking response */
				if (response != null ) {
					int statusCode = response.getStatusLine().getStatusCode();
					if(statusCode == 200){
						InputStream inStream = response.getEntity().getContent();
						long size = response.getEntity().getContentLength();
						result = convertStreamToString(inStream,size);
						inStream = null;
					}else{
						throw new CustomException(Constants.ERR_CONNECTION_SERVERDATA,Constants.ERR_SEND);	
					}

				}
			}else {
				throw new CustomException(Constants.ERR_CONNECTION_UNAVAIL,Constants.ERR_SEND);
			}

		}catch(SocketTimeoutException timeOut) {
			throw new CustomException(Constants.ERR_TIME_OUT,Constants.ERR_SEND);
		}catch(IOException ioException) {
			throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
		}finally {
			httpclient.getConnectionManager().shutdown();
		}

		return result;
	}

	/**
	 * Method to do get
	 * 
	 * @param url
	 * @return String Response String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doPostImage(String url,byte[] bytes) throws CustomException{
		String result = null;
		HttpResponse response = null;
		Log.d("Url Request", url);
		url = url.replace("\n", "%20");
		url = url.replace(" ", "%20");
        String image_str = Base64.encodeBytes(bytes);
        ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("FileName",image_str));
        HttpClient httpclient = new DefaultHttpClient();
				
		try {
			if (isOnline()) {
				HttpPost httppost = new HttpPost(url);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);

				if (response != null ) {
					int statusCode = response.getStatusLine().getStatusCode();
					if(statusCode == 200){
						InputStream inStream = response.getEntity().getContent();
						long size = response.getEntity().getContentLength();
						result = convertStreamToString(inStream,size);
						inStream = null;
					}else{
						throw new CustomException(Constants.ERR_CONNECTION_SERVERDATA,Constants.ERR_SEND);	
					}
				}
			}else {
				throw new CustomException(Constants.ERR_CONNECTION_UNAVAIL,Constants.ERR_SEND);
			}

		}catch(SocketTimeoutException timeOut) {
			throw new CustomException(Constants.ERR_TIME_OUT,Constants.ERR_SEND);
		}catch(IOException ioException) {
			throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
		}finally {
			httpclient.getConnectionManager().shutdown();
		}

		return result;

	}



	/**
	 * Performs an HTTP Post request to the specified url with the specified
	 * parameters.
	 * 
	 * @param url
	 *            The web address to post the request to
	 * @param postParameters
	 *            The parameters to send via the request
	 * @return The result of the request
	 * @throws Exception
	 */
	public String executeHttpPost(String url, JSONObject object) throws CustomException {

		String result = null;
		HttpResponse response = null;
		url = url.replace(" ", "%20");
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUTCONNECTION);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUTSOCKET);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		try {

			HttpPost httppost = new HttpPost(url);

			StringEntity se = new StringEntity(object.toString());
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/xml"));
			httppost.setEntity(se);

			httppost.setHeader("Content-Type", "application/xml");
			httppost.setHeader("Accept", "application/xml");

			if (isOnline()) {
				response = httpclient.execute(httppost);
				/* Checking response */
				if (response != null) {
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {

						InputStream inStream = response.getEntity().getContent();
						long size =response.getEntity().getContentLength();

						result = convertStreamToString(inStream,size);
						inStream = null;

					} else {

						throw new CustomException(Constants.ERR_CONNECTION_SERVERDATA,Constants.ERR_SEND);
					}
				} else {
					throw new CustomException(Constants.ERR_CONNECTION_UNAVAIL,Constants.ERR_SEND);
				}
			}
		} catch (SocketTimeoutException timeOut) {
			throw new CustomException(Constants.ERR_TIME_OUT,Constants.ERR_SEND);
		} catch (ClientProtocolException e) {
			throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
		} catch (IOException ioException) {
			throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return result;
	}

	/**
	 * 
	 * @param is
	 * @return
	 */
	private String convertStreamToString(InputStream is, long size) {

		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try{
			reader = new BufferedReader(new InputStreamReader(is));
		}catch (Exception e) {
			// TODO: handle exception
		}
		

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * Method to identify whether connection available
	 * 
	 * @param context
	 * @return
	 */
	private boolean isOnline() {
		boolean valid = false;
		try {

			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null || !cm.getBackgroundDataSetting()) {
				valid = false;
			}
			valid = cm.getActiveNetworkInfo().isConnected();
		} catch (Exception e) {
			return false;
		}
		return valid;
	}

	/**
	 * Method to identify whether connection available
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOnlines(Context context)throws CustomException {
		boolean valid = false;
		try {

			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null || !cm.getBackgroundDataSetting()) {
				valid = false;
			}
			valid = cm.getActiveNetworkInfo().isConnected();
		} catch (Exception e) {
			throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
		}
		return valid;
		//return false;
	}
	/**
	 * 
	 * @param context
	 * @return
	 * @throws CustomException
	 */
	public static String isOnline(Context context) throws CustomException {

		String valid = "";
		try {
			ConnectivityManager mConnectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			// Skip if no connection, or background data disabled
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			if (info == null || !mConnectivity.getBackgroundDataSetting()) {
				valid = "no connection, or background data disabled";
				return valid;
			}
			
			// Only update if WiFi or 3G is connected and not roaming
			int netType = info.getType();
			// int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				if (info.isConnected()) {
					valid = info.getTypeName();
				}
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				if (info.isConnected()) {
					valid = info.getTypeName() + info.getSubtypeName();
				}
			} else {
				valid = "Not Connected";
			}
			
		} catch (Exception e) {
			throw new CustomException(Constants.ERR_CONNECTION,Constants.ERR_SEND);
		}
		return valid;
	}
}
