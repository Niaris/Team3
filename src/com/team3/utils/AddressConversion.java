/** 
 * INTRODUCED IN FR2.
 * AddressConversion.java is used to retrieve and pass the Address to MainActivity.java and specifically it is called
 * when the onLocationChanged is called.
 * @author Andreas Stavrou
 * @author Salman Alghmdi
 * @author Rufai Ahmad
 * @author Sijia Liu
 * @version 1.2 - 22 October 2013 | Refactored FINISHED on 00/MONTH/2013
 */

package com.team3.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
/*
 * AddressConversion class, responsible for converting the Longitude and Latitude to readable Address.
 */
public class AddressConversion {
	/*
	 * Contractor method.
	 */
	public AddressConversion() {

	}
	/*
	 * getLocationInfo method, used to convert the Longitude and Latitude to a readable Address.
	 * 
	 * @param double lat, Holds the value of the current latitude coordinate.
	 * @param double lan, Holds the value of the current longitude coordinate.
	 * @return JSONObject returns a JSON object that contains the readable address.
	 */
	public JSONObject getLocationInfo(double lat, double lng) {

		HttpGet httpGet = new HttpGet(
				"http://maps.google.com/maps/api/geocode/json?latlng=" + lat
						+ "," + lng + "&sensor=true");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
